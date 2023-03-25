package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.renderer.entity.biped.*;
import com.yor42.projectazure.client.renderer.entity.misc.EntityClaymoreRenderer;
import com.yor42.projectazure.client.renderer.entity.misc.EntityMissileDroneRenderer;
import com.yor42.projectazure.client.renderer.entity.misc.EntityPlanef4fwildcatRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.*;
import com.yor42.projectazure.gameobject.entity.companion.bonus.*;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityHK416;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityM4A1;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityShiroko;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityW;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityAmiya;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityKyaru;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntitySylvi;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.*;
import com.yor42.projectazure.gameobject.entity.companion.ranged.EntitySchwarz;
import com.yor42.projectazure.gameobject.entity.companion.ships.*;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import com.yor42.projectazure.gameobject.entity.misc.EntityMissileDrone;
import com.yor42.projectazure.gameobject.entity.planes.EntityF4fWildcat;
import com.yor42.projectazure.gameobject.entity.projectiles.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

@Mod.EventBusSubscriber(modid = Constants.MODID ,bus = Mod.EventBusSubscriber.Bus.MOD)
public class registerEntity {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);
    public static final RegistryObject<EntityType<EntityMissileDrone>> MISSILEDRONE = ENTITIES.register("missiledrone", () -> EntityType.Builder.of(EntityMissileDrone::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("missiledrone").toString()));
    //Planes
    public static final RegistryObject<EntityType<EntityF4fWildcat>> PLANE_F4FWILDCAT = ENTITIES.register("planef4fwildcat", () -> EntityType.Builder.of(EntityF4fWildcat::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("entityplanef4fwildcat").toString()));

    //?????
    public static final RegistryObject<EntityType<EntityClaymore>> CLAYMORE = ENTITIES.register("entityclaymore", () -> EntityType.Builder.of(EntityClaymore::new, MobCategory.CREATURE).sized(0.7F, 2.31F).build(ModResourceLocation("entityclaymore").toString()));
    public static final RegistryObject<EntityType<EntityRailgunProjectile>> PROJECTILE_RAILGUN = ENTITIES.register("projectile_railgun", () -> EntityType.Builder.<EntityRailgunProjectile>of(EntityRailgunProjectile::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectile_railgun").toString()));
    public static final RegistryObject<EntityType<EntitySupernovaProjectile>> PROJECTILE_SUPERNOVA = ENTITIES.register("projectile_supernova", () -> EntityType.Builder.<EntitySupernovaProjectile>of(EntitySupernovaProjectile::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectile_supernova").toString()));
    public static final RegistryObject<EntityType<EntityThrownKnifeProjectile>> PROJECTILE_THROWN_KNIFE = ENTITIES.register("projectile_knife", () -> EntityType.Builder.<EntityThrownKnifeProjectile>of(EntityThrownKnifeProjectile::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectile_knife").toString()));
    public static final RegistryObject<EntityType<EntityMissileDroneMissile>> DRONE_MISSILE = ENTITIES.register("projectiledrone_missile", () -> EntityType.Builder.of(EntityMissileDroneMissile::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectiledrone_missile").toString()));
    public static final RegistryObject<EntityType<EntityProjectileTorpedo>> PROJECTILE_TORPEDO = ENTITIES.register("entitytorpedo", () -> EntityType.Builder.<EntityProjectileTorpedo>of(EntityProjectileTorpedo::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectiletorpedo").toString()));
    public static final RegistryObject<EntityType<EntityArtsProjectile>> PROJECTILE_ARTS = ENTITIES.register("entityartsprojectile", () -> EntityType.Builder.<EntityArtsProjectile>of(EntityArtsProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilearts").toString()));
    //projectile
    public static final RegistryObject<EntityType<EntityCannonPelllet>> PROJECTILE_CANNONSHELL = ENTITIES.register("entitycannonshell", () -> EntityType.Builder.<EntityCannonPelllet>of(EntityCannonPelllet::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilecannonshell").toString()));
    public static final RegistryObject<EntityType<EntitySpellBall>> PROJECTILE_SPELLBALL = ENTITIES.register("entityspellball", () -> EntityType.Builder.<EntitySpellBall>of(EntitySpellBall::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilespellball").toString()));
    public static final RegistryObject<EntityType<EntityFallingSword>> PROJECTILE_FALLINGSWORD = ENTITIES.register("entityfaillingsword", () -> EntityType.Builder.<EntityFallingSword>of(EntityFallingSword::new, MobCategory.MISC).sized(1F, 1F).build(ModResourceLocation("projectilefallingsword").toString()));

    public static final RegistryObject<EntityType<EntityArtoria>> ARTORIA = ENTITIES.register("entityartoria", () -> EntityType.Builder.of(EntityArtoria::new, MobCategory.CREATURE).sized(0.572F, 1.54F).build(ModResourceLocation("entityartoria").toString()));
    public static final RegistryObject<EntityType<EntityScathath>> SCATHATH = ENTITIES.register("entityscathath", () -> EntityType.Builder.of(EntityScathath::new, MobCategory.CREATURE).sized(0.572F, 1.68F).build(ModResourceLocation("entityscathath").toString()));

    public static final RegistryObject<EntityType<EntityYamato>> YAMATO = ENTITIES.register("entityyamato", () -> EntityType.Builder.of(EntityYamato::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityyamato").toString()));

    public static final RegistryObject<EntityType<EntitySylvi>> SYLVI = ENTITIES.register("entitysylvi", () -> EntityType.Builder.of(EntitySylvi::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entitysylvi").toString()));

    public static final RegistryObject<EntityType<EntityHaru>> HARU = ENTITIES.register("entityharu", () -> EntityType.Builder.of(EntityHaru::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityharu").toString()));

    public static final RegistryObject<EntityType<EntityNearl>> NEARL = ENTITIES.register("entitynearl", () -> EntityType.Builder.of(EntityNearl::new, MobCategory.CREATURE).sized(0.572F, 1.71F).build(ModResourceLocation("entitynearl").toString()));
    public static final RegistryObject<EntityType<EntitySchwarz>> SCHWARZ = ENTITIES.register("entityschwarz", () -> EntityType.Builder.of(EntitySchwarz::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityschwarz").toString()));
    public static final RegistryObject<EntityType<EntitySiege>> SIEGE = ENTITIES.register("entitysiege", () -> EntityType.Builder.of(EntitySiege::new, MobCategory.CREATURE).sized(0.572F, 1.72F).build(ModResourceLocation("entitysiege").toString()));
    public static final RegistryObject<EntityType<EntityLappland>> LAPPLAND = ENTITIES.register("entitylappland", () -> EntityType.Builder.of(EntityLappland::new, MobCategory.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitylappland").toString()));
    public static final RegistryObject<EntityType<EntityFrostnova>> FROSTNOVA = ENTITIES.register("entityfrostnova", () -> EntityType.Builder.of(EntityFrostnova::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entityfrostnova").toString()));
    public static final RegistryObject<EntityType<EntityTexas>> TEXAS = ENTITIES.register("entitytexas", () -> EntityType.Builder.of(EntityTexas::new, MobCategory.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitytexas").toString()));
    public static final RegistryObject<EntityType<EntityYato>> YATO = ENTITIES.register("entityyato", () -> EntityType.Builder.of(EntityYato::new, MobCategory.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entityyato").toString()));
    public static final RegistryObject<EntityType<EntityCrownSlayer>> CROWNSLAYER = ENTITIES.register("entitycrownslayer", () -> EntityType.Builder.of(EntityCrownSlayer::new, MobCategory.CREATURE).sized(0.572F, 1.66F).build(ModResourceLocation("entitycrownslayer").toString()));
    public static final RegistryObject<EntityType<EntityTalulah>> TALULAH = ENTITIES.register("entitytalulah", () -> EntityType.Builder.of(EntityTalulah::new, MobCategory.CREATURE).sized(0.572F, 1.78F).build(ModResourceLocation("entitytalulah").toString()));
    public static final RegistryObject<EntityType<EntityRosmontis>> ROSMONTIS = ENTITIES.register("entityrosmontis", () -> EntityType.Builder.of(EntityRosmontis::new, MobCategory.CREATURE).sized(0.572F, 1.42F).build(ModResourceLocation("entityamiya").toString()));
    public static final RegistryObject<EntityType<EntityAmiya>> AMIYA = ENTITIES.register("entityamiya", () -> EntityType.Builder.of(EntityAmiya::new, MobCategory.CREATURE).sized(0.572F, 1.42F).build(ModResourceLocation("entityamiya").toString()));
    public static final RegistryObject<EntityType<EntityMudrock>> MUDROCK = ENTITIES.register("entitymudrock", () -> EntityType.Builder.of(EntityMudrock::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entitymudrock").toString()));
    public static final RegistryObject<EntityType<EntityChen>> CHEN = ENTITIES.register("entitychen", () -> EntityType.Builder.of(EntityChen::new, MobCategory.CREATURE).sized(0.572F, 1.68F).build(ModResourceLocation("entitychen").toString()));
    public static final RegistryObject<EntityType<EntityW>> W = ENTITIES.register("entityw", () -> EntityType.Builder.of(EntityW::new, MobCategory.CREATURE).sized(0.572F, 1.65F).build(ModResourceLocation("entityw").toString()));
    public static final RegistryObject<EntityType<EntitySkullShatterer>> SKULLSHATTERER = ENTITIES.register("entityskullshatterer", () -> EntityType.Builder.of(EntitySkullShatterer::new, MobCategory.CREATURE).sized(0.572F, 1.58F).build(ModResourceLocation("entityskullshatterer").toString()));

    public static final RegistryObject<EntityType<EntityShiroko>> SHIROKO = ENTITIES.register("entityshiroko", () -> EntityType.Builder.of(EntityShiroko::new, MobCategory.CREATURE).sized(0.572F, 1.575F).build(ModResourceLocation("entityshiroko").toString()));

    public static final RegistryObject<EntityType<EntityHK416>> HK416 = ENTITIES.register("entityhk416", () -> EntityType.Builder.of(EntityHK416::new, MobCategory.CREATURE).sized(0.572F, 1.58F).build(ModResourceLocation("entityhk416").toString()));public static final RegistryObject<EntityType<EntityM4A1>> M4A1 = ENTITIES.register("entitym4a1", () -> EntityType.Builder.of(EntityM4A1::new, MobCategory.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entitym4a1").toString()));
    public static final RegistryObject<EntityType<EntityNagato>> NAGATO = ENTITIES.register("entitynagato", () -> EntityType.Builder.of(EntityNagato::new, MobCategory.CREATURE).sized(0.572F, 1.32F).build(ModResourceLocation("entitynagato").toString()));
    public static final RegistryObject<EntityType<EntityEnterprise>> ENTERPRISE = ENTITIES.register("entityenterprise", () -> EntityType.Builder.of(EntityEnterprise::new, MobCategory.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entityenterprise").toString()));
    public static final RegistryObject<EntityType<EntityGangwon>> GANGWON = ENTITIES.register("entitygangwon", () -> EntityType.Builder.of(EntityGangwon::new, MobCategory.CREATURE).sized(0.572F, 1.35F).build(ModResourceLocation("entitygandwon").toString()));
    public static final RegistryObject<EntityType<EntityLaffey>> LAFFEY = ENTITIES.register("entitylaffey", () -> EntityType.Builder.of(EntityLaffey::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entitylaffey").toString()));
    public static final RegistryObject<EntityType<EntityZ23>> Z23 = ENTITIES.register("entityz23", () -> EntityType.Builder.of(EntityZ23::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityz23").toString()));
    public static final RegistryObject<EntityType<EntityJavelin>> JAVELIN = ENTITIES.register("entityjavelin", () -> EntityType.Builder.of(EntityJavelin::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString()));
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> EntityType.Builder.of(EntityAyanami::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString()));
    public static final RegistryObject<EntityType<EntityKyaru>> KYARU = ENTITIES.register("entitykyaru", () -> EntityType.Builder.of(EntityKyaru::new, MobCategory.CREATURE).sized(0.572F, 1.52F).build(ModResourceLocation("entitykyaru").toString()));
    public static final RegistryObject<EntityType<EntityShiki>> SHIKI = ENTITIES.register("entityshiki", () -> EntityType.Builder.of(EntityShiki::new, MobCategory.CREATURE).sized(0.572F, 1.6F).build(ModResourceLocation("entityshiki").toString()));
    public static final RegistryObject<EntityType<EntityExcela>> EXCELA = ENTITIES.register("entityexcela", () -> EntityType.Builder.of(EntityExcela::new, MobCategory.CREATURE).sized(0.572F, 1.68F).build(ModResourceLocation("entityexcela").toString()));
    public static final RegistryObject<EntityType<EntityPurifier>> PURIFIER = ENTITIES.register("entitypurifier", () -> EntityType.Builder.of(EntityPurifier::new, MobCategory.CREATURE).sized(0.572F, 1.58F).build(ModResourceLocation("entitypurifier").toString()));
    public static final RegistryObject<EntityType<EntityMash>> MASH = ENTITIES.register("entitymash", () -> EntityType.Builder.of(EntityMash::new, MobCategory.CREATURE).sized(0.572F, 1.64F).build(ModResourceLocation("entitymash").toString()));

    public static final RegistryObject<EntityType<EntityCausalBlackhole>> BLACKHOLE = ENTITIES.register("entitycausalblackhole", () -> EntityType.Builder.<EntityCausalBlackhole>of(EntityCausalBlackhole::new, MobCategory.MISC).sized(3F, 3F).build(ModResourceLocation("entitycausalblackhole").toString()));

    @SubscribeEvent
    public static void RegisterAttributes(EntityAttributeCreationEvent event){
        event.put(AYANAMI.get(), EntityAyanami.MutableAttribute().build());
        event.put(JAVELIN.get(), EntityJavelin.MutableAttribute().build());
        event.put(GANGWON.get(), EntityGangwon.MutableAttribute().build());
        event.put(ENTERPRISE.get(), EntityEnterprise.MutableAttribute().build());
        event.put(PLANE_F4FWILDCAT.get(), EntityF4fWildcat.MutableAttribute().build());
        event.put(MISSILEDRONE.get(), EntityMissileDrone.MutableAttribute().build());
        event.put(SHIROKO.get(), EntityShiroko.MutableAttribute().build());
        event.put(NAGATO.get(), EntityNagato.MutableAttribute().build());
        event.put(CHEN.get(), EntityChen.MutableAttribute().build());
        event.put(ROSMONTIS.get(), EntityRosmontis.MutableAttribute().build());
        event.put(TALULAH.get(), EntityTalulah.MutableAttribute().build());
        event.put(Z23.get(), EntityZ23.MutableAttribute().build());
        event.put(LAFFEY.get(), EntityLaffey.MutableAttribute().build());
        event.put(M4A1.get(), EntityM4A1.MutableAttribute().build());
        event.put(AMIYA.get(), EntityAmiya.MutableAttribute().build());
        event.put(MUDROCK.get(), EntityMudrock.MutableAttribute().build());
        event.put(TEXAS.get(), EntityTexas.MutableAttribute().build());
        event.put(FROSTNOVA.get(), EntityFrostnova.MutableAttribute().build());
        event.put(LAPPLAND.get(), EntityLappland.MutableAttribute().build());
        event.put(SIEGE.get(), EntitySiege.MutableAttribute().build());
        event.put(SCHWARZ.get(), EntitySchwarz.MutableAttribute().build());
        event.put(NEARL.get(), EntityNearl.MutableAttribute().build());
        event.put(SYLVI.get(), EntitySylvi.MutableAttribute().build());
        event.put(YAMATO.get(), EntityYamato.MutableAttribute().build());
        event.put(CLAYMORE.get(), EntityClaymore.MutableAttribute().build());
        event.put(CROWNSLAYER.get(), EntityCrownSlayer.MutableAttribute().build());
        event.put(YATO.get(), EntityYato.MutableAttribute().build());
        event.put(ARTORIA.get(), EntityArtoria.MutableAttribute().build());
        event.put(SCATHATH.get(), EntityArtoria.MutableAttribute().build());
        event.put(KYARU.get(), EntityKyaru.MutableAttribute().build());
        event.put(SHIKI.get(), EntityShiki.MutableAttribute().build());
        event.put(EXCELA.get(), EntityExcela.MutableAttribute().build());
        event.put(W.get(), EntityW.MutableAttribute().build());
        event.put(SKULLSHATTERER.get(), EntitySkullShatterer.MutableAttribute().build());
        event.put(HK416.get(), EntityHK416.MutableAttribute().build());
        event.put(PURIFIER.get(), EntityPurifier.MutableAttribute().build());
        event.put(MASH.get(), EntityMash.MutableAttribute().build());
        event.put(HARU.get(), EntityHaru.MutableAttribute().build());
    }
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(registerEntity.AYANAMI.get(), entityAyanamiRenderer::new);
        event.registerEntityRenderer(registerEntity.JAVELIN.get(), entityJavelinRenderer::new);
        event.registerEntityRenderer(registerEntity.Z23.get(), EntityZ23Renderer::new);
        event.registerEntityRenderer(registerEntity.LAFFEY.get(), EntityLaffeyRenderer::new);
        event.registerEntityRenderer(registerEntity.GANGWON.get(), entityGangwonRenderer::new);
        event.registerEntityRenderer(registerEntity.SHIROKO.get(), entityShirokoRenderer::new);
        event.registerEntityRenderer(registerEntity.ENTERPRISE.get(), entityEnterpriseRenderer::new);
        event.registerEntityRenderer(registerEntity.NAGATO.get(), entityNagatoRenderer::new);
        event.registerEntityRenderer(registerEntity.CHEN.get(), EntityChenRenderer::new);
        event.registerEntityRenderer(registerEntity.MUDROCK.get(), EntityMudrockRenderer::new);
        event.registerEntityRenderer(registerEntity.ROSMONTIS.get(), EntityRosmontisRenderer::new);
        event.registerEntityRenderer(registerEntity.TALULAH.get(), EntityTalulahRenderer::new);
        event.registerEntityRenderer(registerEntity.AMIYA.get(), EntityAmiyaRenderer::new);
        event.registerEntityRenderer(registerEntity.M4A1.get(), EntityM4A1Renderer::new);
        event.registerEntityRenderer(registerEntity.TEXAS.get(), EntityTexasRenderer::new);
        event.registerEntityRenderer(registerEntity.FROSTNOVA.get(), EntityFrostNovaRenderer::new);
        event.registerEntityRenderer(registerEntity.CROWNSLAYER.get(), EntityCrownslayerRenderer::new);
        event.registerEntityRenderer(registerEntity.YATO.get(), EntityYatoRenderer::new);
        event.registerEntityRenderer(registerEntity.LAPPLAND.get(), EntityLapplandRenderer::new);
        event.registerEntityRenderer(registerEntity.SIEGE.get(), EntitySiegeRenderer::new);
        event.registerEntityRenderer(registerEntity.SCHWARZ.get(), EntitySchwarzRenderer::new);
        event.registerEntityRenderer(registerEntity.SYLVI.get(), EntitySylviRenderer::new);
        event.registerEntityRenderer(registerEntity.YAMATO.get(), EntityYamatoRenderer::new);
        event.registerEntityRenderer(registerEntity.ARTORIA.get(), EntityArtoriaRenderer::new);
        event.registerEntityRenderer(registerEntity.SCATHATH.get(), EntityScathathRenderer::new);
        event.registerEntityRenderer(registerEntity.NEARL.get(), EntityNearlRenderer::new);
        event.registerEntityRenderer(registerEntity.KYARU.get(), EntityKyaruRenderer::new);
        event.registerEntityRenderer(registerEntity.SHIKI.get(), EntityShikiRenderer::new);
        event.registerEntityRenderer(registerEntity.EXCELA.get(), EntityExcelaRenderer::new);
        event.registerEntityRenderer(registerEntity.W.get(), EntityWRenderer::new);
        event.registerEntityRenderer(registerEntity.HK416.get(), EntityHK416Renderer::new);
        event.registerEntityRenderer(registerEntity.PURIFIER.get(), EntityPurifierRenderer::new);
        event.registerEntityRenderer(registerEntity.MASH.get(), EntityMashRenderer::new);
        event.registerEntityRenderer(registerEntity.SKULLSHATTERER.get(), EntitySkullShattererRenderer::new);
        event.registerEntityRenderer(registerEntity.HARU.get(), EntityHaruRenderer::new);

        event.registerEntityRenderer(registerEntity.MISSILEDRONE.get(), EntityMissileDroneRenderer::new);

        event.registerEntityRenderer(registerEntity.BLACKHOLE.get(), EntityBlackHoleRenderer::new);
        event.registerEntityRenderer(registerEntity.PROJECTILE_CANNONSHELL.get(), entityCannonPelletRenderer::new);
        event.registerEntityRenderer(registerEntity.PROJECTILE_SPELLBALL.get(), EntitySpellBallRenderer::new);
        event.registerEntityRenderer(registerEntity.PROJECTILE_RAILGUN.get(), EntityRailgunProjectileRenderer::new);
        event.registerEntityRenderer(registerEntity.PROJECTILE_TORPEDO.get(), EntityProjectileTorpedoRenderer::new);
        event.registerEntityRenderer(registerEntity.PROJECTILE_ARTS.get(), EntityArtsProjectileRenderer::new);
        event.registerEntityRenderer(registerEntity.DRONE_MISSILE.get(), MissileDroneMissileRenderer::new);
        event.registerEntityRenderer(registerEntity.PROJECTILE_THROWN_KNIFE.get(), EntityThrownKnifeRenderer::new);
        event.registerEntityRenderer(registerEntity.PROJECTILE_FALLINGSWORD.get(), EntityFallingSwordRenderer::new);
        event.registerEntityRenderer(registerEntity.PROJECTILE_SUPERNOVA.get(), EntitySupernovaProjectileRenderer::new);

        event.registerEntityRenderer(registerEntity.CLAYMORE.get(), EntityClaymoreRenderer::new);

        event.registerEntityRenderer(registerEntity.PLANE_F4FWILDCAT.get(), EntityPlanef4fwildcatRenderer::new);
    }
    public static void registerRenderer(){
    }

    public static void loadClass(){
        Main.LOGGER.info("Registering entities....");
    }

}
