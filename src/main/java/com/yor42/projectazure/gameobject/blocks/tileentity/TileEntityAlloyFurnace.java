package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.blocks.machines.AlloyFurnaceBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerAlloyFurnace;
import com.yor42.projectazure.gameobject.crafting.recipes.AlloyingRecipe;
import com.yor42.projectazure.setup.register.registerRecipes;
import com.yor42.projectazure.setup.register.registerTE;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityAlloyFurnace extends BaseContainerBlockEntity implements MenuProvider, RecipeHolder, StackedContentsCompatible, TickableBlockEntity {

    ItemStackHandler inventory = new ItemStackHandler(4);
    private int burnTime;
    private int totalBurntime;
    private int cookTime;
    private int cookTimeTotal;
    protected final ContainerData machineInfo = new ContainerData() {
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

        public int getCount() {
            return 4;
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    protected final RecipeType<AlloyingRecipe> recipeType = registerRecipes.Types.ALLOYING;

    public TileEntityAlloyFurnace() {
        super(registerTE.ALLOY_FURNACE.get());
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        this.burnTime = nbt.getInt("BurnTime");
        this.cookTime = nbt.getInt("CookTime");
        this.cookTimeTotal = nbt.getInt("CookTimeTotal");

        //This is where forge kicks in
        this.totalBurntime = ForgeHooks.getBurnTime(this.inventory.getStackInSlot(2));
        CompoundTag compoundnbt = nbt.getCompound("RecipesUsed");

        for(String s : compoundnbt.getAllKeys()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }

    }

    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.put("inventory", this.inventory.serializeNBT());
        CompoundTag compoundnbt = new CompoundTag();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        compound.put("RecipesUsed", compoundnbt);
        return compound;
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("tileentity.alloy_furnace.name");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
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
    public int getContainerSize() {
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
    public ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = this.inventory.getStackInSlot(index);
        stack.shrink(count);
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index >=0 && index < this.inventory.getSlots()){
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

    @Override
    public void fillStackedContents(StackedContents helper) {
        for(int i=0;i<this.inventory.getSlots();i++) {
            helper.accountStack(this.inventory.getStackInSlot(i));
        }
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!(this.level != null && this.level.isClientSide)) {
            ItemStack FuelStack = this.inventory.getStackInSlot(2);
            if (this.isBurning() || !FuelStack.isEmpty() && !this.inventory.getStackInSlot(0).isEmpty()&& !this.inventory.getStackInSlot(1).isEmpty()) {
                Recipe<?> irecipe = this.level.getRecipeManager().getRecipeFor(this.recipeType, this, this.level).orElse(null);

                if(irecipe != null){
                    this.cookTimeTotal = this.getCookTime();
                }

                if (!this.isBurning() && this.canSmelt(irecipe)) {
                    this.burnTime = ForgeHooks.getBurnTime(FuelStack, RecipeType.BLASTING);
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
                this.cookTime = Mth.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AlloyFurnaceBlock.ACTIVE, this.isBurning()), 3);
            }
        }

        if (flag1) {
            this.setChanged();
        }

    }
    protected boolean canSmelt(@Nullable Recipe<?> recipeIn) {
        if (!this.inventory.getStackInSlot(0).isEmpty()&&!this.inventory.getStackInSlot(1).isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.getResultItem();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack outputstack = this.inventory.getStackInSlot(3);
                if (outputstack.isEmpty()) {
                    return true;
                } else if (!outputstack.sameItem(itemstack)) {
                    return false;
                } else if (outputstack.getCount() + itemstack.getCount() <= this.getMaxStackSize() && outputstack.getCount() + itemstack.getCount() <= outputstack.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return outputstack.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    private void smelt(@Nullable Recipe<?> recipe) {
        if (recipe instanceof AlloyingRecipe && this.canSmelt(recipe)) {
            ItemStack recipeOutput = recipe.getResultItem();
            ItemStack outputslot = this.inventory.getStackInSlot(3);
            if (outputslot.isEmpty()) {
                this.inventory.setStackInSlot(3, recipeOutput.copy());
            } else if (outputslot.getItem() == recipeOutput.getItem()) {
                outputslot.grow(recipeOutput.getCount());
            }

            if (!(this.level != null && this.level.isClientSide)) {
                this.setRecipeUsed(recipe);
            }

            for(int i=0; i<2; i++){
                byte count = ((AlloyingRecipe)recipe).ingredients.get(i).getSecond();
                this.inventory.getStackInSlot(i).shrink(count);
            }
        }
    }

    protected int getCookTime() {
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, this, this.level).map(AlloyingRecipe::getProcessTick).orElse(200);
    }

    public void encodeExtraData(FriendlyByteBuf buffer) {
    }
}
