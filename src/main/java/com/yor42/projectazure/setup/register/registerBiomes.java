package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

import java.util.ArrayList;
import java.util.List;

public class registerBiomes {

    private static final List<BiomeData> BiomeList = new ArrayList<>();
    public static Biome makeMirrorSea(){

        MobSpawnInfo.Builder spawnbuilder = new MobSpawnInfo.Builder();

        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN).category(Biome.Category.OCEAN).depth(-1.8F).scale(0.1F).temperature(0.5F).downfall(0.5F).setEffects((new BiomeAmbience.Builder()).setWaterColor(0x0a0091).setWaterFogColor(0x480066).setFogColor(0x8a0037).withSkyColor(0x001a5b).setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build()).withMobSpawnSettings(spawnbuilder.copy()).withGenerationSettings(getOceanGenerationSettingsBuilder().build()).build();
    }

    public static void addBiometoList(String BiomeID, Biome biome, int chance, BiomeManager.BiomeType climate) {
        /*
        ResourceLocation ModResourceLocation = ResourceUtils.ModResourceLocation(BiomeID);

        if (WorldGenRegistries.BIOME.keySet().contains(ModResourceLocation)) {
            throw new IllegalStateException("Biome ID Conflict: \"" + ModResourceLocation + "\" already exists.");
        }
        biome.setRegistryName(ModResourceLocation);

         */
        BiomeList.add(new BiomeData(BiomeID, biome, chance, climate));
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
        addBiometoList("mirror_sea" , makeMirrorSea(), 10, BiomeManager.BiomeType.WARM);

        for(BiomeData data:BiomeList){
            registerviaData(data);
        }
    }

    public static void registerviaData(BiomeData data){
        registerManager.BIOMES.register(data.getID(), data::getBiome);
        BiomeManager.addBiome(data.getType(), new BiomeManager.BiomeEntry(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, ResourceUtils.ModResourceLocation(data.getID())), data.getChance()));
        BiomeDictionary.addTypes(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, data.getResourceLocationID()), BiomeDictionary.Type.OCEAN);
    }

    private static class BiomeData{

        private final Biome biome;
        private final int chance;
        private final String ID;
        private final BiomeManager.BiomeType type;

        public BiomeData(String ID, Biome Biome, int chance, BiomeManager.BiomeType climate){
            this.ID = ID;
            this.biome = Biome;
            this.chance = chance;
            this.type = climate;
        }

        public Biome getBiome() {
            return this.biome;
        }

        public int getChance() {
            return this.chance;
        }

        public String getID(){
            return this.ID;
        }

        public ResourceLocation getResourceLocationID(){
            return ResourceUtils.ModResourceLocation(this.ID);
        }

        public BiomeManager.BiomeType getType() {
            return this.type;
        }
    }

}
