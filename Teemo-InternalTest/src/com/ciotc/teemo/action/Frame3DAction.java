package com.ciotc.teemo.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

import javax.swing.Icon;
import javax.swing.JInternalFrame;

import com.ciotc.teemo.file.controller.DisplayController;

public class Frame3DAction extends MyAction {

	public Frame3DAction(DisplayController displayController, String name, Icon icon) {
		super(displayController, name, icon);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//b = !b;
		//displayController.changeIsFirstContact(true);
		JInternalFrame jif = displayController.getDft().d3DInFrame;
		jif.setVisible(true);
		displayController.getDft().isExist3D = true;
		try {
			jif.setSelected(true);
			jif.setIcon(false);
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}
		
	}
}
