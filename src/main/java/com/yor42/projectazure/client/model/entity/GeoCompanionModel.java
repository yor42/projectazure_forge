package com.yor42.projectazure.client.model.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.AnimationUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

import javax.annotation.Nullable;

public abstract class GeoCompanionModel<E extends AbstractEntityCompanion> extends AnimatedGeoModel<E> {

    protected int blinkinterval = 0;
    protected long LastBlinkTime = 0;

    @Override
    public void setLivingAnimations(E entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        if(!Minecraft.getInstance().isPaused()) {
            super.setLivingAnimations(entity, uniqueID, customPredicate);
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            IBone head = this.getAnimationProcessor().getBone("Head");
            IBone body = this.getAnimationProcessor().getBone("Body");
            IBone LeftArm = this.getAnimationProcessor().getBone("LeftArm");
            IBone RightArm = this.getAnimationProcessor().getBone("RightArm");
            IBone Chest = this.getAnimationProcessor().getBone("Chest");
            float pitch = extraData.headPitch* ((float) Math.PI / 180F);
            float yaw = extraData.netHeadYaw* ((float) Math.PI / 180F);
            if (!(entity.isBeingPatted() || entity.isSleeping())) {
                Vector2f headoffset = this.getHeadOffset(entity);
                head.setRotationX(head.getRotationX()+pitch);
                head.setRotationY(head.getRotationY()+yaw);
            }

            if (entity.getOwner() != null && entity.getVehicle() == entity.getOwner()) {
                body.setPositionZ(body.getPositionZ() - 10);
                if (entity.getOwner().isCrouching()) {
                    body.setPositionZ(body.getPositionZ() + 8);
                    body.setPositionY(body.getPositionY() - 8);
                    body.setRotationX(MathUtil.DegreeToRadian(90F / (float) Math.PI) * -1);
                }
            } else if (entity.isSleeping()) {
                body.setPositionY(SleepingBodyYPosition());
                body.setPositionZ(SleepingBodyZPosition());
            } else if (!(entity.isBeingPatted() || entity.islewded())) {

                if (!entity.isOrderedToSit()) {
                    if (entity.isChargingCrossbow()) {
                        AnimationUtils.GeckolibanimateCrossbowCharge(RightArm, LeftArm, entity, true);
                    } else if (entity.getMainHandItem().getItem() instanceof CrossbowItem) {
                        AnimationUtils.GeckolibanimateCrossbowHold(RightArm, LeftArm, head, true);
                    }
                    else if(entity.isUsingItem() && entity.isHolding((item)->item instanceof BowItem)){
                        boolean isLeftHanded = entity.isLeftHanded();
                        switch(entity.getUsedItemHand()){
                            case OFF_HAND:
                                if (isLeftHanded) {
                                    animatebowRightArm(RightArm, LeftArm, pitch, yaw);
                                } else {
                                    animatebowLeftArm(RightArm, LeftArm, pitch, yaw);
                                }
                                break;
                            case MAIN_HAND:
                                if (isLeftHanded) {
                                    animatebowLeftArm(RightArm, LeftArm, pitch, yaw);
                                } else {
                                    animatebowRightArm(RightArm, LeftArm, pitch, yaw);
                                }
                                break;
                        }
                    }
                }
            }
            AnimationUtils.SwingArm(LeftArm, RightArm, Chest, head, entity, customPredicate.getPartialTick());
        }
    }

    protected void animatebowRightArm(IBone RightArm, IBone LeftArm, float headpitch, float headyaw){
        RightArm.setRotationY((-0.1F + headyaw)*-1);
        LeftArm.setRotationY((0.1F + headyaw + 0.4F)*-1);
        RightArm.setRotationX (((float)Math.PI / 2F) + headpitch);
        LeftArm.setRotationX(((float)Math.PI / 2F) + headpitch);
    }

    protected void animatebowLeftArm(IBone RightArm, IBone LeftArm, float headpitch, float headyaw){
        RightArm.setRotationY((-0.1F + headyaw - 0.4F)*-1);
        LeftArm.setRotationY((0.1F + headyaw)*-1);
        RightArm.setRotationX (((float)Math.PI / 2F) + headpitch);
        LeftArm.setRotationX(((float)Math.PI / 2F) + headpitch);
    }

    protected Vector2f getHeadOffset(E entity){
        return Vector2f.ZERO;
    }

    protected abstract int SleepingBodyYPosition();

    protected int SleepingBodyZPosition(){
        return 0;
    }

    @Override
    public void setMolangQueries(IAnimatable animatable, double currentTick) {
        super.setMolangQueries(animatable, currentTick);
        MolangParser parser = GeckoLibCache.getInstance().parser;
        if(animatable instanceof AbstractEntityCompanion){
            parser.setValue("query.head_pitch", ((LivingEntity)animatable).xRot);
            parser.setValue("query.head_yaw", ((LivingEntity)animatable).yHeadRot-((LivingEntity)animatable).yBodyRot);

            parser.setValue("query.prev_head_pitch", ((LivingEntity)animatable).xRotO * ((float) Math.PI / 180F));
            parser.setValue("query.prev_head_yaw", ((LivingEntity)animatable).yHeadRotO * ((float) Math.PI / 180F));

            parser.setValue("query.animation_speed", ((AbstractEntityCompanion)animatable).animationSpeed);
        }
    }
}
