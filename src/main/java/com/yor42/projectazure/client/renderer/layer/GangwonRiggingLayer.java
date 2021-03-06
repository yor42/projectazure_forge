package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityGangwon;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingDD;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

public class GangwonRiggingLayer extends GeoLayerRenderer<EntityGangwon> implements IGeoRenderer {

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

    public GangwonRiggingLayer(IGeoRenderer<EntityGangwon> entityRendererIn) {
        super(entityRendererIn);
    }


    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityGangwon entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.getRigging() != ItemStack.EMPTY) {

            matrixStackIn.push();
            this.modelRiggingProvider = ((ItemRiggingBase) entitylivingbaseIn.getRigging().getItem()).getModel();
            int riggingoffset = 28;
            IBone hostbone = getEntityModel().getModel(getEntityModel().getModelLocation(null)).getBone("Body").get();
            if(getEntityModel().getModel(getEntityModel().getModelLocation(null)).getBone("Body").isPresent()){
                matrixStackIn.translate(hostbone.getPositionX()/16, (hostbone.getPositionY()+riggingoffset)/16, (hostbone.getPositionZ()-1)/16);
            }
            RenderType type = RenderType.getEntitySmoothCutout(((ItemRiggingBase) entitylivingbaseIn.getRigging().getItem()).getTexture());
            render(this.modelRiggingProvider.getModel(this.modelRiggingProvider.getModelLocation(this.modelRiggingProvider)), entitylivingbaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.pop();

            if(entitylivingbaseIn.getRigging().getItem() instanceof ItemRiggingBase) {
                ItemStackHandler Equipments = ((ItemRiggingBase) entitylivingbaseIn.getRigging().getItem()).getEquipments(entitylivingbaseIn.getRigging());

                //TODO: Clean this shit up with loop
                /*
                for(int i = 0; i<Equipments.getSlots(); i++){
                    matrixStackIn.push();
                    RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase)Equipments.getStackInSlot(i).getItem()).getTexture());
                    matrixStackIn.translate((21.25+hostbone.getPositionX())/16, (32.5+hostbone.getPositionY()+riggingoffset)/16, -(4+hostbone.getPositionZ())/16);
                    matrixStackIn.rotate(((ItemRiggingBase) entitylivingbaseIn.getRigging().getItem()).getEquipmentRotation()[i]);

                    GeoModel EquipmentModel = ((ItemEquipmentBase)Equipments.getStackInSlot(i).getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) Equipments.getStackInSlot(i).getItem()).getEquipmentModel().getModelLocation(null));
                }
                 */



                if (entitylivingbaseIn.getRigging().getItem() == registerItems.DD_DEFAULT_RIGGING.get()){

                    //gun Renderer
                    if(Equipments.getStackInSlot(0) != ItemStack.EMPTY){

                        matrixStackIn.push();
                        RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase)Equipments.getStackInSlot(0).getItem()).getTexture());
                        matrixStackIn.translate((21.5+hostbone.getPositionX())/16, (-2+hostbone.getPositionY()+riggingoffset)/16, -(5+hostbone.getPositionZ())/16);
                        matrixStackIn.rotate(new Quaternion(0, 0, -90, true));

                        GeoModel EquipmentModel = ((ItemEquipmentBase)Equipments.getStackInSlot(0).getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) Equipments.getStackInSlot(0).getItem()).getEquipmentModel().getModelLocation(null));
                        EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian(entitylivingbaseIn.rotationPitch));
                        EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(-entitylivingbaseIn.getRotationYawHead()-entitylivingbaseIn.renderYawOffset, 7.5F, -12.5F, false, true));
                        render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                        matrixStackIn.pop();
                    }

                    if(Equipments.getStackInSlot(1) != ItemStack.EMPTY){

                        matrixStackIn.push();
                        RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase)Equipments.getStackInSlot(1).getItem()).getTexture());
                        matrixStackIn.translate(-(21.5+hostbone.getPositionX())/16, (-2+hostbone.getPositionY()+riggingoffset)/16, -(5+hostbone.getPositionZ())/16);
                        matrixStackIn.rotate(new Quaternion(0, 0, 90, true));
                        GeoModel EquipmentModel = ((ItemEquipmentBase)Equipments.getStackInSlot(1).getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) Equipments.getStackInSlot(1).getItem()).getEquipmentModel().getModelLocation(null));
                        EquipmentModel.getBone("MountX").get().setRotationY(MathUtil.DegreeToRadian(entitylivingbaseIn.rotationPitch));
                        EquipmentModel.getBone("Barrel").get().setRotationX(-MathUtil.LimitAngleMovement(-entitylivingbaseIn.getRotationYawHead()-entitylivingbaseIn.renderYawOffset, 7.5F, -12.5F, false, true));
                        render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                        matrixStackIn.pop();
                    }

                    //Torpedo Renderer
                    if(Equipments.getStackInSlot(3) != ItemStack.EMPTY){
                        if(Equipments.getStackInSlot(3).getItem() == registerItems.EQUIPMENT_TORPEDO_533MM.get()) {
                            matrixStackIn.push();
                            RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase) Equipments.getStackInSlot(3).getItem()).getTexture());
                            matrixStackIn.translate((11.5 + hostbone.getPositionX()) / 16, (-13 + hostbone.getPositionY() + riggingoffset) / 16, (12.5 + hostbone.getPositionZ()) / 16);
                            matrixStackIn.rotate(new Quaternion(0, 0, -90, true));
                            GeoModel EquipmentModel = ((ItemEquipmentBase) Equipments.getStackInSlot(3).getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) Equipments.getStackInSlot(3).getItem()).getEquipmentModel().getModelLocation(null));
                            EquipmentModel.getBone("MountX").get().setRotationY(-0.75F - MathUtil.DegreeToRadian(entitylivingbaseIn.rotationPitch));

                            int AmmoCount = getRemainingAmmo(Equipments.getStackInSlot(3));
                            EquipmentModel.getBone("torpedo4").get().setHidden(AmmoCount<4);
                            EquipmentModel.getBone("torpedo3").get().setHidden(AmmoCount<3);
                            EquipmentModel.getBone("torpedo2").get().setHidden(AmmoCount<2);
                            EquipmentModel.getBone("torpedo1").get().setHidden(AmmoCount<1);

                            render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                            matrixStackIn.pop();
                        }
                    }
                    if(Equipments.getStackInSlot(4) != ItemStack.EMPTY){
                        if(Equipments.getStackInSlot(4).getItem() == registerItems.EQUIPMENT_TORPEDO_533MM.get()) {
                            matrixStackIn.push();
                            RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase) Equipments.getStackInSlot(3).getItem()).getTexture());
                            matrixStackIn.translate((0 + hostbone.getPositionX()) / 16, (-10 + hostbone.getPositionY() + riggingoffset) / 16, (26.75 + hostbone.getPositionZ()) / 16);
                            matrixStackIn.rotate(new Quaternion(90, 180, 0, true));
                            GeoModel EquipmentModel = ((ItemEquipmentBase) Equipments.getStackInSlot(4).getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) Equipments.getStackInSlot(4).getItem()).getEquipmentModel().getModelLocation(null));
                            EquipmentModel.getBone("MountX").get().setRotationY((MathUtil.LimitAngleMovement(-entitylivingbaseIn.getRotationYawHead() + entitylivingbaseIn.prevRotationYaw, 45F, -45F, false, true)));
                            int AmmoCount = getRemainingAmmo(Equipments.getStackInSlot(4));
                            EquipmentModel.getBone("torpedo4").get().setHidden(AmmoCount<4);
                            EquipmentModel.getBone("torpedo3").get().setHidden(AmmoCount<3);
                            EquipmentModel.getBone("torpedo2").get().setHidden(AmmoCount<2);
                            EquipmentModel.getBone("torpedo1").get().setHidden(AmmoCount<1);

                            render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                            matrixStackIn.pop();
                        }
                    }

                    if(Equipments.getStackInSlot(5) != ItemStack.EMPTY){
                        if(Equipments.getStackInSlot(5).getItem() == registerItems.EQUIPMENT_TORPEDO_533MM.get()) {
                            matrixStackIn.push();
                            RenderType renderType = RenderType.getEntitySmoothCutout(((ItemEquipmentBase) Equipments.getStackInSlot(3).getItem()).getTexture());
                            matrixStackIn.translate(-(11.5 + hostbone.getPositionX()) / 16, (-13 + hostbone.getPositionY() + riggingoffset) / 16, (12.5 + hostbone.getPositionZ()) / 16);
                            matrixStackIn.rotate(new Quaternion(0, 0, 90, true));
                            GeoModel EquipmentModel = ((ItemEquipmentBase) Equipments.getStackInSlot(5).getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) Equipments.getStackInSlot(5).getItem()).getEquipmentModel().getModelLocation(null));
                            EquipmentModel.getBone("MountX").get().setRotationY(0.75F + MathUtil.DegreeToRadian(entitylivingbaseIn.rotationPitch));

                            int AmmoCount = getRemainingAmmo(Equipments.getStackInSlot(5));
                            EquipmentModel.getBone("torpedo4").get().setHidden(AmmoCount<4);
                            EquipmentModel.getBone("torpedo3").get().setHidden(AmmoCount<3);
                            EquipmentModel.getBone("torpedo2").get().setHidden(AmmoCount<2);
                            EquipmentModel.getBone("torpedo1").get().setHidden(AmmoCount<1);

                            render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                            matrixStackIn.pop();
                        }
                    }
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
