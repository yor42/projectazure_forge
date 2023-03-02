package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class EntityCannonPelllet extends DamagingProjectileEntity {

    private AmmoProperties properties;
    private LivingEntity shooter;
    private BlockPos originPos;

    public EntityCannonPelllet(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, AmmoProperties properties){
        super(registerEntity.PROJECTILE_CANNONSHELL.get(), shooter, accelX, accelY, accelZ, worldIn);
        this.properties = properties;
        this.shooter = shooter;
        this.originPos = shooter.blockPosition();
    }

    public EntityCannonPelllet(EntityType<? extends DamagingProjectileEntity> entityType, World worldIn) {
        super(entityType,worldIn);
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public AmmoProperties getProperties() {
        return this.properties;
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
        super.onHitBlock(p_230299_1_);

        if(this.properties == null) {
            return;
        }
        BlockPos blockPosIn = this.blockPosition();
        if(this.getCommandSenderWorld().getBlockState(blockPosIn).isPathfindable(this.getCommandSenderWorld(), blockPosIn, PathType.AIR)) {
            return;
        }

        boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
        this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.properties.isExplosive()? 1F:0.2F, this.properties.isFiery(), flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
        this.remove();
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity target = result.getEntity();
        if(target == this.shooter){
            return;
        }
        if(!this.level.isClientSide()) {
            double DistanceMultiplier = Math.min(400 / distanceToSqr(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0);
            if (this.properties != null) {
                if (target instanceof EntityKansenBase) {
                    ((EntityKansenBase) target).attackEntityFromCannon(DamageSources.causeCannonDamage(this, this.getOwner()), this.properties, DistanceMultiplier);
                } else
                    target.hurt(DamageSources.causeCannonDamage(this, this.getOwner()), (float) (this.properties.getMaxDamage() * 1.2));
            }
        }
        this.remove();
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d vector3d = (new Vector3d(x, y, z)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
        this.setDeltaMovement(vector3d);
        float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
        this.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
        this.xRot = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        if(p_70097_2_>=8) {
            if (this.properties != null && this.properties.getCategory().ShouldDamageMultipleComponent()) {
                this.getCommandSenderWorld().explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.Mode.DESTROY);
            } else {
                this.remove();
            }
        }

        return false;
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if(this.tickCount == 600 || this.isInWater()){
            this.remove();
        }
        super.tick();
    }
}
