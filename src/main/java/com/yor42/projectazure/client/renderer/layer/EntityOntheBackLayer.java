package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMixinPlayerEntity;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;

@OnlyIn(Dist.CLIENT)
public class EntityOntheBackLayer<T extends PlayerEntity> extends LayerRenderer<T, PlayerModel<T>> {

    private AbstractEntityCompanion companion = null;
    private final EntityRendererManager dispatcher;

    public EntityOntheBackLayer(LivingRenderer<T, PlayerModel<T>> p_i50926_1_) {
        super(p_i50926_1_);
        this.dispatcher = p_i50926_1_.getDispatcher();
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        CompoundNBT compoundnbt = ((IMixinPlayerEntity)player).getEntityonBack();

        if(compoundnbt.isEmpty() || Minecraft.getInstance().level == null){
            this.companion = null;
            return;
        }
        else if (this.companion == null) {
            EntityType.create(compoundnbt, ClientUtils.getClientWorld()).ifPresent((entity)->{
                if(entity instanceof AbstractEntityCompanion){
                    this.companion = (AbstractEntityCompanion) entity;
                    this.companion.setNoAi(true);
                    this.companion.setOnPlayersBack(true);
                }});
        }

        if(this.companion == null){
            return;
        }

        matrixStackIn.pushPose();
        this.companion.yBodyRot = 180;
        this.companion.yBodyRotO = 180;
        this.companion.tickCount = player.tickCount;
        float playerheaddelta = player.yHeadRot-player.yBodyRot+180;
        this.companion.yHeadRot = playerheaddelta;
        this.companion.yHeadRotO = playerheaddelta;
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180));
        //clampRotation(player, this.companion);
        this.dispatcher.setRenderShadow(false);
        this.render(this.companion,player, 0, 0, 0, netHeadYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();

    }

    public <E extends AbstractEntityCompanion & IAnimatable> void render(E p_229084_1_,T player, double p_229084_2_, double p_229084_4_, double p_229084_6_, float p_229084_8_, float p_229084_9_, MatrixStack p_229084_10_, IRenderTypeBuffer p_229084_11_, int p_229084_12_) {
        EntityRenderer<? super E> entityrenderer1 = this.dispatcher.getRenderer(p_229084_1_);

        if(!(entityrenderer1 instanceof GeoCompanionRenderer)){
            return;
        }

        GeoCompanionRenderer<? super E> entityrenderer = (GeoCompanionRenderer<? super E>) entityrenderer1;

        try {
            Vector3d vector3d = new Vector3d(0,-p_229084_1_.getEyeHeight()+0.2, 0.25);

            double d2 = p_229084_2_ + vector3d.x();
            double d3 = p_229084_4_ + vector3d.y();
            double d0 = p_229084_6_ + vector3d.z();
            p_229084_10_.pushPose();
            p_229084_10_.translate(d2, d3, d0);

            MatrixStack nametagStack = new MatrixStack();
            nametagStack.translate(player.getX(), player.getY(), player.getZ());
            entityrenderer.renderonLayer(p_229084_1_, p_229084_8_, p_229084_9_, p_229084_10_, p_229084_11_, p_229084_12_);
            p_229084_10_.translate(-vector3d.x(), -vector3d.y(), -vector3d.z());
            p_229084_10_.popPose();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            p_229084_1_.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Assigned renderer", entityrenderer);
            crashreportcategory1.setDetail("Location", CrashReportCategory.formatLocation(p_229084_2_, p_229084_4_, p_229084_6_));
            crashreportcategory1.setDetail("Rotation", p_229084_8_);
            crashreportcategory1.setDetail("Delta", p_229084_9_);
            throw new ReportedException(crashreport);
        }
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
