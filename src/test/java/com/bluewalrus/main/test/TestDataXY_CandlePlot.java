package com.bluewalrus.main.test;

import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.bluewalrus.chart.Chart;
import com.bluewalrus.chart.XYChart;
import com.bluewalrus.chart.XYDataSeries;
import com.bluewalrus.chart.axis.NumericalInterval;
import com.bluewalrus.chart.axis.XAxis;
import com.bluewalrus.chart.axis.YAxis;
import com.bluewalrus.chart.datapoint.DataPoint;
import com.bluewalrus.chart.datapoint.DataPointBoxPlot;
import com.bluewalrus.chart.datapoint.DataPointCandleStick;
import com.bluewalrus.chart.draw.GridLine;
import com.bluewalrus.chart.draw.Line;
import com.bluewalrus.chart.draw.point.UIPointBoxPlot;
import com.bluewalrus.chart.draw.point.UIPointCandleStick;
import com.bluewalrus.chart.draw.point.UIPointCircle;
import com.bluewalrus.chart.draw.point.UIPointSquare;
import com.bluewalrus.chart.draw.point.UIPointTriangle;
import com.bluewalrus.scaling.LinearNumericalAxisScalingX;
import com.bluewalrus.scaling.LinearNumericalAxisScalingY;

public class TestDataXY_CandlePlot extends ChartTester {

	public static void main(String[] s) throws ParseException {
		
		ChartTester t = new TestDataXY_CandlePlot();
		t.testChart(t.getChart());
		
//		getChart();
	}

	



	public XYChart getChart() {

		// TEST_DATA_START

		ArrayList<XYDataSeries> xySeriesList = new ArrayList<XYDataSeries>();

		ArrayList<DataPoint> values = new ArrayList<DataPoint>();
//		values.add(new DataPointBoxPlot(5, 53, 15, 26, 37, 49, 70));

		values.add(new DataPointCandleStick(10, 80, 70, 60, 50, true));
		values.add(new DataPointCandleStick(20, 80, 75, 50, 15, true));
		values.add(new DataPointCandleStick(30, 67, 45, 55, 41, false));
//		values.add(new DataPointCandleStick(40, 54, 40, 46, 73, true));
//		values.add(new DataPointCandleStick(50, 30, 34, 39, 72, false));
//		values.add(new DataPointCandleStick(60, 45, 36, 45, 58, true));
//		values.add(new DataPointCandleStick(70, 47, 41, 45, 49, false));
//		values.add(new DataPointCandleStick(80, 70, 68, 64, 50, false));

		XYDataSeries series = new XYDataSeries(values, new UIPointCandleStick(
				new Color(181, 197, 207, 100)), null, "1994");

		YAxis yAxis = new YAxis(new LinearNumericalAxisScalingY(0.0, 100.0, 50.0, 10.0, null), "y Axis");
		XAxis xAxis = new XAxis(new LinearNumericalAxisScalingX(0.0, 100.0, 50.0, 10.0, null), "x Axis");

		xySeriesList.add(series);

		XYChart chart = new XYChart(xySeriesList, yAxis, xAxis);

		chart.setSize(1000, 500);
		chart.rightOffset = 200;

		chart.setTitleFont(new Font("Ariel", Font.PLAIN, 24));
		chart.setTitle("Box Plot");

		return chart;
	}

}
