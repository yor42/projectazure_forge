package com.yor42.projectazure.gameobject.items.rigging;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.gameobject.capability.RiggingItemCapabilityProvider;
import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvEquipmentHandlerItemStack;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvStackHandlerItemStack;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColor;

public abstract class ItemRiggingBase extends ItemDestroyable implements IAnimatable, IGeoRenderer {

    public AnimationFactory factory = new AnimationFactory(this);

    protected enums.shipClass validclass;

    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return null;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    public ItemRiggingBase(Properties properties, int HP) {
        super(properties, HP);
        this.addHardPoints();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (worldIn == null) return; // thanks JEI very cool

        IFluidHandlerItem tank = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElseThrow(() -> new RuntimeException("Can't get the fuel tank of non rigging item!"));
        int fluidAmount = tank.getFluidInTank(0).getAmount();
        int fluidCapacity = tank.getTankCapacity(0);
        float fillRatio = (float) fluidAmount / fluidCapacity;
        ChatFormatting color;
        if (fillRatio < 0.3F) {
            color = ChatFormatting.RED;
        } else if (fillRatio < 0.6F) {
            color = ChatFormatting.YELLOW;
        } else {
            color = ChatFormatting.GREEN;
        }
        tooltip.add(new TextComponent("HP: " + getCurrentHP(stack) + "/" + this.getMaxHP()).setStyle(Style.EMPTY.withColor(getHPColor(stack))));
        tooltip.add(new TranslatableComponent("item.tooltip.remainingfuel").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(fluidAmount + "/" + fluidCapacity).append("mb").withStyle(color)));
        tooltip.add(new TranslatableComponent("rigging_valid_on.tooltip").append(" ").append(new TranslatableComponent(this.validclass.getName())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(8900331))));
        if (worldIn.isClientSide) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, MultiInvUtil.getCap(stack), worldIn, tooltip, flagIn));
        }

        if (flagIn.isAdvanced()) {
            tooltip.add(new TextComponent(stack.getOrCreateTag().toString()));
        }
    }

    public void addHardPoints(){

    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, IMultiInventory inventories, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        TextColor CategoryColor = TextColor.parseColor("#6bb82d");
        for(int i = 0; i< inventories.getInventoryCount(); i++){
            IItemHandler Equipments = inventories.getInventory(i);
            enums.SLOTTYPE slottype = enums.SLOTTYPE.values()[i];
            //not really needed but its here to make mc to not add header of equipment that isnt supported by rigging
            if(Equipments.getSlots()>0) {
                tooltip.add((new TextComponent("===").append(new TranslatableComponent(slottype.getName()).append(new TextComponent("==="))).setStyle(Style.EMPTY.withColor(CategoryColor))));
                for (int j = 0; j < Equipments.getSlots(); j++) {
                    ItemStack currentstack = Equipments.getStackInSlot(j);
                    if (currentstack.getItem() instanceof ItemEquipmentBase)
                        tooltip.add(currentstack.getHoverName().plainCopy().append(" (" + getCurrentHP(currentstack) + "/" + ((ItemEquipmentBase) currentstack.getItem()).getMaxHP() + ")").setStyle(Style.EMPTY.withColor(getHPColor(currentstack))));
                    else {
                        tooltip.add((new TextComponent("-").append(new TranslatableComponent("equiment.empty")).append("-")).setStyle(Style.EMPTY.withItalic(true).withColor(TextColor.fromRgb(7829367))));
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(!world.isClientSide()) {
            if (player.isShiftKeyDown()) {
                NetworkHooks.openGui((ServerPlayer) player, new RiggingContainer.Provider(stack), buf -> buf.writeItem(stack));
                return InteractionResultHolder.success(stack);
            }
        }
        return super.use(world, player, hand);
    }

    public enums.shipClass getValidclass() {
        return validclass;
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    public int getMainGunSlotCount(){return 0;}
    public int getSubGunSlotCount(){return 0;}
    public int getAASlotCount(){return 0;}

    public int getTorpedoSlotCount(){return 0;}

    public int getHangerSlots(){
        return 0;
    }

    public int getFuelTankCapacity(){return 50000;}

    public MultiInvStackHandlerItemStack[] createInventories(ItemStack container) {
        return new MultiInvStackHandlerItemStack[]{
                new MultiInvEquipmentHandlerItemStack(container, "MainGun", getMainGunSlotCount(), enums.SLOTTYPE.MAIN_GUN),
                new MultiInvEquipmentHandlerItemStack(container, "SubGun", getSubGunSlotCount(), enums.SLOTTYPE.SUB_GUN),
                new MultiInvEquipmentHandlerItemStack(container, "AA", getAASlotCount(), enums.SLOTTYPE.AA),
                new MultiInvEquipmentHandlerItemStack(container, "Torpedo", getTorpedoSlotCount(), enums.SLOTTYPE.TORPEDO),
                new MultiInvEquipmentHandlerItemStack(container, "Hangar", getHangerSlots(), enums.SLOTTYPE.PLANE)
        };
    }

    public abstract AnimatedGeoModel getModel();

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new RiggingItemCapabilityProvider(stack, null, this.getFuelTankCapacity(), this.createInventories(stack));
    }

    public void onUpdate(ItemStack stack) {
        IMultiInventory inventories = MultiInvUtil.getCap(stack);
        for (int invIndex = 0; invIndex < inventories.getInventoryCount(); invIndex++) {
            IItemHandler inventory = inventories.getInventory(invIndex);
            for (int slot = 0; slot < inventory.getSlots(); slot++) {
                ItemStack stackInSlot = inventory.getStackInSlot(0);
                Item item = stackInSlot.getItem();
                if (item instanceof ItemEquipmentBase) {
                    ((ItemEquipmentBase) item).onUpdate(stackInSlot, stack);
                }
            }
        }
    }

    @Override
    public GeoModelProvider getGeoModelProvider() {
        return this.getModel();
    }

    @Override
    public ResourceLocation getTextureLocation(Object instance) {
        return this.getModel().getTextureLocation(null);
    }

    public ResourceLocation getTexture(){
            return this.getModel().getTextureLocation(null);
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void RenderRigging(GeoModelProvider<?> entityModel, ItemStack Rigging, AbstractEntityCompanion entitylivingbaseIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

}
