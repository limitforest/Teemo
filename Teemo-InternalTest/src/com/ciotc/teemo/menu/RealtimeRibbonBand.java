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
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

import com.ciotc.teemo.file.template.RealtimeFileTemplate;
import com.ciotc.teemo.file.template.RealtimeFileTemplate.Status;
import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.frame.MainTabbedPane;
import com.ciotc.teemo.usbdll.USBDLL;

/**
 * 录制一个影片的操作
 * @author Linxiaozhi
 *
 */
public class RealtimeRibbonBand extends JRibbonBand implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用来表示“录制”按钮当前的功能
	 * 0 表示录制
	 * 1 表示暂停
	 * 2表示继续录制
	 */
	int recordIndex = 0;

	private JCommandButton recordButton; // 录制、暂停、继续录制共用
	//private JCommandButton pauseButton;
	private JCommandButton stopButton;
	private JCommandButton saveButton;
	private JCommandButton closeButton;

	private JCommandButton gainButton; //增益设置

	RealtimeFileTemplate rft;

	public RealtimeRibbonBand() {
		super(getResourceString("newRecord"), getResizableIconFromResource("images/toolbar/newMovie.png"));

		recordButton = new JCommandButton(getResourceString("record"), getResizableIconFromResource("images/toolbar/play.png"));
		//pauseButton = new JCommandButton(getResourceString("pause"), getResizableIconFromResource("images/toolbar/pause.png"));
		stopButton = new JCommandButton(getResourceString("stop"), getResizableIconFromResource("images/toolbar/pause.png"));
		saveButton = new JCommandButton(getResourceString("save"), getResizableIconFromResource("images/toolbar/save_as.png"));
		closeButton = new JCommandButton(getResourceString("close"), getResizableIconFromResource("images/toolbar/close.png"));
		gainButton = new JCommandButton(getResourceString("gainInputtingDialog.title"), getResizableIconFromResource("images/toolbar/setting1.png"));

		recordButton.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
		//pauseButton.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
		stopButton.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
		saveButton.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
		closeButton.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
		gainButton.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
		//icon = getResizableIconFromResource("images/48px-Crystal_Clear_action_bookmark.png");
		
		startGroup();
		addCommandButton(recordButton, RibbonElementPriority.TOP);
		//addCommandButton(pauseButton, RibbonElementPriority.MEDIUM);
		addCommandButton(stopButton, RibbonElementPriority.MEDIUM);
		addCommandButton(saveButton, RibbonElementPriority.MEDIUM);
		addCommandButton(closeButton, RibbonElementPriority.MEDIUM);
		startGroup();
		addCommandButton(gainButton, RibbonElementPriority.MEDIUM);

		recordButton.setEnabled(false);
		//pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		saveButton.setEnabled(false);
		closeButton.setEnabled(false);
		gainButton.setEnabled(false);

		//if(MainFrame.isOpenHandle){

		recordButton.addActionListener(new RecordListener());
		//pauseButton.addActionListener(new PauseListener());
		stopButton.addActionListener(new StopListener());
		saveButton.addActionListener(new SaveListner());
		closeButton.addActionListener(new CloseListener());
		gainButton.addActionListener(new GainListener());

		//}

		setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()), new IconRibbonBandResizePolicy(getControlPanel())));

	}

	class RecordListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("record");
			if (recordIndex == 0)
				synchronized (MainFrame.lock) {
					USBDLL.setButton2On();
				}
			//rft.play();
			else if (recordIndex == 1)
				rft.pause();
			else if (recordIndex == 2)
				rft.restore();
		}

	}

	class StopListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			rft.stop();
		}

	}

	class SaveListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			rft.save();
		}

	}

	class CloseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			rft.closeDirectly();
		}

	}

	public class GainListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			rft.showGainInFrm();
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		System.out.println("RealtimeRibbonBand Info - "+evt);
		if (evt.getPropertyName().equals(MainTabbedPane.CURRENT_PANE)) {
			Object comp = evt.getNewValue();
			if (comp instanceof RealtimeFileTemplate) {
				rft = (RealtimeFileTemplate) comp;
				if (rft.status.get() == Status.START) {
					recordIndex = 0;
					recordButton.setText(getResourceString("record"));
					recordButton.setIcon(getResizableIconFromResource("images/toolbar/play.png"));
					recordButton.setEnabled(true);
					closeButton.setEnabled(true);
					gainButton.setEnabled(true);
				}
			}

		} else if (evt.getPropertyName().equals(MainTabbedPane.RECORD_STSTUS)) {
			Status status = (Status) evt.getNewValue();
			switch (status) {
			case START:
				recordIndex = 0;
				recordButton.setText(getResourceString("record"));
				recordButton.setIcon(getResizableIconFromResource("images/toolbar/play.png"));
				recordButton.setEnabled(true);
				stopButton.setEnabled(false);
				saveButton.setEnabled(false);
				closeButton.setEnabled(true);
				gainButton.setEnabled(true);
				break;
			case RECORDING:
				recordIndex = 1;
				recordButton.setText(getResourceString("pause"));
				recordButton.setIcon(getResizableIconFromResource("images/toolbar/stop.png"));
				recordButton.setEnabled(true);
				stopButton.setEnabled(true);
				saveButton.setEnabled(false);
				closeButton.setEnabled(true);
				gainButton.setEnabled(false);
				break;
			case PAUSE:
				recordIndex = 2;
				recordButton.setText(getResourceString("record"));
				recordButton.setIcon(getResizableIconFromResource("images/toolbar/play.png"));
				recordButton.setEnabled(true);
				stopButton.setEnabled(true);
				saveButton.setEnabled(false);
				closeButton.setEnabled(true);
				gainButton.setEnabled(false);
				break;
			case STOP:
				recordButton.setEnabled(false);
				stopButton.setEnabled(false);
				saveButton.setEnabled(true);
				closeButton.setEnabled(true);
				gainButton.setEnabled(false);
				break;
			case SAVED:
				recordButton.setEnabled(false);
				stopButton.setEnabled(false);
				saveButton.setEnabled(false);
				closeButton.setEnabled(true);
				gainButton.setEnabled(false);
				break;
			case END:
				recordButton.setText(getResourceString("record"));
				recordButton.setIcon(getResizableIconFromResource("images/toolbar/play.png"));
				recordButton.setEnabled(false);
				stopButton.setEnabled(false);
				saveButton.setEnabled(false);
				closeButton.setEnabled(false);
				gainButton.setEnabled(false);
				break;
			default:
				break;

//			case NEWRECORD:
//				recordButton.setEnabled(true);
//				stopButton.setEnabled(false);
//				saveButton.setEnabled(false);
//				closeButton.setEnabled(true);
//				break;
//			case RECORDING:
//				recordButton.setEnabled(false);
//				stopButton.setEnabled(true);
//				saveButton.setEnabled(false);
//				closeButton.setEnabled(true);
//				break;
//			case STOP:
//				recordButton.setEnabled(false);
//				stopButton.setEnabled(false);
//				saveButton.setEnabled(true);
//				closeButton.setEnabled(true);
//				break;
//			case SAVED:
//				recordButton.setEnabled(false);
//				stopButton.setEnabled(false);
//				saveButton.setEnabled(false);
//				closeButton.setEnabled(true);
//				break;
//			case CLOSERECORD:
//				recordButton.setEnabled(false);
//				stopButton.setEnabled(false);
//				saveButton.setEnabled(false);
//				closeButton.setEnabled(false);
//			default:
//				break;
			}
		}
	}
}
