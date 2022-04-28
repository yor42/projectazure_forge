package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    protected MultiblockStructuralBlockTE(BlockEntityType<?> typeIn) {
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
    protected <P extends BlockEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    protected void playsound() {

    }

    @Override
    public void encodeExtraData(FriendlyByteBuf  buffer) {
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
    protected void SpawnResultEntity(ServerPlayer owner) {
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

}
