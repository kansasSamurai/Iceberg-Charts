package com.bluewalrus.scaling;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bluewalrus.chart.Chart;
import com.bluewalrus.chart.DateUtils;
import com.bluewalrus.chart.Orientation;
import com.bluewalrus.chart.XYChart;
import com.bluewalrus.chart.XYDataSeries;
import com.bluewalrus.chart.axis.AbstractInterval;
import com.bluewalrus.chart.axis.NumericalInterval;
import com.bluewalrus.chart.axis.TimeInterval;
import com.bluewalrus.chart.axis.TimeInterval.Type;
import com.bluewalrus.chart.draw.XAxisDrawUtil;

public class TimeSeriesAxisScaling extends AxisScaling {

	public Date dateStart;
	public Date dateEnd;

	public TimeSeriesAxisScaling(Date dateStart, Date dateEnd,
			TimeInterval timeInt1, TimeInterval timeInt2, TimeInterval timeInt3) {

		super();

		this.dateStart = dateStart;
		this.dateEnd = dateEnd;

		this.interval1 = timeInt1;
		this.interval2 = timeInt2;
		this.interval3 = timeInt3;

		if (interval1 != null)
			this.interval1.setLevel(1);
		if (interval2 != null)
			this.interval2.setLevel(2);
		if (interval3 != null)
			this.interval3.setLevel(3);

	}

	/**
	 * Draw the intervals ticks and labels for one particular interval:
	 * 
	 * 10--| | | 20--| |
	 * 
	 * 
	 * @param interval
	 * @param g
	 * @param chart
	 * @param showLabel
	 */
	protected void drawIntervalTickAndLabels(TimeInterval interval, Graphics2D g,
			XYChart chart, boolean showLabel) {

		int incrementNo = getIncrementNumber(interval);

		double totalIncrementPixs = 0;
		double lastPix = 0;

		double dayInPixel = getIncrementInPixels(TimeInterval.Type.DAY, chart);

		for (int i = 1; i < (incrementNo + 1); i++) {

			double intervalInPixels = 0;
			
			/**
			 * TICK
			 */
			drawIntervalTick(interval, g, chart, i, totalIncrementPixs);

			/**
			 * LABEL
			 */
			if (showLabel)
				drawIntervalLabel(interval, g, chart, i, totalIncrementPixs);
			
			/**
			 * LINE
			 */
			drawGridLine(interval, g, chart, i, totalIncrementPixs);
			
						
			if (interval.type.equals(TimeInterval.Type.MONTH)) {
				intervalInPixels = getIncrementInPixelsForMonthAfterStartDate(i, dayInPixel);
			} else if (interval.type.equals(TimeInterval.Type.YEAR)) {
				intervalInPixels = getIncrementInPixelsForYearAfterStartDate(i, dayInPixel);
			} else {
				intervalInPixels = getIncrementInPixels(interval.type, chart);
			}
			lastPix = totalIncrementPixs;
			totalIncrementPixs = totalIncrementPixs + intervalInPixels;
		}
	}


	/**
	 * TODO not working correctly!!!
	 * 
	 * @param interval
	 * @param g
	 * @param chart
	 * @param i
	 * @param totalIncrementPixs
	 * @param lastPix
	 */

	private void drawGridFill(TimeInterval interval, Graphics2D g,
			XYChart chart, int i, double totalIncrementPixs, double lastPix) {
		
		g.setColor(chart.xAxis.axisColor);

		double factor = getMultiplicationFactor(chart);

		// to first increment (edge of chart to first interval value)
		double toFirstInPixels = getToFirstIntervalValueFromMinInPixels(
				interval, factor);

		double totalDistanceFromEdge = chart.leftOffset + toFirstInPixels
				+ totalIncrementPixs;
		
		double width = (double)(totalIncrementPixs - lastPix);
		
		totalDistanceFromEdge = totalDistanceFromEdge-width;
		
		XAxisDrawUtil.drawGridFill(interval, g, chart,
				totalDistanceFromEdge, width, i);
		
	}
	
	
	private void drawGridLine(TimeInterval interval, Graphics2D g,
			XYChart chart, int i, double totalIncrementPixs) {
		
		g.setColor(chart.xAxis.axisColor);

		double factor = getMultiplicationFactor(chart);

		// to first increment (edge of chart to first interval value)
		double toFirstInPixels = getToFirstIntervalValueFromMinInPixels(
				interval, factor);

		double totalDistanceFromEdge = chart.leftOffset + toFirstInPixels
				+ totalIncrementPixs;
		
		XAxisDrawUtil.drawGridLine(interval, g, chart,
				totalDistanceFromEdge);
		
	}
	
	
	@Override
	protected void drawGridFills(AbstractInterval intv, Graphics2D g,
			XYChart chart) {
		
		TimeInterval interval = (TimeInterval)intv;
		
		int incrementNo = getIncrementNumber(interval);

		double totalIncrementPixs = 0;
		double lastPix = chart.leftOffset;

		double dayInPixel = getIncrementInPixels(TimeInterval.Type.DAY, chart);

		for (int i = 1; i < (incrementNo + 1); i++) {

			double intervalInPixels = 0;
			
			
			
			/**
			 * FILL
			 */
			drawGridFill(interval, g, chart, i, totalIncrementPixs, lastPix);
						
			if (interval.type.equals(TimeInterval.Type.MONTH)) {
				intervalInPixels = (int) getIncrementInPixelsForMonthAfterStartDate(i, dayInPixel);
			} else if (interval.type.equals(TimeInterval.Type.YEAR)) {
				intervalInPixels = (int) getIncrementInPixelsForYearAfterStartDate(i, dayInPixel);
			} else {
				intervalInPixels = (double) getIncrementInPixels(interval.type, chart);
			}
			lastPix = totalIncrementPixs;
			totalIncrementPixs = totalIncrementPixs + intervalInPixels;
		}
		
	}



	/**
	 * TODO : (see super class comments)
	 */
	public void drawGridLines(AbstractInterval interval, Graphics2D g,
			XYChart chart) {
		
		//ignore
	}

	/**
	 * given a number i, get the i'th month after the start date month and get
	 * its increments in pixels.
	 * 
	 * @param type
	 * @param i
	 *            month after start month
	 * @param dayInPixel
	 * @return
	 */
	private double getIncrementInPixelsForMonthAfterStartDate(int i,
			double dayInPixel) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(this.dateStart);

		cal.add(Calendar.MONTH, i);

		int days = DateUtils.getDaysInMonth(cal.getTime());

		return dayInPixel * days;
	}
	
	private double getIncrementInPixelsForYearAfterStartDate(int i,
			double dayInPixel) {
		
		Calendar cal = Calendar.getInstance();

		cal.setTime(this.dateStart);

		cal.add(Calendar.YEAR, i);

		int days = DateUtils.getDaysInYear(cal.getTime());

		return dayInPixel * days;
	}
	
	

	protected double getIncrementInPixels(AbstractInterval interval,
			XYChart chart) {

		TimeInterval inter = (TimeInterval) interval;

		TimeInterval.Type t = inter.getInterval();

		return getIncrementInPixels(t, chart);

	}

	protected double getIncrementInPixels(TimeInterval.Type t, XYChart chart) {

		long increment = DateUtils.getMsForType(t);
		
		double factor = this.getMultiplicationFactor(chart);

		double incrementInPixel = (double) (increment * factor);

		return incrementInPixel;
	}

	/**
	 * Get number of increments to display on axis for a particular time
	 * interval type.
	 * 
	 * @param interval
	 * @return
	 */
	protected int getIncrementNumber(AbstractInterval interval) {

		TimeInterval inter = (TimeInterval) interval;

		TimeInterval.Type t = inter.getInterval();

		if (t.equals(TimeInterval.Type.MONTH)) {

		} else if (t.equals(TimeInterval.Type.YEAR)) {

		}

		long increment = DateUtils.getMsForType(t); // :(

		/**
		 * TODO this code isn't very correct. The method getMsForType is
		 * incorrect for year and month because they are both variable and this
		 * could lead to the incrementNo below being incorrectly calculated.
		 * incrementNo could be out by one.
		 */
		int incrementNo = (int) ((dateEnd.getTime() - dateStart.getTime()) / increment); // shit!!

		return incrementNo;
	}

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

	@Override
	public void drawAll(Graphics2D g2d, XYChart xyChart,
			ArrayList<XYDataSeries> data) {

		// NOTE! data is ignored here. It's only used for enumeration

		drawAllIntervalTickAndLabelsAndGridLines(g2d, xyChart);
	}

	@Override
	public void drawAllPre(Graphics2D g2d, XYChart xyChart,
			ArrayList<XYDataSeries> data) {

		drawGridFills(g2d, xyChart);
	}



	/**
	 * Draw all the intervals and ticks for all intervals
	 * 
	 * @param g
	 * @param chart
	 */
	public void drawAllIntervalTickAndLabelsAndGridLines(Graphics2D g, XYChart chart) {

		if (this.interval1.isValid() && this.interval1.isActive()) {
			drawIntervalTickAndLabels((TimeInterval) this.interval1, g, chart,
					true);
		}
		if (this.interval2 != null && this.interval2.isValid()
				&& this.interval2.isActive()) {
			drawIntervalTickAndLabels((TimeInterval) this.interval2, g, chart,
					true);
		}
		if (this.interval3 != null && this.interval3.isValid()
				&& this.interval3.isActive()) {
			drawIntervalTickAndLabels((TimeInterval) this.interval3, g, chart,
					false);
		}
	}

	protected void drawIntervalLabel(TimeInterval interval, Graphics g,
			XYChart chart, int incrementNumber, double totalIncrementPixs) {

		g.setColor(chart.xAxis.axisColor);

		double factor = getMultiplicationFactor(chart);

		// to first increment
		double toFirstInPixels = getToFirstIntervalValueFromMinInPixels(
				interval, factor);

		long ms = DateUtils.getMsToNearestDataType(this.dateStart,
				interval.type);
		long timePointAtFirstInterval = dateStart.getTime() + ms;

		long totalTime = -1;

		if (interval.type == Type.YEAR) {
			totalTime = DateUtils.addYear(timePointAtFirstInterval,
					incrementNumber);
		} else if (interval.type == Type.MONTH) {

			// System.out.println("Time Point to first interval is " + new
			// Date(timePointAtFirstInterval));

			totalTime = DateUtils.addMonth(timePointAtFirstInterval,
					incrementNumber);

			// System.out.println("                                    (increment) "
			// + incrementNumber + "---> Total Time " + new Date(totalTime));

		} else if (interval.type == Type.DAY) {
			totalTime = DateUtils.addDay(timePointAtFirstInterval,
					incrementNumber);
		} else if (interval.type == Type.WEEK) {
			totalTime = DateUtils.addWeek(timePointAtFirstInterval,
					incrementNumber);
		} else {

			throw new RuntimeException("Unknown interval type " + interval.type);
		}

		// FORMAT
		SimpleDateFormat df;

		if (interval.dateFormat != null) {
			df = interval.dateFormat;
		} else {
			df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		}

		String xLabel = df.format(totalTime);

		// DRAW

		double totalDistanceFromEdge = chart.leftOffset + toFirstInPixels
				+ totalIncrementPixs;

		System.out.println("xLabel " + xLabel + " totalDistanceFromEdge "
				+ totalDistanceFromEdge + " toFirstInPixels " + toFirstInPixels
				+ " totalIncrementPixs " + totalIncrementPixs);

		XAxisDrawUtil.drawXIntervalLabel(g, chart, totalDistanceFromEdge,
				xLabel, chart.xAxis, interval);

	}

	/**
	 * The distance in pixels to the first displayable interval
	 * 
	 * Get the first interval that should be displayed on the axis. Eg. if the
	 * interval increment is 50, then we want the first value to be a multiple
	 * of 50.
	 * 
	 * if the min/max range is 3/101, then the first value would be 50 (and not
	 * 3) *
	 * 
	 * @param increment
	 * @param maxValue
	 * @param minValue
	 * @param factor
	 * @return
	 */
	protected double getToFirstIntervalValueFromMinInPixels(
			AbstractInterval increment, double factor) {

		TimeInterval inter = (TimeInterval) increment;

		long ms = DateUtils.getMsToNearestDataType(this.dateStart, inter.type);

		double pix = (ms) * factor; // convert to pixels

		if (pix < 0)
			throw new RuntimeException(
					"pixels cannot be negative. First val to min = " + ms
							+ ". dateStart.getTime() " + dateStart.getTime());

		return pix;
	}

	@Override
	protected void drawGridLineOnZero(Graphics2D g) {
		throw new RuntimeException("This has not yet been implemented");
	}

	@Override
	protected double getMultiplicationFactor(XYChart chart) {
		return ((double) chart.widthChart / (double) (dateEnd.getTime() - dateStart
				.getTime()));
	}

	/**
	 * Draw the tick of the interval. Usually just a small line coming out
	 * perpendicular to the axis.
	 * 
	 * @param interval
	 * @param g
	 * @param chart
	 * @param i
	 * @param totalIncrementPixs
	 */
	protected void drawIntervalTick(TimeInterval interval, Graphics g,
			XYChart chart, int i, double totalIncrementPixs) {

		g.setColor(chart.xAxis.axisColor);

		double factor = getMultiplicationFactor(chart);

		// to first increment (edge of chart to first interval value)
		double toFirstInPixels = getToFirstIntervalValueFromMinInPixels(
				interval, factor);

		double totalDistanceFromEdge = chart.leftOffset + toFirstInPixels
				+ totalIncrementPixs;

		XAxisDrawUtil.drawIntervalTick(interval, g, chart,
				totalDistanceFromEdge, chart.xAxis);
	}

	@Override
	protected double getToFirstIntervalValueFromMinInPixels(Double interval,
			double factor) {

		throw new RuntimeException("This has not yet been implemented");
	}

	// TODO!!! This method is identical to LinearNumerical!!
	@Override
	protected double getFromStart(XYChart chart, double toFirstInPixels,
			double incrementInPixel, int i) {
		double fromLeft = chart.leftOffset + (i * incrementInPixel)
				+ toFirstInPixels;
		return fromLeft;
	}

	public double getMaxValue() {
		return this.dateEnd.getTime(); // dangerous, casting to double??
	}

	public double getMinValue() {
		return this.dateStart.getTime();
	}


}
