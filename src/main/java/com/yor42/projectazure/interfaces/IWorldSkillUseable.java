package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public interface IWorldSkillUseable {

    boolean canUseWorldSkill(ServerWorld world, BlockPos pos, AbstractEntityCompanion companion);

    boolean executeWorldSkill(ServerWorld world, BlockPos pos, AbstractEntityCompanion entity);

    float getWorldSkillRange();

    boolean executeWorldSkillTick(ServerWorld world, BlockPos pos, AbstractEntityCompanion entity);

    default void endWorldSkill(ServerWorld world, BlockPos pos, AbstractEntityCompanion entity){};

}
