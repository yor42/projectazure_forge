package com.yor42.projectazure.mixin;

import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface FurnaceAccessors {
    @Accessor
    ContainerData getDataAccess();

    @Accessor
    RecipeType<? extends AbstractCookingRecipe> getRecipeType();
}
