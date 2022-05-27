package com.yor42.projectazure.setup.register;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lowdragmc.lowdraglib.utils.FileUtility;
import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.definition.PartDefinition;
import com.lowdragmc.multiblocked.api.pattern.JsonBlockPattern;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class registerMultiBlocks {

    private static final ArrayList<String> multiblockdefinitions;
    private static final ArrayList<String> PartList;

    static{
        ArrayList<String> multiblocklist = new ArrayList<>();
        multiblocklist.add("projectazure_originiumgenerator");
        multiblockdefinitions = multiblocklist;

        ArrayList<String> partlist = new ArrayList<>();
        partlist.add("projectazure_energyhatch");
        partlist.add("projectazure_itemhatch");
        PartList = partlist;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerMultiBlocksonEvent(RegistryEvent.Register<Block> event){
        HatchTE.registerItemHatch();
    }

    private static void registerMultiblockDef(){
        for(String filename:multiblockdefinitions) {
            try {
                InputStream inputstream = ResourceLocation.class.getResourceAsStream("/assets/"+Constants.MODID+"/definition/controller/"+filename+".json");
                if (inputstream != null) {
                    JsonElement jsonElement = new JsonParser().parse(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                    if (jsonElement instanceof JsonObject) {
                        JsonObject config = jsonElement.getAsJsonObject();
                        ControllerDefinition definition = Multiblocked.GSON.fromJson(config, ControllerDefinition.class);
                        if (definition != null) {
                            MbdComponents.registerComponent(definition);
                            MbdComponents.handlers.add(() -> {
                                definition.basePattern = Multiblocked.GSON.fromJson(config.get("basePattern"), JsonBlockPattern.class).build();
                                definition.recipeMap = RecipeMap.RECIPE_MAP_REGISTRY.getOrDefault(config.get("recipeMap").getAsString(), RecipeMap.EMPTY);
                            });
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void registerPartDef(){
        for(String filename:PartList) {
            try {
                InputStream inputstream = ResourceLocation.class.getResourceAsStream("/assets/"+Constants.MODID+"/definition/parts/"+filename+".json");
                PartDefinition definition = Multiblocked.GSON.fromJson(FileUtility.readInputStream(inputstream), PartDefinition.class);
                if (definition != null) {
                    MbdComponents.registerComponent(definition);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void register(){};

}
