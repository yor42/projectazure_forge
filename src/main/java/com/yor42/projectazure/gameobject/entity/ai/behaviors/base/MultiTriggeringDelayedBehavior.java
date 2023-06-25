package com.yor42.projectazure.gameobject.entity.ai.behaviors.base;

import com.yor42.projectazure.interfaces.IMeleeAttacker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public abstract class MultiTriggeringDelayedBehavior<E extends LivingEntity> extends ExtendedBehaviour<E> {

    @NonNull
    protected List<Integer> delaylist = new ArrayList<>();
    @NonNull
    protected List<Long> delaysFinishesAt = new ArrayList<>();
    @NonNull
    protected BiConsumer<E, Integer> delayedCallback = (entity, counter) -> {};

    private int currentIndex = 0;

    public MultiTriggeringDelayedBehavior(){
        runFor((e)-> Math.max(60, Collections.max(delaylist)));
    }



    public final MultiTriggeringDelayedBehavior<E> addDelay(int delay){
        delaylist.add(delay);
        return this;
    }

    public final MultiTriggeringDelayedBehavior<E> addDelays(List<Integer> delays){
        delaylist.addAll(delays);
        return this;
    }

    public final MultiTriggeringDelayedBehavior<E> whenActivating(BiConsumer<E, Integer> callback) {
        this.delayedCallback = callback;
        return this;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        if (!this.delaylist.isEmpty()) {
            for(int i: this.delaylist){
                if(i == 0){
                    doDelayedActions(entity, currentIndex);
                    currentIndex+=1;
                }
                this.delaysFinishesAt.add(gameTime + i);
            }

            super.start(level, entity, gameTime);
        }
        else {
            super.start(level, entity, gameTime);
            doDelayedActions(entity, 0);
        }
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return Collections.max(delaysFinishesAt)>=entity.level.getGameTime();
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        super.tick(level, entity, gameTime);

        if(gameTime>=this.delaysFinishesAt.get(this.currentIndex)){
            doDelayedActions(entity, this.currentIndex);
            this.delayedCallback.accept(entity, this.currentIndex);
        }

    }

    protected void doDelayedActions(E entity, int counter) {}
}
