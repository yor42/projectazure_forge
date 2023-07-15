package com.yor42.projectazure.gameobject.capability.multiinv;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.libs.enums;

import java.util.List;

public interface IMultiInventory {

    int getInventoryCount();

    MultiInvStackHandler getInventory(enums.SLOTTYPE slottype);

    ImmutableList<MultiInvStackHandler> getAllInvs();

    ImmutableList<enums.SLOTTYPE> getAllKeys();

    class Impl implements IMultiInventory {

        ImmutableMap<enums.SLOTTYPE, MultiInvStackHandler> inventories;

        public Impl(ImmutableMap<enums.SLOTTYPE, MultiInvStackHandler> inventories) {
            this.inventories = inventories;
        }

        @Override
        public int getInventoryCount() {
            return inventories.size();
        }

        @Override
        public MultiInvStackHandler getInventory(enums.SLOTTYPE type) {
            if (!inventories.containsKey(type)) {
                throw new IllegalArgumentException(String.format("Argument 'type' out of bounds, value %s", type.getName()));
            }

            return inventories.get(type);
        }

        @Override
        public ImmutableList<MultiInvStackHandler> getAllInvs() {
            return inventories.values().asList();
        }

        @Override
        public ImmutableList<enums.SLOTTYPE> getAllKeys() {
            return inventories.keySet().asList();
        }
    }
}
