package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.SurfaceBuilders;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.biome.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class registerBiomes {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Constants.MODID);
    private static final List<BiomeData> BiomeList = new ArrayList<>();
    public static Biome makeMirrorSea(){

        MobSpawnSettings.Builder spawnbuilder = new MobSpawnSettings.Builder();

        return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.OCEAN).depth(-1.8F).scale(0.1F).temperature(0.5F).downfall(0.5F).specialEffects((new BiomeSpecialEffects.Builder()).waterColor(0x0a0091).waterFogColor(0x480066).fogColor(0x8a0037).skyColor(0x001a5b).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(spawnbuilder.build()).generationSettings(getOceanGenerationSettingsBuilder().build()).build();
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
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.GRASS);
        ConfiguredStructureFeature<?, ?> structurefeature = StructureFeatures.OCEAN_RUIN_COLD;
        BiomeDefaultFeatures.addDefaultOverworldOceanStructures(biomegenerationsettings$builder);
        biomegenerationsettings$builder.addStructureStart(structurefeature);
        biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_OCEAN);
        BiomeDefaultFeatures.addOceanCarvers(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultLakes(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addWaterTrees(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultFlowers(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultGrass(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultSprings(biomegenerationsettings$builder);
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
        BIOMES.register(data.getID(), data::getBiome);
        BiomeManager.addBiome(data.getType(), new BiomeManager.BiomeEntry(ResourceKey.create(Registry.BIOME_REGISTRY, ResourceUtils.ModResourceLocation(data.getID())), data.getChance()));
        BiomeDictionary.addTypes(ResourceKey.create(Registry.BIOME_REGISTRY, data.getResourceLocationID()), BiomeDictionary.Type.OCEAN);
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
