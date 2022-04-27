package com.yor42.projectazure.gameobject.items.materials;

import com.yor42.projectazure.data.ModTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;

public enum ModMaterials implements Tier {

    CHIXIAO(3, 2500, 7.5F, 7, 8, () -> {
        return Ingredient.of(ModTags.Items.INGOT_TIN);}),

    SHEATH(2, 1256, 5.5F, 5, 8, () -> {
        return Ingredient.of(ModTags.Items.INGOT_TIN);}),

    FLEXABLESWORD(2, 340, 5.5F, 3, 8, () -> {
        return Ingredient.of(ModTags.Items.INGOT_TIN);}),

    SLEDGEHAMMER(2, 600, 0.5F, 1, 3, () -> {
        return Ingredient.of(Tags.Items.INGOTS_IRON);}),

    WARHAMMER(7, 600, 0.5F, 6, 3, () -> {
        return Ingredient.of(Tags.Items.INGOTS_IRON);}),

    CLAYMORE(2, 5100, 1F, 11, 3, () -> {
        return Ingredient.of(ModTags.Items.INGOT_STEEL);}),

    CRESCENT_KATANA_KURO(2, 756, 1F, 5, 3, () -> {
        return Ingredient.of(ModTags.Items.INGOT_STEEL);}),

    CRESCENT_KATANA_SHIRO(2, 540, 1F, 3, 3, () -> {
        return Ingredient.of(Tags.Items.INGOTS_IRON);}),

    KNIFE(2, 500, 6.0F, 1.0F, 14, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    ModMaterials(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyLoadedValue<>(repairMaterialIn);
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
