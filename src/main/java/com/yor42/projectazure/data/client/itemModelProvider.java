package com.yor42.projectazure.data.client;

import com.yor42.projectazure.libs.defined;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class itemModelProvider extends ItemModelProvider {

    public itemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, defined.MODID, existingFileHelper);
    }


    @Override
    protected void registerModels() {

        buildModel("ingot_copper");
        buildModel("ingot_tin");
        buildModel("ingot_bronze");
        buildModel("ingot_lead");
        buildModel("ingot_aluminium");
        buildModel("ingot_zinc");
        buildModel("ingot_steel");

        buildModel("plate_copper");
        buildModel("plate_tin");
        buildModel("plate_bronze");
        buildModel("plate_lead");
        buildModel("plate_aluminium");
        buildModel("plate_zinc");
        buildModel("plate_steel");
        buildModel("plate_iron");

        buildModel("dust_copper");
        buildModel("dust_tin");
        buildModel("dust_bronze");
        buildModel("dust_lead");
        buildModel("dust_aluminium");
        buildModel("dust_zinc");
        buildModel("dust_coal");
        buildModel("dust_iron");
        buildModel("dust_steel");

        buildModel("mortar_iron");
        buildModel("hammer_iron");

        buildModel("iron_pipe");
        buildModel("steel_pipe");
        buildModel("mechanical_parts");
        buildModel("motor_basic");
        buildModel("tree_sap");
        buildModel("circuit_primitive");

        buildModel("copper_wire");
        buildModel("copper_coil");
        buildModel("steel_cutter");

        buildModel("mold_plate");
        buildModel("mold_wire");
        buildModel("mold_extraction");


        /*
        Build Ore Block Item model
         */
        buildOreModel("ore_stone_bauxite");
        buildOreModel("ore_stone_copper");
        buildOreModel("ore_stone_tin");
        buildOreModel("ore_stone_lead");

        buildModel("rainbow_wisdomcube");
        buildModel("wisdomcube");
        buildModel("disc_fridaynight");
        buildModel("oath_ring");
        buildModel("ammo_generic");
        buildModel("disc_brainpower");
        buildModel("bonk_bat");

        buildModel("bandage_roll");

        buildModel("5.56_magazine");

        buildModelWisdomCube("spawnayanami");
        buildModelWisdomCube("spawngangwon");
        buildModelWisdomCube("spawnenterprise");
        buildModelWisdomCube("spawnnagato");

        buildModelBlueArchive("spawnshiroko");

    }

    public void buildOreModel(String name){
        withExistingParent(name, modLoc("block/"+name));
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

    private ItemModelBuilder buildModelBlueArchive(String ItemName){
        return buildModelWithSingleTex(ItemName, "spawn_bluearchive");
    }
}
