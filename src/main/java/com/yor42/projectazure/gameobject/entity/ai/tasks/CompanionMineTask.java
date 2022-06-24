package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import static com.yor42.projectazure.setup.register.RegisterAI.NEAREST_ORE;
import static net.minecraft.util.Hand.MAIN_HAND;

public class CompanionMineTask extends Task<AbstractEntityCompanion> {

    private int breakingTime;
    private int previousBreakProgress = -1;

    public CompanionMineTask() {
        super(ImmutableMap.of(NEAREST_ORE.get(), MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld world, AbstractEntityCompanion entity) {
        return entity.getBrain().getMemory(NEAREST_ORE.get()).map((pos)->world.getBlockState(pos).getBlock() instanceof OreBlock || world.getBlockState(pos).is(Tags.Blocks.ORES) && entity.shouldHelpMine()).orElse(false) && entity.getNavigation().isDone();
    }

    @Override
    protected boolean canStillUse(ServerWorld p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return this.checkExtraStartConditions(p_212834_1_, p_212834_2_);
    }

    @Override
    protected void tick(ServerWorld world, AbstractEntityCompanion entity, long p_212833_3_) {
        entity.getBrain().getMemory(NEAREST_ORE.get()).ifPresent((pos)->{
            ItemStack mainHandStack = entity.getMainHandItem();
            BlockState state = world.getBlockState(pos);
            this.breakingTime = (int) (this.breakingTime + mainHandStack.getDestroySpeed(state));
            int i = (int) ((float) (this.breakingTime) / 60.0F * 10);
            if(breakingTime%2 == 0){
                world.playSound(null, entity, state.getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.8F+(MathUtil.getRand().nextFloat()*0.4F), 0.8F+(MathUtil.getRand().nextFloat()*0.4F));
            }

            if (i != this.previousBreakProgress) {
                entity.level.destroyBlockProgress(entity.getId(), pos, i);
                this.previousBreakProgress = i;
                if (!entity.swinging) {
                    entity.swing(MAIN_HAND);
                }
            }
            if (this.breakingTime > 60.0F) {
                entity.level.destroyBlock(pos, true);
                mainHandStack.hurtAndBreak(1, entity, b -> b.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
                this.breakingTime = 0;
                entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
                entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                entity.getBrain().eraseMemory(NEAREST_ORE.get());
            }
        });
    }
}
