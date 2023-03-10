package com.yor42.projectazure.setup.register;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.utils.FileUtility;
import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.definition.ComponentDefinition;
import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.lowdragmc.multiblocked.api.registry.MbdComponents.registerComponent;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class registerMultiBlocks {

    public static final ArrayList<Pair<String, ResourceLocation>> DEFINITIONS = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerMultiBlocksonEvent(RegistryEvent.Register<Block> event){
        HatchTE.registerItemHatch();
        OriginiumGeneratorControllerTE.registerTE();
        AmmoPressControllerTE.registerTE();
        RiftwayControllerTE.registerTE();
        AdvancedAlloySmelterControllerTE.registerTE();
        SiliconeCrucibleTE.RegisterTE();
        //registerTEandRecipefromJSON("silicone_crucible");
    }

    public static void registerTEandRecipefromJSON(String name){
        DEFINITIONS.add(new Pair<>(name, registerComponentFromResource(Multiblocked.GSON, ResourceUtils.ModResourceLocation("controller/"+name), ControllerDefinition::new, null)));
    }

    public static <T extends ComponentDefinition> ResourceLocation registerComponentFromResource(Gson gson, ResourceLocation location, Function<ResourceLocation, T> constructor, BiConsumer<T, JsonObject> postHandler) {
        return registerComponentFromResource(location, constructor, null, null, postHandler);
    }

    public static <T extends ComponentDefinition, B extends Block> ResourceLocation registerComponentFromResource(ResourceLocation location, Function<ResourceLocation, T> constructor, @Nullable Function<T, B> block, @Nullable Function<B, BlockItem> item, BiConsumer<T, JsonObject> postHandler) {
        try {
            InputStream inputstream = ResourceLocation.class.getResourceAsStream(String.format("/assets/%s/definition/%s.json", location.getNamespace(), location.getPath()));
            JsonObject config = FileUtility.jsonParser.parse(new InputStreamReader(inputstream)).getAsJsonObject();
            T definition = (T) constructor.apply(new ResourceLocation(config.get("location").getAsString()));
            definition.fromJson(config);
            MbdComponents.registerComponent(definition);
            return definition.location;
        } catch (Exception var9) {
            Multiblocked.LOGGER.error("error while loading the definition resource {}", location.toString());
        }
        return null;
    }

    public static JsonObject readTrait(ResourceLocation location) {
        try {
            InputStream inputstream = ResourceLocation.class.getResourceAsStream(String.format("/assets/%s/%s.json", location.getNamespace(), location.getPath()));
            JsonObject config = FileUtility.jsonParser.parse(new InputStreamReader(inputstream)).getAsJsonObject();
            return config.has("traits")? config.getAsJsonObject("traits"): config;
        } catch (Exception var9) {
            Main.LOGGER.error("error while loading the resource {}", location.toString());
        }
        return null;
    }

    public static JsonObject readTrait(String name) {
        JsonObject object = readTrait(ResourceUtils.ModResourceLocation("traits/"+name));
        return object == null? new JsonObject():object;
    }

    public static void register(){}

}
