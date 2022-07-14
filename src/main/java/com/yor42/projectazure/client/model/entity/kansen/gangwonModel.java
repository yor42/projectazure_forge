package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityGangwon;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.AnimationUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.GeoModelEntityLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class gangwonModel extends GeoCompanionModel<EntityGangwon> {

    @Override
    public ResourceLocation getModelLocation(EntityGangwon entityGangwon) {
        return GeoModelEntityLocation("modelgangwon");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGangwon entityGangwon) {
        return TextureEntityLocation("modelgangwon");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityGangwon entityGangwon) {
        return new ResourceLocation(Constants.MODID,"animations/entity/kansen/gangwon.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityGangwon entity, Integer uniqueID, @Nullable AnimationEvent customPredicate)
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
        IBone faint = this.getAnimationProcessor().getBone("faint");

        IBone body = this.getAnimationProcessor().getBone("Body");

        super.setLivingAnimations(entity, uniqueID, customPredicate);

        if(entity.isDeadOrDying() || entity.isCriticallyInjured()){
            faint.setHidden(false);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            PoutFace.setHidden(true);
            Angry.setHidden(true);
        }
        else if(entity.isAngry()){
            faint.setHidden(true);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            PoutFace.setHidden(true);
            Angry.setHidden(false);
        }
        else if(entity.getAngerWarningCount() == 2){
            faint.setHidden(true);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(false);
            Flustered.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Angry.setHidden(true);
        }
        else if(entity.islewded()){
            faint.setHidden(true);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(true);
            Flustered.setHidden(false);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(true);
            Angry.setHidden(true);
        }
        else if(entity.isBeingPatted()){
            if(entity.isOrderedToSit()) {
                faint.setHidden(true);
                NormalFace.setHidden(true);
                PatFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                PoutFace.setHidden(true);
                Flustered.setHidden(false);
                ExcitedFace.setHidden(true);
                SleepFace.setHidden(true);
                Angry.setHidden(true);
            }
            else {
                faint.setHidden(true);
                NormalFace.setHidden(true);
                PatFace.setHidden(false);
                EyeclosedFace.setHidden(true);
                PoutFace.setHidden(true);
                Flustered.setHidden(true);
                ExcitedFace.setHidden(true);
                SleepFace.setHidden(true);
                Angry.setHidden(true);
            }
        }
        else if(entity.isSleeping()){
            faint.setHidden(true);
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(true);
            Flustered.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(false);
            Angry.setHidden(true);
            body.setPositionY(-20);
        }
        else {
            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
                faint.setHidden(true);
                NormalFace.setHidden(false);
                PatFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                PoutFace.setHidden(true);
                Flustered.setHidden(true);
                ExcitedFace.setHidden(true);
                Angry.setHidden(true);
                SleepFace.setHidden(true);
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    faint.setHidden(true);
                    NormalFace.setHidden(true);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    PoutFace.setHidden(true);
                    Flustered.setHidden(true);
                    ExcitedFace.setHidden(true);
                    SleepFace.setHidden(true);
                    Angry.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    faint.setHidden(true);
                    NormalFace.setHidden(false);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    PoutFace.setHidden(true);
                    Flustered.setHidden(true);
                    ExcitedFace.setHidden(true);
                    Angry.setHidden(true);
                    SleepFace.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }


        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(!(entity.isBeingPatted() || entity.isSleeping())) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }

        if(entity.getOwner() != null && entity.getVehicle() == entity.getOwner()) {
            body.setPositionY(body.getPositionY() - 50);
            body.setPositionZ(body.getPositionZ() + 10);

            if(entity.getOwner().isCrouching()){
                body.setPositionZ(body.getPositionZ() + 2);
                body.setPositionY(body.getPositionY() + 2);
                body.setRotationX(MathUtil.DegreeToRadian(90F / (float) Math.PI)*-1);
            }
        }
        else if(entity.isSleeping()){
            body.setPositionY(-23);
            body.setPositionZ(-5);
        }

        IBone LeftArm = this.getAnimationProcessor().getBone("LeftArm");
        IBone RightArm = this.getAnimationProcessor().getBone("RightArm");
        IBone Chest = this.getAnimationProcessor().getBone("Chest");
        AnimationUtils.SwingArm(LeftArm, RightArm, Chest, head, entity, customPredicate.getPartialTick());
    }

    @Override
    protected int SleepingBodyYPosition() {
        return -23;
    }

}
