package com.yor42.projectazure.gameobject.crafting.ingredients;

import com.lowdragmc.multiblocked.api.recipe.EntityIngredient;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

public class EntityIngredientCompanions extends EntityIngredient {

    public static EntityIngredientCompanions of(EntityType<? extends AbstractEntityCompanion> o) {
        EntityIngredientCompanions ingredient = new EntityIngredientCompanions();
        ingredient.type = o;
        return ingredient;
    }

    @Override
    public void spawn(ServerLevel serverLevel, CompoundTag tag, BlockPos pos) {
        Entity entity = type.spawn(serverLevel, tag, null, null, pos, MobSpawnType.NATURAL, false, false);
        if(entity instanceof AbstractEntityCompanion){
            AbstractEntityCompanion companion = (AbstractEntityCompanion) entity;
            companion.setOrderedToSit(true);
            serverLevel.addFreshEntity(companion);
        }
    }
}
