package com.ciotc.teemo.file.frame;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.usbdll.USBDLL;
import com.ciotc.teemo.util.GeneralUtils;

public class GainSettingInternalFrame extends JInternalFrame implements ChangeListener, ItemListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Preferences pref = Preferences.userNodeForPackage(MainFrame.class);

	@SuppressWarnings("rawtypes")
	private JComboBox jComboBox;
	private JSlider jSlider;
	private javax.swing.JLabel powaTitle;
	private javax.swing.JLabel gainTitle;
	private javax.swing.JLabel powaText;
	private javax.swing.JLabel gainText;
	private javax.swing.JLabel valueText;
	private javax.swing.JLabel valueTitle;

	private javax.swing.JRadioButton jRadioButton1;
	private javax.swing.JRadioButton jRadioButton2;
	private ButtonGroup buttonGroup;

	JToggleButton button;

	JLabel labelSelection;
	JLabel labelDown ;
	boolean isDownEnable ; //是否是真的不能
	JLabel labelUp ;
	boolean isUpEnable ; //是否是真的不能
	/**value**/
	int powa1;
	int gain1;
	
	int powa2;
	int gain2;

	public GainSettingInternalFrame() {
		super(getResourceString("gainInputtingDialog.title"), false, true, false, true);
		getContentPane().add(createMainPanel());
		load1();
	
		pack();
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
	}

	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		jRadioButton1 = new javax.swing.JRadioButton();
		jRadioButton1.setText(getResourceString("setting")+1);
		jRadioButton2 = new javax.swing.JRadioButton();
		jRadioButton2.setText(getResourceString("setting")+2);
		jRadioButton1.addActionListener(this);
		jRadioButton2.addActionListener(this);
		buttonGroup = new ButtonGroup();
		buttonGroup.add(jRadioButton1);
		buttonGroup.add(jRadioButton2);
		buttonGroup.setSelected(jRadioButton1.getModel(), true);
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(jRadioButton1, BorderLayout.NORTH);
		JPanel jPanel1 = createPanel1();
		//jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jp.add(jPanel1);
		panel.add(jp);
		jp = new JPanel(new BorderLayout());
		jp.add(jRadioButton2, BorderLayout.NORTH);
		JPanel jPanel2 = createPanel2();
		// jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jp.add(jPanel2);
		panel.add(jp, BorderLayout.SOUTH);
		return panel;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JPanel createPanel1() {
		JPanel panel = new JPanel();

		jComboBox = new JComboBox();
		jSlider = new JSlider(0, 199);
		powaTitle = new javax.swing.JLabel();
		gainTitle = new javax.swing.JLabel();
		powaText = new javax.swing.JLabel();
		gainText = new javax.swing.JLabel();
		valueText = new javax.swing.JLabel();
		valueTitle = new javax.swing.JLabel();

		jComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "4", "5", "8", "10", "16", "32" }));
		jSlider.addChangeListener(this);
		jComboBox.addItemListener(this);

		
		
		
		powaTitle.setText(getResourceString("gainSettingDialog.powA"));

		gainTitle.setText(getResourceString("gainSettingDialog.gain"));

		powaText.setText("");

		gainText.setText("");

		valueText.setText("");

		valueTitle.setText(getResourceString("gainSettingDialog.value"));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
		panel.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addGap(21, 21, 21)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(powaTitle).addComponent(gainTitle).addComponent(valueTitle))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(valueText).addComponent(gainText).addComponent(powaText))
						.addContainerGap(24, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addComponent(powaText).addGap(18, 18, 18).addComponent(gainText))
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(powaTitle))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(gainTitle))))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(valueText).addComponent(valueTitle))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		return panel;
	}

	PropertiesConfiguration pc = GeneralUtils.getProp();
	int selection;

	int width = 30; // 默认的长和宽
	int height = 30;

	private JPanel createPanel2() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel jp = new JPanel(new GridLayout(1, 3));

		//jp.setBounds(new Rectangle(width *3, height));
		//jp.setBackground(Color.LIGHT_GRAY);
		/**
		 * 摆放位置： 2  ----  1 ---- 3
		 */

		String s = (String) pc.getProperty("gainselection");
		selection = Integer.parseInt(s);
		labelSelection = new JLabel();
		labelSelection.setFont(new Font(null, Font.PLAIN /*| Font.BOLD*/, 11));
		//label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		getGainBySelection();

		labelDown = new JLabel(new MyIcon(0)); //下降
		labelDown.setDisabledIcon(new MyDisableIcon(0));
		labelUp = new JLabel(new MyIcon(1)); //上升
		labelUp.setDisabledIcon(new MyDisableIcon(1));

		labelDown.setBounds(0, 0, width, height);
		labelUp.setBounds(width * 3, 0, width, height);
		labelSelection.setBounds(width, 0, width*2, height);
		
		load2();
		
		MyMouseAdapter mma = new MyMouseAdapter();
		labelDown.addMouseListener(mma);
		labelDown.setName("jl2");
		labelUp.addMouseListener(mma);
		labelUp.setName("jl3");
		//labelDown.setBounds(new Rectangle(width, height));
		//labelSelection.setBounds(new Rectangle(width, height));
		//labelUp.setBounds(new Rectangle(width, height));
		jp.add(labelDown);
		jp.add(labelSelection);
		jp.add(labelUp);

		panel.add(jp, BorderLayout.CENTER);
		panel.add(new JPanel(), BorderLayout.NORTH);
		panel.add(new JPanel(), BorderLayout.SOUTH);
		//jp.setBounds(215, 410, width * 3, height); //定死
		return panel;
	}

	class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel jl = (JLabel) e.getSource();
			if (jl.getName().equals("jl2")) {
				isUpEnable = true;
				labelUp.setEnabled(true);
				if (selection == 2) {//下限
					isDownEnable = false;
					labelDown.setEnabled(false);
				}
				if (selection > 1) {
					selection--;
					labelSelection.setText("  " + selection + "");
					pc.setProperty("gainselection", String.valueOf(selection));
					getGainBySelection();
					apply2();
				}
			} else if (jl.getName().equals("jl3")) {
				isDownEnable = true;
				labelDown.setEnabled(true);
				if (selection == 4) {//上限
					isUpEnable = false;
					labelUp.setEnabled(false);
				}
				if (selection < 5) {
					selection++;
					//label.setText("  " + selection + "");
					pc.setProperty("gainselection", String.valueOf(selection));
					getGainBySelection();
					apply2();
				}
			}

		}
	}

	class MyDisableIcon implements Icon {
		BufferedImage bi = null;

		public MyDisableIcon(int in) {

			if (in == 0) { //向下
				/**
				 *    0 - 1
				 *     \ /
				 *      2
				 */
				bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
				Graphics g = bi.getGraphics();
				int[] xps = { 0, width, width / 2 };
				int[] yps = { 0, 0, height };
				Polygon p = new Polygon(xps, yps, 3);
				//g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, width, height);
				g.setColor(Color.GRAY);
				g.fillPolygon(p);

			} else if (in == 1) { //向上
				/**
				 *     0
				 *    / \
				 *   2 - 1
				 */
				bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
				Graphics g = bi.getGraphics();
				int[] xps = { width / 2, width, 0 };
				int[] yps = { 0, height, height };
				Polygon p = new Polygon(xps, yps, 3);
				//g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, width, height);
				g.setColor(Color.GRAY);
				g.fillPolygon(p);

			}

		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {

			g.drawImage(bi, x, y, c);

		}

		@Override
		public int getIconWidth() {
			return width;
		}

		@Override
		public int getIconHeight() {
			return height;
		}
	}

	class MyIcon implements Icon {

		BufferedImage bi = null;

		public MyIcon(int in) {

			if (in == 0) { //向下
				/**
				 *    0 - 1
				 *     \ /
				 *      2
				 */
				bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
				Graphics g = bi.getGraphics();
				int[] xps = { 0, width, width / 2 };
				int[] yps = { 0, 0, height };
				Polygon p = new Polygon(xps, yps, 3);
				//g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, width, height);
				g.setColor(Color.BLUE);
				g.fillPolygon(p);

			} else if (in == 1) { //向上
				/**
				 *     0
				 *    / \
				 *   2 - 1
				 */
				bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
				Graphics g = bi.getGraphics();
				int[] xps = { width / 2, width, 0 };
				int[] yps = { 0, height, height };
				Polygon p = new Polygon(xps, yps, 3);
				//g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, width, height);
				g.setColor(Color.RED);
				g.fillPolygon(p);

			}

		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {

			g.drawImage(bi, x, y, c);

		}

		@Override
		public int getIconWidth() {
			return width;
		}

		@Override
		public int getIconHeight() {
			return height;
		}

	}


	private void getGainBySelection() {
		switch (selection) {
		case 1:
			powa2 = Integer.parseInt((String) pc.getProperty("pwa1"));
			gain2 = Integer.parseInt((String) pc.getProperty("gain1"));
			break;
		case 2:
			powa2 = Integer.parseInt((String) pc.getProperty("pwa2"));
			gain2 = Integer.parseInt((String) pc.getProperty("gain2"));
			break;
		case 3:
			powa2 = Integer.parseInt((String) pc.getProperty("pwa3"));
			gain2 = Integer.parseInt((String) pc.getProperty("gain3"));
			break;
		case 4:
			powa2 = Integer.parseInt((String) pc.getProperty("pwa4"));
			gain2 = Integer.parseInt((String) pc.getProperty("gain4"));
			break;
		case 5:
			powa2 = Integer.parseInt((String) pc.getProperty("pwa5"));
			gain2 = Integer.parseInt((String) pc.getProperty("gain5"));
			break;
		default:
			break;
		}
		labelSelection.setText("  " + selection + " [powa:" + powa2 + " gain:" + USBDLL.GAINS[gain2]+"]");
	}

	void load1() {
		powa1 = pref.getInt("powa", 10);
		gain1 = pref.getInt("gain", 0);
		jSlider.setValue(powa1);
		jComboBox.setSelectedIndex(gain1);
		gainText.setText(USBDLL.GAINS[gain1] + "");
	}

	void load2(){
		isDownEnable = true;
		isUpEnable = true;
		if (selection == 1) {
			isDownEnable = false;
			labelDown.setEnabled(false);
		}
		if (selection == 5){
			isUpEnable = false;
			labelUp.setEnabled(false);
		}

		/**
		 * 默认一开始都不能用 因为它选择了第一个
		 */
		labelDown.setEnabled(false);
		labelUp.setEnabled(false);
		
	}
	
	void apply1() {
		pref.putInt("powa", powa1);
		pref.putInt("gain", gain1);
	}
	void apply2() {
		pref.putInt("powa", powa2);
		pref.putInt("gain", gain2);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		if (e.getStateChange() == ItemEvent.DESELECTED)
			return;
		gain1 = jComboBox.getSelectedIndex();
		gainText.setText(USBDLL.GAINS[gain1] + "");
		valueText.setText(powa1 * USBDLL.GAINS[gain1] + "");
		apply1();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		powa1 = jSlider.getValue();
		powaText.setText(powa1 + "");
		valueText.setText(powa1 * USBDLL.GAINS[gain1] + "");
		apply1();
	}

	public static void main(String[] args) {
		new JFrame() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				GainSettingInternalFrame gif = new GainSettingInternalFrame();
				JDesktopPane jsp = new JDesktopPane();
				jsp.add(gif);
				gif.setLocation(0, 0);
				gif.setVisible(true);
				getContentPane().add(jsp);
				setSize(600, 600);
				setVisible(true);
				setLocationRelativeTo(null);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		};
	}

	//boolean labelEnable = false;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jRadioButton1) {
			//labelEnable = false;
			labelUp.setEnabled(false);
			labelDown.setEnabled(false);
			jSlider.setEnabled(true);
			jComboBox.setEnabled(true);
			apply1();
		} else if (e.getSource() == jRadioButton2) {
			jSlider.setEnabled(false);
			jComboBox.setEnabled(false);
			//labelEnable = true;
			if (isUpEnable)
				labelUp.setEnabled(true);
			if (isDownEnable)
				labelDown.setEnabled(true);
			apply2();
		}
	}

}
