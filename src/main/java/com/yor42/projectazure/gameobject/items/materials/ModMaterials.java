package com.yor42.projectazure.gameobject.items.materials;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;

public enum ModMaterials implements IItemTier {

    CHIXIAO(3, 2500, 7.5F, 7, 8, () -> {
        return Ingredient.of(ModTags.Items.INGOT_TIN);}),

    SHEATH(2, 1256, 5.5F, 5, 8, () -> {
        return Ingredient.of(ModTags.Items.INGOT_TIN);}),

    SLEDGEHAMMER(2, 600, 1F, 10, 3, () -> {
        return Ingredient.of(Tags.Items.INGOTS_IRON);});

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    ModMaterials(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyValue<>(repairMaterialIn);
    }

    public int getUses() {
        return this.maxUses;
    }

    public float getSpeed() {
        return this.efficiency;
    }

    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    public int getLevel() {
        return this.harvestLevel;
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
