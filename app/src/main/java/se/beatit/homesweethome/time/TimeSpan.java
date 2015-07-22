package se.beatit.homesweethome.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by stefan on 2015-05-01.
 */
public class TimeSpan implements Serializable {

    private Date fromDate = new Date();
    private Date toDate = new Date();

    //resolution can be y, M, D, h or m
    private String resolution;

    public static final String RES_YEAR = "y";
    public static final String RES_MONTH = "M";
    public static final String RES_DAY = "D";
    public static final String RES_HOUR = "h";
    public static final String RES_MINUTE = "m";

    public TimeSpan(int startDateFromNow, int stopDateFromNow, String resolution) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, startDateFromNow);
        fromDate = cal.getTime();

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, stopDateFromNow+1);
        toDate = cal.getTime();

        this.resolution = resolution;
    }

    public TimeSpan(Date fromDate, Date toDate, String resolution) {
        this.fromDate = fromDate;

        Calendar cal = Calendar.getInstance();
        cal.setTime(toDate);
        cal.add(Calendar.DATE, 1);

        this.toDate = cal.getTime();

        this.resolution = resolution;
    }


    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
