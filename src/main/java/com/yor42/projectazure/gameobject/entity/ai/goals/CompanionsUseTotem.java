package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.tuple.ImmutablePair;

import static net.minecraft.world.InteractionHand.OFF_HAND;


public class CompanionsUseTotem extends Goal {

    private final AbstractEntityCompanion companion;
    private ImmutablePair<ItemStack, Integer> TotemStack = new ImmutablePair<>(ItemStack.EMPTY, -1);

    public CompanionsUseTotem(AbstractEntityCompanion companion){
        this.companion = companion;
    }

    @Override
    public boolean canUse() {

        int lastattacktime = this.companion.tickCount - this.companion.getLastHurtByMobTimestamp();

        if(this.companion.isCriticallyInjured()){
            return false;
        }

        if(this.companion.getHealth() > 8 || lastattacktime>400){
            return false;
        }
        else{
            this.TotemStack = this.hasTotem();
            return this.TotemStack.getKey() != ItemStack.EMPTY;
        }
    }

    @Override
    public void start() {
        ItemStack buffer = this.companion.getOffhandItem();
        this.companion.setItemInHand(OFF_HAND, this.TotemStack.getKey());
        this.companion.getInventory().setStackInSlot(this.hasTotem().getValue(), buffer);
        this.companion.setItemSwapIndexOffHand(this.TotemStack.getValue());
    }

    @Override
    public boolean canContinueToUse() {
        boolean flag1 = (this.companion.getRemainingFireTicks() > 0 || this.companion.hasEffect(MobEffects.WITHER) || this.companion.tickCount - this.companion.getLastHurtByMobTimestamp()<400);
        boolean value = this.companion.getHealth()<8;
        boolean canuseTotem = this.companion.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING || this.hasTotem().getKey() != ItemStack.EMPTY;
        return value && flag1 && canuseTotem;
    }

    @Override
    public void stop() {
        ItemStack buffer = this.companion.getOffhandItem();
        this.companion.setItemInHand(OFF_HAND, this.companion.getInventory().getStackInSlot(this.companion.getItemSwapIndexOffHand()));
        this.companion.getInventory().setStackInSlot(this.companion.getItemSwapIndexOffHand(), buffer);
        this.companion.setItemSwapIndexOffHand(-1);
    }

    @Override
    public void tick() {
        if(this.companion.getOffhandItem() == ItemStack.EMPTY && this.companion.hasEffect(MobEffects.REGENERATION) && this.companion.hasEffect(MobEffects.ABSORPTION)){
            this.stop();
        }
    }

    private ImmutablePair<ItemStack, Integer> hasTotem(){
        for(int i = 0; i < this.companion.getInventory().getSlots(); i++){
            ItemStack stack = this.companion.getInventory().getStackInSlot(i);
            if(stack.getItem() == Items.TOTEM_OF_UNDYING){
                return new ImmutablePair<>(stack, i);
            }
        }
        return new ImmutablePair<>(ItemStack.EMPTY, -1);
    }
}
