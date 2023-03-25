package com.yor42.projectazure.gameobject.advencements.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.resources.ResourceLocation;

public class NameEntityCriterion  extends SimpleCriterionTrigger<NameEntityCriterion.Instance> {
    private static final ResourceLocation ID = new ResourceLocation("name_animal");

    @Override
    protected NameEntityCriterion.Instance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
        EntityPredicate.Composite entitypredicate$andpredicate = EntityPredicate.Composite.fromJson(json, "entity", conditionsParser);
        return new NameEntityCriterion.Instance(entityPredicate, entitypredicate$andpredicate);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite entity;

        public Instance(EntityPredicate.Composite player, EntityPredicate.Composite entity) {
            super(NameEntityCriterion.ID, player);
            this.entity = entity;
        }

        public static NameEntityCriterion.Instance any() {
            return new NameEntityCriterion.Instance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
        }

        public static NameEntityCriterion.Instance create(EntityPredicate entityCondition) {
            return new NameEntityCriterion.Instance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(entityCondition));
        }

        public boolean test(LootContext context) {
            return this.entity.matches(context);
        }

        public JsonObject serializeToJson(SerializationContext conditions) {
            JsonObject jsonobject = super.serializeToJson(conditions);
            jsonobject.add("entity", this.entity.toJson(conditions));
            return jsonobject;
        }
    }
}
