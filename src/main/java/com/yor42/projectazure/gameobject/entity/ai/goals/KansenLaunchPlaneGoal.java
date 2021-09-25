package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.EntityUtils.EntityHasPlanes;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getPreparedPlane;

public class KansenLaunchPlaneGoal extends Goal {

    private EntityKansenAircraftCarrier entity;
    private LivingEntity targetEntity;

    private int PlaneDelay;
    private int seeTime;
    private final int maxRangedAttackTime;
    private final float attackRadius;

    private final int attackIntervalMin;

    public KansenLaunchPlaneGoal(EntityKansenAircraftCarrier entity, float maxAttackDistanceIn, int minDelayPerPlane, int maxDelayPerPlane){
        this.entity = entity;
        this.attackRadius = maxAttackDistanceIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));

        this.attackIntervalMin = minDelayPerPlane;
        this.maxRangedAttackTime = maxDelayPerPlane;
    }

    @Override
    public boolean shouldExecute() {

        if(this.entity == null){
            return false;
        }

        if(!(this.entity.getShipClass()== enums.shipClass.AircraftCarrier ||this.entity.getShipClass()==  enums.shipClass.LightAircraftCarrier || this.entity.getShipClass()== enums.shipClass.SubmarineCarrier)){
            return false;
        }

        boolean flag = !this.entity.isSitting()&&this.entity.hasRigging() && this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isAlive();

        if (flag){
            boolean flag2 =this.entity.getEntitySenses().canSee(this.entity.getAttackTarget()) && EntityHasPlanes(this.entity);

            if(flag2) {
                this.targetEntity = entity.getAttackTarget();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.seeTime = 0;
        this.PlaneDelay = -1;
    }

    @Override
    public void tick() {
        super.tick();
        double d0 = this.entity.getDistanceSq(this.targetEntity.getPosX(), this.targetEntity.getPosY(), this.targetEntity.getPosZ());
        boolean flag = this.entity.getEntitySenses().canSee(this.targetEntity);
        Entity targetEntity = this.entity.getAttackTarget();
        if(targetEntity!= null) {
            if (flag) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }

            if (this.seeTime > 0) {
                this.entity.getLookController().setLookPositionWithEntity(targetEntity, 10, 10);
            }

            if (entity.getRigging().getItem() instanceof ItemRiggingBase) {
                ItemStackHandler hanger = ((ItemRiggingBase) this.entity.getRigging().getItem()).getHangers(this.entity.getRigging());

                if (hanger != null) {
                    int hangerIndex = getPreparedPlane(this.entity, hanger);
                    if(hangerIndex>=0) {
                        ItemStack planestack = hanger.getStackInSlot(hangerIndex);

                        this.entity.getLookController().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);

                        boolean flag3 = --this.PlaneDelay == 0 && planestack.getItem() instanceof ItemEquipmentPlaneBase;
                        if (flag3) {
                            if (!flag) {
                                return;
                            }
                            EntityType<? extends AbstractEntityPlanes> planetype = ((ItemEquipmentPlaneBase) planestack.getItem()).getEntityType();

                            AbstractEntityPlanes planeEntity = planetype.create(this.entity.getEntityWorld());
                            if (planeEntity != null) {
                                this.entity.LaunchPlane(planestack, planeEntity, this.targetEntity, hanger, hangerIndex);
                            }
                            float f = MathHelper.sqrt(d0) / this.attackRadius;
                            this.PlaneDelay = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) * this.entity.getPlanetoLaunch();
                        } else if (this.PlaneDelay < 0) {
                            float f2 = MathHelper.sqrt(d0) / this.attackRadius;
                            this.PlaneDelay = MathHelper.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) * this.entity.getPlanetoLaunch();
                        }
                    }
                }
            }
        }
    }



}
