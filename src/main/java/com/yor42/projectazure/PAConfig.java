package com.yor42.projectazure;

import com.yor42.projectazure.libs.Constants;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import static com.yor42.projectazure.PAConfig.COMPANION_DEATH.RESPAWN;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PAConfig {

    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final PAModConfig CONFIG;

    static
    {
        final Pair<PAModConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(PAModConfig::new);
        CONFIG_SPEC = pair.getRight();
        CONFIG = pair.getLeft();
    }

    public static class PAModConfig {

        public final ForgeConfigSpec.BooleanValue EnablePVP;
        public final ForgeConfigSpec.BooleanValue EnableShipLandCombat;
        public final ForgeConfigSpec.BooleanValue EnableTorpedoBlockDamage;

        public final ForgeConfigSpec.DoubleValue PurifierHealth;
        public final ForgeConfigSpec.DoubleValue PurifierSwimSpeed;
        public final ForgeConfigSpec.DoubleValue PurifierAttackDamage;
        public final ForgeConfigSpec.DoubleValue PurifierMovementSpeed;

        public final ForgeConfigSpec.DoubleValue AyanamiHealth;
        public final ForgeConfigSpec.DoubleValue AyanamiSwimSpeed;
        public final ForgeConfigSpec.DoubleValue AyanamiAttackDamage;
        public final ForgeConfigSpec.DoubleValue AyanamiMovementSpeed;

        public final ForgeConfigSpec.DoubleValue JavelinHealth;
        public final ForgeConfigSpec.DoubleValue JavelinSwimSpeed;
        public final ForgeConfigSpec.DoubleValue JavelinAttackDamage;
        public final ForgeConfigSpec.DoubleValue JavelinMovementSpeed;

        public final ForgeConfigSpec.DoubleValue Z23Health;
        public final ForgeConfigSpec.DoubleValue Z23SwimSpeed;
        public final ForgeConfigSpec.DoubleValue Z23AttackDamage;
        public final ForgeConfigSpec.DoubleValue Z23MovementSpeed;

        public final ForgeConfigSpec.DoubleValue LaffeyHealth;
        public final ForgeConfigSpec.DoubleValue LaffeySwimSpeed;
        public final ForgeConfigSpec.DoubleValue LaffeyAttackDamage;
        public final ForgeConfigSpec.DoubleValue LaffeyMovementSpeed;

        public final ForgeConfigSpec.DoubleValue GangwonHealth;
        public final ForgeConfigSpec.DoubleValue GangwonSwimSpeed;
        public final ForgeConfigSpec.DoubleValue GangwonAttackDamage;
        public final ForgeConfigSpec.DoubleValue GangwonMovementSpeed;

        public final ForgeConfigSpec.DoubleValue EnterpriseHealth;
        public final ForgeConfigSpec.DoubleValue EnterpriseSwimSpeed;
        public final ForgeConfigSpec.DoubleValue EnterpriseAttackDamage;
        public final ForgeConfigSpec.DoubleValue EnterpriseMovementSpeed;

        public final ForgeConfigSpec.DoubleValue NagatoHealth;
        public final ForgeConfigSpec.DoubleValue NagatoSwimSpeed;
        public final ForgeConfigSpec.DoubleValue NagatoAttackDamage;
        public final ForgeConfigSpec.DoubleValue NagatoMovementSpeed;

        public final ForgeConfigSpec.DoubleValue ShirokoHealth;
        public final ForgeConfigSpec.DoubleValue ShirokoSwimSpeed;
        public final ForgeConfigSpec.DoubleValue ShirokoAttackDamage;
        public final ForgeConfigSpec.DoubleValue ShirokoMovementSpeed;

        public final ForgeConfigSpec.DoubleValue M4A1Health;
        public final ForgeConfigSpec.DoubleValue M4A1SwimSpeed;
        public final ForgeConfigSpec.DoubleValue M4A1AttackDamage;
        public final ForgeConfigSpec.DoubleValue M4A1MovementSpeed;

        public final ForgeConfigSpec.DoubleValue HK416Health;
        public final ForgeConfigSpec.DoubleValue HK416SwimSpeed;
        public final ForgeConfigSpec.DoubleValue HK416AttackDamage;
        public final ForgeConfigSpec.DoubleValue HK416MovementSpeed;

        public final ForgeConfigSpec.DoubleValue ChenHealth;
        public final ForgeConfigSpec.DoubleValue ChenSwimSpeed;
        public final ForgeConfigSpec.DoubleValue ChenAttackDamage;
        public final ForgeConfigSpec.DoubleValue ChenMovementSpeed;

        public final ForgeConfigSpec.DoubleValue TexasHealth;
        public final ForgeConfigSpec.DoubleValue TexasSwimSpeed;
        public final ForgeConfigSpec.DoubleValue TexasAttackDamage;
        public final ForgeConfigSpec.DoubleValue TexasMovementSpeed;

        public final ForgeConfigSpec.DoubleValue LapplandHealth;
        public final ForgeConfigSpec.DoubleValue LapplandSwimSpeed;
        public final ForgeConfigSpec.DoubleValue LapplandAttackDamage;
        public final ForgeConfigSpec.DoubleValue LapplandMovementSpeed;

        public final ForgeConfigSpec.DoubleValue SiegeHealth;
        public final ForgeConfigSpec.DoubleValue SiegeSwimSpeed;
        public final ForgeConfigSpec.DoubleValue SiegeAttackDamage;
        public final ForgeConfigSpec.DoubleValue SiegeMovementSpeed;

        public final ForgeConfigSpec.DoubleValue NearlHealth;
        public final ForgeConfigSpec.DoubleValue NearlSwimSpeed;
        public final ForgeConfigSpec.DoubleValue NearlAttackDamage;
        public final ForgeConfigSpec.DoubleValue NearlMovementSpeed;

        public final ForgeConfigSpec.DoubleValue SchwarzHealth;
        public final ForgeConfigSpec.DoubleValue SchwarzSwimSpeed;
        public final ForgeConfigSpec.DoubleValue SchwarzAttackDamage;
        public final ForgeConfigSpec.DoubleValue SchwarzMovementSpeed;

        public final ForgeConfigSpec.DoubleValue AmiyaHealth;
        public final ForgeConfigSpec.DoubleValue AmiyaSwimSpeed;
        public final ForgeConfigSpec.DoubleValue AmiyaAttackDamage;
        public final ForgeConfigSpec.DoubleValue AmiyaMovementSpeed;

        public final ForgeConfigSpec.DoubleValue RosmontisHealth;
        public final ForgeConfigSpec.DoubleValue RosmontisSwimSpeed;
        public final ForgeConfigSpec.DoubleValue RosmontisAttackDamage;
        public final ForgeConfigSpec.DoubleValue RosmontisMovementSpeed;

        public final ForgeConfigSpec.DoubleValue MudrockHealth;
        public final ForgeConfigSpec.DoubleValue MudrockSwimSpeed;
        public final ForgeConfigSpec.DoubleValue MudrockAttackDamage;
        public final ForgeConfigSpec.DoubleValue MudrockMovementSpeed;

        public final ForgeConfigSpec.DoubleValue FrostnovaHealth;
        public final ForgeConfigSpec.DoubleValue FrostnovaSwimSpeed;
        public final ForgeConfigSpec.DoubleValue FrostnovaAttackDamage;
        public final ForgeConfigSpec.DoubleValue FrostnovaMovementSpeed;

        public final ForgeConfigSpec.DoubleValue TalulahHealth;
        public final ForgeConfigSpec.DoubleValue TalulahSwimSpeed;
        public final ForgeConfigSpec.DoubleValue TalulahAttackDamage;
        public final ForgeConfigSpec.DoubleValue TalulahMovementSpeed;

        public final ForgeConfigSpec.DoubleValue WHealth;
        public final ForgeConfigSpec.DoubleValue WSwimSpeed;
        public final ForgeConfigSpec.DoubleValue WAttackDamage;
        public final ForgeConfigSpec.DoubleValue WMovementSpeed;

        public final ForgeConfigSpec.DoubleValue CrownslayerHealth;
        public final ForgeConfigSpec.DoubleValue CrownslayerSwimSpeed;
        public final ForgeConfigSpec.DoubleValue CrownslayerAttackDamage;
        public final ForgeConfigSpec.DoubleValue CrownslayerMovementSpeed;

        public final ForgeConfigSpec.DoubleValue YatoHealth;
        public final ForgeConfigSpec.DoubleValue YatoSwimSpeed;
        public final ForgeConfigSpec.DoubleValue YatoAttackDamage;
        public final ForgeConfigSpec.DoubleValue YatoMovementSpeed;

        public final ForgeConfigSpec.DoubleValue SylviHealth;
        public final ForgeConfigSpec.DoubleValue SylviSwimSpeed;
        public final ForgeConfigSpec.DoubleValue SylviAttackDamage;
        public final ForgeConfigSpec.DoubleValue SylviMovementSpeed;

        public final ForgeConfigSpec.DoubleValue YamatoHealth;
        public final ForgeConfigSpec.DoubleValue YamatoSwimSpeed;
        public final ForgeConfigSpec.DoubleValue YamatoAttackDamage;
        public final ForgeConfigSpec.DoubleValue YamatoMovementSpeed;

        public final ForgeConfigSpec.DoubleValue ArtoriaHealth;
        public final ForgeConfigSpec.DoubleValue ArtoriaSwimSpeed;
        public final ForgeConfigSpec.DoubleValue ArtoriaAttackDamage;
        public final ForgeConfigSpec.DoubleValue ArtoriaMovementSpeed;

        public final ForgeConfigSpec.DoubleValue ScathathHealth;
        public final ForgeConfigSpec.DoubleValue ScathathSwimSpeed;
        public final ForgeConfigSpec.DoubleValue ScathathAttackDamage;
        public final ForgeConfigSpec.DoubleValue ScathathMovementSpeed;

        public final ForgeConfigSpec.DoubleValue MashHealth;
        public final ForgeConfigSpec.DoubleValue MashSwimSpeed;
        public final ForgeConfigSpec.DoubleValue MashAttackDamage;
        public final ForgeConfigSpec.DoubleValue MashMovementSpeed;

        public final ForgeConfigSpec.DoubleValue ShikiHealth;
        public final ForgeConfigSpec.DoubleValue ShikiSwimSpeed;
        public final ForgeConfigSpec.DoubleValue ShikiAttackDamage;
        public final ForgeConfigSpec.DoubleValue ShikiMovementSpeed;

        public final ForgeConfigSpec.DoubleValue KyaruHealth;
        public final ForgeConfigSpec.DoubleValue KyaruSwimSpeed;
        public final ForgeConfigSpec.DoubleValue KyaruAttackDamage;
        public final ForgeConfigSpec.DoubleValue KyaruMovementSpeed;

        public final ForgeConfigSpec.DoubleValue ExcelaHealth;
        public final ForgeConfigSpec.DoubleValue ExcelaSwimSpeed;
        public final ForgeConfigSpec.DoubleValue ExcelaAttackDamage;
        public final ForgeConfigSpec.DoubleValue ExcelaMovementSpeed;
        public final ForgeConfigSpec.DoubleValue ExcelaArmor;

        public final ForgeConfigSpec.DoubleValue Star_1_Chance;
        public final ForgeConfigSpec.DoubleValue Star_2_Chance;
        public final ForgeConfigSpec.DoubleValue Star_3_Chance;
        public final ForgeConfigSpec.DoubleValue Star_4_Chance;
        public final ForgeConfigSpec.DoubleValue Star_5_Chance;
        public final ForgeConfigSpec.DoubleValue Star_6_Chance;

        public final ForgeConfigSpec.BooleanValue ALLOW_DUPLICATE;

        public final ForgeConfigSpec.IntValue Star_1_MinTime;
        public final ForgeConfigSpec.IntValue Star_1_MaxTime;
        public final ForgeConfigSpec.IntValue Star_2_MinTime;
        public final ForgeConfigSpec.IntValue Star_2_MaxTime;
        public final ForgeConfigSpec.IntValue Star_3_MinTime;
        public final ForgeConfigSpec.IntValue Star_3_MaxTime;
        public final ForgeConfigSpec.IntValue Star_4_MinTime;
        public final ForgeConfigSpec.IntValue Star_4_MaxTime;
        public final ForgeConfigSpec.IntValue Star_5_MinTime;
        public final ForgeConfigSpec.IntValue Star_5_MaxTime;
        public final ForgeConfigSpec.IntValue Star_6_MinTime;
        public final ForgeConfigSpec.IntValue Star_6_MaxTime;

        public final ForgeConfigSpec.BooleanValue ENABLE_COPPER;
        public final ForgeConfigSpec.IntValue COPPER_VEINSIZE;
        public final ForgeConfigSpec.IntValue COPPER_VEINSPERCHUNK;
        public final ForgeConfigSpec.IntValue COPPER_MINHEIGHT;
        public final ForgeConfigSpec.IntValue COPPER_MAXHEIGHT;

        public final ForgeConfigSpec.BooleanValue ENABLE_ZINC;
        public final ForgeConfigSpec.IntValue ZINC_VEINSIZE;
        public final ForgeConfigSpec.IntValue ZINC_VEINSPERCHUNK;
        public final ForgeConfigSpec.IntValue ZINC_MINHEIGHT;
        public final ForgeConfigSpec.IntValue ZINC_MAXHEIGHT;

        public final ForgeConfigSpec.BooleanValue ENABLE_TIN;
        public final ForgeConfigSpec.IntValue TIN_VEINSIZE;
        public final ForgeConfigSpec.IntValue TIN_VEINSPERCHUNK;
        public final ForgeConfigSpec.IntValue TIN_MINHEIGHT;
        public final ForgeConfigSpec.IntValue TIN_MAXHEIGHT;

        public final ForgeConfigSpec.BooleanValue ENABLE_LEAD;
        public final ForgeConfigSpec.IntValue LEAD_VEINSIZE;
        public final ForgeConfigSpec.IntValue LEAD_VEINSPERCHUNK;
        public final ForgeConfigSpec.IntValue LEAD_MINHEIGHT;
        public final ForgeConfigSpec.IntValue LEAD_MAXHEIGHT;

        public final ForgeConfigSpec.BooleanValue ENABLE_ORIROCK;
        public final ForgeConfigSpec.IntValue ORIROCK_VEINSIZE;
        public final ForgeConfigSpec.IntValue ORIROCK_VEINSPERCHUNK;
        public final ForgeConfigSpec.IntValue ORIROCK_MINHEIGHT;
        public final ForgeConfigSpec.IntValue ORIROCK_MAXHEIGHT;

        public final ForgeConfigSpec.IntValue BeaconFindSpawnPositionTries;
        public final ForgeConfigSpec.ConfigValue<Integer> InjuredRecoveryTimer;
        public final ForgeConfigSpec.ConfigValue<Integer> FaintTimeLimit;

        public final ForgeConfigSpec.BooleanValue RedStonePoweredMachines;

        public final ForgeConfigSpec.BooleanValue shouldRecruitBeaconSpawnAllCompanions;
        public final ForgeConfigSpec.EnumValue<COMPANION_DEATH> death_type;
        public final ForgeConfigSpec.BooleanValue RiggingInfiniteFuel;

        private PAModConfig(ForgeConfigSpec.Builder builder){
            builder.push("Letter from developer");
            builder.comment("As a Mod dev/modpack maker, I believe everything in mod should be configurable.");
            builder.comment("Minecraft is after all, game that no one plays the same.");
            builder.comment("So Nobody should tell you how to play the game.");
            builder.comment("So here you go. Go crazy with it.");
            builder.comment("with <3, yor42");

            builder.pop().push("General");
            EnablePVP = builder.define("Enable PVP Combat", false);
            EnableShipLandCombat = builder.define("Enable Ship Weapons on land", true);
            EnableTorpedoBlockDamage = builder.define("Enable Torpedo's block damage", true);

            builder.pop().push("Entity Stat").push("Azur lane");
            AyanamiHealth = builder.defineInRange("Max Health of Ayanami", 40D, 1, 1024);
            AyanamiSwimSpeed = builder.defineInRange("Swim Speed of Ayanami", 2.5, 0, 1024);
            AyanamiAttackDamage = builder.defineInRange("Attack Damage of Ayanami", 2.0F, 0, 1024);
            AyanamiMovementSpeed = builder.defineInRange("Movement speed of Ayanami", 0.3F, 0, 2);

            JavelinHealth = builder.defineInRange("Max Health of Javelin", 40D, 1, 1024);
            JavelinSwimSpeed = builder.defineInRange("Swim Speed of Javelin", 2.5, 0, 1024);
            JavelinAttackDamage = builder.defineInRange("Attack Damage of Javelin", 2.0F, 0, 1024);
            JavelinMovementSpeed = builder.defineInRange("Movement speed of Javelin", 0.3F, 0, 2);

            Z23Health = builder.defineInRange("Max Health of Z23", 40D, 1, 1024);
            Z23SwimSpeed = builder.defineInRange("Swim Speed of Z23", 2.5, 0, 1024);
            Z23AttackDamage = builder.defineInRange("Attack Damage of Z23", 2.0F, 0, 1024);
            Z23MovementSpeed = builder.defineInRange("Movement speed of Z23", 0.3F, 0, 2);

            LaffeyHealth = builder.defineInRange("Max Health of Laffey", 40D, 1, 1024);
            LaffeySwimSpeed = builder.defineInRange("Swim Speed of Laffey", 2.5, 0, 1024);
            LaffeyAttackDamage = builder.defineInRange("Attack Damage of Laffey", 2.0F, 0, 1024);
            LaffeyMovementSpeed = builder.defineInRange("Movement speed of Laffey", 0.3F, 0, 2);

            EnterpriseHealth = builder.defineInRange("Max Health of Enterprise", 30D, 1, 1024);
            EnterpriseSwimSpeed = builder.defineInRange("Swim Speed of Enterprise", 2.5, 0, 1024);
            EnterpriseAttackDamage = builder.defineInRange("Attack Damage of Enterprise", 2.0F, 0, 1024);
            EnterpriseMovementSpeed = builder.defineInRange("Movement speed of Enterprise", 0.3F, 0, 2);

            NagatoHealth = builder.defineInRange("Max Health of Nagato", 50D, 1, 1024);
            NagatoSwimSpeed = builder.defineInRange("Swim Speed of Nagato", 2.5, 0, 1024);
            NagatoAttackDamage = builder.defineInRange("Attack Damage of Nagato", 2.0F, 0, 1024);
            NagatoMovementSpeed = builder.defineInRange("Movement speed of Nagato", 0.3F, 0, 2);

            PurifierHealth = builder.defineInRange("Max Health of Purifier", 80D, 1, 1024);
            PurifierSwimSpeed = builder.defineInRange("Swim Speed of Purifier", 2.5, 0, 1024);
            PurifierAttackDamage = builder.defineInRange("Attack Damage of Purifier", 2.0F, 0, 1024);
            PurifierMovementSpeed = builder.defineInRange("Movement speed of Purifier", 0.3F, 0, 2);

            builder.pop().push("Blue Archive");
            ShirokoHealth = builder.defineInRange("Max Health of Shiroko", 40D, 1, 1024);
            ShirokoSwimSpeed = builder.defineInRange("Swim Speed of Shiroko", 2.5, 0, 1024);
            ShirokoAttackDamage = builder.defineInRange("Attack Damage of Shiroko", 2.0F, 0, 1024);
            ShirokoMovementSpeed = builder.defineInRange("Movement speed of Shiroko", 0.3F, 0, 2);

            builder.pop().push("Arknights");
            ChenHealth = builder.defineInRange("Max Health of Ch'en", 25D, 1, 1024);
            ChenSwimSpeed = builder.defineInRange("Swim Speed of Ch'en", 2.5, 0, 1024);
            ChenAttackDamage = builder.defineInRange("Attack Damage of Ch'en", 2.0F, 0, 1024);
            ChenMovementSpeed = builder.defineInRange("Movement speed of Ch'en", 0.3F, 0, 2);

            TexasHealth = builder.defineInRange("Max Health of Texas", 20D, 1, 1024);
            TexasSwimSpeed = builder.defineInRange("Swim Speed of Texas", 2.5, 0, 1024);
            TexasAttackDamage = builder.defineInRange("Attack Damage of Texas", 2.0F, 0, 1024);
            TexasMovementSpeed = builder.defineInRange("Movement speed of Texas", 0.3F, 0, 2);

            LapplandHealth = builder.defineInRange("Max Health of Lappland", 20D, 1, 1024);
            LapplandSwimSpeed = builder.defineInRange("Swim Speed of Lappland", 2.5, 0, 1024);
            LapplandAttackDamage = builder.defineInRange("Attack Damage of Lappland", 2.0F, 0, 1024);
            LapplandMovementSpeed = builder.defineInRange("Movement speed of Lappland", 0.3F, 0, 2);

            SiegeHealth = builder.defineInRange("Max Health of Siege", 20D, 1, 1024);
            SiegeSwimSpeed = builder.defineInRange("Swim Speed of Siege", 2.5, 0, 1024);
            SiegeAttackDamage = builder.defineInRange("Attack Damage of Siege", 2.0F, 0, 1024);
            SiegeMovementSpeed = builder.defineInRange("Movement speed of Siege", 0.3F, 0, 2);

            NearlHealth = builder.defineInRange("Max Health of Nearl", 20D, 1, 1024);
            NearlSwimSpeed = builder.defineInRange("Swim Speed of Nearl", 2.5, 0, 1024);
            NearlAttackDamage = builder.defineInRange("Attack Damage of Nearl", 2.0F, 0, 1024);
            NearlMovementSpeed = builder.defineInRange("Movement speed of Nearl", 0.3F, 0, 2);

            SchwarzHealth = builder.defineInRange("Max Health of Schwarz", 20D, 1, 1024);
            SchwarzSwimSpeed = builder.defineInRange("Swim Speed of Schwarz", 2.5, 0, 1024);
            SchwarzAttackDamage = builder.defineInRange("Attack Damage of Schwarz", 2.0F, 0, 1024);
            SchwarzMovementSpeed = builder.defineInRange("Movement speed of Schwarz", 0.3F, 0, 2);

            AmiyaHealth = builder.defineInRange("Max Health of Amiya", 20D, 1, 1024);
            AmiyaSwimSpeed = builder.defineInRange("Swim Speed of Amiya", 2.5, 0, 1024);
            AmiyaAttackDamage = builder.defineInRange("Attack Damage of Amiya", 2.0F, 0, 1024);
            AmiyaMovementSpeed = builder.defineInRange("Movement speed of Amiya", 0.3F, 0, 2);

            RosmontisHealth = builder.defineInRange("Max Health of Rosmontis", 20D, 1, 1024);
            RosmontisSwimSpeed = builder.defineInRange("Swim Speed of Rosmontis", 2.5, 0, 1024);
            RosmontisAttackDamage = builder.defineInRange("Attack Damage of Rosmontis", 2.0F, 0, 1024);
            RosmontisMovementSpeed = builder.defineInRange("Movement speed of Rosmontis", 0.3F, 0, 2);

            MudrockHealth = builder.defineInRange("Max Health of Mudrock", 20D, 1, 1024);
            MudrockSwimSpeed = builder.defineInRange("Swim Speed of Mudrock", 2.5, 0, 1024);
            MudrockAttackDamage = builder.defineInRange("Attack Damage of Mudrock", 2.0F, 0, 1024);
            MudrockMovementSpeed = builder.defineInRange("Movement speed of Mudrock", 0.3F, 0, 2);

            FrostnovaHealth = builder.defineInRange("Max Health of Frostnova", 20D, 1, 1024);
            FrostnovaSwimSpeed = builder.defineInRange("Swim Speed of Frostnova", 2.5, 0, 1024);
            FrostnovaAttackDamage = builder.defineInRange("Attack Damage of Frostnova", 2.0F, 0, 1024);
            FrostnovaMovementSpeed = builder.defineInRange("Movement speed of Frostnova", 0.3F, 0, 2);

            TalulahHealth = builder.defineInRange("Max Health of Talulah", 40D, 1, 1024);
            TalulahSwimSpeed = builder.defineInRange("Swim Speed of Talulah", 2.5, 0, 1024);
            TalulahAttackDamage = builder.defineInRange("Attack Damage of Talulah", 2.0F, 0, 1024);
            TalulahMovementSpeed = builder.defineInRange("Movement speed of Talulah", 0.3F, 0, 2);

            WHealth = builder.defineInRange("Max Health of W", 20D, 1, 1024);
            WSwimSpeed = builder.defineInRange("Swim Speed of W", 2.5, 0, 1024);
            WAttackDamage = builder.defineInRange("Attack Damage of W", 2.0F, 0, 1024);
            WMovementSpeed = builder.defineInRange("Movement speed of W", 0.3F, 0, 2);

            CrownslayerHealth = builder.defineInRange("Max Health of Crownslayer", 20D, 1, 1024);
            CrownslayerSwimSpeed = builder.defineInRange("Swim Speed of Crownslayer", 2.5, 0, 1024);
            CrownslayerAttackDamage = builder.defineInRange("Attack Damage of Crownslayer", 2.0F, 0, 1024);
            CrownslayerMovementSpeed = builder.defineInRange("Movement speed of Crownslayer", 0.3F, 0, 2);

            YatoHealth = builder.defineInRange("Max Health of Yato", 20D, 1, 1024);
            YatoSwimSpeed = builder.defineInRange("Swim Speed of Yato", 2.5, 0, 1024);
            YatoAttackDamage = builder.defineInRange("Attack Damage of Yato", 2.0F, 0, 1024);
            YatoMovementSpeed = builder.defineInRange("Movement speed of Yato", 0.3F, 0, 2);

            builder.pop().push("Warship Girls");
            GangwonHealth = builder.defineInRange("Max Health of Gangwon", 40D, 1, 1024);
            GangwonSwimSpeed = builder.defineInRange("Swim Speed of Gangwon", 2.5, 0, 1024);
            GangwonAttackDamage = builder.defineInRange("Attack Damage of Gangwon", 2.0F, 0, 1024);
            GangwonMovementSpeed = builder.defineInRange("Movement speed of Gangwon", 0.3F, 0, 2);

            builder.pop().push("Closers");
            SylviHealth = builder.defineInRange("Max Health of Sylvi Lee", 20D, 1, 1024);
            SylviSwimSpeed = builder.defineInRange("Swim Speed of Sylvi Lee", 2.5, 0, 1024);
            SylviAttackDamage = builder.defineInRange("Attack Damage of Sylvi Lee" , 2.0F, 0, 1024);
            SylviMovementSpeed = builder.defineInRange("Movement speed of Sylvi Lee", 0.3F, 0, 2);

            builder.pop().push("Kantai Collection");
            YamatoHealth = builder.defineInRange("Max Health of Yamato", 40D, 1, 1024);
            YamatoSwimSpeed = builder.defineInRange("Swim Speed of Yamato", 2.5, 0, 1024);
            YamatoAttackDamage = builder.defineInRange("Attack Damage of Yamato" , 2.0F, 0, 1024);
            YamatoMovementSpeed = builder.defineInRange("Movement speed of Yamato", 0.3F, 0, 2);

            builder.pop().push("Fate/Grand Order");
            ArtoriaHealth = builder.defineInRange("Max Health of Artoria", 30D, 1, 1024);
            ArtoriaSwimSpeed = builder.defineInRange("Swim Speed of Artoria", 2.5, 0, 1024);
            ArtoriaAttackDamage = builder.defineInRange("Attack Damage of Artoria" , 2.0F, 0, 1024);
            ArtoriaMovementSpeed = builder.defineInRange("Movement speed of Artoria", 0.3F, 0, 2);

            ScathathHealth = builder.defineInRange("Max Health of Scathath", 30D, 1, 1024);
            ScathathSwimSpeed = builder.defineInRange("Swim Speed of Scathath", 2.5, 0, 1024);
            ScathathAttackDamage = builder.defineInRange("Attack Damage of Scathath" , 2.0F, 0, 1024);
            ScathathMovementSpeed = builder.defineInRange("Movement speed of Scathath", 0.3F, 0, 2);

            MashHealth = builder.defineInRange("Max Health of Mash", 40D, 1, 1024);
            MashSwimSpeed = builder.defineInRange("Swim Speed of Mash", 2.5, 0, 1024);
            MashAttackDamage = builder.defineInRange("Attack Damage of Mash" , 2.0F, 0, 1024);
            MashMovementSpeed = builder.defineInRange("Movement speed of Mash", 0.3F, 0, 2);

            ShikiHealth = builder.defineInRange("Max Health of Shiki", 30D, 1, 1024);
            ShikiSwimSpeed = builder.defineInRange("Swim Speed of Shiki", 2.5, 0, 1024);
            ShikiAttackDamage = builder.defineInRange("Attack Damage of Shiki" , 2.0F, 0, 1024);
            ShikiMovementSpeed = builder.defineInRange("Movement speed of Shiki", 0.3F, 0, 2);

            builder.pop().push("Princess Connect: REDIVE");
            KyaruHealth = builder.defineInRange("Max Health of Kyaru", 20D, 1, 1024);
            KyaruSwimSpeed = builder.defineInRange("Swim Speed of Kyaru", 2.5, 0, 1024);
            KyaruAttackDamage = builder.defineInRange("Attack Damage of Kyaru", 2.0F, 0, 1024);
            KyaruMovementSpeed = builder.defineInRange("Movement speed of Kyaru", 0.3F, 0, 2);

            builder.pop().push("Shining Resonance");
            ExcelaHealth = builder.defineInRange("Max Health of Excela", 20D, 1, 1024);
            ExcelaSwimSpeed = builder.defineInRange("Swim Speed of Excela", 2.5, 0, 1024);
            ExcelaAttackDamage = builder.defineInRange("Attack Damage of Excela", 2.0F, 0, 1024);
            ExcelaMovementSpeed = builder.defineInRange("Movement speed of Excela", 0.3F, 0, 2);
            ExcelaArmor = builder.defineInRange("Armor Value of Excela", 15F, 0, 2);

            builder.pop().push("Girl's Frontline");
            M4A1Health = builder.defineInRange("Max Health of M4A1", 40D, 1, 1024);
            M4A1SwimSpeed = builder.defineInRange("Swim Speed of M4A1", 2.5, 0, 1024);
            M4A1AttackDamage = builder.defineInRange("Attack Damage of M4A1", 2.0F, 0, 1024);
            M4A1MovementSpeed = builder.defineInRange("Movement speed of M4A1", 0.3F, 0, 2);

            HK416Health = builder.defineInRange("Max Health of HK416", 40D, 1, 1024);
            HK416SwimSpeed = builder.defineInRange("Swim Speed of HK416", 2.5, 0, 1024);
            HK416AttackDamage = builder.defineInRange("Attack Damage of HK416", 2.0F, 0, 1024);
            HK416MovementSpeed = builder.defineInRange("Movement speed of HK416", 0.3F, 0, 2);

            builder.pop().pop().push("Gacha Roll RNG").comment("Chance of Roll per rarity.").comment("1 star is most common, and 6 star is most rare.").comment("Higher the number is, Commoner the item is.");
            ALLOW_DUPLICATE = builder.define("Allow Duplicate Entity for same player?", false);

            Star_1_Chance =  builder.defineInRange("1 Star Rarity", 60D, 0, Double.MAX_VALUE);
            Star_2_Chance = builder.defineInRange("2 Star Rarity", 20D, 0, Double.MAX_VALUE);
            Star_3_Chance = builder.defineInRange("3 Star Rarity", 10D, 0, Double.MAX_VALUE);
            Star_4_Chance = builder.defineInRange("4 Star Rarity", 5D, 0, Double.MAX_VALUE);
            Star_5_Chance = builder.defineInRange("5 Star Rarity", 3D, 0, Double.MAX_VALUE);
            Star_6_Chance = builder.defineInRange("6 Star Rarity", 2D, 0, Double.MAX_VALUE);

            builder.comment("Construction/'Recruiting time for each rarity in seconds");
            Star_1_MinTime = builder.defineInRange("1 Star Minimum Recruiting Time", 450, 0, Integer.MAX_VALUE);
            Star_1_MaxTime = builder.defineInRange("1 Star Maximum Recruiting Time", 90, 0, Integer.MAX_VALUE);
            Star_2_MinTime = builder.defineInRange("2 Star Minimum Recruiting Time", 60, 0, Integer.MAX_VALUE);
            Star_2_MaxTime = builder.defineInRange("2 Star Maximum Recruiting Time", 100, 0, Integer.MAX_VALUE);
            Star_3_MinTime = builder.defineInRange("3 Star Minimum Recruiting Time", 70, 0, Integer.MAX_VALUE);
            Star_3_MaxTime = builder.defineInRange("3 Star Maximum Recruiting Time", 140, 0, Integer.MAX_VALUE);
            Star_4_MinTime = builder.defineInRange("4 Star Minimum Recruiting Time", 110, 0, Integer.MAX_VALUE);
            Star_4_MaxTime = builder.defineInRange("4 Star Maximum Recruiting Time", 180, 0, Integer.MAX_VALUE);
            Star_5_MinTime = builder.defineInRange("5 Star Minimum Recruiting Time", 150, 0, Integer.MAX_VALUE);
            Star_5_MaxTime = builder.defineInRange("5 Star Maximum Recruiting Time", 210, 0, Integer.MAX_VALUE);
            Star_6_MinTime = builder.defineInRange("6 Star Minimum Recruiting Time", 180, 0, Integer.MAX_VALUE);
            Star_6_MaxTime = builder.defineInRange("6 Star Maximum Recruiting Time", 250, 0, Integer.MAX_VALUE);

            builder.pop().push("Performance").comment("Things that might impact performance");
            BeaconFindSpawnPositionTries = builder.defineInRange("How many tries should Recruit beacon try to find spawn position of entity before returning machine's own position?", 5, 1, 20);

            builder.pop().push("Worldgen").comment("Options about worldgen like ores");
            ENABLE_COPPER = builder.define("Enable Copper Generation", true);
            COPPER_VEINSPERCHUNK = builder.defineInRange("Count of copper vein in single chunk", 8,0,128);
            COPPER_VEINSIZE = builder.defineInRange("Size of Copper vein", 4,0,512);
            COPPER_MINHEIGHT = builder.defineInRange("Minimum Y axis value that copper can generate", 0,0,256);
            COPPER_MAXHEIGHT = builder.defineInRange("Maximum Y axis value that copper can generate", 60,0,256);

            ENABLE_ZINC = builder.define("Enable Zinc Generation", true);
            ZINC_VEINSPERCHUNK = builder.defineInRange("Count of Zinc vein in single chunk", 3,0,128);
            ZINC_VEINSIZE = builder.defineInRange("Size of Zinc vein", 5,0,512);
            ZINC_MINHEIGHT = builder.defineInRange("Minimum Y axis value that Zinc can generate", 0,0,256);
            ZINC_MAXHEIGHT = builder.defineInRange("Maximum Y axis value that Zinc can generate", 40,0,256);

            ENABLE_TIN = builder.define("Enable Tin Generation", true);
            TIN_VEINSPERCHUNK = builder.defineInRange("Count of Tin vein in single chunk", 4,0,128);
            TIN_VEINSIZE = builder.defineInRange("Size of Tin vein", 7,0,512);
            TIN_MINHEIGHT = builder.defineInRange("Minimum Y axis value that Tin can generate", 0,0,256);
            TIN_MAXHEIGHT = builder.defineInRange("Maximum Y axis value that Tin can generate", 60,0,256);

            ENABLE_LEAD = builder.define("Enable Lead Generation", true);
            LEAD_VEINSPERCHUNK = builder.defineInRange("Count of Lead vein in single chunk", 4,0,128);
            LEAD_VEINSIZE = builder.defineInRange("Size of Lead vein", 4,0,512);
            LEAD_MINHEIGHT = builder.defineInRange("Minimum Y axis value that Lead can generate", 5,0,256);
            LEAD_MAXHEIGHT = builder.defineInRange("Maximum Y axis value that Lead can generate", 40,0,256);

            ENABLE_ORIROCK = builder.define("Enable Orirock Generation", true);
            ORIROCK_VEINSPERCHUNK = builder.defineInRange("Count of Orirock vein in single chunk", 4,0,128);
            ORIROCK_VEINSIZE = builder.defineInRange("Size of Orirock vein", 40,0,512);
            ORIROCK_MINHEIGHT = builder.defineInRange("Minimum Y axis value that Orirock can generate", 0,0,256);
            ORIROCK_MAXHEIGHT = builder.defineInRange("Maximum Y axis value that Orirock can generate", 50,0,256);

            builder.pop();
            builder.push("Debug");
            RedStonePoweredMachines = builder.define("Make Machines can be powered with redstone", false);

            builder.pop().push("Misc").comment("Other Thingys");
            shouldRecruitBeaconSpawnAllCompanions = builder.define("Should Recruit Beacon Spawn ALL Companion?", false);
            death_type = builder.defineEnum("What should happen when companion fully dies?", RESPAWN);
            InjuredRecoveryTimer = builder.define("How long should it take before companion to recover from injury? (in seconds, please.)", 8400);
            FaintTimeLimit = builder.define("How long should it take before companion die after fainting? (in seconds, set it to 0 if you wish to disable fainting mechanic)", 1200);
            builder.pop();

            builder.push("Cheats").comment("wuss mode");
            RiggingInfiniteFuel = builder.define("Rigging does not require fuel", false);
            builder.pop();
        }
    }

    public enum COMPANION_DEATH{
        PERMADEATH,
        STASIS_CRYSTAL,
        RESPAWN
    }
}
