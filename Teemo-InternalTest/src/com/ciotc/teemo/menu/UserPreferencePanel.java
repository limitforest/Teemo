package com.ciotc.teemo.menu;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.update.Check4Update;
import com.ciotc.teemo.util.GeneralUtils;

public class UserPreferencePanel extends JTabbedPane {
	public static final String FRAME_OR_PERIOD = "FrameOrPeriod";

	public static final String LEFT_CONSTANT = "left";

	public static final String TOP_CONSTANT = "top";

	public static final String THRESHOLD_CONSTANT = "threshold";

	public static final String MAX_OPTION = "maxOption";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class COFPreferencePanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public COFPreferencePanel() {
			super();
			GroupLayout layout = new GroupLayout(this);
			/* getContentPane(). */setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);

			// 1box
			float min = 1, max = 100/*, def = oldThreshold*/;
			Box box = Box.createHorizontalBox();
			TitledBorder title = BorderFactory.createTitledBorder(getResourceString("COFPreferencePanel.title1"));
			JLabel jl1 = new JLabel(getResourceString("COFPreferencePanel.threshold"));
			box.add(jl1);
			jl1 = new JLabel("(" + min + "%—" + max + "%) ");
			box.add(jl1);
			COFPreferencePanelThresholdField = new JTextField(8);

			COFPreferencePanelThresholdField.setMaximumSize(new Dimension(54, 21));
			COFPreferencePanelThresholdField.setInputVerifier(new MyInputVerifierFloat(min, max));
			box.add(COFPreferencePanelThresholdField);
			jl1 = new JLabel(" %");
			box.add(jl1);
			box.setBorder(title);

			// 2box
			Box box2 = Box.createVerticalBox();
			title = BorderFactory.createTitledBorder(getResourceString("COFPreferencePanel.title2"));
			box2.setBorder(title);

			// 2.1 box
			min = 14.27f;
			max = 40.34f;
			Box b = Box.createHorizontalBox();
			JLabel jl2 = new JLabel(getResourceString("COFPreferencePanel.top"));
			b.add(jl2);
			jl2 = new JLabel("(" + min + "—" + max + ") ");
			b.add(jl2);
			COFPreferencePanelTopField = new JTextField(8);

			COFPreferencePanelTopField.setMaximumSize(new Dimension(54, 21));
			COFPreferencePanelTopField.setInputVerifier(new MyInputVerifierFloat(min, max));
			b.add(COFPreferencePanelTopField);
			jl2 = new JLabel(" mm");
			b.add(jl2);
			box2.add(b);

			// 2.2 box
			min = 9.27f;
			max = 65.66f;
			b = Box.createHorizontalBox();
			jl2 = new JLabel(getResourceString("COFPreferencePanel.left"));
			b.add(jl2);
			jl2 = new JLabel("(" + min + "—" + max + ") ");
			b.add(jl2);
			COFPreferencePanelLeftField = new JTextField(8);

			COFPreferencePanelLeftField.setMaximumSize(new Dimension(54, 21));
			COFPreferencePanelLeftField.setInputVerifier(new MyInputVerifierFloat(min, max));
			b.add(COFPreferencePanelLeftField);
			jl2 = new JLabel(" mm");
			b.add(jl2);
			box2.add(b);

			layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(box)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(box2)));

			layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(box).addComponent(box2)));

		}

	}

	class MaxOptionPanel extends JPanel implements ActionListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ButtonGroup group;

		private MaxOptionPanel() {
			super();

			JPanel tabControls = new JPanel();
			TitledBorder title;
			title = BorderFactory.createTitledBorder(getResourceString("MaxOptionPanel.label"));
			tabControls.setBorder(title);

			// tabControls.add(new
			// JLabel(getResourceString("MaxOptionPanel.label")));
			maxOptionPanelselect1 = (JRadioButton) tabControls.add(new JRadioButton(getResourceString("MaxOptionPanel.select1")));
			maxOptionPanelselect2 = (JRadioButton) tabControls.add(new JRadioButton(getResourceString("MaxOptionPanel.select2")));

			add(tabControls, BorderLayout.CENTER);

			group = new ButtonGroup();
			group.add(maxOptionPanelselect1);
			group.add(maxOptionPanelselect2);

			maxOptionPanelselect1.addActionListener(this);
			maxOptionPanelselect2.addActionListener(this);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//maxOptionPanelOldValue = maxOptionPanelValue;
			if (e.getSource() == maxOptionPanelselect1) {
				maxOptionPanelValue = 1;
			}
			if (e.getSource() == maxOptionPanelselect2) {
				maxOptionPanelValue = 2;
			}

		}

	}

	class MovieStatusPanel extends JPanel implements ActionListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ButtonGroup group;

		private MovieStatusPanel() {
			super();

			JPanel tabControls = new JPanel();
			TitledBorder title;
			title = BorderFactory.createTitledBorder(getResourceString("MovieStatusPanel.label"));
			tabControls.setBorder(title);

			// tabControls.add(new
			// JLabel(getResourceString("MovieStatusPanel.label")));
			movieStatusPanelselect1 = (JRadioButton) tabControls.add(new JRadioButton(getResourceString("MovieStatusPanel.select1")));
			movieStatusPanelselect2 = (JRadioButton) tabControls.add(new JRadioButton(getResourceString("MovieStatusPanel.select2")));

			// add(tabControls, BorderLayout.CENTER);
			add(tabControls);
			group = new ButtonGroup();
			group.add(movieStatusPanelselect1);
			group.add(movieStatusPanelselect2);

			movieStatusPanelselect1.addActionListener(this);
			movieStatusPanelselect2.addActionListener(this);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//movieStatusPanelOldValue = movieStatusPanelValue;
			if (e.getSource() == movieStatusPanelselect1) {
				movieStatusPanelValue = 1;
			}
			if (e.getSource() == movieStatusPanelselect2) {
				movieStatusPanelValue = 2;
			}

		}

	}

	class MyInputVerifierFloat extends InputVerifier {

		float maxValue;
		float minValue;

		private MyInputVerifierFloat(float minValue, float maxValue) {
			super();
			this.maxValue = maxValue;
			this.minValue = minValue;
		}

		@Override
		public boolean verify(JComponent input) {
			JTextField jtf = (JTextField) input;
			try {
				float f = Float.parseFloat(jtf.getText());
				if (f < minValue || f > maxValue) {
					showTipInformation(minValue, maxValue);
					return false;
				}
			} catch (Exception e) {
				showTipInformation();
				return false;
			}
			return true;
		}

	}

	class ShowUpdatePanel extends JPanel implements ItemListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JLabel tips;
		private JLabel httpTips;
		private JPanel panel;
		
		public ShowUpdatePanel() {
			showUpdateCheckBox = new javax.swing.JCheckBox();
			showUpdateCheckBox.addItemListener(this);
			tips = new javax.swing.JLabel();

			setLayout(null);
			tips.setText(getResourceString("Check4Update.tips"));
			add(tips);
			tips.setBounds(20, 10, 380, 80);

			showUpdateCheckBox.setText(getResourceString("Check4Update.checkbox"));
			add(showUpdateCheckBox);
			showUpdateCheckBox.setBounds(20, 103, 380, 23);

			panel = new JPanel(new BorderLayout());
			JPanel jp = new JPanel(new BorderLayout());
			httpTips = new JLabel(getResourceString("Check4Update.httpTips"));
			jp.add(httpTips,BorderLayout.WEST);
			
			httpURLTextField = new JTextField(30);
			jp.add(httpURLTextField,BorderLayout.SOUTH);
			panel.add(jp,BorderLayout.WEST);
			add(panel);
			panel.setBounds(20, 130, 380, 70);
			
			
			httpTips.setVisible(false);
			httpURLTextField.setVisible(false);
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();
			if (source == showUpdateCheckBox) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					showUpdate = false;
				else
					showUpdate = true;

			}

		}

	}

	//private JButton cancelButton;
	JCheckBox showUpdateCheckBox = null;
	JTextField httpURLTextField;
	JTextField COFPreferencePanelLeftField = null;

	JTextField COFPreferencePanelThresholdField = null;

	JTextField COFPreferencePanelTopField = null;
	//private JButton defaultButton;
	//private JButton applyButton;
	//MainFrame mainFrame = null;
	JPanel mainPanel = null;
	JRadioButton maxOptionPanelselect1; // select 1 to MA
	JRadioButton maxOptionPanelselect2; // select 1 to end
	int maxOptionPanelValue = 1;
	int maxOptionPanelOldValue = 1;
	JRadioButton movieStatusPanelselect1; //
	JRadioButton movieStatusPanelselect2; //
	int movieStatusPanelValue = 1;
	int movieStatusPanelOldValue = 1;
	//private JButton okButton;
	Preferences preferences;
	JTabbedPane tabbedpane = null;
	float threshold;
	float oldThreshold = 10;
	float top;
	float oldTop = 31.025f;
	float left;
	float oldLeft = 33.020f;

	boolean showUpdate;
	String httpURL;
	UserPreferencePanel() {
		//super(parent);
		//this.mainFrame = parent;

		preferences = Preferences.userNodeForPackage(MainFrame.class);

		//setTitle(getResourceString("userPreferenceDialog.title"));

		//setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		//setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

		add(getResourceString("MaxOptionPanel.title"), new MaxOptionPanel());
		add(getResourceString("MovieStatusPanel.title"), new MovieStatusPanel());
		add(getResourceString("COFPreferencePanel.title"), new COFPreferencePanel());
		add(getResourceString("Check4Update.title"), new ShowUpdatePanel());

		load();

		//tabbedpane.add("test3", new JPanel());
		// mainPanel = new JPanel();
		// mainPanel.add(tabbedpane, BorderLayout.CENTER);
		//add(tabbedpane);

//		JPanel buttonPane = new JPanel();
//		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
//		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		buttonPane.add(Box.createHorizontalGlue());
//		cancelButton = new JButton(getResourceString("cancel"));
//		okButton = new JButton(getResourceString("ok"));
//		defaultButton = new JButton(getResourceString("default"));
//		applyButton = new JButton(getResourceString("apply"));
//		buttonPane.add(okButton);
//		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
//		buttonPane.add(cancelButton);
//		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
//		buttonPane.add(applyButton);
//		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
//		buttonPane.add(defaultButton);
//		add(buttonPane, BorderLayout.SOUTH);
//
//		okButton.addActionListener(this);
//		cancelButton.addActionListener(this);
//		defaultButton.addActionListener(this);
//		applyButton.addActionListener(this);
		//setAlwaysOnTop(true);
		//setSize(500, 300);
		//setLocationRelativeTo(parent);
		//setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}

	public void load() {
		// 1.COFPreferencePanel
		oldThreshold = preferences.getFloat(THRESHOLD_CONSTANT, THREASHOLD);
		oldTop = preferences.getFloat(TOP_CONSTANT, TOP);
		oldLeft = preferences.getFloat(LEFT_CONSTANT, LEFT);
		COFPreferencePanelThresholdField.setText(String.valueOf(oldThreshold));
		COFPreferencePanelTopField.setText(String.valueOf(oldTop));
		COFPreferencePanelLeftField.setText(String.valueOf(oldLeft));

		// 2.MovieStatusPanel
		movieStatusPanelValue = preferences.getInt(FRAME_OR_PERIOD, MOVIE_STATUS_PANE_LVALUE);
		if (movieStatusPanelValue == 1)
			movieStatusPanelselect1.setSelected(true);
		else
			movieStatusPanelselect2.setSelected(true);

		// 3.maxOptionPanel
		maxOptionPanelValue = preferences.getInt(MAX_OPTION, MAX_OPTION_PANE_LVALUE);
		if (maxOptionPanelValue == 1)
			maxOptionPanelselect1.setSelected(true);
		else
			maxOptionPanelselect2.setSelected(true);
		
		// 4.ShowUpdatePanel
		showUpdate = GeneralUtils.getProp().getString(Check4Update.SHOW_UPDATE).equals("true") ? true : false;
		showUpdateCheckBox.setSelected(!showUpdate);
		try{
		String temp = GeneralUtils.getProp().getString(Check4Update.HTTP_URL);
		//httpURL = temp.substring(0,temp.lastIndexOf(HTTP_URL_SUFFIX));
		httpURL = temp;
		}catch(Exception e){
			e.printStackTrace();
			httpURL = HTTP_URL;
		}
		httpURLTextField.setText(httpURL);
	}

	public void apply() {
		// 依次取出每个设置变量（取出的数一定是正确的） 存入preference 更改变量值
		// 1.COFPreferencePanel

		threshold = Float.parseFloat(COFPreferencePanelThresholdField.getText());
		oldThreshold = preferences.getFloat(THRESHOLD_CONSTANT, THREASHOLD);
		preferences.putFloat(THRESHOLD_CONSTANT, threshold);
		UserPreferencePanel.this.firePropertyChange(THRESHOLD_CONSTANT, oldThreshold, threshold);

		top = Float.parseFloat(COFPreferencePanelTopField.getText());
		oldTop = preferences.getFloat(TOP_CONSTANT, TOP);
		preferences.putFloat(TOP_CONSTANT, top);
		UserPreferencePanel.this.firePropertyChange(TOP_CONSTANT, oldTop, top);

		left = Float.parseFloat(COFPreferencePanelLeftField.getText());
		oldLeft = preferences.getFloat(LEFT_CONSTANT, LEFT);
		preferences.putFloat(LEFT_CONSTANT, left);
		UserPreferencePanel.this.firePropertyChange(LEFT_CONSTANT, oldLeft, left);
		//System.out.println(threshold+","+top+","+left);
		// 2.MovieStatusPanel
		movieStatusPanelOldValue = preferences.getInt(FRAME_OR_PERIOD, MOVIE_STATUS_PANE_LVALUE);
		preferences.putInt(FRAME_OR_PERIOD, movieStatusPanelValue);
		UserPreferencePanel.this.firePropertyChange(FRAME_OR_PERIOD, movieStatusPanelOldValue, movieStatusPanelValue);

		// 3.maxOptionPanel
		maxOptionPanelOldValue = preferences.getInt(MAX_OPTION, MAX_OPTION_PANE_LVALUE);
		preferences.putInt(MAX_OPTION, maxOptionPanelValue);
		UserPreferencePanel.this.firePropertyChange(MAX_OPTION, maxOptionPanelOldValue, maxOptionPanelValue);
		
		// 4.ShowUpdatePanel
		GeneralUtils.getProp().setProperty(Check4Update.SHOW_UPDATE, showUpdate==true?"true":"false");
		httpURL = httpURLTextField.getText();
		GeneralUtils.getProp().setProperty(Check4Update.HTTP_URL,httpURL/*+HTTP_URL_SUFFIX*/);
	}

	private final static float THREASHOLD = 10.0f;
	private final static float TOP = 31.025f;
	private final static float LEFT = 33.02f;
	private final static int MOVIE_STATUS_PANE_LVALUE = 1;
	private final static int MAX_OPTION_PANE_LVALUE = 1;
	private final static boolean SHOW_UPDATE = true;
	private final static String HTTP_URL = "http://www.myteemo.com/updates/teemo1/updates.xml";//"http://192.168.110.50";
	//private final static String HTTP_URL_SUFFIX = "/teemo/updates.xml";
	public void setDefault() {
		// 1.COFPreferencePanel
		oldThreshold = THREASHOLD;
		oldTop = TOP;
		oldLeft = LEFT;
		COFPreferencePanelThresholdField.setText(oldThreshold + "");
		COFPreferencePanelTopField.setText(oldTop + "");
		COFPreferencePanelLeftField.setText(oldLeft + "");

		// 2.MovieStatusPanel
		movieStatusPanelValue = MOVIE_STATUS_PANE_LVALUE;
		if (movieStatusPanelValue == 1)
			movieStatusPanelselect1.setSelected(true);
		else
			movieStatusPanelselect2.setSelected(true);

		// 3.maxOptionPanel
		maxOptionPanelValue = MAX_OPTION_PANE_LVALUE;
		if (maxOptionPanelValue == 1)
			maxOptionPanelselect1.setSelected(true);
		else
			maxOptionPanelselect2.setSelected(true);
		
		// 4.ShowUpdatePanel
		showUpdate = SHOW_UPDATE;
		showUpdateCheckBox.setSelected(!showUpdate);
		httpURL = HTTP_URL;
		httpURLTextField.setText(httpURL);
	}

//	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == okButton) {
//			// 依次取出每个设置变量（取出的数一定是正确的） 存入preference 更改变量值
//			// 1.COFPreferencePanel
//
//			threshold = Float.parseFloat(COFPreferencePanelThresholdField.getText());
//			oldThreshold = preferences.getFloat("threshold", threshold);
//			preferences.putFloat("threshold", threshold);
//			UserPreferencePanel.this.firePropertyChange("threshold", oldThreshold, threshold);
//
//			top = Float.parseFloat(COFPreferencePanelTopField.getText());
//			oldTop = preferences.getFloat("top", top);
//			preferences.putFloat("top", top);
//			UserPreferencePanel.this.firePropertyChange("top", oldTop, top);
//
//			left = Float.parseFloat(COFPreferencePanelLeftField.getText());
//			oldLeft = preferences.getFloat("left", left);
//			preferences.putFloat("left", left);
//			UserPreferencePanel.this.firePropertyChange("left", oldLeft, left);
//
//			// 2.MovieStatusPanel
//			movieStatusPanelOldValue = preferences.getInt("FrameOrPeriod", movieStatusPanelValue);
//			preferences.putInt("FrameOrPeriod", movieStatusPanelValue);
//			UserPreferencePanel.this.firePropertyChange("FrameOrPeriod", movieStatusPanelOldValue, movieStatusPanelValue);
//
//			// 3.maxOptionPanel
//			maxOptionPanelOldValue = preferences.getInt("maxOption", maxOptionPanelValue);
//			preferences.putInt("maxOption", maxOptionPanelValue);
//			UserPreferencePanel.this.firePropertyChange("maxOption", maxOptionPanelOldValue, maxOptionPanelValue);
//
//			//dispose();
//		}
//		if (e.getSource() == cancelButton) {
//			// 还原原来的值
//			// 1.COFPreferencePanel
//			oldThreshold = preferences.getFloat("threshold", threshold);
//			COFPreferencePanelThresholdField.setText(String.valueOf(oldThreshold));
//			oldTop = preferences.getFloat("top", top);
//			COFPreferencePanelTopField.setText(String.valueOf(oldTop));
//			oldLeft = preferences.getFloat("left", left);
//			COFPreferencePanelLeftField.setText(String.valueOf(oldLeft));
//
//			// 2.MaxOptionPanel
//			int k = preferences.getInt("FrameOrPeriod", movieStatusPanelValue);
//			if (k == 1)
//				maxOptionPanelselect1.setSelected(true);
//			else
//				maxOptionPanelselect2.setSelected(true);
//
//			// 3.MovieStatusPanel
//			k = preferences.getInt("maxOption", maxOptionPanelValue);
//			if (k == 1)
//				movieStatusPanelselect1.setSelected(true);
//			else
//				movieStatusPanelselect2.setSelected(true);
//			//dispose();
//		}
//		if (e.getSource() == defaultButton) {
//			// 1.COFPreferencePanel
//			COFPreferencePanelThresholdField.setText("10");
//			COFPreferencePanelTopField.setText("31.025");
//			COFPreferencePanelLeftField.setText("33.020");
//
//			// 2.MaxOptionPanel
//			maxOptionPanelselect1.setSelected(true);
//			maxOptionPanelValue = 1;
//			// 3.MovieStatusPanel
//			movieStatusPanelselect1.setSelected(true);
//			movieStatusPanelValue = 1;
//		}
//		if (e.getSource() == applyButton) {
//			//和ok的一样只不过是没有将其释放
//
//			// 依次取出每个设置变量（取出的数一定是正确的） 存入preference 更改变量值
//			// 1.COFPreferencePanel
//
//			threshold = Float.parseFloat(COFPreferencePanelThresholdField.getText());
//			oldThreshold = preferences.getFloat("threshold", threshold);
//			preferences.putFloat("threshold", threshold);
//			UserPreferencePanel.this.firePropertyChange("threshold", oldThreshold, threshold);
//
//			top = Float.parseFloat(COFPreferencePanelTopField.getText());
//			oldTop = preferences.getFloat("top", top);
//			preferences.putFloat("top", top);
//			UserPreferencePanel.this.firePropertyChange("top", oldTop, top);
//
//			left = Float.parseFloat(COFPreferencePanelLeftField.getText());
//			oldLeft = preferences.getFloat("left", left);
//			preferences.putFloat("left", left);
//			UserPreferencePanel.this.firePropertyChange("left", oldLeft, left);
//
//			// 2.MovieStatusPanel
//			movieStatusPanelOldValue = preferences.getInt("FrameOrPeriod", movieStatusPanelValue);
//			preferences.putInt("FrameOrPeriod", movieStatusPanelValue);
//			UserPreferencePanel.this.firePropertyChange("FrameOrPeriod", movieStatusPanelOldValue, movieStatusPanelValue);
//
//			// 3.maxOptionPanel
//			maxOptionPanelOldValue = preferences.getInt("maxOption", maxOptionPanelValue);
//			preferences.putInt("maxOption", maxOptionPanelValue);
//			UserPreferencePanel.this.firePropertyChange("maxOption", maxOptionPanelOldValue, maxOptionPanelValue);
//
//		}
//	}

	/**
	 * 提示输入的数据格式不正确
	 */
	private void showTipInformation() {
		JOptionPane.showMessageDialog(this, getResourceString("tipforErrorInputing"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
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
	 * 提示输入的数据不在最大和最小范围之内
	 * 
	 * @param maxValue
	 *                最大值
	 * @param minValue
	 *                最小值
	 */
	@SuppressWarnings("unused")
	private void showTipInformation(int minValue, int maxValue) {
		JOptionPane.showMessageDialog(this, getResourceString("tipforErrorInputing2") + "  " + minValue + "~" + maxValue, getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
	}

}
