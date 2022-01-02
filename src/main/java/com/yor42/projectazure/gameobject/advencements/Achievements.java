package com.yor42.projectazure.gameobject.advencements;

import com.yor42.projectazure.gameobject.advencements.triggers.NameEntityCriterion;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.TameAnimalTrigger;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;

public class Achievements implements Consumer<Consumer<Advancement>>{
    @Override
    public void accept(Consumer<Advancement> advancementConsumer) {
        Advancement.Builder.builder().withDisplay(Items.LEAD, new TranslationTextComponent("achievements.nicetry.title"), new TranslationTextComponent("achievements.nicetry.description"), null, FrameType.GOAL, true, true, true).withCriterion("name_dinnerbone", NameEntityCriterion.Instance.any()).register(advancementConsumer, "achievement/nice_try");
    }
}
