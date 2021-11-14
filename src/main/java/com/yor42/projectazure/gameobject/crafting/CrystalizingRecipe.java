package com.yor42.projectazure.gameobject.crafting;

import com.google.gson.JsonObject;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityCrystalGrowthChamber;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.fluid.Fluid;
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

import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.CRYSTALIZING;

public class CrystalizingRecipe implements IRecipe<TileEntityCrystalGrowthChamber> {

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
    public boolean matches(@Nonnull TileEntityCrystalGrowthChamber inv, @Nonnull World worldIn) {
        return this.seed.test(inv.getStackInSlot(0)) && this.solution == inv.getSolutionTank().getFluid().getFluid() && inv.getSolutionTank().getFluid().getAmount()>0;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull TileEntityCrystalGrowthChamber inv) {
        return this.output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    public int getGrowthTime(){
        return this.growthTime;
    }

    @Override
    public ItemStack getRecipeOutput() {
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
        nonnulllist.add(this.seed.getMatchingStacks()[0]);
        return nonnulllist;
    }

    public NonNullList<FluidStack> getFluid() {
        NonNullList<FluidStack> nonnulllist = NonNullList.create();
        nonnulllist.add(new FluidStack(this.solution, this.growthTime));
        return nonnulllist;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return CRYSTALIZING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return registerRecipes.Types.CRYSTALIZING;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrystalizingRecipe>{
        @Override
        public CrystalizingRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient seed = Ingredient.deserialize(json.get("seed"));
            ResourceLocation FluidID = new ResourceLocation(JSONUtils.getString(json, "solution"));
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(FluidID);
            int growthTime = JSONUtils.getInt(json, "growthtime", 1800);
            ResourceLocation ItemID = new ResourceLocation(JSONUtils.getString(json, "result"));
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(ItemID), 1);
            return new CrystalizingRecipe(recipeId, seed, fluid, growthTime, result);
        }

        @Nullable
        @Override
        public CrystalizingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient seed = Ingredient.read(buffer);
            FluidStack stack2Read =buffer.readFluidStack();
            ItemStack result = buffer.readItemStack();
            Fluid solution = stack2Read.getFluid();
            int processtime = stack2Read.getAmount();
            return new CrystalizingRecipe(recipeId, seed, solution, processtime, result);
        }

        @Override
        public void write(PacketBuffer buffer, CrystalizingRecipe recipe) {
            recipe.seed.write(buffer);
            buffer.writeFluidStack(new FluidStack(recipe.solution, recipe.growthTime));
            buffer.writeItemStack(recipe.output);
        }
    }
}
