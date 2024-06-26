package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.capability;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.capability.IO;
import com.lowdragmc.multiblocked.api.capability.MultiblockCapability;
import com.lowdragmc.multiblocked.api.capability.proxy.CapabilityProxy;
import com.lowdragmc.multiblocked.api.capability.trait.CapabilityTrait;
import com.lowdragmc.multiblocked.api.gui.recipe.ContentWidget;
import com.lowdragmc.multiblocked.api.recipe.Recipe;
import com.lowdragmc.multiblocked.api.recipe.ingredient.EntityIngredient;
import com.lowdragmc.multiblocked.api.recipe.serde.content.IContentSerializer;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.api.tile.ComponentTileEntity;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.client.gui.multiblocked.CompanionContentWidget;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes.RiftwayRecipes;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.crafting.ingredients.EntityIngredientCompanions;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CompanionMultiblockCapability extends MultiblockCapability<EntityIngredientCompanions> {

    public static final CompanionMultiblockCapability CAP = new CompanionMultiblockCapability();

    public CompanionMultiblockCapability(){
        super("companion", 0xFF65CB9D, new IContentSerializer<>() {

            @Override
            public EntityIngredientCompanions fromJson(JsonElement json) {
                return (EntityIngredientCompanions) EntityIngredientCompanions.fromJson(json);
            }

            @Override
            public JsonElement toJson(EntityIngredientCompanions content) {
                return content.toJson();
            }

            @Override
            public EntityIngredientCompanions of(Object o) {
                return (EntityIngredientCompanions) EntityIngredientCompanions.of(o);
            }
        });
    }

    @Override
    public EntityIngredientCompanions defaultContent() {
        return new EntityIngredientCompanions();
    }

    @Override
    public boolean isBlockHasCapability(@Nonnull IO io, @Nonnull BlockEntity tileEntity) {
        if (tileEntity instanceof ComponentTileEntity) {
            return ((ComponentTileEntity<?>) tileEntity).hasTrait(CAP);
        }
        return false;
    }

    @Override
    public EntityIngredientCompanions copyInner(EntityIngredientCompanions content) {
        return (EntityIngredientCompanions) content.copy();
    }

    @Override
    public ContentWidget<? super EntityIngredient> createContentWidget() {
        return new CompanionContentWidget();
    }

    @Override
    protected CapabilityProxy<? extends EntityIngredientCompanions> createProxy(@Nonnull IO io, @Nonnull BlockEntity tileEntity) {
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
    public EntityIngredientCompanions of(Object o) {
        return (EntityIngredientCompanions) EntityIngredientCompanions.of(o);
    }

    @Override
    public EntityIngredientCompanions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (EntityIngredientCompanions) EntityIngredientCompanions.fromJson(json);
    }


    public static class EntityCapabilityProxy extends CapabilityProxy<EntityIngredientCompanions> {

        public EntityCapabilityProxy(BlockEntity tileEntity) {
            super(CompanionMultiblockCapability.CAP, tileEntity);
        }

        @Override
        protected List<EntityIngredientCompanions> handleRecipeInner(IO io, Recipe recipe, List<EntityIngredientCompanions> left, @Nullable String slotName, boolean simulate) {
            BlockEntity tileEntity =getTileEntity();
            if (tileEntity instanceof ComponentTileEntity) {
                ComponentTileEntity<?> component = (ComponentTileEntity<?>) tileEntity;
                BlockPos pos = component.getBlockPos().relative(component.getFrontFacing());
                if (io == IO.IN) {
                    List<Entity> entities = component.getLevel().getEntities(null, new AABB(
                            pos,
                            pos.offset(1, 1, 1)));
                    for (Entity entity : entities) {
                        if (entity.isAlive()) {
                            if (left.removeIf(ingredient -> ingredient.match(entity))) {
                                if (!simulate) {
                                    entity.remove(Entity.RemovalReason.DISCARDED);
                                }
                            }
                        }
                    }
                } else if (io == IO.OUT){
                    if (!simulate && component.getLevel() instanceof ServerLevel) {
                        ServerLevel serverLevel = (ServerLevel) component.getLevel();
                        if(recipe instanceof RiftwayRecipes) {
                            UUID playeruuid = ((RiftwayRecipes) recipe).getPlayerUUID();
                            if (playeruuid == null) {
                                return null;
                            }
                            Player player = serverLevel.getPlayerByUUID(playeruuid);
                            ProjectAzurePlayerCapability cap = ProjectAzurePlayerCapability.getCapability(player);
                            for (EntityIngredient ingredient : left) {
                                Entity entity;
                                if (cap.isDupe(ingredient.type) && !PAConfig.CONFIG.ALLOW_DUPLICATE.get()) {
                                    ItemStack stack = new ItemStack(RegisterItems.TOKEN.get());
                                    stack.getOrCreateTag().putString("vaildentity", ingredient.type.toString());
                                    entity = new ItemEntity(serverLevel, pos.getX(), pos.getY(), pos.getZ(), stack);
                                }
                                else {
                                    entity = ingredient.type.spawn(serverLevel, ingredient.tag, null, null, pos, MobSpawnType.NATURAL, false, false);
                                }


                                if (entity instanceof AbstractEntityCompanion) {
                                    AbstractEntityCompanion spawnedEntity = (AbstractEntityCompanion) entity;
                                    spawnedEntity.handleInitialspawn(player);
                                    serverLevel.addFreshEntity(spawnedEntity);

                                }
                                else{
                                    serverLevel.addFreshEntity(entity);
                                }
                            }
                        }
                        else{
                            for (EntityIngredient ingredient : left) {
                                Entity entity = ingredient.type.spawn(serverLevel, ingredient.tag, null, null, pos, MobSpawnType.NATURAL, false, false);
                                serverLevel.addFreshEntity(entity);
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
                List<Entity> entities = component.getLevel().getEntities(null, new AABB(
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
