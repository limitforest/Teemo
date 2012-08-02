package com.ciotc.teemo.menu;

import static com.ciotc.teemo.resource.MyResources.getResizableIconFromResource;
import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JToolBar;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

import com.ciotc.teemo.frame.MainTabbedPane;

public class SettingRibbonBand extends JRibbonBand  {
	private JToolBar toolbar;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private JCommandButton lengendButton;
	private JCommandButton optionButton;
	
	private MainTabbedPane tabs;
	
	public SettingRibbonBand(){
		super(getResourceString("setting"), getResizableIconFromResource("images/toolbar/setting1.png"));
		
		lengendButton = new JCommandButton(getResourceString("lengendDialog.title"), getResizableIconFromResource("images/toolbar/color.png"));
		optionButton = new JCommandButton(getResourceString("setting"), getResizableIconFromResource("images/toolbar/setting1.png"));
		
		addCommandButton(optionButton, RibbonElementPriority.TOP);
		addCommandButton(lengendButton, RibbonElementPriority.TOP);
		
		setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()), new IconRibbonBandResizePolicy(getControlPanel())));
		
		lengendButton .addActionListener(new LengendActionListener());
		
		optionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final SettingDialog sd = new SettingDialog();
				//sd.setLocationRelativeTo()
				sd.addListener(tabs);
				sd.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						sd.removeListener(tabs);
					}
				});
				sd.setLocationRelativeTo(tabs);
				sd.setModal(true);
				sd.setVisible(true);
			}
		});
		
	}

	class LengendActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(toolbar.isVisible()){
				toolbar.setVisible(false);
			}else
				toolbar.setVisible(true);
		}
		
	}

	

	public void setToolbar(JToolBar toolbar) {
		this.toolbar = toolbar;
	}



	public void setTabs(MainTabbedPane tabs) {
		this.tabs = tabs;
	}
	
	
	

}
