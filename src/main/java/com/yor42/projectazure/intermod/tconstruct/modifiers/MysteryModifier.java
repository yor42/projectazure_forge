package com.yor42.projectazure.intermod.tconstruct.modifiers;

import com.yor42.projectazure.libs.utils.MathUtil;
//import javafx.scene.paint.Color;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
import slimeknights.tconstruct.library.modifiers.SingleLevelModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;

import javax.annotation.Nullable;
import java.util.List;

public class MysteryModifier extends IncrementalModifier {
    public MysteryModifier() {
        super(0x59f8ff);
    }

    @Override
    public int onDamageTool(IModifierToolStack toolStack, int level, int amount, @Nullable LivingEntity holder) {

        if(MathUtil.rand.nextFloat()*11<level){
            return 0;
        }

        return super.onDamageTool(toolStack, level, amount, holder);
    }

    @Override
    public void addInformation(IModifierToolStack tool, int level, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        super.addInformation(tool, level, player, tooltip, tooltipKey, tooltipFlag);
        tooltip.add(new TranslationTextComponent("modifier.projectazure.mystery.tooltip", String.format("%.2f", level/11F*100)+"%").withStyle(TextFormatting.AQUA));
    }
}
