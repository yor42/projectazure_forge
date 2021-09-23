package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerBasicRefinery;
import com.yor42.projectazure.libs.utils.BlockStateUtil;
import com.yor42.projectazure.setup.register.registerFluids;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock.ACTIVE;

public class TileEntityBasicRefinery extends LockableTileEntity implements INamedContainerProvider, ISidedInventory, IRecipeHelperPopulator, ITickableTileEntity {


    private int burnTime;
    private int totalBurntime = 0;
    private int bitumentick;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TileEntityBasicRefinery.this.burnTime;
                case 1:
                    return TileEntityBasicRefinery.this.totalBurntime;
                case 2:
                    return TileEntityBasicRefinery.this.CrudeOilTank.getFluidAmount();
                case 3:
                    return TileEntityBasicRefinery.this.CrudeOilTank.getCapacity();
                case 4:
                    return TileEntityBasicRefinery.this.GasolineTank.getFluidAmount();
                case 5:
                    return TileEntityBasicRefinery.this.GasolineTank.getCapacity();
                case 6:
                    return TileEntityBasicRefinery.this.DieselTank.getFluidAmount();
                case 7:
                    return TileEntityBasicRefinery.this.DieselTank.getCapacity();
                case 8:
                    return TileEntityBasicRefinery.this.FuelOilTank.getFluidAmount();
                case 9:
                    return TileEntityBasicRefinery.this.FuelOilTank.getCapacity();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    TileEntityBasicRefinery.this.burnTime = value;
                    break;
                case 1:
                    TileEntityBasicRefinery.this.totalBurntime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 10;
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
            if(slot%2 == 0 && slot<8){
                FluidTank tank = TileEntityBasicRefinery.this.CrudeOilTank;
                switch (slot){
                    case 2:
                        tank = TileEntityBasicRefinery.this.GasolineTank;
                        break;
                    case 4:
                        tank = TileEntityBasicRefinery.this.DieselTank;
                        break;
                    case 6:
                        tank = TileEntityBasicRefinery.this.FuelOilTank;
                        break;
                }
                return FluidUtil.getFluidContained(stack).isPresent() && tank.isFluidValid(FluidUtil.getFluidContained(stack).get());

            }
            else if(slot == 9){
                return ForgeHooks.getBurnTime(stack, IRecipeType.SMELTING)>0;
            }
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            ItemStack stack = this.getStackInSlot(slot);
            Item item = stack.getItem();
            if(FluidUtil.getFluidContained(stack).isPresent()) {
                switch (slot) {
                    case 0: {
                        Consumer<FluidStack> TransferLiquids = (fluidstack) -> {
                            FluidTank tank = TileEntityBasicRefinery.this.CrudeOilTank;
                            if (tank.fill(fluidstack, IFluidHandler.FluidAction.SIMULATE) >= fluidstack.getAmount()) {
                                tank.fill(fluidstack, IFluidHandler.FluidAction.EXECUTE);
                            }
                            if (item.hasContainerItem(stack)) {
                                TileEntityBasicRefinery.this.ITEMHANDLER.setStackInSlot(1, item.getContainerItem(stack));
                            }
                            stack.shrink(1);
                        };
                        FluidUtil.getFluidContained(stack).ifPresent(TransferLiquids);
                        break;
                    }
                    case 2: {
                        FluidTank tank = TileEntityBasicRefinery.this.GasolineTank;
                        if (FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, false).isSuccess()) {
                            FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, true);
                            TileEntityBasicRefinery.this.ITEMHANDLER.setStackInSlot(3, stack.copy());
                            stack.shrink(1);
                        }
                        break;
                    }
                    case 4: {
                        FluidTank tank = TileEntityBasicRefinery.this.DieselTank;
                        if (FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, false).isSuccess()) {
                            FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, true);
                            TileEntityBasicRefinery.this.ITEMHANDLER.setStackInSlot(5, stack.copy());
                            stack.shrink(1);
                        }
                        break;
                    }
                    case 6: {
                        FluidTank tank = TileEntityBasicRefinery.this.FuelOilTank;
                        if (FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, false).isSuccess()) {
                            FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, true);
                            TileEntityBasicRefinery.this.ITEMHANDLER.setStackInSlot(7, stack.copy());
                            stack.shrink(1);
                        }
                        break;
                    }
                }
            }
        }
    };

    private final Predicate<FluidStack> CrudeOilPredicate = (fluidStack)-> fluidStack.getFluid() == registerFluids.CRUDE_OIL_SOURCE;
    private final Predicate<FluidStack> GasolinePredicate = (fluidStack)-> fluidStack.getFluid() == registerFluids.GASOLINE_SOURCE;
    private final Predicate<FluidStack> DieselPredicate = (fluidStack)-> fluidStack.getFluid() == registerFluids.DIESEL_SOURCE;
    private final Predicate<FluidStack> FuelOilPredicate = (fluidStack)-> fluidStack.getFluid() == registerFluids.FUEL_OIL_SOURCE;

    public FluidTank CrudeOilTank = new FluidTank(2000,this.CrudeOilPredicate);
    public FluidTank GasolineTank = new FluidTank(1000,this.GasolinePredicate);
    public FluidTank DieselTank = new FluidTank(1000,this.DieselPredicate);
    public FluidTank FuelOilTank = new FluidTank(1000,this.FuelOilPredicate);


    public TileEntityBasicRefinery() {
        super(registerTE.BASIC_REFINERY.get());
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if(this.getWorld().getBlockState(this.getPos()).hasProperty(AbstractMachineBlock.FACING)) {
            Direction blockfacing = this.getWorld().getBlockState(this.getPos()).get(AbstractMachineBlock.FACING);
            BlockStateUtil.RelativeDirection relativeDirection = BlockStateUtil.getRelativeDirection(direction, blockfacing);
            switch (relativeDirection) {
                case LEFT: {
                    return new int[]{0};
                }
                case UP: {
                    return new int[]{2,4,6};
                }
                case RIGHT: {
                    return new int[]{8};
                }
            }
        }
        return new int[] {1,3,5,7};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, @Nullable Direction direction) {
        return this.ITEMHANDLER.isItemValid(i, itemStack);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("tileentity.basic_refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ContainerBasicRefinery(id, player, this.ITEMHANDLER, this.fields);
    }

    @Override
    public int getSizeInventory() {
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
    public ItemStack getStackInSlot(int i) {
        return this.ITEMHANDLER.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        this.ITEMHANDLER.getStackInSlot(i).shrink(i1);
        return this.ITEMHANDLER.getStackInSlot(i);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if(index >= 0 && index < this.ITEMHANDLER.getSlots()){
            this.ITEMHANDLER.setStackInSlot(index, ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        this.ITEMHANDLER.setStackInSlot(i, itemStack);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public void clear() {
        for(int i=0;i<this.ITEMHANDLER.getSlots(); i++){
            this.ITEMHANDLER.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void fillStackedContents(RecipeItemHelper recipeItemHelper) {
        for(int i=0;i<this.ITEMHANDLER.getSlots();i++) {
            recipeItemHelper.accountStack(this.ITEMHANDLER.getStackInSlot(i));
        }
    }


    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void tick() {
        if(this.getWorld() != null) {
            boolean flag = this.isBurning();
            boolean flag1 = false;
            if (this.isBurning()) {
                --this.burnTime;
            }

            if (!this.getWorld().isRemote) {
                ItemStack itemstack = this.ITEMHANDLER.getStackInSlot(9);
                if(this.isBurning() || this.CrudeOilTank.getFluidAmount()>100 && !itemstack.isEmpty()){
                    if (!this.isBurning() && this.CrudeOilTank.getFluidAmount()>100) {
                        this.burnTime = ForgeHooks.getBurnTime(itemstack, IRecipeType.SMELTING);
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
                        FluidStack stack = this.CrudeOilTank.drain(10, IFluidHandler.FluidAction.EXECUTE);
                        int drainedAmount = stack.getAmount();
                        int FuelOilAmount = (int) (drainedAmount*0.3);
                        int GasolineAmount = (int) (drainedAmount*0.46);
                        int DieselAmount = (int) (drainedAmount*0.3);
                        this.DieselTank.fill(new FluidStack(registerFluids.DIESEL_SOURCE, DieselAmount), IFluidHandler.FluidAction.EXECUTE);
                        this.GasolineTank.fill(new FluidStack(registerFluids.GASOLINE_SOURCE, GasolineAmount), IFluidHandler.FluidAction.EXECUTE);
                        this.FuelOilTank.fill(new FluidStack(registerFluids.FUEL_OIL_SOURCE, FuelOilAmount), IFluidHandler.FluidAction.EXECUTE);
                        if(this.bitumentick%600==0){
                            if(this.ITEMHANDLER.getStackInSlot(8).isEmpty()){
                                this.ITEMHANDLER.setStackInSlot(8, new ItemStack(registerItems.BITUMEN.get()));
                            }
                            else {
                                this.ITEMHANDLER.getStackInSlot(8).grow(1);
                            }
                        }
                    }
                }

                if (flag != this.isBurning() && this.getWorld().getBlockState(this.getPos()).hasProperty(ACTIVE)) {
                    flag1 = true;
                    this.getWorld().setBlockState(this.pos, this.getWorld().getBlockState(this.pos).with(ACTIVE, this.isBurning()), 3);
                }

            }

            if (flag1) {
                this.markDirty();
            }

        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inventory", this.ITEMHANDLER.serializeNBT());
        compound.put("crudeoiltank", this.CrudeOilTank.writeToNBT(new CompoundNBT()));
        compound.put("gasolinetank", this.GasolineTank.writeToNBT(new CompoundNBT()));
        compound.put("dieseltank", this.DieselTank.writeToNBT(new CompoundNBT()));
        compound.put("fueloiltank", this.FuelOilTank.writeToNBT(new CompoundNBT()));
        compound.putInt("bitumentick", this.bitumentick);
        compound.putInt("totalburntime", this.totalBurntime);
        compound.putInt("burntime", this.burnTime);
        return compound;
    }

    @Override
    public void read(BlockState p_230337_1_, CompoundNBT compound) {
        super.read(p_230337_1_, compound);
        this.ITEMHANDLER.deserializeNBT(compound.getCompound("inventory"));
        this.CrudeOilTank.readFromNBT(compound.getCompound("crudeoiltank"));
        this.GasolineTank.readFromNBT(compound.getCompound("gasolinetank"));
        this.DieselTank.readFromNBT(compound.getCompound("dieseltank"));
        this.FuelOilTank.readFromNBT(compound.getCompound("fueloiltank"));
        this.bitumentick = compound.getInt("bitumentick");
        this.totalBurntime = compound.getInt("totalburntime");
        this.burnTime = compound.getInt("burntime");
    }

    public void encodeExtraData(PacketBuffer buffer) {
        int[] var = {this.burnTime,this.totalBurntime,this.CrudeOilTank.getFluidAmount(),this.CrudeOilTank.getCapacity(),this.GasolineTank.getFluidAmount(),
                this.GasolineTank.getCapacity(),this.DieselTank.getFluidAmount(),this.DieselTank.getCapacity(),this.FuelOilTank.getFluidAmount(),this.FuelOilTank.getCapacity()};
        buffer.writeVarIntArray(var);
    }
}
