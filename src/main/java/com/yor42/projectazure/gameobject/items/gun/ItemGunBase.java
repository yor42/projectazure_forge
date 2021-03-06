package com.yor42.projectazure.gameobject.items.gun;

import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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

    private boolean isSemiAuto;
    private int minFireDelay;
    private int reloadDelay;

    private final SoundEvent fireSound;
    private SoundEvent reloadSound;
    private float damage, accuracy;

    private int magCap, roundsPerReload;
    protected final String controllerName = "gunController";

    public AnimationFactory factory = new AnimationFactory(this);

    public ItemGunBase(boolean semiAuto, int minFiretime, int clipsize, int reloadtime, float damage, SoundEvent firesound, SoundEvent reloadsound, int roundsPerReload, float accuracy, Properties properties) {
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

    public void shootGun(ItemStack gun, World world, PlayerEntity entity, boolean zooming, Hand hand, @Nullable Entity target){


        ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(entity);
        AnimationController controller = GeckoLibUtil.getControllerForStack(this.getFactory(), gun, this.getFactoryName());
        int ammo = this.getAmmo(gun);
        if(ammo>=1 ) {
            if (capability.getDelay(hand) <= 0) {

                entity.playSound(this.fireSound, 1.0F, (getRand().nextFloat() - getRand().nextFloat()) * 0.2F + 1.0F);
                    if (!entity.isCreative()) {
                        this.useAmmo(gun, (short) 1);
                    }
                    if (world.isRemote() && controller.getAnimationState() == AnimationState.Stopped) {
                        controller.markNeedsReload();
                        this.doFireAnimation(controller);
                    }
                    if(!world.isRemote()) {
                        capability.setDelay(hand, this.getMinFireDelay());
                    }
            }
        }
        else{
                capability.setDelay(hand, this.reloadDelay - this.minFireDelay);
                if (this.roundsPerReload > 0) {
                    int i = 1;
                    while (i < this.roundsPerReload) {
                        i++;
                    }
                    this.reloadAmmo(gun, i);
                } else {
                    this.reloadAmmo(gun);
                }
            controller.markNeedsReload();

                this.doReloadAnimation(controller);
        }
    }

    protected abstract void doReloadAnimation(AnimationController controller);

    public void reloadAmmo(ItemStack gun, int amount) {
        short ammo = this.getAmmo(gun);

        CompoundNBT nbt = gun.getOrCreateTag();
        nbt.putShort("ammo", (short) (ammo+amount));
    }

   public void reloadAmmo(ItemStack gun){
        this.reloadAmmo(gun, this.magCap);
   }

    protected abstract void doFireAnimation(AnimationController controller);


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
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

    public short getAmmo(ItemStack stack){
        CompoundNBT compound = stack.getOrCreateTag();
        return compound.getShort("ammo");
    }

    public void useAmmo(ItemStack stack, short amount){
        short ammo = getAmmo(stack);
        CompoundNBT compound = stack.getOrCreateTag();
        if (ammo-amount>=0){
            compound.putShort("ammo", (short) (ammo-amount));
        }
        else{
            compound.putShort("ammo", (short) 0);
        }
    }



    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
