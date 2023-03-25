package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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

        channel.messageBuilder(SyncTeamListPacket.class,2)
                .decoder(SyncTeamListPacket::decode)
                .encoder(SyncTeamListPacket::encode)
                .consumer(SyncTeamListPacket::handle)
                .add();

        channel.messageBuilder(spawnParticlePacket.class, 3)
                .decoder(spawnParticlePacket::decode)
                .encoder(spawnParticlePacket::encode)
                .consumer(spawnParticlePacket::handle)
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

        channel.messageBuilder(CreateTeamPacket.class,10)
                .consumer(CreateTeamPacket::handle)
                .decoder(CreateTeamPacket::decode)
                .encoder(CreateTeamPacket::encode)
                .add();

        channel.messageBuilder(RemoveTeamPacket.class,11)
                .consumer(RemoveTeamPacket::handle)
                .decoder(RemoveTeamPacket::decode)
                .encoder(RemoveTeamPacket::encode)
                .add();

        channel.messageBuilder(EditTeamMemberPacket.class,12)
                .consumer(EditTeamMemberPacket::handle)
                .decoder(EditTeamMemberPacket::decode)
                .encoder(EditTeamMemberPacket::encode)
                .add();

        channel.messageBuilder(DeleteHomePacket.class,13)
                .consumer(DeleteHomePacket::handle)
                .decoder(DeleteHomePacket::decode)
                .encoder(DeleteHomePacket::encode)
                .add();

        channel.messageBuilder(SelectInitialSpawnSetPacket.class,14)
                .consumer(SelectInitialSpawnSetPacket::handle)
                .decoder(SelectInitialSpawnSetPacket::decode)
                .encoder(SelectInitialSpawnSetPacket::encode)
                .add();

        channel.messageBuilder(ChangeContainerPacket.class,15)
                .decoder(ChangeContainerPacket::decode)
                .encoder(ChangeContainerPacket::encode)
                .consumer(ChangeContainerPacket::handle)
                .add();

        channel.messageBuilder(ReopenEntityInventoryPacket.class,16)
                .consumer(ReopenEntityInventoryPacket::handle)
                .decoder(ReopenEntityInventoryPacket::decode)
                .encoder(ReopenEntityInventoryPacket::encode)
                .add();

        channel.messageBuilder(PlaySoundPacket.class,17)
                .consumer(PlaySoundPacket::handle)
                .decoder(PlaySoundPacket::decode)
                .encoder(PlaySoundPacket::encode)
                .add();

        return channel;
    }

}
