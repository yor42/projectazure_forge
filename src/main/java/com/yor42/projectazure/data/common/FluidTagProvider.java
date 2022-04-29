package com.yor42.projectazure.data.common;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class FluidTagProvider extends FluidTagsProvider {
    public FluidTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.Fluids.DIESEL).add(Main.DIESEL_REGISTRY.get()).add(Main.DIESEL_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.GASOLINE).add(Main.GASOLINE_REGISTRY.get()).add(Main.GASOLINE_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.CRUDEOIL).add(Main.CRUDE_OIL_REGISTRY.get()).add(Main.CRUDE_OIL_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.FUELOIL).add(Main.FUEL_OIL_REGISTRY.get()).add(Main.FUEL_OIL_FLOWING_REGISTRY.get());
    }
}
