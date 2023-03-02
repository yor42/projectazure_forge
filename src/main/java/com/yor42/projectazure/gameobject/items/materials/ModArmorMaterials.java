package com.yor42.projectazure.gameobject.items.materials;

import mekanism.api.annotations.NonNull;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import javax.annotation.Nonnull;

public class ModArmorMaterials {

    public enum ArmorModMaterials implements IArmorMaterial{
        GASMASK("gasmask",new int[]{0, 0, 0, 0}, new int[]{2,0,0,0}, 0, 0, 0, SoundEvents.ARMOR_EQUIP_LEATHER, Ingredient.EMPTY);

        private final int[] durability, defence;
        private final int enchantment;
        private final float toughness, kbresistance;
        private final SoundEvent equipSound;
        private final Ingredient repairItem;
        private final String name;

        ArmorModMaterials(String name, int[] durability, int[] defence, int enchantment, float toughness, float knockbackresistance, SoundEvent equipsound, Ingredient repairitem){
            this.name = name;
            this.durability = durability;
            this.defence = defence;
            this.enchantment = enchantment;
            this.toughness = toughness;
            this.kbresistance = knockbackresistance;
            this.equipSound = equipsound;
            this.repairItem = repairitem;
        }

        @Override
        public int getDurabilityForSlot(EquipmentSlotType p_200896_1_) {
            return this.durability[p_200896_1_.getIndex()];
        }

        @Override
        public int getDefenseForSlot(EquipmentSlotType p_200902_1_) {
            return this.defence[p_200902_1_.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return this.enchantment;
        }

        @Override
        @NonNull
        public SoundEvent getEquipSound() {
            return this.equipSound;
        }

        @Nonnull
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public float getToughness() {
            return this.toughness;
        }

        @Override
        public float getKnockbackResistance() {
            return this.kbresistance;
        }
    }

}
