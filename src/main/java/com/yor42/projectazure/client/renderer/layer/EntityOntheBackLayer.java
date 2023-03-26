package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMixinPlayerEntity;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;

@OnlyIn(Dist.CLIENT)
public class EntityOntheBackLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {

    private AbstractEntityCompanion companion = null;
    private final EntityRenderDispatcher dispatcher;

    public EntityOntheBackLayer(LivingEntityRenderer<T, PlayerModel<T>> p_i50926_1_) {
        super(p_i50926_1_);
        this.dispatcher = p_i50926_1_.getDispatcher();
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        CompoundTag compoundnbt = ((IMixinPlayerEntity)player).getEntityonBack();

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
        if(player.isCrouching()) {
            matrixStackIn.translate(0,0.25, 0);
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90F / (float) Math.PI));
        }
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

    public <E extends AbstractEntityCompanion & IAnimatable> void render(E p_229084_1_,T player, double p_229084_2_, double p_229084_4_, double p_229084_6_, float p_229084_8_, float p_229084_9_, PoseStack p_229084_10_, MultiBufferSource p_229084_11_, int p_229084_12_) {
        EntityRenderer<? super E> entityrenderer1 = this.dispatcher.getRenderer(p_229084_1_);

        if(!(entityrenderer1 instanceof GeoCompanionRenderer)){
            return;
        }

        GeoCompanionRenderer<? super E> entityrenderer = (GeoCompanionRenderer<? super E>) entityrenderer1;

        try {
            Vec3 vector3d = new Vec3(0,-p_229084_1_.getEyeHeight()+0.2, 0.25);

            double d2 = p_229084_2_ + vector3d.x();
            double d3 = p_229084_4_ + vector3d.y();
            double d0 = p_229084_6_ + vector3d.z();
            p_229084_10_.pushPose();
            p_229084_10_.translate(d2, d3, d0);

            PoseStack nametagStack = new PoseStack();
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
        target.setYBodyRot(source.getYRot());
        float f = Mth.wrapDegrees(target.getYRot() - source.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        target.yRotO += f1 - f;
        target.setYRot(target.getYRot()+ f1 - f);
        target.setYHeadRot(target.getYRot());
    }
}
