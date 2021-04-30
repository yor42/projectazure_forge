package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.gameobject.energy.CustomEnergyStorage;
import com.yor42.projectazure.setup.register.registerRecipes;
import com.yor42.projectazure.setup.register.registerTE;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;

public class TileEntityMetalPress extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity  {
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    protected final IRecipeType<? extends PressingRecipe> recipeType;
    protected CustomEnergyStorage energyStorage = new CustomEnergyStorage(15000);
    protected ItemStackHandler inventory = new ItemStackHandler(3);
    protected int ProcessTime, totalProcessTime;

    public TileEntityMetalPress() {
        super(registerTE.METAL_PRESS.get());
        this.recipeType = registerRecipes.Types.PRESSING;
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
        return null;
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

        if (index == 0 && !flag) {
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

    @Override
    public void tick() {

    }
}
