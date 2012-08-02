package com.ciotc.teemo.print;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.file.view.Display2DView;
import com.ciotc.teemo.file.view.GraphView;
import com.ciotc.teemo.util.Constant;

public class PrintData {
private Book book;
private BufferedImage srcImg1;
private BufferedImage srcImg2;
private JFrame frame;

public PrintData(DisplayFileTemplate dtf, boolean directlly) {
	setPrintData(dtf);
	if (directlly == false) {
		// 显示打印预览界面。
		PreviewDialog dialog = new PreviewDialog("打印预览", null, this.book);
		dialog.setVisible(true);
	} else {
		// 直接打印不显示打印预览界面。
		new PrintAction(this.book);
	}
	
}

private void setPrintData(DisplayFileTemplate dtf) {
	final List<String> listPrint = new ArrayList<String>();
	// StringBuff 的append方法和用html标签的方法都不可行。
	if (dtf == null) {
		try {
			throw new Exception("DisplayFileTemplate 对象传递错误。");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} else {
		listPrint.add("患者姓名：" + dtf.commentDialog.getTxtName());
		listPrint.add("患者ID：" + dtf.commentDialog.getTxtId());
		listPrint.add("患者身份证号：" + dtf.commentDialog.getTxtIdCard());
		listPrint.add("患者出生年月：" + dtf.commentDialog.getTxtBirth());
		listPrint.add("患者性别：" + dtf.commentDialog.getTxtGender());
		listPrint.add("患者门牙宽度：" + dtf.commentDialog.getTxtArch());
		// 每行打印的字符个数。
		int length = 36;
		String pntComments = "患者影片注解：" + dtf.commentDialog.getTxtAreaComnt();
		int lines = pntComments.length() / length;
		
		List<String> listComments = new ArrayList<String>();
		String temp = pntComments;
		if (lines > 0) {
			int linesNum = 0;
			if (pntComments.length() % length == 0) {
				linesNum = lines;
			} else {
				linesNum = lines + 1;
			}
			for (int i = 1; i <= linesNum; i++) {
				// 判断最后一行是否越界
				if (length * i - 1 > pntComments.length()) {
					listComments.add(temp.substring(length * (i - 1), pntComments.length() - 1));
				} else {
					listComments.add(temp.substring(length * (i - 1), length * i - 1));
				}
				
			}
			for (int j = 0; j < listComments.size(); j++) {
				listPrint.add(listComments.get(j));
			}
		} else {
			// 长度不足length
			listPrint.add(pntComments);
		}
		
		srcImg1 = getImage(dtf.d2dView);
		srcImg2 = getImage2(dtf.graphView);
		// srcImg2 = getImage("images/srcImg1.jpg");
		book = new Book();
	}
	// Page1
	book.append(new Printable() {
		public int print(Graphics g, PageFormat f, int index) {
			g.setFont(Constant.font);
			int xOff = 72, yOff = 72;
			for (int i = 0; i < listPrint.size(); i++) {
				g.drawString(listPrint.get(i), xOff, yOff += (g.getFontMetrics().getHeight() + 4));
			}
			g.drawString("- - - - - - - - - - - 以上是患者基本信息。患者牙齿图片在第二页- - - - - - - - - - - - ", 72,
					700);
			g.drawString("Page 1/2", 300, 720);
			return index == 0 ? 0 : 1;
		}
	}, new PageFormat());
	
	// Page2
	book.append(new Printable() {
		public int print(Graphics g, PageFormat f, int index) {
			g.setFont(Constant.font);
			Graphics2D g2d = (Graphics2D) g;
			// 直接固定起点画。
			g2d.drawImage(srcImg1, 70, 100, null);
//			System.out.println(srcImg1.getHeight());
//			System.out.println(srcImg1.getWidth());
			g2d.drawImage(srcImg2, 70, srcImg1.getHeight() + 100+10, null);
			// g2d.drawImage(srcImg2, 60, srcImg1.getHeight() + 10, null);
			g.drawString("Page 2/2", 300, 720);
			return index == 1 ? 0 : 1;
		}
	}, new PageFormat());
	
	// 横向页。Page3
	/*PageFormat format = new PageFormat();
	format.setOrientation(0);
	book.append(new Printable() {
		public int print(Graphics g, PageFormat f, int index) {
			Graphics2D g2d = (Graphics2D) g;
			g.setFont(new Font("微软雅黑", 1, 48));
			int stringWidth = g.getFontMetrics().stringWidth("横向纸张 放图形测试。");
			int x = (int) f.getWidth() / 2 - stringWidth / 2;
			int y = (int) f.getHeight() / 2;
			GradientPaint redtowhite = new GradientPaint(x - 40, y, Color.red,
					x + stringWidth + 40, y, Color.green);
			g2d.setPaint(redtowhite);
			g.drawString("横向纸张 放图形测试。", x, y);
			return index == 2 ? 0 : 1;
		}
	}, format);*/
}

private BufferedImage getImage(Display2DView view) {
	BufferedImage bi = new BufferedImage(view.getWidth(), view.getHeight(),
			BufferedImage.TYPE_INT_RGB);
	Graphics g = bi.getGraphics();
	g.setColor(Color.WHITE);
	g.fillRect(0, 0, view.getWidth(), view.getHeight());
	view.drawFrame(g, view.displayData);
	g.dispose();
	return zoomImage(bi,440,400);
}

/**
 * 缩放原始图片到合适大小.
 * 
 * @param srcImage
 *            原始图片
 * @param fixedWidth
 *            规定的宽度
 * @param fixedHeight
 *            规定的高度
 * @return BufferedImage - 处理后的图片
 */
public BufferedImage zoomImage(BufferedImage srcImage, int fixedWidth, int fixedHeight) {
	int MAX_WIDTH = fixedWidth;// TODO: 缩放后的图片最大宽度
	int MAX_HEIGHT = fixedHeight;// TODO: 缩放后的图片最大高度
	int imageWidth = srcImage.getWidth(null); // 800
	int imageHeight = srcImage.getHeight(null); // 600
	
	int thumbWidth = MAX_WIDTH;
	int thumbHeight = MAX_HEIGHT;
	double thumbRatio = (double) thumbWidth / (double) thumbHeight; // 1 ： 图片的比率
	double imageRatio = (double) imageWidth / (double) imageHeight; // 8/6
	if (thumbRatio < imageRatio) {
		thumbHeight = (int) (thumbWidth / imageRatio);
	} else {
		thumbWidth = (int) (thumbHeight * imageRatio);
	}
	// 如果图片小于所略图大小, 不作处理
	if (imageWidth < MAX_WIDTH && imageHeight < MAX_HEIGHT) {
		thumbWidth = imageWidth;
		thumbHeight = imageHeight;
	}
	
	BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
			BufferedImage.TYPE_INT_RGB);
	Graphics2D graphics2D = thumbImage.createGraphics();
	graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	graphics2D.drawImage(srcImage, 0, 0, thumbWidth, thumbHeight, null);
//	System.out.println("thumbWidth=" + thumbWidth);
//	System.out.println("thumbHeight=" + thumbHeight);
	return thumbImage;
	
}

private BufferedImage getImage2(GraphView view) {
	int width = 440;
	int height = 200;
	return view.chart2Image(width, height);
}

}