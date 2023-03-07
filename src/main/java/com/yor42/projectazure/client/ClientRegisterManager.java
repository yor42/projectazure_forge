package com.yor42.projectazure.client;

import com.yor42.projectazure.client.gui.container.*;
import com.yor42.projectazure.setup.register.RegisterContainer;
import net.minecraft.client.gui.ScreenManager;

public class ClientRegisterManager {

    public static void registerScreen(){
        ScreenManager.register(RegisterContainer.SHIP_CONTAINER.get(), GuiALInventory::new);
        ScreenManager.register(RegisterContainer.RIGGING_INVENTORY.get(), guiRiggingInventory::new);
        ScreenManager.register(RegisterContainer.BA_CONTAINER.get(), guiBAInventory::new);
        ScreenManager.register(RegisterContainer.GFL_CONTAINER.get(), GuiGFLInventory::new);
        ScreenManager.register(RegisterContainer.CLS_CONTAINER.get(), GuiCLSInventory::new);
        ScreenManager.register(RegisterContainer.AKN_CONTAINER.get(), GuiAKNInventory::new);
        ScreenManager.register(RegisterContainer.FGO_CONTAINER.get(), GuiFGOInventory::new);
        ScreenManager.register(RegisterContainer.PCR_CONTAINER.get(), GuiPCRInventory::new);
        ScreenManager.register(RegisterContainer.SR_CONTAINER.get(), GuiSRInventory::new);
        ScreenManager.register(RegisterContainer.SW_CONTAINER.get(), GuiSWInventory::new);

        ScreenManager.register(RegisterContainer.METAL_PRESS_CONTAINER.get(), guiMetalPress::new);
        ScreenManager.register(RegisterContainer.ALLOY_FURNACE_CONTAINER.get(), guiAlloyFurnace::new);
        ScreenManager.register(RegisterContainer.RECRUIT_BEACON_CONTAINER.get(), guiRecruitBeacon::new);
        ScreenManager.register(RegisterContainer.BASIC_REFINERY_CONTAINER.get(), guiBasicRefinery::new);
        ScreenManager.register(RegisterContainer.GROWTH_CHAMBER_CONTAINER.get(), GuiCrystalGrowthChamber::new);
        ScreenManager.register(RegisterContainer.PANTRY_CONTAINER.get(), GuiPantryInventory::new);
        ScreenManager.register(RegisterContainer.BASICCCHEMICALREACTOR.get(), GuiBasicChemicalReactor::new);
    }
}
