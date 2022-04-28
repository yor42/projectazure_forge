package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityAyanami;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

public class ayanamiModel extends AnimatedGeoModel<EntityAyanami> {

    private int blinkinterval = 0;
    private long LastBlinkTime = 0;

    @Override
    public ResourceLocation getModelLocation(EntityAyanami entityAyanami) {
        return new ResourceLocation(Constants.MODID, "geo/entity/modelayanami.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAyanami entityAyanami) {
        return new ResourceLocation(Constants.MODID, "textures/entity/modelayanami.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAyanami entityAyanami) {
        return new ResourceLocation(Constants.MODID, "animations/entity/kansen/ayanami.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityAyanami entity, Integer uniqueID, @Nullable AnimationEvent customPredicate)
    {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone Angry = this.getAnimationProcessor().getBone("angry");
        IBone Pout = this.getAnimationProcessor().getBone("pout");
        IBone Flushed = this.getAnimationProcessor().getBone("flushed");
        IBone Faint = this.getAnimationProcessor().getBone("faint");
        IBone Injured = this.getAnimationProcessor().getBone("injured");

        IBone body = this.getAnimationProcessor().getBone("Body");

        if(entity.isDeadOrDying() || (entity.isCriticallyInjured() && entity.isSleeping())){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            Angry.setHidden(true);
            Pout.setHidden(true);
            Flushed.setHidden(true);
            Faint.setHidden(false);
            Injured.setHidden(true);
        }
        else if(entity.isCriticallyInjured()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            Angry.setHidden(true);
            Pout.setHidden(true);
            Flushed.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(false);
        }
        else if(entity.isAngry()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            Angry.setHidden(false);
            Pout.setHidden(true);
            Flushed.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(false);
            EyeclosedFace.setHidden(true);
            Angry.setHidden(true);
            Pout.setHidden(false);
            Flushed.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isinQinteraction()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            Angry.setHidden(true);
            Pout.setHidden(true);
            Flushed.setHidden(false);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(false);
            EyeclosedFace.setHidden(true);
            Angry.setHidden(true);
            Pout.setHidden(true);
            Flushed.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);

        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(false);
            Angry.setHidden(true);
            Pout.setHidden(true);
            Flushed.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else {
            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                NormalFace.setHidden(false);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                Angry.setHidden(true);
                Pout.setHidden(true);
                Flushed.setHidden(true);
                Faint.setHidden(true);
                Injured.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    Angry.setHidden(true);
                    Pout.setHidden(true);
                    Flushed.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    Angry.setHidden(true);
                    Pout.setHidden(true);
                    Flushed.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }

        if(customPredicate != null) {
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            if (!(entity.isBeingPatted() || entity.isSleeping())) {
                head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
                head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
            }
        }

        if(entity.getOwner() != null && entity.getVehicle() == entity.getOwner()) {
            body.setPositionY(body.getPositionY() - 60);
            body.setPositionZ(body.getPositionZ() + 10);

            if(entity.getOwner().isCrouching()){
                body.setPositionZ(body.getPositionZ() + 2);
                body.setPositionY(body.getPositionY() + 2);
                body.setRotationX(MathUtil.DegreeToRadian(90F / (float) Math.PI)*-1);
            }
        }
        else if(entity.isSleeping()){
            body.setPositionY(-33);
            body.setPositionZ(-5);
        }
    }
}
