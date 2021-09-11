package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public abstract class ItemEquipmentTorpedo extends ItemEquipmentBase implements ICraftingTableReloadable {

    protected boolean isreloadable;
    protected int MaxAmmoCap;

    public ItemEquipmentTorpedo(Properties properties,int maxHP) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.TORPEDO;
    }

    @Override
    public enums.AmmoCalibur getAmmoType() {
        return enums.AmmoCalibur.TORPEDO;
    }

    public int getMaxAmmo(){
        return this.MaxAmmoCap;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        TextFormatting color;

        if(((float)getRemainingAmmo(stack)/this.getMaxAmmo())<0.3F){
            color = TextFormatting.RED;
        }
        else if(((float)getRemainingAmmo(stack)/this.getMaxAmmo())<0.6F){
            color = TextFormatting.YELLOW;
        }
        else{
            color = TextFormatting.GREEN;
        }

        boolean isreloadable = this.isreloadable;
        tooltip.add(new TranslationTextComponent("item.tooltip.remainingammo").appendString(": ").mergeStyle(TextFormatting.GRAY).append(new StringTextComponent(getRemainingAmmo(stack)+"/"+this.getMaxAmmo()).mergeStyle(color)));
        tooltip.add(new TranslationTextComponent(isreloadable? "item.tooltip.torpedo_reloadable": "item.tooltip.torpedo_not_reloadable" ).setStyle(Style.EMPTY.setColor(Color.fromInt(isreloadable? 0x00FF00:0xff0000))));
    }
}
