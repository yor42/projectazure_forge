package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityPurifier;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

public class PurifierModel extends GeoCompanionModel<EntityPurifier> {

    @Override
    public ResourceLocation getModelLocation(EntityPurifier object) {
        return new ResourceLocation(Constants.MODID, "geo/entity/modelpurifier.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPurifier object) {
        return new ResourceLocation(Constants.MODID, "textures/entity/modelpurifier.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityPurifier animatable) {
        return new ResourceLocation(Constants.MODID, "animations/entity/kansen/purifier.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityPurifier entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
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
        if (entity.isDeadOrDying() || entity.isCriticallyInjured()) {
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
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
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(false);
        }
        else if (entity.isAngry()) {
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(false);
            Faint.setHidden(true);
            Injured.setHidden(true);
        } else if (entity.getAngerWarningCount() == 2) {
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(false);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        } else if (entity.islewded()) {
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(true);
            Flushed.setHidden(false);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        } else if (entity.isBeingPatted()) {
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(false);
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        } else if (entity.isSleeping()) {
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(false);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Angry3.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        } else {
            if (this.LastBlinkTime == 0) {
                this.LastBlinkTime = System.currentTimeMillis();
                NormalFace.setHidden(false);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                PatFace.setHidden(true);
                SleepFace.setHidden(true);
                Flushed.setHidden(true);
                Angry1.setHidden(true);
                Angry2.setHidden(true);
                Angry3.setHidden(true);
                Faint.setHidden(true);
                Injured.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime >= this.blinkinterval) {
                if (EyeclosedFace.isHidden()) {
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    PatFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Angry3.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat() * 300) + 100);
                } else {
                    NormalFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    PatFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Angry3.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat() * 1000) + 3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }
    }
}
