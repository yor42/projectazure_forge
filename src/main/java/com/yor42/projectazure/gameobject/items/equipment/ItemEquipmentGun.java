package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemEquipmentGun extends ItemEquipmentBase{

    protected enums.CanonSize size;

    public ItemEquipmentGun(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.GUN;
    }

    public enums.CanonSize getSize(){
        return this.size;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.tooltip.firerate").appendString(": ").append(new StringTextComponent(String.format("%.2f",((float)1/this.firedelay)*20)+"R/s")));
    }
}
