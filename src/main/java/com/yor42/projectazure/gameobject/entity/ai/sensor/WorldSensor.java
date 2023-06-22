package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IWorldSkillUseable;
import com.yor42.projectazure.setup.register.RegisterAI;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.object.TriPredicate;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.Comparator;
import java.util.List;

public class WorldSensor<E extends AbstractEntityCompanion> extends ExtendedSensor<E> {

    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(RegisterAI.NEAR_ORES.get(), RegisterAI.NEAR_HARVESTABLES.get(), RegisterAI.NEAR_BONEMEALABLE.get(), RegisterAI.NEAR_PLANTABLE.get(), RegisterAI.NEAREST_WORLDSKILLABLE.get());
    private final TriPredicate<E, BlockPos, BlockState> orePredicate = (entity, pos, state)->state.is(Tags.Blocks.ORES) || state.getBlock() instanceof OreBlock;
    private final TriPredicate<E, BlockPos, BlockState> harvestPredicate = (entity, pos, state)-> state.getBlock() instanceof CropBlock || ((CropBlock)state.getBlock()).isMaxAge(state);
    private final TriPredicate<E, BlockPos, BlockState> bonemealPredicate = (entity, pos, state)-> state.getBlock() instanceof CropBlock || !((CropBlock)state.getBlock()).isMaxAge(state);

    protected SquareRadius radius = new SquareRadius(8, 4);
    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return null;
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {

        for(MemoryModuleType<?> memory:MEMORIES){
            BrainUtils.clearMemory(entity, memory);
        }

        List<Pair<BlockPos, BlockState>> ores = new ObjectArrayList<>();
        List<Pair<BlockPos, BlockState>> harvestable = new ObjectArrayList<>();
        List<Pair<BlockPos, BlockState>> bonemealable = new ObjectArrayList<>();
        List<Pair<BlockPos, BlockState>> plantable = new ObjectArrayList<>();
        List<Pair<BlockPos, BlockState>> worldskillable = new ObjectArrayList<>();

        for (BlockPos pos : BlockPos.betweenClosed(entity.blockPosition().subtract(this.radius.toVec3i()), entity.blockPosition().offset(this.radius.toVec3i()))) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if(state.is(Tags.Blocks.ORES) || block instanceof OreBlock){
                if(entity.getNavigation().createPath(pos, 4) == null) {
                    continue;
                }
                if(level.clip(new ClipContext(entity.getEyePosition(), Vec3.atCenterOf(pos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getBlockPos().equals(pos)) {
                    ores.add(Pair.of(pos, state));
                }
            }
            else if(block instanceof CropBlock){
                if(!level.clip(new ClipContext(entity.getEyePosition(), Vec3.atCenterOf(pos), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().equals(pos)) {
                    continue;
                }

                if(((CropBlock)state.getBlock()).isMaxAge(state)){
                    harvestable.add(Pair.of(pos, state));
                }
                else {
                    bonemealable.add(Pair.of(pos, state));
                }
            }
            else if(state.isAir() && level.getBlockState(pos.below()).isFertile(level, pos)){
                if(!level.clip(new ClipContext(entity.getEyePosition(), Vec3.atBottomCenterOf(pos), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().equals(pos)) {
                    continue;
                }
                plantable.add(Pair.of(pos, state));
            }

            if(entity instanceof IWorldSkillUseable){
                if(((IWorldSkillUseable) entity).canUseWorldSkill(level,pos, entity)){

                    if(!level.clip(new ClipContext(entity.getEyePosition(), Vec3.atCenterOf(pos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getBlockPos().equals(pos)) {
                        continue;
                    }

                    worldskillable.add(Pair.of(pos, state));
                }
            }
        }

        ores.sort(Comparator.comparingDouble((pair)-> entity.distanceToSqr(pair.getFirst().getX(),pair.getFirst().getY(),pair.getFirst().getZ())));
        harvestable.sort(Comparator.comparingDouble((pair)-> entity.distanceToSqr(pair.getFirst().getX(),pair.getFirst().getY(),pair.getFirst().getZ())));
        bonemealable.sort(Comparator.comparingDouble((pair)-> entity.distanceToSqr(pair.getFirst().getX(),pair.getFirst().getY(),pair.getFirst().getZ())));
        plantable.sort(Comparator.comparingDouble((pair)-> entity.distanceToSqr(pair.getFirst().getX(),pair.getFirst().getY(),pair.getFirst().getZ())));
        worldskillable.sort(Comparator.comparingDouble((pair)-> entity.distanceToSqr(pair.getFirst().getX(),pair.getFirst().getY(),pair.getFirst().getZ())));
        if(!ores.isEmpty()) BrainUtils.setMemory(entity, RegisterAI.NEAR_ORES.get(), ores);
        if(!plantable.isEmpty()) BrainUtils.setMemory(entity, RegisterAI.NEAR_PLANTABLE.get(), plantable);
        if(!bonemealable.isEmpty()) BrainUtils.setMemory(entity, RegisterAI.NEAR_BONEMEALABLE.get(), bonemealable);
        if(!harvestable.isEmpty()) BrainUtils.setMemory(entity, RegisterAI.NEAR_HARVESTABLES.get(), harvestable);
        if(!worldskillable.isEmpty()) BrainUtils.setMemory(entity, RegisterAI.NEAREST_WORLDSKILLABLE.get(), worldskillable.get(0).getFirst());

    }
}
