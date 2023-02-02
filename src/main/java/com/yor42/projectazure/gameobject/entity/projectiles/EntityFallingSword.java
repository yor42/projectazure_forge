package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerEntity;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class EntityFallingSword extends AbstractArrowEntity {

    public EntityFallingSword(EntityType<EntityFallingSword> p_i48546_1_, World p_i48546_2_) {
        super(p_i48546_1_, p_i48546_2_);
    }

    public EntityFallingSword(LivingEntity owner, double p_i48547_2_, double p_i48547_4_, double p_i48547_6_, World p_i48547_8_) {
        super(registerEntity.PROJECTILE_FALLINGSWORD.get(), p_i48547_2_, p_i48547_4_, p_i48547_6_, p_i48547_8_);
        this.setOwner(owner);
    }

    @Nonnull
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return registerSounds.TEXAS_SWORD_HIT;
    }

    @Override
    protected void onHitEntity(@Nonnull EntityRayTraceResult hitresult) {
        Entity target = hitresult.getEntity();

        if(target == this.getOwner()){
            return;
        }

        if(this.getOwner() instanceof AbstractEntityCompanion && target instanceof LivingEntity && ((AbstractEntityCompanion) this.getOwner()).isAlly((LivingEntity) target)){
            return;
        }

        super.onHitEntity(hitresult);
        this.playSound(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
