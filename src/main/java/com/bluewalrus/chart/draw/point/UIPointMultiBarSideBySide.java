package com.bluewalrus.chart.draw.point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.bluewalrus.chart.XYChart;
import com.bluewalrus.chart.XYFactor;
import com.bluewalrus.chart.datapoint.DataPoint;
import com.bluewalrus.chart.datapoint.DataPointBar;
import com.bluewalrus.chart.datapoint.DataPointMultiBar;

public class UIPointMultiBarSideBySide extends UIPointAbstractMultiBar{

	private int x;
	private int y;
	private int width;
	private int height;
	private int shift;
	private Color colorToUse;
	private Color muchmuchdarker;
	
	public UIPointMultiBarSideBySide() {
		super(Color.BLACK); //unimportant, never used.
//		barWidthPercent = 90;
	}

	public UIPointMultiBarSideBySide(int totalBarWidthPercent) {
		super(Color.BLACK); //unimportant, never used.
		barWidthPercent = totalBarWidthPercent;
	}

	public void draw(Graphics2D g, Point point, Point lastPoint, DataPoint dataPoint, XYFactor xyFactor, XYChart chart, int pixBtnFirst2Pts) {

		DataPointMultiBar multiBarDataPoint = (DataPointMultiBar)dataPoint;
	    
		x = 0;
        y = 0;
        width = 0;
        height = 0;

        shift = 0;
        
        int distanceBetweenXPoints = 40;
        if (lastPoint != null) {
            distanceBetweenXPoints = lastPoint.x - point.x;
        	
        }
        
        
        int totalWidthOfBars = multiBarDataPoint.bars.size() * pointDiffWidth;
        
        totalWidthOfBars = distanceBetweenXPoints;
        
        
    	/**
    	 * Draw each of the (multi) bars
    	 */
        for (DataPointBar dpb : multiBarDataPoint.bars) {
        	
            if (dpb.y > 0) { // greater than zero
                x = point.x - (totalWidthOfBars/2);
                y = chart.topOffset + chart.heightChart - (int)(dpb.y * xyFactor.yFactor);
                width = pointDiffWidth;
                height = (int)((dpb.y * xyFactor.yFactor));
                
                colorToUse = color;
            	
            }else { // less than zero
            	
                x = point.x - (totalWidthOfBars/2);
                y = point.y + (int)( dpb.y * xyFactor.yFactor);
                width = pointDiffWidth;
                height = (int)((- dpb.y * xyFactor.yFactor)); 
                
                colorToUse = color;
            }

            if (dpb.color != null) {

            	colorToUse = dpb.color;
            }
            
            muchmuchdarker = colorToUse.darker(); 
            
    		clipAndDrawPoint(g, chart);
            
            shift = shift+pointDiffWidth;
		}
	}

	
	@Override
	public void drawPoint(Graphics2D g) {
		
		
        g.setColor(colorToUse);
        
        //bottom rect
        g.fillRect(x + shift,
        		y,
        		width,
        		height);
        
        g.setColor(muchmuchdarker);
        
        //bottom rect
        g.drawRect(x + shift,
        		y,
        		width,
        		height);
	}
	

	@Override
	public boolean doesShapeContainPoint(Point point) {
		// TODO Auto-generated method stub
		return false;
	}

}
