package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.security.acl.Owner;
import java.util.UUID;

import static com.yor42.projectazure.gameobject.misc.DamageSources.CAUSAL_BLACKHOLE;

public class EntityCausalBlackhole extends Entity {

    private int life;
    private final UUID owner;

    public EntityCausalBlackhole(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
        this.life = 200;
        this.noPhysics = true;
        this.owner = null;
    }

    public EntityCausalBlackhole(World p_i48580_2_, LivingEntity owner) {
        super(registerEntity.BLACKHOLE.get(), p_i48580_2_);
        this.life = 400;
        this.noPhysics = true;
        this.owner = owner.getUUID();
    }

    public static void SpawnAroundTarget(AbstractEntityCompanion entityCompanion, LivingEntity target){
        EntityCausalBlackhole blackhole = new EntityCausalBlackhole(entityCompanion.getCommandSenderWorld(), entityCompanion);

        double x = target.getX()-4+(8* MathUtil.getRand().nextDouble());
        double y = target.getZ()-2+(4* MathUtil.getRand().nextDouble());
        double z = target.getZ()-4+(8* MathUtil.getRand().nextDouble());
        blackhole.setPos(x,y,z);
        entityCompanion.getCommandSenderWorld().addFreshEntity(blackhole);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if(!this.getCommandSenderWorld().isClientSide()) {

            this.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0D, 8, 8)).stream().filter((entity) -> {
                if(entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()){
                    return false;
                }
                Entity Owner = ((ServerWorld)this.getCommandSenderWorld()).getEntity(this.owner);

                if(Owner == null){
                    return true;
                }
                LivingEntity CompanionOwner = ((AbstractEntityCompanion) Owner).getOwner();

                if(CompanionOwner == null){
                    return true;
                }
                return ((AbstractEntityCompanion) Owner).wantsToAttack(entity, CompanionOwner);
            }).forEach((entity)->{
                Vector3d delta = new Vector3d(this.getX()-entity.getX(), this.getY(0.5)-entity.getY(0.5), this.getZ()-entity.getZ());
                double distance = delta.length();
                double d2 = 1.0D - distance / 8.0D;
                entity.setDeltaMovement(entity.getDeltaMovement().add(delta.normalize().scale(d2*0.4)));

                if (this.tickCount % 20 == 0) {
                    if(distance<=2){
                        entity.hurt(CAUSAL_BLACKHOLE, 4);
                    }
                }
            });
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        this.life = p_70037_1_.getInt("lifespan");
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        p_213281_1_.putInt("lifespan", this.life);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
