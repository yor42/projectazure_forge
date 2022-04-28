package com.yor42.projectazure.libs;

import com.yor42.projectazure.events.GunFireEvent;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class GunRecoilUtil {

    public static  GunRecoilUtil instance;
    private Random random = new Random();
    private double gunRecoilNormal;
    private double gunRecoilAngle;
    private float gunRecoilRandom;
    private float cameraRecoil;
    private float progressCameraRecoil;

    public static GunRecoilUtil getInstance() {
        if (instance == null) {
            instance = new GunRecoilUtil();
        }
        return instance;
    }

    @SubscribeEvent
    public void onGunFire(GunFireEvent.PostFire event)
    {
        if(!event.isRemote())
            return;

        ItemStack heldItem = event.getGun();
        ItemGunBase gunItem = (ItemGunBase) heldItem.getItem();
        this.cameraRecoil = gunItem.getRecoil();
        this.progressCameraRecoil = 0F;
        this.gunRecoilRandom = random.nextFloat();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END || this.cameraRecoil <= 0)
            return;

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        float recoilAmount = this.cameraRecoil * mc.getDeltaFrameTime() * 0.1F;
        float startProgress = this.progressCameraRecoil / this.cameraRecoil;
        float endProgress = (this.progressCameraRecoil + recoilAmount) / this.cameraRecoil;

        if(startProgress < 0.2F)
        {
            mc.player.setXRot(mc.player.getXRot() - ((endProgress - startProgress) / 0.2F) * this.cameraRecoil);
        }
        else
        {
            mc.player.setXRot(mc.player.getXRot()+ ((endProgress - startProgress) / 0.8F) * this.cameraRecoil);
        }

        this.progressCameraRecoil += recoilAmount;

        if(this.progressCameraRecoil >= this.cameraRecoil)
        {
            this.cameraRecoil = 0;
            this.progressCameraRecoil = 0;
        }
    }

}
