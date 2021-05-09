package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityGangwon;
import com.yor42.projectazure.libs.defined;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;
import java.util.Random;

import static com.yor42.projectazure.libs.utils.ResourceUtils.GeoModelEntityLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class gangwonModel extends AnimatedGeoModel<EntityGangwon> {

    private int blinkinterval = 0;
    private final Random random = new Random();

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
        return new ResourceLocation(defined.MODID,"animations/entity/kansen/gangwon.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityGangwon entity, Integer uniqueID, @Nullable AnimationEvent customPredicate)
    {

        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PoutFace = this.getAnimationProcessor().getBone("Tsun");
        IBone Flustered = this.getAnimationProcessor().getBone("Fluster");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone SleepFace = this.getAnimationProcessor().getBone("Sleep");

        IBone body = this.getAnimationProcessor().getBone("Body");

        super.setLivingAnimations(entity, uniqueID, customPredicate);

        if(entity.isBeingPatted()){
            if(entity.isEntitySleeping()){
                    NormalFace.setHidden(true);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    PoutFace.setHidden(true);
                    PoutFace.setHidden(true);
                    Flustered.setHidden(false);
                    ExcitedFace.setHidden(true);
                    SleepFace.setHidden(true);
            }
            else {
                NormalFace.setHidden(true);
                PatFace.setHidden(false);
                EyeclosedFace.setHidden(true);
                PoutFace.setHidden(true);
                PoutFace.setHidden(true);
                Flustered.setHidden(true);
                ExcitedFace.setHidden(true);
                SleepFace.setHidden(true);
            }
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PoutFace.setHidden(true);
            Flustered.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(false);

            body.setPositionY(-20);
            body.setPositionZ(-10);
        }
        else {
            if (this.blinkinterval <= 5) {
                NormalFace.setHidden(true);
                PatFace.setHidden(true);
                EyeclosedFace.setHidden(false);
                PoutFace.setHidden(true);
                Flustered.setHidden(true);
                ExcitedFace.setHidden(true);
                SleepFace.setHidden(true);
                if (this.blinkinterval == 0) {
                    this.blinkinterval = 20 * (random.nextInt(9) + 2);
                }
                this.blinkinterval--;
            } else {
                this.blinkinterval--;
                NormalFace.setHidden(false);
                PatFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                PoutFace.setHidden(true);
                Flustered.setHidden(true);
                ExcitedFace.setHidden(true);
                SleepFace.setHidden(true);
            }
        }


        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(!(entity.isBeingPatted() || entity.isSleeping())) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
