package com.ciotc.teemo.file.model;

import javax.swing.AbstractAction;
import javax.swing.event.ChangeEvent;

public class ToolBarModel extends AbstractModel{
	private String buttonName;
	private AbstractAction action;
	
	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
		fireChangeEvent(new ChangeEvent(this));
	}

	public AbstractAction getAction() {
		return action;
	}

	public void setAction(AbstractAction action) {
		this.action = action;
		fireChangeEvent(new ChangeEvent(this));
	}

	
}
