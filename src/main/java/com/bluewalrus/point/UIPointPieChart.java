/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluewalrus.point;

import com.bluewalrus.chart.PieBubbleChartSettings;
import com.bluewalrus.datapoint.DataPoint;
import com.bluewalrus.datapoint.DataPointPieChart;
import com.bluewalrus.pie.Segment;
import com.bluewalrus.renderer.XYFactor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.util.ArrayList;

/**
 *
 * @author oliver
 */
public class UIPointPieChart extends UIPointComplexXY {

    PieBubbleChartSettings pbcs;

    boolean scaleOnX = false;

    public UIPointPieChart(Color color, PieBubbleChartSettings pbcs) {
        super();
        this.pbcs = pbcs;
    }

    public void draw(Graphics2D g, Point point, DataPoint dataPoint, XYFactor xyFactor) {

        DataPointPieChart pieChartDataPoint = (DataPointPieChart) dataPoint;

        double magnitude = 0;

        if (scaleOnX) {
            magnitude = pieChartDataPoint.magnitude * xyFactor.xFactor;
        } else {
            magnitude = pieChartDataPoint.magnitude * xyFactor.yFactor;
        }

        g.setColor(color);

        Paint gp = g.getPaint();

        int x = (int) (point.x - (magnitude / 2));
        int y = (int) (point.y - (magnitude / 2));

        drawPieChart(pieChartDataPoint, g, point, magnitude);

        g.setPaint(gp);

        g.setColor(Color.BLACK);
        g.drawString(pieChartDataPoint.name, x, y);

//        Utils.outlineText(g, "hi there", x, y);
    }

    private void drawPieChart(DataPointPieChart pieChartDataPoint, Graphics2D g2d, Point point, double magnitude) {

        Double startAngle = 0.0;
        ArrayList<Segment> values = pieChartDataPoint.pievalues;

        int width = (int) magnitude;
        
        //TODO mag should be not beradius

        int alpha = 256;

        //pull x y to the top left corner
        int x = (int) (point.x - (magnitude / 2));
        int y = (int) (point.y - (magnitude / 2));
        
        if (this.pbcs != null) {

            Color c1 = new Color(pbcs.c1.getRed(), pbcs.c1.getGreen(), pbcs.c1.getBlue(), pbcs.frontalTransparancy);
            Color c2 = new Color(pbcs.c2.getRed(), pbcs.c2.getGreen(), pbcs.c2.getBlue(), pbcs.frontalTransparancy);

            Color[] colors = {c1, c2};

            float[] dist = {.3f, .7f};

            RadialGradientPaint rgp = new RadialGradientPaint(
                    new Point(point.x, point.y),
                    (int) width,
                    dist,
                    colors);

            g2d.setPaint(rgp);

            
            System.out.println(" x " + x);
            System.out.println(" y " + y);
            
            g2d.fillOval(x, y, (int) width, (int) width);

            alpha = this.pbcs.frontalTransparancy;
        }

        for (int i = 0; i < values.size(); i++) {

            Segment s = values.get(i);

            Double angleOfThisSegment = (s.magnitude / 100) * 360;

            Color c = new Color(s.color.getRed(), s.color.getGreen(), s.color.getBlue(), alpha);
            g2d.setColor(c);

            g2d.fillArc(x, y, width, width, startAngle.intValue(), angleOfThisSegment.intValue());

            startAngle += angleOfThisSegment;
        }
    }

	@Override
	public boolean doesShapeContainPoint(Point point) {
		// TODO Auto-generated method stub
		return false;
	}
}