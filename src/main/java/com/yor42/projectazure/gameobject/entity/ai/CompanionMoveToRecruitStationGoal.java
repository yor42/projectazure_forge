package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import org.lwjgl.system.CallbackI;
import org.objectweb.asm.tree.AbstractInsnNode;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CompanionMoveToRecruitStationGoal extends RandomWalkingGoal {

    private final AbstractEntityCompanion host;

    public CompanionMoveToRecruitStationGoal(AbstractEntityCompanion companion, double speed){
        super(companion, speed);
        this.host = companion;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        return this.host.isMovingtoRecruitStation && this.host.getRecruitStationPos() != BlockPos.ZERO && super.shouldExecute() && this.host.getDistanceSq(this.host.getRecruitStationPos().getX(),this.host.getRecruitStationPos().getY(),this.host.getRecruitStationPos().getZ())>25;
    }

    @Nullable
    @Override
    protected Vector3d getPosition() {
        return RandomPositionGenerator.findRandomTargetBlockTowards(this.host, 10, 7, Vector3d.copyCentered(this.host.getRecruitStationPos()));
    }
}
