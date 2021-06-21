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

public class CompanionMoveToRecruitStationGoal extends Goal {

    private final AbstractEntityCompanion host;
    Vector3d NextPos;

    public CompanionMoveToRecruitStationGoal(AbstractEntityCompanion companion){
        this.host = companion;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        this.NextPos = this.getPosition();
        return this.host.isMovingtoRecruitStation && this.host.getRecruitStationPos() != BlockPos.ZERO && this.NextPos != null;
    }

    @Override
    public void tick() {
        if(this.NextPos == null || this.host.getDistanceSq(this.NextPos)<=9){
            this.NextPos = this.getPosition();
            this.host.getNavigator().clearPath();
        }

        if(this.NextPos != null) {
            this.host.getNavigator().tryMoveToXYZ(this.NextPos.getX(), this.NextPos.getY(), this.NextPos.getZ(), 1.0);
        }

        if(this.host.getDistanceSq(this.host.getRecruitStationPos().getX(),this.host.getRecruitStationPos().getY(),this.host.getRecruitStationPos().getZ())<=9){
            this.host.getNavigator().clearPath();
            this.host.func_233687_w_(true);
            this.host.isMovingtoRecruitStation = false;
        }

    }

    @Nullable
    protected Vector3d getPosition() {
        return RandomPositionGenerator.findRandomTargetBlockTowards(this.host, 10, 7, Vector3d.copyCentered(this.host.getRecruitStationPos()));
    }
}
