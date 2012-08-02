package com.ciotc.teemo.util;

import static com.ciotc.teemo.resource.MyResources.getResourceString;
import static com.ciotc.teemo.util.UpDateDB.updateDB;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.usbdll.USBDLL;

public class SwingConsole {
	/**
	 * 一启动看有无连接上传感器 仅各提示而已
	 */
	private static void giveTips() {
		int deviceIndex = 0;
		synchronized (MainFrame.lock) {
			deviceIndex = USBDLL.getDevicesNum();
		}
		if (deviceIndex == 0) { //没有连接
			JLabel lblDialog = new JLabel(getResourceString("noHandleTips"));
			lblDialog.setFont(Constant.font);
			JOptionPane.showMessageDialog(null, lblDialog, getResourceString("Teemo"), JOptionPane.WARNING_MESSAGE);
		}
	}

	public static void run(final float widthRate, final float heightRate) {
		try {
//			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//				if ("Metal".equals(info.getName())) {
//					UIManager.setLookAndFeel(info.getClassName());
//					break;
//				}
//			}
			//LookAndFeel laf = UIManager.getLookAndFeel();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Font font = new Font("微软雅黑", Font.PLAIN, 12);
			UIManager.put("Button.font", font);
			UIManager.put("Menu.font", font);
			UIManager.put("MenuItem.font", font);
		} catch (Exception ignored) {
		}
		
		updateDB();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 选择当前操作系统的外观
				/*
				 * try { UIManager.setLookAndFeel(UIManager.
				 * getSystemLookAndFeelClassName()); } catch
				 * (ClassNotFoundException e) { // TODO
				 * Auto-generated catch block
				 * e.printStackTrace(); } catch
				 * (InstantiationException e) { // TODO
				 * Auto-generated catch block
				 * e.printStackTrace(); } catch
				 * (IllegalAccessException e) { // TODO
				 * Auto-generated catch block
				 * e.printStackTrace(); } catch
				 * (UnsupportedLookAndFeelException e) { // TODO
				 * Auto-generated catch block
				 * e.printStackTrace(); }
				 */

//				try {
//					UIManager.setLookAndFeel(new SubstanceLookAndFeel());
//					JFrame.setDefaultLookAndFeelDecorated(true);
//					JDialog.setDefaultLookAndFeelDecorated(true);
//					// SubstanceLookAndFeel.setCurrentTheme(new
//					// SubstanceTerracottaTheme());
//					// SubstanceLookAndFeel.setSkin(new
//					// EmeraldDuskSkin());
//					// SubstanceLookAndFeel.setCurrentButtonShaper(new
//					// ClassicButtonShaper());
//					// SubstanceLookAndFeel.setCurrentWatermark(new
//					// SubstanceBubblesWatermark());
//					// SubstanceLookAndFeel.setCurrentBorderPainter(new
//					// StandardBorderPainter());
//					// SubstanceLookAndFeel.setCurrentGradientPainter(new
//					// StandardGradientPainter());
//					SubstanceLookAndFeel.setCurrentTitlePainter(new FlatTitlePainter());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				JFrame frame = new MainFrame();
				
				//设置工具栏的显示时间
				ToolTipManager tm = ToolTipManager.sharedInstance();
				tm.setInitialDelay(100);
				tm.setReshowDelay(300);
				/*System.out.println(tm.getDismissDelay()+"");
				System.out.println(tm.getInitialDelay()+"");
				System.out.println(tm.getReshowDelay()+"");*/

				Dimension dimension = getSizeDependOnScreen(widthRate, heightRate);
				Dimension dimensionTop = getSizeDependOnScreen((1 - widthRate) / 2, (1 - heightRate) / 2);

				frame.setLocation(dimensionTop.width, dimensionTop.height);
				frame.setMinimumSize(new Dimension(750, 500));
				//f.setSize(dimension);
				frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				//f.setTitle(f.getClass().getSimpleName());

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}

		});
	}

	/**
	 * 根据屏幕大小返回合适的尺寸
	 * @param widthRate 宽度比例
	 * @param heightRate 高度比例
	 * @return 尺寸大小
	 */
	public static Dimension getSizeDependOnScreen(double widthRate, double heightRate) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new Dimension((int) (screenSize.width * widthRate), (int) (screenSize.height * heightRate));

	}

}
