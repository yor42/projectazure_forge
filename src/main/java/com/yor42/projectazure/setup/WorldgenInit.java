package com.yor42.projectazure.setup;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;

public class WorldgenInit {

    public static void registerWorldgen(final BiomeLoadingEvent event){
        if(PAConfig.CONFIG.ENABLE_COPPER.get()) {
            addOreSpawn(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE, RegisterBlocks.COPPER_ORE, PAConfig.CONFIG.COPPER_VEINSIZE.get(), PAConfig.CONFIG.COPPER_MINHEIGHT.get(), PAConfig.CONFIG.COPPER_MAXHEIGHT.get(), PAConfig.CONFIG.COPPER_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_TIN.get()) {
            addOreSpawn(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE, RegisterBlocks.TIN_ORE, PAConfig.CONFIG.TIN_VEINSIZE.get(), PAConfig.CONFIG.TIN_MINHEIGHT.get(), PAConfig.CONFIG.TIN_MAXHEIGHT.get(), PAConfig.CONFIG.TIN_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_LEAD.get()) {
            addOreSpawn(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE, RegisterBlocks.LEAD_ORE, PAConfig.CONFIG.LEAD_VEINSIZE.get(), PAConfig.CONFIG.LEAD_MINHEIGHT.get(), PAConfig.CONFIG.LEAD_MAXHEIGHT.get(), PAConfig.CONFIG.LEAD_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_ORIROCK.get()) {
            addOreSpawn(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE, RegisterBlocks.ORIROCK, PAConfig.CONFIG.ORIROCK_VEINSIZE.get(), PAConfig.CONFIG.ORIROCK_MINHEIGHT.get(), PAConfig.CONFIG.ORIROCK_MAXHEIGHT.get(), PAConfig.CONFIG.ORIROCK_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_ZINC.get()) {
            addOreSpawn(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE, RegisterBlocks.ZINC_ORE, PAConfig.CONFIG.ZINC_VEINSIZE.get(), PAConfig.CONFIG.ZINC_MINHEIGHT.get(), PAConfig.CONFIG.ZINC_MAXHEIGHT.get(), PAConfig.CONFIG.ZINC_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_RMA7012.get()) {
            addOreSpawn(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE, RegisterBlocks.RMA_7012_ORE, PAConfig.CONFIG.RMA7012_VEINSIZE.get(), PAConfig.CONFIG.RMA7012_MINHEIGHT.get(), PAConfig.CONFIG.RMA7012_MAXHEIGHT.get(), PAConfig.CONFIG.RMA7012_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_MANGANESE.get()) {
            addOreSpawn(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE, RegisterBlocks.MANGANESE_ORE, PAConfig.CONFIG.MANGANESE_VEINSIZE.get(), PAConfig.CONFIG.MANGANESE_MINHEIGHT.get(), PAConfig.CONFIG.MANGANESE_MAXHEIGHT.get(), PAConfig.CONFIG.MANGANESE_VEINSPERCHUNK.get());
        }
        if(PAConfig.CONFIG.ENABLE_PYROXENE.get()) {
            addOreSpawn(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE, RegisterBlocks.PYROXENE_ORE, PAConfig.CONFIG.PYROXENE_VEINSIZE.get(), PAConfig.CONFIG.PYROXENE_MINHEIGHT.get(), PAConfig.CONFIG.PYROXENE_MAXHEIGHT.get(), PAConfig.CONFIG.PYROXENE_VEINSPERCHUNK.get());
        }
    }


    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, RegistryObject<? extends Block> registryObject, int veinSize, int minHeight, int maxHeight, int amount){
        addOreSpawn(event, rule, getBlockDefaultState(registryObject), veinSize, minHeight, maxHeight, amount);
    }

    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, Block block, int veinSize, int minHeight, int maxHeight, int amount){
       addOreSpawn(event, rule, block.defaultBlockState(), veinSize, minHeight, maxHeight, amount);
    }

    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, BlockState blockState, int veinSize, int minHeight, int maxHeight, int amount){
        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(rule, blockState, veinSize)).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minHeight, 0, maxHeight))).squared().count(amount));
    }

    private static BlockState getBlockDefaultState(RegistryObject<? extends Block> registryEntry){
        return registryEntry.get().defaultBlockState();
    }

}
