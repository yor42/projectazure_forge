package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.libs.utils.ParticleUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.swing.text.html.parser.Entity;
import java.util.function.Supplier;

public class spawnParticlePacket {

    private Entity entity;
    private int EntityID, particleID;
    private double PosX, PosY, PosZ, NormalizedLookX, NormalizedLookY, NormalizedLookZ;

    public spawnParticlePacket(LivingEntity entity, int particleID, double NormalizedLookX, double NormalizedLookY, double NormalizedLookZ){
        this.EntityID = entity.getEntityId();
        this.particleID = particleID;
        this.PosX = entity.getPosX();
        this.PosY = entity.getPosY();
        this.PosZ = entity.getPosZ();
        this.NormalizedLookX = NormalizedLookX;
        this.NormalizedLookY = NormalizedLookY;
        this.NormalizedLookZ = NormalizedLookZ;
    }
    public spawnParticlePacket(LivingEntity entity, int particleID, double posx, double posy, double posz, double NormalizedLookX, double NormalizedLookY, double NormalizedLookZ){
        this.EntityID = entity.getEntityId();
        this.particleID = particleID;
        this.PosX = posx;
        this.PosY = posy;
        this.PosZ = posz;
        this.NormalizedLookX = NormalizedLookX;
        this.NormalizedLookY = NormalizedLookY;
        this.NormalizedLookZ = NormalizedLookZ;
    }

    public spawnParticlePacket(int entityID, int particleID, double posx, double posy, double posz, double NormalizedLookX, double NormalizedLookY, double NormalizedLookZ){
        this.EntityID = entityID;
        this.particleID = particleID;
        this.PosX = posx;
        this.PosY = posy;
        this.PosZ = posz;
        this.NormalizedLookX = NormalizedLookX;
        this.NormalizedLookY = NormalizedLookY;
        this.NormalizedLookZ = NormalizedLookZ;
    }

    public static spawnParticlePacket decode(PacketBuffer buffer){
        final int EntityID = buffer.readInt();
        final int ParticleID = buffer.readInt();
        final double posX = buffer.readDouble();
        final double posY = buffer.readDouble();
        final double posZ = buffer.readDouble();
        final double NormalizedLookX = buffer.readDouble();
        final double NormalizedLookY = buffer.readDouble();
        final double NormalizedLookZ = buffer.readDouble();

        return new spawnParticlePacket(EntityID, ParticleID, posX, posY, posZ, NormalizedLookX, NormalizedLookY, NormalizedLookZ);
    }

    public static void encode(final spawnParticlePacket message, final PacketBuffer buffer){
        buffer.writeInt(message.EntityID);
        buffer.writeInt(message.particleID);
        buffer.writeDouble(message.PosX);
        buffer.writeDouble(message.PosY);
        buffer.writeDouble(message.PosZ);
        buffer.writeDouble(message.NormalizedLookX);
        buffer.writeDouble(message.NormalizedLookY);
        buffer.writeDouble(message.NormalizedLookZ);
    }

    public static void handle(final spawnParticlePacket message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {

            switch (message.particleID){
                case defined.PARTICLE_CANNON_FIRE_ID:{
                    ParticleUtils.spawnCannonParticleModerate(message.PosX, message.PosY, message.PosZ, message.NormalizedLookX, message.NormalizedLookY);
                    break;
                }
            }

        }));
        ctx.get().setPacketHandled(true);
    }
}
