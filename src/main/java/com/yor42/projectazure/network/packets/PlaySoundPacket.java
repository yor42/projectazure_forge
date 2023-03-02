package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.Supplier;

public class PlaySoundPacket {

    private final ResourceLocation sound;
    private final double x,y,z;


    public PlaySoundPacket(SoundEvent soundEvent, double x, double y, double z){
        this(soundEvent.getRegistryName(), x,y,z);
    }

    public PlaySoundPacket(ResourceLocation soundEvent, double x, double y, double z){
        this.sound = soundEvent;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static PlaySoundPacket decode (final PacketBuffer buffer){
        ResourceLocation soundEvent = buffer.readResourceLocation();
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new PlaySoundPacket(soundEvent, x,y,z);
    }

    public static void encode(final PlaySoundPacket msg, final PacketBuffer buffer){
        buffer.writeResourceLocation(msg.sound);
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
    }

    public static void handle(final PlaySoundPacket msg, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(msg.sound);
                if(sound!=null) {
                    ClientWorld world = Minecraft.getInstance().level;
                    world.playSound(Minecraft.getInstance().player, msg.x, msg.y, msg.z, sound, SoundCategory.NEUTRAL, 0.8F + (0.4F * MathUtil.rand.nextFloat()), 0.8F + (0.4F * MathUtil.rand.nextFloat()));
                }
            });
            ctx.get().setPacketHandled(true);
        });
    }

}
