package com.yor42.projectazure.intermod.tconstruct;

import com.yor42.projectazure.intermod.tconstruct.datagen.PAMaterialProvider;
import com.yor42.projectazure.intermod.tconstruct.datagen.PAMaterialSpriteGenerator;
import com.yor42.projectazure.intermod.tconstruct.datagen.PATConRecipeProvider;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.library.client.data.material.GeneratorPartTextureJsonGenerator;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.tools.data.sprite.TinkerMaterialSpriteProvider;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;

public class Tconstruct {

    public static void runDatagens(GatherDataEvent event){
        runTextureProviders(event);
        DataGenerator gen = event.getGenerator();
        gen.addProvider(new PATConRecipeProvider(gen));
    }
    private static void runTextureProviders(GatherDataEvent event){
        DataGenerator gen = event.getGenerator();

        AbstractMaterialDataProvider materials = new PAMaterialProvider(gen);
        gen.addProvider(materials);
        gen.addProvider(new PAMaterialProvider.PAMaterialStats(gen, materials));
        gen.addProvider(new PAMaterialProvider.PAMaterialTraits(gen, materials));

        PAMaterialSpriteGenerator materialSpriteGenerator = new PAMaterialSpriteGenerator();
        TinkerMaterialSpriteProvider tinkerMaterialSprites = new TinkerMaterialSpriteProvider();
        gen.addProvider(new MaterialPartTextureGenerator(gen, event.getExistingFileHelper(), new TinkerPartSpriteProvider(), materialSpriteGenerator));
    }

    public static FluidObject<? extends FlowingFluid> TINKERSFLUID_TO_FLUIDOBJECT(RegisterFluids.TinkersFluids fluids){
        return new FluidObject<>(new ResourceLocation(Constants.MODID, fluids.name), fluids.name, fluids.FLUID, fluids.FLUID_FLOW, fluids.FLUID_BLOCK);
    }

}
