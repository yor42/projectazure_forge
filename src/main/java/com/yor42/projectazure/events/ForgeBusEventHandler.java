package com.yor42.projectazure.events;

import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.recipe.ingredient.EntityIngredient;
import com.lowdragmc.multiblocked.api.recipe.ingredient.SizedIngredient;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.FluidMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.tac.guns.init.ModItems;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipebuilders.WeightedRecipeBuilder;
import com.yor42.projectazure.gameobject.crafting.recipes.CrushingRecipe;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.GasMaskItem;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibCharger;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibPaddle;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IMixinPlayerEntity;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.yor42.projectazure.intermod.curios.CuriosCompat.getCurioItemStack;
import static com.yor42.projectazure.libs.utils.CompatibilityUtils.isCurioLoaded;
import static net.minecraft.world.InteractionHand.MAIN_HAND;
import static net.minecraft.world.InteractionHand.OFF_HAND;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEventHandler {

    @SubscribeEvent
    public static void onItemUseFinish(@Nonnull LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntityLiving();
        ItemStack stack = event.getResultStack();
        Item item = stack.getItem();
        if (!(entity instanceof AbstractEntityCompanion)) {
            return;
        }

        if (item instanceof PotionItem) {
            event.setResultStack(new ItemStack(Items.GLASS_BOTTLE));
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(@Nonnull LivingHurtEvent event){
        LivingEntity entity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();

        ItemStack headstack = entity.getItemBySlot(EquipmentSlot.HEAD);
        Item headItem = headstack.getItem();

        if(headItem instanceof GasMaskItem){
            CompoundTag compoundNBT = headstack.getOrCreateTag();
            ListTag filters = compoundNBT.getList("filters", Tag.TAG_COMPOUND);
            for(int i = 0; i<filters.size(); i++){
                ItemStack filterstack = ItemStack.of(filters.getCompound(i));
                if(ItemStackUtils.isDestroyed(filterstack)){
                    continue;
                }

                if(damageSource == DamageSource.DRAGON_BREATH || damageSource.isMagic()){
                    event.setCanceled(true);
                }
                return;
            }
        }

    }

    @SubscribeEvent
    public static void onEXPEvent(@Nonnull PlayerXpEvent.XpChange event){
        Player player1 = event.getPlayer();

        if(player1.getLevel().isClientSide()){
            return;
        }

        ServerPlayer player = (ServerPlayer) player1;
        IMixinPlayerEntity mixinplayer = ((IMixinPlayerEntity)player);

        CompoundTag entityonBack = mixinplayer.getEntityonBack();
        if(entityonBack.isEmpty()){
            return;
        }

        float exp = entityonBack.getFloat("exp");
        int level = entityonBack.getInt("level");
        int maxexp = 10+(5*level);
        int maxlevel = 50;
        float deltaExp = event.getAmount()/2F;

        float newExp = exp+deltaExp;
        int newLevel = level;
        if(exp+deltaExp >= maxexp) {
            player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 0.5F);
            while (newExp > maxexp) {

                if (newLevel >= maxlevel) {
                    newLevel = maxlevel;
                    newExp = maxlevel;
                } else {
                    newLevel++;
                    newExp -= maxexp;
                }
            }
        }

        entityonBack.putFloat("exp", newExp);
        entityonBack.putInt("level", newLevel);

        event.setAmount((int) (event.getAmount()-deltaExp));
    }

    @SubscribeEvent
    public static void onPotionApplicapable(@Nonnull PotionEvent.PotionApplicableEvent event){

        LivingEntity entity = event.getEntityLiving();

        ItemStack headstack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if(headstack.isEmpty() && CompatibilityUtils.isCurioLoaded()){
            headstack = getCurioItemStack(entity, (stack-> stack.getItem() == RegisterItems.GASMASK.get()));
        }

        Item headItem = headstack.getItem();

        if(headItem == RegisterItems.GASMASK.get()){
            CompoundTag compoundNBT = headstack.getOrCreateTag();
            ListTag filters = compoundNBT.getList("filters", Tag.TAG_COMPOUND);
            for(int i = 0; i<filters.size(); i++){
                ItemStack filterstack = ItemStack.of(filters.getCompound(i));
                if(ItemStackUtils.isDestroyed(filterstack)){
                    continue;
                }
                event.setResult(Event.Result.DENY);
                return;
            }
        }


        event.setResult(Event.Result.DEFAULT);

    }

    private static void registerRecipeFromJSON(ResourceLocation location) throws IOException {
        InputStream stream = Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        RecipeMap map = GsonHelper.fromJson(Multiblocked.GSON, reader, RecipeMap.class);
        if (map != null) {
            RecipeMap.register(map);
        }
    }

    @SubscribeEvent
    public static void OnplayerRightClicked(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getWorld();
        Player player = event.getPlayer();
        if (player.isCrouching() && player.getMainHandItem() == ItemStack.EMPTY) {

            ((IMixinPlayerEntity) player).removeEntityOnBack().ifPresent((entity) -> {
                if (!(entity instanceof AbstractEntityCompanion)) {
                    return;
                }
                BlockPos pos = event.getPos();
                boolean isInjured = ((AbstractEntityCompanion) entity).isCriticallyInjured();
                if (isInjured || world.isNight()) {
                    BlockState state = world.getBlockState(pos);
                    if (state.isBed(world, pos, (LivingEntity) entity)) {
                        pos = state.getBlock() instanceof BedBlock ? state.getValue(BedBlock.PART) == BedPart.HEAD ? pos : pos.relative(state.getValue(BedBlock.FACING)) : pos;
                        ((AbstractEntityCompanion) entity).startSleeping(pos);
                        event.setCanceled(true);
                    }
                } else if (((AbstractEntityCompanion) entity).isDeadOrDying()) {
                    player.displayClientMessage(new TranslatableComponent("item.tooltip.not_revived"), true);
                }
                player.swing(MAIN_HAND);
            });
        }
    }

    @SubscribeEvent
    public static void OnplayerRightClickedEntity(PlayerInteractEvent.EntityInteract event){
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        InteractionHand OtherHand = hand == MAIN_HAND? OFF_HAND:MAIN_HAND;
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
                boolean isCurio = false;
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack invstack = player.getInventory().getItem(i);
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
                            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerLevel) world);
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
    public static void onServerStart(ServerStartingEvent event) {
        if (event.getServer().isDedicatedServer()) {
            loadcrushingRecipes(event.getServer().getRecipeManager());
        }
    }

    private static <R extends Recipe<?>> List<R> filterRecipes(Collection<Recipe<?>> recipes, Class<R> recipeClass, RecipeType<R> recipeType) {
        return recipes.stream()
                .filter(iRecipe -> iRecipe.getType() == recipeType)
                .map(recipeClass::cast)
                .collect(Collectors.toList());
    }

    private static void loadcrushingRecipes(RecipeManager manager) {
        Collection<Recipe<?>> recipes = manager.getRecipes();
        if (recipes.isEmpty()) {
            return;
        }
        Main.CRUSHING_REGISTRY.setRecipes(filterRecipes(recipes, CrushingRecipe.class, registerRecipes.Types.CRUSHING.get()));
    }
}
