package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.pattern.FactoryBlockPattern;
import com.lowdragmc.multiblocked.api.pattern.Predicates;
import com.lowdragmc.multiblocked.api.recipe.RecipeLogic;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.yor42.projectazure.client.renderer.block.MBDGeoRenderer;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;

import static com.lowdragmc.multiblocked.api.block.CustomProperties.RotationState.Y_AXIS;

public class AmmoPressControllerTE extends ControllerTileEntity {
    public static final ControllerDefinition AmmoPressDefinition = new ControllerDefinition(new ResourceLocation(Constants.MODID, "ammo_press"), AmmoPressControllerTE::new);
    public AmmoPressControllerTE(ControllerDefinition definition) {
        super(definition);
    }

    @Override
    public RecipeLogic getRecipeLogic() {
        return super.getRecipeLogic();
    }

    public static void registerTE(){
        AmmoPressDefinition.setRecipeMap(new RecipeMap("ammopress"));
        AmmoPressDefinition.getRecipeMap().inputCapabilities.add(ItemMultiblockCapability.CAP);
        AmmoPressDefinition.getRecipeMap().inputCapabilities.add(FEMultiblockCapability.CAP);
        AmmoPressDefinition.getRecipeMap().outputCapabilities.add(ItemMultiblockCapability.CAP);
        AmmoPressDefinition.getBaseStatus().setRenderer(()->ResourceUtils.getMBDBlockModel("ammo_press_controller"));
        AmmoPressDefinition.getIdleStatus().setRenderer(()-> new MBDGeoRenderer("ammo_press", true));
        AmmoPressDefinition.getWorkingStatus().setRenderer(()->new MBDGeoRenderer("ammo_press_on", true));
        AmmoPressDefinition.properties.isOpaque = false;
        AmmoPressDefinition.properties.tabGroup = "pa_machines";
        AmmoPressDefinition.setBasePattern(FactoryBlockPattern.start().aisle("PPPP", "kkkk", "MMMM").aisle("PPPP", "kMMk", "MDDM").aisle("P@PP", "GGGG", "MMMM")
                .where("P", Predicates.component(HatchTE.EnergyHatchDefinition).or(Predicates.component(HatchTE.ItemHatchDefinition)).or(Predicates.blocks(RegisterBlocks.MACHINE_FRAME.get())))
                .where("k", Predicates.blocks(RegisterBlocks.MACHINE_FRAME.get()).disableRenderFormed()).where("M", Predicates.blocks(RegisterBlocks.MACHINE_COMPONENTBLOCK.get()).disableRenderFormed())
                .where("G", Predicates.blocks(Blocks.GLASS).disableRenderFormed()).where("@", Predicates.component(AmmoPressDefinition)).where("D", Predicates.blocks(RegisterBlocks.MACHINE_DYNAMO.get()).disableRenderFormed()).build());

        RecipeMap.register(AmmoPressDefinition.getRecipeMap());
        MbdComponents.registerComponent(AmmoPressDefinition);
    }
}
