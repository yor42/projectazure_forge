package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.libs.defined;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.system.CallbackI;

public class MinecraftUtils {

    public static ResourceLocation ModResourceLocation(String ID){
        return new ResourceLocation(defined.MODID, ID);
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
        //lazy-ass
        if (!FileName.contains(".animation.json"))
            return ModResourceLocation("animations/entity/kansen/"+FileName+".animation.json");
        else if (!FileName.contains(".json"))
            return ModResourceLocation("animations/entity/kansen/"+FileName+".json");
        else
            return ModResourceLocation("animations/entity/kansen/"+FileName);
    }

}
