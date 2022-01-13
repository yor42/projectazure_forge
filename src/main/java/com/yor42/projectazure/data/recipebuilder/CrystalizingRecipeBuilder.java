package com.yor42.projectazure.data.recipebuilder;

import com.google.gson.JsonObject;
import com.yor42.projectazure.gameobject.crafting.CrystalizingRecipe;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CrystalizingRecipeBuilder {

    private final Item result;
    private final Ingredient seed;
    private final Fluid solution;
    private final int growthTime;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    public CrystalizingRecipeBuilder(IItemProvider result, Ingredient seed, Fluid solution, int growthTime){
        this.result = result.asItem();
        this.seed = seed;
        this.solution = solution;
        this.growthTime = growthTime;
    }

    public static CrystalizingRecipeBuilder addRecipe(IItemProvider result, Ingredient seed, Fluid solution, int growthTime){
        return new CrystalizingRecipeBuilder(result, seed, solution, growthTime);
    }

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, Registry.ITEM.getKey(this.result));
    }

    public CrystalizingRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
        this.advancementBuilder.addCriterion(name, criterionIn);
        return this;
    }

    private void validate(ResourceLocation id) {
        if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        ResourceLocation resourcelocation1 = new ResourceLocation(save);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, resourcelocation1);
        }
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate(id);
        this.advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(IRequirementsStrategy.OR);
        consumerIn.accept(new CrystalizingRecipeBuilder.Result(id, "", this.seed, this.result, this.growthTime, this.solution, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath()), registerRecipes.Serializers.CRYSTALIZING.get()));
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient seed;
        private final Fluid solution;
        private final String group;
        private final Item result;
        private final int GrowthTime;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<CrystalizingRecipe> serializer;

        public Result(ResourceLocation idIn, String groupIn, Ingredient seedIn, Item resultIn, int growthTimeIn, Fluid fluidIn, Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn, IRecipeSerializer<CrystalizingRecipe> serializerIn){
            this.id = idIn;
            this.group = groupIn;
            this.seed = seedIn;
            this.result = resultIn;
            this.GrowthTime = growthTimeIn;
            this.solution = fluidIn;
            this.advancementBuilder = advancementBuilderIn;
            this.advancementId = advancementIdIn;
            this.serializer = serializerIn;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.add("seed", this.seed.toJson());
            json.addProperty("solution", ForgeRegistries.FLUIDS.getKey(this.solution).toString());
            json.addProperty("growthtime", this.GrowthTime);
            json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getType() {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancementBuilder.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }

}
