package com.yor42.projectazure.intermod.curios.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.renderer.armor.GasMaskRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderGasMask extends GasMaskRenderer implements ICurioRenderer {

    private static final RenderGasMask INSTANCE = new RenderGasMask();

    public static RenderGasMask getInstance(){
        return INSTANCE;
    }

    @Override
    public void render(ItemStack stack, LivingEntity entity, MatrixStack posestack, IRenderTypeBuffer buffer, int light, float partialTicks) {
        this.setCurrentItem(entity, stack, EquipmentSlotType.HEAD);
        this.applySlot(EquipmentSlotType.HEAD);
        this.render(partialTicks, posestack, ItemRenderer.getFoilBufferDirect(buffer, renderType(this.getGeoModelProvider().getTextureLocation(null)), false, stack.hasFoil()), light);
    }
}
