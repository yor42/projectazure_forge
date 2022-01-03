package com.yor42.projectazure.gameobject.items.rigging;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.rigging.modelCVRiggingDefault;
import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class ItemRiggingCVDefault extends ItemRiggingCV{

    public ItemRiggingCVDefault(Properties properties, int HP) {
        super(properties, HP);
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
        return 4;
    }

    @Override
    public int getTorpedoSlotCount() {
        return 0;
    }

    @Override
    public int getHangerSlots() {
        return 3;
    }

    @Override
    public int getFuelTankCapacity() {
        return 5000;
    }

    @Override
    public AnimatedGeoModel getModel() {
        return new modelCVRiggingDefault();
    }

    @Override
    public void RenderRigging(GeoModelProvider<?> entityModel, ItemStack Rigging, AbstractEntityCompanion entitylivingbaseIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        GeoModelProvider modelRiggingProvider = this.getModel();
        IBone hostbone = entityModel.getModel(entityModel.getModelLocation(null)).getBone("Body").get();
        if (entityModel.getModel(entityModel.getModelLocation(null)).getBone("Body").isPresent()) {
            matrixStackIn.translate(hostbone.getPositionX() / 16, (hostbone.getPositionY() + 45) / 16, hostbone.getPositionZ() / 16);
        }
        RenderType type = RenderType.getEntitySmoothCutout(modelRiggingProvider.getTextureLocation(null));
        GeoModel riggingmodel = modelRiggingProvider.getModel(modelRiggingProvider.getModelLocation(null));
        this.render(riggingmodel, entitylivingbaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();

        IMultiInventory inventories = MultiInvUtil.getCap(entitylivingbaseIn.getRigging());

        //gun Renderer
        IItemHandler inventory = inventories.getInventory(enums.SLOTTYPE.SUB_GUN.ordinal());
        ItemStack stack = inventory.getStackInSlot(0);
        if (stack.getItem() instanceof ItemEquipmentBase) {

            matrixStackIn.push();
            RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase) stack.getItem()).getTexture());
            matrixStackIn.translate((23.25 + hostbone.getPositionX()) / 16, (42 + hostbone.getPositionY()) / 16, -(2 + hostbone.getPositionZ()) / 16);
            GeoModel EquipmentModel = ((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModelLocation(null));
            EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian(entitylivingbaseIn.rotationYawHead - entitylivingbaseIn.renderYawOffset));
            EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(entitylivingbaseIn.rotationPitch, 7.5F, -12.5F, false, true));
            this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.pop();
        }

        stack = inventory.getStackInSlot(1);
        if (stack.getItem() instanceof ItemEquipmentBase) {

            matrixStackIn.push();
            RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase) stack.getItem()).getTexture());
            matrixStackIn.translate(-(23.25 + hostbone.getPositionX()) / 16, (42 + hostbone.getPositionY()) / 16, -(2 + hostbone.getPositionZ()) / 16);
            GeoModel EquipmentModel = ((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModelLocation(null));
            EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian(entitylivingbaseIn.rotationYawHead - entitylivingbaseIn.renderYawOffset));
            EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(entitylivingbaseIn.rotationPitch, 7.5F, -12.5F, false, true));
            this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.pop();
        }

    }
}
