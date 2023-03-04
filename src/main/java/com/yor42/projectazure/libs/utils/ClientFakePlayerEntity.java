package com.yor42.projectazure.libs.utils;

import com.lowdragmc.lowdraglib.utils.DummyWorld;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ClientFakePlayerEntity extends PlayerEntity {

    public static ClientFakePlayerEntity FAKEPLAYER;

    public static ClientFakePlayerEntity getFakePlayer(){
        if(FAKEPLAYER == null){
            FAKEPLAYER = new ClientFakePlayerEntity(new DummyWorld());
        }
        return FAKEPLAYER;
    }

    private ClientFakePlayerEntity(World p_i241920_1_) {
        super(p_i241920_1_, BlockPos.ZERO, 0, new GameProfile(UUID.randomUUID(), ""));
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
