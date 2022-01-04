package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public abstract class AbstractCompanionMagicUser extends AbstractEntityCompanion {

    protected int StartedAttackTimeStamp = -1;
    protected static final DataParameter<Integer> SPELLDELAY = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);

    public AbstractCompanionMagicUser(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(SPELLDELAY, -1);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.getAffection()>=75? registerSounds.CHIMERA_TALK_NORMAL: registerSounds.CHIMERA_TALK_HIGH_AFFECTION;
    }

    public int getSpellDelay(){
        return this.getDataManager().get(SPELLDELAY);
    }

    public void setSpellDelay(int value) {
        this.getDataManager().set(SPELLDELAY, value);
    }

    /*
    Delay between Each fire
     */
    public abstract int getInitialSpellDelay();

    /*
    Delay of Pre animation before actually firing projectile
     */
    public abstract int getProjectilePreAnimationDelay();

    public abstract Hand getSpellUsingHand();

    public boolean shouldUseSpell(){
        if(this.getGunStack().getItem() instanceof ItemGunBase) {
            boolean hasAmmo = getRemainingAmmo(this.getGunStack()) > 0;
            boolean reloadable = this.HasRightMagazine((((ItemGunBase) this.getGunStack().getItem()).getAmmoType()));

            return !(hasAmmo || reloadable);
        }
        else return this.getHeldItem(getSpellUsingHand()).isEmpty() && !this.isSwimming();
    }

    public boolean isUsingSpell(){
        return this.getSpellDelay()>0;
    }

    public abstract void ShootProjectile(World world, @Nonnull LivingEntity target);

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.getDataManager().set(SPELLDELAY, compound.getInt("spelldelay"));
    }

    public void writeAdditional(@Nonnull CompoundNBT compound){
        super.writeAdditional(compound);
        compound.putInt("spelldelay", this.getDataManager().get(SPELLDELAY));
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if(!this.getEntityWorld().isRemote()) {
            int currentspelldelay = this.getSpellDelay();
            @Nullable
            LivingEntity target = this.getAttackTarget();
            if (currentspelldelay > 0) {
                if (target == null || !target.isAlive()) {
                    this.setSpellDelay(0);
                } else {
                    this.getNavigator().clearPath();
                    setSpellDelay(currentspelldelay - 1);
                    if (this.ticksExisted - this.StartedAttackTimeStamp == getProjectilePreAnimationDelay()) {
                        this.ShootProjectile(this.getEntityWorld(), target);
                    }
                }
            }
        }
    }

    public void StartShootingEntity(LivingEntity target) {
        this.setSpellDelay(this.getInitialSpellDelay());
        this.StartedAttackTimeStamp = this.ticksExisted;
    }
}
