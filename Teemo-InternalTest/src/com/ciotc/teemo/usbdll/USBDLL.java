package com.ciotc.teemo.usbdll;

/**
 * 此类还不是最终版，最终版会把dll在控制台打印的话去掉，并去掉test方法(已去掉)
 * @author 林晓智
 *
 */
public class USBDLL {

	static {
		//System.out.println(System.getProperty("user.dir"));
		//System.loadLibrary("lib.USB");
		//加载dll
		String path = System.getProperty("user.dir");
		System.load(path + "\\lib\\usb_top.dll");
		System.load(path + "\\lib\\USB.dll");
		//System.loadLibrary("USB");
	}

	//public static native void test();

	/**
	 * 
	 * @return 与电脑连接的USB设备数目
	 */
	/* 1 */public static native int getDevicesNum();

	/**
	 * 得到相应USB索引端口的序列号
	 * 
	 * @param deviceIndex
	 *                索引编号，若GetDevicesNum()得到的时N，则N个USB设备的编号为0,1,2,3,,,,N
	 * @return USB索引端口的序列号
	 */
	/* 2 */public static native String getSerialNumberSingle(int deviceIndex);

	/**
	 * 打开索引号为dwDeviceIndex的USB设备<br>
	 * 当有多个设备时，可以调用Open来切换操作的USB设备，但只有一个USB设备能运行，不能同时运行多个USB设备
	 * dwDeviceIndex：索引编号，若GetDevicesNum()得到的时N，则N个USB设备的编号为0,1,2,3,,,,N
	 * 
	 * @param deviceIndex 索引编号
	 * @return 返回是否打开成功
	 */
	/* 3 */public static native boolean open(int deviceIndex);

	/**
	 * 关闭USB设备
	 */
	/* 4 */public static native void close();

	/**
	 * 获得按键1的状态
	 * 
	 * @return 0 ------ 获得按键1的状态失败,获得按键1的状态成功但得不到按键打开和关闭状态时也返回0<br>
	 *         1 ------ 获得按键1的状态成功，按键打开<br>
	 *         2 ------ 获得按键1的状态成功，按键关闭
	 */
	/* 5 */public static native int getButton1Info();

	/**
	 * 获得按键2的状态
	 * 
	 * @return 0 ------ 获得按键2的状态失败,获得按键2的状态成功但得不到按键打开和关闭状态时也返回0<br>
	 *         1 ------ 获得按键2的状态成功，按键打开<br>
	 *         2 ------ 获得按键2的状态成功，按键关闭
	 */
	/* 6 */public static native int getButton2Info();

	/**
	 * 清除按键1状态
	 * @return 清除按键1状态是否成功,操作不成功也返回失败
	 */
	/* 7 */public static native boolean clearButton1Info();

	/**
	 * 清除按键2状态
	 * @return 清除按键2状态是否成功,操作不成功也返回失败
	 */
	/* 8 */public static native boolean clearButton2Info();

	/**
	 * 采集2288个点的值
	 * @return 返回采集的字节数组,失败时返回全0的字节数组
	 */
	/* 9 */public static native byte[] collectFrame();

	/**
	 * 检测传感器是否连接
	 * @return 返回是否检测成功.获得检测状态失败也返回检测失败.
	 */
	/* 10 */public static native boolean senseLink();

	/**
	 * 使按键1置为开状态
	 * @return 设置是否成功,操作不成功也返回失败
	 */
	/*11*/public static native boolean setButton1On();

	/**
	 * 使按键2置为开状态 
	 * @return 设置是否成功,操作不成功也返回失败
	 */
	/*12*/public static native boolean setButton2On();

	/**
	 * 调节增溢方法1 以后可能需要与14方法合并 
	 * 注：13 14方法只有在采集时才生效
	 * @param powA
	 */
	/*13*/public static native void setPowA(int powA);

	/**
	 * 调节增溢方法2 
	 * @param gain
	 */
	/*14*/public static native void setGain(int gain);

	/**
	 * 调节AD的参考电压
	 * 缺省值应该设定成128
	 * @param powD
	 */
	public static native void setPowD(int powD);
	
	/**
	 * gain索引表 通过该表获得实际的gain值<br>具体值如下:<br>
	 * 0 --> 1<br>  1 --> 2 <br> 2-->4 <br>
	 * 3-->5 <br>4-->8 <br>5-->10 <br>6-->32 <br>
	 */
	public final static int[] GAINS = { 1, 2, 4, 5, 8, 10, 16, 32 };

	
	
	public static void main(String[] args) throws InterruptedException {		
		//test();
		int deviceIndex = getDevicesNum();
		System.out.println("getDevicesNum:" + deviceIndex);
		if (deviceIndex == 0)
			return;
		System.out.println("getSerialNumberSingle:" + getSerialNumberSingle(deviceIndex - 1));
		System.out.println("open:" + open(deviceIndex - 1));
		System.out.println("open:" + open(deviceIndex - 1));
		System.out.println("open:" + open(deviceIndex - 1));
		System.out.println("open:" + open(deviceIndex - 1));
		
		//Thread.sleep(10000);
		System.out.println("clearButton1Info:" + clearButton1Info());
		System.out.println("clearButton2Info:" + clearButton2Info());
		System.out.println("getButton1Info:" + getButton1Info());
		System.out.println("getButton2Info:" + getButton2Info());
		System.out.println("setButton1On:" + setButton1On());
		System.out.println("setButton2On:" + setButton2On());
		System.out.println("getButton1Info:" + getButton1Info());
		System.out.println("getButton2Info:" + getButton2Info());
		
		
//		setGain(16);
		//setGain(6);
		//setPowA(GAINS[4]);
		setPowA(6);
		setGain(0);
		setPowD(128);
		for(int t = 0;t<3;t++){
			byte[] tempData = collectFrame();
			//System.out.println("collectFrame:" + collectFrame());
	//		for(int i = 0;i<2288;i++)
	//			System.out.print(tempData[i]);
	
			System.out.println("-----------------------------------------");
			//System.out.println(tempData.length);
			for(int i =0;i<52;i++){
				//System.out.println("J"+i);
				for(int j = 0;j<44;j++)
					//System.out.print("J"+j+"="+tempData[i*44+j]+" ");
					System.out.printf("%4d",tempData[i*44+j]& 0xff);
				System.out.println();
			}
			System.out.println();
			
			Thread.sleep(300);
		}
		System.out.println("--------another collcet frame--------");
//		setGain(16);
//		setGain(6);
//		setPowA(GAINS[0]);
		setPowA(6);
		setGain(4);
		
		for(int t = 0;t<3;t++){
			byte[] tempData = collectFrame();
			//System.out.println("collectFrame:" + collectFrame());
	//		for(int i = 0;i<2288;i++)
	//			System.out.print(tempData[i]);
	
			System.out.println("-----------------------------------------");
			//System.out.println(tempData.length);
			for(int i =0;i<52;i++){
				//System.out.println("J"+i);
				for(int j = 0;j<44;j++)
					//System.out.print("J"+j+"="+tempData[i*44+j]+" ");
					System.out.printf("%4d",tempData[i*44+j] & 0xff);
				System.out.println();
			}
			System.out.println();
			
			Thread.sleep(300);
		}
		
		
		System.out.println("senseLink:" + senseLink());
		System.out.println("close:");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
	}
}
