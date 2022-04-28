package com.yor42.projectazure.setup;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.RegistryObject;

public class WorldgenInit {
    /*
    public static void registerWorldgen(final BiomeLoadingEvent event){
        //Wait, We have copper now!
        if(PAConfig.CONFIG.ENABLE_COPPER.get()) {
            //addOreSpawn(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE, registerBlocks.COPPER_ORE, PAConfig.CONFIG.COPPER_VEINSIZE.get(), PAConfig.CONFIG.COPPER_MINHEIGHT.get(), PAConfig.CONFIG.COPPER_MAXHEIGHT.get(), PAConfig.CONFIG.COPPER_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_TIN.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, registerBlocks.TIN_ORE, PAConfig.CONFIG.TIN_VEINSIZE.get(), PAConfig.CONFIG.TIN_MINHEIGHT.get(), PAConfig.CONFIG.TIN_MAXHEIGHT.get(), PAConfig.CONFIG.TIN_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_LEAD.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, registerBlocks.LEAD_ORE, PAConfig.CONFIG.LEAD_VEINSIZE.get(), PAConfig.CONFIG.LEAD_MINHEIGHT.get(), PAConfig.CONFIG.LEAD_MAXHEIGHT.get(), PAConfig.CONFIG.LEAD_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_ORIROCK.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, registerBlocks.ORIROCK, PAConfig.CONFIG.ORIROCK_VEINSIZE.get(), PAConfig.CONFIG.ORIROCK_MINHEIGHT.get(), PAConfig.CONFIG.ORIROCK_MAXHEIGHT.get(), PAConfig.CONFIG.ORIROCK_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_ZINC.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, registerBlocks.ZINC_ORE, PAConfig.CONFIG.ZINC_VEINSIZE.get(), PAConfig.CONFIG.ZINC_MINHEIGHT.get(), PAConfig.CONFIG.ZINC_MAXHEIGHT.get(), PAConfig.CONFIG.ZINC_VEINSPERCHUNK.get());
        }
    }


    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, RegistryObject<? extends Block> registryObject, int veinSize, int minHeight, int maxHeight, int amount){
        addOreSpawn(event, rule, getBlockDefaultState(registryObject), veinSize, minHeight, maxHeight, amount);
    }

    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, Block block, int veinSize, int minHeight, int maxHeight, int amount){
       addOreSpawn(event, rule, block.defaultBlockState(), veinSize, minHeight, maxHeight, amount);
    }

    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, BlockState blockState, int veinSize, int minHeight, int maxHeight, int amount){
        FeatureUtils.register("ore_iron_small", Feature.ORE, new OreConfiguration(ORE_IRON_TARGET_LIST, 4));
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.place(new OreFeatureConfig(rule, blockState, veinSize)).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minHeight, 0, maxHeight))).squared().count(amount));
    }

    private static BlockState getBlockDefaultState(RegistryObject<? extends Block> registryEntry){
        return registryEntry.get().defaultBlockState();
    }

     */

}
