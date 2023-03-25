package com.yor42.projectazure.client;

import com.yor42.projectazure.client.gui.container.*;
import com.yor42.projectazure.setup.register.RegisterContainer;
import net.minecraft.client.gui.screens.MenuScreens;

public class ClientRegisterManager {

    public static void registerScreen(){
        MenuScreens.register(RegisterContainer.SHIP_CONTAINER.get(), GuiALInventory::new);
        MenuScreens.register(RegisterContainer.RIGGING_INVENTORY.get(), guiRiggingInventory::new);
        MenuScreens.register(RegisterContainer.BA_CONTAINER.get(), guiBAInventory::new);
        MenuScreens.register(RegisterContainer.GFL_CONTAINER.get(), GuiGFLInventory::new);
        MenuScreens.register(RegisterContainer.CLS_CONTAINER.get(), GuiCLSInventory::new);
        MenuScreens.register(RegisterContainer.AKN_CONTAINER.get(), GuiAKNInventory::new);
        MenuScreens.register(RegisterContainer.FGO_CONTAINER.get(), GuiFGOInventory::new);
        MenuScreens.register(RegisterContainer.PCR_CONTAINER.get(), GuiPCRInventory::new);
        MenuScreens.register(RegisterContainer.SR_CONTAINER.get(), GuiSRInventory::new);
        MenuScreens.register(RegisterContainer.SW_CONTAINER.get(), GuiSWInventory::new);

        MenuScreens.register(RegisterContainer.METAL_PRESS_CONTAINER.get(), guiMetalPress::new);
        MenuScreens.register(RegisterContainer.ALLOY_FURNACE_CONTAINER.get(), guiAlloyFurnace::new);
        MenuScreens.register(RegisterContainer.RECRUIT_BEACON_CONTAINER.get(), guiRecruitBeacon::new);
        MenuScreens.register(RegisterContainer.BASIC_REFINERY_CONTAINER.get(), guiBasicRefinery::new);
        MenuScreens.register(RegisterContainer.GROWTH_CHAMBER_CONTAINER.get(), GuiCrystalGrowthChamber::new);
        MenuScreens.register(RegisterContainer.PANTRY_CONTAINER.get(), GuiPantryInventory::new);
        MenuScreens.register(RegisterContainer.BASICCCHEMICALREACTOR.get(), GuiBasicChemicalReactor::new);
    }
}
