package com.yor42.projectazure.gameobject.entity.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityProjectileBullet extends Entity {

    private UUID ShooterUUID;
    private int ShooterEntityID;
    private float Damage = 2.0F;


    public EntityProjectileBullet(EntityType<? extends EntityProjectileBullet> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityProjectileBullet(EntityType<? extends EntityProjectileBullet> type, double x, double y, double z, World worldIn){
        this(type, worldIn);
        this.setPosition(x, y, z);
    }

    public EntityProjectileBullet(EntityType<? extends EntityProjectileBullet> type, LivingEntity shooter, World worldIn){
        this(type, worldIn);
        this.setShooter(shooter);

    }

    private void setShooter(LivingEntity shooter) {
        this.ShooterUUID = shooter.getUniqueID();
        this.ShooterEntityID = shooter.getEntityId();
    }

    @Nullable
    public Entity getShooter(){
        if(this.ShooterUUID != null && this.world instanceof ServerWorld){
            return ((ServerWorld) this.world).getEntityByUuid(this.ShooterUUID);
        }
        else{
            return this.ShooterEntityID != 0? this.world.getEntityByID(this.ShooterEntityID):null;
        }

    }


    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
