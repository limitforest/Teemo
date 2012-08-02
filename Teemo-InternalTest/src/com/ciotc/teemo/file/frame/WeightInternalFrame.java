package com.ciotc.teemo.file.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.ciotc.teemo.file.doc.RealtimeDoc.WeightInput;
import com.ciotc.teemo.file.template.RealtimeFileTemplate;
import com.ciotc.teemo.file.template.RealtimeFileTemplate.Status;
import com.ciotc.teemo.frame.MainTabbedPane;

public class WeightInternalFrame extends JInternalFrame implements ActionListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField textField;
	JButton set;
	RealtimeFileTemplate rft;
	JButton send;

	/**
	 * input
	 */
	WeightInput wi;
	/**
	 * output
	 */
	double force;
	JLabel countField1;
	JLabel valueField1;
	JLabel countField2;
	JLabel valueField2;

	public WeightInternalFrame(RealtimeFileTemplate rft) {
		this();
		this.rft = rft;
		rft.addPropertyChangeListener(this);
	}

	public WeightInternalFrame() {
		super("weight", true, true, false, true);
		getContentPane().add(createMainPanel());
		getContentPane().add(createSecondPanel(), BorderLayout.SOUTH);

		pack();
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
	}

	private Component createMainPanel() {
		JPanel panel = new JPanel();

		JLabel label = new JLabel("wight:");
		textField = new JTextField(20);
		set = new JButton("set");
		set.addActionListener(this);
		set.setActionCommand("set");
		send = new JButton("send");
		send.setActionCommand("send");
		send.addActionListener(this);

		panel.add(label);
		panel.add(textField);
		panel.add(set);
		panel.add(send);
		return panel;
	}

	private Component createSecondPanel() {
		JPanel jp = new JPanel(new GridLayout(2, 1));

		Panel panel = new Panel();

		JLabel wiLabel = new JLabel("input1-->");
		JLabel countLabel = new JLabel("count:");
		countField1 = new JLabel("    ");
		//countField1.setEditable(false);
		JLabel forceLabel = new JLabel("force:");
		valueField1 = new JLabel("    ");
		//valueField1.setEditable(false);

		panel.add(wiLabel);
		panel.add(countLabel);
		panel.add(countField1);
		panel.add(forceLabel);
		panel.add(valueField1);

		jp.add(panel);

		panel = new Panel();

		wiLabel = new JLabel("input2-->");
		countLabel = new JLabel("count:");
		countField2 = new JLabel("    ");
		//countField2.setEditable(false);
		forceLabel = new JLabel("force:");
		valueField2 = new JLabel("    ");
		//valueField2.setEditable(false);

		panel.add(wiLabel);
		panel.add(countLabel);
		panel.add(countField2);
		panel.add(forceLabel);
		panel.add(valueField2);

		jp.add(panel);

		return jp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("set")) {
			force = Double.parseDouble(textField.getText());
			wi = rft.rdoc.getWeightInput();

			countField1.setText(wi.getCount() + "");
			valueField1.setText(wi.getValue() + "");

			countField2.setText(wi.getMaxCount() + "");
			valueField2.setText(wi.getMaxValue() + "");

			revalidate();
			repaint();

		} else if (e.getActionCommand().equals("send")) {

			//将这些数据传送到服务器的一个指定位置
			Socket socket = null;
			try {
				socket = new Socket("127.0.0.1", 52341);

				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				DataInputStream dis = new DataInputStream(socket.getInputStream());

				dos.writeUTF("acceptData");

				if (dis.readUTF().equals("ok")) {
					dos.writeInt(wi.getCount());
					dos.writeInt(wi.getValue());
					dos.writeInt(wi.getMaxCount());
					dos.writeInt(wi.getMaxValue());
					dos.writeInt(wi.getPowa());
					dos.writeInt(wi.getGain());
					dos.writeDouble(force);

					//@date2012.6.13
					dos.writeInt(wi.getMaxPointValue());
					dos.writeInt(wi.getMinPointValue());
					dos.writeDouble(wi.getAvgPointValue());
					for (int in : wi.getValues()) {
						dos.writeInt(in);
					}
					dos.writeInt(Integer.MIN_VALUE);
				}

				dos.writeUTF("finish");
				if (dis.readUTF().equals("ok")) {
					dos.close();
					dis.close();
					socket.close();
				}

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(rft, "wrong" + e1.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(rft, "wrong" + e1.getMessage());
			} finally {
				if (socket != null && !socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MainTabbedPane.RECORD_STSTUS)) {
			Status status = (Status) evt.getNewValue();
			switch (status) {
			case STOP:
			case SAVED:
			case END:
				setVisible(true);
				set.setEnabled(true);
				break;
			default:
				setVisible(false);
				set.setEnabled(false);
				break;
			}
		}
	}
}
