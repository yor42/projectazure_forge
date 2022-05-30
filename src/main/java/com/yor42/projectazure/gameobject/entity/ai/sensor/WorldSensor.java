package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IWorldSkillUseable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.yor42.projectazure.libs.enums.ResourceBlockType.*;
import static com.yor42.projectazure.setup.register.registerManager.*;
import static net.minecraft.block.FarmlandBlock.MOISTURE;

public class WorldSensor extends Sensor<AbstractEntityCompanion> {

    private BlockPos LastUpdated;
    private int lastUpdatedTime = 0;

    @Override
    protected void doTick(@Nonnull ServerWorld world, @Nonnull AbstractEntityCompanion entity) {
        Brain<AbstractEntityCompanion> brain = entity.getBrain();
        if((this.LastUpdated == null || this.LastUpdated.distSqr(entity.blockPosition())>=4 || entity.tickCount-this.lastUpdatedTime>=200 || !brain.hasMemoryValue(NEAREST_ORE.get())|| !brain.hasMemoryValue(NEAREST_HARVESTABLE.get())|| !brain.hasMemoryValue(NEAREST_PLANTABLE.get())|| !brain.hasMemoryValue(NEAREST_BONEMEALABLE.get())) && (this.shouldWork(entity) || (entity instanceof IWorldSkillUseable && !entity.getBrain().hasMemoryValue(NEAREST_WORLDSKILLABLE.get())))){
            brain.eraseMemory(NEAREST_ORE.get());
            brain.eraseMemory(NEAREST_HARVESTABLE.get());
            brain.eraseMemory(NEAREST_PLANTABLE.get());
            brain.eraseMemory(NEAREST_WORLDSKILLABLE.get());
            brain.eraseMemory(registerManager.NEAREST_BONEMEALABLE.get());

            List<Pair<BlockPos, enums.ResourceBlockType>> blocklist = UpdateBlockList(world, entity).stream().filter((entry) -> {
                BlockPos pos = entry.getFirst();
                enums.ResourceBlockType type = entry.getSecond();
                Path path = entity.getNavigation().createPath(pos, type == ORE? 4:1);
                return path != null && path.canReach();
            }).sorted(Comparator.comparingDouble((entry) -> entity.distanceToSqr(Vector3d.atCenterOf(entry.getFirst())))).collect(Collectors.toList());

            blocklist.stream().filter((entry)->entry.getSecond() == ORE && entity.getMainHandItem().isCorrectToolForDrops(world.getBlockState(entry.getFirst())) && !entity.blockPosition().below().equals(entry.getFirst())).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(NEAREST_ORE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getSecond() == CROP_HARVESTABLE).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(NEAREST_HARVESTABLE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getSecond() == CROP_PLANTABLE).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(NEAREST_PLANTABLE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getSecond() == WORLDSKILL).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(NEAREST_WORLDSKILLABLE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getSecond() == CROP_BONEMEALABLE).map(Pair::getFirst).findFirst().ifPresent((pos)->{
                brain.setMemory(registerManager.NEAREST_BONEMEALABLE.get(), pos);
            });
            this.LastUpdated = entity.blockPosition();
            this.lastUpdatedTime = entity.tickCount;
        }
    }

    private List<Pair<BlockPos, enums.ResourceBlockType>> UpdateBlockList(@Nonnull ServerWorld world, @Nonnull AbstractEntityCompanion host){

        int diameter = 10;
        int height = 10;
        ArrayList<Pair<BlockPos, enums.ResourceBlockType>> BlockList = new ArrayList<>();
        BlockPos pos = new BlockPos(host.blockPosition().getX()-(diameter/2), Math.max(host.blockPosition().getY()-(height/2), 0), host.blockPosition().getZ()-(diameter/2));
        int size = diameter*diameter*height;
        Block block;
        for(int i = 0; i<size; i++) {
            BlockPos CurrentPos = pos.offset(i % diameter, i / diameter / diameter, (i / diameter) % diameter);

            BlockState state = world.getBlockState(CurrentPos);
            if (state.isAir(host.getCommandSenderWorld(), CurrentPos) || state.getDestroySpeed(world, CurrentPos) < 0) {
                //Skip air and unbreakable blocks
                continue;
            }
            block = state.getBlock();
            if (block instanceof FlowingFluidBlock || block instanceof IFluidBlock) {
                //Skip liquids
                continue;
            }

            this.getResourceType(host, world, CurrentPos).ifPresent((blocktype)->BlockList.add(new Pair<>(CurrentPos, blocktype)));
        }
        return BlockList;
    }

    private Optional<enums.ResourceBlockType> getResourceType(AbstractEntityCompanion host, ServerWorld worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        BlockState stateBelow = worldIn.getBlockState(pos.below());
        Block blockbelow = stateBelow.getBlock();
        boolean isOre = block instanceof OreBlock || block.is(Tags.Blocks.ORES);
        boolean crop = block instanceof CropsBlock;
        boolean isCactusorSugarcane = block instanceof SugarCaneBlock || block instanceof CactusBlock;
        BlockRayTraceResult result;

        //Determine if entity can ACTUALLY see that block. we dont want cheating.
        if(isOre||isCactusorSugarcane){
            result = worldIn.clip(new RayTraceContext(host.getEyePosition(1.0F), Vector3d.atCenterOf(pos), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, host));
        }
        else if(crop){
            result = worldIn.clip(new RayTraceContext(host.getEyePosition(1.0F), Vector3d.atBottomCenterOf(pos.above()), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, host));
        }
        else{
            result = worldIn.clip(new RayTraceContext(host.getEyePosition(1.0F), Vector3d.atCenterOf(pos), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, host));
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
            };
        }
        else if(blockbelow == Blocks.FARMLAND && state.getValue(MOISTURE)>0 && state.getMaterial() == Material.AIR){
            return Optional.of(enums.ResourceBlockType.CROP_PLANTABLE);
        }
        else if(block instanceof CropsBlock){
            if(!((CropsBlock) block).isMaxAge(state)){
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
        return ImmutableSet.of(NEAREST_ORE.get(), NEAREST_HARVESTABLE.get(), NEAREST_PLANTABLE.get(), registerManager.NEAREST_BONEMEALABLE.get(), NEAREST_WORLDSKILLABLE.get());
    }
}
