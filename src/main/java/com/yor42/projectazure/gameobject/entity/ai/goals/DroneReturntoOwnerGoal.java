package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityFollowingDrone;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class DroneReturntoOwnerGoal extends Goal {

    AbstractEntityFollowingDrone entity;
    public DroneReturntoOwnerGoal(AbstractEntityFollowingDrone entityIn){
        this.entity = entityIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {

        if(!this.entity.getOwner().isPresent()){
            return false;
        }

        boolean outofammo = this.entity.getammo() == 0;
        boolean needRepair = this.entity.getHealth()<3;
        boolean returningtoOwner = this.entity.isReturningToOwner();

        return outofammo || needRepair|| returningtoOwner;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        super.start();
        if(this.entity.getOwner().isPresent()) {
            this.entity.setReturningtoOwner(true);
            this.entity.getNavigation().stop();
            this.entity.getNavigation().moveTo(this.entity.getOwner().get(), 1);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entity.getOwner().isPresent()) {
            Entity owner = this.entity.getOwner().get();
            if(this.entity.tickCount % 30 == 0){
                this.entity.getNavigation().moveTo(owner, 1);
            }

            if(this.entity.distanceTo(owner)>3){
                ItemStack stack = this.entity.turnPlanetoItemStack();
                boolean ItemInserted = false;
                if(owner instanceof Player){
                    for(int i = 0; i< ((Player) owner).inventory.getContainerSize(); i++){
                        if(((Player) owner).inventory.canPlaceItem(i, stack)){
                            ((Player) owner).inventory.setItem(i, stack);
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
                    this.entity.serializePlane(this.entity.getCommandSenderWorld());
                }
            }
        }
    }
}
