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
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

import se.beatit.homesweethome.intent.GraphActivityIntent;
import se.beatit.homesweethome.io.restclient.History;
import se.beatit.homesweethome.io.restclient.IotHomeRestRoot;
import se.beatit.homesweethome.io.restclient.RetrofitRestClient;
import se.beatit.homesweethome.time.DateFormatter;
import se.beatit.homesweethome.time.TimeSpan;


public class GraphActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<History> {

    private TimeSpan timeSpan;
    private GraphView theGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        showProgressBar();

        theGraph = (GraphView)findViewById(R.id.graph);

        Intent intent = getIntent();
        GraphActivityIntent graphActivityIntent = new GraphActivityIntent(intent);

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

        // Retrieve the share menu item
        //MenuItem actionItem = menu.findItem(R.id.action_settings);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_change_time_span) {
            selectDatesPressed(null);
            return true;
        }
        return false;
    }

    public void selectDatesPressed(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
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
        System.out.println("On finished loader");

        List<Long> electricityUseHistory = history.getElectricityuse();
        DataPoint[] points = new DataPoint[electricityUseHistory.size()];
        int i = 0;

        for (Long usage : electricityUseHistory) {
            points[i] = new DataPoint(i, usage);
            i++;
        }

//LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(points);
        series.setSpacing(30);

        theGraph.getViewport().setYAxisBoundsManual(true);
        theGraph.getViewport().setXAxisBoundsManual(true);
        theGraph.getViewport().setMaxX(electricityUseHistory.size());
        theGraph.getViewport().setMinX(-1);
        theGraph.getViewport().setMaxY(series.getHighestValueY()+(series.getHighestValueY()*0.1));
        theGraph.getViewport().setMinY(0);


        DateFormatter df = new DateFormatter(timeSpan);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(theGraph);
        staticLabelsFormatter.setHorizontalLabels(
                new String[]{df.getDisplayableFrom(), df.getDisplayableTo()});
        //staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
        theGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        theGraph.removeAllSeries();
        theGraph.addSeries(series);

        hideProgressBar();
        theGraph.invalidate();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        System.out.println("On loader reset!!!!");
    }

    public void showTodayClicked(View view) {
        updateGraphWithNewTimespan(new TimeSpan(-1, 0, TimeSpan.RES_HOUR));
    }

    public void showWeekClicked(View view) {
        updateGraphWithNewTimespan(new TimeSpan(-7, 0, TimeSpan.RES_DAY));
    }

    public void showMonthClicked(View view) {
        updateGraphWithNewTimespan(new TimeSpan(-31, 0, TimeSpan.RES_DAY));
    }

    public void updateGraphWithNewTimespan(TimeSpan ts) {
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
