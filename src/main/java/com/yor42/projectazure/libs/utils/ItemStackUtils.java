package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.RiggingInventoryCapability;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentTorpedo;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Color;
import net.minecraftforge.items.ItemStackHandler;

import java.lang.annotation.Target;
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
            return getCurrentDamage(stack) >= ((ItemDestroyable)stack.getItem()).getMaxHP();
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
            int EquipmentCount = riggingItem.getEquipments(riggingStack).getSlots();
            int DamageRiggingCount = 1;

            if(shouldDamageMultiple)
                DamageRiggingCount = rollDamagingRiggingCount(EquipmentCount);

            int MainDamageIndex = generateRandomInt(EquipmentCount-1);

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

    public static ItemStack serializePlane(AbstractEntityPlanes plane) {
        ItemStack planeStack = new ItemStack(plane.getPlaneItem());
        setCurrentDamage(planeStack, (int) (plane.getMaxHealth()-plane.getHealth()));
        CompoundNBT nbt = planeStack.getOrCreateTag();

        if(!plane.hasPayload()){
            nbt.putInt("armDelay", plane.getPlaneItem().getreloadTime());
        }

        nbt.putInt("fuel", ((ItemEquipmentPlaneBase)plane.getPlaneItem()).getMaxOperativeTime()-plane.ticksExisted);
        return planeStack;
    }



    public static ItemStack getPreparedPlane(EntityKansenBase entity, ItemStackHandler hanger){
        if(entity.getRigging().getItem() instanceof ItemRiggingBase) {

            if(hanger != null) {
                for (int i = 0; i < hanger.getSlots(); i++) {
                    if(hanger.getStackInSlot(i).getItem() instanceof ItemEquipmentPlaneBase) {
                        if (getCurrentHP(hanger.getStackInSlot(i)) > ((ItemEquipmentPlaneBase) hanger.getStackInSlot(i).getItem()).getMaxHP() * 0.6) {


                            //check if its ready to go out, etc... yada yada
                            if (hanger.getStackInSlot(i).getOrCreateTag().getInt("armDelay") <= 0 && hanger.getStackInSlot(i).getOrCreateTag().getInt("fuel") >= getRequiredMinimumFuel(entity, (ItemEquipmentPlaneBase) hanger.getStackInSlot(i).getItem())) {
                                return hanger.getStackInSlot(i);
                            }
                        }
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static void usePlane(EntityKansenBase entity, ItemStack plane, ItemStackHandler hanger){
        if(entity.getRigging().getItem() instanceof ItemRiggingBase) {
            ItemRiggingBase riggingItem = (ItemRiggingBase) entity.getRigging().getItem();

            if(hanger != null) {
                for (int i = 0; i < hanger.getSlots(); i++) {
                    if(hanger.getStackInSlot(i).getItem() instanceof ItemEquipmentPlaneBase) {
                        if (hanger.getStackInSlot(i) == plane) {
                            hanger.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }


    public static boolean hasPlanes(ItemStack rigging){
        if(rigging.getItem() instanceof ItemRiggingBase) {
            ItemRiggingBase riggingItem = (ItemRiggingBase) rigging.getItem();
            ItemStackHandler hanger = new RiggingInventoryCapability(rigging).getHangar();
            if(hanger != null) {
                if (hanger.getSlots() > 0) {
                    for (int i = 0; i < hanger.getSlots(); i++) {
                        boolean flag = hanger.getStackInSlot(i).getItem() instanceof ItemEquipmentBase;

                        if(flag) {
                            ItemEquipmentBase item = (ItemEquipmentBase) hanger.getStackInSlot(i).getItem();
                            if (item.getSlot() == enums.SLOTTYPE.PLANE) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void serializeInventory(ItemStack TargetStack, ItemStackHandler inventory){
        TargetStack.getOrCreateTag().put("Inventory", inventory.serializeNBT());
    }


    public static ItemStack getPreparedWeapon(ItemStack rigging, enums.SLOTTYPE slottype, MobEntity Shooter){
        if(rigging.getItem() instanceof ItemRiggingBase){
            ItemRiggingBase riggingItem = (ItemRiggingBase) rigging.getItem();
            ItemStackHandler equipments = riggingItem.getEquipments(rigging);
            for(int i = 0; i<equipments.getSlots(); i++){
                if(equipments.getStackInSlot(i).getItem() instanceof ItemEquipmentBase) {
                    if (((ItemEquipmentBase) equipments.getStackInSlot(i).getItem()).getSlot() == slottype) {
                        if (getDelayofEquipment(equipments.getStackInSlot(i)) <= 0 && !isDestroyed(equipments.getStackInSlot(i))) {
                            if(slottype == enums.SLOTTYPE.TORPEDO){
                                if (!isOutOfAmmo(equipments.getStackInSlot(i))){
                                    return equipments.getStackInSlot(i);
                                }
                            }
                            else {
                                return equipments.getStackInSlot(i);
                            }
                        }
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean isPlaneFuelReady(MobEntity entity, ItemStack planeStack){
        if(planeStack.getItem() instanceof ItemEquipmentPlaneBase) {
            return planeStack.getOrCreateTag().getInt("fuel") >= getRequiredMinimumFuel(entity, (ItemEquipmentPlaneBase) planeStack.getItem());
        }
        return false;
    }

    public static int getPlaneFuel(ItemStack planeStack){
        if(planeStack.getItem() instanceof ItemEquipmentPlaneBase) {
            return planeStack.getOrCreateTag().getInt("fuel");
        }
        return 0;
    }

    public static float getRequiredMinimumFuel(MobEntity Shooter, ItemEquipmentPlaneBase planeItem){
        //Movement speed 0.1 = roughly 0.2 block/tick
        if(Shooter.getAttackTarget() != null && Shooter.getAttackTarget().isAlive()) {
            double distance = Shooter.getDistance(Shooter.getAttackTarget())*2.5;//2 way trip+ another 0.5X of fuel because minecraft entity are stupid
            double speed = planeItem.getMovementSpeed();
            return (int)Math.ceil(Math.abs(distance/speed));
        }
        return 0;
    }



    public static boolean hasAttackableCannon(ItemStack rigging){
        if(rigging.getItem() instanceof ItemRiggingBase){
            ItemRiggingBase riggingItem = (ItemRiggingBase) rigging.getItem();
            ItemStackHandler equipments = riggingItem.getEquipments(rigging);
            for(int i = 0; i<equipments.getSlots(); i++){
                if(equipments.getStackInSlot(i).getItem() instanceof ItemEquipmentBase) {
                    if(((ItemEquipmentBase) equipments.getStackInSlot(i).getItem()).getSlot() == enums.SLOTTYPE.GUN){
                        return !isDestroyed(equipments.getStackInSlot(i));
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasGunOrTorpedo(ItemStack riggingStack){
        if(riggingStack.getItem() instanceof ItemRiggingBase) {
            ItemStackHandler equipments = ((ItemRiggingBase) riggingStack.getItem()).getEquipments(riggingStack);
            for(int i = 0; i<equipments.getSlots(); i++){
                if(equipments.getStackInSlot(i).getItem() instanceof ItemEquipmentBase){
                    if(((ItemEquipmentBase) equipments.getStackInSlot(i).getItem()).getSlot() == enums.SLOTTYPE.TORPEDO || ((ItemEquipmentBase) equipments.getStackInSlot(i).getItem()).getSlot() == enums.SLOTTYPE.GUN){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean canUseTorpedo(ItemStack riggingStack){
        if(riggingStack.getItem() instanceof ItemRiggingBase) {
            return getPreparedWeapon(riggingStack, enums.SLOTTYPE.TORPEDO, null) != ItemStack.EMPTY;
        }
        else return false;
    }

    public static boolean canUseCannon(ItemStack riggingStack){
        if(riggingStack.getItem() instanceof ItemRiggingBase) {
            return getPreparedWeapon(riggingStack, enums.SLOTTYPE.GUN, null) != ItemStack.EMPTY;
        }
        else return false;
    }

    public static boolean isOutOfAmmo(ItemStack equipment){
        CompoundNBT compoundNBT = equipment.getOrCreateTag();
        if(equipment.getItem() instanceof ItemEquipmentTorpedo){
            return compoundNBT.getInt("UsedAmmo")>= ((ItemEquipmentTorpedo) equipment.getItem()).getMaxAmmoCap();
        }
        return true;
    }

    public static void useTorpedoAmmo(ItemStack torpedo){
        CompoundNBT compoundNBT = torpedo.getOrCreateTag();
        int prevammo =  compoundNBT.getInt("UsedAmmo");
        compoundNBT.putInt("UsedAmmo", prevammo+1);
    }

    public static int getRemainingAmmo(ItemStack equipment){
        CompoundNBT compoundNBT = equipment.getOrCreateTag();
        if(equipment.getItem() instanceof ItemEquipmentTorpedo){
            return ((ItemEquipmentTorpedo) equipment.getItem()).getMaxAmmoCap() - compoundNBT.getInt("UsedAmmo");
        }
        return 0;
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
