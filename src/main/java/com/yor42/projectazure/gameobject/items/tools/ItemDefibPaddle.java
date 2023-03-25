package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.client.renderer.items.ItemDefibPaddleRenderer;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.CapabilityEnergy;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
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
import static com.yor42.projectazure.intermod.curios.CuriosCompat.getCurioItemStack;
import static com.yor42.projectazure.libs.utils.CompatibilityUtils.isCurioLoaded;
import static net.minecraft.util.Hand.MAIN_HAND;

import net.minecraft.world.item.Item.Properties;

public class ItemDefibPaddle extends Item implements IAnimatable, ISyncable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String controllerName = "paddle_controller";

    public ItemDefibPaddle() {
        super(new Properties().tab(PA_WEAPONS).setISTER(()->ItemDefibPaddleRenderer::new).stacksTo(1));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack p_111207_1_, @Nonnull Player p_111207_2_, @Nonnull LivingEntity p_111207_3_, @Nonnull InteractionHand p_111207_4_) {
        return super.interactLivingEntity(p_111207_1_, p_111207_2_, p_111207_3_, p_111207_4_);
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, controllerName, 1, this::predicate));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player entity, @Nonnull InteractionHand hand) {
        InteractionHand oppositehand = hand == MAIN_HAND? InteractionHand.OFF_HAND: MAIN_HAND;
        if(entity.getItemInHand(oppositehand).getItem() instanceof ItemDefibPaddle) {
            if (entity.isCrouching()) {
                ItemStack ChargerStack = ItemStack.EMPTY;
                for (int i = 0; i < entity.inventory.getContainerSize(); i++) {
                    ItemStack stack = entity.inventory.getItem(i);
                    Item item = stack.getItem();
                    if (item instanceof ItemDefibCharger && ItemDefibCharger.isOn(stack) && ItemDefibCharger.getChargeProgress(stack)<100) {
                        if (stack.getCapability(CapabilityEnergy.ENERGY).map((e) -> e.extractEnergy(100, true) == 100).orElse(false)) {
                            ChargerStack = stack;
                            break;
                        }
                    }
                }
                if(ChargerStack.isEmpty() && isCurioLoaded()){
                    ChargerStack = getCurioItemStack(entity, (stack)->{
                        Item item = stack.getItem();
                        if (item instanceof ItemDefibCharger&& ItemDefibCharger.getChargeProgress(stack)<100) {
                            return stack.getCapability(CapabilityEnergy.ENERGY).map((e) -> e.extractEnergy(100, true) == 100).orElse(false);
                        }
                        return false;
                    });

                    if(!ItemDefibCharger.isOn(ChargerStack)){
                        ItemDefibCharger.setOn(ChargerStack, true);
                    }
                }

                if(!ChargerStack.isEmpty()) {
                    if (!ItemDefibCharger.ShouldCharging(ChargerStack)) {
                        ItemDefibCharger.setCharging(ChargerStack, true);
                        entity.playSound(registerSounds.DEFIB_CHARGING, 1.0f, 1.0f);
                    }
                }else{
                    entity.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".tooltip.nocharger").withStyle(ChatFormatting.RED), true);
                }
            }
        }

        return super.use(world, entity, hand);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack p_77624_1_, @Nullable Level p_77624_2_, @Nonnull List<Component> tooltips, TooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltips, p_77624_4_);
        tooltips.add(new TranslatableComponent(this.getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {

    }
}
