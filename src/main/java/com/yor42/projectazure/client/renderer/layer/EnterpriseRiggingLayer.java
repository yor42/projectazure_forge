package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.capability.RiggingInventoryCapability;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityEnterprise;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingCVDefault;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingDD;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public class EnterpriseRiggingLayer extends GeoLayerRenderer<EntityEnterprise> implements IGeoRenderer {

    private AnimatedGeoModel modelRiggingProvider;

    static {
        AnimationController.addModelFetcher((IAnimatable object) ->
        {
            if (object instanceof ItemRiggingDD) {
                return ((ItemRiggingBase) object).getModel();
            }
            return null;
        });
    }

    public EnterpriseRiggingLayer(IGeoRenderer<EntityEnterprise> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityEnterprise entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Item item = entitylivingbaseIn.getRigging().getItem();

        boolean flag = item instanceof ItemRiggingBase;
        if (flag) {

            matrixStackIn.push();
            this.modelRiggingProvider = ((ItemRiggingBase) item).getModel();
            IBone hostbone = getEntityModel().getModel(getEntityModel().getModelLocation(null)).getBone("Body").get();
            if(getEntityModel().getModel(getEntityModel().getModelLocation(null)).getBone("Body").isPresent()){
                matrixStackIn.translate(hostbone.getPositionX()/16, (hostbone.getPositionY()+45)/16, hostbone.getPositionZ()/16);
            }
            RenderType type = RenderType.getEntitySmoothCutout(((ItemRiggingBase) entitylivingbaseIn.getRigging().getItem()).getTexture());
            GeoModel riggingmodel = this.modelRiggingProvider.getModel(this.modelRiggingProvider.getModelLocation(null));
            render(riggingmodel, entitylivingbaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.pop();
            ItemStackHandler Equipments = new RiggingInventoryCapability(entitylivingbaseIn.getRigging(), entitylivingbaseIn).getEquipments();

            if (entitylivingbaseIn.getRigging().getItem() instanceof ItemRiggingCVDefault){

                //gun Renderer
                if(Equipments.getStackInSlot(0).getItem() instanceof ItemEquipmentBase){

                    matrixStackIn.push();
                    RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase)Equipments.getStackInSlot(0).getItem()).getTexture());
                    matrixStackIn.translate((23.25+hostbone.getPositionX())/16, (42+hostbone.getPositionY())/16, -(2+hostbone.getPositionZ())/16);
                    GeoModel EquipmentModel = ((ItemEquipmentBase)Equipments.getStackInSlot(0).getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) Equipments.getStackInSlot(0).getItem()).getEquipmentModel().getModelLocation(null));
                    EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian(entitylivingbaseIn.rotationYawHead-entitylivingbaseIn.renderYawOffset));
                    EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(entitylivingbaseIn.rotationPitch, 7.5F, -12.5F, false, true));
                    render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                    matrixStackIn.pop();
                }

                if(Equipments.getStackInSlot(1).getItem() instanceof ItemEquipmentBase){

                    matrixStackIn.push();
                    RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase)Equipments.getStackInSlot(1).getItem()).getTexture());
                    matrixStackIn.translate(-(23.25+hostbone.getPositionX())/16, (42+hostbone.getPositionY())/16, -(2+hostbone.getPositionZ())/16);
                    GeoModel EquipmentModel = ((ItemEquipmentBase)Equipments.getStackInSlot(1).getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) Equipments.getStackInSlot(1).getItem()).getEquipmentModel().getModelLocation(null));
                    EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian(entitylivingbaseIn.rotationYawHead-entitylivingbaseIn.renderYawOffset));
                    EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(entitylivingbaseIn.rotationPitch, 7.5F, -12.5F, false, true));
                    render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                    matrixStackIn.pop();
                }
            }
        }
    }

    @Override
    public GeoModelProvider getGeoModelProvider() {
        return this.modelRiggingProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return this.modelRiggingProvider.getTextureLocation(o);
    }
}
