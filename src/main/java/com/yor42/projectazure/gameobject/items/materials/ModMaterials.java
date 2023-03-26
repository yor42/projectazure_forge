package com.yor42.projectazure.gameobject.items.materials;

import com.yor42.projectazure.data.ModTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;

public enum ModMaterials implements Tier {

    CHIXIAO(3, 2500, 7.5F, 7, 8, () -> Ingredient.of(ModTags.Items.INGOT_TIN)),

    SHEATH(2, 1256, 5.5F, 5, 8, () -> Ingredient.of(ModTags.Items.INGOT_TIN)),

    FLEXABLESWORD(2, 340, 5.5F, 3, 8, () -> Ingredient.of(ModTags.Items.INGOT_TIN)),

    GRAVINET(2, 924, 3.5F, 9, 3, () -> Ingredient.of(ModTags.Items.INGOT_STEEL)),

    EXCALIBUR(3, 1024, 5.5F, 4, 1, () -> Ingredient.of(Tags.Items.INGOTS_IRON)),

    SLEDGEHAMMER(2, 600, 0.5F, 1, 3, () -> Ingredient.of(Tags.Items.INGOTS_IRON)),

    WARHAMMER(7, 600, 0.5F, 6, 3, () -> Ingredient.of(Tags.Items.INGOTS_IRON)),

    CLAYMORE(2, 5100, 1F, 11, 3, () -> Ingredient.of(ModTags.Items.INGOT_STEEL)),

    CRESCENT_KATANA_KURO(2, 756, 1F, 5, 3, () -> Ingredient.of(ModTags.Items.INGOT_STEEL)),

    SOULUMSWORD(2, 800, 1F, 10, 8, () -> Ingredient.of(ModTags.Items.INGOT_STEEL)),

    CRESCENT_KATANA_SHIRO(2, 540, 1F, 3, 3, () -> Ingredient.of(Tags.Items.INGOTS_IRON)),

    KNIFE(2, 730, 6.0F, 2.0F, 3, () -> Ingredient.of(ModTags.Items.INGOT_STEEL)),

    KITCHEN_KNIFE(1, 600, 4.5F, 4.0F, 5, () -> Ingredient.of(Tags.Items.INGOTS_IRON)),

    LEAD(1, 125, 5.5F, 6F, 6, () -> Ingredient.of(ModTags.Items.INGOT_LEAD)),
    STEEL(2, 310, 8.0F, 3.0F, 14, () -> Ingredient.of(ModTags.Items.INGOT_STEEL)),
    COPPER(1, 205, 5F, 1.5F, 12, () -> Ingredient.of(ModTags.Items.INGOT_COPPER)),
    TIN(1, 220, 5F, 1.5F, 10, () -> Ingredient.of(ModTags.Items.INGOT_TIN)),
    BRONZE(1, 230, 5.5F, 2.0F, 13, () -> Ingredient.of(ModTags.Items.INGOT_BRONZE)),
    RMA_70_12(2, 276, 6.75F, 2F, 15, () -> Ingredient.of(ModTags.Items.INGOT_RMA7012)),


    RMA_70_24(3, 1200, 8.25F, 4.5F, 18, () -> Ingredient.of(ModTags.Items.INGOT_RMA7024)),

    D32(4, 2562, 12F, 3F, 30, () -> Ingredient.of(ModTags.Items.INGOT_D32));

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

    public String getmaterialname(){
        return this.toString().toLowerCase();
    }

    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
