package com.ciotc.teemo.action;

import javax.swing.Icon;

import com.ciotc.teemo.file.controller.DisplayController;

public class COFAction extends MyAction{

	public COFAction(DisplayController displayController, String name, Icon icon) {
		super(displayController, name, icon);
		putValue("set", "0"); // 初始状态为假
	}

}
