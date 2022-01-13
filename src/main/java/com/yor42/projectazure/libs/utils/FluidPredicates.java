package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.data.ModTags;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

import static com.yor42.projectazure.data.ModTags.Fluids.FUELOIL;

public class FluidPredicates {

    public static final Predicate<FluidStack> CrudeOilPredicate = (fluidStack)-> fluidStack.getFluid().is(ModTags.Fluids.CRUDEOIL);
    public static final Predicate<FluidStack> GasolinePredicate = (fluidStack)-> fluidStack.getFluid().is(ModTags.Fluids.GASOLINE);
    public static final Predicate<FluidStack> DieselPredicate = (fluidStack)-> fluidStack.getFluid().is(ModTags.Fluids.DIESEL);
    public static final Predicate<FluidStack> FuelOilPredicate = (fluidStack)-> fluidStack.getFluid().is(FUELOIL);

}
