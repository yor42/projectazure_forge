package com.yor42.projectazure.intermod.tconstruct.datagen;

import com.yor42.projectazure.intermod.tconstruct.Tconstruct;
import com.yor42.projectazure.intermod.tconstruct.TinkersRegistry;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.RegisterFluids;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.ICommonRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

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
        this.metalMelting(consumer, TinkersRegistry.MoltenD32.FLUID.get(), "d32", false, false, meltingfoler, false);
        this.metalTagCasting(consumer, TinkersRegistry.MoltenD32.OBJECT, "d32", castingfoler, true);
        this.metalMaterialRecipe(consumer, PAMaterialProvider.D32, materials, "d32", false);
        this.materialMeltingCasting(consumer, PAMaterialProvider.D32, TinkersRegistry.MoltenD32.OBJECT, true, materials);
        //metalCasting(consumer, TinkersRegistry.MoltenD32.OBJECT, true, RegisterBlocks.D32_BLOCK.get(), RegisterItems.INGOT_D32.get(), RegisterItems.NUGGET_D32.get(), castingfoler, "d32");
    }
}
