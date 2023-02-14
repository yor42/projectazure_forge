package com.yor42.projectazure.intermod.tconstruct.datagen;

import com.yor42.projectazure.intermod.tconstruct.Tconstruct;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.RegisterFluids;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import slimeknights.tconstruct.library.data.recipe.ICommonRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;

import java.util.function.Consumer;

public class PATConRecipeProvider extends RecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper, ISmelteryRecipeHelper, ICommonRecipeHelper {
    public PATConRecipeProvider(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    public String getModId() {
        return Constants.MODID;
    }

    private final String castingfoler ="tconstruct/casting/";
    private final String meltingfoler ="tconstruct/melting/";

    private final String materials ="tconstruct/materials/";

    @Override
    public void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        metalMelting(consumer, RegisterFluids.MOLTEN_D32.FLUID.get(), "d32_melting", false, meltingfoler, false);
        metalTagCasting(consumer, Tconstruct.TINKERSFLUID_TO_FLUIDOBJECT(RegisterFluids.MOLTEN_D32), "d32_casting", castingfoler, true);
        metalMaterialRecipe(consumer, PAMaterialProvider.D32, materials, PAMaterialProvider.D32.getPath(), false);
        materialMeltingCasting(consumer,  PAMaterialProvider.D32, Tconstruct.TINKERSFLUID_TO_FLUIDOBJECT(RegisterFluids.MOLTEN_D32), "");
    }
}
