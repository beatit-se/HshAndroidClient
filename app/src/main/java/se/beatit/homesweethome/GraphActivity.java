package se.beatit.homesweethome;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jjoe64.graphview.GraphView;

import java.util.Calendar;
import java.util.Date;

import se.beatit.homesweethome.intent.GraphActivityIntent;
import se.beatit.homesweethome.io.restclient.History;
import se.beatit.homesweethome.io.restclient.IotHomeRestRoot;
import se.beatit.homesweethome.io.restclient.RetrofitRestClient;
import se.beatit.homesweethome.time.DateFormatter;
import se.beatit.homesweethome.time.TimeSpan;
import se.beatit.homesweethome.ui.Graph;


public class GraphActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<History> {

    private TimeSpan timeSpan;
    private Graph theGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        showProgressBar();

        theGraph = new Graph((GraphView)findViewById(R.id.graph));
        GraphActivityIntent graphActivityIntent = new GraphActivityIntent(getIntent());

        if(graphActivityIntent.hasTimeSpan()) {
            timeSpan = graphActivityIntent.getTimeSpan();
        } else {
            timeSpan = new TimeSpan(-7,0,TimeSpan.RES_DAY);
        }

        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_change_time_span) {
            showCalendarClicked(null);
            return true;
        }
        return false;
    }

    @Override
    public Loader<History> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<History>(this) {

            @Override
            public History loadInBackground() {
                try {
                    DateFormatter df = new DateFormatter(timeSpan);
                    System.out.println("Starts to load in bg, from " + df.getDisplayableFrom() + " to " + df.getDisplayableTo());

                    String serverBaseUri = getServerBaseUri();
                    IotHomeRestRoot restRoot = RetrofitRestClient.getIoTHomeServerRestClient(serverBaseUri);
                    return restRoot.gethistoricUsage("furubo", df.getRestFormattedFrom(), df.getRestFormattedTo(), timeSpan.getResolution());
                } catch(Exception e) {
                    System.out.println("Failed to load ne historic data " + e.toString());
                    return new History();
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, History history) {
        System.out.println("Loader done, updating graph");

        theGraph.updateGraph(timeSpan, history);
        hideProgressBar();
        theGraph.invalidate();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        System.out.println("On loader reset!!!!");
    }

    public void showTodayClicked(View view) {
        findViewById(R.id.showDayButton).setBackgroundColor(getResources().getColor(R.color.color_button_selected));
        updateGraphWithNewTimeSpan(new TimeSpan(0, 1, TimeSpan.RES_HOUR));
    }

    public void showWeekClicked(View view) {
        updateGraphWithNewTimeSpan(new TimeSpan(-6, 1, TimeSpan.RES_DAY));
    }

    public void showMonthClicked(View view) {
        Calendar calFrom = Calendar.getInstance();
        calFrom.set(Calendar.DAY_OF_MONTH, 1);
        Date to = new Date();

        updateGraphWithNewTimeSpan(new TimeSpan(calFrom.getTime(), to, TimeSpan.RES_DAY));

    }

    public void showYearClicked(View view) {
        Calendar calFrom = Calendar.getInstance();
        calFrom.set(Calendar.MONTH, Calendar.JANUARY);
        calFrom.set(Calendar.DAY_OF_MONTH, 1);
        Date to = new Date();

        updateGraphWithNewTimeSpan(new TimeSpan(calFrom.getTime(), to, TimeSpan.RES_MONTH));
    }

    public void showCalendarClicked(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void setMonthResClicked(View view) {
        timeSpan.setResolution(TimeSpan.RES_MONTH);
        updateGraphWithNewTimeSpan(timeSpan);
    }

    public void setDayResClicked(View view) {
        timeSpan.setResolution(TimeSpan.RES_DAY);
        updateGraphWithNewTimeSpan(timeSpan);
    }

    public void setHourResClicked(View view) {
        timeSpan.setResolution(TimeSpan.RES_HOUR);
        updateGraphWithNewTimeSpan(timeSpan);
    }

    public void setMinuteResClicked(View view) {
        timeSpan.setResolution(TimeSpan.RES_MINUTE);
        updateGraphWithNewTimeSpan(timeSpan);
    }

    private void updateGraphWithNewTimeSpan(TimeSpan ts) {
        GraphActivityIntent graphActivityIntent = new GraphActivityIntent(this, ts);
        graphActivityIntent.startActivity();
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private String getServerBaseUri() {
        Resources res = getResources();
        return res.getString(R.string.server_url);
    }

}
