package com.yor42.projectazure.data.recipebuilder;

import com.google.gson.JsonObject;
import com.yor42.projectazure.gameobject.crafting.recipes.PressingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PressingRecipeBuilder {

    private final Item result;
    private final Ingredient ingredient, mold;
    private final int processingTime, count;

    public PressingRecipeBuilder(IItemProvider result, Ingredient ingredient, Ingredient mold, int count, int processingTime) {
        this.result = result.asItem();
        this.ingredient = ingredient;
        this.mold = mold;
        this.count = count;
        this.processingTime = processingTime;
    }

    public static PressingRecipeBuilder addRecipe(IItemProvider result, Ingredient ingredient, Ingredient mold, int count, int processingTime){
        return new PressingRecipeBuilder(result, ingredient, mold, count, processingTime);
    }

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result));
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
        ResourceLocation resourcelocation1 = ResourceUtils.ModResourceLocation(save);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, resourcelocation1);
        }
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(new PressingRecipeBuilder.Result(id, "", this.ingredient, this.mold, this.result, this.count, this.processingTime, registerRecipes.Serializers.PRESSING.get()));
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient, mold;
        private final String group;
        private final Item result;
        private final int cookingTime, count;
        private final IRecipeSerializer<PressingRecipe> serializer;

        public Result(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, Ingredient moldIn, Item resultIn, int count, int cookingTimeIn, IRecipeSerializer<PressingRecipe> serializerIn) {
            this.id = idIn;
            this.group = groupIn;
            this.ingredient = ingredientIn;
            this.mold = moldIn;
            this.result = resultIn;
            this.count = count;
            this.cookingTime = cookingTimeIn;
            this.serializer = serializerIn;
        }

        public void serializeRecipeData(@Nonnull JsonObject json) {

            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.add("ingredient", this.ingredient.toJson());
            json.add("mold", this.mold.toJson());
            json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
            json.addProperty("count", this.count);
            json.addProperty("cookingtime", this.cookingTime);
        }

        public IRecipeSerializer<?> getType() {
            return this.serializer;
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getId() {
            return this.id;
        }

        /**
         * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
         */
        @Nullable
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
