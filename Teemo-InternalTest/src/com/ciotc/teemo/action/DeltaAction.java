package com.ciotc.teemo.action;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import com.ciotc.teemo.file.controller.DisplayController;

public class DeltaAction extends MyAction {

	public DeltaAction(DisplayController displayController, String name, Icon icon) {
		super(displayController, name, icon);
		putValue("set", "false"); // 初始状态为假
	}

}
