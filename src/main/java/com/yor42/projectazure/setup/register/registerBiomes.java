package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
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

        return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.OCEAN)/*.depth(-1.8F).scale(0.1F)*/.temperature(0.5F).downfall(0.5F).specialEffects((new BiomeSpecialEffects.Builder()).waterColor(0x0a0091).waterFogColor(0x480066).fogColor(0x8a0037).skyColor(0x001a5b).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(spawnbuilder.build()).generationSettings(getOceanGenerationSettingsBuilder().build()).build();
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
        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder();//.surfaceBuilder(SurfaceBuilders.GRASS)
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);

        //net.minecraft.data.worldgen.placement.OrePlacements
        // Holder<ConfiguredFeature<?, ?>> a = Holder.direct(new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(1, 1, 1, null)));
        // builder.addFeature(Decoration.LAKES, Holder.direct(new PlacedFeature(null, List.of(RarityFilter.onAverageOnceEvery(2)))));
        // builder.addFeature(Decoration.LAKES, StructureFeatures.OCEAN_RUIN_COLD);
        // builder.addFeature(Decoration.LAKES, StructureFeatures.RUINED_PORTAL_OCEAN);

        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);

        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultOres(builder);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder);
        BiomeDefaultFeatures.addWaterTrees(builder);
        BiomeDefaultFeatures.addDefaultFlowers(builder);
        BiomeDefaultFeatures.addDefaultGrass(builder);
        BiomeDefaultFeatures.addDefaultMushrooms(builder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        return builder;
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
