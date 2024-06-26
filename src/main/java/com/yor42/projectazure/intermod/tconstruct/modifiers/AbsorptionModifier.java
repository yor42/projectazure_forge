package com.yor42.projectazure.intermod.tconstruct.modifiers;

import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.impl.SingleLevelModifier;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class AbsorptionModifier extends SingleLevelModifier {

    public static final int COLOR = 0x675f3e;
    public AbsorptionModifier() {
        super();
    }


    @Override
    public void afterBlockBreak(IToolStackView tool, int level, ToolHarvestContext context) {
        super.afterBlockBreak(tool, level, context);
        if(context.getTargetedState().getBlock() == RegisterBlocks.ORIROCK.get()){
            tool.setDamage(tool.getDamage()-2);
        }
    }

    @Override
    public List<ItemStack> processLoot(IToolStackView tool, int level, List<ItemStack> generatedLoot, LootContext context) {
        for(ItemStack stack:generatedLoot){
            if(stack.getItem() == RegisterItems.ORIGINITE.get()){
                generatedLoot.remove(stack);
                tool.setDamage(tool.getDamage()-2);
            }
        }

        return generatedLoot;
    }
}
