package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.client.model.equipments.modelEquipmentTorpedo533mm;
import com.yor42.projectazure.libs.enums;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public class ItemEquipmentTorpedo533Mm extends ItemEquipmentTorpedo implements IAnimatable {
    public ItemEquipmentTorpedo533Mm(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.isreloadable = false;
        this.MaxAmmoCap = 4;
        this.firedelay = 639;
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

    @Override
    public void applyEquipmentCustomRotation(ItemStack equipment, GeoModel EquipmentModel, enums.SLOTTYPE slottype, int index, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        int AmmoCount = getRemainingAmmo(equipment);
        EquipmentModel.getBone("torpedo4").ifPresent((bone) -> bone.setHidden(AmmoCount < 4));
        EquipmentModel.getBone("torpedo3").ifPresent((bone) -> bone.setHidden(AmmoCount < 3));
        EquipmentModel.getBone("torpedo2").ifPresent((bone) -> bone.setHidden(AmmoCount < 2));
        EquipmentModel.getBone("torpedo1").ifPresent((bone) -> bone.setHidden(AmmoCount < 1));
    }
}
