package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.client.renderer.items.CVDefaultRiggingRenderer;
import com.yor42.projectazure.client.renderer.items.ItemPlanef4fWildcatRenderer;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.function.Consumer;

public class ItemPlanef4Fwildcat extends ItemEquipmentPlaneBase{
    public ItemPlanef4Fwildcat(Properties properties, int maxHP) {
        super(properties, maxHP, 15);
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

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new ItemPlanef4fWildcatRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }


}
