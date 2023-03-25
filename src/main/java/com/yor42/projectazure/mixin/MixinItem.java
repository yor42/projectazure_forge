package com.yor42.projectazure.mixin;

import com.yor42.projectazure.gameobject.items.GasMaskItem;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.yor42.projectazure.intermod.curios.CuriosCompat.getCurioItemStack;

@Mixin(Item.class)
public abstract class MixinItem {

    //You can't shove food in through gas mask lol
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void onItemUse(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        ItemStack stack = player.getItemInHand(hand);
        if(stack.getUseAnimation() != UseAnim.EAT){
            return;
        }

        if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof GasMaskItem){
            cir.setReturnValue(InteractionResultHolder.fail(stack));
        }
        else if(CompatibilityUtils.isCurioLoaded()){
            if(!getCurioItemStack(player, (stack1)->stack1.getItem() instanceof GasMaskItem).isEmpty()){
                cir.setReturnValue(InteractionResultHolder.fail(stack));
            }
        }
    }

}
