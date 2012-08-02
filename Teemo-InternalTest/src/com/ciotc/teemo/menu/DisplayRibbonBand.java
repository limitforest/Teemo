package com.ciotc.teemo.menu;

import static com.ciotc.teemo.resource.MyResources.getResizableIconFromResource;
import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;

import com.ciotc.teemo.action.MovieAction;
import com.ciotc.teemo.file.model.DisplayModel;
import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.frame.MainTabbedPane;
import com.ciotc.teemo.print.PrintData;
import com.ciotc.teemo.util.FileOperation;

/**
 * 显示窗口的菜单栏
 * 
 * @author Linxiaozhi
 * 
 */
public class DisplayRibbonBand extends JRibbonBand implements PropertyChangeListener, ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DisplayFileTemplate dft;

//	private JCommandButton button1;
//	private JCommandButton button2;
//	private JCommandButton button3;
//	private JCommandButton button4;
//	private JCommandButton button5;
//	private JCommandButton button6;
//
//	private JCommandMenuButton[] buttons1;
//	private JCommandMenuButton[] buttons2;
//	private JCommandMenuButton[] buttons3;
//	private JCommandMenuButton[] buttons4;
//	private JCommandMenuButton[] buttons5;
//	private JCommandMenuButton[] buttons6;
	// private ResizableIcon icon;

//	private MDIDesktopPane desktop;
//	private DisplayFileTemplate fileTemplate;

//	public void setDesktop(MDIDesktopPane desktop) {
//		this.desktop = desktop;
//	}

//	private JCommandPopupMenu createMenu4Button1() {
//		JCommandPopupMenu menu = new JCommandPopupMenu();
//		buttons1 = new JCommandMenuButton[3];
//		buttons1[0] = new JCommandMenuButton(getResourceString("2D"),
//				getResizableIconFromResource("images/frame2D.png"));
//		buttons1[1] = new JCommandMenuButton(getResourceString("3D"),
//				getResizableIconFromResource("images/frame3D.png"));
//		buttons1[2] = new JCommandMenuButton(getResourceString("graph"),
//				getResizableIconFromResource("images/frameGraph.png"));
//
//		fileTemplate = desktop.getSelectedFileTemplate();
//
//		for (int i = 0; i < 3; i++) {
//			menu.addMenuButton(buttons1[i]);
//		}
//
//		if (fileTemplate == null) {
//			for (int i = 0; i < 3; i++)
//				buttons1[i].setEnabled(false);
//			return menu;
//		}
//
//		if (!fileTemplate.isExist2D) {
//			// menu1 = new ChildViewMenuItem(fileTemplate.d3DInFrame);
//			buttons1[0].addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					// TODO Auto-generated method stub
//					// ViewMenu.this.fileTemplate.build2D(ViewMenu.this.desktop);
//					fileTemplate.isExist2D = true;
//					fileTemplate.d2DInFrame.setVisible(true);
//				}
//
//			});
//			// buttons[0].setIcon(fileTemplate.d3DInFrame.getFrameIcon());
//			// add(menu1);
//		} else {
//			// buttons[0] = new JMenuItem("2D");
//			// add(buttons[0]);
//			buttons1[0].setEnabled(false);
//
//		}
//		if (!fileTemplate.isExist3D) {
//			// buttons[1] = new ChildViewMenuItem(fileTemplate.d2DInFrame);
//			buttons1[1].addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					// TODO Auto-generated method stub
//					// ViewMenu.this.fileTemplate.build3D(ViewMenu.this.desktop);
//					fileTemplate.isExist3D = true;
//					fileTemplate.d3DInFrame.setVisible(true);
//				}
//
//			});
//		} else {
//			buttons1[1].setEnabled(false);
//		}
//		if (!fileTemplate.isExistGrpah) {
//			buttons1[2].addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					fileTemplate.isExistGrpah = true;
//					fileTemplate.graphInFrame.setVisible(true);
//
//				}
//			});
//		} else {
//			buttons1[2].setEnabled(false);
//		}
//
//		return menu;
//	}
//
//	private JCommandPopupMenu createMenu4Button2() {
//		JCommandPopupMenu menu = new JCommandPopupMenu();
//		buttons2 = new JCommandMenuButton[2];
//		buttons2[0] = new JCommandMenuButton(getResourceString("playforward"),
//				getResizableIconFromResource("images/toolbar/play.png"));
//		buttons2[1] = new JCommandMenuButton(getResourceString("stop"),
//				getResizableIconFromResource("images/toolbar/stop.png"));
//
//		for (int i = 0; i < 2; i++) {
//			menu.addMenuButton(buttons2[i]);
//			buttons2[i].setEnabled(true);
//		}
//
//		JInternalFrame internalFrame = desktop.getSelectedFrame();
//		if (internalFrame == null
//				|| internalFrame instanceof RealtimeInternalFrame) {
//			for (int i = 0; i < 2; i++)
//				buttons2[i].setEnabled(false);
//			return menu;
//		}
//
//		if (internalFrame instanceof DisplayInternalFrame
//				|| internalFrame instanceof GraphInternalFrame) {
//
//			MoiveListener ml = new MoiveListener();
//			ml.setFrame(internalFrame);
//			buttons2[0].addActionListener(ml);
//			buttons2[1].addActionListener(ml);
//
//			DisplayModel dm;
//			if (internalFrame instanceof DisplayInternalFrame) {
//				DisplayInternalFrame inFrame = (DisplayInternalFrame) internalFrame;
//				dm = inFrame.getDisplayFileTemplate().getDisplayModel();
//			} else {
//				GraphInternalFrame inFrame = (GraphInternalFrame) internalFrame;
//				dm = inFrame.getDisplayFileTemplate().getDisplayModel();
//			}
//			if (dm.isStrange()) {
//				buttons2[0].setEnabled(false);
//				buttons2[1].setEnabled(false);
//			}
//			boolean isPlay = dm.isPlay();
//			if (isPlay == true) // 不能播放
//				buttons2[0].setEnabled(false);
//			if (isPlay == false) // 不能停止
//				buttons2[1].setEnabled(false);
//
//		}
//
//		return menu;
//	}
//
//	private JCommandPopupMenu createMenu4Button3() {
//		JCommandPopupMenu menu = new JCommandPopupMenu();
//		buttons3 = new JCommandMenuButton[5];
//		ResizableIcon icon0 = getResizableIconFromResource("images/toolbar/star_3.png");
//		ResizableIcon icon1 = getResizableIconFromResource("images/toolbar/star.png");
//		buttons3[0] = new JCommandMenuButton(getResourceString("slowest"),
//				icon0);
//		buttons3[1] = new JCommandMenuButton(getResourceString("mediumSlow"),
//				icon0);
//		buttons3[2] = new JCommandMenuButton(getResourceString("nomal"), icon0);
//		buttons3[3] = new JCommandMenuButton(getResourceString("mediumFast"),
//				icon0);
//		buttons3[4] = new JCommandMenuButton(getResourceString("fastest"),
//				icon0);
//
//		MoiveListener ml = new MoiveListener();
//		for (int i = 0; i < 5; i++) {
//			menu.addMenuButton(buttons3[i]);
//			buttons3[i].setEnabled(true);
//			buttons3[i].addActionListener(ml);
//		}
//
//		JInternalFrame internalFrame = desktop.getSelectedFrame();
//		if (internalFrame == null
//				|| internalFrame instanceof RealtimeInternalFrame) {
//			for (int i = 0; i < 5; i++)
//				buttons3[i].setEnabled(false);
//			return menu;
//		}
//
//		if (internalFrame instanceof DisplayInternalFrame
//				|| internalFrame instanceof GraphInternalFrame) {
//
//			ml.setFrame(internalFrame);
//
//			DisplayModel dm;
//			if (internalFrame instanceof DisplayInternalFrame) {
//				DisplayInternalFrame inFrame = (DisplayInternalFrame) internalFrame;
//				dm = inFrame.getDisplayFileTemplate().getDisplayModel();
//			} else {
//				GraphInternalFrame inFrame = (GraphInternalFrame) internalFrame;
//				dm = inFrame.getDisplayFileTemplate().getDisplayModel();
//			}
//			if (dm.isStrange()) {
//				for (int i = 0; i < 5; i++)
//					buttons3[i].setEnabled(false);
//			}
//
//			int selectedSpeed = dm.getSelectedSpeed();
//
//			buttons3[selectedSpeed].setIcon(icon1);
//
//		}
//
//		return menu;
//	}
//
//	private JCommandPopupMenu createMenu4Button4() {
//		JCommandPopupMenu menu = new JCommandPopupMenu();
//		buttons4 = new JCommandMenuButton[4];
//
//		buttons4[0] = new JCommandMenuButton(getResourceString("first"),
//				getResizableIconFromResource("images/toolbar/previous.png"));
//		buttons4[1] = new JCommandMenuButton(getResourceString("last"),
//				getResizableIconFromResource("images/toolbar/next.png"));
//		buttons4[2] = new JCommandMenuButton(getResourceString("forward"),
//				getResizableIconFromResource("images/toolbar/forward.png"));
//		buttons4[3] = new JCommandMenuButton(getResourceString("backward"),
//				getResizableIconFromResource("images/toolbar/backward.png"));
//
//		MoiveListener ml = new MoiveListener();
//		for (int i = 0; i < 4; i++) {
//			menu.addMenuButton(buttons4[i]);
//			buttons4[i].setEnabled(true);
//			buttons4[i].addActionListener(ml);
//		}
//
//		JInternalFrame internalFrame = desktop.getSelectedFrame();
//		if (internalFrame == null
//				|| internalFrame instanceof RealtimeInternalFrame) {
//			for (int i = 0; i < 4; i++)
//				buttons4[i].setEnabled(false);
//			return menu;
//		}
//
//		if (internalFrame instanceof DisplayInternalFrame
//				|| internalFrame instanceof GraphInternalFrame) {
//
//			ml.setFrame(internalFrame);
//
//			DisplayModel dm;
//			if (internalFrame instanceof DisplayInternalFrame) {
//				DisplayInternalFrame inFrame = (DisplayInternalFrame) internalFrame;
//				dm = inFrame.getDisplayFileTemplate().getDisplayModel();
//			} else {
//				GraphInternalFrame inFrame = (GraphInternalFrame) internalFrame;
//				dm = inFrame.getDisplayFileTemplate().getDisplayModel();
//			}
//			if (dm.isStrange()) {
//				for (int i = 0; i < 4; i++)
//					buttons4[i].setEnabled(false);
//			}
//
//			int frameIndex = dm.getFrameIndex();
//			int totalFrameNum = dm.getTotalFrameNum();
//			if (frameIndex == 0) // 不能向前播放
//				buttons4[2].setEnabled(false);
//			if (frameIndex >= totalFrameNum - 1) // 不能向后播放
//				buttons4[3].setEnabled(false);
//
//		}
//
//		return menu;
//	}
//
//	private JCommandPopupMenu createMenu4Button5() {
//		JCommandPopupMenu menu = new JCommandPopupMenu();
//		buttons5 = new JCommandMenuButton[5];
//
//		buttons5[0] = new JCommandMenuButton(getResourceString("sendDcm"),
//				getResizableIconFromResource("images/toolbar/send.png"));
//		buttons5[1] = new JCommandMenuButton(getResourceString("saveAsMovie"),
//				getResizableIconFromResource("images/toolbar/save_as.png"));
//
//		SendDCMActionListener ml1 = new SendDCMActionListener();
//		SaveMovieListener ml2 = new SaveMovieListener();
//		for (int i = 0; i < 2; i++) {
//			menu.addMenuButton(buttons5[i]);
//			buttons5[i].setEnabled(true);
//
//		}
//
//		JInternalFrame internalFrame = desktop.getSelectedFrame();
//		if (internalFrame == null
//				|| internalFrame instanceof RealtimeInternalFrame) {
//			for (int i = 0; i < 2; i++)
//				buttons5[i].setEnabled(false);
//			return menu;
//		}
//
//		if (internalFrame instanceof DisplayInternalFrame
//				|| internalFrame instanceof GraphInternalFrame) {
//			buttons5[0].addActionListener(ml1);
//			buttons5[1].addActionListener(ml2);
//			ml1.setFrame(internalFrame);
//			ml2.setFrame(internalFrame);
//		}
//
//		return menu;
//	}
//
//	private JCommandPopupMenu createMenu4Button6() {
//		JCommandPopupMenu menu = new JCommandPopupMenu();
//		buttons6 = new JCommandMenuButton[2];
//
//		// add img to the icon
//		buttons6[0] = new JCommandMenuButton(getResourceString("print"),
//				getResizableIconFromResource("images/toolbar/printer.gif"));
//		buttons6[1] = new JCommandMenuButton(getResourceString("printPreview"),
//				getResizableIconFromResource("images/toolbar/preview.gif"));
//
//		for (int i = 0; i < 2; i++) {
//			menu.addMenuButton(buttons6[i]);
//			buttons6[i].setEnabled(true);
//		}
//
//		JInternalFrame internalFrame = desktop.getSelectedFrame();
//		if (internalFrame == null
//				|| internalFrame instanceof RealtimeInternalFrame) {
//			for (int i = 0; i < 2; i++)
//				buttons6[i].setEnabled(false);
//			return menu;
//		}
//
//		if (internalFrame instanceof DisplayInternalFrame
//				|| internalFrame instanceof GraphInternalFrame) {
//			fileTemplate = desktop.getSelectedFileTemplate();
//			// 直接打印。不显示预览。
//			buttons6[0].addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					if (JOptionPane.showConfirmDialog(null, "确定直接打印？", "打印确认",
//							JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
//						new PrintData(fileTemplate, true);
//					}else{
//						
//					}
//				}
//			});
//
//			buttons6[1].addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					new PrintData(fileTemplate, false);
//				}
//			});
//
//		}
//
//		return menu;
//	}
//
//	/**
//	 * 与影片有关的操作
//	 * 
//	 * @author 林晓智
//	 * 
//	 */
//	class MoiveListener implements ActionListener {
//		JInternalFrame frame;
//
//		public void setFrame(JInternalFrame frame) {
//			this.frame = frame;
//		}
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//
//			String menuText = ((JCommandButton) (e.getSource())).getText();
//
//			MovieAction ma = null;
//
//			if (frame instanceof DisplayInternalFrame) {
//				DisplayInternalFrame jInFrame = (DisplayInternalFrame) frame;
//				ma = new MovieAction(jInFrame.getDisplayFileTemplate()
//						.getDisplayController());
//			} else if (frame instanceof GraphInternalFrame) {
//				GraphInternalFrame jInFrame = (GraphInternalFrame) frame;
//				ma = new MovieAction(jInFrame.getDisplayFileTemplate()
//						.getDisplayController());
//			} else {
//				return;
//			}
//			Pattern p = Pattern.compile("(.*)");
//			Matcher m = p.matcher(menuText);
//			String s = "";
//			if (m.matches())
//				s = m.group(1);
//			else
//				s = menuText;
//			ma.setMenuNam(s);
//			ma.actionPerformed(e);
//		}
//
//	}
//
//	class SendDCMActionListener implements ActionListener {
//		JInternalFrame frame;
//
//		public void setFrame(JInternalFrame frame) {
//			this.frame = frame;
//		}
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			DisplayFileTemplate dft = null;
//			if (frame instanceof DisplayInternalFrame)
//				dft = ((DisplayInternalFrame) frame).getDisplayFileTemplate();
//			if (frame instanceof GraphInternalFrame)
//				dft = ((GraphInternalFrame) frame).getDisplayFileTemplate();
//			// bad design
//			if (dft == null)
//				return;
//			DisplayInternalFrame dif = dft.d2DInFrame;
//
////			if (dif.getView().onSaveImage())
////				JOptionPane.showMessageDialog(dft.desktopPane.mainFrame,
////						getResourceString("sendSuccess"),
////						getResourceString("Teemo"),
////						JOptionPane.INFORMATION_MESSAGE);
////			else
////				JOptionPane.showMessageDialog(dft.desktopPane.mainFrame,
////						getResourceString("sendFail"),
////						getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//		}
//
//	}
//
//	class SaveMovieListener implements ActionListener {
//
//		JInternalFrame frame;
//
//		public void setFrame(JInternalFrame frame) {
//			this.frame = frame;
//		}
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			DisplayFileTemplate dft = null;
//			if (frame instanceof DisplayInternalFrame)
//				dft = ((DisplayInternalFrame) frame).getDisplayFileTemplate();
//			if (frame instanceof GraphInternalFrame)
//				dft = ((GraphInternalFrame) frame).getDisplayFileTemplate();
//			// bad design
//			if (dft == null)
//				return;
//
//			String filePath = FileOperation.saveFile(desktop.mainFrame,
//					dft.ddoc.getFileName());
//			if (filePath == null) {
//				// System.out.println("you didn't save file.");
//				// //以后改成提示消息 
//				return;
//			}
//			File srcFile = new File(dft.ddoc.getFilePath());
//			File destFile = new File(filePath);
//			try {
//				FileUtils.copyFile(srcFile, destFile);
//			} catch (IOException e2) {
//				e2.printStackTrace();
//				JOptionPane.showMessageDialog(desktop.mainFrame,
//						getResourceString("saveFail"),
//						getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//			JOptionPane
//					.showMessageDialog(desktop.mainFrame,
//							getResourceString("saveSuccess"),
//							getResourceString("Teemo"),
//							JOptionPane.INFORMATION_MESSAGE);
//		}
//
//	}

	private JCommandButton view2DButton;
	private JCommandButton view3DButton;
	private JCommandButton viewGraphButton;

	private JCommandButton playButton;
	private JCommandButton stopButton;
	private JCommandButton forwardButton;
	private JCommandButton backwardButton;
	private JCommandButton firstButton;
	private JCommandButton lastButton;
	private JCommandButton speedButton;

	private JCommandButton saveAsButton;
	private JCommandButton sendDCMButton;

	private JCommandButton printButton;
	private JCommandButton printPreviewButton;

	private JCommandButton closeButton;

	private JCommandMenuButton[] speedButtons;

	private ResizableIcon icon0;

	private ResizableIcon icon1;

	private JCommandPopupMenu speedMenu;

	class View2DListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dft.d2DInFrame.setVisible(true);
			dft.d2DInFrame.toFront();
			try {
				dft.d2DInFrame.setSelected(true);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		}

	}

	class View3DListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dft.d3DInFrame.setVisible(true);
			dft.d3DInFrame.toFront();
			try {
				dft.d3DInFrame.setSelected(true);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		}

	}

	class ViewGraphListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dft.graphInFrame.setVisible(true);
			dft.graphInFrame.toFront();
			try {
				dft.graphInFrame.setSelected(true);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		}

	}

	/**
	 * 与影片有关的操作
	 * 
	 * @author 林晓智
	 * 
	 */
	class MoiveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String menuText = ((JCommandButton) (e.getSource())).getText();
			MovieAction ma = new MovieAction(dft.getDisplayController());
			Pattern p = Pattern.compile("(.*)");
			Matcher m = p.matcher(menuText);
			String s = "";
			if (m.matches())
				s = m.group(1);
			else
				s = menuText;
			ma.setMenuNam(s);
			ma.actionPerformed(e);
		}

	}

	/**
	 * 与影片有关的操作
	 * 
	 * @author 林晓智
	 * 
	 */
	class SpeedMoiveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < 5; i++) {
				speedButtons[i].setIcon(icon0);
			}
			JCommandMenuButton jmb = ((JCommandMenuButton) (e.getSource()));
			jmb.setIcon(icon1);
			String menuText = jmb.getText();
			MovieAction ma = new MovieAction(dft.getDisplayController());
			Pattern p = Pattern.compile("(.*)");
			Matcher m = p.matcher(menuText);
			String s = "";
			if (m.matches())
				s = m.group(1);
			else
				s = menuText;
			ma.setMenuNam(s);
			ma.actionPerformed(e);
		}

	}

	class SendDCMListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (dft.d2dView.onSaveImage())
				JOptionPane.showMessageDialog(dft.getMainFrame(), getResourceString("sendSuccess"), getResourceString("Teemo"), JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(dft.getMainFrame(), getResourceString("sendFail"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
		}

	}

	class SaveAsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String filePath = FileOperation.saveFile(dft.getMainFrame(), dft.ddoc.getFileName());
			if (filePath == null)

				return;

			File srcFile = new File(dft.ddoc.getFilePath());
			File destFile = new File(filePath);
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (IOException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(dft.getMainFrame(), getResourceString("saveFail"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(dft.getMainFrame(), getResourceString("saveSuccess"), getResourceString("Teemo"), JOptionPane.INFORMATION_MESSAGE);

		}

	}

	class PrintListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (JOptionPane.showConfirmDialog(dft.getMainFrame(), "确定直接打印？", "打印确认", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			//	new PrintData(dft, true);
				dft.ddoc.printData();
			}
			
		}

	}

	class PrintPreviewListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new PrintData(dft, false);

		}

	}

	class CloseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//关闭的时候让事件监听也移掉 以免造成过多事件监听
			dft.getDisplayModel().removeChangeListener(DisplayRibbonBand.this);
			dft.closeDirectly();
		}

	}

	public DisplayRibbonBand() {
		super(getResourceString("movieMenuFile"), getResizableIconFromResource("images/view.png"));

		startGroup();
		view2DButton = new JCommandButton(getResourceString("2D"), getResizableIconFromResource("images/frame2D.png"));
		view2DButton.addActionListener(new View2DListener());
		addCommandButton(view2DButton, RibbonElementPriority.TOP);

		view3DButton = new JCommandButton(getResourceString("3D"), getResizableIconFromResource("images/frame3D.png"));
		view3DButton.addActionListener(new View3DListener());
		addCommandButton(view3DButton, RibbonElementPriority.TOP);

		viewGraphButton = new JCommandButton(getResourceString("graph"), getResizableIconFromResource("images/frameGraph.png"));
		viewGraphButton.addActionListener(new ViewGraphListner());
		addCommandButton(viewGraphButton, RibbonElementPriority.TOP);

		startGroup();
		playButton = new JCommandButton(getResourceString("playforward"), getResizableIconFromResource("images/toolbar/play.png"));
		firstButton = new JCommandButton(getResourceString("first"), getResizableIconFromResource("images/toolbar/previous.png"));
		forwardButton = new JCommandButton(getResourceString("forward"), getResizableIconFromResource("images/toolbar/backward.png"));
		stopButton = new JCommandButton(getResourceString("stop"), getResizableIconFromResource("images/toolbar/stop.png"));
		lastButton = new JCommandButton(getResourceString("last"), getResizableIconFromResource("images/toolbar/next.png"));
		backwardButton = new JCommandButton(getResourceString("backward"), getResizableIconFromResource("images/toolbar/forward.png"));

		MoiveListener ml = new MoiveListener();
		playButton.addActionListener(ml);
		firstButton.addActionListener(ml);
		forwardButton.addActionListener(ml);
		stopButton.addActionListener(ml);
		lastButton.addActionListener(ml);
		backwardButton.addActionListener(ml);

		addCommandButton(playButton, RibbonElementPriority.MEDIUM);
		addCommandButton(firstButton, RibbonElementPriority.MEDIUM);
		addCommandButton(forwardButton, RibbonElementPriority.MEDIUM);
		addCommandButton(stopButton, RibbonElementPriority.MEDIUM);
		addCommandButton(lastButton, RibbonElementPriority.MEDIUM);
		addCommandButton(backwardButton, RibbonElementPriority.MEDIUM);

		startGroup();
		speedButton = new JCommandButton(getResourceString("speed"), getResizableIconFromResource("images/speed.png"));
		speedButton.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
		addCommandButton(speedButton, RibbonElementPriority.TOP);
		constructMenu4SpeedButton();
		speedButton.setPopupCallback(new PopupPanelCallback() {

			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return speedMenu;
			}

		});

		startGroup();

		saveAsButton = new JCommandButton(getResourceString("saveAsMovie"), getResizableIconFromResource("images/toolbar/save_as.png"));
		sendDCMButton = new JCommandButton(getResourceString("sendDcm"), getResizableIconFromResource("images/toolbar/send.png"));
		addCommandButton(saveAsButton, RibbonElementPriority.TOP);
		addCommandButton(sendDCMButton, RibbonElementPriority.TOP);
		saveAsButton.addActionListener(new SaveAsListener());
		sendDCMButton.addActionListener(new SendDCMListener());

		startGroup();
		printPreviewButton = new JCommandButton(getResourceString("printPreview"), getResizableIconFromResource("images/toolbar/preview.png"));
		printButton = new JCommandButton(getResourceString("print"), getResizableIconFromResource("images/toolbar/printer.png"));
		addCommandButton(printPreviewButton, RibbonElementPriority.TOP);
		addCommandButton(printButton, RibbonElementPriority.TOP);
		printPreviewButton.addActionListener(new PrintPreviewListener());
		printButton.addActionListener(new PrintListener());

		startGroup();

		closeButton = new JCommandButton(getResourceString("close"), getResizableIconFromResource("images/toolbar/close.png"));
		addCommandButton(closeButton, RibbonElementPriority.TOP);
		closeButton.addActionListener(new CloseListener());

		view2DButton.setEnabled(false);
		view3DButton.setEnabled(false);
		viewGraphButton.setEnabled(false);
		playButton.setEnabled(false);
		stopButton.setEnabled(false);
		firstButton.setEnabled(false);
		lastButton.setEnabled(false);
		forwardButton.setEnabled(false);
		backwardButton.setEnabled(false);
		for (int i = 0; i < 5; i++) {
			speedButtons[i].setEnabled(false);
		}
		saveAsButton.setEnabled(false);
		sendDCMButton.setEnabled(false);
		printPreviewButton.setEnabled(false);
		printButton.setEnabled(false);
		closeButton.setEnabled(false);

		List<RibbonBandResizePolicy> result = new ArrayList<RibbonBandResizePolicy>();
		result.add(new CoreRibbonResizePolicies.Mirror(getControlPanel()));
//		result.add(new CoreRibbonResizePolicies.Mid2Low(getControlPanel()));
		result.add(new CoreRibbonResizePolicies.High2Mid(getControlPanel()));
		//result.add(new CoreRibbonResizePolicies.High2Low(getControlPanel()));
		result.add(new IconRibbonBandResizePolicy(getControlPanel()));

		setResizePolicies(result);

//		button1 = new JCommandButton(getResourceString("view"), getResizableIconFromResource("images/view.png"));
//		button2 = new JCommandButton(getResourceString("playforward"),getResizableIconFromResource("images/play.png"));
//		button3 = new JCommandButton(getResourceString("speed"),getResizableIconFromResource("images/speed.png"));
//		button4 = new JCommandButton(getResourceString("movieFrame") ,getResizableIconFromResource("images/frame.png"));
//		button5 = new JCommandButton(getResourceString("file") ,getResizableIconFromResource("images/document.png"));
//		button6 = new JCommandButton(getResourceString("print"),getResizableIconFromResource("images/printer.png"));
//
//		button1.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
//		button2.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
//		button3.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
//		button4.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
//		button5.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
//		button6.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
//		// icon =
//		// getResizableIconFromResource("images/48px-Crystal_Clear_action_bookmark.png");
//
//		 addCommandButton(button1, RibbonElementPriority.TOP);
//		 addCommandButton(button2, RibbonElementPriority.MEDIUM);
//		 addCommandButton(button3, RibbonElementPriority.MEDIUM);
//		 addCommandButton(button4, RibbonElementPriority.MEDIUM);
//		 addCommandButton(button5, RibbonElementPriority.MEDIUM);
//		 addCommandButton(button6, RibbonElementPriority.MEDIUM);
//
////		addRibbonComponent(new JRibbonComponent(button1));
////		addRibbonComponent(new JRibbonComponent(button2));
////		addRibbonComponent(new JRibbonComponent(button3));
////		addRibbonComponent(new JRibbonComponent(button4));
////		addRibbonComponent(new JRibbonComponent(button5));
////		addRibbonComponent(new JRibbonComponent(button6));
//
//		button1.setPopupCallback(new PopupPanelCallback() {
//
//			@Override
//			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
//				return createMenu4Button1();
//			}
//		});
//		button2.setPopupCallback(new PopupPanelCallback() {
//
//			@Override
//			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
//				return createMenu4Button2();
//			}
//		});
//		button3.setPopupCallback(new PopupPanelCallback() {
//
//			@Override
//			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
//				return createMenu4Button3();
//			}
//		});
//		button4.setPopupCallback(new PopupPanelCallback() {
//
//			@Override
//			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
//				return createMenu4Button4();
//			}
//		});
//		button5.setPopupCallback(new PopupPanelCallback() {
//
//			@Override
//			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
//				return createMenu4Button5();
//			}
//		});
//		button6.setPopupCallback(new PopupPanelCallback() {
//
//			@Override
//			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
//				return createMenu4Button6();
//			}
//		});
//
//		setResizePolicies((List) Arrays.asList(
//				new CoreRibbonResizePolicies.None(getControlPanel()),
//				new IconRibbonBandResizePolicy(getControlPanel())));

		// button.addActionListener(listner);
		// button.setEnabled(false);
	}

	private void constructMenu4SpeedButton() {
		speedMenu = new JCommandPopupMenu();
		speedButtons = new JCommandMenuButton[5];
		icon0 = getResizableIconFromResource("images/toolbar/star_3.png");
		icon1 = getResizableIconFromResource("images/toolbar/star.png");
		speedButtons[0] = new JCommandMenuButton(getResourceString("slowest"), icon0);
		speedButtons[1] = new JCommandMenuButton(getResourceString("mediumSlow"), icon0);
		speedButtons[2] = new JCommandMenuButton(getResourceString("nomal"), icon0);
		speedButtons[3] = new JCommandMenuButton(getResourceString("mediumFast"), icon0);
		speedButtons[4] = new JCommandMenuButton(getResourceString("fastest"), icon0);
		SpeedMoiveListener sml = new SpeedMoiveListener();
		for (int i = 0; i < 5; i++) {
			speedMenu.addMenuButton(speedButtons[i]);
			speedButtons[i].setEnabled(true);
			speedButtons[i].addActionListener(sml);
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		System.out.println("DisplayRibbonBand Info - " + evt);
		if (evt.getPropertyName().equals(MainTabbedPane.CURRENT_PANE)) {
			Object comp = evt.getNewValue();
			if (comp instanceof DisplayFileTemplate) {
				dft = (DisplayFileTemplate) comp;
				dft.getDisplayModel().addChangeListener(this);
				stateChange(dft.getDisplayModel());
			} else {
				view2DButton.setEnabled(false);
				view3DButton.setEnabled(false);
				viewGraphButton.setEnabled(false);
				playButton.setEnabled(false);
				stopButton.setEnabled(false);
				firstButton.setEnabled(false);
				lastButton.setEnabled(false);
				forwardButton.setEnabled(false);
				backwardButton.setEnabled(false);
				for (int i = 0; i < 5; i++) {
					speedButtons[i].setEnabled(false);
				}
				saveAsButton.setEnabled(false);
				sendDCMButton.setEnabled(false);
				printPreviewButton.setEnabled(false);
				printButton.setEnabled(false);
				closeButton.setEnabled(false);

			}

		}
	}

	private void stateChange(DisplayModel dm) {
		view2DButton.setEnabled(true);
		view3DButton.setEnabled(true);
		viewGraphButton.setEnabled(true);
		saveAsButton.setEnabled(true);
		sendDCMButton.setEnabled(true);
		printPreviewButton.setEnabled(true);
		printButton.setEnabled(true);
		closeButton.setEnabled(true);
		if (dm.isStrange()) {
			playButton.setEnabled(false);
			stopButton.setEnabled(false);
			firstButton.setEnabled(false);
			lastButton.setEnabled(false);
			forwardButton.setEnabled(false);
			backwardButton.setEnabled(false);
			for (int i = 0; i < 5; i++) {
				speedButtons[i].setEnabled(false);
			}
			return;
		}
		playButton.setEnabled(true);
		stopButton.setEnabled(true);
		firstButton.setEnabled(true);
		lastButton.setEnabled(true);
		forwardButton.setEnabled(true);
		backwardButton.setEnabled(true);
		for (int i = 0; i < 5; i++) {
			speedButtons[i].setEnabled(true);
		}

		boolean isPlay = dm.isPlay();
		if (isPlay == true) // 不能播放
			playButton.setEnabled(false);
		if (isPlay == false) // 不能停止
			stopButton.setEnabled(false);

		int frameIndex = dm.getFrameIndex();
		int totalFrameNum = dm.getTotalFrameNum();
		if (frameIndex == 0) // 不能向前播放
			forwardButton.setEnabled(false);
		if (frameIndex >= totalFrameNum - 1) // 不能向后播放
			backwardButton.setEnabled(false);

		int selectedSpeed = dm.getSelectedSpeed();
		for (int i = 0; i < 5; i++) {
			if (i != selectedSpeed)
				speedButtons[i].setIcon(icon0);
			else
				speedButtons[i].setIcon(icon1);
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		//System.out.println("displayRibbonBand info " + e);
		DisplayModel dm = (DisplayModel) e.getSource();
		if (dft.getDisplayModel() == dm)
			stateChange(dm);
	}
}
