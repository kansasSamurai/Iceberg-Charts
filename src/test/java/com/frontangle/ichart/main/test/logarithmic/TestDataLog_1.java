package com.frontangle.ichart.main.test.logarithmic;


import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import com.frontangle.ichart.chart.Chart;
import com.frontangle.ichart.chart.XYChart;
import com.frontangle.ichart.chart.XYDataSeries;
import com.frontangle.ichart.chart.axis.NumericalInterval;
import com.frontangle.ichart.chart.axis.TimeInterval;
import com.frontangle.ichart.chart.axis.XAxis;
import com.frontangle.ichart.chart.axis.YAxis;
import com.frontangle.ichart.chart.datapoint.DataPoint;
import com.frontangle.ichart.chart.draw.Line;
import com.frontangle.ichart.chart.draw.point.UIPointSquare;
import com.frontangle.ichart.main.test.ChartTester;
import com.frontangle.ichart.scaling.LinearNumericalAxisScaling;
import com.frontangle.ichart.scaling.LogarithmicAxisScaling;
import com.frontangle.ichart.scaling.TimeSeriesAxisScaling;

public class TestDataLog_1 extends ChartTester {

	@Test
	public void testSnapshot() throws IOException, ParseException {
		super.testSnapshot();
	}
	
	@Override
	public Chart getChart() throws ParseException {

		ArrayList<XYDataSeries> xySeriesList = new ArrayList<XYDataSeries>();
		
		ArrayList<DataPoint> values = new ArrayList<DataPoint>();
		values.add(new DataPoint(5, -15));
		values.add(new DataPoint(15, 5));
		values.add(new DataPoint(139, 8));
		values.add(new DataPoint(2001, 14));
		values.add(new DataPoint(9301, 19));

		XYDataSeries series = new XYDataSeries(new UIPointSquare(Color.BLUE), new Line(Color.BLUE), "");
		series.dataPoints = values;

		NumericalInterval t1 = new NumericalInterval(6, 50.0, new Line(Color.GRAY, false, 1));
		NumericalInterval t2 = new NumericalInterval(3, 10.0, new Line(Color.LIGHT_GRAY, true, 1));
		NumericalInterval t3 = new NumericalInterval(1, 5.0, null);

		YAxis yAxis = new YAxis(new LinearNumericalAxisScaling(-90.0, 100.0, t1, t2, t3), "Y Axis");
		
		XAxis xAxis = new XAxis(new LogarithmicAxisScaling(1, 10000.0), "Log Axis"); 

		xySeriesList.add(series);

		XYChart chart = new XYChart("","","",xySeriesList, yAxis, xAxis,false);

		chart.setSize(1000, 500);
		chart.rightOffset = 200;

		chart.setTitleFont(new Font("Ariel", Font.PLAIN, 24));
		chart.setTitle("Logarithmic");

		return chart;
	}
	
	@Override
	public String getNiceTitle() {
		return "Logarithmic ";
	}
	
	public static void main(String[] args) throws Exception {
		ChartTester t = new TestDataLog_1();
		t.testChart(t.getChart());
	}
	
	
}

