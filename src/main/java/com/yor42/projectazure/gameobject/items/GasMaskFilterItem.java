package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColor;

public class GasMaskFilterItem extends ItemDestroyable {
    public GasMaskFilterItem(Properties p_i48487_1_, int maxDamage) {
        super(p_i48487_1_.stacksTo(1), maxDamage);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {

        ItemStack filterstack = player.getItemInHand(hand);
        ItemStack gasmask = player.getItemBySlot(EquipmentSlotType.HEAD);
        if(!(gasmask.getItem() instanceof GasMaskItem)){
            return ActionResult.pass(filterstack);
        }

        CompoundNBT compoundNBT = gasmask.getOrCreateTag();
        ListNBT list = compoundNBT.getList("filters", Constants.NBT.TAG_COMPOUND);
        if(list.size()<2){
            list.add(filterstack.save(new CompoundNBT()));
            compoundNBT.put("filters", list);
            filterstack.shrink(1);
            player.setItemInHand(hand, filterstack);
            return ActionResult.consume(filterstack);
        }
        else{
            int damage = ItemStackUtils.getCurrentDamage(filterstack);
            int index = -1;
            for(int i=0; i<2; i++){
                ItemStack existingfilter = ItemStack.of(list.getCompound(i));
                int dmg = ItemStackUtils.getCurrentDamage(existingfilter);
                if(damage >= dmg){
                    continue;
                }
                damage = dmg;
                index = i;
            }
            if(index<0){
                return ActionResult.fail(filterstack);
            }
            ItemStack buffer = ItemStack.of(list.getCompound(index)).copy();
            list.set(index, filterstack.save(new CompoundNBT()));
            player.setItemInHand(hand, buffer);
            return ActionResult.success(buffer);
        }

    }

    @Override
    public int getRepairAmount(ItemStack candidateItem) {

        if (candidateItem.getItem() == RegisterItems.CHARCOAL_FILTER.get()){
            return this.getMaxHP()/2+200;
        }

        return super.getRepairAmount(candidateItem);
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        p_77624_3_.add(new TranslationTextComponent("item.projectazure.filter.tooltip", new StringTextComponent(String.format("%.2f", (double)ItemStackUtils.getCurrentHP(p_77624_1_)/(double) this.getMaxHP()*100)+"%").withStyle(Style.EMPTY.withColor(getHPColor(p_77624_1_)))).withStyle(TextFormatting.GRAY));
    }
}
