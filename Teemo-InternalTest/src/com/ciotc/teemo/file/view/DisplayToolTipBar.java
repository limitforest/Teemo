package com.ciotc.teemo.file.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.ciotc.teemo.frame.SimpleStatusBar;

public class DisplayToolTipBar extends StatusBar implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DisplayToolBar toolbar;

	JTextArea label;
	
	String firstID = "first_zone";
	/**
	 * 高
	 */
	int height = 30;
	public DisplayToolTipBar() {
		label = new JTextArea(2,1);
		label.setEditable(false);
		label.setLineWrap(true);
		//label.setPreferredSize(new Dimension(100, height));
		//label.setMinimumSize(new Dimension(100, height));
		setZones(new String[] { firstID }, new Component[] { label }, new String[] { "100%" });
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
		setZones(new String[] { firstID }, new Component[] { label }, new String[] { "100%" });
		JButton button = (JButton) e.getSource();
		String detail = (String) button.getClientProperty("detail");
		label.setText(detail);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		label.setText("");
	}

	

}
