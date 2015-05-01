package com.bluewalrus.point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import com.bluewalrus.datapoint.DataPoint;
import com.bluewalrus.datapoint.DataPointWithMagnitude;
import com.bluewalrus.renderer.XYFactor;

public class BubblePoint extends ComplexXYPoint {

    //by default scale on Y
    boolean scaleOnX = false;
    
    public BubblePoint(Color color) {
        super(color);
    }



    public void draw(Graphics2D g, Point point, DataPoint dataPoint, XYFactor xyFactor) {

        DataPointWithMagnitude dpWithM = (DataPointWithMagnitude) dataPoint;

        double mag = 0;

        if (scaleOnX) {
            mag = dpWithM.magnitude * xyFactor.xFactor;
        } else {
            mag = dpWithM.magnitude * xyFactor.yFactor;
        }

        g.setColor(color);

        Paint gp = g.getPaint();

        Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        Color c2 = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 100);

        Color[] colors = {c, c2};

        float[] dist = {.3f, .7f};

        int x = (int) (point.x - (mag / 2));
        int y = (int) (point.y - (mag / 2));

        RadialGradientPaint rgp = new RadialGradientPaint(
                new Point(point.x, point.y),
                (int) mag,
                dist,
                colors);

        g.setPaint(rgp);

        g.fillOval(x, y, (int) mag, (int) mag );

        g.setPaint(gp);

        g.setColor(Color.BLACK);

        if (dpWithM.name != null)
            g.drawString("" + dpWithM.name, x, y);

//        Utils.outlineText(g, "hi there", x, y);
    }
}
