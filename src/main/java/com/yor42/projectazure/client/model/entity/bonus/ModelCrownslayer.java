package com.yor42.projectazure.client.model.entity.bonus;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityCrownSlayer;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.GeoModelEntityLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class ModelCrownslayer extends GeoCompanionModel<EntityCrownSlayer> {

    @Override
    public ResourceLocation getModelLocation(EntityCrownSlayer object) {
        return GeoModelEntityLocation("modelcrownslayer");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCrownSlayer object) {
        return TextureEntityLocation("modelcrownslayer");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityCrownSlayer animatable) {
        return new ResourceLocation(Constants.MODID,"animations/entity/bonus/crownslayer.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityCrownSlayer entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone EarR = this.getAnimationProcessor().getBone("EarR");
        IBone EarL = this.getAnimationProcessor().getBone("EarL");
        IBone hood = this.getAnimationProcessor().getBone("hood");
        IBone head = this.getAnimationProcessor().getBone("Head");
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
        IBone body = this.getAnimationProcessor().getBone("Body");
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

        if (entity.isWearingHood()) {
            EarL.setHidden(true);
            EarR.setHidden(true);
            hood.setHidden(false);
        } else {
            EarL.setHidden(false);
            EarR.setHidden(false);
            hood.setHidden(true);
        }
    }

    @Override
    protected int SleepingBodyYPosition() {
        return -35;
    }
}
