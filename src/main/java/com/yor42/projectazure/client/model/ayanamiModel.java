package com.yor42.projectazure.client.model;

import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import com.yor42.projectazure.libs.defined;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;
import java.util.Random;

public class ayanamiModel extends AnimatedGeoModel<EntityAyanami> {

    private int blinkinterval = 0;
    private Random random = new Random();


    @Override
    public ResourceLocation getModelLocation(EntityAyanami entityAyanami) {
        return new ResourceLocation(defined.MODID, "geo/entity/modelayanami.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAyanami entityAyanami) {
        return new ResourceLocation(defined.MODID, "textures/entity/modelayanami.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAyanami entityAyanami) {
        return new ResourceLocation(defined.MODID, "animations/entity/kansen/ayanami.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityAyanami entity, Integer uniqueID, @Nullable AnimationEvent customPredicate)
    {

        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone RightArm = this.getAnimationProcessor().getBone("RightArm");
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone LeftArm = this.getAnimationProcessor().getBone("LeftArm");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        if(!entity.isSailing()){
            LeftArm.setRotationX(MathHelper.cos(entity.limbSwing * 0.6662F) * 0.8F * entity.limbSwingAmount);
            RightArm.setRotationX(MathHelper.cos(entity.limbSwing * 0.6662F + (float) Math.PI) * 0.8F * entity.limbSwingAmount);
        }

        if(this.blinkinterval == 0){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(false);
            this.blinkinterval = random.nextInt(9);
        }
        else{
            --this.blinkinterval;
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            EyeclosedFace.setHidden(false);
        }


        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float)Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
        NormalFace.setHidden(false);
        ExcitedFace.setHidden(true);
        EyeclosedFace.setHidden(true);

    }
}
