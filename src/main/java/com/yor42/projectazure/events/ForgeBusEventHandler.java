package com.yor42.projectazure.events;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.crafting.CrushingRecipe;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibCharger;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibPaddle;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerRecipes;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

    @SubscribeEvent
    public static void OnplayerRightClicked(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getWorld();
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
                        player.displayClientMessage(new TranslatableComponent("item.tooltip.not_revived"), true);
                    }
                }
            }
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
        Level world = event.getWorld();

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
