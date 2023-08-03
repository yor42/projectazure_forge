package com.yor42.projectazure.gameobject.items;

import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import com.tac.guns.util.Process;
import com.yor42.projectazure.gameobject.capability.ItemPowerCapabilityProvider;
import com.yor42.projectazure.interfaces.ISecondaryBar;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemEnergyGun extends TimelessGunItem implements ISecondaryBar {

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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemPowerCapabilityProvider(stack, this.energycapacity);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putInt("AmmoCount", this.getGun().getReloads().getMaxAmmo());
            stack.getOrCreateTag().putBoolean("IgnoreAmmo", this.ignoreAmmo);
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((energystorage)->energystorage.receiveEnergy(energystorage.getMaxEnergyStored(), false));
            stacks.add(stack);
        }
    }

    @Override
    public void onCraftedBy(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull Player player) {
        super.onCraftedBy(stack, world, player);
        stack.getOrCreateTag().putBoolean("IgnoreAmmo", this.ignoreAmmo);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        Gun modifiedGun = this.getModifiedGun(stack);
        Item ammo = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if(doesIgnoreAmmo(stack)) {

            Component text = new TranslatableComponent("message.energyguns.gun.ammo.energy").withStyle(ChatFormatting.BLUE);
            tooltip.add((new TranslatableComponent("info.tac.ammo_type", (text))).withStyle(ChatFormatting.DARK_GRAY));
        }
        else if (ammo != null) {
            MutableComponent text = new TranslatableComponent("message.energyguns.gun.ammo.energy").withStyle(ChatFormatting.BLUE);
            text.append(new TextComponent(", ").withStyle(ChatFormatting.DARK_GRAY)).append(new TranslatableComponent(ammo.getDescriptionId()).withStyle(ChatFormatting.GOLD));
            tooltip.add((new TranslatableComponent("info.tac.ammo_type", (text)).withStyle(ChatFormatting.DARK_GRAY)));
        }



        String additionalDamageText = "";
        CompoundTag tagCompound = stack.getTag();
        float additionalDamage;
        if (tagCompound != null && tagCompound.contains("AdditionalDamage", 99)) {
            additionalDamage = tagCompound.getFloat("AdditionalDamage");
            additionalDamage += GunModifierHelper.getAdditionalDamage(stack);
            if (additionalDamage > 0.0F) {
                additionalDamageText = ChatFormatting.GREEN + " +" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage);
            } else if (additionalDamage < 0.0F) {
                additionalDamageText = ChatFormatting.RED + " " + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage);
            }
        }

        additionalDamage = modifiedGun.getProjectile().getDamage();
        additionalDamage = GunModifierHelper.getModifiedProjectileDamage(stack, additionalDamage);
        additionalDamage = GunEnchantmentHelper.getAcceleratorDamage(stack, additionalDamage);
        tooltip.add((new TranslatableComponent("info.tac.damage", ChatFormatting.GOLD + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage) + additionalDamageText)).withStyle(ChatFormatting.DARK_GRAY));
        if (tagCompound != null) {
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((energyhandler) -> {
                tooltip.add((new TranslatableComponent("tooltip.energygun.remainingenergy", ChatFormatting.BLUE.toString() + energyhandler.getEnergyStored() + "/" + energyhandler.getMaxEnergyStored()).withStyle(ChatFormatting.DARK_GRAY)));
            });
            if (!tagCompound.getBoolean("IgnoreAmmo")) {
                int ammoCount = tagCompound.getInt("AmmoCount");
                tooltip.add((new TranslatableComponent("info.tac.ammo", ChatFormatting.GOLD.toString() + ammoCount + "/" + GunModifierHelper.getAmmoCapacity(stack, modifiedGun))).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        if (tagCompound != null && tagCompound.get("CurrentFireMode") != null) {
            if (tagCompound.getInt("CurrentFireMode") == 0) {
                tooltip.add((new TranslatableComponent("info.tac.firemode_safe", (new KeybindComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH))).withStyle(ChatFormatting.GREEN));
            } else if (tagCompound.getInt("CurrentFireMode") == 1) {
                tooltip.add((new TranslatableComponent("info.tac.firemode_semi", (new KeybindComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH))).withStyle(ChatFormatting.RED));
            } else if (tagCompound.getInt("CurrentFireMode") == 2) {
                tooltip.add((new TranslatableComponent("info.tac.firemode_auto", (new KeybindComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH))).withStyle(ChatFormatting.RED));
            }
        }

        if (tagCompound != null) {
            GunItem gun = (GunItem)stack.getItem();
            float speed = 0.1F / (1.0F + (gun.getModifiedGun(stack).getGeneral().getWeightKilo() * (1.0F + GunModifierHelper.getModifierOfWeaponWeight(stack)) + GunModifierHelper.getAdditionalWeaponWeight(stack)) * 0.0275F);
            speed = Math.max(Math.min(speed, 0.095F), 0.075F);
            if ((double)speed > 0.09D) {
                tooltip.add((new TranslatableComponent("info.tac.lightWeightGun", (new TranslatableComponent(-((int)((0.1D - (double)speed) * 1000.0D)) + "%")).withStyle(ChatFormatting.RED))).withStyle(ChatFormatting.DARK_AQUA));
            } else if ((double)speed < 0.09D && (double)speed > 0.0825D) {
                tooltip.add((new TranslatableComponent("info.tac.standardWeightGun", (new TranslatableComponent(-((int)((0.1D - (double)speed) * 1000.0D)) + "%")).withStyle(ChatFormatting.RED))).withStyle(ChatFormatting.DARK_GREEN));
            } else {
                tooltip.add((new TranslatableComponent("info.tac.heavyWeightGun", (new TranslatableComponent(-((int)((0.1D - (double)speed) * 1000.0D)) + "%")).withStyle(ChatFormatting.RED))).withStyle(ChatFormatting.DARK_RED));
            }
        }

        tooltip.add((new TranslatableComponent("info.tac.attachment_help", (new KeybindComponent("key.tac.attachments")).getString().toUpperCase(Locale.ENGLISH))).withStyle(ChatFormatting.YELLOW));
    }


    @Override
    public int getSecondaryBarWidth(ItemStack stack) {
        if(stack.getCapability(CapabilityEnergy.ENERGY).isPresent()&&doesIgnoreAmmo(stack)){
            return stack.getCapability(CapabilityEnergy.ENERGY).map((energystorage)-> (int)(13* (double)energystorage.getEnergyStored() / (double)energystorage.getMaxEnergyStored())).orElse(super.getBarWidth(stack));
        }
        return super.getBarWidth(stack);
    }

    @Override
    public boolean isSecondaryBarVisible(ItemStack pStack) {
        if(pStack.getCapability(CapabilityEnergy.ENERGY).isPresent()){
            return true;
        }
        return super.isBarVisible(pStack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 5636095;
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
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int tick, boolean isfocused) {
        super.inventoryTick(stack, world, entity, tick, isfocused);
        CompoundTag tagCompound = stack.getOrCreateTag();
        int currentfiremode = tagCompound.getInt("CurrentFireMode");
        int previousfiremode = tagCompound.getInt("previousfiremode");

        if(!(entity instanceof Player && ((Player) entity).getAbilities().instabuild)&&currentfiremode != 0){
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
