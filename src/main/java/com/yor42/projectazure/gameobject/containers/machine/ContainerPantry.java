package com.yor42.projectazure.gameobject.containers.machine;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.blocks.PantryBlock;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.setup.register.registerManager.PANTRY_CONTAINER;

public class ContainerPantry extends Container {

    private final IWorldPosCallable access;
    private IInventory inv;

    public ContainerPantry(int p_i50105_2_, PlayerInventory playerInv,IInventory inv, final IWorldPosCallable access) {
        super(PANTRY_CONTAINER.get(), p_i50105_2_);
        this.access = access;
        this.inv = inv;
        inv.startOpen(playerInv.player);

        for(int j = 0; j < 6; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot( this.inv, k + j * 9, 8 + k * 18, 18 + j * 18){
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack p_75214_1_) {
                        Food food = p_75214_1_.getItem().getFoodProperties();
                        if (food == null) {
                            return false;
                        }

                        if (food.getEffects().isEmpty()) {
                            return true;
                        } else {
                            for (Pair<EffectInstance, Float> effect : food.getEffects()) {
                                if (effect.getFirst().getEffect().getCategory() == EffectType.HARMFUL) {
                                    return false;
                                }
                            }
                        }

                        return true;
                    }
                });
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInv, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + 36));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInv, i1, 8 + i1 * 18, 161 + 36));
        }
    }

    public ContainerPantry(int i, PlayerInventory inventory, PacketBuffer buffer) {
        this(i, inventory,new Inventory(54){

            @Override
            public boolean canPlaceItem(int p_94041_1_, ItemStack p_94041_2_) {
                Food food = p_94041_2_.getItem().getFoodProperties();
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
            }}, IWorldPosCallable.NULL);
    }

    public IInventory getInventory(){
        return this.inv;
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity p_75145_1_) {
        return stillValid(this.access, p_75145_1_);
    }

    protected static boolean stillValid(IWorldPosCallable p_216963_0_, PlayerEntity p_216963_1_) {
        return p_216963_0_.evaluate((p_216960_2_, p_216960_3_) -> p_216960_2_.getBlockState(p_216960_3_).getBlock() instanceof PantryBlock && p_216963_1_.distanceToSqr((double) p_216960_3_.getX() + 0.5D, (double) p_216960_3_.getY() + 0.5D, (double) p_216960_3_.getZ() + 0.5D) <= 64.0D, true);
    }

    public void removed(@Nonnull PlayerEntity p_75134_1_) {
        super.removed(p_75134_1_);
        this.inv.stopOpen(p_75134_1_);
    }

    @Nonnull
    public ItemStack quickMoveStack(@Nonnull PlayerEntity p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_82846_2_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_82846_2_ < 54) {
                if (!this.moveItemStackTo(itemstack1, 54, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 54, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}
