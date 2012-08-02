package com.ciotc.teemo.file.view;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MouseInputListener;

import com.ciotc.contour.MyColor;
import com.ciotc.contour.Point1;
import com.ciotc.contour.RenderContour;
import com.ciotc.contour.Triangle;
import com.ciotc.contour.Triangulate;
import com.ciotc.teemo.file.model.DisplayModel;
import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.splines.ArchPoints;
import com.ciotc.teemo.splines.ControlCurve;
import com.ciotc.teemo.splines.MyLine;
import com.ciotc.teemo.splines.NatCubic;
import com.ciotc.teemo.splines.TeethLines;

public class Display2DView extends DisplayView {

	class ForLineMouseListener implements MouseInputListener {
		Point oldPoint;
		boolean isDragge;

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			switch (pressIndex) {
			case 0:
				break;
			case 1:
				if (e.getX() >= 0 && e.getX() <= getWidth() && e.getY() >= 0 && e.getY() <= getHeight()) {
					int lenx = e.getX() - oldPoint.x;
					int leny = e.getY() - oldPoint.y;
					tmpSelectedLine.x0 = selectedLine.x0 + lenx;
					tmpSelectedLine.x1 = selectedLine.x1 + lenx;
					tmpSelectedLine.y0 = selectedLine.y0 + leny;
					tmpSelectedLine.y1 = selectedLine.y1 + leny;
					tmpSelectedLine.constructShape();
					// tmpSelectedLine.constructShapeBasedPixel(pixel);
				}
				isDragge = true;
				break;
			case 2:
				MyLine line = selectedLine;
				if (line.isSelectP0) {
					constructXY0(line, e.getX(), e.getY());
				}
				if (line.isSelectP1) {
					constructXY1(line, e.getX(), e.getY());
				}
				// line.range2 = pixel;
				line.constructShape();
				// line.constructShapeBasedPixel(pixel);
				break;
			case 3:
				constructXY1(inLine, e.getX(), e.getY());
				inLine.constructShape();
				// inLine.range2 = pixel;
				// inLine.constructShapeBasedPixel(pixel);
				break;
			default:
				break;

			}
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			/** 判断是否移动到某直线上或者直线的端点上 **/
			// setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			if (pressIndex != 3)
				pressIndex = 0;
			// 先清空
			for (MyLine line : lines) {
				line.isSelectP0 = false;
				line.isSelectP1 = false;
				line.isSelected = false;
			}
			if (pressIndex == 3) {
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				return;
			}

			// 判断是否选中直线，若是则将光标改变
			for (MyLine line : lines) {
				if (line.contains(e.getX(), e.getY())) {
					line.isSelected = true;
					pressIndex = 1;
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					break;
				}
			}
			// 如果选中了某条直线，再判断该直线的端点是否被选中
			if (selectedLine != null) {
				MyLine line = selectedLine;
				Point p1 = new Point(line.a, line.b); // 左端点
				Point p2 = new Point(line.c, line.d); // 右端点
				Point p = new Point(e.getX(), e.getY());
				if (isRangeForLine(p1, p)) {
					line.isSelectP0 = true;
					pressIndex = 2;
					setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				}
				if (isRangeForLine(p2, p)) {
					line.isSelectP1 = true;
					pressIndex = 2;
					setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				}

			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
			switch (pressIndex) {
			case 0:
				selectedLine = null;
				for (MyLine line : lines) {
					line.isSelectP0 = false;
					line.isSelectP1 = false;
					line.isSelected = false;
				}
				break;
			case 1:
				for (MyLine line : lines) {
					if (line.isSelected) {
						selectedLine = line;
						break;
					}
				}
				tmpSelectedLine = new MyLine();
				tmpSelectedLine.x0 = selectedLine.x0;
				tmpSelectedLine.x1 = selectedLine.x1;
				tmpSelectedLine.y0 = selectedLine.y0;
				tmpSelectedLine.y1 = selectedLine.y1;
				tmpSelectedLine.constructShape();
				// tmpSelectedLine.constructShapeBasedPixel(pixel);
				oldPoint = new Point(e.getX(), e.getY());
				break;
			case 2:

				break;
			case 3:
				inLine = new MyLine();
				inLine.x0 = e.getX();
				inLine.y0 = e.getY();
				break;
			default:
				break;

			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			switch (pressIndex) {
			case 0:
				break;
			case 1:
				int x = getRightcoordinate(e.getX(), 0, getWidth());
				int y = getRightcoordinate(e.getY(), 0, getHeight());
				int lenx = x - oldPoint.x;
				int leny = y - oldPoint.y;
				if (selectedLine != null) {
					selectedLine.x0 += lenx;
					selectedLine.x1 += lenx;
					selectedLine.y0 += leny;
					selectedLine.y1 += leny;
					// selectedLine.range2 = pixel;
					selectedLine.constructShape();
					tmpSelectedLine = null;

					if (!isDragge && e.isPopupTrigger()) {
						popup2.show(e.getComponent(), e.getX(), e.getY());
					}
				}
				isDragge = false;
				break;
			case 2:
				MyLine line = selectedLine;
				if (line.isSelectP0) {
					constructXY0(line, e.getX(), e.getY());
				}
				if (line.isSelectP1) {
					constructXY1(line, e.getX(), e.getY());
				}
				line.constructShape();
				break;
			case 3:
				constructXY1(inLine, e.getX(), e.getY());
				inLine.range2 = pixel;
				inLine.constructShape();

				//if(distance==null){
				//}

				// inLine.constructShapeBasedPixel(pixel);
				lines.add(inLine);
				selectedLine = inLine;
				selectedLine.isSelected = true;
				inLine = null;
				pressIndex = 0;
				break;
			default:
				break;

			}
			// 统一在鼠标释放的时候重绘
			repaint();

		}

	}

	

	class ForTeethLineMouseListener2 implements MouseInputListener {
		Point oldPoint;
	

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (!isShowArch)
				return;
			archpoints.setPoint(e.getPoint(),oldPoint);
			oldPoint = e.getPoint();
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (!isShowArch)
				return;
			switch(archpoints.selectPointOrLine(e.getX(), e.getY())){
			case 1:
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				break;
			case 2:
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				break;
			default:
				break;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (!isShowArch)
				return;
			archpoints.selectPointOrLine(e.getX(), e.getY());
			archpoints.setPoint(e.getPoint(),e.getPoint());
			oldPoint = e.getPoint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

	}
	
	
	class myMouseInputListener implements MouseInputListener {

		private boolean isPressed;

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (isStrange)
				return;
			if (isPressed) {
				isDragged = true;
				if (e.getX() < 0)
					x = 0;
				else if (e.getX() > getWidth())
					x = (float) (Math.round(getWidth() / range2) * range2);
				else
					// x = e.getX();
					x = (float) (Math.round(e.getX() / range2) * range2);
				repaint();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			int i1 = Math.round(e.getX() / pixel);
			int i2 = Math.round(e.getY() / pixel);

			int[] is = InWhichBox(i1, i2, e.getX(), e.getY());
			Display2DView.this.getDfTemplate().getDisplayModel().selectForceInXY(is[0], is[1]);
			// System.out.println(i1+","+i2+";"+e.getX()+","+e.getY()+Arrays.toString(is));
			if (isStrange) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}
			if (isRange(e.getX(), x)) {
				isCur = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
			} else {
				isCur = false;
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (isStrange)
				return;
			if (isCur) {
				isPressed = true;
				oldx = x;
			} else
				isPressed = false;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isStrange)
				return;
			if (isPressed) {
				x = e.getX();
				if (e.getX() < 0)
					x = 0;
				else if (e.getX() > getWidth())
					x = (float) (Math.round(getWidth() / range2) * range2);
				else
					// x = e.getX();
					x = (float) (Math.round(e.getX() / range2) * range2);
				isDragged = false;
				isPressed = false;
				getDfTemplate().getDisplayModel().calculatePercentData(Math.round(x / range2));
				repaint();
			}
		}
	}

	class MyPopupListener extends MouseAdapter {
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
//				if(isShowArch){
//					if(archpoints.isDirty()) 
//						menuItemForPoints.setEnabled(true);
//					else
//						menuItemForPoints.setEnabled(false);
//				}else
//					menuItemForPoints.setEnabled(false);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void mousePressed(MouseEvent e) {
			// maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
	}
	/**
	 * 窗口改变时事件监听器 主要是改变pixel值
	 * @author Linxiaozhi
	 *
	 */
	class MyComponentAdaper extends ComponentAdapter{

		@Override
		public void componentResized(ComponentEvent e) {
			super.componentResized(e);
			float f1 = Math.round(getWidth() * 1.0f / 52 * 10) * 1.0f / 10;
			float f2 = Math.round(getHeight() * 1.0f / 44 * 10) * 1.0f / 10;

			if (f1 == f2)
				pixel = f1;
			else
				pixel = Math.round((f1 + f2) * 1.0f / 2 * 10) * 1.0f / 10;
			
			archpoints.rePositionedPoints(range2, pixel);
		}
		
	}
	
	BasicStroke b1 = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	BasicStroke b3 = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 6.1f }, 0.0f);

	BasicStroke b2 = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	private int contourOr2D = 1;
	private double[] forcePercent;
	private boolean isCur;
	private boolean isDragged;
	private boolean isShowArch; // 是否显示弓形轮廓
	private boolean isShowForceDist;
	private float oldx = 260f;
	private Path2D path = null;
	private double[] percentForce;
	JPopupMenu popup; // 添加直线和评论的弹出式菜单
	JPopupMenu popup2; // 删除线条的弹出式菜单
	JMenuItem menuItemForPoints  = null; //把它变成全局 为了操作方便 但设计不好
	private int range = 2;
	private float range2 = 10f;
	private int showCOF;
	private float x = 260f; // 249
	private List<MyLine> lines = new ArrayList<MyLine>();
	//private List<MyLine> teethLines = new ArrayList<MyLine>();
	private ArchPoints archpoints = null;

	private MyLine inLine;
	private MyLine selectedLine; // 选中的line
	//private MyLine selectedTeethLine; // 选中的line
	private MyLine tmpSelectedLine; // 移动时临时的线
	//private MyLine tmpSelectedTeethLine; // 移动时临时的线
	private float rangeForLine = 4f; // 判断鼠标是否在直线端点的范围内
	private Map<MyLine, Double> myLinesDistance = new HashMap<MyLine, Double>();
	/**
	 * 4种状态：0无任何状态1选中直线2选中端点3开始画线
	 */
	int pressIndex = 0;
	/**
	 * 3种状态：0无任何状态1选中直线2选中端点
	 */
	int pressIndex2 = 0;

	final static Point[] pointSouth = { new Point(15, 44), new Point(16, 41), new Point(17, 36), new Point(18, 31), new Point(19, 26), new Point(20, 22), new Point(21, 21), new Point(22, 20), new Point(22, 19), new Point(30, 19), new Point(30, 20), new Point(31, 21), new Point(32, 22),
			new Point(33, 26), new Point(34, 31), new Point(35, 36), new Point(36, 41), new Point(37, 44) };
	final static Point[] pointEast = { new Point(52, 33), new Point(51, 30), new Point(50, 25), new Point(49, 23), new Point(48, 21), new Point(47, 19), new Point(46, 17), new Point(45, 15), new Point(44, 13), new Point(43, 11), new Point(42, 10), new Point(41, 7), new Point(40, 5),
			new Point(39, 4), new Point(38, 3), new Point(36, 2), new Point(35, 0) };
	final static Point[] pointWest = { new Point(17, 0), new Point(16, 2), new Point(14, 3), new Point(13, 4), new Point(12, 5), new Point(11, 7), new Point(10, 9), new Point(9, 11), new Point(8, 13), new Point(7, 15), new Point(6, 17), new Point(5, 19), new Point(4, 21), new Point(3, 23),
			new Point(2, 25), new Point(1, 30), new Point(0, 33), new Point(0, 32) };
//	final static Point[] archPointsOut = { new Point(0, 36), new Point(2, 29), new Point(4, 21), new Point(6, 16), new Point(9, 10), new Point(12, 5), new Point(18, 1), new Point(26, 0), new Point(33, 1), new Point(40, 5), new Point(43, 11), new Point(46, 16), new Point(47, 21),
//			new Point(49, 29), new Point(52, 36) };
//
//	final static Point[] archPointsIn = { new Point(12, 43), new Point(13, 38), new Point(14, 31), new Point(15, 24), new Point(16, 19), new Point(18, 14), new Point(19, 10), new Point(21, 8), new Point(26, 6), new Point(31, 8), new Point(33, 10), new Point(34, 14), new Point(36, 19),
//			new Point(37, 24), new Point(38, 32), new Point(39, 38), new Point(40, 43) };

	public Display2DView(DisplayFileTemplate dfTemplate) {
		super(dfTemplate);
		//添加窗口改变大小的事件监听
		addComponentListener(new MyComponentAdaper());
		// 响应拖动动中心分割线的事件
		constructPopup();
		myMouseInputListener mm = new myMouseInputListener();
		addMouseListener(mm);
		addMouseMotionListener(mm);
		addMouseListener(new MyPopupListener());
		// 响应画直线事件
		ForLineMouseListener ml = new ForLineMouseListener();
		ForTeethLineMouseListener2 ml2 = new ForTeethLineMouseListener2();
		addMouseListener(ml);
		addMouseListener(ml2);
		addMouseMotionListener(ml);
		addMouseMotionListener(ml2);
		constructPopup2();
		
		//constructArchPoints();
		archpoints = dfTemplate.archPoints;
		
	}

	/**
	 * 
	 * @param pos
	 * @param k
	 *                递推次数，控制曲线的光滑度
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private List<Point1> B2Spline(List<Point1> pos, int k) {
		if (pos.size() <= 3)
			return pos;
		List<Point1> points = new ArrayList<Point1>();
		Point1 _p1 = pos.get(0);
		Point1 _p2 = pos.get(1);
		points.add(new Point1((_p1.x + _p2.x) * 0.5, (_p1.y + _p2.y) * 0.5, 0));
		for (int i = 1; i < pos.size() - 1; i++) {
			for (int j = 0; j < k; j++) {
				double t = j * 1.0 / k;
				Point1 p1 = pos.get(i - 1);
				Point1 p2 = pos.get(i);
				Point1 p3 = pos.get(i + 1);
				double x = ((p1.x - 2 * p2.x + p3.x) * t * t + (-2 * p1.x + 2 * p2.x) * t + (p1.x + p2.x)) * 0.5;
				double y = ((p1.y - 2 * p2.y + p3.y) * t * t + (-2 * p1.y + 2 * p2.y) * t + (p1.y + p2.y)) * 0.5;

				Point1 pp = new Point1(x, y, 0);
				points.add(pp);
			}
		}
		return points;
	}

	private List<Point1> B3Spline(List<Point1> pos, int k) {
		if (pos.size() <= 4)
			return pos;
		List<Point1> points = new ArrayList<Point1>();
		Point1 _p1 = pos.get(0);
		Point1 _p2 = pos.get(1);
		Point1 _p3 = pos.get(2);
		points.add(new Point1((_p1.x + 4 * _p2.x + _p3.x) / 6.0, (_p1.y + 4 * _p2.y + _p3.y) / 6.0, 0));
		for (int i = 1; i < pos.size() - 2; i++) {
			for (int j = 0; j < k; j++) {
				double t = j * 1.0 / k;
				Point1 p1 = pos.get(i - 1);
				Point1 p2 = pos.get(i);
				Point1 p3 = pos.get(i + 1);
				Point1 p4 = pos.get(i + 2);

				double x = ((-p1.x + 3 * p2.x - 3 * p3.x + p4.x) * t * t * t + (3 * p1.x - 6 * p2.x + 3 * p3.x) * t * t + (-3 * p1.x + 3 * p3.x) * t + (p1.x + 4 * p2.x + p3.x)) / 6.0;
				double y = ((-p1.y + 3 * p2.y - 3 * p3.y + p4.y) * t * t * t + (3 * p1.y - 6 * p2.y + 3 * p3.y) * t * t + (-3 * p1.y + 3 * p3.y) * t + (p1.y + 4 * p2.y + p3.y)) / 6.0;

				Point1 pp = new Point1(x, y, 0);
				points.add(pp);
			}
		}
		return points;
	}

	public void constructPopup() {
		popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem(getResourceString("addline"));
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pressIndex = 3; // 添加直线
			}
		});
		popup.add(menuItem);
//		menuItem = new JMenuItem(getResourceString("comments"));
//		menuItem.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//将评论内容的对话框显示出来
//				dfTemplate.commentDialog.setVisible(true);
//				//JOptionPane.showMessageDialog(null, "not complete yet", "Teemo", JOptionPane.INFORMATION_MESSAGE);
//
//			}
//		});
//		popup.add(menuItem);
//		menuItemForPoints = new JMenuItem(getResourceString("restorePoints"));
//		menuItemForPoints.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//还原弓形模型
//				archpoints.resetArchPoints();
//				repaint();
//			}
//		});
//		popup.add(menuItemForPoints);

	}

	private void constructPopup2() {
		popup2 = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem(getResourceString("removeline"));
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (lines.remove(selectedLine)) {
					selectedLine = null;
					pressIndex = 0;
					myLinesDistance.remove(selectedLine);
					JOptionPane.showMessageDialog(null, getResourceString("removesuccess"), getResourceString("Teemo"), JOptionPane.INFORMATION_MESSAGE);
				}
				repaint();
			}
		});
		popup2.add(menuItem);
	}

	private void constructXY0(MyLine ln, int x, int y) {
		if (x > getWidth())
			ln.x0 = getWidth();
		else if (x < 0)
			ln.x0 = 0;
		else
			ln.x0 = x;
		if (y > getHeight())
			ln.y0 = getHeight();
		else if (y < 0)
			ln.y0 = 0;
		else
			ln.y0 = y;
	}

	private void constructXY1(MyLine ln, int x, int y) {
		if (x > getWidth())
			ln.x1 = getWidth();
		else if (x < 0)
			ln.x1 = 0;
		else
			ln.x1 = x;
		if (y > getHeight())
			ln.y1 = getHeight();
		else if (y < 0)
			ln.y1 = 0;
		else
			ln.y1 = y;
	}

	/**
	 * 画双椭圆
	 * 
	 * @param g
	 */
	private void draw2Eclipse(Graphics2D g) {
		/**
		 * 画椭圆 两层椭圆 大椭圆 左上角顶点 (bx0,by0) 宽高 bw,bh=7.65+6.48=14.13 小椭圆
		 * 左上角顶点 (sx0,sy0) 宽高 sw,sh=6.48 参数来自T-scan 以及帮助文档Rationale for
		 * Center of Force target 椭圆中心距上边缘31mm 格子距1.016mm 间隔0.254mm
		 * sizePerGrid = (44 *1.016 + 43 * 0.254) / 44 = 55.626 / 44 =
		 * 1.264227 55.626 - 31 = 24.626 另一套方案 55.626 - 35 = 20.626
		 * 24.626 - 6.48 = 18.146 20.626 - 6 = 14.626 18.146 -7.65 =
		 * 10.496 14.626 - 7 = 7.626 6.48 + 7.65 = 14.13
		 * 
		 * 矩阵的宽 52 * 1.016+43*0.254 = 66.04; 矩阵的长55.626
		 */
		Preferences preferences = Preferences.userNodeForPackage(MainFrame.class);
		float top = preferences.getFloat("top", 31.025f);
		float left = preferences.getFloat("left", 33.020f);

		float sizePerGrid = 1.264227f; // mm
		float bx0 = (left / 66.04f * colNum - 6) * pixel;
		float by0 = /* 10.496f */(top - 14.13f) * pixel / sizePerGrid; // 31
										// -14.13=16.87
		float bw = 12 * pixel;
		float bh = /* 14.13f */14.13f * 2.0f * pixel / sizePerGrid; // 13
		float sx0 = (left / 66.04f * colNum - 3) * pixel;
		float sy0 = /* 18.146f */(top - 6.48f) * pixel / sizePerGrid; // 31
										// -6.48=24.52
		float sw = 6 * pixel;
		float sh = /* 6.48f */6.48f * 2.0f * pixel / sizePerGrid; // 6

		Ellipse2D eli1 = new Ellipse2D.Float(bx0, by0, bw, bh);
		Ellipse2D eli2 = new Ellipse2D.Float(sx0, sy0, sw, sh);
		g.setColor(Color.GRAY);
		g.fill(eli1);
		g.setColor(Color.WHITE);
		g.fill(eli2);

		g.setColor(Color.BLACK);
		g.draw(eli1);
		g.draw(eli2);
	}

	

	/**
	 * 画背景底色 用方块填充
	 * 
	 * @param g
	 */
	private void drawBackgound(Graphics2D g) {
		for (int ii = 0; ii < 44; ii++)
			for (int jj = 0; jj < 52; jj++)
				if (backgroundArray[ii][jj] == 1) {
					g.setColor(Color.WHITE);
					Rectangle2D rect = new Rectangle2D.Float((colNum - jj - 1) * pixel, (rowNum - ii - 1) * pixel, pixel, pixel);
					g.fill(rect);
				}
	}

	/**
	 * 根据牙齿的轮廓来填充背景图 该方法绘制的图形更加圆滑
	 */
	public void drawBackgoundByPath(Graphics2D g) {
		path = new Path2D.Double();

		// Point p0 = new Point(0, 0);
		// Point p1 = new Point(52, 0);
		// 上述公式不合适
		Point p0 = new Point(0, 44);
		Point p1 = new Point(52, 44);

		path.moveTo(p0.x * pixel, p0.y * pixel);

		List<Point1> pN = getPointBaseB3Pline(pointSouth);
		for (Point1 p : pN) {
			path.lineTo(p.x * pixel, p.y * pixel);
		}

		path.lineTo(p1.x * pixel, p1.y * pixel);

		List<Point1> pE = getPointBaseB3Pline(pointEast);
		for (Point1 p : pE) {
			path.lineTo(p.x * pixel, p.y * pixel);
		}

		List<Point1> pW = getPointBaseB3Pline(pointWest);
		for (Point1 p : pW) {
			path.lineTo(p.x * pixel, p.y * pixel);
		}

		path.closePath();
		g.setColor(Color.WHITE);
		g.fill(path);
	}

	/**
	 * 画中心点和中心点轨迹
	 * 
	 * @param g
	 */
	private void drawCOF(Graphics2D g) {
		int x;
		int y;
		Preferences preference = Preferences.userNodeForPackage(MainFrame.class);
		float f = preference.getFloat("threshold", 10);
		/**
		 * 画中心点轨迹
		 */
		for (int i = 1; i <= frameIndex; i++) {
			if (forcePercent[i] * 100 >= f)
				drawCOFLine(g, pointLists[i - 1], pointLists[i]);
		}
		// 若力度百分比小于设定的值 那就不画
		if (forcePercent[frameIndex] * 100 <= f)
			return;
		/**
		 * 画中心点
		 */
		x = pointLists[frameIndex].x;
		y = pointLists[frameIndex].y;
		// 画菱形的算法
		// 菱形内有两条线
		float m = /* pixel * */0.5f;

		// g.fill(centerPoint);
		// g.fillPolygon(px, py, 4);
		// g.fillOval(x-m, y-m, (int) (pixel*1.2), (int)
		// (pixel*1.2));
		// 第一种算法对应的公式
		if (x != 0 || y != 0) {
			// Graphics2D g2 = (Graphics2D) g;
			// Ellipse2D e = new Ellipse2D.Double(x * pixel
			// - pixel / 10, y * pixel - pixel / 10, pixel *
			// 1.2, pixel * 1.2);
			// g.fill(e);
			// g.setColor(Color.BLACK);
			Path2D centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + m) * pixel, (y - m) * pixel);
			centerPoint.lineTo((x + 2 * m) * pixel, y * pixel);
			centerPoint.lineTo((x + m) * pixel, (y + m) * pixel);
			centerPoint.lineTo(x * pixel, y * pixel);
			centerPoint.closePath();
			g.setColor(Color.RED);
			g.fill(centerPoint);

			centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + 2 * m) * pixel, y * pixel);
			centerPoint.lineTo((x + 3 * m) * pixel, (y + m) * pixel);
			centerPoint.lineTo((x + 2 * m) * pixel, (y + 2 * m) * pixel);
			centerPoint.lineTo(x * pixel, (y + 2 * m) * pixel);
			centerPoint.closePath();
			g.setColor(Color.WHITE);
			g.fill(centerPoint);

			centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + 2 * m) * pixel, (y + 2 * m) * pixel);
			centerPoint.lineTo((x + m) * pixel, (y + 3 * m) * pixel);
			centerPoint.lineTo(x * pixel, (y + 2 * m) * pixel);
			centerPoint.lineTo((x + m) * pixel, (y + m) * pixel);
			centerPoint.closePath();
			g.setColor(Color.RED);
			g.fill(centerPoint);

			centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + m) * pixel, (y + m) * pixel);
			centerPoint.lineTo(x * pixel, (y + 2 * m) * pixel);
			centerPoint.lineTo((x - m) * pixel, (y + m) * pixel);
			centerPoint.lineTo(x * pixel, y * pixel);
			centerPoint.closePath();
			g.setColor(Color.WHITE);
			g.fill(centerPoint);

			centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + m) * pixel, (y - m) * pixel);
			centerPoint.lineTo((x + 3 * m) * pixel, (y + m) * pixel);
			centerPoint.lineTo((x + m) * pixel, (y + 3 * m) * pixel);
			centerPoint.lineTo((x - m) * pixel, (y + m) * pixel);
			centerPoint.closePath();
			centerPoint.moveTo((x + 2 * m) * pixel, y * pixel);
			centerPoint.lineTo(x * pixel, (y + 2 * m) * pixel);
			centerPoint.moveTo(x * pixel, y * pixel);
			centerPoint.lineTo((x + 2 * m) * pixel, (y + 2 * m) * pixel);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(1.0f));
			g.draw(centerPoint);
		}

	}

	/**
	 * 画中心点轨迹
	 * 
	 * @param g2
	 * @param p1
	 * @param p2
	 */
	private void drawCOFLine(Graphics g2, Point p1, Point p2) {
		Graphics2D g = (Graphics2D) g2;
		if ((p1.x == 0 && p1.y == 0) || (p2.x == 0 && p2.y == 0))
			return;
		float len = pixel / 6;

		// Color oldColor = g.getColor();
		Paint oldPaint = g.getPaint();
		// g.setColor(Color.BLUE);
		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke(len));

		double m = pixel / 2;
		double a = p1.x * pixel + m;
		double b = p1.y * pixel + m;
		double c = p2.x * pixel + m;
		double d = p2.y * pixel + m;

		double[] px = new double[4];
		double[] py = new double[4];

		double distance = Math.sqrt((c - a) * (c - a) + (d - b) * (d - b));
		if (c == a && d == b) {
			Line2D line = new Line2D.Double(a, b, c, d);
			g.setPaint(Color.BLUE);
			g.draw(line);
		} else {

			double sin = (c - a) / distance;
			double cos = (d - b) / distance;
			px[0] = a + len * cos;
			py[0] = b - len * sin;
			px[1] = c + len * cos;
			py[1] = d - len * sin;
			px[2] = c - len * cos;
			py[2] = d + len * sin;
			px[3] = a - len * cos;
			py[3] = b + len * sin;

			GeneralPath gp = new GeneralPath(Path2D.WIND_NON_ZERO);
			gp.moveTo(px[0], py[0]);
			gp.lineTo(px[1], py[1]);
			gp.lineTo(px[2], py[2]);
			gp.lineTo(px[3], py[3]);
			gp.closePath();
			g.setPaint(Color.WHITE);
			g.fill(gp);
			g.setPaint(Color.BLUE);
			g.draw(gp);
		}
		g.setPaint(oldPaint);
		g.setStroke(s);
	}

	/**
	 * 画二维轮廓图
	 * 
	 * @param g
	 * @param dd
	 */
	private void drawContour(Graphics2D g, int[] dd) {
		// double[][] data = new double[colNum][rowNum];
		// int len = dd.length;
		// for(int i = 0;i<len;i++){
		// int x = i / rowNum;
		// int y = i % rowNum;
		// int t = dd[i] < 0? dd[i]+256:dd[i];
		// data[colNum - x - 1][rowNum - y - 1] = t;
		// }

		// 改造 只需简单改变 就可以解决边界的问题!
		double[][] data = new double[colNum + 2][rowNum + 2];
		int len = dd.length;
		for (int i = 0; i < len; i++) {
			int x = i / rowNum;
			int y = i % rowNum;
			int t = dd[i];//dd[i] < 0 ? dd[i] + 256 : dd[i];

			// data[colNum - x][rowNum - y] = t; // 其余的都是0
			// 牙齿方位改变 上面公式不适合
			data[x + 1][y + 1] = t; // 其余的都是0
		}

		Triangulate t = new Triangulate(data);
		Set<java.lang.Double> vals = t.getValues();
		List<Triangle> traingles = t.getTriangles();

		List<List<List<Point1>>> lists = new ArrayList<List<List<Point1>>>();
		RenderContour rc = new RenderContour(traingles);

		// long l1 = System.currentTimeMillis();
		for (double val : vals) {
			List<List<Point1>> points = rc.getContour(val);
			lists.add(points);
		}
		// long l2 = System.currentTimeMillis();
		// System.out.println(l2-l1);

		g.setColor(Color.BLACK);
		Stroke s = g.getStroke();
		for (List<List<Point1>> points : lists) {
			for (List<Point1> pos : points) {
				Path2D path = new Path2D.Double();
				int k = 15;
				// List<Point1> ps = B2Spline(pos,k);
				List<Point1> ps = B3Spline(pos, k);
				// int piexel = 15;
				path.moveTo(ps.get(0).x * pixel, ps.get(0).y * pixel);
				for (int i = 1; i < ps.size(); i++) {
					Point1 p = ps.get(i);
					path.lineTo(p.x * pixel, p.y * pixel);
				}
				path.closePath();

				int val = (int) Math.round(pos.get(0).z);

				Color color;
				/*
				 * if (val == 0) color = MyColor.q.getRgb();
				 * else if(val == 255) color =
				 * MyColor.q.getRgb(); else {
				 */
				int v = val >> 4;
				color = MyColor.values()[v].getRgb();

				// }
				g.setColor(color);
				g.fill(path);
				// g.setColor(Color.BLACK);
				// g.draw(path);

			}
		}
		g.setStroke(s);
	}

	/**
	 * 画一帧数据 主要方法
	 */
	@Override
	public void drawFrame(Graphics g2, int[] dd) {
		Color oldColor;
		Graphics2D g = (Graphics2D) g2;
		oldColor = g.getColor();
		Stroke stroke = g.getStroke();
		// 背景为灰色
		// 计算单位像素
		g.setColor(Color.LIGHT_GRAY);
		
//		float f1 = Math.round(getWidth() * 1.0f / 52 * 10) * 1.0f / 10;
//		float f2 = Math.round(getHeight() * 1.0f / 44 * 10) * 1.0f / 10;
//
//		if (f1 == f2)
//			pixel = f1;
//		else
//			pixel = Math.round((f1 + f2) * 1.0f / 2 * 10) * 1.0f / 10;
//		
//		//controlCurveOut.rePositionedPoints(range2, pixel);
//		//controlCurveIn.rePositionedPoints(range2, pixel);
//		archpoints.rePositionedPoints(range2, pixel);
		
		this.x = this.x / range2 * pixel;
		range2 = pixel;
		this.x = (float) (Math.round(this.x / range2) * range2);

		for (MyLine line : lines) {
			line.constructShapeBasedPixel(pixel);
		}
		/*for (MyLine line : teethLines) {
			line.constructShapeBasedPixel(pixel);
		}*/
		
		
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(new Color(0, 0, 0));

		/**
		 * 画坐标 第二版 不画坐标 而是填色
		 */
		if (contourOr2D == 1) {
			drawBackgound(g);
		} else if (contourOr2D == 2) {
			drawBackgoundByPath(g);

		}

		g.setColor(oldColor);
		// 1 代表 2D 图 矩阵形式 ，2代表2D 轮廓图
		
		//先对数据进行调整，包括过滤
		//int[] newDd  = dataFilter(dd);
		
		if (contourOr2D == 1) {
			drawMatrix(g, dd);
		} else if (contourOr2D == 2) {
			drawContour(g, dd);
		}

		if (!isStrange) {
			if (showCOF == 1 || showCOF == 2) {
				draw2Eclipse(g);
			}
			if (showCOF == 2) {
				drawCOF(g);
			}

			drawSeparateLine(g);
		}
		if (isShowArch) {
			drawArchPoints(g);
			//drawArchOutLine(g);
			//drawTeethLines(g);
		}
		
		if(isShowForceDist){
			drawForceDist(g,dd);
		}
		
		drawMyLines(g);
		g.setStroke(stroke);
		g.setColor(oldColor);

		
	}
	
	
	
	
	public void drawFrame(Graphics g2, int[] dd,float arg) {
		float old = this.pixel;
		this.pixel = pixel*arg;
	Color oldColor;
	Graphics2D g = (Graphics2D) g2;
	oldColor = g.getColor();
	Stroke stroke = g.getStroke();
	// 背景为灰色
	// 计算单位像素
	g.setColor(Color.LIGHT_GRAY);
	
//	float f1 = Math.round(getWidth() * 1.0f / 52 * 10) * 1.0f / 10;
//	float f2 = Math.round(getHeight() * 1.0f / 44 * 10) * 1.0f / 10;
//
//	if (f1 == f2)
//		pixel = f1;
//	else
//		pixel = Math.round((f1 + f2) * 1.0f / 2 * 10) * 1.0f / 10;
//	
//	//controlCurveOut.rePositionedPoints(range2, pixel);
//	//controlCurveIn.rePositionedPoints(range2, pixel);
//	archpoints.rePositionedPoints(range2, pixel);
	
//	this.x = this.x / range2 * pixel;
//	range2 = pixel;
//	this.x = (float) (Math.round(this.x / range2) * range2);

//	for (MyLine line : lines) {
//		line.constructShapeBasedPixel(pixel);
//	}
	/*for (MyLine line : teethLines) {
		line.constructShapeBasedPixel(pixel);
	}*/
	
	
	g.fillRect(0, 0,getWidth(), getHeight());
	g.setColor(new Color(0, 0, 0));

	/**
	 * 画坐标 第二版 不画坐标 而是填色
	 */
	if (contourOr2D == 1) {
		drawBackgound(g);
	} else if (contourOr2D == 2) {
		drawBackgoundByPath(g);

	}

	g.setColor(oldColor);
	// 1 代表 2D 图 矩阵形式 ，2代表2D 轮廓图
	
	//先对数据进行调整，包括过滤
	//int[] newDd  = dataFilter(dd);
	
	if (contourOr2D == 1) {
		drawMatrix(g, dd);
	} else if (contourOr2D == 2) {
		drawContour(g, dd);
	}

	if (!isStrange) {
		if (showCOF == 1 || showCOF == 2) {
			draw2Eclipse(g);
		}
		if (showCOF == 2) {
			drawCOF(g);
		}

	drawSeparateLine(g);
	}
	if (isShowArch) {
		drawArchPoints(g);
		//drawArchOutLine(g);
		//drawTeethLines(g);
	}
	
	if(isShowForceDist){
		drawForceDist(g,dd);
	}
	
//	drawMyLines(g);
	g.setStroke(stroke);
	g.setColor(oldColor);

	this.pixel = old;
	
	}


	/**
	 * 画每个牙齿的力度百分比
	 * @param dd 
	 */
	private void drawForceDist(Graphics2D g, int[] dd){
		archpoints.drawDataPercent(g,dd);
	}
	/**
	 * 画轮廓线和牙齿分割线
	 * @param g
	 */
	private void drawArchPoints(Graphics2D g) {
		archpoints.draw(g);
	}

	/**
	 * 画矩阵
	 */
	private void drawMatrix(Graphics2D g, int[] dd) {
		int x;
		int y;
		for (int i = 0; i < totalNum; i++) {
			Color color = null;
			x = i / rowNum; // rowNum == 44; x
			y = i % rowNum; // y

			int dataColor = dd[i];
//			if (dataColor < 0)
//				dataColor += 256;
			if (dataColor == 255)
				color = MyColor.values()[16].getRgb();
			else if (dataColor == 0) {
				continue;
			} else {
				int in = dataColor >>> 4;
				color = MyColor.values()[in].getRgb();
			}
			g.setColor(color);
			Rectangle2D rect = new Rectangle2D.Float(x * pixel, y * pixel, pixel, pixel);
			g.fill(rect);
		}
	}

	/**
	 * 画添加在图上的直线
	 * 
	 * @param g2
	 */
	private void drawMyLines(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		Paint oldPaint = g.getPaint();
		Stroke s = g.getStroke();
		// int i = 0;
		for (MyLine line : lines) {

			g.setStroke(new BasicStroke(MyLine.lineWidth));
			g.setPaint(Color.BLACK);
			GeneralPath shape = line.shape;
			// GeneralPath shape =
			// line.constructShapeBasedPixel(pixel);

			int midx = Math.round((line.x0 + line.x1) / 2.0f);
			int midy = Math.round((line.y0 + line.y1) / 2.0f);
			// 重新算距离
			// 矩阵的长 (44 *1.016 + 43 * 0.254) = 55.626 mm
			// 矩阵的宽 52 * 1.016+43*0.254 = 66.04 mm

			//使用缓存存储距离 不用
			//Double distance = myLinesDistance.get(line);
			double dx = 66.04 * Math.abs(line.x0 - line.x1) / getWidth();
			double dy = 55.626 * Math.abs(line.y0 - line.y1) / getHeight();
			double distance = Math.hypot(dx, dy);
			//myLinesDistance.put(line, distance);

			String str = String.format(" %d mm ", Math.round(distance));
			Rectangle2D rect = new Rectangle2D.Double(midx, midy - 12, str.length() * 6, 14);
			g.setColor(Color.LIGHT_GRAY);
			g.fill(rect);
			g.setColor(Color.BLACK);
			g.draw(rect);
			g.drawString(str, midx, midy);
			g.fill(shape);

		}
		// 如果被选择，那么直线的两端应该会有两个黑点
		if (selectedLine != null) {
			MyLine line = selectedLine;
			g.setPaint(Color.BLACK);
			int l = 6;
			g.drawOval(line.a - l / 2, line.b - l / 2, l, l);
			g.drawOval(line.c - l / 2, line.d - l / 2, l, l);
			g.setPaint(Color.GREEN);
			g.fillOval(line.a - l / 2, line.b - l / 2, l, l);
			g.fillOval(line.c - l / 2, line.d - l / 2, l, l);
		}

		if (tmpSelectedLine != null) {
			g.setStroke(new BasicStroke(MyLine.lineWidth));
			g.setPaint(Color.BLACK);
			GeneralPath shape = tmpSelectedLine.shape;
			// GeneralPath shape =
			// tmpSelectedLine.constructShapeBasedPixel(pixel);
			g.fill(shape);
		}

		// 画正在产生的直线
		if (inLine != null) {
			g.setStroke(new BasicStroke(MyLine.lineWidth));
			g.setPaint(Color.BLACK);
			GeneralPath shape = inLine.shape;
			// GeneralPath shape =
			// inLine.constructShapeBasedPixel(pixel);
			if (shape != null)
				g.fill(shape);
		}
		g.setPaint(oldPaint);
		g.setStroke(s);
		// g2.dispose();
	}

	/**
	 * 画方框 分割区域
	 */
	private void drawSeparateLine(Graphics2D g) {
		Stroke s = g.getStroke();
		int w = getWidth(), h = getHeight();
		if (!isDragged) {
			g.setStroke(b1);

			g.setColor(Color.GREEN);
			Rectangle2D rect = new Rectangle2D.Float(0, 0, x - range / 2, h);
			g.draw(rect);
			g.setColor(Color.RED);
			rect = new Rectangle2D.Float(x + range / 2, 0, w - x - 3/* stroke */, h);
			g.draw(rect);
		} else {
			g.setStroke(b1);
			g.setColor(Color.GREEN);
			Rectangle2D rect = new Rectangle2D.Float(0, 0, oldx - range / 2, h);
			g.draw(rect);
			g.setColor(Color.RED);
			rect = new Rectangle2D.Float(oldx + range / 2, 0, w - oldx - 3, h);
			g.draw(rect);
			g.setColor(Color.GRAY);
			g.setStroke(b2);
			Line2D line = new Line2D.Float(x, 0, x, h);
			g.draw(line);
		}
		g.setStroke(s);

		/**
		 * 写力度百分比
		 */

		String s0 = String.format("%s = %.2f %s", getResourceString("dataPanel.left"), percentForce[1] * 100, "%");
		String s1 = String.format("%s = %.2f %s", getResourceString("dataPanel.right"), percentForce[2] * 100, "%");
		int h0 = 5, w0 = 75;
		g.setColor(Color.BLACK);
		g.drawString(s0, x - range - w0, h - h0);
		g.drawString(s1, x + range + 5, h - h0);
	}

	

	public List<Point1> getPointBaseB3Pline(Point[] points) {
		List<Point1> lis = new LinkedList<Point1>();

		for (Point pos : points) {
			lis.add(new Point1(pos.x, pos.y, 0));
		}
		List<Point1> list = B3Spline(lis, 15);
		list.add(0, new Point1(points[0].x, points[0].y, 0));
		list.add(list.size(), new Point1(points[points.length - 1].x, points[points.length - 1].y, 0));

		return list;
	}

	

	/**
	 * 判断该数是不是在范围之内，若不是，则让它等于最小值或最大值
	 * 
	 * @param in
	 *                需要判断值
	 * @param min
	 *                最小值
	 * @param max
	 *                最大值
	 */
	private int getRightcoordinate(int in, int min, int max) {
		if (in > max)
			return max;
		if (in < min)
			return min;
		return in;
	}

	/**
	 * 
	 * @param x
	 *                初略的横坐标
	 * @param y
	 *                初略的纵坐标
	 * @param m
	 *                鼠标的横坐标
	 * @param n
	 *                鼠标的纵坐标
	 * @return 返回准确的横纵坐标
	 */
	private int[] InWhichBox(int x, int y, int m, int n) {
		float[] f = new float[4];
		f[0] = (x - 1) * pixel;
		f[1] = x * pixel;
		f[2] = (x + 1) * pixel;
		f[3] = (x + 2) * pixel;
		float[] g = new float[4];
		g[0] = (y - 1) * pixel;
		g[1] = y * pixel;
		g[2] = (y + 1) * pixel;
		g[3] = (y + 2) * pixel;
		int i = 0, j = 0, k;
		for (k = 0; k < 3; k++) {
			if (m >= f[k] && m < f[k + 1]) {
				i = (x - 1) + k;
				break;
			}
		}
		for (k = 0; k < 3; k++) {
			if (n >= g[k] && n < g[k + 1]) {
				j = (y - 1) + k;
				break;
			}
		}
		return new int[] { i, j };
	}

	private boolean isRange(int a, float b) {
		if (Math.abs(a - b) <= range / 2)
			return true;
		return false;
	}

	/**
	 * 判断两个点是否离得很近 近的标准用range来横来
	 * 
	 * @param a
	 *                第一个点坐标
	 * @param b
	 *                第二点坐标
	 * @return 离得近为真，否则为假
	 */
	private boolean isRangeForLine(Point a, Point b) {
		if (Math.hypot(a.x - b.x, a.y - b.y) < rangeForLine)
			return true;
		return false;
	}

	@Override
	public void onSize(int lenght, int flag) {
		int h0 = 108; // 56
		int w0 = 30;// 8
		if (flag == 0)
			pixel = (float) ((lenght - w0) * 1.0 / colNum);
		else if (flag == 1)
			pixel = (float) ((lenght - h0) * 1.0 / rowNum);

	}

	

	@Override
	public void stateChanged(ChangeEvent e) {
		DisplayModel dm = (DisplayModel) e.getSource();
		if (dm.getTowho() == 1)
			return;
		contourOr2D = dm.getContourOr2D();
		displayData = dm.getaFrame();
		frameIndex = dm.getFrameIndex();
		isStrange = dm.isStrange();
		isShowArch = dm.isShowArch();
		isShowForceDist = dm.isShowForceDist();
		//if(isShowForceDist) archpoints.setData(displayData);
		// x = dm.getX();
		// colLine = dm.getColLine();
		percentForce = dm.getPercentForce();
		showCOF = dm.getShowCOF();
		forcePercent = dm.getPercentData()[0];
		repaint();
	}
}
