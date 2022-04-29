package com.yor42.projectazure.data.common;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.data.recipebuilder.AlloyingRecipeBuilder;
import com.yor42.projectazure.data.recipebuilder.CrushingRecipeBuilder;
import com.yor42.projectazure.data.recipebuilder.CrystalizingRecipeBuilder;
import com.yor42.projectazure.data.recipebuilder.PressingRecipeBuilder;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
        BuildMetalRecipe(consumer, 0.5F, new Metals("copper", Main.INGOT_COPPER.get(), ModTags.Items.INGOT_COPPER).ore(Main.COPPER_ORE.get().asItem(), ModTags.Items.ORES_COPPER).dust(Main.DUST_COPPER.get(), ModTags.Items.DUST_COPPER).plates(Main.PLATE_COPPER.get(), ModTags.Items.PLATE_COPPER).gear(Main.GEAR_COPPER.get(), ModTags.Items.GEAR_COPPER).nugget(Main.NUGGET_COPPER.get(), ModTags.Items.NUGGET_COPPER));
        BuildMetalRecipe(consumer, 0.5F, new Metals("tin", Main.INGOT_TIN.get(), ModTags.Items.INGOT_TIN).ore(Main.TIN_ORE.get().asItem(), ModTags.Items.ORES_TIN).dust(Main.DUST_TIN.get(), ModTags.Items.DUST_TIN).plates(Main.PLATE_TIN.get(), ModTags.Items.PLATE_TIN).gear(Main.GEAR_TIN.get(), ModTags.Items.GEAR_TIN).nugget(Main.NUGGET_TIN.get(), ModTags.Items.NUGGET_TIN));
        BuildMetalRecipe(consumer, 0.5F, new Metals("lead", Main.INGOT_LEAD.get(), ModTags.Items.INGOT_LEAD).ore(Main.LEAD_ORE.get().asItem(), ModTags.Items.ORES_LEAD).dust(Main.DUST_LEAD.get(), ModTags.Items.DUST_LEAD).plates(Main.PLATE_LEAD.get(), ModTags.Items.PLATE_LEAD).nugget(Main.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD).nugget(Main.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD));
        BuildMetalRecipe(consumer, 0.5F, new Metals("bronze", Main.INGOT_BRONZE.get(), ModTags.Items.INGOT_BRONZE).dust(Main.DUST_BRONZE.get(), ModTags.Items.DUST_BRONZE).plates(Main.PLATE_BRONZE.get(), ModTags.Items.PLATE_BRONZE).gear(Main.GEAR_BRONZE.get(), ModTags.Items.GEAR_BRONZE).nugget(Main.NUGGET_BRONZE.get(), ModTags.Items.NUGGET_BRONZE));
        BuildMetalRecipe(consumer, 0.5F, new Metals("aluminium", Main.INGOT_ALUMINIUM.get(), ModTags.Items.INGOT_ALUMINIUM).ore(Main.BAUXITE_ORE.get().asItem(), ModTags.Items.ORES_ALUMINIUM).dust(Main.DUST_ALUMINIUM.get(), ModTags.Items.DUST_ALUMINIUM).plates(Main.PLATE_ALUMINIUM.get(), ModTags.Items.PLATE_ALUMINIUM));
        BuildMetalRecipe(consumer, 0.5F, new Metals("steel", Main.INGOT_STEEL.get(), ModTags.Items.INGOT_STEEL).dust(Main.DUST_STEEL.get(), ModTags.Items.DUST_STEEL).plates(Main.PLATE_STEEL.get(), ModTags.Items.PLATE_STEEL).gear(Main.GEAR_STEEL.get(), ModTags.Items.GEAR_STEEL).nugget(Main.NUGGET_STEEL.get(), ModTags.Items.NUGGET_STEEL));
        BuildMetalRecipe(consumer, 0.5F, new Metals("brass", Main.INGOT_BRASS.get(), ModTags.Items.INGOT_BRASS).dust(Main.DUST_BRASS.get(), ModTags.Items.DUST_BRASS).plates(Main.PLATE_BRASS.get(), ModTags.Items.PLATE_BRASS).nugget(Main.NUGGET_BRASS.get(), ModTags.Items.PLATE_BRASS));
        BuildMetalRecipe(consumer, 0.5F, new Metals("zinc", Main.INGOT_ZINC.get(), ModTags.Items.INGOT_ZINC).dust(Main.DUST_ZINC.get(), ModTags.Items.DUST_ZINC).plates(Main.PLATE_ZINC.get(), ModTags.Items.PLATE_ZINC).nugget(Main.NUGGET_ZINC.get(), ModTags.Items.NUGGET_ZINC));

        BuildMetalRecipe(consumer, 0.5F, new Metals("iron", Items.IRON_INGOT, Tags.Items.INGOTS_IRON).dust(Main.DUST_IRON.get(), ModTags.Items.DUST_IRON).plates(Main.PLATE_IRON.get(), ModTags.Items.PLATE_IRON).gear(Main.GEAR_IRON.get(), ModTags.Items.GEAR_IRON));
        BuildMetalRecipe(consumer, 0.5F, new Metals("gold", Items.GOLD_INGOT, Tags.Items.INGOTS_GOLD).dust(Main.DUST_GOLD.get(), ModTags.Items.DUST_GOLD).plates(Main.PLATE_GOLD.get(), ModTags.Items.PLATE_GOLD).gear(Main.GEAR_GOLD.get(), ModTags.Items.GEAR_GOLD));

        ShapedRecipeBuilder.shaped(Main.HAMMER_IRON.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.STICK)
                .pattern("III")
                .pattern("ISI")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Items.STICK))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(consumer);
        ShapedRecipeBuilder.shaped(Main.MORTAR_IRON.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.STICK)
                .define('C', Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern("CCC")
                .unlockedBy("has_stick", has(Items.STICK))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.STEEL_CUTTER.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .define('S', Items.STICK)
                .define('N', Items.IRON_NUGGET)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Items.STICK))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.MACHINE_FRAME.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .define('P', Main.MECHANICAL_PARTS.get())
                .define('N', Tags.Items.INGOTS_IRON)
                .pattern("NIN")
                .pattern("IPI")
                .pattern("NIN")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.BASIC_REFINERY.get())
                .define('M', Main.MACHINE_FRAME.get())
                .define('P', Main.STEEL_PIPE.get())
                .define('B', Items.BLAST_FURNACE)
                .define('S', Items.SMOOTH_STONE_SLAB)
                .pattern("MPP")
                .pattern("MMM")
                .pattern("BSS")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.ALLOY_FURNACE.get())
                .define('F', Items.FURNACE)
                .define('B', Blocks.BRICKS)
                .define('S', ModTags.Items.PLATE_STEEL)
                .pattern("SBS")
                .pattern("BFB")
                .pattern("SBS")
                .unlockedBy("has_furnace", has(Items.FURNACE))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.METAL_PRESS.get())
                .define('L', Items.LEVER)
                .define('R', Blocks.REDSTONE_BLOCK)
                .define('P', Items.PISTON)
                .define('S', Main.MACHINE_FRAME.get())
                .define('B', Main.PRIMITIVE_CIRCUIT.get())
                .define('W', Main.COPPER_WIRE.get())
                .define('C', Main.COPPER_COIL.get())
                .pattern("WBL")
                .pattern("PRP")
                .pattern("CSC")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.RECRUIT_BEACON.get())
                .define('L', Main.STEEL_PIPE.get())
                .define('R', ModTags.Items.PLATE_STEEL)
                .define('O', Main.ORUNDUM.get())
                .define('S', Main.MACHINE_FRAME.get())
                .define('B', Main.PRIMITIVE_CIRCUIT.get())
                .define('C', Main.COPPER_COIL.get())
                .pattern("  L")
                .pattern("ORC")
                .pattern("BSB")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.DUST_BRONZE.get(), 2)
                .define('C', ModTags.Items.DUST_COPPER)
                .define('T', ModTags.Items.DUST_TIN)
                .pattern("CC")
                .pattern("CT")
                .unlockedBy("has_copper", has(ModTags.Items.DUST_COPPER))
                .unlockedBy("has_tin", has(ModTags.Items.DUST_TIN))
                .save(consumer, new ResourceLocation("bronze_powder_alloying"));

        ShapedRecipeBuilder.shaped(Main.EQUIPMENT_GUN_127MM.get())
                .define('S', ModTags.Items.PLATE_STEEL)
                .define('B', Main.STEEL_PIPE.get())
                .define('P', Main.MECHANICAL_PARTS.get())
                .define('M', Main.BASIC_MOTOR.get())
                .define('E', Main.PRIMITIVE_CIRCUIT.get())
                .pattern("PSS")
                .pattern("BPS")
                .pattern("EME")
                .unlockedBy("has_circuit", has(Main.PRIMITIVE_CIRCUIT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(Main.COPPER_WIRE.get(), 3)
                .requires(ModTags.Items.CUTTER)
                .requires(ModTags.Items.PLATE_COPPER)
                .unlockedBy("has_copper_plate", has(ModTags.Items.PLATE_COPPER))
                .unlockedBy("has_cutter", has(ModTags.Items.CUTTER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.COPPER_COIL.get(), 2)
                .define('W', Main.COPPER_WIRE.get())
                .define('B', Main.IRON_PIPE.get())
                .pattern("WBW")
                .pattern("WBW")
                .pattern("WBW")
                .unlockedBy("has_wire", has(Main.COPPER_WIRE.get()))
                .unlockedBy("has_pipe", has(Main.IRON_PIPE.get()))
                .save(consumer);

        AlloyingRecipeBuilder.addRecipe(Ingredient.of(ModTags.Items.INGOT_COPPER), 3,Ingredient.of(ModTags.Items.INGOT_TIN), 1, Main.INGOT_BRONZE.get(), 4, 300)
                .addCriterion("hastin", has(ModTags.Items.INGOT_TIN))
                .addCriterion("hascopper", has(ModTags.Items.INGOT_COPPER))
                .build(consumer);

        AlloyingRecipeBuilder.addRecipe(Ingredient.of(ModTags.Items.INGOT_COPPER), 2,Ingredient.of(ModTags.Items.INGOT_ZINC), 1, Main.INGOT_BRASS.get(), 3, 300)
                .addCriterion("haszinc", has(ModTags.Items.INGOT_ZINC))
                .addCriterion("hascopper", has(ModTags.Items.INGOT_COPPER))
                .build(consumer);

        PressingRecipeBuilder.addRecipe(Main.TREE_SAP.get(), Ingredient.of(ItemTags.LOGS), Ingredient.of(Main.MOLD_EXTRACTION.get()), 1, 200)
                .addCriterion("hasmold", has(Main.MOLD_EXTRACTION.get()))
                .build(consumer, new ResourceLocation("iron_plate_pressing"));

        CrystalizingRecipeBuilder.addRecipe(Main.ORIGINIUM_PRIME.get(), Ingredient.of(Main.ORIGINIUM_SEED.get()), Main.ORIGINIUM_SOLUTION_REGISTRY.get(), 3000)
                .addCriterion("hasseed", has(Main.ORIGINIUM_SEED.get()))
                .build(consumer, new ResourceLocation("originium_crystalizing"));

        CrystalizingRecipeBuilder.addRecipe(Items.QUARTZ, Ingredient.of(Main.NETHER_QUARTZ_SEED.get()), Main.NETHER_QUARTZ_SOLUTION_REGISTRY.get(), 400)
                .addCriterion("hasseed", has(Main.NETHER_QUARTZ_SEED.get()))
                .build(consumer, new ResourceLocation("quartz_crystalizing"));

        ShapedRecipeBuilder.shaped(Main.TREE_SAP.get(),2)
                .define('L', ItemTags.LOGS)
                .pattern("LLL")
                .pattern("LLL")
                .pattern("LLL")
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.REENFORCEDCONCRETE.get().asItem(),2)
                .define('G', Blocks.GRAY_CONCRETE_POWDER)
                .define('S', ModTags.Items.PLATE_STEEL)
                .pattern("SGS")
                .pattern("GSG")
                .pattern("SGS")
                .unlockedBy("has_concrete_powder", has(Blocks.GRAY_CONCRETE_POWDER))
                .save(consumer);


        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModTags.Items.TREE_SAP), Main.PLATE_POLYMER.get(), 0.5F, 200)
                .unlockedBy("has_item", has(ModTags.Items.TREE_SAP))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.CAPACITOR_PRIMITIVE.get(), 1)
                .define('A', Main.PLATE_ALUMINIUM.get())
                .define('P', Items.PAPER)
                .define('C', Main.COPPER_WIRE.get())
                .pattern("APA")
                .pattern("APA")
                .pattern("C C")
                .unlockedBy("has_wire", has(Main.COPPER_WIRE.get()))
                .unlockedBy("has_aluminium", has(ModTags.Items.PLATE_ALUMINIUM))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.RESISTOR_PRIMITIVE.get(), 1)
                .define('C', ModTags.Items.DUST_COAL)
                .define('D', Main.DUST_IRON.get())
                .define('P', Items.PAPER)
                .define('W', Main.COPPER_WIRE.get())
                .pattern(" P ")
                .pattern("WCW")
                .pattern(" D ")
                .unlockedBy("has_wire", has(Main.COPPER_WIRE.get()))
                .unlockedBy("has_carbon", has(ModTags.Items.DUST_COAL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.BASIC_MOTOR.get(), 1)
                .define('C', Main.COPPER_COIL.get())
                .define('D', Items.IRON_INGOT)
                .define('P', Main.PLATE_IRON.get())
                .define('W', Main.COPPER_WIRE.get())
                .pattern(" D ")
                .pattern("PCP")
                .pattern("PWP")
                .unlockedBy("has_coil", has(Main.COPPER_COIL.get()))
                .unlockedBy("has_wire", has(Main.COPPER_WIRE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.PRIMITIVE_CIRCUIT.get(), 1)
                .define('C', ModTags.Items.PLATE_COPPER)
                .define('P', Main.PLATE_POLYMER.get())
                .define('I', ModTags.Items.PLATE_IRON)
                .define('L', Items.REDSTONE)
                .define('R', Main.RESISTOR_PRIMITIVE.get())
                .define('T', Main.CAPACITOR_PRIMITIVE.get())
                .pattern(" I ")
                .pattern("RLT")
                .pattern("CPC")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_COPPER))
                .unlockedBy("has_polymer", has(Main.PLATE_POLYMER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.MECHANICAL_PARTS.get(), 2)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('I', Tags.Items.INGOTS_IRON)
                .pattern("GPG")
                .pattern("PIP")
                .pattern("GPG")
                .unlockedBy("has_gear", has(ModTags.Items.GEAR_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.CLAYMORE_ITEM.get())
                .define('S', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('P', Main.IRON_PIPE.get())
                .pattern("PP")
                .pattern("SS")
                .pattern("SS")
                .unlockedBy("has_handle", has(Tags.Items.STORAGE_BLOCKS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.ORUNDUM.get(), 4)
                .define('C', ModTags.Items.WIRE_COPPER)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('O', ModTags.Items.ORIGINITE)
                .define('R', ModTags.Items.CIRCUITS_BASIC)
                .pattern("CPC")
                .pattern("ROR")
                .pattern("CPC")
                .unlockedBy("has_originite", has(ModTags.Items.ORIGINITE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.HEADHUNTING_PCB.get())
                .define('P', Items.ENDER_PEARL)
                .define('E', Items.ENDER_EYE)
                .define('R', ModTags.Items.CIRCUITS_BASIC)
                .define('O', Main.ORUNDUM.get())
                .pattern("EP")
                .pattern("OR")
                .unlockedBy("has_orundum", has(Main.ORUNDUM.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(Main.BANDAGE_ROLL.get(), 4)
                .define('W', ItemTags.WOOL)
                .pattern("WWW")
                .pattern("W W")
                .pattern("WWW")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.ADVANCED_CIRCUIT.get(), 2)
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('O', Main.ORUNDUM.get())
                .define('R', Main.RESISTOR_PRIMITIVE.get())
                .define('T', Main.CAPACITOR_PRIMITIVE.get())
                .define('K', Main.PLATE_POLYMER.get())
                .pattern("ROT")
                .pattern("CKC")
                .unlockedBy("has_orundum", has(Main.ORUNDUM.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(Main.DRYDOCKCONTROLLER.get(), 1)
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('F', Main.MACHINE_FRAME.get().asItem())
                .define('A', ModTags.Items.GEAR_STEEL)
                .define('L', Items.REDSTONE_LAMP)
                .define('M', Main.BASIC_MOTOR.get())
                .define('O', Main.ORUNDUM.get())
                .pattern("COC")
                .pattern("LFL")
                .pattern("MAM")
                .unlockedBy("has_orundum", has(Main.ORUNDUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.SLEDGEHAMMER.get())
                .define('S', Main.STEEL_PIPE.get())
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(Main.ORIGINITE.get(), 9)
                .requires(ModTags.Items.ORIGINIUM_PRIME).unlockedBy("hasoriginium", has(Main.ORIGINIUM_PRIME.get()));

        ShapedRecipeBuilder.shaped(Main.WISDOM_CUBE.get(), 2)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .pattern("QCQ")
                .pattern("LDR")
                .pattern("QCQ")
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.AMMO_5_56.get(), 2)
                .define('B', ModTags.Items.INGOT_BRASS)
                .define('L', ModTags.Items.INGOT_LEAD)
                .define('G', Tags.Items.GUNPOWDER)
                .pattern(" L ")
                .pattern("BGB")
                .pattern("BGB")
                .unlockedBy("has_breass", has(ModTags.Items.INGOT_BRASS))
                .unlockedBy("has_lead", has(ModTags.Items.INGOT_LEAD))
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.STEEL_RIFLE_FRAME.get(), 1)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('I', ModTags.Items.INGOT_STEEL)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('M', Main.MECHANICAL_PARTS.get())
                .pattern("PPP")
                .pattern("MIM")
                .pattern("PPG")
                .unlockedBy("has_parts", has(Main.MECHANICAL_PARTS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.COMMANDING_STICK.get(), 1)
                .define('S', Items.STICK)
                .define('B', Tags.Items.DYES_BLACK)
                .pattern("  S")
                .pattern(" S ")
                .pattern("B  ")
                .unlockedBy("has_sticc", has(Items.STICK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.PISTOL_GRIP.get(), 1)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('I', ModTags.Items.INGOT_STEEL)
                .pattern("IP ")
                .pattern("PIP")
                .pattern(" PP")
                .unlockedBy("has_parts", has(Main.MECHANICAL_PARTS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.ABYDOS_550.get(), 1)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('I', ModTags.Items.INGOT_STEEL)
                .define('B', Main.STEEL_RIFLE_FRAME.get())
                .define('M', Main.MAGAZINE_5_56.get())
                .define('R', Main.STEEL_PIPE.get())
                .define('G', Main.PISTOL_GRIP.get())
                .define('T', Main.HAMMER_IRON.get())
                .pattern("PPP")
                .pattern("RBI")
                .pattern("TMG")
                .unlockedBy("has_parts", has(Main.STEEL_RIFLE_FRAME.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.AMMO_GENERIC.get(), 2)
                .define('B', ModTags.Items.PLATE_BRASS)
                .define('L', ModTags.Items.INGOT_LEAD)
                .define('G', Tags.Items.GUNPOWDER)
                .pattern(" L ")
                .pattern("BGB")
                .pattern("BGB")
                .unlockedBy("has_breass", has(ModTags.Items.PLATE_BRASS))
                .unlockedBy("has_lead", has(ModTags.Items.INGOT_LEAD))
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.CRESCENTKATANA_KURO.get())
                .define('B', ModTags.Items.INGOT_STEEL)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('L', Main.STEEL_PIPE.get())
                .pattern("  B")
                .pattern(" BP")
                .pattern("LP ")
                .unlockedBy("has_ingot", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.CRESCENTKATANA_SHIRO.get())
                .define('B', Tags.Items.INGOTS_IRON)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('L', Main.STEEL_PIPE.get())
                .pattern("  B")
                .pattern(" BP")
                .pattern("LP ")
                .unlockedBy("has_ingot", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.WARHAMMER.get())
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('S', ModTags.Items.INGOT_STEEL)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('L', Main.STEEL_PIPE.get())
                .pattern("BSP")
                .pattern(" L ")
                .pattern(" L ")
                .unlockedBy("has_ingot", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.AMMO_TORPEDO.get(), 1)
                .define('A', ModTags.Items.INGOT_ALUMINIUM)
                .define('T', Blocks.TNT.asItem())
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('S', ModTags.Items.PLATE_STEEL)
                .pattern(" AT")
                .pattern("ACA")
                .pattern("SA ")
                .unlockedBy("has_aluminium", has(ModTags.Items.INGOT_ALUMINIUM))
                .unlockedBy("has_circuit", has(ModTags.Items.CIRCUITS_BASIC))
                .unlockedBy("has_tnt", has(Blocks.TNT.asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.DISC_FRIDAYNIGHT.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.DYES_GREEN)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.DYES_GREEN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.REENFORCED_PLANK.get(), 4)
                .define('P', ItemTags.PLANKS)
                .define('S', ModTags.Items.INGOT_STEEL)
                .pattern("SPS")
                .pattern("PSP")
                .pattern("SPS")
                .unlockedBy("has_steel", has(ModTags.Items.INGOT_STEEL))
                .unlockedBy("has_plank", has(ItemTags.PLANKS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.DISC_BRAINPOWER.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.DYES_BLACK)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.DYES_BLACK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.DISC_SANDSTORM.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.SAND)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_sand", has(Tags.Items.SAND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.DISC_CC5.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', ModTags.Items.ORIGINIUM_PRIME)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_sand", has(ModTags.Items.ORIGINIUM_PRIME))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(Main.DISC_SANDROLL.get(),1).requires(Main.DISC_SANDSTORM.get()).requires(Main.DISC_RICKROLL.get())
                .unlockedBy("has_sandstorm", has(Main.DISC_SANDSTORM.get()))
                .unlockedBy("has_rickroll", has(Main.DISC_RICKROLL.get()))
                        .save(consumer);

        ShapedRecipeBuilder.shaped(Main.DISC_RICKROLL.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.DYES_GRAY)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.DYES_GRAY))
                .save(consumer);

        SpecialRecipeBuilder.special((SimpleRecipeSerializer<?>) Main.RELOADING.get()).save(consumer, "reloading_items");
        SpecialRecipeBuilder.special((SimpleRecipeSerializer<?>) Main.REPAIRING.get()).save(consumer, "repairing_items");

        ShapedRecipeBuilder.shaped(Main.CRYSTAL_GROWTH_CHAMBER.get(), 1)
                .define('S', Blocks.SMOOTH_STONE_SLAB)
                .define('C', Blocks.CAULDRON)
                .define('P', Main.STEEL_PIPE.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .pattern("III")
                .pattern("CPC")
                .pattern("SSS")
                .unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.NETHER_QUARTZ_SEED.get(), 4)
                .define('S', ItemTags.SAND)
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .pattern("SQ")
                .pattern("QS")
                .unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.ORIGINIUM_SEED.get(), 4)
                .define('S', ItemTags.SAND)
                .define('O', Main.DUST_ORIGINIUM.get())
                .pattern("SO")
                .pattern("OS")
                .unlockedBy("has_originium", has(Main.DUST_ORIGINIUM.get()))
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(Main.DUST_ORIGINIUM.get(), 3)
                .requires(Main.ORIGINITE.get())
                .requires(Main.HAMMER_IRON.get())
                .unlockedBy("has_originite", has(Main.ORIGINITE.get()))
                .save(consumer, new ResourceLocation("originium_dust_from_originite"));

        ShapelessRecipeBuilder.shapeless(Main.DUST_NETHER_QUARTZ.get(), 2)
                .requires(Items.QUARTZ)
                .requires(Main.HAMMER_IRON.get())
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .save(consumer, new ResourceLocation("quartz_dust_from_quartz"));

        ShapelessRecipeBuilder.shapeless(Main.DUST_STEEL.get(), 2)
                .requires(ModTags.Items.DUST_IRON)
                .requires(ModTags.Items.DUST_IRON)
                .requires(ModTags.Items.DUST_COAL)
                .unlockedBy("has_iron", has(ModTags.Items.DUST_IRON))
                .save(consumer, new ResourceLocation("steel_dust_from_iron"));

        ShapedRecipeBuilder.shaped(Main.DD_DEFAULT_RIGGING.get(), 1)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .define('S', ModTags.Items.INGOT_STEEL)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', Main.MECHANICAL_PARTS.get())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('M', Main.BASIC_MOTOR.get())
                .define('W', Main.WISDOM_CUBE.get())
                .pattern("CSP")
                .pattern("AWG")
                .pattern(" MA")
                .unlockedBy("has_cube", has(Main.WISDOM_CUBE.get()))
                .unlockedBy("has_motor", has(Main.BASIC_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.IRON_PIPE.get(), 1)
                .define('A', ModTags.Items.PLATE_IRON)
                .pattern("A")
                .pattern("A")
                .unlockedBy("has_ironplate", has(ModTags.Items.PLATE_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.STEEL_PIPE.get(), 1)
                .define('A', ModTags.Items.PLATE_STEEL)
                .pattern("A")
                .pattern("A")
                .unlockedBy("has_steelplate", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.BB_DEFAULT_RIGGING.get(), 1)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .define('S', ModTags.Items.PLATE_STEEL)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', Main.MECHANICAL_PARTS.get())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('M', Main.BASIC_MOTOR.get())
                .define('D', Main.DD_DEFAULT_RIGGING.get())
                .pattern("SMS")
                .pattern("PDG")
                .pattern("ACA")
                .unlockedBy("has_dd_rigging", has(Main.DD_DEFAULT_RIGGING.get()))
                .unlockedBy("has_motor", has(Main.BASIC_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.CV_DEFAULT_RIGGING.get(), 1)
                .define('W', Main.REENFORCED_PLANK.get().asItem())
                .define('M', Main.BASIC_MOTOR.get())
                .define('D', Main.DD_DEFAULT_RIGGING.get())
                .define('P', Main.MECHANICAL_PARTS.get())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('S', ModTags.Items.PLATE_STEEL)
                .define('I', ModTags.Items.INGOT_STEEL)
                .pattern("SSW")
                .pattern("IDW")
                .pattern("PCM")
                .unlockedBy("has_dd_rigging", has(Main.DD_DEFAULT_RIGGING.get()))
                .unlockedBy("has_motor", has(Main.BASIC_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.EQUIPMENT_TORPEDO_533MM.get(), 1)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .define('S', Main.STEEL_PIPE.get())
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', Main.MECHANICAL_PARTS.get())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('M', Main.BASIC_MOTOR.get())
                .pattern("ACA")
                .pattern("SSS")
                .pattern("GMP")
                .unlockedBy("has_barrel", has(Main.STEEL_PIPE.get()))
                .unlockedBy("has_part", has(Main.MECHANICAL_PARTS.get()))
                .unlockedBy("has_motor", has(Main.BASIC_MOTOR.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(Main.SHEATH.get())
                .define('B', ModTags.Items.PLATE_STEEL)
                .define('L', Main.STEEL_PIPE.get())
                .pattern("  B")
                .pattern(" B ")
                .pattern("L  ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        //Temp Recipe
        ShapedRecipeBuilder.shaped(Main.FLEXABLE_SWORD_THINGY.get())
                .define('B', ModTags.Items.PLATE_STEEL)
                .define('L', Main.PLATE_POLYMER.get())
                .pattern("  B")
                .pattern(" B ")
                .pattern("L  ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        createHammerRecipes(consumer, Blocks.STONE, Blocks.COBBLESTONE, "cobblestone");
        createHammerRecipes(consumer, Blocks.COBBLESTONE, Blocks.GRAVEL, "gravel");
        createHammerRecipes(consumer, Blocks.GRAVEL, Blocks.SAND, "sand");
        createHammerRecipes(consumer, ModTags.Items.ORES_COPPER, Main.DUST_COPPER.get(), 2, "copper");
        createHammerRecipes(consumer, ModTags.Items.ORES_ALUMINIUM, Main.DUST_ALUMINIUM.get(), 2, "aluminium");
        createHammerRecipes(consumer, ModTags.Items.ORES_LEAD, Main.DUST_LEAD.get(), 2, "lead");
        createHammerRecipes(consumer, ModTags.Items.ORES_TIN, Main.DUST_TIN.get(), 2, "tin");
        createHammerRecipes(consumer, ModTags.Items.ORES_ZINC, Main.DUST_ZINC.get(), 2, "zinc");
        createHammerRecipes(consumer, Tags.Items.ORES_IRON, Main.DUST_IRON.get(), 2, "iron");
        createHammerRecipes(consumer, Tags.Items.ORES_COAL, Main.DUST_COAL.get(), 2, "coal");
        createHammerRecipes(consumer, Tags.Items.ORES_GOLD, Main.DUST_GOLD.get(), 2, "gold");

        ShapedRecipeBuilder.shaped(Main.DEFIB_PADDLE.get(), 1)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Items.STONE_BUTTON)
                .define('P', Main.PLATE_POLYMER.get())
                .define('C', Main.COPPER_COIL.get())
                .define('H', ModTags.Items.CIRCUITS_BASIC)
                .pattern("BPP")
                .pattern("PCH")
                .pattern("III")
                .unlockedBy("has_part", has(Main.PRIMITIVE_CIRCUIT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.DEFIB_CHARGER.get(), 1)
                .define('W', Main.COPPER_COIL.get())
                .define('C', Main.CAPACITOR_PRIMITIVE.get())
                .define('P', Items.REDSTONE_LAMP)
                .define('I',  ModTags.Items.CIRCUITS_ADVANCED)
                .define('B', Main.HEADHUNTING_PCB.get())
                .define('R', Main.RESISTOR_PRIMITIVE.get())
                .pattern("WPW")
                .pattern("RBR")
                .pattern("CIC")
                .unlockedBy("has_paddle", has(Main.DEFIB_PADDLE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Main.MOLD_EXTRACTION.get(), 1)
                .define('N', Tags.Items.NUGGETS_IRON)
                .define('P', ModTags.Items.PLATE_IRON)
                .define('M', Main.MOLD_PLATE.get())
                .pattern("PNP")
                .pattern("PMP")
                .pattern("NPN")
                .unlockedBy("has_part", has(ModTags.Items.PLATE_IRON))
                .save(consumer);
    }

    private void BuildMetalRecipe(Consumer<FinishedRecipe> consumer, float smeltingXp, Metals metal) {
        if (metal.ore != null) {
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(metal.oreTag), metal.ingot, smeltingXp, 100)
                    .unlockedBy("has_item", has(metal.oreTag))
                    .save(consumer, ResourceUtils.ModResourceLocation(metal.name + "_ore_blasting"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(metal.oreTag), metal.ingot, smeltingXp, 200)
                    .unlockedBy("has_item", has(metal.oreTag))
                    .save(consumer);
        }

        CriterionTriggerInstance hasIngot = has(metal.ingotTag);
        CriterionTriggerInstance hasPlate = has(metal.plateTag);

        if (metal.block != null) {
            ShapelessRecipeBuilder.shapeless(metal.ingot, 9)
                    .requires(metal.blockTag)
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer, new ResourceLocation(metal.ingot.asItem().getRegistryName() + "_from_block"));
            ShapedRecipeBuilder.shaped(metal.block)
                    .define('#', metal.ingotTag)
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer);
        }
        if (metal.nugget != null) {
            ShapelessRecipeBuilder.shapeless(metal.nugget, 9)
                    .requires(metal.ingotTag)
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer);
            ShapedRecipeBuilder.shaped(metal.ingot)
                    .define('#', metal.nuggetTag)
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer, new ResourceLocation(metal.ingot.asItem().getRegistryName() + "_from_nuggets"));
        }
        if (metal.dustTag != null) {
            Ingredient dustOrChunks = metal.chunksTag != null
                    ? Ingredient.of(metal.chunksTag)
                    : Ingredient.of(metal.dustTag);
            SimpleCookingRecipeBuilder.blasting(dustOrChunks, metal.ingot, smeltingXp, 100)
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer, ResourceUtils.ModResourceLocation(metal.name + "_dust_blasting"));
            SimpleCookingRecipeBuilder.smelting(dustOrChunks, metal.ingot, smeltingXp, 200)
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer, ResourceUtils.ModResourceLocation(metal.name + "_dust_smelting"));
        }
        if (metal.dust != null) {
            ShapelessRecipeBuilder.shapeless(metal.dust, 1)
                    .requires(ModTags.Items.MORTAR)
                    .requires(metal.ingotTag)
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer);
        }

        if (metal.gear != null) {
            ShapedRecipeBuilder.shaped(metal.gear)
                    .define('#', metal.plateTag)
                    .define('C', ModTags.Items.CUTTER)
                    .pattern(" # ")
                    .pattern("#C#")
                    .pattern(" # ")
                    .unlockedBy("has_plate", hasPlate)
                    .save(consumer);
        }

        if (metal.plate != null) {
            ShapelessRecipeBuilder.shapeless(metal.plate, 1)
                    .requires(ModTags.Items.HAMMER)
                    .requires(metal.ingotTag)
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer);
            ShapedRecipeBuilder.shaped(metal.plate)
                    .define('#', metal.ingotTag)
                    .pattern("###")
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer, new ResourceLocation(metal.ingot.asItem().getRegistryName() + "_without_hammer"));

            PressingRecipeBuilder.addRecipe(metal.plate, Ingredient.of(metal.ingotTag), Ingredient.of(Main.MOLD_PLATE.get()), 1, 200)
                    .addCriterion("hasmold", has(Main.MOLD_PLATE.get()))
                    .build(consumer, new ResourceLocation(metal.ingot.asItem().getRegistryName() + "_pressing"));
        }
    }



    private static class Metals {
        private final String name;
        private ItemLike ore;
        private TagKey<Item> oreTag;
        private ItemLike block;
        private TagKey<Item> blockTag;
        private final ItemLike ingot;
        private final TagKey<Item> ingotTag;
        private ItemLike nugget;
        private TagKey<Item> nuggetTag;
        private ItemLike dust;
        private TagKey<Item> dustTag;
        private ItemLike plate;
        private TagKey<Item> plateTag;
        private ItemLike gear;
        private TagKey<Item> gearTag;
        private TagKey<Item> chunksTag;

        public Metals(String name, ItemLike ingot, TagKey<Item> ingotTag) {
            this.name = name;
            this.ingot = ingot;
            this.ingotTag = ingotTag;
        }

        public Metals ore(ItemLike item, TagKey<Item> tag) {
            this.ore = item;
            this.oreTag = tag;
            return this;
        }

        public Metals block(ItemLike item, TagKey<Item> tag) {
            this.block = item;
            this.blockTag = tag;
            return this;
        }

        public Metals gear(ItemLike item, TagKey<Item> tag) {
            this.gear = item;
            this.gearTag = tag;
            return this;
        }

        public Metals nugget(ItemLike item, TagKey<Item> tag) {
            this.nugget = item;
            this.nuggetTag = tag;
            return this;
        }

        public Metals dust(ItemLike item, TagKey<Item> tag) {
            this.dust = item;
            this.dustTag = tag;
            return this;
        }

        public Metals chunks(TagKey<Item> tag) {
            this.chunksTag = tag;
            return this;
        }

        public Metals plates(ItemLike item, TagKey<Item> tag) {
            this.plate = item;
            this.plateTag = tag;
            return this;
        }
    }

    protected void createHammerRecipes(Consumer<FinishedRecipe> consumer, Block blockInput, Block blockOutput, String id) {
        CrushingRecipeBuilder.builder().input(blockInput).addDrop(blockOutput).build(consumer, ResourceUtils.ModResourceLocation("sledgehammer/"+id));
    }

    protected void createHammerRecipes(Consumer<FinishedRecipe> consumer, Block blockInput, Item blockOutput, String id) {
        CrushingRecipeBuilder.builder().input(blockInput).addDrop(blockOutput).build(consumer, ResourceUtils.ModResourceLocation("sledgehammer/"+id));
    }

    protected void createHammerRecipes(Consumer<FinishedRecipe> consumer, TagKey<Item> blockInput, Item out, int count, String id) {
        CrushingRecipeBuilder.builder().input(blockInput).addDrop(out, count).build(consumer, ResourceUtils.ModResourceLocation("sledgehammer/"+id));
    }
}
