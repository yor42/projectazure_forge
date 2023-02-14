package com.yor42.projectazure.events;

import com.lowdragmc.multiblocked.api.recipe.EntityIngredient;
import com.lowdragmc.multiblocked.api.recipe.ItemsIngredient;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.FluidMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.tac.guns.init.ModItems;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.AdvancedAlloySmelterControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.AmmoPressControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.OriginiumGeneratorControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.RiftwayControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipebuilders.WeightedRecipeBuilder;
import com.yor42.projectazure.gameobject.crafting.recipes.CrushingRecipe;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibCharger;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibPaddle;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.*;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.yor42.projectazure.intermod.curios.CuriosCompat.getCurioItemStack;
import static com.yor42.projectazure.libs.Constants.isCurioLoaded;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Hand.OFF_HAND;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEventHandler {

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntityLiving();
        ItemStack stack = event.getResultStack();
        Item item = stack.getItem();
        if (entity instanceof AbstractEntityCompanion) {
            if (item.getClass() == PotionItem.class) {
                event.setResultStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStart(FMLServerStartedEvent event){
        Main.LOGGER.debug("Starting multiblock recipe registration.");
        try{
            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getRecipeMap().start().name("prime_to_power").input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.ORIGINIUM_PRIME), 1)).perTick(true).output(FEMultiblockCapability.CAP, 2000).duration(1200).buildAndRegister();
            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getRecipeMap().start().name("originite_to_power").input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.ORIGINITE), 1)).perTick(true).output(FEMultiblockCapability.CAP, 4000).duration(400).buildAndRegister();

            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getRecipeMap().start().name("amber_originium_to_power").input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(RegisterItems.AMBER_ORIGINIUM.get()), 1)).perTick(true).output(FEMultiblockCapability.CAP, 8000).duration(500).buildAndRegister();
            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.getRecipeMap().start().name("amber_originium_fuel_to_power").input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(RegisterItems.AMBER_ORIGINIUM_FUEL_ROD.get()), 1)).perTick(true).output(FEMultiblockCapability.CAP, 10000).duration(1000).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("9mm")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER)))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_9.get(), 16)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("12gauge")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 12))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.PAPER), 2))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_10g.get(), 16)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("50bmg")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 3))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 3))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GEMS_DIAMOND), 1))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_50_BMG.get(), 12)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("30winchester")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 8))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 8))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_30_WIN.get(), 10)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("308win")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 3))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 8))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER)))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_308.get(), 24)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("7.62x25")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 4))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_762x39.get(), 30)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("7.62x39")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 4))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_762x39.get(), 30)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("7.62x54")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER), 2))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_762x39.get(), 20)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("45acp")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 16))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER), 1))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_45.get(), 16)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("5.56")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 16))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 24))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_556.get(), 32)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.getRecipeMap().start().name("5.8mm")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_IRON), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER), 1))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_58x42.get(), 40)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("rma70-24")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_RMA7012), 1), new ItemsIngredient(Ingredient.of(ModTags.Items.ORES_ORIROCK), 2))
                    .input(FluidMultiblockCapability.CAP, new FluidStack(RegisterFluids.KETON_SOURCE_REGISTRY.get(), 100))
                    .output(ItemMultiblockCapability.CAP, new ItemsIngredient(new ItemStack(RegisterItems.INGOT_RMA7024.get(),2)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 300).duration(400).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("bronze")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER), 3), new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_TIN), 1))
                    .output(ItemMultiblockCapability.CAP, new ItemsIngredient(new ItemStack(RegisterItems.INGOT_BRONZE.get(),4)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 120).duration(100).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("brass")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER)), new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_ZINC)))
                    .output(ItemMultiblockCapability.CAP, new ItemsIngredient(new ItemStack(RegisterItems.INGOT_BRASS.get(),2)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 120).duration(100).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("originium_vitrified")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.ORIGINIUM_PRIME)), new ItemsIngredient(Ingredient.of(Tags.Items.GEMS_QUARTZ), 2))
                    .output(ItemMultiblockCapability.CAP, new ItemsIngredient(new ItemStack(RegisterItems.VITRIFIED_ORIGINIUM.get(),2)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 200).duration(200).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("originium_amber")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.ORIGINIUM_PRIME)), new ItemsIngredient(Ingredient.of(Tags.Items.GEMS_QUARTZ), 1), new ItemsIngredient(Ingredient.of(Tags.Items.RODS_BLAZE), 1))
                    .output(ItemMultiblockCapability.CAP, new ItemsIngredient(new ItemStack(RegisterItems.AMBER_ORIGINIUM.get(),2)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(500).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("c99")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ItemTags.COALS)))
                    .output(ItemMultiblockCapability.CAP, new ItemsIngredient(new ItemStack(RegisterItems.C99_CARBON.get(),2)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(500).buildAndRegister();

            AdvancedAlloySmelterControllerTE.SMELTRYDefinition.getRecipeMap().start().name("incandescent_alloy")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_MANGANESE)), new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_RMA7012)))
                    .output(ItemMultiblockCapability.CAP, new ItemsIngredient(new ItemStack(RegisterItems.INGOT_INCANDESCENT_ALLOY.get(),2)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(500).buildAndRegister();

            new WeightedRecipeBuilder(RiftwayControllerTE.RIFTWAYRECIPEMAP).name("akn")
                    .inputItems(new ItemsIngredient(Ingredient.of(RegisterItems.ORUNDUM.get()),10), new ItemsIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
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
                    .inputItems(new ItemsIngredient(Ingredient.of(RegisterItems.SAINT_QUARTZ.get()),3), new ItemsIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .addCompanionOutput(registerEntity.MASH.get())
                    .addCompanionOutput(registerEntity.ARTORIA.get())
                    .addCompanionOutput(registerEntity.SCATHATH.get())
                    .addCompanionOutput(registerEntity.SHIKI.get())
                    .perTick(true).inputFE(2500).duration(1200).buildAndRegisterRiftway();

            new WeightedRecipeBuilder(RiftwayControllerTE.RIFTWAYRECIPEMAP).name("etc")
                    .inputItems(new ItemsIngredient(Ingredient.of(RegisterItems.HEADHUNTING_PCB.get()),2), new ItemsIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .addCompanionOutput(registerEntity.SHIROKO.get())
                    .addCompanionOutput(registerEntity.SYLVI.get())
                    .addCompanionOutput(registerEntity.KYARU.get())
                    .addCompanionOutput(registerEntity.EXCELA.get())
                    .perTick(true).inputFE(2500).duration(1200).buildAndRegisterRiftway();

            RiftwayControllerTE.RIFTWAYRECIPEMAP.start().name("cow")
                    .inputItems(new ItemsIngredient(Ingredient.of(Tags.Items.CROPS_WHEAT),3), new ItemsIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .outputEntities(EntityIngredient.of(EntityType.COW.getRegistryName()))
                    .perTick(true).inputFE(100).duration(120).buildAndRegister();

            RiftwayControllerTE.RIFTWAYRECIPEMAP.start().name("chicken")
                    .inputItems(new ItemsIngredient(Ingredient.of(Tags.Items.SEEDS_WHEAT),1), new ItemsIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .outputEntities(EntityIngredient.of(EntityType.CHICKEN.getRegistryName()))
                    .chance(0.75F).outputEntities(EntityIngredient.of(EntityType.CHICKEN.getRegistryName()))
                    .chance(0.3F).outputEntities(EntityIngredient.of(EntityType.CHICKEN.getRegistryName()))
                    .chance(1).perTick(true).inputFE(100).duration(600).buildAndRegister();

            RiftwayControllerTE.RIFTWAYRECIPEMAP.start().name("sheep")
                    .inputItems(new ItemsIngredient(Ingredient.of(Items.GRASS),2), new ItemsIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .outputEntities(EntityIngredient.of(EntityType.SHEEP.getRegistryName()))
                    .chance(0.5F).outputEntities(EntityIngredient.of(EntityType.SHEEP.getRegistryName()))
                    .chance(1).perTick(true).inputFE(100).duration(120).buildAndRegister();

            RiftwayControllerTE.RIFTWAYRECIPEMAP.start().name("pig")
                    .inputItems(new ItemsIngredient(Ingredient.of(Tags.Items.CROPS_CARROT),3), new ItemsIngredient(Ingredient.of(RegisterItems.FOR_DESTABILIZER.get()),1))
                    .outputEntities(EntityIngredient.of(EntityType.PIG.getRegistryName()))
                    .chance(1).perTick(true).inputFE(100).duration(120).buildAndRegister();


        }
        catch (Exception e){
            Main.LOGGER.error("Failed to register recipe:"+ Arrays.toString(e.getStackTrace()));
        }
    }

    @SubscribeEvent
    public static void OnplayerRightClicked(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        List<Entity> passengers = player.getPassengers();
        BlockPos pos = event.getPos();
        if (!passengers.isEmpty() && player.isCrouching() && player.getMainHandItem() == ItemStack.EMPTY) {
            for (Entity entity : passengers) {
                entity.stopRiding();

                if (!(entity instanceof AbstractEntityCompanion)) {
                    return;
                }

                boolean isInjured = ((AbstractEntityCompanion) entity).isCriticallyInjured();
                if (isInjured || world.isNight()) {
                    BlockState state = world.getBlockState(pos);
                    if (state.isBed(world, pos, (LivingEntity) entity)) {
                        pos = state.getBlock() instanceof BedBlock ? state.getValue(BedBlock.PART) == BedPart.HEAD ? pos : pos.relative(state.getValue(BedBlock.FACING)) : pos;
                        ((AbstractEntityCompanion) entity).startSleeping(pos);
                        event.setCanceled(true);
                    }
                    break;
                }
                else if(((AbstractEntityCompanion) entity).isDeadOrDying()){
                    player.displayClientMessage(new TranslationTextComponent("item.tooltip.not_revived"), true);
                }
            }
            player.swing(MAIN_HAND);
        }
    }

    @SubscribeEvent
    public static void OnplayerRightClickedEntity(PlayerInteractEvent.EntityInteract event){
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        Hand OtherHand = hand == MAIN_HAND?Hand.OFF_HAND:MAIN_HAND;
        ItemStack otherHandStack = player.getItemInHand(OtherHand);
        Entity target = event.getTarget();
        World world = event.getWorld();

        if(target instanceof LivingEntity){
            if(stack.getItem() instanceof ItemDefibPaddle && otherHandStack.getItem() instanceof ItemDefibPaddle){
                if(hand == OFF_HAND){
                    event.setCanceled(true);
                    return;
                }
                ItemStack ChargerStack = ItemStack.EMPTY;
                boolean isCurio = false;
                for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                    ItemStack invstack = player.inventory.getItem(i);
                    Item item = invstack.getItem();
                    if (item instanceof ItemDefibCharger && ItemDefibCharger.isOn(invstack) && ItemDefibCharger.getChargeProgress(invstack)==100) {
                        ChargerStack = invstack;
                        break;
                    }
                }

                if(ChargerStack.isEmpty() && isCurioLoaded()){
                    ChargerStack = getCurioItemStack(player, (itemstack)->{
                        Item item = itemstack.getItem();
                        return item instanceof ItemDefibCharger && ItemDefibCharger.isOn(itemstack) && ItemDefibCharger.getChargeProgress(itemstack) == 100;
                    });
                    isCurio = true;
                }

                if(!ChargerStack.isEmpty()){

                    if(!world.isClientSide()){
                        for(ItemStack itemstack : new ItemStack[]{stack, otherHandStack}) {
                            ItemDefibPaddle item = (ItemDefibPaddle) itemstack.getItem();
                            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerWorld) world);
                            final AnimationController controller = GeckoLibUtil.getControllerForID(item.factory, id, ItemDefibPaddle.controllerName);
                            controller.markNeedsReload();
                            controller.setAnimation(new AnimationBuilder().addAnimation("shock"));
                        }
                    }

                    if(PAConfig.CONFIG.FaintTimeLimit.get()>0) {
                        ItemDefibCharger.setChargeProgress(ChargerStack, 0);
                        player.playSound(registerSounds.DEFIB_SHOCK, 0.8F + (0.4F + MathUtil.getRand().nextFloat()), 0.8F + (0.4F + MathUtil.getRand().nextFloat()));
                        if (target instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) target).isDeadOrDying() && ((AbstractEntityCompanion) target).isOwnedBy(player)) {
                            ((AbstractEntityCompanion) target).reviveCompanion();
                            ((AbstractEntityCompanion) target).setOrderedToSit(true);
                        } else {
                            target.hurt(DamageSources.causeDefibDamage(player), 20);
                        }

                        if(isCurio){
                            ItemDefibCharger.setOn(ChargerStack, false);
                        }

                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void loadClientRecipes(RecipesUpdatedEvent event) {
        Main.CRUSHING_REGISTRY.clearRecipes();
        loadcrushingRecipes(event.getRecipeManager());
    }

    @SubscribeEvent
    public static void onServerStart(FMLServerStartingEvent event) {
        if (event.getServer().isDedicatedServer()) {
            loadcrushingRecipes(event.getServer().getRecipeManager());
        }
    }

    private static <R extends IRecipe<?>> List<R> filterRecipes(Collection<IRecipe<?>> recipes, Class<R> recipeClass, IRecipeType<R> recipeType) {
        return recipes.stream()
                .filter(iRecipe -> iRecipe.getType() == recipeType)
                .map(recipeClass::cast)
                .collect(Collectors.toList());
    }

    private static void loadcrushingRecipes(RecipeManager manager) {
        Collection<IRecipe<?>> recipes = manager.getRecipes();
        if (recipes.isEmpty()) {
            return;
        }
        Main.CRUSHING_REGISTRY.setRecipes(filterRecipes(recipes, CrushingRecipe.class, registerRecipes.Types.CRUSHING));
    }
}
