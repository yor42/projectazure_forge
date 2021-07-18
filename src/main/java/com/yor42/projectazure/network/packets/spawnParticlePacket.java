package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.libs.utils.ParticleUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import javax.swing.text.html.parser.Entity;
import java.util.function.Supplier;

public class spawnParticlePacket {

    private final int EntityID;
    private final Particles particleID;
    private final double PosX;
    private final double PosY;
    private final double PosZ;
    private final double NormalizedLookX;
    private final double NormalizedLookY;
    private final double NormalizedLookZ;

    @Nullable
    private final ItemStack stack;

    public spawnParticlePacket(LivingEntity entity, @Nullable ItemStack particleStack){
        this.EntityID = entity.getEntityId();
        this.particleID = Particles.ITEM_PARTICLE;
        this.PosX = entity.getPosX();
        this.PosY = entity.getPosY();
        this.PosZ = entity.getPosZ();
        this.stack = particleStack;
        Vector3d vector3d = entity.getLook(1.0F);
        this.NormalizedLookX = vector3d.getX();
        this.NormalizedLookY = vector3d.getY();
        this.NormalizedLookZ = vector3d.getZ();
    }

    public spawnParticlePacket(LivingEntity entity, Particles particleID){
        this.EntityID = entity.getEntityId();
        this.particleID = particleID;
        this.PosX = entity.getPosX();
        this.PosY = entity.getPosY();
        this.PosZ = entity.getPosZ();
        this.stack = null;
        Vector3d vector3d = entity.getLook(1.0F);
        this.NormalizedLookX = vector3d.getX();
        this.NormalizedLookY = vector3d.getY();
        this.NormalizedLookZ = vector3d.getZ();
    }

    public spawnParticlePacket(LivingEntity entity, Particles particleID, double NormalizedLookX, double NormalizedLookY, double NormalizedLookZ){
        this.EntityID = entity.getEntityId();
        this.particleID = particleID;
        this.PosX = entity.getPosX();
        this.PosY = entity.getPosY();
        this.PosZ = entity.getPosZ();
        this.stack = null;
        this.NormalizedLookX = NormalizedLookX;
        this.NormalizedLookY = NormalizedLookY;
        this.NormalizedLookZ = NormalizedLookZ;
    }
    public spawnParticlePacket(LivingEntity entity, Particles particleID, double posx, double posy, double posz, double NormalizedLookX, double NormalizedLookY, double NormalizedLookZ){
        this.EntityID = entity.getEntityId();
        this.particleID = particleID;
        this.PosX = posx;
        this.PosY = posy;
        this.PosZ = posz;
        this.stack = null;
        this.NormalizedLookX = NormalizedLookX;
        this.NormalizedLookY = NormalizedLookY;
        this.NormalizedLookZ = NormalizedLookZ;
    }

    public spawnParticlePacket(int entityID, Particles particleID, double posx, double posy, double posz, double NormalizedLookX, double NormalizedLookY, double NormalizedLookZ){
        this.EntityID = entityID;
        this.particleID = particleID;
        this.PosX = posx;
        this.PosY = posy;
        this.PosZ = posz;
        this.stack = null;
        this.NormalizedLookX = NormalizedLookX;
        this.NormalizedLookY = NormalizedLookY;
        this.NormalizedLookZ = NormalizedLookZ;
    }

    public spawnParticlePacket(int entityID, Particles particleID, double posx, double posy, double posz, ItemStack stack, double NormalizedLookX, double NormalizedLookY, double NormalizedLookZ){
        this.EntityID = entityID;
        this.particleID = particleID;
        this.PosX = posx;
        this.PosY = posy;
        this.PosZ = posz;
        this.stack = stack == ItemStack.EMPTY? null : stack;
        this.NormalizedLookX = NormalizedLookX;
        this.NormalizedLookY = NormalizedLookY;
        this.NormalizedLookZ = NormalizedLookZ;
    }

    public static spawnParticlePacket decode(PacketBuffer buffer){
        final int EntityID = buffer.readInt();
        final Particles ParticleID = buffer.readEnumValue(Particles.class);
        final double posX = buffer.readDouble();
        final double posY = buffer.readDouble();
        final double posZ = buffer.readDouble();
        final ItemStack itemStack = buffer.readItemStack();
        final double NormalizedLookX = buffer.readDouble();
        final double NormalizedLookY = buffer.readDouble();
        final double NormalizedLookZ = buffer.readDouble();

        return new spawnParticlePacket(EntityID, ParticleID, posX, posY, posZ, itemStack, NormalizedLookX, NormalizedLookY, NormalizedLookZ);
    }

    public static void encode(final spawnParticlePacket message, final PacketBuffer buffer){
        buffer.writeInt(message.EntityID);
        buffer.writeEnumValue(message.particleID);
        buffer.writeDouble(message.PosX);
        buffer.writeDouble(message.PosY);
        buffer.writeDouble(message.PosZ);
        buffer.writeItemStack(message.stack == null? ItemStack.EMPTY: message.stack);
        buffer.writeDouble(message.NormalizedLookX);
        buffer.writeDouble(message.NormalizedLookY);
        buffer.writeDouble(message.NormalizedLookZ);
    }

    public static void handle(final spawnParticlePacket message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            switch (message.particleID){
                case CANNON_SMOKE:{
                    ParticleUtils.spawnCannonParticleModerate(message.PosX, message.PosY, message.PosZ, message.NormalizedLookX, message.NormalizedLookY);
                    break;
                }
                case AFFECTION_HEART:{
                    ParticleUtils.spawnPatHeartParticle(message.EntityID);
                    break;
                }
                case AFFECTION_SMOKE:{
                    ParticleUtils.spawnPatSmokeParticle(message.EntityID);
                    break;
                }
                case ITEM_PARTICLE:
                {
                    if(message.stack != null){
                        ParticleUtils.SpawnItemParticles(message.EntityID, message.stack);
                    }
                }
            }

        }));
        ctx.get().setPacketHandled(true);
    }

    public enum Particles{
        CANNON_SMOKE,
        AFFECTION_HEART,
        AFFECTION_SMOKE,
        ITEM_PARTICLE
    }

}
