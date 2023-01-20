package com.yor42.projectazure.data.recipebuilder;

import com.google.gson.JsonObject;
import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class AlloyingRecipeBuilder {

    private final Item result;
    private final Ingredient ingredient1, ingredient2;
    private final int processingTime, count, ing1count, ing2count;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    public AlloyingRecipeBuilder(Ingredient ingredient1, int ing1Count, Ingredient ingredient2, int ing2Count, IItemProvider result, int count, int processingTime) {
        this.result = result.asItem();
        this.count = count;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.ing1count = ing1Count;
        this.ing2count = ing2Count;
        this.processingTime = processingTime;
    }

    public static AlloyingRecipeBuilder addRecipe(Ingredient ingredient1, int ing1Count, Ingredient ingredient2, int ing2Count, IItemProvider result, int count, int processingTime){
        return new AlloyingRecipeBuilder(ingredient1, ing1Count, ingredient2, ing2Count, result, count, processingTime);
    }

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, Registry.ITEM.getKey(this.result));
    }

    /**
     * Adds a criterion needed to unlock the recipe.
     */
    public AlloyingRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
        this.advancementBuilder.addCriterion(name, criterionIn);
        return this;
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
        this.advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(IRequirementsStrategy.OR);
        consumerIn.accept(new AlloyingRecipeBuilder.Result(id, "", this.ingredient1, this.ing1count, this.ingredient2, this.ing2count, this.result, this.count, this.processingTime, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath()), registerRecipes.Serializers.ALLOYING.get()));
    }

    public static class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient ingredient1, ingredients2;
        private final String group;
        private final Item result;
        private final int cookingTime, outputcount, ing1count, ing2count;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<AlloyingRecipe> serializer;

        public Result(ResourceLocation id, String group, Ingredient ingredient1, int ing1count, Ingredient ingredient2, int ing2count,Item resultIn, int outputcount, int cookingTimeIn, Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn, IRecipeSerializer<AlloyingRecipe> serializerIn){
            this.id = id;
            this.group = group;
            this.ingredient1 = ingredient1;
            this.ingredients2 = ingredient2;
            this.ing1count = ing1count;
            this.ing2count = ing2count;
            this.result = resultIn;
            this.outputcount = outputcount;
            this.cookingTime = cookingTimeIn;
            this.advancementBuilder = advancementBuilderIn;
            this.advancementId = advancementIdIn;
            this.serializer = serializerIn;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.add("ingredient1", this.ingredient1.toJson());
            json.addProperty("ing1count", this.ing1count);
            json.add("ingredient2", this.ingredients2.toJson());
            json.addProperty("ing2count", this.ing2count);
            json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
            json.addProperty("resultcount", this.outputcount);
            json.addProperty("processtime", this.cookingTime);

        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getType() {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancementBuilder.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }

}
