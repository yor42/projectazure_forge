package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemPlanef4fwildcat extends ItemEquipmentPlaneBase{
    public ItemPlanef4fwildcat(Properties properties, int maxHP) {
        super(properties, maxHP);
    }

    @Override
    public EntityType<? extends AbstractEntityPlanes> getEntityType() {
        return null;
    }

    @Override
    public AnimatedGeoModel getEquipmentModel() {
        return null;
    }

    @Override
    public int getreloadTime() {
        return 200;
    }

    @Override
    public enums.PLANE_TYPE getType() {
        //I know that this is fighter plane. this is for debugging
        return enums.PLANE_TYPE.TORPEDO_BOMBER;
    }

    @Override
    public int getMaxOperativeTime() {
        return 1200;
    }


}
