package com.ciotc.teemo.weight;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * 得到R-F关系
 * @author Linxiaozhi
 *
 */
public class RFRelation {

	public static Map<Double, Double> readRFMapFromMatlab() {
		/**
		 * R-->F
		 */
		Map<Double, Double> rfMap = new TreeMap<Double, Double>();

		try {
			BufferedReader br = new BufferedReader(new FileReader("rf-matlab.txt"));
			br.readLine();
			String[] s0 = br.readLine().split("\\s");
			String[] s1 = br.readLine().split("\\s");

			br.close();
			double[] d0 = new double[s0.length];
			double[] d1 = new double[s1.length];

			for (int i = 0; i < s0.length; i++) {
				d0[i] = Double.parseDouble(s0[i]);
				d1[i] = Double.parseDouble(s1[i]);
			}

			for (int i = 0; i < s0.length; i++) {
				rfMap.put(d1[i], d0[i]);
			}
//			List<Double> rlists = new ArrayList<Double>(rfMap.keySet());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return rfMap;
	}

	public static final String RF_FILE_NAME = "rf-relation.dat";

	public static boolean storeRFMap() {
		Map<Double, Double> rfMap = readRFMapFromMatlab();
		try {
			ObjectOutputStream dos = new ObjectOutputStream(new FileOutputStream(RF_FILE_NAME));
			dos.writeObject(rfMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static Map<Double, Double> readRFMap() {
		Map<Double, Double> rfMap = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RF_FILE_NAME));
			rfMap = (Map<Double, Double>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return rfMap;
		} catch (IOException e) {
			return rfMap;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return rfMap;
		}
		return rfMap;
	}
}
