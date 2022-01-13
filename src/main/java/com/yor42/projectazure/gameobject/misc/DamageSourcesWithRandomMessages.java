package com.yor42.projectazure.gameobject.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.Random;

public class DamageSourcesWithRandomMessages extends DamageSource {

    private final int MessageCounts;

    public DamageSourcesWithRandomMessages(String damageTypeIn, int DeathMessageCounts) {
        super(damageTypeIn);
        this.MessageCounts = DeathMessageCounts;
    }

    @Nonnull
    @Override
    public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {

        int randomMessage = new Random().nextInt(MessageCounts-1);

        LivingEntity livingentity = entityLivingBaseIn.getKillCredit();
        String s = "death.attack." + this.msgId+"_"+randomMessage;
        String s1 = s + ".player";
        return livingentity != null ? new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName());
    }
}
