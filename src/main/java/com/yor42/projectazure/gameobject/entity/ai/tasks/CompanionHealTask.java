package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.yor42.projectazure.setup.register.RegisterAI.HEAL_POTION_INDEX;
import static com.yor42.projectazure.setup.register.RegisterAI.REGENERATION_POTION_INDEX;

public class CompanionHealTask extends Behavior<AbstractEntityCompanion> {
    private int cooldown = 0;
    @Nullable
    private InteractionHand PotionHand = null;
    public CompanionHealTask() {
        super(ImmutableMap.of(HEAL_POTION_INDEX.get(), MemoryStatus.REGISTERED, REGENERATION_POTION_INDEX.get(), MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, AbstractEntityCompanion entity) {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }if (entity.getHealth() < 14) {
            if (this.getPotionHand(entity).isPresent()) {
                this.PotionHand = this.getPotionHand(entity).get();
                boolean val = entity.isEating() || !entity.isAggressive() && entity.getTarget() == null;
                return val;
            }
            int idx = entity.getBrain().getMemory(HEAL_POTION_INDEX.get()).orElse(entity.getBrain().getMemory(REGENERATION_POTION_INDEX.get()).orElse(-1));
            if(idx >-1 && entity.getItemSwapIndexOffHand() == -1){
                this.ChangeItem(idx, entity);
                this.cooldown = 15;
                return false;
            }
        }
        return false;
    }

    @Override
    protected void start(ServerLevel world, AbstractEntityCompanion entity, long p_212831_3_) {
        if(this.PotionHand!=null) {
            ItemStack stack = entity.getItemInHand(this.PotionHand);
            Item item = stack.getItem();
            if (!(item instanceof SplashPotionItem)){
                entity.startUsingItem(this.PotionHand);
            }
            else{
                this.throwPotion(entity, stack);
            }
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        List<LivingEntity> list = p_212834_1_.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(4.0D, 3.0D, 4.0D));

        if (!list.isEmpty() && entity.getFoodStats().getFoodLevel()>5) {
            for (LivingEntity mob : list) {
                if (mob != null) {
                    if (mob instanceof Monster) {
                        return false;
                    }
                }
            }
        }

        return entity.isUsingItem() || entity.getItemInHand(entity.getUsedItemHand()).isEdible();
    }

    public void throwPotion(AbstractEntityCompanion entity, ItemStack potionStack) {
        if(PotionHand!= null) {
            Potion potion = PotionUtils.getPotion(potionStack);
            Vec3 vector3d = entity.getDeltaMovement();
            double d0 = entity.getX() + vector3d.x - entity.getX();
            double d1 = entity.getEyeY() - (double) 1.1F - entity.getY();
            double d2 = entity.getZ() + vector3d.z - entity.getZ();
            float f = Mth.sqrt((float) (d0 * d0 + d2 * d2));

            ThrownPotion potionentity = new ThrownPotion(entity.getCommandSenderWorld(), entity);
            potionentity.setItem(PotionUtils.setPotion(potionStack, potion));
            potionentity.setXRot(potionentity.getXRot() + 20.0F);
            potionentity.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 8.0F);
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITCH_THROW, entity.getSoundSource(), 1.0F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
            entity.swing(this.PotionHand, true);
            entity.level.addFreshEntity(potionentity);
        }
    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {
        entity.stopUsingItem();
        if(entity.getItemSwapIndexOffHand()>-1) {
            ItemStack buffer = entity.getOffhandItem();
            entity.setItemInHand(InteractionHand.OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
            entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer);
            entity.setItemSwapIndexOffHand(-1);
        }
    }

    private void ChangeItem(int index, AbstractEntityCompanion entity){
        ItemStack Buffer = entity.getOffhandItem();
        entity.setItemInHand(InteractionHand.OFF_HAND, entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        entity.setItemSwapIndexOffHand(index);
    }

    private Optional<InteractionHand> getPotionHand(AbstractEntityCompanion entity){
        for(InteractionHand h : InteractionHand.values()){
            if(entity.getItemInHand(h).getItem() instanceof PotionItem){
                return Optional.of(h);
            }
        }
        return Optional.empty();
    }

}
