package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.registerFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class FluidTagProvider extends FluidTagsProvider {
    public FluidTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(FluidTags.WATER).add(registerFluids.CRUDE_OIL_SOURCE).add(registerFluids.CRUDE_OIL_FLOWING).add(registerFluids.GASOLINE_SOURCE).add(registerFluids.GASOLINE_FLOWING).add(registerFluids.DIESEL_FLOWING).add(registerFluids.DIESEL_SOURCE);
        this.getOrCreateBuilder(ModTags.Fluids.DIESEL).add(registerFluids.DIESEL_SOURCE).add(registerFluids.DIESEL_FLOWING);
        this.getOrCreateBuilder(ModTags.Fluids.GASOLINE).add(registerFluids.GASOLINE_SOURCE).add(registerFluids.GASOLINE_FLOWING);
        this.getOrCreateBuilder(ModTags.Fluids.CRUDEOIL).add(registerFluids.CRUDE_OIL_SOURCE).add(registerFluids.CRUDE_OIL_FLOWING);
        this.getOrCreateBuilder(ModTags.Fluids.FUELOIL).add(registerFluids.FUEL_OIL_SOURCE).add(registerFluids.FUEL_OIL_FLOWING);
    }
}
