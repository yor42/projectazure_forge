package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.data.recipebuilder.AlloyingRecipeBuilder;
import com.yor42.projectazure.data.recipebuilder.CrystalizingRecipeBuilder;
import com.yor42.projectazure.data.recipebuilder.PressingRecipeBuilder;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerFluids;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        BuildMetalRecipe(consumer, 0.5F, new Metals("copper", registerItems.INGOT_COPPER.get(), ModTags.Items.INGOT_COPPER).ore(registerBlocks.COPPER_ORE.get().asItem(), ModTags.Items.ORES_COPPER).dust(registerItems.DUST_COPPER.get(), ModTags.Items.DUST_COPPER).plates(registerItems.PLATE_COPPER.get(), ModTags.Items.PLATE_COPPER).gear(registerItems.GEAR_COPPER.get(), ModTags.Items.GEAR_COPPER).nugget(registerItems.NUGGET_COPPER.get(), ModTags.Items.NUGGET_COPPER));
        BuildMetalRecipe(consumer, 0.5F, new Metals("tin", registerItems.INGOT_TIN.get(), ModTags.Items.INGOT_TIN).ore(registerBlocks.TIN_ORE.get().asItem(), ModTags.Items.ORES_TIN).dust(registerItems.DUST_TIN.get(), ModTags.Items.DUST_TIN).plates(registerItems.PLATE_TIN.get(), ModTags.Items.PLATE_TIN).gear(registerItems.GEAR_TIN.get(), ModTags.Items.GEAR_TIN).nugget(registerItems.NUGGET_TIN.get(), ModTags.Items.NUGGET_TIN));
        BuildMetalRecipe(consumer, 0.5F, new Metals("lead", registerItems.INGOT_LEAD.get(), ModTags.Items.INGOT_LEAD).ore(registerBlocks.LEAD_ORE.get().asItem(), ModTags.Items.ORES_LEAD).dust(registerItems.DUST_LEAD.get(), ModTags.Items.DUST_LEAD).plates(registerItems.PLATE_LEAD.get(), ModTags.Items.PLATE_LEAD).nugget(registerItems.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD).nugget(registerItems.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD));
        BuildMetalRecipe(consumer, 0.5F, new Metals("bronze", registerItems.INGOT_BRONZE.get(), ModTags.Items.INGOT_BRONZE).dust(registerItems.DUST_BRONZE.get(), ModTags.Items.DUST_BRONZE).plates(registerItems.PLATE_BRONZE.get(), ModTags.Items.PLATE_BRONZE).gear(registerItems.GEAR_BRONZE.get(), ModTags.Items.GEAR_BRONZE).nugget(registerItems.NUGGET_BRONZE.get(), ModTags.Items.NUGGET_BRONZE));
        BuildMetalRecipe(consumer, 0.5F, new Metals("aluminium", registerItems.INGOT_ALUMINIUM.get(), ModTags.Items.INGOT_ALUMINIUM).ore(registerBlocks.BAUXITE_ORE.get().asItem(), ModTags.Items.ORES_ALUMINIUM).dust(registerItems.DUST_ALUMINIUM.get(), ModTags.Items.DUST_ALUMINIUM).plates(registerItems.PLATE_ALUMINIUM.get(), ModTags.Items.PLATE_ALUMINIUM));
        BuildMetalRecipe(consumer, 0.5F, new Metals("steel", registerItems.INGOT_STEEL.get(), ModTags.Items.INGOT_STEEL).dust(registerItems.DUST_STEEL.get(), ModTags.Items.DUST_STEEL).plates(registerItems.PLATE_STEEL.get(), ModTags.Items.PLATE_STEEL).gear(registerItems.GEAR_STEEL.get(), ModTags.Items.GEAR_STEEL).nugget(registerItems.NUGGET_STEEL.get(), ModTags.Items.NUGGET_STEEL));
        BuildMetalRecipe(consumer, 0.5F, new Metals("brass", registerItems.INGOT_BRASS.get(), ModTags.Items.INGOT_BRASS).dust(registerItems.DUST_BRASS.get(), ModTags.Items.DUST_BRASS).plates(registerItems.PLATE_BRASS.get(), ModTags.Items.PLATE_BRASS).nugget(registerItems.NUGGET_BRASS.get(), ModTags.Items.PLATE_BRASS));
        BuildMetalRecipe(consumer, 0.5F, new Metals("zinc", registerItems.INGOT_ZINC.get(), ModTags.Items.INGOT_ZINC).dust(registerItems.DUST_ZINC.get(), ModTags.Items.DUST_ZINC).plates(registerItems.PLATE_ZINC.get(), ModTags.Items.PLATE_ZINC).nugget(registerItems.NUGGET_ZINC.get(), ModTags.Items.NUGGET_ZINC));

        BuildMetalRecipe(consumer, 0.5F, new Metals("iron", Items.IRON_INGOT, Tags.Items.INGOTS_IRON).dust(registerItems.DUST_IRON.get(), ModTags.Items.DUST_IRON).plates(registerItems.PLATE_IRON.get(), ModTags.Items.PLATE_IRON).gear(registerItems.GEAR_IRON.get(), ModTags.Items.GEAR_IRON));


        ShapedRecipeBuilder.shapedRecipe(registerItems.HAMMER_IRON.get())
                .key('I', Tags.Items.INGOTS_IRON)
                .key('S', Items.STICK)
                .patternLine("III")
                .patternLine("ISI")
                .patternLine(" S ")
                .addCriterion("has_stick", hasItem(Items.STICK))
                .addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(registerItems.MORTAR_IRON.get())
                .key('I', Tags.Items.INGOTS_IRON)
                .key('S', Items.STICK)
                .key('C', Items.COBBLESTONE)
                .patternLine("  S")
                .patternLine("CIC")
                .patternLine("CCC")
                .addCriterion("has_stick", hasItem(Items.STICK))
                .addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.STEEL_CUTTER.get())
                .key('I', ModTags.Items.PLATE_STEEL)
                .key('S', Items.STICK)
                .key('N', Items.IRON_NUGGET)
                .patternLine(" I ")
                .patternLine("INS")
                .patternLine(" S ")
                .addCriterion("has_stick", hasItem(Items.STICK))
                .addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerBlocks.MACHINE_FRAME.get())
                .key('I', ModTags.Items.PLATE_STEEL)
                .key('P', registerItems.MECHANICAL_PARTS.get())
                .key('N', Tags.Items.INGOTS_IRON)
                .patternLine("NIN")
                .patternLine("IPI")
                .patternLine("NIN")
                .addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                .addCriterion("has_steel", hasItem(ModTags.Items.PLATE_STEEL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerBlocks.BASIC_REFINERY.get())
                .key('M', registerBlocks.MACHINE_FRAME.get())
                .key('P', registerItems.STEEL_PIPE.get())
                .key('B', Items.BLAST_FURNACE)
                .key('S', Items.SMOOTH_STONE_SLAB)
                .patternLine("MPP")
                .patternLine("MMM")
                .patternLine("BSS")
                .addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                .addCriterion("has_steel", hasItem(ModTags.Items.PLATE_STEEL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerBlocks.ALLOY_FURNACE.get())
                .key('F', Items.FURNACE)
                .key('B', Blocks.BRICKS)
                .key('S', ModTags.Items.PLATE_STEEL)
                .patternLine("SBS")
                .patternLine("BFB")
                .patternLine("SBS")
                .addCriterion("has_furnace", hasItem(Items.FURNACE))
                .addCriterion("has_steel", hasItem(ModTags.Items.PLATE_STEEL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerBlocks.METAL_PRESS.get())
                .key('L', Items.LEVER)
                .key('R', Blocks.REDSTONE_BLOCK)
                .key('P', Items.PISTON)
                .key('S', registerBlocks.MACHINE_FRAME.get())
                .key('B', registerItems.PRIMITIVE_CIRCUIT.get())
                .key('W', registerItems.COPPER_WIRE.get())
                .key('C', registerItems.COPPER_COIL.get())
                .patternLine("WBL")
                .patternLine("PRP")
                .patternLine("CSC")
                .addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                .addCriterion("has_steel", hasItem(ModTags.Items.PLATE_STEEL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerBlocks.RECRUIT_BEACON.get())
                .key('L', registerItems.STEEL_PIPE.get())
                .key('R', ModTags.Items.PLATE_STEEL)
                .key('O', registerItems.ORUNDUM.get())
                .key('S', registerBlocks.MACHINE_FRAME.get())
                .key('B', registerItems.PRIMITIVE_CIRCUIT.get())
                .key('C', registerItems.COPPER_COIL.get())
                .patternLine("  L")
                .patternLine("ORC")
                .patternLine("BSB")
                .addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                .addCriterion("has_steel", hasItem(ModTags.Items.PLATE_STEEL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.DUST_BRONZE.get(), 2)
                .key('C', ModTags.Items.DUST_COPPER)
                .key('T', ModTags.Items.DUST_TIN)
                .patternLine("CC")
                .patternLine("CT")
                .addCriterion("has_copper", hasItem(ModTags.Items.DUST_COPPER))
                .addCriterion("has_tin", hasItem(ModTags.Items.DUST_TIN))
                .build(consumer, new ResourceLocation("bronze_powder_alloying"));

        ShapedRecipeBuilder.shapedRecipe(registerItems.STEEL_PIPE.get())
                .key('S', ModTags.Items.PLATE_STEEL)
                .patternLine(" S ")
                .patternLine("S S")
                .patternLine(" S ")
                .addCriterion("has_steel", hasItem(ModTags.Items.INGOT_STEEL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.EQUIPMENT_GUN_127MM.get())
                .key('S', ModTags.Items.PLATE_STEEL)
                .key('B', registerItems.STEEL_PIPE.get())
                .key('P', registerItems.MECHANICAL_PARTS.get())
                .key('M', registerItems.BASIC_MOTOR.get())
                .key('E', registerItems.PRIMITIVE_CIRCUIT.get())
                .patternLine("PSS")
                .patternLine("BPS")
                .patternLine("EME")
                .addCriterion("has_circuit", hasItem(registerItems.PRIMITIVE_CIRCUIT.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(registerItems.COPPER_WIRE.get(), 3)
                .addIngredient(ModTags.Items.CUTTER)
                .addIngredient(ModTags.Items.PLATE_COPPER)
                .addCriterion("has_copper_plate", hasItem(ModTags.Items.PLATE_COPPER))
                .addCriterion("has_cutter", hasItem(ModTags.Items.CUTTER))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.COPPER_COIL.get(), 2)
                .key('W', registerItems.COPPER_WIRE.get())
                .key('B', registerItems.IRON_PIPE.get())
                .patternLine("WBW")
                .patternLine("WBW")
                .patternLine("WBW")
                .addCriterion("has_wire", hasItem(registerItems.COPPER_WIRE.get()))
                .addCriterion("has_pipe", hasItem(registerItems.IRON_PIPE.get()))
                .build(consumer);

        AlloyingRecipeBuilder.addRecipe(Ingredient.fromTag(ModTags.Items.INGOT_COPPER), 3,Ingredient.fromTag(ModTags.Items.INGOT_TIN), 1, registerItems.INGOT_BRONZE.get(), 4, 300)
                .addCriterion("hastin", hasItem(ModTags.Items.INGOT_TIN))
                .addCriterion("hascopper", hasItem(ModTags.Items.INGOT_COPPER))
                .build(consumer);

        AlloyingRecipeBuilder.addRecipe(Ingredient.fromTag(ModTags.Items.INGOT_COPPER), 2,Ingredient.fromTag(ModTags.Items.INGOT_ZINC), 1, registerItems.INGOT_BRASS.get(), 3, 300)
                .addCriterion("haszinc", hasItem(ModTags.Items.INGOT_ZINC))
                .addCriterion("hascopper", hasItem(ModTags.Items.INGOT_COPPER))
                .build(consumer);

        PressingRecipeBuilder.addRecipe(registerItems.TREE_SAP.get(), Ingredient.fromTag(ItemTags.LOGS), Ingredient.fromItems(registerItems.MOLD_EXTRACTION.get()), 1, 200)
                .addCriterion("hasmold", hasItem(registerItems.MOLD_EXTRACTION.get()))
                .build(consumer, new ResourceLocation("iron_plate_pressing"));

        CrystalizingRecipeBuilder.addRecipe(registerItems.ORIGINIUM_PRIME.get(), Ingredient.fromItems(registerItems.ORIGINIUM_SEED.get()), registerFluids.ORIGINIUM_SOLUTION_SOURCE, 3000)
                .addCriterion("hasseed", hasItem(registerItems.ORIGINIUM_SEED.get()))
                .build(consumer, new ResourceLocation("originium_crystalizing"));

        ShapedRecipeBuilder.shapedRecipe(registerItems.TREE_SAP.get(),2)
                .key('L', ItemTags.LOGS)
                .patternLine("LLL")
                .patternLine("LLL")
                .patternLine("LLL")
                .addCriterion("has_log", hasItem(ItemTags.LOGS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerBlocks.REENFORCEDCONCRETE.get().asItem(),2)
                .key('G', Blocks.GRAY_CONCRETE_POWDER)
                .key('S', ModTags.Items.PLATE_STEEL)
                .patternLine("SGS")
                .patternLine("GSG")
                .patternLine("SGS")
                .addCriterion("has_concrete_powder", hasItem(Blocks.GRAY_CONCRETE_POWDER))
                .build(consumer);


        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.TREE_SAP), registerItems.PLATE_POLYMER.get(), 0.5F, 200)
                .addCriterion("has_item", hasItem(ModTags.Items.TREE_SAP))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.CAPACITOR_PRIMITIVE.get(), 1)
                .key('A', registerItems.PLATE_ALUMINIUM.get())
                .key('P', Items.PAPER)
                .key('C', registerItems.COPPER_WIRE.get())
                .patternLine("APA")
                .patternLine("APA")
                .patternLine("C C")
                .addCriterion("has_wire", hasItem(registerItems.COPPER_WIRE.get()))
                .addCriterion("has_aluminium", hasItem(ModTags.Items.PLATE_ALUMINIUM))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.RESISTOR_PRIMITIVE.get(), 1)
                .key('C', ModTags.Items.DUST_COAL)
                .key('D', registerItems.DUST_IRON.get())
                .key('P', Items.PAPER)
                .key('W', registerItems.COPPER_WIRE.get())
                .patternLine(" P ")
                .patternLine("WCW")
                .patternLine(" D ")
                .addCriterion("has_wire", hasItem(registerItems.COPPER_WIRE.get()))
                .addCriterion("has_carbon", hasItem(ModTags.Items.DUST_COAL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.BASIC_MOTOR.get(), 1)
                .key('C', registerItems.COPPER_COIL.get())
                .key('D', Items.IRON_INGOT)
                .key('P', registerItems.PLATE_IRON.get())
                .key('W', registerItems.COPPER_WIRE.get())
                .patternLine(" D ")
                .patternLine("PCP")
                .patternLine("PWP")
                .addCriterion("has_coil", hasItem(registerItems.COPPER_COIL.get()))
                .addCriterion("has_wire", hasItem(registerItems.COPPER_WIRE.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.PRIMITIVE_CIRCUIT.get(), 1)
                .key('C', ModTags.Items.PLATE_COPPER)
                .key('P', registerItems.PLATE_POLYMER.get())
                .key('I', ModTags.Items.PLATE_IRON)
                .key('L', Items.REDSTONE)
                .key('R', registerItems.RESISTOR_PRIMITIVE.get())
                .key('T', registerItems.CAPACITOR_PRIMITIVE.get())
                .patternLine(" I ")
                .patternLine("RLT")
                .patternLine("CPC")
                .addCriterion("has_plate", hasItem(ModTags.Items.PLATE_COPPER))
                .addCriterion("has_polymer", hasItem(registerItems.PLATE_POLYMER.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.MECHANICAL_PARTS.get(), 2)
                .key('G', ModTags.Items.GEAR_STEEL)
                .key('P', ModTags.Items.PLATE_STEEL)
                .key('I', Tags.Items.INGOTS_IRON)
                .patternLine("GPG")
                .patternLine("PIP")
                .patternLine("GPG")
                .addCriterion("has_gear", hasItem(ModTags.Items.GEAR_STEEL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.ORUNDUM.get(), 4)
                .key('C', ModTags.Items.WIRE_COPPER)
                .key('P', ModTags.Items.PLATE_STEEL)
                .key('O', ModTags.Items.ORIGINITE)
                .key('R', ModTags.Items.CIRCUITS_BASIC)
                .patternLine("CPC")
                .patternLine("ROR")
                .patternLine("CPC")
                .addCriterion("has_originite", hasItem(ModTags.Items.ORIGINITE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.HEADHUNTING_PCB.get())
                .key('P', Items.ENDER_PEARL)
                .key('E', Items.ENDER_EYE)
                .key('R', ModTags.Items.CIRCUITS_BASIC)
                .key('O', registerItems.ORUNDUM.get())
                .patternLine("EP")
                .patternLine("OR")
                .addCriterion("has_orundum", hasItem(registerItems.ORUNDUM.get()))
                .build(consumer);


        ShapedRecipeBuilder.shapedRecipe(registerItems.BANDAGE_ROLL.get(), 4)
                .key('W', ItemTags.WOOL)
                .patternLine("WWW")
                .patternLine("W W")
                .patternLine("WWW")
                .addCriterion("has_wool", hasItem(ItemTags.WOOL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.ADVANCED_CIRCUIT.get(), 2)
                .key('C', ModTags.Items.CIRCUITS_BASIC)
                .key('O', registerItems.ORUNDUM.get())
                .key('R', registerItems.RESISTOR_PRIMITIVE.get())
                .key('T', registerItems.CAPACITOR_PRIMITIVE.get())
                .key('K', registerItems.PLATE_POLYMER.get())
                .patternLine("ROT")
                .patternLine("CKC")
                .addCriterion("has_orundum", hasItem(registerItems.ORUNDUM.get()))
                .build(consumer);


        ShapedRecipeBuilder.shapedRecipe(registerBlocks.DRYDOCKCONTROLLER.get(), 1)
                .key('C', ModTags.Items.CIRCUITS_ADVANCED)
                .key('F', registerBlocks.MACHINE_FRAME.get().asItem())
                .key('A', ModTags.Items.GEAR_STEEL)
                .key('L', Items.REDSTONE_LAMP)
                .key('M', registerItems.BASIC_MOTOR.get())
                .key('O', registerItems.ORUNDUM.get())
                .patternLine("COC")
                .patternLine("LFL")
                .patternLine("MAM")
                .addCriterion("has_orundum", hasItem(registerItems.ORUNDUM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.ORIGINIUM_PRIME.get(), 1)
                .key('N', ModTags.Items.ORIGINITE)
                .patternLine("NNN")
                .patternLine("NNN")
                .patternLine("NNN")
                .addCriterion("has_originite", hasItem(ModTags.Items.ORIGINITE))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(registerItems.ORIGINITE.get(), 9)
                .addIngredient(ModTags.Items.ORIGINIUM_PRIME).addCriterion("hasoriginium", hasItem(registerItems.ORIGINIUM_PRIME.get()));

        ShapedRecipeBuilder.shapedRecipe(registerItems.WISDOM_CUBE.get(), 2)
                .key('D', Tags.Items.GEMS_DIAMOND)
                .key('Q', Tags.Items.GEMS_QUARTZ)
                .key('C', ModTags.Items.CIRCUITS_BASIC)
                .key('L', Tags.Items.GEMS_LAPIS)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .patternLine("QCQ")
                .patternLine("LDR")
                .patternLine("QCQ")
                .addCriterion("has_diamond", hasItem(Tags.Items.GEMS_DIAMOND))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.AMMO_5_56.get(), 2)
                .key('B', ModTags.Items.INGOT_BRASS)
                .key('L', ModTags.Items.INGOT_LEAD)
                .key('G', Tags.Items.GUNPOWDER)
                .patternLine(" L ")
                .patternLine("BGB")
                .patternLine("BGB")
                .addCriterion("has_breass", hasItem(ModTags.Items.INGOT_BRASS))
                .addCriterion("has_lead", hasItem(ModTags.Items.INGOT_LEAD))
                .addCriterion("has_gunpowder", hasItem(Tags.Items.GUNPOWDER))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.STEEL_RIFLE_FRAME.get(), 1)
                .key('P', ModTags.Items.PLATE_STEEL)
                .key('I', ModTags.Items.INGOT_STEEL)
                .key('G', ModTags.Items.GEAR_STEEL)
                .key('M', registerItems.MECHANICAL_PARTS.get())
                .patternLine("PPP")
                .patternLine("MIM")
                .patternLine("PPG")
                .addCriterion("has_parts", hasItem(registerItems.MECHANICAL_PARTS.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.PISTOL_GRIP.get(), 1)
                .key('P', ModTags.Items.PLATE_STEEL)
                .key('I', ModTags.Items.INGOT_STEEL)
                .patternLine("IP ")
                .patternLine("PIP")
                .patternLine(" PP")
                .addCriterion("has_parts", hasItem(registerItems.MECHANICAL_PARTS.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.ABYDOS_550.get(), 1)
                .key('P', ModTags.Items.PLATE_STEEL)
                .key('I', ModTags.Items.INGOT_STEEL)
                .key('B', registerItems.STEEL_RIFLE_FRAME.get())
                .key('M', registerItems.MAGAZINE_5_56.get())
                .key('R', registerItems.STEEL_PIPE.get())
                .key('G', registerItems.PISTOL_GRIP.get())
                .key('T', registerItems.HAMMER_IRON.get())
                .patternLine("PPP")
                .patternLine("RBI")
                .patternLine("TMG")
                .addCriterion("has_parts", hasItem(registerItems.STEEL_RIFLE_FRAME.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.AMMO_GENERIC.get(), 2)
                .key('B', ModTags.Items.PLATE_BRASS)
                .key('L', ModTags.Items.INGOT_LEAD)
                .key('G', Tags.Items.GUNPOWDER)
                .patternLine(" L ")
                .patternLine("BGB")
                .patternLine("BGB")
                .addCriterion("has_breass", hasItem(ModTags.Items.PLATE_BRASS))
                .addCriterion("has_lead", hasItem(ModTags.Items.INGOT_LEAD))
                .addCriterion("has_gunpowder", hasItem(Tags.Items.GUNPOWDER))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.AMMO_TORPEDO.get(), 1)
                .key('A', ModTags.Items.INGOT_ALUMINIUM)
                .key('T', Blocks.TNT.asItem())
                .key('C', ModTags.Items.CIRCUITS_BASIC)
                .key('S', ModTags.Items.PLATE_STEEL)
                .patternLine(" AT")
                .patternLine("ACA")
                .patternLine("SA ")
                .addCriterion("has_aluminium", hasItem(ModTags.Items.INGOT_ALUMINIUM))
                .addCriterion("has_circuit", hasItem(ModTags.Items.CIRCUITS_BASIC))
                .addCriterion("has_tnt", hasItem(Blocks.TNT.asItem()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.DISC_FRIDAYNIGHT.get(), 1)
                .key('S', ModTags.Items.PLATE_IRON)
                .key('D', Tags.Items.DYES_GREEN)
                .patternLine(" S ")
                .patternLine("SDS")
                .patternLine(" S ")
                .addCriterion("has_plate", hasItem(ModTags.Items.PLATE_IRON))
                .addCriterion("has_dye", hasItem(Tags.Items.DYES_GREEN))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerBlocks.REENFORCED_PLANK.get(), 4)
                .key('P', ItemTags.PLANKS)
                .key('S', ModTags.Items.INGOT_STEEL)
                .patternLine("SPS")
                .patternLine("PSP")
                .patternLine("SPS")
                .addCriterion("has_steel", hasItem(ModTags.Items.INGOT_STEEL))
                .addCriterion("has_plank", hasItem(ItemTags.PLANKS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.DISC_BRAINPOWER.get(), 1)
                .key('S', ModTags.Items.PLATE_IRON)
                .key('D', Tags.Items.DYES_BLACK)
                .patternLine(" S ")
                .patternLine("SDS")
                .patternLine(" S ")
                .addCriterion("has_plate", hasItem(ModTags.Items.PLATE_IRON))
                .addCriterion("has_dye", hasItem(Tags.Items.DYES_BLACK))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.DISC_RICKROLL.get(), 1)
                .key('S', ModTags.Items.PLATE_IRON)
                .key('D', Tags.Items.DYES_GRAY)
                .patternLine(" S ")
                .patternLine("SDS")
                .patternLine(" S ")
                .addCriterion("has_plate", hasItem(ModTags.Items.PLATE_IRON))
                .addCriterion("has_dye", hasItem(Tags.Items.DYES_GRAY))
                .build(consumer);

        CustomRecipeBuilder.customRecipe((SpecialRecipeSerializer<?>) registerRecipes.Serializers.RELOADING.get()).build(consumer, "reloading_items");
        CustomRecipeBuilder.customRecipe((SpecialRecipeSerializer<?>) registerRecipes.Serializers.REPAIRING.get()).build(consumer, "repairing_items");

        ShapedRecipeBuilder.shapedRecipe(registerItems.DD_DEFAULT_RIGGING.get(), 1)
                .key('A', ModTags.Items.PLATE_ALUMINIUM)
                .key('S', ModTags.Items.INGOT_STEEL)
                .key('G', ModTags.Items.GEAR_STEEL)
                .key('P', registerItems.MECHANICAL_PARTS.get())
                .key('C', ModTags.Items.CIRCUITS_ADVANCED)
                .key('M', registerItems.BASIC_MOTOR.get())
                .key('W', registerItems.WISDOM_CUBE.get())
                .patternLine("CSP")
                .patternLine("AWG")
                .patternLine(" MA")
                .addCriterion("has_cube", hasItem(registerItems.WISDOM_CUBE.get()))
                .addCriterion("has_motor", hasItem(registerItems.BASIC_MOTOR.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.BB_DEFAULT_RIGGING.get(), 1)
                .key('A', ModTags.Items.PLATE_ALUMINIUM)
                .key('S', ModTags.Items.PLATE_STEEL)
                .key('G', ModTags.Items.GEAR_STEEL)
                .key('P', registerItems.MECHANICAL_PARTS.get())
                .key('C', ModTags.Items.CIRCUITS_ADVANCED)
                .key('M', registerItems.BASIC_MOTOR.get())
                .key('D', registerItems.DD_DEFAULT_RIGGING.get())
                .patternLine("SMS")
                .patternLine("PDG")
                .patternLine("ACA")
                .addCriterion("has_dd_rigging", hasItem(registerItems.DD_DEFAULT_RIGGING.get()))
                .addCriterion("has_motor", hasItem(registerItems.BASIC_MOTOR.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.CV_DEFAULT_RIGGING.get(), 1)
                .key('W', registerBlocks.REENFORCED_PLANK.get().asItem())
                .key('M', registerItems.BASIC_MOTOR.get())
                .key('D', registerItems.DD_DEFAULT_RIGGING.get())
                .key('P', registerItems.MECHANICAL_PARTS.get())
                .key('C', ModTags.Items.CIRCUITS_ADVANCED)
                .key('S', ModTags.Items.PLATE_STEEL)
                .key('I', ModTags.Items.INGOT_STEEL)
                .patternLine("SSW")
                .patternLine("IDW")
                .patternLine("PCM")
                .addCriterion("has_dd_rigging", hasItem(registerItems.DD_DEFAULT_RIGGING.get()))
                .addCriterion("has_motor", hasItem(registerItems.BASIC_MOTOR.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(registerItems.EQUIPMENT_TORPEDO_533MM.get(), 1)
                .key('A', ModTags.Items.PLATE_ALUMINIUM)
                .key('S', registerItems.STEEL_PIPE.get())
                .key('G', ModTags.Items.GEAR_STEEL)
                .key('P', registerItems.MECHANICAL_PARTS.get())
                .key('C', ModTags.Items.CIRCUITS_ADVANCED)
                .key('M', registerItems.BASIC_MOTOR.get())
                .patternLine("ACA")
                .patternLine("SSS")
                .patternLine("GMP")
                .addCriterion("has_barrel", hasItem(registerItems.STEEL_PIPE.get()))
                .addCriterion("has_part", hasItem(registerItems.MECHANICAL_PARTS.get()))
                .addCriterion("has_motor", hasItem(registerItems.BASIC_MOTOR.get()))
                .build(consumer);



    }

    private void BuildMetalRecipe(Consumer<IFinishedRecipe> consumer, float smeltingXp, Metals metal) {
        if (metal.ore != null) {
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(metal.oreTag), metal.ingot, smeltingXp, 100)
                    .addCriterion("has_item", hasItem(metal.oreTag))
                    .build(consumer, ResourceUtils.ModResourceLocation(metal.name + "_ore_blasting"));
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(metal.oreTag), metal.ingot, smeltingXp, 200)
                    .addCriterion("has_item", hasItem(metal.oreTag))
                    .build(consumer);
        }

        InventoryChangeTrigger.Instance hasIngot = hasItem(metal.ingotTag);
        InventoryChangeTrigger.Instance hasPlate = hasItem(metal.plateTag);

        if (metal.block != null) {
            ShapelessRecipeBuilder.shapelessRecipe(metal.ingot, 9)
                    .addIngredient(metal.blockTag)
                    .addCriterion("has_item", hasIngot)
                    .build(consumer, new ResourceLocation(metal.ingot.asItem().getRegistryName() + "_from_block"));
            ShapedRecipeBuilder.shapedRecipe(metal.block)
                    .key('#', metal.ingotTag)
                    .patternLine("###")
                    .patternLine("###")
                    .patternLine("###")
                    .addCriterion("has_item", hasIngot)
                    .build(consumer);
        }
        if (metal.nugget != null) {
            ShapelessRecipeBuilder.shapelessRecipe(metal.nugget, 9)
                    .addIngredient(metal.ingotTag)
                    .addCriterion("has_item", hasIngot)
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(metal.ingot)
                    .key('#', metal.nuggetTag)
                    .patternLine("###")
                    .patternLine("###")
                    .patternLine("###")
                    .addCriterion("has_item", hasIngot)
                    .build(consumer, new ResourceLocation(metal.ingot.asItem().getRegistryName() + "_from_nuggets"));
        }
        if (metal.dustTag != null) {
            Ingredient dustOrChunks = metal.chunksTag != null
                    ? Ingredient.fromItemListStream(Stream.of(new Ingredient.TagList(metal.chunksTag), new Ingredient.TagList(metal.dustTag)))
                    : Ingredient.fromTag(metal.dustTag);
            CookingRecipeBuilder.blastingRecipe(dustOrChunks, metal.ingot, smeltingXp, 100)
                    .addCriterion("has_item", hasIngot)
                    .build(consumer, ResourceUtils.ModResourceLocation(metal.name + "_dust_blasting"));
            CookingRecipeBuilder.smeltingRecipe(dustOrChunks, metal.ingot, smeltingXp, 200)
                    .addCriterion("has_item", hasIngot)
                    .build(consumer, ResourceUtils.ModResourceLocation(metal.name + "_dust_smelting"));
        }
        if (metal.dust != null) {
            ShapelessRecipeBuilder.shapelessRecipe(metal.dust, 1)
                    .addIngredient(ModTags.Items.MORTAR)
                    .addIngredient(metal.ingotTag)
                    .addCriterion("has_item", hasIngot)
                    .build(consumer);
        }

        if (metal.gear != null) {
            ShapedRecipeBuilder.shapedRecipe(metal.gear)
                    .key('#', metal.plateTag)
                    .key('C', ModTags.Items.CUTTER)
                    .patternLine(" # ")
                    .patternLine("#C#")
                    .patternLine(" # ")
                    .addCriterion("has_plate", hasPlate)
                    .build(consumer);
        }

        if (metal.plate != null) {
            ShapelessRecipeBuilder.shapelessRecipe(metal.plate, 1)
                    .addIngredient(ModTags.Items.HAMMER)
                    .addIngredient(metal.ingotTag)
                    .addCriterion("has_item", hasIngot)
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(metal.plate)
                    .key('#', metal.ingotTag)
                    .patternLine("###")
                    .addCriterion("has_item", hasIngot)
                    .build(consumer, new ResourceLocation(metal.ingot.asItem().getRegistryName() + "_without_hammer"));

            PressingRecipeBuilder.addRecipe(metal.plate, Ingredient.fromTag(metal.ingotTag), Ingredient.fromItems(registerItems.MOLD_PLATE.get()), 1, 200)
                    .addCriterion("hasmold", hasItem(registerItems.MOLD_PLATE.get()))
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
}
