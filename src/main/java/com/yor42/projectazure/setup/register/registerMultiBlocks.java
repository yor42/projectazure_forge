package com.yor42.projectazure.setup.register;

import com.lowdragmc.multiblocked.Multiblocked;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches.HatchTE;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.swing.plaf.multi.MultiButtonUI;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class registerMultiBlocks {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerMultiBlocksonEvent(RegistryEvent.Register<Block> event){
        HatchTE.registerItemHatch();
        OriginiumGeneratorControllerTE.registerTE();
        AmmoPressControllerTE.registerTE();
        RiftwayControllerTE.registerTE();
        AdvancedAlloySmelterControllerTE.registerTE();
    }

    public static void register(){}

}
