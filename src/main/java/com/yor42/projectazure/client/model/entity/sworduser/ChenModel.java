package com.yor42.projectazure.client.model.entity.sworduser;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityChen;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.GeoModelEntityLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class ChenModel extends GeoCompanionModel<EntityChen> {
    @Override
    public ResourceLocation getModelLocation(EntityChen entityChen) {
        return GeoModelEntityLocation("modelchen_remaster");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityChen entityChen) {
        return TextureEntityLocation("modelchen_remaster");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityChen entityChen) {
        return new ResourceLocation(Constants.MODID,"animations/entity/sworduser/chen.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityChen entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone anger1 = this.getAnimationProcessor().getBone("angry1");
        IBone anger2 = this.getAnimationProcessor().getBone("angry2");
        IBone anger3 = this.getAnimationProcessor().getBone("angry3");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone Sleep = this.getAnimationProcessor().getBone("Sleeping");
        IBone Faint = this.getAnimationProcessor().getBone("Faint");
        IBone Injured = this.getAnimationProcessor().getBone("Injured");

        IBone flushed = this.getAnimationProcessor().getBone("flushed");

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
            Injured.setHidden(true);
            flushed.setHidden(true);
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
            Faint.setHidden(true);
            Injured.setHidden(false);
            flushed.setHidden(true);
        }
        else if(entity.isAngry()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(true);
            anger3.setHidden(false);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            flushed.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(false);
            anger3.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            flushed.setHidden(true);
        }
        else if(entity.islewded()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(false);
            anger2.setHidden(true);
            anger3.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            flushed.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(false);
            anger1.setHidden(true);
            anger2.setHidden(true);
            anger3.setHidden(true);
            Sleep.setHidden(true);
            Faint.setHidden(true);
            Injured.setHidden(true);
            flushed.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            anger1.setHidden(true);
            anger2.setHidden(true);
            anger3.setHidden(true);
            Sleep.setHidden(false);
            Faint.setHidden(true);
            Injured.setHidden(true);
            flushed.setHidden(true);
        }
        else {

            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                NormalFace.setHidden(false);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                anger1.setHidden(true);
                anger2.setHidden(true);
                anger3.setHidden(true);
                Sleep.setHidden(true);
                Faint.setHidden(true);
                Injured.setHidden(true);
                flushed.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    PatFace.setHidden(true);
                    anger1.setHidden(true);
                    anger2.setHidden(true);
                    anger3.setHidden(true);
                    Sleep.setHidden(true);
                    Faint.setHidden(true);
                    Injured.setHidden(true);
                    flushed.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    anger1.setHidden(true);
                    anger2.setHidden(true);
                    anger3.setHidden(true);
                    Sleep.setHidden(true);
                    Faint.setHidden(true);
                    flushed.setHidden(true);
                    Injured.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected int SleepingBodyYPosition() {
        return -40;
    }

}
