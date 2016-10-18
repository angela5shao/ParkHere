package csci310.parkhere.resource;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ivylinlaw on 10/16/16.
 */
public class TimeInterval implements Serializable {

    private static final long serialVersionUID = -1285945581416137744L;

    public GregorianCalendar startTime;
    public GregorianCalendar endTime;

    private long timeIntervalMilliSec;

    public TimeInterval() {
        startTime = new GregorianCalendar();
        endTime = new GregorianCalendar();
        calElapseTime();
    }

    public TimeInterval(GregorianCalendar inStartTime, GregorianCalendar inEndTime) {
        //GregorianCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second)

        startTime = new GregorianCalendar(inStartTime.get(Calendar.YEAR),
                inStartTime.get(Calendar.MONTH),
                inStartTime.get(Calendar.DAY_OF_MONTH),
                inStartTime.get(Calendar.HOUR_OF_DAY),
                inStartTime.get(Calendar.MINUTE),
                inStartTime.get(Calendar.SECOND));
        endTime = new GregorianCalendar(inEndTime.get(Calendar.YEAR),
                inEndTime.get(Calendar.MONTH),
                inEndTime.get(Calendar.DAY_OF_MONTH),
                inEndTime.get(Calendar.HOUR_OF_DAY),
                inEndTime.get(Calendar.MINUTE),
                inEndTime.get(Calendar.SECOND));
        calElapseTime();
    }

    public long elapseSecond() {
        return timeIntervalMilliSec / 1000;
    }

    public long elapseMinute() {
        return timeIntervalMilliSec / (1000 * 60);
    }

    public long elapseHour() {
        return timeIntervalMilliSec / (1000 * 60 * 60);
    }

    public long elapseDay() {
        return timeIntervalMilliSec / (1000 * 60 * 60 * 24);
    }

    public long elapseWeek() {
        return timeIntervalMilliSec / (1000 * 60 * 60 * 24 * 7);
    }

    public long elapseMonth() {
        long diff_month = (endTime.get(Calendar.YEAR) - startTime.get(Calendar.YEAR)) * 12
                + endTime.get(Calendar.MONTH) - startTime.get(Calendar.MONTH);
        return diff_month;
    }

    public long elapseYear() {
        return endTime.get(Calendar.YEAR) - startTime.get(Calendar.YEAR);
    }

    private void calElapseTime() {
        timeIntervalMilliSec = endTime.getTimeInMillis() - startTime.getTimeInMillis();
    }
}
