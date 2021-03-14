package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.client.renderer.equipment.Equipment127mmGunRenderer;
import com.yor42.projectazure.client.renderer.equipment.equipment533mmTorpedoRenderer;
import com.yor42.projectazure.client.renderer.items.CVDefaultRiggingRenderer;
import com.yor42.projectazure.client.renderer.items.DDDefaultRiggingRenderer;
import com.yor42.projectazure.client.renderer.items.ItemAbydos550Renderer;
import com.yor42.projectazure.client.renderer.items.ItemPlanef4fWildcatRenderer;
import com.yor42.projectazure.gameobject.items.*;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentGun127Mm;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentTorpedo533Mm;
import com.yor42.projectazure.gameobject.items.equipment.ItemPlanef4Fwildcat;
import com.yor42.projectazure.gameobject.items.gun.ItemAbydos550;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingCVDefault;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.Main.PA_GROUP;
import static com.yor42.projectazure.Main.PA_SHIPS;
import static com.yor42.projectazure.setup.register.registerManager.*;

public class registerItems {

    public static final RegistryObject<Item> Rainbow_Wisdom_Cube = registerManager.ITEMS.register("rainbow_wisdomcube", () -> new itemRainbowWisdomCube(new Item.Properties()
            .group(PA_GROUP)
            .rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> WISDOM_CUBE = registerManager.ITEMS.register("wisdomcube", () -> new ItemBaseTooltip(new Item.Properties()
            .group(PA_GROUP)
            .rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> OATHRING = registerManager.ITEMS.register("oath_ring", () -> new ItemBaseTooltip(new Item.Properties()
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

    public static final RegistryObject<Item> ABYDOS_550 = registerManager.ITEMS.register("abydos550", () -> new ItemAbydos550(false, 10, 30, 60, 3, SoundEvents.ENTITY_EGG_THROW, SoundEvents.BLOCK_LEVER_CLICK, 30, (float) 0.3, new Item.Properties()
            .setISTER(() -> ItemAbydos550Renderer::new)
            .group(PA_GROUP).maxStackSize(1)));



    public static final RegistryObject<Item> BONKBAT = registerManager.ITEMS.register("bonk_bat", () -> new ItemBonkBat(new Item.Properties()
            .group(PA_GROUP)
            .rarity(Rarity.EPIC)
            .maxStackSize(1)));

    public static final RegistryObject<Item> DD_DEFAULT_RIGGING = registerManager.ITEMS.register("dd_default_rigging", () -> new itemRiggingDDDefault(new Item.Properties()
    .setISTER(() -> DDDefaultRiggingRenderer::new)
    .group(PA_GROUP).maxStackSize(1), 500));

    public static final RegistryObject<Item> CV_DEFAULT_RIGGING = registerManager.ITEMS.register("cv_default_rigging", () -> new ItemRiggingCVDefault(new Item.Properties()
            .setISTER(() -> CVDefaultRiggingRenderer::new)
            .group(PA_GROUP).maxStackSize(1), 1000));

    public static final RegistryObject<Item> EQUIPMENT_TORPEDO_533MM = registerManager.ITEMS.register("equipment_torpedo_533mm", () -> new ItemEquipmentTorpedo533Mm(new Item.Properties()
            .setISTER(() -> equipment533mmTorpedoRenderer::new)
            .group(PA_GROUP).maxStackSize(1), 40));

    public static final RegistryObject<Item> EQUIPMENT_GUN_127MM = registerManager.ITEMS.register("equipment_gun_127mm", () -> new ItemEquipmentGun127Mm(new Item.Properties()
            .setISTER(() -> Equipment127mmGunRenderer::new)
            .group(PA_GROUP).maxStackSize(1), 40));

    public static float WildcatHP = 30;

    public static final RegistryObject<Item> EQUIPMENT_PLANE_F4FWildcat = registerManager.ITEMS.register("equipment_plane_f4fwildcat", () -> new ItemPlanef4Fwildcat(new Item.Properties()
            .setISTER(() -> ItemPlanef4fWildcatRenderer::new)
            .group(PA_GROUP).maxStackSize(1), (int) WildcatHP));


    public static final RegistryObject<Item> SPAWM_AYANAMI = registerManager.ITEMS.register("spawnayanami", () -> new ItemKansenSpawnEgg(ENTITYAYANAMI, new Item.Properties()
            .group(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWN_GANGWON = registerManager.ITEMS.register("spawngangwon", () -> new ItemKansenSpawnEgg(ENTITYGANGWON, new Item.Properties()
            .group(PA_SHIPS)));

    public static final RegistryObject<Item> SPAWM_ENTERPRISE = registerManager.ITEMS.register("spawnenterprise", ()-> new ItemKansenSpawnEgg(ENTERPRISE_ENTITY_TYPE, new Item.Properties().group(PA_SHIPS)));



    public static final RegistryObject<Item> SPAWN_SHIROKO = registerManager.ITEMS.register("spawnshiroko", () -> new ItemKansenSpawnEgg(SHIROKO_ENTITY_TYPE, new Item.Properties()
            .group(PA_SHIPS)));

    public static final RegistryObject<Item> BANDAGE_ROLL = registerManager.ITEMS.register("bandage_roll", () -> new ItemBandage(new Item.Properties()
            .group(PA_GROUP)
    .maxStackSize(1)
    .maxDamage(7)));





    public static void register(){};

}
