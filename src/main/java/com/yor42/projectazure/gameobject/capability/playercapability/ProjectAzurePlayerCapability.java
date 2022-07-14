package com.yor42.projectazure.gameobject.capability.playercapability;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ProjectAzurePlayerCapability {
    public int OffHandFireDelay = 0;
    public int MainHandFireDelay = 0;
    //looks Kinda Unoptimal if you ask me...
    public ArrayList<AbstractEntityCompanion> companionList = new ArrayList<>();

    public PlayerEntity player;

    public static final ResourceLocation CapabilityID = ModResourceLocation("capability_paplayer");

    @CapabilityInject(ProjectAzurePlayerCapability.class)
    public static final Capability<ProjectAzurePlayerCapability> PA_PLAYER_CAPABILITY = null;

    public ProjectAzurePlayerCapability(PlayerEntity player){
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

    public static ProjectAzurePlayerCapability getCapability(@Nonnull PlayerEntity player){
        return player.getCapability(PA_PLAYER_CAPABILITY).orElseThrow(() -> new IllegalStateException("Failed to get PA Player Capability for player " + player));
    }

    public static LazyOptional<ProjectAzurePlayerCapability> getOptional(@Nonnull PlayerEntity entity){
        LazyOptional<ProjectAzurePlayerCapability> optional = entity.getCapability(PA_PLAYER_CAPABILITY, null).cast();
        if(!optional.isPresent()){
            Main.LOGGER.warn("Failed to get Player Capability!", new Throwable().fillInStackTrace());
        }
        return optional;
    }

    public static ICapabilityProvider createNewCapability(final PlayerEntity player) {
        return new ICapabilitySerializable<CompoundNBT>() {

            final ProjectAzurePlayerCapability inst = new ProjectAzurePlayerCapability(player);
            final LazyOptional<ProjectAzurePlayerCapability> opt = LazyOptional.of(() -> inst);

            @Override
            public void deserializeNBT(CompoundNBT nbt) {
                PA_PLAYER_CAPABILITY.getStorage().readNBT(PA_PLAYER_CAPABILITY, inst, null, nbt);
            }

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
                return PA_PLAYER_CAPABILITY.orEmpty(capability, opt);
            }

            @Override
            public CompoundNBT serializeNBT() {
                return (CompoundNBT) PA_PLAYER_CAPABILITY.getStorage().writeNBT(PA_PLAYER_CAPABILITY, inst, null);
            }
        };
    }

    public CompoundNBT serializeNBT(){
        CompoundNBT nbt = new CompoundNBT();
        ListNBT entityList = new ListNBT();
        for(AbstractEntityCompanion companion: this.companionList){
            entityList.add(companion.serializeNBT());
        }
        nbt.put("companions", entityList);
        return nbt;
    }

    public void deserializeNBT(CompoundNBT compound){
        ListNBT entities = compound.getList("companions", Constants.NBT.TAG_COMPOUND);
        for(int i=0; i<entities.size(); i++){
            CompoundNBT nbt = entities.getCompound(i);
            World world = this.player.getCommandSenderWorld();
            if(!world.isClientSide()){
                ServerWorld server = (ServerWorld) world;
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
        public INBT writeNBT(Capability<ProjectAzurePlayerCapability> capability, ProjectAzurePlayerCapability instance, Direction side) {
            return instance.serializeNBT();

        }

        @Override
        public void readNBT(Capability<ProjectAzurePlayerCapability> capability, ProjectAzurePlayerCapability instance, Direction side, INBT nbt) {
            instance.deserializeNBT((CompoundNBT) nbt);
        }
    }


}
