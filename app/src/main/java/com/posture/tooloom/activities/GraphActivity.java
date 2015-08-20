package com.posture.tooloom.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.posture.tooloom.garysposture.AccelData;
import com.posture.tooloom.garysposture.R;

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
            TimeSeries longtermZtimeseries = new TimeSeries("Filtered Posture");
            TimeSeries rawZtimeseries = new TimeSeries("Raw Posture");
            TimeSeries upperSeries = new TimeSeries("");
            TimeSeries lowerSeries = new TimeSeries("");

            for (AccelData data : sensorData) {
                rawZtimeseries.add(data.getTimestamp(), data.getZ());
                longtermZtimeseries.add(data.getTimestamp(), data.getLongtermZ());
                upperSeries.add(data.getTimestamp(), data.getUpper());
                lowerSeries.add(data.getTimestamp(), data.getLower());
            }

            dataset.addSeries(rawZtimeseries);
            dataset.addSeries(longtermZtimeseries);
            dataset.addSeries(upperSeries);
            dataset.addSeries(lowerSeries);

            XYSeriesRenderer longTermZRenderer = new XYSeriesRenderer();
            longTermZRenderer.setColor(Color.GREEN);
            longTermZRenderer.setPointStyle(PointStyle.DIAMOND);
            longTermZRenderer.setFillPoints(true);
            longTermZRenderer.setLineWidth(5);
            longTermZRenderer.setDisplayChartValues(false);

            XYSeriesRenderer rawZRenderer = new XYSeriesRenderer();
            rawZRenderer.setColor(Color.CYAN);
            rawZRenderer.setPointStyle(PointStyle.CIRCLE);
            rawZRenderer.setFillPoints(true);
            rawZRenderer.setLineWidth(5);
            rawZRenderer.setDisplayChartValues(false);

            XYSeriesRenderer upperRenderer = new XYSeriesRenderer();
            upperRenderer.setColor(Color.RED);
            upperRenderer.setPointStyle(PointStyle.TRIANGLE);
            upperRenderer.setFillPoints(true);
            upperRenderer.setLineWidth(2);
            upperRenderer.setDisplayChartValues(false);
            upperRenderer.setShowLegendItem(false);

            XYSeriesRenderer lowerRenderer = new XYSeriesRenderer();
            lowerRenderer.setColor(Color.RED);
            lowerRenderer.setPointStyle(PointStyle.TRIANGLE);
            lowerRenderer.setFillPoints(true);
            lowerRenderer.setLineWidth(2);
            lowerRenderer.setDisplayChartValues(false);
            lowerRenderer.setShowLegendItem(false);

            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

            multiRenderer.addSeriesRenderer(longTermZRenderer);
            multiRenderer.addSeriesRenderer(rawZRenderer);
            multiRenderer.addSeriesRenderer(upperRenderer);
            multiRenderer.addSeriesRenderer(lowerRenderer);

 //           multiRenderer.setClickEnabled(true);
 //           multiRenderer.setSelectableBuffer(20);
 //           multiRenderer.setPanEnabled(true);

            multiRenderer.setLabelsTextSize(val / 2);
            multiRenderer.setAxisTitleTextSize(val);
            multiRenderer.setYLabelsPadding(25f);
            multiRenderer.setMargins(new int[]{0, 50, 25, 0}); //Top, Left, Bottom, Right
            multiRenderer.setLegendTextSize(val);
            multiRenderer.setFitLegend(true);
            multiRenderer.setZoomEnabled(true, true);

            // Creating a Line Chart
            mChart = ChartFactory.getTimeChartView(getBaseContext(), dataset,
                    multiRenderer, "H:mm:ss");

            // Adding the Line Chart to the LinearLayout
            layout.addView(mChart);

        }
    }

}
