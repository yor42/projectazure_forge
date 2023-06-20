package com.yor42.projectazure.setup.worldgen;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static com.yor42.projectazure.setup.worldgen.ModOreFeature.*;
import static net.minecraft.data.worldgen.features.OreFeatures.NATURAL_STONE;

public class ModOrePlacement {

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_TIN_LARGE = FeatureUtils.register("ore_tin_large", Feature.ORE, new OreConfiguration(ORE_TIN_TARGET_LIST, 12));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_TIN_SMALL = FeatureUtils.register("ore_tin_small", Feature.ORE, new OreConfiguration(ORE_TIN_TARGET_LIST, 8));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_ZINC_LARGE = FeatureUtils.register("ore_zinc_large", Feature.ORE, new OreConfiguration(ORE_ZINC_TARGET_LIST, 4));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_ZINC_SMALL = FeatureUtils.register("ore_zinc_small", Feature.ORE, new OreConfiguration(ORE_ZINC_TARGET_LIST, 2));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_BAUXITE_LARGE = FeatureUtils.register("ore_bauxite_large", Feature.ORE, new OreConfiguration(ORE_BAUXITE_TARGET_LIST, 8));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_BAUXITE_SMALL = FeatureUtils.register("ore_bauxite_small", Feature.ORE, new OreConfiguration(ORE_BAUXITE_TARGET_LIST, 6));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_LEAD_LARGE = FeatureUtils.register("ore_lead_large", Feature.ORE, new OreConfiguration(ORE_LEAD_TARGET_LIST, 8));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_LEAD_SMALL = FeatureUtils.register("ore_lead_small", Feature.ORE, new OreConfiguration(ORE_LEAD_TARGET_LIST, 4));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_RMA7012_LARGE = FeatureUtils.register("ore_rma7012_large", Feature.ORE, new OreConfiguration(ORE_RMA7012_TARGET_LIST, 6));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_RMA7012_SMALL = FeatureUtils.register("ore_rma7012_small", Feature.ORE, new OreConfiguration(ORE_RMA7012_TARGET_LIST, 4));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_MANGANESE_LARGE = FeatureUtils.register("ore_manganese_large", Feature.ORE, new OreConfiguration(ORE_MANGANESE_TARGET_LIST, 8));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_MANGANESE_SMALL = FeatureUtils.register("ore_manganese_small", Feature.ORE, new OreConfiguration(ORE_MANGANESE_TARGET_LIST, 5));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_PYROXENE_LARGE = FeatureUtils.register("ore_pyroxene_large", Feature.ORE, new OreConfiguration(ORE_PYROXENE_TARGET_LIST, 8));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_PYROXENE_SMALL = FeatureUtils.register("ore_pyroxene_small", Feature.ORE, new OreConfiguration(ORE_PYROXENE_TARGET_LIST, 5));


    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_ORIROCK = FeatureUtils.register("ore_orirock", Feature.ORE, new OreConfiguration(NATURAL_STONE, RegisterBlocks.ORIROCK.get().defaultBlockState(), 64));

    public static final Holder<PlacedFeature> ORE_TIN_LARGEPLACEMENT = PlacementUtils.register("ore_tin_largeplacement", ORE_TIN_LARGE, commonOrePlacement(PAConfig.CONFIG.TIN_VEINSPERCHUNK.get(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(16))));
    public static final Holder<PlacedFeature> ORE_TIN_SMALLPLACEMENT = PlacementUtils.register("ore_tin_smallplacement", ORE_TIN_SMALL, commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.absolute(45), VerticalAnchor.absolute(80))));

    public static final Holder<PlacedFeature> ORE_ZINC_LARGEPLACEMENT = PlacementUtils.register("ore_zinc_largeplacement", ORE_ZINC_LARGE, commonOrePlacement(PAConfig.CONFIG.ZINC_VEINSPERCHUNK.get(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(-20))));
    public static final Holder<PlacedFeature> ORE_ZINC_SMALLPLACEMENT = PlacementUtils.register("ore_zinc_smallplacement", ORE_ZINC_SMALL, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(30), VerticalAnchor.absolute(80))));

    public static final Holder<PlacedFeature> ORE_BAUXITE_LARGEPLACEMENT = PlacementUtils.register("ore_bauxite_largeplacement", ORE_BAUXITE_LARGE, commonOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(16))));
    public static final Holder<PlacedFeature> ORE_BAUXITE_SMALLPLACEMENT = PlacementUtils.register("ore_bauxite_smallplacement", ORE_BAUXITE_SMALL, commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(90))));

    public static final Holder<PlacedFeature> ORE_LEAD_LARGEPLACEMENT = PlacementUtils.register("ore_lead_largeplacement", ORE_LEAD_LARGE, commonOrePlacement(PAConfig.CONFIG.LEAD_VEINSPERCHUNK.get(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(40))));
    public static final Holder<PlacedFeature> ORE_LEAD_SMALLPLACEMENT = PlacementUtils.register("ore_lead_smallplacement", ORE_LEAD_SMALL, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(-20))));

    public static final Holder<PlacedFeature> ORE_RMA7012_LARGEPLACEMENT = PlacementUtils.register("ore_rma7012_largeplacement", ORE_RMA7012_LARGE, commonOrePlacement(PAConfig.CONFIG.RMA7012_VEINSPERCHUNK.get(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-20))));
    public static final Holder<PlacedFeature> ORE_RMA7012_SMALLPLACEMENT = PlacementUtils.register("ore_rma7012_smallplacement", ORE_RMA7012_SMALL, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(10), VerticalAnchor.absolute(40))));

    public static final Holder<PlacedFeature> ORE_ORIROCK_LARGEPLACEMENT = PlacementUtils.register("ore_orirock_largeplacement", ORE_ORIROCK, commonOrePlacement(PAConfig.CONFIG.ORIROCK_VEINSPERCHUNK.get(), HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(16))));
    public static final Holder<PlacedFeature> ORE_ORIROCK_SMALLPLACEMENT = PlacementUtils.register("ore_orirock_smallplacement", ORE_ORIROCK, commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.absolute(45), VerticalAnchor.absolute(80))));

    public static final Holder<PlacedFeature> ORE_PYROXENE_LARGEPLACEMENT = PlacementUtils.register("ore_pyroxene_largeplacement", ORE_PYROXENE_LARGE, rareOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(-30))));
    public static final Holder<PlacedFeature> ORE_PYROXENE_SMALLPLACEMENT = PlacementUtils.register("ore_pyroxene_smallplacement", ORE_PYROXENE_SMALL, rareOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-15), VerticalAnchor.absolute(15))));

    public static final Holder<PlacedFeature> ORE_MANGANESE_LARGEPLACEMENT = PlacementUtils.register("ore_manganese_largeplacement", ORE_MANGANESE_LARGE, commonOrePlacement(PAConfig.CONFIG.MANGANESE_VEINSPERCHUNK.get(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(10))));
    public static final Holder<PlacedFeature> ORE_MANGANESE_SMALLPLACEMENT = PlacementUtils.register("ore_manganese_smallplacement", ORE_MANGANESE_SMALL, commonOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.absolute(10), VerticalAnchor.absolute(30))));


    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }


}
