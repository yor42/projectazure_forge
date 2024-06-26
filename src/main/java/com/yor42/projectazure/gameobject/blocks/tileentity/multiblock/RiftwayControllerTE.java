package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.TabContainer;
import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.gui.controller.IOPageWidget;
import com.lowdragmc.multiblocked.api.gui.controller.structure.StructurePageWidget;
import com.lowdragmc.multiblocked.api.pattern.FactoryBlockPattern;
import com.lowdragmc.multiblocked.api.pattern.Predicates;
import com.lowdragmc.multiblocked.api.recipe.RecipeLogic;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.lowdragmc.multiblocked.common.capability.EntityMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.yor42.projectazure.client.gui.multiblocked.RiftwayRecipePage;
import com.yor42.projectazure.client.renderer.block.MBDGeoRenderer;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.capability.CompanionMultiblockCapability;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipelogics.RiftwayRecipeLogic;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class RiftwayControllerTE extends ControllerTileEntity {

    public static RecipeMap RIFTWAYRECIPEMAP = new RecipeMap("riftway");
    public static final ControllerDefinition RiftwayDefinition = new ControllerDefinition(new ResourceLocation(Constants.MODID, "riftway"),RiftwayControllerTE.class);

    public RiftwayControllerTE(ControllerDefinition definition, BlockPos pos, BlockState state) {
        super(definition, pos, state);
    }

    public static void registerTE(){
        RiftwayDefinition.setRecipeMap(RIFTWAYRECIPEMAP);
        RiftwayDefinition.getRecipeMap().inputCapabilities.add(ItemMultiblockCapability.CAP);
        RiftwayDefinition.getRecipeMap().inputCapabilities.add(FEMultiblockCapability.CAP);
        RiftwayDefinition.getRecipeMap().outputCapabilities.add(CompanionMultiblockCapability.CAP);
        RiftwayDefinition.getRecipeMap().outputCapabilities.add(EntityMultiblockCapability.CAP);
        RiftwayDefinition.setBasePattern(FactoryBlockPattern.start().aisle("CDLDC", "FDDDF", "FDDDF", "FFCFF").aisle("CCCCC", "FDFDF", "FDFDF", "FFCFF").aisle("LFEFL", "DFAF@", "CFAFC", "SSSSS").aisle("SSSSS", "AAAAA", "AAAAA", "AAAAA").where("A", Predicates.any()).where("@", Predicates.component(RiftwayDefinition)).where("E", Predicates.component(HatchTE.EntityDefinition).disableRenderFormed()).where("L", Predicates.component(HatchTE.EnergyHatchDefinition).or(Predicates.component(HatchTE.ItemHatchDefinition)).or(Predicates.component(HatchTE.FluidHatchDefinition))).where("A", Predicates.air()).where("S", Predicates.blocks(RegisterBlocks.MACHINE_FRAME_SLAB.get()).disableRenderFormed()).where("F", Predicates.blocks(RegisterBlocks.MACHINE_FRAME.get()).disableRenderFormed()).where("D", Predicates.blocks(RegisterBlocks.MACHINE_DYNAMO.get()).disableRenderFormed()).where("C", Predicates.blocks(RegisterBlocks.MACHINE_COMPONENTBLOCK.get()).disableRenderFormed()).build());
        RiftwayDefinition.getBaseStatus().setRenderer(ResourceUtils.getMBDBlockModel("riftway_controller"));
        RiftwayDefinition.getIdleStatus().setRenderer(new MBDGeoRenderer("riftway_off", "riftway", true));
        RiftwayDefinition.getWorkingStatus().setRenderer(new MBDGeoRenderer("riftway_on", "riftway", true));
        RiftwayDefinition.properties.isOpaque = false;
        RiftwayDefinition.properties.tabGroup = "pa_machines";
        RecipeMap.register(RiftwayDefinition.getRecipeMap());
        MbdComponents.registerComponent(RiftwayDefinition);
    }

    @Override
    public RecipeLogic createRecipeLogic() {
        return new RiftwayRecipeLogic(this);
    }

    @Override
    public ModularUI createComponentUI(Player entityPlayer) {
        TabContainer tabContainer = new TabContainer(0, 0, 200, 166);
        if (isFormed()) {
            new RiftwayRecipePage(this, tabContainer);
            new IOPageWidget(this, tabContainer);
        } else {
            new StructurePageWidget(this.definition, tabContainer);
        }
        return new ModularUI(196, 256, this, entityPlayer).widget(tabContainer);
    }

    @Override
    public void update() {
        super.update();
    }
}
