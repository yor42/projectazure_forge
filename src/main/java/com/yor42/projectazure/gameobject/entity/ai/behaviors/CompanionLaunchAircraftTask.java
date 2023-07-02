package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

import static com.yor42.projectazure.libs.utils.EntityUtils.EntityHasPlanes;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getPreparedPlane;
import static com.yor42.projectazure.setup.register.RegisterAI.Animations.MELEE_ATTACK;

public class CompanionLaunchAircraftTask extends DelayedBehaviour<AbstractEntityCompanion> {
    public CompanionLaunchAircraftTask(AbstractEntityCompanion entity) {
        super(entity instanceof EntityKansenAircraftCarrier? ((EntityKansenAircraftCarrier) entity).getPlanePreLaunchAnimationDelay():-1);
        runFor((e)-> entity instanceof EntityKansenAircraftCarrier ? ((EntityKansenAircraftCarrier) entity).getPlaneLaunchDelay():-1);
        cooldownFor((e)->{
            if(!(e instanceof EntityKansenAircraftCarrier)){
                return 200;
            }

            LivingEntity target = BrainUtils.getTargetOfEntity(e);

            double d0 = target == null? 10: e.distanceToSqr(target);
            float f = (float)Math.sqrt(d0) / e.getPlaneRange();
            return Mth.floor(f * (float) (200 - 80) + 80) *((EntityKansenAircraftCarrier)e).getPlanetoLaunch();});
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {

        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if(target == null || this.delayTime<0 || entity.isAnimating()){
            return false;
        }


        if(entity instanceof EntityKansenAircraftCarrier host){

            if(!(host.getShipClass()== enums.shipClass.AircraftCarrier ||host.getShipClass()==  enums.shipClass.LightAircraftCarrier || host.getShipClass()== enums.shipClass.SubmarineCarrier)){
                return false;
            }
            boolean hasrigging = host.hasRigging();
            boolean canseeandarmed = host.getSensing().hasLineOfSight(target) && EntityHasPlanes(host) && entity.wantsToAttack(target, host);
            boolean canFire = host.isSailing() || PAConfig.CONFIG.EnableShipLandCombat.get();
            return hasrigging && canFire && canseeandarmed;
        }

        return false;
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if(target == null){
            return;
        }

        if(!entity.closerThan(target, entity.getPlaneRange())){
            entity.setAnimation(RegisterAI.Animations.SHOOT_CANNON, entity.CannonAttackAnimLength());
            this.clearWalkTarget(entity);
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AbstractEntityCompanion entity, long gameTime) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if(target == null){
            return false;
        }

        return this.checkExtraStartConditions(level, entity)&& entity.closerThan(target, entity.getPlaneRange()) || entity.isAnimating(RegisterAI.Animations.LAUNCH_PLANE);
    }

    @Override
    protected void doDelayedAction(AbstractEntityCompanion e) {

        LivingEntity target = BrainUtils.getTargetOfEntity(e);
        if(target == null){
            return;
        }


        if (e.getRigging().getItem() instanceof ItemRiggingBase) {
            if(e instanceof EntityKansenAircraftCarrier entity) {
                IItemHandlerModifiable hanger = MultiInvUtil.getCap(entity.getRigging()).getInventory(enums.SLOTTYPE.PLANE.ordinal());

                if (hanger == null) {
                    return;
                }

                int hangerIndex = getPreparedPlane(entity, hanger);
                if (hangerIndex >= 0) {
                    ItemStack planestack = hanger.getStackInSlot(hangerIndex);

                    if (planestack.getItem() instanceof ItemEquipmentPlaneBase) {
                        EntityType<? extends AbstractEntityPlanes> planetype = ((ItemEquipmentPlaneBase) planestack.getItem()).getEntityType();

                        AbstractEntityPlanes planeEntity = planetype.create(entity.getLevel());
                        if (planeEntity != null) {
                            entity.LaunchPlane(planestack, planeEntity, target, hanger, hangerIndex);
                            entity.setAnimation(RegisterAI.Animations.SHOOT_CANNON, entity.CannonAttackAnimLength());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }

    private void clearWalkTarget(LivingEntity p_233967_1_) {
        p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}
