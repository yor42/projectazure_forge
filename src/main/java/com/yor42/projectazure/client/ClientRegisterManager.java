package com.yor42.projectazure.client;

import com.yor42.projectazure.client.gui.*;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screens.MenuScreens;

public class ClientRegisterManager {

    public static void registerScreen(){
        MenuScreens.register(registerManager.SHIP_CONTAINER.get(), GuiALInventory::new);
        ScreenManager.register(registerManager.RIGGING_INVENTORY.get(), guiRiggingInventory::new);
        ScreenManager.register(registerManager.BA_CONTAINER.get(), guiBAInventory::new);
        ScreenManager.register(registerManager.GFL_CONTAINER.get(), GuiGFLInventory::new);
        ScreenManager.register(registerManager.CLS_CONTAINER.get(), GuiCLSInventory::new);
        ScreenManager.register(registerManager.AKN_CONTAINER.get(), GuiAKNInventory::new);

        ScreenManager.register(registerManager.METAL_PRESS_CONTAINER.get(), guiMetalPress::new);
        ScreenManager.register(registerManager.ALLOY_FURNACE_CONTAINER.get(), guiAlloyFurnace::new);
        ScreenManager.register(registerManager.RECRUIT_BEACON_CONTAINER.get(), guiRecruitBeacon::new);
        ScreenManager.register(registerManager.DRYDOCK_CONTAINER.get(), guiDryDock::new);
        ScreenManager.register(registerManager.BASIC_REFINERY_CONTAINER.get(), guiBasicRefinery::new);
        ScreenManager.register(registerManager.GROWTH_CHAMBER_CONTAINER.get(), GuiCrystalGrowthChamber::new);
    }

}
