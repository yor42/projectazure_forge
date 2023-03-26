package com.yor42.projectazure.gameobject.items;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.intermod.curios.client.ICurioRenderer;
import com.yor42.projectazure.intermod.curios.client.RenderGasMask;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.gameobject.items.materials.ModArmorMaterials.ArmorModMaterials.GASMASK;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColor;

public class GasMaskItem extends GeoArmorItem implements IAnimatable, ICurioItem {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public GasMaskItem(Properties builder) {
        super(GASMASK, EquipmentSlot.HEAD, builder);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationEvent<?> animationEvent) {
        return PlayState.STOP;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return CuriosCompat.addCapability(stack);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {

        CompoundTag compoundNBT = stack.getOrCreateTag();
        if(!compoundNBT.contains("filters")){
            return 1;
        }

        int damage=0, totaldamage=0;

        ListTag filters = compoundNBT.getList("filters", Tag.TAG_COMPOUND);
        for(int i = 0; i<filters.size(); i++){
            ItemStack filterstack = ItemStack.of(filters.getCompound(i));
            Item item = filterstack.getItem();
            if(item instanceof GasMaskFilterItem){
                damage+=ItemStackUtils.getCurrentDamage(filterstack);
                totaldamage+=((GasMaskFilterItem) item).getMaxHP();
            }
        }

        if (totaldamage == 0) {
            return 1;
        }

        return (double) damage/(double) totaldamage;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        super.onArmorTick(stack, world, player);
        CompoundTag compoundNBT = stack.getOrCreateTag();
        boolean activefilter = false;
        ListTag filters = compoundNBT.getList("filters", Tag.TAG_COMPOUND);
        for(int i = 0; i<filters.size(); i++){
            ItemStack filterstack = ItemStack.of(filters.getCompound(i));
            if(ItemStackUtils.DamageItem(player.isUnderWater()?5F:1F, filterstack)){
                activefilter = true;
                break;
            }
        }

        if(activefilter){
            if(player.tickCount%80==0){
                player.playSound(registerSounds.GASMASK_INHALE, 0.8F*(0.4F*MathUtil.rand.nextFloat()), 0.3F*(0.4F*MathUtil.rand.nextFloat()));

            }
            else if(40+player.tickCount%80==0){
                player.playSound(registerSounds.GASMASK_EXHALE, 0.8F*(0.4F*MathUtil.rand.nextFloat()), 0.3F*(0.4F*MathUtil.rand.nextFloat()));

            }
        }
    }

    @Override
    public void curioTick(LivingEntity entity, int index, ItemStack stack) {
        if(entity instanceof Player) {
            this.onArmorTick(stack, entity.getCommandSenderWorld(), (Player) entity);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTicks) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        
        RenderSystem.disableAlphaTest();
        Minecraft.getInstance().getTextureManager().bind(ResourceUtils.ModResourceLocation("textures/misc/gasmaskblur.png"));
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0.0D, height, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(width, height, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(width, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, List<Component> p_77624_3_, TooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, p_77624_3_, p_77624_4_);
        CompoundTag compoundNBT = stack.getOrCreateTag();
        ListTag filters = compoundNBT.getList("filters", Tag.TAG_COMPOUND);
        for(int i = 0; i<filters.size(); i++){
            ItemStack filterstack = ItemStack.of(filters.getCompound(i));
            Item filteritem = filterstack.getItem();
            if(filteritem instanceof GasMaskFilterItem){
                p_77624_3_.add(new TranslatableComponent("item.projectazure.gasmask.tooltip.filter", i+1, new TextComponent(String.format("%.2f", (double)ItemStackUtils.getCurrentHP(filterstack)/(double) ((GasMaskFilterItem) filteritem).getMaxHP()*100)+"%").withStyle(Style.EMPTY.withColor(getHPColor(filterstack)))).withStyle(ChatFormatting.GRAY));
                //p_77624_3_.add(new StringTextComponent(Integer.toHexString(ItemStackUtils.getHPColorInt(filterstack))));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_77659_1_, Player p_77659_2_, InteractionHand p_77659_3_) {
        ItemStack stack = p_77659_2_.getItemInHand(p_77659_3_);
        CompoundTag compoundNBT = stack.getOrCreateTag();
        ListTag filters = compoundNBT.getList("filters", Tag.TAG_COMPOUND);

        if(filters.isEmpty()){
            return InteractionResultHolder.pass(stack);
        }

        if(p_77659_2_.isCrouching()){
            ItemStack filterstack = ItemStack.of(filters.getCompound(0));
            filters.remove(0);
            int index = p_77659_2_.getInventory().getFreeSlot();

            if(index<0){
                p_77659_2_.drop(filterstack, true, true);
            }
            else{
                p_77659_2_.getInventory().setItem(index, filterstack);
            }
        }
        p_77659_2_.playSound(registerSounds.GASMASK_FILTER_REMOVE, 0.8F*(0.4F* MathUtil.rand.nextFloat()), 0.8F*(0.4F*MathUtil.rand.nextFloat()));
        compoundNBT.put("filters", filters);
        return InteractionResultHolder.success(stack);
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public ICurioRenderer getSlotRenderer() {
        return RenderGasMask.getInstance();
    }
}
