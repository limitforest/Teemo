package com.ciotc.teemo.file.view;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.teemo.file.model.DisplayModel;
import com.ciotc.teemo.frame.MainFrame;

public class DisplayStatusBar extends StatusBar implements ChangeListener, MouseListener {

	private int frameIndex;
	private int totalFrameNum;
	//private float speed;
	private float time;
	private int x;
	private int lenx;
	private int y;
	private int leny;

	private int force;
	private boolean isStrange;

//	String firstID = "first_zone";
//	String secondID = "second_zone";
//	String remainID = "remain_zone";
//
//	JLabel firstLabel = new JLabel();
//	JLabel secondLabel = new JLabel();
//	JLabel remainLabel = new JLabel();

	String statusID = "stauts_zone";
	JLabel statusLabel = new JLabel();

	String tooltipID = "tooltip_zone";
	JTextArea tooltip = null;

	private int period;

	/**
	 * 增加一个标志位，用来判断它是用在2D图还是3D图
	 * index = 2 --> 2D
	 * index = 3 -->3D
	 */
	int index;

	/**
	 * 用来判断它是用在2D图还是3D图
	 * index = 2 --> 2D
	 * index = 3 -->3D
	 * @param index
	 */
	public DisplayStatusBar(int index) {
		super();
		//setEditable(false);
		this.index = index;
		setZoneBorder(BorderFactory.createLineBorder(Color.GRAY));

		setPreferredSize(new Dimension(100, 35));

		tooltip = new JTextArea(2, 1);
		tooltip.setEditable(false);
		tooltip.setLineWrap(true);

		setStatusZone();

	}

	public void setStatusZone() {

		//setZones(new String[] { firstID, secondID, remainID }, new Component[] { firstLabel, secondLabel, remainLabel }, new String[] { "30%", "40%", "*" });
		setZones(new String[] { statusID }, new Component[] { statusLabel }, new String[] { "100%" });
	}

	public void setTooltipZone() {
		setZones(new String[] { tooltipID }, new Component[] { tooltip }, new String[] { "100%" });
	}

	/**
	 * For Test
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setBounds(200, 200, 600, 200);
		frame.setTitle("Status bar simulator");

		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		DisplayStatusBar statusBar = new DisplayStatusBar(2);
		contentPane.add(statusBar, BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.addMouseListener(statusBar);

//		int frameIndex =4;
//		int totalFrameNum = 123;
//		int x = 143;
//		int y = 23;
//		int force =234;
//		float speed = 5.000f;
//		String s= String.format("%4dof%4d %-6s(%2d,%2d) %-6s%3d %-6s%5.3f", 
//				frameIndex+1,totalFrameNum,"(x,y):",x,y,"force:",force,"speed:",speed);
//		System.out.println(s);
//		
//		frameIndex =133;
//		totalFrameNum = 1234;
//		x = 23;
//		y = 145;
//		force =32;
//		speed = 0.342f;
//		s= String.format("%4d of %4d %-6s(%2d,%2d) %-6s%3d %-6s%5.3f", 
//				frameIndex+1,totalFrameNum,"(x,y):",x,y,"force:",force,"speed:",speed);
//		System.out.println(s);
//		
//		frameIndex =133;
//		totalFrameNum = 1234;
//		x = 23;
//		y = 145;
//		force =32;
//		speed = 0.342f;
//		float time = speed;
//		s = String.format("%8.3f of %8.3f %-6s(%2d,%2d) %-6s%3d %-6s%5.3f", 
//				frameIndex * time,(totalFrameNum-1)*time,"(x,y):",x,y,"force:",force,"speed:",speed);
//		System.out.println(s);
//		
//		frameIndex =45;
//		totalFrameNum = 124;
//		x = 123;
//		y = 4;
//		force =123;
//		speed = 0.100f;
//		time = speed;
//		s = String.format("%8.3f of %8.3f %-6s(%2d,%2d) %-6s%3d %-6s%5.3f", 
//				frameIndex * time,(totalFrameNum-1)*time,"(x,y):",x,y,"force:",force,"speed:",speed);
//		System.out.println(s);

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		DisplayModel dm = (DisplayModel) e.getSource();

		frameIndex = dm.getFrameIndex();
		totalFrameNum = dm.getTotalFrameNum();
		period = dm.getPeriod();
		isStrange = dm.isStrange();
		time = dm.getRecordPeriod();
		x = dm.getX();
		y = dm.getY();
		force = dm.getForce();
		int[] powaGain = dm.getPowaGain();

		Preferences p = Preferences.userNodeForPackage(MainFrame.class);
		int in = p.getInt("FrameOrPeriod", 1);

		//String s ="";
		String status1 = "";
		String status2 = "";
		String status3 = "";
		String status4 = "";
		StringBuilder status = new StringBuilder();
		if (!isStrange) {
			if (in == 1) { //frame
//				s= String.format("%-4dof%4d %-6s(%2d,%2d) %-6s%3d %-6s%5.3f", 
//						frameIndex+1,totalFrameNum,"(x,y):",x,y,"force:",force,"speed:",speed);

				status1 = MessageFormat.format(getResourceString("displayStatusBar11"), totalFrameNum, frameIndex + 1);

			} else { //period
//				s = String.format("%-7.3fof%7.3f %-6s(%2d,%2d) %-6s%3d %-6s%5.3f", 
//						frameIndex * time,(totalFrameNum-1)*time,"(x,y):",x,y,"force:",force,"speed:",speed);
				status1 = MessageFormat.format(getResourceString("displayStatusBar12"), (totalFrameNum - 1) * time, frameIndex * time);

			}
			if (index == 2)
				status2 = MessageFormat.format(getResourceString("displayStatusBar2"), x, y, force);
			else if(index == 3)
				status2 = getResourceString("displayStatusBar4");
			status3 = MessageFormat.format(getResourceString("displayStatusBar3"), period);
			if (powaGain != null)
				status4 = MessageFormat.format(getResourceString("displayStatusBar5"), powaGain[0], powaGain[1]);

			status.append("<html>");
			status.append(status1 + "&nbsp;&nbsp;");
			status.append(status3 + "&nbsp;&nbsp;");
			status.append(status4 + "<br>");
			status.append(status2 + "&nbsp;&nbsp;");
			status.append("</html>");

		}
		//statusLabel.setFont(new Font(null,Font.PLAIN,12));
		statusLabel.setText(status.toString());
		//JLabel label = (JLabel) getZone(firstID);
		//firstLabel.setText(status1);
		//label = (JLabel) getZone(secondID);
		//secondLabel.setText(status2);
		//label = (JLabel) getZone(remainID);
		//remainLabel.setText(status3);

		//setText("");
		//setText(s);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton button = (JButton) e.getSource();
			String detail = (String) button.getClientProperty("detail");
			tooltip.setFont(new Font(null, Font.PLAIN, 12));
			tooltip.setText(detail);
		}
		setTooltipZone();
		revalidate();
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		tooltip.setText("");
		setStatusZone();
		revalidate();
		repaint();
	}

}
