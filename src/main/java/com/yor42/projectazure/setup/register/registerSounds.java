package com.yor42.projectazure.setup.register;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;

import static com.yor42.projectazure.libs.defined.MODID;

public final class registerSounds {

    public static final SoundEvent DISC_FRIDAY_NIGHT = registerSoundEvent("disc_fridaynight");
    public static final SoundEvent DISC_BRAINPOWER = registerSoundEvent("disc_brainpower");

    public static final SoundEvent WEAPON_BONK = registerSoundEvent("bonk");

    private static SoundEvent registerSoundEvent(final String soundname) {
        final ResourceLocation soundID = new ResourceLocation(MODID, soundname);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }

    public static void register(RegistryEvent.Register<SoundEvent> evt) {
        IForgeRegistry<SoundEvent> registry = evt.getRegistry();
        registry.register(DISC_FRIDAY_NIGHT);
        registry.register(DISC_BRAINPOWER);
        registry.register(WEAPON_BONK);
    }
}
