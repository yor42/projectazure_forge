package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.libs.enums;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColor;

public abstract class ItemEquipmentBase extends ItemDestroyable implements IAnimatable {


    public AnimationFactory factory = new AnimationFactory(this);

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
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TextComponent("HP: "+ getCurrentHP(stack)+"/"+this.getMaxHP()).setStyle(Style.EMPTY.withColor(getHPColor(stack))));
        tooltip.add(new TranslatableComponent("item.tooltip.equipmenttype").append(": ").withStyle(ChatFormatting.GRAY).append(new TranslatableComponent(this.slot.getName()).withStyle(ChatFormatting.BLUE)));
    }

    public ResourceLocation getTexture(){
        return this.getEquipmentModel().getTextureLocation(null);
    };
}
