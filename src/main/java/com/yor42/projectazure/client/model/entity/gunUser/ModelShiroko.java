package com.yor42.projectazure.client.model.entity.gunUser;

import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityShiroko;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import java.util.Random;

import static com.yor42.projectazure.libs.utils.MathUtil.DegreeToRadian;
import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class ModelShiroko extends AnimatedGeoModel<EntityShiroko> {

    private int blinkinterval = 0;
    private final Random random = new Random();

    @Override
    public ResourceLocation getModelLocation(EntityShiroko entityShiroko) {
        return GeoModelEntityLocation("modelshiroko");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityShiroko entityShiroko) {
        return TextureEntityLocation("entityshiroko");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityShiroko entityShiroko) {
        return AnimationLocation("entity/gunuser/shiroko");
    }

    @Override
    public void setLivingAnimations(EntityShiroko entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone Halo = this.getAnimationProcessor().getBone("Halo");
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone NormalFace = this.getAnimationProcessor().getBone("Normal");
        IBone EyeclosedFace = this.getAnimationProcessor().getBone("Eye_Closed");
        IBone ExcitedFace = this.getAnimationProcessor().getBone("Excited");
        IBone PatFace = this.getAnimationProcessor().getBone("Pat");

        if(entity.isBeingPatted()){
            NormalFace.setHidden(true);
            ExcitedFace.setHidden(true);
            PatFace.setHidden(false);
            EyeclosedFace.setHidden(true);
        }
        else {
            if (this.blinkinterval <= 5) {
                NormalFace.setHidden(true);
                ExcitedFace.setHidden(true);
                PatFace.setHidden(true);
                EyeclosedFace.setHidden(false);
                if (this.blinkinterval == 0) {
                    this.blinkinterval = 20 * (random.nextInt(9) + 2);
                }
                this.blinkinterval--;
            } else {
                this.blinkinterval--;
                NormalFace.setHidden(false);
                PatFace.setHidden(true);
                ExcitedFace.setHidden(true);
                EyeclosedFace.setHidden(true);
            }
        }

        Halo.setPositionY((float) (Math.sin(2*Math.PI*0.0125*entity.ticksExisted)*1.0F)%80);

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(!entity.isBeingPatted()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }


    }
}
