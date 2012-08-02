package com.ciotc.teemo.frame;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.file.template.RealtimeFileTemplate;
import com.ciotc.teemo.menu.LanguagePanel;
import com.ciotc.teemo.menu.UserPreferencePanel;

public class MainTabbedPane extends JTabbedPane implements ChangeListener, PropertyChangeListener {

	public static final String RECORD_STSTUS = "recordStstus";

	public static final String IS_EXIST_PANE = "isExistPane";

	public static final String IS_EXIST_REALTIME_FILE = "isExistRealtimeFile";

	public static final String CURRENT_PANE = "currentPane";

	public static final String CLOSE_DISPLAY_FILE_TEMPLATE = "closeDisplayFileTemplate";

	public static final String CLOSE_REALTIME_FILE_TEMPLATE = "closeRealtimeFileTemplate";

	public static final String RESTART = "restart";
	/**
	 * tab栏的图标
	 */
	public static final Icon TITLE_ICON = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/view_s.png"));
	public static final Icon CLOSE_ICON = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/close_s.png"));
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int realtimeFileCounter = 1;

	boolean isExistRealtimeFile = false;
	boolean isExistPane = false;

	private Component currentPane = null;

	private MainFrame mainFrame = null;

	public MainTabbedPane() {
		super();
		addChangeListener(this);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		System.out.println("mainTabbedPane Info - " + evt);
		if (evt.getPropertyName().equals(CLOSE_REALTIME_FILE_TEMPLATE)) {
			RealtimeFileTemplate rft = (RealtimeFileTemplate) evt.getNewValue();
			closeRealtimeFileTemplate(rft);
		} else if (evt.getPropertyName().equals(CLOSE_DISPLAY_FILE_TEMPLATE)) {
			DisplayFileTemplate dft = (DisplayFileTemplate) evt.getNewValue();
			closeDisplayFileTemplate(dft);
		} else if (evt.getPropertyName().equals(RECORD_STSTUS)) {
			firePropertyChange(RECORD_STSTUS, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(LengendPanel.LOWER)) {
			refreshAll(evt);
		} else if (evt.getPropertyName().equals(UserPreferencePanel.MAX_OPTION)) {
			refreshAll(evt);
		} else if (evt.getPropertyName().equals(UserPreferencePanel.FRAME_OR_PERIOD) || evt.getPropertyName().equals(UserPreferencePanel.THRESHOLD_CONSTANT) || evt.getPropertyName().equals(UserPreferencePanel.TOP_CONSTANT) || evt.getPropertyName().equals(UserPreferencePanel.LEFT_CONSTANT)) {
			refreshAll(evt);
		} else if (evt.getPropertyName().equals(LanguagePanel.RESTART)) {
			firePropertyChange(RESTART, evt.getOldValue(), evt.getNewValue());
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		//tab切换
		//System.out.println(e);
		Component comp = getSelectedComponent();
		firePropertyChange(CURRENT_PANE, currentPane, comp);
		currentPane = comp;
	}

	/**
	 * 关闭当前窗口
	 */
	public void close() {
		Component comp = getSelectedComponent();

		if (comp != null) {
			//JDesktopPane pane = (JDesktopPane) comp;

			if (comp instanceof DisplayFileTemplate) {
				DisplayFileTemplate dft = (DisplayFileTemplate) comp;
				dft.closeDirectly();
			} else if (comp instanceof RealtimeFileTemplate) {
				RealtimeFileTemplate rft = (RealtimeFileTemplate) comp;
				rft.closeDirectly();
			}

			remove(comp);

			if (comp instanceof RealtimeFileTemplate) {
				firePropertyChange(IS_EXIST_REALTIME_FILE, isExistRealtimeFile, false);
				isExistRealtimeFile = false;
			}

		}
		boolean bool = getTabCount() == 0 ? false : true;
		firePropertyChange(IS_EXIST_PANE, isExistPane, bool);
		isExistPane = bool;
	}

	/**
	 * 关闭全部窗口
	 */
	public void closeAll() {
		int count = getTabCount();
		for (int i = count - 1; i >= 0; i--) {
//			Component comp = getTabComponentAt(i);
			Component comp = getComponentAt(i);
			if (comp != null) {

				//JDesktopPane pane = (JDesktopPane) comp;
				if (comp instanceof DisplayFileTemplate) {
					DisplayFileTemplate dft = (DisplayFileTemplate) comp;
					dft.closeDirectly();
				} else if (comp instanceof RealtimeFileTemplate) {
					RealtimeFileTemplate rft = (RealtimeFileTemplate) comp;
					rft.closeDirectly();
				}

				if (comp instanceof RealtimeFileTemplate) {
					firePropertyChange(IS_EXIST_REALTIME_FILE, isExistRealtimeFile, false);
					isExistRealtimeFile = false;
				}
			}
		}
		removeAll();

		firePropertyChange(IS_EXIST_PANE, isExistPane, false);
		isExistPane = false;
	}

	/**
	 * 根据设置的参数 改变窗口
	 */
	public void refreshAll(PropertyChangeEvent evt) {
		int count = getTabCount();
		for (int i = count - 1; i >= 0; i--) {
			Component comp = getComponentAt(i);
			if (comp != null) {
				if (comp instanceof DisplayFileTemplate) {
					DisplayFileTemplate dft = (DisplayFileTemplate) comp;
					dft.refresh(evt);
				}
			}
		}
	}

	public void closeDisplayFileTemplate(DisplayFileTemplate dft) {
		int index = indexOfComponent(dft);
		if (index != -1) {
			removeTabAt(index);
		}

		boolean bool = getTabCount() == 0 ? false : true;
		firePropertyChange(IS_EXIST_PANE, isExistPane, bool);
		isExistPane = bool;
	}

	public void addDisplayFileTemplate(DisplayFileTemplate dft) {
		dft.addPropertyChangeListener(this);
		String title = getResourceString("view") + "-" + dft.ddoc.getFilePath();			
		//addTab(title,/* getResizableIconFromResource("images/view.png")*/TITLE_ICON, dft);
			
		addTab(title, dft);
		
		JPanel panel = createTabComponent(title,dft);
		setTabComponentAt(indexOfComponent(dft), panel);		
		setSelectedIndex(indexOfComponent(dft));
		firePropertyChange(IS_EXIST_PANE, isExistPane, true);
		isExistPane = true;
	}

	/**
	 * 打开一个影片文件
	 * @param path 该文件的路径 要用绝对路径
	 * @return 打开成功返回true 否则返回false
	 */
	public boolean openDisplayFileTemplate(String path) {
		String title = getResourceString("view") + "-" + path;
		int index = indexOfTab(title);
		if (index == -1) {//表明没有
			DisplayFileTemplate dft = new DisplayFileTemplate(path);
			dft.setMainFrame(mainFrame);
			if (dft.isCreateSuccess) {
				addDisplayFileTemplate(dft);
				return true;
			} else {
				//提示错误
				return false;
			}
		} else {
//			System.out.println("index:" + getSelectedIndex());
			setSelectedComponent(getComponentAt(index));

			return true;
		}
	}

	/**
	 * 关闭一个影片
	 * @param path 该文件的路径
	 * @return
	 */
	public void closeDisplayFileTemplate(String path) {
		String title = getResourceString("view") + "-" + path;
		int index = indexOfTab(title);
		if (index == -1) {//表明没有
		} else {
			Component comp = getComponentAt(index);
			if (comp != null) {
				if (comp instanceof DisplayFileTemplate) {
					DisplayFileTemplate dft = (DisplayFileTemplate) comp;
					dft.closeDirectly();
				}
			}
		}
		boolean bool = getTabCount() == 0 ? false : true;
		firePropertyChange(IS_EXIST_PANE, isExistPane, bool);
		isExistPane = bool;
	}

	public void addRealtimeFileTemplate(RealtimeFileTemplate rft) {
		rft.addPropertyChangeListener(this);
		rft.setMainFrame(mainFrame);
		String title = getResourceString("realtime") + (realtimeFileCounter++) + getResourceString("realtimeFrame.title") + rft.rdoc.getPatientName();
		addTab(title, rft);
		JPanel panel = createTabComponent(title,rft);
		setTabComponentAt(indexOfComponent(rft), panel);
		setSelectedIndex(indexOfComponent(rft));
		firePropertyChange(IS_EXIST_REALTIME_FILE, isExistRealtimeFile, true);
		isExistRealtimeFile = true;

		firePropertyChange(IS_EXIST_PANE, isExistPane, true);
		isExistPane = true;
	}

	public void closeRealtimeFileTemplate(RealtimeFileTemplate rft) {

		int index = indexOfComponent(rft);
		if (index != -1) {
			removeTabAt(index);
		}

		firePropertyChange(IS_EXIST_REALTIME_FILE, isExistRealtimeFile, false);
		isExistRealtimeFile = false;

		boolean bool = getTabCount() == 0 ? false : true;
		firePropertyChange(IS_EXIST_PANE, isExistPane, bool);
		isExistPane = bool;
	}

	private JPanel createTabComponent(String title,final DisplayFileTemplate dft){
		JPanel panel = new JPanel(new BorderLayout());
		JLabel imageLabel = new JLabel(TITLE_ICON);
		JLabel titleLabel = new JLabel(title);
		JLabel closeLabel = new JLabel(CLOSE_ICON);
		closeLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dft.closeDirectly();
			}
		});
		panel.add(imageLabel,BorderLayout.WEST);
		panel.add(titleLabel,BorderLayout.CENTER);
		panel.add(closeLabel,BorderLayout.EAST);
		return panel;
	}
	private JPanel createTabComponent(String title,final RealtimeFileTemplate dft){
		JPanel panel = new JPanel(new BorderLayout());
		JLabel imageLabel = new JLabel(TITLE_ICON);
		JLabel titleLabel = new JLabel(title);
		JLabel closeLabel = new JLabel(CLOSE_ICON);
		closeLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dft.closeDirectly();
			}
		});
		panel.add(imageLabel,BorderLayout.WEST);
		panel.add(titleLabel,BorderLayout.CENTER);
		panel.add(closeLabel,BorderLayout.EAST);
		return panel;
	}
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

}
