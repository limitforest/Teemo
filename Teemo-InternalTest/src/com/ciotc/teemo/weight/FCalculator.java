package com.ciotc.teemo.weight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 计算力
 * @author Linxiaozhi
 *
 */
public class FCalculator {

	Map<Double, Double> rfMap = null;
	List<Double> rlists = null;

	public FCalculator(Map<Double, Double> rfMap) {
		super();
		this.rfMap = rfMap;
		rlists = new ArrayList<Double>(rfMap.keySet());
		System.out.println("rlists:"+rlists);
	}

	/**
	 * 
	 * @param values 所有点的值
	 * @param powa 驱动电压值
	 * @return double 力值
	 */
	public double calculateF(List<Integer> values, int powa) {
		double total = 0;
		for (Integer inv : values) {
			int index = 0;
			double r = calR(inv, powa);
			double f = 0;
			int pos = Collections.binarySearch(rlists, r);
			if (pos < 0) { //找不到
				index = -pos - 1;
System.out.print(" "+pos+" ");
				//针对index==-1
				if (index == 0) {
					double d = rlists.get(0);
					f = rfMap.get(d);

				} else if (index == rlists.size()) {
					double d = rlists.get(index-1);
					f = rfMap.get(d);
				} else {

					double r0 = rlists.get(index);
					double r1 = rlists.get(index - 1);
					double f0 = rfMap.get(r0);
					double f1 = rfMap.get(r1);

					double k = (f0 - f1) / (r0 - r1);
					double b = (f1 * r0 - f0 * r1) / (r0 - r1);

					f = k * r + b;
				}
			} else {
				r = rlists.get(index);
				f = rfMap.get(r);
			}
			total += f;
		}
		System.out.println();
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
