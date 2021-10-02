package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.interfaces.IRiggingContainerSupplier;
import com.yor42.projectazure.network.packets.syncRiggingCapabilityPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.FluidPredicates.FuelOilPredicate;

//fallback class for new common(dynamic) rigging inventory
public class RiggingInventoryCapability implements INamedContainerProvider, IRiggingContainerSupplier, ICapabilityProvider {
    protected final LivingEntity entity;
    protected final ItemStack stack;

    protected final ItemStackHandler equipments = new ItemStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            RiggingInventoryCapability.this.saveAll();

        }
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    protected final ItemStackHandler hangar = new ItemStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            RiggingInventoryCapability.this.saveAll();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    protected final RiggintFuelTank fuelTank = new RiggintFuelTank(null , 5000, FuelOilPredicate){
        @Override
        protected void onContentsChanged() {
            RiggingInventoryCapability.this.saveAll();
            Main.LOGGER.debug("Saving Fluid in Rigging...");
        }
    };

    public RiggingInventoryCapability(ItemStack stack){
        this(stack, null);
    }

    public RiggingInventoryCapability(ItemStack stack, LivingEntity entity){
        this.stack = stack;
        this.entity = entity;
        int slotCount = ((ItemRiggingBase) stack.getItem()).getTotalSlotCount();
        this.getEquipments().setSize(slotCount);
        int fuelCapacity = ((ItemRiggingBase) stack.getItem()).getFuelTankCapacity();
        this.fuelTank.setItemStack(stack);
        this.fuelTank.setCapacity(fuelCapacity);

        int HangerSlots = ((ItemRiggingBase) stack.getItem()).getHangerSlots();

        if(this.getHangar() != null) {
            this.getHangar().setSize(HangerSlots);
        }

        this.loadEquipments(this.getNBT(stack));
    }

    public void saveAll(){
        this.saveEquipments(this.getNBT(this.stack));
        this.saveHanger(this.getNBT(this.stack));
        this.saveFuel(this.getNBT(this.stack));
        this.sendpacket(this.getNBT(this.stack));
    }

    private void saveHanger(CompoundNBT nbt) {
        nbt.put("hanger", this.hangar.serializeNBT());
    }

    private void saveFuel(CompoundNBT nbt) {
        nbt.put("fuels", this.fuelTank.writeToNBT(new CompoundNBT()));
    }

    public void sendpacket(CompoundNBT nbt) {
        if(this.entity instanceof PlayerEntity){
            Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new syncRiggingCapabilityPacket(this));
        }
    }

    @Nullable
    public ItemStackHandler getHangar(){
        if(this.hangar.getSlots()>0) {
            return this.hangar;
        }
        return null;
    }

    public void saveEquipments(CompoundNBT nbt) {
        nbt.put("Inventory" ,this.equipments.serializeNBT());
    }

    public CompoundNBT getNBT(ItemStack stack) {
        return stack.getOrCreateTag();
    }

    public static void openGUI(ServerPlayerEntity serverPlayerEntity, ItemStack stack) {
        if (!serverPlayerEntity.world.isRemote) {
            NetworkHooks.openGui(serverPlayerEntity, new RiggingInventoryCapability(stack, serverPlayerEntity));//packetBuffer.writeItemStack(stack, false).writeByte(screenID));
        }
        Main.PROXY.setSharedStack(stack);
    }

    public void loadEquipments(CompoundNBT nbt){
        this.equipments.deserializeNBT(nbt.getCompound("Inventory"));
        this.hangar.deserializeNBT(nbt.getCompound("hanger"));
        this.fuelTank.readFromNBT(nbt.getCompound("fuels"));
    }

    @Override
    public ItemStackHandler getEquipments() {
        return this.equipments;
    }

    public RiggintFuelTank getFuelTank() {
        return this.fuelTank;
    }

    @Override
    public ItemStack getRigging() {
        return this.stack;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.rigginginventory");
    }
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity entity) {
        return new RiggingContainer(windowID, playerInventory, this);
    }

    public ItemStack getItemStack(){
        return this.stack;
    }

    private final LazyOptional<ItemStackHandler> ITEMHANDLERCAP = LazyOptional.of(()->this.equipments);
    private final LazyOptional<RiggintFuelTank> FUELTANKCAP = LazyOptional.of(()->this.fuelTank);
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return ITEMHANDLERCAP.cast();
        }
        else if(cap == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY){
            return FUELTANKCAP.cast();
        }
        else{
            return LazyOptional.empty();
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return this.getCapability(cap, null);
    }
}
