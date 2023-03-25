package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipebuilders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.utils.DummyWorld;
import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.capability.MultiblockCapability;
import com.lowdragmc.multiblocked.api.recipe.*;
import com.lowdragmc.multiblocked.common.capability.*;
import com.lowdragmc.multiblocked.common.recipe.conditions.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.capability.CompanionMultiblockCapability;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes.RiftwayRecipes;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes.WeightedRandomRecipe;
import com.yor42.projectazure.gameobject.crafting.ingredients.EntityIngredientCompanions;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

public class WeightedRecipeBuilder {

    public final RecipeMap recipeMap;
    public final Map<MultiblockCapability<?>, ImmutableList.Builder<Content>> inputBuilder = new HashMap<>();
    public final Map<MultiblockCapability<?>, ImmutableList.Builder<Content>> tickInputBuilder = new HashMap<>();
    public final Map<MultiblockCapability<?>, ImmutableList.Builder<Content>> outputBuilder = new HashMap<>();
    public final Map<MultiblockCapability<?>, ImmutableList.Builder<Content>> tickOutputBuilder = new HashMap<>();
    public final Map<String, Object> data = new HashMap<>();

    public final List<RecipeCondition> conditions = new ArrayList<>();
    protected int duration;
    protected Component text;
    protected boolean perTick;
    protected String fixedName;
    protected String slotName;
    protected float chance = 1;

    public WeightedRecipeBuilder(RecipeMap recipeMap) {
        this.recipeMap = recipeMap;
    }

    public WeightedRecipeBuilder copy() {
        WeightedRecipeBuilder copy = new WeightedRecipeBuilder(recipeMap);
        inputBuilder.forEach((k, v)->{
            ImmutableList.Builder<Content> builder = ImmutableList.builder();
            copy.inputBuilder.put(k, builder.addAll(v.build()));
        });
        outputBuilder.forEach((k, v)->{
            ImmutableList.Builder<Content> builder = ImmutableList.builder();
            copy.outputBuilder.put(k, builder.addAll(v.build()));
        });
        tickInputBuilder.forEach((k, v)->{
            ImmutableList.Builder<Content> builder = ImmutableList.builder();
            copy.tickInputBuilder.put(k, builder.addAll(v.build()));
        });
        tickOutputBuilder.forEach((k, v)->{
            ImmutableList.Builder<Content> builder = ImmutableList.builder();
            copy.tickOutputBuilder.put(k, builder.addAll(v.build()));
        });
        copy.data.putAll(data);
        copy.conditions.addAll(conditions);
        copy.duration = this.duration;
        copy.fixedName = null;
        copy.chance = this.chance;
        copy.perTick = this.perTick;
        return copy;
    }

    public WeightedRecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public WeightedRecipeBuilder data(String key, Object object) {
        this.data.put(key, object);
        return this;
    }


    public WeightedRecipeBuilder text(Component text) {
        this.text = text;
        return this;
    }

    public WeightedRecipeBuilder perTick(boolean perTick) {
        this.perTick = perTick;
        return this;
    }

    public WeightedRecipeBuilder name(String name) {
        this.fixedName = name;
        return this;
    }

    public WeightedRecipeBuilder chance(float chance) {
        this.chance = chance;
        return this;
    }

    public WeightedRecipeBuilder slotName(String slotName) {
        this.slotName = slotName != null && !slotName.isEmpty() ? slotName : null;
        return this;
    }

    public <T> WeightedRecipeBuilder input(MultiblockCapability<T> capability, T... obj) {
        (perTick ? tickInputBuilder : inputBuilder).computeIfAbsent(capability, c -> ImmutableList.builder()).addAll(Arrays.stream(obj).map(o->new Content(o, chance, slotName)).iterator());
        return this;
    }

    public <T> WeightedRecipeBuilder output(MultiblockCapability<T> capability, T... obj) {
        (perTick ? tickOutputBuilder : outputBuilder).computeIfAbsent(capability, c -> ImmutableList.builder()).addAll(Arrays.stream(obj).map(o->new Content(o, chance, slotName)).iterator());
        return this;
    }

    public <T> WeightedRecipeBuilder inputs(MultiblockCapability<T> capability, Object... obj) {
        (perTick ? tickInputBuilder : inputBuilder).computeIfAbsent(capability, c -> ImmutableList.builder()).addAll(Arrays.stream(obj)
                .map(capability::of)
                .map(o->new Content(o, chance, slotName)).iterator());
        return this;
    }

    public <T> WeightedRecipeBuilder outputs(MultiblockCapability<T> capability, Object... obj) {
        (perTick ? tickOutputBuilder : outputBuilder).computeIfAbsent(capability, c -> ImmutableList.builder()).addAll(Arrays.stream(obj)
                .map(capability::of)
                .map(o->new Content(o, chance, slotName)).iterator());
        return this;
    }

    public WeightedRecipeBuilder addCondition(RecipeCondition condition) {
        conditions.add(condition);
        return this;
    }

    public WeightedRecipeBuilder inputFE(int forgeEnergy) {
        return input(FEMultiblockCapability.CAP, forgeEnergy);
    }

    public WeightedRecipeBuilder outputFE(int forgeEnergy) {
        return output(FEMultiblockCapability.CAP, forgeEnergy);
    }

    public WeightedRecipeBuilder inputItems(ItemsIngredient... inputs) {
        return input(ItemMultiblockCapability.CAP, inputs);
    }

    public WeightedRecipeBuilder outputItems(ItemStack... outputs) {
        return output(ItemMultiblockCapability.CAP, Arrays.stream(outputs).map(ItemsIngredient::new).toArray(ItemsIngredient[]::new));
    }

    public WeightedRecipeBuilder inputFluids(FluidStack... inputs) {
        return input(FluidMultiblockCapability.CAP, inputs);
    }

    public WeightedRecipeBuilder outputFluids(FluidStack... outputs) {
        return output(FluidMultiblockCapability.CAP, outputs);
    }

    public WeightedRecipeBuilder inputHeat(double heat) {
        if (Multiblocked.isMekLoaded()) {
            return input(HeatMekanismCapability.CAP, heat);
        }
        return this;
    }

    public WeightedRecipeBuilder outputHeat(double heat) {
        if (Multiblocked.isMekLoaded()) {
            return output(HeatMekanismCapability.CAP, heat);
        }
        return this;
    }

    public WeightedRecipeBuilder inputGases(Object... inputs) {
        if (Multiblocked.isMekLoaded()) {
            return input(ChemicalMekanismCapability.CAP_GAS, Arrays.stream(inputs).map(ChemicalMekanismCapability.CAP_GAS::of).toArray(GasStack[]::new));
        }
        return this;
    }

    public WeightedRecipeBuilder outputGases(Object... outputs) {
        if (Multiblocked.isMekLoaded()) {
            return output(ChemicalMekanismCapability.CAP_GAS, Arrays.stream(outputs).map(ChemicalMekanismCapability.CAP_GAS::of).toArray(GasStack[]::new));
        }
        return this;
    }

    public WeightedRecipeBuilder inputSlurries(Object... inputs) {
        if (Multiblocked.isMekLoaded()) {
            return input(ChemicalMekanismCapability.CAP_SLURRY, Arrays.stream(inputs).map(ChemicalMekanismCapability.CAP_SLURRY::of).toArray(SlurryStack[]::new));
        }
        return this;
    }

    public WeightedRecipeBuilder outputSlurries(Object... outputs) {
        if (Multiblocked.isMekLoaded()) {
            return output(ChemicalMekanismCapability.CAP_SLURRY, Arrays.stream(outputs).map(ChemicalMekanismCapability.CAP_SLURRY::of).toArray(SlurryStack[]::new));
        }
        return this;
    }

    public WeightedRecipeBuilder inputInfusions(Object... inputs) {
        if (Multiblocked.isMekLoaded()) {
            return input(ChemicalMekanismCapability.CAP_INFUSE, Arrays.stream(inputs).map(ChemicalMekanismCapability.CAP_INFUSE::of).toArray(InfusionStack[]::new));
        }
        return this;
    }

    public WeightedRecipeBuilder outputInfusions(Object... outputs) {
        if (Multiblocked.isMekLoaded()) {
            return output(ChemicalMekanismCapability.CAP_INFUSE, Arrays.stream(outputs).map(ChemicalMekanismCapability.CAP_INFUSE::of).toArray(InfusionStack[]::new));
        }
        return this;
    }

    public WeightedRecipeBuilder inputPigments(Object... inputs) {
        if (Multiblocked.isMekLoaded()) {
            return input(ChemicalMekanismCapability.CAP_PIGMENT, Arrays.stream(inputs).map(ChemicalMekanismCapability.CAP_PIGMENT::of).toArray(PigmentStack[]::new));
        }
        return this;
    }

    public WeightedRecipeBuilder outputPigments(Object... outputs) {
        if (Multiblocked.isMekLoaded()) {
            return output(ChemicalMekanismCapability.CAP_PIGMENT, Arrays.stream(outputs).map(ChemicalMekanismCapability.CAP_PIGMENT::of).toArray(PigmentStack[]::new));
        }
        return this;
    }

    public WeightedRecipeBuilder inputMana(int mana) {
        if (Multiblocked.isBotLoaded()) {
            return input(ManaBotaniaCapability.CAP, mana);
        }
        return this;
    }

    public WeightedRecipeBuilder outputMana(int mana) {
        if (Multiblocked.isBotLoaded()) {
            return output(ManaBotaniaCapability.CAP, mana);
        }
        return this;
    }
    public WeightedRecipeBuilder inputEntities(EntityIngredient... inputs) {
        return input(EntityMultiblockCapability.CAP, inputs);
    }

    public WeightedRecipeBuilder outputEntities(EntityIngredient... outputs) {
        return output(EntityMultiblockCapability.CAP, outputs);
    }

    public WeightedRecipeBuilder inputCreate(float capacity) {
        if (Multiblocked.isCreateLoaded()) {
            return input(CreateStressCapacityCapability.CAP, capacity);
        }
        return this;
    }

    public WeightedRecipeBuilder outputCreate(float capacity) {
        if (Multiblocked.isCreateLoaded()) {
            return output(CreateStressCapacityCapability.CAP, capacity);
        }
        return this;
    }

    // conditions
    public WeightedRecipeBuilder dimension(ResourceLocation dimension, boolean reverse) {
        return addCondition(new DimensionCondition(dimension).setReverse(reverse));
    }

    public WeightedRecipeBuilder dimension(ResourceLocation dimension) {
        return dimension(dimension, false);
    }

    public WeightedRecipeBuilder biome(ResourceLocation biome, boolean reverse) {
        return addCondition(new BiomeCondition(biome).setReverse(reverse));
    }

    public WeightedRecipeBuilder biome(ResourceLocation biome) {
        return biome(biome, false);
    }

    public WeightedRecipeBuilder rain(float level, boolean reverse) {
        return addCondition(new RainingCondition(level).setReverse(reverse));
    }

    public WeightedRecipeBuilder rain(float level) {
        return rain(level, false);
    }

    public WeightedRecipeBuilder thunder(float level, boolean reverse) {
        return addCondition(new ThunderCondition(level).setReverse(reverse));
    }

    public WeightedRecipeBuilder thunder(float level) {
        return thunder(level, false);
    }

    public WeightedRecipeBuilder posY(int min, int max, boolean reverse) {
        return addCondition(new PositionYCondition(min, max).setReverse(reverse));
    }

    public WeightedRecipeBuilder posY(int min, int max) {
        return posY(min, max, false);
    }

    public WeightedRecipeBuilder block(BlockState blockState, int count, boolean reverse) {
        return addCondition(new BlockCondition(blockState, count).setReverse(reverse));
    }

    public WeightedRecipeBuilder block(BlockState blockState, int count) {
        return block(blockState, count, false);
    }

    public Recipe build() {
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> inputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.inputBuilder.entrySet()) {
            inputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> outputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.outputBuilder.entrySet()) {
            outputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickInputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickInputBuilder.entrySet()) {
            tickInputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickOutputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickOutputBuilder.entrySet()) {
            tickOutputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        return new Recipe(fixedName == null ? UUID.randomUUID().toString() : fixedName, inputBuilder.build(), outputBuilder.build(), tickInputBuilder.build(), tickOutputBuilder.build(), ImmutableList.copyOf(conditions), data.isEmpty() ? Recipe.EMPTY : ImmutableMap.copyOf(data), text, duration);
    }

    public WeightedRandomRecipe buildWeighted() {
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> inputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.inputBuilder.entrySet()) {
            inputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> outputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.outputBuilder.entrySet()) {
            outputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickInputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickInputBuilder.entrySet()) {
            tickInputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickOutputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickOutputBuilder.entrySet()) {
            tickOutputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        return new WeightedRandomRecipe(fixedName == null ? UUID.randomUUID().toString() : fixedName, inputBuilder.build(), outputBuilder.build(), tickInputBuilder.build(), tickOutputBuilder.build(), ImmutableList.copyOf(conditions), data.isEmpty() ? Recipe.EMPTY : ImmutableMap.copyOf(data), text, duration);
    }

    public WeightedRecipeBuilder addCompanionOutput(EntityType<?extends AbstractEntityCompanion> type) {

        return this.chance(type.create(new DummyWorld()).getRarity().getWeight()).output(CompanionMultiblockCapability.CAP, EntityIngredientCompanions.of(type)).chance(1F);
    }

    public void buildAndRegisterWeighted() {
        buildAndRegisterWeighted(false);
    }

    public void buildAndRegisterWeighted(boolean isFuel) {
        if (isFuel) {
            recipeMap.addFuelRecipe(buildWeighted());
        } else {
            recipeMap.addRecipe(buildWeighted());
        }
    }


    public RiftwayRecipes buildRiftWay() {
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> inputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.inputBuilder.entrySet()) {
            inputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> outputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.outputBuilder.entrySet()) {
            outputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickInputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickInputBuilder.entrySet()) {
            tickInputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickOutputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickOutputBuilder.entrySet()) {
            tickOutputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        return new RiftwayRecipes(fixedName == null ? UUID.randomUUID().toString() : fixedName, inputBuilder.build(), outputBuilder.build(), tickInputBuilder.build(), tickOutputBuilder.build(), ImmutableList.copyOf(conditions), data.isEmpty() ? Recipe.EMPTY : ImmutableMap.copyOf(data), text, duration);
    }

    public void buildAndRegisterRiftway() {
        buildAndRegisterRiftway(false);
    }

    public void buildAndRegisterRiftway(boolean isFuel) {
        if (isFuel) {
            recipeMap.addFuelRecipe(buildRiftWay());
        } else {
            recipeMap.addRecipe(buildRiftWay());
        }
    }
}
