package com.yor42.projectazure.gameobject.advencements.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;

import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

public class NameEntityCriterion  extends AbstractCriterionTrigger<NameEntityCriterion.Instance> {
    private static final ResourceLocation ID = new ResourceLocation("name_animal");

    @Override
    protected NameEntityCriterion.Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        EntityPredicate.AndPredicate entitypredicate$andpredicate = EntityPredicate.AndPredicate.deserializeJSONObject(json, "entity", conditionsParser);
        return new NameEntityCriterion.Instance(entityPredicate, entitypredicate$andpredicate);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Instance extends CriterionInstance {
        private final EntityPredicate.AndPredicate entity;

        public Instance(EntityPredicate.AndPredicate player, EntityPredicate.AndPredicate entity) {
            super(NameEntityCriterion.ID, player);
            this.entity = entity;
        }

        public static NameEntityCriterion.Instance any() {
            return new NameEntityCriterion.Instance(EntityPredicate.AndPredicate.ANY_AND, EntityPredicate.AndPredicate.ANY_AND);
        }

        public static NameEntityCriterion.Instance create(EntityPredicate entityCondition) {
            return new NameEntityCriterion.Instance(EntityPredicate.AndPredicate.ANY_AND, EntityPredicate.AndPredicate.createAndFromEntityCondition(entityCondition));
        }

        public boolean test(LootContext context) {
            return this.entity.testContext(context);
        }

        public JsonObject serialize(ConditionArraySerializer conditions) {
            JsonObject jsonobject = super.serialize(conditions);
            jsonobject.add("entity", this.entity.serializeConditions(conditions));
            return jsonobject;
        }
    }
}
