package com.yor42.projectazure.setup.register;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

import static com.yor42.projectazure.libs.Constants.MODID;

public final class registerSounds {

    public static final ArrayList<SoundEvent> Sounds = new ArrayList<>();

    //sound files are EXCLUDED from repo due to COPYRIGHT. Issue about these topic will be closed with no response.
    public static final SoundEvent CANON_FIRE_MEDIUM = registerSoundEvent("gun_fire_medium");

    public static final SoundEvent RIFLE_FIRE_SUPPRESSED = registerSoundEvent("rifle_suppressed");
    public static final SoundEvent GUN_CLICK = registerSoundEvent("gun_click");

    public static final SoundEvent DISC_FRIDAY_NIGHT = registerSoundEvent("disc_fridaynight");
    public static final SoundEvent DISC_BRAINPOWER = registerSoundEvent("disc_brainpower");
    public static final SoundEvent DISC_RICKROLL = registerSoundEvent("disc_rickroll");

    public static final SoundEvent WEAPON_BONK = registerSoundEvent("bonk");

    public static final SoundEvent PLANE_GUN = registerSoundEvent("plane_gun");

    public static final SoundEvent SHEATH_HIT = registerSoundEvent("sheath_hit");
    public static final SoundEvent CHIXIAO_HIT = registerSoundEvent("chixiao_hit");

    public static final SoundEvent CHIMERA_PROJECTILE_LAUNCH = registerSoundEvent("chimera_projectile_launch");
    public static final SoundEvent CHIMERA_PROJECTILE_HIT = registerSoundEvent("chimera_projectile_impact");

    public static final SoundEvent CHIMERA_TALK_NORMAL = registerSoundEvent("chimera_talk_normal");
    public static final SoundEvent CHIMERA_TALK_HIGH_AFFECTION = registerSoundEvent("chimera_talk_high_affection");

    private static SoundEvent registerSoundEvent(final String soundname) {
        final ResourceLocation soundID = new ResourceLocation(MODID, soundname);
        SoundEvent sound = new SoundEvent(soundID).setRegistryName(soundID);
        Sounds.add(sound);
        return sound;
    }

    public static void register(RegistryEvent.Register<SoundEvent> evt) {
        IForgeRegistry<SoundEvent> registry = evt.getRegistry();
        for(SoundEvent sound:Sounds){
            registry.register(sound);
        }
    }
}
