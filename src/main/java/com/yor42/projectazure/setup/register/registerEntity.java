package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityCrownSlayer;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityFrostnova;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityTalulah;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityM4A1;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityShiroko;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityAmiya;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntitySylvi;
import com.yor42.projectazure.gameobject.entity.companion.ranged.EntitySchwarz;
import com.yor42.projectazure.gameobject.entity.companion.ships.*;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.*;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import com.yor42.projectazure.gameobject.entity.misc.EntityMissileDrone;
import com.yor42.projectazure.gameobject.entity.planes.EntityF4fWildcat;
import com.yor42.projectazure.gameobject.entity.projectiles.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class registerEntity {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);
    public static final RegistryObject<EntityType<EntityMissileDrone>> MISSILEDRONE = ENTITIES.register("missiledrone", () -> EntityType.Builder.of(EntityMissileDrone::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("missiledrone").toString()));
    //Planes
    public static final RegistryObject<EntityType<EntityF4fWildcat>> PLANE_F4FWILDCAT = ENTITIES.register("planef4fwildcat", () -> EntityType.Builder.of(EntityF4fWildcat::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("entityplanef4fwildcat").toString()));
    //?????
    public static final RegistryObject<EntityType<EntityClaymore>> CLAYMORE = ENTITIES.register("entityclaymore", () -> EntityType.Builder.of(EntityClaymore::new, EntityClassification.CREATURE).sized(0.7F, 2.31F).build(ModResourceLocation("entityclaymore").toString()));
    public static final RegistryObject<EntityType<EntityRailgunProjectile>> PROJECTILE_RAILGUN = ENTITIES.register("projectile_railgun", () -> EntityType.Builder.<EntityRailgunProjectile>of(EntityRailgunProjectile::new, EntityClassification.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectile_railgun").toString()));
    public static final RegistryObject<EntityType<EntityThrownKnifeProjectile>> PROJECTILE_THROWN_KNIFE = ENTITIES.register("projectile_knife", () -> EntityType.Builder.<EntityThrownKnifeProjectile>of(EntityThrownKnifeProjectile::new, EntityClassification.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectile_knife").toString()));
    public static final RegistryObject<EntityType<EntityMissileDroneMissile>> DRONE_MISSILE = ENTITIES.register("projectiledrone_missile", () -> EntityType.Builder.of(EntityMissileDroneMissile::new, EntityClassification.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectiledrone_missile").toString()));
    public static final RegistryObject<EntityType<EntityProjectileTorpedo>> PROJECTILE_TORPEDO = ENTITIES.register("entitytorpedo", () -> EntityType.Builder.<EntityProjectileTorpedo>of(EntityProjectileTorpedo::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectiletorpedo").toString()));
    public static final RegistryObject<EntityType<EntityArtsProjectile>> PROJECTILE_ARTS = ENTITIES.register("entityartsprojectile", () -> EntityType.Builder.<EntityArtsProjectile>of(EntityArtsProjectile::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilearts").toString()));
    //projectile
    public static final RegistryObject<EntityType<EntityCannonPelllet>> PROJECTILE_CANNONSHELL = ENTITIES.register("entitycannonshell", () -> EntityType.Builder.<EntityCannonPelllet>of(EntityCannonPelllet::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilecannonshell").toString()));


    public static final RegistryObject<EntityType<EntityArtoria>> ARTORIA = ENTITIES.register("entityyartoria", () -> EntityType.Builder.of(EntityArtoria::new, EntityClassification.CREATURE).sized(0.572F, 1.54F).build(ModResourceLocation("entityyartoria").toString()));
    public static final RegistryObject<EntityType<EntityYamato>> YAMATO = ENTITIES.register("entityyamato", () -> EntityType.Builder.of(EntityYamato::new, EntityClassification.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityyamato").toString()));
    public static final RegistryObject<EntityType<EntitySylvi>> SYLVI = ENTITIES.register("entitysylvi", () -> EntityType.Builder.of(EntitySylvi::new, EntityClassification.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityschwarz").toString()));
    public static final RegistryObject<EntityType<EntityNearl>> NEARL = ENTITIES.register("entitynearl", () -> EntityType.Builder.of(EntityNearl::new, EntityClassification.CREATURE).sized(0.572F, 1.71F).build(ModResourceLocation("entitynearl").toString()));
    public static final RegistryObject<EntityType<EntitySchwarz>> SCHWARZ = ENTITIES.register("entityschwarz", () -> EntityType.Builder.of(EntitySchwarz::new, EntityClassification.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityschwarz").toString()));
    public static final RegistryObject<EntityType<EntitySiege>> SIEGE = ENTITIES.register("entitysiege", () -> EntityType.Builder.of(EntitySiege::new, EntityClassification.CREATURE).sized(0.572F, 1.72F).build(ModResourceLocation("entitysiege").toString()));
    public static final RegistryObject<EntityType<EntityLappland>> LAPPLAND = ENTITIES.register("entitylappland", () -> EntityType.Builder.of(EntityLappland::new, EntityClassification.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitylappland").toString()));
    public static final RegistryObject<EntityType<EntityFrostnova>> FROSTNOVA = ENTITIES.register("entityfrostnova", () -> EntityType.Builder.of(EntityFrostnova::new, EntityClassification.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entityfrostnova").toString()));
    public static final RegistryObject<EntityType<EntityTexas>> TEXAS = ENTITIES.register("entitytexas", () -> EntityType.Builder.of(EntityTexas::new, EntityClassification.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitytexas").toString()));
    public static final RegistryObject<EntityType<EntityYato>> YATO = ENTITIES.register("entityyato", () -> EntityType.Builder.of(EntityYato::new, EntityClassification.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entityyato").toString()));
    public static final RegistryObject<EntityType<EntityCrownSlayer>> CROWNSLAYER = ENTITIES.register("entitycrownslayer", () -> EntityType.Builder.of(EntityCrownSlayer::new, EntityClassification.CREATURE).sized(0.572F, 1.66F).build(ModResourceLocation("entitycrownslayer").toString()));
    public static final RegistryObject<EntityType<EntityTalulah>> TALULAH = ENTITIES.register("entitytalulah", () -> EntityType.Builder.of(EntityTalulah::new, EntityClassification.CREATURE).sized(0.572F, 1.78F).build(ModResourceLocation("entitytalulah").toString()));
    public static final RegistryObject<EntityType<EntityRosmontis>> ROSMONTIS = ENTITIES.register("entityrosmontis", () -> EntityType.Builder.of(EntityRosmontis::new, EntityClassification.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entityamiya").toString()));
    public static final RegistryObject<EntityType<EntityAmiya>> AMIYA = ENTITIES.register("entityamiya", () -> EntityType.Builder.of(EntityAmiya::new, EntityClassification.CREATURE).sized(0.572F, 1.42F).build(ModResourceLocation("entityamiya").toString()));
    public static final RegistryObject<EntityType<EntityMudrock>> MUDROCK = ENTITIES.register("entitymudrock", () -> EntityType.Builder.of(EntityMudrock::new, EntityClassification.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entitymudrock").toString()));
    public static final RegistryObject<EntityType<EntityChen>> CHEN = ENTITIES.register("entitychen", () -> EntityType.Builder.of(EntityChen::new, EntityClassification.CREATURE).sized(0.572F, 1.68F).build(ModResourceLocation("entitychen").toString()));
    public static final RegistryObject<EntityType<EntityShiroko>> SHIROKO = ENTITIES.register("entityshiroko", () -> EntityType.Builder.of(EntityShiroko::new, EntityClassification.CREATURE).sized(0.572F, 1.575F).build(ModResourceLocation("entityshiroko").toString()));
    public static final RegistryObject<EntityType<EntityM4A1>> M4A1 = ENTITIES.register("entitym4a1", () -> EntityType.Builder.of(EntityM4A1::new, EntityClassification.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entitym4a1").toString()));
    public static final RegistryObject<EntityType<EntityNagato>> NAGATO = ENTITIES.register("entitynagato", () -> EntityType.Builder.of(EntityNagato::new, EntityClassification.CREATURE).sized(0.572F, 1.32F).build(ModResourceLocation("entitynagato").toString()));
    public static final RegistryObject<EntityType<EntityEnterprise>> ENTERPRISE = ENTITIES.register("entityenterprise", () -> EntityType.Builder.of(EntityEnterprise::new, EntityClassification.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entityenterprise").toString()));
    public static final RegistryObject<EntityType<EntityGangwon>> GANGWON = ENTITIES.register("entitygangwon", () -> EntityType.Builder.of(EntityGangwon::new, EntityClassification.CREATURE).sized(0.572F, 1.35F).build(ModResourceLocation("entitygandwon").toString()));
    public static final RegistryObject<EntityType<EntityLaffey>> LAFFEY = ENTITIES.register("entitylaffey", () -> EntityType.Builder.of(EntityLaffey::new, EntityClassification.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entitylaffey").toString()));
    public static final RegistryObject<EntityType<EntityZ23>> Z23 = ENTITIES.register("entityz23", () -> EntityType.Builder.of(EntityZ23::new, EntityClassification.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityz23").toString()));
    public static final RegistryObject<EntityType<EntityJavelin>> JAVELIN = ENTITIES.register("entityjavelin", () -> EntityType.Builder.of(EntityJavelin::new, EntityClassification.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString()));
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> EntityType.Builder.of(EntityAyanami::new, EntityClassification.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString()));

    public static void RegisterAttributes(){
        GlobalEntityTypeAttributes.put(AYANAMI.get(), EntityAyanami.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(JAVELIN.get(), EntityJavelin.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(GANGWON.get(), EntityGangwon.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(ENTERPRISE.get(), EntityEnterprise.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(PLANE_F4FWILDCAT.get(), EntityF4fWildcat.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(MISSILEDRONE.get(), EntityMissileDrone.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(SHIROKO.get(), EntityShiroko.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(NAGATO.get(), EntityNagato.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(CHEN.get(), EntityChen.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(ROSMONTIS.get(), EntityRosmontis.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(TALULAH.get(), EntityTalulah.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(Z23.get(), EntityZ23.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(LAFFEY.get(), EntityLaffey.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(M4A1.get(), EntityM4A1.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(AMIYA.get(), EntityAmiya.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(MUDROCK.get(), EntityMudrock.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(TEXAS.get(), EntityTexas.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(FROSTNOVA.get(), EntityFrostnova.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(LAPPLAND.get(), EntityLappland.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(SIEGE.get(), EntitySiege.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(SCHWARZ.get(), EntitySchwarz.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(NEARL.get(), EntityNearl.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(SYLVI.get(), EntitySylvi.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(YAMATO.get(), EntityYamato.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(CLAYMORE.get(), EntityClaymore.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(CROWNSLAYER.get(), EntityCrownSlayer.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(YATO.get(), EntityYato.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(ARTORIA.get(), EntityArtoria.MutableAttribute().build());
    }

    public static void loadClass(){
        Main.LOGGER.info("Registering entities....");
    }

}
