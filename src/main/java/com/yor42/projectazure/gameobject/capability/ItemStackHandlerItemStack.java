package com.yor42.projectazure.gameobject.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStackHandlerItemStack implements IItemHandler, ICapabilityProvider {

    /*
    Yo dawg, I heard you like Itemstack so I made Itemstack that holds Itemstack so you can ItemStack while you ItemStack.
    Okay I'll stop.
     */

    public static final String ITEMSTACKHANDLER_NBTKEY = "Inventory";

    private final LazyOptional<IItemHandler> inventory = LazyOptional.of(() ->this);
    protected ItemStack container;
    protected int size;

    public ItemStackHandlerItemStack(ItemStack container, int size){
        this.container = container;
        this.setSize(size);
    }

    public ItemStack getContainerItemStack(){
        return this.container;
    }

    @Override
    public int getSlots() {
        CompoundTag nbt = this.getContainerNBT();
        return nbt.getInt("Size");
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        CompoundTag nbt = this.getContainerNBT();
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        CompoundTag itemTags = tagList.getCompound(slot);
        if(slot == itemTags.getInt("Slot"))
            return ItemStack.of(itemTags);
        else
        {
            //in case of item isnt saved in order, doubt it will ever happen, but as an failsafe because ItemStack handler checks this too.
            for (int i = 0; i < tagList.size(); i++){
                itemTags = tagList.getCompound(i);
                int slotnum = itemTags.getInt("Slot");
                if(slot == slotnum){
                    return ItemStack.of(itemTags);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        validateSlotIndex(slot);
        CompoundTag nbt = this.getContainerNBT();
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        CompoundTag itemTags = tagList.getCompound(slot);
        if(slot == itemTags.getInt("Slot")){
            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Slot", slot);
            stack.save(itemTag);
            tagList.set(slot, itemTag);
        }
        else
        {
            //in case of item isnt saved in order, doubt it will ever happen, but as an failsafe because ItemStack handler checks this too.
            for (int i = 0; i < tagList.size(); i++){
                itemTags = tagList.getCompound(i);
                int slotnum = itemTags.getInt("Slot");
                if(slot == slotnum){
                    CompoundTag itemTag = new CompoundTag();
                    itemTag.putInt("Slot", slot);
                    stack.save(itemTag);
                    tagList.set(slot, itemTag);
                }
            }
        }

        onContentsChanged(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.getStackInSlot(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty())
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate)
        {
            ItemStack stacktoSave;
            if (existing.isEmpty())
            {
                stacktoSave = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
            }
            else
            {
                existing.grow(reachedLimit ? limit : stack.getCount());
                stacktoSave = existing;
            }
            this.setStackInSlot(slot, stacktoSave);
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = getStackInSlot(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract)
        {
            if (!simulate)
            {
                this.setStackInSlot(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
                return existing;
            }
            else
            {
                return existing.copy();
            }
        }
        else
        {
            if (!simulate)
            {
                this.setStackInSlot(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }
    @Override
    public int getSlotLimit(int slot)
    {
        return 64;
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack)
    {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return true;
    }

    private void setSize(int newsize) {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < newsize; i++){
            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Slot", i);
            ItemStack.EMPTY.save(itemTag);
            nbtTagList.add(itemTag);
        }
        CompoundTag nbt = this.getContainerNBT();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", newsize);
    }

    private CompoundTag getContainerNBT(){
        return this.container.getOrCreateTag().getCompound(ITEMSTACKHANDLER_NBTKEY);
    }

    protected void validateSlotIndex(int slot)
    {
        ListTag tagList = getContainerNBT().getList("Items", Tag.TAG_COMPOUND);
        if (slot>this.size || slot>=tagList.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + Math.max(this.size, tagList.size()) + ")");
    }

    protected void onContentsChanged(int slot)
    {

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, inventory);
    }
}
