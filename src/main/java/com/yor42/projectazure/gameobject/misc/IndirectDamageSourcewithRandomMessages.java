package com.yor42.projectazure.gameobject.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class IndirectDamageSourcewithRandomMessages extends IndirectEntityDamageSource {

    private final int MessageCounts;
    private final Entity indirectEntity;

    public IndirectDamageSourcewithRandomMessages(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn, int MessageCounts) {
        super(damageTypeIn, source, indirectEntityIn);
        this.MessageCounts = MessageCounts;
        this.indirectEntity = indirectEntityIn;
    }

    @Nonnull
    @Override
    public ITextComponent getLocalizedDeathMessage(@Nonnull LivingEntity entityLivingBaseIn) {
        int randomMessage = new Random().nextInt(MessageCounts-1);
        ITextComponent itextcomponent = this.indirectEntity == null ? this.entity.getDisplayName() : this.indirectEntity.getDisplayName();
        ItemStack itemstack = this.indirectEntity instanceof LivingEntity ? ((LivingEntity)this.indirectEntity).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + this.msgId+"_"+randomMessage;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName(), itextcomponent, itemstack.getDisplayName()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), itextcomponent);
    }
}
