package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

import static com.yor42.projectazure.setup.register.registerManager.TOTEM_INDEX;
import static net.minecraft.util.Hand.OFF_HAND;

public class CompanionUseTotemTask extends Task<AbstractEntityCompanion> {

    private int previousSwitchIndex = -1;
    private boolean changedItemInHand = false;

    public CompanionUseTotemTask() {
        super(ImmutableMap.of(TOTEM_INDEX.get(), MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld world, AbstractEntityCompanion entity) {
        int lastattacktime = entity.tickCount - entity.getLastHurtByMobTimestamp();
        return entity.getHealth()<=8 && lastattacktime<=300;
    }

    @Override
    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        if(!this.getTotemHand(entity).isPresent()) {
            ItemStack buffer = entity.getOffhandItem();
            this.previousSwitchIndex = entity.getItemSwapIndexOffHand();
            entity.getBrain().getMemory(TOTEM_INDEX.get()).ifPresent((index) -> {
                entity.setItemInHand(OFF_HAND, entity.getInventory().getStackInSlot(index));
                entity.getInventory().setStackInSlot(index, buffer);
                entity.setItemSwapIndexOffHand(index);
            });
            this.changedItemInHand = true;
        }
    }

    @Override
    protected boolean canStillUse(ServerWorld p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        boolean flag1 = (entity.getRemainingFireTicks() > 0 || entity.hasEffect(Effects.WITHER) || entity.tickCount - entity.getLastHurtByMobTimestamp()<400);
        boolean value = entity.getHealth()<8;
        boolean canuseTotem = entity.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING;
        return value && flag1 && canuseTotem;
    }

    @Override
    protected void tick(ServerWorld world, AbstractEntityCompanion entity, long p_212833_3_) {
        if(!this.getTotemHand(entity).isPresent() && entity.hasEffect(Effects.REGENERATION) && entity.hasEffect(Effects.ABSORPTION)){
            this.doStop(world, entity, p_212833_3_);
        }
    }

    @Override
    protected void stop(ServerWorld world, AbstractEntityCompanion entity, long p_212835_3_) {
        if (this.changedItemInHand) {
            ItemStack buffer = entity.getOffhandItem();
            entity.setItemInHand(OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
            entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer);

            if (this.previousSwitchIndex > -1) {
                entity.setItemSwapIndexOffHand(this.previousSwitchIndex);
                ItemStack buffer2 = entity.getOffhandItem();
                entity.setItemInHand(OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
                entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer2);
            }

            entity.setItemSwapIndexOffHand(-1);
        }
    }

    private Optional<Hand> getTotemHand(AbstractEntityCompanion entity){
        for(Hand hand : Hand.values()){
            Item item = entity.getItemInHand(hand).getItem();
            if(item == Items.TOTEM_OF_UNDYING){
                return Optional.of(hand);
            }
        }
        return Optional.empty();
    }
}
