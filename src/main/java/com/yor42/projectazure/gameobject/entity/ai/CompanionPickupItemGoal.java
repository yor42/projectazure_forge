package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;

import java.util.EnumSet;
import java.util.List;

public class CompanionPickupItemGoal extends Goal {

    private final AbstractEntityCompanion host;
    private final float range;
    private List<ItemEntity> list;

    public CompanionPickupItemGoal(AbstractEntityCompanion host) {
        this.host = host;
        this.range = 10;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {

        this.list = this.host.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, this.host.getBoundingBox().grow(this.range));
        return ((this.host.getAttackTarget() == null || this.host.getRevengeTarget() == null) && !this.list.isEmpty() && !(this.host.isSleeping()&&this.host.isEntitySleeping())) && (this.host.getFirstEmptyStack() != -1 || this.host.storeItemStack(list.get(0).getItem()) != -1);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public void tick() {
        if(this.host.ticksExisted *40 == 0){
            this.list = this.host.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, this.host.getBoundingBox().grow(this.range));;
        }

        if(!this.list.isEmpty()) {

                ItemEntity target = null;
                for (ItemEntity itemEntity : this.list) {
                    if (itemEntity != null) {
                        target = itemEntity;
                        break;
                    }
                }
                if (target!=null) {
                    this.host.getNavigator().tryMoveToEntityLiving(target, 1.0F);

                    if (this.host.getDistanceSq(target) < 1.5) {
                        this.host.PickUpItem(target);
                    }
                }
        }
    }
}
