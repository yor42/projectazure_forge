package com.yor42.projectazure.gameobject.entity.ai.behaviors.base;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import javax.annotation.Nonnull;

public abstract class ExtendedItemSwitchingTasks<E extends AbstractEntityCompanion> extends ExtendedBehaviour<E> {

    @Override
    protected void stop(@Nonnull ServerLevel level, @Nonnull E entity, long gameTime) {
        super.stop(level, entity, gameTime);

        int itemswapindex = this.SwapHand() == InteractionHand.MAIN_HAND? entity.getItemSwapIndexMainHand() : entity.getItemSwapIndexOffHand();

        if(itemswapindex>-1) {
            ItemStack buffer = entity.getItemInHand(this.SwapHand());
            entity.setItemInHand(this.SwapHand(), entity.getInventory().getStackInSlot(itemswapindex));
            entity.getInventory().setStackInSlot(itemswapindex, buffer);
            setSwapIndex(entity, -1);
        }
    }

    protected void ChangeItem(int index, AbstractEntityCompanion entity){
        ItemStack Buffer = entity.getOffhandItem();
        entity.setItemInHand(this.SwapHand(), entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        setSwapIndex(entity, index);
    }

    protected void ChangeItemwithMemory(MemoryModuleType<Integer> memory, AbstractEntityCompanion entity){
        Integer idx = BrainUtils.getMemory(entity, memory);
        if(idx == null){
            return;
        }
        this.ChangeItem(idx, entity);
    }

    private void setSwapIndex(AbstractEntityCompanion entity, int idx){
        if(this.SwapHand() == InteractionHand.MAIN_HAND) {
            entity.setItemSwapIndexMainHand(idx);
        }
        else{
            entity.setItemSwapIndexOffHand(idx);
        }
    }

    protected abstract InteractionHand SwapHand();

}
