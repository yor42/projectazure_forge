package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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

public abstract class AbstractAnimateableEnergyTickTE extends BlockEntity implements IAnimatable, Nameable, Furnace {
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

    @Override
    public void tick() {
        if (this.level != null && this.level.hasNeighborSignal(this.getBlockPos()) && PAConfig.CONFIG.RedStonePoweredMachines.get()) {
            this.energyStorage.receiveEnergy(1000, false);
        }
    }

    protected abstract <P extends BlockEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event);

    protected abstract void playsound();

    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i=0;i<this.inventory.getSlots(); i++){
            if(this.inventory.getStackInSlot(i) != ItemStack.EMPTY){
                return false;
            }
        }
        return true;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.energyStorage.deserializeNBT(nbt.getCompound("energy_storage"));
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("energy_storage", this.energyStorage.serializeNBT());
        return compound;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundTag syncTag = this.getUpdateTag();
        syncTag.put("inventory", this.inventory.serializeNBT());
        syncTag.put("energy", this.energyStorage.serializeNBT());
        return new SUpdateTileEntityPacket(worldPosition, 1, syncTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundTag syncTag = pkt.getTag();
        this.inventory.deserializeNBT(syncTag.getCompound("inventory"));
        this.energyStorage.deserializeNBT(syncTag.getCompound("energy"));
    }

    @Override
    public ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = this.inventory.getStackInSlot(index);
        stack.shrink(count);
        this.inventory.setStackInSlot(index, stack);
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index >=0 && index < this.inventory.getSlots()){
            this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack);

        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public void clearContent() {
        for(int i=0;i<this.inventory.getSlots(); i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
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
