package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IShaderEquipment {
    ResourceLocation shaderLocation(ItemStack stack);

    boolean shouldDisplayShader(ItemStack stack);
}
