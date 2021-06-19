package com.yor42.projectazure;

import com.yor42.projectazure.libs.defined;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = defined.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

        public final ForgeConfigSpec.DoubleValue AyanamiHealth;
        public final ForgeConfigSpec.DoubleValue AyanamiSwimSpeed;
        public final ForgeConfigSpec.DoubleValue AyanamiAttackDamage;
        public final ForgeConfigSpec.DoubleValue AyanamiMovementSpeed;

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

        public final ForgeConfigSpec.DoubleValue ChenHealth;
        public final ForgeConfigSpec.DoubleValue ChenSwimSpeed;
        public final ForgeConfigSpec.DoubleValue ChenAttackDamage;
        public final ForgeConfigSpec.DoubleValue ChenMovementSpeed;

        public final ForgeConfigSpec.DoubleValue Star_1_Chance;
        public final ForgeConfigSpec.DoubleValue Star_2_Chance;
        public final ForgeConfigSpec.DoubleValue Star_3_Chance;
        public final ForgeConfigSpec.DoubleValue Star_4_Chance;
        public final ForgeConfigSpec.DoubleValue Star_5_Chance;
        public final ForgeConfigSpec.DoubleValue Star_6_Chance;

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

        public final ForgeConfigSpec.BooleanValue RedStonePoweredMachines;

        private PAModConfig(ForgeConfigSpec.Builder builder){

            builder.push("General");
            EnablePVP = builder.define("Enable PVP Combat", false);

            builder.pop().push("Entity Stat").comment("Azur lane");
            AyanamiHealth = builder.defineInRange("Max Health of Ayanami", 40D, 1, 1024);
            AyanamiSwimSpeed = builder.defineInRange("Swim Speed of Ayanami", 2.5, 0, 1024);
            AyanamiAttackDamage = builder.defineInRange("Attack Damage of Ayanami", 2.0F, 0, 1024);
            AyanamiMovementSpeed = builder.defineInRange("Movement speed of Ayanami", 0.35F, 0, 2);

            EnterpriseHealth = builder.defineInRange("Max Health of Enterprise", 30D, 1, 1024);
            EnterpriseSwimSpeed = builder.defineInRange("Swim Speed of Enterprise", 2.5, 0, 1024);
            EnterpriseAttackDamage = builder.defineInRange("Attack Damage of Enterprise", 2.0F, 0, 1024);
            EnterpriseMovementSpeed = builder.defineInRange("Movement speed of Enterprise", 0.35F, 0, 2);

            NagatoHealth = builder.defineInRange("Max Health of Nagato", 50D, 1, 1024);
            NagatoSwimSpeed = builder.defineInRange("Swim Speed of Nagato", 2.5, 0, 1024);
            NagatoAttackDamage = builder.defineInRange("Attack Damage of Nagato", 2.0F, 0, 1024);
            NagatoMovementSpeed = builder.defineInRange("Movement speed of Nagato", 0.35F, 0, 2);

            builder.comment("Blue Archive");
            ShirokoHealth = builder.defineInRange("Max Health of Shiroko", 40D, 1, 1024);
            ShirokoSwimSpeed = builder.defineInRange("Swim Speed of Shiroko", 2.5, 0, 1024);
            ShirokoAttackDamage = builder.defineInRange("Attack Damage of Shiroko", 2.0F, 0, 1024);
            ShirokoMovementSpeed = builder.defineInRange("Movement speed of Shiroko", 0.4F, 0, 2);

            builder.comment("Arknights");
            ChenHealth = builder.defineInRange("Max Health of Ch'en", 25D, 1, 1024);
            ChenSwimSpeed = builder.defineInRange("Swim Speed of Ch'en", 2.5, 0, 1024);
            ChenAttackDamage = builder.defineInRange("Attack Damage of Ch'en", 2.0F, 0, 1024);
            ChenMovementSpeed = builder.defineInRange("Movement speed of Ch'en", 0.25F, 0, 2);

            builder.comment("Warship Girls");
            GangwonHealth = builder.defineInRange("Max Health of Gangwon", 40D, 1, 1024);
            GangwonSwimSpeed = builder.defineInRange("Swim Speed of Gangwon", 2.5, 0, 1024);
            GangwonAttackDamage = builder.defineInRange("Attack Damage of Gangwon", 2.0F, 0, 1024);
            GangwonMovementSpeed = builder.defineInRange("Movement speed of Gangwon", 0.35F, 0, 2);

            builder.pop().push("Gacha Roll RNG").comment("Chance of Roll per rarity.").comment("1 star is most common, and 6 star is most rare.").comment("Higher the number is, Commoner the item is.");
            Star_1_Chance = (ForgeConfigSpec.DoubleValue) builder.define("1 Star Rarity", 60D);
            Star_2_Chance = (ForgeConfigSpec.DoubleValue) builder.define("2 Star Rarity", 20D);
            Star_3_Chance = (ForgeConfigSpec.DoubleValue) builder.define("3 Star Rarity", 10D);
            Star_4_Chance = (ForgeConfigSpec.DoubleValue) builder.define("4 Star Rarity", 5D);
            Star_5_Chance = (ForgeConfigSpec.DoubleValue) builder.define("5 Star Rarity", 3D);
            Star_6_Chance = (ForgeConfigSpec.DoubleValue) builder.define("6 Star Rarity", 2D);

            builder.comment("Construction/'Recruiting time for each rarity in seconds");
            Star_1_MinTime = (ForgeConfigSpec.IntValue) builder.define("1 Star Minimum Recruiting Time", 45);
            Star_1_MaxTime = (ForgeConfigSpec.IntValue) builder.define("1 Star Maximum Recruiting Time", 90);
            Star_2_MinTime = (ForgeConfigSpec.IntValue) builder.define("2 Star Minimum Recruiting Time", 60);
            Star_2_MaxTime = (ForgeConfigSpec.IntValue) builder.define("2 Star Maximum Recruiting Time", 100);
            Star_3_MinTime = (ForgeConfigSpec.IntValue) builder.define("3 Star Minimum Recruiting Time", 70);
            Star_3_MaxTime = (ForgeConfigSpec.IntValue) builder.define("3 Star Maximum Recruiting Time", 140);
            Star_4_MinTime = (ForgeConfigSpec.IntValue) builder.define("4 Star Minimum Recruiting Time", 110);
            Star_4_MaxTime = (ForgeConfigSpec.IntValue) builder.define("4 Star Maximum Recruiting Time", 180);
            Star_5_MinTime = (ForgeConfigSpec.IntValue) builder.define("5 Star Minimum Recruiting Time", 150);
            Star_5_MaxTime = (ForgeConfigSpec.IntValue) builder.define("5 Star Maximum Recruiting Time", 210);
            Star_6_MinTime = (ForgeConfigSpec.IntValue) builder.define("6 Star Minimum Recruiting Time", 180);
            Star_6_MaxTime = (ForgeConfigSpec.IntValue) builder.define("6 Star Maximum Recruiting Time", 250);

            builder.pop();
            builder.push("Debug");
            RedStonePoweredMachines = builder.define("Make Machines can be powered with redstone", false);
        }
    }

}
