package com.yor42.projectazure.intermod.tconstruct.modifiers;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.tconstruct.library.modifiers.impl.IncrementalModifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.utils.TooltipKey;

import javax.annotation.Nullable;
import java.util.List;

public class MysteryModifier extends IncrementalModifier {

    public static final int COLOR = 0x59f8ff;
    public MysteryModifier() {
        super();
    }

    @Override
    public int onDamageTool(IToolStackView toolStack, int level, int amount, @Nullable LivingEntity holder) {

        if(MathUtil.rand.nextFloat()*11<level){
            return 0;
        }

        return super.onDamageTool(toolStack, level, amount, holder);
    }

    @Override
    public void addInformation(IToolStackView tool, int level, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        super.addInformation(tool, level, player, tooltip, tooltipKey, tooltipFlag);
        tooltip.add(new TranslatableComponent("modifier.projectazure.mystery.tooltip", String.format("%.2f", level/11F*100)+"%").withStyle(ChatFormatting.AQUA));
    }
}
