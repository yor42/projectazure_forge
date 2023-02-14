package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.pattern.FactoryBlockPattern;
import com.lowdragmc.multiblocked.api.pattern.Predicates;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.FluidMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.util.ResourceLocation;

public class AdvancedAlloySmelterControllerTE extends ControllerTileEntity {
    public static final ControllerDefinition SMELTRYDefinition = new ControllerDefinition(new ResourceLocation(Constants.MODID, "advanced_alloy_smelter"), AdvancedAlloySmelterControllerTE::new);
    public AdvancedAlloySmelterControllerTE(ControllerDefinition definition) {
        super(definition);
    }

    public static void registerTE(){
        SMELTRYDefinition.setRecipeMap(new RecipeMap("advanced_alloy_smelter"));
        SMELTRYDefinition.getRecipeMap().inputCapabilities.add(ItemMultiblockCapability.CAP);
        SMELTRYDefinition.getRecipeMap().inputCapabilities.add(FluidMultiblockCapability.CAP);
        SMELTRYDefinition.getRecipeMap().inputCapabilities.add(FEMultiblockCapability.CAP);
        SMELTRYDefinition.getRecipeMap().outputCapabilities.add(ItemMultiblockCapability.CAP);
        SMELTRYDefinition.setBasePattern(FactoryBlockPattern.start().aisle("PPP", "PFP","PPP").aisle("PCP", "FEF", "PCP").aisle("PPP", "P@P", "PPP").where("A", Predicates.any()).where("@", Predicates.component(SMELTRYDefinition)).where("P", Predicates.component(HatchTE.EnergyHatchDefinition).or(Predicates.component(HatchTE.ItemHatchDefinition)).or(Predicates.component(HatchTE.FluidHatchDefinition)).or(Predicates.blocks(RegisterBlocks.MACHINE_FRAME.get()))).where("E", Predicates.air()).where("F", Predicates.blocks(RegisterBlocks.INCANDESCENT_ALLOY_BLOCK.get())).where("C", Predicates.blocks(RegisterBlocks.MACHINE_DYNAMO.get())).build());
        SMELTRYDefinition.getBaseStatus().setRenderer(ResourceUtils.getMBDBlockModel("advanced_alloy_smelter_off"));
        SMELTRYDefinition.getIdleStatus().setRenderer(ResourceUtils.getMBDBlockModel("advanced_alloy_smelter_off"));
        SMELTRYDefinition.getWorkingStatus().setRenderer(ResourceUtils.getMBDBlockModel("advanced_alloy_smelter_off"));
        SMELTRYDefinition.properties.isOpaque = true;
        SMELTRYDefinition.properties.tabGroup = "pa_machines";
        RecipeMap.register(SMELTRYDefinition.getRecipeMap());
        MbdComponents.registerComponent(SMELTRYDefinition);
    }
}
