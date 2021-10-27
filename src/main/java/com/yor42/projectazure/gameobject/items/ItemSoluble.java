package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.interfaces.ISoluble;
import net.minecraft.item.Item;

public abstract class ItemSoluble extends Item implements ISoluble {
    public ItemSoluble(Properties properties) {
        super(properties);
    }

    @Override
    public int SolubleAmount() {
        return 1000;
    }
}
