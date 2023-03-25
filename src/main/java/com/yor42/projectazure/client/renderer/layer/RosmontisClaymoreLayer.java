package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.client.model.entity.misc.ModelClaymore;
import com.yor42.projectazure.client.model.items.ModelItemClaymore;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModelLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureLocation;
@OnlyIn(Dist.CLIENT)
public class RosmontisClaymoreLayer extends GeoLayerRenderer<EntityRosmontis> implements IGeoRenderer<EntityRosmontis> {

    public MultiBufferSource rtb;

    public RosmontisClaymoreLayer(IGeoRenderer<EntityRosmontis> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityRosmontis entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.rtb = bufferIn;
        int SpellItemIndex = entityLivingBaseIn.getNextSkillItemindex();
        if(entityLivingBaseIn.isSkillItemInindex(0)){
            matrixStackIn.pushPose();
            GeoModelProvider<EntityClaymore> modelProvider = new ModelClaymore();
            matrixStackIn.scale(1/0.4f,1/0.4f,1/0.4f);
            float ymovement = (float) Math.sin(2 * Math.PI * 0.0125 * ageInTicks+0.785398);
            boolean shouldDraw = true;
            if(entityLivingBaseIn.isUsingSpell() && SpellItemIndex == 0){
                int skillprogress = entityLivingBaseIn.getInitialSpellDelay()-entityLivingBaseIn.getSpellDelay();
                ymovement = 50*(float) Math.sin(2*Math.PI*(1/(float)entityLivingBaseIn.getInitialSpellDelay())*skillprogress);
                shouldDraw = ((float)skillprogress/entityLivingBaseIn.getInitialSpellDelay())<=0.25;
            }
            if(shouldDraw) {
                matrixStackIn.translate(0.75, 0.5 + (ymovement * 0.1), -0.75);
                matrixStackIn.mulPose(new Quaternion(15f, -45, 15f, true));
                GeoModel model = modelProvider.getModel(ModelLocation("misc/modelclaymore1"));
                RenderType type = RenderType.entitySmoothCutout(TextureLocation("entity/modelclaymore1"));
                this.render(model, entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            }
            matrixStackIn.popPose();
        }
        ItemStack stack = entityLivingBaseIn.getSkillItem1();
        if(entityLivingBaseIn.isSkillItem(stack)){
            matrixStackIn.pushPose();
            GeoModelProvider<EntityClaymore> modelProvider = new ModelClaymore();
            matrixStackIn.scale(1/0.4f,1/0.4f,1/0.4f);
            float ymovement = (float) Math.sin(2 * Math.PI * 0.0125 * ageInTicks+0.785398);
            boolean shouldDraw = true;
            if(entityLivingBaseIn.isUsingSpell() && SpellItemIndex == 1){
                int skillprogress = entityLivingBaseIn.getInitialSpellDelay()-entityLivingBaseIn.getSpellDelay();
                ymovement = 50*(float) Math.sin(2*Math.PI*(1/(float)entityLivingBaseIn.getInitialSpellDelay())*skillprogress);
                shouldDraw = ((float)skillprogress/entityLivingBaseIn.getInitialSpellDelay())<=0.25;
            }
            if(shouldDraw) {
                matrixStackIn.translate(-0.75, 0.5 + (ymovement * 0.1), -0.75);
                matrixStackIn.mulPose(new Quaternion(15f, 45, -15f, true));
                GeoModel model = modelProvider.getModel(ModelLocation("misc/modelclaymore1"));
                RenderType type = RenderType.entitySmoothCutout(TextureLocation("entity/modelclaymore1"));
                this.render(model, entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            }
            matrixStackIn.popPose();
        }
        if(entityLivingBaseIn.isSkillItemInindex(2)){
            matrixStackIn.pushPose();
            float ymovement = (float) Math.sin(2 * Math.PI * 0.0125 * ageInTicks+0.52);
            boolean shouldDraw = true;
            if(entityLivingBaseIn.isUsingSpell() && SpellItemIndex == 2){
                int skillprogress = entityLivingBaseIn.getInitialSpellDelay()-entityLivingBaseIn.getSpellDelay();
                ymovement = 50*(float) Math.sin(2*Math.PI*(1/(float)entityLivingBaseIn.getInitialSpellDelay())*skillprogress);
                shouldDraw = ((float)skillprogress/entityLivingBaseIn.getInitialSpellDelay())<=0.25;
            }
            if(shouldDraw) {
                GeoModelProvider<EntityClaymore> modelProvider = new ModelClaymore();
                matrixStackIn.scale(1 / 0.4f, 1 / 0.4f, 1 / 0.4f);
                matrixStackIn.translate(0.75, 0.5 + (ymovement * 0.1), 0.75);
                matrixStackIn.mulPose(new Quaternion(-15f, 45, 15f, true));
                GeoModel model = modelProvider.getModel(ModelLocation("misc/modelclaymore1"));
                RenderType type = RenderType.entitySmoothCutout(TextureLocation("entity/modelclaymore1"));
                this.render(model, entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            }
            matrixStackIn.popPose();
        }
        if(entityLivingBaseIn.isSkillItemInindex(3)){
            matrixStackIn.pushPose();
            GeoModelProvider<EntityClaymore> modelProvider = new ModelClaymore();
            float ymovement = (float) Math.sin(2 * Math.PI * 0.0125 * ageInTicks+1.04);
            boolean shouldDraw = true;
            if(entityLivingBaseIn.isUsingSpell() && SpellItemIndex == 3){
                int skillprogress = entityLivingBaseIn.getInitialSpellDelay()-entityLivingBaseIn.getSpellDelay();
                ymovement = 50*(float) Math.sin(2*Math.PI*(1/(float)entityLivingBaseIn.getInitialSpellDelay())*skillprogress);
                shouldDraw = ((float)skillprogress/entityLivingBaseIn.getInitialSpellDelay())<=0.25;
            }
            if(shouldDraw) {
                matrixStackIn.scale(1 / 0.4f, 1 / 0.4f, 1 / 0.4f);
                matrixStackIn.translate(-0.75, 0.5 + (ymovement * 0.1), 0.75);
                matrixStackIn.mulPose(new Quaternion(-15f, -45, -15f, true));
                GeoModel model = modelProvider.getModel(ModelLocation("misc/modelclaymore1"));
                RenderType type = RenderType.entitySmoothCutout(TextureLocation("entity/modelclaymore1"));
                this.render(model, entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            }
            matrixStackIn.popPose();
        }
    }

    @Override
    public void setCurrentRTB(MultiBufferSource rtb) {
        this.rtb = rtb;
    }

    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    @Override
    public GeoModelProvider getGeoModelProvider() {
        return new ModelItemClaymore();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRosmontis instance) {
        return TextureLocation("entity/modelclaymore");
    }
}
