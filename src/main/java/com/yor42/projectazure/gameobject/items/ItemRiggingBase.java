package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public abstract class ItemRiggingBase extends itemBaseTooltip implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);

    private int totalHP;

    public static final ItemStackHandler equipments = new ItemStackHandler();
    protected enums.shipClass validclass;

    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return null;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    public ItemRiggingBase(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack,worldIn,tooltip,flagIn);
        tooltip.add(new TranslationTextComponent("rigging_valid_on.tooltip").appendString(" ").append(new TranslationTextComponent(this.validclass.getName())).setStyle(Style.EMPTY.setColor(Color.fromInt(8900331)).setItalic(true)));
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0-(double)this.getCurrentHP(stack) / (double)this.getTotalHP(stack);
    }

    public void InitHP(ItemStack stack, int value){
        this.setTotalHP(stack, value);
        this.setCurrentHP(stack, value);
    }

    public void setTotalHP(ItemStack stack, int value) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }
        if(stack.getTag() != null){
            stack.getTag().putInt("maxHP", value);
        }
    }

    public int getTotalHP(ItemStack stack) {
        if(stack.getTag() == null || !stack.hasTag() || stack.getTag().contains("maxHP")){
            return 0;
        }
        else {
            return stack.getTag().getInt("maxHP");
        }
    }

    public void setCurrentHP(ItemStack stack, int value) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }
        if(stack.getTag() != null){
            stack.getTag().putInt("currentHP", value);
        }
    }

    public int getCurrentHP(ItemStack stack) {
        if(stack.getTag() == null || !stack.hasTag() || stack.getTag().contains("currentHP")){
            return 0;
        }
        else {
            return stack.getTag().getInt("currentHP");
        }
    }

    public enums.shipClass getValidclass() {
        return validclass;
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    public IAnimatableModel getModel(){
        GeoItemRenderer provider =  (GeoItemRenderer) this.getItemStackTileEntityRenderer();
        return provider.getGeoModelProvider();
    };

    public ItemStackHandler getEquipments(){
        return equipments;
    }

    public int damageRigging(ItemStack stack, int amount){

        if(stack.getTag() != null) {
            int oldHP = stack.getTag().getInt("currentHP");
            if (stack.getTag().contains("currentHP")) {
                int overdamage = oldHP - amount;
                if (overdamage <= 0) {
                    stack.getTag().putInt("currentHP", oldHP - amount);
                    return 0;
                } else {
                    stack.getTag().putInt("currentHP", 0);
                    return Math.abs(overdamage);
                }
            } else {
                stack.getTag().putInt("currentHP", this.totalHP - amount);
                return 0;
            }
        }
        else {
            stack.setTag(new CompoundNBT());
            int finalHP = this.totalHP-amount;
            if(finalHP >= 0){
                stack.getTag().putInt("currentHP", finalHP);
            }
            else {
                stack.getTag().putInt("currentHP", 0);
                return Math.abs(finalHP);
            }
        }
        return amount;
    }

    protected boolean canDamageRigging(ItemStack stack){
        int oldHP = stack.getTag().getInt("currentHP");
        return oldHP>0;
    };
}
