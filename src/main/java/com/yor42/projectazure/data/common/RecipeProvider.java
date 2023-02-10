package com.yor42.projectazure.data.common;

import com.tac.guns.crafting.WorkbenchRecipeBuilder;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.data.recipebuilder.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.OriginiumGeneratorControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.RiftwayControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerFluids;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
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
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        this.generateMetalRecipes(consumer);

        CookingRecipeBuilder.smelting(Ingredient.of(ModTags.Items.TREE_SAP), registerItems.PLATE_POLYMER.get(), 0.5F, 200)
                .unlockedBy("has_item", has(ModTags.Items.TREE_SAP))
                .save(consumer);

        CookingRecipeBuilder.blasting(Ingredient.of(registerBlocks.COBBLED_ORIROCK.get()), registerBlocks.ORIROCK.get(), 0.1F, 100)
                .unlockedBy("has_item", has(registerBlocks.COBBLED_ORIROCK.get()))
                .save(consumer);

        CustomRecipeBuilder.special((SpecialRecipeSerializer<?>) registerRecipes.Serializers.RELOADING.get()).save(consumer, "reloading_items");
        CustomRecipeBuilder.special((SpecialRecipeSerializer<?>) registerRecipes.Serializers.REPAIRING.get()).save(consumer, "repairing_items");
        this.generateAlloyingRecipe(consumer);
        this.generatePressingRecipe(consumer);
        this.generateCrystalizingRecipe(consumer);
        this.generateCrushingRecipe(consumer);
        this.generateGunBenchRecipe(consumer);
        this.generateShapedRecipe(consumer);
        this.generateShapelessRecipe(consumer);

        this.generateChemicalReactionRecipe(consumer);
    }

    private void generateChemicalReactionRecipe(Consumer<IFinishedRecipe> consumer) {
        ChemicalReactorRecipeBuilder.add(registerFluids.KETON_SOURCE_REGISTRY.get(), 100, Ingredient.of(registerItems.PIG_FAT.get()), consumer);
    }

    private void generateGunBenchRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){
        WorkbenchRecipeBuilder.workbenchRecipe(registerItems.CASELESS_4MM.get(),6).addIngredient(registerItems.GUNPOWDER_COMPOUND.get(), 3).addIngredient(registerItems.INGOT_LEAD.get(), 5).build(consumer, Constants.MODID,"gunbench_4mmcaseless_lead");
        WorkbenchRecipeBuilder.workbenchRecipe(registerItems.CASELESS_4MM.get(),6).addIngredient(registerItems.GUNPOWDER_COMPOUND.get(), 3).addIngredient(Items.IRON_INGOT, 5).build(consumer, Constants.MODID,"gunbench_4mmcaseless_iron");

        WorkbenchRecipeBuilder.workbenchRecipe(registerItems.W_GRANADELAUNCHER.get()).addIngredient(ModTags.Items.PLATE_STEEL, 14).addIngredient(registerItems.BASIC_MOTOR.get(), 2).addIngredient(ModTags.Items.CIRCUITS_BASIC).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer, Constants.MODID,"gunbench_granadelauncher");
        WorkbenchRecipeBuilder.workbenchRecipe(registerItems.TYPHOON.get()).addIngredient(ModTags.Items.PLATE_STEEL, 25).addIngredient(ModTags.Items.INGOT_STEEL, 30).addIngredient(ModTags.Items.CIRCUITS_BASIC,4).addIngredient(ModTags.Items.CIRCUITS_ADVANCED, 2).build(consumer, Constants.MODID,"gunbench_typhoon");
        WorkbenchRecipeBuilder.workbenchRecipe(registerItems.WHITEFANG_465.get()).addIngredient(ModTags.Items.PLATE_STEEL, 9).addIngredient(ModTags.Items.INGOT_STEEL, 10).build(consumer, Constants.MODID,"gunbench_whitefang");
        WorkbenchRecipeBuilder.workbenchRecipe(registerItems.SANGVIS_RAILGUN.get()).addIngredient(ModTags.Items.PLATE_STEEL, 35).addIngredient(ModTags.Items.CIRCUITS_ADVANCED, 10).build(consumer, Constants.MODID,"gunbench_cannon");
    }

    private void generateMetalRecipes(@Nonnull Consumer<IFinishedRecipe> consumer){
        BuildMetalRecipe(consumer, 0.5F, new Metals("copper", registerItems.INGOT_COPPER.get(), ModTags.Items.INGOT_COPPER).block(registerBlocks.COPPER_BLOCK.get(), ModTags.Items.BLOCK_COPPER).ore(registerBlocks.COPPER_ORE.get().asItem(), ModTags.Items.ORES_COPPER).dust(registerItems.DUST_COPPER.get(), ModTags.Items.DUST_COPPER).plates(registerItems.PLATE_COPPER.get(), ModTags.Items.PLATE_COPPER).gear(registerItems.GEAR_COPPER.get(), ModTags.Items.GEAR_COPPER).nugget(registerItems.NUGGET_COPPER.get(), ModTags.Items.NUGGET_COPPER));
        BuildMetalRecipe(consumer, 0.5F, new Metals("tin", registerItems.INGOT_TIN.get(), ModTags.Items.INGOT_TIN).block(registerBlocks.TIN_BLOCK.get(), ModTags.Items.BLOCK_TIN).ore(registerBlocks.TIN_ORE.get().asItem(), ModTags.Items.ORES_TIN).dust(registerItems.DUST_TIN.get(), ModTags.Items.DUST_TIN).plates(registerItems.PLATE_TIN.get(), ModTags.Items.PLATE_TIN).gear(registerItems.GEAR_TIN.get(), ModTags.Items.GEAR_TIN).nugget(registerItems.NUGGET_TIN.get(), ModTags.Items.NUGGET_TIN));
        BuildMetalRecipe(consumer, 0.5F, new Metals("lead", registerItems.INGOT_LEAD.get(), ModTags.Items.INGOT_LEAD).block(registerBlocks.LEAD_BLOCK.get(), ModTags.Items.BLOCK_LEAD).ore(registerBlocks.LEAD_ORE.get().asItem(), ModTags.Items.ORES_LEAD).dust(registerItems.DUST_LEAD.get(), ModTags.Items.DUST_LEAD).plates(registerItems.PLATE_LEAD.get(), ModTags.Items.PLATE_LEAD).nugget(registerItems.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD).nugget(registerItems.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD));
        BuildMetalRecipe(consumer, 0.5F, new Metals("bronze", registerItems.INGOT_BRONZE.get(), ModTags.Items.INGOT_BRONZE).block(registerBlocks.BRONZE_BLOCK.get(), ModTags.Items.BLOCK_BRONZE).dust(registerItems.DUST_BRONZE.get(), ModTags.Items.DUST_BRONZE).plates(registerItems.PLATE_BRONZE.get(), ModTags.Items.PLATE_BRONZE).gear(registerItems.GEAR_BRONZE.get(), ModTags.Items.GEAR_BRONZE).nugget(registerItems.NUGGET_BRONZE.get(), ModTags.Items.NUGGET_BRONZE));
        BuildMetalRecipe(consumer, 0.5F, new Metals("aluminium", registerItems.INGOT_ALUMINIUM.get(), ModTags.Items.INGOT_ALUMINIUM).block(registerBlocks.ALUMINIUM_BLOCK.get(), ModTags.Items.BLOCK_ALUMINIUM).ore(registerBlocks.BAUXITE_ORE.get().asItem(), ModTags.Items.ORES_ALUMINIUM).dust(registerItems.DUST_ALUMINIUM.get(), ModTags.Items.DUST_ALUMINIUM).plates(registerItems.PLATE_ALUMINIUM.get(), ModTags.Items.PLATE_ALUMINIUM));
        BuildMetalRecipe(consumer, 0.5F, new Metals("steel", registerItems.INGOT_STEEL.get(), ModTags.Items.INGOT_STEEL).block(registerBlocks.STEEL_BLOCK.get(), ModTags.Items.BLOCK_STEEL).dust(registerItems.DUST_STEEL.get(), ModTags.Items.DUST_STEEL).plates(registerItems.PLATE_STEEL.get(), ModTags.Items.PLATE_STEEL).gear(registerItems.GEAR_STEEL.get(), ModTags.Items.GEAR_STEEL).nugget(registerItems.NUGGET_STEEL.get(), ModTags.Items.NUGGET_STEEL));
        BuildMetalRecipe(consumer, 0.5F, new Metals("brass", registerItems.INGOT_BRASS.get(), ModTags.Items.INGOT_BRASS).block(registerBlocks.BRASS_BLOCK.get(), ModTags.Items.BLOCK_BRASS).dust(registerItems.DUST_BRASS.get(), ModTags.Items.DUST_BRASS).plates(registerItems.PLATE_BRASS.get(), ModTags.Items.PLATE_BRASS).nugget(registerItems.NUGGET_BRASS.get(), ModTags.Items.PLATE_BRASS));
        BuildMetalRecipe(consumer, 0.5F, new Metals("zinc", registerItems.INGOT_ZINC.get(), ModTags.Items.INGOT_ZINC).block(registerBlocks.ZINC_BLOCK.get(), ModTags.Items.BLOCK_ZINC).dust(registerItems.DUST_ZINC.get(), ModTags.Items.DUST_ZINC).plates(registerItems.PLATE_ZINC.get(), ModTags.Items.PLATE_ZINC).nugget(registerItems.NUGGET_ZINC.get(), ModTags.Items.NUGGET_ZINC));
        BuildMetalRecipe(consumer, 0.5F, new Metals("rma70-12", registerItems.INGOT_RMA7012.get(), ModTags.Items.INGOT_RMA7012).block(registerBlocks.RMA_7012_BLOCK.get(), ModTags.Items.BLOCK_RMA7012).ore(registerBlocks.RMA_7012_ORE.get(), ModTags.Items.ORES_RMA7012).dust(registerItems.DUST_RMA7012.get(), ModTags.Items.DUST_RMA7012).plates(registerItems.PLATE_RMA7012.get(), ModTags.Items.PLATE_RMA7012).nugget(registerItems.NUGGET_RMA7012.get(), ModTags.Items.NUGGET_RMA7012));
        BuildMetalRecipe(consumer, 0.5F, new Metals("rma70-24", registerItems.INGOT_RMA7024.get(), ModTags.Items.INGOT_RMA7024).block(registerBlocks.RMA_7024_BLOCK.get(), ModTags.Items.BLOCK_RMA7024).dust(registerItems.DUST_RMA7024.get(), ModTags.Items.DUST_RMA7024).plates(registerItems.PLATE_RMA7024.get(), ModTags.Items.PLATE_RMA7024).nugget(registerItems.NUGGET_RMA7024.get(), ModTags.Items.NUGGET_RMA7024));
        BuildMetalRecipe(consumer, 0.5F, new Metals("d32steel", registerItems.INGOT_D32.get(), ModTags.Items.INGOT_D32).block(registerBlocks.D32_BLOCK.get(), ModTags.Items.BLOCK_D32).dust(registerItems.DUST_D32.get(), ModTags.Items.DUST_D32).plates(registerItems.PLATE_D32.get(), ModTags.Items.PLATE_D32).nugget(registerItems.NUGGET_D32.get(), ModTags.Items.NUGGET_D32));

        BuildMetalRecipe(consumer, 0.5F, new Metals("iron", Items.IRON_INGOT, Tags.Items.INGOTS_IRON).dust(registerItems.DUST_IRON.get(), ModTags.Items.DUST_IRON).plates(registerItems.PLATE_IRON.get(), ModTags.Items.PLATE_IRON).gear(registerItems.GEAR_IRON.get(), ModTags.Items.GEAR_IRON));
        BuildMetalRecipe(consumer, 0.5F, new Metals("gold", Items.GOLD_INGOT, Tags.Items.INGOTS_GOLD).dust(registerItems.DUST_GOLD.get(), ModTags.Items.DUST_GOLD).plates(registerItems.PLATE_GOLD.get(), ModTags.Items.PLATE_GOLD).gear(registerItems.GEAR_GOLD.get(), ModTags.Items.GEAR_GOLD));

    }

    private void generateCrushingRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){
        createHammerRecipes(consumer, Blocks.STONE, Blocks.COBBLESTONE, "cobblestone");
        createHammerRecipes(consumer, Blocks.COBBLESTONE, Blocks.GRAVEL, "gravel");
        createHammerRecipes(consumer, Blocks.GRAVEL, Blocks.SAND, "sand");
        createHammerRecipes(consumer, registerBlocks.COBBLED_ORIROCK.get(), registerItems.DUST_ORIROCK.get(), 4, "orirock_from_cobble");
        createHammerRecipes(consumer, ModTags.Items.ORES_COPPER, registerItems.DUST_COPPER.get(), 2, "copper");
        createHammerRecipes(consumer, ModTags.Items.ORES_ALUMINIUM, registerItems.DUST_ALUMINIUM.get(), 2, "aluminium");
        createHammerRecipes(consumer, ModTags.Items.ORES_LEAD, registerItems.DUST_LEAD.get(), 2, "lead");
        createHammerRecipes(consumer, ModTags.Items.ORES_TIN, registerItems.DUST_TIN.get(), 2, "tin");
        createHammerRecipes(consumer, ModTags.Items.ORES_ORIROCK, registerItems.DUST_ORIROCK.get(), 4, "orirock");
        createHammerRecipes(consumer, Tags.Items.ORES_GOLD, registerItems.DUST_GOLD.get(), 2, "gold");
        createHammerRecipes(consumer, Tags.Items.ORES_IRON, registerItems.DUST_IRON.get(), 2, "iron");
        createHammerRecipes(consumer, Tags.Items.ORES_COAL, registerItems.DUST_COAL.get(), 2, "coal");
    }

    private void generateAlloyingRecipe(@Nonnull Consumer<IFinishedRecipe> consumer) {
        AlloyingRecipeBuilder.AlloyRecipe(registerItems.INGOT_BRONZE.get(), (byte)4)
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER), (byte) 3)
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_TIN), (byte) 1)
                .build(consumer);

        AlloyingRecipeBuilder.AlloyRecipe(registerItems.INGOT_BRASS.get(), (byte)3)
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_ZINC))
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER))
                .build(consumer);
    }
    private void generatePressingRecipe(@Nonnull Consumer<IFinishedRecipe> consumer) {
        PressingRecipeBuilder.addRecipe(registerItems.TREE_SAP.get(), Ingredient.of(ItemTags.LOGS), Ingredient.of(registerItems.MOLD_EXTRACTION.get()), 1, 200)
                .build(consumer, new ResourceLocation("iron_plate_pressing"));
    }

    private void generateCrystalizingRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){
        CrystalizingRecipeBuilder.addRecipe(registerItems.ORIGINIUM_PRIME.get(), Ingredient.of(registerItems.ORIGINIUM_SEED.get()), registerFluids.ORIGINIUM_SOLUTION_SOURCE_REGISTRY.get(), 3000)
                .build(consumer, new ResourceLocation("originium_crystalizing"));

        CrystalizingRecipeBuilder.addRecipe(Items.QUARTZ, Ingredient.of(registerItems.NETHER_QUARTZ_SEED.get()), registerFluids.NETHER_QUARTZ_SOLUTION_SOURCE_REGISTRY.get(), 400)
                .build(consumer, new ResourceLocation("quartz_crystalizing"));
    }

    private void generateShapelessRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){

        ShapelessRecipeBuilder.shapeless(registerItems.COPPER_WIRE.get(), 3)
                .requires(ModTags.Items.CUTTER)
                .requires(ModTags.Items.PLATE_COPPER)
                .unlockedBy("has_copper_plate", has(ModTags.Items.PLATE_COPPER))
                .unlockedBy("has_cutter", has(ModTags.Items.CUTTER))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(registerItems.ORIGINITE.get(), 9)
                .requires(ModTags.Items.ORIGINIUM_PRIME).unlockedBy("hasoriginium", has(registerItems.ORIGINIUM_PRIME.get()));

        ShapelessRecipeBuilder.shapeless(registerItems.DISC_SANDROLL.get(),1).requires(registerItems.DISC_SANDSTORM.get()).requires(registerItems.DISC_RICKROLL.get())
                .unlockedBy("has_sandstorm", has(registerItems.DISC_SANDSTORM.get()))
                .unlockedBy("has_rickroll", has(registerItems.DISC_RICKROLL.get()))
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

        ShapelessRecipeBuilder.shapeless(registerItems.DUST_STEEL.get(), 2)
                .requires(ModTags.Items.DUST_IRON)
                .requires(ModTags.Items.DUST_IRON)
                .requires(ModTags.Items.DUST_COAL)
                .unlockedBy("has_iron", has(ModTags.Items.DUST_IRON))
                .save(consumer, new ResourceLocation("steel_dust_from_iron"));

        ShapelessRecipeBuilder.shapeless(Items.GUNPOWDER, 1)
                .requires(ModTags.Items.MORTAR)
                .requires(Items.FLINT)
                .unlockedBy("has_mortar", has(ModTags.Items.MORTAR))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(registerItems.PIG_FAT.get(), 1)
                .requires(Items.PORKCHOP)
                .unlockedBy("has_porkchop", has(Items.PORKCHOP))
                .save(consumer, ResourceUtils.ModResourceLocation("pork_raw_noknife"));

        ShapelessRecipeBuilder.shapeless(registerItems.PIG_FAT.get(), 2)
                .requires(Items.PORKCHOP)
                .requires(ModTags.Items.KNIFE)
                .unlockedBy("has_knife", has(ModTags.Items.KNIFE))
                .save(consumer, ResourceUtils.ModResourceLocation("pork_raw_knife"));

        ShapelessRecipeBuilder.shapeless(registerItems.PIG_FAT.get(), 2)
                .requires(Items.COOKED_PORKCHOP)
                .unlockedBy("has_porkchop", has(Items.PORKCHOP))
                .save(consumer, ResourceUtils.ModResourceLocation("pork_cooked_noknife"));

        ShapelessRecipeBuilder.shapeless(registerItems.PIG_FAT.get(), 4)
                .requires(Items.COOKED_PORKCHOP)
                .requires(ModTags.Items.KNIFE)
                .unlockedBy("has_knife", has(ModTags.Items.KNIFE))
                .save(consumer, ResourceUtils.ModResourceLocation("pork_cooked_knife"));

    }

    private void generateShapedRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){

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

        ShapedRecipeBuilder.shaped(registerItems.COPPER_COIL.get(), 2)
                .define('W', registerItems.COPPER_WIRE.get())
                .define('B', registerItems.IRON_PIPE.get())
                .pattern("WBW")
                .pattern("WBW")
                .pattern("WBW")
                .unlockedBy("has_wire", has(registerItems.COPPER_WIRE.get()))
                .unlockedBy("has_pipe", has(registerItems.IRON_PIPE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.GUNPOWDER_COMPOUND.get(), 5)
                .define('G', Tags.Items.GUNPOWDER)
                .define('S', Tags.Items.SLIMEBALLS)
                .define('T', ModTags.Items.TREE_SAP)
                .pattern("TGT")
                .pattern("GSG")
                .pattern("TGT")
                .unlockedBy("has_sap", has(ModTags.Items.TREE_SAP))
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
                .save(consumer);

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

        ShapedRecipeBuilder.shaped(registerItems.FOR_DESTABILIZER.get(),2)
                .define('O', Tags.Items.OBSIDIAN)
                .define('C', registerItems.ORUNDUM.get())
                .define('E', Items.ENDER_EYE)
                .pattern("OCO")
                .pattern("CEC")
                .pattern("OCO")
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))
                .unlockedBy("has_eye", has(Items.ENDER_EYE))
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

        ShapedRecipeBuilder.shaped(registerItems.DEFIB_PADDLE.get(), 1)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Items.STONE_BUTTON)
                .define('P', registerItems.PLATE_POLYMER.get())
                .define('C', registerItems.COPPER_COIL.get())
                .define('H', ModTags.Items.CIRCUITS_BASIC)
                .pattern("BPP")
                .pattern("PCH")
                .pattern("III")
                .unlockedBy("has_part", has(registerItems.PRIMITIVE_CIRCUIT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.DEFIB_CHARGER.get(), 1)
                .define('W', registerItems.COPPER_COIL.get())
                .define('C', registerItems.CAPACITOR_PRIMITIVE.get())
                .define('P', Items.REDSTONE_LAMP)
                .define('I',  ModTags.Items.CIRCUITS_ADVANCED)
                .define('B', registerItems.HEADHUNTING_PCB.get())
                .define('R', registerItems.RESISTOR_PRIMITIVE.get())
                .pattern("WPW")
                .pattern("RBR")
                .pattern("CIC")
                .unlockedBy("has_paddle", has(registerItems.DEFIB_PADDLE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.MOLD_EXTRACTION.get(), 1)
                .define('N', Tags.Items.NUGGETS_IRON)
                .define('P', ModTags.Items.PLATE_IRON)
                .define('M', registerItems.MOLD_PLATE.get())
                .pattern("PNP")
                .pattern("PMP")
                .pattern("NPN")
                .unlockedBy("has_part", has(ModTags.Items.PLATE_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.OAK_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.OAK_LOGS)
                .define('P', Items.OAK_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.OAK_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.ACACIA_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.ACACIA_LOGS)
                .define('P', Items.ACACIA_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.ACACIA_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.BIRCH_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.BIRCH_LOGS)
                .define('P', Items.BIRCH_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.BIRCH_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.SPRUCE_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.SPRUCE_LOGS)
                .define('P', Items.SPRUCE_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.SPRUCE_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.DARK_OAK_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.DARK_OAK_LOGS)
                .define('P', Items.DARK_OAK_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.DARK_OAK_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.JUNGLE_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.JUNGLE_LOGS)
                .define('P', Items.JUNGLE_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.JUNGLE_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.WARPED_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.WARPED_STEMS)
                .define('P', Items.WARPED_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.WARPED_STEMS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.CRIMSON_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.CRIMSON_STEMS)
                .define('P', Items.CRIMSON_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.CRIMSON_STEMS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getStackForm().getItem(), 1)
                .define('F', registerBlocks.MACHINE_FRAME.get().asItem())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('P', Blocks.PISTON.asItem())
                .define('G', Blocks.GLASS.asItem())
                .define('I', HatchTE.ItemHatchDefinition.getStackForm().getItem())
                .define('D', registerBlocks.MACHINE_DYNAMO.get())
                .pattern("PDP")
                .pattern("IGI")
                .pattern("CFC")
                .unlockedBy("has_part", has(HatchTE.ItemHatchDefinition.getStackForm().getItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(HatchTE.ItemHatchDefinition.getStackForm().getItem(), 4)
                .define('F', registerBlocks.MACHINE_FRAME.get().asItem())
                .define('C', Tags.Items.CHESTS)
                .pattern(" F ")
                .pattern("FCF")
                .pattern(" F ")
                .unlockedBy("has_part", has(registerBlocks.MACHINE_FRAME.get().asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerBlocks.MACHINE_DYNAMO.get(), 1)
                .define('F', registerBlocks.MACHINE_FRAME.get().asItem())
                .define('C', registerItems.COPPER_COIL.get())
                .define('P', ModTags.Items.PLATE_IRON)
                .define('I', Tags.Items.INGOTS_IRON)
                .pattern("PCP")
                .pattern("IFI")
                .pattern("PCP")
                .unlockedBy("has_part", has(registerBlocks.MACHINE_FRAME.get().asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(HatchTE.EnergyHatchDefinition.getStackForm().getItem(), 4)
                .define('F', registerBlocks.MACHINE_FRAME.get().asItem())
                .define('C', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .pattern(" F ")
                .pattern("FCF")
                .pattern(" F ")
                .unlockedBy("has_part", has(registerBlocks.MACHINE_FRAME.get().asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(HatchTE.FluidHatchDefinition.getStackForm().getItem(), 4)
                .define('F', registerBlocks.MACHINE_FRAME.get().asItem())
                .define('C', Items.BUCKET)
                .pattern(" F ")
                .pattern("FCF")
                .pattern(" F ")
                .unlockedBy("has_part", has(registerBlocks.MACHINE_FRAME.get().asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(HatchTE.EntityDefinition.getStackForm().getItem(), 4)
                .define('F', registerBlocks.MACHINE_FRAME.get().asItem())
                .define('C', registerItems.FOR_DESTABILIZER.get())
                .pattern(" F ")
                .pattern("FCF")
                .pattern(" F ")
                .unlockedBy("has_part", has(registerItems.FOR_DESTABILIZER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.KYARU_STAFF.get(), 1)
                .define('S', Items.STICK)
                .define('C', ModTags.Items.INGOT_BRONZE)
                .define('B', Items.ENCHANTED_BOOK)
                .pattern(" CB")
                .pattern(" SC")
                .pattern("S  ")
                .unlockedBy("has_part", has(Items.ENCHANTED_BOOK))
                .save(consumer);

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

        ShapedRecipeBuilder.shaped(registerItems.IRON_PIPE.get(), 1)
                .define('A', ModTags.Items.PLATE_IRON)
                .pattern("A")
                .pattern("A")
                .unlockedBy("has_ironplate", has(ModTags.Items.PLATE_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.STEEL_PIPE.get(), 1)
                .define('A', ModTags.Items.PLATE_STEEL)
                .pattern("A")
                .pattern("A")
                .unlockedBy("has_steelplate", has(ModTags.Items.PLATE_STEEL))
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


        ShapedRecipeBuilder.shaped(registerItems.SHEATH.get())
                .define('B', ModTags.Items.PLATE_STEEL)
                .define('L', registerItems.STEEL_PIPE.get())
                .pattern("  B")
                .pattern(" B ")
                .pattern("L  ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        //Temp Recipe
        ShapedRecipeBuilder.shaped(registerItems.FLEXABLE_SWORD_THINGY.get())
                .define('B', ModTags.Items.PLATE_STEEL)
                .define('L', registerItems.PLATE_POLYMER.get())
                .pattern("  B")
                .pattern(" B ")
                .pattern("L  ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

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

        ShapedRecipeBuilder.shaped(registerItems.DISC_RICKROLL.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.DYES_GRAY)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.DYES_GRAY))
                .save(consumer);

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

        ShapedRecipeBuilder.shaped(registerItems.COMMANDING_STICK.get(), 1)
                .define('S', Items.STICK)
                .define('B', Tags.Items.DYES_BLACK)
                .pattern("  S")
                .pattern(" S ")
                .pattern("B  ")
                .unlockedBy("has_sticc", has(Items.STICK))
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

        ShapedRecipeBuilder.shaped(registerItems.CRESCENTKATANA_KURO.get())
                .define('B', ModTags.Items.INGOT_STEEL)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('L', registerItems.STEEL_PIPE.get())
                .pattern("  B")
                .pattern(" BP")
                .pattern("LP ")
                .unlockedBy("has_ingot", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.CRESCENTKATANA_SHIRO.get())
                .define('B', Tags.Items.INGOTS_IRON)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('L', registerItems.STEEL_PIPE.get())
                .pattern("  B")
                .pattern(" BP")
                .pattern("LP ")
                .unlockedBy("has_ingot", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.WARHAMMER.get())
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('S', ModTags.Items.INGOT_STEEL)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('L', registerItems.STEEL_PIPE.get())
                .pattern("BSP")
                .pattern(" L ")
                .pattern(" L ")
                .unlockedBy("has_ingot", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.AMMO_TORPEDO.get(), 4)
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

        ShapedRecipeBuilder.shaped(registerItems.AMMO_MISSILE.get(), 4)
                .define('A', ModTags.Items.INGOT_ALUMINIUM)
                .define('T', Blocks.TNT.asItem())
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('S', ModTags.Items.EXPLOSIVE_COMPOUND)
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

        ShapedRecipeBuilder.shaped(registerItems.DISC_REVENGE.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.GUNPOWDER)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.GUNPOWDER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.DISC_FALLEN_KINGDOM.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Items.GOLDEN_SWORD)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Items.GOLDEN_SWORD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.DISC_FIND_THE_PIECES.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.GEMS_EMERALD)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.GEMS_EMERALD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.DISC_TAKE_BACK_THE_NIGHT.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Blocks.CRYING_OBSIDIAN.asItem())
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Blocks.CRYING_OBSIDIAN.asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.DISC_DRAGONHEARTED.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Items.ENDER_EYE)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Items.ENDER_EYE))
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

        ShapedRecipeBuilder.shaped(registerItems.DISC_SANDSTORM.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.SAND)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_sand", has(Tags.Items.SAND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.DISC_CC5.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', ModTags.Items.ORIGINIUM_PRIME)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_sand", has(ModTags.Items.ORIGINIUM_PRIME))
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

        ShapedRecipeBuilder.shaped(registerItems.SLEDGEHAMMER.get())
                .define('S', registerItems.STEEL_PIPE.get())
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RiftwayControllerTE.RiftwayDefinition.getStackForm().getItem())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('P', registerItems.HEADHUNTING_PCB.get())
                .define('M', registerBlocks.MACHINE_FRAME.get())
                .define('O', registerItems.ORUNDUM.get())
                .define('D', registerItems.FOR_DESTABILIZER.get())
                .define('L', registerItems.COPPER_COIL.get())
                .pattern("LDL")
                .pattern("COC")
                .pattern("PMP")
                .unlockedBy("has_item", has(registerItems.FOR_DESTABILIZER.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(registerItems.RMA7012_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.RMA7012_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.RMA7012_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.RMA7012_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.RMA7012_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern(" I")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);


        ShapedRecipeBuilder.shaped(registerItems.RMA7024_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.RMA7024_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.RMA7024_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.RMA7024_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.RMA7024_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern(" I")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);


        ShapedRecipeBuilder.shaped(registerItems.D32_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.D32_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.D32_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.D32_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(registerItems.D32_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern(" I")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

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
        CrushingRecipeBuilder.builder().input(blockInput).addDrop(blockOutput).build(consumer, ResourceUtils.ModResourceLocation("sledgehammer_"+id));
    }

    protected void createHammerRecipes(Consumer<IFinishedRecipe> consumer, Block blockInput, Item blockOutput, int count, String id) {
        CrushingRecipeBuilder.builder().input(blockInput).addDrop(blockOutput, count).build(consumer, ResourceUtils.ModResourceLocation("sledgehammer_"+id));
    }

    protected void createHammerRecipes(Consumer<IFinishedRecipe> consumer, ITag<Item> blockInput, Item out, int count, String id) {
        CrushingRecipeBuilder.builder().input(blockInput).addDrop(out, count).build(consumer, ResourceUtils.ModResourceLocation("sledgehammer_"+id));
    }
}
