package com.ciotc.teemo.menu;

import static com.ciotc.teemo.resource.MyResources.getResizableIconFromResource;
import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import com.ciotc.teemo.frame.MainFrame;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.frame.NewPatientsDialog;

public class PatientRibbonBand extends JRibbonBand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JCommandButton button1;
	private JCommandButton button2;
	private JCommandButton button3;
	private MyActionListener1 listner1;
	private MyActionListener2 listner2;
	private MyActionListener3 listner3;

	private MainFrame mainFrame;

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public PatientRibbonBand() {
		super(getResourceString("patients"), getResizableIconFromResource("images/toolbar/patients.png"));

		button1 = new JCommandButton(getResourceString("patients"), getResizableIconFromResource("images/toolbar/patients.png"));
		button2 = new JCommandButton(getResourceString("newPatients"), getResizableIconFromResource("images/toolbar/newPatient.png"));
		button3 = new JCommandButton(getResourceString("openPatients"), getResizableIconFromResource("images/toolbar/openPatient.png"));
		//button4 = new JCommandButton(getResourceString("saveMovie"), getResizableIconFromResource("images/48px-Crystal_Clear_app_error.png"));

		addCommandButton(button1, RibbonElementPriority.TOP);
		addCommandButton(button2, RibbonElementPriority.MEDIUM);
		addCommandButton(button3, RibbonElementPriority.MEDIUM);
		//addCommandButton(button4, RibbonElementPriority.MEDIUM);

		setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()), new IconRibbonBandResizePolicy(getControlPanel())));

		listner1 = new MyActionListener1();
		button1.addActionListener(listner1);
		listner2 = new MyActionListener2();
		button2.addActionListener(listner2);
		listner3 = new MyActionListener3();
		button3.addActionListener(listner3);

	}

	class MyActionListener1 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!mainFrame.pntFrm.isVisible()) {
				mainFrame.pntFrm.setAlwaysOnTop(true);
				mainFrame.pntFrm.setVisible(true);
				mainFrame.pntFrm.setAlwaysOnTop(false);
			} else {
				if (mainFrame.pntFrm.getExtendedState() != JFrame.NORMAL) // JFrame.NORMAL
												// ==
												// 0
												// 判断是否最小化
					mainFrame.pntFrm.setExtendedState(JFrame.NORMAL);
				if (!mainFrame.pntFrm.isActive())
					mainFrame.pntFrm.setVisible(true);
			}
		}

	}

	class MyActionListener2 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			NewPatientsDialog newPntDialog = new NewPatientsDialog(mainFrame.pntFrm, getResourceString("NewPatientsDialog.title"));
			newPntDialog.setVisible(true);

		}

	}

	class MyActionListener3 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			mainFrame.pntFrm.newOpenFrm(String.valueOf(mainFrame.patientID));
		}

	}

}
