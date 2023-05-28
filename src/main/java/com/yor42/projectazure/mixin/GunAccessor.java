package com.yor42.projectazure.mixin;

import com.tac.guns.common.Gun;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gun.class)
public interface GunAccessor {

    @Accessor(remap = false)
    void setModules(Gun.Modules module);

}
