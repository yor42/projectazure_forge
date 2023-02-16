package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.capability.ItemPowerCapabilityProvider;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBattery extends Item {
    private final int capacity, maxin, maxout;

    public ItemBattery(Properties property, int capacity) {
        this(property, capacity, capacity, capacity);
    }

    public ItemBattery(Properties property, int capacity, int maxin, int maxout) {
        super(property);
        this.capacity = capacity;
        this.maxin = maxin;
        this.maxout = maxout;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemPowerCapabilityProvider(stack, this.capacity, this.maxin, this.maxout);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int remainingBattery = stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
        return (double) (this.capacity-remainingBattery)/this.capacity;
    }

    @Override
    public void fillItemCategory(ItemGroup p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        super.fillItemCategory(p_150895_1_, p_150895_2_);
        if (this.allowdedIn(p_150895_1_)) {
            ItemStack stack = new ItemStack(this);
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((battery)->{
                while(battery.getEnergyStored()<battery.getMaxEnergyStored()){
                    battery.receiveEnergy(battery.getMaxEnergyStored(), false);
                }
                p_150895_2_.add(stack);
            });
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack p_77624_1_, @Nullable World p_77624_2_,@Nonnull List<ITextComponent> p_77624_3_,@Nonnull ITooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        int remainingBattery = p_77624_1_.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
        float percentage = (float) remainingBattery/this.capacity;

        TextFormatting color;
        if(percentage>=0.6){
            color = TextFormatting.GREEN;
        }
        else if(percentage>=0.3){
            color = TextFormatting.YELLOW;
        }
        else{
            color = TextFormatting.DARK_RED;
        }
        percentage*=100;
        p_77624_3_.add(new StringTextComponent(MathUtil.formatValueMatric(remainingBattery)+" FE / "+MathUtil.formatValueMatric(this.capacity)+String.format(" FE (%.2f", percentage)+"%)").withStyle(color));
    }
}
