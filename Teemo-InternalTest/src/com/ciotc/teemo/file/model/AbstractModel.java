package com.ciotc.teemo.file.model;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class AbstractModel {
	private Vector<ChangeListener> vectorChangeListeners = new Vector<ChangeListener>();

	public void addChangeListener(ChangeListener changelistener) {
		vectorChangeListeners.addElement(changelistener);
	}

	public void removeChangeListener(ChangeListener changelistener) {
		vectorChangeListeners.removeElement(changelistener);
	}

	protected void fireChangeEvent(ChangeEvent changeevent) {
		Enumeration<ChangeListener> enumeration = vectorChangeListeners.elements();
		while (enumeration.hasMoreElements()) {
			ChangeListener changelistener = enumeration.nextElement();
			changelistener.stateChanged(changeevent);
		}
	}

}
