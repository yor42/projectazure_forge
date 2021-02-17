package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.libs.utils.ParticleUtils;
import com.yor42.projectazure.libs.utils.ProjectileUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncProjectilePacket {

    private double x,y,z,AccelX,AccelY,AccelZ;
    private final int EntityID, TypeID;


    public SyncProjectilePacket(int ID, double X, double Y, double Z, double AccelX, double AccelY, double AccelZ, int TypeID){
        this.EntityID = ID;
        this.x = X;
        this.y = Y;
        this.z = Z;
        this.AccelX = AccelX;
        this.AccelY = AccelY;
        this.AccelZ = AccelZ;

        this.TypeID = TypeID;
    }

    public static SyncProjectilePacket decode(PacketBuffer buffer){
        final int EntityID = buffer.readInt();
        final double posX = buffer.readDouble();
        final double posY = buffer.readDouble();
        final double posZ = buffer.readDouble();
        final double Accelx = buffer.readDouble();
        final double AccelY = buffer.readDouble();
        final double AccelZ = buffer.readDouble();

        final int TypeID = buffer.readInt();

        return new SyncProjectilePacket(EntityID, posX, posY, posZ, Accelx, AccelY, AccelZ, TypeID);
    }

    public static void encode(final SyncProjectilePacket message, final PacketBuffer buffer){
        buffer.writeInt(message.EntityID);
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
        buffer.writeDouble(message.AccelX);
        buffer.writeDouble(message.AccelY);
        buffer.writeDouble(message.AccelZ);
        buffer.writeInt(message.TypeID);
    }

    public static void handle(final SyncProjectilePacket message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {

            switch (message.EntityID){
                case defined.PROJECTILETYPE_CANNON:{
                    ProjectileUtils.spawnClientCannonShot(message.TypeID, message.x, message.y, message.z, message.AccelX, message.AccelY, message.AccelZ);
                    break;
                }
            }

        }));
        ctx.get().setPacketHandled(true);
    }

}
