package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.serializePlane;

public class PlaneReturntoOwnerGoal extends Goal {

    AbstractEntityPlanes entity;
    public PlaneReturntoOwnerGoal(AbstractEntityPlanes entityIn){
        this.entity = entityIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }


    @Override
    public boolean canUse() {
        return (!this.entity.hasPayload() || this.entity.getTarget() == null || this.entity.getMaxOperativeTick() - this.entity.tickCount <200 || !this.entity.getTarget().isAlive()) && this.entity.getOwner() instanceof EntityKansenAircraftCarrier;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entity.getOwner() instanceof EntityKansenAircraftCarrier) {
            this.entity.getNavigation().moveTo(this.entity.getOwner(), 2);
            this.entity.setReturningtoOwner(true);
            if(this.entity.distanceToSqr(this.entity.getOwner())<4F) {
                if (((EntityKansenAircraftCarrier) this.entity.getOwner()).hasRigging()) {
                    IItemHandler Hanger = ((EntityKansenAircraftCarrier) this.entity.getOwner()).getHanger();
                    if(Hanger != null){
                        ItemStack PlaneStack = serializePlane(this.entity);
                        for(int i = 0; i<Hanger.getSlots(); i++){
                            PlaneStack = Hanger.insertItem(i, PlaneStack, false);
                            if(PlaneStack == ItemStack.EMPTY)
                                break;
                        }
                        this.entity.remove();
                    }
                }
            }
        }
    }
}
