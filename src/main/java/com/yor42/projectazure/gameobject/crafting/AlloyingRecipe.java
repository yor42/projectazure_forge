package com.yor42.projectazure.gameobject.crafting;

import com.google.gson.JsonObject;
import com.yor42.projectazure.Main;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf ;
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

import javax.annotation.Nullable;

public class AlloyingRecipe implements Recipe<Container> {

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
    public boolean matches(Container inv, Level worldIn) {

        boolean isItemCorrect = (this.ingredient1.test(inv.getItem(0))||this.ingredient1.test(inv.getItem(1))) && (this.ingredient1.test(inv.getItem(0))||this.ingredient1.test(inv.getItem(1)));
        boolean isItemCountEnough = getIng1ItemCount(inv)>=this.ing1Count && getIng2ItemCount(inv)>=this.ing2Count;

        return isItemCorrect && isItemCountEnough;
    }

    private int getIng1ItemCount(Container inventory){
        if(this.ingredient1.test(inventory.getItem(0))){
            return inventory.getItem(0).getCount();
        }
        else if(this.ingredient1.test(inventory.getItem(1))){
            return inventory.getItem(1).getCount();
        }
        return 0;
    }

    private int getIng2ItemCount(Container inventory){
        if(this.ingredient2.test(inventory.getItem(0))){
            return inventory.getItem(0).getCount();
        }
        else if(this.ingredient2.test(inventory.getItem(1))){
            return inventory.getItem(1).getCount();
        }
        return 0;
    }

    @Override
    public ItemStack assemble(Container p_44001_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public NonNullList<ItemStack> getIngredientStack() {
        NonNullList<ItemStack> list = NonNullList.create();

        ItemStack stack1 = this.ingredient1.getItems()[0];
        stack1.setCount(this.ing1Count);

        ItemStack stack2 = this.ingredient2.getItems()[0];
        stack2.setCount(this.ing2Count);

        list.add(stack1);
        list.add(stack2);

        return list;
    }


    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(ingredient1);
        list.add(ingredient2);
        return list;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Main.ALLOYING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE.Instance;
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

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AlloyingRecipe>{


        @Override
        public AlloyingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            Ingredient ingredient1 = Ingredient.fromJson(json.get("ingredient1"));
            int ing1count = GsonHelper.getAsInt(json, "ing1count", 1);
            Ingredient ingredient2 = Ingredient.fromJson(json.get("ingredient2"));
            int ing2count = GsonHelper.getAsInt(json, "ing2count", 1);

            ResourceLocation ItemID = new ResourceLocation(GsonHelper.getAsString(json, "result"));
            int processtime = GsonHelper.getAsInt(json, "processtime", 200);
            int resultcount = GsonHelper.getAsInt(json, "resultcount", 1);

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(ItemID), resultcount);

            return new AlloyingRecipe(recipeId, ingredient1, ing1count, ingredient2, ing2count, processtime, result);
        }

        @Nullable
        @Override
        public AlloyingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf  buffer) {

            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            int ing1count = buffer.readInt();
            Ingredient ingredient2 = Ingredient.fromNetwork(buffer);
            int ing2count = buffer.readInt();
            int processtick = buffer.readInt();
            ItemStack resultStack = buffer.readItem();

            return new AlloyingRecipe(recipeId, ingredient1, ing1count, ingredient2, ing2count, processtick, resultStack);
        }

        @Override
        public void toNetwork(FriendlyByteBuf  buffer, AlloyingRecipe recipe) {
            recipe.ingredient1.toNetwork(buffer);
            buffer.writeInt(recipe.ing1Count);
            recipe.ingredient2.toNetwork(buffer);
            buffer.writeInt(recipe.ing2Count);
            buffer.writeInt(recipe.processTick);
            buffer.writeItem(recipe.result);
        }
    }

    public static class TYPE implements RecipeType<AlloyingRecipe> {
        private TYPE() {}
        public static final TYPE Instance = new TYPE();
        public static final String ID = "alloying";
    }
}
