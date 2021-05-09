package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.libs.defined;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class ResourceUtils {

    public static ResourceLocation ModResourceLocation(String Location){
        return new ResourceLocation(defined.MODID, Location);
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

    //From Mekanism
    public boolean isKeyPressed(KeyBinding key){
        if (key.isKeyDown()) {
            return true;
        }
        if (key.getKeyConflictContext().isActive() && key.getKeyModifier().isActive(key.getKeyConflictContext())) {
            //Manually check in case keyBinding#pressed just never got a chance to be updated
            return isKeyDown(key);
        }
        //If we failed, due to us being a key modifier as our key, check the old way
        return KeyModifier.isKeyCodeModifier(key.getKey()) && isKeyDown(key);
    }

    public static boolean isKeyDown(KeyBinding keyBinding) {
        InputMappings.Input key = keyBinding.getKey();
        int keyCode = key.getKeyCode();
        if (keyCode != InputMappings.INPUT_INVALID.getKeyCode()) {
            long windowHandle = Minecraft.getInstance().getMainWindow().getHandle();
            try {
                if (key.getType() == InputMappings.Type.KEYSYM) {
                    return InputMappings.isKeyDown(windowHandle, keyCode);
                } else if (key.getType() == InputMappings.Type.MOUSE) {
                    return GLFW.glfwGetMouseButton(windowHandle, keyCode) == GLFW.GLFW_PRESS;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}
