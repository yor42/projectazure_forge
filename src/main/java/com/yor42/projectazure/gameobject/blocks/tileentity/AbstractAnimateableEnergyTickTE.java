package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;

public abstract class AbstractAnimateableEnergyTickTE extends BaseContainerBlockEntity implements IAnimatable, MenuProvider, BlockEntityTicker<AbstractAnimateableEnergyTickTE> {
    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    protected final CustomEnergyStorage energyStorage;
    protected ItemStackHandler inventory;

    protected AbstractAnimateableEnergyTickTE(BlockEntityType<?> typeIn, BlockPos blockpos, BlockState blockstate) {
        this(typeIn, 15000, 1000, 1000, 1, blockpos, blockstate);
    }

    protected AbstractAnimateableEnergyTickTE(BlockEntityType<?> typeIn, int buffercapacity, int inventorysize, BlockPos blockpos, BlockState blockstate) {
        this(typeIn, buffercapacity, buffercapacity, buffercapacity, inventorysize, blockpos, blockstate);
    }

    protected AbstractAnimateableEnergyTickTE(BlockEntityType<?> typeIn, int buffercapacity, int maxin, int maxout, int invsize, BlockPos blockpos, BlockState blockstate) {
        super(typeIn, blockpos, blockstate);
        this.energyStorage = new CustomEnergyStorage(buffercapacity, maxin, maxout);
        this.inventory = new ItemStackHandler(invsize);
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
    public void tick(Level level, BlockPos blockpos, BlockState blockstate, AbstractAnimateableEnergyTickTE abstractanimateableenergytickte) {
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
    public void load(CompoundTag compoundtag) {
        super.load(compoundtag);
        this.energyStorage.deserializeNBT(compoundtag.getCompound("energy_storage"));
        this.inventory.deserializeNBT(compoundtag.getCompound("inventory"));
    }

    @Override
    public void saveAdditional(CompoundTag compoundtag) {
        super.saveAdditional(compoundtag);
        compoundtag.put("inventory", this.inventory.serializeNBT());
        compoundtag.put("energy_storage", this.energyStorage.serializeNBT());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
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
    public boolean stillValid(Player player) {
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

    public abstract void encodeExtraData(FriendlyByteBuf buffer);

    protected int getPowerConsumption(){
        return 100;
    }

    public boolean isPowered(){
        return this.getEnergyStorage().getEnergyStored()>=this.getPowerConsumption();
    }
}
