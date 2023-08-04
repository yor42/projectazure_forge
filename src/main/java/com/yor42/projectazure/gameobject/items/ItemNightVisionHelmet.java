package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.interfaces.IActivatable;
import com.yor42.projectazure.interfaces.ICurioItem;
import com.yor42.projectazure.interfaces.IHelmetOverlay;
import com.yor42.projectazure.interfaces.IShaderEquipment;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

import static com.yor42.projectazure.Main.PA_RESOURCES;
import static com.yor42.projectazure.Main.PA_WEAPONS;
import static com.yor42.projectazure.gameobject.items.materials.ModArmorMaterials.ArmorModMaterials.NIGHTVISION;
import static com.yor42.projectazure.libs.utils.ClientUtils.NVGOVERLAY;

public class ItemNightVisionHelmet extends GeoArmorItem implements IAnimatable, ICurioItem, IShaderEquipment, IHelmetOverlay {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final int batterysize = 50000;
    public static final String controllerName = "nightvision_controller";

    public ItemNightVisionHelmet() {
        super(NIGHTVISION, EquipmentSlot.HEAD, new Item.Properties().tab(PA_WEAPONS).stacksTo(1));
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, controllerName, 10, this::predicate));
    }

    private PlayState predicate(AnimationEvent<ItemNightVisionHelmet> itemNightVisionAnimationEvent) {
        ItemStack stack = itemNightVisionAnimationEvent.getExtraDataOfType(ItemStack.class).get(0);
        AnimationController<ItemNightVisionHelmet> controller = itemNightVisionAnimationEvent.getController();
        if (ItemStackUtils.isOn(stack)){
            controller.setAnimation(new AnimationBuilder().addAnimation("on", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else{
            controller.setAnimation(new AnimationBuilder().addAnimation("off", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
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

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return CuriosCompat.addEnergyCapability(stack, this.batterysize);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {

        ItemStack stack = pPlayer.getItemInHand(pHand);
        if(pPlayer.isCrouching()){
            ItemStackUtils.TogglePower(stack);
            return InteractionResultHolder.success(stack);
        }

        return super.use(pLevel, pPlayer, pHand);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        this.handleCharge(pStack);
    }

    public void handleCharge(ItemStack stack){
        if(ItemStackUtils.isOn(stack)) {
            if (stack.getCapability(CapabilityEnergy.ENERGY).map((e) -> e.extractEnergy(1, true) < 1).orElse(true)) {
                ItemStackUtils.setOn(stack, false);
            }
            else{
                stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> e.extractEnergy(1, false));
            }
        }
        /*

        if(!world.isClientSide()) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) world);
            final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            if (controller.getAnimationState() == AnimationState.Stopped) {
                controller.markNeedsReload();
                String animationname = ItemStackUtils.isOn(stack) ? "on_still" : "off_still";
                controller.setAnimation(new AnimationBuilder().addAnimation(animationname));
            }
        }

         */
    }

    @Override
    public ResourceLocation shaderLocation(ItemStack stack) {
        return ResourceUtils.ModResourceLocation("shaders/post/nightvision.json");
    }

    @Override
    public boolean shouldDisplayShader(ItemStack stack) {
        return ItemStackUtils.isOn(stack);
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

        if(ItemStackUtils.isOn(stack)){
            tooltips.add(new TranslatableComponent("item.tooltip.on").withStyle(ChatFormatting.AQUA));
        }
        else{
            tooltips.add(new TranslatableComponent("item.tooltip.off").withStyle(ChatFormatting.DARK_RED));
        }
        super.appendHoverText(stack, worldin, tooltips, tooltipadvanced);
    }

    @Override
    public ResourceLocation getOverlayTexture(ItemStack stack, LocalPlayer player) {
        if(ItemStackUtils.isOn(stack)) {
            return NVGOVERLAY;
        }
        else return null;
    }
}
