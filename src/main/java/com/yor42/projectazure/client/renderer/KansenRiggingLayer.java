package com.yor42.projectazure.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import com.yor42.projectazure.gameobject.items.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.ItemRiggingDD;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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

public class KansenRiggingLayer extends GeoLayerRenderer<EntityAyanami> implements IGeoRenderer {

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

    public KansenRiggingLayer(IGeoRenderer<EntityAyanami> entityRendererIn) {
        super(entityRendererIn);
    }


    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityAyanami entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.getRigging() != ItemStack.EMPTY) {

            matrixStackIn.push();
            this.modelRiggingProvider = ((ItemRiggingBase) entitylivingbaseIn.getRigging().getItem()).getModel();

            if(getEntityModel().getModel(getEntityModel().getModelLocation(null)).getBone("Body").isPresent()){
                IBone hostbone = getEntityModel().getModel(getEntityModel().getModelLocation(null)).getBone("Body").get();
                matrixStackIn.translate(hostbone.getPositionX()/16, hostbone.getPositionY()/16, hostbone.getRotationZ()/16);
            }
            RenderType type = RenderType.getEntitySmoothCutout(((ItemRiggingBase) entitylivingbaseIn.getRigging().getItem()).getTexture());
            render(this.modelRiggingProvider.getModel(this.modelRiggingProvider.getModelLocation(this.modelRiggingProvider)), entitylivingbaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.pop();

            if(entitylivingbaseIn.getRigging().getTag() != null) {
                matrixStackIn.push();
                GeoModel riggingmodel = this.modelRiggingProvider.getModel(this.modelRiggingProvider.getModelLocation(this.modelRiggingProvider));
                ItemStackHandler Equipments = new ItemStackHandler();
                Equipments.deserializeNBT(entitylivingbaseIn.getRigging().getTag().getCompound("Inventory"));

                if (entitylivingbaseIn.getRigging().getItem() == registerItems.DD_DEFAULT_RIGGING.get()){
                    if(Equipments.getStackInSlot(3) != ItemStack.EMPTY){

                        RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase)Equipments.getStackInSlot(3).getItem()).getTexture());
                        matrixStackIn.translate(11.5/16, 21.5/16, 11.5/16);
                        matrixStackIn.rotate(new Quaternion(0, 0, -90, true));
                        GeoModel EquipmentModel = ((ItemEquipmentBase)Equipments.getStackInSlot(3).getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) Equipments.getStackInSlot(3).getItem()).getEquipmentModel().getModelLocation(null));
                        EquipmentModel.getBone("MountX").get().setRotationY(-0.75F-MathUtil.DegreeToRadian(entitylivingbaseIn.rotationPitch));
                        render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                    }
                }

                matrixStackIn.pop();
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
