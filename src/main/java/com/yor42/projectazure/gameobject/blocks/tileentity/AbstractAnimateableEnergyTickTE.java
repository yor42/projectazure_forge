package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractAnimateableEnergyTickTE extends BlockEntity implements IAnimatable {
    protected final AnimationFactory factory = new AnimationFactory(this);
    protected CustomEnergyStorage energyStorage = new CustomEnergyStorage(15000);
    protected ItemStackHandler inventory = new ItemStackHandler(1);

    public AbstractAnimateableEnergyTickTE(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller_machine", 0, this::predicate_machine));
    }

    public void tick() {
        if (this.level != null && this.level.hasNeighborSignal(this.getBlockPos()) && PAConfig.CONFIG.RedStonePoweredMachines.get()) {
            this.energyStorage.receiveEnergy(1000, false);
        }
    }

    protected abstract <P extends BlockEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event);

    protected abstract void playsound();

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.energyStorage.deserializeNBT(nbt.getCompound("energy_storage"));
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("energy_storage", this.energyStorage.serializeNBT());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundTag syncTag = pkt.getTag();
        this.inventory.deserializeNBT(syncTag.getCompound("inventory"));
        this.energyStorage.deserializeNBT(syncTag.getCompound("energy"));
    }
    @Nonnull
    public CustomEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @Nonnull
    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public abstract void encodeExtraData(FriendlyByteBuf  buffer);

    protected int getPowerConsumption(){
        return 100;
    };

    public boolean isPowered(){
        return this.getEnergyStorage().getEnergyStored()>=this.getPowerConsumption();
    }
}
