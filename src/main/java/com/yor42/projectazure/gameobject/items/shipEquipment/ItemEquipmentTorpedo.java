package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        ChatFormatting color;

        if(((float)getRemainingAmmo(stack)/this.getMaxAmmo())<0.3F){
            color = ChatFormatting.RED;
        }
        else if(((float)getRemainingAmmo(stack)/this.getMaxAmmo())<0.6F){
            color = ChatFormatting.YELLOW;
        }
        else{
            color = ChatFormatting.GREEN;
        }

        boolean isreloadable = this.isreloadable;
        tooltip.add(new TranslatableComponent("item.tooltip.remainingammo").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(getRemainingAmmo(stack)+"/"+this.getMaxAmmo()).withStyle(color)));
        tooltip.add(new TranslatableComponent(isreloadable? "item.tooltip.torpedo_reloadable": "item.tooltip.torpedo_not_reloadable" ).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(isreloadable? 0x00FF00:0xff0000))));
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
