package com.yor42.projectazure.libs.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class ModifiedClickData {

    public final int button;
    public final boolean isShiftClick;
    public final boolean isCtrlClick;
    public final boolean isRemote;
    public final UUID clickedUserUUID;

    private ModifiedClickData(int button, boolean isShiftClick, boolean isCtrlClick, boolean isRemote, UUID clickedUserUUID) {
        this.button = button;
        this.isShiftClick = isShiftClick;
        this.isCtrlClick = isCtrlClick;
        this.isRemote = isRemote;
        this.clickedUserUUID = clickedUserUUID;
    }

    @OnlyIn(Dist.CLIENT)
    public ModifiedClickData() {
        MouseHandler mouseHelper = Minecraft.getInstance().mouseHandler;
        long id = Minecraft.getInstance().getWindow().getWindow();
        this.button = mouseHelper.isLeftPressed() ? 0 : mouseHelper.isRightPressed() ? 1 : 2;
        this.isShiftClick = InputConstants.isKeyDown(id, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(id, GLFW.GLFW_KEY_LEFT_SHIFT);
        this.isCtrlClick = InputConstants.isKeyDown(id, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(id, GLFW.GLFW_KEY_RIGHT_CONTROL);
        this.isRemote = true;
        this.clickedUserUUID = Minecraft.getInstance().player.getUUID();
    }

    @OnlyIn(Dist.CLIENT)
    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeVarInt(button);
        buf.writeBoolean(isShiftClick);
        buf.writeBoolean(isCtrlClick);
        buf.writeUUID(clickedUserUUID);
    }

    public static ModifiedClickData readFromBuf(FriendlyByteBuf buf) {
        int button = buf.readVarInt();
        boolean shiftClick = buf.readBoolean();
        boolean ctrlClick = buf.readBoolean();
        UUID playerid = buf.readUUID();
        return new ModifiedClickData(button, shiftClick, ctrlClick, false, playerid);
    }
}
