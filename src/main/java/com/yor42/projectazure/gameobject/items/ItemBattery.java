package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.capability.ItemPowerCapabilityProvider;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorageItem;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBattery extends Item {
    private final int capacity, maxin, maxout;
    private final boolean shouldeFilledonCraft;

    public ItemBattery(Properties property, int capacity) {
        this(property, capacity, capacity, capacity);
    }

    public ItemBattery(Properties property, int capacity, int maxin, int maxout) {
        this(property, capacity, maxin, maxout, false);
    }

    public ItemBattery(Properties property, int capacity, int maxin, int maxout, boolean shouldeFilledonCraft) {
        super(property);
        this.capacity = capacity;
        this.maxin = maxin;
        this.maxout = maxout;
        this.shouldeFilledonCraft = shouldeFilledonCraft || maxin<=0;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemPowerCapabilityProvider(stack, this.capacity, this.maxin, this.maxout);
    }

    // @Override
    // public boolean showDurabilityBar(ItemStack stack) {
    //     return true;
    // }

    // @Override
    // public double getDurabilityForDisplay(ItemStack stack) {
    //     int remainingBattery = stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    //     return (double) (this.capacity-remainingBattery)/this.capacity;
    // }

    @Override
    public void fillItemCategory(CreativeModeTab p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        super.fillItemCategory(p_150895_1_, p_150895_2_);
        if (this.allowdedIn(p_150895_1_)) {
            ItemStack stack = new ItemStack(this);
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((battery)->{
                if(battery instanceof CustomEnergyStorageItem){
                    ((CustomEnergyStorageItem) battery).setEnergyStored(battery.getMaxEnergyStored());
                    p_150895_2_.add(stack);
                }
            });
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level p_77622_2_, Player p_77622_3_) {
        super.onCraftedBy(stack, p_77622_2_, p_77622_3_);
        if(this.shouldeFilledonCraft){
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((battery)->{
                if(battery instanceof CustomEnergyStorageItem){
                    ((CustomEnergyStorageItem) battery).setEnergyStored(battery.getMaxEnergyStored());
                }
            });
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack p_77624_1_, @Nullable Level p_77624_2_,@Nonnull List<Component> p_77624_3_,@Nonnull TooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        int remainingBattery = p_77624_1_.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
        float percentage = (float) remainingBattery/this.capacity;

        ChatFormatting color;
        if(percentage>=0.6){
            color = ChatFormatting.GREEN;
        }
        else if(percentage>=0.3){
            color = ChatFormatting.YELLOW;
        }
        else{
            color = ChatFormatting.DARK_RED;
        }
        percentage*=100;
        p_77624_3_.add(new TranslatableComponent("item.tooltip.energystored", new TextComponent(MathUtil.formatValueMatric(remainingBattery)+" FE / "+MathUtil.formatValueMatric(this.capacity)+String.format(" FE (%.2f", percentage)+"%)").withStyle(color)).withStyle(ChatFormatting.GRAY));
        if(this.maxin<=0){
            p_77624_3_.add(new TranslatableComponent("item.tooltip.energynonrechargeable").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
