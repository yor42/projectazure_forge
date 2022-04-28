package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

import java.util.ArrayList;
import java.util.List;

public class registerBiomes {

    private static final List<BiomeData> BiomeList = new ArrayList<>();
    public static Biome makeMirrorSea(){

        MobSpawnInfo.Builder spawnbuilder = new MobSpawnInfo.Builder();

        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN).biomeCategory(Biome.Category.OCEAN).depth(-1.8F).scale(0.1F).temperature(0.5F).downfall(0.5F).specialEffects((new BiomeAmbience.Builder()).waterColor(0x0a0091).waterFogColor(0x480066).fogColor(0x8a0037).skyColor(0x001a5b).ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(spawnbuilder.build()).generationSettings(getOceanGenerationSettingsBuilder().build()).build();
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
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder()).surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        StructureFeature<?, ?> structurefeature = StructureFeatures.OCEAN_RUIN_COLD;
        DefaultBiomeFeatures.addDefaultOverworldOceanStructures(biomegenerationsettings$builder);
        biomegenerationsettings$builder.addStructureStart(structurefeature);
        biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_OCEAN);
        DefaultBiomeFeatures.addOceanCarvers(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultLakes(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultMonsterRoom(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addWaterTrees(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultFlowers(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSprings(biomegenerationsettings$builder);
        return biomegenerationsettings$builder;
    }

    public static void register(){
        //addBiometoList("mirror_sea" , makeMirrorSea(), 10, BiomeManager.BiomeType.WARM);
        /*
        for(BiomeData data:BiomeList){
            registerviaData(data);
        }

         */
    }

    public static void registerviaData(BiomeData data){
        registerManager.BIOMES.register(data.getID(), data::getBiome);
        BiomeManager.addBiome(data.getType(), new BiomeManager.BiomeEntry(RegistryKey.create(Registry.BIOME_REGISTRY, ResourceUtils.ModResourceLocation(data.getID())), data.getChance()));
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, data.getResourceLocationID()), BiomeDictionary.Type.OCEAN);
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
