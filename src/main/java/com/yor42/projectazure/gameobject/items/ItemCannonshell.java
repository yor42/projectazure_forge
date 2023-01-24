package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

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
    public void addInformationAfterShift(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        AmmoProperties properties = this.properties;
        super.addInformationAfterShift(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent("----------"));
        tooltip.add(new TranslationTextComponent("tooltip.shell.hitchance").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(properties.getRawhitChance() * 100 +"%").withStyle(TextFormatting.DARK_GREEN)));
        tooltip.add(new TranslationTextComponent("tooltip.shell.riggingdamage").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(String.valueOf(properties.getRawRiggingDamage())).withStyle(TextFormatting.DARK_GREEN)));
        tooltip.add(new TranslationTextComponent("tooltip.shell.componentdamage").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(String.valueOf(properties.getRawEntityDamage())).withStyle(TextFormatting.DARK_GREEN)));
        tooltip.add(new TranslationTextComponent("tooltip.shell.entitydamage").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(String.valueOf(properties.getRawEntityDamage())).withStyle(TextFormatting.DARK_GREEN)));
        tooltip.add(new TranslationTextComponent("tooltip.shell.minimumdamagepercentage").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(properties.getRawDamageModifer()*100 +"%").withStyle(TextFormatting.DARK_GREEN)));

        IFormattableTextComponent component = new TranslationTextComponent("tooltip.shell.specialeffect").append(": ").withStyle(TextFormatting.GRAY);
        if(properties.isFiery()){
            component.append(new TranslationTextComponent("tooltip.shell.incendiary").withStyle(TextFormatting.DARK_RED).withStyle(TextFormatting.BOLD));
            if(properties.isExplosive()){
                component.append(", ").append(new TranslationTextComponent("tooltip.shell.explosive").withStyle(TextFormatting.AQUA).withStyle(TextFormatting.BOLD));
            }
        }
        else{
            if(properties.isExplosive()){
                component.append(new TranslationTextComponent("tooltip.shell.explosive").withStyle(TextFormatting.AQUA).withStyle(TextFormatting.BOLD));
            }
            else{
                component.append(new TranslationTextComponent("tooltip.shell.none").withStyle(TextFormatting.DARK_GRAY));
            }
        }

        tooltip.add(component);

    }
}
