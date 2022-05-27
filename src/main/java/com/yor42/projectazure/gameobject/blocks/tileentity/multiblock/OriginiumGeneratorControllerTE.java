package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.definition.PartDefinition;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.util.ResourceLocation;

public class OriginiumGeneratorControllerTE extends ControllerTileEntity {
    public static final ControllerDefinition OriginiumGeneratorDefinition = new ControllerDefinition(new ResourceLocation(Constants.MODID, "originium_generator"), (definition)->new OriginiumGeneratorControllerTE(definition, HatchTE.HatchType.ITEM));
    public OriginiumGeneratorControllerTE(ControllerDefinition definition) {
        super(definition);
    }
}
