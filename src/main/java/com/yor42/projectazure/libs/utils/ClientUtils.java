package com.yor42.projectazure.libs.utils;

import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.item.ItemBlueprint;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.registries.RegistryObject;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {
    public static Level getClientWorld()
    {
        return Minecraft.getInstance().level;
    }

    public static void RegisterModelProperties(){
        ItemProperties.register(RegisterItems.RECHARGEABLE_BATTERY.get(), new ResourceLocation(Constants.MODID, "charge"), (stack, world, entity, seed)-> stack.getCapability(CapabilityEnergy.ENERGY, null).map((energy)-> ((float)energy.getEnergyStored())/((float)energy.getMaxEnergyStored())).orElse(0F));

    }


}
