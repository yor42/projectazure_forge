package com.yor42.projectazure.client;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.gui.*;
import net.minecraft.client.gui.screens.MenuScreens;

public class ClientRegisterManager {

    public static void registerScreen(){
        MenuScreens.register(Main.SHIP_CONTAINER.get(), GuiALInventory::new);
        MenuScreens.register(Main.RIGGING_INVENTORY.get(), guiRiggingInventory::new);
        MenuScreens.register(Main.BA_CONTAINER.get(), guiBAInventory::new);
        MenuScreens.register(Main.GFL_CONTAINER.get(), GuiGFLInventory::new);
        MenuScreens.register(Main.CLS_CONTAINER.get(), GuiCLSInventory::new);
        MenuScreens.register(Main.AKN_CONTAINER.get(), GuiAKNInventory::new);

        MenuScreens.register(Main.METAL_PRESS_CONTAINER.get(), guiMetalPress::new);
        MenuScreens.register(Main.ALLOY_FURNACE_CONTAINER.get(), guiAlloyFurnace::new);
        MenuScreens.register(Main.RECRUIT_BEACON_CONTAINER.get(), guiRecruitBeacon::new);
        MenuScreens.register(Main.DRYDOCK_CONTAINER.get(), guiDryDock::new);
        MenuScreens.register(Main.BASIC_REFINERY_CONTAINER.get(), guiBasicRefinery::new);
        MenuScreens.register(Main.GROWTH_CHAMBER_CONTAINER.get(), GuiCrystalGrowthChamber::new);
    }

}
