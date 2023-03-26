package com.yor42.projectazure.gameobject.capability.playercapability;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.*;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ProjectAzurePlayerCapability {
    //looks Kinda Unoptimal if you ask me...
    public ArrayList<AbstractEntityCompanion> companionList = new ArrayList<>();

    public Player player;

    public UUID lastGUIOpenedEntityID;

    public static final ResourceLocation CapabilityID = ModResourceLocation("capability_paplayer");

    public static final Capability<ProjectAzurePlayerCapability> PA_PLAYER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});;

    public ProjectAzurePlayerCapability(Player player){
        this.player = player;
    }

    public ProjectAzurePlayerCapability(){
        this.player = null;
    }

    public ArrayList<AbstractEntityCompanion> getCompanionList(){
        return this.companionList;
    }

    public void addCompanion(AbstractEntityCompanion companion){
        this.companionList.add(companion);
    }

    public void removeCompanion(AbstractEntityCompanion companion){
        this.companionList.remove(companion);
    }

    public boolean isDupe(AbstractEntityCompanion companion){
        return isDupe(companion.getType());
    }

    public boolean isDupe(EntityType<?> companion){
        for(AbstractEntityCompanion existing: this.companionList){
            if(companion == existing.getType()){
                return true;
            }
        }
        return false;
    }

    public static ProjectAzurePlayerCapability getCapability(@Nonnull Player player){
        return player.getCapability(PA_PLAYER_CAPABILITY).orElseThrow(() -> new IllegalStateException("Failed to get PA Player Capability for player " + player));
    }

    public static LazyOptional<ProjectAzurePlayerCapability> getOptional(@Nonnull Player entity){
        LazyOptional<ProjectAzurePlayerCapability> optional = entity.getCapability(PA_PLAYER_CAPABILITY, null).cast();
        if(!optional.isPresent()){
            Main.LOGGER.warn("Failed to get Player Capability!", new Throwable().fillInStackTrace());
        }
        return optional;
    }

    public static ICapabilityProvider createNewCapability(final Player player) {
        return new ICapabilitySerializable<CompoundTag>() {

            final ProjectAzurePlayerCapability inst = new ProjectAzurePlayerCapability(player);
            final LazyOptional<ProjectAzurePlayerCapability> opt = LazyOptional.of(() -> inst);

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                PA_PLAYER_CAPABILITY.getStorage().readNBT(PA_PLAYER_CAPABILITY, inst, null, nbt);
            }

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
                return PA_PLAYER_CAPABILITY.orEmpty(capability, opt);
            }

            @Override
            public CompoundTag serializeNBT() {
                return (CompoundTag) PA_PLAYER_CAPABILITY.getStorage().writeNBT(PA_PLAYER_CAPABILITY, inst, null);
            }
        };
    }

    public CompoundTag serializeNBT(){
        CompoundTag nbt = new CompoundTag();
        ListTag entityList = new ListTag();
        for(AbstractEntityCompanion companion: this.companionList){
            entityList.add(companion.serializeNBT());
        }
        nbt.put("companions", entityList);
        return nbt;
    }

    public void deserializeNBT(CompoundTag compound){
        ListTag entities = compound.getList("companions", Tag.TAG_COMPOUND);
        for(int i=0; i<entities.size(); i++){
            CompoundTag nbt = entities.getCompound(i);
            Level world = this.player.getCommandSenderWorld();
            if(!world.isClientSide()){
                ServerLevel server = (ServerLevel) world;
                Entity entity = server.getEntity(nbt.getUUID("UUID"));
                if(entity instanceof AbstractEntityCompanion && ((AbstractEntityCompanion)entity).getOwner() == this.player){
                    this.companionList.add((AbstractEntityCompanion) entity);
                }
            }
        }
    }

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ProjectAzurePlayerCapability.class, new Inventory(), ProjectAzurePlayerCapability::new);
    }

    private static class Inventory implements Capability.IStorage<ProjectAzurePlayerCapability>{

        @Nullable
        @Override
        public Tag writeNBT(Capability<ProjectAzurePlayerCapability> capability, ProjectAzurePlayerCapability instance, Direction side) {
            return instance.serializeNBT();

        }

        @Override
        public void readNBT(Capability<ProjectAzurePlayerCapability> capability, ProjectAzurePlayerCapability instance, Direction side, Tag nbt) {
            instance.deserializeNBT((CompoundTag) nbt);
        }
    }


}
