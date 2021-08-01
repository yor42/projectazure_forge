package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityJavelin;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityZ23;
import com.yor42.projectazure.libs.defined;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

public class Z23Model extends AnimatedGeoModel<EntityZ23> {
    private int blinkinterval = 0;
    private long LastBlinkTime = 0;

    @Override
    public ResourceLocation getModelLocation(EntityZ23 object) {
        return new ResourceLocation(defined.MODID, "geo/entity/modelnimi.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityZ23 object) {
        return new ResourceLocation(defined.MODID, "textures/entity/entitynimi.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityZ23 animatable) {
        return new ResourceLocation(defined.MODID, "animations/entity/kansen/nimi.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityZ23 entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");

        IBone body = this.getAnimationProcessor().getBone("Body");


        if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(false);
            EyeclosedFace.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(false);
            PatFace.setHidden(true);
            body.setPositionY(-45);
            body.setPositionZ(-10);
        }
        else {
            if(this.LastBlinkTime == 0){
                this.LastBlinkTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - this.LastBlinkTime>=this.blinkinterval) {
                if(EyeclosedFace.isHidden()){
                    NormalFace.setHidden(true);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    PatFace.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    PatFace.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*1000)+3000);
                }
                this.LastBlinkTime = System.currentTimeMillis();
            }
        }

        if(customPredicate != null) {
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            if (!(entity.isBeingPatted() || entity.isSleeping())) {
                head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
                head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
            }

        }
    }
}
