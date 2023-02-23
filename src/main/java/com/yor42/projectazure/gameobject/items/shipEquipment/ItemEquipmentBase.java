package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColor;

public abstract class ItemEquipmentBase extends ItemDestroyable implements IAnimatable {


    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    protected enums.SLOTTYPE slot;
    protected int firedelay;

    public ItemEquipmentBase(Properties properties, int maxHP) {
        super(properties, maxHP);
    }

    public enums.SLOTTYPE getSlot() {
        return slot;
    }

    @Override
    public boolean shouldOverrideMultiplayerNbt() {
        return true;
    }

    public abstract AnimatedGeoModel<?> getEquipmentModel();

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    public int getFiredelay() {
        return this.firedelay;
    }

    public abstract void onUpdate(ItemStack EquipmentStack, ItemStack RiggingStack);

    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent("HP: "+ getCurrentHP(stack)+"/"+this.getMaxHP()).setStyle(Style.EMPTY.withColor(getHPColor(stack))));
        tooltip.add(new TranslationTextComponent("item.tooltip.equipmenttype").append(": ").withStyle(TextFormatting.GRAY).append(new TranslationTextComponent(this.slot.getName()).withStyle(TextFormatting.BLUE)));
    }

    public ResourceLocation getTexture(){
        return this.getEquipmentModel().getTextureLocation(null);
    };

    @OnlyIn(Dist.CLIENT)
    public void applyEquipmentCustomRotation(ItemStack equipment, GeoModel EquipmentModel, enums.SLOTTYPE slottype, int index, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){

    }
}
