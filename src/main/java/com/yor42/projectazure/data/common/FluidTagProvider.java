package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class FluidTagProvider extends FluidTagsProvider {
    public FluidTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.Fluids.DIESEL).add(RegisterFluids.DIESEL_SOURCE_REGISTRY.get()).add(RegisterFluids.DIESEL_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.GASOLINE).add(RegisterFluids.GASOLINE_SOURCE_REGISTRY.get()).add(RegisterFluids.GASOLINE_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.CRUDEOIL).add(RegisterFluids.CRUDE_OIL_SOURCE_REGISTRY.get()).add(RegisterFluids.CRUDE_OIL_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.FUELOIL).add(RegisterFluids.FUEL_OIL_SOURCE_REGISTRY.get()).add(RegisterFluids.FUEL_OIL_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.KETON).add(RegisterFluids.KETON_SOURCE_REGISTRY.get()).add(RegisterFluids.KETON_FLOWING_REGISTRY.get());
    }
}
