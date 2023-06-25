package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.BlockWithShield;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import javax.annotation.Nullable;
import java.util.List;

public class CompanionRaiseShield extends BlockWithShield<AbstractEntityCompanion> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = List.of(Pair.of(SBLMemoryTypes.INCOMING_PROJECTILES.get(), MemoryStatus.REGISTERED), Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    @Nullable
    private Entity blockTarget = null;


    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        if(entity.getShieldCoolDown() != 0){
            return false;
        }

        List<Projectile> list = BrainUtils.getMemory(entity, SBLMemoryTypes.INCOMING_PROJECTILES.get());
        Monster NearestMob = EntityRetrievalUtil.getNearestEntity(level, entity.getBoundingBox().inflate(2,2,2), entity.position(), obj -> obj instanceof Monster);;

        if(list != null && !list.isEmpty()){
            blockTarget = list.get(0);
        }

        if (NearestMob != null && (blockTarget == null || entity.distanceTo(NearestMob)<entity.distanceTo(blockTarget))) {
            blockTarget = NearestMob;
        }

        return blockTarget != null;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        super.start(entity);
        if(blockTarget == null){
            return;
        }

        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(blockTarget, true));
    }
}
