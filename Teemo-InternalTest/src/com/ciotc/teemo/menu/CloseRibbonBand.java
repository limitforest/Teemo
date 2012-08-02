package com.ciotc.teemo.menu;

import static com.ciotc.teemo.resource.MyResources.getResizableIconFromResource;
import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.file.template.RealtimeFileTemplate;
import com.ciotc.teemo.frame.MainTabbedPane;

public class CloseRibbonBand extends JRibbonBand implements PropertyChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JCommandButton closeButton;
	private JCommandButton closeAllButton;
	private CloseListener closeListener;
	private CloseAllListener closeAllListener;

	MainTabbedPane tabs;
	
	public CloseRibbonBand() {
		super(getResourceString("close")+getResourceString("file"), getResizableIconFromResource("images/toolbar/close.png"));

		//button1 = new JCommandButton(getResourceString("openMovie"), getResizableIconFromResource("images/48px-Crystal_Clear_app_kthememgr.png"));
		closeButton = new JCommandButton(getResourceString("close"), getResizableIconFromResource("images/toolbar/close.png"));
		closeAllButton = new JCommandButton(getResourceString("closeAll"), getResizableIconFromResource("images/toolbar/closeAll.png"));
		//button4 = new JCommandButton(getResourceString("saveMovie"), getResizableIconFromResource("images/48px-Crystal_Clear_app_error.png"));

		//addCommandButton(button1, RibbonElementPriority.TOP);
		addCommandButton(closeButton, RibbonElementPriority.TOP);
		addCommandButton(closeAllButton, RibbonElementPriority.MEDIUM);
		//addCommandButton(button4, RibbonElementPriority.MEDIUM);

		setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()), new IconRibbonBandResizePolicy(getControlPanel())));

		closeButton.setEnabled(false);
		closeAllButton.setEnabled(false);
		
		closeListener = new CloseListener();
		closeButton.addActionListener(closeListener);
		closeAllListener = new CloseAllListener();
		closeAllButton.addActionListener(closeAllListener);

	}

//	class MyActionListener1 implements ActionListener {
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			String filePath = FileOperation.chooseFile(mainFrame);
//			if (filePath == null)
//				return;
//			// if(!dfTemplateHashMap.containsKey(filePath)){
//			if (!mainFrame.desktopPane.containsKey(filePath)) {
//				DisplayFileTemplate dfTemplate = new DisplayFileTemplate(mainFrame.desktopPane, filePath);
//				if (dfTemplate.isCreateSuccess)
//					mainFrame.desktopPane.addFileTemplate(dfTemplate);
//				else
//					JOptionPane.showMessageDialog(mainFrame, getResourceString("OpenPatientsFrame.openFileException"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//			}
//		}
//
//	}

	class CloseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			tabs.close();
		}

	}

	class CloseAllListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			tabs.closeAll();
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		System.out.println("CloseRibbonBand Info - "+evt);
		if(evt.getPropertyName().equals(MainTabbedPane.CURRENT_PANE)){
			Object  currentPane = evt.getNewValue();
			if(currentPane instanceof DisplayFileTemplate || currentPane instanceof RealtimeFileTemplate)
				closeButton.setEnabled(true);
			else 
				closeButton.setEnabled(false);
		}else if(evt.getPropertyName().equals(MainTabbedPane.IS_EXIST_PANE)){
			boolean bool = (Boolean) evt.getNewValue();
			if(bool)
				closeAllButton.setEnabled(true);
			else 
				closeAllButton.setEnabled(false);
		}
	}

	public void setTabs(MainTabbedPane tabs) {
		this.tabs = tabs;
	}

}
