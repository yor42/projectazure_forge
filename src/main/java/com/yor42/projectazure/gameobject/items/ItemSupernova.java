package com.yor42.projectazure.gameobject.items;

import com.tac.guns.common.Gun;
import com.tac.guns.util.Process;
import com.yor42.projectazure.client.renderer.gun.SupernovaGeoRenderer;
import com.yor42.projectazure.client.renderer.items.BBDefaultRiggingRenderer;
import com.yor42.projectazure.interfaces.IChargeFire;
import com.yor42.projectazure.mixin.ModuleAccessor;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ItemSupernova extends GeoGunItem implements IChargeFire {
    public ItemSupernova(int energycapacity, int energypershot, int idleenergyconsumption, boolean ignoreAmmo, @Nullable SoundEvent safereleasesound, @Nullable SoundEvent safesetsound, @Nullable SoundEvent NoAmmoSound, Process<Item.Properties> properties) {
        super(energycapacity, energypershot, idleenergyconsumption, ignoreAmmo, safereleasesound, safesetsound, NoAmmoSound, properties);
    }

    @Override
    public int getMaxChargeTime() {
        return 60;
    }

    @Nullable
    @Override
    public SoundEvent getChargeSound() {
        return registerSounds.SUPERNOVA_CHARGE;
    }

    @Override
    public Gun getGun() {
        Gun gun = super.getGun();
        ((ModuleAccessor)gun.getModules()).setZoom(null);
        return gun;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new SupernovaGeoRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

}
