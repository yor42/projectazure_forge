package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.interfaces.IActivatable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.hasPlanes;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.isOn;

public class EntityUtils {
    public static boolean EntityHasPlanes(EntityKansenBase entity){
        return hasPlanes(entity.getRigging());
    }


    public static void ChangeArmorStatus(LivingEntity entity, EquipmentSlot slot){
        ItemStack stack = entity.getItemBySlot(slot);
        Item item = stack.getItem();

        if(item instanceof IActivatable act){
            if(isOn(stack)){
                act.onSwitchOff(stack, entity);
            }
            else{
                act.onSwitchOn(stack, entity);
            }
        }
    }

}
