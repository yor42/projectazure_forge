package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.blocks.MetalPressBlock;
import com.yor42.projectazure.gameobject.containers.ContainerMetalPress;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.gameobject.energy.CustomEnergyStorage;
import com.yor42.projectazure.setup.register.registerRecipes;
import com.yor42.projectazure.setup.register.registerTE;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityMetalPress extends LockableTileEntity implements INamedContainerProvider, ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity  {
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    protected final IRecipeType<? extends PressingRecipe> recipeType;
    protected CustomEnergyStorage energyStorage = new CustomEnergyStorage(15000);
    protected ItemStackHandler inventory = new ItemStackHandler(3);
    protected int ProcessTime, totalProcessTime;
    protected final int powerConsumption;

    public TileEntityMetalPress() {
        super(registerTE.METAL_PRESS.get());
        this.recipeType = registerRecipes.Types.PRESSING;
        this.powerConsumption = 100;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.energyStorage.deserializeNBT(nbt.getCompound("inventory"));
        this.inventory.deserializeNBT(nbt.getCompound("energy_storage"));
        this.ProcessTime = nbt.getInt("processtime");
        this.totalProcessTime = nbt.getInt("totalprocesstime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("energy_storage", this.energyStorage.serializeNBT());
        compound.putInt("processtime", this.ProcessTime);
        compound.putInt("totalprocesstime", this.totalProcessTime);
        return compound;
    }

    public void encodeExtraData(PacketBuffer buffer){
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0){
            return true;
        }
        else if(index == 1){
            return stack.getItem().isIn(ModTags.Items.EXTRUSION_MOLD);
        }
        else{
            return false;
        }
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("te.metal_press");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ContainerMetalPress(id, player, this.inventory, this.pos);
    }

    public boolean isActive(){
        return this.ProcessTime>0;
    }

    @Override
    public int getSizeInventory() {
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
    public ItemStack getStackInSlot(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = this.inventory.getStackInSlot(index);
        stack.shrink(count);
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index >=0 && index < this.inventory.getSlots()){
            this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack stack1 = this.getStackInSlot(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(stack1) && ItemStack.areItemStackTagsEqual(stack, stack1);
        this.inventory.setStackInSlot(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 || index==1 && !flag) {
            this.totalProcessTime = this.getTargetProcessTime();
            this.ProcessTime = 0;
            this.markDirty();
        }
    }

    public int getTotalProcessTime() {
        return this.totalProcessTime;
    }

    protected int getTargetProcessTime(){
        return this.world.getRecipeManager().getRecipe((IRecipeType<? extends PressingRecipe>)this.recipeType, this, this.world).map(PressingRecipe::getProcessTick).orElse(200);
    }

    public int getProcessTime() {
        return this.ProcessTime;
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
        for(int i=0; i<this.inventory.getSlots(); i++) {
            helper.accountStack(this.inventory.getStackInSlot(i));
        }
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    public CustomEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @Override
    public void tick() {
        boolean isActive = this.isActive();
        boolean shouldsave = false;

        if(this.world.isBlockPowered(this.getPos())){
            this.energyStorage.receiveEnergy(1000, false);
        }


        if(this.world != null && !this.world.isRemote){
            ItemStack ingredient = this.inventory.getStackInSlot(0);
            ItemStack mold = this.inventory.getStackInSlot(1);

            if(!ingredient.isEmpty() && !mold.isEmpty()){
                IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe((IRecipeType<? extends PressingRecipe>)this.recipeType, this, this.world).orElse(null);
                if(this.energyStorage.getEnergyStored()>=this.powerConsumption && this.canProcess(irecipe)){
                    shouldsave = true;
                    this.ProcessTime++;
                    this.energyStorage.extractEnergy(this.powerConsumption, false);
                    if(this.ProcessTime == this.totalProcessTime) {
                        this.ProcessTime = 0;
                        this.totalProcessTime = this.getTargetProcessTime();
                        this.process(irecipe);
                        shouldsave = true;
                    }
                }
                else {
                    this.ProcessTime = 0;
                }
            }else {
                this.ProcessTime = 0;
            }

            if (isActive != this.isActive()) {
                shouldsave = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(MetalPressBlock.ACTIVE, this.isActive()), 3);
            }
        }

        if(shouldsave){
            this.markDirty();
        }
    }

    private void process(IRecipe<?> irecipe) {

        if(irecipe != null && this.canProcess(irecipe)){
            ItemStack ingredient = this.inventory.getStackInSlot(0);
            ItemStack mold = this.inventory.getStackInSlot(1);
            ItemStack output = irecipe.getRecipeOutput();
            ItemStack outputslot = this.inventory.getStackInSlot(2);

            if (outputslot.isEmpty()) {
                this.inventory.setStackInSlot(2, output.copy());
            } else if (outputslot.getItem() == outputslot.getItem()) {
                outputslot.grow(output.getCount());
            }

            if (!(this.world != null && this.world.isRemote)) {
                this.setRecipeUsed(irecipe);
            }

            if (ingredient.hasContainerItem()) {
                this.inventory.setStackInSlot(1, ingredient.getContainerItem());
            } else if (!ingredient.isEmpty()) {
                ingredient.shrink(1);
                if (ingredient.isEmpty()) {
                    this.inventory.setStackInSlot(1, ingredient.getContainerItem());
                }
            }

            if (mold.hasContainerItem()) {
                this.inventory.setStackInSlot(1, mold.getContainerItem());
            } else if (!mold.isEmpty()) {
                mold.shrink(1);
                if (mold.isEmpty()) {
                    this.inventory.setStackInSlot(1, mold.getContainerItem());
                }
            }
            
        }
    }

    protected boolean canProcess(@Nullable IRecipe<?> recipeIn) {
        if (!this.inventory.getStackInSlot(0).isEmpty() && !this.inventory.getStackInSlot(1).isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.getRecipeOutput();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.inventory.getStackInSlot(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }
}
