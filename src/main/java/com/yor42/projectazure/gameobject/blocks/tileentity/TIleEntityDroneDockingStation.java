package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.entity.misc.EntityLogisticsDrone;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import com.yor42.projectazure.libs.ItemFilterEntry;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TIleEntityDroneDockingStation extends LockableTileEntity implements ITickableTileEntity {

    protected boolean isDestination = false;
    protected final CustomEnergyStorage energyStorage;
    protected final ItemStackHandler inventory;

    protected boolean invertloaditem, invertunloaditem;
    protected final ArrayList<ItemFilterEntry> ItemtoLoad = new ArrayList<>();
    protected final ArrayList<ItemFilterEntry> ItemtounLoad = new ArrayList<>();
    protected int ticksExisted=0;
    private int inventoryscanindex = 0;


    protected TIleEntityDroneDockingStation(TileEntityType<?> typeIn) {
        super(typeIn);
        this.energyStorage = new CustomEnergyStorage(25000,2500,2000);
        this.inventory = new ItemStackHandler(18);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("tileentity.dronedockingstation.name");
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return null;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {

        compound.put("energy",this.energyStorage.serializeNBT());
        compound.put("inventory", this.inventory.serializeNBT());
        CompoundNBT compound2 = new CompoundNBT();
        int listsize = this.ItemtoLoad.size();
        for(int i=0; i<listsize;i++){
            compound2.put("filter"+i, this.ItemtoLoad.get(i).serializeNBT());
        }
        compound2.putInt("size", listsize);
        compound2.putBoolean("inverted", this.invertloaditem);
        compound.put("ItemtoLoad", compound2);

        compound2 = new CompoundNBT();
        listsize = this.ItemtounLoad.size();
        for(int i=0; i<listsize;i++){
            compound2.put("filter"+i, this.ItemtounLoad.get(i).serializeNBT());
        }
        compound2.putInt("size", listsize);
        compound2.putBoolean("inverted", this.invertunloaditem);
        compound.put("ItemtounLoad", compound2);
        compound.putInt("inventoryscanindex", this.inventoryscanindex);

        return super.save(compound);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT compound) {

        this.energyStorage.deserializeNBT(compound.getCompound("energy"));
        this.inventory.deserializeNBT(compound.getCompound("inventory"));

        CompoundNBT compound2 = compound.getCompound("ItemtoLoad");
        int filtersize = compound2.getInt("size");
        for(int i=0; i<filtersize; i++){
            this.ItemtoLoad.add(ItemFilterEntry.deserializeNBT(compound2.getCompound("filter"+i)));
        }
        this.invertloaditem=compound2.getBoolean("inverted");

        compound2 = compound.getCompound("ItemtounLoad");
        filtersize = compound2.getInt("size");
        for(int i=0; i<filtersize; i++){
            this.ItemtounLoad.add(ItemFilterEntry.deserializeNBT(compound2.getCompound("filter"+i)));
        }
        this.invertunloaditem=compound2.getBoolean("inverted");
        this.inventoryscanindex = compound.getInt("inventoryscanindex");
        super.load(p_230337_1_, compound);
    }

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

    @Nonnull
    @Override
    public ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = this.inventory.getStackInSlot(index);
        stack.shrink(count);
        this.inventory.setStackInSlot(index, stack);
        return stack;
    }

    @Nonnull
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
    public boolean stillValid(@Nonnull PlayerEntity player) {
        if (Objects.requireNonNull(this.level).getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        for(int i=0;i<this.inventory.getSlots(); i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void tick() {
        World world = this.getLevel();
        if(world == null){
            return;
        }
        this.ticksExisted++;

        boolean loadingcomplete = false;
        boolean chargecomplete = false;

        if(this.ticksExisted%10!=0) {
            return;
        }


        List<EntityLogisticsDrone> Dronelist = world.getEntitiesOfClass(EntityLogisticsDrone.class, new AxisAlignedBB(this.worldPosition.above()));

        if(Dronelist.isEmpty()){
            return;
        }

        EntityLogisticsDrone drone = Dronelist.get(0);
        //chargebattery
        CustomEnergyStorage battery = drone.getBattery();
        int batterytoDrain = Math.min(100, battery.getMaxEnergyStored() - battery.getEnergyStored());
        boolean isDroneBatteryFull = batterytoDrain == 0;
        if (!isDroneBatteryFull) {
            int batteryextract = this.energyStorage.extractEnergy(batterytoDrain, false);
            //Out of Internal Battery
            if (batteryextract == 0) {
                return;
            }
            int dronecharged = battery.receiveEnergy(batteryextract, false);

            if(dronecharged == 0){
                chargecomplete = true;
            }

        }

        //cargo unload
        ItemStackHandler droneInventory = drone.getInventory();
        if(this.inventoryscanindex <9){
            ItemStack stack = droneInventory.getStackInSlot(this.inventoryscanindex);
            if(!stack.isEmpty()) {

                ItemFilterEntry activefilter = null;
                for (ItemFilterEntry entry : this.ItemtounLoad) {
                    boolean cond1 = entry.test(stack);
                    if (this.invertunloaditem) {
                        cond1 = !cond1;
                    }
                    if (cond1) {
                        activefilter = entry;
                        break;
                    }
                }
                if (activefilter != null) {
                    ItemStack stack1 = droneInventory.extractItem(this.inventoryscanindex, activefilter.getMaxCount(), false);
                    ItemStack inserted = ItemStack.EMPTY;
                    for (int i = 0; i < 9; i++) {
                        inserted = this.inventory.insertItem(i, stack1, false);
                        if (inserted.isEmpty()) {
                            break;
                        }
                    }

                    if (!inserted.isEmpty()) {
                        droneInventory.insertItem(this.inventoryscanindex, inserted, false);
                    }
                }
            }
        }else{
            ItemStack stack = this.inventory.getStackInSlot(this.inventoryscanindex);
            if(!stack.isEmpty()) {
                ItemFilterEntry activefilter = null;
                for (ItemFilterEntry entry : this.ItemtoLoad) {
                    boolean cond1 = entry.test(stack);
                    if (this.invertloaditem) {
                        cond1 = !cond1;
                    }
                    if (cond1) {
                        activefilter = entry;
                        break;
                    }
                }
                if (activefilter != null) {
                    ItemStack stack1 = this.inventory.extractItem(this.inventoryscanindex, activefilter.getMaxCount(), false);
                    ItemStack inserted = ItemStack.EMPTY;
                    for (int i = 0; i < 9; i++) {
                        inserted = droneInventory.insertItem(i, stack1, false);
                        if (inserted.isEmpty()) {
                            break;
                        }
                    }

                    if (!inserted.isEmpty()) {
                        this.inventory.insertItem(this.inventoryscanindex, inserted, false);
                    }
                }
            }

        }


        this.inventoryscanindex += 1;
        if (inventoryscanindex == this.inventory.getSlots()) {
            this.inventoryscanindex = 0;
            loadingcomplete = true;
        }

        if(loadingcomplete && chargecomplete){
            Dronelist.get(0).StartMovetoNextDestination();
        }

    }
}
