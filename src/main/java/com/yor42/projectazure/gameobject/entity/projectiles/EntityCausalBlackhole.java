package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.yor42.projectazure.gameobject.misc.DamageSources.CAUSAL_BLACKHOLE;

public class EntityCausalBlackhole extends Entity {

    private UUID owner;

    public EntityCausalBlackhole(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
        this.noPhysics = true;
        this.owner = null;
    }

    public EntityCausalBlackhole(Level p_i48580_2_, LivingEntity owner) {
        super(registerEntity.BLACKHOLE.get(), p_i48580_2_);
        this.noPhysics = true;
        this.owner = owner.getUUID();
    }

    public static void SpawnAroundTarget(ServerLevel world, AbstractEntityCompanion entityCompanion, LivingEntity target){
        EntityCausalBlackhole blackhole = new EntityCausalBlackhole(entityCompanion.getCommandSenderWorld(), entityCompanion);

        double x = target.getX()-4+(8* MathUtil.getRand().nextDouble());
        double y = target.getY()+(2* MathUtil.getRand().nextDouble());
        double z = target.getZ()-4+(8* MathUtil.getRand().nextDouble());
        blackhole.setPos(x,y,z);
        world.addFreshEntity(blackhole);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        int life = 400;
        if(this.tickCount>= life){
            this.discard();
        }
        if(!this.getCommandSenderWorld().isClientSide()) {
            this.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0D, 8, 8)).stream().filter((entity) -> {
                if(entity instanceof Player && ((Player) entity).isCreative() || entity.getUUID().equals(this.owner)){
                    return false;
                }
                Entity Owner = ((ServerLevel)this.getCommandSenderWorld()).getEntity(this.owner);

                if(Owner == null){
                    return true;
                }
                LivingEntity CompanionOwner = ((AbstractEntityCompanion) Owner).getOwner();

                if(CompanionOwner == null){
                    return true;
                }
                return ((AbstractEntityCompanion) Owner).wantsToAttack(entity, CompanionOwner);
            }).forEach((entity)->{
                Vec3 delta = new Vec3(this.getX()-entity.getX(), this.getY(0.5)-entity.getY(0.5), this.getZ()-entity.getZ());
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
    protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
        this.owner = p_70037_1_.getUUID("owner");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
        p_213281_1_.putUUID("owner", this.owner);
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
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
