package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityDrone;
import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

import java.util.UUID;

public abstract class AbstractItemPlaceableDrone extends ItemDestroyable implements IAnimatable, ICraftingTableReloadable {
    private final int AmmoCount;
    private final int maxFuel;
    public AbstractItemPlaceableDrone(Properties properties, int MaxHP, int AmmoCount, int maxFuelmb) {
        super(properties, MaxHP);
        this.AmmoCount = AmmoCount;
        this.maxFuel = maxFuelmb;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        stack.getOrCreateTag().putString("planeUUID", UUID.randomUUID().toString());
    }


    public abstract EntityType<? extends AbstractEntityDrone> getEntityType();

    public abstract int getreloadDelay();

    @Override
    public int getMaxAmmo() {
        return this.AmmoCount;
    }

    @Override
    public enums.AmmoCalibur getCalibur() {
        return enums.AmmoCalibur.DRONE_MISSILE;
    }

    public int getFuelCapacity(){
        return this.maxFuel;
    };

    public void AddInfotoDrone(ItemStack droneItem, AbstractEntityDrone drone){
        CompoundNBT stackCompound = droneItem.getOrCreateTag();
        if(stackCompound.contains("planedata")) {
            drone.readAdditional(stackCompound.getCompound("planedata"));
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        if(!context.getWorld().isRemote() && context.getPlayer() != null && context.getPlayer().isSneaking()){
            AbstractEntityDrone DroneEntity = this.getEntityType().create(context.getWorld());
            ItemStack stack = context.getItem();
            CompoundNBT stackCompound = stack.getOrCreateTag();
            if(DroneEntity != null){
                this.AddInfotoDrone(stack, DroneEntity);
                DroneEntity.setOwner(context.getPlayer());
                DroneEntity.setPosition(context.getPos().getX(), context.getPos().getY()+1, context.getPos().getZ());
                DroneEntity.setHealth(ItemStackUtils.getCurrentHP(stack));
                DroneEntity.setAmmo(this.getMaxAmmo());
                context.getWorld().addEntity(DroneEntity);
                if(!context.getPlayer().isCreative()){
                    stack.shrink(1);
                }
                return ActionResultType.CONSUME;
            }
        }

        return super.onItemUse(context);
    }
}
