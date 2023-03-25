package com.yor42.projectazure.data.common;

import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.SlotTypePreset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CuriosItemTagProvider extends ItemTagProvider {

    public static Map<SlotTypePreset, ArrayList<Item>> CURIOITEMS = new HashMap<>();

    public CuriosItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, existingFileHelper);
    }

    @Override
    protected void addTags() {
        CURIOITEMS.forEach((slot, itemlist)-> CuriosSlot(slot, itemlist.toArray(new Item[0])));
    }

    private void CuriosSlot(SlotTypePreset slottype, Item... item){
        this.tag(curios(slottype.getIdentifier())).add(item);
    }

    private static Tag.Named<Item> curios(String path) {
        return ItemTags.bind(new ResourceLocation("curios", path).toString());
    }

    @Nonnull
    @Override
    public String getName() {
        return "Project Azure-Curios Tag Provider";
    }
}
