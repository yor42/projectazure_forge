package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {

        ItemStack filterstack = player.getItemInHand(hand);
        ItemStack gasmask = player.getItemBySlot(EquipmentSlot.HEAD);
        if(!(gasmask.getItem() instanceof GasMaskItem)){

            if(CompatibilityUtils.isCurioLoaded()){
                gasmask = CuriosCompat.getCurioItemStack(player, "head",(stack)-> stack.getItem() instanceof GasMaskItem);
            }


            if(!(gasmask.getItem() instanceof GasMaskItem)){
                return InteractionResultHolder.pass(filterstack);
            }
        }



        CompoundTag compoundNBT = gasmask.getOrCreateTag();
        ListTag list = compoundNBT.getList("filters", Tag.TAG_COMPOUND);
        if(list.size()<2){
            list.add(filterstack.save(new CompoundTag()));
            compoundNBT.put("filters", list);
            filterstack.shrink(1);
            player.setItemInHand(hand, filterstack);
            player.playSound(registerSounds.GASMASK_FILTER_ADD, 0.8F*(0.4F*MathUtil.rand.nextFloat()), 0.8F*(0.4F*MathUtil.rand.nextFloat()));
            return InteractionResultHolder.consume(filterstack);
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
                return InteractionResultHolder.fail(filterstack);
            }
            ItemStack buffer = ItemStack.of(list.getCompound(index)).copy();
            list.set(index, filterstack.save(new CompoundTag()));
            player.setItemInHand(hand, buffer);
            player.playSound(registerSounds.GASMASK_FILTER_CHANGE, 0.8F*(0.4F* MathUtil.rand.nextFloat()), 0.8F*(0.4F*MathUtil.rand.nextFloat()));
            return InteractionResultHolder.success(buffer);
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
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> p_77624_3_, TooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        p_77624_3_.add(new TranslatableComponent("item.projectazure.filter.tooltip", new TextComponent(String.format("%.2f", (double)ItemStackUtils.getCurrentHP(p_77624_1_)/(double) this.getMaxHP()*100)+"%").withStyle(Style.EMPTY.withColor(getHPColor(p_77624_1_)))).withStyle(ChatFormatting.GRAY));
    }
}
