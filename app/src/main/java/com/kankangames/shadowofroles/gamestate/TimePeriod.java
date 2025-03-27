package com.kankangames.shadowofroles.gamestate;

import java.util.Objects;

public final class TimePeriod {

    private Time time;
    private int dayCount;

    public TimePeriod(Time time, int dayCount) {
        this.time = time;
        this.dayCount = dayCount;
    }

    public static TimePeriod of(Time time, int dayCount){
        return new TimePeriod(time, dayCount);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        TimePeriod that = (TimePeriod) object;
        return dayCount == that.dayCount && time == that.time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, dayCount);
    }

    public Time time() {
        return time;
    }

    public void incrementDayCount(){
        dayCount++;
    }
    public int dayCount() {
        return dayCount;
    }

    public TimePeriod setTime(Time time) {
        this.time = time;
        return this;
    }

    public TimePeriod setDayCount(int dayCount) {
        this.dayCount = dayCount;
        return this;
    }
}
