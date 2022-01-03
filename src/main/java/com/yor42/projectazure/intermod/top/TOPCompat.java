package com.yor42.projectazure.intermod.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.InterModComms;

import java.util.function.Function;

public class TOPCompat {
    private static boolean registered;
    public static void register() {
        if (registered)
            return;
        registered = true;
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
    }


    public static class GetTheOneProbe implements Function<ITheOneProbe,Void>{

        @Override
        public Void apply(ITheOneProbe iTheOneProbe) {
            iTheOneProbe.registerEntityProvider(new EntityInfoProvider());
            return null;
        }
    }
}
