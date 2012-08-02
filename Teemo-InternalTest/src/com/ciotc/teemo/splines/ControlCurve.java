package com.ciotc.teemo.splines;

/** This class represents a curve defined by a sequence of control points */

import java.awt.*;

public class ControlCurve {

	protected Polygon pts;
	//protected int selection = -1;

	public ControlCurve() {
	}
	
	/**
	 * 构造完后，还需添加点
	 */
	public ControlCurve(Polygon pg) {
		//pts = new Polygon();
		this.pts = pg;
	}

	static Font f = new Font("Courier", Font.PLAIN, 12);

	/** paint this curve into g. */
	public void paint(Graphics g) {
		FontMetrics fm = g.getFontMetrics(f);
		g.setFont(f);
//		int h = fm.getAscent() / 2;
//
//		for (int i = 0; i < pts.npoints; i++) {
//			String s = Integer.toString(i);
//			int w = fm.stringWidth(s) / 2;
//			g.setColor(Color.BLACK); // added by lin
//			g.drawString(Integer.toString(i), pts.xpoints[i] - w, pts.ypoints[i] + h);
//		}
	}

//	static final int EPSILON = 36; /* square of distance for picking */
//
//	/** return index of control point near to (x,y) or -1 if nothing near */
//	public int selectPoint(int x, int y) {
//		int mind = Integer.MAX_VALUE;
//		selection = -1;
//		for (int i = 0; i < pts.npoints; i++) {
//			int d = sqr(pts.xpoints[i] - x) + sqr(pts.ypoints[i] - y);
//			if (d < mind && d < EPSILON) {
//				mind = d;
//				selection = i;
//			}
//		}
//		return selection;
//	}
//
//	// square of an int
//	static int sqr(int x) {
//		return x * x;
//	}
//
//	/** add a control point, return index of new control point */
//	public int addPoint(int x, int y) {
//		pts.addPoint(x, y);
//		return selection = pts.npoints - 1;
//	}
//
//	/** set selected control point */
//	public void setPoint(int x, int y) {
//		if (selection >= 0) {
//			pts.xpoints[selection] = x;
//			pts.ypoints[selection] = y;
//		}
//	}
//
//	/** remove selected control point */
//	public void removePoint() {
//		if (selection >= 0) {
//			pts.npoints--;
//			for (int i = selection; i < pts.npoints; i++) {
//				pts.xpoints[i] = pts.xpoints[i + 1];
//				pts.ypoints[i] = pts.ypoints[i + 1];
//			}
//		}
//	}
//
//	public String toString() {
//		StringBuffer result = new StringBuffer();
//		for (int i = 0; i < pts.npoints; i++) {
//			result.append(" " + pts.xpoints[i] + " " + pts.ypoints[i]);
//		}
//		return result.toString();
//	}
//	
//	/**
//	 * 根据实际的面板大小 重新定位它的位置 实时的进行缩放
//	 * 
//	 * @param oldPixel
//	 *                旧的像素值
//	 * @param newPixel
//	 *                新的像素值
//	 */
//	public void rePositionedPoints(float oldPixel,float newPixel) {
//		for (int i = pts.npoints - 1; i >= 0; i--) {
//			pts.xpoints[i]=  Math.round(pts.xpoints[i] / oldPixel * newPixel );
//			pts.ypoints[i]=  Math.round(pts.ypoints[i] / oldPixel * newPixel );
//		}
//	}
//	/**
//	 * 提供访问内部点的接口
//	 * @param index 该点的索引号
//	 * @return 所要求的点 索引号不合法时返回null
//	 */
//	public Point getPoint(int index){
//		if(index <0 || index>=pts.npoints) return null;
//		return new Point(pts.xpoints[index],pts.ypoints[index]);
//	}
}
