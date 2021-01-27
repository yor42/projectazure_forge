package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.network.packets.selectedStarterPacket;
import com.yor42.projectazure.network.packets.syncEntityInventoryPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class registerNetwork {

    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(defined.MODID, "network");
    public static final String NETWORK_VERSION = new ResourceLocation(defined.MODID, "1").toString();

    public static SimpleChannel getNetworkChannel() {
        final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> NETWORK_VERSION)
                .simpleChannel();

        channel.messageBuilder(selectedStarterPacket.class, 1)
                .decoder(selectedStarterPacket::decode)
                .encoder(selectedStarterPacket::encode)
                .consumer(selectedStarterPacket::handle)
                .add();

        channel.messageBuilder(syncEntityInventoryPacket.class, 1)
                .decoder(syncEntityInventoryPacket::decode)
                .encoder(syncEntityInventoryPacket::encode)
                .consumer(syncEntityInventoryPacket::handle)
                .add();

        return channel;
    }

}
