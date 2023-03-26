package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.containers.entity.*;
import com.yor42.projectazure.gameobject.containers.machine.*;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MODID);
    public static final RegistryObject<MenuType<ContainerPantry>> PANTRY_CONTAINER = CONTAINER.register("pantry_container", () -> IForgeContainerType.create(ContainerPantry::new));
    public static final RegistryObject<MenuType<ContainerCrystalGrowthChamber>> GROWTH_CHAMBER_CONTAINER = CONTAINER.register("crystal_growth_chamber_container", () -> IForgeContainerType.create(ContainerCrystalGrowthChamber::new));
    public static final RegistryObject<MenuType<ContainerBasicChemicalReactor>> BASICCCHEMICALREACTOR = CONTAINER.register("basic_chemical_reactor_container", () -> IForgeContainerType.create(ContainerBasicChemicalReactor::new));
    public static final RegistryObject<MenuType<ContainerBasicRefinery>> BASIC_REFINERY_CONTAINER = CONTAINER.register("basic_refinery_container", () -> IForgeContainerType.create(ContainerBasicRefinery::new));
    public static final RegistryObject<MenuType<ContainerRecruitBeacon>> RECRUIT_BEACON_CONTAINER = CONTAINER.register("recruit_beacon_container", () -> IForgeContainerType.create(ContainerRecruitBeacon::new));
    public static final RegistryObject<MenuType<ContainerAlloyFurnace>> ALLOY_FURNACE_CONTAINER = CONTAINER.register("alloy_furnace_container", () -> IForgeContainerType.create(ContainerAlloyFurnace::new));
    public static final RegistryObject<MenuType<ContainerMetalPress>> METAL_PRESS_CONTAINER = CONTAINER.register("metal_press_container", () -> IForgeContainerType.create(ContainerMetalPress::new));
    public static final RegistryObject<MenuType<ContainerCLSInventory>> CLS_CONTAINER = CONTAINER.register("closers_inventory", () -> new MenuType<>((IContainerFactory<ContainerCLSInventory>)ContainerCLSInventory::new));
    public static final RegistryObject<MenuType<ContainerSWInventory>> SW_CONTAINER = CONTAINER.register("soulworker_inventory", () -> new MenuType<>((IContainerFactory<ContainerSWInventory>) ContainerSWInventory::new));

    public static final RegistryObject<MenuType<ContainerGFLInventory>> GFL_CONTAINER = CONTAINER.register("girlsfrontline_inventory", () -> new MenuType<>((IContainerFactory<ContainerGFLInventory>)ContainerGFLInventory::new));
    public static final RegistryObject<MenuType<ContainerAKNInventory>> AKN_CONTAINER = CONTAINER.register("arknights_inventory", () -> new MenuType<>((IContainerFactory<ContainerAKNInventory>)ContainerAKNInventory::new));
    public static final RegistryObject<MenuType<ContainerFGOInventory>> FGO_CONTAINER = CONTAINER.register("fategrandorder_inventory", () -> new MenuType<>((IContainerFactory<ContainerFGOInventory>)ContainerFGOInventory::new));
    public static final RegistryObject<MenuType<ContainerPCRInventory>> PCR_CONTAINER = CONTAINER.register("priconne_inventory", () -> new MenuType<>((IContainerFactory<ContainerPCRInventory>)ContainerPCRInventory::new));
    public static final RegistryObject<MenuType<ContainerSRInventory>> SR_CONTAINER = CONTAINER.register("shiningresonance_inventory", () -> new MenuType<>((IContainerFactory<ContainerSRInventory>) ContainerSRInventory::new));
    public static final RegistryObject<MenuType<ContainerBAInventory>> BA_CONTAINER = CONTAINER.register("bluearchive_inventory", () -> new MenuType<>((IContainerFactory<ContainerBAInventory>)ContainerBAInventory::new));
    public static final RegistryObject<MenuType<RiggingContainer>> RIGGING_INVENTORY = CONTAINER.register("rigging_inventory", () -> new MenuType<>((IContainerFactory<RiggingContainer>)RiggingContainer::new));
    //Container
    public static final RegistryObject<MenuType<ContainerALInventory>> SHIP_CONTAINER = CONTAINER.register("kansen_inventory", () -> new MenuType<>((IContainerFactory<ContainerALInventory>) ContainerALInventory::new));
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Constants.MODID);

    public static void register() {
    }
}
