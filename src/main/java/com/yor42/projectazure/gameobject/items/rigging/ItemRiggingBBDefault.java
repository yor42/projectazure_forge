package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.client.model.rigging.modelBBRiggingDefault;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemRiggingBBDefault extends ItemRiggingBase{
    public ItemRiggingBBDefault(Properties properties, int maingunslotslots, int subgunslots, int aaslots, int torpedoslots, int hangerslots, int utilityslots,int fuelcapccity, int HP) {
        super(properties, maingunslotslots, subgunslots, aaslots, torpedoslots, hangerslots, utilityslots, fuelcapccity, HP);
        this.validclass = enums.shipClass.Battleship;
    }
    @Override
    public AnimatedGeoModel getModel() {
        return new modelBBRiggingDefault();
    }


    @Override
    public void applyEquipmentCustomRotation(ItemStack equipment, GeoModel EquipmentModel, enums.SLOTTYPE slottype, int index, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        switch(slottype){
            case MAIN_GUN:
                EquipmentModel.getBone("MountX").ifPresent((bone)->bone.setRotationY(-MathUtil.DegreeToRadian(netHeadYaw)));
                EquipmentModel.getBone("Barrel").ifPresent((bone)->bone.setRotationX(MathUtil.LimitAngleMovement(headPitch, 7.5F, -12.5F, false, true)));
                break;
            case SUB_GUN:
                switch (index){
                    case 0:
                        EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY(MathUtil.DegreeToRadian(headPitch)));
                        EquipmentModel.getBone("Barrel").ifPresent((bone) -> bone.setRotationX(-MathUtil.LimitAngleMovement(netHeadYaw, 7.5F, -12.5F, true, true)));
                        break;
                    case 1:
                        EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY(-MathUtil.DegreeToRadian(headPitch)));
                        EquipmentModel.getBone("Barrel").ifPresent((bone) -> bone.setRotationX(MathUtil.LimitAngleMovement(netHeadYaw, 7.5F, -12.5F, true, true)));
                        break;
                }
                break;
            case TORPEDO:
                EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY((MathUtil.LimitAngleMovement(-netHeadYaw, 45F, -45F, false, true))));
                break;
        }
    }


    @Override
    public void onAnimationSync(int id, int state) {

    }
}
