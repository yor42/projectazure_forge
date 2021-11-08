package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.containers.machine.ContainerCrystalGrowthChamber;
import com.yor42.projectazure.gameobject.crafting.CrystalizingRecipe;
import com.yor42.projectazure.setup.register.registerFluids;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import static com.yor42.projectazure.setup.register.registerTE.CRYSTAL_GROWTH_CHAMBER;

public class TileEntityCrystalGrowthChamber extends LockableTileEntity implements INamedContainerProvider, IRecipeHelperPopulator, IInventory, ITickableTileEntity {

    public FluidTank waterTank = new FluidTank(4000, (fluidStack)->fluidStack.getFluid() == Fluids.WATER);
    public FluidTank SolutionTank = new FluidTank(4000);
    /*
    0: Seed in
    1, 2, 3: Dust in for solution
    4: output
    5: bucket in
    6: bucket out
     */
    public ItemStackHandler inventory = new ItemStackHandler(7){
        @Override
        protected void onContentsChanged(int slot) {
            if(slot == 0){
                TileEntityCrystalGrowthChamber.this.MaxGrowthProgress = TileEntityCrystalGrowthChamber.this.getGrowthTime();
            }
        }
    };
   private int growthProgress = 0;
    private int MaxGrowthProgress;
    private IRecipe<? extends IInventory> currentRecipe;
    protected final IRecipeType<? extends CrystalizingRecipe> recipeType;

    private final int[] FieldArray = {this.growthProgress,this.MaxGrowthProgress,this.waterTank.getFluidAmount(), this.waterTank.getCapacity(),this.SolutionTank.getFluidAmount(), this.SolutionTank.getCapacity()};

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index){
                default: return TileEntityCrystalGrowthChamber.this.growthProgress;
                case 1: return TileEntityCrystalGrowthChamber.this.MaxGrowthProgress;
                case 2: return TileEntityCrystalGrowthChamber.this.waterTank.getFluidAmount();
                case 3: return TileEntityCrystalGrowthChamber.this.waterTank.getCapacity();
                case 4: return TileEntityCrystalGrowthChamber.this.SolutionTank.getFluidAmount();
                case 5: return TileEntityCrystalGrowthChamber.this.SolutionTank.getCapacity();
            }
        }

        @Override
        public void set(int index, int value) {
            if (index == 1) {
                TileEntityCrystalGrowthChamber.this.MaxGrowthProgress = value;
            } else if(index == 0){
                TileEntityCrystalGrowthChamber.this.growthProgress = value;
            }
        }

        @Override
        public int size() {
            return 6;
        }
    };

    public TileEntityCrystalGrowthChamber() {
        super(CRYSTAL_GROWTH_CHAMBER.get());
        this.recipeType = registerRecipes.Types.CRYSTALIZING;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.projectazure.crystal_chamber");
    }

    //TODO
    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ContainerCrystalGrowthChamber(id, player, this.inventory, this.fields, this.getWaterTank().getFluid(), this.getSolutionTank().getFluid());
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i=0;i<this.inventory.getSlots();i++){
            if(!this.inventory.getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    public FluidTank getSolutionTank() {
        return this.SolutionTank;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        this.inventory.getStackInSlot(i).shrink(i1);
        return this.inventory.getStackInSlot(i);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if(index >= 0 && index < this.inventory.getSlots()){
            this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack);
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

    @Override
    public void fillStackedContents(RecipeItemHelper helper) {
        for(int i=0;i<this.inventory.getSlots();i++) {
            helper.accountStack(this.inventory.getStackInSlot(i));
        }
    }

    @Override
    public void tick() {
        boolean shouldsave = false;
        if(!this.world.isRemote()){
            for(int i = 1; i<4; i++){
                if(this.tryFillingSolutionTank(i)){
                    break;
                }
            }

            ItemStack stack = this.getStackInSlot(5);
            Consumer<FluidStack> TransferLiquids = (fluidstack) -> {
                FluidTank tank = this.waterTank;
                FluidActionResult actionresult = FluidUtil.tryEmptyContainer(stack, tank, tank.getCapacity(), null, true);
                if (actionresult.isSuccess()) {
                    ItemStack result = actionresult.getResult();
                    if (TileEntityCrystalGrowthChamber.this.inventory.insertItem(6, result, true).isEmpty()) {
                        if (TileEntityCrystalGrowthChamber.this.inventory.getStackInSlot(6).isEmpty()){
                            TileEntityCrystalGrowthChamber.this.inventory.setStackInSlot(6, result);
                        }
                        else {
                            TileEntityCrystalGrowthChamber.this.inventory.insertItem(6, result, false);
                        }
                        stack.shrink(1);
                        TileEntityCrystalGrowthChamber.this.inventory.setStackInSlot(5, stack);
                    }

                }
            };
            FluidUtil.getFluidContained(stack).ifPresent(TransferLiquids);

            ItemStack SeedStack = this.inventory.getStackInSlot(0);
            if(!this.SolutionTank.isEmpty()) {
                if (!SeedStack.isEmpty()) {
                    IRecipe<? extends IInventory> recipe = this.world.getRecipeManager().getRecipe((IRecipeType<? extends CrystalizingRecipe>) this.recipeType, this, this.world).orElse(null);
                    if (this.isProcessable(recipe)) {
                        if (this.currentRecipe == null) {
                            this.currentRecipe = recipe;
                        }
                        if (this.MaxGrowthProgress == 0) {
                            this.MaxGrowthProgress = this.getGrowthTime();
                        }

                        if (!this.isProcessing()) {
                            shouldsave = true;
                            this.growthProgress++;
                            this.MaxGrowthProgress = this.getGrowthTime();
                        } else {
                            this.growthProgress++;
                            if (this.MaxGrowthProgress != 0 && this.growthProgress == this.MaxGrowthProgress) {
                                this.growthProgress = 0;
                                this.MaxGrowthProgress = this.getGrowthTime();
                                this.process(recipe);
                                shouldsave = true;
                            }
                        }
                        this.getSolutionTank().drain(1, IFluidHandler.FluidAction.EXECUTE);
                    } else if (recipe != this.currentRecipe || this.inventory.getStackInSlot(0).isEmpty()) {
                        this.growthProgress = 0;
                    }
                }
                else{
                    this.growthProgress = 0;
                }
            }
        }

        if (shouldsave) {
            this.markDirty();
        }
    }

    private void process(IRecipe<? extends IInventory> recipe) {
        if (this.isProcessable(recipe)) {
            ItemStack seed = this.inventory.getStackInSlot(0);
            ItemStack itemstack1 = ((IRecipe<TileEntityCrystalGrowthChamber>) recipe).getCraftingResult(this);
            ItemStack output = this.inventory.getStackInSlot(4);
            if (output.isEmpty()) {
                this.inventory.setStackInSlot(4, itemstack1.copy());
            } else if (output.getItem() == itemstack1.getItem()) {
                output.grow(itemstack1.getCount());
            }
            seed.shrink(1);
        }
    }

    private boolean isProcessing(){
        return this.growthProgress>0;
    }

    protected int getGrowthTime() {
        return this.world.getRecipeManager().getRecipe((IRecipeType<CrystalizingRecipe>)this.recipeType, this, this.world).map(CrystalizingRecipe::getGrowthTime).orElse(1800);
    }

    private boolean isProcessable(IRecipe<? extends IInventory> recipe) {
        if(recipe != null && !this.inventory.getStackInSlot(0).isEmpty()){
            ItemStack output = ((IRecipe<TileEntityCrystalGrowthChamber>)recipe).getCraftingResult(this);
            if(output.isEmpty()){
                return false;
            }
            else {
                ItemStack itemstack1 = this.inventory.getStackInSlot(4);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(output)) {
                    return false;
                } else if (itemstack1.getCount() + output.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + output.getCount() <= itemstack1.getMaxStackSize()) {
                    return true;
                } else {
                    return itemstack1.getCount() + output.getCount() <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }

    @Nonnull
    public ItemStackHandler getInventory(){
        return this.inventory;
    }

    @Nonnull
    public FluidTank getWaterTank(){
        return this.waterTank;
    }

    public boolean tryFillingSolutionTank(int index2try){
        ItemStack stack = this.inventory.getStackInSlot(index2try);
        Item item = stack.getItem();
        FluidStack FluidToAdd = this.getAmountfromItem(item);
        int fillableAmount = this.SolutionTank.fill(FluidToAdd, IFluidHandler.FluidAction.SIMULATE);
        int drainableAmount = this.waterTank.drain(FluidToAdd.getAmount(), IFluidHandler.FluidAction.SIMULATE).getAmount();
        if(fillableAmount>=FluidToAdd.getAmount() && drainableAmount>0){
            int amount2Add = Math.min(fillableAmount, drainableAmount);
            FluidToAdd.setAmount(amount2Add);
            this.SolutionTank.fill(FluidToAdd, IFluidHandler.FluidAction.EXECUTE);
            this.waterTank.drain(amount2Add, IFluidHandler.FluidAction.EXECUTE);
            stack.shrink(1);
            return true;
        }
        return false;
    }

    private final LazyOptional<ItemStackHandler> Invhandler = LazyOptional.of(this::getInventory);
    private final LazyOptional<FluidTank> Fluidhandler = LazyOptional.of(this::getWaterTank);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.Invhandler.cast();
        }
        else if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return Fluidhandler.cast();
        }
        return super.getCapability(capability, side);
    }

    public FluidStack getAmountfromItem(Item item){
        if(item == registerItems.DUST_ORIGINIUM.get()){
            return new FluidStack(registerFluids.ORIGINIUM_SOLUTION, 400);
        }
        else if(item == registerItems.DUST_NETHER_QUARTZ.get()){
            return new FluidStack(registerFluids.NETHER_QUARTZ_SOLUTION, 800);
        }
        return FluidStack.EMPTY;
    }

    public void encodeExtraData(PacketBuffer buffer) {
        buffer.writeVarIntArray(this.FieldArray);
        buffer.writeFluidStack(this.waterTank.getFluid());
        buffer.writeFluidStack(this.SolutionTank.getFluid());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.growthProgress = nbt.getInt("progress");
        this.MaxGrowthProgress = nbt.getInt("maxprogress");
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        this.waterTank.readFromNBT(nbt.getCompound("water"));
        this.SolutionTank.readFromNBT(nbt.getCompound("solution"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("progress", this.growthProgress);
        compound.putInt("maxprogress", this.MaxGrowthProgress);
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("water", this.waterTank.writeToNBT(new CompoundNBT()));
        compound.put("solution", this.SolutionTank.writeToNBT(new CompoundNBT()));
        return compound;
    }
}
