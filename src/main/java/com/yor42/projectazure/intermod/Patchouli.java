package com.yor42.projectazure.intermod;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class Patchouli {

    public static void HandlePatchouliCompatibility(PlayerEntity player){
        PlayerInventory inv = player.inventory;

        if(!CompatibilityUtils.isPatchouliLoaded()) {
            player.sendMessage(new TranslationTextComponent("message.nopatchouliinstalled"), UUID.randomUUID());
        }

        Item book = ForgeRegistries.ITEMS.getValue(new ResourceLocation("patchouli:guide_book"));

        if (book == null || !PAConfig.CONFIG.GiveCodexOnSpawn.get()) {
            return;
        }

        ItemStack bookstack = new ItemStack(book);
        bookstack.getOrCreateTag().putString("patchouli:book", "projectazure:codex");
        inv.add(inv.getFreeSlot(), bookstack);
    }

}
