package com.ciotc.teemo.file.frame;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class ActivedInternalFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class MyObservable extends  Observable{

		@Override
		public synchronized void setChanged() {
			super.setChanged();
		}
		
	};
	
	MyObservable observable = new MyObservable();
	
	class MyActivedInterFrameAdapter extends InternalFrameAdapter{

		@Override
		public void internalFrameActivated(InternalFrameEvent e) {
			notifyObservers(ActivedInternalFrame.this);
		}

		@Override
		public void internalFrameClosing(InternalFrameEvent e) {
			
			notifyObservers("closing");
		}
	}
		
	public void addObserver(Observer o){
		observable.addObserver(o);
	}
	
	public void deleteObserver(Observer o){
		observable.deleteObserver(o);
	}
	
	public void notifyObservers(Object arg){
		observable.setChanged();
		observable.notifyObservers(arg);
	}
	
	public ActivedInternalFrame() {
		super();
		addInternalFrameListener(new MyActivedInterFrameAdapter());
	}

	public ActivedInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
		super(title, resizable, closable, maximizable, iconifiable);
		addInternalFrameListener(new MyActivedInterFrameAdapter());
	}

	public ActivedInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
		super(title, resizable, closable, maximizable);
		addInternalFrameListener(new MyActivedInterFrameAdapter());
	}

	public ActivedInternalFrame(String title, boolean resizable, boolean closable) {
		super(title, resizable, closable);
		addInternalFrameListener(new MyActivedInterFrameAdapter());
	}

	public ActivedInternalFrame(String title, boolean resizable) {
		super(title, resizable);
		addInternalFrameListener(new MyActivedInterFrameAdapter());
	}

	public ActivedInternalFrame(String title) {
		super(title);
		addInternalFrameListener(new MyActivedInterFrameAdapter());
	}
	
	
	
}
