package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        BuildMetalRecipe(consumer, 0.5F, new Metals("copper", registerItems.INGOT_COPPER.get(), ModTags.Items.INGOT_COPPER).ore(registerBlocks.COPPER_ORE.get().asItem(), ModTags.Items.ORES_COPPER).dust(registerItems.DUST_COPPER.get(), ModTags.Items.DUST_COPPER).plates(registerItems.PLATE_COPPER.get(), ModTags.Items.PLATE_COPPER));
        BuildMetalRecipe(consumer, 0.5F, new Metals("tin", registerItems.INGOT_TIN.get(), ModTags.Items.INGOT_TIN).ore(registerBlocks.TIN_ORE.get().asItem(), ModTags.Items.ORES_TIN).dust(registerItems.DUST_TIN.get(), ModTags.Items.DUST_TIN).plates(registerItems.PLATE_TIN.get(), ModTags.Items.PLATE_TIN));
        BuildMetalRecipe(consumer, 0.5F, new Metals("lead", registerItems.INGOT_LEAD.get(), ModTags.Items.INGOT_LEAD).ore(registerBlocks.LEAD_ORE.get().asItem(), ModTags.Items.ORES_LEAD).dust(registerItems.DUST_LEAD.get(), ModTags.Items.DUST_LEAD).plates(registerItems.PLATE_LEAD.get(), ModTags.Items.PLATE_LEAD));
        BuildMetalRecipe(consumer, 0.5F, new Metals("bronze", registerItems.INGOT_BRONZE.get(), ModTags.Items.INGOT_BRONZE).dust(registerItems.DUST_BRONZE.get(), ModTags.Items.DUST_BRONZE).plates(registerItems.PLATE_BRONZE.get(), ModTags.Items.PLATE_BRONZE));
        BuildMetalRecipe(consumer, 0.5F, new Metals("aluminium", registerItems.INGOT_ALUMINIUM.get(), ModTags.Items.INGOT_ALUMINIUM).ore(registerBlocks.BAUXITE_ORE.get().asItem(), ModTags.Items.ORES_ALUMINIUM).dust(registerItems.DUST_ALUMINIUM.get(), ModTags.Items.DUST_ALUMINIUM).plates(registerItems.PLATE_ALUMINIUM.get(), ModTags.Items.PLATE_ALUMINIUM));

        BuildMetalRecipe(consumer, 0.5F, new Metals("iron", Items.IRON_INGOT, Tags.Items.INGOTS_IRON).dust(registerItems.DUST_IRON.get(), ModTags.Items.DUST_IRON).plates(registerItems.PLATE_IRON.get(), ModTags.Items.PLATE_IRON));

        ShapedRecipeBuilder.shapedRecipe(registerItems.DUST_BRONZE.get(), 2)
                .key('C', ModTags.Items.DUST_COPPER)
                .key('T', ModTags.Items.DUST_TIN)
                .patternLine("CC")
                .patternLine("CT")
                .addCriterion("has_copper", hasItem(ModTags.Items.DUST_COPPER))
                .addCriterion("has_tin", hasItem(ModTags.Items.DUST_TIN))
                .build(consumer, new ResourceLocation("bronze_powder_alloying"));

        ShapedRecipeBuilder.shapedRecipe(registerItems.DUST_STEEL.get(), 2)
                .key('I', ModTags.Items.DUST_IRON)
                .key('C', ModTags.Items.DUST_COAL)
                .patternLine("II")
                .patternLine("IC")
                .addCriterion("has_coal", hasItem(ModTags.Items.DUST_COAL))
                .addCriterion("has_iron", hasItem(ModTags.Items.DUST_IRON))
                .build(consumer);

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
