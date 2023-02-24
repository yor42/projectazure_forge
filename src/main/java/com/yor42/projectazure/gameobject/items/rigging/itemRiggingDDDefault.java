package com.yor42.projectazure.gameobject.items.rigging;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvStackHandler;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.ArrayList;
import java.util.Arrays;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public class itemRiggingDDDefault extends ItemRiggingBase implements IAnimatable {

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
                    case 0:
                        EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY(-MathUtil.DegreeToRadian(headPitch)));
                        break;
                    case 1:
                        EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY((MathUtil.LimitAngleMovement(-netHeadYaw, 45F, -45F, false, true))));
                        break;
                    case 2:
                        EquipmentModel.getBone("MountX").ifPresent((bone) -> bone.setRotationY(MathUtil.DegreeToRadian(headPitch)));
                        break;
                }
        }

    }
}
