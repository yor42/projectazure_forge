package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.yor42.projectazure.Main.PA_RESOURCES;

public class ItemCraftTool extends Item {

    public ItemCraftTool(final int Durability){
        this(new Item.Properties().tab(PA_RESOURCES).durability(Durability));
    }


    public ItemCraftTool(Properties properties) {
        super(properties);
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
