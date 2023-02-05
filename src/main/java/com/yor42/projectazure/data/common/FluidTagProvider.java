package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.registerFluids;
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
        this.tag(ModTags.Fluids.DIESEL).add(registerFluids.DIESEL_SOURCE_REGISTRY.get()).add(registerFluids.DIESEL_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.GASOLINE).add(registerFluids.GASOLINE_SOURCE_REGISTRY.get()).add(registerFluids.GASOLINE_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.CRUDEOIL).add(registerFluids.CRUDE_OIL_SOURCE_REGISTRY.get()).add(registerFluids.CRUDE_OIL_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.FUELOIL).add(registerFluids.FUEL_OIL_SOURCE_REGISTRY.get()).add(registerFluids.FUEL_OIL_FLOWING_REGISTRY.get());
        this.tag(ModTags.Fluids.KETON).add(registerFluids.KETON_SOURCE_REGISTRY.get()).add(registerFluids.KETON_FLOWING_REGISTRY.get());
    }
}
