package com.yor42.projectazure.gameobject.misc;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

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

    public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {

        ItemStack itemstack = this.damageSourceEntity != null ? this.damageSourceEntity.getMainHandItem() : ItemStack.EMPTY;
        String s;
        if(this.MessageCounts>1){
            int randomMessage = new Random().nextInt(MessageCounts-1);
            s = "death.attack." + this.msgId+"_"+randomMessage;
        }
        else {
            s = "death.attack." + this.msgId;
        }
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslatableComponent(s + ".item", entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName(), itemstack.getDisplayName()) : new TranslatableComponent(s, entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName());
    }

    @Nullable
    public Vec3 getSourcePosition() {
        return this.damageSourceEntity != null ? this.damageSourceEntity.position() : null;
    }
}
