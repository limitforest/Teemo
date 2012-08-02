package com.ciotc.teemo.file.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.prefs.Preferences;

import com.ciotc.contour.MyColor;
import com.ciotc.teemo.file.template.RealtimeFileTemplate;
import com.ciotc.teemo.frame.MainFrame;

public class Realtime2DView extends RealtimeView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int totalNum = 2288;
	/**
	 * 像素
	 */
	public float pixel = 10;

	/**
	 * 行数 44
	 */
	public int rowNum = 44;

	/**
	 * 列数
	 */
	public int colNum = 52;

	Preferences pref = Preferences.userNodeForPackage(MainFrame.class);

	public Realtime2DView(RealtimeFileTemplate dfTemplate) {
		super(dfTemplate);
	}

	@Override
	public void drawFrame(Graphics g2, byte[] dd) {
		Color oldColor;
		int x, y;
		//中心点
		Point point = null;
		float sum = 0;
		float productfx = 0, productfy = 0;

		Graphics2D g = (Graphics2D) g2;
		oldColor = g.getColor();
		// 背景为灰色
		g.setColor(Color.LIGHT_GRAY);

		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(new Color(0, 0, 0));

		for (int ii = 0; ii < rowNum; ii++)
			for (int jj = 0; jj < colNum; jj++)
				//{ // 46
				if (backgroundArray[ii][jj] == 1) {
					g.setColor(Color.WHITE);
					Rectangle2D rect = new Rectangle2D.Float((colNum - jj - 1) * pixel, (rowNum - ii - 1) * pixel, pixel, pixel);
//						Rectangle2D rect = new Rectangle2D.Float(jj * pixel,ii  * pixel,pixel,pixel);
					g.fill(rect);
					//g.setColor(Color.BLACK);
					//g.draw(rect);

				}
		oldColor = g.getColor();

		int index = pref.getInt("sensorSize", 2); //1表示大 2表示1

		int lower = pref.getInt("lower", 0); //下限值
		
	
		/**
		 * 画矩阵 
		 * 同时还求出中心点
		 */
		for (int i = 0; i < totalNum; i++) {
			Color color = null;

			x = i / rowNum; // rowNum == 44; x
			y = i % rowNum; // y

			int f = dd[i];

			//if(backgroundArray[rowNum - y - 1][colNum - x - 1]==0) dataColor = 0;

			if (index == 2) {
				if (existArrayInSmall[rowNum - y - 1][colNum - x - 1] == 0) //先用小的
//				if(existArrayInSmall[y ][x] == 0) //先用小的
					f = 0;
			} else {
				if (existArrayInLarge[rowNum - y - 1][colNum - x - 1] == 0) //先用小的
					f = 0;
			}

			if (f < 0)
				f += 256;

			if (f < lower)
				f = 0;

			sum = sum + f;

			//更改牙齿方位 上面公式不适合
			productfx += x * f;
			productfy += y * f;

			if (f == 255)
				color = MyColor.values()[16].getRgb();
			else if (f == 0) {
				// color = MyColor.values()[17].getRgb();
				continue;
			} else {
				int in = f >>> 4;
				color = MyColor.values()[in].getRgb();
			}

			g.setColor(color);

			Rectangle2D rect = new Rectangle2D.Float(x * pixel, y * pixel, pixel, pixel);

			g.fill(rect);

		}

		if (sum == 0) {
			point = new Point(0, 0);

		} else {
			x = (int) (productfx / sum);
			y = (int) (productfy / sum);
			point = new Point(x, y);
		}

//		drawCOF(g, point);

		g.setColor(oldColor);
	}

	/**
	 * 画中心点
	 * 
	 * @param g
	 */
	private void drawCOF(Graphics2D g, Point p) {
		int x;
		int y;
		if (p == null)
			return;
		/**
		 * 画中心点
		 */
		x = p.x;
		y = p.y;
		// 画菱形的算法
		// 菱形内有两条线
		float m = 0.5f;

		// 第一种算法对应的公式
		if (x != 0 || y != 0) {
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

}
