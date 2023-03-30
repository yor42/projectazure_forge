package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.client.renderer.items.ItemDefibChargerRenderer;
import com.yor42.projectazure.gameobject.items.ICurioItem;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.yor42.projectazure.Main.PA_WEAPONS;
import static com.yor42.projectazure.setup.register.registerSounds.*;

public class ItemDefibCharger extends Item implements IAnimatable, ISyncable, ICurioItem {
    private static final int ANIM_ON = 0;
    private static final int ANIM_OFF = 1;
    private final int batterysize = 40000;
    public static final String controllerName = "defibcharger_controller";
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public ItemDefibCharger() {
        super(new Item.Properties().tab(PA_WEAPONS).stacksTo(1));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new ItemDefibChargerRenderer();
            }
        });
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return CuriosCompat.addEnergyCapability(stack, this.batterysize);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {

        ItemStack stack= player.getItemInHand(hand);
        if(stack.getItem() instanceof ItemDefibCharger){
            if(player.isCrouching()){
                boolean nextstate = !isOn(stack);

                if(nextstate && stack.getCapability(CapabilityEnergy.ENERGY).map((e)-> e.extractEnergy(1, true)<=0).orElse(true)){
                    player.playSound(DEFIB_NOBATTERY, 1.0F, 1.0F);
                    return InteractionResultHolder.fail(stack);
                }

                if(!world.isClientSide()){
                    final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) world);

                    int state = nextstate?ANIM_ON:ANIM_OFF;

                    final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);
                    GeckoLibNetwork.syncAnimation(target, this, id, state);
                }

                if(!nextstate){
                    setChargeProgress(stack, 0);
                    setCharging(stack, false);
                }

                player.playSound(nextstate?DEFIB_POWERON:DEFIB_POWEROFF, 1.0F, 1.0F);
                setOn(stack, nextstate);
                return InteractionResultHolder.pass(stack);
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        super.inventoryTick(stack, world, p_77663_3_, p_77663_4_, p_77663_5_);
        this.handleCharge(stack, p_77663_3_, world);
    }

    public void handleCharge(ItemStack stack, Entity entity, Level world){
        if(isOn(stack)){
            int chargeprogress = getChargeProgress(stack);
            if(ShouldCharging(stack)){
                if(stack.getCapability(CapabilityEnergy.ENERGY).map((e)->e.extractEnergy(100, true)>=100).orElse(false)) {
                    int charge = Math.min(100, chargeprogress + 1);
                    if (charge == 100) {
                        setCharging(stack, false);
                        entity.playSound(DEFIB_READY, 1.0F, 1.0F);
                    }
                    setChargeProgress(stack, charge);
                    stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> e.extractEnergy(100, false));
                }else{
                    int charge = Math.max(0, getChargeProgress(stack)-4);
                    if (charge == 0) {
                        setCharging(stack, false);
                        if(entity instanceof Player) {
                            Player player = (Player) entity;
                            player.displayClientMessage(new TranslatableComponent("item.tooltip.chargefailed_nobattery").withStyle(ChatFormatting.RED), true);
                        }
                    }
                    setChargeProgress(stack, charge);
                }

            }
            else {
                if(entity.tickCount%5 == 0) {
                    if(stack.getCapability(CapabilityEnergy.ENERGY).map((e) -> e.extractEnergy(1, true)==1).orElse(false)){
                        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> e.extractEnergy(1, false));
                    }
                    else{
                        setChargeProgress(stack, 0);
                        setCharging(stack, false);
                        if(!world.isClientSide()){
                            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) world);
                            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity);
                            GeckoLibNetwork.syncAnimation(target, this, id, ANIM_OFF);
                        }
                        entity.playSound(DEFIB_POWEROFF, 1.0F, 1.0F);
                        setOn(stack, false);
                    }
                }
            }
        }
        if(entity instanceof Player) {
            Player player = (Player) entity;
            if (ShouldCharging(stack)) {
                int charge = (int) (((float) getChargeProgress(stack) / 100) * 100);
                player.displayClientMessage(new TranslatableComponent("item.tooltip.chargeprogress", charge + "%").withStyle(ChatFormatting.AQUA), true);
            } else if (getChargeProgress(stack) == 100) {
                player.displayClientMessage(new TranslatableComponent("item.tooltip.ready").withStyle(ChatFormatting.GREEN), true);
            }
        }

        if(!world.isClientSide()) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) world);
            final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            if (controller.getAnimationState() == AnimationState.Stopped) {
                controller.markNeedsReload();
                String animationname = isOn(stack) ? "on_still" : "off_still";
                controller.setAnimation(new AnimationBuilder().addAnimation(animationname));
            }
        }
    }

    @Override
    public void curioTick(LivingEntity entity, int index, ItemStack stack) {
        this.handleCharge(stack, entity, entity.getLevel());
    }

    public static boolean isOn(@Nonnull ItemStack stack){
        return stack.getOrCreateTag().getBoolean("isOn");
    }

    public static void setOn(@Nonnull ItemStack stack, boolean value){
        stack.getOrCreateTag().putBoolean("isOn", value);
    }

    public static int getChargeProgress(@Nonnull ItemStack stack){
        return stack.getOrCreateTag().getInt("chargeProgress");
    }

    public static void setChargeProgress(@Nonnull ItemStack stack, int value){
        stack.getOrCreateTag().putInt("chargeProgress", value);
    }

    public static boolean ShouldCharging(@Nonnull ItemStack stack){
        return stack.getOrCreateTag().getBoolean("charging");
    }

    public static void setCharging(@Nonnull ItemStack stack, boolean value){
        stack.getOrCreateTag().putBoolean("charging", value);
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, controllerName, 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (this.allowdedIn(p_150895_1_)) {
            ItemStack stack = new ItemStack(this);
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((battery)->battery.receiveEnergy(battery.getMaxEnergyStored(), false));
            p_150895_2_.add(stack);
            p_150895_2_.add(new ItemStack(this));
        }
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        controller.markNeedsReload();
        if (state == ANIM_ON) {
            // Always use GeckoLibUtil to get AnimationControllers when you don't have
            // access to an AnimationEvent
            controller.setAnimation(new AnimationBuilder().addAnimation("on", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("on_still"));
        }
        else if(state == ANIM_OFF){
            controller.setAnimation(new AnimationBuilder().addAnimation("off", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("off_still"));
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldin, @Nonnull List<Component> tooltips, @Nonnull TooltipFlag tooltipadvanced) {
        tooltips.add(new TranslatableComponent(this.getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));

        int remainingBattery = stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
        float percentage = (float) remainingBattery/this.batterysize;

        ChatFormatting color;
        if(percentage>=0.6){
            color = ChatFormatting.GREEN;
        }
        else if(percentage>=0.3){
            color = ChatFormatting.YELLOW;
        }
        else{
            color = ChatFormatting.DARK_RED;
        }
        percentage*=100;
        tooltips.add(new TranslatableComponent("item.tooltip.energystored", new TextComponent(MathUtil.formatValueMatric(remainingBattery)+" FE / "+MathUtil.formatValueMatric(this.batterysize)+String.format(" FE (%.2f", percentage)+"%)").withStyle(color)));

        if(ShouldCharging(stack)){
            int charge = (int) (((float)getChargeProgress(stack)/100)*100);
            tooltips.add(new TranslatableComponent("item.tooltip.energystored", charge+"%").withStyle(ChatFormatting.AQUA));
        }
        else if(getChargeProgress(stack) == 100){
            tooltips.add(new TranslatableComponent("item.tooltip.ready").withStyle(ChatFormatting.GREEN));
        }
        super.appendHoverText(stack, worldin, tooltips, tooltipadvanced);
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        return super.getShareTag(stack);
    }

}
