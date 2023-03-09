package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.pattern.FactoryBlockPattern;
import com.lowdragmc.multiblocked.api.pattern.Predicates;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.lowdragmc.multiblocked.common.capability.EntityMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.yor42.projectazure.client.renderer.block.MBDGeoRenderer;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.capability.CompanionMultiblockCapability;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.util.ResourceLocation;
import static com.lowdragmc.multiblocked.api.registry.MbdComponents.DEFINITION_REGISTRY;

public class SiliconeCrucibleTE extends ControllerTileEntity {

    public static final ControllerDefinition DEF = new ControllerDefinition(new ResourceLocation(Constants.MODID, "riftway"), SiliconeCrucibleTE::new);

    public SiliconeCrucibleTE(ControllerDefinition definition) {
        super(definition);
    }

    public static void RegisterTE(){
        DEF.setRecipeMap(new RecipeMap("silicon_crucible"));
        DEF.getRecipeMap().inputCapabilities.add(ItemMultiblockCapability.CAP);
        DEF.getRecipeMap().inputCapabilities.add(FEMultiblockCapability.CAP);
        DEF.getRecipeMap().outputCapabilities.add(ItemMultiblockCapability.CAP);
        DEF.setBasePattern(FactoryBlockPattern.start().aisle("AEA", "AAA", "AAA").aisle("C@C", "ADA", "ACA").where("A", Predicates.any()).where("@", Predicates.component(DEF)).where("C", Predicates.anyCapability(ItemMultiblockCapability.CAP).disableRenderFormed()).where("L", Predicates.anyCapability(FEMultiblockCapability.CAP).or(Predicates.component(HatchTE.ItemHatchDefinition)).or(Predicates.component(HatchTE.FluidHatchDefinition))).where("A", Predicates.air()).where("S", Predicates.blocks(RegisterBlocks.MACHINE_FRAME_SLAB.get()).disableRenderFormed()).where("F", Predicates.blocks(RegisterBlocks.MACHINE_FRAME.get()).disableRenderFormed()).where("D", Predicates.blocks(RegisterBlocks.MACHINE_DYNAMO.get()).disableRenderFormed()).where("C", Predicates.blocks(RegisterBlocks.MACHINE_COMPONENTBLOCK.get()).disableRenderFormed()).build());
        DEF.getBaseStatus().setRenderer(ResourceUtils.getMBDBlockModel("riftway_controller"));
        DEF.getIdleStatus().setRenderer(new MBDGeoRenderer("riftway_off", "riftway", true));
        DEF.getWorkingStatus().setRenderer(new MBDGeoRenderer("riftway_on", "riftway", true));
        DEF.properties.isOpaque = false;
        DEF.properties.tabGroup = "pa_machines";
        RecipeMap.register(DEF.getRecipeMap());
        MbdComponents.registerComponent(DEF);
    }
}
