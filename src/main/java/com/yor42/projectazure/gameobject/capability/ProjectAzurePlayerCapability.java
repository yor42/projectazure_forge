package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ProjectAzurePlayerCapability {

    public int OffHandFireDelay = 0;
    public int MainHandFireDelay = 0;
    public ArrayList<AbstractEntityCompanion> companionList = new ArrayList<>();

    public Player player;

    public static final ResourceLocation CapabilityID = ModResourceLocation("capability_paplayer");

    public static final Capability<ProjectAzurePlayerCapability> PA_PLAYER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public ProjectAzurePlayerCapability(Player player) {
        this.player = player;
    }

    public ProjectAzurePlayerCapability() {
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

    public void setDelay(InteractionHand handIn, int delay) {
        if (handIn == InteractionHand.MAIN_HAND) {
            setMainHandFireDelay(delay);
        } else {
            setOffHandFireDelay(delay);
        }
    }

    public ArrayList<AbstractEntityCompanion> getCompanionList() {
        return this.companionList;
    }

    public void addCompanion(AbstractEntityCompanion companion) {
        this.companionList.add(companion);
    }

    public void removeCompanion(AbstractEntityCompanion companion) {
        this.companionList.remove(companion);
    }

    public int getDelay(InteractionHand handIn) {
        return handIn == InteractionHand.MAIN_HAND ? this.getMainHandFireDelay() : getOffHandFireDelay();
    }

    public static ProjectAzurePlayerCapability getCapability(@Nonnull Player player) {
        return player.getCapability(PA_PLAYER_CAPABILITY).orElseThrow(() -> new IllegalStateException("Failed to get PA Player Capability for player " + player));
    }

    public static LazyOptional<ProjectAzurePlayerCapability> getOptional(@Nonnull Player entity) {
        LazyOptional<ProjectAzurePlayerCapability> optional = entity.getCapability(PA_PLAYER_CAPABILITY, null).cast();
        if (!optional.isPresent()) {
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
                player.getCapability(PA_PLAYER_CAPABILITY).ifPresent((cap) -> {
                    cap.deserializeNBT(nbt);
                });
            }

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
                return PA_PLAYER_CAPABILITY.orEmpty(capability, opt);
            }

            @Override
            public CompoundTag serializeNBT() {
                return player.getCapability(PA_PLAYER_CAPABILITY).map(ProjectAzurePlayerCapability::serializeNBT).orElse(new CompoundTag());
            }
        };
    }

    public @NotNull CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("mainHandDelay", this.getMainHandFireDelay());
        nbt.putInt("offHandDelay", this.getOffHandFireDelay());
        ListTag entityList = new ListTag();
        for (AbstractEntityCompanion companion : this.companionList) {
            entityList.add(companion.serializeNBT());
        }
        nbt.put("companions", entityList);
        return nbt;
    }

    public void deserializeNBT(CompoundTag compound) {
        this.setMainHandFireDelay(compound.getInt("mainHandDelay"));
        this.setMainHandFireDelay(compound.getInt("offHandDelay"));
        ListTag entities = compound.getList("companions", 0);
        for (int i = 0; i < entities.size(); i++) {
            CompoundTag nbt = entities.getCompound(i);
            Level world = this.player.getCommandSenderWorld();
            if (!world.isClientSide()) {
                ServerLevel server = (ServerLevel) world;
                Entity entity = server.getEntity(nbt.getUUID("UUID"));
                if (entity instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) entity).getOwner() == this.player) {
                    this.companionList.add((AbstractEntityCompanion) entity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.register(ProjectAzurePlayerCapability.class);
    }
}
