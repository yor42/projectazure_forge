package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemRiggingCV extends ItemRiggingBase implements IAnimatable {
    public ItemRiggingCV(Properties properties, int HP) {
        super(properties, HP);
        this.validclass = enums.shipClass.AircraftCarrier;
    }

    @Override
    public int getHangerSlots() {
        //minimum size
        return 45;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(getHangerSlots() >0){
        tooltip.add(new TranslationTextComponent("item.tooltip.hanger_slot_usage").appendString(": "+getPlaneCount(stack)+"/"+getHangerSlots()));
        }
    }

    private int getPlaneCount(ItemStack riggingstack){
        ItemStackHandler hanger= this.getHangers(riggingstack);
        int count = 0;
        if(hanger != null) {
            for (int i = 0; i < hanger.getSlots(); i++) {
                if(hanger.getStackInSlot(i).getItem() instanceof ItemEquipmentPlaneBase){
                    count++;
                }
            }
        }
        return count;
    }
}
