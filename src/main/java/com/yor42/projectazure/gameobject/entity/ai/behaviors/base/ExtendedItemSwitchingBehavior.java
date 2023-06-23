package com.yor42.projectazure.gameobject.entity.ai.behaviors.base;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class ExtendedItemSwitchingBehavior<E extends AbstractEntityCompanion> extends ExtendedBehaviour<E> {

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
        if(this.getSwapIndex(entity)<0){
            return;
        }

        ItemStack Buffer = entity.getItemInHand(SwapHand());
        entity.setItemInHand(this.SwapHand(), entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        setSwapIndex(entity, index);
    }

    protected boolean ChangeItemwithMemory(MemoryModuleType<Integer> memory, AbstractEntityCompanion entity){
        Integer idx = BrainUtils.getMemory(entity, memory);
        if(idx == null){
            return false;
        }
        this.ChangeItem(idx, entity);
        return true;
    }


    @SafeVarargs
    protected final void ChangeItemWithPriority(AbstractEntityCompanion entity, MemoryModuleType<Integer>... memories){
        for(MemoryModuleType<Integer> memory:memories){
            if(ChangeItemwithMemory(memory, entity)){
                break;
            }
        }
    }

    private void setSwapIndex(AbstractEntityCompanion entity, int idx){
        if(this.SwapHand() == InteractionHand.MAIN_HAND) {
            entity.setItemSwapIndexMainHand(idx);
        }
        else{
            entity.setItemSwapIndexOffHand(idx);
        }
    }

    @Nullable
    protected static InteractionHand getItemHand(AbstractEntityCompanion entity, Predicate<ItemStack> predicate){
        for(InteractionHand hand : InteractionHand.values()){
            ItemStack stack = entity.getItemInHand(hand);
            if(predicate.test(stack)){
                return hand;
            }
        }
        return null;
    }

    private int getSwapIndex(AbstractEntityCompanion entity){
        return entity.getItemSwapIndex(SwapHand());
    }

    protected abstract InteractionHand SwapHand();

}
