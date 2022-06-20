package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/*
This class is for timeless and classic addon.


 */
@Mod.EventBusSubscriber(
        modid = Constants.MODID,
        value = {Dist.CLIENT},
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public enum registerGunAttachments {

    WHITEFANG465_VERTICALGRIP("whitefang465_tactical_grip"),
    WHITEFANG465_SUPPRESSOR("whitefang465_suppressor"),
    WHITEFANG465_BODY("whitefang465_body"),

    SANGVIS_RAILGUN_BODY("sangvis_railgun"),
    SANGVIS_RAILGUN_SAFE("sangvis_railgun_safe");


    private final ResourceLocation modelLocation;
    private final boolean specialModel;
    @OnlyIn(Dist.CLIENT)
    private IBakedModel cachedModel;

    registerGunAttachments(String modelName) {
        this(new ResourceLocation(Constants.MODID, "special/" + modelName), true);
    }

    registerGunAttachments(ResourceLocation resource, boolean specialModel) {
        this.modelLocation = resource;
        this.specialModel = specialModel;
    }

    @OnlyIn(Dist.CLIENT)
    public IBakedModel getModel() {
        if (this.cachedModel == null) {
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
            if (model == Minecraft.getInstance().getModelManager().getMissingModel()) {
                return model;
            }

            this.cachedModel = model;
        }

        return this.cachedModel;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void register(ModelRegistryEvent event) {
        registerGunAttachments[] var1 = values();

        for (registerGunAttachments model : var1) {
            if (model.specialModel) {
                ModelLoader.addSpecialModel(model.modelLocation);
            }
        }

    }

}
