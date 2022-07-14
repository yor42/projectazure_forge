package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.blocks.PantryBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerPantry;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityPantry extends TileEntity implements IInventory {
    private int openCount;
    public TileEntityPantry() {
        super(registerTE.PANTRY.get());
    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inventory", this.inventory.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void load(@Nonnull BlockState p_230337_1_, @Nonnull CompoundNBT compoundNBT) {
        super.load(p_230337_1_, compoundNBT);
        this.inventory.deserializeNBT(compoundNBT.getCompound("inventory"));
    }

    private final ItemStackHandler inventory = new ItemStackHandler(54){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            Food food = stack.getItem().getFoodProperties();
            if(food == null){
                return false;
            }

            if(food.getEffects().isEmpty()){
                return true;
            }
            else{
                for(Pair<EffectInstance, Float> effect : food.getEffects()){
                    if(effect.getFirst().getEffect().getCategory() == EffectType.HARMFUL){
                        return false;
                    }
                }
            }

            return true;
        }
    };

    public void recheckOpen() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.openCount = getOpenCount(this.level, this, i, j, k);
        if (this.openCount > 0) {
            this.scheduleRecheck();
        } else {
            BlockState blockstate = this.getBlockState();
            if (!(blockstate.getBlock() instanceof PantryBlock)) {
                this.setRemoved();
                return;
            }

            boolean flag = blockstate.getValue(PantryBlock.OPEN);
            if (flag) {
                this.playSound(blockstate, SoundEvents.CHEST_CLOSE);
                this.updateBlockState(blockstate, false);
            }
        }
    }

    public static int getOpenCount(World p_213976_0_, TileEntityPantry p_213976_1_, int p_213976_2_, int p_213976_3_, int p_213976_4_) {
        int i = 0;

        for(PlayerEntity playerentity : p_213976_0_.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB((double)((float)p_213976_2_ - 5.0F), (float)p_213976_3_ - 5.0F, (double)((float)p_213976_4_ - 5.0F), (float)(p_213976_2_ + 1) + 5.0F, (float)(p_213976_3_ + 1) + 5.0F, (float)(p_213976_4_ + 1) + 5.0F))) {
            if (playerentity.containerMenu instanceof ContainerPantry) {
                IInventory iinventory = ((ContainerPantry)playerentity.containerMenu).getInventory();
                if (iinventory == p_213976_1_) {
                    ++i;
                }
            }
        }

        return i;
    }

    @Override
    public void startOpen(PlayerEntity p_174889_1_) {
        if (!p_174889_1_.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }

            ++this.openCount;
            BlockState blockstate = this.getBlockState();
            boolean flag = blockstate.getValue(PantryBlock.OPEN);
            if (!flag) {
                this.playSound(blockstate, SoundEvents.CHEST_OPEN);
                this.updateBlockState(blockstate, true);
            }

            this.scheduleRecheck();
        }
    }

    private void scheduleRecheck() {
        this.level.getBlockTicks().scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 5);
    }

    private void updateBlockState(BlockState p_213963_1_, boolean p_213963_2_) {
        this.level.setBlock(this.getBlockPos(), p_213963_1_.setValue(PantryBlock.OPEN, p_213963_2_), 3);
    }

    private void playSound(BlockState p_213965_1_, SoundEvent p_213965_2_) {
        Vector3i vector3i = p_213965_1_.getValue(PantryBlock.FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5D + (double)vector3i.getX() / 2.0D;
        double d1 = (double)this.worldPosition.getY() + 0.5D + (double)vector3i.getY() / 2.0D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D + (double)vector3i.getZ() / 2.0D;
        this.level.playSound(null, d0, d1, d2, p_213965_2_, SoundCategory.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 1.5F);
    }
    private final LazyOptional<ItemStackHandler> INVENTORY = LazyOptional.of(()->this.inventory);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return INVENTORY.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i=0; i<this.inventory.getSlots();i++){
            if(!this.inventory.getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return this.inventory.getStackInSlot(p_70301_1_);
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return this.inventory.extractItem(p_70298_1_,p_70298_2_, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        this.inventory.setStackInSlot(p_70304_1_,ItemStack.EMPTY);
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
        this.inventory.setStackInSlot(p_70299_1_,p_70299_2_);
    }

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return p_70300_1_.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        for(int i=0; i<this.inventory.getSlots();i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void stopOpen(PlayerEntity p_174886_1_) {
        if (!p_174886_1_.isSpectator()) {
            --this.openCount;
        }

    }

    public static class ContainerProvider implements INamedContainerProvider{

        private final TranslationTextComponent name;
        private final BlockPos pos;
        private final World world;
        public ContainerProvider(World world, BlockPos pos, TranslationTextComponent text){
            this.name = text;
            this.world = world;
            this.pos = pos;
        }

        @Nonnull
        @Override
        public ITextComponent getDisplayName() {
            return this.name;
        }

        @Nullable
        @Override
        public Container createMenu(int p_createMenu_1_, @Nonnull PlayerInventory p_createMenu_2_, @Nonnull PlayerEntity p_createMenu_3_) {
            TileEntity te = this.world.getBlockEntity(pos);
            if(te instanceof TileEntityPantry) {
                return new ContainerPantry(p_createMenu_1_, p_createMenu_2_, (IInventory) te, IWorldPosCallable.create(world, pos));
            }
            else{
                return null;
            }
        }
    }
}
