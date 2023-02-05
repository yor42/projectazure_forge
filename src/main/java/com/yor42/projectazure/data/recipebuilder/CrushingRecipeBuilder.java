package com.yor42.projectazure.data.recipebuilder;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yor42.projectazure.libs.utils.ItemStackWithChance;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/*
 * This class is distributed as part of the Ex Nihilo Sequentia Mod.
 * Get the Source Code in github:
 * https://github.com/NovaMachina-Mods/ExNihiloSequentia
 *
 * Ex Nihilo Sequentia is Open Source and distributed under the
 * CC BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en
 */
public class CrushingRecipeBuilder implements IFinishedRecipe {

    protected JsonArray conditions = null;
    protected JsonArray inputArray = null;
    protected int inputCount = 0;
    protected int maxInputCount = 1;
    protected int maxOutputCount = 1;
    protected JsonArray outputArray = null;
    protected int outputCount = 0;
    private final List<Consumer<JsonObject>> writerFunctions;
    private ResourceLocation id;

    public CrushingRecipeBuilder(){
        this.writerFunctions = new ArrayList<>();
        setMultipleResults(Integer.MAX_VALUE);
    }

    public CrushingRecipeBuilder addDrop(IItemProvider drop) {
        return addDrop(drop, 1, 1.0F);
    }

    public static CrushingRecipeBuilder builder() {
        return new CrushingRecipeBuilder();
    }


    public CrushingRecipeBuilder addDrop(IItemProvider drop, int count) {
        return addDrop(drop, count, 1.0F);
    }

    public CrushingRecipeBuilder addDrop(IItemProvider drop, float chance) {
        return addDrop(drop, 1, chance);
    }

    public CrushingRecipeBuilder addDrop(IItemProvider drop, int count, float chance) {
        return this.addResult(new ItemStackWithChance(new ItemStack(drop, count), chance));
    }

    public CrushingRecipeBuilder input(Ingredient input) {
        return this.addInput(input);
    }

    protected CrushingRecipeBuilder addInput(ItemStack input) {
        if (inputArray != null) {
            return addMultiInput(serializeItemStack(input));
        } else {
            return addItem("input", input);
        }
    }

    protected CrushingRecipeBuilder addInput(Ingredient input) {
        return addInput("input", input);
    }

    protected CrushingRecipeBuilder addInput(String key, Ingredient input) {
        if (inputArray != null) {
            return addMultiInput(input.toJson());
        } else {
            return addItem(key, input);
        }
    }

    public CrushingRecipeBuilder input(IItemProvider input) {
        return this.input(Ingredient.of(input));
    }

    public CrushingRecipeBuilder input(ITag<Item> input) {
        return this.input(Ingredient.of(input));
    }

    protected CrushingRecipeBuilder addInput(IItemProvider input) {
        return addInput(new ItemStack(input));
    }

    protected CrushingRecipeBuilder addInput(ITag.INamedTag<Item> tag) {
        return addInput(Ingredient.of(tag));
    }

    protected CrushingRecipeBuilder addInput(String id, IItemProvider block) {
        return this.addItem(id, new ItemStack(block));
    }

    protected CrushingRecipeBuilder addResult(ItemStackWithChance itemStack) {
        if (outputArray != null) {
            return addMultiResult(itemStack.serialize());
        } else {
            return addItem("result", itemStack.serialize());
        }
    }

    public CrushingRecipeBuilder setMultipleResults(int maxResultCount) {
        this.outputArray = new JsonArray();
        this.maxOutputCount = maxResultCount;
        return addWriter(jsonObject -> jsonObject.add("results", outputArray));
    }

    protected boolean isComplete() {
        return true;
    }

    private CrushingRecipeBuilder addItem(String key, ItemStack itemStack) {
        Preconditions.checkArgument(!itemStack.isEmpty(), "ItemStack cannot be empty.");
        return addWriter(jsonObj -> jsonObj.add(key, serializeItemStack(itemStack)));
    }

    private CrushingRecipeBuilder addItem(String key, Ingredient ingredient) {
        return addWriter(jsonObj -> jsonObj.add(key, ingredient.toJson()));
    }

    private CrushingRecipeBuilder addItem(String key, JsonElement obj) {
        return addWriter(jsonObj -> jsonObj.add(key, obj));
    }

    public CrushingRecipeBuilder addWriter(Consumer<JsonObject> writer) {
        Preconditions.checkArgument(id == null, "This recipe has already been finalized.");
        this.writerFunctions.add(writer);
        return this;
    }

    private CrushingRecipeBuilder addMultiInput(JsonElement obj) {
        Preconditions.checkArgument(maxInputCount > 1, "This recipe does not support multiple inputs.");
        Preconditions
                .checkArgument(inputCount < maxInputCount, "This recipe can only have " + maxInputCount + "inputs.");
        inputArray.add(obj);
        inputCount++;
        return this;
    }

    private CrushingRecipeBuilder addMultiResult(JsonElement obj) {
        Preconditions.checkArgument(maxOutputCount > 1, "This recipe does not support multiple results.");
        Preconditions
                .checkArgument(outputCount < maxOutputCount, "This recipe can only have " + maxOutputCount + "results.");
        outputArray.add(obj);
        outputCount++;
        return this;
    }

    private JsonObject serializeItemStack(ItemStack itemStack) {
        JsonObject obj = new JsonObject();
        obj.addProperty("item", itemStack.getItem().getRegistryName().toString());
        if (itemStack.getCount() > 1) {
            obj.addProperty("count", itemStack.getCount());
        }
        if (itemStack.hasTag()) {
            obj.addProperty("nbt", itemStack.getTag().toString());
        }
        return obj;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        for (Consumer<JsonObject> writer : this.writerFunctions) {
            writer.accept(json);
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        Preconditions.checkArgument(isComplete(), "This recipe is incomplete.");
        this.id = id;
        consumer.accept(this);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getType() {
        return registerRecipes.Serializers.CRUSHING.get();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return null;
    }
}
