package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.ItemTier;

import javax.annotation.Nullable;

import static com.yor42.projectazure.Main.PA_RESOURCES;

import net.minecraft.world.item.Item.Properties;

public class ItemCraftTool extends Item {

    private final byte tier;

    public ItemCraftTool(final int Durability){
        this(new Item.Properties().tab(PA_RESOURCES).durability(Durability), 0);
    }

    public ItemCraftTool(final int Durability ,Tier tier){
        this(new Item.Properties().tab(PA_RESOURCES).durability(Durability), tier.getLevel());
    }

    public byte getTier(){
        return this.tier;
    }

    public ItemCraftTool(Properties properties, int tier) {
        super(properties);
        this.tier = (byte) tier;
    }



    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack stack = itemStack.copy();
        if (!stack.hurt(1, MathUtil.getRand(), null)) {
            return stack;
        }
        else return ItemStack.EMPTY;
    }
}
