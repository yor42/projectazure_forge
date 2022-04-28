package com.yor42.projectazure.client.model.entity.magicuser;

import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntitySylvi;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class SylviModel extends AnimatedGeoModel<EntitySylvi> {

    private int blinkinterval = 0;
    private long LastBlinkTime = 0;

    @Override
    public ResourceLocation getModelLocation(EntitySylvi object) {
        return GeoModelEntityLocation("modelsylvi");
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySylvi object) {
        return TextureEntityLocation("modelsylvi");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySylvi animatable) {
        return ModResourceLocation("animations/entity/spelluser/sylvi.animation.json");
    }

    @Override
    public void setLivingAnimations(EntitySylvi entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone body = this.getAnimationProcessor().getBone("Body");
        IBone HealFace = this.getAnimationProcessor().getBone("Heal");
        IBone Flushed = this.getAnimationProcessor().getBone("flushed");
        IBone Angry1 = this.getAnimationProcessor().getBone("angry1");
        IBone Angry2 = this.getAnimationProcessor().getBone("angry2");
        IBone Faint = this.getAnimationProcessor().getBone("faint");
        IBone Injured = this.getAnimationProcessor().getBone("injured");
        IBone Sleep = this.getAnimationProcessor().getBone("Sleep");
        IBone head = this.getAnimationProcessor().getBone("Head");

        if(entity.isDeadOrDying() ||(entity.isCriticallyInjured() && entity.isSleeping())){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            HealFace.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(false);
            Injured.setHidden(true);
            Sleep.setHidden(true);
        }
        else if(entity.isCriticallyInjured()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            HealFace.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(false);
            Sleep.setHidden(true);
        }
        else if(entity.isAngry()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            HealFace.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(false);
            Faint.setHidden(true);
            Injured.setHidden(true);
            Sleep.setHidden(true);
            Sleep.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            HealFace.setHidden(true);
            Angry1.setHidden(false);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            Sleep.setHidden(true);
        }
        else if(entity.isinQinteraction()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(false);
            HealFace.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            Sleep.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(false);
            EyeclosedFace.setHidden(true);
            HealFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            Sleep.setHidden(true);
        }
        else if(entity.isGettingHealed()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            HealFace.setHidden(false);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            Sleep.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            HealFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            Sleep.setHidden(false);
        }
        else {
            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                NormalFace.setHidden(false);
                PatFace.setHidden(true);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                HealFace.setHidden(true);
                Flushed.setHidden(true);
                Angry1.setHidden(true);
                Angry2.setHidden(true);
                Faint.setHidden(true);
                Injured.setHidden(true);
                Sleep.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    HealFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    Sleep.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    PatFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    HealFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Faint.setHidden(true);
                    Sleep.setHidden(true);
                    Injured.setHidden(true);

                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();

            }
        }
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(!(entity.isBeingPatted()||entity.isSleeping())) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }

        if(entity.getOwner() != null && entity.getVehicle() == entity.getOwner()) {
            body.setPositionY(body.getPositionY() - 60);
            body.setPositionZ(body.getPositionZ() + 10);
            if(entity.getOwner().isCrouching()){
                body.setPositionZ(body.getPositionZ() + 2);
                body.setPositionY(body.getPositionY() + 2);
                body.setRotationX(MathUtil.DegreeToRadian(90F / (float) Math.PI)*-1);
            }
        }else if(entity.isSleeping()){
            body.setPositionY(-38);
        }
    }
}
