package com.yor42.projectazure.gameobject.crafting.recipes;

import com.google.gson.JsonObject;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityCrystalGrowthChamber;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
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

import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.CRYSTALIZING;

public class CrystalizingRecipe implements Recipe<TileEntityCrystalGrowthChamber> {

    protected final Fluid solution;
    protected final ItemStack output;
    protected final Ingredient seed;
    protected final int growthTime;
    protected final ResourceLocation id;

    public CrystalizingRecipe(ResourceLocation id, Ingredient seed, Fluid solution, int growthTime, ItemStack result){
        this.solution = solution;
        this.seed = seed;
        this.id = id;
        this.growthTime = growthTime;
        this.output = result;
    }

    @Override
    public boolean matches(@Nonnull TileEntityCrystalGrowthChamber inv, @Nonnull Level worldIn) {
        return this.seed.test(inv.getItem(0)) && this.solution == inv.getSolutionTank().getFluid().getFluid() && inv.getSolutionTank().getFluid().getAmount()>0;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull TileEntityCrystalGrowthChamber inv) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public int getGrowthTime(){
        return this.growthTime;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.seed);
        return nonnulllist;
    }

    public NonNullList<ItemStack> getIngredientStack() {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        nonnulllist.addAll(List.of(this.seed.getItems()));
        return nonnulllist;
    }

    public NonNullList<FluidStack> getFluid() {
        NonNullList<FluidStack> nonnulllist = NonNullList.create();
        nonnulllist.add(new FluidStack(this.solution, this.growthTime));
        return nonnulllist;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRYSTALIZING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return registerRecipes.Types.CRYSTALIZING;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CrystalizingRecipe>{
        @Override
        public CrystalizingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient seed = Ingredient.fromJson(json.get("seed"));
            ResourceLocation FluidID = new ResourceLocation(GsonHelper.getAsString(json, "solution"));
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(FluidID);
            int growthTime = GsonHelper.getAsInt(json, "growthtime", 1800);
            ResourceLocation ItemID = new ResourceLocation(GsonHelper.getAsString(json, "result"));
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(ItemID), 1);
            return new CrystalizingRecipe(recipeId, seed, fluid, growthTime, result);
        }

        @Nullable
        @Override
        public CrystalizingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient seed = Ingredient.fromNetwork(buffer);
            FluidStack stack2Read =buffer.readFluidStack();
            ItemStack result = buffer.readItem();
            Fluid solution = stack2Read.getFluid();
            int processtime = stack2Read.getAmount();
            return new CrystalizingRecipe(recipeId, seed, solution, processtime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrystalizingRecipe recipe) {
            recipe.seed.toNetwork(buffer);
            buffer.writeFluidStack(new FluidStack(recipe.solution, recipe.growthTime));
            buffer.writeItem(recipe.output);
        }
    }
}
