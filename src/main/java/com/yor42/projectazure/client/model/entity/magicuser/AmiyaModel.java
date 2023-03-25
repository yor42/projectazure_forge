package com.yor42.projectazure.client.model.entity.magicuser;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityAmiya;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class AmiyaModel extends GeoCompanionModel<EntityAmiya> {
    @Override
    public ResourceLocation getModelLocation(EntityAmiya entity) {
        return GeoModelEntityLocation("modelamiya");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAmiya entity) {
        return TextureEntityLocation("modelamiya");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAmiya entity) {
        return ModResourceLocation("animations/entity/spelluser/amiya.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityAmiya entity, Integer uniqueID, @Nullable AnimationEvent customPredicate)
    {

        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone Angry = this.getAnimationProcessor().getBone("angry");
        IBone Pout = this.getAnimationProcessor().getBone("Pout");
        IBone Flushed = this.getAnimationProcessor().getBone("Flushed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone fainted = this.getAnimationProcessor().getBone("fainted");
        IBone SleepFace = this.getAnimationProcessor().getBone("Sleep");

        IBone body = this.getAnimationProcessor().getBone("Body");

        IBone diamond = this.getAnimationProcessor().getBone("rangedattackdiamond");

        diamond.setHidden(!entity.isUsingSpell());
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        if(entity.isDeadOrDying() || entity.isCriticallyInjured()){
            fainted.setHidden(false);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Pout.setHidden(true);
            Angry.setHidden(true);
            Flushed.setHidden(true);
        }
        else if(entity.isAngry()){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Pout.setHidden(true);
            Angry.setHidden(false);
            Flushed.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Pout.setHidden(false);
            Angry.setHidden(true);
            Flushed.setHidden(true);
        }
        else if(entity.islewded()){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Pout.setHidden(true);
            Angry.setHidden(true);
            Flushed.setHidden(false);
        }
        else if(entity.isBeingPatted()){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(false);
            SleepFace.setHidden(true);
            Pout.setHidden(true);
            Angry.setHidden(true);
            Flushed.setHidden(true);
        }
        else if(entity.isSleeping()){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(false);
            Pout.setHidden(true);
            Angry.setHidden(true);
            Flushed.setHidden(true);
            body.setPositionY(-30);
            body.setPositionZ(2);
        }
        else {

            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                fainted.setHidden(true);
                NormalFace.setHidden(false);
                PatFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                ExcitedFace.setHidden(true);
                SleepFace.setHidden(true);
                Pout.setHidden(true);
                Angry.setHidden(true);
                Flushed.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    fainted.setHidden(true);
                    NormalFace.setHidden(true);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Pout.setHidden(true);
                    Angry.setHidden(true);
                    Flushed.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    fainted.setHidden(true);
                    NormalFace.setHidden(false);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Pout.setHidden(true);
                    Angry.setHidden(true);
                    Flushed.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }
    }

}
