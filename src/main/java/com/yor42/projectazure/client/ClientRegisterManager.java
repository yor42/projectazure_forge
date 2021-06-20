package com.yor42.projectazure.client;

import com.yor42.projectazure.client.gui.*;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.gui.ScreenManager;

public class ClientRegisterManager {

    public static void registerScreen(){
        ScreenManager.registerFactory(registerManager.SHIP_CONTAINER.get(), guiShipInventory::new);
        ScreenManager.registerFactory(registerManager.RIGGING_INVENTORY.get(), guiRiggingInventory::new);
        ScreenManager.registerFactory(registerManager.BA_CONTAINER.get(), guiBAInventory::new);
        ScreenManager.registerFactory(registerManager.AKN_CONTAINER.get(), GuiAKNInventory::new);

        ScreenManager.registerFactory(registerManager.METAL_PRESS_CONTAINER.get(), guiMetalPress::new);
        ScreenManager.registerFactory(registerManager.ALLOY_FURNACE_CONTAINER.get(), guiAlloyFurnace::new);
        ScreenManager.registerFactory(registerManager.RECRUIT_BEACON_CONTAINER.get(), guiRecruitBeacon::new);
    }

}
