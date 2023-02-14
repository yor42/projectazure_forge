package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.tooltip.firerate").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(String.format("%.2f",((float)1/this.firedelay)*20)+"R/s").withStyle(TextFormatting.YELLOW)));
    }

    @Override
    public int getRepairAmount(ItemStack candidateItem) {
        if(candidateItem.getItem() == RegisterItems.PLATE_STEEL.get()){
            return 2;
        }
        else if(candidateItem.getItem() == RegisterItems.MECHANICAL_PARTS.get()){
            return 4;
        }
        return super.getRepairAmount(candidateItem);
    }
}
