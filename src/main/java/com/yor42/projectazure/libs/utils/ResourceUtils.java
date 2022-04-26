package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.resources.ResourceLocation;

public class ResourceUtils {

    public static ResourceLocation ModResourceLocation(String Location){
        return new ResourceLocation(Constants.MODID, Location);
    }

    public static ResourceLocation GeoModelEntityLocation(String FileName){
        if (!FileName.contains(".geo.json"))
            return ModResourceLocation("geo/entity/"+FileName+".geo.json");
        else if (!FileName.contains(".json"))
            return ModResourceLocation("geo/entity/"+FileName+".json");
        else
            return ModResourceLocation("geo/entity/"+FileName);
    }



    public static ResourceLocation TextureEntityLocation(String Filename){
        if(!Filename.contains(".png"))
            return ModResourceLocation("textures/entity/"+Filename+".png");
        else
            return ModResourceLocation("textures/entity/"+Filename);
    }

    public static ResourceLocation AnimationEntityKansenLocation(String FileName){
        if (!FileName.contains(".animation.json"))
            return ModResourceLocation("animations/entity/kansen/"+FileName+".animation.json");
        else if (!FileName.contains(".json"))
            return ModResourceLocation("animations/entity/kansen/"+FileName+".json");
        else
            return ModResourceLocation("animations/entity/kansen/"+FileName);
    }

    public static ResourceLocation AnimationEntityKanmusuLocation(String FileName){
        if (!FileName.contains(".animation.json"))
            return ModResourceLocation("animations/entity/kanmusu/"+FileName+".animation.json");
        else if (!FileName.contains(".json"))
            return ModResourceLocation("animations/entity/kanmusu/"+FileName+".json");
        else
            return ModResourceLocation("animations/entity/kanmusu/"+FileName);
    }

    public static ResourceLocation TextureLocation(String Filename) {
        return ModResourceLocation("textures/"+Filename+".png");
    }

    public static ResourceLocation ModelLocation(String Filename) {
        if (!Filename.contains(".geo.json"))
            return ModResourceLocation("geo/"+Filename+".geo.json");
        else if (!Filename.contains(".json"))
            return ModResourceLocation("geo/"+Filename+".json");
        else
        return ModResourceLocation("geo/"+Filename);
    }

    public static ResourceLocation AnimationLocation(String Filename) {
        if (!Filename.contains(".animation.json"))
            return ModResourceLocation("animations/"+Filename+".animation.json");
        else if (!Filename.contains(".json"))
            return ModResourceLocation("animations/"+Filename+".json");
        else
            return ModResourceLocation("animations/"+Filename);
    }
}
