package com.yor42.projectazure.gameobject.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;

public class AlloyingRecipe implements IRecipe<IInventory> {

    protected final Ingredient ingredient1, ingredient2;
    protected ItemStack result;
    protected final int processTick, ing1Count, ing2Count;
    protected final ResourceLocation id;

    public AlloyingRecipe(ResourceLocation id, Ingredient ingredient1, int ing1Count, Ingredient ingredient2, int ing2Count, int processTick, ItemStack result) {

        this.id = id;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.result = result;

        this.processTick = processTick;
        this.ing1Count = ing1Count;
        this.ing2Count = ing2Count;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {

        boolean isItemCorrect = (this.ingredient1.test(inv.getStackInSlot(0))||this.ingredient1.test(inv.getStackInSlot(1))) && (this.ingredient1.test(inv.getStackInSlot(0))||this.ingredient1.test(inv.getStackInSlot(1)));
        boolean isItemCountEnough = getIng1ItemCount(inv)>=this.ing1Count && getIng2ItemCount(inv)>=this.ing2Count;

        return isItemCorrect && isItemCountEnough;
    }

    private int getIng1ItemCount(IInventory inventory){
        if(this.ingredient1.test(inventory.getStackInSlot(0))){
            return inventory.getStackInSlot(0).getCount();
        }
        else if(this.ingredient1.test(inventory.getStackInSlot(1))){
            return inventory.getStackInSlot(1).getCount();
        }
        return 0;
    }

    private int getIng2ItemCount(IInventory inventory){
        if(this.ingredient2.test(inventory.getStackInSlot(0))){
            return inventory.getStackInSlot(0).getCount();
        }
        else if(this.ingredient2.test(inventory.getStackInSlot(1))){
            return inventory.getStackInSlot(1).getCount();
        }
        return 0;
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
        return registerRecipes.Serializers.ALLOYING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return registerRecipes.Types.ALLOYING;
    }

    public int getProcessTick() {
        return this.processTick;
    }

    public int getIng1Count() {
        return this.ing1Count;
    }

    public int getIng2Count() {
        return this.ing2Count;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlloyingRecipe>{


        @Override
        public AlloyingRecipe read(ResourceLocation recipeId, JsonObject json) {

            Ingredient ingredient1 = Ingredient.deserialize(json.get("ingredient1"));
            int ing1count = JSONUtils.getInt(json, "ing1count", 1);
            Ingredient ingredient2 = Ingredient.deserialize(json.get("ingredient2"));
            int ing2count = JSONUtils.getInt(json, "ing2count", 1);

            ResourceLocation ItemID = new ResourceLocation(JSONUtils.getString(json, "result"));
            int processtime = JSONUtils.getInt(json, "processtime", 200);
            int resultcount = JSONUtils.getInt(json, "resultcount", 1);

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(ItemID), resultcount);

            return new AlloyingRecipe(recipeId, ingredient1, ing1count, ingredient2, ing2count, processtime, result);
        }

        @Nullable
        @Override
        public AlloyingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {

            Ingredient ingredient1 = Ingredient.read(buffer);
            int ing1count = buffer.readInt();
            Ingredient ingredient2 = Ingredient.read(buffer);
            int ing2count = buffer.readInt();
            int processtick = buffer.readInt();
            ItemStack resultStack = buffer.readItemStack();

            return new AlloyingRecipe(recipeId, ingredient1, ing1count, ingredient2, ing2count, processtick, resultStack);
        }

        @Override
        public void write(PacketBuffer buffer, AlloyingRecipe recipe) {
            recipe.ingredient1.write(buffer);
            buffer.writeInt(recipe.ing1Count);
            recipe.ingredient2.write(buffer);
            buffer.writeInt(recipe.ing2Count);
            buffer.writeInt(recipe.processTick);
            buffer.writeItemStack(recipe.result);
        }
    }
}
