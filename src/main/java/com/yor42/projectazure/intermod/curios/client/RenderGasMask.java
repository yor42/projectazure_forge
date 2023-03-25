package com.yor42.projectazure.intermod.curios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.client.renderer.armor.GasMaskRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.type.capability.ICurio;

@OnlyIn(Dist.CLIENT)
public class RenderGasMask extends GasMaskRenderer implements ICurioRenderer {

    private static final RenderGasMask INSTANCE = new RenderGasMask();

    public static RenderGasMask getInstance(){
        return INSTANCE;
    }

    @Override
    public void render(ItemStack stack, String identifier, int index, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setCurrentItem(livingEntity, stack, EquipmentSlot.HEAD);
        this.applySlot(EquipmentSlot.HEAD);
        this.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
        this.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ICurio.RenderHelper.followBodyRotations(livingEntity, this);
        this.render(partialTicks, matrixStack, ItemRenderer.getFoilBufferDirect(renderTypeBuffer, renderType(this.getGeoModelProvider().getTextureLocation(null)), false, stack.hasFoil()), light);
    }
}
