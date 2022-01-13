package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

import net.minecraft.item.Item.Properties;

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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
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
        tooltip.add(new TranslationTextComponent("item.tooltip.remainingammo").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(getRemainingAmmo(stack)+"/"+this.getMaxAmmo()).withStyle(color)));
        tooltip.add(new TranslationTextComponent(isreloadable? "item.tooltip.torpedo_reloadable": "item.tooltip.torpedo_not_reloadable" ).setStyle(Style.EMPTY.withColor(Color.fromRgb(isreloadable? 0x00FF00:0xff0000))));
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
