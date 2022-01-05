package com.yor42.projectazure.gameobject.entity.companion.sworduser;

import com.google.common.collect.Lists;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TieredItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

import static com.yor42.projectazure.setup.register.registerSounds.CHIXIAO_HIT;
import static com.yor42.projectazure.setup.register.registerSounds.SHEATH_HIT;

public class EntityChen extends AbstractSwordUserBase implements IAknOp {
    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.OPERATOR;
    }

    public EntityChen(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public int getInitialAttackDelay() {
        return 31;
    }

    @Override
    public ArrayList<Integer> getAttackPreAnimationDelay() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(8);
        list.add(24);
        return list;
    }

    public ArrayList<Item> getTalentedWeaponList(){
        return new ArrayList<>(Arrays.asList(registerItems.CHIXIAO.get(), registerItems.SHEATH.get()));
    }

    public SoundEvent getAttackSound(){
        return this.getHeldItemMainhand().getItem() == registerItems.CHIXIAO.get()? CHIXIAO_HIT:SHEATH_HIT;
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return isUsingTalentedWeapon? 4:3;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        this.playSound(getAttackSound(), 1F, 0.8F + this.getRNG().nextFloat() * 0.4F);
        if(AttackCount == 2){
            target.attackEntityFrom(DamageSource.causeMobDamage(this), damage+3);
        }
        else{
            target.attackEntityFrom(DamageSource.causeMobDamage(this), damage*0.3F);
            target.applyKnockback(0.09F, MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F)), -MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F)));
        }
    }

    @Override
    public float getAttackSpeedModifier(boolean isUsingTalentedWeapon) {
        return isUsingTalentedWeapon? 1:1.2F;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        super.registerControllers(animationData);
        animationData.addAnimationController(new AnimationController<>(this, "controller_tail", 10, this::predicate_tail));
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSitting() || this.getRidingEntity() != null){
            event.getController().setAnimation(builder.addAnimation("sit_leg_start").addAnimation("sit_leg", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }
        else if(this.isAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_leg", true));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_leg", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_leg", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            return PlayState.STOP;
        }
        event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        return PlayState.STOP;
    }

    protected <E extends IAnimatable> PlayState predicate_tail(AnimationEvent<E> event) {
        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSitting()){
            event.getController().setAnimation(builder.addAnimation("sit_tail_start").addAnimation("sit_tail", true));
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat_tail", true));
        }
        else{
            event.getController().setAnimation(builder.addAnimation("idle_tail", true));
        }

        return PlayState.CONTINUE;

    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep", true));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", true));
            return PlayState.CONTINUE;
        }

        if(this.isOpeningDoor()){
            if(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND)== ItemStack.EMPTY && this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("opendoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("opendoorR", false));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isReloadingMainHand()){
            event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
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
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", true));
            return PlayState.CONTINUE;
        }

        if(this.isSitting()){
            if(this.getHeldItemMainhand().getItem() instanceof TieredItem){
                event.getController().setAnimation(builder.addAnimation("sit_arm_toolmainhand", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_arm_emptymainhand", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else{
            if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){
                if(((ItemGunBase) this.getHeldItemMainhand().getItem()).isTwoHanded()){
                    event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
                }
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(builder.addAnimation("idle_arm", true));
            return PlayState.CONTINUE;
        }
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.ChenMovementSpeed.get())
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.ChenSwimSpeed.get())
                .createMutableAttribute(Attributes.MAX_HEALTH, PAConfig.CONFIG.ChenHealth.get())
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.ChenAttackDamage.get())
                ;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerAKNInventory.Supplier(this));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_6;
    }

    @Override
    public enums.OperatorClass getOperatorClass() {
        return enums.OperatorClass.GUARD;
    }

    @Override
    public boolean canUseSkill() {
        return this.hasSkillItem();
    }

    @Override
    public boolean isSkillItem(ItemStack stack) {
        return stack.getItem() == registerItems.CHIXIAO.get();
    }
}
