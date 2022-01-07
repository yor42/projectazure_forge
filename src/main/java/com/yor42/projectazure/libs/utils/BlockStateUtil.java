package com.yor42.projectazure.libs.utils;

import net.minecraft.util.Direction;

public class BlockStateUtil {
    public static RelativeDirection getRelativeDirection(Direction direction, Direction MachineFacings) {
        switch (MachineFacings) {
            case NORTH:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.UP;
                    case NORTH:
                        return RelativeDirection.BACK;
                    case EAST:
                        return RelativeDirection.RIGHT;
                    case WEST:
                        return RelativeDirection.LEFT;
                    case SOUTH:
                        return RelativeDirection.FRONT;
                    case DOWN:
                        return RelativeDirection.DOWN;
                }
            }
            case EAST:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.UP;
                    case NORTH:
                        return RelativeDirection.LEFT;
                    case EAST:
                        return RelativeDirection.FRONT;
                    case WEST:
                        return RelativeDirection.BACK;
                    case SOUTH:
                        return RelativeDirection.RIGHT;
                    case DOWN:
                        return RelativeDirection.DOWN;
                }
            }
            case WEST:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.UP;
                    case NORTH:
                        return RelativeDirection.RIGHT;
                    case EAST:
                        return RelativeDirection.FRONT;
                    case WEST:
                        return RelativeDirection.BACK;
                    case SOUTH:
                        return RelativeDirection.LEFT;
                    case DOWN:
                        return RelativeDirection.DOWN;
                }
            }
            case SOUTH:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.UP;
                    case NORTH:
                        return RelativeDirection.BACK;
                    case EAST:
                        return RelativeDirection.LEFT;
                    case WEST:
                        return RelativeDirection.RIGHT;
                    case SOUTH:
                        return RelativeDirection.FRONT;
                    case DOWN:
                        return RelativeDirection.DOWN;
                }
            }
            case UP:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.FRONT;
                    case NORTH:
                        return RelativeDirection.UP;
                    case EAST:
                        return RelativeDirection.LEFT;
                    case WEST:
                        return RelativeDirection.RIGHT;
                    case SOUTH:
                        return RelativeDirection.DOWN;
                    case DOWN:
                        return RelativeDirection.BACK;
                }
            }
            case DOWN:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.BACK;
                    case NORTH:
                        return RelativeDirection.UP;
                    case EAST:
                        return RelativeDirection.LEFT;
                    case WEST:
                        return RelativeDirection.RIGHT;
                    case SOUTH:
                        return RelativeDirection.DOWN;
                    case DOWN:
                        return RelativeDirection.FRONT;
                }
            }
        }
        return RelativeDirection.FRONT;
    }

    public enum RelativeDirection {
        FRONT,
        LEFT,
        RIGHT,
        BACK,
        UP,
        DOWN;
    }
}
