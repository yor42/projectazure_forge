package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.client.renderer.equipment.Equipment127mmGunRenderer;
import com.yor42.projectazure.client.renderer.equipment.equipment533mmTorpedoRenderer;
import com.yor42.projectazure.client.renderer.items.*;
import com.yor42.projectazure.gameobject.items.*;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentGun127Mm;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentTorpedo533Mm;
import com.yor42.projectazure.gameobject.items.equipment.ItemPlanef4Fwildcat;
import com.yor42.projectazure.gameobject.items.gun.ItemAbydos550;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBBDefault;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingCVDefault;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.Main.*;
import static com.yor42.projectazure.setup.register.registerManager.*;

public class registerItems {

    //Resources.
    //Remove on 1.17
    public static final RegistryObject<Item> INGOT_COPPER = registerManager.ITEMS.register("ingot_copper", () -> new ItemResource("copper", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_LEAD = registerManager.ITEMS.register("ingot_lead", () ->new ItemResource("lead", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_TIN = registerManager.ITEMS.register("ingot_tin", () -> new ItemResource("tin", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_ALUMINIUM = registerManager.ITEMS.register("ingot_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_BRONZE = registerManager.ITEMS.register("ingot_bronze", () -> new ItemResource("bronze", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_ZINC = registerManager.ITEMS.register("ingot_zinc", () -> new ItemResource("zinc", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_STEEL = registerManager.ITEMS.register("ingot_steel", () -> new ItemResource("steel", enums.ResourceType.INGOT));

    public static final RegistryObject<Item> DUST_COPPER = registerManager.ITEMS.register("dust_copper", () -> new ItemResource("copper", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_LEAD = registerManager.ITEMS.register("dust_lead", () -> new ItemResource("lead", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_TIN = registerManager.ITEMS.register("dust_tin", () -> new ItemResource("tin", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ALUMINIUM = registerManager.ITEMS.register("dust_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_BRONZE = registerManager.ITEMS.register("dust_bronze", () -> new ItemResource("bronze", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ZINC = registerManager.ITEMS.register("dust_zinc", () -> new ItemResource("bronze", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_IRON = registerManager.ITEMS.register("dust_iron", () -> new ItemResource("iron", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_COAL = registerManager.ITEMS.register("dust_coal", () -> new ItemResource("coal", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_STEEL = registerManager.ITEMS.register("dust_steel", () -> new ItemResource("steel", enums.ResourceType.DUST));


    public static final RegistryObject<Item> PLATE_COPPER = registerManager.ITEMS.register("plate_copper", () -> new ItemResource("copper", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_LEAD = registerManager.ITEMS.register("plate_lead", () -> new ItemResource("lead", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_TIN = registerManager.ITEMS.register("plate_tin", () -> new ItemResource("tin", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_ALUMINIUM = registerManager.ITEMS.register("plate_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_BRONZE = registerManager.ITEMS.register("plate_bronze", () -> new ItemResource("bronze", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_ZINC = registerManager.ITEMS.register("plate_zinc", () -> new ItemResource("zinc", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_IRON = registerManager.ITEMS.register("plate_iron", () -> new ItemResource("iron", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_STEEL = registerManager.ITEMS.register("plate_steel", () -> new ItemResource("steel", enums.ResourceType.PLATE));

    public static final RegistryObject<Item> COPPER_WIRE = registerManager.ITEMS.register("copper_wire", () -> new ItemResource("copper", enums.ResourceType.WIRE));

    public static final RegistryObject<Item> IRON_PIPE = registerManager.ITEMS.register("iron_pipe", () -> new Item(new Item.Properties()
            .group(PA_RESOURCES)));
    public static final RegistryObject<Item> STEEL_PIPE = registerManager.ITEMS.register("steel_pipe", () -> new Item(new Item.Properties()
            .group(PA_RESOURCES)));
    public static final RegistryObject<Item> MECHANICAL_PARTS = registerManager.ITEMS.register("mechanical_parts", () -> new Item(new Item.Properties()
            .group(PA_RESOURCES)));
    public static final RegistryObject<Item> BASIC_MOTOR = registerManager.ITEMS.register("motor_basic", () -> new Item(new Item.Properties()
            .group(PA_RESOURCES)));
    public static final RegistryObject<Item> PRIMITIVE_CIRCUIT = registerManager.ITEMS.register("circuit_primitive", () -> new Item(new Item.Properties()
            .group(PA_RESOURCES)));
    public static final RegistryObject<Item> COPPER_COIL = registerManager.ITEMS.register("copper_coil", () -> new Item(new Item.Properties()
            .group(PA_RESOURCES)));


    public static final RegistryObject<Item> MOLD_PLATE = registerManager.ITEMS.register("mold_plate", () -> new Item(new Item.Properties()
            .group(PA_RESOURCES).maxDamage(128)){
        @Override
        public boolean hasContainerItem(ItemStack stack) {
            return true;
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemStack) {
            ItemStack stack = itemStack.copy();
            if (!stack.attemptDamageItem(1, MathUtil.getRand(), null)) {
                return stack;
            }
            else return ItemStack.EMPTY;
        }
    });


    //crafting items
    public static final RegistryObject<Item> MORTAR_IRON = registerManager.ITEMS.register("mortar_iron", () -> new ItemCraftTool(50));

    public static final RegistryObject<Item> HAMMER_IRON = registerManager.ITEMS.register("hammer_iron", () -> new ItemCraftTool(43));

    public static final RegistryObject<Item> STEEL_CUTTER = registerManager.ITEMS.register("steel_cutter", () -> new ItemCraftTool(70));


    public static final RegistryObject<Item> MAGAZINE_5_56 = registerManager.ITEMS.register("5.56_magazine", () -> new ItemMagazine(enums.AmmoCalibur.AMMO_5_56, 30, new Item.Properties()
            .group(PA_GROUP)
            .maxStackSize(1)
            .maxDamage(7)));

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

    public static final RegistryObject<Item> AMMO_GENERIC = registerManager.ITEMS.register("ammo_generic", () -> new ItemCannonshell(enums.AmmoCategory.GENERIC ,new Item.Properties()
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

    public static final RegistryObject<Item> ABYDOS_550 = registerManager.ITEMS.register("abydos550", () -> new ItemAbydos550(false, 2, 30, 72, 3, registerSounds.RIFLE_FIRE_SUPPRESSED, SoundEvents.BLOCK_LEVER_CLICK, 0, (float) 0.3, new Item.Properties()
            .setISTER(() -> ItemAbydos550Renderer::new)
            .group(PA_GROUP).maxStackSize(1), true, MAGAZINE_5_56.get()));



    public static final RegistryObject<Item> BONKBAT = registerManager.ITEMS.register("bonk_bat", () -> new ItemBonkBat(new Item.Properties()
            .group(PA_GROUP)
            .rarity(Rarity.EPIC)
            .maxStackSize(1)));

    public static final RegistryObject<Item> DD_DEFAULT_RIGGING = registerManager.ITEMS.register("dd_default_rigging", () -> new itemRiggingDDDefault(new Item.Properties()
    .setISTER(() -> DDDefaultRiggingRenderer::new)
    .group(PA_GROUP).maxStackSize(1), 500));

    public static final RegistryObject<Item> CV_DEFAULT_RIGGING = registerManager.ITEMS.register("cv_default_rigging", () -> new ItemRiggingCVDefault(new Item.Properties()
            .setISTER(() -> CVDefaultRiggingRenderer::new)
            .group(PA_GROUP).maxStackSize(1), 750));

    public static final RegistryObject<Item> BB_DEFAULT_RIGGING = registerManager.ITEMS.register("bb_default_rigging", () -> new ItemRiggingBBDefault(new Item.Properties()
            .setISTER(() -> BBDefaultRiggingRenderer::new)
            .group(PA_GROUP).maxStackSize(1), 1200));

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


    public static final RegistryObject<Item> SPAWN_NAGATO = registerManager.ITEMS.register("spawnnagato", () -> new ItemKansenSpawnEgg(ENTITYTYPE_NAGATO, new Item.Properties()
            .group(PA_SHIPS)));


    public static final RegistryObject<Item> SPAWN_SHIROKO = registerManager.ITEMS.register("spawnshiroko", () -> new ItemKansenSpawnEgg(SHIROKO_ENTITY_TYPE, new Item.Properties()
            .group(PA_SHIPS)));

    public static final RegistryObject<Item> BANDAGE_ROLL = registerManager.ITEMS.register("bandage_roll", () -> new ItemBandage(new Item.Properties()
            .group(PA_GROUP)
    .maxStackSize(1)
    .maxDamage(7)));




    public static void register(){};

}
