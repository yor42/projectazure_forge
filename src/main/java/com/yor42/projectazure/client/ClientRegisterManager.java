package com.yor42.projectazure.client;

import com.yor42.projectazure.client.gui.guiBAInventory;
import com.yor42.projectazure.client.gui.guiRiggingInventory;
import com.yor42.projectazure.client.gui.guiShipInventory;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.gui.ScreenManager;

public class ClientRegisterManager {

    public static void registerScreen(){
        ScreenManager.registerFactory(registerManager.SHIP_CONTAINER.get(), guiShipInventory::new);
        ScreenManager.registerFactory(registerManager.RIGGING_INVENTORY.get(), guiRiggingInventory::new);
        ScreenManager.registerFactory(registerManager.BA_CONTAINER.get(), guiBAInventory::new);
    }

}
