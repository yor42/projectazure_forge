package com.yor42.projectazure.gameobject.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Random;

public class PACompanionAttackDamage extends DamageSource {
    @Nullable
    protected final LivingEntity damageSourceEntity;
    private final int MessageCounts;
    public PACompanionAttackDamage(String damageTypeIn, LivingEntity source, int messagecount) {
        super(damageTypeIn);
        this.damageSourceEntity = source;
        this.MessageCounts = messagecount;
    }

    @Nullable
    public LivingEntity getEntity() {
        return this.damageSourceEntity;
    }

    public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {

        ItemStack itemstack = this.damageSourceEntity != null ? this.damageSourceEntity.getMainHandItem() : ItemStack.EMPTY;
        String s;
        if(this.MessageCounts>1){
            int randomMessage = new Random().nextInt(MessageCounts-1);
            s = "death.attack." + this.msgId+"_"+randomMessage;
        }
        else {
            s = "death.attack." + this.msgId;
        }
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslationTextComponent(s + ".item", entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName(), itemstack.getDisplayName()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName());
    }

    @Nullable
    public Vector3d getSourcePosition() {
        return this.damageSourceEntity != null ? this.damageSourceEntity.position() : null;
    }
}
