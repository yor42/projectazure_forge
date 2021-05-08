package com.yor42.projectazure.gameobject.entity.companion.gunusers;

import com.yor42.projectazure.gameobject.containers.entity.ContainerBAInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public abstract class EntityGunUserBase extends AbstractEntityCompanion {

    private ItemStackHandler AmmoStorage = new ItemStackHandler(8){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() instanceof ItemMagazine;
        }

        //TODO check if items are instance of bullets
    };

    protected EntityGunUserBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void openGUI(PlayerEntity player) {
        NetworkHooks.openGui((ServerPlayerEntity) player, new ContainerBAInventory.Supplier(this));
    }

    public ItemStackHandler getInventory() {
        return this.Inventory;
    }

    public ItemStackHandler getAmmoStorage() {
        return this.AmmoStorage;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("ammoInv", this.getAmmoStorage().serializeNBT());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.getAmmoStorage().deserializeNBT(compound.getCompound("ammoInv"));
    }

    @Override
    public boolean canUseRigging() {
        return false;
    }

    public ItemStack HasRightMagazine(enums.AmmoCalibur calibur){
        for(int i=0; i<this.getAmmoStorage().getSlots();i++){
            if(this.getAmmoStorage().getStackInSlot(i).getItem() instanceof ItemMagazine && ((ItemMagazine) this.getAmmoStorage().getStackInSlot(i).getItem()).getCalibur() == calibur){
                return this.getAmmoStorage().getStackInSlot(i);
            }
        }
        return ItemStack.EMPTY;
    }

                                      @Override
    public boolean canUseGun() {
        return true;
    }
}
