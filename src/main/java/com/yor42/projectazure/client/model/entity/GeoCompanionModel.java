package com.yor42.projectazure.client.model.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.AnimationUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.mixin.PathNavigatorAccessors;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.Hand;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;

import javax.annotation.Nullable;

import static net.minecraft.util.Hand.MAIN_HAND;

public abstract class GeoCompanionModel<E extends AbstractEntityCompanion> extends AnimatedGeoModel<E> {

    public GeoCompanionModel(){}

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
                head.setRotationX(head.getRotationX()+pitch);
                head.setRotationY(head.getRotationY()+yaw);
            }

            if (entity.getOwner() != null && entity.getVehicle() == entity.getOwner()) {
                body.setPositionZ(body.getPositionZ() - 15);
                if (entity.getOwner().isCrouching()) {
                    body.setPositionZ(body.getPositionZ() + 8);
                    body.setPositionY(body.getPositionY() - 4);
                    body.setRotationX(MathUtil.DegreeToRadian(90F / (float) Math.PI) * -1);
                }
            }else if (!(entity.isBeingPatted() || entity.islewded())) {
                if (!entity.isOrderedToSit()) {
                    if(entity.isHolding((item)->item instanceof CrossbowItem)){
                        Hand CrossbowHand = entity.getItemInHand(MAIN_HAND).getItem() instanceof CrossbowItem? MAIN_HAND:Hand.OFF_HAND;
                        boolean isMainhanded = CrossbowHand == MAIN_HAND;
                        if(entity.isChargingCrossbow()){
                            AnimationUtils.GeckolibanimateCrossbowCharge(RightArm, LeftArm, entity, isMainhanded);
                        }
                        else{
                            AnimationUtils.GeckolibanimateCrossbowHold(RightArm, LeftArm, head, true);
                        }
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

    protected abstract int SleepingBodyYPosition();

    @Override
    public void setMolangQueries(IAnimatable animatable, double currentTick) {
        super.setMolangQueries(animatable, currentTick);
        MolangParser parser = GeckoLibCache.getInstance().parser;
        if(animatable instanceof AbstractEntityCompanion){
            parser.setValue("query.head_pitch", ()->((LivingEntity) animatable).xRot);
            parser.setValue("query.head_yaw", ()->((LivingEntity)animatable).yHeadRot-((LivingEntity)animatable).yBodyRot);

            parser.setValue("query.prev_head_pitch", ()->((LivingEntity)animatable).xRotO * ((float) Math.PI / 180F));
            parser.setValue("query.prev_head_yaw", ()->((LivingEntity)animatable).yHeadRotO * ((float) Math.PI / 180F));

            parser.setValue("query.animation_speed", ()->(Math.min(((AbstractEntityCompanion)animatable).animationSpeed*2,1)));
            parser.setValue("query.speed_modifier", ()->(((PathNavigatorAccessors)((AbstractEntityCompanion)animatable).getNavigation()).getSpeedModifier()));
        }
    }
}
