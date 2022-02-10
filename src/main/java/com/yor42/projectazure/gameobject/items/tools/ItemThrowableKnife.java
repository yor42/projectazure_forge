package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.gameobject.entity.projectiles.EntityThrownKnifeProjectile;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import static com.yor42.projectazure.setup.register.registerManager.PROJECTILE_KNIFE;

public class ItemThrowableKnife extends SwordItem {
    public ItemThrowableKnife(IItemTier p_i48460_1_, int p_i48460_2_, float p_i48460_3_, Properties p_i48460_4_) {
        super(p_i48460_1_, p_i48460_2_, p_i48460_3_, p_i48460_4_);
    }

    public boolean canAttackBlock(BlockState p_195938_1_, World p_195938_2_, BlockPos p_195938_3_, PlayerEntity p_195938_4_) {
        return !p_195938_4_.isCreative();
    }

    public void releaseUsing(ItemStack p_77615_1_, World p_77615_2_, LivingEntity p_77615_3_, int p_77615_4_) {
        if (p_77615_3_ instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)p_77615_3_;
            int i = this.getUseDuration(p_77615_1_) - p_77615_4_;
            if (i >= 10) {
                int j = EnchantmentHelper.getRiptide(p_77615_1_);
                if (j <= 0 || playerentity.isInWaterOrRain()) {
                    if (!p_77615_2_.isClientSide) {
                        p_77615_1_.hurtAndBreak(1, playerentity, (p_220047_1_) -> {
                            p_220047_1_.broadcastBreakEvent(p_77615_3_.getUsedItemHand());
                        });
                        if (j == 0) {
                            EntityThrownKnifeProjectile tridententity = new EntityThrownKnifeProjectile(PROJECTILE_KNIFE, playerentity, p_77615_2_, p_77615_1_);
                            tridententity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
                            if (playerentity.abilities.instabuild) {
                                tridententity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                            }

                            p_77615_2_.addFreshEntity(tridententity);
                            if (!playerentity.abilities.instabuild) {
                                playerentity.inventory.removeItem(p_77615_1_);
                            }
                        }
                    }

                }
            }
        }
    }

    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.SPEAR;
    }

    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return ActionResult.fail(itemstack);
        }else {
            p_77659_2_.startUsingItem(p_77659_3_);
            return ActionResult.consume(itemstack);
        }
    }

    public int getEnchantmentValue() {
        return 1;
    }
}
