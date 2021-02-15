package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.client.renderer.equipment.Equipment127mmGunRenderer;
import com.yor42.projectazure.client.renderer.equipment.equipment533mmTorpedoRenderer;
import com.yor42.projectazure.client.renderer.items.DDDefaultRiggingRenderer;
import com.yor42.projectazure.gameobject.items.*;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentGun127mm;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentTorpedo533mm;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.Main.PA_GROUP;

public class registerItems {

    public static final RegistryObject<Item> Rainbow_Wisdom_Cube = registerManager.ITEMS.register("rainbow_wisdomcube", () -> new itemRainbowWisdomCube(new Item.Properties()
            .group(PA_GROUP)
            .rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> WISDOM_CUBE = registerManager.ITEMS.register("wisdomcube", () -> new itemBaseTooltip(new Item.Properties()
            .group(PA_GROUP)
            .rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> OATHRING = registerManager.ITEMS.register("oath_ring", () -> new itemRainbowWisdomCube(new Item.Properties()
            .group(PA_GROUP)
            .rarity(Rarity.EPIC)
            .maxStackSize(1)));

    public static final RegistryObject<Item> AMMO_GENERIC = registerManager.ITEMS.register("ammo_generic", () -> new ItemAmmo(enums.AmmoCategory.GENERIC ,new Item.Properties()
            .group(PA_GROUP)));

    //I'M READY TO GO TONIIIIGHT YEAH THERES PARTY ALRIGHTTTTTTT WE DON'T NEED REASON FOR JOY OH YEAHHHHH
    public static final RegistryObject<Item> DISC_FRIDAYNIGHT = registerManager.ITEMS.register("disc_fridaynight", () -> new MusicDiscItem(15, registerSounds.DISC_FRIDAY_NIGHT, new Item.Properties()
            .group(PA_GROUP).maxStackSize(1)){
        @Override
        public ITextComponent getDisplayName(ItemStack stack) {
            return new TranslationTextComponent("item.projectazure.music_disc");
        }
    });

    //LET THE BASS KICK O-oooooooooo AAAA E A A I A U
    public static final RegistryObject<Item> DISC_BRAINPOWER = registerManager.ITEMS.register("disc_brainpower", () -> new MusicDiscItem(15, registerSounds.DISC_BRAINPOWER, new Item.Properties()
            .group(PA_GROUP).maxStackSize(1))
    {
        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            tooltip.add(new TranslationTextComponent("disc.brainpower.desc1").setStyle(Style.EMPTY.setColor(Color.fromInt(7829367))));
        }

        @Override
        public ITextComponent getDisplayName(ItemStack stack) {
            return new TranslationTextComponent("item.projectazure.music_disc");
        }
    });

    public static final RegistryObject<Item> DD_DEFAULT_RIGGING = registerManager.ITEMS.register("dd_default_rigging", () -> new itemRiggingDDDefault(new Item.Properties()
    .setISTER(() -> DDDefaultRiggingRenderer::new)
    .group(PA_GROUP).maxStackSize(1), 500));

    public static final RegistryObject<Item> EQUIPMENT_TORPEDO_533MM = registerManager.ITEMS.register("equipment_torpedo_533mm", () -> new ItemEquipmentTorpedo533mm(new Item.Properties()
            .setISTER(() -> equipment533mmTorpedoRenderer::new)
            .group(PA_GROUP).maxStackSize(1), 40));

    public static final RegistryObject<Item> EQUIPMENT_GUN_127MM = registerManager.ITEMS.register("equipment_gun_127mm", () -> new ItemEquipmentGun127mm(new Item.Properties()
            .setISTER(() -> Equipment127mmGunRenderer::new)
            .group(PA_GROUP).maxStackSize(1), 40));
    /*
    public static final RegistryObject<Item> SPAWM_AYANAMI = registerManager.ITEMS.register("spawnayanami", () -> new ItemKansenSpawnEgg(registerManager.AYANAMI.get(), new Item.Properties()
            .group(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_GANGWON = registerManager.ITEMS.register("spawngangwon", () -> new ItemKansenSpawnEgg(registerManager.GANGWON.get(), new Item.Properties()
            .group(PA_SHIPS)));

     */


    public static void register(){};

}
