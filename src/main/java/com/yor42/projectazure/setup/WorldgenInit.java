package com.yor42.projectazure.setup;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class WorldgenInit {

    public static void registerWorldgen(final BiomeLoadingEvent event){
        if(PAConfig.CONFIG.ENABLE_TIN.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.TIN_ORE, PAConfig.CONFIG.TIN_VEINSIZE.get(), PAConfig.CONFIG.TIN_MINHEIGHT.get(), PAConfig.CONFIG.TIN_MAXHEIGHT.get(), PAConfig.CONFIG.TIN_VEINSPERCHUNK.get());
            addOreSpawn(event, OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.TIN_ORE_DEEPSLATE, PAConfig.CONFIG.TIN_VEINSIZE_DEEP.get(), PAConfig.CONFIG.TIN_MINHEIGHT_DEEP.get(), PAConfig.CONFIG.TIN_MAXHEIGHT_DEEP.get(), PAConfig.CONFIG.TIN_VEINSPERCHUNK_DEEP.get());
        }
        if(PAConfig.CONFIG.ENABLE_LEAD.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.LEAD_ORE, PAConfig.CONFIG.LEAD_VEINSIZE.get(), PAConfig.CONFIG.LEAD_MINHEIGHT.get(), PAConfig.CONFIG.LEAD_MAXHEIGHT.get(), PAConfig.CONFIG.LEAD_VEINSPERCHUNK.get());
            addOreSpawn(event, OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.LEAD_ORE, PAConfig.CONFIG.LEAD_VEINSIZE_DEEP.get(), PAConfig.CONFIG.LEAD_MINHEIGHT_DEEP.get(), PAConfig.CONFIG.LEAD_MAXHEIGHT_DEEP.get(), PAConfig.CONFIG.LEAD_VEINSPERCHUNK_DEEP.get());
        }
        if(PAConfig.CONFIG.ENABLE_ORIROCK.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.ORIROCK, PAConfig.CONFIG.ORIROCK_VEINSIZE.get(), PAConfig.CONFIG.ORIROCK_MINHEIGHT.get(), PAConfig.CONFIG.ORIROCK_MAXHEIGHT.get(), PAConfig.CONFIG.ORIROCK_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_ZINC.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.ZINC_ORE, PAConfig.CONFIG.ZINC_VEINSIZE.get(), PAConfig.CONFIG.ZINC_MINHEIGHT.get(), PAConfig.CONFIG.ZINC_MAXHEIGHT.get(), PAConfig.CONFIG.ZINC_VEINSPERCHUNK.get());
            addOreSpawn(event, OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.ZINC_ORE, PAConfig.CONFIG.ZINC_VEINSIZE_DEEP.get(), PAConfig.CONFIG.ZINC_MINHEIGHT_DEEP.get(), PAConfig.CONFIG.ZINC_MAXHEIGHT_DEEP.get(), PAConfig.CONFIG.ZINC_VEINSPERCHUNK_DEEP.get());
        }
        if(PAConfig.CONFIG.ENABLE_RMA7012.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.RMA_7012_ORE, PAConfig.CONFIG.RMA7012_VEINSIZE.get(), PAConfig.CONFIG.RMA7012_MINHEIGHT.get(), PAConfig.CONFIG.RMA7012_MAXHEIGHT.get(), PAConfig.CONFIG.RMA7012_VEINSPERCHUNK.get());
            addOreSpawn(event, OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.RMA_7012_ORE, PAConfig.CONFIG.RMA7012_VEINSIZE_DEEP.get(), PAConfig.CONFIG.RMA7012_MINHEIGHT_DEEP.get(), PAConfig.CONFIG.RMA7012_MAXHEIGHT_DEEP.get(), PAConfig.CONFIG.RMA7012_VEINSPERCHUNK_DEEP.get());
        }
        if(PAConfig.CONFIG.ENABLE_MANGANESE.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.MANGANESE_ORE, PAConfig.CONFIG.MANGANESE_VEINSIZE.get(), PAConfig.CONFIG.MANGANESE_MINHEIGHT.get(), PAConfig.CONFIG.MANGANESE_MAXHEIGHT.get(), PAConfig.CONFIG.MANGANESE_VEINSPERCHUNK.get());
            addOreSpawn(event, OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.MANGANESE_ORE, PAConfig.CONFIG.MANGANESE_VEINSIZE_DEEP.get(), PAConfig.CONFIG.MANGANESE_MINHEIGHT_DEEP.get(), PAConfig.CONFIG.MANGANESE_MAXHEIGHT_DEEP.get(), PAConfig.CONFIG.MANGANESE_VEINSPERCHUNK_DEEP.get());
        }
        if(PAConfig.CONFIG.ENABLE_PYROXENE.get()) {
            addOreSpawn(event, OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.PYROXENE_ORE, PAConfig.CONFIG.PYROXENE_VEINSIZE.get(), PAConfig.CONFIG.PYROXENE_MINHEIGHT.get(), PAConfig.CONFIG.PYROXENE_MAXHEIGHT.get(), PAConfig.CONFIG.PYROXENE_VEINSPERCHUNK.get());
            addOreSpawn(event, OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.PYROXENE_ORE, PAConfig.CONFIG.PYROXENE_VEINSIZE_DEEP.get(), PAConfig.CONFIG.PYROXENE_MINHEIGHT_DEEP.get(), PAConfig.CONFIG.PYROXENE_MAXHEIGHT_DEEP.get(), PAConfig.CONFIG.PYROXENE_VEINSPERCHUNK_DEEP.get());
        }
    }


    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, RegistryObject<? extends Block> registryObject, int veinSize, int minHeight, int maxHeight, int amount){
        addOreSpawn(event, rule, getBlockDefaultState(registryObject), veinSize, minHeight, maxHeight, amount);
    }

    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, Block block, int veinSize, int minHeight, int maxHeight, int amount){
       addOreSpawn(event, rule, block.defaultBlockState(), veinSize, minHeight, maxHeight, amount);
    }

    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, BlockState blockState, int veinSize, int minHeight, int maxHeight, int amount){
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(rule, blockState, veinSize))), List.of(RarityFilter.onAverageOnceEvery(2)))));
    }

    private static BlockState getBlockDefaultState(RegistryObject<? extends Block> registryEntry){
        return registryEntry.get().defaultBlockState();
    }

}
