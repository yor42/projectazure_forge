package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerBasicRefinery;
import com.yor42.projectazure.libs.utils.BlockStateUtil;
import com.yor42.projectazure.setup.register.registerFluids;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock.ACTIVE;
import static com.yor42.projectazure.libs.utils.FluidPredicates.*;

public class TileEntityBasicRefinery extends BlockEntity implements MenuProvider, Container {


    private int burnTime = 0;
    private int totalBurntime = 0;
    private int bitumentick;
    private int isActive = 0;

    public FluidTank CrudeOilTank = new FluidTank(4000, CrudeOilPredicate);
    public FluidTank GasolineTank = new FluidTank(2000,GasolinePredicate);
    public FluidTank DieselTank = new FluidTank(2000,DieselPredicate);
    public FluidTank FuelOilTank = new FluidTank(2000,FuelOilPredicate);

    private final int[] FieldArray = {this.burnTime,this.totalBurntime,this.CrudeOilTank.getFluidAmount(), this.CrudeOilTank.getCapacity(),this.GasolineTank.getFluidAmount(), this.GasolineTank.getCapacity(),this.DieselTank.getFluidAmount(),this.DieselTank.getCapacity(),this.FuelOilTank.getFluidAmount(),this.FuelOilTank.getCapacity(), this.isActive};

    private final ContainerData fields = new ContainerData() {
        @Override
        public int get(int index) {
            switch(index){
                default: return TileEntityBasicRefinery.this.burnTime;
                case 1: return TileEntityBasicRefinery.this.totalBurntime;
                case 2: return TileEntityBasicRefinery.this.CrudeOilTank.getFluidAmount();
                case 3: return TileEntityBasicRefinery.this.CrudeOilTank.getCapacity();
                case 4: return TileEntityBasicRefinery.this.GasolineTank.getFluidAmount();
                case 5: return TileEntityBasicRefinery.this.GasolineTank.getCapacity();
                case 6: return TileEntityBasicRefinery.this.DieselTank.getFluidAmount();
                case 7: return TileEntityBasicRefinery.this.DieselTank.getCapacity();
                case 8: return TileEntityBasicRefinery.this.FuelOilTank.getFluidAmount();
                case 9: return TileEntityBasicRefinery.this.FuelOilTank.getCapacity();
                case 10: return TileEntityBasicRefinery.this.isActive;
            }
        }

        @Override
        public void set(int index, int value) {
            if (index == 1) {
                TileEntityBasicRefinery.this.totalBurntime = value;
            } else if(index == 0){
                TileEntityBasicRefinery.this.burnTime = value;
            }
        }

        @Override
        public int getCount() {
            return 11;
        }
    };

    /*
    Index:
    0: Crude Oil in
    1: Crude Oil out
    2: Gasoline Bucket in
    3: Gasoline Bucket out
    4: Diesel Bucket in
    5: Diesel Bucket out
    6: Fuel Oil Bucket in
    7: Fuel Oil Bucket out
    8: Bitumen out
    9: Fuel
     */


    public ItemStackHandler ITEMHANDLER = new ItemStackHandler(10){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if(slot<8){
                if(slot == 0) {
                    FluidTank tank = TileEntityBasicRefinery.this.CrudeOilTank;
                    return FluidUtil.getFluidContained(stack).isPresent() && tank.isFluidValid(FluidUtil.getFluidContained(stack).get());
                }
                else{
                    return FluidUtil.getFluidHandler(stack).isPresent();
                }
            }
            else if(slot == 9){
                return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING)>0;
            }
            return false;
        }
    };

    public TileEntityBasicRefinery(BlockPos p_153215_, BlockState p_153216_) {
        super(registerTE.BASIC_REFINERY.get(), p_153215_, p_153216_);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("tileentity.basic_refinery");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new ContainerBasicRefinery(p_39954_, p_39955_, this.ITEMHANDLER, this.fields, this.CrudeOilTank.getFluid(), this.GasolineTank.getFluid(), this.DieselTank.getFluid(), this.FuelOilTank.getFluid());
    }

    @Override
    public int getContainerSize() {
        return this.ITEMHANDLER.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i=0;i<this.ITEMHANDLER.getSlots();i++){
            if(!this.ITEMHANDLER.getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return this.ITEMHANDLER.getStackInSlot(i);
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        this.ITEMHANDLER.getStackInSlot(i).shrink(i1);
        return this.ITEMHANDLER.getStackInSlot(i);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if(index >= 0 && index < this.ITEMHANDLER.getSlots()){
            this.ITEMHANDLER.setStackInSlot(index, ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.ITEMHANDLER.setStackInSlot(i, itemStack);
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        return true;
    }

    @Override
    public void clearContent() {
        for(int i=0;i<this.ITEMHANDLER.getSlots(); i++){
            this.ITEMHANDLER.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("inventory", this.ITEMHANDLER.serializeNBT());
        compound.put("crudeoiltank", this.CrudeOilTank.writeToNBT(new CompoundTag()));
        compound.put("gasolinetank", this.GasolineTank.writeToNBT(new CompoundTag()));
        compound.put("dieseltank", this.DieselTank.writeToNBT(new CompoundTag()));
        compound.put("fueloiltank", this.FuelOilTank.writeToNBT(new CompoundTag()));
        compound.putInt("bitumentick", this.bitumentick);
        compound.putInt("totalburntime", this.totalBurntime);
        compound.putInt("burntime", this.burnTime);
        compound.putInt("activestate", this.isActive);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.ITEMHANDLER.deserializeNBT(compound.getCompound("inventory"));
        this.CrudeOilTank.readFromNBT(compound.getCompound("crudeoiltank"));
        this.GasolineTank.readFromNBT(compound.getCompound("gasolinetank"));
        this.DieselTank.readFromNBT(compound.getCompound("dieseltank"));
        this.FuelOilTank.readFromNBT(compound.getCompound("fueloiltank"));
        this.bitumentick = compound.getInt("bitumentick");
        this.totalBurntime = compound.getInt("totalburntime");
        this.burnTime = compound.getInt("burntime");
        this.isActive = compound.getInt("activestate");
    }

    public void encodeExtraData(FriendlyByteBuf  buffer) {
        buffer.writeVarIntArray(this.FieldArray);
        buffer.writeFluidStack(this.CrudeOilTank.getFluid());
        buffer.writeFluidStack(this.GasolineTank.getFluid());
        buffer.writeFluidStack(this.DieselTank.getFluid());
        buffer.writeFluidStack(this.FuelOilTank.getFluid());
    }

    private final LazyOptional<FluidTank> crudeTankCap = LazyOptional.of(()->this.CrudeOilTank);
    private final LazyOptional<FluidTank> gasolineTankCap = LazyOptional.of(()->this.GasolineTank);
    private final LazyOptional<FluidTank> dieselTankCap = LazyOptional.of(()->this.DieselTank);
    private final LazyOptional<FluidTank> fueloilTankCap = LazyOptional.of(()->this.FuelOilTank);
    private final LazyOptional<ItemStackHandler> INVENTORY = LazyOptional.of(()->this.ITEMHANDLER);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction direction) {

        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.INVENTORY.cast();
        }
        else if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            if(this.getBlockState().hasProperty(AbstractMachineBlock.FACING))
            switch (BlockStateUtil.getRelativeDirection(direction, this.getLevel().getBlockState(this.getBlockPos()).getValue(AbstractMachineBlock.FACING))){
                case LEFT:
                    return this.crudeTankCap.cast();
                case FRONT:
                    return this.gasolineTankCap.cast();
                case BACK:
                    return this.dieselTankCap.cast();
                case RIGHT:
                    return this.fueloilTankCap.cast();
            }
        }
        return super.getCapability(capability, direction);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof TileEntityBasicRefinery){
            TileEntityBasicRefinery machine = (TileEntityBasicRefinery) t;
            boolean flag = machine.isBurning();
            boolean flag1 = false;
            if (machine.isBurning()) {
                --machine.burnTime;
            }


            for (int slot = 0; slot < 7; slot += 2) {
                ItemStack stack = machine.getItem(slot);
                if (slot == 0) {
                    int finalSlot1 = slot;
                    NonNullConsumer<IFluidHandlerItem> TransferLiquids = (fluidHandler) -> {
                        FluidTank tank = machine.CrudeOilTank;

                        ItemStack existingStack = machine.ITEMHANDLER.getStackInSlot(1);
                        int existingStacklimit = Math.min(existingStack.getMaxStackSize(), 64);
                        int Itemcount = existingStack.getCount() + fluidHandler.getContainer().getCount();
                        if (existingStack.isEmpty() || existingStacklimit >= Itemcount) {
                            FluidActionResult result = FluidUtil.tryEmptyContainer(stack, tank, tank.getCapacity(), null, true);
                            if (result.isSuccess()) {
                                if (existingStack.isEmpty()) {
                                    machine.ITEMHANDLER.setStackInSlot(1, result.getResult());
                                } else {
                                    machine.ITEMHANDLER.getStackInSlot(1).grow(result.getResult().getCount());
                                }
                                stack.shrink(1);
                                machine.ITEMHANDLER.setStackInSlot(finalSlot1, stack);
                            }
                        }
                    };
                    FluidUtil.getFluidHandler(stack).ifPresent(TransferLiquids);
                } else {
                    FluidTank tank;
                    switch (slot) {
                        default: {
                            tank = machine.GasolineTank;
                            break;
                        }
                        case 4: {
                            tank = machine.DieselTank;
                            break;
                        }
                        case 6: {
                            tank = machine.FuelOilTank;
                            break;
                        }
                    }
                    int finalSlot = slot;
                    NonNullConsumer<IFluidHandlerItem> TransferLiquidtoStack = (FluidHandler) -> {
                        ItemStack existingStack = machine.ITEMHANDLER.getStackInSlot(finalSlot + 1);
                        int existingStacklimit = Math.min(existingStack.getMaxStackSize(), 64);
                        int Itemcount = existingStack.getCount() + FluidHandler.getContainer().getCount();
                        if (existingStack.isEmpty() || Itemcount <= existingStacklimit) {
                            FluidActionResult result = FluidUtil.tryFillContainer(stack, tank, tank.getCapacity(), null, true);
                            if (result.isSuccess()) {
                                stack.shrink(1);
                                if (existingStack.isEmpty()) {
                                    machine.ITEMHANDLER.setStackInSlot(finalSlot + 1, result.getResult());
                                } else {
                                    machine.ITEMHANDLER.getStackInSlot(finalSlot + 1).grow(result.getResult().getCount());
                                }
                                machine.ITEMHANDLER.setStackInSlot(finalSlot, stack);
                            }
                        }
                    };
                    FluidUtil.getFluidHandler(stack).ifPresent(TransferLiquidtoStack);
                }
            }

            if (!machine.getLevel().isClientSide) {
                ItemStack itemstack = machine.ITEMHANDLER.getStackInSlot(9);
                if (machine.isBurning() || machine.CrudeOilTank.getFluidAmount() > 100 && !itemstack.isEmpty()) {
                    if (!machine.isBurning() && machine.CrudeOilTank.getFluidAmount() > 100) {
                        machine.burnTime = ForgeHooks.getBurnTime(itemstack, RecipeType.SMELTING);
                        machine.totalBurntime = machine.burnTime;
                        if (machine.isBurning()) {
                            flag1 = true;
                            if (itemstack.hasContainerItem())
                                machine.ITEMHANDLER.setStackInSlot(9, itemstack.getContainerItem());
                            else if (!itemstack.isEmpty()) {
                                itemstack.shrink(1);
                                if (itemstack.isEmpty()) {
                                    machine.ITEMHANDLER.setStackInSlot(1, itemstack.getContainerItem());
                                }
                            }
                        }
                    }

                    if (machine.isBurning() && machine.CrudeOilTank.getFluidAmount() > 100 && (machine.ITEMHANDLER.getStackInSlot(8).isEmpty() || machine.ITEMHANDLER.getStackInSlot(8).getCount() < 64)) {
                        //Do process
                        machine.bitumentick++;
                        machine.isActive = 1;
                        FluidStack stack = machine.CrudeOilTank.drain(10, IFluidHandler.FluidAction.SIMULATE);
                        int drainedAmount = stack.getAmount();
                        int FuelOilAmount = (int) (drainedAmount * 0.3);
                        int GasolineAmount = (int) (drainedAmount * 0.46);
                        int DieselAmount = (int) (drainedAmount * 0.3);
                        int DieselFill = machine.DieselTank.fill(new FluidStack(registerFluids.DIESEL_SOURCE, DieselAmount), IFluidHandler.FluidAction.EXECUTE);
                        int GasolineFill = machine.GasolineTank.fill(new FluidStack(registerFluids.GASOLINE_SOURCE, GasolineAmount), IFluidHandler.FluidAction.EXECUTE);
                        int FuelFill = machine.FuelOilTank.fill(new FluidStack(registerFluids.FUEL_OIL_SOURCE, FuelOilAmount), IFluidHandler.FluidAction.EXECUTE);
                        int MaxFill = Math.max(Math.max(DieselFill, GasolineFill), FuelFill);
                        machine.CrudeOilTank.drain(MaxFill == 0 ? 0 : 10, IFluidHandler.FluidAction.EXECUTE);
                        if (machine.bitumentick >= 600) {
                            if (machine.ITEMHANDLER.getStackInSlot(8).isEmpty()) {
                                machine.ITEMHANDLER.setStackInSlot(8, new ItemStack(registerItems.BITUMEN.get()));
                            } else {
                                machine.ITEMHANDLER.getStackInSlot(8).grow(1);
                            }
                            machine.bitumentick = 0;
                        }
                    } else {
                        machine.isActive = 0;
                    }
                }

                if (flag != machine.isBurning() && machine.getLevel().getBlockState(machine.getBlockPos()).hasProperty(ACTIVE)) {
                    flag1 = true;
                    machine.getLevel().setBlock(machine.worldPosition, machine.getLevel().getBlockState(machine.worldPosition).setValue(ACTIVE, machine.isBurning()), 3);
                }

            }

            if (flag1) {
                machine.setChanged();
            }
        }
    }
}
