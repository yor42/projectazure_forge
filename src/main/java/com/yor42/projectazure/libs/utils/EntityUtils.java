package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityUtils {

    public static enums.InteractionPoint getHitPointFromClientEntity(AbstractEntityCompanion EntityIn){
        int hith = getEntityHitHeightByClientPlayer(EntityIn);
        int hits = getEntityHitSideByClientPlayer(EntityIn);

        return (getHitBodyID(getBodyIDFromHeight(hith, EntityIn), getHitAngleID(hits)));
    }

    public static enums.InteractionPoint getHitPointFromEntity(LivingEntity player, AbstractEntityCompanion EntityIn){
        int hith = (int) getEntityHitHeight(player, EntityIn);
        int hits = getEntityHitSide(player, EntityIn);

        return (getHitBodyID(getBodyIDFromHeight(hith, EntityIn), getHitAngleID(hits)));
    }

    public static enums.BodySide getHitAngleID(int angle)
    {
        //right
        if (angle >= 250 && angle < 290)
        {
            return enums.BodySide.RIGHT;
        }
        //front
        else if (angle >= 110 && angle < 250)
        {
            return enums.BodySide.FRONT;
        }
        //left
        else if (angle >= 70 && angle < 110)
        {
            return enums.BodySide.LEFT;
        }
        //back
        else
        {
            return enums.BodySide.BACK;
        }
    }

    public static enums.InteractionPoint getHitBodyID(enums.BodyHeight h, enums.BodySide s)
    {
        switch (h)
        {
            case TOP:
                return enums.InteractionPoint.TOP;
            case HEAD:
                if (s == enums.BodySide.FRONT)
                {
                    return enums.InteractionPoint.FACE;
                }
                else
                {
                    return enums.InteractionPoint.HEAD;
                }
            case NECK:
                return enums.InteractionPoint.NECK;
            case CHEST:
                switch (s)
                {
                    case FRONT:
                        return enums.InteractionPoint.CHEST;
                    case BACK:
                        return enums.InteractionPoint.BACK;
                    default:
                        return enums.InteractionPoint.ARM;
                }
            case BELLY:
                switch (s)
                {
                    case FRONT:
                        return enums.InteractionPoint.BELLY;
                    case BACK:
                        return enums.InteractionPoint.BUTT;
                    default:
                        return enums.InteractionPoint.ARM;
                }
            case UBELLY:
                if (s == enums.BodySide.FRONT)
                {
                    return enums.InteractionPoint.UBELLY;
                }
                else
                {
                    return enums.InteractionPoint.BUTT;
                }
            default:  //leg
                return enums.InteractionPoint.LEG;
        }
    }

    public static int[] getBodyRangeFromHeight(int heightPercent, AbstractEntityCompanion ship)
    {
        if (ship != null)
        {
            int hit = getBodyArrayIDFromHeight(heightPercent, ship);
            byte[] heightArray;

            //check stand or sit
            if (ship.isEntitySleeping()) heightArray = ship.getBodyHeightSit();
            else heightArray = ship.getBodyHeightStand();

            //get body id
            switch (hit)
            {
                case 0:
                    return new int[] {120, heightArray[0]};
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    return new int[] {heightArray[hit-1], heightArray[hit]};
                default:
                    return new int[] {heightArray[5], -20};
            }
        }

        return new int[] {1, 0};
    }

    /** check body cube height array, return arrayIndex of body cube */
    public static int getBodyArrayIDFromHeight(int heightPercent, AbstractEntityCompanion companion)
    {
        int hit = -1;

        if (companion != null)
        {
            byte[] heightArray;

            //check stand or sit
            if (companion.isEntitySleeping()) heightArray = companion.getBodyHeightSit();
            else heightArray = companion.getBodyHeightStand();

            //check height
            for (int i = 0; i < heightArray.length; i++)
            {
                if (heightPercent > heightArray[i])
                {
                    hit = i;
                    break;
                }
            }
        }

        return hit;
    }

    public static enums.BodyHeight getBodyIDFromHeight(int heightPercent, AbstractEntityCompanion ship)
    {
        if (ship != null)
        {
            int hit = getBodyArrayIDFromHeight(heightPercent, ship);

            //get body id
            switch (hit)
            {
                case 0:
                    return enums.BodyHeight.TOP;
                case 1:
                    return enums.BodyHeight.HEAD;
                case 2:
                    return enums.BodyHeight.NECK;
                case 3:
                    return enums.BodyHeight.CHEST;
                case 4:
                    return enums.BodyHeight.BELLY;
                case 5:
                    return enums.BodyHeight.UBELLY;
                default:
                    return enums.BodyHeight.LEG;
            }
        }

        return enums.BodyHeight.LEG;
    }

    /** get entity hit height by player's sight ray (client player sight)
     *  return height percent by entity height
     *
     *  calculation:
     *    x1 = (distance - target.width / 2) * tan(pitch)
     *    x2 = host.posY + host.eyeHeight - target.posY
     *    hit height = x2 - x1
     *
     *  note:
     *    1. too close -> tan approach infinity -> retrurn 110 or -10
     */
    @OnlyIn(Dist.CLIENT)
    public static int getEntityHitHeightByClientPlayer(Entity target)
    {
        return (int) getEntityHitHeight(ClientProxy.getClientPlayer(), target);

    }

    /** get entity hit height by entity's sight ray
     * @return*/
    public static float getEntityHitHeight(Entity host, Entity target)
    {
        float result = 0;

        if (target != null && host != null && target.getHeight() > 0.1F)
        {
            //calc tan(pitch)
            float x1 = (float) Math.tan(host.rotationPitch * (Math.PI/180));

            //check tan infinity
            if (x1 > 30) return -10;		//look down inf
            else if (x1 < -30) return 110;	//look up inf

            //calc distance
            float dist = (float) host.getDistance(target);

            //check dist > 0
            if (dist < 0) dist = 0;

            x1 *= dist;

            float x2 = (float) (host.getPosY() + host.getEyeHeight() - target.getPosY());
            float x = x2 - x1;

            //turn height(float) to percent(int)
            result = (x / target.getHeight()) * 100F;
        }

        return result;
    }

    /** calc entity hit side
     *
     *  front: 180
     *  back:  0
     *  right: 270
     *  left:  90
     */
    @OnlyIn(Dist.CLIENT)
    public static int  getEntityHitSideByClientPlayer(Entity target)
    {
        int result = 0;

        if (target != null)
        {
            float angHost = ClientProxy.getClientPlayer().rotationYawHead;
            float angTarget = 0F;

            if (target instanceof LivingEntity)
            {
                angTarget = ((LivingEntity) target).renderYawOffset;
            }
            else
            {
                angTarget = target.rotationYaw;
            }

            result = (int) ((angHost % 360 - angTarget % 360) % 360);

            if(result < 0) result += 360;
        }

        return result;
    }

    /** calc entity hit side
     *
     *  front: 180
     *  back:  0
     *  right: 270
     *  left:  90
     */
    public static int getEntityHitSide(Entity host, Entity target)
    {
        int result = 0;

        if (host != null && target != null)
        {
            result = (int) ((host.rotationYaw % 360 - target.rotationYaw % 360) % 360);

            if(result < 0) result += 360;
        }

        return result;
    }

}
