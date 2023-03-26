package com.yor42.projectazure.data;

import com.yor42.projectazure.data.client.BlockModelProvider;
import com.yor42.projectazure.data.client.itemModelProvider;
import com.yor42.projectazure.data.common.BlockTagProvider;
import com.yor42.projectazure.data.common.FluidTagProvider;
import com.yor42.projectazure.data.common.ItemTagProvider;
import com.yor42.projectazure.data.common.RecipeProvider;
import com.yor42.projectazure.data.common.loot.LootTableProvider;
import com.yor42.projectazure.intermod.tconstruct.Tconstruct;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class datagenerator {
    private datagenerator() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper filehelper = event.getExistingFileHelper();
        if(event.includeClient()) {
            gen.addProvider(new BlockModelProvider(gen, filehelper));
            gen.addProvider(new itemModelProvider(gen, filehelper));
        }
        if(event.includeServer()) {
            BlockTagProvider blockTagProvider = new BlockTagProvider(gen, filehelper);
            gen.addProvider(blockTagProvider);

            FluidTagProvider fluidTagProvider = new FluidTagProvider(gen, filehelper);
            gen.addProvider(fluidTagProvider);

            gen.addProvider(new ItemTagProvider(gen, blockTagProvider, filehelper));
            gen.addProvider(new LootTableProvider(gen));
            gen.addProvider(new RecipeProvider(gen));
        }

        if(CompatibilityUtils.isTConLoaded()){
            Tconstruct.runDatagens(event);
        }
    }

}
