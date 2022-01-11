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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemContributorBonus extends Item {
    //Item Provided to people who helped development on idea and such containing spawn eggs. this mod has no patreon or any way of financial support.

    public ItemContributorBonus(Properties properties) {
        super(properties.maxStackSize(1).rarity(Rarity.EPIC));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.getOrCreateTag().contains("inventory");
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {

        ItemStack cube = playerIn.getHeldItem(handIn);

        UUID PlayerUUID = playerIn.getUniqueID();
        CompoundNBT compound = cube.getOrCreateTag();

        //openGui
        if(compound.hasUniqueId("owner")){
            UUID OwnerUUID = compound.getUniqueId("owner");
            if(!PlayerUUID.equals(OwnerUUID)) {
                playerIn.sendMessage(new TranslationTextComponent("message.rewardbag.notowner"), UUID.randomUUID());
                return ActionResult.resultFail(cube);
            }
        }
        if(compound.contains("inventory")){
            ListNBT list = compound.getList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++)
            {
                CompoundNBT ItemInfo = list.getCompound(i);
                ItemStack stack = ItemStack.read(ItemInfo);
                int index = playerIn.inventory.getFirstEmptyStack();
                if(index>0){
                    playerIn.inventory.setInventorySlotContents(index, stack);
                }
                else{
                    ItemEntity entity = new ItemEntity(worldIn, playerIn.getPosX(), playerIn.getPosY(),playerIn.getPosZ(), stack);
                    worldIn.addEntity(entity);
                }
            }
        }
        playerIn.getHeldItem(handIn).shrink(1);
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(stack.getItem().getTranslationKey()+".tooltip").mergeStyle(TextFormatting.GRAY));
        if (worldIn != null && worldIn.isRemote) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        CompoundNBT compound = stack.getOrCreateTag();
        UUID OwnerID = compound.getUniqueId("owner");
        @Nullable
        PlayerEntity owner = worldIn.getPlayerByUuid(OwnerID);
        if (owner != null) {
            tooltip.add(new TranslationTextComponent(stack.getItem().getTranslationKey()+".tooltip.owner").mergeStyle(TextFormatting.GRAY).append(new StringTextComponent(": ")).append(new StringTextComponent(owner.getDisplayName().getString()).mergeStyle(TextFormatting.YELLOW)));
        }
        else{
            tooltip.add(new TranslationTextComponent(stack.getItem().getTranslationKey()+".tooltip.owner").mergeStyle(TextFormatting.GRAY).append(new StringTextComponent(": ")).append(new StringTextComponent(OwnerID.toString()).mergeStyle(TextFormatting.RED)));
        }
        if(compound.contains("inventory")){
            tooltip.add(new TranslationTextComponent(stack.getItem().getTranslationKey()+".tooltip.content").mergeStyle(TextFormatting.YELLOW));
            ListNBT list = compound.getList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT ItemInfo = list.getCompound(i);
                ItemStack content = ItemStack.read(ItemInfo);
                tooltip.add(content.getDisplayName());
            }
        }
    }
}
