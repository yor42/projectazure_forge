package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.containers.entity.*;
import com.yor42.projectazure.gameobject.containers.machine.*;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegisterContainer {
    public static final DeferredRegister<ContainerType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MODID);
    public static final RegistryObject<ContainerType<ContainerPantry>> PANTRY_CONTAINER = CONTAINER.register("pantry_container", () -> IForgeContainerType.create(ContainerPantry::new));
    public static final RegistryObject<ContainerType<ContainerCrystalGrowthChamber>> GROWTH_CHAMBER_CONTAINER = CONTAINER.register("crystal_growth_chamber_container", () -> IForgeContainerType.create(ContainerCrystalGrowthChamber::new));
    public static final RegistryObject<ContainerType<ContainerBasicChemicalReactor>> BASICCCHEMICALREACTOR = CONTAINER.register("basic_chemical_reactor_container", () -> IForgeContainerType.create(ContainerBasicChemicalReactor::new));
    public static final RegistryObject<ContainerType<ContainerBasicRefinery>> BASIC_REFINERY_CONTAINER = CONTAINER.register("basic_refinery_container", () -> IForgeContainerType.create(ContainerBasicRefinery::new));
    public static final RegistryObject<ContainerType<ContainerRecruitBeacon>> RECRUIT_BEACON_CONTAINER = CONTAINER.register("recruit_beacon_container", () -> IForgeContainerType.create(ContainerRecruitBeacon::new));
    public static final RegistryObject<ContainerType<ContainerAlloyFurnace>> ALLOY_FURNACE_CONTAINER = CONTAINER.register("alloy_furnace_container", () -> IForgeContainerType.create(ContainerAlloyFurnace::new));
    public static final RegistryObject<ContainerType<ContainerMetalPress>> METAL_PRESS_CONTAINER = CONTAINER.register("metal_press_container", () -> IForgeContainerType.create(ContainerMetalPress::new));
    public static final RegistryObject<ContainerType<ContainerCLSInventory>> CLS_CONTAINER = CONTAINER.register("closers_inventory", () -> new ContainerType<>((IContainerFactory<ContainerCLSInventory>)ContainerCLSInventory::new));
    public static final RegistryObject<ContainerType<ContainerSoulworkerInventory>> SW_CONTAINER = CONTAINER.register("soulworker_inventory", () -> new ContainerType<>((IContainerFactory<ContainerSoulworkerInventory>)ContainerSoulworkerInventory::new));

    public static final RegistryObject<ContainerType<ContainerGFLInventory>> GFL_CONTAINER = CONTAINER.register("girlsfrontline_inventory", () -> new ContainerType<>((IContainerFactory<ContainerGFLInventory>)ContainerGFLInventory::new));
    public static final RegistryObject<ContainerType<ContainerAKNInventory>> AKN_CONTAINER = CONTAINER.register("arknights_inventory", () -> new ContainerType<>((IContainerFactory<ContainerAKNInventory>)ContainerAKNInventory::new));
    public static final RegistryObject<ContainerType<ContainerFGOInventory>> FGO_CONTAINER = CONTAINER.register("fategrandorder_inventory", () -> new ContainerType<>((IContainerFactory<ContainerFGOInventory>)ContainerFGOInventory::new));
    public static final RegistryObject<ContainerType<ContainerPCRInventory>> PCR_CONTAINER = CONTAINER.register("priconne_inventory", () -> new ContainerType<>((IContainerFactory<ContainerPCRInventory>)ContainerPCRInventory::new));
    public static final RegistryObject<ContainerType<ContainerShiningResonanceInventory>> SR_CONTAINER = CONTAINER.register("shiningresonance_inventory", () -> new ContainerType<>((IContainerFactory<ContainerShiningResonanceInventory>)ContainerShiningResonanceInventory::new));
    public static final RegistryObject<ContainerType<ContainerBAInventory>> BA_CONTAINER = CONTAINER.register("bluearchive_inventory", () -> new ContainerType<>((IContainerFactory<ContainerBAInventory>)ContainerBAInventory::new));
    public static final RegistryObject<ContainerType<RiggingContainer>> RIGGING_INVENTORY = CONTAINER.register("rigging_inventory", () -> new ContainerType<>((IContainerFactory<RiggingContainer>)RiggingContainer::new));
    //Container
    public static final RegistryObject<ContainerType<ContainerALInventory>> SHIP_CONTAINER = CONTAINER.register("kansen_inventory", () -> new ContainerType<>((IContainerFactory<ContainerALInventory>) ContainerALInventory::new));
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Constants.MODID);

    public static void register() {
    }
}
