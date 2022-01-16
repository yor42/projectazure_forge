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
    protected NameEntityCriterion.Instance createInstance(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        EntityPredicate.AndPredicate entitypredicate$andpredicate = EntityPredicate.AndPredicate.fromJson(json, "entity", conditionsParser);
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
            return new NameEntityCriterion.Instance(EntityPredicate.AndPredicate.ANY, EntityPredicate.AndPredicate.ANY);
        }

        public static NameEntityCriterion.Instance create(EntityPredicate entityCondition) {
            return new NameEntityCriterion.Instance(EntityPredicate.AndPredicate.ANY, EntityPredicate.AndPredicate.wrap(entityCondition));
        }

        public boolean test(LootContext context) {
            return this.entity.matches(context);
        }

        public JsonObject serializeToJson(ConditionArraySerializer conditions) {
            JsonObject jsonobject = super.serializeToJson(conditions);
            jsonobject.add("entity", this.entity.toJson(conditions));
            return jsonobject;
        }
    }
}
