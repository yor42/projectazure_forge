package com.yor42.projectazure.data.common;

import com.tac.guns.crafting.WorkbenchRecipeBuilder;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.data.recipebuilder.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.AdvancedAlloySmelterControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.AmmoPressControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.OriginiumGeneratorControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.RiftwayControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.RegisterFluids;
import com.yor42.projectazure.setup.register.RegisterItems;
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
import net.minecraftforge.registries.ForgeRegistries;

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

        CookingRecipeBuilder.smelting(Ingredient.of(ModTags.Items.TREE_SAP), RegisterItems.PLATE_POLYMER.get(), 0.5F, 200)
                .unlockedBy("has_item", has(ModTags.Items.TREE_SAP))
                .save(consumer);

        CookingRecipeBuilder.blasting(Ingredient.of(RegisterBlocks.COBBLED_ORIROCK.get()), RegisterBlocks.ORIROCK.get(), 0.1F, 100)
                .unlockedBy("has_item", has(RegisterBlocks.COBBLED_ORIROCK.get()))
                .save(consumer);

        CustomRecipeBuilder.special((SpecialRecipeSerializer<?>) registerRecipes.Serializers.RELOADING.get()).save(consumer, "reloading_items");
        CustomRecipeBuilder.special((SpecialRecipeSerializer<?>) registerRecipes.Serializers.REPAIRING.get()).save(consumer, "repairing_items");
        CustomRecipeBuilder.special((SpecialRecipeSerializer<?>) registerRecipes.Serializers.TRANSFERPOTION.get()).save(consumer, "potion_transfer");
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
        ChemicalReactorRecipeBuilder.add(RegisterFluids.KETON_SOURCE_REGISTRY.get(), 100, Ingredient.of(RegisterItems.PIG_FAT.get()), consumer);
    }

    private void generateGunBenchRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){
        WorkbenchRecipeBuilder.workbenchRecipe(RegisterItems.CASELESS_4MM.get(),6).addIngredient(RegisterItems.GUNPOWDER_COMPOUND.get(), 3).addIngredient(RegisterItems.INGOT_LEAD.get(), 5).build(consumer, Constants.MODID,"gunbench_4mmcaseless_lead");

        WorkbenchRecipeBuilder.workbenchRecipe(RegisterItems.W_GRANADELAUNCHER.get()).addIngredient(ModTags.Items.PLATE_STEEL, 14).addIngredient(RegisterItems.PRIMITIVE_MOTOR.get(), 2).addIngredient(ModTags.Items.CIRCUITS_BASIC).addIngredient(RegisterItems.MECHANICAL_PARTS.get(), 4).build(consumer, Constants.MODID,"gunbench_granadelauncher");
        WorkbenchRecipeBuilder.workbenchRecipe(RegisterItems.TYPHOON.get()).addIngredient(ModTags.Items.PLATE_STEEL, 25).addIngredient(ModTags.Items.PLATE_ALUMINIUM, 10).addIngredient(ModTags.Items.INGOT_INCANDESCENT_ALLOY, 30).addIngredient(RegisterItems.CRYSTALLINE_COMPONENT.get(),4).addIngredient(ModTags.Items.CIRCUITS_ADVANCED, 2).build(consumer, Constants.MODID,"gunbench_typhoon");
        WorkbenchRecipeBuilder.workbenchRecipe(RegisterItems.WHITEFANG_465.get()).addIngredient(ModTags.Items.PLATE_STEEL, 9).addIngredient(ModTags.Items.INGOT_STEEL, 10).build(consumer, Constants.MODID,"gunbench_whitefang");
        WorkbenchRecipeBuilder.workbenchRecipe(RegisterItems.SANGVIS_RAILGUN.get()).addIngredient(ModTags.Items.INGOT_STEEL, 40).addIngredient(ModTags.Items.CIRCUITS_CRYSTALLINE, 9).addIngredient(RegisterItems.BASIC_DEVICE.get(), 12).build(consumer, Constants.MODID,"gunbench_cannon");
        WorkbenchRecipeBuilder.workbenchRecipe(RegisterItems.SUPERNOVA.get()).addIngredient(ModTags.Items.INGOT_STEEL, 33).addIngredient(ModTags.Items.CIRCUITS_CRYSTALLINE, 12).addIngredient(RegisterItems.BASIC_DEVICE.get(), 8).build(consumer, Constants.MODID,"gunbench_supernova");
    }

    private void generateMetalRecipes(@Nonnull Consumer<IFinishedRecipe> consumer){
        BuildMetalRecipe(consumer, 0.5F, new Metals("copper", RegisterItems.INGOT_COPPER.get(), ModTags.Items.INGOT_COPPER).block(RegisterBlocks.COPPER_BLOCK.get(), ModTags.Items.BLOCK_COPPER).ore(RegisterBlocks.COPPER_ORE.get().asItem(), ModTags.Items.ORES_COPPER).dust(RegisterItems.DUST_COPPER.get(), ModTags.Items.DUST_COPPER).plates(RegisterItems.PLATE_COPPER.get(), ModTags.Items.PLATE_COPPER).nugget(RegisterItems.NUGGET_COPPER.get(), ModTags.Items.NUGGET_COPPER));
        BuildMetalRecipe(consumer, 0.5F, new Metals("tin", RegisterItems.INGOT_TIN.get(), ModTags.Items.INGOT_TIN).block(RegisterBlocks.TIN_BLOCK.get(), ModTags.Items.BLOCK_TIN).ore(RegisterBlocks.TIN_ORE.get().asItem(), ModTags.Items.ORES_TIN).dust(RegisterItems.DUST_TIN.get(), ModTags.Items.DUST_TIN).plates(RegisterItems.PLATE_TIN.get(), ModTags.Items.PLATE_TIN).nugget(RegisterItems.NUGGET_TIN.get(), ModTags.Items.NUGGET_TIN));
        BuildMetalRecipe(consumer, 0.5F, new Metals("lead", RegisterItems.INGOT_LEAD.get(), ModTags.Items.INGOT_LEAD).block(RegisterBlocks.LEAD_BLOCK.get(), ModTags.Items.BLOCK_LEAD).ore(RegisterBlocks.LEAD_ORE.get().asItem(), ModTags.Items.ORES_LEAD).dust(RegisterItems.DUST_LEAD.get(), ModTags.Items.DUST_LEAD).plates(RegisterItems.PLATE_LEAD.get(), ModTags.Items.PLATE_LEAD).nugget(RegisterItems.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD).nugget(RegisterItems.NUGGET_LEAD.get(), ModTags.Items.NUGGET_LEAD));
        BuildMetalRecipe(consumer, 0.5F, new Metals("bronze", RegisterItems.INGOT_BRONZE.get(), ModTags.Items.INGOT_BRONZE).block(RegisterBlocks.BRONZE_BLOCK.get(), ModTags.Items.BLOCK_BRONZE).dust(RegisterItems.DUST_BRONZE.get(), ModTags.Items.DUST_BRONZE).plates(RegisterItems.PLATE_BRONZE.get(), ModTags.Items.PLATE_BRONZE).gear(RegisterItems.GEAR_BRONZE.get(), ModTags.Items.GEAR_BRONZE).nugget(RegisterItems.NUGGET_BRONZE.get(), ModTags.Items.NUGGET_BRONZE));
        BuildMetalRecipe(consumer, 0.5F, new Metals("aluminium", RegisterItems.INGOT_ALUMINIUM.get(), ModTags.Items.INGOT_ALUMINIUM).block(RegisterBlocks.ALUMINIUM_BLOCK.get(), ModTags.Items.BLOCK_ALUMINIUM).ore(RegisterBlocks.BAUXITE_ORE.get().asItem(), ModTags.Items.ORES_ALUMINIUM).dust(RegisterItems.DUST_ALUMINIUM.get(), ModTags.Items.DUST_ALUMINIUM).plates(RegisterItems.PLATE_ALUMINIUM.get(), ModTags.Items.PLATE_ALUMINIUM));
        BuildMetalRecipe(consumer, 0.5F, new Metals("steel", RegisterItems.INGOT_STEEL.get(), ModTags.Items.INGOT_STEEL).block(RegisterBlocks.STEEL_BLOCK.get(), ModTags.Items.BLOCK_STEEL).dust(RegisterItems.DUST_STEEL.get(), ModTags.Items.DUST_STEEL).plates(RegisterItems.PLATE_STEEL.get(), ModTags.Items.PLATE_STEEL).gear(RegisterItems.GEAR_STEEL.get(), ModTags.Items.GEAR_STEEL).nugget(RegisterItems.NUGGET_STEEL.get(), ModTags.Items.NUGGET_STEEL));
        BuildMetalRecipe(consumer, 0.5F, new Metals("brass", RegisterItems.INGOT_BRASS.get(), ModTags.Items.INGOT_BRASS).block(RegisterBlocks.BRASS_BLOCK.get(), ModTags.Items.BLOCK_BRASS).dust(RegisterItems.DUST_BRASS.get(), ModTags.Items.DUST_BRASS).plates(RegisterItems.PLATE_BRASS.get(), ModTags.Items.PLATE_BRASS).nugget(RegisterItems.NUGGET_BRASS.get(), ModTags.Items.PLATE_BRASS));
        BuildMetalRecipe(consumer, 0.5F, new Metals("zinc", RegisterItems.INGOT_ZINC.get(), ModTags.Items.INGOT_ZINC).block(RegisterBlocks.ZINC_BLOCK.get(), ModTags.Items.BLOCK_ZINC).dust(RegisterItems.DUST_ZINC.get(), ModTags.Items.DUST_ZINC).plates(RegisterItems.PLATE_ZINC.get(), ModTags.Items.PLATE_ZINC).nugget(RegisterItems.NUGGET_ZINC.get(), ModTags.Items.NUGGET_ZINC));
        BuildMetalRecipe(consumer, 0.5F, new Metals("rma70-12", RegisterItems.INGOT_RMA7012.get(), ModTags.Items.INGOT_RMA7012).block(RegisterBlocks.RMA_7012_BLOCK.get(), ModTags.Items.BLOCK_RMA7012).ore(RegisterBlocks.RMA_7012_ORE.get(), ModTags.Items.ORES_RMA7012).dust(RegisterItems.DUST_RMA7012.get(), ModTags.Items.DUST_RMA7012).plates(RegisterItems.PLATE_RMA7012.get(), ModTags.Items.PLATE_RMA7012).nugget(RegisterItems.NUGGET_RMA7012.get(), ModTags.Items.NUGGET_RMA7012));
        BuildMetalRecipe(consumer, 0.5F, new Metals("rma70-24", RegisterItems.INGOT_RMA7024.get(), ModTags.Items.INGOT_RMA7024).block(RegisterBlocks.RMA_7024_BLOCK.get(), ModTags.Items.BLOCK_RMA7024).dust(RegisterItems.DUST_RMA7024.get(), ModTags.Items.DUST_RMA7024).plates(RegisterItems.PLATE_RMA7024.get(), ModTags.Items.PLATE_RMA7024).nugget(RegisterItems.NUGGET_RMA7024.get(), ModTags.Items.NUGGET_RMA7024));
        BuildMetalRecipe(consumer, 0.5F, new Metals("d32steel", RegisterItems.INGOT_D32.get(), ModTags.Items.INGOT_D32).block(RegisterBlocks.D32_BLOCK.get(), ModTags.Items.BLOCK_D32).dust(RegisterItems.DUST_D32.get(), ModTags.Items.DUST_D32).plates(RegisterItems.PLATE_D32.get(), ModTags.Items.PLATE_D32).nugget(RegisterItems.NUGGET_D32.get(), ModTags.Items.NUGGET_D32));
        BuildMetalRecipe(consumer, 0.5F, new Metals("incandescent_alloy", RegisterItems.INGOT_INCANDESCENT_ALLOY.get(), ModTags.Items.INGOT_INCANDESCENT_ALLOY).dust(RegisterItems.DUST_INCANDESCENT_ALLOY.get(), ModTags.Items.DUST_INCANDESCENT_ALLOY).plates(RegisterItems.PLATE_INCANDESCENT_ALLOY.get(), ModTags.Items.PLATE_INCANDESCENT_ALLOY).block(RegisterBlocks.INCANDESCENT_ALLOY_BLOCK.get(), ModTags.Items.BLOCK_INCANDESCENT_ALLOY).nugget(RegisterItems.NUGGET_INCANDESCENT_ALLOY.get(), ModTags.Items.NUGGET_INCANDESCENT_ALLOY));
        BuildMetalRecipe(consumer, 0.5F, new Metals("manganese", RegisterItems.INGOT_MANGANESE.get(), ModTags.Items.INGOT_MANGANESE).dust(RegisterItems.DUST_MANGANESE.get(), ModTags.Items.DUST_MANGANESE).plates(RegisterItems.PLATE_MANGANESE.get(), ModTags.Items.PLATE_MANGANESE).block(RegisterBlocks.MANGANESE_BLOCK.get(), ModTags.Items.BLOCK_MANGANESE).nugget(RegisterItems.NUGGET_MANGANESE.get(), ModTags.Items.NUGGET_MANGANESE));
        BuildMetalRecipe(consumer, 0.5F, new Metals("iron", Items.IRON_INGOT, Tags.Items.INGOTS_IRON).dust(RegisterItems.DUST_IRON.get(), ModTags.Items.DUST_IRON).plates(RegisterItems.PLATE_IRON.get(), ModTags.Items.PLATE_IRON).gear(RegisterItems.GEAR_IRON.get(), ModTags.Items.GEAR_IRON));
        BuildMetalRecipe(consumer, 0.5F, new Metals("gold", Items.GOLD_INGOT, Tags.Items.INGOTS_GOLD).dust(RegisterItems.DUST_GOLD.get(), ModTags.Items.DUST_GOLD).plates(RegisterItems.PLATE_GOLD.get(), ModTags.Items.PLATE_GOLD));

    }

    private void generateCrushingRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){
        createHammerRecipes(consumer, Blocks.STONE, Blocks.COBBLESTONE, "cobblestone");
        createHammerRecipes(consumer, Blocks.COBBLESTONE, Blocks.GRAVEL, "gravel");
        createHammerRecipes(consumer, Blocks.GRAVEL, Blocks.SAND, "sand");
        createHammerRecipes(consumer, RegisterBlocks.COBBLED_ORIROCK.get(), RegisterItems.DUST_ORIROCK.get(), 4, "orirock_from_cobble");
        createHammerRecipes(consumer, ModTags.Items.ORES_COPPER, RegisterItems.DUST_COPPER.get(), 2, "copper");
        createHammerRecipes(consumer, ModTags.Items.ORES_ALUMINIUM, RegisterItems.DUST_ALUMINIUM.get(), 2, "aluminium");
        createHammerRecipes(consumer, ModTags.Items.ORES_LEAD, RegisterItems.DUST_LEAD.get(), 2, "lead");
        createHammerRecipes(consumer, ModTags.Items.ORES_TIN, RegisterItems.DUST_TIN.get(), 2, "tin");
        createHammerRecipes(consumer, ModTags.Items.ORES_ORIROCK, RegisterItems.DUST_ORIROCK.get(), 4, "orirock");
        createHammerRecipes(consumer, Tags.Items.ORES_GOLD, RegisterItems.DUST_GOLD.get(), 2, "gold");
        createHammerRecipes(consumer, Tags.Items.ORES_IRON, RegisterItems.DUST_IRON.get(), 2, "iron");
        createHammerRecipes(consumer, Tags.Items.ORES_COAL, RegisterItems.DUST_COAL.get(), 2, "coal");

    }

    private void generateAlloyingRecipe(@Nonnull Consumer<IFinishedRecipe> consumer) {
        AlloyingRecipeBuilder.AlloyRecipe(RegisterItems.INGOT_BRONZE.get(), (byte)4)
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER), (byte) 3)
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_TIN), (byte) 1)
                .build(consumer);

        AlloyingRecipeBuilder.AlloyRecipe(RegisterItems.INGOT_BRASS.get(), (byte)2)
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_ZINC))
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER))
                .build(consumer);

        AlloyingRecipeBuilder.AlloyRecipe(RegisterItems.VITRIFIED_ORIGINIUM.get(), (byte)1)
                .addIngredient(Ingredient.of(ModTags.Items.ORIGINIUM_PRIME))
                .addIngredient(Ingredient.of(Tags.Items.GEMS_QUARTZ), (byte) 2)
                .build(consumer);

        AlloyingRecipeBuilder.AlloyRecipe(RegisterItems.C99_CARBON.get(), (byte)1)
                .addIngredient(Ingredient.of(ItemTags.COALS), (byte) 1)
                .addIngredient(Ingredient.of(Items.SAND), (byte) 2)
                .build(consumer);

        AlloyingRecipeBuilder.AlloyRecipe(RegisterItems.INGOT_INCANDESCENT_ALLOY.get(), (byte)1)
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_MANGANESE), (byte) 1)
                .addIngredient(Ingredient.of(ModTags.Items.INGOT_RMA7012), (byte) 1)
                .build(consumer);
    }
    private void generatePressingRecipe(@Nonnull Consumer<IFinishedRecipe> consumer) {
        PressingRecipeBuilder.addRecipe(RegisterItems.TREE_SAP.get(), Ingredient.of(ItemTags.LOGS), Ingredient.of(RegisterItems.MOLD_EXTRACTION.get()), 1, 200)
                .build(consumer, new ResourceLocation("iron_plate_pressing"));
    }

    private void generateCrystalizingRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){
        CrystalizingRecipeBuilder.addRecipe(RegisterItems.ORIGINIUM_PRIME.get(), Ingredient.of(RegisterItems.ORIGINIUM_SEED.get()), RegisterFluids.ORIGINIUM_SOLUTION_SOURCE_REGISTRY.get(), 3000)
                .build(consumer, new ResourceLocation("originium_crystalizing"));

        CrystalizingRecipeBuilder.addRecipe(Items.QUARTZ, Ingredient.of(RegisterItems.NETHER_QUARTZ_SEED.get()), RegisterFluids.NETHER_QUARTZ_SOLUTION_SOURCE_REGISTRY.get(), 400)
                .build(consumer, new ResourceLocation("quartz_crystalizing"));
    }

    private void generateShapelessRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){

        ShapelessRecipeBuilder.shapeless(RegisterItems.COPPER_WIRE.get(), 4)
                .requires(ModTags.Items.CUTTER)
                .requires(ModTags.Items.PLATE_COPPER)
                .unlockedBy("has_copper_plate", has(ModTags.Items.PLATE_COPPER))
                .unlockedBy("has_cutter", has(ModTags.Items.CUTTER))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RegisterItems.GOLD_WIRE.get(), 4)
                .requires(ModTags.Items.CUTTER)
                .requires(ModTags.Items.PLATE_GOLD)
                .unlockedBy("has_copper_plate", has(ModTags.Items.PLATE_GOLD))
                .unlockedBy("has_cutter", has(ModTags.Items.CUTTER))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RegisterItems.ORIGINITE.get(), 9)
                .requires(ModTags.Items.ORIGINIUM_PRIME).unlockedBy("hasoriginium", has(ModTags.Items.ORIGINIUM_PRIME));


        ShapelessRecipeBuilder.shapeless(RegisterItems.DUST_ORIGINIUM.get(), 4)
            .requires(ModTags.Items.ORIGINITE)
            .requires(ModTags.Items.HAMMER)
            .unlockedBy("has_originite", has(RegisterItems.ORIGINITE.get()))
            .save(consumer, new ResourceLocation("originium_dust_from_originite"));

        ShapelessRecipeBuilder.shapeless(RegisterItems.DUST_ORIGINIUM.get(), 2)
                .requires(ModTags.Items.ORIGINIUM_PRIME)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy("has_originite", has(RegisterItems.ORIGINITE.get()))
                .save(consumer, new ResourceLocation("originium_dust_from_prime"));

        ShapelessRecipeBuilder.shapeless(RegisterItems.DUST_NETHER_QUARTZ.get(), 2)
                .requires(Items.QUARTZ)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .save(consumer, new ResourceLocation("quartz_dust_from_quartz"));

        ShapelessRecipeBuilder.shapeless(RegisterItems.DUST_STEEL.get(), 2)
                .requires(ModTags.Items.DUST_IRON)
                .requires(ModTags.Items.DUST_IRON)
                .requires(ModTags.Items.DUST_COAL)
                .unlockedBy("has_iron", has(ModTags.Items.DUST_IRON))
                .save(consumer, new ResourceLocation("steel_dust_from_iron"));

        ShapelessRecipeBuilder.shapeless(RegisterItems.DUST_INCANDESCENT_ALLOY.get(), 2)
                .requires(ModTags.Items.DUST_ZINC)
                .requires(ModTags.Items.DUST_MANGANESE)
                .unlockedBy("has_manganese", has(ModTags.Items.DUST_MANGANESE))
                .unlockedBy("has_zinc", has(ModTags.Items.DUST_ZINC))
                .save(consumer, new ResourceLocation("incandescent_alloy_dust_from_zinc_and_manganese"));

        ShapelessRecipeBuilder.shapeless(Items.GUNPOWDER, 1)
                .requires(ModTags.Items.MORTAR)
                .requires(Items.FLINT)
                .unlockedBy("has_mortar", has(ModTags.Items.MORTAR))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RegisterItems.PIG_FAT.get(), 1)
                .requires(Items.PORKCHOP)
                .unlockedBy("has_porkchop", has(Items.PORKCHOP))
                .save(consumer, ResourceUtils.ModResourceLocation("pork_raw_noknife"));

        ShapelessRecipeBuilder.shapeless(RegisterItems.PIG_FAT.get(), 2)
                .requires(Items.PORKCHOP)
                .requires(ModTags.Items.KNIFE)
                .unlockedBy("has_knife", has(ModTags.Items.KNIFE))
                .save(consumer, ResourceUtils.ModResourceLocation("pork_raw_knife"));

        ShapelessRecipeBuilder.shapeless(RegisterItems.PIG_FAT.get(), 2)
                .requires(Items.COOKED_PORKCHOP)
                .unlockedBy("has_porkchop", has(Items.PORKCHOP))
                .save(consumer, ResourceUtils.ModResourceLocation("pork_cooked_noknife"));

        ShapelessRecipeBuilder.shapeless(RegisterItems.PIG_FAT.get(), 4)
                .requires(Items.COOKED_PORKCHOP)
                .requires(ModTags.Items.KNIFE)
                .unlockedBy("has_knife", has(ModTags.Items.KNIFE))
                .save(consumer, ResourceUtils.ModResourceLocation("pork_cooked_knife"));

        this.registerSawingRecipe(ItemTags.OAK_LOGS, Items.OAK_PLANKS, consumer);
        this.registerSawingRecipe(ItemTags.ACACIA_LOGS, Items.ACACIA_PLANKS, consumer);
        this.registerSawingRecipe(ItemTags.SPRUCE_LOGS, Items.SPRUCE_PLANKS, consumer);
        this.registerSawingRecipe(ItemTags.BIRCH_LOGS, Items.BIRCH_PLANKS, consumer);
        this.registerSawingRecipe(ItemTags.DARK_OAK_LOGS, Items.DARK_OAK_PLANKS, consumer);
        this.registerSawingRecipe(ItemTags.JUNGLE_LOGS, Items.JUNGLE_PLANKS, consumer);
        this.registerSawingRecipe(ItemTags.WARPED_STEMS, Items.WARPED_PLANKS, consumer);
        this.registerSawingRecipe(ItemTags.CRIMSON_STEMS, Items.CRIMSON_PLANKS, consumer);
        this.registerSawingRecipe(ItemTags.LOGS, Items.STICK, consumer);

        ShapelessRecipeBuilder.shapeless(RegisterItems.COPPER_IRON_PROBE.get())
                .requires(RegisterItems.POTATO_BATTERY.get())
                .unlockedBy("has_material", has(RegisterItems.POTATO_BATTERY.get()))
                .save(consumer,"probefrompotatobattery");

        ShapelessRecipeBuilder.shapeless(RegisterItems.COPPER_IRON_PROBE.get())
                .requires(RegisterItems.PRIMITIVE_ORIGINIUM_BATTERY.get())
                .unlockedBy("has_material", has(RegisterItems.PRIMITIVE_ORIGINIUM_BATTERY.get()))
                .save(consumer,"probefromoriginiumbattery");

        ShapelessRecipeBuilder.shapeless(RegisterItems.PRIMITIVE_ORIGINIUM_BATTERY.get())
                .requires(RegisterItems.COPPER_IRON_PROBE.get())
                .requires(ModTags.Items.ORIGINIUM_PRIME)
                .unlockedBy("has_material", has(RegisterItems.COPPER_IRON_PROBE.get()))
                .save(consumer, "primebattery_with_probe");

        ShapelessRecipeBuilder.shapeless(RegisterItems.POTATO_BATTERY.get())
                .requires(RegisterItems.COPPER_IRON_PROBE.get())
                .requires(Tags.Items.CROPS_POTATO)
                .unlockedBy("has_material", has(RegisterItems.COPPER_IRON_PROBE.get()))
                .save(consumer, "potatobattery_with_probe");

        ShapelessRecipeBuilder.shapeless(RegisterItems.POTATO_BATTERY.get())
                .requires(RegisterItems.COPPER_IRON_PROBE.get())
                .requires(Items.POISONOUS_POTATO)
                .unlockedBy("has_material", has(RegisterItems.COPPER_IRON_PROBE.get()))
                .save(consumer, "poisonouspotatobattery_with_probe");

    }

    private void generateShapedRecipe(@Nonnull Consumer<IFinishedRecipe> consumer){

        ShapedRecipeBuilder.shaped(RegisterItems.TREE_SAP.get(),4)
                .define('L', ItemTags.LOGS)
                .pattern("LLL")
                .pattern("LLL")
                .pattern("LLL")
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.REENFORCEDCONCRETE.get().asItem(),2)
                .define('G', Blocks.GRAY_CONCRETE_POWDER)
                .define('S', ModTags.Items.PLATE_STEEL)
                .pattern("SGS")
                .pattern("GSG")
                .pattern("SGS")
                .unlockedBy("has_concrete_powder", has(Blocks.GRAY_CONCRETE_POWDER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.ASPHALTCONCRETE.get().asItem(),4)
                .define('B', RegisterItems.BITUMEN.get())
                .define('G', Tags.Items.GRAVEL)
                .pattern("BG")
                .pattern("GB")
                .unlockedBy("has_resource", has(RegisterItems.BITUMEN.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.STEEL_FOUNDATION.get().asItem(),2)
                .define('F', RegisterBlocks.MACHINE_FRAME.get())
                .define('Y', Tags.Items.DYES_YELLOW)
                .define('B', Tags.Items.DYES_BLACK)
                .pattern("FY")
                .pattern("BF")
                .unlockedBy("has_resource", has(RegisterBlocks.MACHINE_FRAME.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.COPPER_COIL.get(), 2)
                .define('W', RegisterItems.COPPER_WIRE.get())
                .define('B', RegisterItems.IRON_PIPE.get())
                .pattern("WBW")
                .pattern("WBW")
                .pattern("WBW")
                .unlockedBy("has_wire", has(RegisterItems.COPPER_WIRE.get()))
                .unlockedBy("has_pipe", has(RegisterItems.IRON_PIPE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.GOLD_COIL.get(), 2)
                .define('W', RegisterItems.GOLD_WIRE.get())
                .define('B', RegisterItems.IRON_PIPE.get())
                .pattern("WBW")
                .pattern("WBW")
                .pattern("WBW")
                .unlockedBy("has_wire", has(RegisterItems.GOLD_WIRE.get()))
                .unlockedBy("has_pipe", has(RegisterItems.IRON_PIPE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.GUNPOWDER_COMPOUND.get(), 5)
                .define('G', Tags.Items.GUNPOWDER)
                .define('S', Tags.Items.SLIMEBALLS)
                .define('T', ModTags.Items.TREE_SAP)
                .pattern("TGT")
                .pattern("GSG")
                .pattern("TGT")
                .unlockedBy("has_sap", has(ModTags.Items.TREE_SAP))
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.PRIMITIVE_ORIGINIUM_BATTERY.get(),1)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', ModTags.Items.INGOT_COPPER)
                .define('O', ModTags.Items.ORIGINIUM_PRIME)
                .define('W', ModTags.Items.WIRE_COPPER)
                .pattern("IW")
                .pattern("OC")
                .unlockedBy("has_material", has(ModTags.Items.ORIGINIUM_PRIME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.POTATO_BATTERY.get(),1)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', ModTags.Items.INGOT_COPPER)
                .define('O', Tags.Items.CROPS_POTATO)
                .define('W', ModTags.Items.WIRE_COPPER)
                .pattern("IW")
                .pattern("OC")
                .unlockedBy("has_material", has(Tags.Items.CROPS_POTATO))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.POTATO_BATTERY.get(),1)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', ModTags.Items.INGOT_COPPER)
                .define('O', Items.POISONOUS_POTATO)
                .define('W', ModTags.Items.WIRE_COPPER)
                .pattern("IW")
                .pattern("OC")
                .unlockedBy("has_material", has(Items.POISONOUS_POTATO))
                .save(consumer, "battery_from_poisonous_potato");

        ShapedRecipeBuilder.shaped(RegisterItems.HAMMER_STONE.get())
                .define('I', Tags.Items.COBBLESTONE)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.COBBLESTONE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.HAMMER_IRON.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.INGOTS_IRON));
        ShapedRecipeBuilder.shaped(RegisterItems.HAMMER_COPPER.get())
                .define('I', ModTags.Items.INGOT_COPPER)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_COPPER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.HAMMER_TIN.get())
                .define('I', ModTags.Items.INGOT_TIN)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_TIN))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.HAMMER_GOLD.get())
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.INGOTS_GOLD))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.HAMMER_STEEL.get())
                .define('I', ModTags.Items.INGOT_STEEL)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.HAMMER_DIAMOND.get())
                .define('I', Tags.Items.GEMS_DIAMOND)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.GEMS_DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.SAW_STONE.get())
                .define('I', Tags.Items.COBBLESTONE)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern("SI")
                .pattern("SI")
                .pattern("S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.COBBLESTONE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.SAW_IRON.get())
                .define('I', ModTags.Items.PLATE_IRON)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern("SI")
                .pattern("SI")
                .pattern("S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_IRON));
        ShapedRecipeBuilder.shaped(RegisterItems.SAW_COPPER.get())
                .define('I', ModTags.Items.PLATE_COPPER)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern("SI")
                .pattern("SI")
                .pattern("S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_COPPER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.SAW_TIN.get())
                .define('I', ModTags.Items.PLATE_TIN)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern("SI")
                .pattern("SI")
                .pattern("S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_TIN))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.SAW_GOLD.get())
                .define('I', ModTags.Items.PLATE_GOLD)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern("SI")
                .pattern("SI")
                .pattern("S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_GOLD))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.SAW_STEEL.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern("SI")
                .pattern("SI")
                .pattern("S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RegisterItems.SAW_DIAMOND.get())
                .define('I', Tags.Items.GEMS_DIAMOND)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern("SI")
                .pattern("SI")
                .pattern("S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.GEMS_DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.KITCHEN_KNIFE.get())
                .define('I', ModTags.Items.PLATE_IRON)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.TACTICAL_KNIFE.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .define('S', RegisterItems.C99_CARBON.get())
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_stick", has(RegisterItems.C99_CARBON.get()))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.OATHRING.get())
                .define('N', Tags.Items.NETHER_STARS)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('S', ModTags.Items.INGOT_D32)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('B', Items.BLACK_WOOL)
                .pattern("DND")
                .pattern("GSG")
                .pattern("BBB")
                .unlockedBy("has_stick", has(Tags.Items.GEMS_DIAMOND))
                .unlockedBy("has_material", has(Tags.Items.NETHER_STARS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.PRIMITIVE_DEVICE.get())
                .define('M', RegisterItems.PRIMITIVE_MOTOR.get())
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('O', RegisterItems.ORUNDUM.get())
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .define('R', ModTags.Items.GEAR_BRONZE)
                .pattern("QRQ")
                .pattern("MCM")
                .pattern("POP")
                .unlockedBy("has_material", has(ModTags.Items.CIRCUITS_BASIC))
                .unlockedBy("has_material2", has(RegisterItems.PRIMITIVE_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.BASIC_DEVICE.get())
                .define('M', RegisterItems.PRIMITIVE_MOTOR.get())
                .define('C', RegisterItems.PRIMITIVE_DEVICE.get())
                .define('B', ModTags.Items.CIRCUITS_ADVANCED)
                .define('O', RegisterItems.CRYSTALLINE_COMPONENT.get())
                .define('R', ModTags.Items.GEAR_STEEL)
                .pattern("COC")
                .pattern("MBM")
                .pattern("CRC")
                .unlockedBy("has_material", has(ModTags.Items.CIRCUITS_ADVANCED))
                .unlockedBy("has_material2", has(RegisterItems.BASIC_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.ADVANCED_DEVICE.get())
                .define('M', RegisterItems.ADVANCED_MOTOR.get())
                .define('C', RegisterItems.BASIC_DEVICE.get())
                .define('B', ModTags.Items.CIRCUITS_CRYSTALLINE)
                .define('O', RegisterItems.LOXICKOHL.get())
                .pattern("COC")
                .pattern("MBM")
                .pattern("COC")
                .unlockedBy("has_material", has(ModTags.Items.CIRCUITS_ADVANCED))
                .unlockedBy("has_material2", has(RegisterItems.ADVANCED_MOTOR.get()))
                .save(consumer);

        SmithingRecipeBuilder.smithing(Ingredient.of(RegisterItems.SAW_DIAMOND.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), RegisterItems.SAW_NETHERITE.get()).unlocks("has_material", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, ResourceUtils.ModResourceLocation("netheritesaw"));
        SmithingRecipeBuilder.smithing(Ingredient.of(RegisterItems.HAMMER_DIAMOND.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), RegisterItems.HAMMER_NETHERITE.get()).unlocks("has_material", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, ResourceUtils.ModResourceLocation("netheritehammer"));
        SmithingRecipeBuilder.smithing(Ingredient.of(RegisterItems.CUTTER_DIAMOND.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), RegisterItems.CUTTER_NETHERITE.get()).unlocks("has_material", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, ResourceUtils.ModResourceLocation("netheritecutter"));

        ShapedRecipeBuilder.shaped(RegisterItems.CHARCOAL_FILTER.get())
                .define('P', Items.PAPER)
                .define('C', Items.CHARCOAL)
                .define('W', ItemTags.WOOL)
                .pattern("CP")
                .pattern("PW")
                .unlockedBy("has_material", has(Items.CHARCOAL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.GASMASK_FILTER.get())
                .define('P', ModTags.Items.PLATE_ALUMINIUM)
                .define('C', ModTags.Items.PLATE_STEEL)
                .define('F', RegisterItems.CHARCOAL_FILTER.get())
                .pattern("CP")
                .pattern("PF")
                .unlockedBy("has_material", has(RegisterItems.CHARCOAL_FILTER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.GASMASK.get())
                .define('P', ModTags.Items.PLATE_PLASTIC)
                .define('G', Tags.Items.GLASS_PANES_COLORLESS)
                .define('S', ModTags.Items.INGOT_STEEL)
                .define('C', ModTags.Items.PLATE_COPPER)
                .pattern("PPP")
                .pattern("GPG")
                .pattern("SCS")
                .unlockedBy("has_material", has(RegisterItems.CHARCOAL_FILTER.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_STONE.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.COBBLESTONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_IRON.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_STEEL.get())
                .define('I', ModTags.Items.INGOT_STEEL)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_COPPER.get())
                .define('I', ModTags.Items.INGOT_COPPER)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_COPPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_TIN.get())
                .define('I', ModTags.Items.INGOT_TIN)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_TIN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_BRONZE.get())
                .define('I', ModTags.Items.INGOT_BRONZE)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_BRONZE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_GOLD.get())
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.INGOTS_GOLD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_DIAMOND.get())
                .define('I', Tags.Items.GEMS_DIAMOND)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.GEMS_DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_NETHERITE.get())
                .define('I', Tags.Items.INGOTS_NETHERITE)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.INGOTS_NETHERITE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_RMA7012.get())
                .define('I', ModTags.Items.INGOT_RMA7012)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_RMA7024.get())
                .define('I', ModTags.Items.INGOT_RMA7024)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MORTAR_D32.get())
                .define('I', ModTags.Items.INGOT_D32)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.COBBLESTONE)
                .pattern("  S")
                .pattern("CIC")
                .pattern(" C ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_STONE.get())
                .define('I', Tags.Items.COBBLESTONE)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" I ")
                .pattern("IIS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.COBBLESTONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_IRON.get())
                .define('I', ModTags.Items.PLATE_IRON)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_STEEL.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_COPPER.get())
                .define('I', ModTags.Items.PLATE_COPPER)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_COPPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_TIN.get())
                .define('I', ModTags.Items.PLATE_TIN)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_TIN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_BRONZE.get())
                .define('I', ModTags.Items.PLATE_BRONZE)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_BRONZE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_GOLD.get())
                .define('I', ModTags.Items.PLATE_BRONZE)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_BRONZE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_DIAMOND.get())
                .define('I', Tags.Items.GEMS_DIAMOND)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(Tags.Items.GEMS_DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_RMA7012.get())
                .define('I', ModTags.Items.PLATE_RMA7012)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_RMA7024.get())
                .define('I', ModTags.Items.PLATE_RMA7024)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CUTTER_D32.get())
                .define('I', ModTags.Items.PLATE_D32)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('N', Tags.Items.NUGGETS_IRON)
                .pattern(" I ")
                .pattern("INS")
                .pattern(" S ")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(ModTags.Items.PLATE_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.MACHINE_FRAME.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .define('P', RegisterItems.MECHANICAL_PARTS.get())
                .define('N', Tags.Items.INGOTS_IRON)
                .pattern("NIN")
                .pattern("IPI")
                .pattern("NIN")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.MACHINE_COMPONENTBLOCK.get())
                .define('I', ModTags.Items.PLATE_INCANDESCENT_ALLOY)
                .define('C', ModTags.Items.PLATE_ALUMINIUM)
                .define('P', RegisterItems.BASIC_DEVICE.get())
                .define('N', RegisterBlocks.MACHINE_FRAME.get())
                .pattern("NIN")
                .pattern("CPC")
                .pattern("NIN")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.BASIC_REFINERY.get())
                .define('M', RegisterBlocks.MACHINE_FRAME.get())
                .define('K', ModTags.Items.PLATE_STEEL)
                .define('P', RegisterItems.STEEL_PIPE.get())
                .define('B', Items.BLAST_FURNACE)
                .define('S', Items.SMOOTH_STONE_SLAB)
                .pattern("KPP")
                .pattern("KMK")
                .pattern("BSS")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.ALLOY_FURNACE.get())
                .define('F', Items.FURNACE)
                .define('B', Blocks.BRICKS)
                .define('S', ModTags.Items.PLATE_STEEL)
                .pattern("SBS")
                .pattern("BFB")
                .pattern("SBS")
                .unlockedBy("has_furnace", has(Items.FURNACE))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.FOR_DESTABILIZER.get(),2)
                .define('O', Tags.Items.OBSIDIAN)
                .define('C', RegisterItems.ORUNDUM.get())
                .define('E', Items.ENDER_EYE)
                .pattern("OCO")
                .pattern("CEC")
                .pattern("OCO")
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))
                .unlockedBy("has_eye", has(Items.ENDER_EYE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.METAL_PRESS.get())
                .define('L', Items.LEVER)
                .define('R', Blocks.REDSTONE_BLOCK)
                .define('P', Items.PISTON)
                .define('S', RegisterBlocks.MACHINE_FRAME.get())
                .define('B', RegisterItems.PRIMITIVE_CIRCUIT.get())
                .define('W', RegisterItems.COPPER_WIRE.get())
                .define('C', RegisterItems.COPPER_COIL.get())
                .pattern("WBL")
                .pattern("PRP")
                .pattern("CSC")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.RECRUIT_BEACON.get())
                .define('L', RegisterItems.STEEL_PIPE.get())
                .define('R', ModTags.Items.PLATE_STEEL)
                .define('O', RegisterItems.ORUNDUM.get())
                .define('S', RegisterBlocks.MACHINE_FRAME.get())
                .define('B', RegisterItems.PRIMITIVE_CIRCUIT.get())
                .define('C', RegisterItems.COPPER_COIL.get())
                .pattern("  L")
                .pattern("ORC")
                .pattern("BSB")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_steel", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DUST_BRONZE.get(), 2)
                .define('C', ModTags.Items.DUST_COPPER)
                .define('T', ModTags.Items.DUST_TIN)
                .pattern("CC")
                .pattern("CT")
                .unlockedBy("has_copper", has(ModTags.Items.DUST_COPPER))
                .unlockedBy("has_tin", has(ModTags.Items.DUST_TIN))
                .save(consumer, new ResourceLocation("bronze_powder_alloying"));

        ShapedRecipeBuilder.shaped(RegisterItems.EQUIPMENT_GUN_127MM.get())
                .define('S', ModTags.Items.PLATE_STEEL)
                .define('B', RegisterItems.STEEL_PIPE.get())
                .define('P', RegisterItems.MECHANICAL_PARTS.get())
                .define('M', RegisterItems.PRIMITIVE_MOTOR.get())
                .define('E', RegisterItems.PRIMITIVE_CIRCUIT.get())
                .pattern("PSS")
                .pattern("BPS")
                .pattern("EME")
                .unlockedBy("has_circuit", has(RegisterItems.PRIMITIVE_CIRCUIT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DEFIB_PADDLE.get(), 1)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Items.STONE_BUTTON)
                .define('P', ModTags.Items.PLATE_PLASTIC)
                .define('C', RegisterItems.COPPER_COIL.get())
                .define('H', ModTags.Items.CIRCUITS_BASIC)
                .pattern("BPP")
                .pattern("PCH")
                .pattern("III")
                .unlockedBy("has_part", has(RegisterItems.PRIMITIVE_CIRCUIT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DEFIB_CHARGER.get(), 1)
                .define('W', RegisterItems.GOLD_COIL.get())
                .define('C', RegisterItems.CAPACITOR_ADVANCED.get())
                .define('P', Items.REDSTONE_LAMP)
                .define('I',  ModTags.Items.CIRCUITS_ADVANCED)
                .define('B', RegisterItems.CRYSTALLINE_CIRCUIT.get())
                .define('R', RegisterItems.RESISTOR_BASIC.get())
                .pattern("WPW")
                .pattern("RBR")
                .pattern("CIC")
                .unlockedBy("has_paddle", has(RegisterItems.DEFIB_PADDLE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MOLD_EXTRACTION.get(), 1)
                .define('N', Tags.Items.NUGGETS_IRON)
                .define('P', ModTags.Items.PLATE_IRON)
                .define('M', RegisterItems.MOLD_PLATE.get())
                .pattern("PNP")
                .pattern("PMP")
                .pattern("NPN")
                .unlockedBy("has_part", has(ModTags.Items.PLATE_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.OAK_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.OAK_LOGS)
                .define('P', Items.OAK_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.OAK_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.ACACIA_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.ACACIA_LOGS)
                .define('P', Items.ACACIA_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.ACACIA_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.BIRCH_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.BIRCH_LOGS)
                .define('P', Items.BIRCH_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.BIRCH_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.SPRUCE_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.SPRUCE_LOGS)
                .define('P', Items.SPRUCE_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.SPRUCE_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.DARK_OAK_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.DARK_OAK_LOGS)
                .define('P', Items.DARK_OAK_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.DARK_OAK_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.JUNGLE_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.JUNGLE_LOGS)
                .define('P', Items.JUNGLE_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.JUNGLE_LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.WARPED_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.WARPED_STEMS)
                .define('P', Items.WARPED_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.WARPED_STEMS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.CRIMSON_PANTRY.get(), 1)
                .define('C', Tags.Items.CHESTS)
                .define('L', ItemTags.CRIMSON_STEMS)
                .define('P', Items.CRIMSON_PLANKS)
                .pattern("LPL")
                .pattern("PCP")
                .pattern("LPL")
                .unlockedBy("has_part", has(ItemTags.CRIMSON_STEMS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getStackForm().getItem(), 1)
                .define('F', RegisterBlocks.MACHINE_FRAME.get().asItem())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('P', Blocks.PISTON.asItem())
                .define('G', RegisterItems.ADVANCED_DEVICE.get())
                .define('I', RegisterItems.CRYSTALLINE_CIRCUIT.get())
                .define('D', RegisterItems.ADVANCED_MOTOR.get())
                .pattern("PDP")
                .pattern("IGI")
                .pattern("CFC")
                .unlockedBy("has_part", has(RegisterItems.ADVANCED_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(HatchTE.ItemHatchDefinition.getStackForm().getItem(), 4)
                .define('F', RegisterBlocks.MACHINE_FRAME.get().asItem())
                .define('C', Tags.Items.CHESTS)
                .define('D', RegisterItems.PRIMITIVE_DEVICE.get())
                .pattern(" F ")
                .pattern("DCD")
                .pattern(" F ")
                .unlockedBy("has_part", has(RegisterBlocks.MACHINE_FRAME.get().asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.MACHINE_DYNAMO.get(), 1)
                .define('F', RegisterBlocks.MACHINE_FRAME.get().asItem())
                .define('C', RegisterItems.COPPER_COIL.get())
                .define('P', ModTags.Items.PLATE_ALUMINIUM)
                .define('I', Tags.Items.INGOTS_IRON)
                .pattern("PCP")
                .pattern("IFI")
                .pattern("PCP")
                .unlockedBy("has_part", has(RegisterBlocks.MACHINE_FRAME.get().asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(HatchTE.EnergyHatchDefinition.getStackForm().getItem(), 4)
                .define('F', RegisterBlocks.MACHINE_FRAME.get().asItem())
                .define('C', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('D', RegisterItems.PRIMITIVE_DEVICE.get())
                .pattern(" F ")
                .pattern("DCD")
                .pattern(" F ")
                .unlockedBy("has_part", has(RegisterBlocks.MACHINE_FRAME.get().asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(HatchTE.FluidHatchDefinition.getStackForm().getItem(), 4)
                .define('F', RegisterBlocks.MACHINE_FRAME.get().asItem())
                .define('C', Items.BUCKET)
                .define('D', RegisterItems.PRIMITIVE_DEVICE.get())
                .pattern(" F ")
                .pattern("DCD")
                .pattern(" F ")
                .unlockedBy("has_part", has(RegisterBlocks.MACHINE_FRAME.get().asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(HatchTE.EntityDefinition.getStackForm().getItem(), 4)
                .define('F', RegisterBlocks.MACHINE_FRAME.get().asItem())
                .define('C', RegisterItems.FOR_DESTABILIZER.get())
                .define('D', RegisterItems.BASIC_DEVICE.get())
                .pattern(" F ")
                .pattern("DCD")
                .pattern(" F ")
                .unlockedBy("has_part", has(RegisterItems.FOR_DESTABILIZER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.KYARU_STAFF.get(), 1)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', ModTags.Items.INGOT_BRASS)
                .define('B', Items.ENCHANTED_BOOK)
                .pattern(" CB")
                .pattern(" SC")
                .pattern("S  ")
                .unlockedBy("has_part", has(Items.ENCHANTED_BOOK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.IRON_PIPE.get(), 1)
                .define('A', ModTags.Items.PLATE_IRON)
                .pattern("A")
                .pattern("A")
                .unlockedBy("has_ironplate", has(ModTags.Items.PLATE_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.STEEL_PIPE.get(), 1)
                .define('A', ModTags.Items.PLATE_STEEL)
                .pattern("A")
                .pattern("A")
                .unlockedBy("has_steelplate", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DD_DEFAULT_RIGGING.get(), 1)
                .define('A', ModTags.Items.BLOCK_D32)
                .define('S', RegisterItems.COGNITIVE_ARRAY.get())
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', RegisterItems.ADVANCED_DEVICE.get())
                .define('C', ModTags.Items.CIRCUITS_CRYSTALLINE)
                .define('M', RegisterItems.ADVANCED_MOTOR.get())
                .define('W', RegisterItems.WISDOM_CUBE.get())
                .pattern("CSP")
                .pattern("AWA")
                .pattern("GMG")
                .unlockedBy("has_cube", has(RegisterItems.WISDOM_CUBE.get()))
                .unlockedBy("has_motor", has(RegisterItems.ADVANCED_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.BB_DEFAULT_RIGGING.get(), 1)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .define('S', ModTags.Items.PLATE_D32)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', RegisterItems.ADVANCED_DEVICE.get())
                .define('C', ModTags.Items.CIRCUITS_CRYSTALLINE)
                .define('M', RegisterItems.ADVANCED_MOTOR.get())
                .define('D', RegisterItems.DD_DEFAULT_RIGGING.get())
                .pattern("SMS")
                .pattern("PDG")
                .pattern("ACA")
                .unlockedBy("has_dd_rigging", has(RegisterItems.DD_DEFAULT_RIGGING.get()))
                .unlockedBy("has_motor", has(RegisterItems.ADVANCED_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CV_DEFAULT_RIGGING.get(), 1)
                .define('W', RegisterBlocks.REENFORCED_PLANK.get().asItem())
                .define('M', RegisterItems.ADVANCED_MOTOR.get())
                .define('D', RegisterItems.DD_DEFAULT_RIGGING.get())
                .define('P', RegisterItems.ADVANCED_DEVICE.get())
                .define('C', ModTags.Items.CIRCUITS_CRYSTALLINE)
                .define('S', ModTags.Items.BLOCK_STEEL)
                .pattern("SSW")
                .pattern("SDW")
                .pattern("PCM")
                .unlockedBy("has_dd_rigging", has(RegisterItems.DD_DEFAULT_RIGGING.get()))
                .unlockedBy("has_motor", has(RegisterItems.ADVANCED_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.EQUIPMENT_TORPEDO_533MM.get(), 1)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .define('S', RegisterItems.STEEL_PIPE.get())
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', RegisterItems.MECHANICAL_PARTS.get())
                .define('C', ModTags.Items.CIRCUITS_ADVANCED)
                .define('M', RegisterItems.PRIMITIVE_MOTOR.get())
                .pattern("ACA")
                .pattern("SSS")
                .pattern("GMP")
                .unlockedBy("has_barrel", has(RegisterItems.STEEL_PIPE.get()))
                .unlockedBy("has_part", has(RegisterItems.MECHANICAL_PARTS.get()))
                .unlockedBy("has_motor", has(RegisterItems.PRIMITIVE_MOTOR.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RegisterItems.SHEATH.get())
                .define('B', ModTags.Items.PLATE_STEEL)
                .define('L', RegisterItems.STEEL_PIPE.get())
                .pattern("  B")
                .pattern(" B ")
                .pattern("L  ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.FLEXABLE_SWORD_THINGY.get())
                .define('B', ModTags.Items.PLATE_RMA7012)
                .define('L', RegisterItems.C99_CARBON.get())
                .pattern("  B")
                .pattern(" B ")
                .pattern("L  ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CHIXIAO.get())
                .define('I', RegisterItems.AMBER_ORIGINIUM.get())
                .define('S', RegisterItems.C99_CARBON.get())
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .unlockedBy("has_material", has(RegisterItems.AMBER_ORIGINIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.CRYSTAL_GROWTH_CHAMBER.get(), 1)
                .define('S', Blocks.SMOOTH_STONE_SLAB)
                .define('C', Blocks.CAULDRON)
                .define('P', RegisterItems.STEEL_PIPE.get())
                .define('I', ModTags.Items.PLATE_STEEL)
                .pattern("III")
                .pattern("CPC")
                .pattern("SSS")
                .unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.NETHER_QUARTZ_SEED.get(), 4)
                .define('S', ItemTags.SAND)
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .pattern("SQ")
                .pattern("QS")
                .unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.ORIGINIUM_SEED.get(), 4)
                .define('S', ItemTags.SAND)
                .define('O', RegisterItems.DUST_ORIGINIUM.get())
                .pattern("SO")
                .pattern("OS")
                .unlockedBy("has_originium", has(RegisterItems.DUST_ORIGINIUM.get()))
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RegisterItems.COMMANDING_STICK.get(), 1)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('B', Tags.Items.DYES_BLACK)
                .pattern("  S")
                .pattern(" S ")
                .pattern("B  ")
                .unlockedBy("has_sticc", has(Tags.Items.RODS_WOODEN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.AMMO_GENERIC.get(), 2)
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

        ShapedRecipeBuilder.shaped(RegisterItems.CRESCENTKATANA_KURO.get())
                .define('B', ModTags.Items.INGOT_STEEL)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('L', RegisterItems.STEEL_PIPE.get())
                .pattern("  B")
                .pattern(" BP")
                .pattern("LP ")
                .unlockedBy("has_ingot", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CRESCENTKATANA_SHIRO.get())
                .define('B', Tags.Items.INGOTS_IRON)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('L', RegisterItems.STEEL_PIPE.get())
                .pattern("  B")
                .pattern(" BP")
                .pattern("LP ")
                .unlockedBy("has_ingot", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.WARHAMMER.get())
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('S', ModTags.Items.INGOT_STEEL)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('L', RegisterItems.STEEL_PIPE.get())
                .pattern("BSP")
                .pattern(" L ")
                .pattern(" L ")
                .unlockedBy("has_ingot", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.AMMO_TORPEDO.get(), 4)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .define('T', RegisterItems.GUNPOWDER_COMPOUND.get())
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('S', ModTags.Items.PLATE_STEEL)
                .pattern(" AT")
                .pattern("ACA")
                .pattern("SA ")
                .unlockedBy("has_aluminium", has(ModTags.Items.INGOT_ALUMINIUM))
                .unlockedBy("has_circuit", has(ModTags.Items.CIRCUITS_BASIC))
                .unlockedBy("has_tnt", has(RegisterItems.GUNPOWDER_COMPOUND.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.AMMO_MISSILE.get(), 4)
                .define('A', ModTags.Items.INGOT_ALUMINIUM)
                .define('T', RegisterItems.GUNPOWDER_COMPOUND.get())
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('S', ModTags.Items.EXPLOSIVE_COMPOUND)
                .pattern(" AT")
                .pattern("ACA")
                .pattern("SA ")
                .unlockedBy("has_aluminium", has(ModTags.Items.INGOT_ALUMINIUM))
                .unlockedBy("has_circuit", has(ModTags.Items.CIRCUITS_BASIC))
                .unlockedBy("has_tnt", has(RegisterItems.GUNPOWDER_COMPOUND.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DISC_FRIDAYNIGHT.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.DYES_GREEN)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.DYES_GREEN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DISC_REVENGE.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.GUNPOWDER)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.GUNPOWDER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DISC_FALLEN_KINGDOM.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Items.GOLDEN_SWORD)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Items.GOLDEN_SWORD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DISC_FIND_THE_PIECES.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Tags.Items.GEMS_EMERALD)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Tags.Items.GEMS_EMERALD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DISC_TAKE_BACK_THE_NIGHT.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Blocks.CRYING_OBSIDIAN.asItem())
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Blocks.CRYING_OBSIDIAN.asItem()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DISC_DRAGONHEARTED.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', Items.ENDER_EYE)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_dye", has(Items.ENDER_EYE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.REENFORCED_PLANK.get(), 4)
                .define('P', ItemTags.PLANKS)
                .define('S', ModTags.Items.INGOT_STEEL)
                .pattern("SPS")
                .pattern("PSP")
                .pattern("SPS")
                .unlockedBy("has_steel", has(ModTags.Items.INGOT_STEEL))
                .unlockedBy("has_plank", has(ItemTags.PLANKS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DISC_CC5.get(), 1)
                .define('S', ModTags.Items.PLATE_IRON)
                .define('D', ModTags.Items.ORIGINIUM_PRIME)
                .pattern(" S ")
                .pattern("SDS")
                .pattern(" S ")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_IRON))
                .unlockedBy("has_sand", has(ModTags.Items.ORIGINIUM_PRIME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CAPACITOR_ADVANCED.get(), 2)
                .define('A', RegisterItems.PLATE_ALUMINIUM.get())
                .define('P', ModTags.Items.PLATE_PLASTIC)
                .define('G', ModTags.Items.PLATE_GOLD)
                .define('C', RegisterItems.GOLD_WIRE.get())
                .pattern("GPG")
                .pattern("APA")
                .pattern("C C")
                .unlockedBy("has_wire", has(RegisterItems.GOLD_WIRE.get()))
                .unlockedBy("has_aluminium", has(ModTags.Items.PLATE_ALUMINIUM))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CAPACITOR_PRIMITIVE.get(), 1)
                .define('A', RegisterItems.PLATE_ALUMINIUM.get())
                .define('P', Items.PAPER)
                .define('C', RegisterItems.COPPER_WIRE.get())
                .pattern("APA")
                .pattern("APA")
                .pattern("C C")
                .unlockedBy("has_wire", has(RegisterItems.COPPER_WIRE.get()))
                .unlockedBy("has_aluminium", has(ModTags.Items.PLATE_ALUMINIUM))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RESISTOR_PRIMITIVE.get(), 2)
                .define('C', ModTags.Items.DUST_COAL)
                .define('D', RegisterItems.DUST_IRON.get())
                .define('P', Items.PAPER)
                .define('W', RegisterItems.COPPER_WIRE.get())
                .pattern(" C ")
                .pattern("WPW")
                .pattern(" D ")
                .unlockedBy("has_wire", has(RegisterItems.COPPER_WIRE.get()))
                .unlockedBy("has_carbon", has(ModTags.Items.DUST_COAL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RESISTOR_BASIC.get(), 2)
                .define('C', RegisterItems.C99_CARBON.get())
                .define('D', ModTags.Items.DUST_IRON)
                .define('R', ModTags.Items.PLATE_PLASTIC)
                .define('W', RegisterItems.COPPER_WIRE.get())
                .pattern(" C ")
                .pattern("WRW")
                .pattern(" D ")
                .unlockedBy("has_wire", has(RegisterItems.COPPER_WIRE.get()))
                .unlockedBy("has_carbon", has(ModTags.Items.DUST_COAL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RESISTOR_ADVANCED.get(), 2)
                .define('C', RegisterItems.C99_CARBON.get())
                .define('D', Items.CLAY)
                .define('R', ModTags.Items.PLATE_PLASTIC)
                .define('W', RegisterItems.GOLD_WIRE.get())
                .pattern(" C ")
                .pattern("WRW")
                .pattern(" D ")
                .unlockedBy("has_wire", has(RegisterItems.COPPER_WIRE.get()))
                .unlockedBy("has_carbon", has(ModTags.Items.DUST_COAL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.PRIMITIVE_MOTOR.get(), 2)
                .define('C', RegisterItems.COPPER_COIL.get())
                .define('D', Items.IRON_INGOT)
                .define('P', RegisterItems.PLATE_IRON.get())
                .define('W', RegisterItems.COPPER_WIRE.get())
                .pattern(" D ")
                .pattern("PCP")
                .pattern("PWP")
                .unlockedBy("has_coil", has(RegisterItems.COPPER_COIL.get()))
                .unlockedBy("has_wire", has(RegisterItems.COPPER_WIRE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.BASIC_MOTOR.get(), 2)
                .define('C', RegisterItems.COPPER_COIL.get())
                .define('D', Items.IRON_INGOT)
                .define('P', RegisterItems.PLATE_ALUMINIUM.get())
                .define('W', RegisterItems.GOLD_COIL.get())
                .pattern(" D ")
                .pattern("PCP")
                .pattern("PWP")
                .unlockedBy("has_coil", has(RegisterItems.GOLD_COIL.get()))
                .unlockedBy("has_wire", has(RegisterItems.COPPER_WIRE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.ADVANCED_MOTOR.get(), 1)
                .define('M', RegisterItems.BASIC_MOTOR.get())
                .define('G', RegisterItems.GOLD_COIL.get())
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('R', RegisterItems.RESISTOR_BASIC.get())
                .define('D', RegisterItems.IRON_PIPE.get())
                .pattern(" D ")
                .pattern("RMR")
                .pattern("GCG")
                .unlockedBy("has_motor", has(RegisterItems.BASIC_MOTOR.get()))
                .unlockedBy("has_circuit", has(ModTags.Items.CIRCUITS_BASIC))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.PRIMITIVE_CIRCUIT.get(), 1)
                .define('C', ModTags.Items.PLATE_COPPER)
                .define('P', ModTags.Items.PLATE_PLASTIC)
                .define('I', ModTags.Items.PLATE_IRON)
                .define('L', Items.REDSTONE)
                .define('R', RegisterItems.RESISTOR_PRIMITIVE.get())
                .define('T', RegisterItems.CAPACITOR_PRIMITIVE.get())
                .pattern(" I ")
                .pattern("RLT")
                .pattern("CPC")
                .unlockedBy("has_plate", has(ModTags.Items.PLATE_COPPER))
                .unlockedBy("has_polymer", has(ModTags.Items.PLATE_PLASTIC))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.MECHANICAL_PARTS.get(), 2)
                .define('G', ModTags.Items.GEAR_STEEL)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('C', ModTags.Items.GEAR_BRONZE)
                .define('I', Tags.Items.INGOTS_IRON)
                .pattern("GPG")
                .pattern("CIC")
                .pattern("GPG")
                .unlockedBy("has_gear", has(ModTags.Items.GEAR_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CLAYMORE.get(), 1)
                .define('S', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('P', RegisterItems.IRON_PIPE.get())
                .pattern("PP")
                .pattern("SS")
                .pattern("SS")
                .unlockedBy("has_gear", has(Tags.Items.STORAGE_BLOCKS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.ORUNDUM.get(), 2)
                .define('C', ModTags.Items.WIRE_COPPER)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('O', ModTags.Items.ORIGINITE)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .pattern("COC")
                .pattern("ORO")
                .pattern("CPC")
                .unlockedBy("has_originite", has(ModTags.Items.ORIGINITE))
                .save(consumer, "orundum_from_originite");

        ShapedRecipeBuilder.shaped(RegisterItems.ORUNDUM.get(), 4)
                .define('C', ModTags.Items.WIRE_COPPER)
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('O', ModTags.Items.ORIGINIUM_PRIME)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .pattern("COC")
                .pattern("ORO")
                .pattern("CPC")
                .unlockedBy("has_originite", has(ModTags.Items.ORIGINIUM_PRIME))
                .save(consumer, "orundum_from_prime");

        ShapedRecipeBuilder.shaped(RegisterItems.CRYSTALLINE_COMPONENT.get(), 2)
                .define('C', RegisterItems.VITRIFIED_ORIGINIUM.get())
                .define('P', ModTags.Items.PLATE_STEEL)
                .define('O', RegisterItems.ORUNDUM.get())
                .pattern("OCO")
                .pattern("PCP")
                .pattern("OCO")
                .unlockedBy("has_material", has(RegisterItems.VITRIFIED_ORIGINIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.CRYSTALLINE_CIRCUIT.get(), 2)
                .define('P', RegisterItems.CRYSTALLINE_COMPONENT.get())
                .define('A', ModTags.Items.CIRCUITS_ADVANCED)
                .define('S', ModTags.Items.PLATE_RMA7024)
                .define('O', RegisterItems.ORUNDUM.get())
                .define('C', RegisterItems.COGNITIVE_CHIP.get())
                .pattern("POC")
                .pattern("SAS")
                .unlockedBy("has_material", has(RegisterItems.CRYSTALLINE_COMPONENT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.COGNITIVE_CHIP.get(), 2)
                .define('O', RegisterItems.VITRIFIED_ORIGINIUM.get())
                .define('A', RegisterItems.CRYSTALLINE_COMPONENT.get())
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .pattern("QOQ")
                .pattern("OAO")
                .pattern("QOQ")
                .unlockedBy("has_material", has(RegisterItems.CRYSTALLINE_COMPONENT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.COGNITIVE_ARRAY.get(), 2)
                .define('O', RegisterItems.VITRIFIED_ORIGINIUM.get())
                .define('A', RegisterItems.CRYSTALLINE_CIRCUIT.get())
                .define('Q', RegisterItems.COGNITIVE_CHIP.get())
                .pattern("QOQ")
                .pattern("OAO")
                .pattern("QOQ")
                .unlockedBy("has_material", has(RegisterItems.COGNITIVE_CHIP.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.WISDOM_CUBE.get())
                .define('O', RegisterItems.COGNITIVE_ARRAY.get())
                .define('Q', RegisterItems.COGNITIVE_CHIP.get())
                .define('L', Tags.Items.GEMS_LAPIS)
                .pattern("QOQ")
                .pattern("LQL")
                .pattern("QOQ")
                .unlockedBy("has_material", has(RegisterItems.COGNITIVE_ARRAY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.COMPUTERCORE.get())
                .define('A', ModTags.Items.CIRCUITS_ADVANCED)
                .define('O', RegisterItems.COGNITIVE_ARRAY.get())
                .define('C', RegisterItems.CRYSTALLINE_CIRCUIT.get())
                .define('G', Tags.Items.GLASS_BLACK)
                .define('Q', ModTags.Items.INGOT_ALUMINIUM)
                .pattern("QAQ")
                .pattern("OGC")
                .pattern("QAQ")
                .unlockedBy("has_material", has(RegisterItems.COGNITIVE_ARRAY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.HEADHUNTING_PCB.get())
                .define('P', Items.ENDER_PEARL)
                .define('E', Items.ENDER_EYE)
                .define('R', ModTags.Items.CIRCUITS_BASIC)
                .define('O', RegisterItems.ORUNDUM.get())
                .pattern("EP")
                .pattern("OR")
                .unlockedBy("has_material", has(RegisterItems.ORUNDUM.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RegisterItems.BANDAGE_ROLL.get(), 4)
                .define('W', ItemTags.WOOL)
                .pattern("WWW")
                .pattern("W W")
                .pattern("WWW")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.ADVANCED_CIRCUIT.get(), 2)
                .define('C', ModTags.Items.CIRCUITS_BASIC)
                .define('O', RegisterItems.ORUNDUM.get())
                .define('R', RegisterItems.RESISTOR_BASIC.get())
                .define('T', RegisterItems.CAPACITOR_ADVANCED.get())
                .define('K', ModTags.Items.PLATE_PLASTIC)
                .pattern("ROT")
                .pattern("CKC")
                .unlockedBy("has_orundum", has(RegisterItems.ORUNDUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.SLEDGEHAMMER.get())
                .define('S', RegisterItems.STEEL_PIPE.get())
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RiftwayControllerTE.RiftwayDefinition.getStackForm().getItem())
                .define('C', RegisterItems.COMPUTERCORE.get())
                .define('W', RegisterItems.WISDOM_CUBE.get())
                .define('P', RegisterItems.CRYSTALLINE_CIRCUIT.get())
                .define('M', RegisterItems.ADVANCED_DEVICE.get())
                .define('O', RegisterItems.ORUNDUM.get())
                .define('D', RegisterItems.FOR_DESTABILIZER.get())
                .define('L', RegisterBlocks.MACHINE_DYNAMO.get())
                .pattern("LDL")
                .pattern("COW")
                .pattern("PMP")
                .unlockedBy("has_item", has(RegisterItems.FOR_DESTABILIZER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getStackForm().getItem())
                .define('C', RegisterBlocks.MACHINE_DYNAMO.get())
                .define('D', RegisterItems.PRIMITIVE_DEVICE.get())
                .define('I', RegisterBlocks.INCANDESCENT_ALLOY_BLOCK.get())
                .define('F', RegisterBlocks.MACHINE_FRAME.get())
                .define('P', ModTags.Items.CIRCUITS_BASIC)
                .pattern("CIC")
                .pattern("CDC")
                .pattern("PFP")
                .unlockedBy("has_item", has(RegisterBlocks.INCANDESCENT_ALLOY_BLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(AmmoPressControllerTE.AmmoPressDefinition.getStackForm().getItem())
                .define('C', RegisterBlocks.MACHINE_DYNAMO.get())
                .define('D', RegisterItems.BASIC_DEVICE.get())
                .define('I', Blocks.PISTON)
                .define('M', RegisterItems.BASIC_MOTOR.get())
                .define('O', RegisterItems.ORUNDUM.get())
                .define('P', ModTags.Items.CIRCUITS_BASIC)
                .pattern("CIC")
                .pattern("MDM")
                .pattern("POP")
                .unlockedBy("has_item", has(RegisterItems.BASIC_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.DRONE_BAMISSILE.get())
                .define('C', RegisterItems.COMPUTERCORE.get())
                .define('M', RegisterItems.BASIC_MOTOR.get())
                .define('D', RegisterItems.BASIC_DEVICE.get())
                .define('I', ModTags.Items.PLATE_ALUMINIUM)
                .define('P', ModTags.Items.CIRCUITS_ADVANCED)
                .pattern("DID")
                .pattern("MCM")
                .pattern("PIP")
                .unlockedBy("has_item", has(RegisterItems.BASIC_MOTOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.EQUIPMENT_PLANE_F4FWildcat.get())
                .define('C', RegisterItems.COMPUTERCORE.get())
                .define('M', RegisterItems.BASIC_MOTOR.get())
                .define('D', RegisterItems.BASIC_DEVICE.get())
                .define('I', ModTags.Items.PLATE_ALUMINIUM)
                .define('P', ModTags.Items.CIRCUITS_ADVANCED)
                .pattern(" M ")
                .pattern("ICI")
                .pattern("DPD")
                .unlockedBy("has_item", has(RegisterItems.BASIC_MOTOR.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RegisterItems.RMA7012_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RMA7012_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RMA7012_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RMA7012_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RMA7012_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7012)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7012))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RegisterItems.RMA7024_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RMA7024_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RMA7024_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RMA7024_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.RMA7024_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_RMA7024)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_RMA7024))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RegisterItems.D32_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.D32_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.D32_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.D32_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.D32_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_D32)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_D32))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.STEEL_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_STEEL)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.STEEL_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_STEEL)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.STEEL_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_STEEL)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.STEEL_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_STEEL)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.STEEL_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_STEEL)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.COPPER_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_COPPER)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_COPPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.COPPER_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_COPPER)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_COPPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.COPPER_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_COPPER)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_COPPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.COPPER_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_COPPER)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_COPPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.COPPER_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_COPPER)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_COPPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.TIN_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_TIN)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_TIN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.TIN_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_TIN)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_TIN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.TIN_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_TIN)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_TIN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.TIN_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_TIN)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_TIN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.TIN_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_TIN)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_TIN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.BRONZE_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_BRONZE)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_BRONZE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.BRONZE_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_BRONZE)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_BRONZE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.BRONZE_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_BRONZE)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_BRONZE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.BRONZE_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_BRONZE)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_BRONZE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.BRONZE_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_BRONZE)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_BRONZE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.LEAD_sword.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_LEAD)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_LEAD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.LEAD_pickaxe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_LEAD)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_LEAD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.LEAD_axe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_LEAD)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_LEAD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.LEAD_hoe.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_LEAD)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_LEAD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.LEAD_shovel.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', ModTags.Items.INGOT_LEAD)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_material", has(ModTags.Items.INGOT_LEAD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.AMBER_ORIGINIUM_FUEL_ROD.get(), 3)
                .define('S', RegisterItems.AMBER_ORIGINIUM.get())
                .define('I', RegisterItems.C99_CARBON.get())
                .define('P', ModTags.Items.DUST_COAL)
                .pattern("SIS")
                .pattern("SPS")
                .pattern("SIS")
                .unlockedBy("has_material", has(RegisterItems.AMBER_ORIGINIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterItems.SYRINGE.get())
                .define('S', ModTags.Items.INGOT_ALUMINIUM)
                .define('P', ModTags.Items.PLATE_PLASTIC)
                .define('G', Tags.Items.GLASS_COLORLESS)
                .define('A', ModTags.Items.PLATE_ALUMINIUM)
                .pattern("PAP")
                .pattern("PGP")
                .pattern(" S ")
                .unlockedBy("has_material1", has(Items.POTION))
                .unlockedBy("has_material2", has(ModTags.Items.INGOT_ALUMINIUM))
                .save(consumer);


    }

    private void BuildMetalRecipe(Consumer<IFinishedRecipe> consumer, float smeltingXp, Metals metal) {
        if (metal.ore != null) {
            CookingRecipeBuilder.blasting(Ingredient.of(metal.oreTag), metal.ingot, smeltingXp, 100)
                    .unlockedBy("has_item", has(metal.oreTag))
                    .save(consumer, ResourceUtils.ModResourceLocation(metal.name + "_ore_blasting"));
            CookingRecipeBuilder.smelting(Ingredient.of(metal.oreTag), metal.ingot, smeltingXp, 200)
                    .unlockedBy("has_item", has(metal.oreTag))
                    .save(consumer, ResourceUtils.ModResourceLocation(metal.name + "_ore_smelting"));
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

            PressingRecipeBuilder.addRecipe(metal.plate, Ingredient.of(metal.ingotTag), Ingredient.of(RegisterItems.MOLD_PLATE.get()), 1, 200)
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

    protected void registerSawingRecipe(ITag<Item> log, Item plank, Consumer<IFinishedRecipe> consumer){
        ShapelessRecipeBuilder.shapeless(plank, 6)
                .requires(log)
                .requires(ModTags.Items.SAW)
                .unlockedBy("has_tool", has(ModTags.Items.SAW))
                .save(consumer, ForgeRegistries.ITEMS.getKey(plank) +"_pa_saw");
    }
}
