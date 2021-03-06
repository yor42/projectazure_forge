package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ProjectAzurePlayerCapability {

    public int OffHandFireDelay = 0;
    public int MainHandFireDelay = 0;

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

    public int getOffHandFireDelay() {
        return this.OffHandFireDelay;
    }

    public int getMainHandFireDelay() {
        return this.MainHandFireDelay;
    }

    public void setOffHandFireDelay(int leftHandFireDelay) {
        this.OffHandFireDelay = leftHandFireDelay;
    }

    public void setMainHandFireDelay(int rightHandFireDelay) {
        this.MainHandFireDelay = rightHandFireDelay;
    }

    public void setDelay(Hand handIn, int delay){
        if (handIn == Hand.MAIN_HAND) {
            setMainHandFireDelay(delay);
        } else {
            setOffHandFireDelay(delay);
        }
    }

    public int getDelay(Hand handIn){
        return handIn == Hand.MAIN_HAND? this.getMainHandFireDelay():getOffHandFireDelay();
    }

    public static ProjectAzurePlayerCapability getCapability(@Nonnull PlayerEntity player){
        return (ProjectAzurePlayerCapability) player.getCapability(PA_PLAYER_CAPABILITY).orElseThrow(() -> new IllegalStateException("Failed to get PA Player Capability for player " + player));
    }

    public static LazyOptional<ProjectAzurePlayerCapability> getOptional(@Nonnull PlayerEntity entity){
        LazyOptional<ProjectAzurePlayerCapability> optional = entity.getCapability(PA_PLAYER_CAPABILITY, null).cast();
        if(!optional.isPresent()){
            Main.LOGGER.warn("Failed to get Player Capability. This might break mod functionality.", new Throwable().fillInStackTrace());
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

    public INBT Savedata(INBT compound){
        return compound;
    }

    public void Loaddata(INBT compound){}

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ProjectAzurePlayerCapability.class, new Inventory(), ProjectAzurePlayerCapability::new);
    }

    private static class Inventory implements Capability.IStorage<ProjectAzurePlayerCapability>{

        @Nullable
        @Override
        public INBT writeNBT(Capability<ProjectAzurePlayerCapability> capability, ProjectAzurePlayerCapability instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            return instance.Savedata(nbt);

        }

        @Override
        public void readNBT(Capability<ProjectAzurePlayerCapability> capability, ProjectAzurePlayerCapability instance, Direction side, INBT nbt) {
            instance.Loaddata(nbt);
        }
    }


}
