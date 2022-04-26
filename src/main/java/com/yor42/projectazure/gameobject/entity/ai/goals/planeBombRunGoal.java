package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.serializePlane;

public class planeBombRunGoal extends Goal {

    private AbstractEntityPlanes entity;

    public planeBombRunGoal(AbstractEntityPlanes host){
        this.entity = host;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && this.entity.getTarget().isAlive() && this.entity.hasPayload() && !this.entity.getMoveControl().hasWanted();
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getMoveControl().hasWanted() && this.entity.getTarget() != null && this.entity.getTarget().isAlive() && this.entity.hasPayload() && this.entity.getSensing().canSee(this.entity.getTarget());
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setOnBombRun(false);
    }

    @Override
    public void start() {
        LivingEntity target = this.entity.getTarget();
        if(target!= null) {
            this.entity.setOnBombRun(true);
            this.entity.getNavigation().moveTo(target.getX(), target.getEyeY()+1, target.getZ(), 2F);
        }
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        if(this.entity.getOwner() != null) {
                if (target != null) {

                    float bombHeightOffset = this.entity.getPlaneItem().getType() == enums.PLANE_TYPE.FIGHTER? 0:2;

                    if (this.entity.tickCount % 2 == 0) {
                        this.entity.getNavigation().moveTo(target.getX(), target.getEyeY()+bombHeightOffset, target.getZ(), 2F);
                    }
                    boolean ShouldDropPayloads;
                    switch (this.entity.getPlaneType()) {
                        default:
                            ShouldDropPayloads = false;
                            break;
                        case DIVE_BOMBER:
                            ShouldDropPayloads = target.getX() - 1 < this.entity.getX() && this.entity.getX() < target.getX() + 1 && target.getZ() - 1 < this.entity.getZ() && this.entity.getZ() < target.getZ() + 1;
                            break;
                        case FIGHTER:
                            ShouldDropPayloads = this.entity.distanceTo(target)<2.5;
                            break;
                        case TORPEDO_BOMBER:
                            ShouldDropPayloads = this.entity.distanceTo(target) < 8F;
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
            this.entity.getMoveControl().setWantedPosition(this.entity.getOwner().getX(), this.entity.getOwner().getY(), this.entity.getOwner().getZ(), 1.0F);
            if(this.entity.distanceToSqr(this.entity.getOwner())<1.5F) {
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
