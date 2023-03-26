package com.yor42.projectazure.intermod;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class Patchouli {

    public static void HandlePatchouliCompatibility(Player player){
        Inventory inv = player.inventory;

        if(!CompatibilityUtils.isPatchouliLoaded()) {
            player.sendMessage(new TranslatableComponent("message.nopatchouliinstalled", new TranslatableComponent("modnames.patchouli").withStyle(ChatFormatting.AQUA).setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/search?page=1&&pageSize=20&sortType=2&class=mc-mods")))).withStyle(ChatFormatting.GRAY), UUID.randomUUID());
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
