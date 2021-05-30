package com.yor42.projectazure.libs.utils;
/*
 * BluSunrize
 * Copyright (c) 2020
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of Immersive Engineering.
 *
 */
import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.libs.defined;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents a value that depends on non-API IE code and should only be written to by IE.
 * It also contains a mechanism to make sure that all fields are actually set.
 *
 * @param <T> type of the contained value
 */
public class SetRestrictedField<T> {

    /*
    I'm so Sorry for lazy copy, but all these multi-block things are just way beyond my brain.
    so thanks, Blusunsize. - yor42
    */

    private static final InitializationTracker CLIENT_FIELDS = new InitializationTracker();
    private static final InitializationTracker COMMON_FIELDS = new InitializationTracker();

    private final InitializationTracker tracker;
    private T value;

    private SetRestrictedField(InitializationTracker tracker)
    {
        this.tracker = tracker;
    }

    public static <T> SetRestrictedField<T> client()
    {
        return CLIENT_FIELDS.make();
    }

    public static <T> SetRestrictedField<T> common()
    {
        return COMMON_FIELDS.make();
    }

    public static void lock(boolean client)
    {
        if(client)
            CLIENT_FIELDS.lock();
        else
            COMMON_FIELDS.lock();
    }

    public static void startInitializing(boolean client)
    {
        if(client)
            CLIENT_FIELDS.startInitialization();
        else
            COMMON_FIELDS.startInitialization();
    }

    public void setValue(T value)
    {
        Preconditions.checkState(tracker.state==TrackerState.INITIALIZING);
        String currentMod = ModLoadingContext.get().getActiveNamespace();
        Preconditions.checkState(
                defined.MODID.equals(currentMod),
                "Restricted fields may only be set by Project: Azure. Mod that tried to set restricted field: %s", currentMod
        );
        this.value = value;
    }

    public T getValue()
    {
        return Preconditions.checkNotNull(value);
    }

    public boolean isInitialized()
    {
        return value!=null;
    }

    private static class InitializationTracker
    {
        private final List<Pair<Exception, SetRestrictedField<?>>> fields = new ArrayList<>();
        private TrackerState state = TrackerState.OPEN;

        <T> SetRestrictedField<T> make()
        {
            Preconditions.checkState(state!=TrackerState.LOCKED);
            SetRestrictedField<T> result = new SetRestrictedField<>(this);
            fields.add(Pair.of(new RuntimeException("Field created here"), result));
            return result;
        }

        public void startInitialization()
        {
            Preconditions.checkState(state==TrackerState.OPEN);
            state = TrackerState.INITIALIZING;
        }

        void lock()
        {
            Preconditions.checkState(state==TrackerState.INITIALIZING);
            for(Pair<Exception, SetRestrictedField<?>> field : fields)
                if(!field.getSecond().isInitialized())
                    throw new RuntimeException(field.getFirst());
            state = TrackerState.LOCKED;
        }
    }

    private enum TrackerState
    {
        OPEN,
        INITIALIZING,
        LOCKED
    }

}
