package com.yor42.projectazure.client.input;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.List;

public class KeybindHandler {

    public static final List<KeyMapping> KEYS = new ArrayList<>();

    public static final KeyMapping HELMET_MODE = new KeybindBuilder().description("keybind.projectazure.helmet_mode").conflictInGame().modifier(KeyModifier.NONE)
            .keyCode(78).build();


    public static void register(){
        for(KeyMapping key:KEYS){
            ClientRegistry.registerKeyBinding(key);
        }
    }

}
