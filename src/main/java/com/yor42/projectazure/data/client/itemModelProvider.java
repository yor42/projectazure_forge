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

        buildModel("rainbow_wisdomcube");
        buildModel("wisdomcube");
        buildModel("disc_fridaynight");
    }

    private ItemModelBuilder buildModel(String name){
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        return buildModel(itemGenerated, name);
    }

    private ItemModelBuilder buildModel(ModelFile model, String name){
        return getBuilder(name).parent(model).texture("layer0", "item/"+name);
    }
}
