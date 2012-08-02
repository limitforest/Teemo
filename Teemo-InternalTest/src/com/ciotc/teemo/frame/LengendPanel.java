package com.ciotc.teemo.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.contour.MyColor;

/**
 * 采用绝对布局 采用连续虑值 不采用间断虑值
 * @author Linxiaozhi
 *
 */
public class LengendPanel extends JPanel implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String LOWER = "lower";

	MainFrame parent = null;

	Preferences preferences;

	JSlider slider = null;

	//int[] lowers = { 0, 15, 31, 47, 63, 79, 95, 111, 127, 143, 159, 175, 191, 207, 223, 239, 255, 256 };
	//int[] lowers = { 0, 1, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240, 255 };
	//int min = 0;
	//int max = 17;
	final static int WIDTH = 50;
	final static int HEIGHT = 480;
	final static int UNIT = 16;
	final static int ICON_WIDTH = WIDTH / 2;
	final static int ICON_HEIGHT = HEIGHT / UNIT;
	final static int ICON_HALF_HEIGHT = ICON_HEIGHT / 2;
	final static int MIN = 0;
	final static int MAX = 255;
	int lower = 0; // it means old value
	int newLower = 0;

	//final Color bgColor = new Color(0xe0, 0xe0, 0xe0);
	class MyIcon implements Icon {
		BufferedImage bi = null;
		int val ;
		
		/**
		 * 构造自己的图标 该图标只有颜色 根据颜色索引值来得到相应的颜色
		 * @param in 颜色索引值
		 */
		public MyIcon(int in) {
			this.val = in;
			if (in != 256) {
				bi = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_BGR);
			}else{
				bi = new BufferedImage(ICON_WIDTH, ICON_HEIGHT/8, BufferedImage.TYPE_INT_BGR);
			}
			
			Graphics g2 = bi.getGraphics();
			Color color = null;
			//if (in != 0) {
			color = MyColor.values()[in >> 4].getRgb();
			//} else {
			//	color = Color.WHITE;
			//}
			g2.setColor(color);

			g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
			//	g2.fillRect(0, 0, halfWidth/2, height);
			//其他部分将它抹掉
			//g2.setColor(bgColor);
			//	g2.fillRect(halfWidth/2, 0, halfWidth, height);
			
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			if(val != 256)
				g.drawImage(bi, x, y - ICON_HALF_HEIGHT, c);
			else
				g.drawImage(bi, x, y - ICON_HALF_HEIGHT+bi.getWidth(), c);
				
		}

		@Override
		public int getIconWidth() {
			return ICON_WIDTH;
		}

		@Override
		public int getIconHeight() {
			return ICON_HEIGHT;
		}

	}

	public LengendPanel(MainFrame mainFrame) {
		parent = mainFrame;
		preferences = Preferences.userNodeForPackage(MainFrame.class);

//		add(new JPanel() {
//			{
//				setBackground(bgColor);
//			}
//		}, BorderLayout.NORTH);
//		add(new JPanel() {
//			{
//				setBackground(bgColor);
//			}
//		}, BorderLayout.SOUTH);

		int val = preferences.getInt(LOWER, MIN);
		//int index = 1;//Arrays.binarySearch(lowers, val);
		slider = new JSlider(JSlider.VERTICAL, MIN, MAX, val);
		slider.setFocusable(false);
		//slider.setInverted(true);
		slider.addChangeListener(this);
		slider.setMinorTickSpacing(1);

		Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		for (int i = 0; i < slider.getMaximum(); i += UNIT)
			table.put(Integer.valueOf(i), new JLabel(new MyIcon(i)));
		table.put(Integer.valueOf(255), new JLabel(new MyIcon(256)));
		//slider.setLabelTable(slider.createStandardLabels(1));
		slider.setLabelTable(table);
		//slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		setLayout(new BorderLayout());
		//JLabel title = new JLabel(getResourceString("lengendDialog.title"));
		//title.setHorizontalTextPosition(SwingConstants.CENTER);
		//add(title,BorderLayout.NORTH);
		add(slider, BorderLayout.CENTER);
		//slider.setBackground(bgColor);
		slider.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		//slider.setSize(345, 100);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		slider.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	public static void main(String[] args) {
		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}
		JFrame frame = new JFrame();

		LengendPanel lengend = new LengendPanel(null);
		frame.add(lengend);
		//frame.setLocationRelativeTo(null);
		frame.setLocation(500, 200);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//new LengendFrame(null);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		newLower = slider.getValue();
		preferences.putInt(LOWER, newLower);
		firePropertyChange(LOWER, lower, newLower);
		lower = newLower;
	}

}
