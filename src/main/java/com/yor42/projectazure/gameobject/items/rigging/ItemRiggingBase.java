package com.yor42.projectazure.gameobject.items.rigging;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.yor42.projectazure.gameobject.capability.multiinv.*;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;
import software.bernie.geckolib3.util.RenderUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColor;

public abstract class ItemRiggingBase extends ItemDestroyable implements IAnimatable, ISyncable, IGeoRenderer {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    static {
        AnimationController.addModelFetcher((IAnimatable object) -> {
            if (object instanceof ItemRiggingBase) {
                ItemRiggingBase item = (ItemRiggingBase) object;
                return (IAnimatableModel<Object>) (item).getGeoModelProvider();
                }
            return null;
        });
    }

    protected enums.shipClass validclass;
    protected final int maingunslotslots, subgunslots, torpedoslots, aaslots, hangerslots, utilityslots, fueltankcapacity;
    protected boolean isShipRigging = true;
    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();
    protected MultiBufferSource rtb;

    public static String CONTROLLER_NAME = "rigging_controller";


    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    @Override
    public void setCurrentRTB(MultiBufferSource rtb) {
        this.rtb = rtb;
    }

    public boolean isShipRigging(){
        return this.isShipRigging;
    }

    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController(this, CONTROLLER_NAME, 0, this::predicate));
    }

    public ItemRiggingBase(Properties properties, int maingunslotslots, int subgunslots, int aaslots, int torpedoslots, int hangerslots, int utilityslots,int fuelcapccity, int HP) {
        super(properties, HP);
        GeckoLibNetwork.registerSyncable(this);
        this.maingunslotslots = maingunslotslots;
        this.subgunslots = subgunslots;
        this.aaslots = aaslots;
        this.torpedoslots = torpedoslots;
        this.hangerslots = hangerslots;
        this.utilityslots = utilityslots;
        this.fueltankcapacity = fuelcapccity;
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

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, IMultiInventory inventories, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        TextColor CategoryColor = TextColor.parseColor("#6bb82d");
        for(int i = 0; i< inventories.getInventoryCount(); i++){
            IItemHandler Equipments = inventories.getInventory(i);
            enums.SLOTTYPE slottype = enums.SLOTTYPE.values()[i];
            //not really needed but its here to make mc to not add header of equipment that isnt supported by rigging
            if(Equipments.getSlots()>0) {
                if(slottype != enums.SLOTTYPE.PLANE) {
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
                else{
                    tooltip.add(new TranslatableComponent("item.tooltip.hanger_slot_usage").append(": " + getPlaneCount(stack) + "/" + getHangerSlots()));
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
                NetworkHooks.openGui((ServerPlayer) player, new RiggingContainer.Provider(stack, null), (buf) -> {
                    buf.writeItem(stack);
                });
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

    public int getMainGunSlotCount(){return this.maingunslotslots;}
    public int getSubGunSlotCount(){return this.subgunslots;}
    public int getAASlotCount(){return this.aaslots;}

    public int getTorpedoSlotCount(){return this.torpedoslots;}

    public int getHangerSlots(){
        return this.hangerslots;
    }
    public int getUtilitySlots(){
        return this.utilityslots;
    }

    public int getFuelTankCapacity(){return this.fueltankcapacity;}

    public MultiInvStackHandlerItemStack[] createInventories(ItemStack container) {
        return new MultiInvStackHandlerItemStack[]{
                new MultiInvEquipmentHandlerItemStack(container, "MainGun", getMainGunSlotCount(), enums.SLOTTYPE.MAIN_GUN),
                new MultiInvEquipmentHandlerItemStack(container, "SubGun", getSubGunSlotCount(), enums.SLOTTYPE.SUB_GUN),
                new MultiInvEquipmentHandlerItemStack(container, "AA", getAASlotCount(), enums.SLOTTYPE.AA),
                new MultiInvEquipmentHandlerItemStack(container, "Torpedo", getTorpedoSlotCount(), enums.SLOTTYPE.TORPEDO),
                new MultiInvEquipmentHandlerItemStack(container, "Hangar", getHangerSlots(), enums.SLOTTYPE.PLANE),
                new MultiInvEquipmentHandlerItemStack(container, "Utility", getUtilitySlots(), enums.SLOTTYPE.UTILITY)
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
    public void renderEarly(Object animatable, PoseStack stackIn, float partialTicks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarlyMat = stackIn.last().pose().copy();
        IGeoRenderer.super.renderEarly(animatable, stackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public Vec3 getRenderOffset(Object animatable, float partialtick) {
        return Vec3.ZERO;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.isTrackingXform()) {
            PoseStack.Pose entry = stack.last();
            Matrix4f boneMat = entry.pose().copy();

            // Model space
            Matrix4f renderEarlyMatInvert = renderEarlyMat.copy();
            renderEarlyMatInvert.invert();
            Matrix4f modelPosBoneMat = boneMat.copy();
            modelPosBoneMat.multiplyBackward(renderEarlyMatInvert);
            bone.setModelSpaceXform(modelPosBoneMat);

            // Local space
            Matrix4f dispatchedMatInvert = this.dispatchedMat.copy();
            dispatchedMatInvert.invert();
            Matrix4f localPosBoneMat = boneMat.copy();
            localPosBoneMat.multiplyBackward(dispatchedMatInvert);
            // (Offset is the only transform we may want to preserve from the dispatched
            // mat)
            Vec3 renderOffset = this.getRenderOffset(this, 1.0F);
            localPosBoneMat.translate(
                    new Vector3f((float) renderOffset.x(), (float) renderOffset.y(), (float) renderOffset.z()));
            bone.setLocalSpaceXform(localPosBoneMat);
            // World space
            Matrix4f worldPosBoneMat = localPosBoneMat.copy();
            /*worldPosBoneMat.translate(new Vector3f((float) Minecraft.getInstance().cameraEntity.getX(),
                    (float) Minecraft.getInstance().cameraEntity.getY(),
                    (float) Minecraft.getInstance().cameraEntity.getZ()));

             */
            bone.setWorldSpaceXform(worldPosBoneMat);
        }

        IGeoRenderer.super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @OnlyIn(Dist.CLIENT)
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

    public void applyEquipmentCustomRotation(ItemStack equipment,GeoModel EquipmentModel, enums.SLOTTYPE slottype, int index, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
    }

    @OnlyIn(Dist.CLIENT)
    public void RenderRigging(GeoModelProvider<?> entityModel, ItemStack Rigging, AbstractEntityCompanion entitylivingbaseIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
        matrixStackIn.pushPose();
        this.dispatchedMat = matrixStackIn.last().pose().copy();
        AnimatedGeoModel modelRiggingProvider = this.getModel();

        if(!(Rigging.getItem() instanceof ItemRiggingBase)){
            return;
        }

        ItemRiggingBase riggingBase = (ItemRiggingBase) Rigging.getItem();


        entityModel.getModel(entityModel.getModelLocation(null)).getBone("RiggingHardPoint").ifPresent(
                (bone)->{
                    matrixStackIn.pushPose();

                    ArrayList<GeoBone> bonetree = new ArrayList<>();
                    GeoBone bone1 = bone;
                    while (true){
                        bonetree.add(bone1);
                        if(bone1.getParent()!=null) {
                            bone1 = bone1.getParent();
                            continue;
                        }
                        break;
                    }

                    for(int i=bonetree.size()-1; i>=0; i--){
                        RenderUtils.prepMatrixForBone(matrixStackIn, bonetree.get(i));
                    }
                    matrixStackIn.translate(bone.getPivotX()/16, bone.getPivotY()/16, bone.getPivotZ()/16);
                    RenderType type = RenderType.entitySmoothCutout(modelRiggingProvider.getTextureLocation(null));
                    GeoModel riggingmodel = modelRiggingProvider.getModel(modelRiggingProvider.getModelLocation(null));
                    AnimationEvent itemEvent = new AnimationEvent(riggingBase, 0, 0, Minecraft.getInstance().getFrameTime(), false, Collections.singletonList(Rigging));
                    modelRiggingProvider.setCustomAnimations(riggingBase, this.getInstanceId(riggingBase), itemEvent);
                    if(entitylivingbaseIn.tickCount%5==0) {
                        /*
                        riggingmodel.getBone("smoke").ifPresent((smokeloc) -> {
                            double x = smokeloc.getWorldPosition().x;
                            double y = smokeloc.getWorldPosition().y;
                            double z = smokeloc.getWorldPosition().z;
                            entitylivingbaseIn.getLevel().addParticle(ParticleTypes.SMOKE,
                                    x,
                                    y,
                                    z, 0, 0, 0);
                        });

                         */

                    }
                    this.render(riggingmodel, this, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                    this.RenderEquipments(Rigging, riggingmodel, matrixStackIn, bufferIn, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    matrixStackIn.popPose();
                });
        matrixStackIn.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public void RenderEquipments(ItemStack Rigging, GeoModel riggingModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
        IMultiInventory inventories = MultiInvUtil.getCap(Rigging);
        for(enums.SLOTTYPE slottype : enums.SLOTTYPE.values()){
            IItemHandler inventory = inventories.getInventory(slottype.ordinal());
            for(int i=0; i<inventory.getSlots(); i++){
                ItemStack equipment = inventory.getStackInSlot(i);
                Item item = equipment.getItem();
                if(!(item instanceof ItemEquipmentBase)){
                    continue;
                }
                ItemEquipmentBase equipmentItem = (ItemEquipmentBase) item;

                int finalI = i;
                riggingModel.getBone(slottype.getName()+(i+1)).ifPresent((bone)->{
                    matrixStackIn.pushPose();

                    ArrayList<GeoBone> bonetree = new ArrayList<>();
                    GeoBone bone1 = bone;
                    while (true){
                        bonetree.add(bone1);
                        if(bone1.getParent()!=null) {
                            bone1 = bone1.getParent();
                            continue;
                        }
                        break;
                    }

                    for(int j=bonetree.size()-1; j>=0; j--){
                        RenderUtils.prepMatrixForBone(matrixStackIn,bonetree.get(j));
                    }

                    matrixStackIn.translate(bone.getPivotX()/16, bone.getPivotY()/16, bone.getPivotZ()/16);
                    RenderType renderType = RenderType.entitySmoothCutout(equipmentItem.getTexture());
                    GeoModel EquipmentModel = equipmentItem.getEquipmentModel().getModel((equipmentItem).getEquipmentModel().getModelLocation(null));
                    equipmentItem.applyEquipmentCustomRotation(equipment, EquipmentModel, slottype, finalI, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    this.applyEquipmentCustomRotation(equipment, EquipmentModel, slottype, finalI, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    this.render(EquipmentModel, equipmentItem, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                    matrixStackIn.popPose();
                });


            }
        }
    }

    private int getPlaneCount(ItemStack riggingStack) {
        IItemHandler hangar = MultiInvUtil.getCap(riggingStack).getInventory(enums.SLOTTYPE.PLANE.ordinal());
        int count = 0;
        for (int i = 0; i < hangar.getSlots(); i++) {
            if (hangar.getStackInSlot(i).getItem() instanceof ItemEquipmentPlaneBase) {
                count++;
            }
        }
        return count;
    }

    @Nullable
    public Pair<String, Integer> getFireAnimationname(enums.SLOTTYPE slottype, int index){
        return null;
    }


}
