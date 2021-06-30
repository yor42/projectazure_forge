package com.yor42.projectazure.gameobject.entity.companion.gunusers;

import com.yor42.projectazure.gameobject.containers.entity.ContainerBAInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;

public abstract class EntityGunUserBase extends AbstractEntityCompanion {

    protected EntityGunUserBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.AmmoStorage.setSize(8);
    }

    @Override
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui((ServerPlayerEntity) player, new ContainerBAInventory.Supplier(this));
    }

    public ItemStackHandler getInventory() {
        return this.Inventory;
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("ammoInv", this.getAmmoStorage().serializeNBT());
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
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
    public void livingTick() {
        super.livingTick();

        if(this.getLastAttackedEntityTime()>200 && this.getGunAmmoCount() <= 0){
            this.setReloadDelay();
        }

    }

    public void reloadAmmo(){
        ItemGunBase gun = (ItemGunBase) this.getGunStack().getItem();
        ItemStack MagStack = this.HasRightMagazine(((ItemGunBase) this.getGunStack().getItem()).getCalibur());
        int i;
        if (gun.getRoundsPerReload() > 0) {
            i = Math.min(gun.getRoundsPerReload(), getRemainingAmmo(MagStack));
        } else {
            i = Math.min(gun.getMaxAmmo(), getRemainingAmmo(MagStack));
        }
        addAmmo(this.getGunStack(), i);
        MagStack.shrink(1);
        ItemStack EmptyMag = new ItemStack(((ItemGunBase) this.getGunStack().getItem()).getMagItem());
        emptyAmmo(EmptyMag);

        //Return Empty Magazine for Reloading
        if(!this.addStackToInventory(EmptyMag)){
            if(!this.addStackToAmmoStorage(EmptyMag)){
                ItemEntity itemEntity = new ItemEntity(this.getEntityWorld(), this.getPosX(), this.getPosY(), this.getPosZ(), EmptyMag);
                this.getEntityWorld().addEntity(itemEntity);
            }
        }
    }

    public short getGunAmmoCount(){
        return getGunAmmo(this.getGunStack());
    }

    public static short getGunAmmo(ItemStack stack){
        if(stack.getItem() instanceof ItemGunBase) {
            CompoundNBT compound = stack.getOrCreateTag();
            return compound.getShort("ammo");
        }
        else return 0;
    }

    public ItemStack getGunStack(){
        if(this.getHeldItem(this.getValidGunHand()).getItem() instanceof ItemGunBase){
            return this.getHeldItem(this.getValidGunHand());
        }
        return ItemStack.EMPTY;
    }

    private Hand getValidGunHand(){
        if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){
            return Hand.MAIN_HAND;
        }
        else
            return Hand.OFF_HAND;
    }

    @Override
    public boolean canUseGun() {
        return true;
    }
}
