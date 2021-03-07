package com.yor42.projectazure.gameobject.entity.companion.gunusers;

import com.yor42.projectazure.gameobject.containers.ContainerBAInventory;
import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

public abstract class EntityGunUserBase extends AbstractEntityCompanion {

    private ItemStackHandler Inventory = new ItemStackHandler(12);
    private ItemStackHandler AmmoStorage = new ItemStackHandler(8){
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
        compound.put("inventory", this.getInventory().serializeNBT());
        compound.put("ammoInv", this.getAmmoStorage().serializeNBT());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.Inventory.deserializeNBT(compound.getCompound("Inventory"));
        this.getAmmoStorage().deserializeNBT(compound.getCompound("ammoInv"));
    }

    @Override
    public boolean canUseRigging() {
        return false;
    }
}
