package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.serializePlane;

public class planeBombRunGoal extends Goal {

    private AbstractEntityPlanes entity;

    public planeBombRunGoal(AbstractEntityPlanes host){
        this.entity = host;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isAlive() && this.entity.hasPayload() && !this.entity.getMoveHelper().isUpdating();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.entity.getMoveHelper().isUpdating() && this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isAlive() && this.entity.hasPayload() && this.entity.getEntitySenses().canSee(this.entity.getAttackTarget());
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
            this.entity.setOnBombRun(true);
            this.entity.getNavigator().tryMoveToXYZ(target.getPosX(), target.getPosYEye()+1, target.getPosZ(), 2F);
        }
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getAttackTarget();
        if(this.entity.getOwner() != null) {
                if (target != null) {

                    float bombHeightOffset = this.entity.getPlaneItem().getType() == enums.PLANE_TYPE.FIGHTER? 0:2;

                    if (this.entity.ticksExisted % 2 == 0) {
                        this.entity.getNavigator().tryMoveToXYZ(target.getPosX(), target.getPosYEye()+bombHeightOffset, target.getPosZ(), 2F);
                    }
                    boolean ShouldDropPayloads;
                    switch (this.entity.getPlaneType()) {
                        default:
                            ShouldDropPayloads = false;
                            break;
                        case DIVE_BOMBER:
                            ShouldDropPayloads = target.getPosX() - 1 < this.entity.getPosX() && this.entity.getPosX() < target.getPosX() + 1 && target.getPosZ() - 1 < this.entity.getPosZ() && this.entity.getPosZ() < target.getPosZ() + 1;
                            break;
                        case FIGHTER:
                            ShouldDropPayloads = this.entity.getDistance(target)<2.5;
                            break;
                        case TORPEDO_BOMBER:
                            ShouldDropPayloads = this.entity.getDistance(target) < 8F;
                            break;
                    }

                    if (ShouldDropPayloads && this.entity.hasPayload()) {
                        this.entity.usePayload(target);
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
