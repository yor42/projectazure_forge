package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.pattern.FactoryBlockPattern;
import com.lowdragmc.multiblocked.api.pattern.Predicates;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.yor42.projectazure.client.renderer.block.MBDGeoRenderer;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.resources.ResourceLocation;

import static com.yor42.projectazure.setup.register.registerMultiBlocks.readTrait;

public class SiliconeCrucibleTE extends ControllerTileEntity {

    public static final RecipeMap SILICONCRUCIBLE_RECIPEMAP;
    public static final ControllerDefinition DEF = new ControllerDefinition(new ResourceLocation(Constants.MODID, "siliconecrucible"), SiliconeCrucibleTE::new);
    private static final MBDGeoRenderer renderer = new MBDGeoRenderer("siliconecrucible", false);
    public SiliconeCrucibleTE(ControllerDefinition definition) {
        super(definition);
    }

    public static void RegisterTE(){
        DEF.setRecipeMap(SILICONCRUCIBLE_RECIPEMAP);
        DEF.setBasePattern(FactoryBlockPattern.start().aisle("AEA", "AAA", "AAA").aisle("C@C", "ADA", "AXA").where("A", Predicates.any()).where("@", Predicates.component(DEF)).where("E", Predicates.anyCapability(FEMultiblockCapability.CAP)).where("C", Predicates.anyCapability(ItemMultiblockCapability.CAP)).where("D", Predicates.blocks(RegisterBlocks.MACHINE_DYNAMO.get()).disableRenderFormed()).where("X", Predicates.blocks(RegisterBlocks.MACHINE_COMPONENTBLOCK.get()).disableRenderFormed()).build());
        DEF.getBaseStatus().setRenderer(ResourceUtils.getMBDBlockModel("silicone_crucible"));
        DEF.getIdleStatus().setRenderer(renderer);
        DEF.getWorkingStatus().setRenderer(renderer);
        DEF.getSuspendStatus().setRenderer(renderer);
        DEF.traits = readTrait("siliconcrucible");
        DEF.properties.isOpaque = false;
        DEF.properties.tabGroup = "pa_machines";
        RecipeMap.register(DEF.getRecipeMap());
        MbdComponents.registerComponent(DEF);
    }

    static {
        SILICONCRUCIBLE_RECIPEMAP= new RecipeMap("silicon_crucible");
        SILICONCRUCIBLE_RECIPEMAP.inputCapabilities.add(ItemMultiblockCapability.CAP);
        SILICONCRUCIBLE_RECIPEMAP.inputCapabilities.add(FEMultiblockCapability.CAP);
        SILICONCRUCIBLE_RECIPEMAP.outputCapabilities.add(ItemMultiblockCapability.CAP);
    }
}
