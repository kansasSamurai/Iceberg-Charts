package com.bluewalrus.scaling;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Date;

import com.bluewalrus.chart.DateUtils;
import com.bluewalrus.chart.Orientation;
import com.bluewalrus.chart.XYChart;
import com.bluewalrus.chart.axis.AbstractInterval;
import com.bluewalrus.chart.axis.TimeInterval;

public abstract class TimeSeriesAxisScaling extends AxisScaling{

	
	public Date dateStart;
	public Date dateEnd;
	
	
	public TimeSeriesAxisScaling(Date dateStart, Date dateEnd, TimeInterval timeInt1,
			TimeInterval timeInt2, TimeInterval timeInt3, Orientation orientation) {
		
		super(orientation);
		
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;

		this.interval1 = timeInt1;
		this.interval2 = timeInt2;
		this.interval3 = timeInt3;
		
		this.interval1.setLevel(1);
		this.interval2.setLevel(2);
		this.interval3.setLevel(3);
		
	}
	
	
	
	/**
	 * Draw the intervals ticks and labels for one particular interval:
	 * 
	 * 10--| 
	 *     | 
	 *     | 
	 * 20--| 
	 *     |
	 * 
	 * 
	 * @param interval
	 * @param g
	 * @param chart
	 * @param showLabel
	 */
	protected void drawIntervalTickAndLabels(TimeInterval interval, Graphics g,
			XYChart chart, boolean showLabel) {
		
		int incrementNo = getIncrementNumber(interval);
		
		double incrementInPixel = getIncrementInPixels(interval, chart); //(double) (increment * factor);

		for (int i = 0; i < (incrementNo + 1); i++) {
			
			drawIntervalTick(interval, g, chart, i, incrementInPixel);

			if (showLabel)
				drawIntervalLabel(interval, g, chart, i, incrementInPixel);
		}
	}
	
	
	protected double getIncrementInPixels(AbstractInterval interval, XYChart chart) {
		
		TimeInterval inter = (TimeInterval) interval;
		
		TimeInterval.Type t = inter.getInterval();
//		
		long increment = DateUtils.getMsForType(t); // :(
		
		double factor = this.getMultiplicationFactor(chart);
		
		double incrementInPixel = (double) (increment * factor);
		
		return incrementInPixel;
		
	}
		
	
	protected int getIncrementNumber(AbstractInterval interval) {
		
		TimeInterval inter = (TimeInterval) interval;
		
		TimeInterval.Type t = inter.getInterval();
		
		long increment = DateUtils.getMsForType(t); // :(
		
		/**
		 * TODO this code isn't very correct. The method getMsForType is incorrect for year and month because they are both variable
		 * and this could lead to the incrementNo below being incorrectly calculated. incrementNo could be out by one.
		 */
		int incrementNo = (int) ((dateEnd.getTime() - dateStart.getTime()) / increment); //shit!!
		
		return incrementNo;
	}
	
	/**
	 * Draw the label next to the tick
	 * 
	 * @param interval
	 * @param g
	 * @param chart
	 * @param i
	 * @param incrementInPixel
	 */
	protected abstract void drawIntervalLabel(TimeInterval interval, Graphics g,
			XYChart chart, int i, double incrementInPixel);

	/**
	 * Draw the tick of the interval. Usually just a small line coming out
	 * perpendicular to the axis.
	 * 
	 * @param interval
	 * @param g
	 * @param chart
	 * @param i
	 * @param incrementInPixel
	 */

	protected abstract void drawIntervalTick(TimeInterval interval, Graphics g,
			XYChart chart, int i, double incrementInPixel);


}
