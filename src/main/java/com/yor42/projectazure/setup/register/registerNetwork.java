package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class registerNetwork {

    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(Constants.MODID, "network");
    public static final String NETWORK_VERSION = new ResourceLocation(Constants.MODID, "5").toString();

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

        channel.messageBuilder(EntityInteractionPacket.class,7)
                .decoder(EntityInteractionPacket::decode)
                .encoder(EntityInteractionPacket::encode)
                .consumer(EntityInteractionPacket::handle)
                .add();

        channel.messageBuilder(EditEntityValuePacket.class,8)
                .decoder(EditEntityValuePacket::decode)
                .encoder(EditEntityValuePacket::encode)
                .consumer(EditEntityValuePacket::handle)
                .add();

        channel.messageBuilder(TeamNameChangedPacket.class,9)
                .decoder(TeamNameChangedPacket::decode)
                .encoder(TeamNameChangedPacket::encode)
                .consumer(TeamNameChangedPacket::handle)
                .add();

        channel.messageBuilder(SyncTeamListPacket.class,10)
                .decoder(SyncTeamListPacket::decode)
                .encoder(SyncTeamListPacket::encode)
                .consumer(SyncTeamListPacket::handle)
                .add();

        return channel;
    }

}
