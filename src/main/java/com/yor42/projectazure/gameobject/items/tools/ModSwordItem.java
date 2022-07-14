package com.yor42.projectazure.gameobject.items.tools;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ModSwordItem extends SwordItem {

    private final boolean isLimitedtoOwner;

    public ModSwordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        this(tier, attackDamageIn, attackSpeedIn, false, builderIn);
    }

    public ModSwordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn,boolean isLimitedtoOwner, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
        this.isLimitedtoOwner = isLimitedtoOwner;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(TextFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        super.inventoryTick(stack, world, entity, p_77663_4_, p_77663_5_);

        CompoundNBT compound = stack.getOrCreateTag();
        if(compound.contains("owner") && this.isLimitedtoOwner){
            UUID owner = compound.getUUID("owner");
            if(!entity.getUUID().equals(owner)){
                stack.shrink(stack.getCount());
                for(int i = 0; i < 5; ++i) {
                    double d0 = random.nextGaussian() * 0.02D;
                    double d1 = random.nextGaussian() * 0.02D;
                    double d2 = random.nextGaussian() * 0.02D;
                    world.addParticle(ParticleTypes.END_ROD, entity.getRandomX(1.0D), entity.getRandomY(), entity.getRandomZ(1.0D), d0, d1, d2);
                }
            }
        }
    }
}
