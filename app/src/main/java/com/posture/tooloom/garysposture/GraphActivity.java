package com.posture.tooloom.garysposture;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;


public class GraphActivity extends Activity {
    ArrayList<AccelData> sensorData;
    private View mChart;
    private LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        sensorData = (ArrayList<AccelData>)getIntent().getSerializableExtra("data");

        setContentView(R.layout.activity_graph);
        layout = (LinearLayout) findViewById(R.id.graphchart_container);

        layout.removeAllViews();
        openChart();


    }

    private void openChart() {
        // Get Medium Text (18dp) size value in pixels, achartengine uses pixels, not dp
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float val = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, metrics);

        if (sensorData != null || sensorData.size() > 0) {
            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
            XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

            XYSeriesRenderer longtermzRenderer = new XYSeriesRenderer();
            longtermzRenderer.setColor(Color.GREEN);
            longtermzRenderer.setPointStyle(PointStyle.DIAMOND);
            longtermzRenderer.setFillPoints(true);
            longtermzRenderer.setLineWidth(5);
            longtermzRenderer.setDisplayChartValues(false);

            renderer.addSeriesRenderer(longtermzRenderer);
            renderer.setClickEnabled(true);
            renderer.setSelectableBuffer(20);
            renderer.setPanEnabled(true);

            TimeSeries timeseries = new TimeSeries("Filtered Posture");
            dataset.addSeries(timeseries);

            for (AccelData data : sensorData) {
                timeseries.add(data.getTimestamp(), data.getLongtermZ());
            }

            renderer.setLabelsTextSize(val / 2);
            renderer.setAxisTitleTextSize(val);
            renderer.setYLabelsPadding(25f);
            renderer.setMargins(new int[]{0, 50, 25, 0}); //Top, Left, Bottom, Right
            renderer.setLegendTextSize(val);
            renderer.setFitLegend(true);
            renderer.setZoomEnabled(true, false);

            // Creating a Line Chart
            mChart = ChartFactory.getTimeChartView(getBaseContext(), dataset,
                    renderer, "H:mm:ss");

            // Adding the Line Chart to the LinearLayout
            layout.addView(mChart);

        }
    }

}
