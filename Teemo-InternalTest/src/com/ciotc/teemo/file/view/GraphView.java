package com.ciotc.teemo.file.view;

import static com.ciotc.teemo.resource.MyResources.getResourceString;
import static com.ciotc.teemo.resource.MyResources.getResourceURL;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.ciotc.teemo.action.ActionConstructor;
import com.ciotc.teemo.file.model.DisplayModel;
import com.ciotc.teemo.file.template.DisplayFileTemplate;

public class GraphView extends JSplitPane implements ChangeListener {
	// private JSplitPane mainPanel;
	private JPanel graphPanel;
	private JPanel dataPanel;
	private JScrollPane jsp;
	private JSlider slider;

	XYSeriesCollection xyseriescollection = new XYSeriesCollection();
	XYSeries xyseries1 = new XYSeries("c1");
	XYSeries xyseries2 = new XYSeries("c2");
	XYSeries xyseries3 = new XYSeries("c3");
	private double[][] data;
	// private double percentInFrame ;
	private float time;
	private int frameIndex;
	private int totalframeNum;
	private boolean isStrange;
	// private double percentForceInRight;
	// private double percentForceInLeft;
	private double[] percentForce;//= new double[3];
	private DisplayFileTemplate displayFielTemplate;
	private JFreeChart jchart;

	public GraphView(DisplayFileTemplate displayFielTemplate) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		this.displayFielTemplate = displayFielTemplate;
		initView();
	}

	private void initView() {
		// graphPanel = new JPanel();
		JPanel jp = new JPanel(new BorderLayout());

		slider = new JSlider(JSlider.HORIZONTAL, 0, totalframeNum, frameIndex);
		MySliderChangeListener mySliderChangeListener = new MySliderChangeListener();
		slider.addChangeListener(mySliderChangeListener);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		jchart = createFreaChart();
		graphPanel = new MyChartPanel(jchart);// createDemoPanel();
		jp.add(graphPanel);
		JPanel jp2 = new JPanel(new BorderLayout());
		jp2.add(new JLabel("  " + getResourceString("timeline")), BorderLayout.WEST);
		jp2.add(new JLabel("    "), BorderLayout.EAST);
		jp2.add(slider);
//		jp.add(slider, BorderLayout.SOUTH);
		jp.add(jp2, BorderLayout.SOUTH);

		JPanel jpp = new JPanel(new BorderLayout());
		dataPanel = new MydataPanel();
		JPanel buttonPanel = createButtonPanel();
		jpp.add(dataPanel, BorderLayout.CENTER);
		jpp.add(buttonPanel, BorderLayout.NORTH);

		//jsp = new JScrollPane(dataPanel);
		jsp = new JScrollPane(jpp);

		setBackground(Color.LIGHT_GRAY);

		// setLeftComponent(graphPanel);
		setLeftComponent(jp);
		//setRightComponent(dataPanel);
		setRightComponent(jsp);
		setDividerLocation(400);

		// add(graphPanel,BorderLayout.CENTER);
		// add(sp,BorderLayout.EAST);
	}

	public static final String imageSuffix = "Image";
	public static final String actionSuffix = "Action";
	public static final String tipSuffix = "Tooltip";
	double weight;

	public JButton createToolbarButton(String key) {

		JButton b = new JButton() {
			private static final long serialVersionUID = 1L;

			@Override
			public float getAlignmentY() {
				return 0.5f;
			}
		};
		Action a = null;
		try {
			String astr = getResourceString(key + actionSuffix);
			if (astr == null) {
				astr = key;
			}
			URL url = getResourceURL(key + imageSuffix);
			a = ActionConstructor.constructAction(astr, displayFielTemplate.displayController, key, new ImageIcon(url));
			//actionMaps.put(key, a);
		} catch (Exception e) {
			a = null;
		}
		b.setFocusPainted(false);
		b.setAction(a);
		b.setText("");

		String tip = getResourceString(key + tipSuffix);
		if (tip != null) {
			b.setToolTipText(tip);
		}

		return b;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		final JButton button1 = createToolbarButton("frame2D");

		//button1.setContentAreaFilled(false);

		//panel.add(button1);
		JButton button2 = createToolbarButton("frame3D");
		//button2.setContentAreaFilled(false);
		toolbar.add(button1);
		toolbar.add(button2);

		//panel.add(button2);
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		panel.add(toolbar, BorderLayout.WEST);
		return panel;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		DisplayModel dm = (DisplayModel) e.getSource();
		isStrange = dm.isStrange();
		if (isStrange) {
			slider.setEnabled(false);
			repaint();
			return;
		} else {
			slider.setEnabled(true);
		}
		if (dm.getTowho() == 0) {
			frameIndex = dm.getFrameIndex();
			time = frameIndex * dm.getRecordPeriod();
			percentForce = dm.getPercentForce();
			totalframeNum = dm.getTotalFrameNum();

			slider.setValue(frameIndex);
			slider.setMaximum(totalframeNum - 1);
			repaint();

			weight = dm.getWeight();
			System.out.println(frameIndex + "  " + weight + "kg");
		} else if (dm.getTowho() == 1) {
			data = dm.getPercentData();
			xyseries1.clear();
			xyseries2.clear();
			xyseries3.clear();
			// 总的
			for (int i = 0; i < data[0].length; i++) {
				xyseries1.add(i + 1, data[0][i] * 100);
				xyseries2.add(i + 1, data[1][i] * 100);
				xyseries3.add(i + 1, data[2][i] * 100);
			}
		}

	}

	class MySliderChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider sl = (JSlider) e.getSource();
			int value = sl.getValue();
			//System.out.println(sl.getValue());

			XYPlot plot = (XYPlot) jchart.getPlot();
			// ValueAxis domainAxis = plot.getDomainAxis();
			// Range range = domainAxis.getRange();
			// double c = domainAxis.getLowerBound() + (value /
			// totalframeNum) * range.getLength();
			plot.setDomainCrosshairValue(value + 1);

			//It's a action.
			displayFielTemplate.getDisplayModel().setaFrameByFrameIndex(value);
		}

	}

	public static void main(String[] args) {
		new JFrame() {
			{
				setSize(800, 500);
				setVisible(true);
				GraphView gv = new GraphView(null);
				JPanel jp = new JPanel();
				jp.setLayout(new BorderLayout());
				JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
				ChartPanel cp = gv.new MyChartPanel(gv.createFreaChart());
				cp.setMinimumSize(new Dimension(100, 200));
				jp.add(cp);
				jp.add(slider, BorderLayout.SOUTH);
				MydataPanel dp = gv.new MydataPanel();
				dp.setSize(50, 100);
				JScrollPane sp = new JScrollPane(dp);
				sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp, sp);
				setContentPane(mainPanel);
				// add(gv.dataPanel,BorderLayout.EAST);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		};
	}

	private JFreeChart createFreaChart() {
		xyseriescollection.addSeries(xyseries1);
		xyseriescollection.addSeries(xyseries2);
		xyseriescollection.addSeries(xyseries3);

		JFreeChart jfreechart = ChartFactory.createXYLineChart(getResourceString("chart.title"),// 图表标题
				getResourceString("chart.xlable"), // 主轴标签
				getResourceString("chart.ylable"),// 范围轴标签
				xyseriescollection, // 数据集
				PlotOrientation.VERTICAL,// 方向
				false, // 是否包含图例
				false, // 提示信息是否显示
				false);// 是否使用urls
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 12));

		// 改变图表的背景颜色
		jfreechart.setBackgroundPaint(Color.LIGHT_GRAY);

		XYPlot plot = (XYPlot) jfreechart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.white);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainCrosshairPaint(Color.BLACK);
		plot.setDomainCrosshairStroke(new BasicStroke(1.5f));
		plot.setDomainCrosshairVisible(true);

		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		// numberaxis.setAutoRange(true);
		numberaxis.setAutoTickUnitSelection(true);
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// numberaxis.setAutoRangeMinimumSize(10);
		numberaxis.setLabelFont(new Font("宋体", Font.BOLD, 12));
		NumberAxis axis = (NumberAxis) plot.getDomainAxis();
		axis.setAutoTickUnitSelection(true);
		axis.setLabelFont(new Font("宋体", Font.BOLD, 12));
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		// lineandshaperenderer.setDrawOutlines(true);
		// lineandshaperenderer.setUseFillPaint(true);
		// lineandshaperenderer.setBaseFillPaint(Color.white);
		renderer.setSeriesPaint(0, Color.BLACK);
		renderer.setSeriesStroke(0, new BasicStroke(1.5F));
		renderer.setSeriesPaint(1, Color.GREEN);
		renderer.setSeriesStroke(1, new BasicStroke(1.5F));
		renderer.setSeriesPaint(2, Color.RED);
		renderer.setSeriesStroke(2, new BasicStroke(1.5F));

		return jfreechart;
	}

	public BufferedImage chart2Image(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		Rectangle2D r2 = new Rectangle2D.Double(0, 0, width, height);
		jchart.draw(g2, r2);
		g2.dispose();
		return image;
	}

	class MyChartPanel extends ChartPanel {

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			// g.drawLine(0, 0, 100, 100);
		}

		public MyChartPanel(JFreeChart chart) {
			super(chart);

		}

		@Override
		public void chartProgress(ChartProgressEvent event) {

		}

	}

//	class MyScrollPane extends JScrollPane{
//		public MyScrollPane(){
//			JPanel jp = new MydataPanel();
//			jp.setPreferredSize(new Dimension(180, 120));
//			jp.setSize(50, 50);
//	                JViewport vp = getViewport();
//	                vp.add(jp);
//			//setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//			//setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		}
//	}

	class MydataPanel extends JPanel {

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(180, 120);
		}

		@Override
		protected void paintComponent(Graphics g2) {
			super.paintComponent(g2);
			if (isStrange)
				return;
			Graphics2D g = (Graphics2D) g2;
			g.setFont(new Font("", Font.PLAIN, 14));

			/**
			 * 增加了显示的字段
			 * @date 2012.4.16
			 */
			String s00 = MessageFormat.format(getResourceString("dataPanel.currentFrame"), frameIndex + 1);
			String s01 = String.format("%s: %.2f mm*mm", getResourceString("dataPanel.contactArea"), percentForce[3]);
			//date 2012.6.12
			//增加了显示重量和力度
//			String s02 = String.format("%s: %.0f ", getResourceString("dataPanel.forceSum"), percentForce[4]);
//			String s03 = String.format("%s: %.2f ", getResourceString("dataPanel.perForce"), percentForce[5]);
			String s021 = String.format("%s: %.0f %s", "weight", weight,"kg");
			String s02 = String.format("%s: %.0f %s", getResourceString("dataPanel.forceSum"), weight*9.8f,"N");
			String s03 = String.format("%s: %.2f ", getResourceString("dataPanel.perForce"), weight/percentForce[3]);
			
			
			String s1 = getResourceString("dataPanel.line1");
			String s2 = String.format("%s = %.2f %s", getResourceString("dataPanel.force"), percentForce[0] * 100, "%");
			String s3 = String.format("%.3f %s", time, getResourceString("dataPanel.time"));
			String s4 = String.format("%s = %.2f %s", getResourceString("dataPanel.left"), percentForce[1] * 100, "%");
			String s5 = String.format("%s = %.2f %s", getResourceString("dataPanel.right"), percentForce[2] * 100, "%");
			float h = 20, w = 15, h0 = h / 4;

			/**0**/
			g.drawString(s00, 2, h);
			g.drawString(s01, 2, h * 2);
			g.drawString(s02, 2, h * 3);
			g.drawString(s03, 2, h * 4);
			//date 2012.6.12
			g.drawString(s021, 2, h * 5);
			
			
			float offSet = h * 5;//相比之前 现在需向下移动 此为偏移量
			/** 1 **/
			g.drawString(s1, 2, offSet + h);
			/** 2 **/
			g.setColor(Color.BLACK);
			Rectangle2D rect = new Rectangle2D.Float(3, offSet + 2 + 2 * h - h0 - h0 / 2, w, 4);
			g.fill(rect);
			g.drawString(s2, 3 + w + 2, offSet + 2 + 2 * h);
			/** 3 **/
			g.drawString(s3, 2, offSet + 2 + 3 * h);
			/** 4 **/
			g.setColor(Color.GREEN);
			rect = new Rectangle2D.Float(3, offSet + 2 + 4 * h - h0, w, 4);
			g.fill(rect);
			g.setColor(Color.BLACK);
			g.drawString(s4, 3 + w + 2, offSet + 2 + 4 * h);
			/** 5 **/
			g.setColor(Color.RED);
			rect = new Rectangle2D.Float(3, offSet + 2 + 5 * h - h0, w, 4);
			g.fill(rect);
			g.setColor(Color.BLACK);
			g.drawString(s5, 3 + w + 2, offSet + 2 + 5 * h);
		}

	}
}
