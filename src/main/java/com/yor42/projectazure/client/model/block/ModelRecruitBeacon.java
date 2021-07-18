package com.yor42.projectazure.client.model.block;

import com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityMetalPress;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityRecruitBeacon;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;
import java.util.List;

public class ModelRecruitBeacon extends AnimatedGeoModel<TileEntityRecruitBeacon> {

    private int lightDelay=0;
    private long LastLightSwitchTime = 0;

    @Override
    public ResourceLocation getModelLocation(TileEntityRecruitBeacon tileEntityMetalPress) {
        return ResourceUtils.ModelLocation("block/recruit_beacon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TileEntityRecruitBeacon TE) {
        return ResourceUtils.ModResourceLocation("textures/block/recruit_beacon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TileEntityRecruitBeacon tileEntityMetalPress) {
        return null;
    }

    @Override
    public void setLivingAnimations(TileEntityRecruitBeacon entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone Light = this.getAnimationProcessor().getBone("light");
        if(this.LastLightSwitchTime == 0){
            this.LastLightSwitchTime = System.currentTimeMillis();
        }
        if(entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).get(RecruitBeaconBlock.ACTIVE)) {
            if (System.currentTimeMillis() - this.LastLightSwitchTime >= this.lightDelay) {
                if (Light.isHidden()) {
                    this.lightDelay = 2000;
                    Light.setHidden(false);
                } else {
                    this.lightDelay = 4000;
                    Light.setHidden(true);
                }
                this.LastLightSwitchTime = System.currentTimeMillis();
            }
        }
        else{
            Light.setHidden(true);
        }
    }
}