package com.yor42.projectazure.mixin;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.util.Splashes;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@OnlyIn(Dist.CLIENT)
@Mixin(Splashes.class)
public class MixinSplashManager {

    @Inject(method = "apply(Ljava/util/List;Lnet/minecraft/resources/IResourceManager;Lnet/minecraft/profiler/IProfiler;)V", at = @At("HEAD"))
    private void onApply(List<String> p_212853_1_, IResourceManager p_212853_2_, IProfiler p_212853_3_, CallbackInfo ci){
        p_212853_1_.addAll(addCustomSplashes());
    }

    private static Collection<String> addCustomSplashes() {
        ArrayList<String> entries = new ArrayList<>();

        entries.add("Creeper? Aw man!");
        entries.add("PROJECT: AZURE!");
        entries.add("Coding since 2009!");
        entries.add("I came to dig dig dig dig...");
        entries.add("vibing parrots included!");
        entries.add("Never gives you up!");
        entries.add("Never lets you down!");
        entries.add("Feeling good might crash later");
        entries.add("We V I B I N '");
        entries.add("0 Sanity!");
        entries.add("No microtransactions!");
        entries.add("Never eat originium prime!");
        entries.add("C R U N C H Y");
        entries.add("Q U A C C");
        entries.add("MAXIMUM OVERDRIVE");
        entries.add("Bane?");
        entries.add("MAKING MOTHER OF ALL CAKES");
        entries.add("no bulli ice bnnui");
        entries.add("Stop eating our mushrooms Kay!");
        entries.add("IT'S GOOD FOR YOU!!!!");
        entries.add("MR FOOOOOX!!");
        entries.add("help I'm stuck in the game!!!!!ANYON#*&$N321E!?!?!#&*$#");
        entries.add("");
        entries.add("How do I craft this again?");
        entries.add("Nobody expects minecrafish inquisition!");
        entries.add("Bring forth the holy TNT of antioch");
        entries.add("NEVER divide with 0!");
        entries.add("This sentence is false!");
        entries.add("weeeeeeeee");
        entries.add("NOOT NOOT");
        entries.add("Based!");
        entries.add("10/10 -yor42");
        entries.add("가즈아ㅏㅏㅏㅏㅏ");
        entries.add("Are ya ready kids?");
        entries.add("NANOBLOCKS, SON!");
        entries.add(":pogchamp:");
        entries.add("So we back in the mine");
        entries.add("Got our pickaxe swingin' from side to side");
        entries.add("Can cause cancer in the state of california");
        entries.add("Primogem not included");
        entries.add("Originium included");
        entries.add("MINCERAFT");
        entries.add("aaaaaaa");
        entries.add("*bonk*");
        entries.add("[missing splash text]");
        entries.add("WHO TOOK MY SPAGHET");
        entries.add("*[Reunion stole this splash text]*");
        entries.add("Bro, REALLY COOL!");
        entries.add("Someone stole my splash text!");
        entries.add("*[Sangvis stole this splash text]*");
        entries.add("Dinnergate ate my splash text!");
        entries.add("Ow that's hot");
        entries.add("Steve just slapped the heck out of me");
        entries.add("*professionally vibing*");
        entries.add("Seal of approval");
        entries.add("yor42 seal of quality");
        entries.add("Haha Mixin goes BRRRT");
        entries.add("Oh no!");
        entries.add("NANI!?!?");
        entries.add("OMAE WA MOU SHINDEIRU");
        entries.add("YOUR NEXT MOVE WILL BE PRESSING SINGLEPLAYER BUTTON");
        entries.add("Heh, Ban this, Microsoft!");
        entries.add("What in the world?");
        entries.add("ON 20% SALE! (No, not really.)");
        entries.add("LET THE BASS KICK");
        entries.add("That's not very cash money of you.");
        entries.add("Turing complete!");
        entries.add("look ma! I'm in the video game!");
        entries.add("Aw dang! here we go again.");
        entries.add(")O) (O( )O) (O(");
        entries.add("Factories must grow!");
        entries.add("ARKNI- Wait wrong game.");
        entries.add("AZURU LA- Oh wait...");
        entries.add("Project azure: <3 from south korea.");
        entries.add("E.");
        entries.add("ᗜˬᗜ");
        entries.add("STANDING HERE I REALIZE");
        entries.add("I Approb");
        entries.add("#Saveminecraft");
        entries.add("MOJANG KEEP YOUR NOSE OUT OF MY SERVER");
        entries.add("My server, my rule!");
        entries.add("Get out of my server mojang!");
        entries.add("Techno Never Dies!");
        entries.add("Nice.");


        return entries;
    }

    @Inject(method = "getSplash", at = @At("HEAD"), cancellable = true)
    private void getSplash(CallbackInfoReturnable<String> cir){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY && MathUtil.rand.nextFloat()<=0.33F){
            cir.setReturnValue("It's Wednesday, My dudes!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 9 && calendar.get(Calendar.DATE) == 18){
            cir.setReturnValue("Happy birthday, yor42!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 8 && calendar.get(Calendar.DATE) == 1){
            cir.setReturnValue("Happy birthday, Gab!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 8 && calendar.get(Calendar.DATE) == 9){
            cir.setReturnValue("Happy birthday, Aoichi!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 9 && calendar.get(Calendar.DATE) == 3){
            cir.setReturnValue("Happy birthday, H2O!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 9 && calendar.get(Calendar.DATE) == 8){
            cir.setReturnValue("Happy birthday, Necrom!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 8 && calendar.get(Calendar.DATE) == 1){
            cir.setReturnValue("Happy birthday, Gab!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 20){
            cir.setReturnValue("◢◤ <3");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 1 && calendar.get(Calendar.DATE) == 16){
            cir.setReturnValue("Happy Arknights anniversary!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 5 && calendar.get(Calendar.DATE) == 8){
            cir.setReturnValue("Happy Girl's frontline anniversary!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 8 && calendar.get(Calendar.DATE) == 17){
            cir.setReturnValue("Happy Azur lane anniversary!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 11 && calendar.get(Calendar.DATE) == 9){
            cir.setReturnValue("Happy Blue archive anniversary!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 9 && calendar.get(Calendar.DATE) == 28){
            cir.setReturnValue("Happy Genshin impact anniversary!");
        }
        else if(calendar.get(Calendar.MONTH) + 1 == 8 && calendar.get(Calendar.DATE) == 15){
            cir.setReturnValue("Happy National Liberation Day of Korea.");
        }
    }


}
