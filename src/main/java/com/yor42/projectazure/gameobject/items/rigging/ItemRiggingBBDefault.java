package com.yor42.projectazure.gameobject.items.rigging;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.yor42.projectazure.client.model.rigging.modelBBRiggingDefault;
import com.yor42.projectazure.client.renderer.items.BBDefaultRiggingRenderer;
import com.yor42.projectazure.client.renderer.items.DDDefaultRiggingRenderer;
import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.items.IItemHandler;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.function.Consumer;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public class ItemRiggingBBDefault extends ItemRiggingBase{
    public ItemRiggingBBDefault(Properties properties, int HP) {
        super(properties, HP);
        this.validclass = enums.shipClass.Battleship;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new BBDefaultRiggingRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public int getMainGunSlotCount() {
        return 3;
    }

    @Override
    public int getSubGunSlotCount() {
        return 2;
    }

    @Override
    public int getAASlotCount() {
        return 3;
    }

    @Override
    public int getTorpedoSlotCount() {
        return 1;
    }

    @Override
    public int getFuelTankCapacity() {
        return 5000;
    }

    @Override
    public AnimatedGeoModel getModel() {
        return new modelBBRiggingDefault();
    }

    @Override
    public void RenderRigging(GeoModelProvider<?> entityModel, ItemStack Rigging, AbstractEntityCompanion entitylivingbaseIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.pushPose();
        GeoModelProvider modelRiggingProvider = this.getModel();
        int riggingoffset = 28;
        IBone hostbone = entityModel.getModel(entityModel.getModelLocation(null)).getBone("Body").get();
        if (entityModel.getModel(entityModel.getModelLocation(null)).getBone("Body").isPresent()) {
            matrixStackIn.translate(hostbone.getPositionX() / 16, (hostbone.getPositionY() + riggingoffset) / 16, (hostbone.getPositionZ() - 1) / 16);
        }
        RenderType type = RenderType.entitySmoothCutout(modelRiggingProvider.getTextureLocation(null));
        GeoModel riggingmodel = modelRiggingProvider.getModel(modelRiggingProvider.getModelLocation(null));
        this.render(riggingmodel, entitylivingbaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.popPose();

        IMultiInventory inventories = MultiInvUtil.getCap(entitylivingbaseIn.getRigging());

        //gun Renderer
        IItemHandler inventory = inventories.getInventory(enums.SLOTTYPE.MAIN_GUN.ordinal());
        ItemStack stack = inventory.getStackInSlot(0);
        if (stack != ItemStack.EMPTY) {

            ItemEquipmentBase EquipmentItem = ((ItemEquipmentBase) stack.getItem());

            matrixStackIn.pushPose();
            RenderType renderType = RenderType.entitySmoothCutout(EquipmentItem.getTexture());
            matrixStackIn.translate((-21 + hostbone.getPositionX()) / 16, (7 + hostbone.getPositionY() + riggingoffset) / 16, (7.5 + hostbone.getPositionZ()) / 16);
            matrixStackIn.mulPose(new Quaternion(0, 0, 0, true));

            GeoModel EquipmentModel = (EquipmentItem.getEquipmentModel().getModel(EquipmentItem.getEquipmentModel().getModelLocation(null)));
            EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian((entitylivingbaseIn.getYHeadRot() - entitylivingbaseIn.yBodyRot)));
            EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(entitylivingbaseIn.getXRot(), 7.5F, -12.5F, false, true));
            this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.popPose();
        }

        stack = inventory.getStackInSlot(1);
        if (stack != ItemStack.EMPTY) {

            ItemEquipmentBase EquipmentItem = ((ItemEquipmentBase) stack.getItem());

            matrixStackIn.pushPose();
            RenderType renderType = RenderType.entitySmoothCutout(EquipmentItem.getTexture());
            matrixStackIn.translate((-21 + hostbone.getPositionX()) / 16, (-1 + hostbone.getPositionY() + riggingoffset) / 16, (-10 + hostbone.getPositionZ()) / 16);
            matrixStackIn.mulPose(new Quaternion(0, 0, 0, true));

            GeoModel EquipmentModel = (EquipmentItem.getEquipmentModel().getModel(EquipmentItem.getEquipmentModel().getModelLocation(null)));
            EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian((entitylivingbaseIn.getYHeadRot() - entitylivingbaseIn.yBodyRot)));
            EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(entitylivingbaseIn.getXRot(), 7.5F, -12.5F, false, true));
            this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.popPose();
        }

        stack = inventory.getStackInSlot(2);
        if (stack != ItemStack.EMPTY) {

            ItemEquipmentBase EquipmentItem = ((ItemEquipmentBase) stack.getItem());

            matrixStackIn.pushPose();
            RenderType renderType = RenderType.entitySmoothCutout(EquipmentItem.getTexture());
            matrixStackIn.translate((-28.0206 + hostbone.getPositionX()) / 16, (-4.35 + hostbone.getPositionY() + riggingoffset) / 16, (7.5 + hostbone.getPositionZ()) / 16);
            matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));

            GeoModel EquipmentModel = (EquipmentItem.getEquipmentModel().getModel(EquipmentItem.getEquipmentModel().getModelLocation(null)));
            EquipmentModel.getBone("MountX").get().setRotationY(MathUtil.DegreeToRadian(entitylivingbaseIn.getXRot()));
            EquipmentModel.getBone("Barrel").get().setRotationX(-MathUtil.LimitAngleMovement(-entitylivingbaseIn.getYRot() - entitylivingbaseIn.yBodyRot, 7.5F, -12.5F, false, true));
            this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.popPose();
        }

        inventory = inventories.getInventory(enums.SLOTTYPE.SUB_GUN.ordinal());
        stack = inventory.getStackInSlot(0);
        if (stack != ItemStack.EMPTY) {

            ItemEquipmentBase EquipmentItem = ((ItemEquipmentBase) stack.getItem());

            matrixStackIn.pushPose();
            RenderType renderType = RenderType.entitySmoothCutout(EquipmentItem.getTexture());
            matrixStackIn.translate((21 + hostbone.getPositionX()) / 16, (7 + hostbone.getPositionY() + riggingoffset) / 16, (7.5 + hostbone.getPositionZ()) / 16);
            matrixStackIn.mulPose(new Quaternion(0, 0, 0, true));

            GeoModel EquipmentModel = (EquipmentItem.getEquipmentModel().getModel(EquipmentItem.getEquipmentModel().getModelLocation(null)));
            EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian((entitylivingbaseIn.getYHeadRot() - entitylivingbaseIn.yBodyRot)));
            EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(entitylivingbaseIn.getXRot(), 7.5F, -12.5F, false, true));
            this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.popPose();
        }

        stack = inventory.getStackInSlot(1);
        if (stack != ItemStack.EMPTY) {

            ItemEquipmentBase EquipmentItem = ((ItemEquipmentBase) stack.getItem());

            matrixStackIn.pushPose();
            RenderType renderType = RenderType.entitySmoothCutout(EquipmentItem.getTexture());
            matrixStackIn.translate((21 + hostbone.getPositionX()) / 16, (-1 + hostbone.getPositionY() + riggingoffset) / 16, (-10 + hostbone.getPositionZ()) / 16);
            matrixStackIn.mulPose(new Quaternion(0, 0, 0, true));

            GeoModel EquipmentModel = (EquipmentItem.getEquipmentModel().getModel(EquipmentItem.getEquipmentModel().getModelLocation(null)));
            EquipmentModel.getBone("MountX").get().setRotationY(-MathUtil.DegreeToRadian((entitylivingbaseIn.getYHeadRot() - entitylivingbaseIn.yBodyRot)));
            EquipmentModel.getBone("Barrel").get().setRotationX(MathUtil.LimitAngleMovement(entitylivingbaseIn.getXRot(), 7.5F, -12.5F, false, true));
            this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStackIn.popPose();
        }

        inventory = inventories.getInventory(enums.SLOTTYPE.TORPEDO.ordinal());
        stack = inventory.getStackInSlot(0);
        if (stack != ItemStack.EMPTY) {
            ItemEquipmentBase EquipmentItem = ((ItemEquipmentBase) stack.getItem());
            if (EquipmentItem == registerItems.EQUIPMENT_TORPEDO_533MM.get()) {
                matrixStackIn.pushPose();
                RenderType renderType = RenderType.entitySmoothCutout(((ItemEquipmentBase) stack.getItem()).getTexture());
                matrixStackIn.translate((0 + hostbone.getPositionX()) / 16, (-10 + hostbone.getPositionY() + riggingoffset) / 16, (26.75 + hostbone.getPositionZ()) / 16);
                matrixStackIn.mulPose(new Quaternion(90, 180, 0, true));
                GeoModel EquipmentModel = ((ItemEquipmentBase) EquipmentItem).getEquipmentModel().getModel(((ItemEquipmentBase) EquipmentItem).getEquipmentModel().getModelLocation(null));
                EquipmentModel.getBone("MountX").get().setRotationY((MathUtil.LimitAngleMovement(-entitylivingbaseIn.getYHeadRot() + entitylivingbaseIn.yRotO, 45F, -45F, false, true)));
                int AmmoCount = getRemainingAmmo(stack);
                EquipmentModel.getBone("torpedo4").get().setHidden(AmmoCount < 4);
                EquipmentModel.getBone("torpedo3").get().setHidden(AmmoCount < 3);
                EquipmentModel.getBone("torpedo2").get().setHidden(AmmoCount < 2);
                EquipmentModel.getBone("torpedo1").get().setHidden(AmmoCount < 1);

                this.render(EquipmentModel, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                matrixStackIn.popPose();
            }
        }
    }
}
