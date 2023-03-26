package com.yor42.projectazure.intermod.tconstruct.modifiers;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;

import javax.annotation.Nonnull;
import java.util.List;

import static com.yor42.projectazure.setup.register.RegisterItems.ORIGINIUM_SHARD;

public class AssimilartingModifier extends IncrementalModifier {
    public AssimilartingModifier() {
        super(0x9d5d0b);
    }

    @Override
    public List<ItemStack> processLoot(IModifierToolStack tool, int level, @Nonnull List<ItemStack> generatedLoot, LootContext context) {

        if(MathUtil.rand.nextFloat()<=(0.2F*level)){
            generatedLoot.add(new ItemStack(ORIGINIUM_SHARD.get(), Math.max(1, (int)(MathUtil.rand.nextFloat()*level))));
        }

        return generatedLoot;
    }
}
