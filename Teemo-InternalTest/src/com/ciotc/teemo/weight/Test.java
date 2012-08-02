package com.ciotc.teemo.weight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Test {
	/**
	 * 输出r-f的值
	 */
	public static boolean OutputRF(String filePath) {
		Map<Double, Set<Double>> rfMap = new TreeMap<Double, Set<Double>>();

		File dir = new File(filePath);
		if (!dir.isDirectory())
			return false;

		for (File file : dir.listFiles()) {
			if (!file.getName().endsWith(".txt"))
				continue;

			try {
				BufferedReader br = new BufferedReader(new FileReader(file));

				br.readLine();

				String[] s1 = br.readLine().split("\\s");   //f
				String[] s0 = br.readLine().split("\\s");  //r

				br.close();
				double[] d0 = new double[s0.length];
				double[] d1 = new double[s1.length];

				for (int i = 0; i < s0.length; i++) {
					d0[i] = Double.parseDouble(s0[i]);
					d1[i] = Double.parseDouble(s1[i]);
				}

				for (int i = 0; i < s0.length; i++) {
					Set<Double> fset = rfMap.get(d0[i]);
					if (fset == null) {
						fset = new TreeSet<Double>();
						rfMap.put(d0[i], fset);
					}
					fset.add(d1[i]);
				}

				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

		}

		try {
			PrintWriter pw = new PrintWriter("d:\\rf.txt");
			pw.printf("%3s %10s %10s%n", "NO", "R", "F");
			int counter = 0;
			
			PrintWriter pw1 = new PrintWriter("d:\\rf1.txt");
			PrintWriter pw2 = new PrintWriter("d:\\rf2.txt");
			
			
			for (Double r : rfMap.keySet()) {
				for (Double f : rfMap.get(r)) {
					pw.printf("%3d %10.3f %10.3f%n", ++counter, r, f);
					pw1.printf("%-10.3f%n", r);
					pw2.printf("%-10.3f%n", f);
				}
			}
			pw.flush();
			pw1.flush();
			pw2.flush();
			pw.close();
			pw1.close();
			pw2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static void main(String[] args) {
		OutputRF("D:\\rf-matlab4");
	}
}
