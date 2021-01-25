package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.items.itemBaseTooltip;
import com.yor42.projectazure.gameobject.items.itemRainbowWisdomCube;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;

import static com.yor42.projectazure.Main.PA_GROUP;

public class registerItems {

    public static final RegistryObject<Item> Rainbow_Wisdom_Cube = registerManager.ITEMS.register("rainbow_wisdomcube", () -> new itemRainbowWisdomCube(new Item.Properties()
            .group(PA_GROUP)
            .rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> WISDOM_CUBE = registerManager.ITEMS.register("wisdomcube", () -> new itemBaseTooltip(new Item.Properties()
            .group(PA_GROUP)
            .rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> DISC_FRIDAYNIGHT = registerManager.ITEMS.register("disc_fridaynight", () -> new MusicDiscItem(15, registerSounds.DISC_FRIDAY_NIGHT, new Item.Properties()
            .group(PA_GROUP)){
        @Override
        public int getItemStackLimit(ItemStack stack) {
            return 1;
        }
    });


    public static void register(){};

}
