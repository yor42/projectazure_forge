package com.yor42.projectazure.gameobject.capability.multiinv;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class MultiInvStackHandlerItemStack extends MultiInvStackHandler {
    private final ItemStack container;
    private final Lazy<NonNullList<ItemStack>> stacks;

    public MultiInvStackHandlerItemStack(ItemStack container, String id, int size) {
        super(id, size);
        this.container = container;
        this.stacks = Lazy.of(() -> {
            NonNullList<ItemStack> stacks = NonNullList.withSize(size, ItemStack.EMPTY);
            CompoundNBT nbt = container.getOrCreateTag();
            if (nbt.contains("Inventory_" + id)) {
                ListNBT items = nbt.getCompound("Inventory_" + id).getList("Items", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < items.size(); i++) {
                    CompoundNBT item = items.getCompound(i);
                    int slot = item.getInt("Slot");
                    if (slot >= 0 && slot < stacks.size()) {
                        stacks.set(slot, ItemStack.read(item));
                    }
                }
            }
            return stacks;
        });
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        validateSlotIndex(slot);
        this.stacks.get().set(slot, stack);
        onContentsChanged(slot);
    }

    @Override
    public int getSlots() {
        return stacks.get().size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return this.stacks.get().get(slot);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get().get(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.stacks.get().set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get().get(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.stacks.get().set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                this.stacks.get().set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    protected void validateSlotIndex(int slot) {
        NonNullList<ItemStack> stacks = this.stacks.get();
        if (slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }

    protected void onLoad() {

    }

    protected void onContentsChanged(int slot) {
        this.serializeNBT(container.getOrCreateTag());
    }
}
