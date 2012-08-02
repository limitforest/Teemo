package com.ciotc.teemo.file.frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.ciotc.teemo.file.frame.DisplayInternalFrame.MyComponentAdapter;
import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.file.view.DisplayView;
import com.ciotc.teemo.file.view.GraphView;

public class GraphInternalFrame extends JInternalFrame {

	DisplayFileTemplate displayFileTemplate;
	GraphView view;

	public GraphInternalFrame(DisplayFileTemplate displayFileTemplate, String title) {
		this(title);
		this.displayFileTemplate = displayFileTemplate;

	}

	public GraphInternalFrame(String title) {
		super(title, true, true, false, true);
		setSize(650, 350);
		setMinimumSize(new Dimension(650, 350));
		setPreferredSize(new Dimension(650, 350));
		setVisible(true);
		addComponentListener(new MyComponentAdapter());
		//addInternalFrameListener(new MyComponentAdapter());
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
	}

	public void addDisplayView(GraphView view) {
		this.view = view;
		//this.getContentPane().add(this.view, BorderLayout.CENTER);
		//this.getContentPane().add(this.view);
		setContentPane(view);
	}

	public GraphView getView() {
		return view;
	}

	public DisplayFileTemplate getDisplayFileTemplate() {
		return displayFileTemplate;
	}

//	class MyComponentAdapter extends InternalFrameAdapter {
//
//		@Override
//		public void internalFrameClosed(InternalFrameEvent e) {
//			displayFileTemplate.setClose(GraphInternalFrame.this);
//		}
//
//
//	}

	class MyComponentAdapter extends ComponentAdapter{

		@Override
		public void componentShown(ComponentEvent e) {
			displayFileTemplate.setOpen(GraphInternalFrame.this);
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			displayFileTemplate.setClose(GraphInternalFrame.this);
		}
		
	}
	
}
