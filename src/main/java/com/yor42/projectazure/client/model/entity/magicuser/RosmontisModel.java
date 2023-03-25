package com.yor42.projectazure.client.model.entity.magicuser;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class RosmontisModel extends GeoCompanionModel<EntityRosmontis> {

    @Override
    public ResourceLocation getModelLocation(EntityRosmontis object) {
        return GeoModelEntityLocation("modelrosmontis");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRosmontis object) {
        return TextureEntityLocation("modelrosmontis");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityRosmontis animatable) {
        return ModResourceLocation("animations/entity/spelluser/rosmontis.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityRosmontis entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone Surprised = this.getAnimationProcessor().getBone("surprised");
        IBone SleepFace = this.getAnimationProcessor().getBone("Sleep");
        IBone Angry1 = this.getAnimationProcessor().getBone("angry1");
        IBone Angry2 = this.getAnimationProcessor().getBone("angry2");
        IBone Faint = this.getAnimationProcessor().getBone("faint");
        IBone Injured = this.getAnimationProcessor().getBone("injured");

        IBone body = this.getAnimationProcessor().getBone("Body");

        super.setLivingAnimations(entity, uniqueID, customPredicate);

        if(entity.isDeadOrDying()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Surprised.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(false);
            Injured.setHidden(true);
        }
        else if(entity.isCriticallyInjured()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Surprised.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(false);
        }
        if(entity.isAngry()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Surprised.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(false);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Surprised.setHidden(true);
            Angry1.setHidden(false);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.islewded()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Surprised.setHidden(false);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(false);
            SleepFace.setHidden(true);
            Surprised.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(false);
            Surprised.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else {
            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                NormalFace.setHidden(false);
                PatFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                ExcitedFace.setHidden(true);
                SleepFace.setHidden(true);
                Surprised.setHidden(true);
                Angry1.setHidden(true);
                Angry2.setHidden(true);
                Faint.setHidden(true);
                Injured.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    NormalFace.setHidden(true);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Surprised.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Surprised.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }
    }

}
