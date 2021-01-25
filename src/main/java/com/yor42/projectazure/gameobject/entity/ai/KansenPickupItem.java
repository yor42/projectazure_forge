package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import net.minecraft.entity.ai.goal.Goal;

public class KansenPickupItem extends Goal {

    private final EntityKansenBase host;
    private boolean InventoryHasSpace;

    public KansenPickupItem(EntityKansenBase host) {
        this.host = host;
    }

    @Override
    public boolean shouldExecute() {
        return false;
    }
}
