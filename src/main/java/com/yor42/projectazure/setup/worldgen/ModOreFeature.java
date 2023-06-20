package com.yor42.projectazure.setup.worldgen;

import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.List;

public class ModOreFeature {

    public static final List<OreConfiguration.TargetBlockState> ORE_TIN_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.TIN_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.TIN_ORE_DEEPSLATE.get().defaultBlockState()));
    public static final List<OreConfiguration.TargetBlockState> ORE_ZINC_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.ZINC_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.ZINC_ORE_DEEPSLATE.get().defaultBlockState()));
    public static final List<OreConfiguration.TargetBlockState> ORE_BAUXITE_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.BAUXITE_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.BAUXITE_ORE_DEEPSLATE.get().defaultBlockState()));
    public static final List<OreConfiguration.TargetBlockState> ORE_LEAD_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.LEAD_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.LEAD_ORE_DEEPSLATE.get().defaultBlockState()));
    public static final List<OreConfiguration.TargetBlockState> ORE_RMA7012_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.RMA_7012_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.RMA_7012_ORE_DEEPSLATE.get().defaultBlockState()));
    public static final List<OreConfiguration.TargetBlockState> ORE_MANGANESE_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.MANGANESE_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.MANGANESE_ORE_DEEPSLATE.get().defaultBlockState()));
    public static final List<OreConfiguration.TargetBlockState> ORE_PYROXENE_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, RegisterBlocks.PYROXENE_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RegisterBlocks.PYROXENE_ORE.get().defaultBlockState()));


}
