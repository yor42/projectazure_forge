package com.yor42.projectazure.gameobject.crafting.recipes;

import com.google.gson.JsonObject;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;


public class PressingRecipe implements Recipe<Container> {

    protected final Ingredient ingredient, mold;
    protected final ItemStack result;
    protected final int processTick;
    protected final ResourceLocation id;

    public PressingRecipe(ResourceLocation recipeId , Ingredient ingredient, Ingredient mold, int processTick,ItemStack result) {
        this.id = recipeId;
        this.mold = mold;
        this.ingredient = ingredient;
        this.processTick = processTick;
        this.result = result;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return this.ingredient.test(inv.getItem(0)) && this.mold.test(inv.getItem(1));
    }

    public int getProcessTick(){
        return this.processTick;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        nonnulllist.add(this.mold);
        return nonnulllist;
    }

    @Override
    public ItemStack assemble(Container inv) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    public List<ItemStack> getIngredientStack() {
        return List.of(this.ingredient.getItems());
    }

    public List<ItemStack> getMold() {
        return List.of(this.mold.getItems());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return registerRecipes.Serializers.PRESSING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return registerRecipes.Types.PRESSING;
    }


    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PressingRecipe>{
        //ReadFromJson
        @Override
        public PressingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            Ingredient mold = Ingredient.fromJson(json.get("mold"));
            ResourceLocation ItemID = new ResourceLocation(GsonHelper.getAsString(json, "result"));
            int processtime = GsonHelper.getAsInt(json, "processtime", 200);
            int count = GsonHelper.getAsInt(json, "count", 1);
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(ItemID), count);

            return new PressingRecipe(recipeId, ingredient, mold, processtime, result);
        }
        //ReadFromNetwork
        @Nullable
        @Override
        public PressingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient mold = Ingredient.fromNetwork(buffer);
            int processtime = buffer.readInt();
            ItemStack result = buffer.readItem();
            return new PressingRecipe(recipeId, ingredient, mold, processtime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PressingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            recipe.mold.toNetwork(buffer);
            buffer.writeInt(recipe.processTick);
            buffer.writeItem(recipe.result);
        }
    }
}
