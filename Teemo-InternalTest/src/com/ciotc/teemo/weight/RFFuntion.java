package com.ciotc.teemo.weight;

import java.util.Collections;
import java.util.List;

/**
 * RF 函数
 * @author Linxiaozhi
 *
 */
public class RFFuntion {

	public static final double LOW = 200;
	public static final double HIGH = 400;

//	/**
//	 * <= 200
//	 * @param r
//	 * @return
//	 */
//	public static double fun1(double r) {
//		double a = 65.65;
//		double b = -0.09209;
//		double c = 10.97;
//		double d = -0.01037;
//		return a * Math.exp(b * r) + c * Math.exp(d * r);
//
//	}
//
//	/**
//	 * >200 and <=400
//	 * @param r
//	 * @return
//	 */
//	public static double fun2(double r) {
//		double a = 3.914;
//		double b = -0.003798;
//		return a * Math.exp(b * r);
//
//	}
//
//	/**
//	 * >400
//	 * @param r
//	 * @return
//	 */
//	public static double fun3(double r) {
//		double a = 2.362;
//		double b = -0.003623;
//		double c = 0.4777;
//		double d = -0.0004446;
//		return a * Math.exp(b * r) + c * Math.exp(d * r);
//
//	}
	
	/**
	 * 用橡皮测的 带缓冲 因此适用于柔软的 接触面积比较大的
	 * @param r
	 * @return
	 */
	public static double fun2(double r) {
		double a = 107.6;
		double b = -1.111;
		double c = 0.009206;
		return a * Math.pow(r, b) + c;
	}

	/**
	 * 适用于单点的情况 
	 * @param r
	 * @return
	 */
	public static double fun1(double r) {
		double a = 484.2;
		double b = -1.03;
		double c = 0.05838;
		return a * Math.pow(r, b) + c;
	}

	public static double calcF(double r) {
		//根据不同的压力类型选择不同的函数
		int selection = 1;
		switch (selection) {
		case 1:
			return fun1(r);
		case 2:
			return fun2(r);
		default:
			return fun1(r);
		}

	}

	public static double calcF(List<Integer> values, int powa) {
		double total = 0;
		for (Integer inv : values) {
			double r = calR(inv, powa);
			double f = 0;
			f = calcF(r);
			total += f;
		}
		return total;
	}

	/**
	 * 计算电阻
	 * @param value 0-255的数值
	 * @param powa 驱动电压
	 */
	private static double calR(int value, int powa) {
		return 100.0 * powa / value;
	}

}
