package com.yor42.projectazure.gameobject.entity.projectiles;

import com.tac.guns.common.Gun;
import com.tac.guns.entity.ProjectileEntity;
import com.tac.guns.item.GunItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityRailgunProjectile extends ProjectileEntity {
    private float power;
    public EntityRailgunProjectile(EntityType<? extends ProjectileEntity> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public EntityRailgunProjectile(EntityType<? extends ProjectileEntity> entityType, World worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun, float power, float randx, float randy) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun, randx, randy);
        this.power = power;
    }

    protected void onHitEntity(Entity entity, Vector3d hitVec, Vector3d startVec, Vector3d endVec, boolean headshot) {
        if(entity!=this.shooter) {
            createExplosion(this, this.power, false);
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
