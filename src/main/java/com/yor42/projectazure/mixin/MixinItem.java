package com.yor42.projectazure.mixin;

import com.yor42.projectazure.gameobject.items.GasMaskItem;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.yor42.projectazure.intermod.curios.CuriosCompat.getCurioItemStack;

@Mixin(Item.class)
public abstract class MixinItem {

    @Shadow(remap = true)
    public abstract UseAction getUseAnimation(ItemStack p_77661_1_);

    //You can't shove food in through gas mask lol
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void onItemUse(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir){
        if(!(player.getItemBySlot(EquipmentSlotType.HEAD).getItem() instanceof GasMaskItem)){
            return;
        }
        else if(CompatibilityUtils.isCurioLoaded()){
            if((getCurioItemStack(player, (stack)->stack.getItem() == RegisterItems.GASMASK.get())).isEmpty()){
                return;
            }
        }
        ItemStack stack = player.getItemInHand(hand);
        if(getUseAnimation(stack) == UseAction.EAT){
            cir.setReturnValue(ActionResult.fail(stack));
        }
    }

}
