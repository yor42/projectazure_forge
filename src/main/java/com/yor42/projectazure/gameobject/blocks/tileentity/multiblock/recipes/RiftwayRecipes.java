package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.lowdragmc.multiblocked.api.capability.MultiblockCapability;
import com.lowdragmc.multiblocked.api.recipe.Content;
import com.lowdragmc.multiblocked.api.recipe.RecipeCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.UUID;

public class RiftwayRecipes extends WeightedRandomRecipe{

    @Nullable
    private UUID playerUUID;

    public RiftwayRecipes(String uid, ImmutableMap<MultiblockCapability<?>, ImmutableList<Content>> inputs, ImmutableMap<MultiblockCapability<?>, ImmutableList<Content>> outputs, ImmutableMap<MultiblockCapability<?>, ImmutableList<Content>> tickInputs, ImmutableMap<MultiblockCapability<?>, ImmutableList<Content>> tickOutputs, ImmutableList<RecipeCondition> conditions, CompoundTag data, Component text, int duration) {
        super(uid, inputs, outputs, tickInputs, tickOutputs, conditions, data, text, duration);
    }

    public void setPlayerUUID(@Nullable UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Nullable
    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
