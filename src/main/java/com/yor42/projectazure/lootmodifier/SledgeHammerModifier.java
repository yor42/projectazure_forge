package com.yor42.projectazure.lootmodifier;

import com.google.gson.JsonObject;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.utils.ItemStackWithChance;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/*
 * This class is distributed as part of the Ex Nihilo Sequentia Mod.
 * Get the Source Code in github:
 * https://github.com/NovaMachina-Mods/ExNihiloSequentia
 *
 * Ex Nihilo Sequentia is Open Source and distributed under the
 * CC BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en
 */
public class SledgeHammerModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected SledgeHammerModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        ItemStack tool = context.getParamOrNull(LootParameters.TOOL);
        BlockState blockState = context.getParamOrNull(LootParameters.BLOCK_STATE);
        List<ItemStack> newLoot = new ArrayList<>();

        if (tool != null && blockState != null && ModTags.Items.SLEDGEHAMMER.contains(tool.getItem()) && Main.CRUSHING_REGISTRY.isHammerable(blockState.getBlock())) {
            List<ItemStackWithChance> list = Main.CRUSHING_REGISTRY.getResult(blockState.getBlock());
            for (ItemStackWithChance stackWithChance : list) {
                if (new Random().nextFloat() <= stackWithChance.getChance()) {
                    if (stackWithChance.getStack() != ItemStack.EMPTY) {
                        newLoot.add(stackWithChance.getStack());
                    }
                }
            }
        }
        if (!newLoot.isEmpty()) {
            generatedLoot = newLoot;
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SledgeHammerModifier> {


        @Override
        public SledgeHammerModifier read(ResourceLocation location, JsonObject object,
                                         ILootCondition[] ailootcondition) {
            return new SledgeHammerModifier(ailootcondition);
        }

        @Override
        public JsonObject write(SledgeHammerModifier instance) {
            return makeConditions(instance.conditions);
        }
    }

}
