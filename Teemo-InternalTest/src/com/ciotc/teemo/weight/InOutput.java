package com.ciotc.teemo.weight;

import static com.ciotc.teemo.neuralnetwork.weight.Constant.INPUT_NUMS;
import static com.ciotc.teemo.neuralnetwork.weight.Constant.OUTPUT_NUMS;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class InOutput {
	int counter = 0;

	double[] inputs = new double[INPUT_NUMS];
	double[] outputs = new double[OUTPUT_NUMS];

	public class Input implements Comparable<Input> {
		double[] inputs = new double[INPUT_NUMS];
		List<Integer> values;
		int powa;

		public Input(double... is) {
			super();
			int i = 0;
			for (double d : is) {
				inputs[i++] = d;
			}
		}

		@Override
		public int compareTo(Input o) {
			return Double.compare(inputs[1], o.inputs[1]);
		}

		@Override
		public String toString() {
			return "Input [inputs=" + Arrays.toString(inputs) + ", values=" + values + ", powa=" + powa + "]";
		}

	}

	PriorityQueue<Input> queue = new PriorityQueue<Input>();

	@Override
	public String toString() {
		return "InOutput [counter=" + counter + ", inputs=" + Arrays.toString(inputs) + ", outputs=" + Arrays.toString(outputs) + ", queue=" + queue + "]";
	}

}