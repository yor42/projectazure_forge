package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.capability.ICapabilityProxyHolder;
import com.lowdragmc.multiblocked.api.capability.IO;
import com.lowdragmc.multiblocked.api.capability.MultiblockCapability;
import com.lowdragmc.multiblocked.api.capability.proxy.CapabilityProxy;
import com.lowdragmc.multiblocked.api.recipe.Content;
import com.lowdragmc.multiblocked.api.recipe.EntityIngredient;
import com.lowdragmc.multiblocked.api.recipe.Recipe;
import com.lowdragmc.multiblocked.api.recipe.RecipeCondition;
import com.lowdragmc.multiblocked.common.capability.EntityMultiblockCapability;
import com.yor42.projectazure.gameobject.blocks.tileentity.AbstractTileEntityGacha;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.capability.CompanionMultiblockCapability;
import com.yor42.projectazure.libs.utils.MathUtil;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.entity.EntityType;
import net.minecraft.util.text.ITextComponent;

import java.util.*;

public class WeightedRandomRecipe extends Recipe {

    private float accumulatedWeight;

    private static class Entry {
        double accumulatedWeight;
        Content content;

        public Entry(Content content, float weight){
            this.accumulatedWeight = weight;
            this.content = content;
        }
    }

    private final List<Entry> entries = new ArrayList<>();

    public WeightedRandomRecipe(String uid,
                  ImmutableMap<MultiblockCapability<?>, ImmutableList<Content>> inputs,
                  ImmutableMap<MultiblockCapability<?>, ImmutableList<Content>> outputs,
                  ImmutableMap<MultiblockCapability<?>, ImmutableList<Content>> tickInputs,
                  ImmutableMap<MultiblockCapability<?>, ImmutableList<Content>> tickOutputs,
                  ImmutableList<RecipeCondition> conditions,
                  ImmutableMap<String, Object> data,
                  ITextComponent text,
                  int duration) {
        super(uid, inputs, outputs, tickInputs, tickOutputs, conditions,data, text, duration);
        for(Content content:outputs.get(CompanionMultiblockCapability.CAP)){
            float chance = content.chance;
            if(chance==1){
                entries.add(new Entry(content, -1));
            }
            else {
                this.accumulatedWeight+=content.chance;
                entries.add(new Entry(content, this.accumulatedWeight));
            }
        }
    }

    public void additionalOutputProcess(Content content){}

    @SuppressWarnings("ALL")
    public boolean handleRecipe(IO io, ICapabilityProxyHolder holder, ImmutableMap<MultiblockCapability<?>, ImmutableList<Content>> contents) {
        Table<IO, MultiblockCapability<?>, Long2ObjectOpenHashMap<CapabilityProxy<?>>> capabilityProxies = holder.getCapabilitiesProxy();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList<Content>> entry : contents.entrySet()) {
            Set<CapabilityProxy<?>> used = new HashSet<>();
            List content = new ArrayList<>();
            Map<String, List> contentSlot = new HashMap<>();
            if(io==IO.OUT){
                float weight = MathUtil.getRand().nextFloat() * accumulatedWeight;
                for (Entry item: entries) {
                    if (item.accumulatedWeight >= weight || item.accumulatedWeight == -1) {
                        Content result = item.content;
                        additionalOutputProcess(result);
                        Object output = result.content;
                        if (item.content.slotName == null) {
                            content.add(output);
                        } else {
                            contentSlot.computeIfAbsent(item.content.slotName, s -> new ArrayList<>()).add(output);
                        }
                        break;
                    }
                }

            }else {
                for (Content cont : entry.getValue()) {
                    if (cont.chance == 1 || Multiblocked.RNG.nextFloat() < cont.chance) { // chance input
                        if (cont.slotName == null) {
                            content.add(cont.content);
                        } else {
                            contentSlot.computeIfAbsent(cont.slotName, s -> new ArrayList<>()).add(cont.content);
                        }
                    }
                }
            }
            if (content.isEmpty() && contentSlot.isEmpty()) continue;
            if (content.isEmpty()) content = null;
            if (capabilityProxies.contains(io, entry.getKey())) {
                for (CapabilityProxy<?> proxy : capabilityProxies.get(io, entry.getKey()).values()) { // search same io type
                    if (used.contains(proxy)) continue;
                    used.add(proxy);
                    if (content != null) {
                        content = proxy.handleRecipe(io, this, content, null);
                    }
                    if (proxy.slots != null) {
                        Iterator<String> iterator = contentSlot.keySet().iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            if (proxy.slots.contains(key)) {
                                List<?> left = proxy.handleRecipe(io, this, contentSlot.get(key), key);
                                if (left == null) iterator.remove();
                            }
                        }
                    }
                    if (content == null && contentSlot.isEmpty()) break;
                }
            }
            if (content == null && contentSlot.isEmpty()) continue;
            if (capabilityProxies.contains(IO.BOTH, entry.getKey())){
                for (CapabilityProxy<?> proxy : capabilityProxies.get(IO.BOTH, entry.getKey()).values()) { // search both type
                    if (used.contains(proxy)) continue;
                    used.add(proxy);
                    if (content != null) {
                        content = proxy.handleRecipe(io, this, content, null);
                    }
                    if (proxy.slots != null) {
                        Iterator<String> iterator = contentSlot.keySet().iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            if (proxy.slots.contains(key)) {
                                List<?> left = proxy.handleRecipe(io, this, contentSlot.get(key), key);
                                if (left == null) iterator.remove();
                            }
                        }
                    }
                    if (content == null && contentSlot.isEmpty()) break;
                }
            }
            if (content != null || !contentSlot.isEmpty()) {
                Multiblocked.LOGGER.warn("io error while handling a recipe {} outputs. holder: {}", uid, holder);
                return false;
            }
        }
        return true;
    }
}
