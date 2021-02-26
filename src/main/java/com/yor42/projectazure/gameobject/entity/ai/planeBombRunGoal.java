package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.items.ItemStackHandler;

import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.serializePlane;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.setCurrentDamage;

public class planeBombRunGoal extends Goal {

    private AbstractEntityPlanes entity;

    public planeBombRunGoal(AbstractEntityPlanes host){
        this.entity = host;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {

        if(this.entity.getAttackTarget() == null || !this.entity.getAttackTarget().isAlive() || !this.entity.hasPayload() || this.entity.getMoveHelper().isUpdating() || this.entity.getPlaneType() == enums.PLANE_TYPE.FIGHTER){
            return false;
        }
        else{
            return this.entity.getNavigator().getPathToEntity(this.entity.getAttackTarget(), 0) != null;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.entity.getMoveHelper().isUpdating() && this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isAlive() && this.entity.hasPayload();
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.setOnBombRun(false);
    }

    @Override
    public void startExecuting() {
        LivingEntity target = this.entity.getAttackTarget();
        if(target!= null) {
            this.entity.getMoveHelper().setMoveTo(target.getPosX(), target.getPosY()+target.getEyeHeight()+2F, target.getPosZ(), 1.0);
            this.entity.setOnBombRun(true);
        }
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getAttackTarget();
        if(this.entity.getOwner() != null) {
                if (target != null) {
                    if (this.entity.ticksExisted % 2 == 0) {
                        this.entity.getMoveHelper().setMoveTo(target.getPosX(), target.getPosY() + target.getEyeHeight() + 2F, target.getPosZ(), 1.0);
                    }

                    boolean ShouldDropPayloads;
                    switch (this.entity.getPlaneType()) {
                        default:
                            ShouldDropPayloads = false;
                            break;
                        case DIVE_BOMBER:
                            ShouldDropPayloads = target.getPosX() - 1 < this.entity.getPosX() && this.entity.getPosX() < target.getPosX() + 1 && target.getPosZ() - 1 < this.entity.getPosZ() && this.entity.getPosZ() < target.getPosZ() + 1;
                            break;
                        case TORPEDO_BOMBER:
                            ShouldDropPayloads = this.entity.getDistanceSq(target) < 10F;
                    }

                    if (ShouldDropPayloads && this.entity.hasPayload()) {
                        this.entity.usePayload();
                    }

                }
        }


    }

    private void returnToOwner() {
        if(this.entity.getOwner() instanceof EntityKansenAircraftCarrier) {
            this.entity.getMoveHelper().setMoveTo(this.entity.getOwner().getPosX(), this.entity.getOwner().getPosY(), this.entity.getOwner().getPosZ(), 1.0F);
            if(this.entity.getDistanceSq(this.entity.getOwner())<1.5F) {
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
