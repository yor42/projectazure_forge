package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.blocks.fluid.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class registerFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Constants.MODID);

    public static final FlowingFluid CRUDE_OIL_SOURCE = new CrudeOilFluid.Source();
    public static final RegistryObject<Fluid> CRUDE_OIL_SOURCE_REGISTRY = FLUIDS.register("crude_oil_source", ()->CRUDE_OIL_SOURCE);
    public static final FlowingFluid CRUDE_OIL_FLOWING = new CrudeOilFluid.Flowing();
    public static final RegistryObject<Fluid> CRUDE_OIL_FLOWING_REGISTRY = FLUIDS.register("crude_oil_flowing", ()->CRUDE_OIL_FLOWING);

    public static final FlowingFluid GASOLINE_SOURCE = new GasolineFluid.Source();
    public static final RegistryObject<Fluid> GASOLINE_SOURCE_REGISTRY = FLUIDS.register("gasoline_source", ()->GASOLINE_SOURCE);
    public static final FlowingFluid GASOLINE_FLOWING = new GasolineFluid.Flowing();
    public static final RegistryObject<Fluid> GASOLINE_FLOWING_REGISTRY = FLUIDS.register("gasoline_flowing", ()->GASOLINE_FLOWING);

    public static final FlowingFluid DIESEL_SOURCE = new DieselFluid.Source();
    public static final RegistryObject<Fluid> DIESEL_SOURCE_REGISTRY = FLUIDS.register("diesel_source", ()->DIESEL_SOURCE);
    public static final FlowingFluid DIESEL_FLOWING = new DieselFluid.Flowing();
    public static final RegistryObject<Fluid> DIESEL_FLOWING_REGISTRY = FLUIDS.register("diesel_flowing", ()->DIESEL_FLOWING);

    public static final FlowingFluid FUEL_OIL_SOURCE = new FuelOilFluid.Source();
    public static final RegistryObject<Fluid> FUEL_OIL_SOURCE_REGISTRY = FLUIDS.register("fuel_oil_source", ()->FUEL_OIL_SOURCE);
    public static final FlowingFluid FUEL_OIL_FLOWING = new FuelOilFluid.Flowing();
    public static final RegistryObject<Fluid> FUEL_OIL_FLOWING_REGISTRY = FLUIDS.register("fuel_oil_flowing", ()->FUEL_OIL_FLOWING);

    public static final Fluid ORIGINIUM_SOLUTION_SOURCE = new OriginiumSolutionFluid.Source();
    public static final Fluid ORIGINIUM_SOLUTION_FLOWING = new OriginiumSolutionFluid.Flowing();
    public static final RegistryObject<Fluid> ORIGINIUM_SOLUTION_REGISTRY = FLUIDS.register("originium_solution", () -> ORIGINIUM_SOLUTION_SOURCE);

    public static final Fluid NETHER_QUARTZ_SOLUTION_SOURCE = new NetherQuartzSolutionFluid.Source();
    public static final Fluid NETHER_QUARTZ_SOLUTION_FLOWING = new NetherQuartzSolutionFluid.Source();
    public static final RegistryObject<Fluid> NETHER_QUARTZ_SOLUTION_REGISTRY = FLUIDS.register("nether_quartz_solution", () -> NETHER_QUARTZ_SOLUTION_SOURCE);

    public static void register() {
    }
}
