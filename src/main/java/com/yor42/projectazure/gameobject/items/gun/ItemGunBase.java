package com.yor42.projectazure.gameobject.items.gun;

import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemGunBase extends Item implements IAnimatable {

    private boolean isSemiAuto;
    private int minFireDelay;
    private int reloadDelay;

    private SoundEvent fireSound, reloadSound;
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

    protected abstract void SecondaryAction(PlayerEntity playerIn, ItemStack heldItem);

    public void shootGun(ItemStack gun, World world, PlayerEntity entity, boolean zooming, Hand hand, @Nullable Entity target){
        ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(entity);
        capability.setDelay(hand, this.getMinFireDelay());

    }

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
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
