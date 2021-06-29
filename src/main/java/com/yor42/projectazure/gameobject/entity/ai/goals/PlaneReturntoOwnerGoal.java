package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.serializePlane;

public class PlaneReturntoOwnerGoal extends Goal {

    AbstractEntityPlanes entity;
    public PlaneReturntoOwnerGoal(AbstractEntityPlanes entityIn){
        this.entity = entityIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }


    @Override
    public boolean shouldExecute() {
        return (!this.entity.hasPayload() || this.entity.getAttackTarget() == null || this.entity.getMaxOperativeTick() - this.entity.ticksExisted <200 || !this.entity.getAttackTarget().isAlive()) && this.entity.getOwner() instanceof EntityKansenAircraftCarrier;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entity.getOwner() instanceof EntityKansenAircraftCarrier) {
            this.entity.getMoveHelper().setMoveTo(this.entity.getOwner().getPosX(), this.entity.getOwner().getPosYEye(), this.entity.getOwner().getPosZ(), 1.2F);
            this.entity.setReturningtoOwner(true);
            if(this.entity.getDistanceSq(this.entity.getOwner())<4F) {
                if (((EntityKansenAircraftCarrier) this.entity.getOwner()).hasRigging()) {
                    ItemStackHandler Hanger = ((EntityKansenAircraftCarrier) this.entity.getOwner()).getHanger();
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
