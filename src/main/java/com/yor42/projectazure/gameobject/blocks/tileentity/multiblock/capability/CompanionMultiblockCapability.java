package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.capability;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.capability.IO;
import com.lowdragmc.multiblocked.api.capability.MultiblockCapability;
import com.lowdragmc.multiblocked.api.capability.proxy.CapabilityProxy;
import com.lowdragmc.multiblocked.api.capability.trait.CapabilityTrait;
import com.lowdragmc.multiblocked.api.recipe.EntityIngredient;
import com.lowdragmc.multiblocked.api.recipe.Recipe;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.api.tile.ComponentTileEntity;
import com.lowdragmc.multiblocked.common.capability.EntityMultiblockCapability;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes.RiftwayRecipes;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes.WeightedRandomRecipe;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IAknOp;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CompanionMultiblockCapability extends MultiblockCapability<EntityIngredient> {

    public static final CompanionMultiblockCapability CAP = new CompanionMultiblockCapability();

    public CompanionMultiblockCapability(){
        super("companion", 0xFF65CB9D);
    }

    @Override
    public EntityIngredient defaultContent() {
        return new EntityIngredient();
    }

    @Override
    public boolean isBlockHasCapability(@Nonnull IO io, @Nonnull TileEntity tileEntity) {
        if (tileEntity instanceof ComponentTileEntity) {
            return ((ComponentTileEntity<?>) tileEntity).hasTrait(CAP);
        }
        return false;
    }

    @Override
    public EntityIngredient copyInner(EntityIngredient content) {
        return content.copy();
    }

    @Override
    protected CapabilityProxy<? extends EntityIngredient> createProxy(@Nonnull IO io, @Nonnull TileEntity tileEntity) {
        return new EntityCapabilityProxy(tileEntity);
    }

    @Override
    public CapabilityTrait createTrait() {
        return new CompanionCapabilityTrait();
    }

    @Override
    public boolean hasTrait() {
        return true;
    }

    @Override
    public BlockInfo[] getCandidates() {
        return new BlockInfo[]{
                BlockInfo.fromBlock(MbdComponents.COMPONENT_BLOCKS_REGISTRY.get(new ResourceLocation(Multiblocked.MODID, "companion"))),
        };
    }

    @Override
    public EntityIngredient of(Object o) {
        return EntityIngredient.of(o);
    }

    @Override
    public EntityIngredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return EntityIngredient.fromJson(json);
    }

    @Override
    public JsonElement serialize(EntityIngredient src, Type typeOfSrc, JsonSerializationContext context) {
        return src.toJson();
    }

    public static class EntityCapabilityProxy extends CapabilityProxy<EntityIngredient> {

        public EntityCapabilityProxy(TileEntity tileEntity) {
            super(CompanionMultiblockCapability.CAP, tileEntity);
        }

        @Override
        protected List<EntityIngredient> handleRecipeInner(IO io, Recipe recipe, List<EntityIngredient> left, @Nullable String slotName, boolean simulate) {
            TileEntity tileEntity =getTileEntity();
            if (tileEntity instanceof ComponentTileEntity) {
                ComponentTileEntity<?> component = (ComponentTileEntity<?>) tileEntity;
                BlockPos pos = component.getBlockPos().relative(component.getFrontFacing());
                if (io == IO.IN) {
                    List<Entity> entities = component.getLevel().getEntities(null, new AxisAlignedBB(
                            pos,
                            pos.offset(1, 1, 1)));
                    for (Entity entity : entities) {
                        if (entity.isAlive()) {
                            if (left.removeIf(ingredient -> ingredient.match(entity))) {
                                if (!simulate) {
                                    entity.remove(false);
                                }
                            }
                        }
                    }
                } else if (io == IO.OUT){
                    if (!simulate && component.getLevel() instanceof ServerWorld) {
                        ServerWorld serverLevel = (ServerWorld) component.getLevel();
                        for (EntityIngredient ingredient : left) {
                            Entity entity = ingredient.type.spawn(serverLevel, ingredient.tag, null, null, pos, SpawnReason.NATURAL, false, false);
                            if(recipe instanceof RiftwayRecipes && entity instanceof AbstractEntityCompanion){
                                UUID playeruuid = ((RiftwayRecipes) recipe).getPlayerUUID();
                                if(playeruuid == null){
                                    return null;
                                }

                                PlayerEntity player = serverLevel.getPlayerByUUID(playeruuid);
                                AbstractEntityCompanion spawnedEntity = (AbstractEntityCompanion) entity;
                                spawnedEntity.tame(player);
                                spawnedEntity.setMorale(150);
                                if(!(spawnedEntity instanceof IAknOp)) {
                                    spawnedEntity.setAffection(50);
                                }
                                serverLevel.addFreshEntity(spawnedEntity);
                            }
                        }
                    }
                    return null;
                }
            }

            return left.isEmpty() ? null : left;
        }
        Set<Entity> entities = new HashSet<>();

        @Override
        protected boolean hasInnerChanged() {
            if (getTileEntity() instanceof ComponentTileEntity<?>) {
                ComponentTileEntity<?> component = (ComponentTileEntity<?>) getTileEntity();
                BlockPos pos = component.getBlockPos().relative(component.getFrontFacing());
                List<Entity> entities = component.getLevel().getEntities(null, new AxisAlignedBB(
                        pos,
                        pos.offset(1, 1, 1)));
                Set<Entity> temp = new HashSet<>();
                for (Entity entity : entities) {
                    if (entity.isAlive()) {
                        temp.add(entity);
                    }
                }
                if (this.entities.size() == temp.size() && this.entities.containsAll(temp)) {
                    return false;
                }
                this.entities = temp;
                return true;
            }
            return false;
        }
    }

}
