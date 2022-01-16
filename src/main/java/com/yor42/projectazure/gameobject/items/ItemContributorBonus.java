package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemContributorBonus extends Item {
    //Item Provided to people who helped development on idea and such containing spawn eggs. this mod has no patreon or any way of financial support.

    public ItemContributorBonus(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrCreateTag().contains("inventory");
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {

        ItemStack cube = playerIn.getItemInHand(handIn);

        UUID PlayerUUID = playerIn.getUUID();
        CompoundNBT compound = cube.getOrCreateTag();

        //openGui
        if(compound.hasUUID("owner")){
            UUID OwnerUUID = compound.getUUID("owner");
            if(!PlayerUUID.equals(OwnerUUID)) {
                playerIn.sendMessage(new TranslationTextComponent("message.rewardbag.notowner"), UUID.randomUUID());
                return ActionResult.fail(cube);
            }
        }
        if(compound.contains("inventory")){
            ListNBT list = compound.getList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++)
            {
                CompoundNBT ItemInfo = list.getCompound(i);
                ItemStack stack = ItemStack.of(ItemInfo);
                int index = playerIn.inventory.getFreeSlot();
                if(index>0){
                    playerIn.inventory.setItem(index, stack);
                }
                else{
                    ItemEntity entity = new ItemEntity(worldIn, playerIn.getX(), playerIn.getY(),playerIn.getZ(), stack);
                    worldIn.addFreshEntity(entity);
                }
            }
        }
        playerIn.getItemInHand(handIn).shrink(1);
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(TextFormatting.GRAY));
        if (worldIn != null && worldIn.isClientSide) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        CompoundNBT compound = stack.getOrCreateTag();
        UUID OwnerID = compound.getUUID("owner");
        @Nullable
        PlayerEntity owner = worldIn.getPlayerByUUID(OwnerID);
        if (owner != null) {
            tooltip.add(new TranslationTextComponent(stack.getItem().getDescriptionId()+".tooltip.owner").withStyle(TextFormatting.GRAY).append(new StringTextComponent(": ")).append(new StringTextComponent(owner.getDisplayName().getString()).withStyle(TextFormatting.YELLOW)));
        }
        else{
            tooltip.add(new TranslationTextComponent(stack.getItem().getDescriptionId()+".tooltip.owner").withStyle(TextFormatting.GRAY).append(new StringTextComponent(": ")).append(new StringTextComponent(OwnerID.toString()).withStyle(TextFormatting.RED)));
        }
        if(compound.contains("inventory")){
            tooltip.add(new TranslationTextComponent(stack.getItem().getDescriptionId()+".tooltip.content").withStyle(TextFormatting.YELLOW));
            ListNBT list = compound.getList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT ItemInfo = list.getCompound(i);
                ItemStack content = ItemStack.of(ItemInfo);
                tooltip.add(content.getHoverName());
            }
        }
    }
}
