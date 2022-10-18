package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.yor42.projectazure.client.renderer.block.MBDGeoRenderer;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.util.ResourceLocation;

public class RiftwayControllerTE extends ControllerTileEntity {
    public static final ControllerDefinition RiftwayDefinition = new ControllerDefinition(new ResourceLocation(Constants.MODID, "riftway"), RiftwayControllerTE::new);
    public RiftwayControllerTE(ControllerDefinition definition) {
        super(definition);
    }

    public static void registerTE(){
        RiftwayDefinition.getRecipeMap().inputCapabilities.add(ItemMultiblockCapability.CAP);
        RiftwayDefinition.getRecipeMap().inputCapabilities.add(FEMultiblockCapability.CAP);
        RiftwayDefinition.getBaseStatus().setRenderer(ResourceUtils.getMBDBlockModel("riftway_controller"));
        RiftwayDefinition.getIdleStatus().setRenderer(new MBDGeoRenderer("riftway", true));
        RiftwayDefinition.getWorkingStatus().setRenderer(new MBDGeoRenderer("riftway", true));
    }
}
