package com.yor42.projectazure.gameobject.entity.companion.kansen;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.interfaces.IAzurLaneKansen;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.items.IItemHandlerModifiable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class EntityEnterprise extends EntityKansenAircraftCarrier implements IAzurLaneKansen {

    protected static final DataParameter<Integer> INVINCIBLE_TIMER = EntityDataManager.createKey(EntityEnterprise.class, DataSerializers.VARINT);

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSitting() || this.getRidingEntity() != null){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.sit_start").addAnimation("animation.enterprise.sit", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (isMoving()) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.sail", true));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.run", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.walk", true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        return PlayState.CONTINUE;
    }

    public EntityEnterprise(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.sleep", true));
            return PlayState.CONTINUE;
        }else if(this.isBeingPatted()){
            if(this.isSitting())
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.pat_sit", true));
            else
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.pat", true));
            return PlayState.CONTINUE;
        } else if(this.isOpeningDoor()){
            if(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND)== ItemStack.EMPTY && this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.doorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.doorR", false));
            }
            return PlayState.CONTINUE;
        }
        else if(this.ShouldPlayReloadAnim()){
            event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getActiveHand() == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", true));
            }
            else if(this.getActiveHand() == Hand.OFF_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_offhand", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.swingProgress>0){
            event.getController().setAnimation(builder.addAnimation(this.swingingHand == Hand.MAIN_HAND?"swingR":"swingL"));
            return PlayState.CONTINUE;
        }
        else if(this.isActiveItemStackBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", true));
            return PlayState.CONTINUE;
        }

        else if(this.isActiveItemStackBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", true));
            return PlayState.CONTINUE;
        }

        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.heal_arm", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.swim_arm", true));
            return PlayState.CONTINUE;
        }

        if (isMoving()) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.sail_arm", true));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.walk_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){
            if(((ItemGunBase) this.getHeldItemMainhand().getItem()).isTwoHanded()){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("animation.enterprise.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_5;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.EnterpriseMovementSpeed.get())
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.EnterpriseSwimSpeed.get())
                .createMutableAttribute(Attributes.MAX_HEALTH, PAConfig.CONFIG.EnterpriseHealth.get())
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.EnterpriseAttackDamage.get())
                ;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.getInvincibleTimer()>0){
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void livingTick() {
        super.livingTick();
        int InvincibleTimer = this.getInvincibleTimer();
        if(InvincibleTimer>0){
            this.setInvincibleTimer(InvincibleTimer-1);
        }

        if (this.world.isRemote && this.getInvincibleTimer()>0 && this.ticksExisted%10 == 0) {
            for(int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(0.5D), this.getPosYRandom() - 0.25D, this.getPosZRandom(0.5D), (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    @Override
    public void LaunchPlane(ItemStack planestack, AbstractEntityPlanes plane, LivingEntity target, IItemHandlerModifiable hanger, int hangerIndex) {
        if(this.getRNG().nextFloat()<0.4F){
            this.setInvincibleTimer(240);
            plane.setAttackDamage(plane.getAttackDamage()*2);
            for(int i = 0; i < 5; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D), this.getPosYRandom() + 1.0D, this.getPosZRandom(1.0D), d0, d1, d2);
            }
        }
        super.LaunchPlane(planestack, plane, target, hanger, hangerIndex);
    }

    private void setInvincibleTimer(int invincibleTimer) {
        this.dataManager.set(INVINCIBLE_TIMER, invincibleTimer);
    }

    private int getInvincibleTimer() {
        return this.dataManager.get(INVINCIBLE_TIMER);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(INVINCIBLE_TIMER, 0);
    }
}
