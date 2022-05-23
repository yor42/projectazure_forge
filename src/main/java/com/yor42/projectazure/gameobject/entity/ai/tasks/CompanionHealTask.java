package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.yor42.projectazure.setup.register.registerManager.HEAL_POTION_INDEX;
import static com.yor42.projectazure.setup.register.registerManager.REGENERATION_POTION_INDEX;
import static net.minecraft.util.Hand.OFF_HAND;

public class CompanionHealTask extends Task<AbstractEntityCompanion> {
    private int cooldown = 0;
    @Nullable
    private Hand PotionHand = null;
    public CompanionHealTask() {
        super(ImmutableMap.of(HEAL_POTION_INDEX.get(), MemoryModuleStatus.REGISTERED, REGENERATION_POTION_INDEX.get(), MemoryModuleStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion entity) {
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
    protected void start(ServerWorld world, AbstractEntityCompanion entity, long p_212831_3_) {
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
    protected boolean canStillUse(ServerWorld p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        List<LivingEntity> list = p_212834_1_.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().expandTowards(4.0D, 3.0D, 4.0D));

        if (!list.isEmpty() && entity.getFoodStats().getFoodLevel()>5) {
            for (LivingEntity mob : list) {
                if (mob != null) {
                    if (mob instanceof MonsterEntity) {
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
            Vector3d vector3d = entity.getDeltaMovement();
            double d0 = entity.getX() + vector3d.x - entity.getX();
            double d1 = entity.getEyeY() - (double) 1.1F - entity.getY();
            double d2 = entity.getZ() + vector3d.z - entity.getZ();
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);

            PotionEntity potionentity = new PotionEntity(entity.getCommandSenderWorld(), entity);
            potionentity.setItem(PotionUtils.setPotion(potionStack, potion));
            potionentity.xRot -= -20.0F;
            potionentity.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 8.0F);
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITCH_THROW, entity.getSoundSource(), 1.0F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
            entity.swing(this.PotionHand, true);
            entity.level.addFreshEntity(potionentity);
        }
    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {
        entity.stopUsingItem();
        if(entity.getItemSwapIndexOffHand()>-1) {
            ItemStack buffer = entity.getOffhandItem();
            entity.setItemInHand(OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
            entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer);
            entity.setItemSwapIndexOffHand(-1);
        }
    }

    private void ChangeItem(int index, AbstractEntityCompanion entity){
        ItemStack Buffer = entity.getOffhandItem();
        entity.setItemInHand(OFF_HAND, entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        entity.setItemSwapIndexOffHand(index);
    }

    private Optional<Hand> getPotionHand(AbstractEntityCompanion entity){
        for(Hand h : Hand.values()){
            if(entity.getItemInHand(h).getItem() instanceof PotionItem){
                return Optional.of(h);
            }
        }
        return Optional.empty();
    }

}
