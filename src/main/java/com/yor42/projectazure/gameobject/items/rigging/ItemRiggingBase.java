package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.gameobject.items.itemBaseTooltip;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
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
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.DamageRiggingorEquipment;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.libs.utils.MathUtil.generateRandomInt;
import static com.yor42.projectazure.libs.utils.MathUtil.rollDamagingRiggingCount;

public abstract class ItemRiggingBase extends itemBaseTooltip implements IAnimatable {

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
        super(properties);
        this.MaxHP = HP;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack,worldIn,tooltip,flagIn);
        tooltip.add(new TranslationTextComponent("rigging_valid_on.tooltip").appendString(" ").append(new TranslationTextComponent(this.validclass.getName())).setStyle(Style.EMPTY.setColor(Color.fromInt(8900331)).setItalic(true)));
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0-getCurrentHP(stack) / (double)this.MaxHP;
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



    protected boolean canDamageRigging(ItemStack stack){
        int oldHP = stack.getTag().getInt("currentHP");
        return oldHP>0;
    };

    public abstract ItemStackHandler getEquipments(ItemStack riggingStack);

    public abstract int getEquipmentCount(ItemStack riggingStack);

    public abstract void onCanonFire(ItemStack stack);

    public abstract boolean canUseCanon(ItemStack stack);

    public abstract void onTorpedoFire(ItemStack stack);

    public abstract boolean canUseTorpedo(ItemStack stack);

    public abstract void onUpdate(ItemStack stack);

    public ResourceLocation getTexture(){
            return this.getModel().getTextureLocation(null);
    };
}
