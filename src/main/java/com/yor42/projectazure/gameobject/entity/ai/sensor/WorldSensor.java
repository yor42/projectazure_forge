package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerManager;
import javafx.util.Pair;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.yor42.projectazure.libs.enums.ResourceBlockType.*;
import static net.minecraft.block.FarmlandBlock.MOISTURE;

public class WorldSensor extends Sensor<AbstractEntityCompanion> {

    BlockPos LastUpdated;

    @Override
    protected void doTick(@Nonnull ServerWorld world, @Nonnull AbstractEntityCompanion entity) {
        if((this.LastUpdated == null || this.LastUpdated.distSqr(entity.blockPosition())>=4) && this.shouldWork(entity)){

            Brain<AbstractEntityCompanion> brain = entity.getBrain();
            brain.eraseMemory(registerManager.NEAREST_ORE.get());
            brain.eraseMemory(registerManager.NEAREST_HARVESTABLE.get());
            brain.eraseMemory(registerManager.NEAREST_PLANTABLE.get());
            brain.eraseMemory(registerManager.NEAREST_BONEMEALABLE.get());

            List<Pair<BlockPos, enums.ResourceBlockType>> blocklist = UpdateBlockList(world, entity).stream().filter((entry) -> {
                BlockPos pos = entry.getKey();
                enums.ResourceBlockType type = entry.getValue();
                Path path = entity.getNavigation().createPath(pos, type == ORE? 4:1);
                return path != null && path.canReach();
            }).sorted(Comparator.comparingDouble((entry) -> entity.distanceToSqr(Vector3d.atCenterOf(entry.getKey())))).collect(Collectors.toList());

            blocklist.stream().filter((entry)->entry.getValue() == ORE).map(Pair::getKey).findFirst().ifPresent((pos)->{
                brain.setMemory(registerManager.NEAREST_ORE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getValue() == CROP_HARVESTABLE).map(Pair::getKey).findFirst().ifPresent((pos)->{
                brain.setMemory(registerManager.NEAREST_HARVESTABLE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getValue() == CROP_PLANTABLE).map(Pair::getKey).findFirst().ifPresent((pos)->{
                brain.setMemory(registerManager.NEAREST_PLANTABLE.get(), pos);
            });
            blocklist.stream().filter((entry)->entry.getValue() == CROP_BONEMEALABLE).map(Pair::getKey).findFirst().ifPresent((pos)->{
                brain.setMemory(registerManager.NEAREST_BONEMEALABLE.get(), pos);
            });
        }
        this.LastUpdated = entity.blockPosition();
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

    private Optional<enums.ResourceBlockType> getResourceType(AbstractEntityCompanion host, IWorldReader worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        BlockState stateBelow = worldIn.getBlockState(pos.below());
        Block blockbelow = stateBelow.getBlock();
        boolean isOre = block instanceof OreBlock;
        boolean isCactusorSugarcane = block instanceof SugarCaneBlock || block instanceof CactusBlock;
        BlockRayTraceResult result;

        //Determine if entity can ACTUALLY see that block. we dont want cheating.
        if(isOre||isCactusorSugarcane){
            result = worldIn.clip(new RayTraceContext(host.getEyePosition(1.0F), Vector3d.atCenterOf(pos), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, host));
        }
        else{
            result = worldIn.clip(new RayTraceContext(host.getEyePosition(1.0F), Vector3d.atBottomCenterOf(pos.above()), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, host));
        }

        if(!result.getBlockPos().equals(pos)){
            return Optional.empty();
        }


        if(isOre){
            return Optional.of(ORE);
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
        return mainHandItem instanceof HoeItem ||mainHandItem instanceof PickaxeItem;
    }

    @Nonnull
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(registerManager.NEAREST_ORE.get(), registerManager.NEAREST_HARVESTABLE.get(), registerManager.NEAREST_PLANTABLE.get(), registerManager.NEAREST_BONEMEALABLE.get());
    }
}
