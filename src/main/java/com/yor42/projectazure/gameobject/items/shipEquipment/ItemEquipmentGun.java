package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ChatFormatting;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.Level;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemEquipmentGun extends ItemEquipmentBase{

    protected enums.CanonSize size;

    public ItemEquipmentGun(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.SUB_GUN;
    }

    public enums.CanonSize getSize(){
        return this.size;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.tooltip.firerate").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(String.format("%.2f",((float)1/this.firedelay)*20)+"R/s").withStyle(ChatFormatting.YELLOW)));
    }

    @Override
    public int getRepairAmount(ItemStack candidateItem) {
        if(candidateItem.getItem() == registerItems.PLATE_STEEL.get()){
            return 2;
        }
        else if(candidateItem.getItem() == registerItems.MECHANICAL_PARTS.get()){
            return 4;
        }
        return super.getRepairAmount(candidateItem);
    }
}
