package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.items.ItemStackHandler;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

import static net.minecraft.world.item.Items.ENCHANTED_GOLDEN_APPLE;
import static net.minecraft.world.item.Items.GOLDEN_APPLE;

public class InventorySensor<E extends AbstractEntityCompanion> extends ExtendedSensor<E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(RegisterAI.FOOD_INDEX.get(), RegisterAI.HEAL_POTION_INDEX.get(), RegisterAI.REGENERATION_POTION_INDEX.get(), RegisterAI.TOTEM_INDEX.get(), RegisterAI.TORCH_INDEX.get());
    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        ItemStackHandler inventory = entity.getInventory();

        for(MemoryModuleType<?> type:memoriesUsed()){
            BrainUtils.clearMemory(entity, type);
        }

        for(int i=0; i<inventory.getSlots(); i++){
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack.isEdible()){
                boolean ispoisonous = false;
                FoodProperties foodProperties = stack.getItem().getFoodProperties(stack, entity);

                if(foodProperties == null){
                    return;
                }

                if(!foodProperties.getEffects().isEmpty()){
                    for(int j =0; j<foodProperties.getEffects().size(); j++){
                        MobEffectInstance instance = foodProperties.getEffects().get(j).getFirst();
                        if(instance.getEffect().getCategory() == MobEffectCategory.HARMFUL){
                            ispoisonous = true;
                            break;
                        }
                    }
                }

                if(!ispoisonous){
                    BrainUtils.setMemory(entity, RegisterAI.FOOD_INDEX.get(), i);
                }
            }
            else if (stack.getItem() == Items.TOTEM_OF_UNDYING){
                BrainUtils.setMemory(entity, RegisterAI.TOTEM_INDEX.get(), i);
            }
            else if (stack.getItem() == Items.TORCH || stack.getItem() == Items.SOUL_TORCH){
                BrainUtils.setMemory(entity, RegisterAI.TORCH_INDEX.get(), i);
            }
            else if(stack.getItem() ==GOLDEN_APPLE || stack.getItem() == ENCHANTED_GOLDEN_APPLE){
                BrainUtils.setMemory(entity, RegisterAI.REGENERATION_POTION_INDEX.get(), i);
            }
            else if(!PotionUtils.getPotion(stack).getEffects().isEmpty()){
                List<MobEffectInstance> effects = PotionUtils.getMobEffects(stack);
                if(effects.stream().noneMatch((effect)->effect.getEffect().getCategory() == MobEffectCategory.HARMFUL)){
                    if(effects.stream().anyMatch((effect)->effect.getEffect() == MobEffects.REGENERATION)){
                        BrainUtils.setMemory(entity, RegisterAI.REGENERATION_POTION_INDEX.get(), i);
                    }
                    else if(effects.stream().anyMatch((effect)->effect.getEffect() == MobEffects.HEAL)){
                        BrainUtils.setMemory(entity, RegisterAI.HEAL_POTION_INDEX.get(), i);
                    }
                }
            }
        }
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return RegisterAI.INVSENSOR.get();
    }
}
