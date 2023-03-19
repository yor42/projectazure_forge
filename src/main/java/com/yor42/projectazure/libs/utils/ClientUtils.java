package com.yor42.projectazure.libs.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {
    public static World getClientWorld()
    {
        return Minecraft.getInstance().level;
    }

    public static void RegisterModelProperties(Item item, String name, IItemPropertyGetter property){
        ItemModelsProperties.register(item, ResourceUtils.ModResourceLocation(name), property);
    }

    public static void RegisterModelProperties(RegistryObject<Item> item, String name, IItemPropertyGetter property){
        RegisterModelProperties(item.get(), name, property);
    }

}
