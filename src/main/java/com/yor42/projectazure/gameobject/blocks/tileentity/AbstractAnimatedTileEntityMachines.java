package com.yor42.projectazure.gameobject.blocks.tileentity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.blocks.AbstractElectricMachineBlock.POWERED;
import static com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock.ACTIVE;

public abstract class AbstractAnimatedTileEntityMachines extends AbstractAnimateableEnergyTickTE implements RecipeHolder, StackedContentsCompatible {

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    protected RecipeType<? extends Recipe<Container>> recipeType;

    protected AbstractAnimatedTileEntityMachines(BlockEntityType<?> typeIn) {
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

                Recipe<?> irecipe = this.level.getRecipeManager().getRecipeFor((RecipeType<? extends Recipe<Container>>) this.recipeType, this, this.level).orElse(null);

                boolean flag1 = this.energyStorage.getEnergyStored() >= this.powerConsumption;
                boolean flag2 = this.canProcess(irecipe);

                if (flag1 && flag2) {
                    if (this.totalProcessTime == 0) {
                        this.totalProcessTime = this.getTargetProcessTime(irecipe);
                    }
                    shouldsave = true;
                    this.ProcessTime++;
                    this.energyStorage.extractEnergy(this.powerConsumption, false);
                    if (this.ProcessTime == this.totalProcessTime) {
                        this.ProcessTime = 0;
                        this.totalProcessTime = this.getTargetProcessTime(irecipe);
                        this.process(irecipe);
                    }
                } else {
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

    protected abstract int getTargetProcessTime(Recipe<?> iRecipe);

    protected abstract void process(Recipe<?> irecipe);

    protected abstract boolean canProcess(Recipe<?> irecipe);

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.ProcessTime = nbt.getInt("processtime");
        this.totalProcessTime = nbt.getInt("totalprocesstime");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putInt("processtime", this.ProcessTime);
        compound.putInt("totalprocesstime", this.totalProcessTime);
        return compound;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag syncTag = super.getUpdateTag();
        syncTag.putInt("progress", this.ProcessTime);
        syncTag.putInt("totalprogress", this.totalProcessTime);
        return new ClientboundBlockEntityDataPacket(worldPosition, 1, syncTag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundTag syncTag = pkt.getTag();
        this.totalProcessTime = syncTag.getInt("totalprogress");
        this.ProcessTime = syncTag.getInt("progress");
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        ItemStack stack1 = this.getItem(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(stack1) && ItemStack.tagMatches(stack, stack1);

        if (index == 0 || index==1 && !flag) {
            this.ProcessTime = 0;
            this.setChanged();
        }
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
        for(int i=0; i<this.inventory.getSlots(); i++) {
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

    public boolean isActive(){
        return this.ProcessTime>0;
    }



}
