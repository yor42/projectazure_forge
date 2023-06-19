package com.yor42.projectazure.mixin;

import com.lowdragmc.multiblocked.api.definition.ComponentDefinition;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ComponentDefinition.class)
public interface MixinComponentDefinitionAccessor {

    @Accessor(remap = false)
    void setUi(CompoundTag tag);
}
