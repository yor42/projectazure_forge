package com.yor42.projectazure.gameobject.items.rigging;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

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
    public void RenderRigging(GeoModelProvider<?> entityModel, ItemStack Rigging, AbstractEntityCompanion entitylivingbaseIn, MatrixStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.pushPose();
        GeoModelProvider modelRiggingProvider = this.getModel();
        IBone hostbone = entityModel.getModel(entityModel.getModelLocation(null)).getBone("Body").get();
        if (entityModel.getModel(entityModel.getModelLocation(null)).getBone("Body").isPresent()) {
            matrixStackIn.translate(hostbone.getPositionX() / 16, (hostbone.getPositionY() + 36.6) / 16, hostbone.getPositionZ() / 16);
        }


        RenderType type = RenderType.entitySmoothCutout(modelRiggingProvider.getTextureLocation(null));
        GeoModel riggingmodel = modelRiggingProvider.getModel(modelRiggingProvider.getModelLocation(null));
        this.render(riggingmodel, entitylivingbaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.popPose();
        IMultiInventory inventories = MultiInvUtil.getCap(entitylivingbaseIn.getRigging());

        //gun Renderer
        IItemHandler inventory = inventories.getInventory(enums.SLOTTYPE.SUB_GUN.ordinal());
        ItemStack stack = inventory.getStackInSlot(0);
        if (stack != ItemStack.EMPTY) {

            matrixStackIn.pushPose();
            RenderType renderType = RenderType.entitySmoothCutout(((ItemEquipmentBase) stack.getItem()).getTexture());
            matrixStackIn.translate((21.25 + hostbone.getPositionX()) / 16, (34.6 + hostbone.getPositionY()) / 16, -(4 + hostbone.getPositionZ()) / 16);
            matrixStackIn.mulPose(new Quaternion(0, 0, -90, true));


            GeoModel EquipmentModel = ((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModelLocation(null));
            EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian(entitylivingbaseIn.xRot));
            EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(-(entitylivingbaseIn.getYHeadRot() - entitylivingbaseIn.yBodyRot), 7.5F, -12.5F, false, true));
            this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.popPose();
        }

        stack = inventory.getStackInSlot(1);
        if (stack != ItemStack.EMPTY) {

            matrixStackIn.pushPose();
            RenderType renderType = RenderType.entitySmoothCutout(((ItemEquipmentBase) stack.getItem()).getTexture());
            matrixStackIn.translate(-(21.25 + hostbone.getPositionX()) / 16, (34.6 + hostbone.getPositionY()) / 16, -(4 + hostbone.getPositionZ()) / 16);
            matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));


            GeoModel EquipmentModel = ((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModelLocation(null));
            EquipmentModel.getBone("MountX").get().setRotationY(MathUtil.DegreeToRadian(entitylivingbaseIn.xRot));
            EquipmentModel.getBone("Barrel").get().setRotationX(-MathUtil.LimitAngleMovement(-(entitylivingbaseIn.getYHeadRot() - entitylivingbaseIn.yBodyRot), 7.5F, -12.5F, false, true));
            this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.popPose();
        }

        //Torpedo Renderer
        inventory = inventories.getInventory(enums.SLOTTYPE.TORPEDO.ordinal());
        stack = inventory.getStackInSlot(0);
        if (stack != ItemStack.EMPTY) {
            if (stack.getItem() == registerItems.EQUIPMENT_TORPEDO_533MM.get()) {
                matrixStackIn.pushPose();
                RenderType renderType = RenderType.entitySmoothCutout(((ItemEquipmentBase) stack.getItem()).getTexture());
                matrixStackIn.translate((11.5 + hostbone.getPositionX()) / 16, (23.6 + hostbone.getPositionY()) / 16, (11.5 + hostbone.getPositionZ()) / 16);
                matrixStackIn.mulPose(new Quaternion(0, 0, -90, true));


                GeoModel EquipmentModel = ((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModelLocation(null));
                EquipmentModel.getBone("MountX").get().setRotationY(-0.75F - MathUtil.DegreeToRadian(entitylivingbaseIn.xRot));

                int AmmoCount = getRemainingAmmo(stack);
                EquipmentModel.getBone("torpedo4").ifPresent((bone) -> bone.setHidden(AmmoCount < 4));
                EquipmentModel.getBone("torpedo3").ifPresent((bone) -> bone.setHidden(AmmoCount < 3));
                EquipmentModel.getBone("torpedo2").ifPresent((bone) -> bone.setHidden(AmmoCount < 2));
                EquipmentModel.getBone("torpedo1").ifPresent((bone) -> bone.setHidden(AmmoCount < 1));

                this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                matrixStackIn.popPose();
            }
        }

        stack = inventory.getStackInSlot(1);
        if (stack != ItemStack.EMPTY) {
            if (stack.getItem() == registerItems.EQUIPMENT_TORPEDO_533MM.get()) {
                matrixStackIn.pushPose();
                RenderType renderType = RenderType.entitySmoothCutout(((ItemEquipmentBase) stack.getItem()).getTexture());
                matrixStackIn.translate((0 + hostbone.getPositionX()) / 16, (26.6 + hostbone.getPositionY()) / 16, (25.75 + hostbone.getPositionZ()) / 16);
                matrixStackIn.mulPose(new Quaternion(90, 180, 0, true));


                GeoModel EquipmentModel = ((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModelLocation(null));
                EquipmentModel.getBone("MountX").get().setRotationY((MathUtil.LimitAngleMovement(-(entitylivingbaseIn.getYHeadRot() - entitylivingbaseIn.yBodyRot), 45F, -45F, false, true)));

                int AmmoCount = getRemainingAmmo(stack);
                EquipmentModel.getBone("torpedo4").ifPresent((bone) -> bone.setHidden(AmmoCount < 4));
                EquipmentModel.getBone("torpedo3").ifPresent((bone) -> bone.setHidden(AmmoCount < 3));
                EquipmentModel.getBone("torpedo2").ifPresent((bone) -> bone.setHidden(AmmoCount < 2));
                EquipmentModel.getBone("torpedo1").ifPresent((bone) -> bone.setHidden(AmmoCount < 1));

                this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                matrixStackIn.popPose();
            }
        }

        stack = inventory.getStackInSlot(2);
        if (stack != ItemStack.EMPTY) {
            if (stack.getItem() == registerItems.EQUIPMENT_TORPEDO_533MM.get()) {
                matrixStackIn.pushPose();
                RenderType renderType = RenderType.entitySmoothCutout(((ItemEquipmentBase) stack.getItem()).getTexture());
                matrixStackIn.translate(-(11.5 + hostbone.getPositionX()) / 16, (23.6 + hostbone.getPositionY()) / 16, (11.5 + hostbone.getPositionZ()) / 16);
                matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));


                GeoModel EquipmentModel = ((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModel(((ItemEquipmentBase) stack.getItem()).getEquipmentModel().getModelLocation(null));
                EquipmentModel.getBone("MountX").get().setRotationY(0.75F + MathUtil.DegreeToRadian(entitylivingbaseIn.xRot));

                int AmmoCount = getRemainingAmmo(stack);
                EquipmentModel.getBone("torpedo4").ifPresent((bone) -> bone.setHidden(AmmoCount < 4));
                EquipmentModel.getBone("torpedo3").ifPresent((bone) -> bone.setHidden(AmmoCount < 3));
                EquipmentModel.getBone("torpedo2").ifPresent((bone) -> bone.setHidden(AmmoCount < 2));
                EquipmentModel.getBone("torpedo1").ifPresent((bone) -> bone.setHidden(AmmoCount < 1));

                this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                matrixStackIn.popPose();
            }
        }
    }

}
