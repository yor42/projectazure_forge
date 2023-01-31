package com.yor42.projectazure.gameobject.crafting.ingredients;

import com.lowdragmc.multiblocked.api.recipe.EntityIngredient;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EntityIngredientCompanions extends EntityIngredient {

    public static EntityIngredientCompanions of(EntityType<? extends AbstractEntityCompanion> o) {
        EntityIngredientCompanions ingredient = new EntityIngredientCompanions();
        ingredient.type = o;
        return ingredient;
    }

    @Override
    public void spawn(ServerWorld serverLevel, CompoundNBT tag, BlockPos pos) {
        Entity entity = type.spawn(serverLevel, tag, null, null, pos, SpawnReason.NATURAL, false, false);
        if(entity instanceof AbstractEntityCompanion){
            AbstractEntityCompanion companion = (AbstractEntityCompanion) entity;
            companion.setOrderedToSit(true);
            serverLevel.addFreshEntity(companion);
        }
    }
}
