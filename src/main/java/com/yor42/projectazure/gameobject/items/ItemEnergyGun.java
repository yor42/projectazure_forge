package com.yor42.projectazure.gameobject.items;

import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import com.tac.guns.util.Process;
import com.yor42.projectazure.gameobject.capability.ItemPowerCapabilityProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemEnergyGun extends TimelessGunItem {

    private final int energycapacity, energypershot;

    public ItemEnergyGun(int energycapacity, int energypershot, Process<Item.Properties> properties) {
        super(properties);
        this.energycapacity = energycapacity;
        this.energypershot = energypershot;
    }

    public int getEnergyperShot(){
        return this.energypershot;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemPowerCapabilityProvider(stack, this.energycapacity);
    }

    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag) {
        Gun modifiedGun = this.getModifiedGun(stack);
        Item ammo = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if (ammo != null) {
            if(ammo == Items.AIR) {
                tooltip.add((new TranslationTextComponent("info.tac.ammo_type", (new TranslationTextComponent("message.energyguns.gun.ammo.energy")).withStyle(TextFormatting.BLUE))).withStyle(TextFormatting.DARK_GRAY));
            }
            else{
                tooltip.add((new TranslationTextComponent("info.projectazure.ammo_type_energy", (new TranslationTextComponent(ammo.getDescriptionId())).withStyle(TextFormatting.GOLD))).withStyle(TextFormatting.DARK_GRAY));
            }
        }


        String additionalDamageText = "";
        CompoundNBT tagCompound = stack.getTag();
        float additionalDamage;
        if (tagCompound != null && tagCompound.contains("AdditionalDamage", 99)) {
            additionalDamage = tagCompound.getFloat("AdditionalDamage");
            additionalDamage += GunModifierHelper.getAdditionalDamage(stack);
            if (additionalDamage > 0.0F) {
                additionalDamageText = TextFormatting.GREEN + " +" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format((double)additionalDamage);
            } else if (additionalDamage < 0.0F) {
                additionalDamageText = TextFormatting.RED + " " + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format((double)additionalDamage);
            }
        }

        additionalDamage = modifiedGun.getProjectile().getDamage();
        additionalDamage = GunModifierHelper.getModifiedProjectileDamage(stack, additionalDamage);
        additionalDamage = GunEnchantmentHelper.getAcceleratorDamage(stack, additionalDamage);
        tooltip.add((new TranslationTextComponent("info.tac.damage", TextFormatting.GOLD + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format((double)additionalDamage) + additionalDamageText)).withStyle(TextFormatting.DARK_GRAY));
        if (tagCompound != null) {
            if (tagCompound.getBoolean("IgnoreAmmo")) {
                tooltip.add((new TranslationTextComponent("info.tac.ignore_ammo")).withStyle(TextFormatting.AQUA));
            } else {
                AtomicBoolean isenergy = new AtomicBoolean(false);
                stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((energyhandler)->{
                    tooltip.add((new TranslationTextComponent("info.tac.ammo", TextFormatting.GOLD.toString() + energyhandler.getEnergyStored() + "/" + energyhandler.getMaxEnergyStored()).withStyle(TextFormatting.DARK_GRAY)));
                    isenergy.set(true);
                });
                if(!isenergy.get()){
                    int ammoCount = tagCompound.getInt("AmmoCount");
                    tooltip.add((new TranslationTextComponent("info.tac.ammo", TextFormatting.GOLD.toString() + ammoCount + "/" + GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun))).withStyle(TextFormatting.DARK_GRAY));
                }
            }
        }

        if (tagCompound != null && tagCompound.get("CurrentFireMode") != null) {
            if (tagCompound.getInt("CurrentFireMode") == 0) {
                tooltip.add((new TranslationTextComponent("info.tac.firemode_safe", (new KeybindTextComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH))).withStyle(TextFormatting.GREEN));
            } else if (tagCompound.getInt("CurrentFireMode") == 1) {
                tooltip.add((new TranslationTextComponent("info.tac.firemode_semi", (new KeybindTextComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH))).withStyle(TextFormatting.RED));
            } else if (tagCompound.getInt("CurrentFireMode") == 2) {
                tooltip.add((new TranslationTextComponent("info.tac.firemode_auto", (new KeybindTextComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH))).withStyle(TextFormatting.RED));
            }
        }

        if (tagCompound != null) {
            GunItem gun = (GunItem)stack.getItem();
            float speed = 0.1F / (1.0F + (gun.getModifiedGun(stack).getGeneral().getWeightKilo() * (1.0F + GunModifierHelper.getModifierOfWeaponWeight(stack)) + GunModifierHelper.getAdditionalWeaponWeight(stack)) * 0.0275F);
            speed = Math.max(Math.min(speed, 0.095F), 0.075F);
            if ((double)speed > 0.09D) {
                tooltip.add((new TranslationTextComponent("info.tac.lightWeightGun", (new TranslationTextComponent(-((int)((0.1D - (double)speed) * 1000.0D)) + "%")).withStyle(TextFormatting.RED))).withStyle(TextFormatting.DARK_AQUA));
            } else if ((double)speed < 0.09D && (double)speed > 0.0825D) {
                tooltip.add((new TranslationTextComponent("info.tac.standardWeightGun", (new TranslationTextComponent(-((int)((0.1D - (double)speed) * 1000.0D)) + "%")).withStyle(TextFormatting.RED))).withStyle(TextFormatting.DARK_GREEN));
            } else {
                tooltip.add((new TranslationTextComponent("info.tac.heavyWeightGun", (new TranslationTextComponent(-((int)((0.1D - (double)speed) * 1000.0D)) + "%")).withStyle(TextFormatting.RED))).withStyle(TextFormatting.DARK_RED));
            }
        }

        tooltip.add((new TranslationTextComponent("info.tac.attachment_help", (new KeybindTextComponent("key.tac.attachments")).getString().toUpperCase(Locale.ENGLISH))).withStyle(TextFormatting.YELLOW));
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        Gun modifiedGun = this.getModifiedGun(stack);
        Item ammo = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if(stack.getCapability(CapabilityEnergy.ENERGY).isPresent()&& ammo == Items.AIR){
            return stack.getCapability(CapabilityEnergy.ENERGY).map((energystorage)-> 1.0D - energystorage.getEnergyStored() / energystorage.getMaxEnergyStored()).orElse(super.getDurabilityForDisplay(stack));
        }
        return super.getDurabilityForDisplay(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        if(stack.getCapability(CapabilityEnergy.ENERGY).isPresent()){
            return true;
        }
        return super.showDurabilityBar(stack);
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        Gun modifiedGun = this.getModifiedGun(stack);
        Item ammo = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if(ammo == Items.AIR) {
            return Objects.requireNonNull(TextFormatting.BLUE.getColor());
        }
        else{
            return super.getRGBDurabilityForDisplay(stack);
        }
    }
}
