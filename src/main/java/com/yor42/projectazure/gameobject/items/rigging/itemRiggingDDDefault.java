package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.gameobject.capability.RiggingInventoryCapability;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class itemRiggingDDDefault extends ItemRiggingDD implements IAnimatable {

    public itemRiggingDDDefault(Properties properties, int HP) {
        super(properties, HP);
    }


    @Override
    public AnimatedGeoModel getModel() {
        return new modelDDRiggingDefault();
    }

    @Override
    public void onUpdate(ItemStack stack) {
        if(stack.getItem() instanceof ItemRiggingDD){
            ItemStackHandler equipment = new RiggingInventoryCapability(stack).getEquipments();

            for(int j = 0; j<equipment.getSlots(); j++){
                if(equipment.getStackInSlot(j).getItem() instanceof ItemEquipmentBase) {
                    ItemEquipmentBase item = (ItemEquipmentBase) equipment.getStackInSlot(j).getItem();
                    item.onUpdate(equipment.getStackInSlot(j));
                }
            }
        }
    }

    @Override
    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event)
    {
        return PlayState.STOP;
    }

    @Override
    public int getGunSlotCount() {
        return 2;
    }

    @Override
    public int getAASlotCount() {
        return 1;
    }

    @Override
    public int getTorpedoSlotCount() {
        return 3;
    }
}
