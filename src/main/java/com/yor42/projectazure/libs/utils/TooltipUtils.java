package com.yor42.projectazure.libs.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
    public static Component getShiftInfoTooltip() {
        Component shift = new TextComponent("[SHIFT]").withStyle(ChatFormatting.YELLOW);
        return new TranslatableComponent("item.tooltip.shiftinfo", shift).withStyle(ChatFormatting.GRAY);
    }
    @OnlyIn(Dist.CLIENT)
    public static void addOnShift(List<Component> tooltip, Runnable lambda) {
        if (Screen.hasShiftDown()) {
            lambda.run();
        } else {
            tooltip.add(getShiftInfoTooltip());
        }
    }
}
