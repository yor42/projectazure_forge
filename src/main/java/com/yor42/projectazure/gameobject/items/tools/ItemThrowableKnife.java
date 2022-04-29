package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityThrownKnifeProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class ItemThrowableKnife extends SwordItem {
    public ItemThrowableKnife(Tier p_i48460_1_, int p_i48460_2_, float p_i48460_3_, Properties p_i48460_4_) {
        super(p_i48460_1_, p_i48460_2_, p_i48460_3_, p_i48460_4_);
    }

    public boolean canAttackBlock(BlockState p_195938_1_, Level p_195938_2_, BlockPos p_195938_3_, Player p_195938_4_) {
        return !p_195938_4_.isCreative();
    }

    public void releaseUsing(ItemStack p_77615_1_, Level p_77615_2_, LivingEntity p_77615_3_, int p_77615_4_) {
        if (p_77615_3_ instanceof Player) {
            Player playerentity = (Player)p_77615_3_;
            int i = this.getUseDuration(p_77615_1_) - p_77615_4_;
            if (i >= 10) {
                int j = EnchantmentHelper.getRiptide(p_77615_1_);
                if (j <= 0 || playerentity.isInWaterOrRain()) {
                    if (!p_77615_2_.isClientSide) {
                        p_77615_1_.hurtAndBreak(1, playerentity, (p_220047_1_) -> {
                            p_220047_1_.broadcastBreakEvent(p_77615_3_.getUsedItemHand());
                        });
                        if (j == 0) {
                            EntityThrownKnifeProjectile tridententity = new EntityThrownKnifeProjectile(Main.THROWN_KNIFE.get(), playerentity, p_77615_2_, p_77615_1_);
                            tridententity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
                            if (playerentity.isCreative()) {
                                tridententity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            p_77615_2_.addFreshEntity(tridententity);
                            if (!playerentity.isCreative()) {
                                playerentity.getInventory().removeItem(p_77615_1_);
                            }
                        }
                    }

                }
            }
        }
    }

    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level p_77659_1_, Player p_77659_2_, InteractionHand p_77659_3_) {
        ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        }else {
            p_77659_2_.startUsingItem(p_77659_3_);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public int getEnchantmentValue() {
        return 1;
    }
}
