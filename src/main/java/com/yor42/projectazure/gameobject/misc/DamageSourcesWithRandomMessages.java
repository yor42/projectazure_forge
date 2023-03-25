package com.yor42.projectazure.gameobject.misc;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

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
    public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {

        int randomMessage = new Random().nextInt(MessageCounts-1);

        LivingEntity livingentity = entityLivingBaseIn.getKillCredit();
        String s = "death.attack." + this.msgId+"_"+randomMessage;
        String s1 = s + ".player";
        return livingentity != null ? new TranslatableComponent(s1, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : new TranslatableComponent(s, entityLivingBaseIn.getDisplayName());
    }
}
