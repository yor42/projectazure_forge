package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityAyanami;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

public class ayanamiModel extends GeoCompanionModel<EntityAyanami> {

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
    public void setCustomAnimations(EntityAyanami entity, int uniqueID, @Nullable AnimationEvent customPredicate)
    {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
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
        else if(entity.islewded()){
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
    }

}
