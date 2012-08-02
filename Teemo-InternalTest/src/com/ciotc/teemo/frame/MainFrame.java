package com.ciotc.teemo.frame;

import static com.ciotc.teemo.resource.MyResources.getResizableIconFromResource;
import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.BaseRibbonBandResizeSequencingPolicy;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizeSequencingPolicies;
import org.pushingpixels.flamingo.internal.ui.ribbon.BasicRibbonUI;

import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.file.template.RealtimeFileTemplate;
import com.ciotc.teemo.menu.CloseRibbonBand;
import com.ciotc.teemo.menu.DisplayRibbonBand;
import com.ciotc.teemo.menu.MovieRibbonBand;
import com.ciotc.teemo.menu.PatientRibbonBand;
import com.ciotc.teemo.menu.RealtimeRibbonBand;
import com.ciotc.teemo.menu.SettingRibbonBand;
import com.ciotc.teemo.usbdll.USBDLL;
import com.ciotc.teemo.util.DelTemp;
import com.install4j.api.launcher.ApplicationLauncher;

public class MainFrame extends JRibbonFrame implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static {
		String path = System.getProperty("user.dir");
		// System.out.println(path);
		System.load(path + "\\lib\\usb_top.dll");
		System.load(path + "\\lib\\USB.dll");
	}

	public boolean isOpenHandle = false;
	//private static boolean isConnectSensor = false;
	boolean isPressOn = false;

	public Timer timerHandler = null; //检查手柄有无连接的
	public Timer timer = null;
	public static Object lock = new Object();
	public final static Object lock2 = new Object();

	public boolean isExistRealtimeFile = false;

	/**view**/
	public MainTabbedPane tabs;
	public JToolBar bar;
	StatusBar statusBar;

	// FileMenu obj.
	public PatientsFrame pntFrm = null;
	public int patientID = 0;
	public String patientName = null;

	LengendPanel lengend = null;

	public static final String IS_OPEN_HANDLE = "isOpenHandle";
	private JToolBar lengendToolbar;

//	/**
//	 * 打开手柄，并初始化
//	 */
//	public void openHandle() {
//		@SuppressWarnings("unused")
//		String serialNumber = "";
//		int deviceIndex = 0;
//		boolean isConnectHandle = false;
//		deviceIndex = USBDLL.getDevicesNum();
//
//		/** 虚拟代码 **/
//
//		if (deviceIndex == 0)
//			isConnectHandle = false;
//		else
//			isConnectHandle = true;
//
//		if (isConnectHandle == true)
//			serialNumber = USBDLL.getSerialNumberSingle(deviceIndex);
//		else {
//			isOpenHandle = false;
//			return;
//		}
//
//		if (USBDLL.open(deviceIndex - 1)) // 要减1
//			isOpenHandle = true;
//		else
//			isOpenHandle = false;
//
//		if (USBDLL.clearButton1Info()) {
//			isPressOn = false;
//		} else {
//			isOpenHandle = false; // 相当于没有打开手柄
//			return;
//		}
//
//	}

	/**
	 * 如果手柄打开了，则关闭手柄，否则返回.
	 */
	public void closeHandle() {
		if (timerHandler != null) {
			timerHandler.cancel();
			timerHandler.purge();
			timerHandler = null;
		}
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}

		if (isOpenHandle) {
			synchronized (MainFrame.lock) {
				USBDLL.clearButton1Info();
			}
			synchronized (MainFrame.lock) {
				USBDLL.clearButton2Info();
			}

			synchronized (MainFrame.lock) {
				USBDLL.close();
			}
		} else
			return;

		// System.out.println("close");
	}

	class CheckTask extends TimerTask {

		private RealtimeFileTemplate rft;

		@Override
		public void run() {
			int info = 0;
			synchronized (MainFrame.lock) {
				info = USBDLL.getButton1Info();
			}

			if (info == 0)
				return;
			if (info == 1) { // 按键1 开
				if (/*!desktopPane.isExistRealtimeFileTemplate() &&*/!isExistRealtimeFile && !isPressOn) {
					// 新建一个影像
					rft = new RealtimeFileTemplate(patientID, patientName);
					tabs.addRealtimeFileTemplate(rft);

				}
				//System.out.println("isPressOn: " + isPressOn + " in info 1");
				isPressOn = true;

			}

			if (info == 2) {
				if (/*desktopPane.isExistRealtimeFileTemplate() &&*/isExistRealtimeFile && isPressOn) {
					//tabs.closeRealtimeFileTemplate(rft);
					//System.out.println(rft);
					rft.closeDirectly();
				}
				//System.out.println("isPressOn: " + isPressOn + " in info 2");
				isPressOn = false;

			}
		}
	}

	class CheckHandle extends TimerTask {

		@Override
		public void run() {
			int deviceIndex = 0;
			synchronized (MainFrame.lock) {
				deviceIndex = USBDLL.getDevicesNum();
			}
			if (deviceIndex == 0) { //没有连接
				//statusBar.setText(getResourceString("noHandleTips"));
				if (isOpenHandle) //由连接变过来的
					destoryTimer();

				firePropertyChange(IS_OPEN_HANDLE, isOpenHandle, false);
				isOpenHandle = false;

				return;
			} else {//连接
				if (!isOpenHandle) { //由没连接变过来的
					boolean bool;
					synchronized (MainFrame.lock) {
						bool = USBDLL.open(deviceIndex - 1);
					}
					if (!bool) //打不开相当没有连接上
						return;

					reConstructTimer();
					firePropertyChange(IS_OPEN_HANDLE, isOpenHandle, true);
				}

//				synchronized (MainFrame.lock) {
//					bool = USBDLL.clearButton1Info();
//				}
//				if (!bool)
//					return;

				isOpenHandle = true;

				//statusBar.setText(getResourceString("alreadyHaveHandle"));
				//开始检测按钮的状态

			}

		}

	}

//	class CheckSensorTask extends TimerTask {
//
//		@Override
//		public void run() {
//			synchronized (MainFrame.lock) {
//				isConnectSensor = USBDLL.senseLink();
//				//System.out.println("sense link is " +isConnectSensor);
//			}
//		}
//	}

	// public static int flag = 1;
	/**
	 * a menubar
	 */
	//private JMenuBar menuBar = new JMenuBar();

	public MainFrame() {
		super(getResourceString("mainFrame.title")); //韶晖
		//setJMenuBar(menuBar);

		/*
		Container content = getContentPane();
		desktopPane = new MDIDesktopPane(this);// new MDIPane(this);
		content.add(desktopPane, BorderLayout.CENTER);
		desktopPane.setBackground(new Color(240, 240, 240));
		 */
		//createDesktopPane();

//		openHandle();
//		if (isOpenHandle) {
//			timer = new Timer();
//			timer.scheduleAtFixedRate(new CheckTask(), 0, 10); // 200ms
//
//			//第一次连接的时候也会检测传感器是否连接上
//			//isConnectSensor = USBDLL.senseLink();//以后改为定时查
//			//System.out.println("sense link is " +isConnectSensor);
//			//timer.scheduleAtFixedRate(new CheckSensorTask(), 0, 200); // 200ms
//		} else {
//			JLabel lblDialog = new JLabel(getResourceString("noHandleTips"));
//			lblDialog.setFont(Constant.font);
//			JOptionPane.showMessageDialog(this, lblDialog, getResourceString("mainFrame.title"), JOptionPane.WARNING_MESSAGE); //韶晖
//		}

		constructJTabbedPane();
		constructStatusBar();
		constructLengendToolbar();
		constructRibbonMenu();

		pntFrm = new PatientsFrame(getResourceString("patientsFrame.title"), this);
		patientName = pntFrm.getLstObj().getSelectedValue().toString();
		patientID = Integer.parseInt(((Patients) pntFrm.getLstObj().getSelectedValue()).getId());

		constructTimerHandler();

		addWindowListener(new MainWindowListener());
	}

	private void constructRibbonMenu() {
		/*-----------------------------menu----------------------------------------*/
		getRibbon().putClientProperty(BasicRibbonUI.IS_USING_TITLE_PANE, true);
		setApplicationIcon(getResizableIconFromResource("images/toolbar/teemo.png"));
		getRibbon().configureHelp(getResizableIconFromResource("images/toolbar/help.png"), new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("cmd.exe /c start help.chm");
				
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(MainFrame.this, getResourceString("cannotOpenDoc"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		RibbonTask task = null;
		BaseRibbonBandResizeSequencingPolicy policy = null;

		//fileBand2 = new FileRibbonBand2();
		PatientRibbonBand patientBand = new PatientRibbonBand();
		patientBand.setMainFrame(this);

		MovieRibbonBand movieBand = new MovieRibbonBand();
		movieBand.setTabs(tabs);
		addPropertyChangeListener(movieBand);
		tabs.addPropertyChangeListener(movieBand);

		CloseRibbonBand closeBand = new CloseRibbonBand();
		closeBand.setTabs(tabs);
		tabs.addPropertyChangeListener(closeBand);

		SettingRibbonBand settingBand = new SettingRibbonBand();
		settingBand.setToolbar(lengendToolbar);
		settingBand.setTabs(tabs);

		task = new RibbonTask(getResourceString("file"), patientBand, movieBand, closeBand, settingBand);
		policy = new CoreRibbonResizeSequencingPolicies.CollapseFromLast(task);
		task.setResizeSequencingPolicy(policy);
		getRibbon().addTask(task);

		RealtimeRibbonBand realtimeBand = new RealtimeRibbonBand();
		tabs.addPropertyChangeListener(realtimeBand);
		task = new RibbonTask(getResourceString("newRecord"), realtimeBand);
		policy = new CoreRibbonResizeSequencingPolicies.CollapseFromLast(task);
		task.setResizeSequencingPolicy(policy);
		getRibbon().addTask(task);

		DisplayRibbonBand displayBand = new DisplayRibbonBand();
		tabs.addPropertyChangeListener(displayBand);
		task = new RibbonTask(getResourceString("movieMenu"), displayBand);
		policy = new CoreRibbonResizeSequencingPolicies.CollapseFromLast(task);
		task.setResizeSequencingPolicy(policy);
		getRibbon().addTask(task);

//		OptionRibbonBand optionBand = new OptionRibbonBand();
//		optionBand.setMainFrame(this);
//		task = new RibbonTask(getResourceString("setting"), optionBand);
//		policy = new CoreRibbonResizeSequencingPolicies.CollapseFromLast(task);
//		task.setResizeSequencingPolicy(policy);
//		getRibbon().addTask(task);

		/*-----------------------------menu----------------------------------------*/
	}

	public void constructJTabbedPane() {
		tabs = new MainTabbedPane();
		tabs.addPropertyChangeListener(this);
		tabs.setMainFrame(this);
		Container content = getContentPane();
		content.add(tabs, BorderLayout.CENTER);
	}

//	public void createDesktopPane() {
//		Container content = getContentPane();
//		desktopPane = new MDIDesktopPane(this);// new MDIPane(this);
//		content.add(desktopPane, BorderLayout.CENTER);
//		desktopPane.setBackground(new Color(240, 240, 240));
//	}

	private void constructStatusBar() {
		statusBar = new StatusBar();
		addPropertyChangeListener(statusBar);
		Container content = getContentPane();
		content.add(statusBar, BorderLayout.SOUTH);

	}

	private void constructLengendToolbar() {
		lengendToolbar = new JToolBar(JToolBar.VERTICAL);
		//lengendToolbar.setLayout(new FlowLayout());
		lengendToolbar.setLayout(new BorderLayout());
		//lengendToolbar.setLayout(null);
		//lengendToolbar.setRollover(true);
		lengend = new LengendPanel(this);
		//lengendToolbar.add(lengend);
		
		lengendToolbar.add(lengend,BorderLayout.NORTH);
		
		lengend.addPropertyChangeListener(tabs);
		Container content = getContentPane();
		content.add(lengendToolbar, BorderLayout.WEST);

		//lengendToolbar.setVisible(false);
		//content.remove(lengendToolbar);
	}

	/**
	 * 构造定时器 用来检测有无连上手柄 周期是2000ms
	 */
	private void constructTimerHandler() {
		timerHandler = new Timer();
		timerHandler.schedule(new CheckHandle(), 0, 1000);
	}

	public void reConstructTimer() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
		timer = null;
		timer = new Timer();
		synchronized (MainFrame.lock) {
			USBDLL.clearButton1Info();
		}
		isPressOn = false;
		timer.scheduleAtFixedRate(new CheckTask(), 0, 10); // 200ms
		//timer.scheduleAtFixedRate(new CheckSensorTask(), 0, 200); // 200ms
	}

	public void destoryTimer() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
	}

	public void restart() {
		//System.exit(0);
		actionBeforeClose();

		dispose();

		//System.exit(0);
		
		try {
			//Runtime.getRuntime().exec("cmd.exe /c start teemo.exe");
			ProcessBuilder pb = new ProcessBuilder("StartTeemo.exe");
			Process p = pb.start();
			System.exit(0);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, getResourceString("cannotRestart"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}

	public void actionBeforeClose(){
		DelTemp.delTemp();
		closeHandle();
		tabs.closeAll();
	}
	
	class MainWindowListener extends WindowAdapter {
		
		
		public void windowOpened(WindowEvent e) {
			//pntFrm.setVisible(true);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							com.ciotc.teemo.update.Check4Update.updateBeforeCheck(myLauncherCallback,MainFrame.this);
							
						}
					});
				}
			}).start();
			
			super.windowOpened(e);
		}

		public void windowClosing(WindowEvent e) {
			actionBeforeClose();
//			for (JInternalFrame jif : desktopPane.getAllFrames()) {
//				jif.doDefaultCloseAction();
//			}
			super.windowClosing(e);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		System.out.println("mainFrame Info - " + evt);
		if (evt.getPropertyName().equals(MainTabbedPane.IS_EXIST_REALTIME_FILE)) {
			isExistRealtimeFile = (Boolean) evt.getNewValue();
			if (isExistRealtimeFile == false) {
				reConstructTimer();
				//让它会第一个工具栏
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						getRibbon().setSelectedTask(getRibbon().getTask(0));
					}
				});
			}
			firePropertyChange(MainTabbedPane.IS_EXIST_REALTIME_FILE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(MainTabbedPane.CURRENT_PANE)) {
			Object comp = evt.getNewValue();
			if (comp instanceof RealtimeFileTemplate) {
				//让它到第二个工具栏 即有录制按钮的工具栏

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						getRibbon().setSelectedTask(getRibbon().getTask(1));
					}
				});
			} else if (comp instanceof DisplayFileTemplate) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						getRibbon().setSelectedTask(getRibbon().getTask(2));
					}
				});

			}
		} else if (evt.getPropertyName().equals(MainTabbedPane.RESTART)) {
			restart();
		}
	}

	ApplicationLauncher.Callback myLauncherCallback = new ApplicationLauncher.Callback(){

		@Override
		public void exited(int arg0) {
			
		}

		@Override
		public void prepareShutdown() {
			actionBeforeClose();
		}
		
	};
	
	class StatusBar extends SimpleStatusBar implements PropertyChangeListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		JLabel label;

		public StatusBar() {
			label = new JLabel();
			//默认假设没有连接上
			label.setText(getResourceString("noHandleTips"));
			add(label);
			label.setHorizontalTextPosition(SwingConstants.LEFT);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
//			System.out.println("statusBar Info - " + evt);
			if (evt.getPropertyName().equals(IS_OPEN_HANDLE)) {
				boolean bool = (Boolean) evt.getNewValue();
				if (bool) {
					label.setText(getResourceString("alreadyHaveHandle"));
				} else {
					label.setText(getResourceString("noHandleTips"));
				}
			}
		}

	}

}
