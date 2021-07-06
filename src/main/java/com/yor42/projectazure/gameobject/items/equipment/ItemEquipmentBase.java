package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
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
    public boolean shouldSyncTag() {
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

    public abstract void onUpdate(ItemStack stack);

    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent("HP: "+ getCurrentHP(stack)+"/"+this.getMaxHP()).setStyle(Style.EMPTY.setColor(getHPColor(stack))));
        tooltip.add(new TranslationTextComponent("item.tooltip.equipmenttype").appendString(": ").mergeStyle(TextFormatting.GRAY).append(new TranslationTextComponent(this.slot.getName()).mergeStyle(TextFormatting.BLUE)));
    }

    public ResourceLocation getTexture(){
        return this.getEquipmentModel().getTextureLocation(null);
    };
}
