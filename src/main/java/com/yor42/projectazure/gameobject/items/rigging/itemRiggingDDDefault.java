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

    public itemRiggingDDDefault(Properties properties, int HP) {
        super(properties, HP);
        this.validclass = enums.shipClass.Destroyer;
    }


    @Override
    public AnimatedGeoModel getModel() {
        return new modelDDRiggingDefault();
    }

    @Override
    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event)
    {
        return PlayState.STOP;
    }

    @Override
    public int getMainGunSlotCount() {
        return 0;
    }

    @Override
    public int getSubGunSlotCount() {
        return 2;
    }

    @Override
    public int getAASlotCount() {
        return 1;
    }

    @Override
    public int getFuelTankCapacity() {
        return 5000;
    }

    @Override
    public int getTorpedoSlotCount() {
        return 3;
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

    @Override
    public void RenderEquipments(ItemStack Rigging, GeoModel riggingModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMultiInventory inventories = MultiInvUtil.getCap(Rigging);
        for(enums.SLOTTYPE slottype : enums.SLOTTYPE.values()){
            IItemHandler inventory = inventories.getInventory(slottype.ordinal());
            for(int i=0; i<inventory.getSlots(); i++){
                ItemStack equipment = inventory.getStackInSlot(i);
                Item item = equipment.getItem();
                if(equipment.isEmpty() || !(item instanceof ItemEquipmentBase)){
                    continue;
                }
                ItemEquipmentBase equipmentItem = (ItemEquipmentBase) item;

                int finalI = i;
                riggingModel.getBone(slottype.getName()+(i+1)).ifPresent((bone)->{
                    matrixStackIn.pushPose();

                    ArrayList<GeoBone> bonetree = new ArrayList<>();
                    GeoBone bone1 = bone;
                    while (true){
                        bonetree.add(bone1);
                        if(bone1.getParent()!=null) {
                            bone1 = bone1.getParent();
                            continue;
                        }
                        break;
                    }

                    for(int j=bonetree.size()-1; j>=0; j--){
                        preparePositionRotationScale(bonetree.get(j), matrixStackIn);
                    }

                    matrixStackIn.translate(bone.getPivotX()/16, bone.getPivotY()/16, bone.getPivotZ()/16);
                    RenderType renderType = RenderType.entitySmoothCutout(equipmentItem.getTexture());
                    GeoModel EquipmentModel = equipmentItem.getEquipmentModel().getModel((equipmentItem).getEquipmentModel().getModelLocation(null));
                    equipmentItem.applyEquipmentCustomRotation(equipment, EquipmentModel, slottype, finalI, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    this.applyEquipmentCustomRotation(equipment, EquipmentModel, slottype, finalI, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    this.render(EquipmentModel, equipmentItem, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                    matrixStackIn.popPose();
                });


            }
        }
    }
}
