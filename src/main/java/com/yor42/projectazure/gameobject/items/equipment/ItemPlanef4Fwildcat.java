package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemPlanef4Fwildcat extends ItemEquipmentPlaneBase{
    public ItemPlanef4Fwildcat(Properties properties, int maxHP) {
        super(properties, maxHP);
    }

    @Override
    public float getAttackDamage() {
        return 15;
    }

    @Override
    public EntityType<? extends AbstractEntityPlanes> getEntityType() {
        return registerManager.PLANEF4FWildCat;
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
        return enums.PLANE_TYPE.FIGHTER;
    }

    @Override
    public int getMaxOperativeTime() {
        return 1200;
    }


}
