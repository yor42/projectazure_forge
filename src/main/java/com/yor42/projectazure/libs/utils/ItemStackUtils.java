package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Color;
import net.minecraftforge.items.ItemStackHandler;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.yor42.projectazure.libs.utils.MathUtil.generateRandomInt;
import static com.yor42.projectazure.libs.utils.MathUtil.rollDamagingRiggingCount;

public class ItemStackUtils {

    public static void DamageRiggingorEquipment(float amount, ItemStack stack){
        if(stack.getItem() instanceof ItemDestroyable) {
            int damage = getCurrentDamage(stack);
            int maxDamage = ((ItemDestroyable) stack.getItem()).getMaxHP();
            if (damage + amount <= maxDamage) {
                setCurrentDamage(stack, (int) (damage + amount));
            } else {
                setCurrentDamage(stack, maxDamage);
            }
        }
        else{
            Main.LOGGER.warn("Item that isn't Rigging or Equipment Tried to run Damage Code.\n" +
                    "well its not a big problem(shouldn't break anything) but it won't do anything either.\n" +
                    "but whoever you are here's some pesky rickroll to encourage you to fix this\n\n" +
                    "We are no strangers to love, You know the rules, and so do i.\n" +
                    "A full commitment's what I'm thinking of\n" +
                    "You wouldn't get this from any other guy");
        }

    }

    public static boolean isDestroyed(ItemStack stack){
        if(stack.getItem() instanceof ItemDestroyable) {
            return getCurrentDamage(stack) <= ((ItemDestroyable)stack.getItem()).getMaxHP();
        }
        return true;
    }

    public static int getCurrentHP(ItemStack stack){
        if(stack.getItem() instanceof ItemDestroyable){
            return ((ItemDestroyable) stack.getItem()).getMaxHP()-getCurrentDamage(stack);
        }
        else return 0;
    }

    public static void setCurrentDamage(ItemStack stack, int value){
        stack.getOrCreateTag().putInt("currentdamage", value);
    }

    public static int getCurrentDamage(ItemStack stack){
        return stack.getOrCreateTag().getInt("currentdamage");
    }

    public static boolean DamageComponent(float damage, ItemStack riggingStack, boolean shouldDamageMultiple){
        if(riggingStack.getItem() instanceof ItemRiggingBase){
            ItemRiggingBase riggingItem = (ItemRiggingBase) riggingStack.getItem();
            int EquipmentCount = riggingItem.getEquipmentCount(riggingStack);
            int DamageRiggingCount = 1;

            if(shouldDamageMultiple)
                DamageRiggingCount = rollDamagingRiggingCount(EquipmentCount);

            int MainDamageIndex = generateRandomInt(EquipmentCount);

            ItemStack MainStack = riggingItem.getEquipments(riggingStack).getStackInSlot(MainDamageIndex);
            if (MainStack.getItem() instanceof ItemEquipmentBase) {
                DamageRiggingorEquipment(damage,MainStack);
            }

            if(DamageRiggingCount>1){
                Set<Integer> equipmentIndex = new LinkedHashSet<>();
                while (equipmentIndex.size()<DamageRiggingCount-1){
                    int random = generateRandomInt(EquipmentCount);
                    if(random!=MainDamageIndex) {
                        equipmentIndex.add(random);
                    }
                }
                Integer[] intArray = new Integer[equipmentIndex.size()];
                intArray =  equipmentIndex.toArray(intArray);
                for (Integer integer : intArray) {
                    ItemStack equipmentStack = riggingItem.getEquipments(riggingStack).getStackInSlot(integer);
                    if (equipmentStack.getItem() instanceof ItemEquipmentBase) {
                        DamageRiggingorEquipment((float) (damage*0.4),equipmentStack);
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    public static ItemStack getPreparedCannon(ItemStack rigging){
        if(rigging.getItem() instanceof ItemRiggingBase){
            ItemRiggingBase riggingItem = (ItemRiggingBase) rigging.getItem();
            ItemStackHandler equipments = riggingItem.getEquipments(rigging);
            for(int i = 0; i<equipments.getSlots(); i++){
                if(equipments.getStackInSlot(i).getItem() instanceof ItemEquipmentBase) {
                    if (((ItemEquipmentBase) equipments.getStackInSlot(i).getItem()).getSlot() == enums.SLOTTYPE.GUN) {
                        if (getDelayofEquipment(equipments.getStackInSlot(i)) <= 0) {
                            return equipments.getStackInSlot(i);
                        }
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean canUseCannon(ItemStack riggingStack){
        return getPreparedCannon(riggingStack) != ItemStack.EMPTY;
    }

    public static int getDelayofEquipment(ItemStack Equipment){
        CompoundNBT tags = Equipment.getOrCreateTag();
        return tags.getInt("delay");
    }

    public static void setEquipmentDelay (ItemStack equipment){
        if(equipment.getItem() instanceof ItemEquipmentBase){
            CompoundNBT tags = equipment.getOrCreateTag();
            ItemEquipmentBase equipmentItem = (ItemEquipmentBase)(equipment.getItem());
            tags.putInt("delay", equipmentItem.getFiredelay());
        }
    }

    public static Color getHPColor(ItemStack stack){
        float HP_Percent = 0;
        String ColorHex = "69b82d";
        if(stack.getItem() instanceof ItemDestroyable){
            HP_Percent = 1.0F-((float)getCurrentDamage(stack)/ ((ItemDestroyable) stack.getItem()).getMaxHP());
        }

        if(HP_Percent<=0.1){
            ColorHex = "e71f19";
        }
        else if(HP_Percent <= 0.4){
            ColorHex = "fff209";
        }

        return Color.fromHex("#"+ColorHex);
    }

}
