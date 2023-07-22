package com.yor42.projectazure.setup.register;

import com.mojang.datafixers.util.Pair;
import com.tac.guns.common.Gun;
import com.tac.guns.item.TransitionalTypes.TimelessAmmoItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.yor42.projectazure.client.renderer.equipment.Equipment127mmGunRenderer;
import com.yor42.projectazure.client.renderer.equipment.equipment533mmTorpedoRenderer;
import com.yor42.projectazure.client.renderer.items.*;
import com.yor42.projectazure.gameobject.items.*;
import com.yor42.projectazure.gameobject.items.materials.ModMaterials;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBBDefault;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingCVDefault;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentGun127Mm;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentTorpedo533Mm;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemPlanef4Fwildcat;
import com.yor42.projectazure.gameobject.items.tools.*;
import com.yor42.projectazure.gameobject.misc.ModFoods;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.mixin.ModuleAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.yor42.projectazure.Main.*;
import static com.yor42.projectazure.data.client.itemModelProvider.ITEMENTRY;
import static com.yor42.projectazure.data.client.itemModelProvider.TOOLENTRY;

@SuppressWarnings("unused")
public class RegisterItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);

    //Resources
    public static final RegistryObject<Item> INGOT_LEAD = registerResource("lead", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_TIN = registerResource("tin", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_ALUMINIUM = registerResource("aluminium", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_BRONZE = registerResource("bronze", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_ZINC = registerResource("zinc", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_STEEL = registerResource("steel", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_BRASS = registerResource("brass", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_RMA7024 = registerResource("rma7024", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_RMA7012 = registerResource("rma7012", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_D32 = registerResource("d32steel", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_INCANDESCENT_ALLOY = registerResource("incandescent_alloy", enums.ResourceItemType.INGOT);
    public static final RegistryObject<Item> INGOT_MANGANESE = registerResource("manganese", enums.ResourceItemType.INGOT);

    public static final RegistryObject<Item> RAW_LEAD = registerResource("lead", enums.ResourceItemType.RAW_ORE);
    public static final RegistryObject<Item> RAW_TIN = registerResource("tin", enums.ResourceItemType.RAW_ORE);
    public static final RegistryObject<Item> RAW_ALUMINIUM = registerResource("aluminium", enums.ResourceItemType.RAW_ORE);
    public static final RegistryObject<Item> RAW_ZINC = registerResource("zinc", enums.ResourceItemType.RAW_ORE);
    public static final RegistryObject<Item> RAW_RMA7012 = registerResource("rma7012", enums.ResourceItemType.RAW_ORE);
    public static final RegistryObject<Item> RAW_MAGANESE = registerResource("manganese", enums.ResourceItemType.RAW_ORE);

    public static final RegistryObject<Item> GEAR_BRONZE = registerResource("bronze", enums.ResourceItemType.GEAR);
    public static final RegistryObject<Item> GEAR_STEEL = registerResource("steel", enums.ResourceItemType.GEAR);
    public static final RegistryObject<Item> GEAR_IRON = registerResource("iron", enums.ResourceItemType.GEAR);
    public static final RegistryObject<Item> DUST_COPPER = registerResource("copper", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_LEAD = registerResource("lead", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_TIN = registerResource("tin", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_ALUMINIUM = registerResource("aluminium", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_BRONZE = registerResource("bronze", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_ZINC = registerResource("zinc", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_IRON = registerResource("iron", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_COAL = registerResource("coal", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_STEEL = registerResource("steel", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_BRASS = registerResource("brass", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_GOLD = registerResource("gold", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_ORIROCK = registerResource("orirock", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_ORIGINIUM = registerResource("originium", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_NETHER_QUARTZ = registerResource("quartz", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_RMA7024 = registerResource("rma7024", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_RMA7012 = registerResource("rma7012",  enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_D32 = registerResource("d32steel", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_INCANDESCENT_ALLOY = registerResource("incandescent_alloy", enums.ResourceItemType.DUST);
    public static final RegistryObject<Item> DUST_MANGANESE = registerResource("manganese", enums.ResourceItemType.DUST);

    public static final RegistryObject<Item> PLATE_COPPER = registerResource("copper", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_LEAD = registerResource("lead", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_TIN = registerResource("tin", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_ALUMINIUM = registerResource("aluminium", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_BRONZE = registerResource("bronze", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_ZINC = registerResource("zinc", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_IRON = registerResource("iron", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_GOLD = registerResource("gold", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_STEEL = registerResource("steel", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_BRASS = registerResource("brass", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_RMA7024 = registerResource("rma7024", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_RMA7012 = registerResource("rma7012", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_D32 = registerResource("d32steel", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_INCANDESCENT_ALLOY = registerResource("incandescent_alloy", enums.ResourceItemType.PLATE);
    public static final RegistryObject<Item> PLATE_MANGANESE = registerResource("manganese", enums.ResourceItemType.PLATE);

    public static final RegistryObject<Item> NUGGET_COPPER = registerResource("copper", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_LEAD = registerResource("lead", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_TIN = registerResource("tin", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_BRONZE = registerResource("bronze", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_ZINC = registerResource("zinc", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_STEEL = registerResource("steel", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_BRASS = registerResource("brass", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_RMA7024 = registerResource("rma7024", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_RMA7012 = registerResource("rma7012", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_D32 = registerResource("d32steel", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_INCANDESCENT_ALLOY = registerResource("incandescent_alloy", enums.ResourceItemType.NUGGET);
    public static final RegistryObject<Item> NUGGET_MANGANESE = registerResource("manganese", enums.ResourceItemType.NUGGET);

    //Electronic Stuff
    public static final RegistryObject<Item> COPPER_WIRE = register("copper_wire", () -> new ItemResource("copper", enums.ResourceItemType.WIRE));
    public static final RegistryObject<Item> GOLD_WIRE = register("gold_wire", () -> new ItemResource("gold", enums.ResourceItemType.WIRE));
    public static final RegistryObject<Item> IRON_PIPE = register("iron_pipe", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> STEEL_PIPE = register("steel_pipe", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> MECHANICAL_PARTS = register("mechanical_parts", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> PRIMITIVE_MOTOR = register("motor_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> BASIC_MOTOR = register("motor_basic", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ADVANCED_MOTOR = register("motor_advanced", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> PRIMITIVE_DEVICE = register("device_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> BASIC_DEVICE = register("device_basic", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ADVANCED_DEVICE = register("device_advanced", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> PRIMITIVE_CIRCUIT = register("circuit_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ADVANCED_CIRCUIT = register("circuit_advanced", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> PYROXENE = register("pyroxene", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));


    public static final RegistryObject<Item> PRIMITIVE_ORIGINIUM_BATTERY = register("primitive_originium_battery", () -> new ItemBattery(new Item.Properties()
            .tab(PA_RESOURCES).stacksTo(8), 2200, 200, 200));

    public static final RegistryObject<Item> POTATO_BATTERY = register("potato_battery", () -> new ItemBattery(new Item.Properties()
            .tab(PA_RESOURCES).stacksTo(4), 1000, 0, 80));

    public static final RegistryObject<Item> RECHARGEABLE_BATTERY = register_withoutTexture("rechargeablebattery", () -> new ItemBattery(new Item.Properties()
            .tab(PA_RESOURCES).stacksTo(16)
            , 25000, 800, 800));


    public static final RegistryObject<Item> COPPER_IRON_PROBE = register("copper_iron_probe", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> GUNPOWDER_COMPOUND = register("gunpowder_compound", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> MONOCRYSTALLINE_SILICONE = register("monocrystalline_silicone", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> SILICONE_WAFER = register("silicone_wafer", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> SILICONE = register("silicone", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> COPPER_COIL = register("copper_coil", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> GOLD_COIL = register("gold_coil", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> CAPACITOR_PRIMITIVE = register("capacitor_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> CAPACITOR_ADVANCED = register("capacitor_advanced", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> RESISTOR_PRIMITIVE = register("resistor_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> RESISTOR_BASIC = register("resistor_basic", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> RESISTOR_ADVANCED = register("resistor_advanced", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    //Natural Resource
    public static final RegistryObject<Item> TREE_SAP = register("tree_sap", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> BITUMEN = register("bitumen", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> PLATE_POLYMER = register("polymer_plate", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));

    //Originium Engineering
    public static final RegistryObject<Item> ORIGINIUM_SEED = register("originium_seed", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> NETHER_QUARTZ_SEED = register("quartz_seed", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ORIGINITE = register("originite", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ORIGINIUM_PRIME = register("originium_prime", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES).food(ModFoods.ORIGINIUM_PRIME)));

    public static final RegistryObject<Item> ORIGINIUM_SHARD = register("originium_shard", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)){
        @Override
        public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
            return 100;
        }
    });

    public static final RegistryObject<Item> HEADHUNTING_PCB = register("headhunting_pcb", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES).stacksTo(16)));
    public static final RegistryObject<Item> ORUNDUM = register("orundum", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> SAINT_QUARTZ = register("saint_quartz", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> COMPUTERCORE = register("computercore", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> CRYSTALLINE_CIRCUIT = register("crystalline_circuit", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> CRYSTALLINE_COMPONENT = register("crystalline_component", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> VITRIFIED_ORIGINIUM = register("vitrifiedoriginium", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> AMBER_ORIGINIUM = register("amberoriginium", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> LOXICKOHL = register("loxickohl", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> C99_CARBON = register("c99", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)){
        @Override
        public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
            return 2400;
        }
    });
    public static final RegistryObject<Item> AMBER_ORIGINIUM_FUEL_ROD = register("amber_originium_fuel_rod", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)){
        @Override
        public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
            return 25000;
        }
    });

    public static final RegistryObject<Item> TOKEN = register("token", () -> new ItemCompanionUpgrade(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> COGNITIVE_CHIP = register("cognitive_chip", () -> new ItemCompanionUpgrade(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> COGNITIVE_ARRAY = register("cognitive_array", () -> new ItemCompanionUpgrade(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> HOLY_GRAIL = register("holygrail", () -> new ItemCompanionUpgrade(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> PROMOTION_KIT = register("promotion_kit", () -> new ItemCompanionUpgrade(new Item.Properties()
            .tab(PA_RESOURCES)));

    public static final RegistryObject<Item> PIG_FAT = register("pork_fat", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES).food(new FoodProperties.Builder().meat().nutrition(1).saturationMod(0.8F).fast().build())));

    public static final RegistryObject<Item> MOLD_PLATE = register("mold_plate", () -> new ItemCraftTool(128));
    public static final RegistryObject<Item> MOLD_WIRE = register("mold_wire", () -> new ItemCraftTool(128));
    public static final RegistryObject<Item> MOLD_EXTRACTION = register("mold_extraction", () -> new ItemCraftTool(128));

    public static final RegistryObject<Item> ENERGY_DRINK_DEBUG = register("energy_drink", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC)){
        @Override
        public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
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

    public static final RegistryObject<Item> CUTTER_STONE = register("stone_cutter", () -> new ItemCraftTool(25));
    public static final RegistryObject<Item> CUTTER_IRON = register("iron_cutter", () -> new ItemCraftTool(92));
    public static final RegistryObject<Item> CUTTER_STEEL = register("steel_cutter", () -> new ItemCraftTool(105));
    public static final RegistryObject<Item> CUTTER_COPPER = register("copper_cutter", () -> new ItemCraftTool(50));
    public static final RegistryObject<Item> CUTTER_TIN = register("tin_cutter", () -> new ItemCraftTool(61));
    public static final RegistryObject<Item> CUTTER_BRONZE = register("bronze_cutter", () -> new ItemCraftTool(82));
    public static final RegistryObject<Item> CUTTER_GOLD = register("gold_cutter", () -> new ItemCraftTool(35));
    public static final RegistryObject<Item> CUTTER_DIAMOND = register("diamond_cutter", () -> new ItemCraftTool(155));
    public static final RegistryObject<Item> CUTTER_NETHERITE = register("netherite_cutter", () -> new ItemCraftTool(281));
    public static final RegistryObject<Item> CUTTER_RMA7012 = register("rma7012_cutter", () -> new ItemCraftTool(101));
    public static final RegistryObject<Item> CUTTER_RMA7024 = register("rma7024_cutter", () -> new ItemCraftTool(137));
    public static final RegistryObject<Item> CUTTER_D32 = register("d32_cutter", () -> new ItemCraftTool(451));

    public static final RegistryObject<Item> MORTAR_STONE = register("stone_mortar", () -> new ItemCraftTool(21));
    public static final RegistryObject<Item> MORTAR_IRON = register("iron_mortar", () -> new ItemCraftTool(80));
    public static final RegistryObject<Item> MORTAR_STEEL = register("steel_mortar", () -> new ItemCraftTool(84));
    public static final RegistryObject<Item> MORTAR_COPPER = register("copper_mortar", () -> new ItemCraftTool(38));
    public static final RegistryObject<Item> MORTAR_TIN = register("tin_mortar", () -> new ItemCraftTool(45));
    public static final RegistryObject<Item> MORTAR_BRONZE = register("bronze_mortar", () -> new ItemCraftTool(67));
    public static final RegistryObject<Item> MORTAR_GOLD = register("gold_mortar", () -> new ItemCraftTool(32));
    public static final RegistryObject<Item> MORTAR_DIAMOND = register("diamond_mortar", () -> new ItemCraftTool(150));
    public static final RegistryObject<Item> MORTAR_NETHERITE = register("netherite_mortar", () -> new ItemCraftTool(261));
    public static final RegistryObject<Item> MORTAR_RMA7012 = register("rma7012_mortar", () -> new ItemCraftTool(109));
    public static final RegistryObject<Item> MORTAR_RMA7024 = register("rma7024_mortar", () -> new ItemCraftTool(138));
    public static final RegistryObject<Item> MORTAR_D32 = register("d32_mortar", () -> new ItemCraftTool(457));

    public static final RegistryObject<Item> HAMMER_STONE = register("stone_hammer", () -> new ItemCraftTool(24));
    public static final RegistryObject<Item> HAMMER_COPPER = register("copper_hammer", () -> new ItemCraftTool(39));
    public static final RegistryObject<Item> HAMMER_TIN = register("tin_hammer", () -> new ItemCraftTool(39));
    public static final RegistryObject<Item> HAMMER_BRONZE = register("bronze_hammer", () -> new ItemCraftTool(52));
    public static final RegistryObject<Item> HAMMER_IRON = register("iron_hammer", () -> new ItemCraftTool(67));
    public static final RegistryObject<Item> HAMMER_GOLD = register("gold_hammer", () -> new ItemCraftTool(40));
    public static final RegistryObject<Item> HAMMER_STEEL = register("steel_hammer", () -> new ItemCraftTool(90));
    public static final RegistryObject<Item> HAMMER_DIAMOND = register("diamond_hammer", () -> new ItemCraftTool(112));
    public static final RegistryObject<Item> HAMMER_NETHERITE = register("netherite_hammer", () -> new ItemCraftTool(286));
    public static final RegistryObject<Item> HAMMER_RMA7012 = register("rma7012_hammer", () -> new ItemCraftTool(84));
    public static final RegistryObject<Item> HAMMER_RMA7024 = register("rma7024_hammer", () -> new ItemCraftTool(132));
    public static final RegistryObject<Item> HAMMER_D32 = register("d32_hammer", () -> new ItemCraftTool(315));

    public static final RegistryObject<Item> SAW_STONE = register("stone_saw", () -> new ItemCraftTool(12));
    public static final RegistryObject<Item> SAW_COPPER = register("copper_saw", () -> new ItemCraftTool(34));
    public static final RegistryObject<Item> SAW_TIN = register("tin_saw", () -> new ItemCraftTool(41));
    public static final RegistryObject<Item> SAW_BRONZE = register("bronze_saw", () -> new ItemCraftTool(67));
    public static final RegistryObject<Item> SAW_IRON = register("iron_saw", () -> new ItemCraftTool(130));
    public static final RegistryObject<Item> SAW_RMA7012 = register("rma7012_saw", () -> new ItemCraftTool(273));
    public static final RegistryObject<Item> SAW_RMA7024 = register("rma7024_saw", () -> new ItemCraftTool(626));
    public static final RegistryObject<Item> SAW_D32 = register("d32_saw", () -> new ItemCraftTool(986));
    public static final RegistryObject<Item> SAW_GOLD = register("gold_saw", () -> new ItemCraftTool(62));
    public static final RegistryObject<Item> SAW_STEEL = register("steel_saw", () -> new ItemCraftTool(358));
    public static final RegistryObject<Item> SAW_DIAMOND = register("diamond_saw", () -> new ItemCraftTool(598));
    public static final RegistryObject<Item> SAW_NETHERITE = register("netherite_saw", () -> new ItemCraftTool(792));


    public static final RegistryObject<Item> FOR_DESTABILIZER = register("fordestabilizer", () -> new ItemCraftTool(3));
    public static final RegistryObject<Item> GLITCHED_PHONE = register("glitched_phone", () -> new ItemGlitchedPhone(new Item.Properties().stacksTo(1).tab(PA_GROUP)));

    public static final RegistryObject<Item> Rainbow_Wisdom_Cube = register("rainbow_wisdomcube", () -> new itemRainbowWisdomCube(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC).stacksTo(1)));

    //THIS, IS A BUCKET
    //No.....
    //AND THERE'S MORE.
    //DEAR GOD....
    public static final RegistryObject<Item> CRUDE_OIL_BUCKET = register("crude_oil_bucket", ()-> new BucketItem(RegisterFluids.CRUDE_OIL_SOURCE_REGISTRY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> GASOLINE_BUCKET = register("gasoline_bucket", ()-> new BucketItem(RegisterFluids.GASOLINE_SOURCE_REGISTRY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> DIESEL_BUCKET = register("diesel_bucket", ()-> new BucketItem(RegisterFluids.DIESEL_SOURCE_REGISTRY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> KETON_BUCKET = register("keton_bucket", ()-> new BucketItem(RegisterFluids.KETON_SOURCE_REGISTRY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> FUEL_OIL_BUCKET = register("fuel_oil_bucket", ()-> new BucketItem(RegisterFluids.FUEL_OIL_SOURCE_REGISTRY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));


    public static final RegistryObject<Item> WISDOM_CUBE = register("wisdomcube", () -> new ItemBaseTooltip(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> OATHRING = register("oath_ring", () -> new ItemBaseTooltip(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC)
            .stacksTo(1)){
        @Override
        public boolean isFoil(@Nonnull ItemStack stack) {
            return true;
        }
    });

    public static final RegistryObject<Item> AMMO_GENERIC = register("ammo_generic", () -> new ItemCannonshell(enums.AmmoCategory.GENERIC ,new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> AMMO_HE = register("ammo_he", () -> new ItemCannonshell(enums.AmmoCategory.HE ,new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> AMMO_INCENDIARY = register("ammo_incendiary", () -> new ItemCannonshell(enums.AmmoCategory.INCENDIARY ,new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> AMMO_AP = register("ammo_ap", () -> new ItemCannonshell(enums.AmmoCategory.AP ,new Item.Properties()
            .tab(PA_GROUP)));

    public static final RegistryObject<Item> AMMO_SAP = register("ammo_sap", () -> new ItemCannonshell(enums.AmmoCategory.SAP ,new Item.Properties()
            .tab(PA_GROUP).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> AMMO_API = register("ammo_api", () -> new ItemCannonshell(enums.AmmoCategory.API ,new Item.Properties()
            .tab(PA_GROUP).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> AMMO_HEIAP = register("ammo_heiap", () -> new ItemCannonshell(enums.AmmoCategory.HEIAP ,new Item.Properties()
            .tab(PA_GROUP).rarity(Rarity.RARE)));


    public static final RegistryObject<Item> AMMO_TORPEDO = register("torpedo_ammo", () -> new ItemAmmo(enums.AmmoCalibur.TORPEDO, 1 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)));

    public static final RegistryObject<Item> AMMO_MISSILE = register("missile_ammo", () -> new ItemAmmo(enums.AmmoCalibur.DRONE_MISSILE, 1 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)));

    //I'M READY TO GO TONIIIIGHT YEAH THERES PARTY ALRIGHTTTTTTT WE DON'T NEED REASON FOR JOY OH YEAHHHHH
    public static final RegistryObject<Item> DISC_FRIDAYNIGHT = register("disc_fridaynight", () -> new RecordItem(15, ()->registerSounds.DISC_FRIDAY_NIGHT, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)){
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_CC5 = register("disc_cc5", () -> new RecordItem(15, ()->registerSounds.DISC_CC5, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });
    public static final RegistryObject<Item> DISC_REVENGE = register("disc_revenge", () -> new RecordItem(15, ()->registerSounds.DISC_REVENGE, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_FALLEN_KINGDOM = register("disc_fallen_kingdom", () -> new RecordItem(15, ()->registerSounds.DISC_FALLEN_KINGDOM, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_FIND_THE_PIECES = register("disc_findthepieces", () -> new RecordItem(15, ()->registerSounds.DISC_FIND_THE_PIECES, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_TAKE_BACK_THE_NIGHT = register("disc_takebackthenight", () -> new RecordItem(15, ()->registerSounds.DISC_TAKE_BACK_THE_NIGHT, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DISC_DRAGONHEARTED = register("disc_dragonhearted", () -> new RecordItem(15, ()->registerSounds.DISC_DRAGONHEARTED, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });

    //Shooty stuff
    public static final RegistryObject<Item> WHITEFANG_465 = register_withoutTexture("whitefang465", ()->new TimelessGunItem((properties) -> properties.tab(PA_WEAPONS)));
    public static final RegistryObject<Item> TYPHOON = register_withoutTexture("typhoon", ()->new TimelessGunItem((properties) -> properties.tab(PA_WEAPONS)));
    public static final RegistryObject<Item> SUPERNOVA = register_withoutTexture("supernova", ()->new ItemSupernova(50000, 2500, 1, false, null, null, null ,(properties)->properties.tab(PA_WEAPONS)));
    public static final RegistryObject<Item> W_GRANADELAUNCHER = register_withoutTexture("granadelauncher", ()->new TimelessGunItem((properties) -> properties.tab(PA_WEAPONS)));
    public static final RegistryObject<Item> SANGVIS_RAILGUN = register_withoutTexture("sangvis_railgun", ()->new ItemEnergyGun(55000, 10000, 100, false, registerSounds.SANGVIS_CANNON_OPEN, registerSounds.SANGVIS_CANNON_CLOSE, registerSounds.SANGVIS_CANNON_NOAMMO, (properties) -> properties.tab(PA_WEAPONS)){
        @Override
        public Gun getGun() {
            Gun gun = super.getGun();
            ((ModuleAccessor)gun.getModules()).setZoom(null);
            return gun;
        }
    });

    public static final RegistryObject<Item> CASELESS_4MM = register_withoutTexture("4mmcaseless", ()->new TimelessAmmoItem((properties) -> properties.tab(PA_WEAPONS)));

    public static final RegistryObject<Item> RAILGUN_AMMO = register("ammo_sangvisrailgun", ()->new TimelessAmmoItem((properties) -> properties.tab(PA_WEAPONS)));
    public static final RegistryObject<Item> SUPERNOVA_AMMO = register("ammo_supernova", ()->new TimelessAmmoItem((properties) -> properties.tab(PA_WEAPONS)));
    //not-so-shooty-stuff
    public static final RegistryObject<Item> CHIXIAO = register_withoutTexture("chixiao", () -> new ModSwordItem(ModMaterials.CHIXIAO, 1, -1.7F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> SHEATH = register_withoutTexture("sheath", () -> new ModSwordItem(ModMaterials.SHEATH, 1, -1.5F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> CRESCENTKATANA_SHIRO = register_withoutTexture("crescentkatana_shiro", () -> new ModSwordItem(ModMaterials.CRESCENT_KATANA_SHIRO, 1, -1.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> CRESCENTKATANA_KURO = register_withoutTexture("crescentkatana_kuro", () -> new ModSwordItem(ModMaterials.CRESCENT_KATANA_KURO, 1, -1.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> WARHAMMER = register_withoutTexture("warhammer", () -> new ModSwordItem(ModMaterials.WARHAMMER, 1, -1.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> FLEXABLE_SWORD_THINGY = register_withoutTexture("flexsword", () -> new ModSwordItem(ModMaterials.FLEXABLESWORD, 1, -1.0F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> EXCALIBUR_SHEATH = register_withoutTexture("excalibur_sheath", ItemExcaliburSheath::new);
    public static final RegistryObject<Item> EXCALIBUR = register_withoutTexture("excalibur", () -> new ModSwordItem(ModMaterials.EXCALIBUR, 1, -1.0F,true, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> TACTICAL_KNIFE = register_withoutTexture("tactical_knife", () -> new ItemThrowableKnife(ModMaterials.KNIFE, 3, -0.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> COMPOUNDBOW = register_withoutTexture("compoundbow", () -> new BowItem(new Item.Properties().tab(PA_WEAPONS).durability(1000)));
    public static final RegistryObject<Item> SOULUMSWORD = register_withoutTexture("soulumsword", () -> new ModSwordItem(ModMaterials.SOULUMSWORD, 1, -1.7F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> LORD_CHALDEAS = register_withoutTexture("lordchaldeas", () -> new ShieldItem(new Item.Properties().tab(PA_WEAPONS).stacksTo(1)));
    public static final RegistryObject<Item> BONKBAT = register("bonk_bat", () -> new ItemBonkBat(new Item.Properties()
            .tab(PA_WEAPONS)
            .rarity(Rarity.EPIC)
            .stacksTo(1)));
    public static final RegistryObject<Item> GRAVINET = register_withoutTexture("gravinet", () -> new ModSwordItem(ModMaterials.GRAVINET, 1, -1.6F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> KYARU_STAFF = register_withoutTexture("kyarustaff", () -> new Item(new Item.Properties()
            .tab(PA_WEAPONS)
            .rarity(Rarity.UNCOMMON)
            .stacksTo(1)));
    public static final RegistryObject<Item> KITCHEN_KNIFE = register_withoutTexture("kitchenknife", () -> new ModSwordItem(ModMaterials.KITCHEN_KNIFE, 1, -0.35F, new Item.Properties().tab(PA_WEAPONS)){
        @Override
        public boolean hasContainerItem(ItemStack stack) {
            return true;
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemStack) {
            ItemStack stack = itemStack.copy();
            if (!stack.hurt(1, MathUtil.getRand(), null)) {
                return stack;
            }
            else return ItemStack.EMPTY;
        }
    });

    public static final RegistryObject<Item> GASMASK_FILTER = register_withoutTexture("gasmask_filter", ()->new GasMaskFilterItem(new Item.Properties().tab(PA_RESOURCES), 24000));
    public static final RegistryObject<Item> CHARCOAL_FILTER = register("replacement_filter", ()->new Item(new Item.Properties().tab(PA_RESOURCES)));
    public static final RegistryObject<Item> GASMASK = register_withoutTexture("gasmask", ()->new GasMaskItem(new Item.Properties().tab(PA_RESOURCES).stacksTo(1)){
        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer) {
            super.initializeClient(consumer);
            consumer.accept(new IItemRenderProperties() {
                private final BlockEntityWithoutLevelRenderer renderer = new GasMaskItemRenderer();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            });
        }
    });
    public static final RegistryObject<Item> SYRINGE = register_withoutTexture("syringe", ()->new ItemSyringe(new Item.Properties().tab(PA_RESOURCES)));
    public static final RegistryObject<Item> RMA7024_pickaxe = registertool("rma7024_pickaxe", () -> new PickaxeItem(ModMaterials.RMA_70_24, 1, -2.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> RMA7024_axe = registertool("rma7024_axe", () -> new AxeItem(ModMaterials.RMA_70_24, 6, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> RMA7024_hoe = registertool("rma7024_hoe", () -> new HoeItem(ModMaterials.RMA_70_24, 0, -1F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> RMA7024_shovel = registertool("rma7024_shovel", () -> new ShovelItem(ModMaterials.RMA_70_24, 1.5F, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> RMA7024_sword = registertool("rma7024_sword", () -> new SwordItem(ModMaterials.RMA_70_24, 3, -2.4F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> D32_pickaxe = registertool("d32_pickaxe", () -> new PickaxeItem(ModMaterials.D32, 1, -2.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> D32_axe = registertool("d32_axe", () -> new AxeItem(ModMaterials.D32, 4, -2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> D32_hoe = registertool("d32_hoe", () -> new HoeItem(ModMaterials.D32, 0, -1F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> D32_shovel = registertool("d32_shovel", () -> new ShovelItem(ModMaterials.D32, 1.5F, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> D32_sword = registertool("d32_sword", () -> new SwordItem(ModMaterials.D32, 3, -2.4F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> RMA7012_pickaxe = registertool("rma7012_pickaxe", () -> new PickaxeItem(ModMaterials.RMA_70_12, 1, -2.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> RMA7012_axe = registertool("rma7012_axe", () -> new AxeItem(ModMaterials.RMA_70_12, 6, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> RMA7012_hoe = registertool("rma7012_hoe", () -> new HoeItem(ModMaterials.RMA_70_12, 0, -1F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> RMA7012_shovel = registertool("rma7012_shovel", () -> new ShovelItem(ModMaterials.RMA_70_12, 1.5F, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> RMA7012_sword = registertool("rma7012_sword", () -> new SwordItem(ModMaterials.RMA_70_12, 3, -2.4F, new Item.Properties().tab(PA_WEAPONS)));

    public static final RegistryObject<Item> LEAD_pickaxe = registertool("lead_pickaxe", () -> new PickaxeItem(ModMaterials.LEAD, 2, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> LEAD_axe = registertool("lead_axe", () -> new AxeItem(ModMaterials.LEAD, 7, -3.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> LEAD_hoe = registertool("lead_hoe", () -> new HoeItem(ModMaterials.LEAD, 1, -1.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> LEAD_shovel = registertool("lead_shovel", () -> new ShovelItem(ModMaterials.LEAD, 2.5F, -3.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> LEAD_sword = registertool("lead_sword", () -> new SwordItem(ModMaterials.LEAD, 4, -2.6F, new Item.Properties().tab(PA_WEAPONS)));


    public static final RegistryObject<Item> COPPER_pickaxe = registertool("copper_pickaxe", () -> new PickaxeItem(ModMaterials.COPPER, 1, -2.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> COPPER_axe = registertool("copper_axe", () -> new AxeItem(ModMaterials.COPPER, 6, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> COPPER_hoe = registertool("copper_hoe", () -> new HoeItem(ModMaterials.COPPER, 0, -1F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> COPPER_shovel = registertool("copper_shovel", () -> new ShovelItem(ModMaterials.COPPER, 1.5F, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> COPPER_sword = registertool("copper_sword", () -> new SwordItem(ModMaterials.COPPER, 3, -2.4F, new Item.Properties().tab(PA_WEAPONS)));

    public static final RegistryObject<Item> TIN_pickaxe = registertool("tin_pickaxe", () -> new PickaxeItem(ModMaterials.TIN, 1, -2.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> TIN_axe = registertool("tin_axe", () -> new AxeItem(ModMaterials.TIN, 6, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> TIN_hoe = registertool("tin_hoe", () -> new HoeItem(ModMaterials.TIN, 0, -1F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> TIN_shovel = registertool("tin_shovel", () -> new ShovelItem(ModMaterials.TIN, 1.5F, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> TIN_sword = registertool("tin_sword", () -> new SwordItem(ModMaterials.TIN, 3, -2.4F, new Item.Properties().tab(PA_WEAPONS)));

    public static final RegistryObject<Item> BRONZE_pickaxe = registertool("bronze_pickaxe", () -> new PickaxeItem(ModMaterials.BRONZE, 1, -2.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> BRONZE_axe = registertool("bronze_axe", () -> new AxeItem(ModMaterials.BRONZE, 6, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> BRONZE_hoe = registertool("bronze_hoe", () -> new HoeItem(ModMaterials.BRONZE, 0, -1F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> BRONZE_shovel = registertool("bronze_shovel", () -> new ShovelItem(ModMaterials.BRONZE, 1.5F, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> BRONZE_sword = registertool("bronze_sword", () -> new SwordItem(ModMaterials.BRONZE, 3, -2.4F, new Item.Properties().tab(PA_WEAPONS)));

    public static final RegistryObject<Item> STEEL_pickaxe = registertool("steel_pickaxe", () -> new PickaxeItem(ModMaterials.STEEL, 1, -2.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> STEEL_axe = registertool("steel_axe", () -> new AxeItem(ModMaterials.STEEL, 6, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> STEEL_hoe = registertool("steel_hoe", () -> new HoeItem(ModMaterials.STEEL, 0, -1F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> STEEL_shovel = registertool("steel_shovel", () -> new ShovelItem(ModMaterials.STEEL, 1.5F, -3F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> STEEL_sword = registertool("steel_sword", () -> new SwordItem(ModMaterials.STEEL, 3, -2.4F, new Item.Properties().tab(PA_WEAPONS)));


    public static final RegistryObject<Item> SLEDGEHAMMER = register_withoutTexture("sledgehammer", () -> new ItemSledgeHammer(10, -3.75F, ModMaterials.SLEDGEHAMMER, new Item.Properties().tab(PA_WEAPONS).stacksTo(1)));
    public static final RegistryObject<Item> CLAYMORE = register_withoutTexture("claymore", ItemClaymore::new);
    public static final RegistryObject<Item> GAE_BOLG = register_withoutTexture("gae_bolg", () -> new GaebolgItem(new Item.Properties().tab(PA_WEAPONS).durability(1000)));

    public static final RegistryObject<Item> COMMANDING_STICK = registertool("commanding_stick", ItemCommandStick::new);

    public static final RegistryObject<Item> DD_DEFAULT_RIGGING = register_withoutTexture("dd_default_rigging", () -> new itemRiggingDDDefault(new Item.Properties()
    .tab(PA_WEAPONS).stacksTo(1), 0,2,1,3,0,0, 5000, 500));

    public static final RegistryObject<Item> CV_DEFAULT_RIGGING = register_withoutTexture("cv_default_rigging", () -> new ItemRiggingCVDefault(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 0,2,4,0, 0,0,5000, 750){
        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer) {
            super.initializeClient(consumer);
            consumer.accept(new IItemRenderProperties() {
                private final BlockEntityWithoutLevelRenderer renderer = new CVDefaultRiggingRenderer();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            });
        }
    });

    public static final RegistryObject<Item> BB_DEFAULT_RIGGING = register_withoutTexture("bb_default_rigging", () -> new ItemRiggingBBDefault(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 4,2,3,1,0,0, 5000, 1200){
        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer) {
            super.initializeClient(consumer);
            consumer.accept(new IItemRenderProperties() {
                private final BlockEntityWithoutLevelRenderer renderer = new BBDefaultRiggingRenderer();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            });
        }
    });

    public static final RegistryObject<Item> EQUIPMENT_TORPEDO_533MM = register_withoutTexture("equipment_torpedo_533mm", () -> new ItemEquipmentTorpedo533Mm(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 40){
        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer) {
            super.initializeClient(consumer);
            consumer.accept(new IItemRenderProperties() {
                private final BlockEntityWithoutLevelRenderer renderer = new equipment533mmTorpedoRenderer();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            });
        }
    });

    public static final RegistryObject<Item> EQUIPMENT_GUN_127MM = register_withoutTexture("equipment_gun_127mm", () -> new ItemEquipmentGun127Mm(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 40){
        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer) {
            super.initializeClient(consumer);
            consumer.accept(new IItemRenderProperties() {
                private final BlockEntityWithoutLevelRenderer renderer = new Equipment127mmGunRenderer();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            });
        }
    });

    public static float WildcatHP = 30;

    public static final RegistryObject<Item> EQUIPMENT_PLANE_F4FWildcat = register_withoutTexture("equipment_plane_f4fwildcat", () -> new ItemPlanef4Fwildcat(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), (int) WildcatHP){
        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer) {
            super.initializeClient(consumer);
            consumer.accept(new IItemRenderProperties() {
                private final BlockEntityWithoutLevelRenderer renderer = new ItemPlanef4fWildcatRenderer();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            });
        }
    });


    public static final RegistryObject<Item> DRONE_BAMISSILE = register_withoutTexture("missiledrone", () -> new ItemMissleDrone(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1),10, 20000){
        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer) {
            super.initializeClient(consumer);
            consumer.accept(new IItemRenderProperties() {
                private final BlockEntityWithoutLevelRenderer renderer = new ItemMissileDroneRenderer();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            });
        }
    });

    public static final RegistryObject<Item> SPAWM_AYANAMI = register_AL("spawnayanami", () -> new ItemCompanionSpawnEgg<>(registerEntity.AYANAMI, new Item.Properties()
            .tab(PA_COMPANIONS)));
    public static final RegistryObject<Item> SPAWM_JAVELIN = register_AL("spawnjavelin", () -> new ItemCompanionSpawnEgg<>(registerEntity.JAVELIN, new Item.Properties()
            .tab(PA_COMPANIONS)));
    public static final RegistryObject<Item> SPAWN_Z23 = register_AL("spawnz23", () -> new ItemCompanionSpawnEgg<>(registerEntity.Z23, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_GANGWON = register_AL("spawngangwon", () -> new ItemCompanionSpawnEgg<>(registerEntity.GANGWON, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_LAFFEY = register_AL("spawnlaffey", () -> new ItemCompanionSpawnEgg<>(registerEntity.LAFFEY, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWM_ENTERPRISE = register_AL("spawnenterprise", ()-> new ItemCompanionSpawnEgg<>(registerEntity.ENTERPRISE, new Item.Properties().tab(PA_COMPANIONS)));


    public static final RegistryObject<Item> SPAWN_NAGATO = register_AL("spawnnagato", () -> new ItemCompanionSpawnEgg<>(registerEntity.NAGATO, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_CHEN = register_AKN("spawnchen", () -> new ItemCompanionSpawnEgg<>(registerEntity.CHEN, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_MUDROCK = register_AKN("spawnmudrock", () -> new ItemCompanionSpawnEgg<>(registerEntity.MUDROCK, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_M4A1 = register_GFL("spawnm4a1", () -> new ItemCompanionSpawnEgg<>(registerEntity.M4A1, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_HK416 = register_GFL("spawnhk416", () -> new ItemCompanionSpawnEgg<>(registerEntity.HK416, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_PURIFIER = register_AL("spawnpurifier", () -> new ItemCompanionSpawnEgg<>(registerEntity.PURIFIER, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_AMIYA = register_AKN("spawnamiya", () -> new ItemCompanionSpawnEgg<>(registerEntity.AMIYA, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_ROSMONTIS = register_AKN("spawnrosmontis", () -> new ItemCompanionSpawnEgg<>(registerEntity.ROSMONTIS, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_TALULAH = register_RUN("spawntalulah", () -> new ItemCompanionSpawnEgg<>(registerEntity.TALULAH, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_CROWNSLAYER = register_RUN("spawncrownslayer", () -> new ItemCompanionSpawnEgg<>(registerEntity.CROWNSLAYER, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_HARU = register_SW("spawnharu", () -> new ItemCompanionSpawnEgg<>(registerEntity.HARU, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_SHIROKO = register_BA("spawnshiroko", () -> new ItemCompanionSpawnEgg<>(registerEntity.SHIROKO, new Item.Properties()
            .tab(PA_COMPANIONS)));
    public static final RegistryObject<Item> SPAWN_SAORI = register_BA("spawnsaori", () -> new ItemCompanionSpawnEgg<>(registerEntity.SAORI, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_RABU = register_BA("spawnrabu", () -> new ItemCompanionSpawnEgg<>(registerEntity.RABU, new Item.Properties()
            .tab(PA_COMPANIONS)));


    public static final RegistryObject<Item> SPAWN_TEXAS = register_AKN("spawntexas", () -> new ItemCompanionSpawnEgg<>(registerEntity.TEXAS, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_FROSTNOVA = register_RUN("spawnfrostnova", () -> new ItemCompanionSpawnEgg<>(registerEntity.FROSTNOVA, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_SKULLSHATTERER = register_RUN("spawnskullshatterer", () -> new ItemCompanionSpawnEgg<>(registerEntity.SKULLSHATTERER, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_SIEGE = register_AKN("spawnsiege", () -> new ItemCompanionSpawnEgg<>(registerEntity.SIEGE, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_NEARL = register_AKN("spawnnearl", () -> new ItemCompanionSpawnEgg<>(registerEntity.NEARL, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_SCHWARZ = register_AKN("spawnschwarz", () -> new ItemCompanionSpawnEgg<>(registerEntity.SCHWARZ, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_LAPPLAND = register_AKN("spawnlappland", () -> new ItemCompanionSpawnEgg<>(registerEntity.LAPPLAND, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_ARTORIA = register_FGO("spawnartoria", () -> new ItemCompanionSpawnEgg<>(registerEntity.ARTORIA, new Item.Properties()
            .tab(PA_COMPANIONS)));
    public static final RegistryObject<Item> SPAWN_SCATHATH = register_FGO("spawnscathath", () -> new ItemCompanionSpawnEgg<>(registerEntity.SCATHATH, new Item.Properties()
            .tab(PA_COMPANIONS)));
    public static final RegistryObject<Item> SPAWN_SHIKI = register_FGO("spawnshiki", () -> new ItemCompanionSpawnEgg<>(registerEntity.SHIKI, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_KYARU = register_PCR("spawnkyaru", () -> new ItemCompanionSpawnEgg<>(registerEntity.KYARU, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> BANDAGE_ROLL = register_AKN("bandage_roll", () -> new ItemBandage(new Item.Properties()
            .tab(PA_GROUP)));
    public static final RegistryObject<Item> SPAWN_SYLVI = register_CLS("spawnsylvi", () -> new ItemCompanionSpawnEgg<>(registerEntity.SYLVI, new Item.Properties()
            .tab(PA_COMPANIONS)));
    public static final RegistryObject<Item> SPAWN_YAMATO = register_KC("spawnyamato", () -> new ItemCompanionSpawnEgg<>(registerEntity.YAMATO, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_YATO = register_AKN("spawnyato", () -> new ItemCompanionSpawnEgg<>(registerEntity.YATO, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_EXCELA = register_SR("spawnexcela", () -> new ItemCompanionSpawnEgg<>(registerEntity.EXCELA, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_W = register_AKN("spawnw", () -> new ItemCompanionSpawnEgg<>(registerEntity.W, new Item.Properties()
            .tab(PA_COMPANIONS)));

    public static final RegistryObject<Item> SPAWN_MASH = register_FGO("spawnmash", () -> new ItemCompanionSpawnEgg<>(registerEntity.MASH, new Item.Properties()
            .tab(PA_COMPANIONS)));


    public static final RegistryObject<Item> MEDKIT = register("medkit", ItemMedKit::new);

    public static final RegistryObject<Item> DEFIB_PADDLE = register_withoutTexture("defib_paddle", ItemDefibPaddle::new);
    public static final RegistryObject<Item> DEFIB_CHARGER = register_withoutTexture("defib_charger", ItemDefibCharger::new);

    public static final RegistryObject<Item> STASIS_CRYSTAL = register("stasis_crystal", ItemStasisCrystal::new);

    public static final RegistryObject<Item> DEVELOPER_BONUS = register("developer_bonus", () -> new ItemContributorBonus(new Item.Properties()));
    public static final RegistryObject<Item> CONTRIBUTOR_BONUS = register("contributor_bonus", () -> new ItemContributorBonus(new Item.Properties()));


    public static RegistryObject<Item> registerResource(String id, enums.ResourceItemType type){
        return register_withTexturename(type.getName()+"_"+id, type.getName()+"_"+id, () -> new ItemResource(id, type));
    }

    public static RegistryObject<Item> register(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, id, supplier);
    }

    public static RegistryObject<Item> registertool(String id, Supplier<? extends Item> supplier){
        return registertool_withTexturename(id, id, supplier, true);
    }

    public static RegistryObject<Item> register_withoutTexture(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, id, supplier, false);
    }

    public static RegistryObject<Item> registerGun_with2DTexture(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "gun_icons/"+id, supplier, true);
    }

    public static RegistryObject<Item> register_AKN(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "akn_document", supplier);
    }
    public static RegistryObject<Item> register_AL(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "wisdomcube", supplier);
    }
    public static RegistryObject<Item> register_BA(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "spawn_bluearchive", supplier);
    }
    public static RegistryObject<Item> register_CLS(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "spawn_closer", supplier);
    }
    public static RegistryObject<Item> register_GFL(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "gfl_manufacture_contract", supplier);
    }
    public static RegistryObject<Item> register_KC(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "kc_card", supplier);
    }
    public static RegistryObject<Item> register_SR(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "sr_sealstone", supplier);
    }
    public static RegistryObject<Item> register_PCR(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "goddess_stone", supplier);
    }
    public static RegistryObject<Item> register_FGO(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "saint_quartz", supplier);
    }
    public static RegistryObject<Item> register_RUN(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "akn_reunion_document", supplier);
    }

    public static RegistryObject<Item> register_SW(String id, Supplier<? extends Item> supplier){
        return register_withTexturename(id, "sw_card", supplier);
    }

    public static RegistryObject<Item> register_withTexturename(String id, String texturename, Supplier<? extends Item> supplier){
        return register_withTexturename(id, texturename, supplier, true);
    }
    public static RegistryObject<Item> register_withTexturename(String id, String texturename, Supplier<? extends Item> supplier, boolean registerItemModel){
        if(registerItemModel) {
            ITEMENTRY.add(new Pair<>(id, texturename));
        }
        return ITEMS.register(id, supplier);
    }

    public static RegistryObject<Item> registertool_withTexturename(String id, String texturename, Supplier<? extends Item> supplier, boolean registerItemModel){
        if(registerItemModel) {
            TOOLENTRY.add(new Pair<>(id, texturename));
        }
        return ITEMS.register(id, supplier);
    }


    public static void register(){}

}
