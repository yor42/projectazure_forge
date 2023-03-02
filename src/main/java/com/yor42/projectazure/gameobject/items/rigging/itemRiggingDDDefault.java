package com.yor42.projectazure.gameobject.items.rigging;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.enums.SLOTTYPE.SUB_GUN;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.PLAY_ONCE;

public class itemRiggingDDDefault extends ItemRiggingBase implements IAnimatable {

    int FIRELEFT = 0;
    int FIRERIGHT = 1;

    public itemRiggingDDDefault(Properties properties, int maingunslotslots, int subgunslots, int aaslots, int torpedoslots, int hangerslots, int utilityslots,int fuelcapccity,  int HP) {
        super(properties, maingunslotslots, subgunslots, aaslots, torpedoslots, hangerslots, utilityslots, fuelcapccity, HP);
        this.validclass = enums.shipClass.Destroyer;
    }

    @Override
    public AnimatedGeoModel getModel() {
        return new modelDDRiggingDefault();
    }

    @Override
    public void applyEquipmentCustomRotation(ItemStack equipment, GeoModel EquipmentModel, enums.SLOTTYPE slottype, int index, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        switch (slottype){
            case SUB_GUN:
                switch (index){
                    case 0:
                        EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY(-MathUtil.DegreeToRadian(headPitch)));
                        EquipmentModel.getBone("Barrel").ifPresent((bone) -> bone.setRotationX(MathUtil.LimitAngleMovement(netHeadYaw, 7.5F, -12.5F, true, true)));
                        break;
                    case 1:
                        EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY(MathUtil.DegreeToRadian(headPitch)));
                        EquipmentModel.getBone("Barrel").ifPresent((bone) -> bone.setRotationX(-MathUtil.LimitAngleMovement(netHeadYaw, 7.5F, -12.5F, true, true)));
                        break;
                }
                break;
            case TORPEDO:
                switch (index){
                    case 2:
                    case 0:
                        EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY(-MathUtil.DegreeToRadian(headPitch)));
                        break;
                    case 1:
                        EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY((MathUtil.LimitAngleMovement(-netHeadYaw, 45F, -45F, false, true))));
                        break;
                }
        }

    }

    @Nullable
    @Override
    public Pair<String, Integer> getFireAnimationname(enums.SLOTTYPE slottype, int index) {
        if(slottype == SUB_GUN){
            if(index == 0){
                return new Pair<>("Fire_Right", FIRERIGHT);
            }
            else{
                return new Pair<>("Fire_Left", FIRELEFT);
            }
        }

        return super.getFireAnimationname(slottype, index);
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, CONTROLLER_NAME);
        if(state == FIRELEFT){
            controller.setAnimation(new AnimationBuilder().addAnimation("Fire_Left", PLAY_ONCE));
        }
        else if(state == FIRERIGHT){
            controller.setAnimation(new AnimationBuilder().addAnimation("Fire_Right", PLAY_ONCE));
        }
    }

}
