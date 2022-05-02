package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.blocks.AlloyFurnaceBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerAlloyFurnace;
import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityAlloyFurnace extends BlockEntity implements MenuProvider, RecipeHolder, net.minecraft.world.Container {

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
    protected final RecipeType<AlloyingRecipe> recipeType = AlloyingRecipe.TYPE.Instance;

    public TileEntityAlloyFurnace(BlockPos pos, BlockState state) {
        super(Main.ALLOY_FURNACE_BLOCK_ENTITY.get(), pos, state);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        this.burnTime = nbt.getInt("BurnTime");
        this.cookTime = nbt.getInt("CookTime");
        this.cookTimeTotal = nbt.getInt("CookTimeTotal");

        //This is where forge kicks in
        this.totalBurntime = ForgeHooks.getBurnTime(this.inventory.getStackInSlot(2), RecipeType.SMELTING);
        CompoundTag compoundnbt = nbt.getCompound("RecipesUsed");

        for(String s : compoundnbt.getAllKeys()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }

    }

    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.put("inventory", this.inventory.serializeNBT());
        CompoundTag compoundnbt = new CompoundTag();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        compound.put("RecipesUsed", compoundnbt);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new ContainerAlloyFurnace(p_39954_, p_39955_, this.inventory, this.machineInfo);
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
                } else if (outputstack.getCount() + itemstack.getCount() <= outputstack.getMaxStackSize()&& outputstack.getCount() + itemstack.getCount() <= outputstack.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
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
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack input1 = this.inventory.getStackInSlot(0);
            ItemStack input2 = this.inventory.getStackInSlot(1);
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
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, this, this.level).map(AlloyingRecipe::getProcessTick).orElse(200);
    }

    public void encodeExtraData(FriendlyByteBuf  buffer) {
        int[] data = {this.burnTime, this.totalBurntime, this.cookTime,this.cookTimeTotal};
        buffer.writeVarIntArray(data);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.alloyfurnace");
    }

    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i=0;i<this.inventory.getSlots(); i++){
            if(!this.inventory.getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        return this.inventory.getStackInSlot(p_18941_);
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        return this.inventory.extractItem(p_18942_, p_18943_, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return this.inventory.extractItem(p_18951_, p_18951_, true);
    }


    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        this.inventory.setStackInSlot(p_18944_, p_18945_);
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return true;
    }

    @Override
    public void clearContent() {
        for(int i=0;i<this.inventory.getSlots(); i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {

        if(t instanceof TileEntityAlloyFurnace) {
            TileEntityAlloyFurnace furnace = (TileEntityAlloyFurnace) t;

            boolean flag = furnace.isBurning();
            boolean flag1 = false;
            if (furnace.isBurning()) {
                --furnace.burnTime;
            }

            if (!(furnace.level != null && furnace.level.isClientSide)) {
                ItemStack FuelStack = furnace.inventory.getStackInSlot(2);
                if (furnace.isBurning() || !FuelStack.isEmpty() && !furnace.inventory.getStackInSlot(0).isEmpty() && !furnace.inventory.getStackInSlot(1).isEmpty()) {
                    Recipe<?> irecipe = furnace.level.getRecipeManager().getRecipeFor(furnace.recipeType, furnace, furnace.level).orElse(null);

                    if (irecipe != null) {
                        furnace.cookTimeTotal = furnace.getCookTime();
                    }

                    if (!furnace.isBurning() && furnace.canSmelt(irecipe)) {
                        furnace.burnTime = ForgeHooks.getBurnTime(FuelStack, RecipeType.BLASTING);
                        furnace.totalBurntime = furnace.burnTime;
                        if (furnace.isBurning()) {
                            flag1 = true;
                            if (FuelStack.hasContainerItem())
                                furnace.inventory.setStackInSlot(2, FuelStack.getContainerItem());
                            else if (!FuelStack.isEmpty()) {
                                FuelStack.shrink(1);
                                if (FuelStack.isEmpty()) {
                                    furnace.inventory.setStackInSlot(2, FuelStack.getContainerItem());
                                }
                            }
                        }
                    }

                    if (furnace.isBurning() && furnace.canSmelt(irecipe)) {
                        ++furnace.cookTime;
                        if (furnace.cookTime >= furnace.cookTimeTotal) {
                            furnace.cookTime = 0;
                            furnace.cookTimeTotal = furnace.getCookTime();
                            furnace.smelt(irecipe);
                            flag1 = true;
                        }
                    } else {
                        furnace.cookTime = 0;
                    }
                } else if (!furnace.isBurning() && furnace.cookTime > 0) {
                    furnace.cookTime = Mth.clamp(furnace.cookTime - 2, 0, furnace.cookTimeTotal);
                }

                if (flag != furnace.isBurning()) {
                    flag1 = true;
                    furnace.level.setBlock(furnace.worldPosition, furnace.level.getBlockState(furnace.worldPosition).setValue(AlloyFurnaceBlock.ACTIVE, furnace.isBurning()), 3);
                }
            }

            if (flag1) {
                furnace.setChanged();
            }
        }
    }
}
