package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getPlaneFuel;

public abstract class EntityKansenAircraftCarrier extends EntityKansenBase {
    protected EntityKansenAircraftCarrier(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setShipClass(enums.shipClass.AircraftCarrier);
    }

    public void LaunchPlane(ItemStack planestack, AbstractEntityPlanes plane, LivingEntity target, IItemHandlerModifiable hanger, int hangerIndex){
        plane.setOwner(this);
        plane.setPos(this.getX(), this.getY() + 2, this.getZ());
        plane.setHealth(getCurrentHP(planestack));
        plane.setPayloads(planestack.getOrCreateTag().getInt("armDelay") <= 0);
        plane.setMaxOperativetime(getPlaneFuel(planestack));
        plane.setTarget(target);
        this.getCommandSenderWorld().addFreshEntity(plane);
        hanger.setStackInSlot(hangerIndex, ItemStack.EMPTY);
    }
}
