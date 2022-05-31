package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.common.GripType;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import mekanism.api.annotations.NonNull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.Pose;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class GeoCompanionRenderer<T extends AbstractEntityCompanion & IAnimatable> extends GeoEntityRenderer<T> {

    protected AbstractEntityCompanion entity;
    protected IRenderTypeBuffer rtb;
    protected ResourceLocation texture;
    protected GeoCompanionRenderer(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
        this.shadowRadius = 0.4F;
    }

    @Override
    public void renderEarly(T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void render(@NonNull T entity, float entityYaw, float partialTicks, @Nonnull MatrixStack stack, @Nonnull IRenderTypeBuffer bufferIn, int packedLightIn) {
        this.renderStatus(entity, stack, bufferIn, packedLightIn);
        this.renderNameTag(entity, entity.getDisplayName(), stack, bufferIn, packedLightIn);
        stack.pushPose();
        stack.scale(0.4F, 0.4F, 0.4F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entitySmoothCutout(textureLocation);
    }

    @Override
    protected void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        Pose pose = entityLiving.getPose();
        if (pose != Pose.SLEEPING) {
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        }

        //PA Start: Do not rotate model when companion is dead.
        if (entityLiving.isAutoSpinAttack()) {
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-90.0F - entityLiving.xRot));
            matrixStackIn
                    .mulPose(Vector3f.YP.rotationDegrees(((float) entityLiving.tickCount + partialTicks) * -75.0F));
        } else if (pose == Pose.SLEEPING) {
            Direction direction = entityLiving.getBedOrientation();
            float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f1));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(this.getDeathMaxRotation(entityLiving)));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(270.0F));
        }
        //PA end;
    }

    protected boolean isLeftHanded(){
        return false;
    }

    @Override
    protected void renderNameTag(@Nonnull T entity, ITextComponent text, MatrixStack stack, IRenderTypeBuffer iRenderTypeBuffer, int light) {
        double d0 = entityRenderDispatcher.distanceToSqr(entity);
        if (net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(entity, d0)) {
            boolean flag = !entity.isDiscrete();
            float renderscale = 0.4F;
            float f = (entity.getBbHeight()+0.15F) / renderscale;
            stack.pushPose();
            stack.scale(renderscale, renderscale, renderscale);
            stack.translate(0.0D, (double) f, 0.0D);
            stack.mulPose(entityRenderDispatcher.cameraOrientation());
            stack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = stack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int) (f1 * 255.0F) << 24;
            FontRenderer fontrenderer = this.getFont();
            float f2 = (float) (-fontrenderer.width(text) / 2);
            fontrenderer.drawInBatch(text, f2, (float) 0, 553648127, false, matrix4f, iRenderTypeBuffer, flag, j, light);
            if (flag) {
                fontrenderer.drawInBatch(text, f2, (float) 0, -1, false, matrix4f, iRenderTypeBuffer, false, 0, light);
            }

            stack.popPose();
        }
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        Vector3d ItemPosition = this.getHandItemCoordinate();
        if (bone.getName().equals("itemMainHand")) {
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.isLeftHanded()? entity.getItemBySlot(EquipmentSlotType.OFFHAND) :entity.getItemBySlot(EquipmentSlotType.MAINHAND);
            stack.translate(ItemPosition.x, ItemPosition.y, ItemPosition.z);
            stack.scale(1.5F, 1.5F, 1.5F);
            if (!mainHandStack.isEmpty()) {
                this.performCustomRotationtoStack(mainHandStack, stack, Hand.MAIN_HAND);
                Item gunItem = entity.getGunStack().getItem();
                if (!entity.isReloadingMainHand() && entity.isUsingGun() && ((GunItem) gunItem).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
                    stack.mulPose(Vector3f.XN.rotationDegrees(27.5F));
                }
                Minecraft.getInstance().getItemRenderer().renderStatic(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.popPose();
        } else if (bone.getName().equals("itemOffHand")) {
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.isLeftHanded()?entity.getItemBySlot(EquipmentSlotType.MAINHAND) :entity.getItemBySlot(EquipmentSlotType.OFFHAND);
            float xvalue = (float) (ItemPosition.x*-1);
            if (mainHandStack.isShield(entity)) {
                stack.mulPose(Vector3f.ZP.rotationDegrees(180));
                xvalue = (xvalue * -1);
            }
            else{
                this.performCustomRotationtoStack(mainHandStack, stack, Hand.OFF_HAND);
            }
            stack.translate(xvalue, ItemPosition.y, ItemPosition.z);
            stack.scale(1.5F, 1.5F, 1.5F);
            if (!mainHandStack.isEmpty()) {
                Minecraft.getInstance().getItemRenderer().renderStatic(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.popPose();
        }
        bufferIn = rtb.getBuffer(RenderType.entitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    protected void performCustomRotationtoStack(ItemStack stack, MatrixStack matrix, Hand hand){}

    protected void renderStatus(T entity, MatrixStack stack, IRenderTypeBuffer iRenderTypeBuffer, int p_225629_5_) {
        ITextComponent text = entity.getMoveStatus().getDIsplayname();
        double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
        if (net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(entity, d0)) {
            boolean flag = !entity.isDiscrete();
            float renderscale = 0.3F;
            float f = (entity.getBbHeight() + 0.05F) / renderscale;
            stack.pushPose();
            stack.scale(renderscale, renderscale, renderscale);
            stack.translate(0.0D, (double) f, 0.0D);
            stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            stack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = stack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int) (f1 * 255.0F) << 24;
            FontRenderer fontrenderer = this.getFont();
            float f2 = (float) (-fontrenderer.width(text) / 2);
            fontrenderer.drawInBatch(text, f2, (float) 0, 553648127, false, matrix4f, iRenderTypeBuffer, flag, j, p_225629_5_);
            if (flag) {
                fontrenderer.drawInBatch(text, f2, (float) 0, -1, false, matrix4f, iRenderTypeBuffer, false, 0, p_225629_5_);
            }

            stack.popPose();
        }
    }

    @Nonnull
    protected abstract Vector3d getHandItemCoordinate();

    private static float getFacingAngle(Direction facingIn) {
        switch (facingIn) {
            case SOUTH:
                return 90.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }
}
