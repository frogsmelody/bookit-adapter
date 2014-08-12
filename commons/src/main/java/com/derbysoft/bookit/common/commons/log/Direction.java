package com.derbysoft.bookit.common.commons.log;

public enum Direction {
    FROM_CHANNEL,
    TO_DSWITCH,
    FROM_DSWITCH,
    TO_CHANNEL;

    public static Direction fromRequest(String code) {
        for (Direction direction : Direction.values()) {
            if (code.contains(direction.name())) {
                return direction;
            }
        }
        return null;
    }
}
