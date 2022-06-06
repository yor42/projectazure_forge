package com.yor42.projectazure.client.model.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.AnimationUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
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
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone body = this.getAnimationProcessor().getBone("Body");
        IBone LeftArm = this.getAnimationProcessor().getBone("LeftArm");
        IBone RightArm = this.getAnimationProcessor().getBone("RightArm");
        IBone Chest = this.getAnimationProcessor().getBone("Chest");
        if(!(entity.isBeingPatted()||entity.isSleeping())) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
        else if(entity.getOwner() != null && entity.getVehicle() == entity.getOwner()) {
            body.setPositionY(body.getPositionY() - 60);
            body.setPositionZ(body.getPositionZ() + 10);
            if(entity.getOwner().isCrouching()){
                body.setPositionZ(body.getPositionZ() + 2);
                body.setPositionY(body.getPositionY() + 2);
                body.setRotationX(MathUtil.DegreeToRadian(90F / (float) Math.PI)*-1);
            }
        }
        else if(entity.isSleeping()){
            body.setPositionY(SleepingBodyYPosition());
            body.setPositionZ(SleepingBodyZPosition());
        }
        else if(!(entity.isBeingPatted()||entity.islewded())) {

            if(!entity.isOrderedToSit()) {
                if (entity.isChargingCrossbow()) {
                    AnimationUtils.GeckolibanimateCrossbowCharge(RightArm, LeftArm, entity, true);
                } else if (entity.getMainHandItem().getItem() instanceof CrossbowItem) {
                    AnimationUtils.GeckolibanimateCrossbowHold(RightArm, LeftArm, head, true);
                }
            }
        }
        AnimationUtils.SwingArm(LeftArm, RightArm, Chest, head, entity, customPredicate.getPartialTick());
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
