package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityLaffey;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

public class laffeyModel extends GeoCompanionModel<EntityLaffey> {
    @Override
    public ResourceLocation getModelLocation(EntityLaffey object) {
        return new ResourceLocation(Constants.MODID, "geo/entity/modellaffey.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLaffey object) {
        return new ResourceLocation(Constants.MODID, "textures/entity/modellaffey.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityLaffey animatable) {
        return new ResourceLocation(Constants.MODID, "animations/entity/kansen/laffey.animation.json");
    }

    @Override
    public void setCustomAnimations(EntityLaffey entity, int uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone SleepingFace = this.getAnimationProcessor().getBone("Sleeping");
        IBone Flushed = this.getAnimationProcessor().getBone("Flushed");
        IBone Angry1 = this.getAnimationProcessor().getBone("Angry1");
        IBone Angry2 = this.getAnimationProcessor().getBone("Angry2");
        IBone faint = this.getAnimationProcessor().getBone("faint");
        IBone body = this.getAnimationProcessor().getBone("Body");

        if(entity.isDeadOrDying() || entity.isCriticallyInjured()){
            faint.setHidden(false);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepingFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
        }
        else if(entity.isAngry()){
            faint.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepingFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(false);
        }
        else if(entity.getAngerWarningCount() == 2){
            faint.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepingFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(false);
            Angry2.setHidden(true);
        }
        else if(entity.islewded()){
            faint.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepingFace.setHidden(true);
            Flushed.setHidden(false);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            faint.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(false);
            EyeclosedFace.setHidden(true);
            SleepingFace.setHidden(true);
            Flushed.setHidden(false);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
        }
        else if(entity.isSleeping()){
            faint.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepingFace.setHidden(false);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            body.setPositionY(-45);
        }
        else {
            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    faint.setHidden(true);
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    PatFace.setHidden(true);
                    SleepingFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    faint.setHidden(true);
                    NormalFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    PatFace.setHidden(true);
                    SleepingFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }
    }

}
