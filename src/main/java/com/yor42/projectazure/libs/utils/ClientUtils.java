package com.yor42.projectazure.libs.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.item.Item;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {
    public static Level getClientWorld()
    {
        return Minecraft.getInstance().level;
    }

    public static void RegisterModelProperties(Item item, String name, ItemPropertyFunction property){
        ItemProperties.register(item, ResourceUtils.ModResourceLocation(name), property);
    }

    public static void RegisterModelProperties(RegistryObject<Item> item, String name, ItemPropertyFunction property){
        RegisterModelProperties(item.get(), name, property);
    }

}
