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
    public static final ArrayList<String> SIMPLETEXTUREBBLOCKLIST = new ArrayList<>();


    @Override
    protected void registerModels() {

        for(String id:SIMPLETEXTURELIST){
            buildModel(id);
        }

        for(String id:SIMPLETEXTUREBBLOCKLIST){
            buildSimpleBlockModel(id);
        }

        for(Pair<String, String> entry:ITEMENTRY){
            buildModelWithSingleTex(entry.getFirst(), entry.getSecond());
        }

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

    private ItemModelBuilder buildModelWithSingleTex(ModelFile model, String name, String Texturename){
        return getBuilder(name).parent(model).texture("layer0", "item/"+Texturename);
    }

    private ItemModelBuilder buildModelWisdomCube(String ItemName){
        return buildModelWithSingleTex(ItemName, "wisdomcube");
    }

    private ItemModelBuilder buildModelBA(String ItemName){
        return buildModelWithSingleTex(ItemName, "spawn_bluearchive");
    }

    private ItemModelBuilder buildModelcls(String ItemName){
        return buildModelWithSingleTex(ItemName, "spawn_closer");
    }


    private ItemModelBuilder buildModelGFL(String ItemName){
        return buildModelWithSingleTex(ItemName, "gfl_manufacture_contract");
    }

    private ItemModelBuilder buildModelKC(String ItemName){
        return buildModelWithSingleTex(ItemName, "kc_card");
    }

    private ItemModelBuilder buildModelSR(String ItemName){
        return buildModelWithSingleTex(ItemName, "sr_sealstone");
    }

    private ItemModelBuilder buildModelAKN(String ItemName){
        return buildModelWithSingleTex(ItemName, "akn_document");
    }

    private ItemModelBuilder buildModelPCR(String ItemName){
        return buildModelWithSingleTex(ItemName, "goddess_stone");
    }

    private ItemModelBuilder buildModelFGO(String ItemName){
        return buildModelWithSingleTex(ItemName, "saint_quartz");
    }

    private ItemModelBuilder buildModelRUN(String ItemName){
        return buildModelWithSingleTex(ItemName, "akn_reunion_document");
    }
}
