package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import javafx.util.Pair;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;

import static net.minecraft.util.Hand.OFF_HAND;

public class CompanionsUseTotem extends Goal {

    private final AbstractEntityCompanion companion;
    private Pair<ItemStack, Integer> TotemStack = new Pair<>(ItemStack.EMPTY, -1);

    public CompanionsUseTotem(AbstractEntityCompanion companion){
        this.companion = companion;
    }

    @Override
    public boolean shouldExecute() {

        int lastattacktime = this.companion.ticksExisted - this.companion.getRevengeTimer();

        if(this.companion.getHealth() > 8 || lastattacktime>400){
            return false;
        }
        else{
            this.TotemStack = this.hasTotem();
            return this.TotemStack.getKey() != ItemStack.EMPTY;
        }
    }

    @Override
    public void startExecuting() {
        ItemStack buffer = this.companion.getHeldItemOffhand();
        this.companion.setHeldItem(OFF_HAND, this.TotemStack.getKey());
        this.companion.getInventory().setStackInSlot(this.hasTotem().getValue(), buffer);
        this.companion.setItemSwapIndexOffHand(this.TotemStack.getValue());
    }

    @Override
    public boolean shouldContinueExecuting() {
        boolean flag1 = (this.companion.getFireTimer() > 0 || this.companion.isPotionActive(Effects.WITHER) || this.companion.ticksExisted - this.companion.getRevengeTimer()<400);
        boolean value = this.companion.getHealth()<8;
        boolean canuseTotem = this.companion.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING || this.hasTotem().getKey() != ItemStack.EMPTY;
        return value && flag1 && canuseTotem;
    }

    @Override
    public void resetTask() {
        ItemStack buffer = this.companion.getHeldItemOffhand();
        this.companion.setHeldItem(OFF_HAND, this.companion.getInventory().getStackInSlot(this.companion.getItemSwapIndexOffHand()));
        this.companion.getInventory().setStackInSlot(this.companion.getItemSwapIndexOffHand(), buffer);
        this.companion.setItemSwapIndexOffHand(-1);
    }

    @Override
    public void tick() {
        if(this.companion.getHeldItemOffhand() == ItemStack.EMPTY && this.companion.isPotionActive(Effects.REGENERATION) && this.companion.isPotionActive(Effects.ABSORPTION)){
            this.resetTask();
        }
    }

    private Pair<ItemStack, Integer> hasTotem(){
        for(int i = 0; i < this.companion.getInventory().getSlots(); i++){
            ItemStack stack = this.companion.getInventory().getStackInSlot(i);
            if(stack.getItem() == Items.TOTEM_OF_UNDYING){
                return new Pair<>(stack, i);
            }
        }
        return new Pair<>(ItemStack.EMPTY, -1);
    }
}
