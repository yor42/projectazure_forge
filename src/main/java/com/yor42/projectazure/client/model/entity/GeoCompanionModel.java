package com.yor42.projectazure.client.model.entity;

import com.tac.guns.client.render.IHeldAnimation;
import com.tac.guns.client.render.pose.AimPose;
import com.tac.guns.client.render.pose.LimbPose;
import com.tac.guns.client.render.pose.WeaponPose;
import com.tac.guns.common.GripType;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.AnimationUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.mixin.PathNavigatorAccessors;
import com.yor42.projectazure.mixin.WeaponPoseAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;

import javax.annotation.Nullable;

import static net.minecraft.util.Hand.MAIN_HAND;

public abstnet.minecraft.world.InteractionHandModel<E extends AbstractEntityCompanion> extends AnimatedGeoModel<E> {

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
                        InteractionHand CrossbowHand = entity.getItemInHand(MAIN_HAND).getItem() instanceof CrossbowItem? MAIN_HAND:InteractionHand.OFF_HAND;
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
                    else if(entity.isHolding((item)->item instanceof TimelessGunItem)){
                        boolean isLeftHanded = entity.isLeftHanded();
                        InteractionHand hand = entity.getItemInHand(MAIN_HAND).getItem() instanceof TimelessGunItem? MAIN_HAND: InteractionHand.OFF_HAND;
                        ItemStack gunstack = entity.getItemInHand(hand);
                        TimelessGunItem gunItem = (TimelessGunItem) gunstack.getItem();
                        GripType type = gunItem.getGun().getGeneral().getGripType();
                        IHeldAnimation animation = type.getHeldAnimation();
                        if(animation instanceof WeaponPose)
                        {
                            WeaponPose pose = (WeaponPose) animation;
                            float zoom = 1F;
                            this.applyPlayerModelRotation(pose, extraData.headPitch, isLeftHanded, LeftArm, RightArm,head, body, extraData.netHeadYaw, zoom);
                        }
                        else{
                            boolean isLefthanded = entity.isLeftHanded();
                            IBone mainArm = isLefthanded ? LeftArm:RightArm;
                            IBone secondaryArm = isLefthanded ? RightArm:LeftArm;
                            if(hand == MAIN_HAND){
                                mainArm.setRotationX((float)Math.toRadians(90+ extraData.headPitch));
                                mainArm.setRotationY((float)Math.toRadians(extraData.netHeadYaw));
                            }
                            else{
                                secondaryArm.setRotationX((float)Math.toRadians(90+ extraData.headPitch));
                                secondaryArm.setRotationY((float)Math.toRadians(extraData.netHeadYaw));
                            }
                        }
                    }
                }
            }
            AnimationUtils.SwingArm(LeftArm, RightArm, Chest, head, entity, customPredicate.getPartialTick());
        }
    }

    public void applyPlayerModelRotation(WeaponPose pose, float headpitch, boolean isLefthanded, IBone left, IBone right,IBone Head, IBone body, float headyaw,float aimProgress) {
        IBone mainArm = isLefthanded ? left:right;
        IBone secondaryArm = isLefthanded ? right:left;
        float angle = headpitch / 90.0F;
        float angleAbs = Math.abs(angle);
        float zoom = ((WeaponPoseAccessor)pose).onHasAimPose() ? aimProgress : 0.0F;
        AimPose targetPose = (double)angle > 0.0 ? ((WeaponPoseAccessor)pose).ongetDownPose() : ((WeaponPoseAccessor)pose).ongetUpPose();
        AimPose forwardpose = ((WeaponPoseAccessor)pose).ongetForwardPose();
        float rightOffset = this.getValue(targetPose.getIdle().getRenderYawOffset(), targetPose.getAiming().getRenderYawOffset(), forwardpose.getIdle().getRenderYawOffset(), forwardpose.getAiming().getRenderYawOffset(), 0.0F, angleAbs, zoom, isLefthanded ? -1.0F : 1.0F);
        body.setRotationY(body.getRotationY()-rightOffset);
        Head.setRotationY(Head.getRotationY()+rightOffset);
        this.applyAimPose(targetPose,forwardpose, mainArm, secondaryArm, angleAbs, zoom, isLefthanded ? -1.0F : 1.0F, rightOffset,false);
    }

    private void applyAimPose(AimPose targetPose,AimPose forwardPose, IBone rightArm, IBone leftArm, float partial, float zoom, float offhand,float rightoffset, boolean sneaking) {
        this.applyLimbPoseToModelRenderer(targetPose.getIdle().getRightArm(), targetPose.getAiming().getRightArm(), forwardPose.getIdle().getRightArm(), forwardPose.getAiming().getRightArm(), rightArm, partial, zoom, offhand, rightoffset, sneaking);
        this.applyLimbPoseToModelRenderer(targetPose.getIdle().getLeftArm(), targetPose.getAiming().getLeftArm(), forwardPose.getIdle().getLeftArm(), forwardPose.getAiming().getLeftArm(), leftArm, partial, zoom, offhand,rightoffset, sneaking);
    }

    private void applyLimbPoseToModelRenderer(LimbPose targetIdlePose, LimbPose targetAimingPose, LimbPose idlePose, LimbPose aimingPose, IBone renderer, float partial, float zoom, float leftHanded,float rightoffset, boolean sneaking) {
        float x = this.getValue(targetIdlePose.getRotationAngleX(), targetAimingPose.getRotationAngleX(), idlePose.getRotationAngleX(), aimingPose.getRotationAngleX(), -renderer.getRotationX(), partial, zoom, 1.0F);
        float y= this.getValue(targetIdlePose.getRotationAngleY(), targetAimingPose.getRotationAngleY(), idlePose.getRotationAngleY(), aimingPose.getRotationAngleY(), -renderer.getRotationY(), partial, zoom, leftHanded);
        float z = this.getValue(targetIdlePose.getRotationAngleZ(), targetAimingPose.getRotationAngleZ(), idlePose.getRotationAngleZ(), aimingPose.getRotationAngleZ(), -renderer.getRotationZ(), partial, zoom, leftHanded);
        x= (float) Math.toRadians(x);
        y = (float) Math.toRadians(y);
        z = (float) Math.toRadians(z);
        renderer.setRotationX(-x);
        renderer.setRotationY(-y);
        renderer.setRotationZ(-z);
        //renderer.setPositionX(16*this.getValue(targetIdlePose.getRotationPointX(), targetAimingPose.getRotationPointX(), idlePose.getRotationPointX(), aimingPose.getRotationPointX(), renderer.getPositionX(), partial, zoom, leftHanded));
        //renderer.setPositionY(16*this.getValue(targetIdlePose.getRotationPointY(), targetAimingPose.getRotationPointY(), idlePose.getRotationPointY(), aimingPose.getRotationPointY(), renderer.getPositionY(), partial, zoom, 1.0F) + (sneaking ? 2.0F : 0.0F));
        //renderer.setPositionZ(16*this.getValue(targetIdlePose.getRotationPointZ(), targetAimingPose.getRotationPointZ(), idlePose.getRotationPointZ(), aimingPose.getRotationPointZ(), renderer.getPositionZ(), partial, zoom, 1.0F));
    }

    private float getValue(@Nullable Float t1, @Nullable Float t2, Float s1, Float s2, Float def, float partial, float zoom, float leftHanded) {

        def = (float) Math.toRadians(def);
        float start = t1 != null && s1 != null ? (s1 + (t1 - s1) * partial) * leftHanded : (s1 != null ? s1 * leftHanded : def);
        float end = t2 != null && s2 != null ? (s2 + (t2 - s2) * partial) * leftHanded : (s2 != null ? s2 * leftHanded : def);
        return Mth.lerp(zoom, start, end);
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
