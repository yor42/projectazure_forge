package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MultiblockStructuralBlockTE extends MultiblockBaseTE{
    protected MultiblockStructuralBlockTE(TileEntityType<?> typeIn) {
        super(typeIn);
        this.energyStorage.setMaxEnergy(0);
        this.inventory = new ItemStackHandler(1){
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return false;
            }
        };
    }

    @Override
    protected <P extends TileEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    protected void playsound() {

    }

    @Override
    public void encodeExtraData(PacketBuffer buffer) {
    }

    @Override
    protected double getResourceChanceBonus() {
        return 0;
    }

    @Override
    protected boolean canStartProcess() {
        return false;
    }

    @Override
    protected void UseGivenResource() {
    }

    @Override
    protected void SpawnResultEntity(ServerPlayerEntity owner) {
    }

    @Override
    public void registerRollEntry() {
    }


    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if(this.hasMaster()) {
            if (capability == CapabilityEnergy.ENERGY)
                return LazyOptional.of(()->this.getMaster().getEnergyStorage()).cast();
            else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
                return LazyOptional.of(()->this.getMaster().getInventory()).cast();
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        TileEntity tile = this.getMaster();
        if(tile instanceof MultiblockDrydockTE){
            return ((MultiblockDrydockTE) tile).canInsertItem(index, itemStackIn, direction);
        }


        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {

        TileEntity tile = this.getMaster();
        if(tile instanceof MultiblockDrydockTE){
            return ((MultiblockDrydockTE) tile).canExtractItem(index, stack, direction);
        }

        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("Never Gonna Give you up");
    }

    @Override
    public boolean canOpen(PlayerEntity p_213904_1_) {
        return false;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }


}
