package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;

import java.util.EnumSet;
import java.util.List;

public class CompanionPickupItemGoal extends Goal {

    private final AbstractEntityCompanion host;
    private final float range;
    private List<ItemEntity> Lootlist;
    private List<ExperienceOrbEntity> Explist;

    public CompanionPickupItemGoal(AbstractEntityCompanion host) {
        this.host = host;
        this.range = 5;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {

        this.Lootlist = this.host.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, this.host.getBoundingBox().grow(this.range));
        this.Explist = this.host.getEntityWorld().getEntitiesWithinAABB(ExperienceOrbEntity.class, this.host.getBoundingBox().grow(this.range));
        return ((this.host.getAttackTarget() == null || this.host.getRevengeTarget() == null) && this.host.shouldPickupItem() && (!this.Lootlist.isEmpty() || !this.Explist.isEmpty()) && !(this.host.isSleeping()&&this.host.isSitting())) && (this.host.getFirstEmptyStack() != -1 || (!Lootlist.isEmpty() && this.host.storeItemStack(Lootlist.get(0).getItem()) != -1));
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public void tick() {
        if(this.host.ticksExisted % 10 == 0){
            this.Lootlist = this.host.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, this.host.getBoundingBox().grow(this.range));;
            this.Explist = this.host.getEntityWorld().getEntitiesWithinAABB(ExperienceOrbEntity.class, this.host.getBoundingBox().grow(this.range));
        }

        if(!this.Lootlist.isEmpty()) {
                ItemEntity target = null;
                for (ItemEntity itemEntity : this.Lootlist) {
                    if (itemEntity != null) {
                        target = itemEntity;
                        break;
                    }
                }
                if (target!=null) {
                    this.host.getNavigator().tryMoveToEntityLiving(target, 1.0F);

                    if (this.host.getDistanceSq(target) < 1) {
                        this.host.PickUpItem(target);
                    }
                }
        }
        else if(!this.Explist.isEmpty()) {
            ExperienceOrbEntity target = null;
            for (ExperienceOrbEntity orbEntity : this.Explist) {

                if (orbEntity != null) {
                    target = orbEntity;
                    break;
                }
            }
            if (target!=null) {
                this.host.getNavigator().tryMoveToEntityLiving(target, 1.0F);

                if (this.host.getDistanceSq(target) < 1.5) {
                    this.host.pickupExpOrb(target);
                }
            }
        }
    }
}
