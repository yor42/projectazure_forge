package com.yor42.projectazure.client.model.entity.magicuser;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityNagato;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityAmiya;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class AmiyaModel extends AnimatedGeoModel<EntityAmiya> {

    private int blinkinterval = 0;
    private long LastBlinkTime = 0;

    @Override
    public ResourceLocation getModelLocation(EntityAmiya entity) {
        return GeoModelEntityLocation("modelamiya");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAmiya entity) {
        return TextureEntityLocation("modelamiya");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAmiya entity) {
        return ModResourceLocation("animations/entity/spelluser/amiya.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityAmiya entity, Integer uniqueID, @Nullable AnimationEvent customPredicate)
    {

        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");
        IBone SleepFace = this.getAnimationProcessor().getBone("Sleep");

        IBone body = this.getAnimationProcessor().getBone("Body");

        IBone diamond = this.getAnimationProcessor().getBone("rangedattackdiamond");

        diamond.setHidden(!entity.isUsingSpell());

        super.setLivingAnimations(entity, uniqueID, customPredicate);

        if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(false);
            SleepFace.setHidden(true);
        }
        else if(entity.isSleeping()){
            NormalFace.setHidden(true);
            PatFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            ExcitedFace.setHidden(true);
            SleepFace.setHidden(false);

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
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(false);
                    ExcitedFace.setHidden(true);
                    SleepFace.setHidden(true);
                    this.blinkinterval = (int) ((getRand().nextFloat()*300)+100);
                }
                else{
                    NormalFace.setHidden(false);
                    PatFace.setHidden(true);
                    EyeclosedFace.setHidden(true);
                    ExcitedFace.setHidden(true);
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
    }

}
