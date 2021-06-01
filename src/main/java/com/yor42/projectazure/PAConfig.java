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
        }
    }

}
