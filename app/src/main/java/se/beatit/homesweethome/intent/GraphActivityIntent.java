package se.beatit.homesweethome.intent;

import android.content.Context;
import android.content.Intent;

import se.beatit.homesweethome.GraphActivity;
import se.beatit.homesweethome.time.TimeSpan;

/**
 * Created by stefan on 2015-05-02.
 */
public class GraphActivityIntent {

    private static String TIMESPAN = "TIMESPAN";

    private Intent intent;
    private Context packageContext;

    public GraphActivityIntent(final Context packageContext, TimeSpan timeSpan) {
        this.packageContext = packageContext;
        intent = new Intent(packageContext, GraphActivity.class);
        intent.putExtra(TIMESPAN, timeSpan);
    }

    public GraphActivityIntent(Intent intent) {
        this.intent = intent;
    }

    public TimeSpan getTimeSpan() {
        return (TimeSpan)intent.getSerializableExtra(TIMESPAN);
    }

    public void startActivity() {
        packageContext.startActivity(intent);
    }

    public boolean hasTimeSpan() {
        return (TimeSpan)intent.getSerializableExtra(TIMESPAN) != null;
    }
}
