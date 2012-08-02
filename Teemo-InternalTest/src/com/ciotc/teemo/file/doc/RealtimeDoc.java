package com.ciotc.teemo.file.doc;

import java.awt.Point;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.prefs.Preferences;

import com.ciotc.teemo.domain.Movies;
import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.file.view.RealtimeView;
import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.service.MoviesService;
import com.ciotc.teemo.service.PatientsService;

/**
 * 文档类，记录了视图的数据
 * @author 林晓智
 *
 */
public class RealtimeDoc {

	/**
	 * 一帧中共有的传感单元数
	 */
	private final static int TOTAL_NUM = 2288;

	/**
	 * 患者ID 由构造函数传过来
	 */
	private int patientID = 0;
	private String patientName = null;
	private String createTime = null;
	/**
	 * 文件路径（规定：包括文件名，即绝对路径）
	 */
	private String filePath = null;
	private String filePathDir = null;

	/**
	 * 帧数
	 */
	private int frameLen = 0;
	/**
	 * 存储的所有数据
	 */
	private byte[][] data;//= new byte[frameLen][2288];
	/**
	 * 录制的周期
	 */
	private float period = 0;
	/**
	 * 新增
	 * @date 2012.4.13
	 */
	private int powa = 0;
	private int gain = 0;
//	/**
//	 * 存储采集到的每一帧数据的临时文件
//	 */
//	private RandomAccessFile dataFile = null;
	/**
	 * 采用链表来存储数据 
	 * 因为不知道一开始不知道它的长度
	 */
	private List<Byte[]> dataList = new ArrayList<Byte[]>();

	public RealtimeDoc(int patientID, String patientName) {
		super();
		//this.filePath = filePath;
		//getFileData();
		this.patientID = patientID;
		this.patientName = patientName;
		//this.patientID = 
		Date date = new Date();

		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		createTime = df2.format(date);

		SimpleDateFormat df = new SimpleDateFormat("_yyyy-MM-dd_kk-mm-ss");
		String str = df.format(date);
		String fileName = patientName + str + ".tmo";
		String dirPath = "Database";
		this.filePathDir = dirPath + "\\" + patientName;
		this.filePath = filePathDir + "\\" + fileName;
		System.out.println("fileName:" + fileName + "," + "filePath:" + filePath);

	}

	public boolean saveData2File() {
		File fileDir = new File(filePathDir);

		//if(!fileDir.mkdir())
		//	return false;
		fileDir.mkdir();

		//新建并存储控制点文件
		//命名格式 名字的hashcode.tma 并将其设为隐藏  
		//不要用名字 因为名字可能为被改
		String str = String.valueOf(patientID);
		File f = new File(filePathDir + "\\" + str + ".tma");
		if (!f.exists()) {
			FileOutputStream fos = null;
			DataOutputStream dos = null;
			try {
				f.createNewFile();

				try {
					fos = new FileOutputStream(f);
					try {
						dos = new DataOutputStream(fos);
						writeArchPoints2File(dos);
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					} finally {
						try {
							if (dos != null)
								dos.close();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								if (fos != null)
									fos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		//先创建该文件
		File file = new File(filePath);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		FileOutputStream os = null;
		DataOutputStream ds = null;
		try {
			os = new FileOutputStream(filePath);
			ds = new DataOutputStream(os);
			//根据患者ID得到患者对象 进而得到患者的所有信息 将它们存入文件中

			String pComment = ""; //默认的评论值为空串
			/**
			 * 数据格式
			 * 1、患者信息:
			 * 	1.患者姓名
			 * 	2.患者ID
			 * 	3.患者身份证
			 * 	4.患者性别
			 * 	5.患者门牙宽度
			 * 2、帧数
			 * 3、录制周期
			 * 4、数据内容
			 * 5、控制点坐标
			 * 6、评论内容
			 * 7、powa和gain 新增加的 @data 2012.4.13
			 */
			writePatient2File(ds);

			ds.writeInt(frameLen);
			ds.writeFloat(period);
			//ds.write(frameLen);
			//得到录制设置：是用大传感器还是小传感器
			Preferences pref = Preferences.userNodeForPackage(MainFrame.class);
			int in = pref.getInt("sensorSize", 1); //1表示大 2表示小
			for (int i = 0; i < frameLen; i++) {
				if (in == 2) {
					int rowNum = 44;
					int colNum = 52;
					//Byte[][] bb = new Byte[44][52];
					for (int j = 0; j < TOTAL_NUM; j++) {
						int x = j / rowNum;
						int y = j % rowNum;
						if (RealtimeView.existArrayInSmall[rowNum - y - 1][colNum - x - 1] == 0)
							data[i][j] = 0;

						ds.write(data[i][j]);
					}
				} else {
					int rowNum = 44;
					int colNum = 52;
					//Byte[][] bb = new Byte[44][52];
					for (int j = 0; j < TOTAL_NUM; j++) {
						int x = j / rowNum;
						int y = j % rowNum;
						if (RealtimeView.existArrayInLarge[rowNum - y - 1][colNum - x - 1] == 0)
							data[i][j] = 0;

						ds.write(data[i][j]);
					}
				}

				//ds.write(data[i], 0, totalNum);
				//ds.flush();
			}
			writeArchPoints2File(ds);
			ds.writeUTF(pComment);

			/**
			 * @date 2012.4.13
			 */
			ds.writeInt(powa);
			ds.writeInt(gain);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (ds != null)
					ds.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					if (os != null)
						os.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

		}
		return true;

	}

	/**
	 * 将患者的信息存入到文件中
	 * @param ds
	 * @throws SQLException
	 * @throws IOException
	 */
	private void writePatient2File(DataOutputStream ds) throws SQLException, IOException {
		PatientsService pntService = PatientsService.getInstance();
		Patients patient = pntService.selectByPrimaryKey(String.valueOf(patientID));
		String pName = patientName;
		String pId = patient.getId();
		String pIDCard = patient.getIdcard();
		int pGender = patient.getGender();
		float pArch = patient.getArch();
		ds.writeUTF(pName);
		ds.writeUTF(pId);
		ds.writeUTF(pIDCard);
		ds.writeInt(pGender);
		ds.writeFloat(pArch);
	}

//	final static Point[] archPointsOut = { new Point(0,43),new Point(1, 36), new Point(2, 29), new Point(4, 21), new Point(6, 16), new Point(9, 10), new Point(12, 5), new Point(18, 1), new Point(26, 0), new Point(33, 1), new Point(40, 5), new Point(43, 11), new Point(46, 16), new Point(47, 21),
//			new Point(49, 29), new Point(51, 36), new Point(52,43)};
//
//	final static Point[] archPointsIn = { new Point(12, 43), new Point(13, 38), new Point(14, 31), new Point(15, 24), new Point(16, 19), new Point(18, 14), new Point(19, 10), /*new Point(21, 8), new Point(26, 6), new Point(31, 8),*/new Point(21, 9), new Point(26, 8), new Point(31, 9), new Point(33, 10), new Point(34, 14), new Point(36, 19),
//			new Point(37, 24), new Point(38, 32), new Point(39, 38), new Point(40, 43) };
	//新的
	final static Point[] archPointsOut = { new Point(1, 41), new Point(2, 36), new Point(3, 31), new Point(4, 26), new Point(6, 20), new Point(9, 15), new Point(13, 11), new Point(20, 7), new Point(26, 6), new Point(32, 7), new Point(39, 11), new Point(43, 15), new Point(46, 20),
			new Point(48, 26), new Point(49, 31), new Point(50, 36), new Point(51, 41) };

	final static Point[] archPointsIn = { new Point(13, 41), new Point(14, 37), new Point(15, 33), new Point(16, 29), new Point(17, 24), new Point(18, 19), new Point(20, 16), new Point(22, 15), new Point(26, 15), new Point(30, 15), new Point(32, 16), new Point(34, 19), new Point(35, 24),
			new Point(36, 29), new Point(37, 33), new Point(38, 37), new Point(39, 41) };

//	final static Point[] archPointsOut = { new Point(0, 36), new Point(2, 29), new Point(4, 21), new Point(6, 16), new Point(9, 10), new Point(12, 5), new Point(18, 1), new Point(26, 0), new Point(33, 1), new Point(40, 5), new Point(43, 11), new Point(46, 16), new Point(47, 21),
//		new Point(49, 29), new Point(52, 36) };
//	
//	final static Point[] archPointsIn = { new Point(12, 43), new Point(13, 38), new Point(14, 31), new Point(15, 24), new Point(16, 19), new Point(18, 14), new Point(19, 10), new Point(21, 8), new Point(26, 6), new Point(31, 8), new Point(33, 10), new Point(34, 14), new Point(36, 19),
//		new Point(37, 24), new Point(38, 32), new Point(39, 38), new Point(40, 43) };

	/**
	 * 将弓形模型的控制点坐标写进文件
	 * @throws IOException 
	 */
	private void writeArchPoints2File(DataOutputStream ds) throws IOException {
		/**
		 * 从外到内每个点依次写进  对于每个点先写x坐标再写y坐标
		 */
		ds.writeInt(archPointsOut.length);
		for (int i = 0; i < archPointsOut.length; i++) {
			ds.writeInt(archPointsOut[i].x * 10);
			ds.writeInt(archPointsOut[i].y * 10);
		}
		ds.writeInt(archPointsIn.length);
		for (int i = 0; i < archPointsIn.length; i++) {
			ds.writeInt(archPointsIn[i].x * 10);
			ds.writeInt(archPointsIn[i].y * 10);
		}
	}

	/**
	 * 插入到影片表
	 * @return
	 */
	public boolean saveData2Database() {
		//先存文件 还是先存数据库 -->先存文件
		//根据patientID 插入数据库
		Movies mov = new Movies();
		MoviesService movService = MoviesService.getInstance();
		mov.setPatientid(String.valueOf(patientID));
		//mov.setDescription("测试");
		//mov.setLinkphoto("photoUrl");
		mov.setCreatetime(createTime);
		mov.setPath(filePath);
		mov.setDescription("无");
		try {
			movService.insertSelective(mov);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 开始录制 它会创建一个临时文件 用来存储录制的帧数据
	 */
	public void startRecord() {
		System.out.println("start record");
//		File file = new File("frame.dat");
//		if (file.exists())
//			file.delete();
//		try {
//			dataFile = new RandomAccessFile(file, "rw");
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}

//		try {
//			FileOutputStream fos = new FileOutputStream(dataFile);
//		} catch (FileNotFoundException e2) {
//			e2.printStackTrace();
//		}
	}

	/**
	 * 结束录制 它会关闭这个临时文件 
	 */
	public synchronized void stopRecord() {
		/**
		 * 把dataList赋值给data 为了兼容以前的函数
		 */
		frameLen = dataList.size();
		data = new byte[frameLen][TOTAL_NUM];
		for (int i = 0; i < frameLen; i++) {
			byte[] aData = new byte[TOTAL_NUM];
			Byte[] bData = dataList.get(i);
			for (int j = 0; j < TOTAL_NUM; j++)
				aData[j] = bData[j];
			data[i] = aData;
		}

//		try {
//			dataFile.seek(0);//跳回起点
//
//			/**
//			 * 先写成把文件再次督读到内存，赋值给data（为了兼容以前的函数）
//			 */
//			data = new byte[frameLen][TOTAL_NUM];
//			for (int i = 0; i < frameLen; i++) {
//				byte[] aData = new byte[TOTAL_NUM];
//				try {
//					dataFile.read(aData, 0, aData.length);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				data[i] = aData;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (dataFile != null)
//				try {
//					dataFile.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//		}

	}

	/**
	 * 正在录制 
	 */
	public synchronized void recroding(byte[] recvdata) {
		int size = recvdata.length;
		Byte[] bData = new Byte[size];

		for (int i = 0; i < size; i++) {
			bData[i] = recvdata[i];
		}
		dataList.add(bData);
		//frameLen++;

//		try {
//			dataFile.write(recvdata, 0, recvdata.length);
//			frameLen++;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	/**
	 * 测量重量的输入
	 * @author Linxiaozhi
	 *
	 */
	public static class WeightInput {
		/**
		 * 接触面积数
		 */
		int count;

		/**
		 * 接触面积的合力
		 */
		int value;

		/**
		 * 最大值情况下的接触面积数
		 */
		int maxCount;

		/**
		 * 合力最大值的
		 */
		int maxValue;

		/**
		 * 最大力度值
		 */
		int maxPointValue;

		/**
		 * 最小力度值
		 */
		int minPointValue;

		/**
		 * 平均力度值
		 */
		double avgPointValue;

		/**
		 * 列出所有点的值
		 */
		List<Integer> values;

		int gain;
		int powa;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public int getGain() {
			return gain;
		}

		public void setGain(int gain) {
			this.gain = gain;
		}

		public int getPowa() {
			return powa;
		}

		public void setPowa(int powa) {
			this.powa = powa;
		}

		public int getMaxCount() {
			return maxCount;
		}

		public void setMaxCount(int maxCount) {
			this.maxCount = maxCount;
		}

		public int getMaxValue() {
			return maxValue;
		}

		public void setMaxValue(int maxValue) {
			this.maxValue = maxValue;
		}

		public int getMaxPointValue() {
			return maxPointValue;
		}

		public void setMaxPointValue(int maxPointValue) {
			this.maxPointValue = maxPointValue;
		}

		public int getMinPointValue() {
			return minPointValue;
		}

		public void setMinPointValue(int minPointValue) {
			this.minPointValue = minPointValue;
		}

		public double getAvgPointValue() {
			return avgPointValue;
		}

		public void setAvgPointValue(double avgPointValue) {
			this.avgPointValue = avgPointValue;
		}

		public List<Integer> getValues() {
			return values;
		}

		public void setValues(List<Integer> values) {
			this.values = values;
		}

		@Override
		public String toString() {
			return "WeightInput [count=" + count + ", value=" + value + ", maxCount=" + maxCount + ", maxValue=" + maxValue + ", maxPointValue=" + maxPointValue + ", minPointValue=" + minPointValue + ", avgPointValue=" + avgPointValue + ", values=" + values + ", gain=" + gain
					+ ", powa=" + powa + "]";
		}

	}

	public WeightInput getWeightInput() {
		int maxIndex = 0, maxForce = -1;

		int x = 0, y = 0; //用于测试
		for (int i = data.length - 1; i >= 0; i--) {
			byte[] bs = data[i];
			for (int j = 0; j < bs.length; j++) {
				int val = bs[j] & 0xff;
				if (val >= maxForce) {
					maxForce = val;
					maxIndex = i;
					x = j / 44;
					y = j % 44;
				}
			}
		}
		System.out.println("maxForce:" + maxForce + ",index:" + (maxIndex + 1));
		System.out.println("x:" + x + ",y:" + y);

		//只针对那一帧做处理
		byte[] maxFrame = data[maxIndex];
		//求输入值{@see WeightInput}
		int force = 0;
		int count = 0;

		for (byte b : maxFrame) {
			int val = b & 0xff;
			if (val != 0) {
				force += val;
				count++;
			}
		}

		WeightInput wi = new WeightInput();
		wi.value = force;
		wi.count = count;
		wi.gain = gain;
		wi.powa = powa;

		//计算另外一种
		maxIndex = 0;
		count = 0;
		int maxCount = 0;
		maxForce = -1;

		for (int i = data.length - 1; i >= 0; i--) {
			byte[] bs = data[i];
			int temp = 0;
			count = 0;
			for (int j = 0; j < bs.length; j++) {
				int val = bs[j] & 0xff;
				if (val != 0) {
					temp += val;
					count++;
				}
			}
			if (temp > maxForce) {
				maxForce = temp;
				maxCount = count;
				maxIndex = i;
			}
		}
		//只针对上面这种情况算最大、最小值等
		int maxPointValue = -1;
		int minPointValue = Integer.MAX_VALUE;
		double avgPointValue = 0;
		List<Integer> lists = new ArrayList<Integer>();
		Set<Integer> sets = new TreeSet<Integer>();
		maxFrame = data[maxIndex];
		for (byte b : maxFrame) {
			int val = b & 0xff;
			sets.add(val);
			if (val != 0) {
				lists.add(val);
			}
			if (val >= maxPointValue)
				maxPointValue = val;
			if (val < minPointValue)
				minPointValue = val;
		}
		if (sets.size() > 1) {
			Iterator<Integer> it = sets.iterator();
			Integer in = it.next();
			if (in != 0)
				minPointValue = in;
			else
				minPointValue = it.next();
		}

		avgPointValue = maxForce * 1.0f / maxCount;
		wi.maxCount = maxCount;
		wi.maxValue = maxForce;
		wi.maxPointValue = maxPointValue;
		wi.minPointValue = minPointValue;
		wi.avgPointValue = avgPointValue;
		wi.values = new ArrayList<Integer>(lists);
		System.out.println(wi);
		return wi;
	}

	/**
	 * 得到电阻和力值的关系表
	 */
	public List<RF> getRFChart(/*byte[][] data,*/double force) {
		//找到有最大数组的那一帧
		int maxIndex = 0, maxForce = -1;
		int x = 0, y = 0; //用于测试
		for (int i = data.length - 1; i >= 0; i--) {
			byte[] bs = data[i];
			for (int j = 0; j < bs.length; j++) {
				int val = bs[j] & 0xff;
				if (val >= maxForce) {
					maxForce = val;
					maxIndex = i;
					x = j / 44;
					y = j % 44;
				}
			}
		}
		System.out.println("maxForce:" + maxForce + ",index:" + (maxIndex + 1));
		System.out.println("x:" + x + ",y:" + y);
		/**-------------insert----------------------**/
		//打印最大那一帧的数据
		try {
			PrintWriter pw = new PrintWriter("rf-max.txt");

			for (int i = 0; i < data.length; i++) {
				pw.println("------------------------------------------" + (i + 1));
				byte[] bs = data[i];

				for (int ii = 0; ii < 44; ii++) {
					for (int jj = 0; jj < 52; jj++) {
						int tt = jj * 44 + ii;
						pw.printf("%5d ", bs[tt] & 0xff);
					}
					pw.println();
				}
				pw.flush();
			}
			pw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/**-------------insert----------------------**/
		//只针对那一帧做处理
		byte[] maxFrame = data[maxIndex];
		//求出总的力值
		int totalValue = 0;
		List<RF> lists = new ArrayList<RealtimeDoc.RF>();
		for (byte b : maxFrame) {
			int val = b & 0xff;
			if (val != 0) {
				totalValue += val;
				RF rf = new RF();
				rf.value = val;
				lists.add(rf);
			}
		}
		//求出每个点的电阻值、力值
		//得到对应关系表
		for (RF rf : lists) {
			rf.re = calR(rf.value, powa);
			rf.force = calF(rf.value, totalValue, force);
		}
		Collections.sort(lists);
		return lists;
	}

	public static class RF implements Comparable<RF> {
		public int value;
		public double re;
		public double force;

		@Override
		public int compareTo(RF o) {
			//Double src = re;
			//Double dest = o.re;
			//比较电阻值
			//return src.compareTo(dest);
			//比较力值
			Double src = force;
			Double dest = o.force;
			return src.compareTo(dest);
		}

	}

	/**
	 * 计算电阻
	 * @param value 0-255的数值
	 * @param powa 驱动电压
	 */
	private double calR(int value, int powa) {
		return 100.0 * powa / value;
	}

	/**
	 * 计算力值
	 * @param value 0-255的数值
	 * @param totalValue 数值的总和
	 * @param force 给定的力度值
	 * @return
	 */
	private double calF(int value, int totalValue, double force) {
		return force * 1.0 / totalValue * value;
	}

	/**
	 * 读取患者表的powa和gain设置到pefre中去 注意这里不是设置powa和gain这两个字段
	 * 如果有该患者的powa和gain的话 那么就读取它 还要注意与旧版本兼容
	 * @date 2012.4.16
	 */
	public void loadPeferGain() {
		Preferences pref = Preferences.userNodeForPackage(MainFrame.class);
		int powa1 = 0; //和成员变量区分开
		int gain1 = 0;
		try {
			PatientsService pntService = PatientsService.getInstance();
			Patients patient = pntService.selectByPrimaryKey(String.valueOf(patientID));
			//...得到powa 和gain
			powa1 = Integer.parseInt(patient.getPowerA());
			gain1 = Integer.parseInt(patient.getGain());
		} catch (SQLException e) {
			e.printStackTrace();
			//有错表明没有这两个字段 或者其他出错 则设置它为初始值
			powa1 = 10;
			gain1 = 0;
		}
		pref.putInt("powa", powa1);
		pref.putInt("gain", gain1);
	}

	/**
	 * 将当前录制使用的powa和gain存入到数据库的患者表中 更新这两个字段的数据
	 */
	public void putPeferGain() {
		System.out.println("powa:" + powa + "," + "gain:" + gain);
		try {
			PatientsService pntService = PatientsService.getInstance();
			Patients patient = pntService.selectByPrimaryKey(String.valueOf(patientID));
			//将powa和gain设置到患者表中的这一条记录
			patient.setPowerA(powa + "");
			patient.setGain(gain + "");
			pntService.updateByPrimaryKey(patient);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String getPatientName() {
		return patientName;
	}

	public int getPatientID() {
		return patientID;
	}

	public void setPeriod(float period) {
		this.period = period;
	}

	public void setPowa(int powa) {
		this.powa = powa;
	}

	public void setGain(int gain) {
		this.gain = gain;
	}

	public int getPowa() {
		return powa;
	}

	public int getGain() {
		return gain;
	}

}
