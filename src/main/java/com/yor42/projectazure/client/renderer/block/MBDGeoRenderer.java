package com.yor42.projectazure.client.renderer.block;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.tile.IComponent;
import com.lowdragmc.multiblocked.client.renderer.IMultiblockedRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.geckolib3.util.RenderUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MBDGeoRenderer extends AnimatedGeoModel<MBDGeoRenderer.ComponentFactory> implements IMultiblockedRenderer, IGeoRenderer<MBDGeoRenderer.ComponentFactory> {
    public static final MBDGeoRenderer INSTANCE = new MBDGeoRenderer(null, false);
    private static final Set<String> particleTexture = new HashSet();
    public final String modelName, texturename;
    public final boolean isGlobal;
    @OnlyIn(Dist.CLIENT)
    private ComponentFactory itemFactory;

    public IRenderTypeBuffer rtb;

    public MBDGeoRenderer(String modelName, String texturename, boolean isGlobal) {
        this.modelName = modelName;
        this.isGlobal = isGlobal;
        this.texturename = texturename;
        if (Multiblocked.isClient() && modelName != null && particleTexture.add(modelName)) {
            this.registerTextureSwitchEvent();
        }
    }

    public MBDGeoRenderer(String modelName, boolean isGlobal) {
        this(modelName, modelName, isGlobal);
    }

    @OnlyIn(Dist.CLIENT)
    public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, boolean leftHand, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay, IBakedModel bakedModel) {
        if (this.itemFactory == null) {
            this.itemFactory = new ComponentFactory((IComponent)null, this);
        }

        GeoModel model = this.getModel(this.getModelLocation(this.itemFactory));
        this.setLivingAnimations(this.itemFactory, this.getUniqueID(this.itemFactory));
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.009999999776482582, 0.0);
        matrixStack.translate(0.5, 0.0, 0.5);
        this.render(model, this.itemFactory, Minecraft.getInstance().getFrameTime(), RenderType.entityTranslucent(this.getTextureLocation(this.itemFactory)), matrixStack, buffer, (IVertexBuilder)null, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public List<BakedQuad> renderModel(IBlockDisplayReader level, BlockPos pos, BlockState state, Direction side, Random rand, IModelData modelData) {
        return Collections.emptyList();
    }

    @OnlyIn(Dist.CLIENT)
    public void onTextureSwitchEvent(TextureStitchEvent.Pre event) {
        event.addSprite(ResourceUtils.ModResourceLocation(this.texturename));
    }
    

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(ResourceUtils.ModResourceLocation(this.modelName));
    }

    public boolean isGlobalRenderer(@Nonnull TileEntity te) {
        return this.isGlobal;
    }

    public String getType() {
        return "geo";
    }

    public IMultiblockedRenderer fromJson(Gson gson, JsonObject jsonObject) {
        return new MBDGeoRenderer(jsonObject.get("modelName").getAsString(), JSONUtils.getAsBoolean(jsonObject, "isGlobal", false));
    }

    public JsonObject toJson(Gson gson, JsonObject jsonObject) {
        jsonObject.addProperty("modelName", this.modelName);
        if (this.isGlobal) {
            jsonObject.addProperty("isGlobal", true);
        }

        return jsonObject;
    }

    public Supplier<IMultiblockedRenderer> createConfigurator(WidgetGroup parent, DraggableScrollableWidgetGroup group, IMultiblockedRenderer current) {
        TextFieldWidget tfw = new TextFieldWidget(1, 1, 150, 20, (Supplier)null, (Consumer)null);
        File path = new File(Multiblocked.location, "assets/multiblocked/geo");
        AtomicBoolean isGlobal = new AtomicBoolean(false);
        if (current instanceof MBDGeoRenderer) {
            tfw.setCurrentString(((MBDGeoRenderer)current).modelName);
            isGlobal.set(((MBDGeoRenderer)current).isGlobal);
        }

        group.addWidget((new ButtonWidget(155, 1, 20, 20, (cd) -> {
            DialogWidget.showFileDialog(parent, "select a geo file", path, true, DialogWidget.suffixFilter(".geo.json"), (r) -> {
                if (r != null && r.isFile()) {
                    tfw.setCurrentString(r.getName().replace(".geo.json", ""));
                }

            });
        })).setButtonTexture(new IGuiTexture[]{new ResourceTexture("multiblocked:textures/gui/darkened_slot.png"), new TextTexture("F", -1)}).setHoverTooltips(new String[]{"multiblocked.gui.tips.file_selector"}));
        group.addWidget(tfw);
        boolean var10006 = isGlobal.get();
        isGlobal.getClass();
        group.addWidget(this.createBoolSwitch(1, 25, "isGlobal", "multiblocked.gui.predicate.geo.0", var10006, isGlobal::set));
        return () -> tfw.getCurrentString().isEmpty() ? null : new MBDGeoRenderer(tfw.getCurrentString(), isGlobal.get());
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isRaw() {
        return !GeckoLibCache.getInstance().getGeoModels().containsKey(this.getModelLocation((ComponentFactory)null));
    }

    public boolean hasTESR(TileEntity tileEntity) {
        return true;
    }

    public void onPostAccess(IComponent component) {
        component.setRendererObject(null);
    }

    public void onPreAccess(IComponent component) {
        component.setRendererObject(new ComponentFactory(component, this));
    }

    public void render(TileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (te instanceof IComponent && ((IComponent)te).getRendererObject() instanceof ComponentFactory) {
            IComponent controller = (IComponent)te;
            ComponentFactory factory = (ComponentFactory)controller.getRendererObject();
            GeoModel model = this.getModel(this.getModelLocation(factory));
            this.setLivingAnimations(factory, this.getUniqueID(factory));
            stack.pushPose();
            stack.translate(0.0, 0.009999999776482582, 0.0);
            stack.translate(0.5, 0.0, 0.5);
            switch (controller.getFrontFacing()) {
                case SOUTH:
                    stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                    break;
                case WEST:
                    stack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
                    break;
                case NORTH:
                    stack.mulPose(Vector3f.YP.rotationDegrees(0.0F));
                    break;
                case EAST:
                    stack.mulPose(Vector3f.YP.rotationDegrees(270.0F));
                    break;
                case UP:
                    stack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                    break;
                case DOWN:
                    stack.mulPose(Vector3f.XN.rotationDegrees(90.0F));
            }

            this.render(model, stack, buffer, combinedLight);
            stack.popPose();
        }

    }

    void render(GeoModel model, MatrixStack matrixStackIn, IRenderTypeBuffer buffers, int packedLightIn) {
        IVertexBuilder currentBuffer = buffers.getBuffer(RenderType.entityCutout(this.getTextureLocation((ComponentFactory)null)));

        GeoBone group;
        for(Iterator var6 = model.topLevelBones.iterator(); var6.hasNext(); currentBuffer = this.renderRecursively(group, matrixStackIn, buffers, currentBuffer, packedLightIn)) {
            group = (GeoBone)var6.next();
        }

    }

    @OnlyIn(Dist.CLIENT)
    public IVertexBuilder renderRecursively(GeoBone bone, MatrixStack stack, IRenderTypeBuffer buffers, IVertexBuilder currentBuffer, int packedLightIn) {
        if (bone.name.contains("emissive")) {
            packedLightIn = 15728880;
        }

        boolean isTranslucent = bone.name.startsWith("translucent");
        if (isTranslucent) {
            currentBuffer = buffers.getBuffer(RenderType.entityTranslucentCull(this.getTextureLocation((ComponentFactory)null)));
        }

        stack.pushPose();
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        RenderUtils.rotate(bone, stack);
        RenderUtils.scale(bone, stack);
        RenderUtils.moveBackFromPivot(bone, stack);
        Iterator var7;
        if (!bone.isHidden()) {
            for(var7 = bone.childCubes.iterator(); var7.hasNext(); stack.popPose()) {
                GeoCube cube = (GeoCube)var7.next();
                stack.pushPose();
                if (!bone.cubesAreHidden()) {
                    this.renderCube(cube, stack, currentBuffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }

        GeoBone childBone;
        if (!bone.childBonesAreHiddenToo()) {
            for(var7 = bone.childBones.iterator(); var7.hasNext(); currentBuffer = this.renderRecursively(childBone, stack, buffers, currentBuffer, packedLightIn)) {
                childBone = (GeoBone)var7.next();
            }
        }

        if (isTranslucent) {
            currentBuffer = buffers.getBuffer(RenderType.entityCutout(this.getTextureLocation((ComponentFactory)null)));
        }

        stack.popPose();
        return currentBuffer;
    }

    public ResourceLocation getAnimationFileLocation(ComponentFactory entity) {
        return ResourceUtils.ModResourceLocation(String.format("animations/block/machine/%s.animation.json", this.texturename));
    }

    public ResourceLocation getModelLocation(ComponentFactory animatable) {
        return ResourceUtils.ModResourceLocation(String.format("geo/block/%s.geo.json", this.modelName));
    }

    public ResourceLocation getTextureLocation(ComponentFactory entity) {
        return ResourceUtils.ModResourceLocation(String.format("textures/block/%s.png", this.texturename));
    }

    @Override
    public void setCurrentRTB(IRenderTypeBuffer rtb) {
        this.rtb = rtb;
    }

    @Override
    public IRenderTypeBuffer getCurrentRTB() {
        return this.rtb;
    }

    public GeoModelProvider<?> getGeoModelProvider() {
        return this;
    }

    static {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            AnimationController.addModelFetcher((object) -> {
                if (object instanceof ComponentFactory) {
                    ComponentFactory factory = (ComponentFactory)object;
                    return (IAnimatableModel)factory.renderer.getGeoModelProvider();
                } else {
                    return null;
                }
            });
        }

    }

    public static class ComponentFactory implements IAnimatable {
        public final IComponent component;
        public final MBDGeoRenderer renderer;
        public final AnimationFile animationFile;
        public String currentStatus;
        private final AnimationFactory factory = new AnimationFactory(this);

        public ComponentFactory(IComponent component, MBDGeoRenderer renderer) {
            this.component = component;
            this.renderer = renderer;
            this.animationFile = GeckoLibCache.getInstance().getAnimations().get(renderer.getAnimationFileLocation(this));
        }

        private PlayState predicate(AnimationEvent<ComponentFactory> event) {
            AnimationController<ComponentFactory> controller = event.getController();
            String lastStatus = this.currentStatus;
            this.currentStatus = this.component == null ? "unformed" : this.component.getStatus();
            if (!Objects.equals(lastStatus, this.currentStatus)) {
                if (this.currentStatus == null) {
                    return PlayState.STOP;
                }

                AnimationBuilder animationBuilder = new AnimationBuilder();
                if (lastStatus != null) {
                    Animation trans = this.animationFile.getAnimation(lastStatus + "-" + this.currentStatus);
                    if (trans != null) {
                        animationBuilder.addAnimation(trans.animationName);
                    }
                }

                if (this.animationFile.getAnimation(this.currentStatus) != null) {
                    animationBuilder.addAnimation(this.currentStatus);
                }

                controller.setAnimation(animationBuilder);
            }

            return PlayState.CONTINUE;
        }

        public void registerControllers(AnimationData data) {
            data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
        }

        public AnimationFactory getFactory() {
            return this.factory;
        }
    }
}
