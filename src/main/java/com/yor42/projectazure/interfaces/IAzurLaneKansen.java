package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.libs.enums;
import net.minecraft.sounds.SoundEvent;

public interface IAzurLaneKansen {
    default enums.ALAffection affectionValuetoEnum(){
        if(this.getAffection()>=100.0D){
            if(this.isOathed())
                return enums.ALAffection.OATH;
            else
                return enums.ALAffection.LOVE;
        }
        else if(this.getAffection()>80 && this.getAffection()<100){
            return enums.ALAffection.CRUSH;
        }
        else if(this.getAffection()>60 && this.getAffection()<=80){
            return enums.ALAffection.FRIENDLY;
        }
        else if(this.getAffection()>30 && this.getAffection()<=60){
            return enums.ALAffection.STRANGER;
        }
        else{
            return enums.ALAffection.DISAPPOINTED;
        }
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
    
    boolean isOathed();
    float getAffection();
}
