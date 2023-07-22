package com.yor42.projectazure.client.model.entity.gunUser;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityRabu;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntitySaori;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class ModelRabu extends GeoCompanionModel<EntityRabu> {
    @Override
    public ResourceLocation getModelLocation(EntityRabu object) {
        return GeoModelEntityLocation("modelrabu");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRabu object) {
        return TextureEntityLocation("modelrabu");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityRabu animatable) {
        return AnimationLocation("entity/gunuser/rabu");
    }

    @Override
    public void setCustomAnimations(EntityRabu entity, int uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);

        IBone Halo = this.getAnimationProcessor().getBone("halo");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_closed");
        IBone Injured = this.getAnimationProcessor().getBone("Injured");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone SleepFace = this.getAnimationProcessor().getBone("Sleeping");
        IBone Flushed = this.getAnimationProcessor().getBone("flushed");
        IBone Angry1 = this.getAnimationProcessor().getBone("angry1");
        IBone Angry2 = this.getAnimationProcessor().getBone("angry2");
        IBone Angry3 = this.getAnimationProcessor().getBone("angry3");
        IBone Faint = this.getAnimationProcessor().getBone("Faint");

        if(entity.isDeadOrDying() ||(entity.isCriticallyInjured() && entity.isSleeping())){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            SleepFace.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(false);
            Injured.setHidden(true);
        }
        else if(entity.isCriticallyInjured()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            SleepFace.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(false);
        }
        else if(entity.isAngry()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            SleepFace.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(false);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            SleepFace.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(false);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.islewded()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            SleepFace.setHidden(true);
            Angry1.setHidden(false);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(false);
            EyeclosedFace.setHidden(true);
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isGettingHealed()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            SleepFace.setHidden(false);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            SleepFace.setHidden(false);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else {
            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                NormalFace.setHidden(false);
                PatFace.setHidden(true);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                SleepFace.setHidden(true);
                Flushed.setHidden(true);
                Angry1.setHidden(true);
                Angry2.setHidden(true);
                Angry3.setHidden(true);
                Faint.setHidden(true);
                Injured.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    SleepFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Angry3.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    PatFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Angry3.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);

                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }
        Halo.setHidden(entity.isSleeping() || entity.isCriticallyInjured());
        Halo.setPositionY((float) (Math.sin(2*Math.PI*0.0125*entity.tickCount)*1.0F)%80);
    }
}
