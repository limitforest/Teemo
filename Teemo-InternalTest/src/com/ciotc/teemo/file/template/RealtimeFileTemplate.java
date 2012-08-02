package com.ciotc.teemo.file.template;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.Preferences;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.ciotc.teemo.file.doc.RealtimeDoc;
import com.ciotc.teemo.file.frame.GainSettingInternalFrame;
import com.ciotc.teemo.file.frame.RFInternalFrame;
import com.ciotc.teemo.file.frame.RealtimeInternalFrame;
import com.ciotc.teemo.file.frame.WeightDisplayInternalFrame;
import com.ciotc.teemo.file.frame.WeightInternalFrame;
import com.ciotc.teemo.file.view.Realtime2DView;
import com.ciotc.teemo.file.view.RealtimeStatusBar;
import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.frame.MainTabbedPane;
import com.ciotc.teemo.usbdll.USBDLL;

/**
 * 展示实时录制文件的模板类：包括框架，视图,文档，注：只有2D图.
 * @author 林晓智
 *
 */
public class RealtimeFileTemplate extends JDesktopPane implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Preferences pref = Preferences.userNodeForPackage(MainFrame.class);

	public RealtimeInternalFrame r2DInFrame = null;
	public Realtime2DView r2dView = null;
	public RealtimeStatusBar r2dStatusBar = null;
	public RealtimeDoc rdoc = null;

	MainFrame mainFrame = null;

	public GainSettingInternalFrame gainInFrame = null;
	
	/**
	 * 定时器
	 */
	Timer timer = null;

	/**
	 * 记录当前所在的状态
	 * @author LinXiaozhi
	 *
	 */
	public enum Status {
		/**
		 * 起始
		 */
		START,
		/**
		 * 正在录制
		 */
		RECORDING,
		/**
		 * 录制暂停
		 */
		PAUSE,
		/**
		 * 录制停止
		 */
		STOP,
		/**
		 * 已保存
		 */
		SAVED,
		/**
		 * 结束
		 */
		END

	}

	//public Status status = Status.START;// 默认状态
	public AtomicReference<Status> status = new AtomicReference<Status>(Status.START);

	WeightDisplayInternalFrame wdif;

	public RealtimeFileTemplate(/*MDIDesktopPane desktopPane,*/int patientID, String patientName) {
		//this.desktopPane = desktopPane;

		//获取数据
		rdoc = new RealtimeDoc(patientID, patientName);
		rdoc.loadPeferGain();
		//2D图
		build2D();
		setBackground(new Color(240, 240, 240));

		constructTimer();
	}

	public void build2D() {
		r2DInFrame = new RealtimeInternalFrame(getResourceString("realtime") + (MainTabbedPane.realtimeFileCounter) + getResourceString("realtimeFrame.title") + rdoc.getPatientName(), this);
		//desktopPane.mainFrame.addObserable4TitleLable(r2DInFrame);
		r2dView = new Realtime2DView(this);
		r2dView.addPropertyChangeListener(this);
		r2DInFrame.addDisplayView(r2dView);
		r2dStatusBar = new RealtimeStatusBar();
		addPropertyChangeListener(r2dStatusBar);
		r2DInFrame.addStatusBar(r2dStatusBar);

		add(r2DInFrame);

		r2DInFrame.setLocation(0, 0);
		r2DInFrame.setVisible(true);

		
		gainInFrame = new GainSettingInternalFrame();
		add(gainInFrame);
		gainInFrame.setLocation(600, 100);
		gainInFrame.setVisible(true);
		
		
		RFInternalFrame rfInFrame = new RFInternalFrame(this);
		add(rfInFrame);
		rfInFrame.setLocation(600, 0);
		
		WeightInternalFrame wif = new WeightInternalFrame(this);
		add(wif);
		wif.setLocation(600, 0);
		
		wdif = new WeightDisplayInternalFrame(this);
		add(wdif);
		wdif.setLocation(600, 500);
		
		//rif.setVisible(true);
		
	}

	private void constructTimer() {
		timer = new Timer();
		//构造任务
		ButtonCheckTask bct = new ButtonCheckTask();
		timer.schedule(bct, 0, ButtonCheckTask.PERIOD);
		DataCheckTask dct = new DataCheckTask();

		/**
		 * 得到录制的周期 至此之后都不再改变
		 * 在整个录制过程中 它不会被改变
		 */
		Preferences pref = Preferences.userNodeForPackage(MainFrame.class);
		int period = Math.round(pref.getFloat("recordPeriod", 0.3f) * 1000);
		int totalFrames = pref.getInt("recordFrame", 1000);
		r2dStatusBar.setPeriod(period);
		rdoc.setPeriod(period);
		dct.setTotalFrames(totalFrames);
		timer.schedule(dct, 0, period);
	}

	/**
	 * 检查黄色按键的状态
	 * @author Linxiaozhi
	 *
	 */
	class ButtonCheckTask extends TimerTask {

		public final static int PERIOD = 10; //ms
		boolean isPresson = false;
		{
			synchronized (MainFrame.lock) {
				USBDLL.clearButton2Info();
			}
		}

		@Override
		public void run() {
			int info = 0;

			synchronized (MainFrame.lock) {
				info = USBDLL.getButton2Info();
			}
			if (info == 0)
				return;
			
//			switch (status.get()) {
//			case START:
//				if(info==1)
//					play();
//				break;
//			case RECORDING:
//			case PAUSE:
//				if(info==2)
//					stop();
//				break;
//			default:
//				cancel();
//				break;
//			}
			
			
			
			if (info == 1) { //按键开
				if (!isPresson) {
					play();
				}
				isPresson = true;
			}
			if (info == 2) { //按键关
				if (isPresson) {
					stop();
				}
				isPresson = false;
			}

		}

	}

	/**
	 * 捕获手柄传送过来的数据
	 * 它每次都会调用{@link USBDLL#collectFrame() 采集一帧数据}
	 * @author Linxiaozhi
	 *
	 */
	class DataCheckTask extends TimerTask {
		/**
		 * 需要录制的总帧数
		 */
		public int totalFrames;


		/**
		 * 记录已经录制了多少帧
		 */
		int counter = 0;

		{
			/**
			 * 清楚第一帧 ,第一帧数据有干扰的数据
			 */
			synchronized (MainFrame.lock) {
				USBDLL.collectFrame();
			}
		}

		@Override
		public void run() {
			byte[] recvdata;
			//在开始录制之前设定好增溢值
			int powa;
			int gain;
			switch (status.get()) {
			case START:
				powa = pref.getInt("powa", 10);
				gain = pref.getInt("gain", 0);
				synchronized (MainFrame.lock) {
					USBDLL.setPowA(powa);
					USBDLL.setGain(gain);
					recvdata = USBDLL.collectFrame();
				}
				r2dView.setRecvdata(recvdata);
				wdif.displayWeight(recvdata,powa);
				break;
			case RECORDING:
				powa = rdoc.getPowa();
				gain = rdoc.getGain();
				synchronized (MainFrame.lock) {
					USBDLL.setPowA(powa);
					USBDLL.setGain(gain);
					recvdata = USBDLL.collectFrame();
				}
				r2dView.setRecvdata(recvdata);
				rdoc.recroding(recvdata);
				wdif.displayWeight(recvdata,powa);
				counter++;
				if (counter <= totalFrames)
					r2dStatusBar.setCounter(counter);
				if (counter >= totalFrames)
					stop();

				break;
			default:
				powa = rdoc.getPowa();
				gain = rdoc.getGain();
				synchronized (MainFrame.lock) {
					USBDLL.setPowA(powa);
					USBDLL.setGain(gain);
					recvdata = USBDLL.collectFrame();
				}
				r2dView.setRecvdata(recvdata);
				wdif.displayWeight(recvdata,powa);
				break;
			}
		}

		public void setTotalFrames(int totalFrames) {
			this.totalFrames = totalFrames;
		}


	}

	public void play() {
		/**
		 * 开始录制的时候 将powa和gain保存到doc中，至此之后录制过程中都按这个来进行
		 */
		int powa = pref.getInt("powa", 10);
		int gain = pref.getInt("gain", 0);
		rdoc.setPowa(powa);
		rdoc.setGain(gain);
		rdoc.startRecord();
		r2dStatusBar.setPowaGain(powa, gain);
		gainInFrame.setVisible(false);
		firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.RECORDING);
		//status = Status.RECORDING;
		status.set(Status.RECORDING);
	}

//	/**
//	 * 内部可能会改变状态：如果录制结束，将转到{@link Status#STOP 结束}
//	 */
//	private void record(byte[] data) {
//		rdoc.recroding(data);
//
//	}

	public void pause() {
		firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.PAUSE);
		//status = Status.PAUSE;
		status.set(Status.PAUSE);
	}

	public void restore() {
		firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.RECORDING);
		//status = Status.RECORDING;
		status.set(Status.RECORDING);
	}

	public void stop() {
		rdoc.stopRecord();
		firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.STOP);
		//status = Status.STOP;
		status.set(Status.STOP);
	}

	/**
	 * 保存<br>
	 * <b>注意：保存成功之后不能再录制了</b>
	 */
	public void save() {

		Object[] options = { getResourceString("save"), getResourceString("notSave") };
		int response = JOptionPane.showOptionDialog(r2DInFrame, getResourceString("isSaveMoive"), getResourceString("Teemo"), JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (response == 0) {
			if (rdoc.saveData2File() && rdoc.saveData2Database()) {
				String id = String.valueOf(rdoc.getPatientID());
				mainFrame.pntFrm.validateOpenFrm(id);
				JOptionPane.showMessageDialog(r2DInFrame, getResourceString("saveSuccess"), getResourceString("Teemo"), JOptionPane.PLAIN_MESSAGE);
				rdoc.putPeferGain();
				if (timer != null) {
					timer.cancel();
					timer.purge();
					timer = null;
				}
				firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.SAVED);
				//status = Status.SAVED;
				status.set(Status.SAVED);
			} else
				JOptionPane.showMessageDialog(r2DInFrame, getResourceString("saveFail"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
		}

	}

	public void close() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
		removeAll();
		firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.END);
		//status = Status.END;
		status.set(Status.END);
		firePropertyChange(MainTabbedPane.CLOSE_REALTIME_FILE_TEMPLATE, null, this);

	}

	/**
	 * 直接关闭 根据现在的状态调用停止 保存 和关闭
	 */
	public void closeDirectly() {
		switch (status.get()) {
		case START:
			close();
			break;
		case RECORDING:
			stop();
			save();
			close();
			break;
		case PAUSE:
			restore();
			stop();
			save();
			close();
			break;
		case STOP:
			save();
			close();
			break;
		case SAVED:
			close();
		default:
			break;
		}
	}

	public void showGainInFrm(){
		gainInFrame.setVisible(true);
	}
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MainTabbedPane.RECORD_STSTUS)) {
			firePropertyChange(MainTabbedPane.RECORD_STSTUS, evt.getOldValue(), evt.getNewValue());
		}
	}

}
