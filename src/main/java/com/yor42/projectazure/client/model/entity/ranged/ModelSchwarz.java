package com.yor42.projectazure.client.model.entity.ranged;

import com.yor42.projectazure.gameobject.entity.companion.ranged.EntitySchwarz;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityTexas;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.GeoModelEntityLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class ModelSchwarz extends AnimatedGeoModel<EntitySchwarz> {
    private int blinkinterval = 0;
    private long LastBlinkTime = 0;

    @Override
    public ResourceLocation getModelLocation(EntitySchwarz object) {
        return GeoModelEntityLocation("modelschwarz");
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySchwarz object) {
        return TextureEntityLocation("modelschwarz");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySchwarz animatable) {
        return new ResourceLocation(Constants.MODID,"animations/entity/ranged/schwarz.animation.json");
    }

    @Override
    public void setLivingAnimations(EntitySchwarz entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone flushed = this.getAnimationProcessor().getBone("flushed");
        IBone anger1 = this.getAnimationProcessor().getBone("angry1");
        IBone anger2 = this.getAnimationProcessor().getBone("angry2");
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
            Sleep.setHidden(true);
            Faint.setHidden(false);
            flushed.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isCriticallyInjured()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(true);
            Sleep.setHidden(true);
            flushed.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(false);
        }
        else if(entity.isAngry()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(false);
            flushed.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(false);
            anger2.setHidden(true);
            flushed.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isinQinteraction()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(true);
            flushed.setHidden(false);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            flushed.setHidden(true);
            PatFace.setHidden(false);
            anger1.setHidden(true);
            anger2.setHidden(true);

            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            flushed.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(true);

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
                flushed.setHidden(true);
                anger1.setHidden(true);
                anger2.setHidden(true);

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
                    flushed.setHidden(true);
                    anger1.setHidden(true);
                    anger2.setHidden(true);

                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    Sleep.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    flushed.setHidden(true);
                    anger1.setHidden(true);
                    anger2.setHidden(true);

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
            body.setPositionY(body.getPositionY() - 63);
            body.setPositionZ(body.getPositionZ() + 12);

            if(entity.getOwner().isCrouching()){
                body.setPositionZ(body.getPositionZ() + 2);
                body.setPositionY(body.getPositionY() + 2);
                body.setRotationX(MathUtil.DegreeToRadian(90F / (float) Math.PI)*-1);
            }
        }
        else if(entity.isSleeping()){
            body.setPositionY(-36);
        }
    }
}
