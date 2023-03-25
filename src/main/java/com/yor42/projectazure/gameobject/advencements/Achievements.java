package com.yor42.projectazure.gameobject.advencements;

import com.yor42.projectazure.gameobject.advencements.triggers.NameEntityCriterion;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Consumer;

public class Achievements implements Consumer<Consumer<Advancement>>{
    @Override
    public void accept(Consumer<Advancement> advancementConsumer) {
        Advancement.Builder.advancement().display(Items.LEAD, new TranslatableComponent("achievements.nicetry.title"), new TranslatableComponent("achievements.nicetry.description"), null, FrameType.GOAL, true, true, true).addCriterion("name_dinnerbone", NameEntityCriterion.Instance.any()).save(advancementConsumer, "achievement/nice_try");
    }
}
