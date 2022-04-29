package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.items.*;
import com.yor42.projectazure.gameobject.items.gun.ItemAbydos550;
import com.yor42.projectazure.gameobject.items.materials.ModMaterials;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBBDefault;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingCVDefault;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentGun127Mm;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentTorpedo533Mm;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemPlanef4Fwildcat;
import com.yor42.projectazure.gameobject.items.tools.*;
import com.yor42.projectazure.gameobject.misc.ModFoods;
import com.yor42.projectazure.libs.enums;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.Main.*;

public class registerItems {

    //Resources.
    //Remove on 1.17
    public static final RegistryObject<Item> INGOT_COPPER = ITEMS.register("ingot_copper", () -> new ItemResource("copper", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_LEAD = ITEMS.register("ingot_lead", () ->new ItemResource("lead", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_TIN = ITEMS.register("ingot_tin", () -> new ItemResource("tin", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_ALUMINIUM = ITEMS.register("ingot_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_BRONZE = ITEMS.register("ingot_bronze", () -> new ItemResource("bronze", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_ZINC = ITEMS.register("ingot_zinc", () -> new ItemResource("zinc", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_STEEL = ITEMS.register("ingot_steel", () -> new ItemResource("steel", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_BRASS = ITEMS.register("ingot_brass", () -> new ItemResource("brass", enums.ResourceType.INGOT));

    public static final RegistryObject<Item> GEAR_COPPER = ITEMS.register("gear_copper", () -> new ItemResource("copper", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_TIN = ITEMS.register("gear_tin", () -> new ItemResource("tin", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_BRONZE = ITEMS.register("gear_bronze", () -> new ItemResource("bronze", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_STEEL = ITEMS.register("gear_steel", () -> new ItemResource("steel", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_IRON = ITEMS.register("gear_iron", () -> new ItemResource("iron", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_GOLD = ITEMS.register("gear_gold", () -> new ItemResource("gold", enums.ResourceType.GEAR));

    public static final RegistryObject<Item> DUST_COPPER = ITEMS.register("dust_copper", () -> new ItemResource("copper", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_LEAD = ITEMS.register("dust_lead", () -> new ItemResource("lead", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_TIN = ITEMS.register("dust_tin", () -> new ItemResource("tin", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ALUMINIUM = ITEMS.register("dust_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_BRONZE = ITEMS.register("dust_bronze", () -> new ItemResource("bronze", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ZINC = ITEMS.register("dust_zinc", () -> new ItemResource("bronze", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_IRON = ITEMS.register("dust_iron", () -> new ItemResource("iron", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_COAL = ITEMS.register("dust_coal", () -> new ItemResource("coal", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_STEEL = ITEMS.register("dust_steel", () -> new ItemResource("steel", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_BRASS = ITEMS.register("dust_brass", () -> new ItemResource("brass", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_GOLD = ITEMS.register("dust_gold", () -> new ItemResource("gold", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ORIGINIUM = ITEMS.register("dust_originium", () -> new ItemResource("originium", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_NETHER_QUARTZ = ITEMS.register("dust_quartz", () -> new ItemResource("nether_quartz", enums.ResourceType.DUST));

    public static final RegistryObject<Item> PLATE_COPPER = ITEMS.register("plate_copper", () -> new ItemResource("copper", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_LEAD = ITEMS.register("plate_lead", () -> new ItemResource("lead", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_TIN = ITEMS.register("plate_tin", () -> new ItemResource("tin", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_ALUMINIUM = ITEMS.register("plate_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_BRONZE = ITEMS.register("plate_bronze", () -> new ItemResource("bronze", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_ZINC = ITEMS.register("plate_zinc", () -> new ItemResource("zinc", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_IRON = ITEMS.register("plate_iron", () -> new ItemResource("iron", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_GOLD = ITEMS.register("plate_gold", () -> new ItemResource("gold", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_STEEL = ITEMS.register("plate_steel", () -> new ItemResource("steel", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_BRASS = ITEMS.register("plate_brass", () -> new ItemResource("brass", enums.ResourceType.PLATE));

    public static final RegistryObject<Item> NUGGET_COPPER = ITEMS.register("nugget_copper", () -> new ItemResource("copper", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_LEAD = ITEMS.register("nugget_lead", () -> new ItemResource("lead", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_TIN = ITEMS.register("nugget_tin", () -> new ItemResource("tin", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_BRONZE = ITEMS.register("nugget_bronze", () -> new ItemResource("bronze", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_ZINC = ITEMS.register("nugget_zinc", () -> new ItemResource("zinc", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_STEEL = ITEMS.register("nugget_steel", () -> new ItemResource("steel", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_BRASS = ITEMS.register("nugget_brass", () -> new ItemResource("brass", enums.ResourceType.NUGGET));

    //Electronic Stuff
    public static final RegistryObject<Item> COPPER_WIRE = ITEMS.register("copper_wire", () -> new ItemResource("copper", enums.ResourceType.WIRE));
    public static final RegistryObject<Item> IRON_PIPE = ITEMS.register("iron_pipe", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> STEEL_PIPE = ITEMS.register("steel_pipe", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> MECHANICAL_PARTS = ITEMS.register("mechanical_parts", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> BASIC_MOTOR = ITEMS.register("motor_basic", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> PRIMITIVE_CIRCUIT = ITEMS.register("circuit_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> ADVANCED_CIRCUIT = ITEMS.register("circuit_advanced", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> COPPER_COIL = ITEMS.register("copper_coil", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> CAPACITOR_PRIMITIVE = ITEMS.register("capacitor_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> RESISTOR_PRIMITIVE = ITEMS.register("resistor_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    //Natural Resource
    public static final RegistryObject<Item> TREE_SAP = ITEMS.register("tree_sap", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> BITUMEN = ITEMS.register("bitumen", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> PLATE_POLYMER = ITEMS.register("polymer_plate", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    //Originium Engineering
    public static final RegistryObject<Item> ORIGINIUM_SEED = ITEMS.register("originium_seed", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> NETHER_QUARTZ_SEED = ITEMS.register("quartz_seed", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ORIGINITE = ITEMS.register("originite", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ORIGINIUM_PRIME = ITEMS.register("originium_prime", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES).food(ModFoods.ORIGINIUM_PRIME)));

    public static final RegistryObject<Item> HEADHUNTING_PCB = ITEMS.register("headhunting_pcb", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES).stacksTo(16)));
    public static final RegistryObject<Item> ORUNDUM = ITEMS.register("orundum", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> MOLD_PLATE = ITEMS.register("mold_plate", () -> new ItemCraftTool(128));
    public static final RegistryObject<Item> MOLD_WIRE = ITEMS.register("mold_wire", () -> new ItemCraftTool(128));
    public static final RegistryObject<Item> MOLD_EXTRACTION = ITEMS.register("mold_extraction", () -> new ItemCraftTool(128));

    public static final RegistryObject<Item> ENERGY_DRINK_DEBUG = ITEMS.register("energy_drink", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC)){
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            if(worldIn != null && worldIn.isClientSide) {
                if (Screen.hasShiftDown()) {
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip1").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(0xff00fc)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip2"));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip3"));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip4").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x990000)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip5").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip6").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip7").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x999900)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip8").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip9").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip10").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                } else {
                    Component shift = new TextComponent("[SHIFT]").withStyle(ChatFormatting.YELLOW);
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.desc1").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e))));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.shiftinfo", shift).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    });


    //crafting items
    public static final RegistryObject<Item> MORTAR_IRON = ITEMS.register("mortar_iron", () -> new ItemCraftTool(50));

    public static final RegistryObject<Item> HAMMER_IRON = ITEMS.register("hammer_iron", () -> new ItemCraftTool(43));

    public static final RegistryObject<Item> STEEL_CUTTER = ITEMS.register("steel_cutter", () -> new ItemCraftTool(70));


    public static final RegistryObject<Item> MAGAZINE_5_56 = ITEMS.register("5.56_magazine", () -> new ItemMagazine(enums.AmmoCalibur.AMMO_5_56, 30, new Item.Properties()
            .tab(PA_GROUP)
            .stacksTo(1)
            .durability(7)));

    public static final RegistryObject<Item> Rainbow_Wisdom_Cube = ITEMS.register("rainbow_wisdomcube", () -> new itemRainbowWisdomCube(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> CRUDE_OIL_BUCKET = ITEMS.register("crude_oil_bucket", ()-> new BucketItem(()->registerFluids.CRUDE_OIL_SOURCE, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> GASOLINE_BUCKET = ITEMS.register("gasoline_bucket", ()-> new BucketItem(()->registerFluids.GASOLINE_SOURCE, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> DIESEL_BUCKET = ITEMS.register("diesel_bucket", ()-> new BucketItem(()->registerFluids.DIESEL_SOURCE, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> FUEL_OIL_BUCKET = ITEMS.register("fuel_oil_bucket", ()-> new BucketItem(()->registerFluids.FUEL_OIL_SOURCE, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));


    public static final RegistryObject<Item> WISDOM_CUBE = ITEMS.register("wisdomcube", () -> new ItemBaseTooltip(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> OATHRING = ITEMS.register("oath_ring", () -> new ItemBaseTooltip(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC)
            .stacksTo(1)){
        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    });

    public static final RegistryObject<Item> PISTOL_GRIP = ITEMS.register("pistol_grip", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> STEEL_RIFLE_FRAME = ITEMS.register("steel_gunframe_rifle", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> AMMO_GENERIC = ITEMS.register("ammo_generic", () -> new ItemCannonshell(enums.AmmoCategory.GENERIC ,new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> AMMO_5_56 = ITEMS.register("5_56_ammo", () -> new ItemAmmo(enums.AmmoCalibur.AMMO_5_56, 6 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(5)));

    public static final RegistryObject<Item> AMMO_TORPEDO = ITEMS.register("torpedo_ammo", () -> new ItemAmmo(enums.AmmoCalibur.TORPEDO, 1 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)));

    public static final RegistryObject<Item> AMMO_MISSILE = ITEMS.register("missile_ammo", () -> new ItemAmmo(enums.AmmoCalibur.DRONE_MISSILE, 1 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)));

    //I'M READY TO GO TONIIIIGHT YEAH THERES PARTY ALRIGHTTTTTTT WE DON'T NEED REASON FOR JOY OH YEAHHHHH
    public static final RegistryObject<Item> DISC_FRIDAYNIGHT = ITEMS.register("disc_fridaynight", () -> new RecordItem(15, ()->registerSounds.DISC_FRIDAY_NIGHT, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)){
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    //LET THE BASS KICK O-oooooooooo AAAA E A A I A U
    public static final RegistryObject<Item> DISC_BRAINPOWER = ITEMS.register("disc_brainpower", () -> new RecordItem(15, ()->registerSounds.DISC_BRAINPOWER, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Override
        public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            tooltip.add(new TranslatableComponent("disc.brainpower.desc1").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(7829367))));
        }

        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    //You know the rules and so do I
    public static final RegistryObject<Item> DISC_RICKROLL = ITEMS.register("disc_rickroll", () -> new RecordItem(15, ()->registerSounds.DISC_RICKROLL, new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_CC5 = ITEMS.register("disc_cc5", () -> new RecordItem(15, ()->registerSounds.DISC_CC5, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_SANDSTORM = ITEMS.register("disc_sandstorm", () -> new RecordItem(15, ()->registerSounds.DISC_SANDSTORM, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_SANDROLL = ITEMS.register("disc_sandroll", () -> new RecordItem(15, ()->registerSounds.DISC_SANDROLL, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_ENTERTHEBEGINNING = ITEMS.register("disc_enterthebeginning", () -> new RecordItem(15, ()->registerSounds.DISC_ENTERTHEBEGINNING, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            tooltip.add(new TranslatableComponent("disc.enterthebeginning.desc1").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(7829367))));
        }
    });

    public static final RegistryObject<Item> ABYDOS_550 = ITEMS.register("abydos550", () -> new ItemAbydos550(false, 2, 30, 72, 3, registerSounds.RIFLE_FIRE_SUPPRESSED, SoundEvents.LEVER_CLICK, 0, 0.5F, new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), true, MAGAZINE_5_56.get()));

    public static final RegistryObject<Item> CHIXIAO = ITEMS.register("chixiao", () -> new ModSwordItem(ModMaterials.CHIXIAO, 1, -1.7F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> SHEATH = ITEMS.register("sheath", () -> new ModSwordItem(ModMaterials.SHEATH, 1, -1.5F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> CRESCENTKATANA_SHIRO = ITEMS.register("crescentkatana_shiro", () -> new ModSwordItem(ModMaterials.CRESCENT_KATANA_SHIRO, 1, -1.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> CRESCENTKATANA_KURO = ITEMS.register("crescentkatana_kuro", () -> new ModSwordItem(ModMaterials.CRESCENT_KATANA_KURO, 1, -1.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> WARHAMMER = ITEMS.register("warhammer", () -> new ModSwordItem(ModMaterials.WARHAMMER, 1, -1.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> FLEXABLE_SWORD_THINGY = ITEMS.register("flexsword", () -> new ModSwordItem(ModMaterials.FLEXABLESWORD, 1, -1.0F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> TACTICAL_KNIFE = ITEMS.register("tactical_knife", () -> new ItemThrowableKnife(ModMaterials.KNIFE, 3, -0.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> COMPOUNDBOW = ITEMS.register("compoundbow", () -> new BowItem(new Item.Properties().tab(PA_WEAPONS).durability(1000)));
    public static final RegistryObject<Item> BONKBAT = ITEMS.register("bonk_bat", () -> new ItemBonkBat(new Item.Properties()
            .tab(PA_WEAPONS)
            .rarity(Rarity.EPIC)
            .stacksTo(1)));
    public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS.register("sledgehammer", () -> new ItemSledgeHammer(10, -3.75F, ModMaterials.SLEDGEHAMMER, new Item.Properties().tab(PA_WEAPONS).stacksTo(1)));
    public static final RegistryObject<Item> CLAYMORE = ITEMS.register("claymore", ItemClaymore::new);


    public static final RegistryObject<Item> COMMANDING_STICK = ITEMS.register("commanding_stick", ItemCommandStick::new);

    public static final RegistryObject<Item> DD_DEFAULT_RIGGING = ITEMS.register("dd_default_rigging", () -> new itemRiggingDDDefault(new Item.Properties()
    .tab(PA_WEAPONS).stacksTo(1), 500));

    public static final RegistryObject<Item> CV_DEFAULT_RIGGING = ITEMS.register("cv_default_rigging", () -> new ItemRiggingCVDefault(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 750));

    public static final RegistryObject<Item> BB_DEFAULT_RIGGING = ITEMS.register("bb_default_rigging", () -> new ItemRiggingBBDefault(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 1200));

    public static final RegistryObject<Item> EQUIPMENT_TORPEDO_533MM = ITEMS.register("equipment_torpedo_533mm", () -> new ItemEquipmentTorpedo533Mm(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 40));

    public static final RegistryObject<Item> EQUIPMENT_GUN_127MM = ITEMS.register("equipment_gun_127mm", () -> new ItemEquipmentGun127Mm(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 40));

    public static float WildcatHP = 30;

    public static final RegistryObject<Item> EQUIPMENT_PLANE_F4FWildcat = ITEMS.register("equipment_plane_f4fwildcat", () -> new ItemPlanef4Fwildcat(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), (int) WildcatHP));


    public static final RegistryObject<Item> DRONE_BAMISSILE = ITEMS.register("missiledrone", () -> new ItemMissleDrone(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1),10, 20000));

    public static final RegistryObject<Item> SPAWM_AYANAMI = ITEMS.register("spawnayanami", () -> new ItemKansenSpawnEgg(ENTITYTYPE_AYANAMI, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWM_JAVELIN = ITEMS.register("spawnjavelin", () -> new ItemKansenSpawnEgg(ENTITYTYPE_JAVELIN, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_Z23 = ITEMS.register("spawnz23", () -> new ItemKansenSpawnEgg(ENTITYTYPE_Z23, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_GANGWON = ITEMS.register("spawngangwon", () -> new ItemKansenSpawnEgg(ENTITYTYPE_GANGWON, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_LAFFEY = ITEMS.register("spawnlaffey", () -> new ItemKansenSpawnEgg(ENTITYTYPE_LAFFEY, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWM_ENTERPRISE = ITEMS.register("spawnenterprise", ()-> new ItemKansenSpawnEgg(ENTITYTYPE_ENTERPRISE, new Item.Properties().tab(PA_SHIPS)));


    public static final RegistryObject<Item> SPAWN_NAGATO = ITEMS.register("spawnnagato", () -> new ItemKansenSpawnEgg(ENTITYTYPE_NAGATO, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_CHEN = ITEMS.register("spawnchen", () -> new ItemKansenSpawnEgg(ENTITYTYPE_CHEN, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_MUDROCK = ITEMS.register("spawnmudrock", () -> new ItemKansenSpawnEgg(ENTITYTYPE_MUDROCK, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_M4A1 = ITEMS.register("spawnm4a1", () -> new ItemKansenSpawnEgg(ENTITYTYPE_M4A1, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_AMIYA = ITEMS.register("spawnamiya", () -> new ItemKansenSpawnEgg(ENTITYTYPE_AMIYA, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_ROSMONTIS = ITEMS.register("spawnrosmontis", () -> new ItemKansenSpawnEgg(ENTITYTYPE_ROSMONTIS, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_TALULAH = ITEMS.register("spawntalulah", () -> new ItemKansenSpawnEgg(ENTITYTYPE_TALULAH, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_SHIROKO = ITEMS.register("spawnshiroko", () -> new ItemKansenSpawnEgg(ENTITYTYPE_SHIROKO, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_TEXAS = ITEMS.register("spawntexas", () -> new ItemKansenSpawnEgg(ENTITYTYPE_TEXAS, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_FROSTNOVA = ITEMS.register("spawnfrostnova", () -> new ItemKansenSpawnEgg(ENTITYTYPE_FROSTNOVA, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_SIEGE = ITEMS.register("spawnsiege", () -> new ItemKansenSpawnEgg(ENTITYTYPE_SIEGE, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_SCHWARZ = ITEMS.register("spawnschwarz", () -> new ItemKansenSpawnEgg(ENTITYTYPE_SCHWARZ, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_LAPPLAND = ITEMS.register("spawnlappland", () -> new ItemKansenSpawnEgg(ENTITYTYPE_LAPPLAND, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> BANDAGE_ROLL = ITEMS.register("bandage_roll", () -> new ItemBandage(new Item.Properties()
            .tab(PA_GROUP)));
    public static final RegistryObject<Item> SPAWM_SYLVI = ITEMS.register("spawnsylvi", () -> new ItemKansenSpawnEgg(ENTITYTYPE_SYLVI, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWM_YAMATO = ITEMS.register("spawnyamato", () -> new ItemKansenSpawnEgg(ENTITYTYPE_YAMATO, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit", ItemMedKit::new);

    public static final RegistryObject<Item> DEFIB_PADDLE = ITEMS.register("defib_paddle", ItemDefibPaddle::new);
    public static final RegistryObject<Item> DEFIB_CHARGER = ITEMS.register("defib_charger", ItemDefibCharger::new);

    public static final RegistryObject<Item> STASIS_CRYSTAL = ITEMS.register("stasis_crystal", ItemStasisCrystal::new);

    public static final RegistryObject<Item> DEVELOPER_BONUS = ITEMS.register("developer_bonus", () -> new ItemContributorBonus(new Item.Properties()));
    public static final RegistryObject<Item> CONTRIBUTOR_BONUS = ITEMS.register("contributor_bonus", () -> new ItemContributorBonus(new Item.Properties()));

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event){};
}
