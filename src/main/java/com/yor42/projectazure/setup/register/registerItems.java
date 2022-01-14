package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.client.renderer.equipment.Equipment127mmGunRenderer;
import com.yor42.projectazure.client.renderer.equipment.equipment533mmTorpedoRenderer;
import com.yor42.projectazure.client.renderer.items.*;
import com.yor42.projectazure.gameobject.items.*;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentGun127Mm;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentTorpedo533Mm;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemPlanef4Fwildcat;
import com.yor42.projectazure.gameobject.items.gun.ItemAbydos550;
import com.yor42.projectazure.gameobject.items.materials.ModMaterials;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBBDefault;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingCVDefault;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;
import com.yor42.projectazure.gameobject.items.tools.*;
import com.yor42.projectazure.gameobject.misc.ModFoods;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.Main.*;
import static com.yor42.projectazure.setup.register.registerManager.*;

public class registerItems {

    //Resources.
    //Remove on 1.17
    public static final RegistryObject<Item> INGOT_COPPER = registerManager.ITEMS.register("ingot_copper", () -> new ItemResource("copper", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_LEAD = registerManager.ITEMS.register("ingot_lead", () ->new ItemResource("lead", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_TIN = registerManager.ITEMS.register("ingot_tin", () -> new ItemResource("tin", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_ALUMINIUM = registerManager.ITEMS.register("ingot_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_BRONZE = registerManager.ITEMS.register("ingot_bronze", () -> new ItemResource("bronze", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_ZINC = registerManager.ITEMS.register("ingot_zinc", () -> new ItemResource("zinc", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_STEEL = registerManager.ITEMS.register("ingot_steel", () -> new ItemResource("steel", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_BRASS = registerManager.ITEMS.register("ingot_brass", () -> new ItemResource("brass", enums.ResourceType.INGOT));

    public static final RegistryObject<Item> GEAR_COPPER = registerManager.ITEMS.register("gear_copper", () -> new ItemResource("copper", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_TIN = registerManager.ITEMS.register("gear_tin", () -> new ItemResource("tin", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_BRONZE = registerManager.ITEMS.register("gear_bronze", () -> new ItemResource("bronze", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_STEEL = registerManager.ITEMS.register("gear_steel", () -> new ItemResource("steel", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_IRON = registerManager.ITEMS.register("gear_iron", () -> new ItemResource("iron", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_GOLD = registerManager.ITEMS.register("gear_gold", () -> new ItemResource("gold", enums.ResourceType.GEAR));

    public static final RegistryObject<Item> DUST_COPPER = registerManager.ITEMS.register("dust_copper", () -> new ItemResource("copper", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_LEAD = registerManager.ITEMS.register("dust_lead", () -> new ItemResource("lead", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_TIN = registerManager.ITEMS.register("dust_tin", () -> new ItemResource("tin", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ALUMINIUM = registerManager.ITEMS.register("dust_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_BRONZE = registerManager.ITEMS.register("dust_bronze", () -> new ItemResource("bronze", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ZINC = registerManager.ITEMS.register("dust_zinc", () -> new ItemResource("bronze", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_IRON = registerManager.ITEMS.register("dust_iron", () -> new ItemResource("iron", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_COAL = registerManager.ITEMS.register("dust_coal", () -> new ItemResource("coal", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_STEEL = registerManager.ITEMS.register("dust_steel", () -> new ItemResource("steel", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_BRASS = registerManager.ITEMS.register("dust_brass", () -> new ItemResource("brass", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_GOLD = registerManager.ITEMS.register("dust_gold", () -> new ItemResource("gold", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ORIGINIUM = registerManager.ITEMS.register("dust_originium", () -> new ItemResource("originium", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_NETHER_QUARTZ = registerManager.ITEMS.register("dust_quartz", () -> new ItemResource("nether_quartz", enums.ResourceType.DUST));

    public static final RegistryObject<Item> PLATE_COPPER = registerManager.ITEMS.register("plate_copper", () -> new ItemResource("copper", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_LEAD = registerManager.ITEMS.register("plate_lead", () -> new ItemResource("lead", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_TIN = registerManager.ITEMS.register("plate_tin", () -> new ItemResource("tin", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_ALUMINIUM = registerManager.ITEMS.register("plate_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_BRONZE = registerManager.ITEMS.register("plate_bronze", () -> new ItemResource("bronze", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_ZINC = registerManager.ITEMS.register("plate_zinc", () -> new ItemResource("zinc", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_IRON = registerManager.ITEMS.register("plate_iron", () -> new ItemResource("iron", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_GOLD = registerManager.ITEMS.register("plate_gold", () -> new ItemResource("gold", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_STEEL = registerManager.ITEMS.register("plate_steel", () -> new ItemResource("steel", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_BRASS = registerManager.ITEMS.register("plate_brass", () -> new ItemResource("brass", enums.ResourceType.PLATE));

    public static final RegistryObject<Item> NUGGET_COPPER = registerManager.ITEMS.register("nugget_copper", () -> new ItemResource("copper", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_LEAD = registerManager.ITEMS.register("nugget_lead", () -> new ItemResource("lead", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_TIN = registerManager.ITEMS.register("nugget_tin", () -> new ItemResource("tin", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_BRONZE = registerManager.ITEMS.register("nugget_bronze", () -> new ItemResource("bronze", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_ZINC = registerManager.ITEMS.register("nugget_zinc", () -> new ItemResource("zinc", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_STEEL = registerManager.ITEMS.register("nugget_steel", () -> new ItemResource("steel", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_BRASS = registerManager.ITEMS.register("nugget_brass", () -> new ItemResource("brass", enums.ResourceType.NUGGET));

    //Electronic Stuff
    public static final RegistryObject<Item> COPPER_WIRE = registerManager.ITEMS.register("copper_wire", () -> new ItemResource("copper", enums.ResourceType.WIRE));
    public static final RegistryObject<Item> IRON_PIPE = registerManager.ITEMS.register("iron_pipe", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> STEEL_PIPE = registerManager.ITEMS.register("steel_pipe", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> MECHANICAL_PARTS = registerManager.ITEMS.register("mechanical_parts", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> BASIC_MOTOR = registerManager.ITEMS.register("motor_basic", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> PRIMITIVE_CIRCUIT = registerManager.ITEMS.register("circuit_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> ADVANCED_CIRCUIT = registerManager.ITEMS.register("circuit_advanced", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> COPPER_COIL = registerManager.ITEMS.register("copper_coil", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> CAPACITOR_PRIMITIVE = registerManager.ITEMS.register("capacitor_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> RESISTOR_PRIMITIVE = registerManager.ITEMS.register("resistor_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    //Natural Resource
    public static final RegistryObject<Item> TREE_SAP = registerManager.ITEMS.register("tree_sap", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> BITUMEN = registerManager.ITEMS.register("bitumen", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> PLATE_POLYMER = registerManager.ITEMS.register("polymer_plate", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    //Originium Engineering
    public static final RegistryObject<Item> ORIGINIUM_SEED = registerManager.ITEMS.register("originium_seed", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> NETHER_QUARTZ_SEED = registerManager.ITEMS.register("quartz_seed", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ORIGINITE = registerManager.ITEMS.register("originite", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ORIGINIUM_PRIME = registerManager.ITEMS.register("originium_prime", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES).food(ModFoods.ORIGINIUM_PRIME)));

    public static final RegistryObject<Item> HEADHUNTING_PCB = registerManager.ITEMS.register("headhunting_pcb", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES).stacksTo(16)));
    public static final RegistryObject<Item> ORUNDUM = registerManager.ITEMS.register("orundum", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> MOLD_PLATE = registerManager.ITEMS.register("mold_plate", () -> new ItemCraftTool(128));
    public static final RegistryObject<Item> MOLD_WIRE = registerManager.ITEMS.register("mold_wire", () -> new ItemCraftTool(128));
    public static final RegistryObject<Item> MOLD_EXTRACTION = registerManager.ITEMS.register("mold_extraction", () -> new ItemCraftTool(128));

    public static final RegistryObject<Item> ENERGY_DRINK_DEBUG = registerManager.ITEMS.register("energy_drink", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC)){
        @Override
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            if(worldIn != null && worldIn.isClientSide) {
                if (Screen.hasShiftDown()) {
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip1").setStyle(Style.EMPTY.withBold(true).withColor(Color.fromRgb(0xff00fc)).withItalic(true)));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip2"));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip3"));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip4").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x990000)).withItalic(true)));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip5").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip6").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip7").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x999900)).withItalic(true)));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip8").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip9").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.tooltip10").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x5e5e5e)).withItalic(true)));
                } else {
                    ITextComponent shift = new StringTextComponent("[SHIFT]").withStyle(TextFormatting.YELLOW);
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.desc1").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x5e5e5e))));
                    tooltip.add(new TranslationTextComponent("item.projectazure.energy_drink.shiftinfo", shift).withStyle(TextFormatting.GRAY));
                }
            }
        }
    });


    //crafting items
    public static final RegistryObject<Item> MORTAR_IRON = registerManager.ITEMS.register("mortar_iron", () -> new ItemCraftTool(50));

    public static final RegistryObject<Item> HAMMER_IRON = registerManager.ITEMS.register("hammer_iron", () -> new ItemCraftTool(43));

    public static final RegistryObject<Item> STEEL_CUTTER = registerManager.ITEMS.register("steel_cutter", () -> new ItemCraftTool(70));


    public static final RegistryObject<Item> MAGAZINE_5_56 = registerManager.ITEMS.register("5.56_magazine", () -> new ItemMagazine(enums.AmmoCalibur.AMMO_5_56, 30, new Item.Properties()
            .tab(PA_GROUP)
            .stacksTo(1)
            .durability(7)));

    public static final RegistryObject<Item> Rainbow_Wisdom_Cube = registerManager.ITEMS.register("rainbow_wisdomcube", () -> new itemRainbowWisdomCube(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> CRUDE_OIL_BUCKET = ITEMS.register("crude_oil_bucket", ()-> new BucketItem(()->registerFluids.CRUDE_OIL_SOURCE, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> GASOLINE_BUCKET = ITEMS.register("gasoline_bucket", ()-> new BucketItem(()->registerFluids.GASOLINE_SOURCE, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> DIESEL_BUCKET = ITEMS.register("diesel_bucket", ()-> new BucketItem(()->registerFluids.DIESEL_SOURCE, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> FUEL_OIL_BUCKET = ITEMS.register("fuel_oil_bucket", ()-> new BucketItem(()->registerFluids.FUEL_OIL_SOURCE, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));


    public static final RegistryObject<Item> WISDOM_CUBE = registerManager.ITEMS.register("wisdomcube", () -> new ItemBaseTooltip(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> OATHRING = registerManager.ITEMS.register("oath_ring", () -> new ItemBaseTooltip(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC)
            .stacksTo(1)){
        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    });

    public static final RegistryObject<Item> PISTOL_GRIP = registerManager.ITEMS.register("pistol_grip", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> STEEL_RIFLE_FRAME = registerManager.ITEMS.register("steel_gunframe_rifle", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> AMMO_GENERIC = registerManager.ITEMS.register("ammo_generic", () -> new ItemCannonshell(enums.AmmoCategory.GENERIC ,new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> AMMO_5_56 = registerManager.ITEMS.register("5_56_ammo", () -> new ItemAmmo(enums.AmmoCalibur.AMMO_5_56, 6 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(5)));

    public static final RegistryObject<Item> AMMO_TORPEDO = registerManager.ITEMS.register("torpedo_ammo", () -> new ItemAmmo(enums.AmmoCalibur.TORPEDO, 1 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)));

    public static final RegistryObject<Item> AMMO_MISSILE = registerManager.ITEMS.register("missile_ammo", () -> new ItemAmmo(enums.AmmoCalibur.DRONE_MISSILE, 1 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)));

    //I'M READY TO GO TONIIIIGHT YEAH THERES PARTY ALRIGHTTTTTTT WE DON'T NEED REASON FOR JOY OH YEAHHHHH
    public static final RegistryObject<Item> DISC_FRIDAYNIGHT = registerManager.ITEMS.register("disc_fridaynight", () -> new MusicDiscItem(15, registerSounds.DISC_FRIDAY_NIGHT, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)){
        @Override
        public ITextComponent getName(ItemStack stack) {
            return new TranslationTextComponent("item.projectazure.music_disc");
        }
    });

    //LET THE BASS KICK O-oooooooooo AAAA E A A I A U
    public static final RegistryObject<Item> DISC_BRAINPOWER = registerManager.ITEMS.register("disc_brainpower", () -> new MusicDiscItem(15, registerSounds.DISC_BRAINPOWER, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Override
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            tooltip.add(new TranslationTextComponent("disc.brainpower.desc1").setStyle(Style.EMPTY.withColor(Color.fromRgb(7829367))));
        }

        @Override
        public ITextComponent getName(ItemStack stack) {
            return new TranslationTextComponent("item.projectazure.music_disc");
        }
    });

    //You know the rules and so do I
    public static final RegistryObject<Item> DISC_RICKROLL = registerManager.ITEMS.register("disc_rickroll", () -> new MusicDiscItem(15, registerSounds.DISC_RICKROLL, new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1))
    {
        @Override
        public ITextComponent getName(ItemStack stack) {
            return new TranslationTextComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_ENTERTHEBEGINNING = registerManager.ITEMS.register("disc_enterthebeginning", () -> new MusicDiscItem(15, registerSounds.DISC_ENTERTHEBEGINNING, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Override
        public ITextComponent getName(ItemStack stack) {
            return new TranslationTextComponent("item.projectazure.music_disc");
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            tooltip.add(new TranslationTextComponent("disc.enterthebeginning.desc1").setStyle(Style.EMPTY.withColor(Color.fromRgb(7829367))));
        }
    });

    public static final RegistryObject<Item> ABYDOS_550 = registerManager.ITEMS.register("abydos550", () -> new ItemAbydos550(false, 2, 30, 72, 3, registerSounds.RIFLE_FIRE_SUPPRESSED, SoundEvents.LEVER_CLICK, 0, 0.5F, new Item.Properties()
            .setISTER(() -> ItemAbydos550Renderer::new)
            .tab(PA_WEAPONS).stacksTo(1), true, MAGAZINE_5_56.get()));

    public static final RegistryObject<Item> CHIXIAO = registerManager.ITEMS.register("chixiao", () -> new ModSwordItem(ModMaterials.CHIXIAO, 1, -1.7F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> SHEATH = registerManager.ITEMS.register("sheath", () -> new ModSwordItem(ModMaterials.SHEATH, 1, -1.5F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> COMPOUNDBOW = registerManager.ITEMS.register("compoundbow", () -> new BowItem(new Item.Properties().tab(PA_WEAPONS).durability(1000)));
    public static final RegistryObject<Item> BONKBAT = registerManager.ITEMS.register("bonk_bat", () -> new ItemBonkBat(new Item.Properties()
            .tab(PA_WEAPONS)
            .rarity(Rarity.EPIC)
            .stacksTo(1)));

    public static final RegistryObject<Item> SLEDGEHAMMER = registerManager.ITEMS.register("sledgehammer", () -> new ItemSledgeHammer(10, -3.7F, ModMaterials.SLEDGEHAMMER, new Item.Properties().tab(PA_WEAPONS).stacksTo(1)));
    public static final RegistryObject<Item> CLAYMORE = registerManager.ITEMS.register("claymore", ItemClaymore::new);


    public static final RegistryObject<Item> COMMANDING_STICK = registerManager.ITEMS.register("commanding_stick", ItemCommandStick::new);

    public static final RegistryObject<Item> DD_DEFAULT_RIGGING = registerManager.ITEMS.register("dd_default_rigging", () -> new itemRiggingDDDefault(new Item.Properties()
    .setISTER(() -> DDDefaultRiggingRenderer::new)
    .tab(PA_WEAPONS).stacksTo(1), 500));

    public static final RegistryObject<Item> CV_DEFAULT_RIGGING = registerManager.ITEMS.register("cv_default_rigging", () -> new ItemRiggingCVDefault(new Item.Properties()
            .setISTER(() -> CVDefaultRiggingRenderer::new)
            .tab(PA_WEAPONS).stacksTo(1), 750));

    public static final RegistryObject<Item> BB_DEFAULT_RIGGING = registerManager.ITEMS.register("bb_default_rigging", () -> new ItemRiggingBBDefault(new Item.Properties()
            .setISTER(() -> BBDefaultRiggingRenderer::new)
            .tab(PA_WEAPONS).stacksTo(1), 1200));

    public static final RegistryObject<Item> EQUIPMENT_TORPEDO_533MM = registerManager.ITEMS.register("equipment_torpedo_533mm", () -> new ItemEquipmentTorpedo533Mm(new Item.Properties()
            .setISTER(() -> equipment533mmTorpedoRenderer::new)
            .tab(PA_WEAPONS).stacksTo(1), 40));

    public static final RegistryObject<Item> EQUIPMENT_GUN_127MM = registerManager.ITEMS.register("equipment_gun_127mm", () -> new ItemEquipmentGun127Mm(new Item.Properties()
            .setISTER(() -> Equipment127mmGunRenderer::new)
            .tab(PA_WEAPONS).stacksTo(1), 40));

    public static float WildcatHP = 30;

    public static final RegistryObject<Item> EQUIPMENT_PLANE_F4FWildcat = registerManager.ITEMS.register("equipment_plane_f4fwildcat", () -> new ItemPlanef4Fwildcat(new Item.Properties()
            .setISTER(() -> ItemPlanef4fWildcatRenderer::new)
            .tab(PA_WEAPONS).stacksTo(1), (int) WildcatHP));


    public static final RegistryObject<Item> DRONE_BAMISSILE = registerManager.ITEMS.register("missiledrone", () -> new ItemMissleDrone(new Item.Properties()
            .setISTER(() -> ItemMissileDroneRenderer::new)
            .tab(PA_WEAPONS).stacksTo(1),10, 20000));

    public static final RegistryObject<Item> SPAWM_AYANAMI = registerManager.ITEMS.register("spawnayanami", () -> new ItemKansenSpawnEgg(ENTITYTYPE_AYANAMI, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWM_JAVELIN = registerManager.ITEMS.register("spawnjavelin", () -> new ItemKansenSpawnEgg(ENTITYTYPE_JAVELIN, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_Z23 = registerManager.ITEMS.register("spawnz23", () -> new ItemKansenSpawnEgg(ENTITYTYPE_Z23, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_GANGWON = registerManager.ITEMS.register("spawngangwon", () -> new ItemKansenSpawnEgg(ENTITYTYPE_GANGWON, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_LAFFEY = registerManager.ITEMS.register("spawnlaffey", () -> new ItemKansenSpawnEgg(ENTITYTYPE_LAFFEY, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWM_ENTERPRISE = registerManager.ITEMS.register("spawnenterprise", ()-> new ItemKansenSpawnEgg(ENTITYTYPE_ENTERPRISE, new Item.Properties().tab(PA_SHIPS)));


    public static final RegistryObject<Item> SPAWN_NAGATO = registerManager.ITEMS.register("spawnnagato", () -> new ItemKansenSpawnEgg(ENTITYTYPE_NAGATO, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_CHEN = registerManager.ITEMS.register("spawnchen", () -> new ItemKansenSpawnEgg(ENTITYTYPE_CHEN, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_MUDROCK = registerManager.ITEMS.register("spawnmudrock", () -> new ItemKansenSpawnEgg(ENTITYTYPE_MUDROCK, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_M4A1 = registerManager.ITEMS.register("spawnm4a1", () -> new ItemKansenSpawnEgg(ENTITYTYPE_M4A1, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_AMIYA = registerManager.ITEMS.register("spawnamiya", () -> new ItemKansenSpawnEgg(ENTITYTYPE_AMIYA, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_ROSMONTIS = registerManager.ITEMS.register("spawnrosmontis", () -> new ItemKansenSpawnEgg(ENTITYTYPE_ROSMONTIS, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_TALULAH = registerManager.ITEMS.register("spawntalulah", () -> new ItemKansenSpawnEgg(ENTITYTYPE_TALULAH, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_SHIROKO = registerManager.ITEMS.register("spawnshiroko", () -> new ItemKansenSpawnEgg(ENTITYTYPE_SHIROKO, new Item.Properties()
            .tab(PA_SHIPS)));

    public static final RegistryObject<Item> BANDAGE_ROLL = registerManager.ITEMS.register("bandage_roll", () -> new ItemBandage(new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> DEVELOPER_BONUS = registerManager.ITEMS.register("developer_bonus", () -> new ItemContributorBonus(new Item.Properties()));
    public static final RegistryObject<Item> CONTRIBUTOR_BONUS = registerManager.ITEMS.register("contributor_bonus", () -> new ItemContributorBonus(new Item.Properties()));




    public static void register(){};

}
