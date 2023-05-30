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

        int id = 0;

        channel.messageBuilder(selectedStarterPacket.class, id++)
                .decoder(selectedStarterPacket::decode)
                .encoder(selectedStarterPacket::encode)
                .consumer(selectedStarterPacket::handle)
                .add();

        channel.messageBuilder(SyncTeamListPacket.class, id++)
                .decoder(SyncTeamListPacket::decode)
                .encoder(SyncTeamListPacket::encode)
                .consumer(SyncTeamListPacket::handle)
                .add();

        channel.messageBuilder(StartRecruitPacket.class, id++)
                .decoder(StartRecruitPacket::decode)
                .encoder(StartRecruitPacket::encode)
                .consumer(StartRecruitPacket::handle)
                .add();

        channel.messageBuilder(EntityInteractionPacket.class, id++)
                .decoder(EntityInteractionPacket::decode)
                .encoder(EntityInteractionPacket::encode)
                .consumer(EntityInteractionPacket::handle)
                .add();

        channel.messageBuilder(EditEntityValuePacket.class, id++)
                .decoder(EditEntityValuePacket::decode)
                .encoder(EditEntityValuePacket::encode)
                .consumer(EditEntityValuePacket::handle)
                .add();

        channel.messageBuilder(TeamNameChangedPacket.class, id++)
                .decoder(TeamNameChangedPacket::decode)
                .encoder(TeamNameChangedPacket::encode)
                .consumer(TeamNameChangedPacket::handle)
                .add();

        channel.messageBuilder(CreateTeamPacket.class, id++)
                .consumer(CreateTeamPacket::handle)
                .decoder(CreateTeamPacket::decode)
                .encoder(CreateTeamPacket::encode)
                .add();

        channel.messageBuilder(RemoveTeamPacket.class, id++)
                .consumer(RemoveTeamPacket::handle)
                .decoder(RemoveTeamPacket::decode)
                .encoder(RemoveTeamPacket::encode)
                .add();

        channel.messageBuilder(EditTeamMemberPacket.class, id++)
                .consumer(EditTeamMemberPacket::handle)
                .decoder(EditTeamMemberPacket::decode)
                .encoder(EditTeamMemberPacket::encode)
                .add();

        channel.messageBuilder(DeleteHomePacket.class, id++)
                .consumer(DeleteHomePacket::handle)
                .decoder(DeleteHomePacket::decode)
                .encoder(DeleteHomePacket::encode)
                .add();

        channel.messageBuilder(SelectInitialSpawnSetPacket.class, id++)
                .consumer(SelectInitialSpawnSetPacket::handle)
                .decoder(SelectInitialSpawnSetPacket::decode)
                .encoder(SelectInitialSpawnSetPacket::encode)
                .add();

        channel.messageBuilder(ChangeContainerPacket.class, id++)
                .decoder(ChangeContainerPacket::decode)
                .encoder(ChangeContainerPacket::encode)
                .consumer(ChangeContainerPacket::handle)
                .add();

        channel.messageBuilder(ReopenEntityInventoryPacket.class, id++)
                .consumer(ReopenEntityInventoryPacket::handle)
                .decoder(ReopenEntityInventoryPacket::decode)
                .encoder(ReopenEntityInventoryPacket::encode)
                .add();

        channel.messageBuilder(PlaySoundPacket.class, id++)
                .consumer(PlaySoundPacket::handle)
                .decoder(PlaySoundPacket::decode)
                .encoder(PlaySoundPacket::encode)
                .add();

        return channel;
    }
}
