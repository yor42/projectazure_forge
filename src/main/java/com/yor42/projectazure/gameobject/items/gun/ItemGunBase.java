package com.yor42.projectazure.gameobject.items.gun;

import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileBullet;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

public abstract class ItemGunBase extends Item implements IAnimatable {

    private boolean isSemiAuto, isTwoHanded;
    private int minFireDelay;
    private int reloadDelay;

    private final SoundEvent fireSound;
    private SoundEvent reloadSound;
    private float damage, accuracy;

    private int magCap, roundsPerReload;
    protected final String controllerName = "gunController";

    public AnimationFactory factory = new AnimationFactory(this);

    public ItemGunBase(boolean semiAuto, int minFiretime, int clipsize, int reloadtime, float damage, SoundEvent firesound, SoundEvent reloadsound, int roundsPerReload, float accuracy, Properties properties, boolean isTwohanded) {
        super(properties);
        this.isSemiAuto = semiAuto;
        this.minFireDelay = minFiretime;
        this.reloadDelay = reloadtime;
        this.fireSound = firesound;
        this.reloadSound = reloadsound;
        this.damage = damage;
        this.accuracy = accuracy;
        this.magCap = clipsize;
        this.roundsPerReload = roundsPerReload;
        this.isTwoHanded = isTwohanded;
    }

    public SoundEvent getFireSound() {
        return this.fireSound;
    }

    public SoundEvent getReloadSound() {
        return this.reloadSound;
    }

    public boolean isTwoHanded(){
        return this.isTwoHanded;
    }


    public int getMinFireDelay() {
        return this.minFireDelay;
    }

    public boolean ShouldFireWithLeftClick(){
        return true;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        this.SecondaryAction(playerIn, playerIn.getHeldItem(handIn));
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }

    public boolean isSemiAuto() {
        return this.isSemiAuto;
    }

    public String getFactoryName(){
        return this.controllerName;
    }

    public boolean ShouldDoBowPose(){
        return true;
    }

    protected abstract void SecondaryAction(PlayerEntity playerIn, ItemStack heldItem);

    public boolean shootGun(ItemStack gun, World world, PlayerEntity entity, boolean zooming, Hand hand, @Nullable Entity target){


        ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(entity);
        //AnimationController controller = GeckoLibUtil.getControllerForStack(this.getFactory(), gun, this.getFactoryName());
        int ammo = this.getAmmo(gun);
        if(ammo>0 ) {
            if (capability.getDelay(hand) <= 0) {

                entity.playSound(this.fireSound, 1.0F, (getRand().nextFloat() - getRand().nextFloat()) * 0.2F + 1.0F);
                if (!entity.isCreative()) {
                        this.useAmmo(gun, (short) 1);
                }
                if(!world.isRemote()) {

                        this.spawnProjectile(entity, world, gun, this.accuracy, this.damage, target, hand);

                        capability.setDelay(hand, this.getMinFireDelay());
                }

            }
            return false;
        }
        else {
            if(!world.isRemote()) {
                capability.setDelay(hand, this.reloadDelay - this.minFireDelay);
            }
            if (this.roundsPerReload > 0) {
                int i = 1;
                while (i < this.roundsPerReload) {
                    i++;
                }
                this.reloadAmmo(gun, i);
            } else {
                this.reloadAmmo(gun);
            }
            return true;
        }
    }

    public boolean shootGunLivingEntity(ItemStack gun, World world, LivingEntity entity, boolean zooming, Hand hand, @Nullable Entity target) {
        entity.playSound(this.fireSound, 1.0F, (getRand().nextFloat() - getRand().nextFloat()) * 0.2F + 1.0F);
        if (!world.isRemote()) {

            this.spawnProjectile(entity, world, gun, this.accuracy, this.damage, target, hand);
        }
        return false;

    }

    public abstract enums.AmmoCalibur getCalibur();

    private void spawnProjectile(LivingEntity Shooter, World worldIn, ItemStack gunStack, float Accuracy, float Damage, Entity target, Hand hand) {
        EntityProjectileBullet entity = new EntityProjectileBullet(Shooter, worldIn, Damage);
        if(target!=null){
            double d0 = target.getPosYEye() - (double)1.1F;
            double d1 = target.getPosX() - Shooter.getPosX();
            double d2 = d0 - entity.getPosY();
            double d3 = target.getPosZ() - Shooter.getPosZ();
            float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
            entity.shoot(d1, d2 + (double)f, d3, 2.0f, Accuracy);
        }
        else{
            entity.ShootFromPlayer(Shooter, Shooter.rotationPitch, Shooter.rotationYaw, 0.0f, 2.0F, Accuracy, hand);

        }
        worldIn.addEntity(entity);


    }

    public short getAmmo(ItemStack stack){
        CompoundNBT compound = stack.getOrCreateTag();
        return compound.getShort("ammo");
    }

    public void useAmmo(ItemStack stack, short amount){
        short ammo = getAmmo(stack);
        CompoundNBT compound = stack.getOrCreateTag();
        compound.putShort("ammo", (short) Math.max(ammo-amount, 0));
    }

    public void reloadAmmo(ItemStack gun, int amount) {
        short ammo = this.getAmmo(gun);

        CompoundNBT nbt = gun.getOrCreateTag();
        nbt.putShort("ammo", (short) (ammo+amount));
    }

   public void reloadAmmo(ItemStack gun){
        this.reloadAmmo(gun, this.magCap);
   }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.tooltip.remaining_ammo").appendString(": "+this.getAmmo(stack)+"/"+this.magCap));
        tooltip.add(new TranslationTextComponent("tempinfo.useable_gun_wip"));
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController controller = new AnimationController(this, this.controllerName, 1, this::predicate);
        animationData.addAnimationController(controller);
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        // Not setting an animation here as that's handled in shootGun()
        return PlayState.CONTINUE;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1-((float)getAmmo(stack)/this.magCap);
    }





    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
