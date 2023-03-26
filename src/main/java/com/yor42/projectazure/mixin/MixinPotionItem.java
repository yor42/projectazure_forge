package com.yor42.projectazure.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=PotionItem.class, priority = 1500)
public abstract class MixinPotionItem extends Item{

    public MixinPotionItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    @Unique(silent = true)
    public boolean hasContainerItem(ItemStack stack) {
        return super.hasContainerItem(stack);
    }

    @Override
    @Unique(silent = true)
    public ItemStack getContainerItem(ItemStack itemStack) {
        return super.getContainerItem(itemStack);
    }

    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Inject(method = {"hasContainerItem"}, at = @At("HEAD"), remap = false, cancellable = true)
    private void onHasContainerItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }
    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Inject(method = {"getContainerItem"}, at = @At("HEAD"), remap = false, cancellable = true)
    private void onGetContainerItem(ItemStack stack, CallbackInfoReturnable<ItemStack> cir){
        cir.setReturnValue(new ItemStack(Items.GLASS_BOTTLE));
    }



}
