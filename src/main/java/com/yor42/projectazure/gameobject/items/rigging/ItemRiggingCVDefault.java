package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.client.model.rigging.modelCVRiggingDefault;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ItemRiggingCVDefault extends ItemRiggingBase{

    int FIRELEFT = 0;
    int FIRERIGHT = 1;

    public ItemRiggingCVDefault(Properties properties, int maingunslotslots, int subgunslots, int aaslots, int torpedoslots, int hangerslots, int utilityslots,int fuelcapccity, int HP) {
        super(properties, maingunslotslots, subgunslots, aaslots, torpedoslots, hangerslots, utilityslots, fuelcapccity, HP);
        this.validclass = enums.shipClass.AircraftCarrier;
    }

    @Override
    public AnimatedGeoModel getModel() {
        return new modelCVRiggingDefault();
    }

    @Override
    public void applyEquipmentCustomRotation(ItemStack equipment, GeoModel EquipmentModel, enums.SLOTTYPE slottype, int index, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(slottype == enums.SLOTTYPE.SUB_GUN){
            EquipmentModel.getBone("MountX").ifPresent((bone)->bone.setRotationY(-MathUtil.DegreeToRadian(netHeadYaw)));
            EquipmentModel.getBone("Barrel").ifPresent((bone)->bone.setRotationX(MathUtil.LimitAngleMovement(headPitch, 7.5F, -12.5F, false, true)));
        }
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, CONTROLLER_NAME);
    }
}
