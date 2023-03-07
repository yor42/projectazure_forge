package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityZ23;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

public class Z23Model extends GeoCompanionModel<EntityZ23> {

    @Override
    public ResourceLocation getModelLocation(EntityZ23 object) {
        return new ResourceLocation(Constants.MODID, "geo/entity/modelnimi.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityZ23 object) {
        return new ResourceLocation(Constants.MODID, "textures/entity/entitynimi.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityZ23 animatable) {
        return new ResourceLocation(Constants.MODID, "animations/entity/kansen/nimi.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityZ23 entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone Flushed = this.getAnimationProcessor().getBone("Flushed");
        IBone Angry1 = this.getAnimationProcessor().getBone("Angry1");
        IBone Angry2 = this.getAnimationProcessor().getBone("Angry2");
        IBone fainted = this.getAnimationProcessor().getBone("faint");
        IBone body = this.getAnimationProcessor().getBone("Body");
        if(entity.isDeadOrDying() || entity.isCriticallyInjured()){
            fainted.setHidden(false);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
        }
        else if(entity.isAngry()){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(false);
        }
        else if(entity.getAngerWarningCount() == 2){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(false);
            Angry2.setHidden(true);
        }
        else if(entity.islewded()){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            Flushed.setHidden(false);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(false);
            EyeclosedFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
        }
        else if(entity.isSleeping()){
            fainted.setHidden(true);
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(false);
            PatFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
        }
        else {
            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                fainted.setHidden(true);
                NormalFace.setHidden(false);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                PatFace.setHidden(true);
                Flushed.setHidden(true);
                Angry1.setHidden(true);
                Angry2.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    fainted.setHidden(true);
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    PatFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    fainted.setHidden(true);
                    NormalFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    PatFace.setHidden(true);
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
