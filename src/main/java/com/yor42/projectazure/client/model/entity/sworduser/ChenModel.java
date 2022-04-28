package com.yor42.projectazure.client.model.entity.sworduser;

import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityChen;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.GeoModelEntityLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class ChenModel extends AnimatedGeoModel<EntityChen> {

    private int blinkinterval = 0;
    private long LastBlinkTime = 0;

    @Override
    public ResourceLocation getModelLocation(EntityChen entityChen) {
        return GeoModelEntityLocation("modelchen");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityChen entityChen) {
        return TextureEntityLocation("modelchen");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityChen entityChen) {
        return new ResourceLocation(Constants.MODID,"animations/entity/sworduser/chen.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityChen entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone anger1 = this.getAnimationProcessor().getBone("lewd1");
        IBone anger2 = this.getAnimationProcessor().getBone("lewd2");
        IBone anger3 = this.getAnimationProcessor().getBone("angry");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone Sleep = this.getAnimationProcessor().getBone("Sleeping");
        IBone Faint = this.getAnimationProcessor().getBone("faint");
        IBone Injured = this.getAnimationProcessor().getBone("injured");

        IBone body = this.getAnimationProcessor().getBone("Body");

        if(entity.isDeadOrDying() || (entity.isCriticallyInjured() && entity.isSleeping())){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(true);
            anger3.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(false);
            Injured.setHidden(true);
        }
        else if(entity.isCriticallyInjured()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(true);
            anger3.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(false);
        }
        else if(entity.isAngry()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(true);
            anger3.setHidden(false);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(false);
            anger3.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isinQinteraction()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(false);
            anger2.setHidden(true);
            anger3.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(false);
            anger1.setHidden(true);
            anger2.setHidden(true);
            anger3.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(true);
            anger3.setHidden(true);
            Sleep.setHidden(false);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else {

            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                NormalFace.setHidden(false);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                anger1.setHidden(true);
                anger2.setHidden(true);
                anger3.setHidden(true);
                Sleep.setHidden(true);
                Faint.setHidden(true);
                Injured.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    PatFace.setHidden(true);
                    anger1.setHidden(true);
                    anger2.setHidden(true);
                    anger3.setHidden(true);
                    Sleep.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    anger1.setHidden(true);
                    anger2.setHidden(true);
                    anger3.setHidden(true);
                    Sleep.setHidden(true);
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
            body.setPositionY(body.getPositionY() - 65);
            body.setPositionZ(body.getPositionZ() + 10);

            if(entity.getOwner().isCrouching()){
                body.setPositionZ(body.getPositionZ() + 2);
                body.setPositionY(body.getPositionY() + 2);
                body.setRotationX(MathUtil.DegreeToRadian(90F / (float) Math.PI)*-1);
            }
        }else if(entity.isSleeping()){
            body.setPositionY(-40);
            body.setPositionZ(-5);
        }

    }
}
