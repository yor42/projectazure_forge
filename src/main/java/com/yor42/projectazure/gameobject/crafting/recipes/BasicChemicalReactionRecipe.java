package com.yor42.projectazure.gameobject.crafting.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Predicate;

import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.BASICCHEMICALREACTION;
import static com.yor42.projectazure.setup.register.registerRecipes.Types.BASIC_CHEMICAL_REACTION;

public class BasicChemicalReactionRecipe implements IRecipe<IInventory>, Predicate<ItemStack> {

    private final Ingredient itemInput;
    private final FluidStack output;
    private final ResourceLocation id;
    private final int processtime;

    public BasicChemicalReactionRecipe(@Nonnull ResourceLocation ID, @Nonnull FluidStack output, @Nonnull Ingredient input, int processtime){
        this.id = ID;
        this.output = output;
        this.itemInput = input;
        this.processtime = processtime;
    }

    public int getProcesstime(){
        return this.processtime;
    }

    @Override
    public boolean matches(IInventory p_77569_1_, World p_77569_2_) {
        return true;
    }

    @Override
    public ItemStack assemble(IInventory p_77572_1_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean test(ItemStack stack) {
        return this.itemInput.test(stack);
    }

    public FluidStack getOutput() {
        return output;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BASICCHEMICALREACTION.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return BASIC_CHEMICAL_REACTION;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = IRecipe.super.getIngredients();
        list.add(this.itemInput);
        return list;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BasicChemicalReactionRecipe>{
        @Override
        public BasicChemicalReactionRecipe fromJson(@Nonnull ResourceLocation recipeId, JsonObject json) {


            JsonObject output = json.get("output").getAsJsonObject();
            ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(output, "fluid"));
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
            int amount = output.get("amount").getAsInt();
            FluidStack result = new FluidStack(Objects.requireNonNull(fluid, "Result fluid can not be null"), amount);

            JsonElement input = json.get("input");
            Ingredient ingredient = Ingredient.fromJson(input);
            int processtime = json.get("processtime").getAsInt();


            return new BasicChemicalReactionRecipe(recipeId, result, ingredient, processtime);
        }

        @Nullable
        @Override
        public BasicChemicalReactionRecipe fromNetwork(@Nonnull ResourceLocation recipeId, PacketBuffer buffer) {
            FluidStack output =buffer.readFluidStack();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int time = buffer.readInt();
            return new BasicChemicalReactionRecipe(recipeId, output, ingredient, time);
        }

        @Override
        public void toNetwork(@Nonnull PacketBuffer buffer, BasicChemicalReactionRecipe recipe) {
            recipe.output.writeToPacket(buffer);
            recipe.itemInput.toNetwork(buffer);
            buffer.writeInt(recipe.getProcesstime());
        }
    }

}
