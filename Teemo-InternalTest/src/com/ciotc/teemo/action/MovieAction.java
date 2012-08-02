package com.ciotc.teemo.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.ciotc.teemo.file.controller.DisplayController;

public class MovieAction extends AbstractAction {
	protected DisplayController displayController;

	public MovieAction(DisplayController displayController) {
		this.displayController = displayController;
	}

	private String menuName;
	
	public void setMenuNam(String s){
		this.menuName = s;
	}
	
	public void actionPerformed(ActionEvent e) {
		displayController.perfomrMovieEvent(menuName);
	}

}
