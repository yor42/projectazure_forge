package com.yor42.projectazure.intermod.tconstruct.modifiers;

import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;

import java.util.List;

public class AbsorptionModifier extends SingleUseModifier {
    public AbsorptionModifier() {
        super(0x675f3e);
    }

    @Override
    public void afterBlockBreak(IModifierToolStack tool, int level, ToolHarvestContext context) {
        super.afterBlockBreak(tool, level, context);
        if(context.getTargetedState().getBlock() == RegisterBlocks.ORIROCK.get()){
            tool.setDamage(tool.getDamage()-2);
        }
    }

    @Override
    public List<ItemStack> processLoot(IModifierToolStack tool, int level, List<ItemStack> generatedLoot, LootContext context) {
        for(ItemStack stack:generatedLoot){
            if(stack.getItem() == RegisterItems.ORIGINITE.get()){
                generatedLoot.remove(stack);
                tool.setDamage(tool.getDamage()-2);
            }
        }

        return generatedLoot;
    }
}