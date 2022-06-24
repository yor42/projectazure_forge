package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static net.minecraft.fluid.Fluids.WATER;
import static net.minecraft.item.Items.*;

public class InventorySensor extends Sensor<AbstractEntityCompanion> {
    @Override
    protected void doTick(@Nonnull ServerWorld world, @Nonnull AbstractEntityCompanion entity) {
        Brain<AbstractEntityCompanion> brain = entity.getBrain();
        ItemStackHandler inventory = entity.getInventory();
        boolean foundTotem = brain.hasMemoryValue(RegisterAI.TOTEM_INDEX.get());
        boolean foundFood = brain.hasMemoryValue(RegisterAI.FOOD_INDEX.get()), foundHealPotion= brain.hasMemoryValue(RegisterAI.HEAL_POTION_INDEX.get()), foundRegenerationPotion= brain.hasMemoryValue(RegisterAI.REGENERATION_POTION_INDEX.get()), foundTorch= brain.hasMemoryValue(RegisterAI.TORCH_INDEX.get()), foundFireExtinguisher= brain.hasMemoryValue(RegisterAI.FIRE_EXTINGIGH_ITEM.get()), foundFallbreaker =  brain.hasMemoryValue(RegisterAI.FALL_BREAK_ITEM_INDEX.get());
        for (int i = 0; i < inventory.getSlots(); i++){
            ItemStack stack = inventory.getStackInSlot(i);
            Item item = stack.getItem();

            if(foundFood && foundTotem && foundTorch && foundHealPotion && foundRegenerationPotion && foundFallbreaker && foundFireExtinguisher){
                break;
            }

            if(!foundTotem && item == Items.TOTEM_OF_UNDYING){
                brain.setMemory(RegisterAI.TOTEM_INDEX.get(), i);
                foundTotem = true;
            }
            else if(!foundTorch && item == Items.TORCH || item == Items.SOUL_TORCH){
                brain.setMemory(RegisterAI.TORCH_INDEX.get(), i);
                foundTorch = true;
            }
            else if(!foundFood && stack.isEdible()){
                boolean ispoisonous = false;
                if(!stack.getItem().getFoodProperties().getEffects().isEmpty()){
                    for(int j =0; j<stack.getItem().getFoodProperties().getEffects().size(); j++){
                        EffectInstance instance = stack.getItem().getFoodProperties().getEffects().get(j).getFirst();
                        if(instance.getEffect().getCategory() == EffectType.HARMFUL){
                            ispoisonous = true;
                        }
                    }
                }

                if(!ispoisonous){
                    brain.setMemory(RegisterAI.FOOD_INDEX.get(), i);
                    foundFood = true;
                }
            }
            else if(FluidUtil.getFluidContained(stack).map((fluid)->fluid.getFluid() == WATER && fluid.getAmount()>=1000).orElse(false)){
                if(!foundFallbreaker){
                    brain.setMemory(RegisterAI.FALL_BREAK_ITEM_INDEX.get(),i);
                    foundFallbreaker = true;
                }
                if(!foundFireExtinguisher){
                    brain.setMemory(RegisterAI.FIRE_EXTINGIGH_ITEM.get(),i);
                    foundFireExtinguisher = true;
                }
            }
            else if(!foundFallbreaker && item == COBWEB && item instanceof BedItem){
                brain.setMemory(RegisterAI.FALL_BREAK_ITEM_INDEX.get(),i);
                foundFallbreaker = true;
            }
            else if(!foundHealPotion || !foundRegenerationPotion ||!foundFireExtinguisher){
                if(!(foundRegenerationPotion && foundFireExtinguisher) && (item ==GOLDEN_APPLE || item == ENCHANTED_GOLDEN_APPLE)){
                    brain.setMemory(RegisterAI.REGENERATION_POTION_INDEX.get(), i);
                    brain.setMemory(RegisterAI.FIRE_EXTINGIGH_ITEM.get(),i);
                    foundRegenerationPotion = true;
                    foundFireExtinguisher = true;
                }
                else if(item instanceof PotionItem){
                    List<EffectInstance> effects = PotionUtils.getMobEffects(stack);
                    if(effects.stream().noneMatch((effect)->effect.getEffect().getCategory() == EffectType.HARMFUL)){
                        if(!foundRegenerationPotion && effects.stream().anyMatch((effect)->effect.getEffect() == Effects.REGENERATION)){
                            brain.setMemory(RegisterAI.REGENERATION_POTION_INDEX.get(), i);
                            foundRegenerationPotion = true;
                        }
                        else if(!foundHealPotion && effects.stream().anyMatch((effect)->effect.getEffect() == Effects.HEAL)){
                            brain.setMemory(RegisterAI.HEAL_POTION_INDEX.get(), i);
                            foundHealPotion = true;
                        }
                        else if(!foundFireExtinguisher && effects.stream().anyMatch((effect)->effect.getEffect() == Effects.FIRE_RESISTANCE)){
                            brain.setMemory(RegisterAI.FIRE_EXTINGIGH_ITEM.get(), i);
                            foundFireExtinguisher = true;
                        }
                    }
                }
            }
        }
        if(!foundFood){
            brain.eraseMemory(RegisterAI.FOOD_INDEX.get());
        }
        if(!foundTotem){
            brain.eraseMemory(RegisterAI.TOTEM_INDEX.get());
        }
        if(!foundHealPotion){
            brain.eraseMemory(RegisterAI.HEAL_POTION_INDEX.get());
        }
        if(!foundRegenerationPotion){
            brain.eraseMemory(RegisterAI.REGENERATION_POTION_INDEX.get());
        }
        if(!foundFallbreaker){
            brain.eraseMemory(RegisterAI.FALL_BREAK_ITEM_INDEX.get());
        }
        if(!foundFireExtinguisher){
            brain.eraseMemory(RegisterAI.FIRE_EXTINGIGH_ITEM.get());
        }
        if(!foundTorch){
            brain.eraseMemory(RegisterAI.FIRE_EXTINGIGH_ITEM.get());
        }

    }

    @Nonnull
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(RegisterAI.FOOD_INDEX.get(), RegisterAI.HEAL_POTION_INDEX.get(), RegisterAI.REGENERATION_POTION_INDEX.get(), RegisterAI.TOTEM_INDEX.get(), RegisterAI.TORCH_INDEX.get(), RegisterAI.FIRE_EXTINGIGH_ITEM.get(), RegisterAI.FALL_BREAK_ITEM_INDEX.get());
    }
}
