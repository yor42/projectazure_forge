package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;

import static com.yor42.projectazure.setup.register.RegisterAI.TOTEM_INDEX;

public class CompanionUseTotemTask extends Behavior<AbstractEntityCompanion> {

    private int previousSwitchIndex = -1;
    private boolean changedItemInHand = false;

    public CompanionUseTotemTask() {
        super(ImmutableMap.of(TOTEM_INDEX.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, AbstractEntityCompanion entity) {
        int lastattacktime = entity.tickCount - entity.getLastHurtByMobTimestamp();
        return entity.getHealth()<=8 && lastattacktime<=300;
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        if(!this.getTotemHand(entity).isPresent()) {
            ItemStack buffer = entity.getOffhandItem();
            this.previousSwitchIndex = entity.getItemSwapIndexOffHand();
            entity.getBrain().getMemory(TOTEM_INDEX.get()).ifPresent((index) -> {
                entity.setItemInHand(InteractionHand.OFF_HAND, entity.getInventory().getStackInSlot(index));
                entity.getInventory().setStackInSlot(index, buffer);
                entity.setItemSwapIndexOffHand(index);
            });
            this.changedItemInHand = true;
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        boolean flag1 = (entity.getRemainingFireTicks() > 0 || entity.hasEffect(MobEffects.WITHER) || entity.tickCount - entity.getLastHurtByMobTimestamp()<400);
        boolean value = entity.getHealth()<8;
        boolean canuseTotem = entity.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING;
        return value && flag1 && canuseTotem;
    }

    @Override
    protected void tick(ServerLevel world, AbstractEntityCompanion entity, long p_212833_3_) {
        if(!this.getTotemHand(entity).isPresent() && entity.hasEffect(MobEffects.REGENERATION) && entity.hasEffect(MobEffects.ABSORPTION)){
            this.doStop(world, entity, p_212833_3_);
        }
    }

    @Override
    protected void stop(ServerLevel world, AbstractEntityCompanion entity, long p_212835_3_) {
        if (this.changedItemInHand) {
            ItemStack buffer = entity.getOffhandItem();
            entity.setItemInHand(InteractionHand.OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
            entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer);

            if (this.previousSwitchIndex > -1) {
                entity.setItemSwapIndexOffHand(this.previousSwitchIndex);
                ItemStack buffer2 = entity.getOffhandItem();
                entity.setItemInHand(InteractionHand.OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
                entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer2);
            }

            entity.setItemSwapIndexOffHand(-1);
        }
    }

    private Optional<InteractionHand> getTotemHand(AbstractEntityCompanion entity){
        for(InteractionHand hand : InteractionHand.values()){
            Item item = entity.getItemInHand(hand).getItem();
            if(item == Items.TOTEM_OF_UNDYING){
                return Optional.of(hand);
            }
        }
        return Optional.empty();
    }
}
