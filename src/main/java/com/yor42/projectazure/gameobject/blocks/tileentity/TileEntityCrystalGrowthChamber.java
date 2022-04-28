package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.containers.machine.ContainerCrystalGrowthChamber;
import com.yor42.projectazure.gameobject.crafting.CrystalizingRecipe;
import com.yor42.projectazure.setup.register.registerFluids;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerRecipes;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
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

import static com.yor42.projectazure.libs.Constants.CRYSTAL_CHAMBER_SOLUTION_TANK_CAPACITY;
import static com.yor42.projectazure.setup.register.registerTE.CRYSTAL_GROWTH_CHAMBER;

public class TileEntityCrystalGrowthChamber extends BlockEntity implements MenuProvider, Container {

    public FluidTank waterTank = new FluidTank(8000, (fluidStack)->fluidStack.getFluid() == Fluids.WATER);
    public FluidTank SolutionTank = new FluidTank(CRYSTAL_CHAMBER_SOLUTION_TANK_CAPACITY);
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
    private Recipe<? extends TileEntityCrystalGrowthChamber> currentRecipe;
    protected final RecipeType<? extends CrystalizingRecipe> recipeType;

    private final int[] FieldArray = {this.growthProgress,this.MaxGrowthProgress,this.waterTank.getFluidAmount(), this.waterTank.getCapacity(),this.SolutionTank.getFluidAmount(), this.SolutionTank.getCapacity()};

    private final ContainerData fields = new ContainerData() {
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
        public int getCount() {
            return 6;
        }
    };

    public TileEntityCrystalGrowthChamber(BlockPos pos, BlockState state) {
        super(CRYSTAL_GROWTH_CHAMBER.get(), pos, state);
        this.recipeType = registerRecipes.Types.CRYSTALIZING;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.projectazure.crystal_chamber");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player p_39956_) {
        return new ContainerCrystalGrowthChamber(id, player, this.inventory, this.fields, this.getWaterTank().getFluid(), this.getSolutionTank().getFluid());
    }

    @Override
    public int getContainerSize() {
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
    public ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        this.inventory.getStackInSlot(i).shrink(i1);
        return this.inventory.getStackInSlot(i);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if(index >= 0 && index < this.inventory.getSlots()){
            this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack);
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

    private void process(Recipe<? extends TileEntityCrystalGrowthChamber> recipe) {
        if (this.isProcessable(recipe)) {
            ItemStack seed = this.inventory.getStackInSlot(0);
            ItemStack itemstack1 = recipe.getResultItem();
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

        if(this.getLevel() == null){
            return 1800;
        }

        return this.getLevel().getRecipeManager().getRecipeFor(this.recipeType, this, this.getLevel()).map(CrystalizingRecipe::getGrowthTime).orElse(1800);
    }

    private boolean isProcessable(Recipe<? extends TileEntityCrystalGrowthChamber> recipe) {
        if(recipe != null && !this.inventory.getStackInSlot(0).isEmpty()){
            ItemStack output = recipe.getResultItem();
            if(output.isEmpty()){
                return false;
            }
            else {
                ItemStack itemstack1 = this.inventory.getStackInSlot(4);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(output)) {
                    return false;
                } else if (itemstack1.getCount() + output.getCount() <= this.getMaxStackSize() && itemstack1.getCount() + output.getCount() <= itemstack1.getMaxStackSize()) {
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
            return new FluidStack(registerFluids.ORIGINIUM_SOLUTION_SOURCE, 400);
        }
        else if(item == registerItems.DUST_NETHER_QUARTZ.get()){
            return new FluidStack(registerFluids.NETHER_QUARTZ_SOLUTION_SOURCE, 800);
        }
        return FluidStack.EMPTY;
    }

    public void encodeExtraData(FriendlyByteBuf  buffer) {
        buffer.writeVarIntArray(this.FieldArray);
        buffer.writeFluidStack(this.waterTank.getFluid());
        buffer.writeFluidStack(this.SolutionTank.getFluid());
    }

    @Override
    public void load (CompoundTag nbt) {
        super.load(nbt);
        this.growthProgress = nbt.getInt("progress");
        this.MaxGrowthProgress = nbt.getInt("maxprogress");
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        this.waterTank.readFromNBT(nbt.getCompound("water"));
        this.SolutionTank.readFromNBT(nbt.getCompound("solution"));
    }

    @Nonnull
    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        compound.putInt("progress", this.growthProgress);
        compound.putInt("maxprogress", this.MaxGrowthProgress);
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("water", this.waterTank.writeToNBT(new CompoundTag()));
        compound.put("solution", this.SolutionTank.writeToNBT(new CompoundTag()));
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof TileEntityCrystalGrowthChamber){
            TileEntityCrystalGrowthChamber chamber = (TileEntityCrystalGrowthChamber) t;
            boolean shouldsave = false;
            if(!chamber.level.isClientSide()){
                for(int i = 1; i<4; i++){
                    if(chamber.tryFillingSolutionTank(i)){
                        break;
                    }
                }

                ItemStack stack = chamber.getItem(5);
                Consumer<FluidStack> TransferLiquids = (fluidstack) -> {
                    FluidTank tank = chamber.waterTank;
                    FluidActionResult actionresult = FluidUtil.tryEmptyContainer(stack, tank, tank.getCapacity(), null, true);
                    if (actionresult.isSuccess()) {
                        ItemStack result = actionresult.getResult();
                        if (chamber.inventory.insertItem(6, result, true).isEmpty()) {
                            if (chamber.inventory.getStackInSlot(6).isEmpty()){
                                chamber.inventory.setStackInSlot(6, result);
                            }
                            else {
                                chamber.inventory.insertItem(6, result, false);
                            }
                            stack.shrink(1);
                            chamber.inventory.setStackInSlot(5, stack);
                        }

                    }
                };
                FluidUtil.getFluidContained(stack).ifPresent(TransferLiquids);

                ItemStack SeedStack = chamber.inventory.getStackInSlot(0);
                if(!chamber.SolutionTank.isEmpty()) {
                    if (!SeedStack.isEmpty()) {
                        Recipe<? extends TileEntityCrystalGrowthChamber> recipe = chamber.level.getRecipeManager().getRecipeFor((RecipeType<? extends CrystalizingRecipe>) chamber.recipeType, chamber, chamber.level).orElse(null);
                        if (chamber.isProcessable(recipe)) {
                            if (chamber.currentRecipe == null) {
                                chamber.currentRecipe = recipe;
                            }
                            if (chamber.MaxGrowthProgress == 0) {
                                chamber.MaxGrowthProgress = chamber.getGrowthTime();
                            }

                            if (!chamber.isProcessing()) {
                                shouldsave = true;
                                chamber.growthProgress++;
                                chamber.MaxGrowthProgress = chamber.getGrowthTime();
                            } else {
                                chamber.growthProgress++;
                                if (chamber.MaxGrowthProgress != 0 && chamber.growthProgress == chamber.MaxGrowthProgress) {
                                    chamber.growthProgress = 0;
                                    chamber.MaxGrowthProgress = chamber.getGrowthTime();
                                    chamber.process(recipe);
                                    shouldsave = true;
                                }
                            }
                            chamber.getSolutionTank().drain(1, IFluidHandler.FluidAction.EXECUTE);
                        } else if (recipe != chamber.currentRecipe || chamber.inventory.getStackInSlot(0).isEmpty()) {
                            chamber.growthProgress = 0;
                        }
                    }
                    else{
                        chamber.growthProgress = 0;
                    }
                }
            }

            if (shouldsave) {
                chamber.setChanged();
            }
        }
    }
}
