package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/*
This class is for timeless and classic addon part of this mod.
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
    SANGVIS_RAILGUN_SAFE("sangvis_railgun_safe"),

    TYPHOON_BODY("typhoon_body"),
    TYPHOON_L_GRIP("typhoon_l_grip"),
    TYPHOON_T_GRIP("typhoon_t_grip"),

    GRANADELAUNCHER_BODY("granadelauncher_body"),
    GRANADELAUNCHER_L_GRIP("granadelauncher_l_grip"),
    GRANADELAUNCHER_T_GRIP("granadelauncher_t_grip");


    private final ResourceLocation modelLocation;
    private final boolean specialModel;
    @OnlyIn(Dist.CLIENT)
    private BakedModel cachedModel;

    registerGunAttachments(String modelName) {
        this(new ResourceLocation(Constants.MODID, "special/" + modelName), true);
    }

    registerGunAttachments(ResourceLocation resource, boolean specialModel) {
        this.modelLocation = resource;
        this.specialModel = specialModel;
    }

    @OnlyIn(Dist.CLIENT)
    public BakedModel getModel() {
        if (this.cachedModel == null) {
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
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
                ForgeModelBakery.addSpecialModel(model.modelLocation);
            }
        }

    }

}
