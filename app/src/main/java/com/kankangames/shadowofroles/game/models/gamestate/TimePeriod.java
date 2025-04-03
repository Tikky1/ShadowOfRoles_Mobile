package com.kankangames.shadowofroles.game.models.gamestate;

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

    public boolean isAfter(TimePeriod other) {
        if (this.dayCount > other.dayCount) {
            return true;
        } else if (this.dayCount == other.dayCount) {
            return this.time.compareTo(other.time) > 0;
        }
        return false;
    }

    public int subtract(TimePeriod timePeriod){
        return dayCount - timePeriod.dayCount;
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "time=" + time +
                ", dayCount=" + dayCount +
                '}';
    }
}
