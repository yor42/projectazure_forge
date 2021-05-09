package com.yor42.projectazure.gameobject.crafting;

import com.google.gson.JsonObject;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;


public class PressingRecipe implements IRecipe<IInventory> {

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
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0)) && this.mold.test(inv.getStackInSlot(1));
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
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return registerRecipes.Serializers.PRESSING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return registerRecipes.Types.PRESSING;
    }


    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PressingRecipe>{
        //ReadFromJson
        @Override
        public PressingRecipe read(ResourceLocation recipeId, JsonObject json) {

            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            Ingredient mold = Ingredient.deserialize(json.get("mold"));
            ResourceLocation ItemID = new ResourceLocation(JSONUtils.getString(json, "result"));
            int processtime = JSONUtils.getInt(json, "processtime", 200);
            int count = JSONUtils.getInt(json, "count", 1);
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(ItemID), count);

            return new PressingRecipe(recipeId, ingredient, mold, processtime, result);
        }
        //ReadFromNetwork
        @Nullable
        @Override
        public PressingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            Ingredient mold = Ingredient.read(buffer);
            int processtime = buffer.readInt();
            ItemStack result = buffer.readItemStack();
            return new PressingRecipe(recipeId, ingredient, mold, processtime, result);
        }

        @Override
        public void write(PacketBuffer buffer, PressingRecipe recipe) {
            recipe.ingredient.write(buffer);
            recipe.mold.write(buffer);
            buffer.writeInt(recipe.processTick);
            buffer.writeItemStack(recipe.result);
        }
    }
}
