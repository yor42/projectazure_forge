package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityArtoria;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ItemExcaliburSheath extends Item implements ICurioItem {
    public ItemExcaliburSheath() {
        super(new Item.Properties().tab(Main.PA_GROUP).stacksTo(1).rarity(Rarity.RARE).fireResistant());
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull Entity holder, int p_77663_4_, boolean p_77663_5_) {
        super.inventoryTick(stack, world, holder, p_77663_4_, p_77663_5_);
        if(holder instanceof LivingEntity) {
            applyRegen(world, (LivingEntity) holder, stack);
        }
    }

    @Override
    public void curioTick(LivingEntity holder, int index, ItemStack stack) {
        applyRegen(holder.getCommandSenderWorld(), holder, stack);
    }

    private static void applyRegen(Level world, LivingEntity holder, ItemStack stack){
        CompoundTag compound = stack.getOrCreateTag();
        if(world.isClientSide()){
            return;
        }
        if(compound.contains("owner")){
            UUID ownerUUID = compound.getUUID("owner");
            Entity ownerEntity = ((ServerLevel)world).getEntity(ownerUUID);

            if(!(ownerEntity instanceof LivingEntity) || ((LivingEntity) ownerEntity).isDeadOrDying()){
                stack.shrink(1);
                return;
            }

            if(ownerEntity instanceof EntityArtoria){
                EntityArtoria entity = (EntityArtoria) ownerEntity;
                if(entity.getOwner() == holder && entity.getAffection()>=90 && entity.blockPosition().closerThan(holder.blockPosition(), 16)){
                    holder.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30, 1));
                }
            }
        }
    }

}
