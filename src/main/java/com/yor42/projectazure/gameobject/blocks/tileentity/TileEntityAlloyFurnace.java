package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.blocks.AlloyFurnaceBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerAlloyFurnace;
import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
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
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityAlloyFurnace extends LockableTileEntity implements INamedContainerProvider, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {

    ItemStackHandler inventory = new ItemStackHandler(4);
    private int burnTime;
    private int totalBurntime;
    private int cookTime;
    private int cookTimeTotal;
    protected final IIntArray machineInfo = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return TileEntityAlloyFurnace.this.burnTime;
                case 1:
                    return TileEntityAlloyFurnace.this.totalBurntime;
                case 2:
                    return TileEntityAlloyFurnace.this.cookTime;
                case 3:
                    return TileEntityAlloyFurnace.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    TileEntityAlloyFurnace.this.burnTime = value;
                    break;
                case 1:
                    TileEntityAlloyFurnace.this.totalBurntime = value;
                    break;
                case 2:
                    TileEntityAlloyFurnace.this.cookTime = value;
                    break;
                case 3:
                    TileEntityAlloyFurnace.this.cookTimeTotal = value;
            }

        }

        public int size() {
            return 4;
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    protected final IRecipeType<AlloyingRecipe> recipeType = registerRecipes.Types.ALLOYING;

    public TileEntityAlloyFurnace() {
        super(registerTE.ALLOY_FURNACE.get());
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        this.burnTime = nbt.getInt("BurnTime");
        this.cookTime = nbt.getInt("CookTime");
        this.cookTimeTotal = nbt.getInt("CookTimeTotal");

        //This is where forge kicks in
        this.totalBurntime = ForgeHooks.getBurnTime(this.inventory.getStackInSlot(2));
        CompoundNBT compoundnbt = nbt.getCompound("RecipesUsed");

        for(String s : compoundnbt.keySet()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }

    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.put("inventory", this.inventory.serializeNBT());
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        compound.put("RecipesUsed", compoundnbt);
        return compound;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("alloy_furnace");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ContainerAlloyFurnace(id, player, this.inventory, this.machineInfo);
    }
    private final LazyOptional<ItemStackHandler> INVENTORY = LazyOptional.of(()->this.inventory);
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {

        if(cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return this.INVENTORY.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public int getSizeInventory() {
        return 4;
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
        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!(this.world != null && this.world.isRemote)) {
            ItemStack FuelStack = this.inventory.getStackInSlot(2);
            if (this.isBurning() || !FuelStack.isEmpty() && !this.inventory.getStackInSlot(0).isEmpty()&& !this.inventory.getStackInSlot(1).isEmpty()) {
                IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe(this.recipeType, this, this.world).orElse(null);

                if(irecipe != null){
                    this.cookTimeTotal = this.getCookTime();
                }

                if (!this.isBurning() && this.canSmelt(irecipe)) {
                    this.burnTime = ForgeHooks.getBurnTime(FuelStack, IRecipeType.BLASTING);
                    this.totalBurntime = this.burnTime;
                    if (this.isBurning()) {
                        flag1 = true;
                        if (FuelStack.hasContainerItem())
                            this.inventory.setStackInSlot(2, FuelStack.getContainerItem());
                        else if (!FuelStack.isEmpty()) {
                            FuelStack.shrink(1);
                            if (FuelStack.isEmpty()) {
                                this.inventory.setStackInSlot(2, FuelStack.getContainerItem());
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt(irecipe)) {
                    ++this.cookTime;
                    if (this.cookTime >= this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.smelt(irecipe);
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AlloyFurnaceBlock.ACTIVE, this.isBurning()), 3);
            }
        }

        if (flag1) {
            this.markDirty();
        }

    }
    protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
        if (!this.inventory.getStackInSlot(0).isEmpty()&&!this.inventory.getStackInSlot(1).isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.getRecipeOutput();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack outputstack = this.inventory.getStackInSlot(3);
                if (outputstack.isEmpty()) {
                    return true;
                } else if (!outputstack.isItemEqual(itemstack)) {
                    return false;
                } else if (outputstack.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && outputstack.getCount() + itemstack.getCount() <= outputstack.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return outputstack.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    private void smelt(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack input1 = this.inventory.getStackInSlot(0);
            ItemStack input2 = this.inventory.getStackInSlot(1);
            ItemStack recipeOutput = recipe.getRecipeOutput();
            ItemStack outputslot = this.inventory.getStackInSlot(3);
            if (outputslot.isEmpty()) {
                this.inventory.setStackInSlot(3, recipeOutput.copy());
            } else if (outputslot.getItem() == recipeOutput.getItem()) {
                outputslot.grow(recipeOutput.getCount());
            }

            if (!(this.world != null && this.world.isRemote)) {
                this.setRecipeUsed(recipe);
            }

            int countslot1 = 1;
            int countslot2 = 1;
            if(recipe instanceof AlloyingRecipe){
                countslot1 = ((AlloyingRecipe) recipe).getIng1Count();
                countslot2 = ((AlloyingRecipe) recipe).getIng2Count();
            }

            input1.shrink(countslot1);
            input2.shrink(countslot2);
        }
    }

    protected int getCookTime() {
        return this.world.getRecipeManager().getRecipe(this.recipeType, this, this.world).map(AlloyingRecipe::getProcessTick).orElse(200);
    }

    public void encodeExtraData(PacketBuffer buffer) {
    }
}
