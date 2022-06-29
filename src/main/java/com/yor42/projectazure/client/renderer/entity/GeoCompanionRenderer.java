package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Pose;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class GeoCompanionRenderer<T extends AbstractEntityCompanion & IAnimatable> extends ExtendedGeoEntityRenderer<T> {

    /*
     * This Part of class is based on ExampleExtendedRendererEntityRenderer from Geckolib.
     * Get the Source Code in github:
     * https://github.com/bernie-g/geckolib/blob/1.16/src/main/java/software/bernie/example/client/renderer/entity/ExampleExtendedRendererEntityRenderer.java
     * Get Mod on curseforge:
     *  https://www.curseforge.com/minecraft/mc-mods/geckolib
     *
     * Geckolib is licensed under MIT license.
     */
    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, T currentEntity) {
        return null;
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return bone.getName().startsWith("armor_");
    }

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, T currentEntity) {
        switch (boneName) {
            case "armor_leftfoot":
            case "armor_rightfoot":
                return boots;
            case "armor_leftleg":
            case "armor_rightleg":
            case "armor_leftleg2":
            case "armor_rightleg2":
                return leggings;
            case "armor_rightarm":
            case "armor_leftarm":
            case "armor_body":
            case "armor_chest":
                return chestplate;
            case "armor_head":
                return helmet;
            default:
                return null;
        }
    }

    @Override
    protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorModel) {
        switch (name) {
            case "armor_leftfoot":
            case "armor_leftleg":
            case "armor_leftleg2":
                return armorModel.leftLeg;
            case "armor_rightfoot":
            case "armor_rightleg":
            case "armor_rightleg2":
                return armorModel.rightLeg;
            case "armor_rightarm":
                return armorModel.rightArm;
            case "armor_leftarm":
                return armorModel.leftArm;
            case "armor_body":
            case "armor_chest":
                return armorModel.body;
            case "armor_head":
                return armorModel.head;
            default:
                return null;
        }
    }

    @Override
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, T currentEntity) {
        switch (boneName) {
            case "armor_leftfoot":
            case "armor_rightfoot":
                return EquipmentSlotType.FEET;
            case "armor_leftleg":
            case "armor_rightleg":
            case "armor_leftleg2":
            case "armor_rightleg2":
                return EquipmentSlotType.LEGS;
            case "armor_rightarm":
                return !currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armor_leftarm":
                return currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armor_body":
            case "armor_chest":
                return EquipmentSlotType.CHEST;
            case "armor_head":
                return EquipmentSlotType.HEAD;
            default:
                return null;
        }
    }

    @Override
    protected ItemStack getHeldItemForBone(String boneName, T currentEntity) {
        switch (boneName) {
            case "itemOffHand":
                return currentEntity.isLeftHanded() ? mainHand : offHand;
            case "itemMainHand":
                return currentEntity.isLeftHanded() ? offHand : mainHand;
        }
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        switch (boneName) {
            case "itemOffHand":
            case "itemMainHand":
                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default:
                return ItemCameraTransforms.TransformType.NONE;
        }
    }

    @Override
    protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, T currentEntity, IBone bone) {
        matrixStack.scale(1F/0.4F,1F/0.4F,1F/0.4F);
        if (item == this.mainHand || item == this.offHand) {
            matrixStack.translate(bone.getPositionX(), bone.getPositionY(), bone.getPositionZ());
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            matrixStack.mulPose(Vector3f.XP.rotation(bone.getRotationX()));
            matrixStack.mulPose(Vector3f.YP.rotation(bone.getRotationY()));
            matrixStack.mulPose(Vector3f.ZP.rotation(bone.getRotationY()));
            boolean shieldFlag = item.isShield(currentEntity) || item.getItem() instanceof ShieldItem;
            if (item == this.mainHand) {
                if (shieldFlag) {
                    matrixStack.translate(0.0, 0.125, -0.25);
                } else {
                    matrixStack.translate(0, 0.08, 0);
                    this.performCustomRotationtoStack(item, matrixStack, Hand.MAIN_HAND);
                }
            } else {
                if (shieldFlag) {
                    matrixStack.translate(0, 0.125, 0.25);
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
                } else {
                    matrixStack.translate(0, 0.08, 0);
                    this.performCustomRotationtoStack(item, matrixStack, Hand.OFF_HAND);
                }

            }
            // item.mulPose(Vector3f.YP.rotationDegrees(180));

            // item.scale(0.75F, 0.75F, 0.75F);
        }
    }

    @Override
    protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, T currentEntity, IBone bone) {

    }

    @Override
    protected BlockState getHeldBlockForBone(String boneName, T currentEntity) {
        return null;
    }

    @Override
    protected void preRenderBlock(BlockState block, String boneName, T currentEntity) {

    }

    @Override
    protected void postRenderBlock(BlockState block, String boneName, T currentEntity) {
    }

    protected GeoCompanionRenderer(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider, 0.4F, 0.4F, 0.4F);
    }

    @Override
    public void renderEarly(T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        if(isBoneCosmetic(bone)) {
            bone.setHidden(this.shouldHideBone(bone.getName()));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    protected boolean isBoneCosmetic(GeoBone bone) {
        return bone.getName().startsWith("cosmetic_") || Objects.equals(bone.getName(), "Chest2");
    }

    protected boolean shouldHideBone(String bone){
        switch (bone){
            default:
                return false;
            case "cosmetic_leftfeet":
            case "cosmetic_rightfeet":
                return !(boots.getItem() == Items.AIR);
            case "cosmetic_leftleg":
            case "cosmetic_rightleg":
            case "cosmetic_leftleg2":
            case "cosmetic_rightleg2":
            case "cosmetic_skirt":
                return !(leggings.getItem() == Items.AIR);
            case "cosmetic_body":
            case "cosmetic_chest":
            case "cosmetic_chest2":
            case "cosmetic_chest3":
            case "cosmetic_chest4":
            case "cosmetic_leftarm":
            case "Chest2":
            case "cosmetic_waist":
            case "cosmetic_rightarm":
            case "cosmetic_lefthand":
            case "cosmetic_righthand":
                boolean val = (chestplate.getItem() == Items.AIR);
                return !val;
            case "cosmetic_head":
            case "cosmetic_head2":
            case "cosmetic_head3":
            case "cosmetic_head4":
            case "cosmetic_head5":
                return !(helmet.getItem() == Items.AIR);
        }
    };

    @Override
    protected void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        Pose pose = entityLiving.getPose();
        if (pose != Pose.SLEEPING) {
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        }

        //Do not rotate model when companion is dead.
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
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if(Minecraft.getInstance().player != null && entity.isOwnedBy(Minecraft.getInstance().player)) {
            this.renderStatus(entity, stack, bufferIn, packedLightIn);
            this.renderNameTag(entity, entity.getDisplayName(), stack, bufferIn, packedLightIn);
        }
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    protected void renderNameTag(@Nonnull T entity, ITextComponent text, MatrixStack stack, IRenderTypeBuffer iRenderTypeBuffer, int light) {
        double d0 = entityRenderDispatcher.distanceToSqr(entity);
        if (net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(entity, d0)) {
            boolean flag = !entity.isDiscrete();
            float renderscale = 0.4F;
            float f = (entity.getBbHeight()+0.2F) / renderscale;
            stack.pushPose();
            stack.scale(renderscale, renderscale, renderscale);
            stack.translate(0.0D, f, 0.0D);
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

    protected void performCustomRotationtoStack(ItemStack stack, MatrixStack matrix, Hand hand){}

    protected void renderStatus(T entity, MatrixStack stack, IRenderTypeBuffer iRenderTypeBuffer, int p_225629_5_) {
        ITextComponent text = entity.getMoveStatus().getDIsplayname();
        double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
        if (net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(entity, d0)) {
            boolean flag = !entity.isDiscrete();
            float renderscale = 0.3F;
            float f = (entity.getBbHeight() + 0.1F) / renderscale;
            stack.pushPose();
            stack.scale(renderscale, renderscale, renderscale);
            stack.translate(0.0D, f, 0.0D);
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
