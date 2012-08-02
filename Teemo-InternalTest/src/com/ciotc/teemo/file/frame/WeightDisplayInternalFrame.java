package com.ciotc.teemo.file.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import com.ciotc.teemo.file.template.RealtimeFileTemplate;
import com.ciotc.teemo.file.template.RealtimeFileTemplate.Status;
import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.frame.MainTabbedPane;
import com.ciotc.teemo.weight.FCalculator;
import com.ciotc.teemo.weight.RFFuntion;
import com.ciotc.teemo.weight.RFRelation;

public class WeightDisplayInternalFrame extends JInternalFrame implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RealtimeFileTemplate rft;
	JLabel inputText;
	JLabel text;

	public WeightDisplayInternalFrame(RealtimeFileTemplate rft) {
		this();
		this.rft = rft;
		rft.addPropertyChangeListener(this);
	}

	public WeightDisplayInternalFrame() {
		super("WeightDisplay", true, true, false, true);

		getContentPane().add(createFirstPanel(), BorderLayout.NORTH);
		getContentPane().add(createMainPanel());
		getContentPane().add(createSecondPanel(), BorderLayout.EAST);

		pack();
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setVisible(true);
	}

	private Component createFirstPanel() {
		inputText = new JLabel("input:                             ");
		inputText.setFont(new Font(null, Font.PLAIN, 14));
		return inputText;
	}

	private Component createSecondPanel() {
		JLabel label = new JLabel("KG");
		label.setFont(new Font(null, Font.BOLD, 20));
		return label;
	}

	private Component createMainPanel() {
		text = new JLabel("              ");
		text.setFont(new Font(null, Font.PLAIN, 20));
		return text;
	}

	//@date 2012.6.14
	//Map<Double, Double> rfMap = RFRelation.readRFMapFromMatlab();
	//FCalculator fc = new FCalculator(rfMap);

	public void displayWeight(byte[] data, int powa) {
		double weight = 0;
		int maxCount = 0;
		int maxWeight = 0;
		List<Integer> values = new ArrayList<Integer>();
		for (byte bt : data) {
			int in = bt & 0xff;
			if (in != 0) {
				maxCount++;
				maxWeight += in;
				values.add(in);
			}
		}
		System.out.println();
		System.out.print("value:" + values + " powa:" + powa + "  ");
		//@date 2012.6.14
		//if (rfMap != null) {
			weight = RFFuntion.calcF(values, powa);//fc.calculateF(values, powa);
			weight = weight / 9.8;
		//}

//之前用神经网络的做法
//		InputStream is = ClassLoader.getSystemResourceAsStream("com/ciotc/teemo/neuralnetwork/weight/network.nnet");
//		NeuralNetwork network = NeuralNetwork.load(is);
//
//		//System.out.print("count:"+maxCount+",weight:"+maxWeight+"       ");
//		
//		double[] ds = new double[2];
//
//
//		ds[0] = maxCount * 1.0f / MAX_COUNT;
//		ds[1] = maxWeight * 1.0f / MAX_VALUE;
//
//		network.setInput(ds);
//		network.calculate();
//		double[] output = network.getOutput();
//		//System.out.println(output[0]*MAX_FORCE);
//		weight = output[0]*MAX_FORCE;

		inputText.setText(String.format("count:%-5d value:%7d", maxCount, maxWeight));

		text.setText(String.format("%.5f", weight));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MainTabbedPane.RECORD_STSTUS)) {
			Status status = (Status) evt.getNewValue();
			switch (status) {
			case END:
				setVisible(false);
				break;
			default:
				setVisible(true);
				break;
			}
		}
	}

}
