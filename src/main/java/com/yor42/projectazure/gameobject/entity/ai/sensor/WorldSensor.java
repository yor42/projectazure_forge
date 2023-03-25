package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IWorldSkillUseable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.yor42.projectazure.libs.enums.ResourceBlockType.*;
import static net.minecraft.world.level.block.FarmBlock.MOISTURE;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WorldSensor extends Sensor<AbstractEntityCompanion> {

    private BlockPos LastUpdated;
    private int lastUpdatedTime = 0;

    @Override
    protected void doTick(@Nonnull ServerLevel world, @Nonnull AbstractEntityCompanion entity) {
        Brain<AbstractEntityCompanion> brain = entity.getBrain();
        if((this.LastUpdated == null || this.LastUpdated.distSqr(entity.blockPosition())>=4 || entity.tickCount-this.lastUpdatedTime>=200 || !brain.hasMemoryValue(RegisterAI.NEAREST_ORE.get())|| !brain.hasMemoryValue(RegisterAI.NEAREST_HARVESTABLE.get())|| !brain.hasMemoryValue(RegisterAI.NEAREST_PLANTABLE.get())|| !brain.hasMemoryValue(RegisterAI.NEAREST_BONEMEALABLE.get())) && (this.shouldWork(entity) || (entity instanceof IWorldSkillUseable && !entity.getBrain().hasMemoryValue(RegisterAI.NEAREST_WORLDSKILLABLE.get())))){
            brain.eraseMemory(RegisterAI.NEAREST_ORE.get());
            brain.eraseMemory(RegisterAI.NEAREST_HARVESTABLE.get());
            brain.eraseMemory(RegisterAI.NEAREST_PLANTABLE.get());
            brain.eraseMemory(RegisterAI.NEAREST_WORLDSKILLABLE.get());
            brain.eraseMemory(RegisterAI.NEAREST_BONEMEALABLE.get());

            List<Pair<BlockPos, enums.ResourceBlockType>> blocklist = UpdateBlockList(world, entity).stream().filter((entry) -> {
                BlockPos pos = entry.getFirst();
                enums.ResourceBlockType type = entry.getSecond();
                Path path = entity.getNavigation().createPath(pos, type == ORE? 4:1);
                return path != null && path.canReach();
            }).sorted(Comparator.comparingDouble((entry) -> entity.distanceToSqr(Vec3.atCenterOf(entry.getFirst())))).collect(Collectors.toList());

            blocklist.stream().filter((entry)->entry.getSecond() == ORE && entity.getMainHandItem().isCorrectToolForDrops(world.getBlockState(entry.getFirst())) && !entity.blockPosition().below().equals(entry.getFirst())).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(RegisterAI.NEAREST_ORE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getSecond() == CROP_HARVESTABLE).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(RegisterAI.NEAREST_HARVESTABLE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getSecond() == CROP_PLANTABLE).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(RegisterAI.NEAREST_PLANTABLE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getSecond() == WORLDSKILL).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(RegisterAI.NEAREST_WORLDSKILLABLE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getSecond() == CROP_BONEMEALABLE).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(RegisterAI.NEAREST_BONEMEALABLE.get(), pos);
            });
            this.LastUpdated = entity.blockPosition();
            this.lastUpdatedTime = entity.tickCount;
        }
    }

    private List<Pair<BlockPos, enums.ResourceBlockType>> UpdateBlockList(@Nonnull ServerLevel world, @Nonnull AbstractEntityCompanion host){

        int diameter = 10;
        int height = 10;
        ArrayList<Pair<BlockPos, enums.ResourceBlockType>> BlockList = new ArrayList<>();
        BlockPos pos = new BlockPos(host.blockPosition().getX()-(diameter/2), Math.max(host.blockPosition().getY()-(height/2), 0), host.blockPosition().getZ()-(diameter/2));
        int size = diameter*diameter*height;
        Block block;
        for(int i = 0; i<size; i++) {
            BlockPos CurrentPos = pos.offset(i % diameter, i / diameter / diameter, (i / diameter) % diameter);

            BlockState state = world.getBlockState(CurrentPos);
            if (state.isAir() || state.getDestroySpeed(world, CurrentPos) < 0) {
                //Skip air and unbreakable blocks
                continue;
            }
            block = state.getBlock();
            if (block instanceof LiquidBlock || block instanceof IFluidBlock) {
                //Skip liquids
                continue;
            }

            this.getResourceType(host, world, CurrentPos).ifPresent((blocktype)->BlockList.add(new Pair<>(CurrentPos, blocktype)));
        }
        return BlockList;
    }

    private Optional<enums.ResourceBlockType> getResourceType(AbstractEntityCompanion host, ServerLevel worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        BlockState stateBelow = worldIn.getBlockState(pos.below());
        Block blockbelow = stateBelow.getBlock();
        boolean isOre = (block instanceof OreBlock || block.is(Tags.Blocks.ORES)) && host.getMainHandItem().isCorrectToolForDrops(state);
        boolean crop = block instanceof CropBlock;
        boolean isCactusorSugarcane = block instanceof SugarCaneBlock || block instanceof CactusBlock;
        BlockHitResult result;

        //Determine if entity can ACTUALLY see that block. we don't want cheating.
        if(isOre||isCactusorSugarcane){
            result = worldIn.clip(new ClipContext(host.getEyePosition(1.0F), Vec3.atCenterOf(pos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, host));
        }
        else if(crop){
            result = worldIn.clip(new ClipContext(host.getEyePosition(1.0F), Vec3.atBottomCenterOf(pos.above()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, host));
        }
        else{
            result = worldIn.clip(new ClipContext(host.getEyePosition(1.0F), Vec3.atCenterOf(pos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, host));
        }

        if(!result.getBlockPos().equals(pos)){
            return Optional.empty();
        }


        if(isOre){
            return Optional.of(ORE);
        }
        else if(host instanceof IWorldSkillUseable){
            if(((IWorldSkillUseable) host).canUseWorldSkill(worldIn, pos, host)){
                return Optional.of(WORLDSKILL);
            }
        }
        else if(blockbelow == Blocks.FARMLAND && state.getValue(MOISTURE)>0 && state.getMaterial() == Material.AIR){
            return Optional.of(enums.ResourceBlockType.CROP_PLANTABLE);
        }
        else if(block instanceof CropBlock){
            if(!((CropBlock) block).isMaxAge(state)){
                return Optional.of(CROP_BONEMEALABLE);
            }
            else{
                return Optional.of(CROP_HARVESTABLE);
            }
        }
        else if(isCactusorSugarcane){
            if(block == blockbelow){
                return Optional.of(CROP_HARVESTABLE);
            }
        }
        return Optional.empty();
    }

    private boolean shouldWork(AbstractEntityCompanion entity){
        Item mainHandItem = entity.getMainHandItem().getItem();
        return entity.shouldHelpMine() || entity.shouldHelpFarm();
    }

    @Nonnull
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(RegisterAI.NEAREST_ORE.get(), RegisterAI.NEAREST_HARVESTABLE.get(), RegisterAI.NEAREST_PLANTABLE.get(), RegisterAI.NEAREST_BONEMEALABLE.get(), RegisterAI.NEAREST_WORLDSKILLABLE.get());
    }
}
