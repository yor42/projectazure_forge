package com.yor42.projectazure.intermod.tconstruct.modifiers;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.impl.IncrementalModifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nonnull;
import java.util.List;

import static com.yor42.projectazure.setup.register.RegisterItems.ORIGINIUM_SHARD;

public class AssimilartingModifier extends IncrementalModifier {

    public static final int COLOR = 0x9d5d0b;

    public AssimilartingModifier() {
        super();
    }

    @Override
    public List<ItemStack> processLoot(IToolStackView tool, int level, @Nonnull List<ItemStack> generatedLoot, LootContext context) {

        if(MathUtil.rand.nextFloat()<=(0.2F*level)){
            generatedLoot.add(new ItemStack(ORIGINIUM_SHARD.get(), Math.max(1, (int)(MathUtil.rand.nextFloat()*level))));
        }

        return generatedLoot;
    }
}
