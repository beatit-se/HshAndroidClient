package se.beatit.homesweethome.time;

import java.text.SimpleDateFormat;

/**
 * Created by stefan on 2015-05-01.
 */
public class DateFormatter {
    private final static SimpleDateFormat restDateFormatter = new SimpleDateFormat("yyyyMMdd2400");
    private final static SimpleDateFormat graphDateFormatter = new SimpleDateFormat("MMM d");

    private TimeSpan timeSpan;

    public DateFormatter(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getRestFormattedFrom() {
        return restDateFormatter.format(timeSpan.getFromDate());
    }

    public String getRestFormattedTo() {
        return restDateFormatter.format(timeSpan.getToDate());
    }

    public String getDisplayableFrom() {
        return graphDateFormatter.format(timeSpan.getFromDate());
    }

    public String getDisplayableTo() {
        return graphDateFormatter.format(timeSpan.getToDate());
    }
}
