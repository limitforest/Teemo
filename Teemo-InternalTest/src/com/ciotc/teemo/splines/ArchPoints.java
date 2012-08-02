package com.ciotc.teemo.splines;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.ciotc.teemo.file.doc.DisplayDoc;
import com.ciotc.teemo.file.doc.RealtimeDoc.WeightInput;

/**
 * 包含了牙齿轮廓线的所有控制点
 * 
 * @author Linxiaozhi
 * 
 */
public class ArchPoints {
	public Polygon ploygonOut; //外轮廓控制点
	public Polygon ploygonIn; //内轮廓控制点 注意 内线比外线多首尾两个点
	//private int selection = -1; //选择的直线或者端点
	private Selection selection = Selection.NONE;

	private ControlCurve controlCurveOut = null; //拥有控制点的控制曲线 外曲线
	private ControlCurve controlCurveIn = null; //拥有控制点的控制曲线 内曲线
	private TeethLines teethLines = null;

	private boolean isDirty = false; //数据是否被修改过

	class MyObservable extends Observable {

		public synchronized void setChanged() {
			super.setChanged();
		}

	}

	MyObservable notifier = new MyObservable();

	/**
	 * 给dirty添加监听者
	 * @param observer
	 */
	public void addObserver(Observer observer) {
		notifier.addObserver(observer);
	}

	/**
	 * 通知所有的监听者
	 */
	public void notifyObservers() {
		notifier.setChanged();
		notifier.notifyObservers(isDirty);
	}

	/**
	 * 枚举选择的是直线或者端点
	 * 
	 * @author Linxiaozhi
	 * 
	 */
	enum Selection {
		OUT, IN, LINE, NONE/*永远不会变*/;
		int index = -1; //选择的索引号

		Selection() {
		}
	}

	private DisplayDoc ddoc;

	public ArchPoints(DisplayDoc ddoc) {
		this(ddoc.getArchPointsOut(), ddoc.getArchPointsIn());
		this.ddoc = ddoc;
	}

	public ArchPoints(Point[] archPointsOut, Point[] archPointsIn) {
		//以下顺序不能颠倒 先有点再有轮廓线和牙齿分割线
		ploygonOut = new Polygon();
		ploygonIn = new Polygon();
//		ploygonIn.addPoint(archPointsIn[0].x, archPointsIn[0].y); //对于内线 还要添加首尾两条
//		for (int i = 0; i < 15; i++) {
//			ploygonOut.addPoint(archPointsOut[i].x, archPointsOut[i].y); //10 表示初始值
//			ploygonIn.addPoint(archPointsIn[i + 1].x, archPointsIn[i + 1].y); //10 表示初始值
//		}
//		ploygonIn.addPoint(archPointsIn[16].x, archPointsIn[16].y);
		for (int i = 0; i < archPointsOut.length; i++) {
			ploygonOut.addPoint(archPointsOut[i].x, archPointsOut[i].y); //10 表示初始值
			ploygonIn.addPoint(archPointsIn[i].x, archPointsIn[i].y); //10 表示初始值
		}

		//构造轮廓曲线的控制点
		controlCurveOut = new NatCubic(ploygonOut);
		controlCurveIn = new NatCubic(ploygonIn);
		//构造牙齿的分割线
		teethLines = new TeethLines(ploygonOut, ploygonIn);

		//初始化 确定长度
		dataPercent = new float[ploygonOut.npoints];
		dataValue = new float[ploygonOut.npoints];
	}

	/**
	 * 重新置所有的控制点
	 */
	public void resetArchPoints() {
		if (ddoc != null) {
			Point[] archPointsOut = ddoc.getArchPointsOut();
			Point[] archPointsIn = ddoc.getArchPointsIn();
			ploygonOut.reset();
			ploygonIn.reset();
//			ploygonIn.addPoint(Math.round(archPointsIn[0].x * pixel /10), Math.round(archPointsIn[0].y * pixel /10)); //对于内线 还要添加首尾两条
//			for (int i = 0; i < 15; i++) {
//				ploygonOut.addPoint(Math.round(archPointsOut[i].x * pixel /10),Math.round( archPointsOut[i].y * pixel /10)); //10 表示初始值
//				ploygonIn.addPoint(Math.round(archPointsIn[i + 1].x* pixel /10), Math.round(archPointsIn[i + 1].y* pixel /10)); //10 表示初始值
//			}
//			ploygonIn.addPoint(Math.round(archPointsIn[16].x* pixel /10), Math.round(archPointsIn[16].y* pixel /10));
			for (int i = 0; i < 17; i++) {
				ploygonOut.addPoint(Math.round(archPointsOut[i].x * pixel / 10), Math.round(archPointsOut[i].y * pixel / 10)); //10 表示初始值
				ploygonIn.addPoint(Math.round(archPointsIn[i].x * pixel / 10), Math.round(archPointsIn[i].y * pixel / 10)); //10 表示初始值
			}
			isDirty = false;
			notifyObservers();
		}
	}

	static final int EPSILON = 36; /* square of distance for picking */
	static final float EPSILON2 = 0.1f; //| 1+1 - 2 | <= ? 表示所能接受的最小范围 

	/**
	 * 选择离该点最近的直线或者端点
	 * 
	 * @param x
	 *                当前鼠标的横坐标
	 * @param y
	 *                当前鼠标的纵坐标
	 * @return 找到端点返回1 找到直线返回2(主要用于改变鼠图案) 否则返回0
	 */
	public int selectPointOrLine(int x, int y) {
		selection = Selection.NONE;
		//判断外线
		int mind1 = Integer.MAX_VALUE;
		for (int i = 0; i < ploygonOut.npoints; i++) {
			int d = sqr(ploygonOut.xpoints[i] - x) + sqr(ploygonOut.ypoints[i] - y);
			if (d < mind1 && d < EPSILON) {
				mind1 = d;
				//selection = i;
				selection = Selection.OUT;
				selection.index = i;
			}
		}
		//判断内线
		int mind2 = Integer.MAX_VALUE;
		for (int i = 0; i < ploygonIn.npoints; i++) {
			int d = sqr(ploygonIn.xpoints[i] - x) + sqr(ploygonIn.ypoints[i] - y);
			if (d < mind2 && d < EPSILON) {
				mind2 = d;
				selection = Selection.IN;
				selection.index = i;
			}
		}
		if (selection != Selection.NONE) {
			selection = mind1 < mind2 ? Selection.OUT : Selection.IN;
			return 1;
		}

		//判断点是否在直线上 用距离来求
		/**
		 * A(out)----------P(x,y)-------------B(int)
		 */
		for (int i = 0; i < ploygonOut.npoints; i++) {
			double pa = Math.hypot(ploygonOut.xpoints[i] - x, ploygonOut.ypoints[i] - y);
			double pb = Math.hypot(ploygonIn.xpoints[i] - x, ploygonIn.ypoints[i] - y);
			double ab = Math.hypot(ploygonIn.xpoints[i] - ploygonOut.xpoints[i], ploygonIn.ypoints[i] - ploygonOut.ypoints[i]);
			//找到一个之后即不再找 与上面算法（上面需要对所有的点都遍历一遍 找到一个最小的）有所不同
			if (pa + pb - ab <= EPSILON2) {
				selection = Selection.LINE;
				selection.index = i;
				return 2;
			}
		}
		return 0;
	}

	// square of an int
	static int sqr(int x) {
		return x * x;
	}

	/**
	 * 改变控制点的坐标
	 * 
	 * @param newPoint
	 *                新的坐标
	 * @param oldPoint
	 *                旧的坐标 该参数只选择线段时有效 
	 */
	public void setPoint(Point newPoint, Point oldPoint) {
		//只要有设置过 就讲isDirty设为假 以后用UndoManger来进行操作

		//再加限制条件 一侧的直线不能越过另一个侧的直线
		/*Side side1 = null;
		Side side2 = null;
		Side side3 = null;
		Side side4 = null;*/
		switch (selection) {
		case OUT:
			/*//右边界
			side1 = judgeWhichSide(newPoint.x, newPoint.y, 
					ploygonOut.xpoints[selection.index + 1], ploygonOut.ypoints[selection.index + 1],
					ploygonIn.xpoints[selection.index + 2], ploygonIn.ypoints[selection.index + 2]);
			//左边界
			side2 = judgeWhichSide(newPoint.x, newPoint.y, 
					ploygonOut.xpoints[selection.index - 1], ploygonOut.ypoints[selection.index - 1],
					ploygonIn.xpoints[selection.index ], ploygonIn.ypoints[selection.index ]);
			//下边界
			side3 = judgeWhichSide(newPoint.x, newPoint.y, 
					ploygonIn.xpoints[selection.index +2], ploygonIn.ypoints[selection.index +2],
					ploygonIn.xpoints[selection.index + 1], ploygonIn.ypoints[selection.index + 1]);
			side4 = judgeWhichSide(newPoint.x, newPoint.y, 
					ploygonIn.xpoints[selection.index +1], ploygonIn.ypoints[selection.index +1],
					ploygonIn.xpoints[selection.index ], ploygonIn.ypoints[selection.index ]);
			if (side1 != Side.RIGHT && side2 != Side.LEFT && side3 != Side.RIGHT && side4 != Side.RIGHT) {*/
			ploygonOut.xpoints[selection.index] = newPoint.x;
			ploygonOut.ypoints[selection.index] = newPoint.y;
			//}
			isDirty = true;
			break;
		case IN:
			/*//右边界
			side1 = judgeWhichSide(newPoint.x, newPoint.y, 
					ploygonOut.xpoints[selection.index ], ploygonOut.ypoints[selection.index ],
					ploygonIn.xpoints[selection.index + 1], ploygonIn.ypoints[selection.index + 1]);
			//左边界
			side2 = judgeWhichSide(newPoint.x, newPoint.y, 
					ploygonOut.xpoints[selection.index - 2], ploygonOut.ypoints[selection.index - 2],
					ploygonIn.xpoints[selection.index -1], ploygonIn.ypoints[selection.index -1]);
			//上边界
			side3 = judgeWhichSide(newPoint.x, newPoint.y, 
					ploygonOut.xpoints[selection.index - 1], ploygonOut.ypoints[selection.index - 1],
					ploygonOut.xpoints[selection.index ], ploygonOut.ypoints[selection.index ]);
			side4 = judgeWhichSide(newPoint.x, newPoint.y, 
					ploygonOut.xpoints[selection.index - 2], ploygonOut.ypoints[selection.index - 2],
					ploygonOut.xpoints[selection.index -1], ploygonOut.ypoints[selection.index -1]);
			if (side1 != Side.RIGHT && side2 != Side.LEFT && side3 != Side.RIGHT && side4 != Side.RIGHT) {*/
			ploygonIn.xpoints[selection.index] = newPoint.x;
			ploygonIn.ypoints[selection.index] = newPoint.y;
			//}
			isDirty = true;
			break;
		case LINE:
			/*int lenx = newPoint.x - oldPoint.x;
			int leny = newPoint.y - oldPoint.y;
			int x1 = ploygonOut.xpoints[selection.index] + lenx;
			int y1 = ploygonOut.ypoints[selection.index] + leny;
			int x2 = ploygonIn.xpoints[selection.index + 1] + lenx;
			int y2 = ploygonIn.ypoints[selection.index + 1] + leny;
			
			side1 = judgeWhichSide(x1, y1, 
					ploygonOut.xpoints[selection.index + 1], ploygonOut.ypoints[selection.index + 1],
					ploygonIn.xpoints[selection.index + 2], ploygonIn.ypoints[selection.index + 2]);
			side2 = judgeWhichSide(x1, y1, 
					ploygonOut.xpoints[selection.index - 1], ploygonOut.ypoints[selection.index - 1],
					ploygonIn.xpoints[selection.index ], ploygonIn.ypoints[selection.index ]);
			side3 = judgeWhichSide(x2, y2, 
					ploygonOut.xpoints[selection.index +1], ploygonOut.ypoints[selection.index +1],
					ploygonIn.xpoints[selection.index + 2], ploygonIn.ypoints[selection.index + 2]);
			side4 = judgeWhichSide(x2, y2, 
					ploygonOut.xpoints[selection.index - 1], ploygonOut.ypoints[selection.index - 1],
					ploygonIn.xpoints[selection.index ], ploygonIn.ypoints[selection.index ]);
			if (side1 != Side.RIGHT && side2 != Side.LEFT && side3 != Side.RIGHT && side4 != Side.LEFT) {*/
			int lenx = newPoint.x - oldPoint.x;
			int leny = newPoint.y - oldPoint.y;
			ploygonOut.xpoints[selection.index] += lenx;
			ploygonOut.ypoints[selection.index] += leny;
			ploygonIn.xpoints[selection.index] += lenx;
			ploygonIn.ypoints[selection.index] += leny;
			//}
			isDirty = true;
			break;
		default:
			break;
		}
		notifyObservers();
	}

	//枚举点在线的位置
	enum Side {
		LEFT, INLINE, RIGHT;
	}

	/**
	 *    p3
	 *   /
	 * P1-------P2
	 * 判断一个点在一条直线（由两个点构成）的哪一侧
	 * @param x3
	 * @param y3
	 * @param x1
	 * @param y1
	 * @param y1
	 * @param y2
	 * @return 
	 */
	@SuppressWarnings("unused")
	private Side judgeWhichSide(int x3, int y3, int x1, int y1, int x2, int y2) {
		int in = y2 * x3 - y1 * x3 + x1 * y3 - x2 * y3 + x2 * y1 - x1 * y2;
		if (in < 0)
			return Side.LEFT;
		else if (in == 0)
			return Side.INLINE;
		else
			return Side.RIGHT;
	}

	/**
	 * 根据实际的面板大小 重新定位它的位置 实时的进行缩放
	 * 
	 * @param oldPixel
	 *                旧的像素值
	 * @param newPixel
	 *                新的像素值
	 */
	public void rePositionedPoints(float oldPixel, float newPixel) {
		for (int i = ploygonOut.npoints - 1; i >= 0; i--) {
			ploygonOut.xpoints[i] = Math.round(ploygonOut.xpoints[i] / oldPixel * newPixel);
			ploygonOut.ypoints[i] = Math.round(ploygonOut.ypoints[i] / oldPixel * newPixel);
		}
		for (int i = ploygonIn.npoints - 1; i >= 0; i--) {
			ploygonIn.xpoints[i] = Math.round(ploygonIn.xpoints[i] / oldPixel * newPixel);
			ploygonIn.ypoints[i] = Math.round(ploygonIn.ypoints[i] / oldPixel * newPixel);
		}
		pixel = newPixel;
	}

	private float pixel = 0L; //用来保存当前的窗口的pixel值

	public void draw(Graphics2D g) {
		//标号
		for (int i = 0; i < ploygonOut.npoints - 1; i++) {
			int midx = (ploygonOut.xpoints[i] + ploygonOut.xpoints[i + 1]) >>> 1;
			int midy = (ploygonOut.ypoints[i] + ploygonOut.ypoints[i + 1]) >>> 1;

			String str = String.format("%3d", i + 1);
			Rectangle2D rect = new Rectangle2D.Double(midx, midy - 12, str.length() * 6, 14);
			g.setStroke(new BasicStroke(0.5f));
			g.setColor(Color.WHITE);
			g.fill(rect);
			g.setColor(Color.LIGHT_GRAY);
			g.draw(rect);
			g.setColor(Color.BLACK);
			g.drawString(str, midx, midy);
		}
		//画牙齿的弓形轮廓
		g.setStroke(new BasicStroke(2.0f));
		controlCurveOut.paint(g);
		controlCurveIn.paint(g);
		//画牙齿分割线
		teethLines.paint(g);
	}

	/**
	 * 画每个牙齿的力度百分比
	 * @param dd 
	 */
	public void drawDataPercent(Graphics2D g, int[] dd) {
		calcDataPercent(dd);
		for (int i = 0; i < ploygonOut.npoints - 1; i++) {
			int midx = (ploygonIn.xpoints[i] + ploygonIn.xpoints[i + 1]) >>> 1;
			int midy = (ploygonIn.ypoints[i] + ploygonIn.ypoints[i + 1]) >>> 1;

			String str = String.format(" %d%s ", Math.round(dataPercent[i]), "%");
			Rectangle2D rect = new Rectangle2D.Double(midx, midy - 12, str.length() * 6, 14);
			g.setStroke(new BasicStroke(0.5f));
			g.setColor(Color.WHITE);
			g.fill(rect);
			g.setColor(Color.LIGHT_GRAY);
			g.draw(rect);
			g.setColor(Color.BLACK);
			g.drawString(str, midx, midy);
		}
	}

	/**
	 * 兼容有DisplayDoc的情况
	 */
	public void savePoints() {
		if (ddoc != null) {
			Point[] p1 = new Point[ploygonOut.npoints];
			for (int i = 0; i < p1.length; i++) {
				int x = Math.round(ploygonOut.xpoints[i] / pixel * 10); //还原成10像素的
				int y = Math.round(ploygonOut.ypoints[i] / pixel * 10);
				p1[i] = new Point(x, y);
			}
			Point[] p2 = new Point[ploygonIn.npoints];
			for (int i = 0; i < p2.length; i++) {
				int x = Math.round(ploygonIn.xpoints[i] / pixel * 10);
				int y = Math.round(ploygonIn.ypoints[i] / pixel * 10);
				p2[i] = new Point(x, y);
			}
			ddoc.setAllArchPoints(p1, p2);
			//ddoc同步后将isDirty设为假
			isDirty = false;
			notifyObservers();
		}
	}

	class MyPoint {
		int x, y;
		int val;

		public MyPoint(int x, int y, int val) {
			this.x = x;
			this.y = y;
			this.val = val;
		}
	}

	//private byte[] data = null;//从view中得来得数据
	private float[] dataPercent = null; //计算的每一块区域的百分比值
	private float[] dataValue = null; //每一块区域的值

	/**
	 * 计算dataPercent
	 * @param data 
	 */
	private void calcDataPercent(int[] data) {
		ArrayList<MyPoint> points = new ArrayList<MyPoint>();
		float total = 0L;
		//构造链表
		for (int i = 0; i < data.length; i++) {
			int x = i / 44;
			int y = i % 44;
			int val = data[i];//< 0 ? data[i] + 256 : data[i];
			//在这里直接去掉0值点 不考虑
			if (val != 0) {
				MyPoint mp = new MyPoint(x, y, val);
				points.add(mp);
			}
		}
		//以最外层的点做基准
		for (int i = 0; i < ploygonOut.npoints; i++) {
			dataValue[i] = 0;
			ArrayList<MyPoint> pps = new ArrayList<MyPoint>(points);
			int ax = ploygonOut.xpoints[i];
			int ay = ploygonOut.ypoints[i];
			int bx = ploygonOut.xpoints[i + 1];
			int by = ploygonOut.ypoints[i + 1];
			int cx = ploygonIn.xpoints[i + 1];
			int cy = ploygonIn.ypoints[i + 1];
			int dx = ploygonIn.xpoints[i];
			int dy = ploygonIn.ypoints[i];
			for (MyPoint mp : pps) {
				/**
				 * a-----b
				 * |     |
				 * |	 |
				 * d-----c
				 */
				if (judgePointInBox(mp.x * pixel, mp.y * pixel, ax, ay, bx, by, cx, cy, dx, dy)) {
					dataValue[i] += mp.val;
					total += mp.val;
					points.remove(mp);
				}
			}
		}

		//计算百分比
		for (int i = 0; i < ploygonOut.npoints; i++) {
			dataPercent[i] = dataValue[i] / total * 100;
		}
	}

	/**
	 * 判断某个点 是否在一个四边形内
	 * a-----b
	 * |     |
	 * |	 |
	 * d-----c
	 * 算法：将四边形分成两个三角形 再判断点是否在这两个三角形内 
	 * 判断点是否在三角形内的算法：用点积
	 * @param p
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private boolean judgePointInBox(float px, float py, int ax, int ay, int bx, int by, int cx, int cy, int dx, int dy) {
		GeneralPath path = new GeneralPath();
		path.moveTo(ax, ay);
		path.lineTo(bx, by);
		path.lineTo(cx, cy);
		path.lineTo(dx, dy);
		path.closePath();
		return Path2D.contains(path.getPathIterator(null), new Point2D.Float(px, py));
	}

	public boolean isDirty() {
		return isDirty;
	}

	public WeightInput[] calcCountAndValue(int[] data) {
		WeightInput[] wis = new WeightInput[16];

		ArrayList<MyPoint> points = new ArrayList<MyPoint>();
		//构造链表
		for (int i = 0; i < data.length; i++) {
			int x = i / 44;
			int y = i % 44;
			int val = data[i];//< 0 ? data[i] + 256 : data[i];
			//在这里直接去掉0值点 不考虑
			if (val != 0) {
				MyPoint mp = new MyPoint(x, y, val);
				points.add(mp);
			}
		}
		//以最外层的点做基准
		for (int i = 0; i < ploygonOut.npoints-1; i++) {
			wis[i]= new WeightInput();
			int counter = 0;
			int value = 0;

			ArrayList<MyPoint> pps = new ArrayList<MyPoint>(points);
			int ax = ploygonOut.xpoints[i];
			int ay = ploygonOut.ypoints[i];
			int bx = ploygonOut.xpoints[i + 1];
			int by = ploygonOut.ypoints[i + 1];
			int cx = ploygonIn.xpoints[i + 1];
			int cy = ploygonIn.ypoints[i + 1];
			int dx = ploygonIn.xpoints[i];
			int dy = ploygonIn.ypoints[i];
			for (MyPoint mp : pps) {
				/**
				 * a-----b
				 * |     |
				 * |	 |
				 * d-----c
				 */
				if (judgePointInBox(mp.x * pixel, mp.y * pixel, ax, ay, bx, by, cx, cy, dx, dy)) {
					counter++;
					value += mp.val;
					points.remove(mp);
				}
			}

			wis[i].setCount(counter);
			wis[i].setValue(value);
		}
		return wis;
	}

}
