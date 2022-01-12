package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.entity.misc.ModelClaymore;
import com.yor42.projectazure.client.model.items.ModelItemClaymore;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModelLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureLocation;

public class RosmontisClaymoreLayer extends GeoLayerRenderer<EntityRosmontis> implements IGeoRenderer {

    public RosmontisClaymoreLayer(IGeoRenderer<EntityRosmontis> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRosmontis entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        int SpellItemIndex = entityLivingBaseIn.getNextSkillItemindex();
        if(entityLivingBaseIn.isSkillItemInindex(0)){
            matrixStackIn.push();
            GeoModelProvider modelProvider = new ModelItemClaymore();
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
                matrixStackIn.rotate(new Quaternion(15f, -45, 15f, true));
                GeoModel model = modelProvider.getModel(ModelLocation("misc/modelclaymore1"));
                RenderType type = RenderType.getEntitySmoothCutout(TextureLocation("entity/modelclaymore1"));
                this.render(model, entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            }
            matrixStackIn.pop();
        }
        ItemStack stack = entityLivingBaseIn.getSkillItem1();
        if(entityLivingBaseIn.isSkillItem(stack)){
            matrixStackIn.push();
            GeoModelProvider modelProvider = new ModelItemClaymore();
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
                matrixStackIn.rotate(new Quaternion(15f, 45, -15f, true));
                GeoModel model = modelProvider.getModel(ModelLocation("misc/modelclaymore1"));
                RenderType type = RenderType.getEntitySmoothCutout(TextureLocation("entity/modelclaymore1"));
                this.render(model, entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            }
            matrixStackIn.pop();
        }
        if(entityLivingBaseIn.isSkillItemInindex(2)){
            matrixStackIn.push();
            float ymovement = (float) Math.sin(2 * Math.PI * 0.0125 * ageInTicks+0.52);
            boolean shouldDraw = true;
            if(entityLivingBaseIn.isUsingSpell() && SpellItemIndex == 2){
                int skillprogress = entityLivingBaseIn.getInitialSpellDelay()-entityLivingBaseIn.getSpellDelay();
                ymovement = 50*(float) Math.sin(2*Math.PI*(1/(float)entityLivingBaseIn.getInitialSpellDelay())*skillprogress);
                shouldDraw = ((float)skillprogress/entityLivingBaseIn.getInitialSpellDelay())<=0.25;
            }
            if(shouldDraw) {
                GeoModelProvider modelProvider = new ModelItemClaymore();
                matrixStackIn.scale(1 / 0.4f, 1 / 0.4f, 1 / 0.4f);
                matrixStackIn.translate(0.75, 0.5 + (ymovement * 0.1), 0.75);
                matrixStackIn.rotate(new Quaternion(-15f, 45, 15f, true));
                GeoModel model = modelProvider.getModel(ModelLocation("misc/modelclaymore1"));
                RenderType type = RenderType.getEntitySmoothCutout(TextureLocation("entity/modelclaymore1"));
                this.render(model, entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            }
            matrixStackIn.pop();
        }
        if(entityLivingBaseIn.isSkillItemInindex(3)){
            matrixStackIn.push();
            GeoModelProvider modelProvider = new ModelClaymore();
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
                matrixStackIn.rotate(new Quaternion(-15f, -45, -15f, true));
                GeoModel model = modelProvider.getModel(ModelLocation("misc/modelclaymore1"));
                RenderType type = RenderType.getEntitySmoothCutout(TextureLocation("entity/modelclaymore1"));
                this.render(model, entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            }
            matrixStackIn.pop();
        }
    }

    @Override
    public GeoModelProvider getGeoModelProvider() {
        return new ModelItemClaymore();
    }

    @Override
    public ResourceLocation getTextureLocation(Object instance) {
        return TextureLocation("entity/modelclaymore");
    }
}
