package com.derbysoft.bookit.common.commons.log;

/**
 * Created by: jason
 * Date: 2012-08-30
 */
public final class LogDetailPair {
    private Object object;
    private Direction direction;

    public LogDetailPair(Object object, Direction direction) {
        this.object = object;
        this.direction = direction;
    }

    public static LogDetailPair build(Object object, Direction direction) {
        return new LogDetailPair(object, direction);
    }
    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
