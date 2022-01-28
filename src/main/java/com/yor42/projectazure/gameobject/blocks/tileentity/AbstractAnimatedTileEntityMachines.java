package com.yor42.projectazure.gameobject.blocks.tileentity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.blocks.AbstractElectricMachineBlock.POWERED;
import static com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock.ACTIVE;

public abstract class AbstractAnimatedTileEntityMachines extends AbstractAnimateableEnergyTickTE implements IRecipeHolder, IRecipeHelperPopulator {

    protected final AnimationFactory factory = new AnimationFactory(this);

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    protected IRecipeType<? extends IRecipe<IInventory>> recipeType;

    protected AbstractAnimatedTileEntityMachines(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    protected int ProcessTime, totalProcessTime;
    protected int powerConsumption;

    public boolean isPowered(){
        return this.getEnergyStorage().getEnergyStored()>=this.getPowerConsumption();
    }

    @Override
    public void tick() {
        boolean isActive = this.isActive();
        boolean shouldsave = false;
        boolean isPowered = this.isPowered();

        if (this.level != null && !this.level.isClientSide) {
            ItemStack ingredient = this.inventory.getStackInSlot(0);
            ItemStack mold = this.inventory.getStackInSlot(1);

            if (!ingredient.isEmpty() && !mold.isEmpty()) {
                IRecipe<?> irecipe = this.level.getRecipeManager().getRecipeFor((IRecipeType<? extends IRecipe<IInventory>>) this.recipeType, this, this.level).orElse(null);

                boolean flag1 = this.energyStorage.getEnergyStored() >= this.powerConsumption;
                boolean flag2 = this.canProcess(irecipe);

                if (flag1 && flag2) {
                    if (this.totalProcessTime == 0) {
                        this.totalProcessTime = this.getTargetProcessTime();
                    }
                    shouldsave = true;
                    this.ProcessTime++;
                    this.energyStorage.extractEnergy(this.powerConsumption, false);
                    if (this.ProcessTime == this.totalProcessTime) {
                        this.ProcessTime = 0;
                        this.totalProcessTime = this.getTargetProcessTime();
                        this.process(irecipe);
                    }
                } else {
                    this.ProcessTime = 0;
                }
            }
            else {
                this.ProcessTime = 0;
            }

        }
        if(shouldsave){
            this.setChanged();
        }

        if(this.level != null && !this.level.isClientSide){
            if(isPowered!=this.isPowered() || this.isPowered() && !this.level.getBlockState(this.worldPosition).getValue(POWERED) || !this.isPowered() && this.level.getBlockState(this.worldPosition).getValue(POWERED)) {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(POWERED, this.isPowered()), 2);
            }
            if(isActive!=this.isActive()){
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(ACTIVE, this.isActive()), 2);
            }
        }

        if(!isActive && this.isActive()){
            this.playsound();
        }


        if(this.getLevel() != null && isActive != this.isActive()) {
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    protected abstract int getTargetProcessTime();

    protected abstract void process(IRecipe<?> irecipe);

    protected abstract boolean canProcess(IRecipe<?> irecipe);

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.ProcessTime = nbt.getInt("processtime");
        this.totalProcessTime = nbt.getInt("totalprocesstime");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("processtime", this.ProcessTime);
        compound.putInt("totalprocesstime", this.totalProcessTime);
        return compound;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT syncTag = super.getUpdateTag();
        syncTag.putInt("progress", this.ProcessTime);
        syncTag.putInt("totalprogress", this.totalProcessTime);
        return new SUpdateTileEntityPacket(worldPosition, 1, syncTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundNBT syncTag = pkt.getTag();
        this.totalProcessTime = syncTag.getInt("totalprogress");
        this.ProcessTime = syncTag.getInt("progress");
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        ItemStack stack1 = this.getItem(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(stack1) && ItemStack.tagMatches(stack, stack1);

        if (index == 0 || index==1 && !flag) {
            this.totalProcessTime = this.getTargetProcessTime();
            this.ProcessTime = 0;
            this.setChanged();
        }
    }

    public int getTotalProcessTime() {
        return this.totalProcessTime;
    }

    public int getProcessTime() {
        return this.ProcessTime;
    }
    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public void clearContent() {
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

    public boolean isActive(){
        return this.ProcessTime>0;
    }



}
