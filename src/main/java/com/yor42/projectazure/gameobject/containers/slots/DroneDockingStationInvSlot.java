package com.yor42.projectazure.gameobject.containers.slots;

import com.yor42.projectazure.gameobject.containers.machine.ContainerDroneDockingStation;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DroneDockingStationInvSlot extends SlotItemHandler {

    private final ContainerDroneDockingStation screen;
    private final ContainerDroneDockingStation.ScreenMode screenmodetodisplay;

    public DroneDockingStationInvSlot(ContainerDroneDockingStation screen, ContainerDroneDockingStation.ScreenMode screenMode, ItemStackHandler p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        this.screen = screen;
        this.screenmodetodisplay = screenMode;
    }

    @Override
    public boolean isActive() {
        return this.screen.getScreenMode() == this.screenmodetodisplay;
    }
}