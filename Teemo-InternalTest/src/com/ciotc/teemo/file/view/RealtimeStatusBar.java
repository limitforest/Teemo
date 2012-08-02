package com.ciotc.teemo.file.view;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import com.ciotc.teemo.file.template.RealtimeFileTemplate.Status;
import com.ciotc.teemo.frame.MainTabbedPane;
import com.ciotc.teemo.usbdll.USBDLL;

public class RealtimeStatusBar extends StatusBar implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String firstID = "record_zone";
	//String secondID = "tips_zone";
	String remainID = "speed_zone";
	JLabel recordLabel = new JLabel();
	JLabel speedLabel = new JLabel();
	private int counter;
	private int period;
	private Status status;
	public RealtimeStatusBar() {
		//setLayout(createHorizontalPercentLayout(0));
		setFont(new Font(null, Font.PLAIN, 14));
		setZoneBorder(BorderFactory.createLineBorder(Color.GRAY));

		setZones(new String[] { firstID/*, secondID*/, remainID }, new Component[] { recordLabel/*, tipsLabel*/, speedLabel }, new String[] { "60%", "*" });
		recordLabel.setText(getResourceString("realtimeStatusBar13"));
	}

	/**
	 * 在状态栏上显示录制的周期
	 * @param period
	 */
	public void setPeriod(int period){
		this.period = period;
		speedLabel.setText(MessageFormat.format(getResourceString("realtimeStatusBar2"),period));
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MainTabbedPane.RECORD_STSTUS)) {
			status = (Status) evt.getNewValue();
			switch (status) {
			case START:
				recordLabel.setText(getResourceString("realtimeStatusBar13"));
				break;
			case RECORDING:
				recordLabel.setText(MessageFormat.format(getResourceString("realtimeStatusBar11"), counter));
				break;
			case PAUSE:
				recordLabel.setText(getResourceString("realtimeStatusBar14"));
				break;
			case STOP:
				recordLabel.setText(MessageFormat.format(getResourceString("realtimeStatusBar12"), counter));
				break;
			case SAVED:
				recordLabel.setText(MessageFormat.format(getResourceString("realtimeStatusBar12"), counter));
				break;
			case END:
				recordLabel.setText(MessageFormat.format(getResourceString("realtimeStatusBar12"), counter));
				break;
			default:
				break;
			}
		}
	}


	public void setCounter(int counter) {
		this.counter = counter;
		if(status == Status.RECORDING)
			recordLabel.setText(MessageFormat.format(getResourceString("realtimeStatusBar11"), counter));
	}
	
	public void setPowaGain(int powa,int gain){
		speedLabel.setText(MessageFormat.format(getResourceString("realtimeStatusBar21"),period,powa,USBDLL.GAINS[gain]));		
	}
}