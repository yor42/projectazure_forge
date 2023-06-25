package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.EntityUtils.EntityHasPlanes;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getPreparedPlane;

public class CompanionLaunchPlaneTasks extends Behavior<AbstractEntityCompanion> {

    private int PlaneDelay;
    private int seeTime;
    private final int maxRangedAttackTime;
    private final float attackRadius;

    private final int attackIntervalMin;

    public CompanionLaunchPlaneTasks(float maxAttackDistanceIn, int minDelayPerPlane, int maxDelayPerPlane) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
        this.attackRadius = maxAttackDistanceIn;
        this.attackIntervalMin = minDelayPerPlane;
        this.maxRangedAttackTime = maxDelayPerPlane;
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, @Nonnull AbstractEntityCompanion host) {
        if(host instanceof EntityKansenAircraftCarrier){
            EntityKansenAircraftCarrier entity = (EntityKansenAircraftCarrier) host;
            if(!(entity.getShipClass()== enums.shipClass.AircraftCarrier ||entity.getShipClass()==  enums.shipClass.LightAircraftCarrier || entity.getShipClass()== enums.shipClass.SubmarineCarrier)){
                return false;
            }
            boolean flag = entity.hasRigging();
            boolean canFire = entity.isSailing() || PAConfig.CONFIG.EnableShipLandCombat.get();
            if (flag && canFire){
                boolean flag2 =entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).map((target)->entity.getSensing().hasLineOfSight(target) && EntityHasPlanes(entity) && entity.wantsToAttack(target, entity)).orElse(false);

                return flag2;
            }
        }
        return false;
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        if(entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()){
            return;
        }
        if(!entity.closerThan(entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get(), entity.getSpellRange())){
            BehaviorUtils.setWalkAndLookTargetMemories(entity, entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get(), 1, (int) (entity.getPlaneRange()-2));
        }
        else {
            this.clearWalkTarget(entity);
        }
    }

    @Override
    protected void tick(@Nonnull ServerLevel p_212833_1_, @Nonnull AbstractEntityCompanion p_212833_2_, long p_212833_3_) {
        if(p_212833_2_ instanceof EntityKansenAircraftCarrier){
            EntityKansenAircraftCarrier entity = (EntityKansenAircraftCarrier) p_212833_2_;
            entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target)->{
                double d0 = entity.distanceToSqr(target.getX(), target.getY(), target.getZ());
                boolean flag = entity.getSensing().hasLineOfSight(target);
                if (flag) {
                    ++this.seeTime;
                } else {
                    this.seeTime = 0;
                }

                if (this.seeTime > 0) {
                    entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
                }

                if (entity.getRigging().getItem() instanceof ItemRiggingBase) {
                    IItemHandlerModifiable hanger = MultiInvUtil.getCap(entity.getRigging()).getInventory(enums.SLOTTYPE.PLANE.ordinal());

                    if (hanger != null) {
                        int hangerIndex = getPreparedPlane(entity, hanger);
                        if(hangerIndex>=0) {
                            ItemStack planestack = hanger.getStackInSlot(hangerIndex);

                            boolean flag3 = --this.PlaneDelay == 0 && planestack.getItem() instanceof ItemEquipmentPlaneBase;
                            if (flag3) {
                                if (!flag) {
                                    return;
                                }
                                EntityType<? extends AbstractEntityPlanes> planetype = ((ItemEquipmentPlaneBase) planestack.getItem()).getEntityType();

                                AbstractEntityPlanes planeEntity = planetype.create(entity.getLevel());
                                if (planeEntity != null) {
                                    entity.LaunchPlane(planestack, planeEntity, target, hanger, hangerIndex);
                                }
                                float f = (float)Math.sqrt(d0) / this.attackRadius;
                                this.PlaneDelay = Mth.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) *entity.getPlanetoLaunch();
                            } else if (this.PlaneDelay < 0) {
                                float f2 = (float)Math.sqrt(d0) / this.attackRadius;
                                this.PlaneDelay = Mth.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) * entity.getPlanetoLaunch();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerLevel p_212834_1_, @Nonnull AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        if(!p_212834_2_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()){
            return false;
        }

        return this.checkExtraStartConditions(p_212834_1_, p_212834_2_)&& p_212834_2_.closerThan(p_212834_2_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get(), p_212834_2_.getPlaneRange());
    }

    @Override
    protected void stop(@Nonnull ServerLevel p_212835_1_, @Nonnull AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        this.seeTime = 0;
        this.PlaneDelay = -1;
    }

    private void clearWalkTarget(LivingEntity p_233967_1_) {
        p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}
