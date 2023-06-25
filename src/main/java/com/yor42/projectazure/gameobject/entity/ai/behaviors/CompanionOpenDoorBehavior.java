package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CompanionOpenDoorBehavior extends ExtendedBehaviour<AbstractEntityCompanion> {

    @Nullable
    private Node lastCheckedNode;

    public CompanionOpenDoorBehavior(){
        cooldownFor((e)->20);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.DOORS_TO_CLOSE, MemoryStatus.REGISTERED), Pair.of(MemoryModuleType.PATH, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        Path path = entity.getBrain().getMemory(MemoryModuleType.PATH).get();
        if (path.notStarted() || path.isDone()) {
            return false;
        }

        return !Objects.equals(this.lastCheckedNode, path.getNextNode());
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        ServerLevel level = (ServerLevel) entity.level;
        Path path = entity.getBrain().getMemory(MemoryModuleType.PATH).get();
        this.lastCheckedNode = path.getNextNode();
        Node pathpoint = path.getPreviousNode();
        Node pathpoint1 = path.getNextNode();
        BlockPos blockpos = pathpoint.asBlockPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.is(BlockTags.WOODEN_DOORS)) {
            DoorBlock doorblock = (DoorBlock)blockstate.getBlock();
            if (!doorblock.isOpen(blockstate)) {
                doorblock.setOpen(entity, level, blockstate, blockpos, true);
                entity.swing(InteractionHand.MAIN_HAND);
            }

            this.rememberDoorToClose(level, entity, blockpos);
        }

        BlockPos blockpos1 = pathpoint1.asBlockPos();
        BlockState blockstate1 = level.getBlockState(blockpos1);
        if (blockstate1.is(BlockTags.WOODEN_DOORS)) {
            DoorBlock doorblock1 = (DoorBlock)blockstate1.getBlock();
            if (!doorblock1.isOpen(blockstate1)) {
                doorblock1.setOpen(entity, level, blockstate1, blockpos1, true);
                this.rememberDoorToClose(level, entity, blockpos1);
                entity.swing(InteractionHand.MAIN_HAND);
            }
        }

        closeDoorsThatIHaveOpenedOrPassedThrough(level, entity, pathpoint, pathpoint1);

    }

    public static void closeDoorsThatIHaveOpenedOrPassedThrough(ServerLevel p_242294_0_, LivingEntity p_242294_1_, @Nullable Node p_242294_2_, @Nullable Node p_242294_3_) {
        Brain<?> brain = p_242294_1_.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.DOORS_TO_CLOSE)) {
            Iterator<GlobalPos> iterator = brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get().iterator();

            while(iterator.hasNext()) {
                GlobalPos globalpos = iterator.next();
                BlockPos blockpos = globalpos.pos();
                if ((p_242294_2_ == null || !p_242294_2_.asBlockPos().equals(blockpos)) && (p_242294_3_ == null || !p_242294_3_.asBlockPos().equals(blockpos))) {
                    if (isDoorTooFarAway(p_242294_0_, p_242294_1_, globalpos)) {
                        iterator.remove();
                    } else {
                        BlockState blockstate = p_242294_0_.getBlockState(blockpos);
                        if (!blockstate.is(BlockTags.WOODEN_DOORS)) {
                            iterator.remove();
                        } else {
                            DoorBlock doorblock = (DoorBlock)blockstate.getBlock();
                            if (!doorblock.isOpen(blockstate)) {
                                iterator.remove();
                            } else if (areOtherMobsComingThroughDoor(p_242294_0_, p_242294_1_, blockpos)) {
                                iterator.remove();
                            } else {
                                doorblock.setOpen(p_242294_1_, p_242294_0_, blockstate, blockpos, false);
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }

    }

    private static boolean areOtherMobsComingThroughDoor(ServerLevel p_242295_0_, LivingEntity p_242295_1_, BlockPos p_242295_2_) {
        Brain<?> brain = p_242295_1_.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.NEAREST_LIVING_ENTITIES) && brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).get().stream().filter((p_242298_1_) -> {
            return p_242298_1_.getType() == p_242295_1_.getType();
        }).filter((p_242299_1_) -> {
            return p_242295_2_.closerToCenterThan(p_242299_1_.position(), 2.0D);
        }).anyMatch((p_242297_2_) -> {
            return isMobComingThroughDoor(p_242295_0_, p_242297_2_, p_242295_2_);
        });
    }

    private static boolean isMobComingThroughDoor(ServerLevel p_242300_0_, LivingEntity p_242300_1_, BlockPos p_242300_2_) {
        if (!p_242300_1_.getBrain().hasMemoryValue(MemoryModuleType.PATH)) {
            return false;
        } else {
            Path path = p_242300_1_.getBrain().getMemory(MemoryModuleType.PATH).get();
            if (path.isDone()) {
                return false;
            } else {
                Node pathpoint = path.getPreviousNode();
                if (pathpoint == null) {
                    return false;
                } else {
                    Node pathpoint1 = path.getNextNode();
                    return p_242300_2_.equals(pathpoint.asBlockPos()) || p_242300_2_.equals(pathpoint1.asBlockPos());
                }
            }
        }
    }

    private static boolean isDoorTooFarAway(ServerLevel p_242296_0_, LivingEntity p_242296_1_, GlobalPos p_242296_2_) {
        return p_242296_2_.dimension() != p_242296_0_.dimension() || !p_242296_2_.pos().closerToCenterThan(p_242296_1_.position(), 2.0D);
    }

    private void rememberDoorToClose(ServerLevel p_242301_1_, LivingEntity p_242301_2_, BlockPos p_242301_3_) {
        Brain<?> brain = p_242301_2_.getBrain();
        GlobalPos globalpos = GlobalPos.of(p_242301_1_.dimension(), p_242301_3_);
        if (brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).isPresent()) {
            brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get().add(globalpos);
        } else {
            brain.setMemory(MemoryModuleType.DOORS_TO_CLOSE, Sets.newHashSet(globalpos));
        }

    }
}
