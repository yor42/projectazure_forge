package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerBasicRefinery;
import com.yor42.projectazure.libs.utils.BlockUtil;
import com.yor42.projectazure.setup.register.RegisterFluids;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
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

public class TileEntityBasicRefinery extends BaseContainerBlockEntity implements MenuProvider, StackedContentsCompatible, BlockEntityTicker<TileEntityBasicRefinery> {


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


    public TileEntityBasicRefinery(BlockPos blockpos, BlockState blockstate) {
        super(registerTE.BASIC_REFINERY.get(), blockpos, blockstate);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("tileentity.basic_refinery.name");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new ContainerBasicRefinery(id, player, this.ITEMHANDLER, this.fields, this.CrudeOilTank.getFluid(), this.GasolineTank.getFluid(), this.DieselTank.getFluid(), this.FuelOilTank.getFluid());
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

    @Override
    public void fillStackedContents(StackedContents recipeItemHelper) {
        for(int i=0;i<this.ITEMHANDLER.getSlots();i++) {
            recipeItemHelper.accountStack(this.ITEMHANDLER.getStackInSlot(i));
        }
    }


    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void tick(Level level, BlockPos blockpos, BlockState blockstate, TileEntityBasicRefinery tileentitybasicrefinery) {
        if(this.getLevel() != null) {
            boolean flag = this.isBurning();
            boolean flag1 = false;
            if (this.isBurning()) {
                --this.burnTime;
            }


            for(int slot = 0; slot<7; slot+=2){
                ItemStack stack = this.getItem(slot);
                if(slot == 0){
                    int finalSlot1 = slot;
                    NonNullConsumer<IFluidHandlerItem> TransferLiquids = (fluidHandler) -> {
                        FluidTank tank = this.CrudeOilTank;

                        ItemStack existingStack = this.ITEMHANDLER.getStackInSlot(1);
                        int existingStacklimit = Math.min(existingStack.getMaxStackSize(), 64);
                        int Itemcount = existingStack.getCount()+fluidHandler.getContainer().getCount();
                        if(existingStack.isEmpty() || existingStacklimit>=Itemcount) {
                            FluidActionResult result = FluidUtil.tryEmptyContainer(stack, tank, tank.getCapacity(), null, true);
                            if (result.isSuccess()) {
                                if(existingStack.isEmpty()){
                                    this.ITEMHANDLER.setStackInSlot(1, result.getResult());
                                }else{
                                    this.ITEMHANDLER.getStackInSlot(1).grow(result.getResult().getCount());
                                }
                                stack.shrink(1);
                                this.ITEMHANDLER.setStackInSlot(finalSlot1, stack);
                            }
                        }
                    };
                    FluidUtil.getFluidHandler(stack).ifPresent(TransferLiquids);
                }
                else {
                    FluidTank tank;
                    switch (slot) {
                        default: {
                            tank = TileEntityBasicRefinery.this.GasolineTank;
                            break;
                        }
                        case 4: {
                            tank = TileEntityBasicRefinery.this.DieselTank;
                            break;
                        }
                        case 6: {
                            tank = TileEntityBasicRefinery.this.FuelOilTank;
                            break;
                        }
                    }
                    int finalSlot = slot;
                    NonNullConsumer<IFluidHandlerItem> TransferLiquidtoStack = (FluidHandler)->{
                        FluidActionResult simresult = FluidUtil.tryFillContainer(stack, tank, tank.getCapacity(), null, false);
                        if(simresult.isSuccess() && this.ITEMHANDLER.insertItem(finalSlot+1, simresult.getResult(), true).isEmpty()) {
                            FluidActionResult result = FluidUtil.tryFillContainer(stack, tank, tank.getCapacity(), null, true);
                            stack.shrink(1);
                            this.ITEMHANDLER.insertItem(finalSlot+1, result.getResult(), false);
                            this.ITEMHANDLER.setStackInSlot(finalSlot, stack);
                        }
                    };
                    FluidUtil.getFluidHandler(stack).ifPresent(TransferLiquidtoStack);
                }
            }

            if (!this.getLevel().isClientSide) {
                ItemStack itemstack = this.ITEMHANDLER.getStackInSlot(9);
                if(this.isBurning() || this.CrudeOilTank.getFluidAmount()>100 && !itemstack.isEmpty()){
                    if (!this.isBurning() && this.CrudeOilTank.getFluidAmount()>100) {
                        this.burnTime = ForgeHooks.getBurnTime(itemstack, RecipeType.SMELTING);
                        this.totalBurntime = this.burnTime;
                        if (this.isBurning()) {
                            flag1 = true;
                            if (itemstack.hasContainerItem())
                                this.ITEMHANDLER.setStackInSlot(9, itemstack.getContainerItem());
                            else if (!itemstack.isEmpty()) {
                                itemstack.shrink(1);
                                if (itemstack.isEmpty()) {
                                    this.ITEMHANDLER.setStackInSlot(1, itemstack.getContainerItem());
                                }
                            }
                        }
                    }

                    if (this.isBurning() && this.CrudeOilTank.getFluidAmount()>100 && (this.ITEMHANDLER.getStackInSlot(8).isEmpty() || this.ITEMHANDLER.getStackInSlot(8).getCount()<64)) {
                        //Do process
                        this.bitumentick++;
                        this.isActive = 1;
                        FluidStack stack = this.CrudeOilTank.drain(10, IFluidHandler.FluidAction.SIMULATE);
                        int drainedAmount = stack.getAmount();
                        int FuelOilAmount = (int) (drainedAmount*0.3);
                        int GasolineAmount = (int) (drainedAmount*0.46);
                        int DieselAmount = (int) (drainedAmount*0.3);
                        int DieselFill = this.DieselTank.fill(new FluidStack(RegisterFluids.DIESEL_SOURCE_REGISTRY.get(), DieselAmount), IFluidHandler.FluidAction.EXECUTE);
                        int GasolineFill = this.GasolineTank.fill(new FluidStack(RegisterFluids.GASOLINE_SOURCE_REGISTRY.get(), GasolineAmount), IFluidHandler.FluidAction.EXECUTE);
                        int FuelFill = this.FuelOilTank.fill(new FluidStack(RegisterFluids.FUEL_OIL_SOURCE_REGISTRY.get(), FuelOilAmount), IFluidHandler.FluidAction.EXECUTE);
                        int MaxFill = Math.max(Math.max(DieselFill, GasolineFill), FuelFill);
                        this.CrudeOilTank.drain(MaxFill == 0? 0: 10, IFluidHandler.FluidAction.EXECUTE);
                        if(this.bitumentick>=600){
                            if(this.ITEMHANDLER.getStackInSlot(8).isEmpty()){
                                this.ITEMHANDLER.setStackInSlot(8, new ItemStack(RegisterItems.BITUMEN.get()));
                            }
                            else {
                                this.ITEMHANDLER.getStackInSlot(8).grow(1);
                            }
                            this.bitumentick = 0;
                        }
                    }
                    else {
                        this.isActive = 0;
                    }
                }

                if (flag != this.isBurning() && this.getLevel().getBlockState(this.getBlockPos()).hasProperty(ACTIVE)) {
                    flag1 = true;
                    this.getLevel().setBlock(this.worldPosition, this.getLevel().getBlockState(this.worldPosition).setValue(ACTIVE, this.isBurning()), 3);
                }

            }

            if (flag1) {
                this.setChanged();
            }

        }
    }

    @Override
    public void saveAdditional(CompoundTag compoundtag) {
        super.saveAdditional(compoundtag);
        compoundtag.put("inventory", this.ITEMHANDLER.serializeNBT());
        compoundtag.put("crudeoiltank", this.CrudeOilTank.writeToNBT(new CompoundTag()));
        compoundtag.put("gasolinetank", this.GasolineTank.writeToNBT(new CompoundTag()));
        compoundtag.put("dieseltank", this.DieselTank.writeToNBT(new CompoundTag()));
        compoundtag.put("fueloiltank", this.FuelOilTank.writeToNBT(new CompoundTag()));
        compoundtag.putInt("bitumentick", this.bitumentick);
        compoundtag.putInt("totalburntime", this.totalBurntime);
        compoundtag.putInt("burntime", this.burnTime);
        compoundtag.putInt("activestate", this.isActive);
    }

    @Override
    public void load(CompoundTag compoundtag) {
        super.load(compoundtag);
        this.ITEMHANDLER.deserializeNBT(compoundtag.getCompound("inventory"));
        this.CrudeOilTank.readFromNBT(compoundtag.getCompound("crudeoiltank"));
        this.GasolineTank.readFromNBT(compoundtag.getCompound("gasolinetank"));
        this.DieselTank.readFromNBT(compoundtag.getCompound("dieseltank"));
        this.FuelOilTank.readFromNBT(compoundtag.getCompound("fueloiltank"));
        this.bitumentick = compoundtag.getInt("bitumentick");
        this.totalBurntime = compoundtag.getInt("totalburntime");
        this.burnTime = compoundtag.getInt("burntime");
        this.isActive = compoundtag.getInt("activestate");
    }

    public void encodeExtraData(FriendlyByteBuf buffer) {
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
            switch (BlockUtil.getRelativeDirection(direction, this.getBlockState().getValue(AbstractMachineBlock.FACING))){
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
}
