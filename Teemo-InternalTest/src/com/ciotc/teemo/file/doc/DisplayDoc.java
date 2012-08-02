package com.ciotc.teemo.file.doc;

import static com.ciotc.teemo.neuralnetwork.weight.Constant.*;
import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.Point;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import org.neuroph.core.NeuralNetwork;

import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.file.doc.RealtimeDoc.WeightInput;
import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.usbdll.USBDLL;
import com.ciotc.teemo.weight.FCalculator;
import com.ciotc.teemo.weight.RFFuntion;
import com.ciotc.teemo.weight.RFRelation;

/**
 * 文档类，记录了视图的数据
 * 
 * @author 林晓智
 * 
 */
public class DisplayDoc {

	private static final int ROW_NUM = 44;
	private static final int COL_NUM = 52;
	private static final float UNIT_CONTACT_AREA = 1.61f; //1.27mm * 1.27mm = 1.61mm^2
	/**
	 * 文件名
	 */
	String fileName = null;

	/**
	 * 文件路径（规定：包括文件名，即绝对路径）
	 */
	String filePath = null;
	/**
	 * 控制点文件路径
	 */
	String pointsFilePath = null;

	/**
	 * 帧数
	 */
	private int frameLen = 0;

	/**
	 * 一帧的总数
	 */
	private int totalNum = 2288;

	/**
	 * 用来判断是否是以前的版本
	 * 以前的版本是不存powa和gain的
	 */
	boolean isOldVersion;
	int powa;
	int gain;

	int patientID = 0;
	String patientName = null;
	Patients patient = null; //从文件里读到的患者数据
	String pComment = null; //文件的评论数据
	long commentPointer = 0L; //评论的位置

	Point[] archPointsOut = null; //最终的控制点
	Point[] archPointsIn = null;

	Point[] archPointsOut0 = null; //控制点文件的内容
	Point[] archPointsIn0 = null;

	Point[] archPointsOut1 = null; // 影片文件里的控制点
	Point[] archPointsIn1 = null;
	long archpointPointer = 0L; //弓形模型控制点坐标

	boolean isExistPointsFile = false; // 是否存在控制点文件

	// 定义一些的数据
	// private byte[][] data = new byte[10][2288]; //1840
	private byte[][] data;// = new byte[frameLen][2288];
	private float period;

	Preferences pref = Preferences.userNodeForPackage(MainFrame.class);

	public DisplayDoc(String filePath) {
		super();
		this.filePath = filePath;
		// getFileData();
		String[] str = filePath.split("\\\\"); // 不是\\
		fileName = str[str.length - 1]; // 得到最后一个 带后缀的

	}

	public byte[][] getData() {
		return data;
	}

	// Max(1 to MA) - MA
	public int[] getDeltaFrame() {
		int[] max = getMaxForceFrame();
		int[] ma = dataFilter(data[getMaxAreaFrame()]);
		int[] b = new int[totalNum];
		for (int j = 0; j < totalNum; j++) {
			int t = max[j];//(max[j] < 0 ? max[j] + 256 : max[j]);
			int m = ma[j];//(ma[j] < 0 ? ma[j] + 256 : ma[j]);
			b[j] = t - m;
		}
		return b;
	}

	/**
	 * 该读取数据方法用RandomAccessFile来实现 新的数据格式
	 * 
	 * @return
	 */
	public boolean getFileData() {
		if (!judgeIsExistPointFile())
			return false;
		if (!isExistPointsFile) { //如果没有控制点文件的话 那么就直接将影片文件的点作为有效的控制点
			if (!getMovieFileData())
				return false;
			setArchPointsIn(archPointsIn1);
			setArchPointsOut(archPointsOut1);
		} else {
			//得到影片文件的控制点及其它所有内容
			if (!getMovieFileData())
				return false;
			//得到控制点文件的点
			getPointsFile();
			//比较两种类型的点
			if (comparePointsFile()) {
				setArchPointsIn(archPointsIn1);
				setArchPointsOut(archPointsOut1);
			} else {
				//不一样弹出对话框提示需要选择哪一种类型的控制点
				Object[] message = new Object[3];
				message[0] = getResourceString("queryselectwhichfile");
				message[1] = getResourceString("queryselectwhichfile1");
				message[2] = getResourceString("queryselectwhichfile2");
				int result = JOptionPane.showOptionDialog(null, message, getResourceString("Teemo"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, 0);
				switch (result) {
				case JOptionPane.YES_OPTION:
					setArchPointsIn(archPointsIn1);
					setArchPointsOut(archPointsOut1);
					break;
				case JOptionPane.NO_OPTION:
					setArchPointsIn(archPointsIn0);
					setArchPointsOut(archPointsOut0);
					break;
				default: //点击关闭按钮时
					setArchPointsIn(archPointsIn1);
					setArchPointsOut(archPointsOut1);
					break;
				}
			}

		}
		return true;
	}

	/**
	 * 获得影片文件的所有数据
	 * @return
	 */
	public boolean getMovieFileData() {
		int i = 0;
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filePath, "r");
			/**
			 * 新数据格式 
			 * 1、患者信息: 
			 * 	1.患者姓名
			 * 	2.患者ID 
			 * 	3.患者身份证 
			 * 	4.患者性别 
			 * 	5.患者门牙宽度
			 * 2、帧数 
			 * 3、录制周期
			 * 4、数据内容
			 * 5、弓形模型控制点坐标
			 * 6、评论内容
			 * 7、gain和powa @date 2012.4.13
			 */
			patient = readPatient2File(rf);
			patientName = patient.getName();
			//patientName = rf.readUTF();
			frameLen = rf.readInt();
			period = rf.readFloat();
			if (frameLen > 2 * 1024 * 1024) // 数据明显不正确
				return false;
			data = new byte[frameLen][totalNum];
			for (i = 0; i < frameLen; i++) {
				rf.read(data[i], 0, totalNum);
//				for(j = 0;j<totalNum;j++)
//					data[i][j] = dis.readByte();

				// 将超出矩阵范围的数据全部去掉
				//将这一方法放在录制时，存储的时候
//				for (j = 0; j < totalNum; j++) {
//
//					int x = j / rowNum;
//					int y = j % rowNum;
//					
//					if (RealtimeView.backgroundArray[rowNum - y - 1][colNum - x - 1] == 0)
//						data[i][j] = 0;
//				}
//				
//				
//				Byte[][] bb = new Byte[44][52];
//				for (j = 0; j < totalNum; j++) {
//					int x = j / rowNum;
//					int y = j % rowNum;
//					bb[rowNum - y - 1][colNum - x - 1] = data[i][j];
//				}

			}

			archpointPointer = rf.getFilePointer();
			int len = rf.readInt();
			archPointsOut1 = new Point[len];
			for (int ii = 0; ii < len; ii++) {
				int px = rf.readInt();
				int py = rf.readInt();
				archPointsOut1[ii] = new Point(px, py);
			}
			len = rf.readInt();
			archPointsIn1 = new Point[len];
			for (int ii = 0; ii < len; ii++) {
				int px = rf.readInt();
				int py = rf.readInt();
				archPointsIn1[ii] = new Point(px, py);
			}

			commentPointer = rf.getFilePointer();
			pComment = rf.readUTF(); //读取评论信息

			/**
			 * 与以前的兼容 
			 * 以前是没有存gain和powa的
			 */
			try {
				powa = rf.readInt();
				gain = rf.readInt();
			} catch (Exception e2) {
				//e2.printStackTrace();
				isOldVersion = true;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 读取控制点文件的数据
	 * 它必须在读到了patientName之后才能进行
	 */
	public void getPointsFile() {
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(pointsFilePath, "rw");
			int len = rf.readInt();
			archPointsOut0 = new Point[len];
			for (int ii = 0; ii < len; ii++) {
				int px = rf.readInt();
				int py = rf.readInt();
				archPointsOut0[ii] = new Point(px, py);
			}
			len = rf.readInt();
			archPointsIn0 = new Point[len];
			for (int ii = 0; ii < len; ii++) {
				int px = rf.readInt();
				int py = rf.readInt();
				archPointsIn0[ii] = new Point(px, py);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 判断是否有控制点文件
	 * 方法还求出了控制点文件路径和isExistPointsFile
	 * @return 
	 */
	private boolean judgeIsExistPointFile() {
		RandomAccessFile rf = null;
		isExistPointsFile = false;
		try {
			rf = new RandomAccessFile(filePath, "r");
			String name = readPatient2File(rf).getId(); //得到患者名 不能用患者名 用id

			//获得控制点文件的路径 它是与影片同文件夹下 以患者名的hascode为名字 文件扩展名为tmp
			//String[] s = filePath.split("\\\\");
			String ss = String.valueOf(name);
			//去掉文件名
			pointsFilePath = filePath.substring(0, filePath.length() - fileName.length() - 1) + "\\" + ss + ".tma";

			File f = new File(pointsFilePath);
			if (f.exists())
				isExistPointsFile = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 比较两个文件的数据控制点是否相同
	 * @return
	 */
	private boolean comparePointsFile() {
		// 如果控制点文件为null的话 肯定不同
		if (archPointsIn0 == null || archPointsOut0 == null)
			return false;
		//长度不等时也返回
		if (archPointsIn0.length != archPointsIn1.length || archPointsOut0.length != archPointsOut1.length)
			return false;
		//具体每个值进行比较
		for (int i = 0; i < archPointsIn0.length; i++) {
			Point p0 = archPointsIn0[i];
			Point p1 = archPointsIn1[i];
			if (!p0.equals(p1))
				return false;
		}
		for (int i = 0; i < archPointsOut0.length; i++) {
			Point p0 = archPointsOut0[i];
			Point p1 = archPointsOut1[i];
			if (!p0.equals(p1))
				return false;
		}
		return true;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	/**
	 * 
	 * @return 找不到的话 返回第一帧
	 */
	public int getFirstContactFrame() {
		// 最简单的算法 遍历
		int i = 0, j = 0;
		for (i = 0; i < data.length; i++) {
			int[] bs = dataFilter(data[i]);
			for (j = 0; j < bs.length; j++) {
				int b = bs[j];
				if (b != 0)
					return i;
			}
		}
		//都找不到表示全部数据都为0
		return 0;
	}

	/**
	 * 
	 * @param in
	 * @param x
	 *                坐标轴上的横坐标
	 * @param y
	 *                坐标轴上的纵坐标
	 * @return
	 */
	public int getForceinXY(int in, int x, int y) {
		//		int i = rowNum * (colNum - 1 - x) + rowNum - 1 - y;
		int i = ROW_NUM * x + y;
		int d = data[in][i];
		d = d < 0 ? d + 256 : d;
		return d;
	}

	public int getFrameLen() {
		return frameLen;
	}

	public int getMaxAreaFrame() {
		// 复杂度o(n^2)
		int i = 0, j = 0;
		long max = 0;
		int k = 0;
		for (i = 0; i < data.length; i++) {
			int[] bs = dataFilter(data[i]);
			int temp = 0;
			for (j = 0; j < bs.length; j++) {
				// int t = bs[j] < 0? bs[j]+256:bs[j];
				// temp += t;
				if (bs[j] != 0)
					temp++;
			}
			if (max <= temp) {
				max = temp;
				k = i;
			}
		}
		return k;
	}

	// Max(1to MA)
	public int[] getMaxForceFrame() {
		int[] temp = new int[totalNum]; // 初始值都为0
		int i = 0, j = 0;

		int p = pref.getInt("maxOption", 1);
		int k = 0;
		switch (p) {
		case 1:
			k = getMaxAreaFrame() + 1;
			break;
		case 2:
			k = frameLen;
			break;
		}

		for (i = 0; i < k; i++) {
			//byte[] bs = data[i];
			int[] bs = dataFilter(data[i]);
			for (j = 0; j < bs.length; j++) {
				int t = bs[j];//(bs[j] < 0 ? bs[j] + 256 : bs[j]);
				int m = temp[j];//(temp[j] < 0 ? temp[j] + 256 : temp[j]);
				temp[j] = (m <= t ? bs[j] : temp[j]);

			}
		}
		return temp;
	}

	public Patients getPatient() {
		return patient;
	}

	public int getPatientID() {
		return patientID;
	}

	public String getPatientName() {
		return patientName;
	}

	public String getpComment() {
		return pComment;
	}

	public double[] getPercentInAFrame(int in, int x) {
		double[] d = new double[2];
		int m;

		for (int i = 0; i < totalNum; i++) {
			int[] dd = dataFilter(data[in]);
			m = i / ROW_NUM;
			if (m < x)
				d[0] += dd[i];
			else
				d[1] += dd[i];
		}
		double dd = (d[0] + d[1]) * 1.0;
		d[0] = Math.round(d[0] * 100.0 / dd);
		d[1] = Math.round(d[1] * 100.0 / dd);
		return d;
	}

	/**
	 * 返回值double[i][j],其中 i表示不同的类型，j表示第几帧数据
	 * 类型表达的含义如下：<br>
	 * 0:这帧的总和占的百分比<br>
	 * 1:右边占的百分比<br>
	 * 2:左边占的百分比<br>
	 * 3:这帧的接触面积<br>
	 * 4:这帧的总的力度值的和<br>
	 * 5:这帧的每接触面积的力度值=总得力度值/接触面积<br>
	 * @param mid
	 * @return
	 */
	public double[][] getPercentPerFrame(int mid) {
		//mid = (colNum - mid) * rowNum;
		mid = mid * ROW_NUM;
		double[][] dd = new double[6][frameLen];
		int i = 0, j = 0;
		long max = 0L;

		// 1求最大力的帧 总的 左边 右边
		for (i = 0; i < frameLen; i++) {
			int[] bs = dataFilter(data[i]);
			int temp0 = 0, temp1 = 0, temp2 = 0;
			// 总的
			//接触面积 @date 2012.4.16
			int contactNumber = 0;
			for (j = 0; j < bs.length; j++) {
				int t = bs[j];//bs[j] < 0 ? bs[j] + 256 : bs[j];
				temp0 += t;

				if (t != 0)
					contactNumber++;
			}
			//接触面积
			dd[3][i] = contactNumber * UNIT_CONTACT_AREA;
			//力度和
			dd[4][i] = temp0;
			//每接触面积力度值
			if (dd[3][i] == 0)
				dd[5][i] = 0;
			else
				dd[5][i] = temp0 / dd[3][i];

			dd[0][i] = temp0;
			if (max <= temp0) {
				max = temp0;
			}

			// int mid = totalNum >> 2;
			// 左边
			for (j = totalNum - 1; j >= mid; j--) {
				int t = bs[j];//bs[j] < 0 ? bs[j] + 256 : bs[j];
				temp1 += t;
			}
			if (temp0 == 0)
				dd[2][i] = 0.0;
			else
				dd[2][i] = Math.round(temp1 * 100.0 / temp0) / 100.0;

			// 右边
			for (j = 0; j < mid; j++) {
				int t = bs[j];//bs[j] < 0 ? bs[j] + 256 : bs[j];
				temp2 += t;
			}
			if (temp0 == 0)
				dd[1][i] = 0.0;
			else
				dd[1][i] = Math.round(temp2 * 100.0 / temp0) / 100.0;
		}
		// 求百分比
		for (i = 0; i < frameLen; i++) {
			dd[0][i] = Math.round(dd[0][i] / max * 100) / 100.0;
		}

		return dd;
	}

	/**
	 * 对数据进行过滤，如将负数调整为正数，去掉低于某一指定值的数
	 * @param dd 数据
	 * @return 经过过滤的新的数据
	 */
	private int[] dataFilter(byte[] dd) {
		int len = dd.length;
		int[] newD = new int[len];

		int lower = pref.getInt("lower", 0); //下限值
		//int[] lowers = {0,15,31,47,63,79,95,111,127,143,159,175,191,207,223,239,255,256};
		//int lower = lowers[2];
		for (int i = 0; i < len; i++) {
			int data = dd[i];
			//将负数调整为正数
			if (data < 0)
				data += 256;
			if (data < lower)
				data = 0;
			newD[i] = data;
		}
		return newD;
	}

	public float getPeriod() {
		return period;
	}

	public int[] getSelectedFrame(int i) {
		return dataFilter(data[i]);
	}

	/**
	 * 从文件中读出患者的信息 返回一个患者对象
	 * 
	 * @param ds
	 * @throws SQLException
	 * @throws IOException
	 */
	private Patients readPatient2File(DataInput ds) throws IOException {

		String pName = ds.readUTF();
		String pId = ds.readUTF();
		String pIDCard = ds.readUTF();
		int pGender = ds.readInt();
		float pArch = ds.readFloat();

		Patients p = new Patients();
		p.setName(pName);
		p.setId(pId);
		p.setIdcard(pIDCard);
		p.setGender(pGender);
		p.setArch(pArch);
		return p;
	}

	/**
	 * 将评论内容写回文件
	 * 
	 * @return
	 */
	public boolean saveComment2File() {
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filePath, "rw");
			//定位到评论内容的位置 因为前面的内容都是已固定的
			//把另一个种格式的阻挡掉
			if (commentPointer != 0) {
				rf.seek(commentPointer);
				rf.writeUTF(pComment);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 将控制点数据存储到影片文件
	 * 将弓形模型的控制点坐标写进文件 注意覆盖问题 写进的文件可能会覆盖后面评论的内容 所以需要把后面评论的内容重新写一遍
	 * @return
	 */
	private boolean saveArchPoints2MovieFile() {
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filePath, "rw");
			if (archpointPointer != 0) {
				rf.seek(archpointPointer);
				rf.writeInt(archPointsOut.length);
				for (int i = 0; i < archPointsOut.length; i++) {
					rf.writeInt(archPointsOut[i].x);
					rf.writeInt(archPointsOut[i].y);
				}
				rf.writeInt(archPointsIn.length);
				for (int i = 0; i < archPointsIn.length; i++) {
					rf.writeInt(archPointsIn[i].x);
					rf.writeInt(archPointsIn[i].y);
				}
			}
			//重新调整评论内容的起始位置
			commentPointer = rf.getFilePointer();
			rf.writeUTF(pComment);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 将控制点数据存储到控制点文件
	 * @return
	 */
	private boolean saveArchPoints2PointsFile() {
		RandomAccessFile rf = null;
		try {
			//暂不考虑文件长度 覆盖等问题
			rf = new RandomAccessFile(pointsFilePath, "rw");
			rf.writeInt(archPointsOut.length);
			for (int i = 0; i < archPointsOut.length; i++) {
				rf.writeInt(archPointsOut[i].x);
				rf.writeInt(archPointsOut[i].y);
			}
			rf.writeInt(archPointsIn.length);
			for (int i = 0; i < archPointsIn.length; i++) {
				rf.writeInt(archPointsIn[i].x);
				rf.writeInt(archPointsIn[i].y);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * 
	 * @throws IOException
	 * @return index 1 保存成功 2保存失败
	 */
	public int saveArchPoints2File() {
		int index = 3;
		if (!isExistPointsFile) {
			if (saveArchPoints2MovieFile())
				index = 1;
			else
				index = 2;
		} else {

			String[] options = { getResourceString("querysavewhichfile1"), getResourceString("querysavewhichfile2"), getResourceString("querysavewhichfile3") };
			int result = JOptionPane.showOptionDialog(null, getResourceString("querysavewhichfile"), getResourceString("Teemo"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
			switch (result) {
			case 0:
				index = saveArchPoints2MovieFile() == true ? 1 : 2;
				break;
			case 1:
				index = saveArchPoints2PointsFile() == true ? 1 : 2;
				break;
			case 2:
				index = saveArchPoints2MovieFile() && saveArchPoints2PointsFile() == true ? 1 : 2;
				break;
			default: //点击关闭按钮时
				index = saveArchPoints2MovieFile() == true ? 1 : 2;
				break;
			}
		}
		return index;
	}

	public void setpComment(String pComment) {
		this.pComment = pComment;
	}

	public Point[] getArchPointsOut() {
		return archPointsOut;
	}

	public Point[] getArchPointsIn() {
		return archPointsIn;
	}

	public void setArchPointsOut(Point[] archPointsOut) {
		this.archPointsOut = archPointsOut;
	}

	public void setArchPointsIn(Point[] archPointsIn) {
		this.archPointsIn = archPointsIn;
	}

	//是否需要保存文件的标识 
	//boolean isDirty = false;
	public void setAllArchPoints(Point[] archPointsOut, Point[] archPointsIn) {
		this.archPointsOut = archPointsOut;
		this.archPointsIn = archPointsIn;
		//isDirty = true;
	}

	public int getPowa() {
		return powa;
	}

	public int getGain() {
		return USBDLL.GAINS[gain];
	}

	public boolean isOldVersion() {
		return isOldVersion;
	}

	//@date2012.6.12
	DisplayFileTemplate dft = null;

	public void setDisplayFileTemplate(DisplayFileTemplate dft) {
		this.dft = dft;
	}

	//@date 2012.6.14
	//Map<Double, Double> rfMap = RFRelation.readRFMapFromMatlab();
	//FCalculator fc = new FCalculator(rfMap);

	//@date2012.5.30
	//@date2012.6.12
	public double getSelectedFrameWeight(int index) {
		double weight = 0;

		if (dft == null)
			return weight;

		int[] bs = dataFilter(data[index]);

		List<Integer> values = new ArrayList<Integer>();
		for (int in : bs) {
			if (in != 0) {
				values.add(in);
			}
		}

		//@date 2012.6.14
		//if (rfMap != null) {
		weight = RFFuntion.calcF(values, powa);//fc.calculateF(values, powa);
		weight = weight / 9.8;
		//}

//不用神经网络		
//		WeightInput[] wi = dft.archPoints.calcCountAndValue(bs);
//		InputStream is = ClassLoader.getSystemResourceAsStream("com/ciotc/teemo/neuralnetwork/weight/network.nnet");
//		if (is == null)
//			return weight;
//		NeuralNetwork network = NeuralNetwork.load(is);
//
//		for (int i = 0; i < wi.length; i++) {
//			double[] ds = new double[2];
//			double maxCount = wi[i].getCount();
//			double maxWeight = wi[i].getValue();
//			
//			//看powa 基础值是15
//			//maxWeight *= (powa / 15);
//			//maxCount *= (powa/15);
//			
//			double tempWeight = 0;
//			if (maxCount == 0 && maxWeight == 0)
//				continue;
//			ds[0] = maxCount * 1.0f / MAX_COUNT;
//			ds[1] = maxWeight * 1.0f / MAX_VALUE;
//
//			network.setInput(ds);
//			network.calculate();
//			double[] output = network.getOutput();
//			//System.out.println(output[0]*MAX_FORCE);
//			tempWeight = output[0] * MAX_FORCE;
//			weight += tempWeight;
//		}
		//System.out.println("weight:" + weight);
		return weight;
	}

	public void printData() {
		PrintStream out = System.out;
		try {
			System.setOut(new PrintStream(getFileName() + "data"+".txt"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.setOut(out);
			System.out.println("cannot open printStream");
			return;
		}

		for (int i = 0; i < frameLen; i++) {
			System.out.println((i + 1) + "----------------");
			int[] bd = dataFilter(data[i]);

			for (int y = 0; y < 52; y++) {
				for (int x = 0; x < 44; x++) {
					int t = bd[x * 44 + y];
					System.out.printf("%3d ", t);
				}
				System.out.println();
			}
			System.out.println();
		}
		System.setOut(out);
	}

}
