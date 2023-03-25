package com.yor42.projectazure.intermod.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.gameobject.items.ICurioItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class PACuriosCap implements ICurio {

    private final ItemStack stack;
    public PACuriosCap(ItemStack stack){
        this.stack = stack;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity) {
        this.getItem().curioTick(livingEntity, index, this.stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        this.getItem().curioOnEquip(slotContext.getWearer(), slotContext.getIndex(), prevStack);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext) {
        return true;
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
        Item item = this.stack.getItem();
        return ((ICurioItem) item).getSlotRenderer() != null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(String identifier, int index, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
        Item item = this.stack.getItem();
        if (!(renderer instanceof RenderLayerParent<?, ?>)) {
            return;
        }
        EntityModel<?> model = ((RenderLayerParent<?, ?>) renderer).getModel();
        if (!(model instanceof HumanoidModel<?>)) {
            return;
        }

        if(item instanceof ICurioItem && ((ICurioItem) item).getSlotRenderer() != null){
            ((ICurioItem) item).getSlotRenderer().render(this.stack, identifier, index, matrixStack, renderTypeBuffer, light, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }

    public ICurioItem getItem(){
        if(stack.getItem() instanceof ICurioItem){
            return (ICurioItem) stack.getItem();
        }
        else{
            throw new IllegalStateException("Item attached to CuriosCapability must be instance of ICurioItem!");
        }
    }
}
