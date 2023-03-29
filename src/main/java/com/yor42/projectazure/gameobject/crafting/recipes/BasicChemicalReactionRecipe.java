package com.yor42.projectazure.gameobject.crafting.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.BASICCHEMICALREACTION;
import static com.yor42.projectazure.setup.register.registerRecipes.Types.BASIC_CHEMICAL_REACTION;

public class BasicChemicalReactionRecipe implements Recipe<Container>, Predicate<ItemStack> {

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
    public boolean matches(Container p_77569_1_, Level p_77569_2_) {
        return true;
    }

    @Override
    public ItemStack assemble(Container p_77572_1_) {
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
    public RecipeSerializer<?> getSerializer() {
        return BASICCHEMICALREACTION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BASIC_CHEMICAL_REACTION.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = Recipe.super.getIngredients();
        list.add(this.itemInput);
        return list;
    }

    @Nonnull
    public NonNullList<ItemStack> getIngredientStack() {
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(List.of(this.itemInput.getItems()));
        return list;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BasicChemicalReactionRecipe>{

        //Thank you korewa_Li for debug
        @Override
        public BasicChemicalReactionRecipe fromJson(@Nonnull ResourceLocation recipeId, JsonObject json) {


            JsonObject output = json.get("output").getAsJsonObject();
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(output, "fluid"));
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
        public BasicChemicalReactionRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            FluidStack output = buffer.readFluidStack();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int time = buffer.readInt();
            return new BasicChemicalReactionRecipe(recipeId, output, ingredient, time);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, BasicChemicalReactionRecipe recipe) {
            buffer.writeFluidStack(recipe.output);
            recipe.itemInput.toNetwork(buffer);
            buffer.writeInt(recipe.getProcesstime());
        }
    }

}
