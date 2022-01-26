package com.yor42.projectazure.setup.register;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
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
    public static final SoundEvent DISC_SANDSTORM = registerSoundEvent("disc_sandstorm");
    public static final SoundEvent DISC_SANDROLL = registerSoundEvent("disc_sandroll");
    public static final SoundEvent DISC_CC5 = registerSoundEvent("disc_cc5");
    public static final SoundEvent DISC_ENTERTHEBEGINNING = registerSoundEvent("disc_enterthebeginning");

    public static final SoundEvent WEAPON_BONK = registerSoundEvent("bonk");

    public static final SoundEvent PLANE_GUN = registerSoundEvent("plane_gun");

    public static final SoundEvent SHEATH_HIT = registerSoundEvent("sheath_hit");
    public static final SoundEvent CHIXIAO_HIT = registerSoundEvent("chixiao_hit");

    public static final SoundEvent HAMMER_SWING = registerSoundEvent("hammer_swing");
    public static final SoundEvent HAMMER_HIT = registerSoundEvent("hammer_hit");

    public static final SoundEvent TEXAS_SWORD_SWING = registerSoundEvent("sword_swing");
    public static final SoundEvent TEXAS_SWORD_HIT = registerSoundEvent("sword_hit");

    public static final SoundEvent CLAYMORE_IMPACT = registerSoundEvent("claymore_hit");

    public static final SoundEvent CHIMERA_PROJECTILE_LAUNCH = registerSoundEvent("chimera_projectile_launch");
    public static final SoundEvent CHIMERA_PROJECTILE_HIT = registerSoundEvent("chimera_projectile_impact");

    public static final SoundEvent DEFIB_NOBATTERY = registerSoundEvent("defib_nobattery");
    public static final SoundEvent DEFIB_POWERON = registerSoundEvent("defib_poweron");
    public static final SoundEvent DEFIB_POWEROFF = registerSoundEvent("defib_poweroff");
    public static final SoundEvent DEFIB_CHARGING = registerSoundEvent("defib_charging");
    public static final SoundEvent DEFIB_READY = registerSoundEvent("defib_ready");
    public static final SoundEvent DEFIB_SHOCK = registerSoundEvent("defib_shock");

    //*jumps off cliff*
    public static final SoundEvent CHIMERA_TALK_NORMAL = registerSoundEvent("chimera_talk_normal");
    public static final SoundEvent CHIMERA_TALK_PAT = registerSoundEvent("chimera_talk_pat");
    public static final SoundEvent CHIMERA_TALK_ATTACK = registerSoundEvent("chimera_talk_attack");
    public static final SoundEvent AMIYA_TALK_HIGH_AFFECTION1 = registerSoundEvent("chimera_talk_high_affection1");
    public static final SoundEvent AMIYA_TALK_HIGH_AFFECTION2 = registerSoundEvent("chimera_talk_high_affection2");
    public static final SoundEvent AMIYA_TALK_HIGH_AFFECTION3 = registerSoundEvent("chimera_talk_high_affection3");

    public static final SoundEvent CHEN_TALK_NORMAL = registerSoundEvent("chen_talk_normal");
    public static final SoundEvent CHEN_TALK_PAT = registerSoundEvent("chen_talk_pat");
    public static final SoundEvent CHEN_TALK_ATTACK = registerSoundEvent("chen_talk_attack");
    public static final SoundEvent CHEN_TALK_HIGH_AFFECTION1 = registerSoundEvent("chen_talk_high_affection1");
    public static final SoundEvent CHEN_TALK_HIGH_AFFECTION2 = registerSoundEvent("chen_talk_high_affection2");
    public static final SoundEvent CHEN_TALK_HIGH_AFFECTION3 = registerSoundEvent("chen_talk_high_affection3");

    public static final SoundEvent TEXAS_TALK_NORMAL = registerSoundEvent("texas_talk_normal");
    public static final SoundEvent TEXAS_TALK_PAT = registerSoundEvent("texas_talk_pat");
    public static final SoundEvent TEXAS_TALK_ATTACK = registerSoundEvent("texas_talk_attack");
    public static final SoundEvent TEXAS_TALK_HIGH_AFFECTION1 = registerSoundEvent("texas_talk_high_affection1");
    public static final SoundEvent TEXAS_TALK_HIGH_AFFECTION2 = registerSoundEvent("texas_talk_high_affection2");
    public static final SoundEvent TEXAS_TALK_HIGH_AFFECTION3 = registerSoundEvent("texas_talk_high_affection3");

    public static final SoundEvent MUDROCK_TALK_NORMAL = registerSoundEvent("mudrock_talk_normal");
    public static final SoundEvent MUDROCK_TALK_PAT = registerSoundEvent("mudrock_talk_pat");
    public static final SoundEvent MUDROCK_TALK_ATTACK = registerSoundEvent("mudrock_talk_attack");
    public static final SoundEvent MUDROCK_TALK_HIGH_AFFECTION1 = registerSoundEvent("mudrock_talk_high_affection1");
    public static final SoundEvent MUDROCK_TALK_HIGH_AFFECTION2 = registerSoundEvent("mudrock_talk_high_affection2");
    public static final SoundEvent MUDROCK_TALK_HIGH_AFFECTION3 = registerSoundEvent("mudrock_talk_high_affection3");

    public static final SoundEvent ROSMONTIS_TALK_NORMAL = registerSoundEvent("rosmontis_talk_normal");
    public static final SoundEvent ROSMONTIS_TALK_PAT = registerSoundEvent("rosmontis_talk_pat");
    public static final SoundEvent ROSMONTIS_TALK_ATTACK = registerSoundEvent("rosmontis_talk_attack");
    public static final SoundEvent ROSMONTIS_TALK_HIGH_AFFECTION1 = registerSoundEvent("rosmontis_talk_high_affection1");
    public static final SoundEvent ROSMONTIS_TALK_HIGH_AFFECTION2 = registerSoundEvent("rosmontis_talk_high_affection2");
    public static final SoundEvent ROSMONTIS_TALK_HIGH_AFFECTION3 = registerSoundEvent("rosmontis_talk_high_affection3");

    public static final SoundEvent LAPPLAND_TALK_NORMAL = registerSoundEvent("lappland_talk_normal");
    public static final SoundEvent LAPPLAND_TALK_PAT = registerSoundEvent("lappland_talk_pat");
    public static final SoundEvent LAPPLAND_TALK_ATTACK = registerSoundEvent("lappland_talk_attack");
    public static final SoundEvent LAPPLAND_TALK_HIGH_AFFECTION1 = registerSoundEvent("lappland_talk_high_affection1");
    public static final SoundEvent LAPPLAND_TALK_HIGH_AFFECTION2 = registerSoundEvent("lappland_talk_high_affection2");
    public static final SoundEvent LAPPLAND_TALK_HIGH_AFFECTION3 = registerSoundEvent("lappland_talk_high_affection3");

    public static final SoundEvent SIEGE_TALK_NORMAL = registerSoundEvent("siege_talk_normal");
    public static final SoundEvent SIEGE_TALK_PAT = registerSoundEvent("siege_talk_pat");
    public static final SoundEvent SIEGE_TALK_ATTACK = registerSoundEvent("siege_talk_attack");
    public static final SoundEvent SIEGE_TALK_HIGH_AFFECTION1 = registerSoundEvent("siege_talk_high_affection1");
    public static final SoundEvent SIEGE_TALK_HIGH_AFFECTION2 = registerSoundEvent("siege_talk_high_affection2");
    public static final SoundEvent SIEGE_TALK_HIGH_AFFECTION3 = registerSoundEvent("siege_talk_high_affection3");

    public static final SoundEvent SCHWARZ_TALK_NORMAL = registerSoundEvent("schwarz_talk_normal");
    public static final SoundEvent SCHWARZ_TALK_PAT = registerSoundEvent("schwarz_talk_pat");
    public static final SoundEvent SCHWARZ_TALK_ATTACK = registerSoundEvent("schwarz_talk_attack");
    public static final SoundEvent SCHWARZ_TALK_HIGH_AFFECTION1 = registerSoundEvent("schwarz_talk_high_affection1");
    public static final SoundEvent SCHWARZ_TALK_HIGH_AFFECTION2 = registerSoundEvent("schwarz_talk_high_affection2");
    public static final SoundEvent SCHWARZ_TALK_HIGH_AFFECTION3 = registerSoundEvent("schwarz_talk_high_affection3");

    public static final SoundEvent SHIROKO_TALK_NORMAL = registerSoundEvent("shiroko_talk_normal");
    public static final SoundEvent SHIROKO_TALK_PAT = registerSoundEvent("shiroko_talk_pat");
    public static final SoundEvent SHIROKO_TALK_ATTACK = registerSoundEvent("shiroko_talk_attack");
    public static final SoundEvent SHIROKO_TALK_HIGH_AFFECTION1 = registerSoundEvent("shiroko_talk_high_affection1");
    public static final SoundEvent SHIROKO_TALK_HIGH_AFFECTION2 = registerSoundEvent("shiroko_talk_high_affection2");
    public static final SoundEvent SHIROKO_TALK_HIGH_AFFECTION3 = registerSoundEvent("shiroko_talk_high_affection3");

    public static final SoundEvent M4A1_TALK_NORMAL = registerSoundEvent("m4a1_talk_normal");
    public static final SoundEvent M4A1_TALK_PAT = registerSoundEvent("m4a1_talk_pat");
    public static final SoundEvent M4A1_TALK_ATTACK = registerSoundEvent("m4a1_talk_attack");

    public static final SoundEvent AYANAMI_TALK_DISAPPOINTED = registerSoundEvent("ayanami_talk_disappointed");
    public static final SoundEvent AYANAMI_TALK_STRANGER = registerSoundEvent("ayanami_talk_stranger");
    public static final SoundEvent AYANAMI_TALK_FRIENDLY = registerSoundEvent("ayanami_talk_friendly");
    public static final SoundEvent AYANAMI_TALK_CRUSH = registerSoundEvent("ayanami_talk_crush");
    public static final SoundEvent AYANAMI_TALK_LOVE = registerSoundEvent("ayanami_talk_love");
    public static final SoundEvent AYANAMI_TALK_OATH = registerSoundEvent("ayanami_talk_oath");
    public static final SoundEvent AYANAMI_TALK_PAT = registerSoundEvent("ayanami_talk_pat");
    public static final SoundEvent AYANAMI_TALK_ATTACK = registerSoundEvent("ayanami_talk_attack");

    public static final SoundEvent ENTERPRISE_TALK_DISAPPOINTED = registerSoundEvent("enterprise_talk_disappointed");
    public static final SoundEvent ENTERPRISE_TALK_STRANGER = registerSoundEvent("enterprise_talk_stranger");
    public static final SoundEvent ENTERPRISE_TALK_FRIENDLY = registerSoundEvent("enterprise_talk_friendly");
    public static final SoundEvent ENTERPRISE_TALK_CRUSH = registerSoundEvent("enterprise_talk_crush");
    public static final SoundEvent ENTERPRISE_TALK_LOVE = registerSoundEvent("enterprise_talk_love");
    public static final SoundEvent ENTERPRISE_TALK_OATH = registerSoundEvent("enterprise_talk_oath");
    public static final SoundEvent ENTERPRISE_TALK_PAT = registerSoundEvent("enterprise_talk_pat");
    public static final SoundEvent ENTERPRISE_TALK_ATTACK = registerSoundEvent("enterprise_talk_attack");

    public static final SoundEvent JAVELIN_TALK_DISAPPOINTED = registerSoundEvent("javelin_talk_disappointed");
    public static final SoundEvent JAVELIN_TALK_STRANGER = registerSoundEvent("javelin_talk_stranger");
    public static final SoundEvent JAVELIN_TALK_FRIENDLY = registerSoundEvent("javelin_talk_friendly");
    public static final SoundEvent JAVELIN_TALK_CRUSH = registerSoundEvent("javelin_talk_crush");
    public static final SoundEvent JAVELIN_TALK_LOVE = registerSoundEvent("javelin_talk_love");
    public static final SoundEvent JAVELIN_TALK_OATH = registerSoundEvent("javelin_talk_oath");
    public static final SoundEvent JAVELIN_TALK_PAT = registerSoundEvent("javelin_talk_pat");
    public static final SoundEvent JAVELIN_TALK_ATTACK = registerSoundEvent("javelin_talk_attack");

    public static final SoundEvent LAFFEY_TALK_DISAPPOINTED = registerSoundEvent("laffey_talk_disappointed");
    public static final SoundEvent LAFFEY_TALK_STRANGER = registerSoundEvent("laffey_talk_stranger");
    public static final SoundEvent LAFFEY_TALK_FRIENDLY = registerSoundEvent("laffey_talk_friendly");
    public static final SoundEvent LAFFEY_TALK_CRUSH = registerSoundEvent("laffey_talk_crush");
    public static final SoundEvent LAFFEY_TALK_LOVE = registerSoundEvent("laffey_talk_love");
    public static final SoundEvent LAFFEY_TALK_OATH = registerSoundEvent("laffey_talk_oath");
    public static final SoundEvent LAFFEY_TALK_PAT = registerSoundEvent("laffey_talk_pat");
    public static final SoundEvent LAFFEY_TALK_ATTACK = registerSoundEvent("laffey_talk_attack");

    public static final SoundEvent NAGATO_TALK_DISAPPOINTED = registerSoundEvent("nagato_talk_disappointed");
    public static final SoundEvent NAGATO_TALK_STRANGER = registerSoundEvent("nagato_talk_stranger");
    public static final SoundEvent NAGATO_TALK_FRIENDLY = registerSoundEvent("nagato_talk_friendly");
    public static final SoundEvent NAGATO_TALK_CRUSH = registerSoundEvent("nagato_talk_crush");
    public static final SoundEvent NAGATO_TALK_LOVE = registerSoundEvent("nagato_talk_love");
    public static final SoundEvent NAGATO_TALK_OATH = registerSoundEvent("nagato_talk_oath");
    public static final SoundEvent NAGATO_TALK_PAT = registerSoundEvent("nagato_talk_pat");
    public static final SoundEvent NAGATO_TALK_ATTACK = registerSoundEvent("nagato_talk_attack");

    public static final SoundEvent Z23_TALK_DISAPPOINTED = registerSoundEvent("z23_talk_disappointed");
    public static final SoundEvent Z23_TALK_STRANGER = registerSoundEvent("z23_talk_stranger");
    public static final SoundEvent Z23_TALK_FRIENDLY = registerSoundEvent("z23_talk_friendly");
    public static final SoundEvent Z23_TALK_CRUSH = registerSoundEvent("z23_talk_crush");
    public static final SoundEvent Z23_TALK_LOVE = registerSoundEvent("z23_talk_love");
    public static final SoundEvent Z23_TALK_OATH = registerSoundEvent("z23_talk_oath");
    public static final SoundEvent Z23_TALK_PAT = registerSoundEvent("z23_talk_pat");
    public static final SoundEvent Z23_TALK_ATTACK = registerSoundEvent("z23_talk_attack");

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
