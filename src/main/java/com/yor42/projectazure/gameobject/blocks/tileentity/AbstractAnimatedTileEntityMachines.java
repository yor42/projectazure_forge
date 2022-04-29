package com.yor42.projectazure.gameobject.blocks.tileentity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.blocks.AbstractElectricMachineBlock.POWERED;
import static com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock.ACTIVE;

public abstract class AbstractAnimatedTileEntityMachines extends AbstractAnimateableEnergyTickTE implements Container, RecipeHolder {

    protected final AnimationFactory factory = new AnimationFactory(this);

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    protected RecipeType<? extends Recipe<?>> recipeType;

    protected AbstractAnimatedTileEntityMachines(BlockEntityType<?> typeIn, BlockPos p_155229_, BlockState p_155230_) {
        super(typeIn, p_155229_, p_155230_);
    }

    protected int ProcessTime, totalProcessTime;
    protected int powerConsumption;

    public boolean isPowered(){
        return this.getEnergyStorage().getEnergyStored()>=this.getPowerConsumption();
    }

    protected abstract int getTargetProcessTime();

    protected abstract void process(Recipe<?> irecipe);

    protected abstract boolean canProcess(Recipe<?> irecipe);

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.ProcessTime = nbt.getInt("processtime");
        this.totalProcessTime = nbt.getInt("totalprocesstime");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("processtime", this.ProcessTime);
        compound.putInt("totalprocesstime", this.totalProcessTime);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
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

    public boolean isActive(){
        return this.ProcessTime>0;
    }

}
