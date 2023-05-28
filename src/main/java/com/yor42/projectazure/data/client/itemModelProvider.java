package com.yor42.projectazure.data.client;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;

public class itemModelProvider extends ItemModelProvider {

    public itemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MODID, existingFileHelper);
    }

    public static final ArrayList<String> SIMPLETEXTURELIST = new ArrayList<>();

    public static final ArrayList<Pair<String, String>> ITEMENTRY = new ArrayList<>();
    public static final ArrayList<Pair<String, String>> TOOLENTRY = new ArrayList<>();
    public static final ArrayList<String> SIMPLETEXTUREBBLOCKLIST = new ArrayList<>();


    @Override
    protected void registerModels() {

        for(String id:SIMPLETEXTURELIST){
            buildModel(id);
        }

        for(String id:SIMPLETEXTUREBBLOCKLIST){
            buildSimpleBlockModel(id);
        }

        for(Pair<String, String> entry:TOOLENTRY){
            buildtoolModelWithSingleTex(entry.getFirst(), entry.getSecond());
        }

        for(Pair<String, String> entry:ITEMENTRY){
            buildModelWithSingleTex(entry.getFirst(), entry.getSecond());
        }

        buildModel("codex");

        buildMultiblockControllerModel("ammo_press");
        buildMultiblockControllerModel("originium_generator");
        buildMultiblockControllerModel("riftway");

    }

    public void buildSimpleBlockModel(String name){
        withExistingParent(name, modLoc("block/"+name));
    }

    public void buildMultiblockControllerModel(String name){
        withExistingParent(name, modLoc("block/"+name+"_controller"));
    }

    public ItemModelBuilder buildModel(String name){
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        return buildModel(itemGenerated, name);
    }

    private ItemModelBuilder buildModel(ModelFile model, String name){
        return getBuilder(name).parent(model).texture("layer0", "item/"+name);
    }

    private ItemModelBuilder buildModelWithSingleTex(String ItemName, String Texturename){
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        return buildModelWithSingleTex(itemGenerated, ItemName, Texturename);
    }

    private ItemModelBuilder buildtoolModelWithSingleTex(String ItemName, String Texturename){
        ModelFile itemGenerated = getExistingFile(mcLoc("item/handheld"));
        return buildModelWithSingleTex(itemGenerated, ItemName, Texturename);
    }

    private ItemModelBuilder buildModelWithSingleTex(ModelFile model, String name, String Texturename){
        return getBuilder(name).parent(model).texture("layer0", "item/"+Texturename);
    }
}
