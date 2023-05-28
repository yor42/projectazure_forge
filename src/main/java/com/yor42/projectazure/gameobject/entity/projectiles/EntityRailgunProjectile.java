package com.yor42.projectazure.gameobject.entity.projectiles;

import com.tac.guns.common.Gun;
import com.tac.guns.entity.ProjectileEntity;
import com.tac.guns.item.GunItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntityRailgunProjectile extends ProjectileEntity {
    private float power;
    public EntityRailgunProjectile(EntityType<? extends ProjectileEntity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public EntityRailgunProjectile(EntityType<? extends ProjectileEntity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun, float power, float randx, float randy) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun, randx, randy);
        this.power = power;
    }

    protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot) {
        if(entity!=this.shooter) {
            createExplosion(this, this.power, false);
            super.onHitEntity(entity, hitVec, startVec, endVec, headshot);
        }
    }

    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z) {
        createExplosion(this, this.power, false);
        this.life = 0;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount=Math.max(10, this.tickCount);
    }
}
