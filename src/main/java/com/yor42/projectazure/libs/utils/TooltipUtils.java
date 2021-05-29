package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.libs.defined;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
public final class TooltipUtils {
    @OnlyIn(Dist.CLIENT)
    public static ITextComponent getShiftInfoTooltip() {
        ITextComponent shift = new StringTextComponent("[SHIFT]").mergeStyle(TextFormatting.YELLOW);
        return new TranslationTextComponent("item."+defined.MODID+".shiftinfo", shift).mergeStyle(TextFormatting.GRAY);
    }
    @OnlyIn(Dist.CLIENT)
    public static void addOnShift(List<ITextComponent> tooltip, Runnable lambda) {
        if (Screen.hasShiftDown()) {
            lambda.run();
        } else {
            tooltip.add(getShiftInfoTooltip());
        }
    }
}
