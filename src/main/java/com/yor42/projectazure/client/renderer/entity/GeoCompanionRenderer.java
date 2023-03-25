package com.yor42.projectazure.client.renderer.entity;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tac.guns.client.render.IHeldAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.render.pose.AimPose;
import com.tac.guns.client.render.pose.WeaponPose;
import com.tac.guns.common.GripType;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.mixin.WeaponPoseAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.network.chat.Component;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
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
    protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
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
    protected EquipmentSlot getEquipmentSlotForArmorBone(String boneName, T currentEntity) {
        switch (boneName) {
            case "armor_leftfoot":
            case "armor_rightfoot":
                return EquipmentSlot.FEET;
            case "armor_leftleg":
            case "armor_rightleg":
            case "armor_leftleg2":
            case "armor_rightleg2":
                return EquipmentSlot.LEGS;
            case "armor_rightarm":
                return !currentEntity.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            case "armor_leftarm":
                return currentEntity.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            case "armor_body":
            case "armor_chest":
                return EquipmentSlot.CHEST;
            case "armor_head":
                return EquipmentSlot.HEAD;
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
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        switch (boneName) {
            case "itemOffHand":
            case "itemMainHand":
                return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default:
                return ItemTransforms.TransformType.NONE;
        }
    }

    @Override
    protected void handleItemAndBlockBoneRendering(PoseStack stack, GeoBone bone, @Nullable ItemStack boneItem, @Nullable BlockState boneBlock, int packedLightIn) {
        if(!this.currentEntityBeingRendered.isPassenger() && !this.currentEntityBeingRendered.isBeingPatted() && !this.currentEntityBeingRendered.islewded()) {
            super.handleItemAndBlockBoneRendering(stack, bone, boneItem, boneBlock, packedLightIn);
        }
    }

    @Override
    protected void renderItemStack(PoseStack stack, MultiBufferSource rtb, int packedLightIn, ItemStack boneItem, String boneName) {

        IOverrideModel model = ModelOverrides.getModel(boneItem);
        if(model != null)
        {
            model.render(Minecraft.getInstance().getFrameTime(), this.getCameraTransformForItemAtBone(boneItem, boneName), boneItem, ItemStack.EMPTY, this.currentEntityBeingRendered,stack, rtb, packedLightIn, OverlayTexture.NO_OVERLAY);
        }
        else {
            super.renderItemStack(stack, rtb, packedLightIn, boneItem, boneName);
        }
    }

    @Override
    protected void preRenderItem(PoseStack matrixStack, ItemStack item, String boneName, T currentEntity, IBone bone) {

        if (item != this.mainHand && item != this.offHand) {
            return;
        }
        IOverrideModel model = ModelOverrides.getModel(item);
        if(model == null) {
            matrixStack.scale(1F / 0.4F, 1F / 0.4F, 1F / 0.4F);
            matrixStack.translate((bone.getPositionX() / 16) * -0.4F, (bone.getPositionY() / 16) * -0.4F, (bone.getPositionZ() / 16) * -0.4F);
        }
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
        boolean shieldFlag = item.isShield(currentEntity) || item.getItem() instanceof ShieldItem;
        if (item == this.mainHand) {
            if (shieldFlag) {
                matrixStack.translate(0.0, 0.125, -0.25);
            }
            else if(item.getItem() instanceof GunItem) {

                GunItem gunItem = (GunItem) item.getItem();

                GripType type = gunItem.getGun().getGeneral().getGripType();
                IHeldAnimation pose = type.getHeldAnimation();

                if(!(pose instanceof WeaponPose))
                {
                    return;
                }

                float leftHanded = currentEntity.isLeftHanded() ? -1.0F : 1.0F;
                matrixStack.translate(0.0, 0.0, 0.05);
                float angle = (float) (Mth.lerp(Minecraft.getInstance().getFrameTime(), currentEntity.xRotO, currentEntity.xRot) / 90.0);
                float angleAbs = Math.abs(angle);
                float zoom = 1F;
                AimPose targetPose = (double)angle > 0.0 ? ((WeaponPoseAccessor)pose).ongetDownPose() : ((WeaponPoseAccessor)pose).ongetUpPose();
                AimPose forwardPose = ((WeaponPoseAccessor)pose).ongetForwardPose();
                float translateX = this.getValue(targetPose.getIdle().getItemTranslate().x(), targetPose.getAiming().getItemTranslate().x(), forwardPose.getIdle().getItemTranslate().x(), forwardPose.getAiming().getItemTranslate().x(), 0.0F, angleAbs, zoom, 1.0F);
                float translateY = this.getValue(targetPose.getIdle().getItemTranslate().y(), targetPose.getAiming().getItemTranslate().y(), forwardPose.getIdle().getItemTranslate().y(), forwardPose.getAiming().getItemTranslate().y(), 0.0F, angleAbs, zoom, 1.0F);
                float translateZ = this.getValue(targetPose.getIdle().getItemTranslate().z(), targetPose.getAiming().getItemTranslate().z(), forwardPose.getIdle().getItemTranslate().z(), forwardPose.getAiming().getItemTranslate().z(), 0.0F, angleAbs, zoom, 1.0F);
                matrixStack.translate((double)translateX+3 * 0.0625 * (double)leftHanded, (double)translateY+3.5 * 0.0625, (double)translateZ-4 * 0.0625);
                float mulPoseX = this.getValue(targetPose.getIdle().getItemRotation().x(), targetPose.getAiming().getItemRotation().x(), forwardPose.getIdle().getItemRotation().x(), forwardPose.getAiming().getItemRotation().x(), 0.0F, angleAbs, zoom, 1.0F);
                float mulPoseY = this.getValue(targetPose.getIdle().getItemRotation().y(), targetPose.getAiming().getItemRotation().y(), forwardPose.getIdle().getItemRotation().y(), forwardPose.getAiming().getItemRotation().y(), 0.0F, angleAbs, zoom, 1.0F);
                float mulPoseZ = this.getValue(targetPose.getIdle().getItemRotation().z(), targetPose.getAiming().getItemRotation().z(), forwardPose.getIdle().getItemRotation().z(), forwardPose.getAiming().getItemRotation().z(), 0.0F, angleAbs, zoom, 1.0F);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(mulPoseX));
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(mulPoseY * leftHanded));
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(mulPoseZ * leftHanded));
            }
            else{
                matrixStack.translate(0, 0.08, 0);
                this.performCustomRotationtoStack(item, matrixStack, InteractionHand.MAIN_HAND);
            }
        } else {
            if (shieldFlag) {
                matrixStack.translate(0, 0.125, 0.25);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
            } else {
                matrixStack.translate(0, 0.08, 0);
                this.performCustomRotationtoStack(item, matrixStack, InteractionHand.OFF_HAND);
            }

        }
        // item.mulPose(Vector3f.YP.rotationDegrees(180));

        // item.scale(0.75F, 0.75F, 0.75F);
    }

    private float getValue(@Nullable Float t1, @Nullable Float t2, Float s1, Float s2, Float def, float partial, float zoom, float leftHanded) {
        float start = t1 != null && s1 != null ? (s1 + (t1 - s1) * partial) * leftHanded : (s1 != null ? s1 * leftHanded : def);
        float end = t2 != null && s2 != null ? (s2 + (t2 - s2) * partial) * leftHanded : (s2 != null ? s2 * leftHanded : def);
        return Mth.lerp(zoom, start, end);
    }

    @Override
    protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, T currentEntity, IBone bone) {

    }

    @Override
    protected BlockState getHeldBlockForBone(String boneName, T currentEntity) {
        return null;
    }

    @Override
    protected void preRenderBlock(PoseStack matrixStack, BlockState block, String boneName, T currentEntity) {

    }

    @Override
    protected void postRenderBlock(PoseStack matrixStack, BlockState block, String boneName, T currentEntity) {

    }

    protected GeoCompanionRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider, 0.4F, 0.4F, 0.4F);
        //this.addLayer(new ArrowLayer<>(this));
    }

    @Override
    public void renderEarly(T animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        if(isBoneCosmetic(bone)) {
            bone.setHidden(this.shouldHideBone(bone.getName()));
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        /*
        if(bone.getName().equals("RiggingHardPoint")){
            stack.pushPose();
            ItemStack Rigging = this.animatable.getRigging();
            if(!(Rigging.getItem() instanceof ItemRiggingBase)){
                return;
            }
            ItemRiggingBase riggingItem = (ItemRiggingBase) Rigging.getItem();
            AnimatedGeoModel modelRiggingProvider = riggingItem.getModel();
            RenderType type = RenderType.entitySmoothCutout(modelRiggingProvider.getTextureLocation(null));
            GeoModel riggingmodel = modelRiggingProvider.getModel(modelRiggingProvider.getModelLocation(null));
            stack.translate(bone.getPivotX()/16, bone.getPivotY()/16, bone.getPivotZ()/16);
            AnimationEvent itemEvent = new AnimationEvent(riggingItem, 0, 0, 0, false, Arrays.asList(Rigging, this.animatable));

            modelRiggingProvider.setLivingAnimations(riggingItem, riggingItem.getUniqueID(riggingItem), itemEvent);
            riggingItem.render(riggingmodel, riggingItem, 0, type, stack, this.rtb, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            stack.popPose();
        }

         */
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
    }

    @Override
    protected void applyRotations(T entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
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
    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        if(Minecraft.getInstance().player != null && entity.isOwnedBy(Minecraft.getInstance().player)) {
            this.renderStatus(entity, stack, bufferIn, packedLightIn);
            this.renderNameTag(entity, entity.getDisplayName(), stack, bufferIn, packedLightIn);
        }
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    public void renderonLayer(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {

        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public void render(GeoModel model, T animatable, float partialTicks, RenderType type, PoseStack matrixStackIn, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    protected void renderNameTag(@Nonnull T entity, Component text, PoseStack stack, MultiBufferSource iRenderTypeBuffer, int light) {
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
            Font fontrenderer = this.getFont();
            float f2 = (float) (-fontrenderer.width(text) / 2);
            fontrenderer.drawInBatch(text, f2, (float) 0, 553648127, false, matrix4f, iRenderTypeBuffer, flag, j, light);
            if (flag) {
                fontrenderer.drawInBatch(text, f2, (float) 0, -1, false, matrix4f, iRenderTypeBuffer, false, 0, light);
            }

            stack.popPose();
        }
    }

    protected void performCustomRotationtoStack(ItemStack stack, PoseStack matrix, InteractionHand hand){}

    protected void renderStatus(T entity, PoseStack stack, MultiBufferSource iRenderTypeBuffer, int p_225629_5_) {
        Component text = entity.getMoveStatus().getDIsplayname();
        double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
        if (net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(entity, d0)) {
            boolean flag = !entity.isDiscrete();
            float renderscale = 0.3F;
            float f = (entity.getBbHeight() + 0.1F) / renderscale;
            stack.pushPose();
            Font fontrenderer = this.getFont();
            float f2 = (float) (-fontrenderer.width(text) / 2);
            stack.scale(renderscale, renderscale, renderscale);
            stack.translate(0.0D, f, 0.0D);
            stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            stack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = stack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int) (f1 * 255.0F) << 24;
            fontrenderer.drawInBatch(text, f2, (float) 0, 553648127, false, matrix4f, iRenderTypeBuffer, flag, j, p_225629_5_);
            if (flag) {
                fontrenderer.drawInBatch(text, f2, (float) 0, -1, false, matrix4f, iRenderTypeBuffer, false, 0, p_225629_5_);
            }

            renderscale = 8;
            stack.scale(renderscale,renderscale,renderscale);

            ResourceTexture texture = new ResourceTexture( ResourceUtils.ModResourceLocation("textures/gui/ship_inventory.png"),176/256F,13/256F, 12/256F,12/256F);

            if(this.currentEntityBeingRendered != null) {
                switch (this.currentEntityBeingRendered.moraleValuetoLevel()) {
                    default:
                        texture = texture.getSubTexture(0,0,1,1);
                        break;
                    case REALLY_HAPPY:
                        texture = texture.getSubTexture(4,0,1,1);
                        break;
                    case HAPPY:
                        texture = texture.getSubTexture(3,0,1,1);
                        break;
                    case NEUTRAL:
                        texture = texture.getSubTexture(2,0,1,1);
                        break;
                    case TIRED:
                        texture = texture.getSubTexture(1,0,1,1);
                        break;
                }
                texture.draw(stack, 0,0 , (f2-12)/renderscale,0,1,1);
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
