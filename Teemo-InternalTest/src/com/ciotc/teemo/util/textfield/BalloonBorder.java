package com.ciotc.teemo.util.textfield;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.border.Border;

/**
 * Border which renders a balloon.
 * 
 * @author Bernhard Pauler
 */
public class BalloonBorder implements Border {

    private Dimension lastComponentSize;
    private Insets insets = new Insets(0, 0, 0, 0);

    private Color fillColor;
    private int offset;

    /**
     * Creates a balloon border.
     * 
     * @param fillColor   color which is used to fill the balloon (currently only the triangular tip)
     * @param offset   number of pixels between component and balloon body
     */
    public BalloonBorder(Color fillColor, int offset) {
        this.fillColor = fillColor;
        this.offset = offset;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {		
        width -= insets.left + insets.right;
        height -= insets.top + insets.bottom;

        Point iPoint = new Point(); // initial point
        Point ePoint = new Point(); // end point

        iPoint.x = x;
        iPoint.y = y;
        ePoint.x = x + width + 1;
        ePoint.y = y;
        printPoints(iPoint, ePoint);
        g.drawLine(iPoint.x, iPoint.y, ePoint.x, ePoint.y);

        iPoint.setLocation(ePoint);
        ePoint.x = x + width + 1;
        ePoint.y = y + height + 1;
        printPoints(iPoint, ePoint);
        g.drawLine(iPoint.x, iPoint.y, ePoint.x, ePoint.y);		

        iPoint.setLocation(ePoint);
        ePoint.x = x + offset*2;
        ePoint.y = y + height + 1;
        printPoints(iPoint, ePoint);
        g.drawLine(iPoint.x, iPoint.y, ePoint.x, ePoint.y);

        iPoint.setLocation(ePoint);
        ePoint.x = x + offset;
        ePoint.y = y + height + offset + 1;
        printPoints(iPoint, ePoint);
        g.drawLine(iPoint.x, iPoint.y, ePoint.x, ePoint.y);

        iPoint.setLocation(ePoint);
        ePoint.x = x + offset;
        ePoint.y = y + height + 1;
        printPoints(iPoint, ePoint);
        g.drawLine(iPoint.x, iPoint.y, ePoint.x, ePoint.y);

        iPoint.setLocation(ePoint);
        ePoint.x = x;
        ePoint.y = y + height + 1;
        printPoints(iPoint, ePoint);
        g.drawLine(iPoint.x, iPoint.y, ePoint.x, ePoint.y);

        iPoint.setLocation(ePoint);
        ePoint.x = x;
        ePoint.y = y;
        printPoints(iPoint, ePoint);
        g.drawLine(iPoint.x, iPoint.y, ePoint.x, ePoint.y);

        int[] xPoints = {x+offset+1, x+offset*2/*-1?*/, x+offset+1             };
        int[] yPoints = {y+height+1, y+height+1       , y+height+offset /*-1?*/};
        printTrianglePoints(xPoints, yPoints);
        g.setColor(fillColor);
        g.fillPolygon(xPoints, yPoints, 3);
    }

    private void printPoints(Point iPoint, Point ePoint) {
//      System.out.println(iPoint.x + "/" + iPoint.y + "->" + ePoint.x + "/" + ePoint.y);
    }

    private void printTrianglePoints(int[] xPoints, int[] yPoints) {
//      System.out.println("Triangle points...");
//      for (int i = 0; i < xPoints.length; i++) {
//      System.out.println(xPoints[i] + "/" + yPoints[i]);
//      }
    }

    public Insets getBorderInsets(Component c) {
        Dimension currentComponent = c.getSize();

        if (currentComponent.equals(lastComponentSize)) {
            return insets;
        }

        insets = new Insets(1, 1, offset + 1 , 1);		
        lastComponentSize = currentComponent;

        return insets;
    }

    public boolean isBorderOpaque() {
        return true;
    }

}
