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
import com.lowdragmc.multiblocked.client.renderer.impl.GeoComponentRenderer;
import com.lowdragmc.multiblocked.client.renderer.impl.MBDBlockStateRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import io.netty.util.Constant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
import software.bernie.geckolib3.util.GeckoLibUtil;
import software.bernie.geckolib3.util.RenderUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MBDGeoRenderer extends GeoComponentRenderer {
    public final String modelName, texturename;

    public MBDGeoRenderer(String modelName, String texturename, boolean isGlobal) {
        super(modelName, isGlobal);
        this.modelName = modelName;
        this.texturename = texturename;
    }

    public MBDGeoRenderer(String modelName, boolean isGlobal) {
        super(modelName, isGlobal);
        this.modelName = modelName;
        this.texturename = modelName;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onTextureSwitchEvent(TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(Constants.MODID, modelName));
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(Constants.MODID, modelName));
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ComponentFactory entity) {
        return new ResourceLocation(Constants.MODID, String.format("animations/block/machine/%s.animation.json", modelName));
    }

    @Override
    public ResourceLocation getModelLocation(ComponentFactory animatable) {
        return new ResourceLocation(Constants.MODID, String.format("geo/block/%s.geo.json", modelName));
    }

    @Override
    public ResourceLocation getTextureLocation(ComponentFactory entity) {
        return new ResourceLocation(Constants.MODID, String.format("textures/block/%s.png", texturename));
    }
}
