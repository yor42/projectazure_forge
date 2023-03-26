package com.yor42.projectazure.gameobject.items.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ModSwordItem extends SwordItem {

    private final boolean isLimitedtoOwner;

    public ModSwordItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        this(tier, attackDamageIn, attackSpeedIn, false, builderIn);
    }

    public ModSwordItem(Tier tier, int attackDamageIn, float attackSpeedIn,boolean isLimitedtoOwner, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
        this.isLimitedtoOwner = isLimitedtoOwner;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        super.inventoryTick(stack, world, entity, p_77663_4_, p_77663_5_);

        CompoundTag compound = stack.getOrCreateTag();
        if(compound.contains("owner") && this.isLimitedtoOwner){
            UUID owner = compound.getUUID("owner");
            if(!entity.getUUID().equals(owner)){
                stack.shrink(stack.getCount());
                for(int i = 0; i < 5; ++i) {
                    double d0 = world.getRandom().nextGaussian() * 0.02D;
                    double d1 = world.getRandom().nextGaussian() * 0.02D;
                    double d2 = world.getRandom().nextGaussian() * 0.02D;
                    world.addParticle(ParticleTypes.END_ROD, entity.getRandomX(1.0D), entity.getRandomY(), entity.getRandomZ(1.0D), d0, d1, d2);
                }
            }
        }
    }
}
