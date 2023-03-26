package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.nbt.Tag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import net.minecraft.world.item.Item.Properties;

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
    public InteractionResultHolder<ItemStack> use(@Nonnull Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {

        ItemStack cube = playerIn.getItemInHand(handIn);

        UUID PlayerUUID = playerIn.getUUID();
        CompoundTag compound = cube.getOrCreateTag();

        //openGui
        if(compound.hasUUID("owner")){
            UUID OwnerUUID = compound.getUUID("owner");
            if(!PlayerUUID.equals(OwnerUUID)) {
                playerIn.sendMessage(new TranslatableComponent("message.rewardbag.notowner"), UUID.randomUUID());
                return InteractionResultHolder.fail(cube);
            }
        }
        if(compound.contains("inventory")){
            ListTag list = compound.getList("inventory", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++)
            {
                CompoundTag ItemInfo = list.getCompound(i);
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
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
        if (worldIn != null && worldIn.isClientSide) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        CompoundTag compound = stack.getOrCreateTag();
        UUID OwnerID = compound.getUUID("owner");
        @Nullable
        Player owner = worldIn.getPlayerByUUID(OwnerID);
        if (owner != null) {
            tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip.owner").withStyle(ChatFormatting.GRAY).append(new TextComponent(": ")).append(new TextComponent(owner.getDisplayName().getString()).withStyle(ChatFormatting.YELLOW)));
        }
        else{
            tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip.owner").withStyle(ChatFormatting.GRAY).append(new TextComponent(": ")).append(new TextComponent(OwnerID.toString()).withStyle(ChatFormatting.RED)));
        }
        if(compound.contains("inventory")){
            tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip.content").withStyle(ChatFormatting.YELLOW));
            ListTag list = compound.getList("inventory", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag ItemInfo = list.getCompound(i);
                ItemStack content = ItemStack.of(ItemInfo);
                tooltip.add(content.getHoverName());
            }
        }
    }
}
