package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.client.renderer.items.ItemDefibChargerRenderer;
import com.yor42.projectazure.gameobject.capability.ItemPowerCapabilityProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
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

import static com.yor42.projectazure.Main.PA_WEAPONS;
import static com.yor42.projectazure.setup.register.registerSounds.*;

public class ItemDefibCharger extends Item implements IAnimatable, ISyncable {
    private static final int ANIM_ON = 0;
    private static final int ANIM_OFF = 1;
    public static final String controllerName = "defibcharger_controller";
    public AnimationFactory factory = new AnimationFactory(this);
    public ItemDefibCharger() {
        super(new Item.Properties().tab(PA_WEAPONS).stacksTo(1).setISTER(()->ItemDefibChargerRenderer::new));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemPowerCapabilityProvider(stack, 40000);
    }
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {

        ItemStack stack= player.getItemInHand(hand);
        if(stack.getItem() instanceof ItemDefibCharger){
            if(player.isCrouching()){
                boolean nextstate = !isOn(stack);

                if(nextstate && stack.getCapability(CapabilityEnergy.ENERGY).map((e)-> e.extractEnergy(1, true)<=0).orElse(true)){
                    player.playSound(DEFIB_NOBATTERY, 1.0F, 1.0F);
                    return ActionResult.fail(stack);
                }
                if(!world.isClientSide()){
                    final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) world);

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
                return ActionResult.pass(stack);
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        super.inventoryTick(stack, world, p_77663_3_, p_77663_4_, p_77663_5_);
        if(isOn(stack)){
            int chargeprogress = getChargeProgress(stack);
            if(ShouldCharging(stack)){
                if(stack.getCapability(CapabilityEnergy.ENERGY).map((e)->e.extractEnergy(100, true)>=100).orElse(false)) {
                    int charge = Math.min(100, chargeprogress + 1);
                    if (charge >= 100) {
                        setCharging(stack, false);
                        p_77663_3_.playSound(DEFIB_READY, 1.0F, 1.0F);
                    }
                    setChargeProgress(stack, charge);
                    stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> e.extractEnergy(100, false));
                }else{
                    int charge = Math.max(0, getChargeProgress(stack)-10);
                    if (charge <= 0) {
                        setCharging(stack, false);
                        if(p_77663_3_ instanceof PlayerEntity) {
                            PlayerEntity player = (PlayerEntity) p_77663_3_;
                            player.displayClientMessage(new TranslationTextComponent("item.tooltip.chargefailed_nobattery").withStyle(TextFormatting.RED), true);
                        }
                    }
                    setChargeProgress(stack, charge);
                }

            }
            else {
                if(p_77663_3_.tickCount%5 == 0) {
                    if(stack.getCapability(CapabilityEnergy.ENERGY).map((e) -> e.extractEnergy(1, true)==1).orElse(false)){
                        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> e.extractEnergy(1, false));
                    }
                    else{
                        setChargeProgress(stack, 0);
                        setCharging(stack, false);
                        if(!world.isClientSide()){
                            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) world);
                            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> p_77663_3_);
                            GeckoLibNetwork.syncAnimation(target, this, id, ANIM_OFF);
                        }
                        p_77663_3_.playSound(DEFIB_POWEROFF, 1.0F, 1.0F);
                        setOn(stack, false);
                    }
                }
            }
        }
        if(p_77663_3_ instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) p_77663_3_;
            if (ShouldCharging(stack)) {
                int charge = (int) (((float) getChargeProgress(stack) / 100) * 100);
                player.displayClientMessage(new TranslationTextComponent("item.tooltip.chargeprogress", charge + "%").withStyle(TextFormatting.AQUA), true);
            } else if (getChargeProgress(stack) == 100) {
                player.displayClientMessage(new TranslationTextComponent("item.tooltip.ready").withStyle(TextFormatting.GREEN), true);
            }
        }

        if(!world.isClientSide()) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) world);
            final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            if (controller.getAnimationState() == AnimationState.Stopped) {
                controller.markNeedsReload();
                String animationname = isOn(stack) ? "on_still" : "off_still";
                controller.setAnimation(new AnimationBuilder().addAnimation(animationname));
            }
        }
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        super.onArmorTick(stack, world, player);
        if(isOn(stack)){
            int chargeprogress = getChargeProgress(stack);
            if(ShouldCharging(stack)){
                if(stack.getCapability(CapabilityEnergy.ENERGY).map((e)->e.extractEnergy(100, true)>=100).orElse(false)) {
                    int charge = Math.min(100, chargeprogress + 1);
                    if (charge >= 100) {
                        setCharging(stack, false);
                        player.playSound(DEFIB_READY, 1.0F, 1.0F);
                    }
                    setChargeProgress(stack, charge);
                    stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> e.extractEnergy(100, false));
                }else{
                    int charge = Math.max(0, getChargeProgress(stack)-10);
                    if (charge <= 0) {
                        setCharging(stack, false);
                    }
                    setChargeProgress(stack, charge);
                }

            }
            else {
                if(player.tickCount%5 == 0) {
                    if(stack.getCapability(CapabilityEnergy.ENERGY).map((e) -> e.extractEnergy(1, true)==1).orElse(false)){
                        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> e.extractEnergy(1, false));
                    }
                    else{
                        setChargeProgress(stack, 0);
                        setCharging(stack, false);
                        if(!world.isClientSide()){
                            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) world);
                            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);
                            GeckoLibNetwork.syncAnimation(target, this, id, ANIM_OFF);
                        }
                        player.playSound(DEFIB_POWEROFF, 1.0F, 1.0F);
                        setOn(stack, false);
                    }
                }
            }
        }

        if(ShouldCharging(stack)){
            int charge = (int) (((float)getChargeProgress(stack)/100)*100);
            player.displayClientMessage(new TranslationTextComponent("item.tooltip.chargeprogress", charge+"%").withStyle(TextFormatting.AQUA), true);
        }
        else if(getChargeProgress(stack) == 100){
            player.displayClientMessage(new TranslationTextComponent("item.tooltip.ready").withStyle(TextFormatting.GREEN), true);
        }

        if(!world.isClientSide()) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) world);
            final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            if (controller.getAnimationState() == AnimationState.Stopped) {
                controller.markNeedsReload();
                String animationname = isOn(stack) ? "on_still" : "off_still";
                controller.setAnimation(new AnimationBuilder().addAnimation(animationname));
            }
        }
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
    public void onAnimationSync(int id, int state) {
        if (state == ANIM_ON) {
            // Always use GeckoLibUtil to get AnimationControllers when you don't have
            // access to an AnimationEvent
            final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            controller.setAnimation(new AnimationBuilder().addAnimation("on").addAnimation("on_still"));
        }
        else if(state == ANIM_OFF){
            final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            controller.setAnimation(new AnimationBuilder().addAnimation("off").addAnimation("off_still"));
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World worldin, @Nonnull List<ITextComponent> tooltips, @Nonnull ITooltipFlag tooltipadvanced) {
        tooltips.add(new TranslationTextComponent(this.getDescriptionId()+".tooltip").withStyle(TextFormatting.GRAY));
        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((cap)->tooltips.add(new TranslationTextComponent("item.tooltip.energystored", cap.getEnergyStored()+"/"+cap.getMaxEnergyStored()).withStyle(TextFormatting.GRAY)));
        if(ShouldCharging(stack)){
            int charge = (int) (((float)getChargeProgress(stack)/100)*100);
            tooltips.add(new TranslationTextComponent("item.tooltip.energystored", charge+"%").withStyle(TextFormatting.AQUA));
        }
        else if(getChargeProgress(stack) == 100){
            tooltips.add(new TranslationTextComponent("item.tooltip.ready").withStyle(TextFormatting.GREEN));
        }
        super.appendHoverText(stack, worldin, tooltips, tooltipadvanced);
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        return super.getShareTag(stack);
    }
}
