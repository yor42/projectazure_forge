package com.yor42.projectazure.intermod.tconstruct;

import com.yor42.projectazure.intermod.tconstruct.datagen.PAMaterialProvider;
import com.yor42.projectazure.intermod.tconstruct.datagen.PAMaterialRenderInfoProvider;
import com.yor42.projectazure.intermod.tconstruct.datagen.PAMaterialSpriteGenerator;
import com.yor42.projectazure.intermod.tconstruct.datagen.PATConRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;

public class Tconstruct {

    public static void runDatagens(GatherDataEvent event){

        if(event.includeClient()) {
            runTextureProviders(event);
        }

        if(event.includeServer()) {
            DataGenerator gen = event.getGenerator();
            gen.addProvider(new PATconFluidTag(gen, event.getExistingFileHelper()));
            gen.addProvider(new PATConRecipeProvider(gen));
            AbstractMaterialDataProvider materials = new PAMaterialProvider(gen);
            gen.addProvider(materials);
            gen.addProvider(new PAMaterialProvider.PAMaterialStats(gen, materials));
            gen.addProvider(new PAMaterialProvider.PAMaterialTraits(gen, materials));
        }
    }
    private static void runTextureProviders(GatherDataEvent event){
        DataGenerator gen = event.getGenerator();
        PAMaterialSpriteGenerator materialSpriteGenerator = new PAMaterialSpriteGenerator();
        gen.addProvider(new MaterialPartTextureGenerator(gen, event.getExistingFileHelper(), new TinkerPartSpriteProvider(), materialSpriteGenerator));
        gen.addProvider(new PAMaterialRenderInfoProvider(gen, materialSpriteGenerator));
    }

}
