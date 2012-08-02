package com.ciotc.teemo.file.model;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.event.ChangeEvent;

import com.ciotc.teemo.file.doc.DisplayDoc;

public class DisplayModel extends AbstractModel implements Observer {

	class PaintTask extends TimerTask {

		@Override
		public void run() {
			try {
				if (frameIndex >= totalFrameNum) {
					stop();
					frameIndex = totalFrameNum - 1;
					setaFrameByFrameIndex(frameIndex);
					return;
				}
				synchronized (DisplayModel.this) {
					while (isPlay == false)
						DisplayModel.this.wait();
				}
				setaFrameByFrameIndex(frameIndex);
				//setaFrame(displayDoc.getSelectedFrame(frameIndex));
				frameIndex++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private final static int colNum = 52;
	private final static int rowNum = 44;
	private final static int totalNum = 2288;

	private int[] aFrame = new int[totalNum];
	private int contourOr2D = 1; // 1 代表 2D图 ，2 代表2D 轮廓图
	private DisplayDoc displayDoc;
	private int force;
	private int frameIndex;
	private boolean isPlay;
	private boolean isStop;
	private boolean isStrange;
	private boolean isShowArch;
	private boolean isShowForceDist;
	private int mid;
	private double[][] percentData;
	private double[] percentForce;//= new double[3];
	private int period = 1000;
	private float recordPeriod;
	private int selectedSpeed = 2;
	private int showCOF;
	Timer timer = new Timer();
	private int totalFrameNum = 0;
	private int towho = 0; // 让谁响应 0代表DisplayView 1代表GraphView
	private int whichInStrange = 0;
	private int x;
	private int y;
	private int[] powaGain;
	private boolean isDirty;

	public DisplayModel(DisplayDoc doc) {
		super();
		this.displayDoc = doc;
		totalFrameNum = displayDoc.getFrameLen();
		recordPeriod = displayDoc.getPeriod();

		//增加了字段powaGain  @date 2012.4.16
		if (!doc.isOldVersion()) {
			powaGain = new int[2];
			powaGain[0] = doc.getPowa();
			powaGain[1] = doc.getGain();
		}

	}

	/**
	 * 向后一帧
	 */
	public void backward() {
		frameIndex++;
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);
	}

	public void calculatePercentData(int mid) {
		this.mid = mid;
		double[][] d = displayDoc.getPercentPerFrame(mid);

		setPercentData(d);
		if (frameIndex < totalFrameNum)
			setaFrameByFrameIndex(frameIndex);
	}

	public void calculatePercentData() {
		double[][] d = displayDoc.getPercentPerFrame(mid);

		setPercentData(d);
		if (frameIndex < totalFrameNum)
			setaFrameByFrameIndex(frameIndex);
	}

	public void calculatePercentDataStrange() {
		double[][] d = displayDoc.getPercentPerFrame(mid);

		setPercentData(d);

	}

//	public void setaFrame(byte[] aFrame) {
//		this.aFrame = aFrame;
//		setPercentForce(new double[]{percentData[0][frameIndex],percentData[1][frameIndex],percentData[2][frameIndex]});
////		setTowho(0);
////		fireChangeEvent(new ChangeEvent(this));
//	}

//	public void setPercentForce(double[] percentForce) {
//		this.percentForce = percentForce;
//		setTowho(0);
//		fireChangeEvent(new ChangeEvent(this));
//	}

	/**
	 * 超快
	 */
	public void fastest() {
		selectedSpeed = 4;
		period = 200;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 第一帧
	 */
	public void first() {
		frameIndex = 0;
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);
		calculatePercentData(mid);
	}

	/**
	 * 向前一帧
	 */
	public void forward() {
		frameIndex--;
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);
	}

	public int[] getaFrame() {
		return aFrame;
	}

	public int getContourOr2D() {
		return contourOr2D;
	}

	public int getForce() {
		return force;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public double[][] getPercentData() {
		return percentData;
	}

//	public double[] getPercentForce() {
//		return percentForce;
//	}

	public float getRecordPeriod() {
		return recordPeriod;
	}

	public int getSelectedSpeed() {
		return selectedSpeed;
	}

	public int getShowCOF() {
		return showCOF;
	}

	public int getTotalFrameNum() {
		return totalFrameNum;
	}

	public int getTowho() {
		return towho;
	}

	public int getWhichInStrange() {
		return whichInStrange;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void initDefault() {

		calculatePercentData(colNum / 2);
		selectfirstFrame();
	}

	public boolean isPlay() {
		return isPlay;
	}

	public boolean isShowArch() {
		return isShowArch;
	}

	public boolean isStop() {
		return isStop;
	}

	public boolean isStrange() {
		return isStrange;
	}

	/**
	 * 最后一帧
	 */
	public void last() {
		frameIndex = totalFrameNum - 1;
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);
	}

	/**
	 * 较快
	 */
	public void mediumFast() {
		selectedSpeed = 3;
		period = 500;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 慢
	 */
	public void mediumSlow() {
		selectedSpeed = 1;
		period = 2000;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 标准
	 */
	public void nomal() {
		selectedSpeed = 2;
		period = 1000;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 播放
	 */
	public void play() {
		isPlay = true;
		timer.scheduleAtFixedRate(new PaintTask(), 0, period);
	}

	public void select2DView() {
		contourOr2D = 1;
		selectNowFrame();
	}

	public void selectArchModel() {
		if (isShowArch)
			setShowArch(false);
		else
			setShowArch(true);
		selectNowFrame();
	}

	/**
	 *  共3种状态
	 *  0 什么都不显示
	 *  1 显示椭圆
	 *  2 显示椭圆和中心点轨迹
	 */
	public void selectCOF() {
		showCOF = ++showCOF % 3;
		selectNowFrame();
	}

	public void selectContourView() {
		contourOr2D = 2;
		selectNowFrame();
	}

	private int indexDleta = 0;

	public void selectDletaFrame() {
		if (isPlay)
			stop();
		if (indexDleta++ % 2 == 0) {
			setStrange(true);
			whichInStrange = 2;
			setaFrameInStrange(displayDoc.getDeltaFrame());
		} else {
			selectNowFrame();
		}
	}

	/**
	 * 转为图例使用
	 */
	public void setDeltaFrame() {
		setaFrameInStrange(displayDoc.getDeltaFrame());
	}

	public void selectfirstFrame() {
		setStrange(false);
		frameIndex = displayDoc.getFirstContactFrame();
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);

	}

	public void selectForceInXY(int x, int y) {
		setX(x);
		setY(y);
		if (frameIndex < totalFrameNum) {
			setForce(displayDoc.getForceinXY(frameIndex, x, y));
			fireChangeEvent(new ChangeEvent(this));
		}
	}

	public void selectMaxAreaFrame() {
		setStrange(false);
		frameIndex = displayDoc.getMaxAreaFrame();
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);
	}

	int indexMaxForce = 0;

	public void selectMaxForceFrame() {
		if (isPlay)
			stop();
		if (indexMaxForce++ % 2 == 0) {
			setStrange(true);
			whichInStrange = 1;
			setaFrameInStrange(displayDoc.getMaxForceFrame());
		} else {
			selectNowFrame();
		}
	}

	public void setMaxForceFrame() {
		setaFrameInStrange(displayDoc.getMaxForceFrame());
	}

	public void selectNowFrame() {
		setStrange(false);
		if (frameIndex < totalFrameNum)
			//setaFrame(displayDoc.getSelectedFrame(frameIndex));
			setaFrameByFrameIndex(frameIndex);
	}

	public void setaFrameByFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
		if (frameIndex < totalFrameNum) {
			this.aFrame = displayDoc.getSelectedFrame(frameIndex);
			int length = percentData.length;
			percentForce = new double[length];
			for (int i = 0; i < length; i++) {
				percentForce[i] = percentData[i][frameIndex];
			}
			//this.percentForce = new double[] { percentData[0][frameIndex], percentData[1][frameIndex], percentData[2][frameIndex],percentData[2][frameIndex],percentData[2][frameIndex] };
			setTowho(0);
			fireChangeEvent(new ChangeEvent(this));
		}
	}

	public void setaFrameInStrange(int[] aFrame) {
		this.aFrame = aFrame;
		setTowho(0);
		fireChangeEvent(new ChangeEvent(this));
	}

	public void setForce(int force) {
		this.force = force;
	}

	public void setPercentData(double[][] percentData) {
		this.percentData = percentData;
		setTowho(1);
		fireChangeEvent(new ChangeEvent(this));
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	public void setShowArch(boolean isShowArch) {
		this.isShowArch = isShowArch;
	}

	public void setShowCOF(int showCOF) {
		this.showCOF = showCOF;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public void setStrange(boolean isStrange) {
		this.isStrange = isStrange;
	}

	public void setTowho(int towho) {
		this.towho = towho;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 超慢
	 */
	public void slowest() {
		selectedSpeed = 0;
		period = 5000;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		isPlay = false;
		synchronized (DisplayModel.this) {
			DisplayModel.this.notify();
		}

		timer.cancel();
		timer.purge();
		timer = new Timer();

		if (frameIndex < totalFrameNum)
			setaFrameByFrameIndex(frameIndex);

	}

	public int getPeriod() {
		return period;
	}

	public boolean isShowForceDist() {
		return isShowForceDist;
	}

	public void setShowForceDist(boolean isShowForceDist) {
		this.isShowForceDist = isShowForceDist;
	}

	public void selectForceDist() {
		if (isShowForceDist)
			setShowForceDist(false);
		else
			setShowForceDist(true);
		selectNowFrame();
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		fireChangeEvent(new ChangeEvent(this));
	}

	public double[] getPercentForce() {
		return percentForce;
	}

	public int[] getPowaGain() {
		return powaGain;
	}

	@Override
	public void update(Observable o, Object arg) {
		Boolean bool = (Boolean) arg;
		setDirty(bool);
	}

	//@date 2012.5.30
	double weight;

	public double getWeight() {
		if (frameIndex < totalFrameNum) {
			weight = displayDoc.getSelectedFrameWeight(frameIndex);
		} else {
			weight = -1;
		}

		return weight;
	}
}
