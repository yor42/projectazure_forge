package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.blocks.PantryBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerPantry;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class TileEntityPantry extends BlockEntity implements Container {
    private int openCount;
    public TileEntityPantry() {
        super(registerTE.PANTRY.get());
    }

    @Nonnull
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("inventory", this.inventory.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void load(@Nonnull BlockState p_230337_1_, @Nonnull CompoundTag compoundNBT) {
        super.load(p_230337_1_, compoundNBT);
        this.inventory.deserializeNBT(compoundNBT.getCompound("inventory"));
    }

    private final ItemStackHandler inventory = new ItemStackHandler(54){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            FoodProperties food = stack.getItem().getFoodProperties();
            if(food == null){
                return false;
            }

            if(food.getEffects().isEmpty()){
                return true;
            }
            else{
                for(Pair<MobEffectInstance, Float> effect : food.getEffects()){
                    if(effect.getFirst().getEffect().getCategory() == MobEffectCategory.HARMFUL){
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

    public static int getOpenCount(Level p_213976_0_, TileEntityPantry p_213976_1_, int p_213976_2_, int p_213976_3_, int p_213976_4_) {
        int i = 0;

        for(Player playerentity : p_213976_0_.getEntitiesOfClass(Player.class, new AABB((float)p_213976_2_ - 5.0F, (float)p_213976_3_ - 5.0F, (float)p_213976_4_ - 5.0F, (float)(p_213976_2_ + 1) + 5.0F, (float)(p_213976_3_ + 1) + 5.0F, (float)(p_213976_4_ + 1) + 5.0F))) {
            if (playerentity.containerMenu instanceof ContainerPantry) {
                Container iinventory = ((ContainerPantry)playerentity.containerMenu).getInventory();
                if (iinventory == p_213976_1_) {
                    ++i;
                }
            }
        }

        return i;
    }

    @Override
    public void startOpen(Player p_174889_1_) {
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
        Vec3i vector3i = p_213965_1_.getValue(PantryBlock.FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5D + (double)vector3i.getX() / 2.0D;
        double d1 = (double)this.worldPosition.getY() + 0.5D + (double)vector3i.getY() / 2.0D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D + (double)vector3i.getZ() / 2.0D;
        this.level.playSound(null, d0, d1, d2, p_213965_2_, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 1.5F);
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
    public boolean stillValid(Player p_70300_1_) {
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

    public void stopOpen(Player p_174886_1_) {
        if (!p_174886_1_.isSpectator()) {
            --this.openCount;
        }

    }

    public static class ContainerProvider implements MenuProvider{

        private final TranslatableComponent name;
        private final BlockPos pos;
        private final Level world;
        public ContainerProvider(Level world, BlockPos pos, TranslatableComponent text){
            this.name = text;
            this.world = world;
            this.pos = pos;
        }

        @Nonnull
        @Override
        public Component getDisplayName() {
            return this.name;
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int p_createMenu_1_, @Nonnull Inventory p_createMenu_2_, @Nonnull Player p_createMenu_3_) {
            BlockEntity te = this.world.getBlockEntity(pos);
            if(te instanceof TileEntityPantry) {
                return new ContainerPantry(p_createMenu_1_, p_createMenu_2_, (Container) te, ContainerLevelAccess.create(world, pos));
            }
            else{
                return null;
            }
        }
    }
}
