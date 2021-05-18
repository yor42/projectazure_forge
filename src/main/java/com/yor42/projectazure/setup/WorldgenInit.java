package com.yor42.projectazure.setup;

import com.yor42.projectazure.setup.register.registerBlocks;
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
        addOreSpawn(event, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, registerBlocks.COPPER_ORE, 7, 5, 60, 8);
        addOreSpawn(event, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, registerBlocks.TIN_ORE, 7, 5, 60, 8);

        addOreSpawn(event, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, registerBlocks.LEAD_ORE, 4, 5, 30, 4);
        addOreSpawn(event, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, registerBlocks.LEAD_ORE, 4, 5, 40, 5);
    }


    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, RegistryObject<? extends Block> registryObject, int veinSize, int minHeight, int maxHeight, int amount){
        addOreSpawn(event, rule, getBlockDefaultState(registryObject), veinSize, minHeight, maxHeight, amount);
    }

    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, Block block, int veinSize, int minHeight, int maxHeight, int amount){
       addOreSpawn(event, rule, block.getDefaultState(), veinSize, minHeight, maxHeight, amount);
    }

    public static void addOreSpawn(final BiomeLoadingEvent event, RuleTest rule, BlockState blockState, int veinSize, int minHeight, int maxHeight, int amount){
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(rule, blockState, veinSize)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight))).square().func_242731_b(amount));
    }

    private static BlockState getBlockDefaultState(RegistryObject<? extends Block> registryEntry){
        return registryEntry.get().getDefaultState();
    }

}
