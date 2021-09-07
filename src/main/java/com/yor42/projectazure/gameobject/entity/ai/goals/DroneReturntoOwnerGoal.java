package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityDrone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;

public class DroneReturntoOwnerGoal extends Goal {

    AbstractEntityDrone entity;
    public DroneReturntoOwnerGoal(AbstractEntityDrone entityIn){
        this.entity = entityIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {

        if(!this.entity.getOwner().isPresent()){
            return false;
        }

        boolean outofammo = this.entity.getammo() == 0;
        boolean needRepair = this.entity.getHealth()<3;
        boolean returningtoOwner = this.entity.isReturningToOwner();

        return outofammo || needRepair|| returningtoOwner;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        if(this.entity.getOwner().isPresent()) {
            this.entity.setReturningtoOwner(true);
            this.entity.getNavigator().clearPath();
            this.entity.getNavigator().tryMoveToEntityLiving(this.entity.getOwner().get(), 1);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entity.getOwner().isPresent()) {
            Entity owner = this.entity.getOwner().get();
            if(this.entity.ticksExisted % 30 == 0){
                this.entity.getNavigator().tryMoveToEntityLiving(owner, 1);
            }

            if(this.entity.getDistance(owner)>3){
                ItemStack stack = this.entity.turnPlanetoItemStack();
                boolean ItemInserted = false;
                if(owner instanceof PlayerEntity){
                    for(int i = 0; i< ((PlayerEntity) owner).inventory.getSizeInventory(); i++){
                        if(((PlayerEntity) owner).inventory.isItemValidForSlot(i, stack)){
                            ((PlayerEntity) owner).inventory.setInventorySlotContents(i, stack);
                            this.entity.remove();
                            ItemInserted = true;
                            break;
                        }
                    }
                }
                else if(owner instanceof AbstractEntityCompanion){
                    for(int k = 0; k<((AbstractEntityCompanion)owner).getSkillItemCount(); k++){
                        int index = 12+k;
                        if(((AbstractEntityCompanion) owner).getInventory().isItemValid(index, stack)){
                            ((AbstractEntityCompanion) owner).getInventory().setStackInSlot(index, stack);
                            this.entity.remove();
                            ItemInserted = true;
                            break;
                        }
                    }
                }
                if(!ItemInserted){
                    this.entity.serializePlane(this.entity.getEntityWorld());
                }
            }
        }
    }
}
