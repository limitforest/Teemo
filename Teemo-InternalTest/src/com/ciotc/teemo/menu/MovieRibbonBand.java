package com.ciotc.teemo.menu;

import static com.ciotc.teemo.resource.MyResources.getResizableIconFromResource;
import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.frame.MainTabbedPane;
import com.ciotc.teemo.usbdll.USBDLL;
import com.ciotc.teemo.util.FileOperation;

/**
 * 新建影片或者打开影片
 * @author Linxiaozhi
 *
 */
public class MovieRibbonBand extends JRibbonBand implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JCommandButton newMovieButton;
	private JCommandButton openMovieButton;

	private boolean bool1; //for isExistRealtimeFile
	private boolean bool2; //for isOpenHandle

	MainTabbedPane tabs;
	
	//private JCommandMenuButton[] buttons1;

	//private ResizableIcon icon;

//	private MDIDesktopPane desktop;
//	
//	public void setDesktop(MDIDesktopPane desktop) {
//		this.desktop = desktop;
//	}

//	private JCommandPopupMenu createMenu4Button1(){
//		JCommandPopupMenu menu = new JCommandPopupMenu();
//		buttons1 = new JCommandMenuButton[3];
//		buttons1[0] = new JCommandMenuButton(getResourceString("newRecord"), getResizableIconFromResource("images/toolbar/newMovie.png"));
//		buttons1[1] = new JCommandMenuButton(getResourceString("record"), getResizableIconFromResource("images/toolbar/play.png"));
//		buttons1[2] = new JCommandMenuButton(getResourceString("stop"), getResizableIconFromResource("images/toolbar/stop.png"));
//		
//		for(int i = 0;i<3;i++)
//		menu.addMenuButton(buttons1[i]);
//		
//		MyActionListener1 listener1 = new MyActionListener1();
//		MyActionListener2 listener2 = new MyActionListener2();
//		MyActionListener3 listener3 = new MyActionListener3();
//		
//		buttons1[0].addActionListener(listener1);
//		buttons1[1].addActionListener(listener2);
//		buttons1[2].addActionListener(listener3);
//		
//		if (!desktop.isExistRealtimeFileTemplate() && MainFrame.isOpenHandle)
//			buttons1[0].setEnabled(true);
//		else
//			buttons1[0].setEnabled(false);
//		
//		JInternalFrame internalFrame = desktop.getSelectedFrame();
//		if (internalFrame instanceof RealtimeInternalFrame) {
//			
//			RealtimeInternalFrame inFrame = (RealtimeInternalFrame) internalFrame;
//			boolean isRecord = inFrame.getView().getIsAtomRecord();
//			boolean isComplete = inFrame.getView().isComplete();
//
//			if (isRecord) {
//				buttons1[2].setEnabled(true);
//				listener3.setFrame(inFrame);
//			}else{
//				buttons1[2].setEnabled(false);
//			}
//
//			if (isRecord)
//				buttons1[1].setEnabled(false);
//			else {
//				if (isComplete)
//					buttons1[1].setEnabled(false);
//				else{
//					buttons1[1].setEnabled(true);
//					listener2.setFrame(inFrame);
//				}
//			}
//
//			if (!MainFrame.isOpenHandle){
//				buttons1[1].setEnabled(false);
//				buttons1[2].setEnabled(false);
//			}
//			
//			
//			
//		}else{
//			buttons1[1].setEnabled(false);
//			buttons1[2].setEnabled(false);
//		}
//		
//		return menu;
//	}

	class MyActionListener1 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (MainFrame.lock) {
				USBDLL.setButton1On();
			}
		}

	}

	

	class OpenMovieListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String filePath = FileOperation.chooseFile(tabs.getMainFrame());
			if (filePath == null)
				return;
			if(!tabs.openDisplayFileTemplate(filePath)){
				JOptionPane.showMessageDialog(tabs, getResourceString("OpenPatientsFrame.openFileException"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);	
			}
				
//			// if(!dfTemplateHashMap.containsKey(filePath)){
//			if (!desktop.containsKey(filePath)) {
				//DisplayFileTemplate dfTemplate = new DisplayFileTemplate(desktop, filePath);
//				if (dfTemplate.isCreateSuccess)
//					desktop.addFileTemplate(dfTemplate);
//				else
//					JOptionPane.showMessageDialog(desktop.mainFrame, getResourceString("OpenPatientsFrame.openFileException"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//			}
			
			
		}

	}

	public MovieRibbonBand() {
		super(getResourceString("newRecord"), getResizableIconFromResource("images/toolbar/newMovie.png"));

		newMovieButton = new JCommandButton(getResourceString("newMovie"), getResizableIconFromResource("images/toolbar/newMovie.png"));
		openMovieButton = new JCommandButton(getResourceString("openMovie"), getResizableIconFromResource("images/toolbar/openMovie.png"));

		newMovieButton.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
		openMovieButton.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
		//icon = getResizableIconFromResource("images/48px-Crystal_Clear_action_bookmark.png");

		addCommandButton(newMovieButton, RibbonElementPriority.TOP);
		addCommandButton(openMovieButton, RibbonElementPriority.MEDIUM);

//		if (!MainFrame.isOpenHandle) {
//			button1.setEnabled(false);
//		} else {
		newMovieButton.setEnabled(false);
		newMovieButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("set button1 on");
				synchronized (MainFrame.lock) {
					USBDLL.setButton1On();
				}
			}
		});
//		}

		openMovieButton.addActionListener(new OpenMovieListener());

		setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()), new IconRibbonBandResizePolicy(getControlPanel())));

		//button.addActionListener(listner);
		//button.setEnabled(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		System.out.println("movieRibbonBand Info - " + evt);
		if (evt.getPropertyName().equals(MainTabbedPane.IS_EXIST_REALTIME_FILE)) {
			bool1 = (Boolean) evt.getNewValue();
			if (!bool1 && bool2)
				newMovieButton.setEnabled(true);
			else
				newMovieButton.setEnabled(false);
		} else if (evt.getPropertyName().equals(MainFrame.IS_OPEN_HANDLE)) {
			bool2 = (Boolean) evt.getNewValue();
			if (!bool1 && bool2)
				newMovieButton.setEnabled(true);
			else
				newMovieButton.setEnabled(false);
		}
	}

	public void setTabs(MainTabbedPane tabs) {
		this.tabs = tabs;
	}

}
