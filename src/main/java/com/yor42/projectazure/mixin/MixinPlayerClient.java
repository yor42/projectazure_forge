package com.yor42.projectazure.mixin;

import com.mojang.authlib.GameProfile;
import com.yor42.projectazure.interfaces.IShaderEquipment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LocalPlayer.class)
public class MixinPlayerClient extends Player {
    /*
    @Unique
    private ItemStack projectazure_forge$prevstack = ItemStack.EMPTY;
    @Unique
    @Nullable
    private PostChain projectazure_forge$prevChain = null;

    @Override
    @Unique(silent = true)
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        super.setItemSlot(pSlot, pStack);
    }

    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Inject(method = {"setItemSlot", "m_8061_"}, at = @At("TAIL"), cancellable = true)
    private void onsetItemSlot(EquipmentSlot pSlot, ItemStack pStack, CallbackInfo cir){
        GameRenderer renderer =  Minecraft.getInstance().gameRenderer;
        if(pStack.getItem() instanceof IShaderEquipment shader){

            if(((GameRendererAccessor)renderer).getEffectActive() && ((GameRendererAccessor)renderer).getPostEffect() != null){
                this.projectazure_forge$prevChain = ((GameRendererAccessor)renderer).getPostEffect();
            }

            renderer.loadEffect(shader.shaderLocation());
        }
        else if(projectazure_forge$prevstack.getItem() instanceof IShaderEquipment){

            PostChain effect = ((GameRendererAccessor)renderer).getPostEffect();
            if(effect != null){
                effect.close();
            }
            ((GameRendererAccessor)renderer).setPostEffect(null);

            if(this.projectazure_forge$prevChain != null){
                ((GameRendererAccessor)renderer).setPostEffect(this.projectazure_forge$prevChain);
                ((GameRendererAccessor)renderer).setEffectActive(true);
                this.projectazure_forge$prevChain = null;
            }

        }
        this.projectazure_forge$prevstack = pStack;
    }
     */


    public MixinPlayerClient(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
