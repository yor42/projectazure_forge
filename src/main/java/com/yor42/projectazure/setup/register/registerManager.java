package com.yor42.projectazure.setup.register;

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
import com.yor42.projectazure.gameobject.entity.companion.sworduser.*;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import com.yor42.projectazure.gameobject.entity.misc.EntityMissileDrone;
import com.yor42.projectazure.gameobject.entity.planes.EntityF4fWildcat;
import com.yor42.projectazure.gameobject.entity.projectiles.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;


public class registerManager {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Constants.MODID);
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Constants.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);


    //entity
    public static final EntityType<EntityAyanami> ENTITYTYPE_AYANAMI = EntityType.Builder.of(EntityAyanami::new, EntityClassification.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString());
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> ENTITYTYPE_AYANAMI);

    public static final EntityType<EntityJavelin> ENTITYTYPE_JAVELIN = EntityType.Builder.of(EntityJavelin::new, EntityClassification.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString());
    public static final RegistryObject<EntityType<EntityJavelin>> JAVELIN = ENTITIES.register("entityjavelin", () -> ENTITYTYPE_JAVELIN);

    public static final EntityType<EntityZ23> ENTITYTYPE_Z23 = EntityType.Builder.of(EntityZ23::new, EntityClassification.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityz23").toString());
    public static final RegistryObject<EntityType<EntityZ23>> Z23 = ENTITIES.register("entityz23", () -> ENTITYTYPE_Z23);

    public static final EntityType<EntityLaffey> ENTITYTYPE_LAFFEY = EntityType.Builder.of(EntityLaffey::new, EntityClassification.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entitylaffey").toString());
    public static final RegistryObject<EntityType<EntityLaffey>> LAFFEY = ENTITIES.register("entitylaffey", () -> ENTITYTYPE_LAFFEY);

    public static final EntityType<EntityGangwon> ENTITYTYPE_GANGWON = EntityType.Builder.of(EntityGangwon::new, EntityClassification.CREATURE).sized(0.572F, 1.35F).build(ModResourceLocation("entitygandwon").toString());
    public static final RegistryObject<EntityType<EntityGangwon>> GANGWON = ENTITIES.register("entitygangwon", () -> ENTITYTYPE_GANGWON);

    public static final EntityType<EntityEnterprise> ENTITYTYPE_ENTERPRISE = EntityType.Builder.of(EntityEnterprise::new, EntityClassification.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entityenterprise").toString());
    public static final RegistryObject<EntityType<EntityEnterprise>> ENTERPRISE = ENTITIES.register("entityenterprise", () -> ENTITYTYPE_ENTERPRISE);

    public static final EntityType<EntityM4A1> ENTITYTYPE_M4A1 = EntityType.Builder.of(EntityM4A1::new, EntityClassification.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entitym4a1").toString());
    public static final RegistryObject<EntityType<EntityM4A1>> M4A1 = ENTITIES.register("entitym4a1", () -> ENTITYTYPE_M4A1);

    public static final EntityType<EntityShiroko> ENTITYTYPE_SHIROKO = EntityType.Builder.of(EntityShiroko::new, EntityClassification.CREATURE).sized(0.572F, 1.575F).build(ModResourceLocation("entityshiroko").toString());
    public static final RegistryObject<EntityType<EntityShiroko>> SHIROKO = ENTITIES.register("entityshiroko", () -> ENTITYTYPE_SHIROKO);

    public static final EntityType<EntityNagato> ENTITYTYPE_NAGATO = EntityType.Builder.of(EntityNagato::new, EntityClassification.CREATURE).sized(0.572F, 1.32F).build(ModResourceLocation("entitynagato").toString());
    public static final RegistryObject<EntityType<EntityNagato>> NAGATO = ENTITIES.register("entitynagato", () -> ENTITYTYPE_NAGATO);

    public static final EntityType<EntityChen> ENTITYTYPE_CHEN = EntityType.Builder.of(EntityChen::new, EntityClassification.CREATURE).sized(0.572F, 1.68F).build(ModResourceLocation("entitychen").toString());
    public static final RegistryObject<EntityType<EntityChen>> CHEN = ENTITIES.register("entitychen", () -> ENTITYTYPE_CHEN);

    public static final EntityType<EntityMudrock> ENTITYTYPE_MUDROCK = EntityType.Builder.of(EntityMudrock::new, EntityClassification.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entitymudrock").toString());
    public static final RegistryObject<EntityType<EntityMudrock>> MUDROCK = ENTITIES.register("entitymudrock", () -> ENTITYTYPE_MUDROCK);

    public static final EntityType<EntityAmiya> ENTITYTYPE_AMIYA = EntityType.Builder.of(EntityAmiya::new, EntityClassification.CREATURE).sized(0.572F, 1.42F).build(ModResourceLocation("entityamiya").toString());
    public static final RegistryObject<EntityType<EntityAmiya>> AMIYA = ENTITIES.register("entityamiya", () -> ENTITYTYPE_AMIYA);

    public static final EntityType<EntityRosmontis> ENTITYTYPE_ROSMONTIS = EntityType.Builder.of(EntityRosmontis::new, EntityClassification.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entityamiya").toString());
    public static final RegistryObject<EntityType<EntityRosmontis>> ROSMONTIS = ENTITIES.register("entityrosmontis", () -> ENTITYTYPE_ROSMONTIS);

    public static final EntityType<EntityTalulah> ENTITYTYPE_TALULAH = EntityType.Builder.of(EntityTalulah::new, EntityClassification.CREATURE).sized(0.572F, 1.78F).build(ModResourceLocation("entitytalulah").toString());
    public static final RegistryObject<EntityType<EntityTalulah>> TALULAH = ENTITIES.register("entitytalulah", () -> ENTITYTYPE_TALULAH);

    public static final EntityType<EntityCrownSlayer> ENTITYTYPE_CROWNSLAYER = EntityType.Builder.of(EntityCrownSlayer::new, EntityClassification.CREATURE).sized(0.572F, 1.66F).build(ModResourceLocation("entitycrownslayer").toString());
    public static final RegistryObject<EntityType<EntityCrownSlayer>> CROWNSLAYER = ENTITIES.register("entitycrownslayer", () -> ENTITYTYPE_CROWNSLAYER);

    public static final EntityType<EntityYato> ENTITYTYPE_YATO = EntityType.Builder.of(EntityYato::new, EntityClassification.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entityyato").toString());
    public static final RegistryObject<EntityType<EntityYato>> YATO = ENTITIES.register("entityyato", () -> ENTITYTYPE_YATO);

    public static final EntityType<EntityTexas> ENTITYTYPE_TEXAS = EntityType.Builder.of(EntityTexas::new, EntityClassification.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitytexas").toString());
    public static final RegistryObject<EntityType<EntityTexas>> TEXAS = ENTITIES.register("entitytexas", () -> ENTITYTYPE_TEXAS);

    public static final EntityType<EntityFrostnova> ENTITYTYPE_FROSTNOVA = EntityType.Builder.of(EntityFrostnova::new, EntityClassification.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entityfrostnova").toString());
    public static final RegistryObject<EntityType<EntityFrostnova>> FROSTNOVA = ENTITIES.register("entityfrostnova", () -> ENTITYTYPE_FROSTNOVA);

    public static final EntityType<EntityLappland> ENTITYTYPE_LAPPLAND = EntityType.Builder.of(EntityLappland::new, EntityClassification.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitylappland").toString());
    public static final RegistryObject<EntityType<EntityLappland>> LAPPLAND = ENTITIES.register("entitylappland", () -> ENTITYTYPE_LAPPLAND);

    public static final EntityType<EntitySiege> ENTITYTYPE_SIEGE = EntityType.Builder.of(EntitySiege::new, EntityClassification.CREATURE).sized(0.572F, 1.72F).build(ModResourceLocation("entitysiege").toString());
    public static final RegistryObject<EntityType<EntitySiege>> SIEGE = ENTITIES.register("entitysiege", () -> ENTITYTYPE_SIEGE);

    public static final EntityType<EntitySchwarz> ENTITYTYPE_SCHWARZ = EntityType.Builder.of(EntitySchwarz::new, EntityClassification.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityschwarz").toString());
    public static final RegistryObject<EntityType<EntitySchwarz>> SCHWARZ = ENTITIES.register("entityschwarz", () -> ENTITYTYPE_SCHWARZ);

    public static final EntityType<EntityNearl> ENTITYTYPE_NEARL = EntityType.Builder.of(EntityNearl::new, EntityClassification.CREATURE).sized(0.572F, 1.71F).build(ModResourceLocation("entitynearl").toString());
    public static final RegistryObject<EntityType<EntityNearl>> NEARL = ENTITIES.register("entitynearl", () -> ENTITYTYPE_NEARL);

    public static final EntityType<EntitySylvi> ENTITYTYPE_SYLVI = EntityType.Builder.of(EntitySylvi::new, EntityClassification.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityschwarz").toString());
    public static final RegistryObject<EntityType<EntitySylvi>> SYLVI = ENTITIES.register("entitysylvi", () -> ENTITYTYPE_SYLVI);

    public static final EntityType<EntityYamato> ENTITYTYPE_YAMATO = EntityType.Builder.of(EntityYamato::new, EntityClassification.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityyamato").toString());
    public static final RegistryObject<EntityType<EntityYamato>> YAMATO = ENTITIES.register("entityyamato", () -> ENTITYTYPE_YAMATO);

    //projectile
    public static final EntityType<EntityCannonPelllet> PROJECTILECANNONSHELL = EntityType.Builder.<EntityCannonPelllet>of(EntityCannonPelllet::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilecannonshell").toString());
    public static final RegistryObject<EntityType<EntityCannonPelllet>> CANNONSHELL = ENTITIES.register("entitycannonshell", () -> PROJECTILECANNONSHELL);

    public static final EntityType<EntityArtsProjectile> PROJECTILEARTS_ENTITYTYPE = EntityType.Builder.<EntityArtsProjectile>of(EntityArtsProjectile::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilearts").toString());
    public static final RegistryObject<EntityType<EntityArtsProjectile>> PROJECTILEARTS = ENTITIES.register("entityartsprojectile", () -> PROJECTILEARTS_ENTITYTYPE);

    public static final EntityType<EntityProjectileTorpedo> PROJECTILETORPEDO = EntityType.Builder.<EntityProjectileTorpedo>of(EntityProjectileTorpedo::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectiletorpedo").toString());
    public static final RegistryObject<EntityType<EntityProjectileTorpedo>> TORPEDO = ENTITIES.register("entitytorpedo", () -> PROJECTILETORPEDO);

    public static final EntityType<EntityMissileDroneMissile> PROJECTILE_DRONE_MISSILE = EntityType.Builder.of(EntityMissileDroneMissile::new, EntityClassification.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectiledrone_missile").toString());
    public static final RegistryObject<EntityType<EntityMissileDroneMissile>> DRONE_MISSILE = ENTITIES.register("projectiledrone_missile", () -> PROJECTILE_DRONE_MISSILE);

    public static final EntityType<EntityThrownKnifeProjectile> PROJECTILE_KNIFE = EntityType.Builder.<EntityThrownKnifeProjectile>of(EntityThrownKnifeProjectile::new, EntityClassification.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectile_knife").toString());
    public static final RegistryObject<EntityType<EntityThrownKnifeProjectile>> THROWN_KNIFE = ENTITIES.register("projectile_knife", () -> PROJECTILE_KNIFE);

    public static final EntityType<EntityRailgunProjectile> PROJECTILE_RAILGUN = EntityType.Builder.<EntityRailgunProjectile>of(EntityRailgunProjectile::new, EntityClassification.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectile_railgun").toString());
    public static final RegistryObject<EntityType<EntityRailgunProjectile>> RAILGUN_PROJECTILE = ENTITIES.register("projectile_railgun", () -> PROJECTILE_RAILGUN);

    //?????
    public static final EntityType<EntityClaymore> ENTITYTYPE_CLAYMORE = EntityType.Builder.of(EntityClaymore::new, EntityClassification.CREATURE).sized(0.7F, 2.31F).build(ModResourceLocation("entityclaymore").toString());
    public static final RegistryObject<EntityType<EntityClaymore>> CLAYMORE = ENTITIES.register("entityclaymore", () -> ENTITYTYPE_CLAYMORE);

    //Planes
    public static final EntityType<EntityF4fWildcat> PLANEF4FWildCat = EntityType.Builder.of(EntityF4fWildcat::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("entityplanef4fwildcat").toString());
    public static final RegistryObject<EntityType<EntityF4fWildcat>> F4FWildCat = ENTITIES.register("planef4fwildcat", () -> PLANEF4FWildCat);

    public static final EntityType<EntityMissileDrone> ENTITYTYPE_MISSILEDRONE = EntityType.Builder.of(EntityMissileDrone::new, EntityClassification.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("missiledrone").toString());
    public static final RegistryObject<EntityType<EntityMissileDrone>> MISSILEDRONE = ENTITIES.register("missiledrone", () -> ENTITYTYPE_MISSILEDRONE);


    public static void register() {
        registerFluids.register();
        registerItems.register();
        registerFluids.register();
        registerTE.register();
        RegisterContainer.register();
        registerBiomes.register();
        registerPotionEffects.register();
        registerRecipes.register();
        RegisterAI.register();
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(eventbus);
        BIOMES.register(eventbus);
        FLUIDS.register(eventbus);
        registerBlocks.BLOCKS.register(eventbus);
        ITEMS.register(eventbus);
        RegisterContainer.CONTAINER.register(eventbus);
        RegisterContainer.TILE_ENTITY.register(eventbus);
        RegisterAI.ACTIVITIES.register(eventbus);
        RegisterAI.MEMORYMODULES.register(eventbus);
        RegisterAI.SENSORS.register(eventbus);
        RegisterAI.SCHEDULES.register(eventbus);
        RegisterAI.POI.register(eventbus);
        RECIPE_SERIALIZERS.register(eventbus);
        EFFECTS.register(eventbus);
    }


}
