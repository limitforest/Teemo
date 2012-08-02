package com.ciotc.teemo.menu;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.ButtonModel;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ciotc.teemo.frame.MainFrame;

public class RecordSettingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JButton advance;
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JRadioButton jRadioButton1;
	private javax.swing.JRadioButton jRadioButton2;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;

	//MainFrame parent = null;

	Preferences pref = Preferences.userNodeForPackage(MainFrame.class);

//	private int frames;
//	private float period;
//	private int selectionInSize;

//	private GainSettingDialog gsd = null;

	protected RecordSettingPanel() {
		//super(parent);
		//this.parent = parent;
		//add(createContent());

		initComponents();
	}

	private void initComponents() {
		buttonGroup1 = new javax.swing.ButtonGroup();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField(6);
		jTextField2 = new javax.swing.JTextField(6);
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		jRadioButton1 = new javax.swing.JRadioButton();
		jRadioButton2 = new javax.swing.JRadioButton();

		buttonGroup1.add(jRadioButton1);
		buttonGroup1.add(jRadioButton2);

		//ok = new javax.swing.JButton();
		//cancel = new javax.swing.JButton();
		advance = new javax.swing.JButton();

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(getResourceString("RecordSettingDialog.border1")));

		jLabel1.setText(getResourceString("RecordSettingDialog.title1"));

		jLabel2.setText(getResourceString("RecordSettingDialog.title2"));

		jLabel3.setText(getResourceString("RecordSettingDialog.explain1"));

		jLabel4.setText(getResourceString("RecordSettingDialog.explain2"));

		jPanel1.setLayout(new GridLayout(2, 1));
		JPanel jp = new JPanel(new BorderLayout());
		JPanel jp2 = new JPanel();
		jp2.add(jLabel1);
		jp2.add(jTextField1);
		jp2.add(jLabel3);
		jp.add(jp2,BorderLayout.WEST);
		jPanel1.add(jp);
		jp = new JPanel(new BorderLayout());
		jp2 = new JPanel();
		jp2.add(jLabel2);
		jp2.add(jTextField2);
		jp2.add(jLabel4);
		jp.add(jp2,BorderLayout.WEST);
		jPanel1.add(jp);
//		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
//		jPanel1.setLayout(jPanel1Layout);
//		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
//				jPanel1Layout.createSequentialGroup()
//						.addContainerGap()
//						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel2))
//						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTextField1).addComponent(jTextField2))
//						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel4).addGap(0, 0, Short.MAX_VALUE))
//								.addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
//		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
//				jPanel1Layout.createSequentialGroup()
//						.addContainerGap()
//						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//								.addComponent(jLabel3))
//						.addGap(18, 18, 18)
//						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2)
//								.addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4))
//						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(getResourceString("RecordSettingDialog.border2")));

		jRadioButton1.setText(getResourceString("RecordSettingDialog.select1"));

		jRadioButton2.setText(getResourceString("RecordSettingDialog.select2"));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel2Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jRadioButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jRadioButton2).addGap(54, 54, 54)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel2Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton1).addComponent(jRadioButton2))
						.addContainerGap()));

		advance.setText(getResourceString("advance"));
		advance.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new GainSettingDialog().setVisible(true);

			}
		});
//		ok.setText(getResourceString("ok"));
////		ok.setBounds(10, 10, 100, 30);
//		ok.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//		getInformationFromText();
//		saveInforamtion();
//				setVisible(false);
//			}
//
//			
//		});

//		cancel.setText(getResourceString("cancel"));
////		cancel.setBounds(10, 50, 100, 30);
//		cancel.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//将值还原
//				loadInformation();
//				setTextByInformation();	
//				setVisible(false);
//			}
//		});

//		advance.setBounds(10, 50, 100, 30);
//		advance.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//if (gsd == null) {
//					new GainSettingDialog().setVisible(true);
////				}
////				if (gsd != null && !gsd.isVisible()) {
////					gsd.loadGainSelectionInformation();
////					gsd.setRadioButtonBySelection();
////					gsd.setVisible(true);
////				}
//			}
//		});
		setLayout(null);
		add(jPanel1);
		jPanel1.setBounds(10, 10, 320, 104);
		add(jPanel2);
		jPanel2.setBounds(10, 124, 320, 60);
		add(advance);
		advance.setBounds(10, 190, 80, 30);

		//addActionListener();

		addInputVerifier();
		load();
		
		//loadInformation();
		//setTextByInformation();

		//pack();
		//setTitle(getResourceString("RecordSettingDialog.title"));
		//setAlwaysOnTop(true);
		//setLocationRelativeTo(null);
		//setVisible(true);
		//setResizable(false);
		//setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}

	/**
	 * 将model中的数据显示在view上
	 */
	public void load() {
		int frames = pref.getInt("recordFrame", FRAMES);
		float period = pref.getFloat("recordPeriod", PERIOD);
		int selectionInSize = pref.getInt("sensorSize", SELECTION_IN_SIZE);

		jTextField1.setText(frames + "");
		jTextField2.setText(period + "");
		switch (selectionInSize) {
		case 1:
			jRadioButton1.setSelected(true);
			break;
		case 2:
			jRadioButton2.setSelected(true);
			break;
		default:
			jRadioButton2.setSelected(true); //默认是小传感器
			break;
		}
	}

	/**
	 * 应用 即将view中的数据得到model中去
	 */
	public void apply() {
		int frames = Integer.parseInt(jTextField1.getText());
		float period = Float.parseFloat(jTextField2.getText());
		ButtonModel bm = buttonGroup1.getSelection();
		int selectionInSize = 2;
		if (bm == jRadioButton1.getModel()) {
			selectionInSize = 1;
		} else if (bm == jRadioButton2.getModel()) {
			selectionInSize = 2;

		}

		pref.putInt("recordFrame", frames);
		pref.putFloat("recordPeriod", period);
		pref.putInt("sensorSize", selectionInSize);
	}

	/**
	 * 默认值
	 */
	private final static int FRAMES = 1500;
	private final static float PERIOD= 0.3f;
	private final static int SELECTION_IN_SIZE = 2;
	
	/**
	 * 还原最原始的model值 并显示在view上
	 */
	public void setDefault() {
		int frames = FRAMES;
		float period = PERIOD;
		int selectionInSize = SELECTION_IN_SIZE;

		jTextField1.setText(frames + "");
		jTextField2.setText(period + "");
		switch (selectionInSize) {
		case 1:
			jRadioButton1.setSelected(true);
			break;
		case 2:
			jRadioButton2.setSelected(true);
			break;
		default:
			jRadioButton2.setSelected(true); //默认是小传感器
			break;
		}

	}

//	class MyActionListener implements ActionListener {
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			if (e.getActionCommand().equals("jrb1")) {
//				selectionInSize = 1;
//			} else if (e.getActionCommand().equals("jrb1")) {
//				selectionInSize = 2;
//			}
//		}
//
//	}

//	private void addActionListener() {
//		MyActionListener mal = new MyActionListener();
//		jRadioButton1.addActionListener(mal);
//		jRadioButton1.setActionCommand("jrb1");
//		jRadioButton2.addActionListener(mal);
//		jRadioButton2.setActionCommand("jrb2");
//	}

	class MyInputVerifier extends InputVerifier {

		@Override
		public boolean verify(JComponent input) {
			JTextField jtf = (JTextField) input;
			try {

				if (jtf.getName().equals("jtf1")) {
					int val = Integer.parseInt(jtf.getText().trim());
					if (val < 1 || val > 3000) {
						showTipInformation(1, 3000);
						return false;
					}
				} else if (jtf.getName().equals("jtf2")) {
					float val = Float.parseFloat(jtf.getText().trim());
					if (val < 0.1 || val > 10) {
						showTipInformation(0.1f, 10f);
						return false;
					}
				}
			} catch (Exception e) {
				showTipInformation();
				return false;
			}
			return true;
		}

	}

	private void addInputVerifier() {
		MyInputVerifier miv = new MyInputVerifier();
		jTextField1.setInputVerifier(miv);
		jTextField1.setName("jtf1");
		jTextField2.setInputVerifier(miv);
		jTextField2.setName("jtf2");
	}

//	/**
//	 * 将frame、period和sensorSize的值得到
//	 */
//	public void loadInformation() {
//		frames = pref.getInt("recordFrame", 1000);
//		period = pref.getFloat("recordPeriod", 0.3f);
//		selectionInSize = pref.getInt("sensorSize", 1);
//	}
//
//	/**
//	 * 将frame、period和sensorSize的值再存回去
//	 */
//	private void saveInforamtion() {
//		pref.putInt("recordFrame", frames);
//		pref.putFloat("recordPeriod", period);
//		pref.putInt("sensorSize", selectionInSize);
//	}
//
//	/**
//	 * 根据文本框的内容得到frame period 的值
//	 * 注意：sensorSize的值是由监听函数得到的
//	 */
//	private void getInformationFromText() {
//		frames = Integer.parseInt(jTextField1.getText());
//		period = Float.parseFloat(jTextField2.getText());
//	}
//
//	/**
//	 * 根据取得的值 将其在界面上显示出来
//	 */
//	public void setTextByInformation() {
//		jTextField1.setText(frames + "");
//		jTextField2.setText(period + "");
//		switch (selectionInSize) {
//		case 1:
//			jRadioButton1.setSelected(true);
//			break;
//		case 2:
//			jRadioButton2.setSelected(true);
//			break;
//		default:
//			jRadioButton2.setSelected(true); //默认是小传感器
//			break;
//		}
//	}

	/**
	 * 提示输入的数据不在最大和最小范围之内
	 * 
	 * @param maxValue
	 *                最大值
	 * @param minValue
	 *                最小值
	 */
	private void showTipInformation(int minValue, int maxValue) {
		JOptionPane.showMessageDialog(this, getResourceString("tipforErrorInputing2") + "  " + minValue + "~" + maxValue, getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 提示输入的数据不在最大和最小范围之内
	 * 
	 * @param maxValue
	 *                最大值
	 * @param minValue
	 *                最小值
	 */
	private void showTipInformation(float minValue, float maxValue) {
		JOptionPane.showMessageDialog(this, getResourceString("tipforErrorInputing2") + "  " + minValue + "~" + maxValue, getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 提示输入的数据格式不正确
	 */
	private void showTipInformation() {
		JOptionPane.showMessageDialog(this, getResourceString("tipforErrorInputing"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
	}

}
