package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.network.ClientEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class PlaySoundPacket {

    private final ResourceLocation sound;
    private final double x,y,z;
    private final float pitch, volume;
    private final SoundSource soundCategory;


    public PlaySoundPacket(SoundEvent soundEvent, SoundSource category, double x, double y, double z, float pitch, float volume){
        this(soundEvent.getRegistryName(),category, x,y,z,pitch,volume);
    }

    public PlaySoundPacket(ResourceLocation soundEvent, SoundSource category, double x, double y, double z, float pitch, float volume){
        this.sound = soundEvent;
        this.x = x;
        this.y = y;
        this.z = z;
        this.soundCategory = category;
        this.pitch = pitch;
        this.volume = volume;
    }

    public static PlaySoundPacket decode (final FriendlyByteBuf buffer){
        ResourceLocation soundEvent = buffer.readResourceLocation();
        SoundSource category = buffer.readEnum(SoundSource.class);
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        float pitch = buffer.readFloat();
        float volume = buffer.readFloat();
        return new PlaySoundPacket(soundEvent, category, x,y,z,pitch,volume);
    }

    public static void encode(final PlaySoundPacket msg, final FriendlyByteBuf buffer){
        buffer.writeResourceLocation(msg.sound);
        buffer.writeEnum(msg.soundCategory);
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeFloat(msg.pitch);
        buffer.writeFloat(msg.volume);
    }

    public static void handle(final PlaySoundPacket msg, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(msg.sound);
                if(sound!=null) {
                    ClientEvents.PlaySound(sound, msg.x, msg.y, msg.z, msg.soundCategory, msg.pitch, msg.volume);
                }
            });
            ctx.get().setPacketHandled(true);
        });
    }

}
