package com.yor42.projectazure.gameobject.crafting.recipes;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlloyingRecipe implements Recipe<Container> {


    public final List<Pair<Ingredient, Byte>> ingredients;
    protected ItemStack result;
    protected final int processTick;
    protected final ResourceLocation id;

    public AlloyingRecipe(ResourceLocation id, int processTick, ItemStack result, List<Pair<Ingredient, Byte>> ingredients) {

        this.id = id;
        this.result = result;
        this.processTick = processTick;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {

        for(Pair<Ingredient, Byte> ingrediententry:this.ingredients){
            boolean found = false;
            for(int i=0; i<inv.getContainerSize(); i++){
                ItemStack stack = inv.getItem(i);
                if(ingrediententry.getFirst().test(stack) && stack.getCount()>ingrediententry.getSecond()){
                    found = true;
                    break;
                }
            }

            if(!found) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(Container inv) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public NonNullList<List<ItemStack>> getIngredientStack() {
        NonNullList<List<ItemStack>> list = NonNullList.create();

        for(Pair<Ingredient, Byte> ingrediententry:this.ingredients){
            List<ItemStack> stacklist = Arrays.asList(ingrediententry.getFirst().getItems());
            list.add(stacklist);
        }


        return list;
    }


    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for(Pair<Ingredient, Byte> ingrediententry:this.ingredients){
            list.add(ingrediententry.getFirst());
        }
        return list;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return registerRecipes.Serializers.ALLOYING.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return registerRecipes.Types.ALLOYING;
    }

    public int getProcessTick() {
        return this.processTick;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AlloyingRecipe>{


        @Override
        public AlloyingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            ImmutableList.Builder<Pair<Ingredient, Byte>> builder = ImmutableList.builder();
            JsonArray input = GsonHelper.getAsJsonArray(json, "materials");
            for(int i = 0; i < input.size(); i++)
            {
                JsonObject itemObject = input.get(i).getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(itemObject.get("item"));

                byte count;
                try {
                    count = GsonHelper.getAsByte(itemObject, "count", (byte) 1);
                }
                catch (JsonSyntaxException e){
                    count = 1;
                }
                builder.add(new Pair<>(ingredient, count));
            }

            ResourceLocation ItemID = new ResourceLocation(GsonHelper.getAsString(json, "result"));
            int processtime = GsonHelper.getAsInt(json, "processtime", 200);
            int resultcount = GsonHelper.getAsByte(json, "resultcount", (byte) 1);

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(ItemID), resultcount);

            return new AlloyingRecipe(recipeId, processtime, result, builder.build());
        }

        @Nullable
        @Override
        public AlloyingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            int processtick = buffer.readInt();
            ItemStack resultStack = buffer.readItem();
            int materials = buffer.readInt();
            List<Pair<Ingredient, Byte>> material = new ArrayList<>();
            for(int i=0; i<materials; i++){
                material.add(new Pair<>(Ingredient.fromNetwork(buffer), buffer.readByte()));
            }

            return new AlloyingRecipe(recipeId, processtick, resultStack, material);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AlloyingRecipe recipe) {
            buffer.writeInt(recipe.processTick);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.ingredients.size());
            for(Pair<Ingredient, Byte> ingrediententry:recipe.ingredients){
                ingrediententry.getFirst().toNetwork(buffer);
                buffer.writeByte(ingrediententry.getSecond());
            }
        }
    }
}
