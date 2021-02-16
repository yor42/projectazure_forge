package com.yor42.projectazure.libs.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import software.bernie.shadowed.fasterxml.jackson.databind.ser.Serializers;

import javax.swing.text.html.parser.Entity;
import java.util.Random;

public class MathUtil {
    public static Random rand = new Random();

    public static Random getRand() {
        return rand;
    }

    //Oh yeah its Big brain time
    public static float DegreeToRadian(float degree){
        return (float) (degree*(Math.PI/180));
    }

    public static float RadianRoDegree(Float Radian){
        return (float) (Radian*(180/Math.PI));
    }

    public static float LimitAngleMovement(float value, float maxDegree, float minDegree, boolean isValueDegree, boolean ShouldReturnRadian){
        float Value = value;
        float Result;
        if(!isValueDegree){
            Value = RadianRoDegree(value);
        }
        if(Value>maxDegree)
            Result = maxDegree;
        else Result = Math.max(Value, minDegree);

        if(ShouldReturnRadian){
            return DegreeToRadian(Result);
        }
        else
            return Result;
    }

    public static float getRiggingedDamageModifier(){
        return (float) (rand.nextBoolean()? 0.01:0.02);
    }

    public static boolean rollBooleanRNG(float chance){
        return rand.nextFloat()<=chance;
    }

    public static int generateRandomInt(int bound){
        return rand.nextInt(bound+1);
    }

    public static int rollDamagingRiggingCount(int RiggingCount){
        if(RiggingCount>=3) {

            int randomInt = rand.nextInt(10);

            if (randomInt <= 2) {
                return 1;
            } else if (randomInt <= 6) {
                return 2;
            } else if (randomInt <= 8) {
                return 3;
            } else
                return 4;
        }
        else
            return rand.nextInt(RiggingCount+1);

    }

    public static float RangedFloatRandom(float min, float max){
        return  (min+(max-min*rand.nextFloat()));
    }


    //happy valentine day i guess
    public boolean Valentine(){
        int you = 2;
        int me = 2;
        return you + me <3;
    }

    public static Vector4f getNormalizedLookVector(LivingEntity entity1, LivingEntity entity2){
        if(entity1 != null && entity2 != null){
            double x = entity1.getPosX() - entity2.getPosX();
            double y = entity1.getPosY() - entity2.getPosY();
            double z = entity1.getPosY() - entity2.getPosY();
            double dist = MathHelper.sqrt(x * x + y * y + z * z);

            if (dist > 1.0E-4D)
            {
                x = x / dist;
                y = y / dist;
                z = z / dist;

                return new Vector4f(((float) x), ((float)y), ((float)z), ((float)dist));
            }
        }
        return new Vector4f(0,0,0,0);
    }

}
/*
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzz#zzzzzzzzzzzzzzzzzzzzzznnxxMMxxxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMxxnz#############zzzzzzzzz#############znxxxxxxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz###########################++++++++++++##znxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMxz###+++++####################+++++++***+++++++#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz#+++++++++++################++++**************++++zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn#+++++++++++++++#############++++******************+++nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz++++++++++++++++++++#########+++*********i************++zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn#++++++++++++++++++++++#######+++********iiiiiiiii********+#xMMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMxxz+++++***++++++++++++++++++#####+++******iiiiiiiiiiiiiii*****+#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx#++********++++++++++++++++++###+++*****iiiiiiiiiiiiiiiiiii*****#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx#+*********+++++++++++++++++++##+++*****iiiiiiiiiiiiiiiiiiiiii****#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn+************+++++++++++++++++++#+++****iiiiiiiii;;;;;;iiiiiiiii***+zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMxxxxn***************+++++++++++++++++++++****iiiiiiiii;;;;;;;;;iiiiiiii***+zMxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz****************+++++++++++++++++++++****iiiiiiii;;;;;;;;;;;iiiiiiii***+nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz*iiiii************+++++++++++++++++++*****iiiiiii;;;;;;;;;;;;iiiiiiiii**++xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz*iiiiiii************++++++++++++++++++****iiiiiii;;;;;;;;;;;;;iiiiiiiiii**+#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxziiiiiiiiii************+++++++++++++++++****iiiiiii;;;;;;;;;;;iiiiiiiiiiiii*++zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxniii;iiiiiiii***********+++++++++++++#+++***iiiiiiii;;;;;;;;;;iiiiiiiiiiiiii**++xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMx*;;;;;iiiiiiii**********++++++++++++++++****iiiiiiii;;;;;;;;;;iiiiiiiiiiiiiii*++#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMxx+;;;;;;;iiiiiiii**********+++++++++++##++****iiiiiiiii;;;;;;;;iiiiiiiiiiiiiiii**++nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx#;;;;;;;iiiiiiiii**********+++++++++++##++****iiiiiiiii;;;;;;;;iiiiiiiiiiiiiiiii*++#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxni;;;;;;;;iiiiiiiiii*********+++++++++###++****iiiiiiiiii;;;;;;;iiiiiiiiiii;;;;ii**++nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*;;;;;;;;;iiiiiiiiiiii*******+++++++++###++***iiiiiiiiiiii;;;;;;iiiiiiiiiiii;;;iii*++#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz;::;;;;;;;;;iiiiiiiiiiii*****+++++++####++****iiiiiiiiiiii;;;;;;iiiiiiiiiiii;;;;ii**+#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxi;::;;;;;;;;;;;iiiiiiiiiii*****++++++####+++****iiiiiiiiiiii;;;;;iiiiiiiiiiii;;;;;ii*++nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx#;:::;;;;;;;;;;;;;;;;iiiiiii****++++######+++****iiiiiiiiiiiii;;:;iiiiiiiiiiii;;;;;;i**+#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxi:::::;;;;;;;;;;;;;;;;;;iiii****++++######++****iiiiiiiiiiiiii;;:;iii*iiiiiiii;;;;;;ii*+#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx#::::::::::::::;;;;;;;;;;;iiii***++++######++****iiiiiiiiiiiiii;;:;ii***iiiiiii;;;;;;;i*+#nxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxi:::::::::::::::::::::;;;;;iii***++++######++****iiiiiiiiiiiiii;;::;ii***iiiiii;;;;;;;ii*+zMxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxn;:::::::::::::::::::::::;;;iii***++++######++****iiiii**iiiiiii;;::;ii***iiiiii;;;;:;;;i*+zxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxx+::::::::::::::,:::::::::;;;iii***++++######++*****iiii****iiiii;;;:;ii****iiiii;;;;;;;;ii*#xMxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxx;::::::::::,,,,,,,:::::::;;;ii***+++++######++*****iiii*****iiiii;;;;ii****iiiii;;;;;;;;;i*+nMxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxz::::::::::,,,,,,,,,::::::;;;ii***+++++######++*****ii*******iiiiiiiiii*****iiiii;;;;;;;;;ii+nxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxx*;::::;::::,,,,,,,,:::::::;;;ii***+++++######++*****ii******iiiii***********iiiiii;;;;;;;iii*zxMxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxMx;;:::;;::::,,,,,,::::::::;;;ii****++++########+*****iiii***iiii**+####++****iiiiii;;;;;iiii**#xxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxMz;:::;;;;::::,,,:::::::::;;;iii***+++++########++****iiii**ii**+#nxxxxnn#+***iiiiiiiiiiiii**++#xxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxx+;:;;;;;;:::::::::::::;;;;;iii****+++++########++****iiiiiii*+#nMMMxxxxxnz+**iiiiiiiiiii*+#zzzzxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxi;;;;;;;;;;:::::::;;;;;;;;iii*****++++#########++****iiiiii*#nMxnzznnnnxxn#+*iiiiiiiii**#nMMMMnnxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxn;;;;;;;;;;;;;;;;;;;;;;;;iiii*****+++++#####zz##++****iiiii*zxnz#####zzzznnz+*iiiiiiii*+zMWWWMMxnxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxz;;;;;;;;;;;;;;;;;;;iiiiiiii*****++++++#####zz##++****iiii*zn#+++**++#####zz#+*iiiiii*+nMxxxxMMxnxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxx+;;;;;;;;;;;iiiiiiiiiiiiiii*****+++++++#####zz###+***iiii*##++++*****++++++#++*i;;ii*+znzz#znxxxnMxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxx*;;;;;;;iiiiiiiiiiiiiiiiii*****+++++++######zzz##++**iiii++**+++#nxz********+**i;;i*+#z#++++znnxnxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxi;;;;;;iiiiiiiiiiiiiiii*******++++++++######zzz##++**iiii*ii***+M#Wx****iiii****i;;*+##+**+##zznzxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxMni;;;;;iiiiiiii***************++++++++#######zzzz##+**iiiiiii*;++M#Wn*iiiiiiii***ii;*+++++#MWn####nxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxzi;;;;iiiiiiii***************+++++++++#######zzzz##+**ii;;i;;;;++xM##+i;;;;;iiiii*iii+*+*+n#@x#+++zxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxx#i;;;iiiiiii****************+++++++++########zzzz##++*ii;;;;:;;i***iii;::::;iiiii**i;+****z@Mz#+**#xxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxx+iiiiiiiiii****************++++++++++########zzzzz##+*iii;::;;;;;i;;;;:::::;iiiii***i******###+*ii+xxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxx*iiiiiiiii****************++++++++++#########zzzzz##+*iii;;:::;;;;;;;;;::;;iiiiii***ii*******+**ii*xxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxx*iiiiiii*****************+++++++++++########zzzzzzz#+*iiii;;::::;;;;;;;:;;iiiiiii***ii*iii******ii*nxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxn*iiiiii*****************+++++++++++#########zzzzzzz##*iiiii;;;::::::::;;;iiiiiiii****i*iiiii*******nxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxn*iiiii*****************++++++++++++########zzzzzzzz##*iiiiiii;;;::::::;;;iiiii;ii****i+*iiiiii****+nxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz*iiii********+++++++++++++++++++++#########zzzzzzzz##+*iiiiiiii;;;;;;;;;iiiiii;ii****i**iiiiii****+nMxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz*iii******++++++++++++++++++++++##########zzzzzzzzz##+*iiiiiiiiii;;;;;;iiiiiiiiii****i**iiiiii***++nxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz*********+++++++++++++++++++++############zzzzzzzzzz#+**iiiiiiiiiiiiiiiiiiiiiiiii****ii*iiiiii***+#nxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz********++++++++++++++++++++##############zzzzzzzzzz##********iiiiiiiiiiiiiiiiiii*****i*iiiiii**++#xxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz*******+++++++++++++++++++###############zzzzzzzzzzz##+********iiiiiiiiiiiiiiiiii*****i*iiiii***+#zxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz******+++++++++++++++++++################zzzzzzzzzzz##+***********iiiiiiiiiiiiiii*****i*iiii***++#zxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz+****++++++++++++++++++++###############zzzzzzzzzzzz##++***********iiiiiiiiiiiiii*****i*iii****+##nxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz++++++++++++######++++++################zzzzzzzzzzzz###++*************iiiiiiiiiii*****ii*ii***++#znxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz++++++++++##########++################zzzzzzzzzzzzzz###++********************iiii******i*i****+##zxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxz++++++++##############################zzzzzzzzzzzzzz###++********************iiii******i*****++#zzxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxn+++++++#############################zzzzzzzzzzzzzzzz###+++*******************iiii******i****++##znxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxn#++++##############################zzzzzzzzzzzzzzzzz###+++++*****************iiii*i****ii+*+++##znxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxx###+#############################zzzzzzzzzzzzzzzzzzz####++++******************iiiiii****i++++##zzxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxx###########################zzzzzzzzzzzzzzzzzzzzzzzzz####++++******************iiiiii****i*++###zzxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxx##########################zzzzzzzzzzzzzzzzzzzzzzzzzz####++++++****************i;;iii****i*#####znxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxz########################zzzzzzzzzzzzzzzzzzzzzzzzzzz####++++++**********++++++i;iii*****ii####zznxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxz#######################zzzzzzzzzzzzzzzzzzzzzzzzzzzz####++++++*********++++++*i***ii****i*####zzxMxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxMn############zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz###+++++**+******++++++#*+###+*i***++####zzxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxMnz#######zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz####++++**+******++++++##nnxxnz##zzznz###znxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxz#####zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz####++++*********++++++#nnnnnxMMWMMn####zznxMxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxzz###zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz####++++*********+++++##zznnxMWWMz######zzxxMxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzznnzzzzzzzzzz####++++++*********+++#zzznnxxMxz#+++###zzxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzznnnnnnzzzzzzzzzz###++++++***********+#nnzzznnnz#+++++##zzxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzznnnnnzzzzzzzzzz###+++++************+znnnnnnnz++++++##zznxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzznnnnnzzzzzzzzzz###+++++************+znnnnnnz+**++++##zznxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnzzzzzzzzzz##++++*************+#zznnnz+*i**+++##zznxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnnzzzzzzzzzz###+++****++*******+##zzzz+*ii**+++#zzzxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnnnnzzzzzzzzz###++++++++++*******++###+iiii*+++#zzznxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnnnnnzzzzzzzzz###++++++++++++*******+++*iii*#####zzznxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnnnnnzzzzzzzzz###++++++++++++++++##zzzz#++#nxnnzzzzzxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnnnnnnzzzzzzzzz###++++++++++#zzz#######znz#++znnzzznxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnnnnnnnzzzzzzzzz###++++++++++#zzzzzz#+++++##zxxz##zznxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnnnnnnnnzzzzzzzzz###+++++++++++#znnnnnnnnxxMMn#++#zzxMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnnnnnnnnnzzzzzzzzz##+++++++++*++#znxxxxMMWMxz+*++#znxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzznnnnnnnnnnnnnnnnnnnnnnnnnnzzzzzzzzzz##+++++++****+#znxxxMMxz#****#zzxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzznnnnnnnnnnnxxnnnnnnnnnnnnzzzzzzzzzz###++++++*****+#znnnnz#**ii*+#znxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzzznnnnnnxnnnnxxxxnnnnnnnnnnnzzzzzzzzzz##++++++****i**+##++*iiiii*+znxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxMnnzzzzzzzzzzzzzzzzznnnnnnxnnnnxxxxxnnnnnnnnnnnzzzzzzzzz###+++++****iii***iii;;;ii*#znMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzzzzzzzzzzzzznnnnnnxnnnnxxxxxxnnnnnnnnnnnzzzzzzzzz###++++****iiiiiii;;;;;i*+#nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzzzz##zzzzzzzzzznnnnnxnnnnnxxxxxxxxnnnnnnnnnnzzzzzzzz###++++***iiiiiii;;;;ii*+zxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMnnzzzz#####zzzzzzzznnnnnxnnnnnxxxxxxxxxnnnnnnnnnnzzzzzzz####+++***iiiiiii;;;;ii*#nxMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMxnzz#z######zzzzzzznnnnnxxnnnnnxxxxxxxxxnnnnnnnnnnnzzzzzz###++++***iiiiii;;;ii*+zxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzzzz########zzzzzznnnnxxnnnnnnxxxxxxxxxnnnnnnnnnnzzzzzzz###+++***iiiiiiiiiii*#nxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzz##########zzzzzznnnnnxnnnnnnnxxxxxxxxxxnnnnnnnnnnzzzzz####+++***iiiiiiiii*+#xMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMnzzz##########zzzzznnnnnxnnnnnnnxxxxxMxxxxxxnnnnnnnnnzzzzz####+++**iiiiiiiii*+zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxzzz###########zzzzznnnnxnnnnnnnnxxxxMMMMxxxxxnnnnnnnnzzzzz####++***iiiii****+nMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzz###########zzzzzznnnnnnnnnnnnnxxxMMMMMMxxxxxnnnnnnnzzzzz###+++***iii*****#xMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzz############zzzzznnnnnnnnnnnnnxxxxMMMMMMMxxxxxnnnnnnzzzzz###+++**ii******#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnzz#############zzzzznnnnnnnnnnnnnxxxxMMMMMMMMxxxxxnnnnnzzzzzz##++**********zMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxzz##############zzzznnnnnnnnnnnnnnxxxxMMMWMMMMMxxxxxnnnnzzzzz###++****++**+nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxzz##############zzzzznnnnnnnnnnnnnnxxxMMMWWWWMMMMxxxxxnnnnzzzz###++**+++++#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxzz###############zzzznnnnnnnnnnnnnnxxxxMMMWWWWWWMMMxxxxxnnnzzzz##+++++##+#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn################zzzzznnnnnnnnnnnnnnnxxxMMMWWW@WWWMMMxxxxnnnnzzzz##+#zzzzznxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxzz################zzzzznnnnzzzznnnnnnnxxxMMMWWW@@@@WWMMxxxxxnnnzzzzzznxnnxMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMnz################zzzzznnnzzzzzznnnnnnnxxxxMMMWWW@@@@WWMMxxxxxxnnnnxMMMMMMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnz####++++++######zzzzznnzzzzzzzzznnnnnnxxxxxMMMMMMWWWWWWWMxxxxxxxMMWWMWMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnz####++++++#######zzzzzzzzzzzzzzzzznnnnnnnxxxxxxMMMMMMWW@WMMMMxxMWWWWWMxxMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnz####++++++########zzzzzzzzzzzzzzzzzznnnnnnnnnxxxxxMMMWWWMxMMMMMMMMMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnz####++++++########zzzzzzzzzzzzzzzzzzznnnnnnnnnnxxxMMMWWMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz####++++++#########zzzzzzzzzzzzzzzzzzzznnnnnnnnxxxMMMWWMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMxxz####++++++#########zzzzzzzzzzzzzzzzzzzzznnnnnnxxxxMMMWMMxxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz####++++++#########zzzzzzzzzzzzzzzzzzzzznnnnnnnxxxMMMMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz####++++++#########zzzzzzzzzzzzzzzzzzzzzznnnnnnxxxMMMMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz####++++++#########zzzzzzzzzzzzzzzzzzzzzznnnnnnxxxMMMxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz####++++++#########zzzzzzzzzzzzzzzzzzzzzznnnnnnxxxMMxxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz###++++++++#########zzzzzzzzzzzzzzzzzzzzznnnnnxxxxMxnxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz###++++++++#########zzzzzzzzzzzzzzzzzzzzznnnnnxxxxxnnxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnz##+++++++++#########zzzzzzzzzzzzzzzzzzzzznnnnnxxxxxznxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxnz##+++++++++++########zzzzzzzzzzzzzzzzzzzznnnnnxxxxzznxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn###+++++++++++########zzzzzzzzz#zzzzzzzzzznnnnnxxxn#znxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn###+++++++++++++#######zzzz######zzzzzzzzznnnnnxxnz##nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn###++++++++++++++#######zz########zzzzzzzzznnnnxnz###zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn##++++++++++++++++#################zzzzzzzznnnnnz#+##zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz##++++++++++++++++++###############zzzzzzzznnnnn#++##zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz##+++++++++++++++++++###############zzzzzzznnnn#++++#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMx##+++++++++++++++++++++##############zzzzzzznnnz+**++#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx##++++++++++++++++++++++++###########zzzzzzzznz#***+++zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn#++++++*******+++++++++++++++#########zzzzzzzz#*****++#nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz#++++**************++++++++++++++#####zzzzzzz#+*****++#zMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz#+++******************+++++++++++#####zzzzzzz+*ii****+#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx#+++****************************+++####zzzzzz#*iiii***++#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn#++******************************+++####zzzz#+iiiii****+#nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz+++*******************************++####zzzz#*iiiiii***+#zMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz++********************************++####zzz#+iiiiiiii**++zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx#++*********************************++###zzz+*i;;iiiii***+#xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx#+++************iiii****************++####z+*i;;;iiiii***+#nMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxn#+++************iiiiiiii*****iiii***+++###+*i;;;;;iiiii**+#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz#++++************iiii********iii***+++##+*i;;;;;;iiiii**+#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxz##++++***********************iii***++++**i;;;;;;;iiii***+#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMnz###++++**********************i********ii;;;;;;;iiiii***+#nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMMnzz###+++++************************i*iii;;;;;;iiiiii***++#nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMWMxnzz####+++++++*****************iiiii;;;iiiiiiiiii***++#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMWMMxnnzzz####++++++++++++++++****iiiiiiiiiiiiiiii****++#zxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMMWWMMxxnnzzzz##########+++++++++**iiiiiiiiiiii*****++#znxMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMWWWMMMxxxnnnnzzzzzzzz#######+++**************+++##znxMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMWWWMMMxxxxxxnnnnnnzzzzzzzz###++++++++++++####znnxMWMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMxxxxMMWWWWWMMMxxxxxnnnnnnnnnzzzzzzzzz##zzzzzzznnxxMWWMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMMWWWWWWMMMMxxxxxxxxnnnnnnnnnnnnxxxxxxMMWWWWMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxMMMWWWWWWWWMMMMMMMMxxxxxxMMMMMMMWWWWWWMMxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
 /$$      /$$            /$$$$$$
| $$$    /$$$           /$$__  $$
| $$$$  /$$$$  /$$$$$$ | $$  \__//$$$$$$$
| $$ $$/$$ $$ |____  $$| $$$$   /$$_____/
| $$  $$$| $$  /$$$$$$$| $$_/  |  $$$$$$
| $$\  $ | $$ /$$__  $$| $$     \____  $$
| $$ \/  | $$|  $$$$$$$| $$     /$$$$$$$/
|__/     |__/ \_______/|__/    |_______/
Im sorry.
 */