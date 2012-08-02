package com.ciotc.teemo.splines;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
/**
 * 包含一系列牙齿分割线 它还有绘制功能
 * @author Linxiaozhi
 *
 */
public class TeethLines {
	Polygon pgOut;
	Polygon pgIn;
	
	static float width = 2f;
	
	public TeethLines() {
		pgOut = new Polygon();
		pgIn = new Polygon();
	}
	
	/**
	 * 根据控制点来构造直线
	 * @param pgOut
	 * @param pgIn
	 */
	public TeethLines(Polygon pgOut,Polygon pgIn) {
		this.pgOut = pgOut;
		this.pgIn = pgIn;
	}
	
	/**
	 * 将它所包含的所有线都绘制出来 
	 * @param g
	 */
	public void paint(Graphics2D g) {
		GeneralPath shape = new GeneralPath();
		for(int i=0;i<pgOut.npoints;i++){
			shape.reset();
			shape.moveTo(pgOut.xpoints[i], pgOut.ypoints[i]);
			shape.lineTo(pgIn.xpoints[i],pgIn.ypoints[i]);
			g.setStroke(new BasicStroke(width));
			g.setPaint(Color.BLACK);
			g.draw(shape);
		}
	}
	
//	static final int EPSILON = 36; /* square of distance for picking */
//	
//	/**
//	 * 给定两个点 添加一条直线 返回该直线的索引值
//	 * @param p0 直线的一个端点
//	 * @param p1 直线的另一个端点
//	 * @return 该直线的索引值
//	 */
//	public int addLine(Point p0,Point p1){
//		MyLine line = new MyLine();
//		line.x0 = p0.x;
//		line.y0 = p0.y;
//		line.x1 = p1.x;
//		line.y1 = p1.y;
//		line.constructShape();
//		teethLines.add(line);
//		return selection = teethLines.size()-1;
//		
//	}
//	
//	/**
//	 * 选择离该点最近的直线 存在返回该直线的索引号 否则返回-1
//	 * @param x 横坐标
//	 * @param y 纵坐标
//	 * @return
//	 */
//	public int selectLine(int x, int y) {
//		selection = -1;
//		for (MyLine line : teethLines) {
//			if (line.contains(x, y)) {
//				selection = teethLines.indexOf(line);
//				break;
//			}
//		}
//		return selection;
//	}
//	
//	/**
//	 * 根据某个点的原来和新的坐标来设置整条直线新的坐标
//	 * @param oldPoint 原来直线上某个点的坐标
//	 * @param newPoint 移动后原来旧点坐标的新坐标
//	 */
//	public void setLine(Point oldPoint,Point newPoint) {
//		if (selection >= 0) {
//			int lenx = newPoint.x - oldPoint.x;
//			int leny = newPoint.y - oldPoint.y;
//			MyLine line = teethLines.get(selection);
//			line.x0 = line.x0 + lenx;
//			line.x1 = line.x1 + lenx;
//			line.y0 = line.y0 + leny;
//			line.y1 = line.y1 + leny;
//			line.constructShape(); 
//		}
//	}
	
	
}
