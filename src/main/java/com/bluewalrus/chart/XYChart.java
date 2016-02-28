/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluewalrus.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.management.RuntimeErrorException;

import com.bluewalrus.bar.Category;
import com.bluewalrus.bar.GridLine;
import com.bluewalrus.bar.Legendable;
import com.bluewalrus.bar.Line;
import com.bluewalrus.bar.XYDataSeries;
import com.bluewalrus.bar.XYDataSeriesType;
import com.bluewalrus.chart.axis.Axis;
import com.bluewalrus.chart.axis.NumericalInterval;
import com.bluewalrus.chart.axis.XAxis;
import com.bluewalrus.chart.axis.YAxis;
import com.bluewalrus.chart.draw.EnumerationAxisDrawX;
import com.bluewalrus.chart.draw.LinearNumericalAxisDrawX;
import com.bluewalrus.chart.draw.LinearNumericalAxisDrawY;
import com.bluewalrus.chart.draw.TimeSeriesAxisDrawX;
import com.bluewalrus.chart.plotter.DatePlotter;
import com.bluewalrus.chart.plotter.NumericalPlotter;
import com.bluewalrus.datapoint.DataPoint;
import com.bluewalrus.datapoint.DataPointBar;
import com.bluewalrus.point.UIPointSquare;
import com.bluewalrus.point.UIPointXY;
import com.bluewalrus.renderer.XYFactor;

/**
 * XYChart is a chart where data is represented by x,y data. Typically the y axis is vertical 
 * and on the left, while the x axis runs along the bottom.
 * 
 * @author Oliver Watkins
 */
public class XYChart extends Chart implements Legendable, MouseMotionListener {

	transient BasicStroke chartBorderLine = new BasicStroke(1, 
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] {
					2, 0 }, // no dash
			0.0f);

	public Color borderLineColor = Color.BLACK;

	public YAxis yAxis;
	public XAxis xAxis;

	public ArrayList<XYDataSeries> data = new ArrayList<XYDataSeries>();

	/**
	 * Create an XY chart by passing in the two axis. This is the default
	 * constructor for an empty chart.
	 * 
	 * @param xAxis 
	 * @param yAxis
	 */
	public XYChart(XAxis xAxis, YAxis yAxis) {
		this.yAxis = yAxis;
		this.xAxis = xAxis;

		this.addMouseMotionListener(this);
	}

	/**
	 * Create an XY chart passing in also the data set.
	 * 
	 * @param listOfSeries
	 * @param yAxis
	 * @param xAxis
	 */
	public XYChart(ArrayList<XYDataSeries> listOfSeries, YAxis yAxis,
			XAxis xAxis) {
		
		this(xAxis, yAxis);
		this.data.addAll(listOfSeries);
	}

	
	/**
	 * 
	 * @param values
	 * @param xAxisMax
	 * @param yAxisMax
	 * @param title
	 */
	
	public XYChart(ArrayList<DataPoint> values, String title) {
		
		
		
		double yMax = ChartUtils.calculateYAxisMax(values);
		double yMin = ChartUtils.calculateYAxisMin(values);
		double xMax = ChartUtils.calculateXAxisMax(values);
		double xMin = ChartUtils.calculateXAxisMin(values);

		//get the diffs
		double xDiff = xMax -xMin;
		double yDiff = yMax -yMin;

		//pad out to 10%
		double yMinAdj = yMin - (yDiff/10);
		double xMinAdj = xMin - (xDiff/10);
		double yMaxAdj = yMax + (yDiff/10);
		double xMaxAdj = xMax + (xDiff/10);
		
		//Needs to be floored. If decimal place then crashes later
		xMaxAdj = Math.floor(xMaxAdj);
		yMaxAdj = Math.floor(yMaxAdj);
		xMinAdj = Math.floor(xMinAdj);
		yMinAdj = Math.floor(yMinAdj);
		

		double magnitude = getInterval(yMinAdj, yMaxAdj);
		
		NumericalInterval t1  = new NumericalInterval(6, magnitude, new GridLine(Color.GRAY, false, 1));
		
		
//		NumericalInterval t1 = null; //new NumericalInterval(6, 50.0, new GridLine(Color.GRAY, false, 1));
		NumericalInterval t2 = null; //new NumericalInterval(3, 10.0, new GridLine(Color.LIGHT_GRAY, true, 1));
		NumericalInterval t3 = null; //new NumericalInterval(1, 5.0, null);
		
		

		YAxis yAxis = new YAxis(new LinearNumericalAxisDrawY(yMinAdj, yMaxAdj, t1, null, null), "");

		NumericalInterval t1x = new NumericalInterval(10, 20.0, new GridLine(Color.GRAY, false, 1));
		NumericalInterval t2x = new NumericalInterval(3, 10.0, new GridLine(Color.LIGHT_GRAY, true, 1));

		XAxis xAxis = new XAxis(new LinearNumericalAxisDrawX(xMinAdj, xMaxAdj, t1x, t2x, null), "");
		
		XYDataSeries series = new XYDataSeries(new UIPointSquare(Color.BLACK),
				new Line(Color.BLACK), "");
		series.dataPoints = values;
		
		ArrayList<XYDataSeries> xySeriesList = new ArrayList<XYDataSeries>();

		xySeriesList.add(series);
		
		this.yAxis = yAxis;
		this.xAxis = xAxis;

		this.addMouseMotionListener(this);
		
		
		this.data.addAll(xySeriesList);
		
		this.setTitle(title);
	}

	private double getInterval(
			double yMinAdj, double yMaxAdj) {
		
//		NumericalInterval t1 = null;
		
		double yT = yMaxAdj;
		double yT2 = yMinAdj;

		double magnitude = 10.0;
		
		boolean ok = isOrderMagnitudeAcceptableFirstInterval(yT, yT2, magnitude);
		
		if (!ok) {
			magnitude = 100.0;
			ok = isOrderMagnitudeAcceptableFirstInterval(yT, yT2, magnitude);
		}
		
		if (!ok) {
			magnitude = 1000.0;
			ok = isOrderMagnitudeAcceptableFirstInterval(yT, yT2, magnitude);
		}

		if (!ok) {
			magnitude = 10000.0;
			ok = isOrderMagnitudeAcceptableFirstInterval(yT, yT2, magnitude);
		}
		return magnitude;
	}

	private boolean isOrderMagnitudeAcceptableFirstInterval(double maxValue, double minValue, double orderOfMagnitude) {
		while (maxValue % orderOfMagnitude != 0) {
			maxValue--;
		}
		
		while (minValue % orderOfMagnitude != 0) {
			minValue++;
		}
		
		double numberTicks = (maxValue - minValue) / orderOfMagnitude;
		
		if ( numberTicks < 10) { // && numberTicks > 2) {
			return true;
		}
		return false;
	}



	/**
	 * Paint the background, Title, Grid and Axis. All the elements of the chart
	 * except for the actual data.
	 *
	 * @param g2d
	 * @param data 
	 */
	protected void prePaint(Graphics2D g2d, ArrayList<XYDataSeries> data) {
		
		this.calculateHeighAndWidthOfChart();

		/**
		 * Maybe we want a filled colored area instead of some lines???
		 */
		drawBackground(g2d);
		drawBottomLine(g2d);
		drawLeftLine(g2d);

		// title
		drawTitle(g2d);

		drawLegend(g2d);

		// fills
		yAxis.axisDraw.drawAllPre(g2d, this, data);  
		xAxis.axisDraw.drawAllPre(g2d, this, data);  

		// y axis
		yAxis.axisDraw.drawAll(g2d, this, data);  
		
		yAxis.drawLabel(g2d, this);
		yAxis.drawBorderLine(g2d, this);

		// x axis
		xAxis.axisDraw.drawAll(g2d, this, data); 
		xAxis.drawLabel(g2d, this);
		xAxis.drawBorderLine(g2d, this);

	}


	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		//draws axis, frame etc
		this.prePaint(g2d, data);
		
		//draws actual data
		drawGraph(g2d);
	}



	@Override
	protected void drawGraph(Graphics g) {
		
		/**
		 * TODO if the x-axis is enumerable then the data needs
		 * to be massaged. Upon creation of the datapoints the
		 * x y values were arbitrary. Now that the chart
		 * has size we can calculate the values where 
		 * the points need to be positioned.
		 * 
		 * This does not look right here :( Maybe put into EnumeratioAxisDraw??
		 */
		if (xAxis.axisDraw instanceof EnumerationAxisDrawX) {
			massageXAxisData_forEnumeration((Graphics2D)g, this, data);
		}
		
    	if (xAxis.axisDraw instanceof TimeSeriesAxisDrawX) {
    		new DatePlotter().drawLinesOrPoints((Graphics2D)g, this, yAxis, xAxis, data);
    	}else {
    		new NumericalPlotter().drawLinesOrPoints((Graphics2D) g, this, yAxis, xAxis, data);
    	}
		
	}
	

	/**
	 * Massage data on X axis.ONLY USED FOR ENUMERATION!!!!
	 * @param g2d
	 * @param xyChart
	 * @param data
	 */
	public void massageXAxisData_forEnumeration(Graphics2D g2d, XYChart xyChart,
			ArrayList<XYDataSeries> data) {

		double xMax = this.xAxis.axisDraw.maxValue;
		double xMin = this.xAxis.axisDraw.minValue;



		double xFactor = ((double) xyChart.widthChart / (double) (xMax - xMin));

		XYFactor xyFactor = new XYFactor(xFactor, 123);
		xyFactor.xZeroOffsetInPixel = (double) ((-xMin / (xMax - xMin)) * xyChart.widthChart);

		ArrayList<DataPoint> dataPoints = data.get(0).dataPoints;

		double xRange = (double) (xMax - xMin);

		// distance between points (bars)
		double pointDistance = (double) (xyChart.widthChart / (dataPoints.size() + 1));

		int i = 1;
		for (DataPoint dataPoint : dataPoints) {

			double xShift2 = (pointDistance * i);

			int x = (int) (xyChart.leftOffset + (xShift2));

			dataPoint.x = x;

			i++;
		}
	}

	@Override
	public void drawLegend(Graphics2D g) {

		ArrayList<Category> categories = new ArrayList<Category>();

		if (data.size() == 1) {
			return;
		}

		for (XYDataSeries series : data) {

			Category category;

			if (series.type == XYDataSeriesType.BUBBLE) {
				category = new Category(series.name, series.seriesColor);
			} else {
				category = new Category(series.name, series.pointType,
						series.line);
			}
			categories.add(category);
		}
		super.drawLegend(g, categories);
	}



	/**
	 * Inner line just inside of the axis line. Potentially optional??
	 * 
	 */
	protected void drawBottomLine(Graphics2D g) {

		if (chartBorderLine == null) {
			chartBorderLine = new BasicStroke(1, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 10.0f, new float[] { 2, 0 }, // no
																			// dash
					0.0f);
		}

		g.setStroke(chartBorderLine);
		g.setColor(borderLineColor);
		g.drawLine(leftOffset, heightChart + topOffset, leftOffset + widthChart, heightChart + topOffset);
	}

	/**
	 * Inner line just inside of the axis line. Potentially optional?? Eg
	 * colored area instead?
	 * 
	 */
	protected void drawLeftLine(Graphics2D g) {

		g.setStroke(chartBorderLine);
		g.setColor(borderLineColor);
		g.drawLine(leftOffset, topOffset, leftOffset, heightChart + topOffset);
	}

	/**
	 * Inner line inside axis line. Not the same as the axis. Only used in XYYChart
	 * @param g
	 */
	protected void drawRightLine(Graphics2D g) {
		g.setStroke(chartBorderLine);
		g.setColor(borderLineColor);
		g.drawLine(leftOffset + widthChart, topOffset, leftOffset + widthChart, heightChart + topOffset);
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		Point point = e.getPoint();

		for (XYDataSeries xyDataSeries : data) {
			ArrayList al = xyDataSeries.dataPoints;

			for (Object object : al) {
				DataPoint dp = (DataPoint) object;

				UIPointXY uip = dp.uiPointXY;

				boolean b = uip.doesShapeContainPoint(point);

				if (b)
					System.out.println("CONTAINS POINT!!");
			}
		}
		this.updateUI();
	}

}
