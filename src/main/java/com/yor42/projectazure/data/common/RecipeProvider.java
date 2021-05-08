package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.data.recipebuilder.AlloyingRecipeBuilder;
import com.yor42.projectazure.data.recipebuilder.PressingRecipeBuilder;
import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.CallbackI;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        BuildMetalRecipe(consumer, 0.5F, new Metals("copper", registerItems.INGOT_COPPER.get(), ModTags.Items.INGOT_COPPER).ore(registerBlocks.COPPER_ORE.get().asItem(), ModTags.Items.ORES_COPPER).dust(registerItems.DUST_COPPER.get(), ModTags.Items.DUST_COPPER).plates(registerItems.PLATE_COPPER.get(), ModTags.Items.PLATE_COPPER).gear(registerItems.GEAR_COPPER.get(), ModTags.Items.GEAR_COPPER));
        BuildMetalRecipe(consumer, 0.5F, new Metals("tin", registerItems.INGOT_TIN.get(), ModTags.Items.INGOT_TIN).ore(registerBlocks.TIN_ORE.get().asItem(), ModTags.Items.ORES_TIN).dust(registerItems.DUST_TIN.get(), ModTags.Items.DUST_TIN).plates(registerItems.PLATE_TIN.get(), ModTags.Items.PLATE_TIN).gear(registerItems.GEAR_TIN.get(), ModTags.Items.GEAR_TIN));
        BuildMetalRecipe(consumer, 0.5F, new Metals("lead", registerItems.INGOT_LEAD.get(), ModTags.Items.INGOT_LEAD).ore(registerBlocks.LEAD_ORE.get().asItem(), ModTags.Items.ORES_LEAD).dust(registerItems.DUST_LEAD.get(), ModTags.Items.DUST_LEAD).plates(registerItems.PLATE_LEAD.get(), ModTags.Items.PLATE_LEAD));
        BuildMetalRecipe(consumer, 0.5F, new Metals("bronze", registerItems.INGOT_BRONZE.get(), ModTags.Items.INGOT_BRONZE).dust(registerItems.DUST_BRONZE.get(), ModTags.Items.DUST_BRONZE).plates(registerItems.PLATE_BRONZE.get(), ModTags.Items.PLATE_BRONZE).gear(registerItems.GEAR_BRONZE.get(), ModTags.Items.GEAR_BRONZE));
        BuildMetalRecipe(consumer, 0.5F, new Metals("aluminium", registerItems.INGOT_ALUMINIUM.get(), ModTags.Items.INGOT_ALUMINIUM).ore(registerBlocks.BAUXITE_ORE.get().asItem(), ModTags.Items.ORES_ALUMINIUM).dust(registerItems.DUST_ALUMINIUM.get(), ModTags.Items.DUST_ALUMINIUM).plates(registerItems.PLATE_ALUMINIUM.get(), ModTags.Items.PLATE_ALUMINIUM));
        BuildMetalRecipe(consumer, 0.5F, new Metals("steel", registerItems.INGOT_STEEL.get(), ModTags.Items.INGOT_STEEL).dust(registerItems.DUST_STEEL.get(), ModTags.Items.DUST_STEEL).plates(registerItems.PLATE_STEEL.get(), ModTags.Items.PLATE_STEEL).gear(registerItems.GEAR_STEEL.get(), ModTags.Items.GEAR_STEEL));

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
                .patternLine(" SS")
                .patternLine("BPS")
                .patternLine("EME")
                .addCriterion("has_steel", hasItem(registerItems.PRIMITIVE_CIRCUIT.get()))
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

        PressingRecipeBuilder.addRecipe(registerItems.TREE_SAP.get(), Ingredient.fromTag(ModTags.Items.LOG), Ingredient.fromItems(registerItems.MOLD_EXTRACTION.get()), 1, 200)
                .addCriterion("hasmold", hasItem(registerItems.MOLD_EXTRACTION.get()))
                .build(consumer, new ResourceLocation("iron_plate_pressing"));

        ShapedRecipeBuilder.shapedRecipe(registerItems.TREE_SAP.get(),4)
                .key('L', ModTags.Items.LOG)
                .patternLine("LLL")
                .patternLine("LLL")
                .patternLine("LLL")
                .addCriterion("has_log", hasItem(ModTags.Items.LOG))
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
