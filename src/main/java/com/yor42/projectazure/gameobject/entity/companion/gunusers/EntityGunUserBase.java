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

}
