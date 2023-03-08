package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class EntityThrownKnifeProjectile extends AbstractArrowEntity implements IAnimatable {

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> SHOULDRETURN = EntityDataManager.defineId(EntityThrownKnifeProjectile.class, DataSerializers.BOOLEAN);
    private int previousDamage = 1;
    boolean dealtDamage = false;
    public EntityThrownKnifeProjectile(EntityType<EntityThrownKnifeProjectile> type, World p_i48546_2_) {
        super(type, p_i48546_2_);
    }

    public EntityThrownKnifeProjectile(EntityType<EntityThrownKnifeProjectile> type, LivingEntity shooter, World world, ItemStack stack) {
        this(type, shooter, world, stack, false);
    }

    protected EntityThrownKnifeProjectile(EntityType<EntityThrownKnifeProjectile> type, LivingEntity shooter, World world, ItemStack stack, boolean shouldReturnToOwner) {
        super(type, shooter, world);
        this.previousDamage = stack.getDamageValue();
        this.entityData.set(SHOULDRETURN, shouldReturnToOwner);
    }
    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (this.entityData.get(SHOULDRETURN)) {
                if (!this.isAcceptibleReturnOwner()) {
                    if (!this.level.isClientSide && this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }

                    this.remove();
                } else {
                    this.setNoPhysics(true);
                    Vector3d vector3d = new Vector3d(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                    this.setPosRaw(this.getX(), this.getY() + vector3d.y * 0.03D, this.getZ());
                    if (this.level.isClientSide) {
                        this.yOld = this.getY();
                    }

                    double d0 = 0.1D;
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(d0)));
                }
            }
        }
        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Nullable
    protected EntityRayTraceResult findHitEntity(Vector3d p_213866_1_, Vector3d p_213866_2_) {
        return this.dealtDamage ? null : super.findHitEntity(p_213866_1_, p_213866_2_);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT comp) {
        super.save(comp);
        comp.putBoolean("shouldreturn", this.entityData.get(SHOULDRETURN));
        comp.putBoolean("dealtdamage", this.dealtDamage);
        comp.putInt("stackdamage", this.previousDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(SHOULDRETURN, compound.getBoolean("shouldreturn"));
        this.dealtDamage = compound.getBoolean("dealtdamage");
        this.previousDamage = compound.getInt("stackdamage");
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        Entity entity = p_213868_1_.getEntity();
        float f = 2.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            f += EnchantmentHelper.getDamageBonus(new ItemStack(RegisterItems.TACTICAL_KNIFE.get()), livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = DamageSources.knife(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity)entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
    }

    @Override
    protected ItemStack getPickupItem() {
        ItemStack stack = new ItemStack(RegisterItems.TACTICAL_KNIFE.get());
        boolean isbroken =  this.previousDamage+1>= RegisterItems.TACTICAL_KNIFE.get().getMaxDamage(stack);
        stack.setDamageValue(this.previousDamage+1);
        return isbroken? ItemStack.EMPTY: stack;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOULDRETURN, false);
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
        super.onHitBlock(p_230299_1_);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
