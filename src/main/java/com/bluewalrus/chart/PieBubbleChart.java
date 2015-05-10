package com.bluewalrus.chart;

import java.util.ArrayList;

import com.bluewalrus.bar.XYDataSeries;
import com.bluewalrus.datapoint.DataPointPieChart;

/**
 * ShowpieBubbleChartSettings piepieBubbleChartSettings apieBubbleChartSettings
 * bubblepieBubbleChartSettings in a bubble chart.
 * 
 * Magnitude needpieBubbleChartSettings to be multiplied by a factor
 * 
 * @author Oliver WatkinpieBubbleChartSettings
 */
public class PieBubbleChart extends XYChart {

	PieBubbleChartSettings pieBubbleChartSettings;

	public PieBubbleChart(ArrayList<XYDataSeries> listOfSeries, YAxis yAxis,
			XAxis xAxis, double multipleMagnitudeBy, PieBubbleChartSettings s) {

		this(listOfSeries, yAxis, xAxis, multipleMagnitudeBy);
		this.pieBubbleChartSettings = s;
	}

	public PieBubbleChart(ArrayList<XYDataSeries> listOfSeries, YAxis yAxis,
			XAxis xAxis, double multipleMagnitudeBy) {

		super(listOfSeries, yAxis, xAxis);

		for (XYDataSeries xyDataSeries : listOfSeries) {

			ArrayList<DataPointPieChart> al = xyDataSeries.dataPoints;

			for (DataPointPieChart dp : al) {

				System.out.println("dp.magnitude before " + dp.magnitude);
				dp.magnitude = dp.magnitude * multipleMagnitudeBy;
				System.out.println("dp.magnitude after " + dp.magnitude);
			}
		}
	}

}
