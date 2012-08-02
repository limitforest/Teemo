package com.ciotc.teemo.file.frame;

import static com.ciotc.teemo.util.SwingConsole.getSizeDependOnScreen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.ciotc.teemo.file.template.RealtimeFileTemplate;
import com.ciotc.teemo.file.view.RealtimeStatusBar;
import com.ciotc.teemo.file.view.RealtimeView;

/**
 * 使JInternalFrame更美观<hr>a</hr>
 * 摘自<a href=http://devbean.blog.51cto.com/448512/92426>FinderCheng 的BLOG</a>
 * 与DisplayInternalFrame，只有少部分更改，以后将这两个类再整合一下
 * @author FinderCheng(部分),林晓智
 * 
 */
public class RealtimeInternalFrame extends ActivedInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RealtimeFileTemplate fileTemplate;

	int preWidth;
	int preHeight;

	private RealtimeView view = null;

	/**
	 * 状态栏
	 */
//	private RealtimeStatusBar statusField = null;

	/**
	 * 一次缩放的像素值
	 */
	int rotateUnitWidth = 52;
	int rotateUnitHeight = 44;
	int minWidthHeight = 0;
	/**
	 * 
	 */
	int rotateNum = 0;

	int maxWidth = 0; //最大的宽度
	int maxHeight = 0;

	int h0 = 50; //108
	int w0 = 16;//30

	/**
	 * Instantiates a new inner frame.
	 */
	public RealtimeInternalFrame(String title, RealtimeFileTemplate fileTemplate) {
		super(title, false, true, false, true); //不可以改变窗口大小，这与DisplayInternalFrame不同
		this.fileTemplate = fileTemplate;

		//setSize(528, 496);//468=460+8 456=400+56
		int random = new Random().nextInt(100);
		setLocation(random, random);
		//maximizable = true;
		//closable = true;
		int constantWidth = rotateUnitWidth * 10 + w0;
		int constantHeight = rotateUnitHeight * 10 + h0;
		setSize(constantWidth, constantHeight);

		setVisible(false); //先置假
		setResizable(false);
		setMinimumSize(new Dimension(528, 496)); //468 456

		//设置图的最大尺寸
		Dimension dimension = getSizeDependOnScreen(0.8f, 0.8f);
		maxWidth = dimension.width;
		maxHeight = dimension.height;
		//System.out.println("MaxSize:"+maxWidth+","+maxHeight);

		if (maxWidth < maxHeight) {
			minWidthHeight = 0;
			rotateNum = (maxWidth - constantWidth) / rotateUnitWidth;
			int remainder = (maxWidth - constantWidth) % rotateUnitWidth;
			if (remainder <= 26) { //22 == 44 /2
				maxWidth -= remainder;
				maxHeight = constantHeight + rotateNum * rotateUnitHeight;
			} else {
				maxWidth += (rotateNum - remainder);
				rotateNum += 1;
				maxHeight = constantHeight + rotateNum * rotateUnitHeight;
			}
		} else {
			minWidthHeight = 1;
			rotateNum = (maxHeight - constantHeight) / rotateUnitHeight;
			int remainder = (maxHeight - constantHeight) % rotateUnitHeight;
			if (remainder <= 22) { // 19 == 40 /2 -1
				maxHeight -= remainder;
				maxWidth = constantWidth + rotateNum * rotateUnitWidth;
			} else {
				maxHeight += (rotateNum - remainder);
				rotateNum += 1;
				maxWidth = constantWidth + rotateNum * rotateUnitWidth;
			}
		}
		setMaximumSize(new Dimension(maxWidth, maxHeight));

		//System.out.println("MaxSize:"+maxWidth+","+maxHeight);
		//addComponentListener(new MyComponentAdapter());
		//addMouseWheelListener(new MyMouseWheelListener());
		addInternalFrameListener(new MyInterFrameAdapter());

		//statusField = new RealtimeStatusBar();
		//statusField.setEditable(false);
		//this.getContentPane().add(statusField, BorderLayout.SOUTH);
		//System.out.println(getDefaultCloseOperation());
		setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

	}

	public void addDisplayView(RealtimeView view) {
		this.view = view;
		this.getContentPane().add(this.view, BorderLayout.CENTER);
		//setDefaultCloseOperation(this.view.getInternalFrameDefaultOperation());
	}
	
	public void addStatusBar(RealtimeStatusBar r2dStatusBar) {
		getContentPane().add(r2dStatusBar, BorderLayout.SOUTH);
	}

	public RealtimeView getView() {
		return view;
	}

	public RealtimeFileTemplate getFileTemplate() {
		return fileTemplate;
	}

//	/**
//	 * 显示录制时当前的一些状态信息
//	 * @param complete 是否已经录制结束
//	 * @param record 是否开始录制
//	 * @param info 传过来的 参数应该是<b>当前录制到第几帧</b> <b>录制的速度</b><br>
//	 */
//	public void showStatusField(boolean record ,boolean complete,Object... info) {
//		statusField.showText(record,complete,info);
//	}

	

	class MyInterFrameAdapter extends InternalFrameAdapter {

		@Override
		public void internalFrameClosing(InternalFrameEvent e) {
			//System.out.println("closing");
			super.internalFrameClosing(e);
			fileTemplate.closeDirectly();
		}

	}


}
