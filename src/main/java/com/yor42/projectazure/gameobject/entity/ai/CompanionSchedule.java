package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;
import org.jetbrains.annotations.Nullable;

public class CompanionSchedule extends SmartBrainSchedule {

    public CompanionSchedule(){
        super(Type.DAYTIME);
        activityAt(10, RegisterAI.ACTIVITY_RELAXING.get());
        activityAt(12000, Activity.REST);
    }

    @Nullable
    @Override
    public Activity tick(LivingEntity brainOwner) {

        return brainOwner.getBrain().getActiveNonCoreActivity().map((activity)->{
            if(activity == RegisterAI.ACTIVITY_RELAXING.get() || activity == Activity.REST){
                return super.tick(brainOwner);
            }
            return null;
        }).orElse(null);
    }
}
