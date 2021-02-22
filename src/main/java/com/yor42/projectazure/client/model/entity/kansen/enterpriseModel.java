package com.yor42.projectazure.client.model.entity.kansen;

import com.yor42.projectazure.gameobject.entity.EntityEnterprise;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class enterpriseModel extends AnimatedGeoModel<EntityEnterprise> {

    private int blinkinterval = 0;

    @Override
    public ResourceLocation getModelLocation(EntityEnterprise entityEnterprise) {
        return GeoModelEntityLocation("modelenterprise");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityEnterprise entityEnterprise) {
        return TextureEntityLocation("modelenterprise");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityEnterprise entityEnterprise) {
        return AnimationEntityKansenLocation("enterprise");
    }

    @Override
    public void setLivingAnimations(EntityEnterprise entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone RightArm = this.getAnimationProcessor().getBone("RightArm");
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone LeftArm = this.getAnimationProcessor().getBone("LeftArm");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");

        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if(!entity.isSailing()){
            LeftArm.setRotationX(MathHelper.cos(entity.limbSwing * 0.6662F) * 0.8F * entity.limbSwingAmount);
            RightArm.setRotationX(MathHelper.cos(entity.limbSwing * 0.6662F + (float) Math.PI) * 0.8F * entity.limbSwingAmount);
        }

        if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(true);
            PatFace.setHidden(false);
        }
        else {
            if (this.blinkinterval <= 5) {
                NormalFace.setHidden(true);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(false);
                PatFace.setHidden(true);
                if (this.blinkinterval == 0) {
                    this.blinkinterval = 20 * (getRand().nextInt(9) + 2);
                }
                this.blinkinterval--;
            } else {
                this.blinkinterval--;
                NormalFace.setHidden(false);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
                PatFace.setHidden(true);
            }
        }


        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(!entity.isBeingPatted()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
