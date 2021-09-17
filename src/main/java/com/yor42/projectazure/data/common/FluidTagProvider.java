package com.yor42.projectazure.data.common;

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
        this.getOrCreateBuilder(FluidTags.WATER).add(registerFluids.CRUDE_OIL_SOURCE);
        this.getOrCreateBuilder(FluidTags.WATER).add(registerFluids.CRUDE_OIL_FLOWING);
    }
}
