package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.BreakBlock;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Objects;

public class CompanionBreakOreBehavior extends BreakBlock<AbstractEntityCompanion> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(Pair.of(RegisterAI.NEAR_ORES.get(), MemoryStatus.VALUE_PRESENT));


    public CompanionBreakOreBehavior(){
        this.timeToBreak((entity, pos, state)-> (int) (1/progresspertick(state, entity, entity.level, pos)));
        this.forBlocks((entity, pos, state)-> true);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        for (Pair<BlockPos, BlockState> pair : Objects.requireNonNull(BrainUtils.getMemory(entity, RegisterAI.NEAR_ORES.get()))) {
            if (this.targetBlockPredicate.test(entity, pair.getFirst(), pair.getSecond())) {
                this.pos = pair.getFirst();
                this.state = pair.getSecond();
                this.timeToBreak = this.digTimePredicate.apply(entity, this.pos, this.state);

                return true;
            }
        }

        return false;
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        if(Math.sqrt(entity.distanceToSqr(this.pos.getX(), this.pos.getY(), this.pos.getZ())) >3){
            BehaviorUtils.setWalkAndLookTargetMemories(entity, this.pos, 1, 1);
        }
    }

    @Override
    protected boolean shouldKeepRunning(AbstractEntityCompanion entity) {
        return super.shouldKeepRunning(entity) && Math.sqrt(entity.distanceToSqr(this.pos.getX(), this.pos.getY(), this.pos.getZ())) <= 3;
    }

    public float progresspertick(BlockState pState, AbstractEntityCompanion entity, BlockGetter pLevel, BlockPos pPos) {
        float f = pState.getDestroySpeed(pLevel, pPos);
        if (f == -1.0F) {
            return 0.0F;
        } else {
            return getDigSpeed(entity,pState) / f / (float)30;
        }
    }

    private static float getDigSpeed(AbstractEntityCompanion entity, BlockState pState){
        float f = entity.getMainHandItem().getDestroySpeed(pState);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getBlockEfficiency(entity);
            ItemStack itemstack = entity.getMainHandItem();
            if (i > 0 && !itemstack.isEmpty()) {
                f += (float)(i * i + 1);
            }
        }

        if (MobEffectUtil.hasDigSpeed(entity)) {
            f *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(entity) + 1) * 0.2F;
        }

        if (entity.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float f1 = switch (Objects.requireNonNull(entity.getEffect(MobEffects.DIG_SLOWDOWN)).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };

            f *= f1;
        }

        if (entity.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(entity)) {
            f /= 5.0F;
        }

        if (!entity.isOnGround()) {
            f /= 5.0F;
        }
        return f;
    }

    @Override
    protected void tick(AbstractEntityCompanion entity) {
        this.breakTime++;
        int progress = (int)(this.breakTime / (float)this.timeToBreak * 10);

        if (progress != this.breakProgress) {
            entity.level.destroyBlockProgress(entity.getId(), this.pos, progress);
            if (!entity.swinging) {
                entity.swing(InteractionHand.MAIN_HAND);
            }
            this.breakProgress = progress;
        }

        if (this.breakTime >= this.timeToBreak) {
            entity.level.destroyBlock(this.pos, true);
            entity.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, this.pos, Block.getId(entity.level.getBlockState(this.pos)));
            entity.getMainHandItem().hurtAndBreak(1, entity, b -> b.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            doStop((ServerLevel)entity.level, entity, entity.level.getGameTime());
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(RegisterAI.NEAR_ORES.get(), MemoryStatus.VALUE_PRESENT));
    }
}
