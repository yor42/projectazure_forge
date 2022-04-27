package com.yor42.projectazure.gameobject.entity.misc;

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
import net.minecraft.inventory.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.EntityDataAccessor;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.SynchedEntityData;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.HandSide;
import net.minecraft.world.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
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
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(EntityClaymore.class, EntityDataSerializers.OPTIONAL_UUID);
    protected final AnimationFactory factory = new AnimationFactory(this);

    public EntityClaymore(EntityType<? extends LivingEntity> type, Level worldIn) {
        super(type, worldIn);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    @Nullable
    public Entity getOwner() {
        if(!this.getCommandSenderWorld().isClientSide()) {
            try {
                UUID uuid = this.getOwnerId();
                return uuid == null ? null : ((ServerWorld)this.level).getEntity(this.getOwnerId());
            } catch (IllegalArgumentException illegalargumentexception) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.getCommandSenderWorld().isClientSide()){

            if(this.tickCount == 5){
                this.playSound(registerSounds.CLAYMORE_IMPACT, 1.0F, 0.8F+(0.4F*this.getRandom().nextFloat()));
                List<LivingEntity> entitylist = this.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2));
                for(LivingEntity entity:entitylist){
                    if(entity == this){
                        continue;
                    }
                    float damage = 20/this.distanceTo(entity);
                    entity.hurt(DamageSources.CLAYMORE, damage);
                }
            }

            if(this.tickCount>=life){
                if(this.getOwner() != null && this.getOwner() instanceof EntityRosmontis){
                    ItemStack stack = new ItemStack(registerItems.CLAYMORE.get());
                    boolean isStored = false;
                    for(int i = 0; i<((EntityRosmontis) this.getOwner()).getSkillItemCount(); i++) {
                        if(((EntityRosmontis) this.getOwner()).getInventory().insertItem(12+i, stack, true).isEmpty()){
                            ((EntityRosmontis) this.getOwner()).getInventory().insertItem(12+i, stack, false);
                            isStored = true;
                            break;
                        }
                    }
                    if(!isStored){
                        for(int i=0; i<12; i++){
                            if(((EntityRosmontis) this.getOwner()).getInventory().insertItem(i, stack, true).isEmpty()){
                                ((EntityRosmontis) this.getOwner()).getInventory().insertItem(i, stack, false);
                                isStored = true;
                                break;
                            }
                        }
                        if(!isStored){
                            ItemEntity entity = new ItemEntity(this.getCommandSenderWorld(), this.getOwner().getX(), this.getOwner().getY(), this.getOwner().getZ(), stack);
                            this.getCommandSenderWorld().addFreshEntity(entity);
                        }
                    }
                }
                this.remove();
            }
        }
    }



    @Override
    public HandSide getMainArm() {
        return RIGHT;
    }


    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
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
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {

    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        if (this.getOwnerId() != null) {
            compound.putUUID("owner", this.getOwnerId());
        }
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
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
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(ForgeMod.SWIM_SPEED.get(), 0)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ATTACK_DAMAGE, 0)
                ;
    }
}
