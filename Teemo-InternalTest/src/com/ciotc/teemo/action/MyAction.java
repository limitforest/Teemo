package com.ciotc.teemo.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.ciotc.teemo.file.controller.DisplayController;

public class MyAction extends AbstractAction {
	protected DisplayController displayController;

	public MyAction(DisplayController displayController, String name, Icon icon) {
		super(name,icon);
		this.displayController = displayController;
	}

	public void actionPerformed(ActionEvent e) {
		//b = !b;
		//displayController.changeIsFirstContact(true);
		displayController.perform(this);
	}
}
