package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static net.minecraft.fluid.Fluids.WATER;
import staticnet.minecraft.world.level.material.Fluids*;

public class InventorySensor extends Sensor<AbstractEntityCompanion> {
    @Override
    protected void doTick(@Nonnull ServerLevel world, @Nonnull AbstractEntityCompanion entity) {
        Brain<AbstractEntityCompanion> brain = entity.getBrain();
        ItemStackHandler inventory = entity.getInventory();
        boolean foundTotem = brain.hasMemoryValue(RegisterAI.TOTEM_INDEX.get());
        boolean foundFood = false, foundHealPotion= false, foundRegenerationPotion= false, foundTorch= false, foundFireExtinguisher= false, foundFallbreaker =  false;
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
                        MobEffectInstance instance = stack.getItem().getFoodProperties().getEffects().get(j).getFirst();
                        if(instance.getEffect().getCategory() == MobEffectCategory.HARMFUL){
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
                else if(!PotionUtils.getPotion(stack).getEffects().isEmpty()){
                    List<MobEffectInstance> effects = PotionUtils.getMobEffects(stack);
                    if(effects.stream().noneMatch((effect)->effect.getEffect().getCategory() == MobEffectCategory.HARMFUL)){
                        if(!foundRegenerationPotion && effects.stream().anyMatch((effect)->effect.getEffect() == MobEffects.REGENERATION)){
                            brain.setMemory(RegisterAI.REGENERATION_POTION_INDEX.get(), i);
                            foundRegenerationPotion = true;
                        }
                        else if(!foundHealPotion && effects.stream().anyMatch((effect)->effect.getEffect() == MobEffects.HEAL)){
                            brain.setMemory(RegisterAI.HEAL_POTION_INDEX.get(), i);
                            foundHealPotion = true;
                        }
                        else if(!foundFireExtinguisher && effects.stream().anyMatch((effect)->effect.getEffect() == MobEffects.FIRE_RESISTANCE)){
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
