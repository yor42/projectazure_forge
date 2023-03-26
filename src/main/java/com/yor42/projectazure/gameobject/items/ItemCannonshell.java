package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCannonshell extends ItemBaseTooltip {
    private final AmmoProperties properties;

    public ItemCannonshell(enums.AmmoCategory category, Properties ItemProperties){
        this(category, category.getRawRiggingDamage(),category.getRawEntityDamage(),category.getRawComponentDamage(),category.getRawhitChance(),category.getRawDamageModifer(),category.ShouldDamageMultipleComponent(), ItemProperties);
    }
    public ItemCannonshell(enums.AmmoCategory ammoType, float damage_rigging, float damage_entity, float damage_component, float hitChance, float minimum_damage_modifier, boolean shouldDamageMultipleComponant, Properties properties) {
        super(properties);
        this.properties = new AmmoProperties(ammoType, damage_rigging, damage_entity, damage_component, hitChance, minimum_damage_modifier, shouldDamageMultipleComponant);
    }


    public AmmoProperties getAmmoProperty() {
        return this.properties;
    }

    @Override
    public void addInformationAfterShift(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        AmmoProperties properties = this.properties;
        super.addInformationAfterShift(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TextComponent("----------"));
        tooltip.add(new TranslatableComponent("tooltip.shell.hitchance").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(properties.getRawhitChance() * 100 +"%").withStyle(ChatFormatting.DARK_GREEN)));
        tooltip.add(new TranslatableComponent("tooltip.shell.riggingdamage").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(String.valueOf(properties.getRawRiggingDamage())).withStyle(ChatFormatting.DARK_GREEN)));
        tooltip.add(new TranslatableComponent("tooltip.shell.componentdamage").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(String.valueOf(properties.getRawEntityDamage())).withStyle(ChatFormatting.DARK_GREEN)));
        tooltip.add(new TranslatableComponent("tooltip.shell.entitydamage").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(String.valueOf(properties.getRawEntityDamage())).withStyle(ChatFormatting.DARK_GREEN)));
        tooltip.add(new TranslatableComponent("tooltip.shell.minimumdamagepercentage").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(properties.getRawDamageModifer()*100 +"%").withStyle(ChatFormatting.DARK_GREEN)));

        MutableComponent component = new TranslatableComponent("tooltip.shell.specialeffect").append(": ").withStyle(ChatFormatting.GRAY);
        if(properties.isFiery()){
            component.append(new TranslatableComponent("tooltip.shell.incendiary").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD));
            if(properties.isExplosive()){
                component.append(", ").append(new TranslatableComponent("tooltip.shell.explosive").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD));
            }
        }
        else{
            if(properties.isExplosive()){
                component.append(new TranslatableComponent("tooltip.shell.explosive").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD));
            }
            else{
                component.append(new TranslatableComponent("tooltip.shell.none").withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        tooltip.add(component);

    }
}
