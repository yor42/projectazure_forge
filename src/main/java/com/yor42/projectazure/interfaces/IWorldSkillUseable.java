package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface IWorldSkillUseable {

    boolean canUseWorldSkill(ServerLevel world, BlockPos pos, AbstractEntityCompanion companion);

    boolean executeWorldSkill(ServerLevel world, BlockPos pos, AbstractEntityCompanion entity);

    float getWorldSkillRange();

    boolean executeWorldSkillTick(ServerLevel world, BlockPos pos, AbstractEntityCompanion entity);

    default void endWorldSkill(ServerLevel world, BlockPos pos, AbstractEntityCompanion entity){}

}
