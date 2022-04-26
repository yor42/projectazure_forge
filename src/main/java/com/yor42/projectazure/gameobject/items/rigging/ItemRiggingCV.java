package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemRiggingCV extends ItemRiggingBase {
    public ItemRiggingCV(Properties properties, int HP) {
        super(properties, HP);
        this.validclass = enums.shipClass.AircraftCarrier;
    }

    @Override
    public int getHangerSlots() {
        //minimum size
        return 3;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (worldIn == null) return; // thanks JEI very cool

        if (getHangerSlots() > 0) {
            tooltip.add(new TranslatableComponent("item.tooltip.hanger_slot_usage").append(": " + getPlaneCount(stack) + "/" + getHangerSlots()));
        }
    }

    private int getPlaneCount(ItemStack riggingStack) {
        IItemHandler hangar = MultiInvUtil.getCap(riggingStack).getInventory(enums.SLOTTYPE.PLANE.ordinal());
        int count = 0;
        for (int i = 0; i < hangar.getSlots(); i++) {
            if (hangar.getStackInSlot(i).getItem() instanceof ItemEquipmentPlaneBase) {
                count++;
            }
        }
        return count;
    }
}
