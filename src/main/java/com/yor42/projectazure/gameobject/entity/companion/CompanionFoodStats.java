package com.yor42.projectazure.gameobject.entity.companion;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion.FOODLEVEL;

public class CompanionFoodStats {

    private int foodLevel = 20;
    private float foodSaturationLevel;
    private float foodExhaustionLevel;
    private int foodTimer;

    public CompanionFoodStats() {
        this.foodSaturationLevel = 5.0F;
    }

    /**
     * Add food stats.
     */
    public void addStats(int foodLevelIn, float foodSaturationModifier) {
        this.foodLevel = Math.min(foodLevelIn + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float)foodLevelIn * foodSaturationModifier * 2.0F, (float)this.foodLevel);
    }

    public void consume(Item maybeFood, ItemStack stack) {
        if (maybeFood.isEdible()) {
            FoodProperties food = maybeFood.getFoodProperties();
            this.addStats(food != null ? food.getNutrition() : 0, food.getSaturationModifier());
        }

    }

    /**
     * Handles the food game logic.
     * same as Vanilla Foodstat. but takes AbstractEntityCompanion Instead of PlayerEntity
     */
    public void tick(AbstractEntityCompanion companion) {
        Difficulty difficulty = companion.level.getDifficulty();
        if (this.foodExhaustionLevel > 4.0F) {
            this.foodExhaustionLevel -= 4.0F;
            if (this.foodSaturationLevel > 0.0F) {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        boolean flag = companion.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        ++this.foodTimer;
        if (flag && this.foodSaturationLevel > 0.0F && companion.shouldHeal() && this.foodLevel >= 20) {
            if (this.foodTimer >= 10) {
                float f = Math.min(this.foodSaturationLevel, 6.0F);
                companion.heal(f / 6.0F);
                this.addExhaustion(f);
                this.foodTimer = 0;
            }
        } else if (flag && this.foodLevel >= 18 && companion.shouldHeal()) {
            if (this.foodTimer >= 80) {
                companion.heal(1.0F);
                this.addExhaustion(6.0F);
                this.foodTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            if (this.foodTimer >= 80) {
                if (companion.getHealth() > 10.0F || difficulty == Difficulty.HARD || companion.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                    //companion.attackEntityFrom(DamageSource.STARVE, 1.0F);
                    companion.addMorale(-0.025F);
                    //companion.addAffection(-0.00001F);
                }

                this.foodTimer = 0;
            }
        } else {
            this.foodTimer = 0;
        }
        companion.getEntityData().set(FOODLEVEL, this.foodLevel);
    }

    /**
     * Reads the food data for the player.
     */
    public void read(CompoundTag compound) {
        if (compound.contains("foodLevel", 99)) {
            this.foodLevel = compound.getInt("foodLevel");
            this.foodTimer = compound.getInt("foodTickTimer");
            this.foodSaturationLevel = compound.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");
        }

    }

    /**
     * Writes the food data for the player.
     */
    public void write(CompoundTag compound) {
        compound.putInt("foodLevel", this.foodLevel);
        compound.putInt("foodTickTimer", this.foodTimer);
        compound.putFloat("foodSaturationLevel", this.foodSaturationLevel);
        compound.putFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }

    /**
     * Get the player's food level.
     */
    public int getFoodLevel() {
        return this.foodLevel;
    }

    /**
     * Get whether the player must eat food.
     */
    public boolean needFood() {
        return this.foodLevel < 20;
    }

    /**
     * adds input to foodExhaustionLevel to a max of 40
     */
    public void addExhaustion(float exhaustion) {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + exhaustion, 40.0F);
    }

    /**
     * Get the player's food saturation level.
     */
    public float getSaturationLevel() {
        return this.foodSaturationLevel;
    }

    public void setFoodLevel(int foodLevelIn) {
        this.foodLevel = foodLevelIn;
    }

    @OnlyIn(Dist.CLIENT)
    public void setFoodSaturationLevel(float foodSaturationLevelIn) {
        this.foodSaturationLevel = foodSaturationLevelIn;
    }

}
