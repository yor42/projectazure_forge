package com.yor42.projectazure.setup.register;

import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;

public class registerBiomes {

    public static Biome makeMirrorSea(){

        MobSpawnInfo.Builder spawnbuilder = new MobSpawnInfo.Builder();

        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN).category(Biome.Category.OCEAN).depth(-1.8F).scale(0.1F).temperature(0.5F).downfall(0.5F).setEffects((new BiomeAmbience.Builder()).setWaterColor(0x0a0091).setWaterFogColor(0x480066).setFogColor(0x8a0037).withSkyColor(0x001a5b).setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build()).withMobSpawnSettings(spawnbuilder.copy()).withGenerationSettings(getOceanGenerationSettingsBuilder().build()).build();
    }

    private static BiomeGenerationSettings.Builder getOceanGenerationSettingsBuilder() {
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder()).withSurfaceBuilder(ConfiguredSurfaceBuilders.field_244178_j);
        StructureFeature<?, ?> structurefeature = StructureFeatures.OCEAN_RUIN_COLD;
        DefaultBiomeFeatures.withOceanStructures(biomegenerationsettings$builder);
        biomegenerationsettings$builder.withStructure(structurefeature);
        biomegenerationsettings$builder.withStructure(StructureFeatures.RUINED_PORTAL_OCEAN);
        DefaultBiomeFeatures.withOceanCavesAndCanyons(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withLavaAndWaterLakes(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withMonsterRoom(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withCommonOverworldBlocks(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withOverworldOres(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withDisks(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withTreesInWater(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withDefaultFlowers(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withBadlandsGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withNormalMushroomGeneration(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withSugarCaneAndPumpkins(biomegenerationsettings$builder);
        DefaultBiomeFeatures.withLavaAndWaterSprings(biomegenerationsettings$builder);
        return biomegenerationsettings$builder;
    }

    public static void register(){

    }

}
