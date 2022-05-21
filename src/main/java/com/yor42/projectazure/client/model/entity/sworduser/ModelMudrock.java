package com.yor42.projectazure.client.model.entity.sworduser;

import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityMudrock;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.AnimationUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.GeoModelEntityLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class ModelMudrock extends AnimatedGeoModel<EntityMudrock> {

    private int blinkinterval = 0;
    private long LastBlinkTime = 0;

    @Override
    public ResourceLocation getModelLocation(EntityMudrock object) {
        return GeoModelEntityLocation("modelmudrock");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMudrock object) {
        return TextureEntityLocation("modelmudrock");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMudrock object) {
        return new ResourceLocation(Constants.MODID,"animations/entity/sworduser/mudrock.animation.json");
    }
    @Override
    public void setLivingAnimations(EntityMudrock entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone SleepFace = this.getAnimationProcessor().getBone("Sleeping");
        IBone Flushed = this.getAnimationProcessor().getBone("flushed");
        IBone Angry1 = this.getAnimationProcessor().getBone("angry1");
        IBone Angry2 = this.getAnimationProcessor().getBone("angry2");
        IBone body = this.getAnimationProcessor().getBone("Body");
        IBone Faint = this.getAnimationProcessor().getBone("fainted");
        if(entity.isDeadOrDying() || entity.isCriticallyInjured()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(false);
        }
        else if(entity.isAngry()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(false);
            Faint.setHidden(true);
        }
        else if(entity.getAngerWarningCount() == 2){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(false);
            Angry2.setHidden(true);
            Faint.setHidden(true);
        }
        else if(entity.islewded()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(true);
            Flushed.setHidden(false);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(false);
            SleepFace.setHidden(true);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(true);
            SleepFace.setHidden(false);
            Flushed.setHidden(true);
            Angry1.setHidden(true);
            Angry2.setHidden(true);
            Faint.setHidden(true);
        }
        else {
            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                NormalFace.setHidden(false);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                PatFace.setHidden(true);
                SleepFace.setHidden(true);
                Flushed.setHidden(true);
                Angry1.setHidden(true);
                Angry2.setHidden(true);
                Faint.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    PatFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Faint.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    PatFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Flushed.setHidden(true);
                    Angry1.setHidden(true);
                    Angry2.setHidden(true);
                    Faint.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }


        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(!(entity.isBeingPatted()||entity.isSleeping())) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }

        if(entity.getOwner() != null && entity.getVehicle() == entity.getOwner()) {
            body.setPositionY(body.getPositionY() - 61);
            body.setPositionZ(body.getPositionZ() + 10);

            if(entity.getOwner().isCrouching()){
                body.setPositionZ(body.getPositionZ() + 2);
                body.setPositionY(body.getPositionY() + 2);
                body.setRotationX(MathUtil.DegreeToRadian(90F / (float) Math.PI)*-1);
            }
        }
        else if(entity.isSleeping()){
            body.setPositionY(-35);
        }

        IBone LeftArm = this.getAnimationProcessor().getBone("LeftArm");
        IBone RightArm = this.getAnimationProcessor().getBone("RightArm");
        IBone Chest = this.getAnimationProcessor().getBone("Chest");
        AnimationUtils.SwingArm(LeftArm, RightArm, Chest, head, entity, customPredicate.getPartialTick());
    }

}
