package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.items.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.ItemRiggingBase;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.yor42.projectazure.libs.utils.MathUtil.generateRandomInt;
import static com.yor42.projectazure.libs.utils.MathUtil.rollDamagingRiggingCount;

public class ItemStackUtils {

    public static void DamageRiggingorEquipment(float amount, ItemStack stack){
        int hp = getCurrentHP(stack);
        if(hp-amount>=0){
            setCurrentHP(stack, (int) (hp-amount));
        }
        else{
            setCurrentHP(stack, 0);
        }
    }

    public static void setCurrentHP(ItemStack stack, int value){
        stack.getOrCreateTag().putInt("HP", value);
    }

    public static int getCurrentHP(ItemStack stack){
        return stack.getOrCreateTag().getInt("HP");
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

}
