package com.yor42.projectazure.setup.register;

import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.definition.PartDefinition;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.swing.plaf.multi.MultiButtonUI;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class registerMultiBlocks {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerMultiBlocksonEvent(RegistryEvent.Register<Block> event){
        HatchTE.registerItemHatch();
        OriginiumGeneratorControllerTE.registerTE();
        AmmoPressControllerTE.registerTE();
        RiftwayControllerTE.registerTE();
        AdvancedAlloySmelterControllerTE.registerTE();
        MbdComponents.registerComponentFromResource(Multiblocked.GSON, ResourceUtils.ModResourceLocation("controller/projectazure_silicone_crucible"), ControllerDefinition::new, null);
    }

    public static void register(){}

}
