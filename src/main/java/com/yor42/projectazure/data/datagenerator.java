package com.yor42.projectazure.data;

import com.yor42.projectazure.data.client.BlockModelProvider;
import com.yor42.projectazure.data.client.itemModelProvider;
import com.yor42.projectazure.data.common.BlockTagProvider;
import com.yor42.projectazure.data.common.ItemTagProvider;
import com.yor42.projectazure.data.common.RecipeProvider;
import com.yor42.projectazure.data.common.loot.LootTableProvider;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class datagenerator {
    private datagenerator() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper filehelper = event.getExistingFileHelper();

        gen.addProvider(new BlockModelProvider(gen, filehelper));
        gen.addProvider(new itemModelProvider(gen, filehelper));

        BlockTagProvider blockTagProvider = new BlockTagProvider(gen, filehelper);
        gen.addProvider(blockTagProvider);
        gen.addProvider(new ItemTagProvider(gen, blockTagProvider, filehelper));
        gen.addProvider(new LootTableProvider(gen));
        gen.addProvider(new RecipeProvider(gen));
    }

}
