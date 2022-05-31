package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.pattern.FactoryBlockPattern;
import com.lowdragmc.multiblocked.api.pattern.Predicates;
import com.lowdragmc.multiblocked.api.recipe.ItemsIngredient;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.yor42.projectazure.client.renderer.block.MBDGeoRenderer;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class OriginiumGeneratorControllerTE extends ControllerTileEntity {
    public static final ControllerDefinition OriginiumGeneratorDefinition = new ControllerDefinition(new ResourceLocation(Constants.MODID, "originium_generator"), OriginiumGeneratorControllerTE::new);
    public OriginiumGeneratorControllerTE(ControllerDefinition definition) {
        super(definition);
    }

    public static void registerTE(){

        OriginiumGeneratorDefinition.recipeMap = new RecipeMap("originium_generation");
        OriginiumGeneratorDefinition.recipeMap.inputCapabilities.add(ItemMultiblockCapability.CAP);
        OriginiumGeneratorDefinition.recipeMap.outputCapabilities.add(FEMultiblockCapability.CAP);
        OriginiumGeneratorDefinition.baseRenderer = ResourceUtils.getMBDBlockModel("originium_generator_controller");
        OriginiumGeneratorDefinition.formedRenderer = new MBDGeoRenderer("originium_generator", true);
        OriginiumGeneratorDefinition.workingRenderer = new MBDGeoRenderer("originium_generator_on", true);
        OriginiumGeneratorDefinition.properties.isOpaque = false;
        OriginiumGeneratorDefinition.properties.tabGroup = "pa_machines";
        OriginiumGeneratorDefinition.basePattern = FactoryBlockPattern.start().aisle("PCP", " E ", " @ ", " F ").aisle("IPI", "F F", "L L", "   ").aisle("FPF", "   ", "   ", "   ").where(' ', Predicates.any()).where('I', Predicates.component(HatchTE.ItemHatchDefinition).disableRenderFormed()).where('E', Predicates.component(HatchTE.EnergyHatchDefinition).disableRenderFormed()).where('@', Predicates.component(OriginiumGeneratorDefinition)).where('P', Predicates.blocks(registerBlocks.MACHINE_COMPONENTBLOCK.get()).disableRenderFormed()).where('F', Predicates.blocks(registerBlocks.MACHINE_FRAME.get()).disableRenderFormed()).where("L", Predicates.blocks(Blocks.PISTON).disableRenderFormed()).where("C", Predicates.blocks(registerBlocks.MACHINE_DYNAMO.get()).disableRenderFormed()).build();
        RecipeMap.register(OriginiumGeneratorDefinition.recipeMap);
        MbdComponents.registerComponent(OriginiumGeneratorDefinition);
    }
}