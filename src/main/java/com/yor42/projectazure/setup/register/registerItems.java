package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.items.itemBaseTooltip;
import com.yor42.projectazure.gameobject.items.itemRainbowWisdomCube;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;

public class registerItems {

    public static final RegistryObject<Item> Rainbow_Wisdom_Cube = registerManager.ITEMS.register("rainbow_wisdomcube", () -> new itemRainbowWisdomCube(new Item.Properties()
            .group(ItemGroup.MISC)
            .rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> WISDOM_CUBE = registerManager.ITEMS.register("wisdomcube", () -> new itemBaseTooltip(new Item.Properties()
            .group(ItemGroup.MISC)
            .rarity(Rarity.UNCOMMON)));


    public static void register(){};

}
