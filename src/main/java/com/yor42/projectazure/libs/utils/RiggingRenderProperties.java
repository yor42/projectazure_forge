package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.libs.enums;

public class RiggingRenderProperties {

    private double xShift, yShift, zShift, MountDefaultRotation;
    private boolean shouldBeNegative;

    public RiggingRenderProperties(double xShift, double yShift, double zShift, double MountDefaultRotation, boolean shouldBeNegative, enums.MOTIONTOTRACK MountTrackTarget){
        this.xShift = xShift;
        this.yShift = yShift;
        this.zShift = zShift;
        this.MountDefaultRotation = MountDefaultRotation;
        this.shouldBeNegative = shouldBeNegative;
    }

    public double getTotalXShift(float hostBoneXIn) {
        return this.shouldBeNegative? this.xShift+hostBoneXIn * -1 : this.xShift+hostBoneXIn;
    }
}
