package com.yor42.projectazure.gameobject.items;

import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import com.tac.guns.util.Process;
import com.yor42.projectazure.gameobject.capability.ItemPowerCapabilityProvider;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemEnergyGun extends TimelessGunItem {

    private final int energycapacity, energypershot, idleenergyconsumption;
    private final boolean ignoreAmmo;
    @Nullable
    private SoundEvent safereleasesound, safesetsound;
    @Nullable
    private SoundEvent NoAmmoSound;

    public ItemEnergyGun(int energycapacity, int energypershot, Process<Item.Properties> properties) {
        this(energycapacity, energypershot, 200, false, properties);
    }

    public ItemEnergyGun(int energycapacity, int energypershot, int idleenergyconsumption, boolean ignoreAmmo, Process<Item.Properties> properties) {
        super(properties);
        this.energycapacity = energycapacity;
        this.energypershot = energypershot;
        this.ignoreAmmo = ignoreAmmo;
        this.idleenergyconsumption=idleenergyconsumption;
    }

    public ItemEnergyGun(int energycapacity, int energypershot, int idleenergyconsumption, boolean ignoreAmmo, @Nullable SoundEvent safereleasesound, @Nullable SoundEvent safesetsound, @Nullable SoundEvent NoAmmoSound,Process<Item.Properties> properties) {
        super(properties);
        this.energycapacity = energycapacity;
        this.energypershot = energypershot;
        this.ignoreAmmo = ignoreAmmo;
        this.idleenergyconsumption=idleenergyconsumption;
        this.safereleasesound = safereleasesound;
        this.safesetsound = safesetsound;
        this.NoAmmoSound = NoAmmoSound;
    }

    @Nullable
    public SoundEvent getNoAmmoSound(){
        return this.NoAmmoSound;
    }

    public int getEnergyperShot(){
        return this.energypershot;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemPowerCapabilityProvider(stack, this.energycapacity);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putInt("AmmoCount", this.getGun().getReloads().getMaxAmmo());
            stack.getOrCreateTag().putBoolean("IgnoreAmmo", this.ignoreAmmo);
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((energystorage)->energystorage.receiveEnergy(energystorage.getMaxEnergyStored(), false));
            stacks.add(stack);
        }
    }

    @Override
    public void onCraftedBy(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull PlayerEntity player) {
        super.onCraftedBy(stack, world, player);
        stack.getOrCreateTag().putBoolean("IgnoreAmmo", this.ignoreAmmo);
    }

    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag) {
        Gun modifiedGun = this.getModifiedGun(stack);
        Item ammo = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if(doesIgnoreAmmo(stack)) {

            ITextComponent text = new TranslationTextComponent("message.energyguns.gun.ammo.energy").withStyle(TextFormatting.BLUE);
            tooltip.add((new TranslationTextComponent("info.tac.ammo_type", (text))).withStyle(TextFormatting.DARK_GRAY));
        }
        else if (ammo != null) {
            IFormattableTextComponent text = new TranslationTextComponent("message.energyguns.gun.ammo.energy").withStyle(TextFormatting.BLUE);
            text.append(new StringTextComponent(", ").withStyle(TextFormatting.DARK_GRAY)).append(new TranslationTextComponent(ammo.getDescriptionId()).withStyle(TextFormatting.GOLD));
            tooltip.add((new TranslationTextComponent("info.tac.ammo_type", (text)).withStyle(TextFormatting.DARK_GRAY)));
        }



        String additionalDamageText = "";
        CompoundNBT tagCompound = stack.getTag();
        float additionalDamage;
        if (tagCompound != null && tagCompound.contains("AdditionalDamage", 99)) {
            additionalDamage = tagCompound.getFloat("AdditionalDamage");
            additionalDamage += GunModifierHelper.getAdditionalDamage(stack);
            if (additionalDamage > 0.0F) {
                additionalDamageText = TextFormatting.GREEN + " +" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage);
            } else if (additionalDamage < 0.0F) {
                additionalDamageText = TextFormatting.RED + " " + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage);
            }
        }

        additionalDamage = modifiedGun.getProjectile().getDamage();
        additionalDamage = GunModifierHelper.getModifiedProjectileDamage(stack, additionalDamage);
        additionalDamage = GunEnchantmentHelper.getAcceleratorDamage(stack, additionalDamage);
        tooltip.add((new TranslationTextComponent("info.tac.damage", TextFormatting.GOLD + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage) + additionalDamageText)).withStyle(TextFormatting.DARK_GRAY));
        if (tagCompound != null) {
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((energyhandler) -> {
                tooltip.add((new TranslationTextComponent("tooltip.energygun.remainingenergy", TextFormatting.BLUE.toString() + energyhandler.getEnergyStored() + "/" + energyhandler.getMaxEnergyStored()).withStyle(TextFormatting.DARK_GRAY)));
            });
            if (!tagCompound.getBoolean("IgnoreAmmo")) {
                int ammoCount = tagCompound.getInt("AmmoCount");
                tooltip.add((new TranslationTextComponent("info.tac.ammo", TextFormatting.GOLD.toString() + ammoCount + "/" + GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun))).withStyle(TextFormatting.DARK_GRAY));
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
        if(stack.getCapability(CapabilityEnergy.ENERGY).isPresent()&&doesIgnoreAmmo(stack)){
            return stack.getCapability(CapabilityEnergy.ENERGY).map((energystorage)-> 1.0D - (double)energystorage.getEnergyStored() / (double)energystorage.getMaxEnergyStored()).orElse(super.getDurabilityForDisplay(stack));
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
        if(doesIgnoreAmmo(stack)) {
            return Objects.requireNonNull(TextFormatting.BLUE.getColor());
        }
        else{
            return super.getRGBDurabilityForDisplay(stack);
        }
    }

    private static boolean doesIgnoreAmmo(ItemStack stack) {

        boolean itemproperty = false;
        Item gunitem = stack.getItem();

        if(gunitem instanceof ItemEnergyGun){
            itemproperty = ((ItemEnergyGun) gunitem).ignoreAmmo;
        }

        return stack.getOrCreateTag().getBoolean("IgnoreAmmo") || itemproperty;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int tick, boolean isfocused) {
        super.inventoryTick(stack, world, entity, tick, isfocused);
        CompoundNBT tagCompound = stack.getOrCreateTag();
        int currentfiremode = tagCompound.getInt("CurrentFireMode");
        int previousfiremode = tagCompound.getInt("previousfiremode");

        if(!(entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.instabuild)&&currentfiremode != 0){
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((energystorage)->{
                if(energystorage.extractEnergy(this.idleenergyconsumption, false)<this.idleenergyconsumption){
                    tagCompound.putInt("CurrentFireMode", 0);
                }
            });
        }

        if(currentfiremode!=previousfiremode){
            if(previousfiremode == 0){
                if(this.safereleasesound!= null){
                    entity.playSound(this.safereleasesound, 0.8F+(MathUtil.getRand().nextFloat()*0.4F),0.8F+(MathUtil.getRand().nextFloat()*0.4F));
                }
            }
            else if(currentfiremode == 0){
                if(this.safesetsound!= null){
                    entity.playSound(this.safesetsound, 0.8F+(MathUtil.getRand().nextFloat()*0.4F),0.8F+(MathUtil.getRand().nextFloat()*0.4F));
                }
            }
        }
        tagCompound.putInt("previousfiremode",currentfiremode);
    }
}
