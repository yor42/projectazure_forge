package com.yor42.projectazure.libs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Predicate;

public class ItemFilterEntry implements Predicate<ItemStack> {

    @Nonnull
    private final FilterType item;
    @Nullable
    private final CompoundTag tag;
    private int min, max;

    @Nonnull
    public static ItemFilterEntry createTagFromItemID(String string){
        FilterType type = new FilterType(ItemTypes.ITEM, string);

        return new ItemFilterEntry(type, null, 0, 64);
    }
    /*
    @Nonnull
    public static ItemFilterEntry createTagFromTag(String string){
        FilterType type = new FilterType(ItemTypes.TAG, string);
        return new ItemFilterEntry(type, null, 0, 64);
    }

     */


    private ItemFilterEntry(@Nonnull FilterType item, @Nullable CompoundTag tag, int min, int max) {
        this.item = item;
        this.tag = tag;
        this.min = min;
        this.max = max;
    }
    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMaxCount() {
        return max;
    }

    @Override
    public boolean test(ItemStack itemStack) {

        if(!this.item.test(itemStack)){
            return false;
        }
        if(itemStack.getCount()<this.min){
            return false;
        }
        return this.tag == null || itemStack.getOrCreateTag() == this.tag;
    }

    public CompoundTag serializeNBT(){
        CompoundTag compound = new CompoundTag();
        compound.put("item", this.item.serializeNBT());
        compound.putInt("max", this.max);
        compound.putInt("min", this.min);
        if(this.tag != null) {
            compound.put("tag", this.tag);
        }
        return compound;
    }

    public static ItemFilterEntry deserializeNBT(CompoundTag compound){
        FilterType type = FilterType.DeserializeNBT(compound.getCompound("item"));
        int max = compound.getInt("max");
        int min = compound.getInt("min");
        CompoundTag tag = null;
        if(compound.contains("tag")){
            compound.getCompound("tag");
        }
        return new ItemFilterEntry(type, tag, min, max);
    }


    private static class FilterType implements Predicate<ItemStack>{
        private final String ID;
        private final ItemTypes type;

        private FilterType(ItemTypes type, String id) {
            ID = id;
            this.type = type;
        }

        private Ingredient asIngredient(){
            return this.type.ID2Ingredient(this.ID);
        }

        public CompoundTag serializeNBT(){
            CompoundTag compound = new CompoundTag();
            compound.putString("Type", this.type.name());
            compound.putString("id", this.ID);
            return compound;
        }

        public static FilterType DeserializeNBT(CompoundTag compound){
            ItemTypes type = ItemTypes.valueOf(compound.getString("Type"));
            String ID = compound.getString("id");
            return new FilterType(type, ID);
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return this.asIngredient().test(itemStack);
        }
    }

    public enum ItemTypes{

        ITEM((id)-> Ingredient.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id))));
        /*
        TAG((id)->{
            Tag<Item> tag = ItemTags.getAllTags().getTag(new ResourceLocation(id));
            if(tag == null){
                return Ingredient.EMPTY;
            }
            return Ingredient.of(tag);
        });

         */

        private final Function<String, Ingredient> IDtoIngredient;
        ItemTypes(Function<String, Ingredient> test){
            this.IDtoIngredient = test;
        }

        public Ingredient ID2Ingredient(String id){
            return this.IDtoIngredient.apply(id);
        }
    }
}
