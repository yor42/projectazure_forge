package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityArtoria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ItemExcaliburSheath extends Item implements ICurioItem {
    public ItemExcaliburSheath() {
        super(new Item.Properties().tab(Main.PA_GROUP).stacksTo(1).rarity(Rarity.RARE).fireResistant());
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity holder, int p_77663_4_, boolean p_77663_5_) {
        super.inventoryTick(stack, world, holder, p_77663_4_, p_77663_5_);
        if(holder instanceof LivingEntity) {
            applyRegen(world, (LivingEntity) holder, stack);
        }
    }

    @Override
    public void curioTick(LivingEntity holder, int index, ItemStack stack) {
        applyRegen(holder.getCommandSenderWorld(), holder, stack);
    }

    private static void applyRegen(World world, LivingEntity holder, ItemStack stack){
        CompoundNBT compound = stack.getOrCreateTag();
        if(world.isClientSide()){
            return;
        }
        if(compound.contains("owner")){
            UUID ownerUUID = compound.getUUID("owner");
            Entity ownerEntity = ((ServerWorld)world).getEntity(ownerUUID);

            if(!(ownerEntity instanceof LivingEntity) || ((LivingEntity) ownerEntity).isDeadOrDying()){
                stack.shrink(1);
                return;
            }

            if(ownerEntity instanceof EntityArtoria){
                EntityArtoria entity = (EntityArtoria) ownerEntity;
                if(entity.getOwner() == holder && entity.getAffection()>=90 && entity.blockPosition().closerThan(holder.blockPosition(), 16)){
                    holder.addEffect(new EffectInstance(Effects.REGENERATION, 30, 1));
                }
            }
        }
    }

}
