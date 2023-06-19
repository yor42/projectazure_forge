package com.yor42.projectazure.mixin;

import com.tac.guns.common.Gun;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gun.Modules.class)
public class MixinModules {

    @Shadow(remap = false)
    private Gun.Modules.Zoom zoom;
    @Shadow(remap = false)
    private Gun.Modules.Attachments attachments;

    @Inject(method = "getZoom", at = @At("HEAD"), remap = false, cancellable = true)
    private void getZoom(CallbackInfoReturnable<Gun.Modules.Zoom> cir){
        if(this.zoom == null){
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "copy", at = @At("HEAD"), remap = false)
    private void onCopy(CallbackInfoReturnable<Gun.Modules> cir){
        if(this.zoom == null){
            Gun.Modules modules = new Gun.Modules();
            ((ModuleAccessor)modules).setZoom(null);
            ((ModuleAccessor)modules).setAttachments(this.attachments.copy());
            cir.setReturnValue(modules);
        }
    }
}
