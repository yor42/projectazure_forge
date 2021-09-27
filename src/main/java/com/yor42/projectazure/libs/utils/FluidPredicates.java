package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.setup.register.registerFluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public class FluidPredicates {

    public static final Predicate<FluidStack> CrudeOilPredicate = (fluidStack)-> fluidStack.getFluid() == registerFluids.CRUDE_OIL_SOURCE;
    public static final Predicate<FluidStack> GasolinePredicate = (fluidStack)-> fluidStack.getFluid() == registerFluids.GASOLINE_SOURCE;
    public static final Predicate<FluidStack> DieselPredicate = (fluidStack)-> fluidStack.getFluid() == registerFluids.DIESEL_SOURCE;
    public static final Predicate<FluidStack> FuelOilPredicate = (fluidStack)-> fluidStack.getFluid() == registerFluids.FUEL_OIL_SOURCE;

}
