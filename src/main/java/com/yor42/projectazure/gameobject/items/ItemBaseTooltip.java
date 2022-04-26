package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.ChatFormatting;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBaseTooltip extends Item {
    public ItemBaseTooltip(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (worldIn != null && worldIn.isClientSide) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
    }
}
