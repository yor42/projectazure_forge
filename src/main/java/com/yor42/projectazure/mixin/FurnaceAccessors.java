package com.yor42.projectazure.mixin;

import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceTileEntity.class)
public interface FurnaceAccessors {
    @Accessor
    IIntArray getDataAccess();

    @Accessor
    IRecipeType<? extends AbstractCookingRecipe> getRecipeType();
}
