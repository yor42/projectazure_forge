package com.yor42.projectazure.client.model.entity.sworduser;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityTexas;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.GeoModelEntityLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class TexasModel extends GeoCompanionModel<EntityTexas> {

    @Override
    public ResourceLocation getModelLocation(EntityTexas object) {
        return GeoModelEntityLocation("modeltexas");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTexas object) {
        return TextureEntityLocation("modeltexas");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityTexas animatable) {
        return new ResourceLocation(Constants.MODID,"animations/entity/sworduser/texas.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityTexas entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone flushed = this.getAnimationProcessor().getBone("flushed");
        IBone anger1 = this.getAnimationProcessor().getBone("angry1");
        IBone anger2 = this.getAnimationProcessor().getBone("angry2");
        IBone anger3 = this.getAnimationProcessor().getBone("angry3");
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
            anger3.setHidden(true);
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
            anger2.setHidden(true);
            anger3.setHidden(false);
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
            anger1.setHidden(true);
            anger2.setHidden(false);
            flushed.setHidden(true);
            anger3.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.islewded()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(false);
            anger2.setHidden(true);
            flushed.setHidden(true);
            anger3.setHidden(true);
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
            flushed.setHidden(true);
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
                flushed.setHidden(true);
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
                    flushed.setHidden(true);
                    anger1.setHidden(true);
                    anger2.setHidden(true);
                    anger3.setHidden(true);
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
                    anger3.setHidden(true);
                    Sleep.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }
    }

}
