package se.beatit.homesweethome.ui;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

import se.beatit.homesweethome.R;
import se.beatit.homesweethome.io.restclient.History;
import se.beatit.homesweethome.time.DateFormatter;
import se.beatit.homesweethome.time.TimeSpan;

/**
 * Created by stefan on 2015-05-06.
 */
public class Graph {
    private GraphView graphView;

    public Graph(GraphView graphView) {
        this.graphView = graphView;
    }

    public void updateGraph(TimeSpan timeSpan, History history) {
        final List<Long> electricityUseHistory = history.getElectricityuse();
        DataPoint[] points = new DataPoint[electricityUseHistory.size()];
        int i = 0;

        for (Long usage : electricityUseHistory) {
            points[i] = new DataPoint(i, usage);
            i++;
        }

//LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(points);
        series.setSpacing(30);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint dataPoint) {

                if(dataPoint.getX() == electricityUseHistory.size()-1) {
                    return Color.GREEN;
                } else {
                    return Color.RED;
                }
            }
        });

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(electricityUseHistory.size());
        graphView.getViewport().setMinX(-1);
        graphView.getViewport().setMaxY(series.getHighestValueY()+(series.getHighestValueY()*0.1));
        graphView.getViewport().setMinY(0);

        DateFormatter df = new DateFormatter(timeSpan);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(
                new String[]{df.getDisplayableFrom(), df.getDisplayableTo()});
        //staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graphView.removeAllSeries();
        graphView.addSeries(series);

    }

    public void invalidate() {
        graphView.invalidate();
    }
}
