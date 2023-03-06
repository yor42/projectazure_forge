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

    public static final SoundEvent GASMASK_INHALE = registerSoundEvent("gasmask_inhale");
    public static final SoundEvent GASMASK_EXHALE=registerSoundEvent("gasmask_exhale");
    public static final SoundEvent GASMASK_EQUIP=registerSoundEvent("gasmask_equip");
    public static final SoundEvent GASMASK_FILTER_ADD = registerSoundEvent("gasmask_filter_add");
    public static final SoundEvent GASMASK_FILTER_REMOVE = registerSoundEvent("gasmask_filter_remove");
    public static final SoundEvent GASMASK_FILTER_CHANGE = registerSoundEvent("gasmask_filter_change");


    public static final SoundEvent SYRINGE_INJECT=registerSoundEvent("syringe_inject");

    public static final SoundEvent SUPERNOVA_FIRE = registerSoundEvent("supernova_fire");

    public static final SoundEvent SANGVIS_CANNON_NOAMMO = registerSoundEvent("sangvis_cannon_noammo");
    public static final SoundEvent SANGVIS_CANNON_OPEN = registerSoundEvent("sangvis_cannon_open");
    public static final SoundEvent SANGVIS_CANNON_CLOSE = registerSoundEvent("sangvis_cannon_close");

    public static final SoundEvent TYPHOON_FIRE = registerSoundEvent("typhoon_fire");
    public static final SoundEvent TYPHOON_RELOAD = registerSoundEvent("typhoon_reload");

    public static final SoundEvent DISC_FRIDAY_NIGHT = registerSoundEvent("disc_fridaynight");
    public static final SoundEvent DISC_CC5 = registerSoundEvent("disc_cc5");
    public static final SoundEvent DISC_REVENGE = registerSoundEvent("disc_revenge");
    public static final SoundEvent DISC_FALLEN_KINGDOM = registerSoundEvent("disc_fallenkingdom");
    public static final SoundEvent DISC_TAKE_BACK_THE_NIGHT = registerSoundEvent("disc_takebackthenight");
    public static final SoundEvent DISC_FIND_THE_PIECES = registerSoundEvent("disc_findthepieces");
    public static final SoundEvent DISC_DRAGONHEARTED = registerSoundEvent("disc_dragonhearted");

    public static final SoundEvent WEAPON_BONK = registerSoundEvent("bonk");

    public static final SoundEvent PLANE_GUN = registerSoundEvent("plane_gun");

    public static final SoundEvent SHEATH_HIT = registerSoundEvent("sheath_hit");
    public static final SoundEvent CHIXIAO_HIT = registerSoundEvent("chixiao_hit");

    public static final SoundEvent HAMMER_SWING = registerSoundEvent("hammer_swing");
    public static final SoundEvent HAMMER_HIT = registerSoundEvent("hammer_hit");

    public static final SoundEvent CRESCENT_KATANA_SWING = registerSoundEvent("lappland_swing");
    public static final SoundEvent CRESCENT_KATANA_HIT = registerSoundEvent("lappland_hit");

    public static final SoundEvent WARHAMMER_SWING = registerSoundEvent("warhammer_swing");
    public static final SoundEvent WARHAMMER_HIT = registerSoundEvent("warhammer_hit");

    public static final SoundEvent TEXAS_SWORD_SWING = registerSoundEvent("sword_swing");
    public static final SoundEvent TEXAS_SWORD_HIT = registerSoundEvent("sword_hit");

    public static final SoundEvent CLAYMORE_IMPACT = registerSoundEvent("claymore_hit");

    public static final SoundEvent NEARL_HEAL = registerSoundEvent("nearl_heal");
    public static final SoundEvent HEAL_BOOST = registerSoundEvent("heal_boost");

    public static final SoundEvent LEAPHAMMER_START = registerSoundEvent("leaphammer_start");
    public static final SoundEvent LEAPHAMMER_HIT = registerSoundEvent("leaphammer_hit");

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

    public static final SoundEvent NEARL_TALK_NORMAL = registerSoundEvent("nearl_talk_normal");
    public static final SoundEvent NEARL_TALK_PAT = registerSoundEvent("nearl_talk_pat");
    public static final SoundEvent NEARL_TALK_AGGRO = registerSoundEvent("nearl_talk_aggro");
    public static final SoundEvent NEARL_TALK_SKILL = registerSoundEvent("nearl_talk_skill");
    public static final SoundEvent NEARL_TALK_HIGH_AFFECTION1 = registerSoundEvent("nearl_talk_high_affection1");
    public static final SoundEvent NEARL_TALK_HIGH_AFFECTION2 = registerSoundEvent("nearl_talk_high_affection2");
    public static final SoundEvent NEARL_TALK_HIGH_AFFECTION3 = registerSoundEvent("nearl_talk_high_affection3");
    public static final SoundEvent CHEN_TALK_NORMAL = registerSoundEvent("chen_talk_normal");
    public static final SoundEvent CHEN_TALK_PAT = registerSoundEvent("chen_talk_pat");
    public static final SoundEvent CHEN_TALK_ATTACK = registerSoundEvent("chen_talk_attack");
    public static final SoundEvent CHEN_TALK_AGGRO = registerSoundEvent("chen_talk_aggro");
    public static final SoundEvent CHEN_TALK_KILL = registerSoundEvent("chen_talk_kill");
    public static final SoundEvent CHEN_TALK_HIGH_AFFECTION1 = registerSoundEvent("chen_talk_high_affection1");
    public static final SoundEvent CHEN_TALK_HIGH_AFFECTION2 = registerSoundEvent("chen_talk_high_affection2");
    public static final SoundEvent CHEN_TALK_HIGH_AFFECTION3 = registerSoundEvent("chen_talk_high_affection3");

    public static final SoundEvent TEXAS_TALK_NORMAL = registerSoundEvent("texas_talk_normal");
    public static final SoundEvent TEXAS_TALK_PAT = registerSoundEvent("texas_talk_pat");
    public static final SoundEvent TEXAS_TALK_ATTACK = registerSoundEvent("texas_talk_attack");
    public static final SoundEvent TEXAS_TALK_AGGRO = registerSoundEvent("texas_talk_aggro");
    public static final SoundEvent TEXAS_PROMOTION_1 = registerSoundEvent("texas_promotion1");

    public static final SoundEvent TEXAS_PROMOTION_2 = registerSoundEvent("texas_promotion2");
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

    public static final SoundEvent YATO_TALK_NORMAL = registerSoundEvent("yato_talk_normal");
    public static final SoundEvent YATO_TALK_PAT = registerSoundEvent("yato_talk_pat");
    public static final SoundEvent YATO_TALK_ATTACK = registerSoundEvent("yato_talk_attack");
    public static final SoundEvent YATO_TALK_HIGH_AFFECTION1 = registerSoundEvent("yato_talk_high_affection1");
    public static final SoundEvent YATO_TALK_HIGH_AFFECTION2 = registerSoundEvent("yato_talk_high_affection2");
    public static final SoundEvent YATO_TALK_HIGH_AFFECTION3 = registerSoundEvent("yato_talk_high_affection3");

    public static final SoundEvent W_TALK_NORMAL = registerSoundEvent("w_talk_normal");
    public static final SoundEvent W_TALK_PAT = registerSoundEvent("w_talk_pat");
    public static final SoundEvent W_TALK_AGGRO = registerSoundEvent("w_talk_aggro");
    public static final SoundEvent W_TALK_HIGH_AFFECTION1 = registerSoundEvent("w_talk_high_affection1");
    public static final SoundEvent W_TALK_HIGH_AFFECTION2 = registerSoundEvent("w_talk_high_affection2");
    public static final SoundEvent W_TALK_HIGH_AFFECTION3 = registerSoundEvent("w_talk_high_affection3");

    public static final SoundEvent ARTORIA_TALK_NORMAL = registerSoundEvent("artoria_talk");
    public static final SoundEvent ARTORIA_TALK_ATTACK = registerSoundEvent("artoria_attack");
    public static final SoundEvent ARTORIA_TALK_AGGRO = registerSoundEvent("artoria_aggro");
    public static final SoundEvent ARTORIA_TALK_HURT = registerSoundEvent("artoria_hurt");
    public static final SoundEvent ARTORIA_TALK_KILL = registerSoundEvent("artoria_kill");
    public static final SoundEvent ARTORIA_TALK_DIE = registerSoundEvent("artoria_die");
    public static final SoundEvent ARTORIA_TALK_HIGH_AFFECTION1 = registerSoundEvent("artoria_talk_bond1");
    public static final SoundEvent ARTORIA_TALK_HIGH_AFFECTION2 = registerSoundEvent("artoria_talk_bond2");
    public static final SoundEvent ARTORIA_TALK_HIGH_AFFECTION3 = registerSoundEvent("artoria_talk_bond3");
    public static final SoundEvent ARTORIA_TALK_HIGH_AFFECTION4 = registerSoundEvent("artoria_talk_bond4");
    public static final SoundEvent ARTORIA_TALK_HIGH_AFFECTION5 = registerSoundEvent("artoria_talk_bond5");

    public static final SoundEvent SHIKI_TALK_NORMAL = registerSoundEvent("shiki_talk");
    public static final SoundEvent SHIKI_TALK_ATTACK = registerSoundEvent("shiki_attack");
    public static final SoundEvent SHIKI_TALK_AGGRO = registerSoundEvent("shiki_aggro");
    public static final SoundEvent SHIKI_TALK_HURT = registerSoundEvent("shiki_hurt");
    public static final SoundEvent SHIKI_TALK_KILL = registerSoundEvent("shiki_kill");
    public static final SoundEvent SHIKI_TALK_DIE = registerSoundEvent("shiki_die");
    public static final SoundEvent SHIKI_TALK_HIGH_AFFECTION1 = registerSoundEvent("shiki_talk_bond1");
    public static final SoundEvent SHIKI_TALK_HIGH_AFFECTION2 = registerSoundEvent("shiki_talk_bond2");
    public static final SoundEvent SHIKI_TALK_HIGH_AFFECTION3 = registerSoundEvent("shiki_talk_bond3");
    public static final SoundEvent SHIKI_TALK_HIGH_AFFECTION4 = registerSoundEvent("shiki_talk_bond4");
    public static final SoundEvent SHIKI_TALK_HIGH_AFFECTION5 = registerSoundEvent("shiki_talk_bond5");

    public static final SoundEvent SHIROKO_TALK_NORMAL = registerSoundEvent("shiroko_talk_normal");
    public static final SoundEvent SHIROKO_TALK_PAT = registerSoundEvent("shiroko_talk_pat");
    public static final SoundEvent SHIROKO_TALK_ATTACK = registerSoundEvent("shiroko_talk_attack");
    public static final SoundEvent SHIROKO_TALK_HIGH_AFFECTION = registerSoundEvent("shiroko_talk_high_affection");
    public static final SoundEvent SHIROKO_HURT = registerSoundEvent("shiroko_hurt");
    public static final SoundEvent SHIROKO_FAINT = registerSoundEvent("shiroko_faint");

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

    public static final SoundEvent EXCELA_ATTACK = registerSoundEvent("exela_attack");
    public static final SoundEvent EXCELA_SKILL = registerSoundEvent("exela_skill");
    public static final SoundEvent EXCELA_TALK = registerSoundEvent("exela_talk");
    public static final SoundEvent EXCELA_HURT = registerSoundEvent("exela_hurt");
    public static final SoundEvent EXCELA_DEATH = registerSoundEvent("exela_die");
    public static final SoundEvent EXCELA_KILL = registerSoundEvent("exela_kill");

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
