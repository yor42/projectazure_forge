package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
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
        return (!this.entity.hasPayload() || this.entity.getTarget() == null || this.entity.getMaxOperativeTick() - this.entity.tickCount <200 || !this.entity.getTarget().isAlive() || this.entity.isReturningToOwner()) && this.entity.getOwner() instanceof EntityKansenAircraftCarrier;
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
            this.entity.setReturningtoOwner();
            if(this.entity.distanceToSqr(this.entity.getOwner())<4F) {
                if (((EntityKansenAircraftCarrier) this.entity.getOwner()).hasRigging()) {
                    IItemHandler Hanger = ((EntityKansenAircraftCarrier) this.entity.getOwner()).getHanger();
                    if(Hanger != null){
                        boolean SuccessfullyReturned = false;
                        ItemStack PlaneStack = serializePlane(this.entity);
                        for(int i = 0; i<Hanger.getSlots(); i++){
                            if(Hanger.insertItem(i, PlaneStack, true) == ItemStack.EMPTY) {
                                Hanger.insertItem(i, PlaneStack, false);
                                SuccessfullyReturned = true;
                                this.entity.remove();
                                break;
                            }
                        }

                        if(!SuccessfullyReturned){
                            this.entity.startCircling(this.entity.getOwner());
                        }

                    }
                }
            }
        }
    }
}
