package com.yor42.projectazure.libs.utils;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.multiinv.CapabilityMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.interfaces.IItemDestroyable;
import com.yor42.projectazure.libs.enums;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.yor42.projectazure.libs.utils.MathUtil.generateRandomInt;
import static com.yor42.projectazure.libs.utils.MathUtil.rollDamagingRiggingCount;

public class ItemStackUtils {

    public static boolean DamageItem(float amount, ItemStack stack){
        if(stack.getItem() instanceof IItemDestroyable) {
            int damage = getCurrentDamage(stack);
            int maxDamage = ((IItemDestroyable) stack.getItem()).getMaxHP();
            if (damage + amount <= maxDamage) {
                setCurrentDamage(stack, (int) (damage + amount));
                return true;
            } else {
                setCurrentDamage(stack, maxDamage);
                return false;
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
        return false;
    }

    public static void RepairItem(ItemStack stack, int amount){
        int damage = getCurrentDamage(stack);
        setCurrentDamage(stack, Math.max(damage - amount, 0));
    }

    public static int getHPColorInt(ItemStack stack){
        if(stack.getItem() instanceof IItemDestroyable) {
            return Mth.hsvToRgb(Math.max(0.0F, 1.0F - (getCurrentDamage(stack) /((IItemDestroyable) stack.getItem()).getMaxHP())) / 3.0F, 1.0F, 1.0F);
        }
        return 0xFFFFFF;
    }

    public static TextColor getHPColor(ItemStack stack){
        return TextColor.fromRgb(getHPColorInt(stack));
    }

    public static boolean isDestroyed(ItemStack stack){
        if(stack.getItem() instanceof IItemDestroyable) {
            return getCurrentDamage(stack) >= ((IItemDestroyable)stack.getItem()).getMaxHP();
        }
        return true;
    }

    public static int getCurrentHP(ItemStack stack){
        if(stack.getItem() instanceof IItemDestroyable){
            return ((IItemDestroyable) stack.getItem()).getMaxHP()-getCurrentDamage(stack);
        }
        else return 0;
    }

    public static void setCurrentDamage(ItemStack stack, int value){
        stack.getOrCreateTag().putInt("currentdamage", value);
    }

    public static void setCurrentHP(ItemStack stack, int HP){
        if(stack.getItem() instanceof IItemDestroyable) {
            stack.getOrCreateTag().putInt("currentdamage", ((IItemDestroyable) stack.getItem()).getMaxHP() - HP);
        }
    }

    public static int getCurrentDamage(ItemStack stack){
        return stack.getOrCreateTag().getInt("currentdamage");
    }

    public static boolean DamageComponent(LivingEntity entity, float damage, ItemStack riggingStack, boolean shouldDamageMultiple) {
        if (riggingStack.getItem() instanceof ItemRiggingBase) {
            IMultiInventory inventories = MultiInvUtil.getCap(riggingStack);
            int slotCount = MultiInvUtil.getSlotCount(inventories, enums.SLOTTYPE.values());
            int toDamage = shouldDamageMultiple ? rollDamagingRiggingCount(slotCount) : 1;
            Set<Integer> slotsToDamage = new HashSet<>();

            int mainDamagedSlot = generateRandomInt(slotCount);
            ItemStack mainDamageStack = MultiInvUtil.getStack(inventories, mainDamagedSlot);
            if (mainDamageStack.getItem() instanceof ItemEquipmentBase) {
                DamageItem(damage, mainDamageStack);
            }
            else{
                mainDamageStack.hurt(MathUtil.generateRandomIntInRange(1, 3), MathUtil.rand, null);
            }
            toDamage--;

            while (slotsToDamage.size() < toDamage) {
                int slot = generateRandomInt(slotCount);
                if (slot != mainDamagedSlot) {
                    slotsToDamage.add(slot);
                }
            }

            for (int slot : slotsToDamage) {
                ItemStack stackToDamage = MultiInvUtil.getStack(inventories, slot);
                if (stackToDamage.getItem() instanceof ItemEquipmentBase) {
                    DamageItem(damage * 0.4f, stackToDamage);
                }else{
                    stackToDamage.hurt(MathUtil.generateRandomIntInRange(1, 3), MathUtil.rand, null);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static ItemStack serializePlane(AbstractEntityPlanes plane) {
        ItemStack planeStack = new ItemStack(plane.getPlaneItem());
        setCurrentDamage(planeStack, (int) (plane.getMaxHealth()-plane.getHealth()));
        CompoundTag nbt = planeStack.getOrCreateTag();

        if(!plane.hasPayload()){
            nbt.putInt("armDelay", plane.getPlaneItem().getreloadTime());
        }

        nbt.putInt("fuel", plane.getPlaneItem().getMaxOperativeTime()-plane.tickCount);
        return planeStack;
    }

    public static boolean isPlaneReady(ItemStack stack, Mob shooter) {
        if (isDestroyed(stack)) {
            return false;
        }

        Item plane = stack.getItem();

        if (!(plane instanceof ItemEquipmentPlaneBase)) {
            return false;
        }

        if (getCurrentHP(stack) <= ((ItemEquipmentPlaneBase) plane).getMaxHP() * 0.6) {
            return false;
        }

        //check if its ready to go out, etc... yada yada
        return stack.getOrCreateTag().getInt("armDelay") <= 0 && stack.getOrCreateTag().getInt("fuel") >= getRequiredMinimumFuel(shooter, (ItemEquipmentPlaneBase) plane);
    }

    public static int getPreparedPlane(EntityKansenBase entity, IItemHandler hanger){
        if(entity.getRigging().getItem() instanceof ItemRiggingBase) {

            if(hanger != null) {
                for (int i = 0; i < hanger.getSlots(); i++) {
                    if(isPlaneReady(hanger.getStackInSlot(i), entity)){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static boolean hasPlanes(ItemStack rigging) {
        return rigging.getCapability(CapabilityMultiInventory.MULTI_INVENTORY_CAPABILITY).map(inventories -> {
            IItemHandler hangar = inventories.getInventory(enums.SLOTTYPE.PLANE);
            for (int slot = 0; slot < hangar.getSlots(); slot++) {
                Item item = hangar.getStackInSlot(slot).getItem();
                if (item instanceof ItemEquipmentBase && ((ItemEquipmentBase) item).getSlot() == enums.SLOTTYPE.PLANE) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }


    @Nonnull
    public static Optional<Pair<enums.SLOTTYPE, Integer>> getPreparedWeapon(LivingEntity shooter, ItemStack rigging, enums.SLOTTYPE... slottypes){

        if(!rigging.getCapability(CapabilityMultiInventory.MULTI_INVENTORY_CAPABILITY).isPresent()){
            return Optional.empty();
        }

        AtomicReference<Pair<enums.SLOTTYPE, Integer>> returnvalue = new AtomicReference<>(null);

        for(enums.SLOTTYPE slot:slottypes) {

                rigging.getCapability(CapabilityMultiInventory.MULTI_INVENTORY_CAPABILITY).ifPresent(inventories -> {
                            IItemHandler equipments = inventories.getInventory(slot);
                            boolean isplane = slot == enums.SLOTTYPE.PLANE;
                            for (int i = 0; i < equipments.getSlots(); i++) {

                                ItemStack stack = equipments.getStackInSlot(i);
                                if(isplane && !isPlaneReady(stack, (Mob) shooter)){
                                    continue;
                                }
                                if (!slot.testPredicate(stack) || isDestroyed(stack) || getDelayofEquipment(equipments.getStackInSlot(i)) > 0) {
                                    continue;
                                } else if (slot == enums.SLOTTYPE.TORPEDO && isOutOfAmmo(stack)) {
                                    continue;
                                }

                                returnvalue.set(new Pair<>(slot, i));
                                return;

                            }
                        }
                );
            }
        return Optional.ofNullable(returnvalue.get());
    }

    public static boolean isPlaneFuelReady(Mob entity, ItemStack planeStack){
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

    public static float getRequiredMinimumFuel(Mob Shooter, ItemEquipmentPlaneBase planeItem){
        //Movement speed 0.1 = roughly 0.2 block/tick
        if(Shooter.getTarget() != null && Shooter.getTarget().isAlive()) {
            double distance = Shooter.distanceTo(Shooter.getTarget())*2.5;//2 way trip+ another 0.5X of fuel because minecraft entity are stupid
            double speed = planeItem.getMovementSpeed();
            return (int)Math.ceil(Math.abs(distance/speed));
        }
        return 0;
    }



    public static boolean hasAttackableCannon(ItemStack rigging) {
        if (rigging.getItem() instanceof ItemRiggingBase) {
            IItemHandler mainGuns = MultiInvUtil.getCap(rigging).getInventory(enums.SLOTTYPE.MAIN_GUN);
            for (int i = 0; i < mainGuns.getSlots(); i++) {
                if(!isDestroyed(mainGuns.getStackInSlot(i))){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasGunOrTorpedo(ItemStack riggingStack) {
        if (riggingStack.getItem() instanceof ItemRiggingBase) {
            IMultiInventory inventories = MultiInvUtil.getCap(riggingStack);
            IItemHandler torpedos = inventories.getInventory(enums.SLOTTYPE.TORPEDO);
            for (int i = 0; i < torpedos.getSlots(); i++) {
                if (torpedos.getStackInSlot(i).getItem() instanceof ItemEquipmentBase) {
                    return true;
                }
            }
            IItemHandler mainGuns = inventories.getInventory(enums.SLOTTYPE.MAIN_GUN);
            for (int i = 0; i < mainGuns.getSlots(); i++) {
                if (mainGuns.getStackInSlot(i).getItem() instanceof ItemEquipmentBase) {
                    return true;
                }
            }
            IItemHandler subGuns = inventories.getInventory(enums.SLOTTYPE.SUB_GUN);
            for (int i = 0; i < subGuns.getSlots(); i++) {
                if (subGuns.getStackInSlot(i).getItem() instanceof ItemEquipmentBase) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canUseTorpedo(LivingEntity shooter, ItemStack riggingStack){
        return getPreparedWeapon(shooter, riggingStack, enums.SLOTTYPE.TORPEDO).isPresent();
    }

    public static boolean canUseCannon(LivingEntity shooter, ItemStack riggingStack){
        return getPreparedWeapon(shooter, riggingStack, enums.SLOTTYPE.MAIN_GUN, enums.SLOTTYPE.SUB_GUN).isPresent();
    }

    public static boolean isOutOfAmmo(ItemStack equipment){
        CompoundTag compoundNBT = equipment.getOrCreateTag();
        if(equipment.getItem() instanceof ICraftingTableReloadable){
            return compoundNBT.getInt("Ammo")<= 0;
        }
        return true;
    }

    public static void useAmmo(ItemStack stack){
        CompoundTag compoundNBT = stack.getOrCreateTag();
        int prevammo =  compoundNBT.getInt("Ammo");
        compoundNBT.putInt("Ammo", prevammo-1);
    }

    public static void emptyAmmo(ItemStack stack){
        setAmmo(stack, 0);
    }

    public static void setAmmo(ItemStack stack, int count){
        CompoundTag compoundNBT = stack.getOrCreateTag();
        if(stack.getItem() instanceof ICraftingTableReloadable){
            compoundNBT.putInt("Ammo", count);
        }
    }

    public static void setAmmoFull(ItemStack stack){
        if(stack.getItem() instanceof ICraftingTableReloadable) {
            CompoundTag compoundNBT = stack.getOrCreateTag();
            compoundNBT.putInt("Ammo", ((ICraftingTableReloadable) stack.getItem()).getMaxAmmo());
        }
    }

    public static ItemStack addAmmo(ItemStack stack, int count) {
        int currentAmmo = getRemainingAmmo(stack);
        if (stack.getItem() instanceof ICraftingTableReloadable) {
            setAmmo(stack, Math.min(currentAmmo+count, ((ICraftingTableReloadable) stack.getItem()).getMaxAmmo()));
        }
        return stack;
    }

    public static int getRemainingAmmo(ItemStack equipment){
        CompoundTag compoundNBT = equipment.getOrCreateTag();
        if(equipment.getItem() instanceof ICraftingTableReloadable){
            return compoundNBT.getInt("Ammo");
        }
        return -1;
    }


    public static int getDelayofEquipment(ItemStack Equipment){
        CompoundTag tags = Equipment.getOrCreateTag();
        return tags.getInt("delay");
    }

    public static void setEquipmentDelay (ItemStack equipment){
        if(equipment.getItem() instanceof ItemEquipmentBase){
            CompoundTag tags = equipment.getOrCreateTag();
            ItemEquipmentBase equipmentItem = (ItemEquipmentBase)(equipment.getItem());
            tags.putInt("delay", equipmentItem.getFiredelay());
        }
    }

}
