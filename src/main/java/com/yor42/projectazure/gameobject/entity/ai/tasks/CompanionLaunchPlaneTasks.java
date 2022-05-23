package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.EntityUtils.EntityHasPlanes;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getPreparedPlane;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.LOOK_TARGET;

public class CompanionLaunchPlaneTasks extends Task<AbstractEntityCompanion> {

    private int PlaneDelay;
    private int seeTime;
    private final int maxRangedAttackTime;
    private final float attackRadius;

    private final int attackIntervalMin;

    public CompanionLaunchPlaneTasks(float maxAttackDistanceIn, int minDelayPerPlane, int maxDelayPerPlane) {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT));
        this.attackRadius = maxAttackDistanceIn;
        this.attackIntervalMin = minDelayPerPlane;
        this.maxRangedAttackTime = maxDelayPerPlane;
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld world, @Nonnull AbstractEntityCompanion host) {
        if(host instanceof EntityKansenAircraftCarrier){
            EntityKansenAircraftCarrier entity = (EntityKansenAircraftCarrier) host;
            if(!(entity.getShipClass()== enums.shipClass.AircraftCarrier ||entity.getShipClass()==  enums.shipClass.LightAircraftCarrier || entity.getShipClass()== enums.shipClass.SubmarineCarrier)){
                return false;
            }
            boolean flag = entity.hasRigging();
            boolean canFire = entity.isSailing() || PAConfig.CONFIG.EnableShipLandCombat.get();
            if (flag && canFire){
                boolean flag2 =entity.getBrain().getMemory(ATTACK_TARGET).map((target)->entity.getSensing().canSee(target) && EntityHasPlanes(entity)).orElse(false);

                return flag2;
            }
        }
        return false;
    }

    @Override
    protected void tick(@Nonnull ServerWorld p_212833_1_, @Nonnull AbstractEntityCompanion p_212833_2_, long p_212833_3_) {
        if(p_212833_2_ instanceof EntityKansenAircraftCarrier){
            EntityKansenAircraftCarrier entity = (EntityKansenAircraftCarrier) p_212833_2_;
            entity.getBrain().getMemory(ATTACK_TARGET).ifPresent((target)->{
                double d0 = entity.distanceToSqr(target.getX(), target.getY(), target.getZ());
                boolean flag = entity.getSensing().canSee(target);
                if (flag) {
                    ++this.seeTime;
                } else {
                    this.seeTime = 0;
                }

                if (this.seeTime > 0) {
                    entity.getBrain().setMemory(LOOK_TARGET, new EntityPosWrapper(target, true));
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

                                AbstractEntityPlanes planeEntity = planetype.create(entity.getCommandSenderWorld());
                                if (planeEntity != null) {
                                    entity.LaunchPlane(planestack, planeEntity, target, hanger, hangerIndex);
                                }
                                float f = MathHelper.sqrt(d0) / this.attackRadius;
                                this.PlaneDelay = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) *entity.getPlanetoLaunch();
                            } else if (this.PlaneDelay < 0) {
                                float f2 = MathHelper.sqrt(d0) / this.attackRadius;
                                this.PlaneDelay = MathHelper.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) * entity.getPlanetoLaunch();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerWorld p_212834_1_, @Nonnull AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return this.checkExtraStartConditions(p_212834_1_, p_212834_2_);
    }

    @Override
    protected void stop(@Nonnull ServerWorld p_212835_1_, @Nonnull AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        this.seeTime = 0;
        this.PlaneDelay = -1;
    }
}
