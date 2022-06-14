package com.yor42.projectazure.events;

import com.lowdragmc.multiblocked.api.recipe.ItemsIngredient;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.tac.guns.init.ModItems;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.AmmoPressControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.OriginiumGeneratorControllerTE;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.crafting.CrushingRecipe;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibCharger;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibPaddle;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerRecipes;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Hand.OFF_HAND;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEventHandler {


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.phase == TickEvent.Phase.START) {

            ProjectAzurePlayerCapability cap = ProjectAzurePlayerCapability.getCapability(event.player);

            if (cap.getOffHandFireDelay() > 0) {
                cap.setOffHandFireDelay(cap.getOffHandFireDelay() - 1);
            }
            if (cap.getMainHandFireDelay() > 0) {
                cap.setMainHandFireDelay(cap.getMainHandFireDelay() - 1);
            }
        }
    }

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
            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.recipeMap.start().name("prime_to_power").input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.ORIGINIUM_PRIME), 1)).perTick(true).output(FEMultiblockCapability.CAP, 2000).duration(1200).buildAndRegister();
            OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.recipeMap.start().name("originite_to_power").input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.ORIGINITE), 1)).perTick(true).output(FEMultiblockCapability.CAP, 4000).duration(400).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("9mm")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER)))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_9.get(), 16)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("12gauge")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 12))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.PAPER), 2))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_10g.get(), 16)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("357magnum")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GEMS_DIAMOND), 1))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.MAGNUM_BULLET.get(), 12)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("50bmg")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 3))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 3))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GEMS_DIAMOND), 1))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.MAGNUM_BULLET.get(), 12)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("30winchester")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 8))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 8))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_30_WIN.get(), 10)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("308win")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 3))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 8))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER)))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_308.get(), 24)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("7.62x25")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 4))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_762x39.get(), 30)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("7.62x39")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 4))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_762x39.get(), 30)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("7.62x54")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_BRASS), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER), 2))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_762x39.get(), 20)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("45acp")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 16))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER), 1))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_45.get(), 16)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("5.56")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_BRASS), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER)))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_LEAD), 16))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.NUGGET_COPPER), 24))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_556.get(), 32)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

            AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("5.8mm")
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.PLATE_IRON), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Tags.Items.GUNPOWDER), 2))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_LEAD), 1))
                    .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(ModTags.Items.INGOT_COPPER), 1))
                    .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_58x42.get(), 40)))
                    .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();
        }
        catch (Exception e){
            Main.LOGGER.error("Failed to register recipe:"+e.getMessage());
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
                if (entity instanceof AbstractEntityCompanion) {
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
                for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                    ItemStack invstack = player.inventory.getItem(i);
                    Item item = invstack.getItem();
                    if (item instanceof ItemDefibCharger && ItemDefibCharger.isOn(invstack) && ItemDefibCharger.getChargeProgress(invstack)==100) {
                        ChargerStack = invstack;
                        break;
                    }

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

                    ItemDefibCharger.setChargeProgress(ChargerStack, 0);
                    player.playSound(registerSounds.DEFIB_SHOCK, 0.8F+(0.4F+ MathUtil.getRand().nextFloat()), 0.8F+(0.4F+MathUtil.getRand().nextFloat()));
                    if(target instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) target).isDeadOrDying() && ((AbstractEntityCompanion) target).isOwnedBy(player)){
                        ((AbstractEntityCompanion) target).reviveCompanion();
                        ((AbstractEntityCompanion) target).setOrderedToSit(true);
                    }
                    else {
                        target.hurt(DamageSources.causeDefibDamage(player), 20);
                    }
                    event.setCanceled(true);
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
