package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.items.ItemNightVisionHelmet;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.lwjgl.system.CallbackI;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;

import java.util.function.Supplier;

public class SyncItemTagPacket {

    private final String slotname;
    private final CompoundTag tag;

    public SyncItemTagPacket(String slotname, CompoundTag tag){
        this.slotname = slotname;
        this.tag = tag;
    }

    public static SyncItemTagPacket decode (final FriendlyByteBuf buffer){
        return new SyncItemTagPacket(buffer.readUtf(), buffer.readNbt());
    }

    public static void encode(final SyncItemTagPacket msg, final FriendlyByteBuf buffer){
        buffer.writeUtf(msg.slotname);
        buffer.writeNbt(msg.tag);
    }


    public static void handle(final SyncItemTagPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if(player == null){
                return;
            }
            String id = msg.slotname;
            ItemStack stack = ItemStack.EMPTY;

            if(id.startsWith("curios") && CompatibilityUtils.isCurioLoaded()){
                id = id.split(":", 2)[1];
                stack = CuriosCompat.getCurioItemStack(player, id, (item)->item.getItem() instanceof ItemNightVisionHelmet);
            }
            else{
                if(id.equals("head")){
                    stack = player.getItemBySlot(EquipmentSlot.HEAD);
                }
            }

            stack.setTag(msg.tag);
        });
        ctx.get().setPacketHandled(true);
    }

}
