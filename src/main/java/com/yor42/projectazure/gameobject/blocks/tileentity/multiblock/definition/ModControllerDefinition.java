package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.definition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.yor42.projectazure.mixin.MixinComponentDefinitionAccessor;
import net.minecraft.resources.ResourceLocation;

import static com.yor42.projectazure.setup.register.registerMultiBlocks.readTrait;
import static com.yor42.projectazure.setup.register.registerMultiBlocks.readUI;

public class ModControllerDefinition extends ControllerDefinition {
    public ModControllerDefinition(ResourceLocation location) {
        super(location);
        this.traits = readTrait(location);
        ((MixinComponentDefinitionAccessor)this).setUi(readUI(location));
    }
}
