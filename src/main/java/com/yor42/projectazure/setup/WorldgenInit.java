package com.yor42.projectazure.setup;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static com.yor42.projectazure.setup.worldgen.ModOrePlacement.*;
import static net.minecraft.data.worldgen.features.OreFeatures.NATURAL_STONE;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class WorldgenInit {

    @SubscribeEvent
    public static void registerWorldgen(final BiomeLoadingEvent event){
        if(PAConfig.CONFIG.ENABLE_TIN.get()) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_TIN_LARGEPLACEMENT);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_TIN_SMALLPLACEMENT);
        }
        if(PAConfig.CONFIG.ENABLE_LEAD.get()) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_LEAD_LARGEPLACEMENT);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_LEAD_SMALLPLACEMENT);
        }
        if(PAConfig.CONFIG.ENABLE_ORIROCK.get()) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_ORIROCK_LARGEPLACEMENT);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_ORIROCK_SMALLPLACEMENT);
        }
        if(PAConfig.CONFIG.ENABLE_ZINC.get()) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_ZINC_LARGEPLACEMENT);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_ZINC_SMALLPLACEMENT);
        }
        if(PAConfig.CONFIG.ENABLE_RMA7012.get()) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_RMA7012_LARGEPLACEMENT);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_RMA7012_SMALLPLACEMENT);
        }
        if(PAConfig.CONFIG.ENABLE_MANGANESE.get()) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_MANGANESE_LARGEPLACEMENT);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_MANGANESE_SMALLPLACEMENT);
        }
        if(PAConfig.CONFIG.ENABLE_PYROXENE.get()) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_PYROXENE_LARGEPLACEMENT);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_PYROXENE_SMALLPLACEMENT);
        }
        if(PAConfig.CONFIG.ENABLE_BAUXITE.get()) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_BAUXITE_LARGEPLACEMENT);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_BAUXITE_SMALLPLACEMENT);
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
