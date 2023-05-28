package com.yor42.projectazure.mixin;

import com.tac.guns.common.Gun;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gun.Modules.class)
public interface ModuleAccessor {
    @Accessor(remap = false)
    void setZoom(Gun.Modules.Zoom zoom);

    @Accessor(remap = false)
    void setAttachments(Gun.Modules.Attachments zoom);
}
