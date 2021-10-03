package com.yor42.projectazure.gameobject.capability.multiinv;

import net.minecraftforge.items.ItemStackHandler;

public interface IMultiInventory {

    int getInventoryCount();
    ItemStackHandler getInventory(int index);

    class Impl implements IMultiInventory {

        MultiInvStackHandler[] inventories;

        public Impl(MultiInvStackHandler... inventories) {
            this.inventories = inventories;
        }

        @Override
        public int getInventoryCount() {
            return inventories.length;
        }

        @Override
        public ItemStackHandler getInventory(int index) {
            if (index < 0 || index >= inventories.length) {
                throw new IllegalArgumentException(String.format("Argument 'index' out of bounds, value %d", index));
            }

            return inventories[index];
        }
    }

}
