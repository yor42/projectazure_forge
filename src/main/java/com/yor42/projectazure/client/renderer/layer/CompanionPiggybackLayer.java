package com.yor42.projectazure.client.renderer.layer;

import com.lowdragmc.lowdraglib.utils.DummyWorld;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMixinPlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CompanionPiggybackLayer<T extends PlayerEntity> extends LayerRenderer<T, PlayerModel<T>> {

    private AbstractEntityCompanion companion = null;

    public CompanionPiggybackLayer(IEntityRenderer<T, PlayerModel<T>> p_i50926_1_) {
        super(p_i50926_1_);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        CompoundNBT compoundnbt = ((IMixinPlayerEntity)player).getEntityonBack();

        if(compoundnbt.isEmpty()){
            return;
        }
        else if (this.companion == null) {
            EntityType.create(compoundnbt, new DummyWorld()).ifPresent((entity)->{
                if(entity instanceof AbstractEntityCompanion){
                    this.companion = (AbstractEntityCompanion) entity;
                }});
        }

        if(this.companion == null){
            return;
        }

        clampRotation(player, this.companion);
        Minecraft.getInstance().getEntityRenderDispatcher().render(this.companion, player.getX(), player.getY(), player.getZ(), netHeadYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

    }

    protected static void clampRotation(Entity source, Entity target) {
        target.setYBodyRot(source.yRot);
        float f = MathHelper.wrapDegrees(target.yRot - source.yRot);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        target.yRotO += f1 - f;
        target.yRot += f1 - f;
        target.setYHeadRot(target.yRot);
    }
}
