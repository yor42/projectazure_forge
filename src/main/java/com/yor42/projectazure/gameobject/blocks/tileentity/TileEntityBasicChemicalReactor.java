package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.containers.machine.ContainerBasicChemicalReactor;
import com.yor42.projectazure.gameobject.crafting.recipes.BasicChemicalReactionRecipe;
import com.yor42.projectazure.setup.register.registerRecipes;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBasicChemicalReactor extends AbstractAnimatedTileEntityMachines{

    public FluidTank OutputTank = new FluidTank(10000);

    private final int[] FieldArray = {this.ProcessTime,this.totalProcessTime,TileEntityBasicChemicalReactor.this.energyStorage.getEnergyStored(), TileEntityBasicChemicalReactor.this.energyStorage.getMaxEnergyStored(),this.OutputTank.getFluidAmount(), this.OutputTank.getCapacity()};


    private final ContainerData fields = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TileEntityBasicChemicalReactor.this.ProcessTime;
                case 1:
                    return TileEntityBasicChemicalReactor.this.totalProcessTime;
                case 2:
                    return TileEntityBasicChemicalReactor.this.energyStorage.getEnergyStored();
                case 3:
                    return TileEntityBasicChemicalReactor.this.energyStorage.getMaxEnergyStored();
                case 4:
                    return TileEntityBasicChemicalReactor.this.OutputTank.getFluidAmount();
                case 5:
                    return TileEntityBasicChemicalReactor.this.OutputTank.getCapacity();

                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    TileEntityBasicChemicalReactor.this.ProcessTime = value;
                    break;
                case 1:
                    TileEntityBasicChemicalReactor.this.totalProcessTime = value;
                    break;
                case 2:
                    TileEntityBasicChemicalReactor.this.energyStorage.setEnergy(value);
                    break;
                case 3:
                    TileEntityBasicChemicalReactor.this.energyStorage.setMaxEnergy(value);
                    break;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    public TileEntityBasicChemicalReactor(BlockPos blockpos, BlockState blockstate) {
        super(registerTE.BASIC_CHEMICAL_REACTOR.get(), blockpos, blockstate);
        this.inventory = new ItemStackHandler(3){
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(slot == 0) {
                    return true;
                }
                else if(slot == 1){
                    return FluidUtil.getFluidHandler(stack).isPresent();
                }
                else {
                    return false;
                }
            }
        };
        this.powerConsumption = 150;
        this.energyStorage.setMaxEnergy(15000);
        this.recipeType = registerRecipes.Types.BASIC_CHEMICAL_REACTION;
    }

    @Override
    public void tick(Level level, BlockPos blockpos, BlockState blockstate, AbstractAnimateableEnergyTickTE abstractanimateableenergytickte) {
        ItemStack stack = this.inventory.getStackInSlot(1);
        NonNullConsumer<IFluidHandlerItem> TransferLiquidtoStack = (FluidHandler)->{
            FluidActionResult simresult = FluidUtil.tryFillContainer(stack, this.OutputTank, this.OutputTank.getCapacity(), null, false);
            if(simresult.isSuccess() && this.inventory.insertItem(2,simresult.getResult(), true).isEmpty()) {
                FluidActionResult result = FluidUtil.tryFillContainer(stack, this.OutputTank, this.OutputTank.getCapacity(), null, true);
                this.inventory.insertItem(2, result.getResult(), false);
                stack.shrink(1);
            }
        };
        FluidUtil.getFluidHandler(stack).ifPresent(TransferLiquidtoStack);
        super.tick(level, blockpos, blockstate, abstractanimateableenergytickte);
    }

    @Override
    protected int getTargetProcessTime(Recipe<?> recipe) {

        if(recipe instanceof BasicChemicalReactionRecipe){
            return ((BasicChemicalReactionRecipe) recipe).getProcesstime();
        }

        return 200;
    }

    @Override
    protected void process(Recipe<?> irecipe) {
        if(irecipe instanceof BasicChemicalReactionRecipe){
            ItemStack inputStack = this.inventory.getStackInSlot(0);
            if(!((BasicChemicalReactionRecipe) irecipe).test(inputStack)){
                return;
            }
            FluidStack result = ((BasicChemicalReactionRecipe) irecipe).getOutput();
            this.OutputTank.fill(result, IFluidHandler.FluidAction.EXECUTE);
            inputStack.shrink(1);
        }
    }

    @Override
    protected boolean canProcess(Recipe<?> irecipe) {
        if(irecipe instanceof BasicChemicalReactionRecipe){
            ItemStack inputStack = this.inventory.getStackInSlot(0);
            if(!((BasicChemicalReactionRecipe) irecipe).test(inputStack)){
                return false;
            }
            FluidStack result = ((BasicChemicalReactionRecipe) irecipe).getOutput();
            int simresult = this.OutputTank.fill(result, IFluidHandler.FluidAction.SIMULATE);
            return simresult == result.getAmount();
        }

        return false;
    }

    @Override
    public boolean isActive() {
        return this.fields.get(0)>0;
    }

    @Override
    protected <P extends BlockEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    protected void playsound() {

    }

    @Override
    public void saveAdditional(CompoundTag compoundtag) {
        super.saveAdditional(compoundtag);
        compoundtag.put("outputtank", this.OutputTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag compoundtag) {
        super.load(compoundtag);
        this.OutputTank.readFromNBT(compoundtag.getCompound("outputtank"));
    }

    @Override
    public void encodeExtraData(FriendlyByteBuf buffer) {
        buffer.writeVarIntArray(this.FieldArray);
        buffer.writeFluidStack(this.OutputTank.getFluid());
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("tileentity.basic_chemicalreactor.name");
    }

    private final LazyOptional<ItemStackHandler> INVENTORY = LazyOptional.of(()->this.inventory);
    private final LazyOptional<EnergyStorage> BATTERY = LazyOptional.of(()->this.energyStorage);
    private final LazyOptional<FluidTank> TANK = LazyOptional.of(()->this.OutputTank);
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return this.TANK.cast();
        }
        if(cap == CapabilityEnergy.ENERGY) {
            return this.BATTERY.cast();
        }
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.INVENTORY.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_213906_1_, Inventory p_213906_2_) {
        return new ContainerBasicChemicalReactor(p_213906_1_, p_213906_2_,this.inventory,  this.fields, this.OutputTank.getFluid());
    }
}
