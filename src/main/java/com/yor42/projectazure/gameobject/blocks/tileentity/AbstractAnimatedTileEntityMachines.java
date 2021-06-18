package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.energy.CustomEnergyStorage;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public abstract class AbstractAnimatedTileEntityMachines extends AbstractAnimateableEnergyTickTE implements IRecipeHolder, IRecipeHelperPopulator {

    protected final AnimationFactory factory = new AnimationFactory(this);

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    protected IRecipeType<? extends IRecipe<IInventory>> recipeType;

    protected AbstractAnimatedTileEntityMachines(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    protected int ProcessTime, totalProcessTime;
    protected int powerConsumption;

    @Override
    public void tick() {
        boolean isActive = this.isActive();
        boolean shouldsave = false;


        if (this.world != null && !this.world.isRemote) {
            ItemStack ingredient = this.inventory.getStackInSlot(0);
            ItemStack mold = this.inventory.getStackInSlot(1);

            if (!ingredient.isEmpty() && !mold.isEmpty()) {
                IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe((IRecipeType<? extends IRecipe<IInventory>>) this.recipeType, this, this.world).orElse(null);

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
            this.markDirty();
        }

        if(!isActive && this.isActive()){
            this.playsound();
        }


        if(this.getWorld() != null && isActive != this.isActive()) {
            this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    protected abstract int getTargetProcessTime();

    protected abstract void process(IRecipe<?> irecipe);

    protected abstract boolean canProcess(IRecipe<?> irecipe);

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.ProcessTime = nbt.getInt("processtime");
        this.totalProcessTime = nbt.getInt("totalprocesstime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
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
        return new SUpdateTileEntityPacket(pos, 1, syncTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundNBT syncTag = pkt.getNbtCompound();
        this.totalProcessTime = syncTag.getInt("totalprogress");
        this.ProcessTime = syncTag.getInt("progress");
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
        ItemStack stack1 = this.getStackInSlot(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(stack1) && ItemStack.areItemStackTagsEqual(stack, stack1);

        if (index == 0 || index==1 && !flag) {
            this.totalProcessTime = this.getTargetProcessTime();
            this.ProcessTime = 0;
            this.markDirty();
        }
    }

    public int getTotalProcessTime() {
        return this.totalProcessTime;
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

    public boolean isActive(){
        return this.ProcessTime>0;
    }



}
