package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityNagato;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class nagatoModel extends GeoCompanionModel<EntityNagato> {
    @Override
    public ResourceLocation getModelLocation(EntityNagato entity) {
        return GeoModelEntityLocation("modelnagato");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNagato entity) {
        return TextureEntityLocation("modelnagato");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityNagato entity) {
        return ModResourceLocation("animations/entity/kansen/nagato.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityNagato entity, Integer uniqueID, @Nullable AnimationEvent customPredicate)
    {

        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PoutFace = this.getAnimationProcessor().getBone("Pout");
        IBone Flustered = this.getAnimationProcessor().getBone("Fluster");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone SleepFace = this.getAnimationProcessor().getBone("Sleep");
        IBone Angry = this.getAnimationProcessor().getBone("Angry");
        IBone Faint = this.getAnimationProcessor().getBone("faint");
        IBone Injured = this.getAnimationProcessor().getBone("injured");
        IBone body = this.getAnimationProcessor().getBone("Body");

        super.setLivingAnimations(entity, uniqueID, customPredicate);

        if(entity.isDeadOrDying() || (entity.isCriticallyInjured() && entity.isSleeping())){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(true);
            Flustered.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Angry.setHidden(true);
            Faint.setHidden(false);
            Injured.setHidden(true);
        }
        else if(entity.isCriticallyInjured()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(true);
            Flustered.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Angry.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(false);
        }
        else if(entity.isAngry()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(true);
            Flustered.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Angry.setHidden(false);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(false);
            Flustered.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Angry.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.islewded()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(true);
            Flustered.setHidden(false);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Angry.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isBeingPatted()) {
            NormalFace.setHidden(true);
            PatFace.setHidden(false);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(true);
            Flustered.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Angry.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(true);
            Flustered.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(false);
            Angry.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            body.setPositionY(-45);
            body.setPositionZ(-10);
        }
        else {

            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                NormalFace.setHidden(false);
                PatFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                PoutFace.setHidden(true);
                Flustered.setHidden(true);
                ExcitedFace.setHidden(true);
                Angry.setHidden(true);
                SleepFace.setHidden(true);
                Faint.setHidden(true);
                Injured.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    NormalFace.setHidden(true);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    PoutFace.setHidden(true);
                    Flustered.setHidden(true);
                    ExcitedFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Angry.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    PoutFace.setHidden(true);
                    Flustered.setHidden(true);
                    ExcitedFace.setHidden(true);
                    Angry.setHidden(true);
                    SleepFace.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected int SleepingBodyYPosition() {
        return -27;
    }


}
