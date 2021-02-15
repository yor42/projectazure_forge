package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.gameobject.items.itemBaseTooltip;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.util.text.Color;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentDamage;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;

public abstract class ItemRiggingBase extends ItemDestroyable implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);

    protected int MaxHP;

    protected enums.shipClass validclass;

    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return null;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    public ItemRiggingBase(Properties properties, int HP) {
        super(properties, HP);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack,worldIn,tooltip,flagIn);
        tooltip.add(new StringTextComponent("HP: "+ getCurrentHP(stack)+"/"+this.getMaxHP()));
        tooltip.add(new TranslationTextComponent("rigging_valid_on.tooltip").appendString(" ").append(new TranslationTextComponent(this.validclass.getName())).setStyle(Style.EMPTY.setColor(Color.fromInt(8900331)).setItalic(true)));
    }


    public enums.shipClass getValidclass() {
        return validclass;
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    public abstract AnimatedGeoModel getModel();

    public abstract ItemStackHandler getEquipments(ItemStack riggingStack);

    public abstract int getEquipmentCount(ItemStack riggingStack);

    public abstract void onUpdate(ItemStack stack);

    public ResourceLocation getTexture(){
            return this.getModel().getTextureLocation(null);
    };
}
