package com.yor42.projectazure.client;

import com.yor42.projectazure.client.gui.*;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.gui.screens.MenuScreens;

public class ClientRegisterManager {

    public static void registerScreen(){
        MenuScreens.register(registerManager.SHIP_CONTAINER.get(), GuiALInventory::new);
        MenuScreens.register(registerManager.RIGGING_INVENTORY.get(), guiRiggingInventory::new);
        MenuScreens.register(registerManager.BA_CONTAINER.get(), guiBAInventory::new);
        MenuScreens.register(registerManager.GFL_CONTAINER.get(), GuiGFLInventory::new);
        MenuScreens.register(registerManager.CLS_CONTAINER.get(), GuiCLSInventory::new);
        MenuScreens.register(registerManager.AKN_CONTAINER.get(), GuiAKNInventory::new);

        MenuScreens.register(registerManager.METAL_PRESS_CONTAINER.get(), guiMetalPress::new);
        MenuScreens.register(registerManager.ALLOY_FURNACE_CONTAINER.get(), guiAlloyFurnace::new);
        MenuScreens.register(registerManager.RECRUIT_BEACON_CONTAINER.get(), guiRecruitBeacon::new);
        MenuScreens.register(registerManager.DRYDOCK_CONTAINER.get(), guiDryDock::new);
        MenuScreens.register(registerManager.BASIC_REFINERY_CONTAINER.get(), guiBasicRefinery::new);
        MenuScreens.register(registerManager.GROWTH_CHAMBER_CONTAINER.get(), GuiCrystalGrowthChamber::new);
    }

}
