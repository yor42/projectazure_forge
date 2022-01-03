package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityArtsProjectile;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;

public class EntityAmiya extends AbstractCompanionMagicUser{
    public EntityAmiya(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.OPERATOR;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {
        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();
        if(this.isSwingInProgress){
            event.getController().setAnimation(builder.addAnimation(this.swingingHand == Hand.MAIN_HAND?"swingR":"swingL", true));

            return PlayState.CONTINUE;
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_arm", false));

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
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", true));

            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", true));

            return PlayState.CONTINUE;
        }
        else if(this.isSitting() || this.getRidingEntity() != null) {
            event.getController().setAnimation(builder.addAnimation("sit_arm").addAnimation("sit_arm_idle", true));

            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOpeningDoor()){
            if(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND)== ItemStack.EMPTY && this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("openDoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("openDoorR", false));
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
        else if(this.isActiveItemStackBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", true));

            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", true));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){
            if(((ItemGunBase) this.getHeldItemMainhand().getItem()).isTwoHanded()){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
            }
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(builder.addAnimation("idle_arm", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_leg", true));
            return PlayState.CONTINUE;
        }

        if(this.isSitting() || this.getRidingEntity() != null){
            event.getController().setAnimation(builder.addAnimation("sit").addAnimation("sit_leg_idle"));
            return PlayState.CONTINUE;
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_leg", false));
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_leg", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_leg", true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerAKNInventory.Supplier(this));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        /*
        Supposed to be 5, but I dont think this entity should be THAT rare..
         */
        return enums.CompanionRarity.STAR_4;
    }

    @Override
    public int getInitialSpellDelay() {
        return 32;
    }

    @Override
    public int getProjectilePreAnimationDelay() {
        return 11;
    }

    @Override
    public Hand getSpellUsingHand() {
        return Hand.OFF_HAND;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.AmiyaMovementSpeed.get())
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.AmiyaSwimSpeed.get())
                .createMutableAttribute(Attributes.MAX_HEALTH, PAConfig.CONFIG.AmiyaHealth.get())
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.AmiyaAttackDamage.get())
                ;
    }

    @Override
    public void ShootProjectile(World world, @Nonnull LivingEntity target) {
        if(target.isAlive()){
            double x = target.getPosX() - (this.getPosX());
            double y = this.getPosYHeight(0.5) - (this.getPosYHeight(0.5));
            double z = target.getPosZ() - (this.getPosZ());

            EntityArtsProjectile projectile = new EntityArtsProjectile(this.getEntityWorld(), target);
            projectile.shoot(x,y,z, 0.8F, 0.05F);
            this.getEntityWorld().addEntity(projectile);

            this.addExp(0.2F);
            this.addMorale(-0.2);
        }
    }
}
