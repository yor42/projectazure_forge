package com.yor42.projectazure.intermod.curios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.client.renderer.armor.GasMaskRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@OnlyIn(Dist.CLIENT)
public class RenderGasMask extends GasMaskRenderer implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity livingEntity = slotContext.entity();
        this.setCurrentItem(livingEntity, stack, EquipmentSlot.HEAD);
        this.applySlot(EquipmentSlot.HEAD);
        this.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
        this.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        ICurioRenderer.followBodyRotations(livingEntity, this);
        this.render(partialTicks, matrixStack, ItemRenderer.getFoilBufferDirect(renderTypeBuffer, renderType(this.getGeoModelProvider().getTextureLocation(null)), false, stack.hasFoil()), light);
    }
}
