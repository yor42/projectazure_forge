package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.data.recipebuilder.AlloyingRecipeBuilder;
import com.yor42.projectazure.data.recipebuilder.CrushingRecipeBuilder;
import com.yor42.projectazure.data.recipebuilder.CrystalizingRecipeBuilder;
import com.yor42.projectazure.data.recipebuilder.PressingRecipeBuilder;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerFluids;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerRecipes;
import mekanism.api.MekanismAPI;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        BuildMetalRecipe(consumer, 0.5F, new Metals("copper", registerItems.INGOT_COPPER.get(), ModTags.Items.INGOT_COPPER).ore(registerBlocks.COPPER_ORE.get().asItem(), ModTags.Items.ORES_COPPER).dust(registerItems.DUST_COPPER.get(), ModTags.Items.DUST_COPPER).plates(registerItems.PLATE_COPPER.get(), ModTags.Items.PLATE_COPPER).gear(registerItems.GEAR_COPPER.get(), ModTags.Items.GEAR_COPPER).nugget(registerItems.NUGGET_COPPER.get(), ModTags.Items.NUGGET_COPPER));
        BuildMetalRecipe(consumer, 0.5F, new Metals("tin", registerItems.INGOT_TIN.get(), ModTags.Items.INGOT_TIN).ore(registerBlocks.TIN_ORE.get().asItem(), ModTags.Items.ORES_TIN).dust(registerItems.DUST_TIN.get(), ModTags.Items.DUST_TIN).plates(registerItems.PLATE_TIN.get(), ModTags.Items.PLATE_TIN).gear(registerItems.GEAR_TIN.get(), ModTags.Items.GEAR_TIN).nugget(registerItems.NUGGET_TIN.get(), ModTags.Items.NUGGET_TIN));
        BuildMetalRecipe(consumer, 0.5F, new Metals("lead", registerItems.INGOT_LEAD.get(), ModTags.Items.INGOT_LEAD).ore(registerBlocks.LEAD_ORE.get().asItem(), ModTags.Items.ORES_LEAD).dust(registerItems.DUST_LEAD.get(), ModTags.Items.DUST_LEAD).plates(registerItems.PLATE_LEAD.get(), ModTags.Items.PLATE_LEAD).nugget(registerItems.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD).nugget(registerItems.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD));
        BuildMetalRecipe(consumer, 0.5F, new Metals("bronze", registerItems.INGOT_BRONZE.get(), ModTags.Items.INGOT_BRONZE).dust(registerItems.DUST_BRONZE.get(), ModTags.Items.DUST_BRONZE).plates(registerItems.PLATE_BRONZE.get(), ModTags.Items.PLATE_BRONZE).gear(registerItems.GEAR_BRONZE.get(), ModTags.Items.GEAR_BRONZE).nugget(registerItems.NUGGET_BRONZE.get(), ModTags.Items.NUGGET_BRONZE));
        BuildMetalRecipe(consumer, 0.5F, new Metals("aluminium", registerItems.INGOT_ALUMINIUM.get(), ModTags.Items.INGOT_ALUMINIUM).ore(registerBlocks.BAUXITE_ORE.get().asItem(), ModTags.Items.ORES_ALUMINIUM).dust(registerItems.DUST_ALUMINIUM.get(), ModTags.Items.DUST_ALUMINIUM).plates(registerItems.PLATE_ALUMINIUM.get(), ModTags.Items.PLATE_ALUMINIUM));
        BuildMetalRecipe(consumer, 0.5F, new Metals("steel", registerItems.INGOT_STEEL.get(), ModTags.Items.INGOT_STEEL).dust(registerItems.DUST_STEEL.get(), ModTags.Items.DUST_STEEL).plates(registerItems.PLATE_STEEL.get(), ModTags.Items.PLATE_STEEL).gear(registerItems.GEAR_STEEL.get(), ModTags.Items.GEAR_STEEL).nugget(registerItems.NUGGET_STEEL.get(), ModTags.Items.NUGGET_STEEL));
        BuildMetalRecipe(consumer, 0.5F, new Metals("brass", registerItems.INGOT_BRASS.get(), ModTags.Items.INGOT_BRASS).dust(registerItems.DUST_BRASS.get(), ModTags.Items.DUST_BRASS).plates(registerItems.PLATE_BRASS.get(), ModTags.Items.PLATE_BRASS).nugget(registerItems.NUGGET_BRASS.get(), ModTags.Items.PLATE_BRASS));
        BuildMetalRecipe(consumer, 0.5F, new Metals("zinc", registerItems.INGOT_ZINC.get(), ModTags.Items.INGOT_ZINC).dust(registerItems.DUST_ZINC.get(), ModTags.Items.DUST_ZINC).plates(registerItems.PLATE_ZINC.get(), ModTags.Items.PLATE_ZINC).nugget(registerItems.NUGGET_ZINC.get(), ModTags.Items.NUGGET_ZINC));

        BuildMetalRecipe(consumer, 0.5F, new Metals("iron", Items.IRON_INGOT, Tags.Items.INGOTS_IRON).dust(registerItems.DUST_IRON.get(), ModTags.Items.DUST_IRON).plates(registerItems.PLATE_IRON.get(), ModTags.Items.PLATE_IRON).gear(registerItems.GEAR_IRON.get(), ModTags.Items.GEAR_IRON));
        BuildMetalRecipe(consumer, 0.5F, new Metals("gold", Items.GOLD_INGOT, Tags.Items.INGOTS_GOLD).dust(registerItems.DUST_GOLD.get(), ModTags.Items.DUST_GOLD).plates(registerItems.PLATE_GOLD.get(), ModTags.Items.PLATE_GOLD).gear(registerItems.GEAR_GOLD.get(), ModTags.Items.GEAR_GOLD));

        ShapedRecipeBuilder.shaped(registerItems.HAMMER_IRON.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.STICK)
                .pattern("III")
                .pattern("ISI")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Items.STICK))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(consumer);
        ShapedRecipeBuilder.shaped(registerItems.MORTAR_IRON.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.STICK)
                .define('C', Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern("CCC")
                .unlockedBy("has_stick", has(Items.STICK))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.STEEL_CUTTER.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .define('S', Items.STICK)
                .define('N', Items.IRON_NUGGET)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Items.STICK))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.MACHINE_FRAME.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .define('P', registerItems.MECHANICAL_PARTS.get())
                .define('N', Tags.Items.INGOTS_IRON)
                .pattern("NIN")
                .pattern("IPI")
                .pattern("NIN")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.BASIC_REFINERY.get())
                .define('M', registerBlocks.MACHINE_FRAME.get())
                .define('P', registerItems.STEEL_PIPE.get())
                .define('B', Items.BLAST_FURNACE)
                .define('S', Items.SMOOTH_STONE_SLAB)
                .pattern("MPP")
                .pattern("MMM")
                .pattern("BSS")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.ALLOY_FURNACE.get())
                .define('F', Items.FURNACE)
                .define('B', Blocks.BRICKS)
                .define('S', ModTags.Items.PLATE_STEEL)
                .pattern("SBS")
                .pattern("BFB")
                .pattern("SBS")
                .unlockedBy("has_furnace", has(Items.FURNACE))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.METAL_PRESS.get())
                .define('L', Items.LEVER)
                .define('R', Blocks.REDSTONE_BLOCK)
                .define('P', Items.PISTON)
                .define('S', registerBlocks.MACHINE_FRAME.get())
                .define('B', registerItems.PRIMITIVE_CIRCUIT.get())
                .define('W', registerItems.COPPER_WIRE.get())
                .define('C', registerItems.COPPER_COIL.get())
                .pattern("WBL")
                .pattern("PRP")
                .pattern("CSC")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.RECRUIT_BEACON.get())
                .define('L', registerItems.STEEL_PIPE.get())
                .define('R', ModTags.Items.PLATE_STEEL)
                .define('O', registerItems.ORUNDUM.get())
                .define('S', registerBlocks.MACHINE_FRAME.get())
                .define('B', registerItems.PRIMITIVE_CIRCUIT.get())
                .define('C', registerItems.COPPER_COIL.get())
                .pattern("  L")
                .pattern("ORC")
                .pattern("BSB")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.DUST_BRONZE.get(), 2)
                .define('C', ModTags.Items.DUST_COPPER)
                .define('T', ModTags.Items.DUST_TIN)
                .pattern("CC")
                .pattern("CT")
                .unlockedBy("has_copper", has(ModTags.Items.DUST_COPPER))
                .unlockedBy("has_tin", has(ModTags.Items.DUST_TIN))
                .save(consumer, new ResourceLocation("bronze_powder_alloying"));

        ShapedRecipeBuilder.shaped(registerItems.STEEL_PIPE.get())
                .define('S', ModTags.Items.PLATE_STEEL)
                .pattern(" S ")
                .pattern("S S")
                .pattern(" S ")
                .unlockedBy("has_steel", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.EQUIPMENT_GUN_127MM.get())
                .define('S', ModTags.Items.PLATE_STEEL)
                .define('B', registerItems.STEEL_PIPE.get())
                .define('P', registerItems.MECHANICAL_PARTS.get())
                .define('M', registerItems.BASIC_MOTOR.get())
                .define('E', registerItems.PRIMITIVE_CIRCUIT.get())
                .pattern("PSS")
                .pattern("BPS")
                .pattern("EME")
                .unlockedBy("has_circuit", has(registerItems.PRIMITIVE_CIRCUIT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(registerItems.COPPER_WIRE.get(), 3)
                .requires(ModTags.Items.CUTTER)
                .requires(ModTags.Items.PLATE_COPPER)
                .unlockedBy("has_copper_plate", has(ModTags.Items.PLATE_COPPER))
                .unlockedBy("has_cutter", has(ModTags.Items.CUTTER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.COPPER_COIL.get(), 2)
                .define('W', registerItems.COPPER_WIRE.get())
                .define('B', registerItems.IRON_PIPE.get())
                .pattern("WBW")
                .pattern("WBW")
                .pattern("WBW")
                .unlockedBy("has_wire", has(registerItems.COPPER_WIRE.get()))
                .unlockedBy("has_pipe", has(registerItems.IRON_PIPE.get()))
                .save(consumer);

        AlloyingRecipeBuilder.addRecipe(Ingredient.of(ModTags.Items.INGOT_COPPER), 3,Ingredient.of(ModTags.Items.INGOT_TIN), 1, registerItems.INGOT_BRONZE.get(), 4, 300)
                .addCriterion("hastin", has(ModTags.Items.INGOT_TIN))
                .addCriterion("hascopper", has(ModTags.Items.INGOT_COPPER))
                .build(consumer);

        AlloyingRecipeBuilder.addRecipe(Ingredient.of(ModTags.Items.INGOT_COPPER), 2,Ingredient.of(ModTags.Items.INGOT_ZINC), 1, registerItems.INGOT_BRASS.get(), 3, 300)
                .addCriterion("haszinc", has(ModTags.Items.INGOT_ZINC))
                .addCriterion("hascopper", has(ModTags.Items.INGOT_COPPER))
                .build(consumer);

        PressingRecipeBuilder.addRecipe(registerItems.TREE_SAP.get(), Ingredient.of(ItemTags.LOGS), Ingredient.of(registerItems.MOLD_EXTRACTION.get()), 1, 200)
                .addCriterion("hasmold", has(registerItems.MOLD_EXTRACTION.get()))
                .build(consumer, new ResourceLocation("iron_plate_pressing"));

        CrystalizingRecipeBuilder.addRecipe(registerItems.ORIGINIUM_PRIME.get(), Ingredient.of(registerItems.ORIGINIUM_SEED.get()), registerFluids.ORIGINIUM_SOLUTION_SOURCE, 3000)
                .addCriterion("hasseed", has(registerItems.ORIGINIUM_SEED.get()))
                .build(consumer, new ResourceLocation("originium_crystalizing"));

        CrystalizingRecipeBuilder.addRecipe(Items.QUARTZ, Ingredient.of(registerItems.NETHER_QUARTZ_SEED.get()), registerFluids.NETHER_QUARTZ_SOLUTION_SOURCE, 400)
                .addCriterion("hasseed", has(registerItems.NETHER_QUARTZ_SEED.get()))
                .build(consumer, new ResourceLocation("quartz_crystalizing"));

        ShapedRecipeBuilder.shaped(registerItems.TREE_SAP.get(),2)
                .define('L', ItemTags.LOGS)
                .pattern("LLL")
                .pattern("LLL")
                .pattern("LLL")
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.REENFORCEDCONCRETE.get().asItem(),2)
                .define('G', Blocks.GRAY_CONCRETE_POWDER)
                .define('S', ModTags.Items.PLATE_STEEL)
                .pattern("SGS")
                .pattern("GSG")
                .pattern("SGS")
                .unlockedBy("has_concrete_powder", has(Blocks.GRAY_CONCRETE_POWDER))
                .save(consumer);


        CookingRecipeBuilder.smelting(Ingredient.of(ModTags.Items.TREE_SAP), registerItems.PLATE_POLYMER.get(), 0.5F, 200)
                .unlockedBy("has_item", has(ModTags.Items.TREE_SAP))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.CAPACITOR_PRIMITIVE.get(), 1)
                .define('A', registerItems.PLATE_ALUMINIUM.get())
                .define('P', Items.PAPER)
                .define('C', registerItems.COPPER_WIRE.get())
                .pattern("APA")
                .pattern("APA")
                .pattern("C C")
                .unlockedBy("has_wire", has(registerItems.COPPER_WIRE.get()))
                .unlockedBy("has_aluminium", has(ModTags.Items.PLATE_ALUMINIUM))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.RESISTOR_PRIMITIVE.get(), 1)
                .define('C', ModTags.Items.DUST_COAL)
                .define('D', registerItems.DUST_IRON.get())
                .define('P', Items.PAPER)
                .define('W', registerItems.COPPER_WIRE.get())
                .pattern(" P ")
                .pattern("WCW")
                .pattern(" D ")
                .unlockedBy("has_wire", has(registerItems.COPPER_WIRE.get()))
                .unlockedBy("has_carbon", has(ModTags.Items.DUST_COAL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.BASIC_MOTOR.get(), 1)
                .define('C', registerItems.COPPER_COIL.get())
                .define('D', Items.IRON_INGOT)
                .define('P', registerItems.PLATE_IRON.get())
                .define('W', registerItems.COPPER_WIRE.get())
                .pattern(" D ")
                .pattern("PCP")
                .pattern("PWP")
                .unlockedBy("has_coil", has(registerItems.COPPER_COIL.get()))
                .unlockedBy("has_wire", has(registerItems.COPPER_WIRE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.PRIMITIVE_CIRCUIT.get(), 1)
                .define('C', ModTags.Items.PLATE_COPPER)
                .define('P', registerItems.PLATE_POLYMER.get())
                .define('I', ModTags.Items.PLATE_IRON)
                .define('L', Items.REDSTONE)
                .define('R', registerItems.RESISTOR_PRIMITIVE.get())
                .define('T', registerItems.CAPACITOR_PRIMITIVE.get())
                .pattern(" I ")
                .pattern("RLT")
                .pattern("CPC")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_COPPER))
                .unlockedBy("has_polymer", has(registerItems.PLATE_POLYMER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.MECHANICAL_PARTS.get(), 2)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('I', Tags.Items.INGOTS_IRON)
                .pattern("GPG")
                .pattern("PIP")
                .pattern("GPG")
                .unlockedBy("has_gear", has(ModTags.Items.GEAR_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.CLAYMORE.get(), 1)
                .define('S', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('P', registerItems.IRON_PIPE.get())
                .pattern("PP")
                .pattern("SS")
                .pattern("SS")
                .unlockedBy("has_gear", has(Tags.Items.STORAGE_BLOCKS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.ORUNDUM.get(), 4)
                .define('C', ModTags.Items.WIRE_COPPER)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('O', ModTags.Items.ORIGINITE)
                .define('R', ModTags.Items.CIRCUITS_BASIC)
                .pattern("CPC")
                .pattern("ROR")
                .pattern("CPC")
                .unlockedBy("has_originite", has(ModTags.Items.ORIGINITE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.HEADHUNTING_PCB.get())
                .define('P', Items.ENDER_PEARL)
                .define('E', Items.ENDER_EYE)
                .define('R', ModTags.Items.CIRCUITS_BASIC)
                .define('O', registerItems.ORUNDUM.get())
                .pattern("EP")
                .pattern("OR")
                .unlockedBy("has_orundum", has(registerItems.ORUNDUM.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(registerItems.BANDAGE_ROLL.get(), 4)
                .define('W', ItemTags.WOOL)
                .pattern("WWW")
                .pattern("W W")
                .pattern("WWW")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.ADVANCED_CIRCUIT.get(), 2)
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('O', registerItems.ORUNDUM.get())
                .define('R', registerItems.RESISTOR_PRIMITIVE.get())
                .define('T', registerItems.CAPACITOR_PRIMITIVE.get())
                .define('K', registerItems.PLATE_POLYMER.get())
                .pattern("ROT")
                .pattern("CKC")
                .unlockedBy("has_orundum", has(registerItems.ORUNDUM.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(registerBlocks.DRYDOCKCONTROLLER.get(), 1)
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('F', registerBlocks.MACHINE_FRAME.get().asItem())
                .define('A', ModTags.Items.GEAR_STEEL)
                .define('L', Items.REDSTONE_LAMP)
                .define('M', registerItems.BASIC_MOTOR.get())
                .define('O', registerItems.ORUNDUM.get())
                .pattern("COC")
                .pattern("LFL")
                .pattern("MAM")
                .unlockedBy("has_orundum", has(registerItems.ORUNDUM.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(registerItems.ORIGINITE.get(), 9)
                .requires(ModTags.Items.ORIGINIUM_PRIME).unlockedBy("hasoriginium", has(registerItems.ORIGINIUM_PRIME.get()));

        ShapedRecipeBuilder.shaped(registerItems.WISDOM_CUBE.get(), 2)
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

        ShapedRecipeBuilder.shaped(registerItems.AMMO_5_56.get(), 2)
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

        ShapedRecipeBuilder.shaped(registerItems.STEEL_RIFLE_FRAME.get(), 1)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('I', ModTags.Items.INGOT_STEEL)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('M', registerItems.MECHANICAL_PARTS.get())
                .pattern("PPP")
                .pattern("MIM")
                .pattern("PPG")
                .unlockedBy("has_parts", has(registerItems.MECHANICAL_PARTS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.COMMANDING_STICK.get(), 1)
                .define('S', Items.STICK)
                .define('B', Tags.Items.DYES_BLACK)
                .pattern("  S")
                .pattern(" S ")
                .pattern("B  ")
                .unlockedBy("has_sticc", has(Items.STICK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.PISTOL_GRIP.get(), 1)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('I', ModTags.Items.INGOT_STEEL)
                .pattern("IP ")
                .pattern("PIP")
                .pattern(" PP")
                .unlockedBy("has_parts", has(registerItems.MECHANICAL_PARTS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.ABYDOS_550.get(), 1)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('I', ModTags.Items.INGOT_STEEL)
                .define('B', registerItems.STEEL_RIFLE_FRAME.get())
                .define('M', registerItems.MAGAZINE_5_56.get())
                .define('R', registerItems.STEEL_PIPE.get())
                .define('G', registerItems.PISTOL_GRIP.get())
                .define('T', registerItems.HAMMER_IRON.get())
                .pattern("PPP")
                .pattern("RBI")
                .pattern("TMG")
                .unlockedBy("has_parts", has(registerItems.STEEL_RIFLE_FRAME.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.AMMO_GENERIC.get(), 2)
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

        ShapedRecipeBuilder.shaped(registerItems.AMMO_TORPEDO.get(), 1)
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

        ShapedRecipeBuilder.shaped(registerItems.DISC_FRIDAYNIGHT.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.DYES_GREEN)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.DYES_GREEN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.REENFORCED_PLANK.get(), 4)
                .define('P', ItemTags.PLANKS)
                .define('S', ModTags.Items.INGOT_STEEL)
                .pattern("SPS")
                .pattern("PSP")
                .pattern("SPS")
                .unlockedBy("has_steel", has(ModTags.Items.INGOT_STEEL))
                .unlockedBy("has_plank", has(ItemTags.PLANKS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.DISC_BRAINPOWER.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.DYES_BLACK)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.DYES_BLACK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.DISC_RICKROLL.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.DYES_GRAY)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.DYES_GRAY))
                .save(consumer);

        CustomRecipeBuilder.special((SpecialRecipeSerializer<?>) registerRecipes.Serializers.RELOADING.get()).save(consumer, "reloading_items");
        CustomRecipeBuilder.special((SpecialRecipeSerializer<?>) registerRecipes.Serializers.REPAIRING.get()).save(consumer, "repairing_items");

        ShapedRecipeBuilder.shaped(registerBlocks.CRYSTAL_GROWTH_CHAMBER.get(), 1)
                .define('S', Blocks.SMOOTH_STONE_SLAB)
                .define('C', Blocks.CAULDRON)
                .define('P', registerItems.STEEL_PIPE.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .pattern("III")
                .pattern("CPC")
                .pattern("SSS")
                .unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.NETHER_QUARTZ_SEED.get(), 4)
                .define('S', ItemTags.SAND)
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .pattern("SQ")
                .pattern("QS")
                .unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.ORIGINIUM_SEED.get(), 4)
                .define('S', ItemTags.SAND)
                .define('O', registerItems.DUST_ORIGINIUM.get())
                .pattern("SO")
                .pattern("OS")
                .unlockedBy("has_originium", has(registerItems.DUST_ORIGINIUM.get()))
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(registerItems.DUST_ORIGINIUM.get(), 3)
                .requires(registerItems.ORIGINITE.get())
                .requires(registerItems.HAMMER_IRON.get())
                .unlockedBy("has_originite", has(registerItems.ORIGINITE.get()))
                .save(consumer, new ResourceLocation("originium_dust_from_originite"));

        ShapelessRecipeBuilder.shapeless(registerItems.DUST_NETHER_QUARTZ.get(), 2)
                .requires(Items.QUARTZ)
                .requires(registerItems.HAMMER_IRON.get())
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .save(consumer, new ResourceLocation("quartz_dust_from_quartz"));

        ShapedRecipeBuilder.shaped(registerItems.DD_DEFAULT_RIGGING.get(), 1)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .define('S', ModTags.Items.INGOT_STEEL)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', registerItems.MECHANICAL_PARTS.get())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('M', registerItems.BASIC_MOTOR.get())
                .define('W', registerItems.WISDOM_CUBE.get())
                .pattern("CSP")
                .pattern("AWG")
                .pattern(" MA")
                .unlockedBy("has_cube", has(registerItems.WISDOM_CUBE.get()))
                .unlockedBy("has_motor", has(registerItems.BASIC_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.BB_DEFAULT_RIGGING.get(), 1)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .define('S', ModTags.Items.PLATE_STEEL)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', registerItems.MECHANICAL_PARTS.get())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('M', registerItems.BASIC_MOTOR.get())
                .define('D', registerItems.DD_DEFAULT_RIGGING.get())
                .pattern("SMS")
                .pattern("PDG")
                .pattern("ACA")
                .unlockedBy("has_dd_rigging", has(registerItems.DD_DEFAULT_RIGGING.get()))
                .unlockedBy("has_motor", has(registerItems.BASIC_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.CV_DEFAULT_RIGGING.get(), 1)
                .define('W', registerBlocks.REENFORCED_PLANK.get().asItem())
                .define('M', registerItems.BASIC_MOTOR.get())
                .define('D', registerItems.DD_DEFAULT_RIGGING.get())
                .define('P', registerItems.MECHANICAL_PARTS.get())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('S', ModTags.Items.PLATE_STEEL)
                .define('I', ModTags.Items.INGOT_STEEL)
                .pattern("SSW")
                .pattern("IDW")
                .pattern("PCM")
                .unlockedBy("has_dd_rigging", has(registerItems.DD_DEFAULT_RIGGING.get()))
                .unlockedBy("has_motor", has(registerItems.BASIC_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.EQUIPMENT_TORPEDO_533MM.get(), 1)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .define('S', registerItems.STEEL_PIPE.get())
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', registerItems.MECHANICAL_PARTS.get())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('M', registerItems.BASIC_MOTOR.get())
                .pattern("ACA")
                .pattern("SSS")
                .pattern("GMP")
                .unlockedBy("has_barrel", has(registerItems.STEEL_PIPE.get()))
                .unlockedBy("has_part", has(registerItems.MECHANICAL_PARTS.get()))
                .unlockedBy("has_motor", has(registerItems.BASIC_MOTOR.get()))
                .save(consumer);
        /*
        Because These weapons are SO OP
        I'm nerfing this harder than TTFAF in guitar hero.
        ...Okay that's maybe an overstatement.
         */
        ShapedRecipeBuilder.shaped(registerItems.SHEATH.get(), 1)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('H', registerItems.HAMMER_IRON.get())
                .define('S', Tags.Items.SANDSTONE)
                .define('R', ModTags.Items.INGOT_STEEL)
                .define('W', registerItems.PLATE_POLYMER.get())
                .define('C', registerItems.STEEL_CUTTER.get())
                .pattern("HSP")
                .pattern("WPS")
                .pattern("RWC")
                .unlockedBy("has_steel", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        createHammerRecipes(consumer, Blocks.STONE, Blocks.COBBLESTONE, "cobblestone");
        createHammerRecipes(consumer, Blocks.COBBLESTONE, Blocks.GRAVEL, "gravel");
        createHammerRecipes(consumer, Blocks.GRAVEL, Blocks.SAND, "sand");
        createHammerRecipes(consumer, ModTags.Items.ORES_COPPER, registerItems.DUST_COPPER.get(), 2, "copper");
        createHammerRecipes(consumer, ModTags.Items.ORES_ALUMINIUM, registerItems.DUST_ALUMINIUM.get(), 2, "aluminium");
        createHammerRecipes(consumer, ModTags.Items.ORES_LEAD, registerItems.DUST_LEAD.get(), 2, "lead");
        createHammerRecipes(consumer, ModTags.Items.ORES_TIN, registerItems.DUST_TIN.get(), 2, "tin");
        createHammerRecipes(consumer, ModTags.Items.ORES_ZINC, registerItems.DUST_ZINC.get(), 2, "zinc");
        createHammerRecipes(consumer, Blocks.IRON_ORE, Blocks.COBBLESTONE, "iron");
        createHammerRecipes(consumer, Tags.Items.ORES_COAL, registerItems.DUST_COAL.get(), 2, "coal");
        createHammerRecipes(consumer, Tags.Items.ORES_GOLD, registerItems.DUST_GOLD.get(), 2, "gold");
    }

    private void BuildMetalRecipe(Consumer<IFinishedRecipe> consumer, float smeltingXp, Metals metal) {
        if (metal.ore != null) {
            CookingRecipeBuilder.blasting(Ingredient.of(metal.oreTag), metal.ingot, smeltingXp, 100)
                    .unlockedBy("has_item", has(metal.oreTag))
                    .save(consumer, ResourceUtils.ModResourceLocation(metal.name + "_ore_blasting"));
            CookingRecipeBuilder.smelting(Ingredient.of(metal.oreTag), metal.ingot, smeltingXp, 200)
                    .unlockedBy("has_item", has(metal.oreTag))
                    .save(consumer);
        }

        InventoryChangeTrigger.Instance hasIngot = has(metal.ingotTag);
        InventoryChangeTrigger.Instance hasPlate = has(metal.plateTag);

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
                    ? Ingredient.fromValues(Stream.of(new Ingredient.TagList(metal.chunksTag), new Ingredient.TagList(metal.dustTag)))
                    : Ingredient.of(metal.dustTag);
            CookingRecipeBuilder.blasting(dustOrChunks, metal.ingot, smeltingXp, 100)
                    .unlockedBy("has_item", hasIngot)
                    .save(consumer, ResourceUtils.ModResourceLocation(metal.name + "_dust_blasting"));
            CookingRecipeBuilder.smelting(dustOrChunks, metal.ingot, smeltingXp, 200)
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

            PressingRecipeBuilder.addRecipe(metal.plate, Ingredient.of(metal.ingotTag), Ingredient.of(registerItems.MOLD_PLATE.get()), 1, 200)
                    .addCriterion("hasmold", has(registerItems.MOLD_PLATE.get()))
                    .build(consumer, new ResourceLocation(metal.ingot.asItem().getRegistryName() + "_pressing"));
        }
    }



    private static class Metals {
        private final String name;
        private IItemProvider ore;
        private ITag<Item> oreTag;
        private IItemProvider block;
        private ITag<Item> blockTag;
        private final IItemProvider ingot;
        private final ITag<Item> ingotTag;
        private IItemProvider nugget;
        private ITag<Item> nuggetTag;
        private IItemProvider dust;
        private ITag<Item> dustTag;
        private IItemProvider plate;
        private ITag<Item> plateTag;
        private IItemProvider gear;
        private ITag<Item> gearTag;
        private ITag<Item> chunksTag;

        public Metals(String name, IItemProvider ingot, ITag<Item> ingotTag) {
            this.name = name;
            this.ingot = ingot;
            this.ingotTag = ingotTag;
        }

        public Metals ore(IItemProvider item, ITag<Item> tag) {
            this.ore = item;
            this.oreTag = tag;
            return this;
        }

        public Metals block(IItemProvider item, ITag<Item> tag) {
            this.block = item;
            this.blockTag = tag;
            return this;
        }

        public Metals gear(IItemProvider item, ITag<Item> tag) {
            this.gear = item;
            this.gearTag = tag;
            return this;
        }

        public Metals nugget(IItemProvider item, ITag<Item> tag) {
            this.nugget = item;
            this.nuggetTag = tag;
            return this;
        }

        public Metals dust(IItemProvider item, ITag<Item> tag) {
            this.dust = item;
            this.dustTag = tag;
            return this;
        }

        public Metals chunks(ITag<Item> tag) {
            this.chunksTag = tag;
            return this;
        }

        public Metals plates(IItemProvider item, ITag<Item> tag) {
            this.plate = item;
            this.plateTag = tag;
            return this;
        }
    }

    protected void createHammerRecipes(Consumer<IFinishedRecipe> consumer, Block blockInput, Block blockOutput, String id) {
        CrushingRecipeBuilder.builder().input(blockInput).addDrop(blockOutput).build(consumer, ResourceUtils.ModResourceLocation("sledgehammer/"+id));
    }

    protected void createHammerRecipes(Consumer<IFinishedRecipe> consumer, Block blockInput, Item blockOutput, String id) {
        CrushingRecipeBuilder.builder().input(blockInput).addDrop(blockOutput).build(consumer, ResourceUtils.ModResourceLocation("sledgehammer/"+id));
    }

    protected void createHammerRecipes(Consumer<IFinishedRecipe> consumer, ITag<Item> blockInput, Item out, int count, String id) {
        CrushingRecipeBuilder.builder().input(blockInput).addDrop(out, count).build(consumer, ResourceUtils.ModResourceLocation("sledgehammer/"+id));
    }
}
