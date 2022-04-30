package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.renderer.block.DrydockControllerRenderer;
import com.yor42.projectazure.client.renderer.block.MachineMetalPressRenderer;
import com.yor42.projectazure.client.renderer.block.MachineRecruitBeaconRenderer;
import com.yor42.projectazure.client.renderer.entity.*;
import com.yor42.projectazure.client.renderer.entity.misc.EntityClaymoreRenderer;
import com.yor42.projectazure.client.renderer.entity.misc.EntityMissileDroneRenderer;
import com.yor42.projectazure.client.renderer.entity.misc.EntityPlanef4fwildcatRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.*;
import com.yor42.projectazure.gameobject.crafting.*;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.Main.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.yor42.projectazure.Main.*;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class registerRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AYANAMI.get(), entityAyanamiRenderer::new);
        event.registerEntityRenderer(JAVELIN.get(), entityJavelinRenderer::new);
        event.registerEntityRenderer(Z23.get(), entityZ23Renderer::new);
        event.registerEntityRenderer(LAFFEY.get(), EntityLaffeyRenderer::new);
        event.registerEntityRenderer(GANGWON.get(), entityGangwonRenderer::new);
        event.registerEntityRenderer(SHIROKO.get(), entityShirokoRenderer::new);
        event.registerEntityRenderer(ENTERPRISE.get(), entityEnterpriseRenderer::new);
        event.registerEntityRenderer(NAGATO.get(), entityNagatoRenderer::new);
        event.registerEntityRenderer(CHEN.get(), EntityChenRenderer::new);
        event.registerEntityRenderer(MUDROCK.get(), EntityMudrockRenderer::new);
        event.registerEntityRenderer(ROSMONTIS.get(), EntityRosmontisRenderer::new);
        event.registerEntityRenderer(TALULAH.get(), EntityTalulahRenderer::new);
        event.registerEntityRenderer(AMIYA.get(), EntityAmiyaRenderer::new);
        event.registerEntityRenderer(M4A1.get(), EntityM4A1Renderer::new);
        event.registerEntityRenderer(TEXAS.get(), EntityTexasRenderer::new);
        event.registerEntityRenderer(FROSTNOVA.get(), EntityFrostNovaRenderer::new);
        event.registerEntityRenderer(LAPPLAND.get(), EntityLapplandRenderer::new);
        event.registerEntityRenderer(SIEGE.get(), EntitySiegeRenderer::new);
        event.registerEntityRenderer(SCHWARZ.get(), EntitySchwarzRenderer::new);
        event.registerEntityRenderer(SYLVI.get(), EntitySylviRenderer::new);
        event.registerEntityRenderer(YAMATO.get(), EntityYamatoRenderer::new);

        event.registerEntityRenderer(MISSILEDRONE.get(), EntityMissileDroneRenderer::new);

        event.registerEntityRenderer(CANNONSHELL.get(), entityCannonPelletRenderer::new);
        event.registerEntityRenderer(TORPEDO.get(), EntityProjectileTorpedoRenderer::new);
        event.registerEntityRenderer(PROJECTILEARTS.get(), EntityArtsProjectileRenderer::new);
        event.registerEntityRenderer(GUN_BULLET.get(), EntityGunBulletRenderer::new);
        event.registerEntityRenderer(DRONE_MISSILE.get(), MissileDroneMissileRenderer::new);
        event.registerEntityRenderer(THROWN_KNIFE.get(), EntityThrownKnifeRenderer::new);

        event.registerEntityRenderer(CLAYMORE.get(), EntityClaymoreRenderer::new);

        event.registerBlockEntityRenderer(METAL_PRESS_BLOCK_ENTITY.get(), MachineMetalPressRenderer::new);
        event.registerBlockEntityRenderer(DRYDOCK_BLOCK_ENTITY.get(), DrydockControllerRenderer::new);
        event.registerBlockEntityRenderer(RECRUIT_BEACON_BLOCK_ENTITY.get(), MachineRecruitBeaconRenderer::new);

        event.registerEntityRenderer(F4FWildCat.get(), EntityPlanef4fwildcatRenderer::new);
    }
}
