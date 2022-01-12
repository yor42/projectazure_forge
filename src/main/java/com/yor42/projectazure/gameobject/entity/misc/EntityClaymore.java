package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.minecraft.util.HandSide.RIGHT;

public class EntityClaymore extends LivingEntity implements IAnimatable {

    public static final int life = 30;
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityClaymore.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    protected final AnimationFactory factory = new AnimationFactory(this);

    public EntityClaymore(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
        this.ignoreFrustumCheck = true;
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Nullable
    public UUID getOwnerId() {
        return this.dataManager.get(OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.world.getPlayerByUuid(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.getEntityWorld().isRemote()){

            if(this.ticksExisted == 5){
                this.playSound(registerSounds.CLAYMORE_IMPACT, 1.0F, 0.8F+(0.4F*this.getRNG().nextFloat()));
                List<LivingEntity> entitylist = this.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(2));
                for(LivingEntity entity:entitylist){
                    if(entity == this){
                        continue;
                    }
                    float damage = 20/this.getDistance(entity);
                    entity.attackEntityFrom(DamageSources.CLAYMORE, damage);
                }
            }

            if(this.ticksExisted>=life){
                if(this.getOwner() != null && this.getOwner() instanceof EntityRosmontis){
                    ItemStack stack = new ItemStack(registerItems.CLAYMORE.get());
                    boolean isStored = false;
                    for(int i = 0; i<((EntityRosmontis) this.getOwner()).getSkillItemCount(); i++) {
                        if(((EntityRosmontis) this.getOwner()).getInventory().insertItem(12+i, stack, true).isEmpty()){
                            ((EntityRosmontis) this.getOwner()).getInventory().insertItem(12+i, stack, false);
                            isStored = true;
                        }
                    }
                    if(!isStored){
                        for(int i=0; i<12; i++){
                            if(((EntityRosmontis) this.getOwner()).getInventory().insertItem(i, stack, true).isEmpty()){
                                ((EntityRosmontis) this.getOwner()).getInventory().insertItem(i, stack, false);
                                isStored = true;
                            }
                        }
                        if(!isStored){
                            ItemEntity entity = new ItemEntity(this.getEntityWorld(), this.getOwner().getPosX(), this.getOwner().getPosY(), this.getOwner().getPosZ(), stack);
                            this.getEntityWorld().addEntity(entity);
                        }
                    }
                }
                this.remove();
            }
        }
    }

    @Override
    public HandSide getPrimaryHand() {
        return RIGHT;
    }


    @Override
    public void readAdditional(CompoundNBT compound) {
        UUID uuid;
        if (compound.hasUniqueId("Owner")) {
            uuid = compound.getUniqueId("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }
    }

    @Nonnull
    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        if (this.getOwnerId() != null) {
            compound.putUniqueId("owner", this.getOwnerId());
        }
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<EntityClaymore> controller = new AnimationController<>(this, "claymore_controller", 0, this::predicate);
        data.addAnimationController(controller);
        controller.registerSoundListener(this::soundListener);
        controller.registerParticleListener(this::particleListener);
        controller.registerCustomInstructionListener(this::instructionListener);
    }

    private void particleListener(ParticleKeyFrameEvent<EntityClaymore> entityClaymoreParticleKeyFrameEvent) {
    }

    private void soundListener(SoundKeyframeEvent<EntityClaymore> entityClaymoreSoundKeyframeEvent) {
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> tAnimationEvent) {
        tAnimationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("attack", false));
        return PlayState.CONTINUE;
    }

    private void instructionListener(CustomInstructionKeyframeEvent<EntityClaymore> customInstructionKeyframeEvent) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0)
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 0)
                .createMutableAttribute(Attributes.MAX_HEALTH, 30)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0)
                ;
    }
}
