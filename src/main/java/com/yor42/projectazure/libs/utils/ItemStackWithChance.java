package com.yor42.projectazure.libs.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/*
 * This class is distributed as part of the Ex Nihilo Sequentia Mod.
 * Get the Source Code in github:
 * https://github.com/NovaMachina-Mods/ExNihiloSequentia
 *
 * Ex Nihilo Sequentia is Open Source and distributed under the
 * CC BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en
 */
public class ItemStackWithChance {
    private static final String BASE_KEY = "item";
    private static final String CHANCE_KEY = "chance";
    private static final String COUNT_KEY = "count";
    private final float chance;
    private final ItemStack itemStack;

    public ItemStackWithChance(ItemStack itemStack, float chance) {
        this.itemStack = itemStack;
        this.chance = chance;
    }

    public static ItemStackWithChance deserialize(JsonElement json) {
        if (json.isJsonObject() && json.getAsJsonObject().has(BASE_KEY)) {
            final float chance = GsonHelper.getAsFloat(json.getAsJsonObject(), CHANCE_KEY, 1.0F);
            final String itemString = GsonHelper.getAsString(json.getAsJsonObject(), BASE_KEY);
            int count = 1;
            if (json.getAsJsonObject().has(COUNT_KEY)) {
                count = json.getAsJsonObject().get(COUNT_KEY).getAsInt();
            }
            return new ItemStackWithChance(new ItemStack(ForgeRegistries.ITEMS
                    .getValue(new ResourceLocation(itemString)), count), chance);
        } else {
            final String itemString = GsonHelper.convertToString(json, BASE_KEY);
            return new ItemStackWithChance(new ItemStack(ForgeRegistries.ITEMS
                    .getValue(new ResourceLocation(itemString))), 1.0F);
        }
    }

    public static ItemStackWithChance read(FriendlyByteBuf buffer) {
        final ItemStack stack = buffer.readItem();
        final float chance = buffer.readFloat();
        return new ItemStackWithChance(stack, chance);
    }

    public float getChance() {
        return chance;
    }

    public ItemStack getStack() {
        return itemStack.copy();
    }

    public JsonElement serialize() {
        JsonObject json = new JsonObject();
        json.addProperty(CHANCE_KEY, getChance());
        json.addProperty(BASE_KEY, getStack().getItem().getRegistryName().toString());
        json.addProperty(COUNT_KEY, getStack().getCount());
        return json;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(getStack());
        buffer.writeFloat(getChance());
    }
}
