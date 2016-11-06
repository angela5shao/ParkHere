package csci310.parkhere.resource;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import resource.Time;

/**
 * Reference: by Nilanchala Panigrahy on 8/24/16.
 *            https://github.com/npanigrahy/Custom-Calendar-View/blob/master/library/src/com/stacktips/view/utils/CalendarUtils.java
 */

public class CalendarUtils {

    public static boolean isSameMonth(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return false;
        return (c1.get(Calendar.ERA) == c2.get(Calendar.ERA)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
    }

    /**
     * <p>Checks if a calendar is today.</p>
     *
     * @param calendar the calendar, not altered, not null.
     * @return true if the calendar is today.
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar calendar) {
        return isSameDay(calendar, Calendar.getInstance());
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null)
            throw new IllegalArgumentException("The dates must not be null");
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static int getTotalWeeks(Calendar calendar) {
        if (null == calendar) return 0;
        int maxWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        return maxWeeks;

    }

    public static int getTotalWeeks(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getTotalWeeks(cal);
    }

    public static boolean isPastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (date.before(calendar.getTime())) ? true : false;
    }

    // Customize function !!!
    public static boolean isBetweenDay(Date date, Date startDate, Date endDate) {
        int compare = startDate.compareTo(date) * date.compareTo(endDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        boolean ifEndDate = ((endCal.get(Calendar.YEAR)==dateCal.get(Calendar.YEAR)) &&
                (endCal.get(Calendar.MONTH)==dateCal.get(Calendar.MONTH)) &&
                (endCal.get(Calendar.DAY_OF_MONTH)==dateCal.get(Calendar.DAY_OF_MONTH)));

        if(compare >=0 || ifEndDate) return true;
        return false;
    }

    public static boolean isBetweenDay(Date date, Time startTime, Time endTime) {
        GregorianCalendar start = new GregorianCalendar(startTime.year, startTime.month,
                startTime.dayOfMonth, startTime.hourOfDay, startTime.minute);
        GregorianCalendar end = new GregorianCalendar(endTime.year, endTime.month,
                endTime.dayOfMonth, endTime.hourOfDay, endTime.minute);
        Date startDate = new Date(start.getTimeInMillis());
        Date endDate = new Date(end.getTimeInMillis());

        int compare = startDate.compareTo(date) * date.compareTo(endDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        boolean ifEndDate = ((endCal.get(Calendar.YEAR)==dateCal.get(Calendar.YEAR)) &&
                (endCal.get(Calendar.MONTH)==dateCal.get(Calendar.MONTH)) &&
                (endCal.get(Calendar.DAY_OF_MONTH)==dateCal.get(Calendar.DAY_OF_MONTH)));

        if(compare >=0 || ifEndDate) return true;
        return false;
    }

}
