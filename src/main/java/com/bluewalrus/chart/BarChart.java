package com.bluewalrus.chart;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import com.bluewalrus.bar.Bar;
import com.bluewalrus.bar.XYDataSeries;
import com.bluewalrus.chart.axis.XAxis;
import com.bluewalrus.chart.axis.YAxis;
import com.bluewalrus.datapoint.DataPointBar;
import com.bluewalrus.point.UIPointBar;

public class BarChart extends XYChart {

    public BarChart(XAxis xAxis, YAxis yAxis, ArrayList<Bar> bars) {
        this(xAxis, yAxis, bars, 10);
    }

    public BarChart(XAxis xAxis, YAxis yAxis, ArrayList<Bar> bars, int barWidth) {
        this(xAxis, yAxis, bars, new UIPointBar(Color.PINK, Color.YELLOW, null, barWidth));
    }

    public BarChart(XAxis xAxis, YAxis yAxis, ArrayList<Bar> bars, UIPointBar barPoint) {
        super(xAxis, yAxis);
        
        //inverse relationship?
        /**
         * The reason baropint needs to reference the chart is because of the dimensions
         * of the bar need to be measured from the X-Axis. Labels ticks everything is related
         * to dimensions of chart. Not sure if possible to remove this dependency
         */
        barPoint.chart = this;

        data = new ArrayList<XYDataSeries>();

        ArrayList<DataPointBar> dataPoints = new ArrayList<DataPointBar>();

        double xRange = (double) (this.xAxis.maxValue - this.xAxis.minValue);

//        space out
        //distance between points
        double pointDistance = (double) (xRange / (bars.size() + 1));

        int i = 1;
        for (Bar bar : bars) {
            dataPoints.add(new DataPointBar((int) (pointDistance * i), (int) bar.value, bar.color, bar.name));
            i++;
        }

        XYDataSeries<DataPointBar> series = new XYDataSeries<DataPointBar>(
                dataPoints,
                barPoint,
                null,
                "");

        data.add(series);
    }




}