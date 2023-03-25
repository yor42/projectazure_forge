package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.enums;
import net.minecraft.sounds.SoundEvent;

public interface IAzurLaneKansen {
    default enums.ALAffection affectionValuetoEnum(){
        if(this instanceof AbstractEntityCompanion) {
            if (((AbstractEntityCompanion) this).getAffection() >= 100.0D) {
                if (((AbstractEntityCompanion) this).isOathed())
                    return enums.ALAffection.OATH;
                else
                    return enums.ALAffection.LOVE;
            } else if (((AbstractEntityCompanion) this).getAffection() > 80) {
                return enums.ALAffection.CRUSH;
            } else if (((AbstractEntityCompanion) this).getAffection() > 60) {
                return enums.ALAffection.FRIENDLY;
            } else if (((AbstractEntityCompanion) this).getAffection() > 30) {
                return enums.ALAffection.STRANGER;
            }
        }
        return enums.ALAffection.DISAPPOINTED;
    }

    default SoundEvent getDisappointedAmbientSound(){
        return null;
    }

    default SoundEvent getStrangerAmbientSound(){
        return null;
    }

    default SoundEvent getFriendlyAmbientSound(){
        return null;
    }

    default SoundEvent getLikeAmbientSound(){
        return null;
    }

    default SoundEvent getLoveAmbientSound(){
        return null;
    }

}
