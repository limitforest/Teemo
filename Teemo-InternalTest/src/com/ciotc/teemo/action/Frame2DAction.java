package com.ciotc.teemo.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

import javax.swing.Icon;
import javax.swing.JInternalFrame;

import com.ciotc.teemo.file.controller.DisplayController;

public class Frame2DAction extends MyAction {

	public Frame2DAction(DisplayController displayController, String name, Icon icon) {
		super(displayController, name, icon);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//b = !b;
		//displayController.changeIsFirstContact(true);
		JInternalFrame jif = displayController.getDft().d2DInFrame;
		//System.out.println(jif.isSelected()+","+jif.isShowing()+","+jif.isIcon()+","+jif.isFocusOwner()+","+jif.isVisible());							
		jif.setVisible(true);
		displayController.getDft().isExist2D = true;
		try {
			jif.setSelected(true);
			jif.setIcon(false);
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}
	}
}
