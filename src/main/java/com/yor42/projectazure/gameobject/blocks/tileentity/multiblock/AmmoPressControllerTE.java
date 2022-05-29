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
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

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
        AmmoPressDefinition.recipeMap = new RecipeMap("ammopress");

        AmmoPressDefinition.recipeMap.inputCapabilities.add(ItemMultiblockCapability.CAP);
        AmmoPressDefinition.recipeMap.inputCapabilities.add(FEMultiblockCapability.CAP);
        AmmoPressDefinition.recipeMap.outputCapabilities.add(ItemMultiblockCapability.CAP);
        AmmoPressDefinition.baseRenderer = ResourceUtils.getMBDBlockModel("ammo_press_controller");
        AmmoPressDefinition.formedRenderer = new MBDGeoRenderer("ammo_press", true);
        AmmoPressDefinition.workingRenderer = new MBDGeoRenderer("ammo_press_on", true);
        AmmoPressDefinition.properties.isOpaque = false;
        AmmoPressDefinition.properties.tabGroup = "pa_machines";
        AmmoPressDefinition.basePattern = FactoryBlockPattern.start().aisle("PPPP", "kkkk", "MMMM").aisle("PPPP", "kMMk", "MDDM").aisle("P@PP", "GGGG", "MMMM")
                .where("P", Predicates.component(HatchTE.EnergyHatchDefinition).or(Predicates.component(HatchTE.ItemHatchDefinition)).or(Predicates.blocks(registerBlocks.MACHINE_FRAME.get())))
                .where("k", Predicates.blocks(registerBlocks.MACHINE_FRAME.get()).disableRenderFormed()).where("M", Predicates.blocks(registerBlocks.MACHINE_COMPONENTBLOCK.get()).disableRenderFormed())
                .where("G", Predicates.blocks(Blocks.GLASS).disableRenderFormed()).where("@", Predicates.component(AmmoPressDefinition)).where("D", Predicates.blocks(registerBlocks.MACHINE_DYNAMO.get()).disableRenderFormed()).build();

        RecipeMap.register(AmmoPressDefinition.recipeMap);
        MbdComponents.registerComponent(AmmoPressDefinition);
    }
}
