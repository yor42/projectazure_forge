package com.yor42.projectazure.setup.register;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lowdragmc.lowdraglib.utils.FileUtility;
import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.definition.ComponentDefinition;
import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.recipe.ingredient.EntityIngredient;
import com.lowdragmc.multiblocked.api.recipe.ingredient.SizedIngredient;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.FluidMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.mojang.datafixers.util.Pair;
import com.tac.guns.init.ModItems;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipebuilders.WeightedRecipeBuilder;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class registerMultiBlocks {

    public static final ArrayList<Pair<String, ResourceLocation>> DEFINITIONS = new ArrayList<>();

    public static void registerMultiBlocksonEvent(RegistryEvent.Register<Block> event){
        HatchTE.registerItemHatch();
        OriginiumGeneratorControllerTE.registerTE();
        AmmoPressControllerTE.registerTE();
        RiftwayControllerTE.registerTE();
        AdvancedAlloySmelterControllerTE.registerTE();
        SiliconeCrucibleTE.RegisterTE();
        //registerTEandRecipefromJSON("silicone_crucible");
    }

    public static void registerTEandRecipefromJSON(String name){
        DEFINITIONS.add(new Pair<>(name, registerComponentFromResource(Multiblocked.GSON, ResourceUtils.ModResourceLocation("controller/"+name), ControllerDefinition::new, null)));
    }

    public static <T extends ComponentDefinition> ResourceLocation registerComponentFromResource(Gson gson, ResourceLocation location, Function<ResourceLocation, T> constructor, BiConsumer<T, JsonObject> postHandler) {
        return registerComponentFromResource(location, constructor, null, null, postHandler);
    }

    public static <T extends ComponentDefinition, B extends Block> ResourceLocation registerComponentFromResource(ResourceLocation location, Function<ResourceLocation, T> constructor, @Nullable Function<T, B> block, @Nullable Function<B, BlockItem> item, BiConsumer<T, JsonObject> postHandler) {
        try {
            InputStream inputstream = ResourceLocation.class.getResourceAsStream(String.format("/assets/%s/definition/%s.json", location.getNamespace(), location.getPath()));
            JsonObject config = FileUtility.jsonParser.parse(new InputStreamReader(inputstream)).getAsJsonObject();
            T definition = (T) constructor.apply(new ResourceLocation(config.get("location").getAsString()));
            definition.fromJson(config);
            MbdComponents.registerComponent(definition);
            return definition.location;
        } catch (Exception var9) {
            Multiblocked.LOGGER.error("error while loading the definition resource {}", location.toString());
        }
        return null;
    }

    public static JsonObject readTrait(ResourceLocation location) {
        InputStream inputstream = Main.class.getResourceAsStream(String.format("/assets/%s/traits/%s.json", location.getNamespace(), location.getPath()));
        if(inputstream==null){
            return new JsonObject();
        }
        JsonObject config = JsonParser.parseReader(new InputStreamReader(inputstream)).getAsJsonObject();
        return config.has("traits")? config.getAsJsonObject("traits"): config;
    }

    public static CompoundTag readUI(ResourceLocation location) {
        try {
            String file = String.format("/assets/%s/uis/%s.mbdui", location.getNamespace(), location.getPath());
            InputStream inputstream = Main.class.getResourceAsStream(file);
            if(inputstream == null){
                return null;
            }
            return NbtIo.read(new DataInputStream(inputstream));
        } catch (IOException var9) {
            Main.LOGGER.error("error while loading the UI {}", location.toString());
        }
        return null;
    }

    public static void registerRecipes(ServerStartedEvent event){

        Main.LOGGER.debug("Starting multiblock recipe registration.");
        try{
            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getRecipeMap().start().name("prime_to_power").input(ItemMultiblockCapability.CAP, Ingredient.of(ModTags.Items.ORIGINIUM_PRIME)).perTick(true).output(FEMultiblockCapability.CAP, 2000).duration(1200).buildAndRegister();
            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getRecipeMap().start().name("originite_to_power").input(ItemMultiblockCapability.CAP, Ingredient.of(ModTags.Items.ORIGINITE)).perTick(true).output(FEMultiblockCapability.CAP, 4000).duration(400).buildAndRegister();

            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getRecipeMap().start().name("amber_originium_to_power").input(ItemMultiblockCapability.CAP, Ingredient.of(RegisterItems.AMBER_ORIGINIUM.get())).perTick(true).output(FEMultiblockCapability.CAP, 8000).duration(500).buildAndRegister();
            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getRecipeMap().start().name("amber_originium_fuel_to_power").input(ItemMultiblockCapability.CAP, Ingredient.of(RegisterItems.AMBER_ORIGINIUM_FUEL_ROD.get())).perTick(true).output(FEMultiblockCapability.CAP, 10000).duration(1000).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("9mm")
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(ModTags.Items.PLATE_BRASS))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(Tags.Items.GUNPOWDER))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(ModTags.Items.INGOT_LEAD))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(Tags.Items.INGOTS_COPPER))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModItems.BULLET_9.get()), 16))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("12gauge")
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(ModTags.Items.PLATE_BRASS))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(Tags.Items.GUNPOWDER))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 12))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Items.PAPER), 2))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_10g.get()), 16))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("50bmg")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 2))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 3))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 3))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.GEMS_DIAMOND), 1))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_50_BMG.get()), 12))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("30winchester")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 2))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 8))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 8))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_30_WIN.get()), 10))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("308win")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 3))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 8))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(Tags.Items.INGOTS_COPPER))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_308.get()), 24))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("4mmcaseless")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.GUNPOWDER_COMPOUND.get()), 4))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 4))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(RegisterItems.CASELESS_4MM.get()), 12))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("7.62x25")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(Tags.Items.GUNPOWDER))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 1))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 4))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_762x39.get()), 30))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("7.62x39")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(Tags.Items.GUNPOWDER))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 1))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 4))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_762x39.get()), 30))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("7.62x54")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 2))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(Tags.Items.GUNPOWDER))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 2))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.INGOTS_COPPER), 2))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_762x39.get()), 20))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("45acp")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(Tags.Items.GUNPOWDER))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 16))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.INGOTS_COPPER), 1))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_45.get()), 16))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("5.56")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(Tags.Items.GUNPOWDER))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 16))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 24))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_556.get()), 32))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("5.8mm")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.PLATE_IRON), 1))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 2))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 1))
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.INGOTS_COPPER), 1))
                    .output(ItemMultiblockCapability.CAP,  new SizedIngredient(Ingredient.of(ModItems.BULLET_58x42.get()), 40))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("rma70-24")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_RMA7012), 1), new SizedIngredient(Ingredient.of(ModTags.Items.ORES_ORIROCK), 2))
                    .input(FluidMultiblockCapability.CAP, new FluidStack(RegisterFluids.KETON_SOURCE_REGISTRY.get(), 100))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.INGOT_RMA7024.get()),2))
                    .perTick(true).input(FEMultiblockCapability.CAP, 300).duration(400).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("bronze")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.INGOTS_COPPER), 3), new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_TIN), 1))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.INGOT_BRONZE.get()),4))
                    .perTick(true).input(FEMultiblockCapability.CAP, 120).duration(100).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("brass")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.INGOTS_COPPER),1), new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_ZINC),1))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.INGOT_BRASS.get()),2))
                    .perTick(true).input(FEMultiblockCapability.CAP, 120).duration(100).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("originium_vitrified")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.ORIGINIUM_PRIME),1), new SizedIngredient(Ingredient.of(Tags.Items.GEMS_QUARTZ), 2))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.VITRIFIED_ORIGINIUM.get()),2))
                    .perTick(true).input(FEMultiblockCapability.CAP, 200).duration(200).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("originium_amber")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.ORIGINIUM_PRIME),1), new SizedIngredient(Ingredient.of(Tags.Items.GEMS_QUARTZ), 1), new SizedIngredient(Ingredient.of(Tags.Items.RODS_BLAZE), 1))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.AMBER_ORIGINIUM.get()),2))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(500).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("c99")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ItemTags.COALS),1))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.C99_CARBON.get()),2))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(500).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("incandescent_alloy")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_MANGANESE),1), new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_RMA7012),1))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.INGOT_INCANDESCENT_ALLOY.get()),2))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(500).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("loxickohl")
                    .input(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(Tags.Items.SLIMEBALLS),1), new SizedIngredient(Ingredient.of(RegisterItems.TREE_SAP.get()), 4))
                    .input(FluidMultiblockCapability.CAP, new FluidStack(RegisterFluids.KETON_SOURCE_REGISTRY.get(), 100))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.LOXICKOHL.get()),2))
                    .perTick(true).input(FEMultiblockCapability.CAP, 200).duration(500).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("d32")
                    .input(ItemMultiblockCapability.CAP, Ingredient.of(ModTags.Items.INGOT_RMA7012), new SizedIngredient(Ingredient.of(ModTags.Items.INGOT_MANGANESE), 4), new SizedIngredient(Ingredient.of(RegisterItems.LOXICKOHL.get()), 2))
                    .input(FluidMultiblockCapability.CAP, new FluidStack(RegisterFluids.KETON_SOURCE_REGISTRY.get(), 200))
                    .output(ItemMultiblockCapability.CAP, new SizedIngredient(Ingredient.of(RegisterItems.INGOT_D32.get()),2))
                    .perTick(true).input(FEMultiblockCapability.CAP, 500).duration(1000).buildAndRegister();

            new WeightedRecipeBuilder(RiftwayControllerTE.RIFTWAYRECIPEMAP).name("akn")
                    .inputItems(new SizedIngredient(Ingredient.of(RegisterItems.ORUNDUM.get()),10), new SizedIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .addCompanionOutput(registerEntity.AMIYA.get())
                    .addCompanionOutput(registerEntity.NEARL.get())
                    .addCompanionOutput(registerEntity.W.get())
                    .addCompanionOutput(registerEntity.SCHWARZ.get())
                    .addCompanionOutput(registerEntity.SIEGE.get())
                    .addCompanionOutput(registerEntity.SCHWARZ.get())
                    .addCompanionOutput(registerEntity.LAPPLAND.get())
                    .addCompanionOutput(registerEntity.TEXAS.get())
                    .addCompanionOutput(registerEntity.CHEN.get())
                    .addCompanionOutput(registerEntity.MUDROCK.get())
                    .addCompanionOutput(registerEntity.YATO.get())
                    .addCompanionOutput(registerEntity.ROSMONTIS.get())
                    .addCompanionOutput(registerEntity.CROWNSLAYER.get())
                    .addCompanionOutput(registerEntity.FROSTNOVA.get())
                    .addCompanionOutput(registerEntity.TALULAH.get())
                    .chance(1F).perTick(true).inputFE(2500).duration(1200).buildAndRegisterRiftway();

            new WeightedRecipeBuilder(RiftwayControllerTE.RIFTWAYRECIPEMAP).name("fgo")
                    .inputItems(new SizedIngredient(Ingredient.of(RegisterItems.SAINT_QUARTZ.get()),3), new SizedIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .addCompanionOutput(registerEntity.MASH.get())
                    .addCompanionOutput(registerEntity.ARTORIA.get())
                    .addCompanionOutput(registerEntity.SCATHATH.get())
                    .addCompanionOutput(registerEntity.SHIKI.get())
                    .perTick(true).inputFE(2500).duration(1200).buildAndRegisterRiftway();

            new WeightedRecipeBuilder(RiftwayControllerTE.RIFTWAYRECIPEMAP).name("al")
                    .inputItems(new SizedIngredient(Ingredient.of(RegisterItems.WISDOM_CUBE.get()),2), new SizedIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .addCompanionOutput(registerEntity.ENTERPRISE.get())
                    .addCompanionOutput(registerEntity.AYANAMI.get())
                    .addCompanionOutput(registerEntity.Z23.get())
                    .addCompanionOutput(registerEntity.LAFFEY.get())
                    .addCompanionOutput(registerEntity.JAVELIN.get())
                    .addCompanionOutput(registerEntity.NAGATO.get())
                    .perTick(true).inputFE(2500).duration(1200).buildAndRegisterRiftway();

            new WeightedRecipeBuilder(RiftwayControllerTE.RIFTWAYRECIPEMAP).name("gfl")
                    .inputItems(new SizedIngredient(Ingredient.of(RegisterItems.COMPUTERCORE.get()),3), new SizedIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .addCompanionOutput(registerEntity.HK416.get())
                    .addCompanionOutput(registerEntity.M4A1.get())
                    .perTick(true).inputFE(2500).duration(1200).buildAndRegisterRiftway();

            new WeightedRecipeBuilder(RiftwayControllerTE.RIFTWAYRECIPEMAP).name("etc")
                    .inputItems(new SizedIngredient(Ingredient.of(RegisterItems.HEADHUNTING_PCB.get()),2), new SizedIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .addCompanionOutput(registerEntity.SHIROKO.get())
                    .addCompanionOutput(registerEntity.SYLVI.get())
                    .addCompanionOutput(registerEntity.KYARU.get())
                    .addCompanionOutput(registerEntity.EXCELA.get())
                    .addCompanionOutput(registerEntity.YAMATO.get())
                    .perTick(true).inputFE(2500).duration(1200).buildAndRegisterRiftway();

            RiftwayControllerTE.RIFTWAYRECIPEMAP.start().name("cow")
                    .inputItems(new SizedIngredient(Ingredient.of(Tags.Items.CROPS_WHEAT),3), new SizedIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .outputEntities(EntityIngredient.of(EntityType.COW.getRegistryName()))
                    .perTick(true).inputFE(100).duration(120).buildAndRegister();

            RiftwayControllerTE.RIFTWAYRECIPEMAP.start().name("chicken")
                    .inputItems(new SizedIngredient(Ingredient.of(Tags.Items.SEEDS_WHEAT),1), new SizedIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .outputEntities(EntityIngredient.of(EntityType.CHICKEN.getRegistryName()))
                    .chance(0.75F).outputEntities(EntityIngredient.of(EntityType.CHICKEN.getRegistryName()))
                    .chance(0.3F).outputEntities(EntityIngredient.of(EntityType.CHICKEN.getRegistryName()))
                    .chance(1).perTick(true).inputFE(100).duration(600).buildAndRegister();

            RiftwayControllerTE.RIFTWAYRECIPEMAP.start().name("sheep")
                    .inputItems(new SizedIngredient(Ingredient.of(Items.GRASS),2), new SizedIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .outputEntities(EntityIngredient.of(EntityType.SHEEP.getRegistryName()))
                    .chance(0.5F).outputEntities(EntityIngredient.of(EntityType.SHEEP.getRegistryName()))
                    .chance(1).perTick(true).inputFE(100).duration(120).buildAndRegister();

            RiftwayControllerTE.RIFTWAYRECIPEMAP.start().name("pig")
                    .inputItems(new SizedIngredient(Ingredient.of(Tags.Items.CROPS_CARROT),3), new SizedIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .outputEntities(EntityIngredient.of(EntityType.PIG.getRegistryName()))
                    .chance(1).perTick(true).inputFE(100).duration(120).buildAndRegister();

            //always match slot names on trait with recipe's slotname!
            SiliconeCrucibleTE.SILICONCRUCIBLE_RECIPEMAP.start().name("silicon_ingot").slotName("input").inputItems(new SizedIngredient(Ingredient.of(Tags.Items.GEMS_QUARTZ), 10)).slotName("output").outputItems(new ItemStack(RegisterItems.MONOCRYSTALLINE_SILICONE.get())).perTick(true).slotName("energy").inputFE(1500).duration(430).buildAndRegister();

        }
        catch (Exception e){
            Main.LOGGER.error("Failed to register recipe:"+ Arrays.toString(e.getStackTrace()));
        }
    }

    public static void register(){}

}
