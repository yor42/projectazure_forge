package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.network.packets.*;
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

        channel.messageBuilder(syncRiggingInventoryPacket.class, 2)
                .decoder(syncRiggingInventoryPacket::decode)
                .encoder(syncRiggingInventoryPacket::encode)
                .consumer(syncRiggingInventoryPacket::handle)
                .add();

        channel.messageBuilder(spawnParticlePacket.class, 3)
                .decoder(spawnParticlePacket::decode)
                .encoder(spawnParticlePacket::encode)
                .consumer(spawnParticlePacket::handle)
                .add();

        channel.messageBuilder(GunFiredPacket.class,4)
                .decoder(GunFiredPacket::decode)
                .encoder(GunFiredPacket::encode)
                .consumer(GunFiredPacket::handle)
                .add();

        channel.messageBuilder(DoGunAnimationPacket.class,5)
                .decoder(DoGunAnimationPacket::decode)
                .encoder(DoGunAnimationPacket::encode)
                .consumer(DoGunAnimationPacket::handle)
                .add();

        channel.messageBuilder(StartRecruitPacket.class,6)
                .decoder(StartRecruitPacket::decode)
                .encoder(StartRecruitPacket::encode)
                .consumer(StartRecruitPacket::handle)
                .add();

        return channel;
    }

}
