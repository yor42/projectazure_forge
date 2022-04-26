package com.yor42.projectazure.gameobject.items.tools;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ChatFormatting;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.Level;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class ModSwordItem extends SwordItem {
    public ModSwordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
    }
}
