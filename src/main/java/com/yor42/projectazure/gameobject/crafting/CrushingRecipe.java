package com.yor42.projectazure.gameobject.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yor42.projectazure.libs.utils.ItemStackWithChance;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/*
 * This class is distributed as part of the Ex Nihilo Sequentia Mod.
 * Get the Source Code in github:
 * https://github.com/NovaMachina-Mods/ExNihiloSequentia
 *
 * Ex Nihilo Sequentia is Open Source and distributed under the
 * CC BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en
 */
import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.CRUSHING;

public class CrushingRecipe implements IRecipe<IInventory> {
    public static final CrushingRecipe EMPTY = new CrushingRecipe(new ResourceLocation("empty"), Ingredient.EMPTY, new ArrayList<>());
    private Ingredient input;
    private final List<ItemStackWithChance> output;
    protected final ResourceLocation id;

    public CrushingRecipe(ResourceLocation id, Ingredient input, List<ItemStackWithChance> output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(IInventory inventory, World worldin) {
        return false;
    }

    public void addOutput(ItemStack output) {
        this.output.add(new ItemStackWithChance(output, 1.0F));
    }

    public void addOutput(ItemStack output, float chance) {
        this.output.add(new ItemStackWithChance(output, chance));
    }

    @Override
    public ItemStack assemble(IInventory p_77572_1_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return false;
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
    public IRecipeSerializer<?> getSerializer() {
        return CRUSHING.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public Ingredient getInput() {
        return input;
    }

    public void setInput(Ingredient input) {
        this.input = input;
    }

    public List<ItemStackWithChance> getOutput() {
        return output;
    }

    public List<ItemStack> getOutputsWithoutChance() {
        List<ItemStack> returnList = new ArrayList<>();
        output.forEach(stack -> returnList.add(stack.getStack()));
        return returnList;
    }

    @Nonnull
    @Override
    public IRecipeType<?> getType() {
        return registerRecipes.Types.CRUSHING;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrushingRecipe>{
        //ReadFromJson
        @Nonnull
        @Override
        public CrushingRecipe fromJson(@Nonnull ResourceLocation recipeId, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            JsonArray results = json.getAsJsonArray("results");
            List<ItemStackWithChance> output = new ArrayList<>(results.size());
            for (int i = 0; i < results.size(); i++) {
                output.add(ItemStackWithChance.deserialize(results.get(i)));
            }
            return new CrushingRecipe(recipeId, input, output);
        }
        //ReadFromNetwork
        @Nullable
        @Override
        public CrushingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            int outputCount = buffer.readInt();
            List<ItemStackWithChance> output = new ArrayList<>(outputCount);
            for (int i = 0; i < outputCount; i++) {
                output.add(ItemStackWithChance.read(buffer));
            }

            return new CrushingRecipe(recipeId, input, output);
        }

        @Override
        public void toNetwork(@Nonnull PacketBuffer buffer, CrushingRecipe recipe) {
            recipe.getInput().toNetwork(buffer);
            buffer.writeInt(recipe.getOutput().size());
            for (ItemStackWithChance stack : recipe.getOutput()) {
                stack.write(buffer);
            }
        }
    }
}
