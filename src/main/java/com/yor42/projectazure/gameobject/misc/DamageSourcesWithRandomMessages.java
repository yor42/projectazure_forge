package com.yor42.projectazure.gameobject.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class DamageSourcesWithRandomMessages extends DamageSource {

    public final int MessageCounts;

    public DamageSourcesWithRandomMessages(String damageTypeIn, int DeathMessageCounts) {
        super(damageTypeIn);
        this.MessageCounts = DeathMessageCounts;
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {

        int randomMessage = new Random().nextInt(MessageCounts-1);

        LivingEntity livingentity = entityLivingBaseIn.getAttackingEntity();
        String s = "death.attack." + this.damageType+"_"+randomMessage;
        String s1 = s + ".player";
        return livingentity != null ? new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName());
    }
}
