package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.ai.behaviors.base.ExtendedItemSwitchingBehavior;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.yor42.projectazure.setup.register.RegisterAI.HEAL_POTION_INDEX;
import static com.yor42.projectazure.setup.register.RegisterAI.REGENERATION_POTION_INDEX;

public class CompanionHealTask extends ExtendedItemSwitchingBehavior<AbstractEntityCompanion> {

    public CompanionHealTask(){
        cooldownFor((e)->10);
        startCondition((entity)->{

            if(!entity.isAggressive() && entity.getTarget() == null && entity.tickCount - entity.getLastHurtByMobTimestamp()>200){
                return entity.getHealth()<entity.getMaxHealth();
            }

            return entity.getHealth()/entity.getMaxHealth() < 0.5;
        });
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {

        InteractionHand potionHand = getPotionHand(entity);
        if(potionHand == null){
            this.ChangeItemWithPriority(entity, HEAL_POTION_INDEX.get(), REGENERATION_POTION_INDEX.get());
            return false;
        }

        return entity.getHealth()<7 || entity.isEating() || BrainUtils.getTargetOfEntity(entity) == null;
    }

    @Override
    protected boolean shouldKeepRunning(AbstractEntityCompanion entity) {

        InteractionHand potionHand = getPotionHand(entity);
        if(potionHand == null){
            return false;
        }

        if(!(entity.getItemInHand(potionHand).getItem() instanceof PotionItem)){
            return false;
        }

        if(!this.startCondition.test(entity)){
            return false;
        }


        return EntityRetrievalUtil.getNearestEntity(entity, 4, (entity1)->entity1 instanceof Monster) == null;
    }

    @Nullable
    private InteractionHand getPotionHand(AbstractEntityCompanion entity){
        for(InteractionHand h : InteractionHand.values()){
            if(entity.getItemInHand(h).getItem() instanceof PotionItem){
                return h;
            }
        }
        return null;
    }

    public void throwPotion(AbstractEntityCompanion entity, ItemStack potionStack) {
        InteractionHand potionHand = getPotionHand(entity);
        if(potionHand == null){
            return;
        }

        Potion potion = PotionUtils.getPotion(potionStack);
        Vec3 vector3d = entity.getDeltaMovement();
        double d0 = entity.getX() + vector3d.x - entity.getX();
        double d1 = entity.getEyeY() - (double) 1.1F - entity.getY();
        double d2 = entity.getZ() + vector3d.z - entity.getZ();
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2));

        ThrownPotion potionentity = new ThrownPotion(entity.getLevel(), entity);
        potionentity.setItem(PotionUtils.setPotion(potionStack, potion));
        potionentity.setXRot(potionentity.getXRot() + 20.0F);
        potionentity.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 8.0F);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITCH_THROW, entity.getSoundSource(), 1.0F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
        entity.swing(potionHand, true);
        entity.level.addFreshEntity(potionentity);
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {

        InteractionHand potionHand = getPotionHand(entity);
        if(potionHand == null){
            return;
        }

        ItemStack stack = entity.getItemInHand(potionHand);
        Item item = stack.getItem();
        if (!(item instanceof SplashPotionItem)){
            entity.startUsingItem(potionHand);
        }
        else{
            this.throwPotion(entity, stack);
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull AbstractEntityCompanion entity, long gameTime) {
        entity.stopUsingItem();
        super.stop(level, entity, gameTime);
    }

    @Override
    protected InteractionHand SwapHand() {
        return InteractionHand.MAIN_HAND;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(HEAL_POTION_INDEX.get(), MemoryStatus.REGISTERED), Pair.of(REGENERATION_POTION_INDEX.get(), MemoryStatus.REGISTERED));
    }
}
