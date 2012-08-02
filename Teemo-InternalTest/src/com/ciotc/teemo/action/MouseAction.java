package com.ciotc.teemo.action;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.event.MouseInputListener;

import com.ciotc.teemo.file.controller.DisplayController;

public class MouseAction implements MouseInputListener{

	protected DisplayController displayController;
	private int range = 2;
	private boolean isPressed;
	public MouseAction(DisplayController displayController) {
		this.displayController = displayController;
	}
	
	private boolean isRange(int a, float b) {
		if (Math.abs(a - b) <= range/2)
			return true;
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		float x = 0;
		if (isRange(e.getX(), x)) {
			isPressed = true;
			//oldx = x;
		} else
			isPressed = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}
