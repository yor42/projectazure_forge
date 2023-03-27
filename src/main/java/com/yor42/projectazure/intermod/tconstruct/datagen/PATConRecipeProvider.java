package com.yor42.projectazure.intermod.tconstruct.datagen;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.intermod.tconstruct.TinkersRegistry;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.library.data.recipe.ICommonRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipeBuilder;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipeBuilder;
import slimeknights.tconstruct.library.tools.SlotType;

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
    private final String upgradefolder ="tconstruct/upgrade/";

    private final String upgradesalvfolder ="tconstruct/upgrade/salvage";

    private final String materials ="tconstruct/materials/";

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        this.metalMelting(consumer, TinkersRegistry.MoltenD32.FLUID.get(), "d32", false, false, meltingfoler, false);
        this.metalTagCasting(consumer, TinkersRegistry.MoltenD32.OBJECT, "d32", castingfoler, true);
        this.metalMaterialRecipe(consumer, PAMaterialProvider.D32, materials, "d32", false);
        this.materialMeltingCasting(consumer, PAMaterialProvider.D32, TinkersRegistry.MoltenD32.OBJECT, true, materials);

        this.metalMelting(consumer, TinkersRegistry.MoltenRMA7012.FLUID.get(), "rma70-12", false, false, meltingfoler, false);
        this.metalTagCasting(consumer, TinkersRegistry.MoltenRMA7012.OBJECT, "rma70-12", castingfoler, true);
        this.metalMaterialRecipe(consumer, PAMaterialProvider.RMA70_12, materials, "rma70-12", false);
        this.materialMeltingCasting(consumer, PAMaterialProvider.RMA70_12, TinkersRegistry.MoltenRMA7012.OBJECT, true, materials);

        this.metalMelting(consumer, TinkersRegistry.MoltenRMA7024.FLUID.get(), "rma70-24", false, false, meltingfoler, false);
        this.metalTagCasting(consumer, TinkersRegistry.MoltenRMA7024.OBJECT, "rma70-24", castingfoler, true);
        this.metalMaterialRecipe(consumer, PAMaterialProvider.RMA70_24, materials, "rma70-24", false);
        this.materialMeltingCasting(consumer, PAMaterialProvider.RMA70_24, TinkersRegistry.MoltenRMA7024.OBJECT, true, materials);

        this.materialRecipe(consumer, PAMaterialProvider.ORIROCK, Ingredient.of(ModTags.Items.MATERIAL_ORIROCK), 1,1,materials+"/orirock");


        incUpgrade(consumer, TinkersRegistry.ASSIMILATING, 5, RegisterItems.ORIGINIUM_PRIME, 5, false);
        incUpgrade(consumer, TinkersRegistry.MYSTERY, 10, RegisterItems.PYROXENE, 2, false);


    }

    private void modifier(Consumer<FinishedRecipe> con, StaticModifier<?> mod, int maxLevel, RegistryObject<Item> item, int amount, float salvageChance, SlotType slotType, String folder, String salvageFolder)
    {
        ModifierRecipeBuilder.modifier(mod)
                .setMaxLevel(maxLevel)
                .addInput(item.get(), amount)
                .setSlots(slotType, 1)
                .saveSalvage(con, new ResourceLocation(Constants.MODID, salvageFolder))
                .save(con, new ResourceLocation(Constants.MODID, folder));
    }

    private void upgrade(Consumer<FinishedRecipe> con, StaticModifier<?> mod, int maxLevel, RegistryObject<Item> item, int amount, float salvageChance)
    {
        modifier(con, mod, maxLevel, item, amount, salvageChance, SlotType.UPGRADE, upgradefolder,
                upgradesalvfolder);
    }

    private void incUpgrade(Consumer<FinishedRecipe> con, StaticModifier<?> inc,
                            int maxLevel, RegistryObject<Item> item, int amount, boolean fullSalvage)
    {
        incremental(con, inc, maxLevel, item, amount, fullSalvage, SlotType.UPGRADE, upgradefolder,
                upgradesalvfolder);
    }

    private void incremental(Consumer<FinishedRecipe> con, StaticModifier<?> inc, int maxLevel, RegistryObject<Item> item, int amount, boolean fullSalvage, SlotType slotType, String folder, String salvageFolder)
    {
        IncrementalModifierRecipeBuilder.modifier(inc)
                .setMaxLevel(maxLevel)
                .setInput(item.get(), 1, amount)
                .setSlots(slotType, 1)
                .saveSalvage(con, new ResourceLocation(Constants.MODID, salvageFolder+inc.getId().getPath()+"_salvage"))
                .save(con, new ResourceLocation(Constants.MODID, folder+inc.getId().getPath()+"_recipe"));
    }
    //private static void registerMaterial(Consumer<IFinishedRecipe> consumer, TinkersRegistry.TinkersFluids fluid, )
}
