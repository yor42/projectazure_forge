package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

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
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));

        this.attackIntervalMin = minDelayPerPlane;
        this.maxRangedAttackTime = maxDelayPerPlane;
    }

    @Override
    public boolean canUse() {

        if(this.entity == null){
            return false;
        }

        if(this.entity.isCriticallyInjured()){
            return false;
        }

        if(!(this.entity.getShipClass()== enums.shipClass.AircraftCarrier ||this.entity.getShipClass()==  enums.shipClass.LightAircraftCarrier || this.entity.getShipClass()== enums.shipClass.SubmarineCarrier)){
            return false;
        }

        boolean flag = !this.entity.isOrderedToSit()&&this.entity.hasRigging() && this.entity.getTarget() != null && this.entity.getTarget().isAlive();
        boolean canFire = this.entity.isSailing() || PAConfig.CONFIG.EnableShipLandCombat.get();

        if (flag && canFire){
            boolean flag2 =this.entity.getSensing().hasLineOfSight(this.entity.getTarget()) && EntityHasPlanes(this.entity);

            if(flag2) {
                this.targetEntity = entity.getTarget();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void stop() {
        super.stop();
        this.seeTime = 0;
        this.PlaneDelay = -1;
    }

    @Override
    public void tick() {
        super.tick();
        double d0 = this.entity.distanceToSqr(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ());
        boolean flag = this.entity.getSensing().hasLineOfSight(this.targetEntity);
        Entity targetEntity = this.entity.getTarget();
        if(targetEntity!= null) {
            if (flag) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }

            if (this.seeTime > 0) {
                this.entity.getLookControl().setLookAt(targetEntity, 10, 10);
            }

            if (entity.getRigging().getItem() instanceof ItemRiggingBase) {
                IItemHandler hanger = MultiInvUtil.getCap(entity.getRigging()).getInventory(enums.SLOTTYPE.PLANE.ordinal());

                if (hanger != null) {
                    int hangerIndex = getPreparedPlane(this.entity, hanger);
                    if(hangerIndex>=0) {
                        ItemStack planestack = hanger.getStackInSlot(hangerIndex);

                        this.entity.getLookControl().setLookAt(this.targetEntity, 30.0F, 30.0F);

                        boolean flag3 = --this.PlaneDelay == 0 && planestack.getItem() instanceof ItemEquipmentPlaneBase;
                        if (flag3) {
                            if (!flag) {
                                return;
                            }
                            EntityType<? extends AbstractEntityPlanes> planetype = ((ItemEquipmentPlaneBase) planestack.getItem()).getEntityType();

                            AbstractEntityPlanes planeEntity = planetype.create(this.entity.getCommandSenderWorld());
                            if (planeEntity != null) {
                                this.entity.LaunchPlane(planestack, planeEntity, this.targetEntity, (IItemHandlerModifiable) hanger, hangerIndex);
                            }
                            float f = Mth.sqrt((float) d0) / this.attackRadius;
                            this.PlaneDelay = Mth.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) * this.entity.getPlanetoLaunch();
                        } else if (this.PlaneDelay < 0) {
                            float f2 = Mth.sqrt((float) d0) / this.attackRadius;
                            this.PlaneDelay = Mth.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) * this.entity.getPlanetoLaunch();
                        }
                    }
                }
            }
        }
    }



}
