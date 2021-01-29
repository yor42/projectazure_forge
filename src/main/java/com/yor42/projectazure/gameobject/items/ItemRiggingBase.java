package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public abstract class ItemRiggingBase extends itemBaseTooltip implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);

    private int remainingHP, totalHP;

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
        this.totalHP = 1000;
        this.remainingHP = totalHP;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("rigging_valid_on.tooltip").setStyle(Style.EMPTY.setColor(Color.fromInt(16711680)).setBold(true)).appendString(" ").append(new TranslationTextComponent(this.validclass.getName())).setStyle(Style.EMPTY.setColor(Color.fromInt(16777215))));
    }

    public enums.shipClass getValidclass() {
        return validclass;
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    public abstract IAnimatableModel getModel();

    public void damageRigging(int amount){
        this.remainingHP -= amount;
    }

    public boolean isUseable(){
        return this.remainingHP > 0;
    }
}
