package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock;
import com.yor42.projectazure.libs.utils.BlockStateUtil;
import com.yor42.projectazure.setup.register.registerFluids;
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
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TileEntityBasicRefinery extends LockableTileEntity implements INamedContainerProvider, ISidedInventory, IRecipeHelperPopulator, ITickableTileEntity {

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
     */


    public ItemStackHandler ITEMHANDLER = new ItemStackHandler(9){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if(slot%2 == 0 && slot<8){
                Item item = stack.getItem();
                return item instanceof BucketItem;
            }
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            ItemStack stack = this.getStackInSlot(slot);
            Item item = stack.getItem();
            switch(slot){
                case 0:{
                    if(item instanceof BucketItem){
                        Consumer<FluidStack> TransferLiquids = (fluidstack)->{
                            FluidTank tank = TileEntityBasicRefinery.this.CrudeOilTank;
                            if(tank.fill(fluidstack, IFluidHandler.FluidAction.SIMULATE)>=fluidstack.getAmount()) {
                                tank.fill(fluidstack, IFluidHandler.FluidAction.EXECUTE);
                            }
                            if(item.hasContainerItem(stack)) {
                                TileEntityBasicRefinery.this.ITEMHANDLER.setStackInSlot(1, item.getContainerItem(stack));
                            }
                            stack.shrink(1);
                        };
                        FluidUtil.getFluidContained(stack).ifPresent(TransferLiquids);
                    }
                    break;
                }
                case 2:{
                    if(item instanceof BucketItem){
                        FluidTank tank = TileEntityBasicRefinery.this.GasolineTank;
                        if(FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, false).isSuccess()){
                            FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, true);
                            TileEntityBasicRefinery.this.ITEMHANDLER.setStackInSlot(3, stack.copy());
                            stack.shrink(1);
                        }
                    }
                    break;
                }
                case 4:{
                    if(item instanceof BucketItem){
                        FluidTank tank = TileEntityBasicRefinery.this.DieselTank;
                        if(FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, false).isSuccess()){
                            FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, true);
                            TileEntityBasicRefinery.this.ITEMHANDLER.setStackInSlot(5, stack.copy());
                            stack.shrink(1);
                        }
                    }
                    break;
                }
                case 6:{
                    if(item instanceof BucketItem){
                        FluidTank tank = TileEntityBasicRefinery.this.FuelOilTank;
                        if(FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, false).isSuccess()){
                            FluidUtil.tryFillContainerAndStow(stack, tank, null, 1000, null, true);
                            TileEntityBasicRefinery.this.ITEMHANDLER.setStackInSlot(7, stack.copy());
                            stack.shrink(1);
                        }
                    }
                    break;
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


    protected TileEntityBasicRefinery(TileEntityType<?> p_i48285_1_) {
        super(p_i48285_1_);
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
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("tileentity.basic_refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
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
    public ItemStack removeStackFromSlot(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {

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

    @Override
    public void tick() {

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inventory", this.ITEMHANDLER.serializeNBT());
        compound.put("crudeoiltank", this.CrudeOilTank.writeToNBT(new CompoundNBT()));
        compound.put("gasolinetank", this.GasolineTank.writeToNBT(new CompoundNBT()));
        compound.put("dieseltank", this.DieselTank.writeToNBT(new CompoundNBT()));
        compound.put("fueloiltank", this.FuelOilTank.writeToNBT(new CompoundNBT()));
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
    }
}
