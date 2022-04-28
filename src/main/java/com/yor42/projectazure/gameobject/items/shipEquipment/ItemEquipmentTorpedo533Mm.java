package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.client.model.equipments.modelEquipmentTorpedo533mm;
import com.yor42.projectazure.client.renderer.equipment.equipment533mmTorpedoRenderer;
import com.yor42.projectazure.client.renderer.items.CVDefaultRiggingRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.function.Consumer;

public class ItemEquipmentTorpedo533Mm extends ItemEquipmentTorpedo implements IAnimatable {
    public ItemEquipmentTorpedo533Mm(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.isreloadable = false;
        this.MaxAmmoCap = 4;
        this.firedelay = 639;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new equipment533mmTorpedoRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public AnimatedGeoModel getEquipmentModel() {
        return new modelEquipmentTorpedo533mm();
    }

    @Override
    public void onUpdate(ItemStack EquipmentStack, ItemStack RiggingStack) {
        CompoundTag tags = EquipmentStack.getOrCreateTag();
        int delay = tags.getInt("delay");
        if(delay >0)
            delay--;
        tags.putInt("delay", delay);
    }
}
