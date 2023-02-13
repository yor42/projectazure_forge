package com.yor42.projectazure.intermod.curios;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.items.CurioItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        Item item = this.stack.getItem();
        return ((CurioItem) item).getSlotRenderer() != null;
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
        Item item = this.stack.getItem();
        if (!(renderer instanceof IEntityRenderer<?, ?>)) {
            return;
        }
        EntityModel<?> model = ((IEntityRenderer<?, ?>) renderer).getModel();
        if (!(model instanceof BipedModel<?>)) {
            return;
        }

        if(item instanceof CurioItem && ((CurioItem) item).getSlotRenderer() != null){
            ((CurioItem) item).getSlotRenderer().render(this.stack, livingEntity, matrixStack, renderTypeBuffer, light, ageInTicks);
        }
    }

    public CurioItem getItem(){
        if(stack.getItem() instanceof CurioItem){
            return (CurioItem) stack.getItem();
        }
        else{
            throw new IllegalStateException("Item attached to CuriosCapability must be instance of CurioItem!");
        }
    }
}
