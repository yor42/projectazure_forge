package com.yor42.projectazure.data.client;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class itemModelProvider extends ItemModelProvider {

    public itemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MODID, existingFileHelper);
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
        buildModel("ingot_brass");

        buildModel("plate_copper");
        buildModel("plate_tin");
        buildModel("plate_bronze");
        buildModel("plate_lead");
        buildModel("plate_aluminium");
        buildModel("plate_zinc");
        buildModel("plate_steel");
        buildModel("plate_iron");
        buildModel("plate_brass");
        buildModel("plate_gold");

        buildModel("gear_copper");
        buildModel("gear_tin");
        buildModel("gear_bronze");
        buildModel("gear_lead");
        buildModel("gear_steel");
        buildModel("gear_iron");
        buildModel("gear_gold");

        buildModel("nugget_copper");
        buildModel("nugget_tin");
        buildModel("nugget_bronze");
        buildModel("nugget_lead");
        buildModel("nugget_steel");
        buildModel("nugget_brass");
        buildModel("nugget_zinc");

        buildModel("dust_copper");
        buildModel("dust_tin");
        buildModel("dust_bronze");
        buildModel("dust_lead");
        buildModel("dust_aluminium");
        buildModel("dust_zinc");
        buildModel("dust_coal");
        buildModel("dust_iron");
        buildModel("dust_steel");
        buildModel("dust_brass");
        buildModel("dust_originium");
        buildModel("dust_quartz");
        buildModel("dust_gold");

        buildModel("crude_oil_bucket");
        buildModel("gasoline_bucket");
        buildModel("diesel_bucket");
        buildModel("fuel_oil_bucket");

        buildModel("polymer_plate");

        buildModel("mortar_iron");
        buildModel("hammer_iron");

        buildModel("iron_pipe");
        buildModel("steel_pipe");
        buildModel("mechanical_parts");
        buildModel("motor_basic");
        buildModel("tree_sap");
        buildModel("circuit_primitive");
        buildModel("capacitor_primitive");
        buildModel("resistor_primitive");
        buildModel("5_56_ammo");
        buildModel("torpedo_ammo");
        buildModel("missile_ammo");
        buildModel("copper_wire");
        buildModel("copper_coil");
        buildModel("steel_cutter");
        buildModel("circuit_advanced");

        buildModel("mold_plate");
        buildModel("mold_wire");
        buildModel("mold_extraction");
        buildModel("energy_drink");

        buildModel("steel_gunframe_rifle");
        buildModel("pistol_grip");

        buildModel("orundum");
        buildModel("headhunting_pcb");

        buildModel("originium_seed");
        buildModel("quartz_seed");
        buildModel("originite");
        buildModel("originium_prime");

        buildModel("bitumen");

        /*
        Build Ore Block Item model
         */
        buildSimpleBlockModel("ore_stone_aluminium");
        buildSimpleBlockModel("ore_stone_copper");
        buildSimpleBlockModel("ore_stone_tin");
        buildSimpleBlockModel("ore_stone_lead");
        buildSimpleBlockModel("ore_stone_zinc");
        buildSimpleBlockModel("reenforced_plank");
        buildSimpleBlockModel("orirock");

        buildSimpleBlockModel("machine_frame");
        buildModel("disc_enterthebeginning");
        buildModel("rainbow_wisdomcube");
        buildModel("wisdomcube");
        buildModel("disc_fridaynight");
        buildModel("developer_bonus");
        buildModel("contributor_bonus");
        buildModel("oath_ring");
        buildModel("ammo_generic");
        buildModel("disc_brainpower");
        buildModel("bonk_bat");
        buildModel("commanding_stick");
        buildModel("disc_rickroll");
        buildModel("disc_sandstorm");
        buildModel("disc_sandroll");
        buildModel("disc_cc5");

        buildModel("bandage_roll");
        buildModel("medkit");
        buildModel("stasis_crystal");
        buildModel("5.56_magazine");

        buildModelWisdomCube("spawnayanami");
        buildModelWisdomCube("spawnjavelin");
        buildModelWisdomCube("spawngangwon");
        buildModelWisdomCube("spawnenterprise");
        buildModelWisdomCube("spawnnagato");
        buildModelWisdomCube("spawnlaffey");
        buildModelWisdomCube("spawnz23");
        buildModelWisdomCube("spawnyamato");

        buildModelGFL("spawnm4a1");

        buildModelAKN("spawnchen");
        buildModelAKN("spawnrosmontis");
        buildModelAKN("spawnamiya");
        buildModelAKN("spawnmudrock");
        buildModelRUN("spawntalulah");
        buildModelRUN("spawnfrostnova");
        buildModelAKN("spawntexas");
        buildModelAKN("spawnsiege");
        buildModelAKN("spawnschwarz");
        buildModelAKN("spawnlappland");
        buildModelAKN("spawnnearl");

        buildModelcls("spawnsylvi");

        buildModelBA("spawnshiroko");
        buildSimpleBlockModel("reenforced_concrete");

    }

    public void buildSimpleBlockModel(String name){
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

    private ItemModelBuilder buildModelBA(String ItemName){
        return buildModelWithSingleTex(ItemName, "spawn_bluearchive");
    }

    private ItemModelBuilder buildModelcls(String ItemName){
        return buildModelWithSingleTex(ItemName, "spawn_closer");
    }


    private ItemModelBuilder buildModelGFL(String ItemName){
        return buildModelWithSingleTex(ItemName, "gfl_manufacture_contract");
    }

    private ItemModelBuilder buildModelAKN(String ItemName){
        return buildModelWithSingleTex(ItemName, "akn_document");
    }

    private ItemModelBuilder buildModelRUN(String ItemName){
        return buildModelWithSingleTex(ItemName, "akn_reunion_document");
    }
}
