package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.energy.CustomEnergyStorage;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public abstract class AbstractAnimateableEnergyTickTE extends LockableTileEntity implements IAnimatable, INamedContainerProvider, ISidedInventory, ITickableTileEntity {
    protected final AnimationFactory factory = new AnimationFactory(this);
    protected CustomEnergyStorage energyStorage = new CustomEnergyStorage(15000);
    protected ItemStackHandler inventory = new ItemStackHandler(1);

    protected AbstractAnimateableEnergyTickTE(TileEntityType<?> typeIn) {
        super(typeIn);
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
        if (this.world != null && this.world.isBlockPowered(this.getPos()) && PAConfig.CONFIG.RedStonePoweredMachines.get()) {
            this.energyStorage.receiveEnergy(1000, false);
        }
    }

    protected abstract <P extends TileEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event);

    protected abstract void playsound();

    @Override
    public int getSizeInventory() {
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
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.energyStorage.deserializeNBT(nbt.getCompound("energy_storage"));
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("energy_storage", this.energyStorage.serializeNBT());
        return compound;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT syncTag = this.getUpdateTag();
        syncTag.put("inventory", this.inventory.serializeNBT());
        syncTag.put("energy", this.energyStorage.serializeNBT());
        return new SUpdateTileEntityPacket(pos, 1, syncTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundNBT syncTag = pkt.getNbtCompound();
        this.inventory.deserializeNBT(syncTag.getCompound("inventory"));
        this.energyStorage.deserializeNBT(syncTag.getCompound("energy"));
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = this.inventory.getStackInSlot(index);
        stack.shrink(count);
        this.inventory.setStackInSlot(index, stack);
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index >=0 && index < this.inventory.getSlots()){
            this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for(int i=0;i<this.inventory.getSlots(); i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);


    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    public CustomEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public abstract void encodeExtraData(PacketBuffer buffer);
}
