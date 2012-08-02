package com.ciotc.teemo.file.frame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.ciotc.teemo.file.doc.RealtimeDoc;
import com.ciotc.teemo.file.template.RealtimeFileTemplate;
import com.ciotc.teemo.file.template.RealtimeFileTemplate.Status;
import com.ciotc.teemo.frame.MainTabbedPane;
import com.ciotc.teemo.usbdll.USBDLL;

public class RFInternalFrame extends JInternalFrame implements ActionListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RealtimeFileTemplate rft;

	XYSeriesCollection xyseriescollection = new XYSeriesCollection();
	//XYSeries xyseries = new XYSeries("rf");

	private JTextField textField;

	private JButton button;

	public RFInternalFrame(RealtimeFileTemplate rft) {
		this();
		this.rft = rft;
		rft.addPropertyChangeListener(this);
	}

	public RFInternalFrame() {
		super("R && F", true, true, false, true);
		getContentPane().add(createMainPanel());

		pack();
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
	}

	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Force:");
		textField = new JTextField(10);
		button = new JButton("set");
		button.setEnabled(false);
		button.addActionListener(this);
		JButton reset = new JButton("reset");
		reset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//xyseries.clear();
				xyseriescollection.removeAllSeries();
				
			}
		});
		JPanel jp = new JPanel();
		jp.add(label);
		jp.add(textField);
		jp.add(button);
		jp.add(reset);
		ChartPanel cp = new ChartPanel(createFreaChart());
		panel.add(cp, BorderLayout.CENTER);
		panel.add(jp, BorderLayout.NORTH);
		return panel;
	}

	private JFreeChart createFreaChart() {

		//xyseriescollection.addSeries(xyseries);

		JFreeChart jfreechart = ChartFactory.createXYLineChart("R && F",// 图表标题
				"F", // 主轴标签
				"R",// 范围轴标签
				xyseriescollection, // 数据集
				PlotOrientation.VERTICAL,// 方向
				true, // 是否包含图例
				false, // 提示信息是否显示
				false);// 是否使用urls

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
		NumberAxis axis = (NumberAxis) plot.getDomainAxis();
		axis.setAutoTickUnitSelection(true);
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, Color.BLACK);
		renderer.setSeriesStroke(0, new BasicStroke(1.5F));

		return jfreechart;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			double force = Double.parseDouble(textField.getText());
			//xyseries.clear();
			
			XYSeries xyseries = new XYSeries(force+"");
			xyseriescollection.addSeries(xyseries);
			List<RealtimeDoc.RF> lists = rft.rdoc.getRFChart(force);
			for (RealtimeDoc.RF rf : lists) {
				xyseries.add(rf.force,rf.re);
			}
			
			//输出数据到文件
			PrintWriter pw = new PrintWriter(new File("rf.txt"));
			pw.println("powa:" + rft.rdoc.getPowa() + "  gain:" + USBDLL.GAINS[rft.rdoc.getGain()]);
			StringBuilder sb = new StringBuilder();
			String format = "%-6s %12s %12s %10s%n";
			String format1 = "%-6s %12.3f %12.3f %10d%n";
			String s = String.format(format, "index", "F", "R", "Value");
			sb.append(s);
			int index = 1;
			for (RealtimeDoc.RF rf : lists) {
				s = String.format(format1, index++, rf.force, rf.re, rf.value);
				sb.append(s);
			}
			pw.print(sb.toString());
			pw.close();
			
			//输出适合matlab的数据文件
			pw = new PrintWriter(new File("rf-matlab.txt"));
			pw.println(force+" "+lists.size());
			sb = new StringBuilder();
			for (RealtimeDoc.RF rf : lists) {
				s = String.format("%.3f", rf.force);
				sb.append(s+" ");
			}
			pw.println(sb.toString().trim());
			sb = new StringBuilder();
			for (RealtimeDoc.RF rf : lists) {
				s = String.format("%.3f", rf.re);
				sb.append(s+" ");
			}
			pw.println(sb.toString().trim());
			pw.close();
		} catch (Exception e2) {
			e2.printStackTrace();
			return;
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
				button.setEnabled(true);
				break;
			default:
				setVisible(false);
				button.setEnabled(false);
				break;
			}
		}
	}
}
