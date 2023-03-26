package com.yor42.projectazure.gameobject.crafting.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yor42.projectazure.libs.utils.ItemStackWithChance;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.CRUSHING;

public class CrushingRecipe implements Recipe<Container> {
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
    public boolean matches(Container inventory, Level worldin) {
        return false;
    }

    public void addOutput(ItemStack output) {
        this.output.add(new ItemStackWithChance(output, 1.0F));
    }

    public void addOutput(ItemStack output, float chance) {
        this.output.add(new ItemStackWithChance(output, chance));
    }

    @Override
    public ItemStack assemble(Container p_77572_1_) {
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
    public RecipeSerializer<?> getSerializer() {
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
    public RecipeType<?> getType() {
        return registerRecipes.Types.CRUSHING;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CrushingRecipe>{
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
        public CrushingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            int outputCount = buffer.readInt();
            List<ItemStackWithChance> output = new ArrayList<>(outputCount);
            for (int i = 0; i < outputCount; i++) {
                output.add(ItemStackWithChance.read(buffer));
            }

            return new CrushingRecipe(recipeId, input, output);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, CrushingRecipe recipe) {
            recipe.getInput().toNetwork(buffer);
            buffer.writeInt(recipe.getOutput().size());
            for (ItemStackWithChance stack : recipe.getOutput()) {
                stack.write(buffer);
            }
        }
    }
}
