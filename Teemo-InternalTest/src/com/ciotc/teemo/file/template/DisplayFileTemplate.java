package com.ciotc.teemo.file.template;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.ciotc.teemo.file.controller.DisplayController;
import com.ciotc.teemo.file.doc.DisplayDoc;
import com.ciotc.teemo.file.doc.RealtimeDoc.WeightInput;
import com.ciotc.teemo.file.frame.DisplayInternalFrame;
import com.ciotc.teemo.file.frame.GraphInternalFrame;
import com.ciotc.teemo.file.model.DisplayModel;
import com.ciotc.teemo.file.view.Display2DView;
import com.ciotc.teemo.file.view.Display3DView;
import com.ciotc.teemo.file.view.DisplayStatusBar;
import com.ciotc.teemo.file.view.DisplayToolBar;
import com.ciotc.teemo.file.view.GraphView;
import com.ciotc.teemo.frame.CommentsDialog;
import com.ciotc.teemo.frame.LengendPanel;
import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.frame.MainTabbedPane;
import com.ciotc.teemo.menu.UserPreferencePanel;
import com.ciotc.teemo.splines.ArchPoints;

/**
 * 展示输入文件的模板类：包括框架，视图（有2D和3D）,文档
 * 
 * @author 林晓智
 * 
 */
public class DisplayFileTemplate extends /*JDesktopPane*/JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DisplayInternalFrame d2DInFrame = null;
	public DisplayInternalFrame d3DInFrame = null;
	public GraphInternalFrame graphInFrame = null;
	public Display2DView d2dView = null;
	public Display3DView d3dView = null;
	public GraphView graphView = null;
	public DisplayDoc ddoc = null;
	public CommentsDialog commentDialog = null;
	public boolean isExist2D = false;
	public boolean isExist3D = false;
	public boolean isExistGrpah = false;

	//public MDIDesktopPane desktopPane;
	MainFrame mainFrame = null;

	public DisplayController displayController = null;

	private DisplayModel displayModel = null;
	//private ToolBarModel toolbarModel = null;
	public ArchPoints archPoints = null; //用来保存弓形模型控制点

	/**
	 * 状态栏
	 */
	public DisplayStatusBar status2DField = null;
	public DisplayStatusBar status3DField = null;

	MyDesktopPane desktop = null;

	//public Display2DStatusPanel status2dPanel; 

	class MyDesktopPane extends JDesktopPane {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Dimension old;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Dimension dim = preferredSizeAllFrames();
			if (old != dim) {
				setPreferredSize(dim);
				revalidate();
				old = dim;
			}
		}

		private Dimension preferredSizeAllFrames() {
			int mWidth = 0, mHeight = 0;
			for (JInternalFrame jif : getAllFrames()) {
				if (jif.isVisible()) {
					int w0 = jif.getX() + jif.getWidth();
					int h0 = jif.getY() + jif.getHeight();
					if (w0 > mWidth)
						mWidth = w0;
					if (h0 > mHeight)
						mHeight = h0;
				}

			}
			return new Dimension(mWidth, mHeight);
		}
	}

	public DisplayFileTemplate(/*MDIDesktopPane desktopPane,*/String filePath) {
		//this.desktopPane = desktopPane;

		//jsp = new JScrollPane(this);
		//jsp.setPreferredSize(new Dimension(400, 400));
		//add(jsp, BorderLayout.CENTER);

		//add(jsp);
		//getContentPane().add(jsp);

		//add(jsp);
		desktop = new MyDesktopPane();
		setViewportView(desktop);
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		isCreateSuccess = constructData(filePath);

		if (isCreateSuccess) {
			//存储控制点坐标
			archPoints = new ArchPoints(ddoc);
			// 2D图 && 3D图
			build2DAnd3D(/*desktopPane*/);

			// 3D图
			// build3D(desktopPane);
			// updateMenu();
		}
	}

	public boolean isCreateSuccess = false;

	//private JScrollPane jsp;

	public boolean constructData(String filePath) {
		// 获取数据
		ddoc = new DisplayDoc(filePath);
		//@date 2012.6.12
		ddoc.setDisplayFileTemplate(this);
		if (ddoc.getFileData())
			return true;
		else
			return false;
	}

	public void build2DAnd3D(/*MDIDesktopPane desktopPane*/) {
		isExist2D = true;
		d2DInFrame = new DisplayInternalFrame(this, getResourceString("2D") + "-" + ddoc.getFilePath());

		isExist3D = true;
		d3DInFrame = new DisplayInternalFrame(this, getResourceString("3D") + "-" + ddoc.getFilePath());

		isExistGrpah = true;
		graphInFrame = new GraphInternalFrame(this, getResourceString("graph") + "-" + ddoc.getFilePath());

		commentDialog = new CommentsDialog(this, getResourceString("comment") + "-" + ddoc.getFilePath());

		// d2DInFrame.addDisplayView(d2dView);
		// controller
		displayController = new DisplayController(this);
		// model
		displayModel = new DisplayModel(ddoc);
		//toolbarModel = new ToolBarModel();
		// view
		DisplayToolBar displayToolbar = new DisplayToolBar(displayController, "toolbar");
		DisplayToolBar displayToolbar2 = new DisplayToolBar(displayController, "toolbar2");
		//DisplayToolTipBar tooltip = new DisplayToolTipBar();
		//status2dPanel = new Display2DStatusPanel();
		//DisplayToolTipBar tooltip2 = new DisplayToolTipBar();

		d2dView = new Display2DView(this);
		d3dView = new Display3DView(this);
		graphView = new GraphView(this);
		status2DField = new DisplayStatusBar(2);
		status3DField = new DisplayStatusBar(3);

		archPoints.addObserver(displayModel);

		displayModel.addChangeListener(d2dView);
		displayModel.addChangeListener(d3dView);
		displayModel.addChangeListener(graphView);
		displayModel.addChangeListener(displayToolbar);
		displayModel.addChangeListener(displayToolbar2);
		displayModel.addChangeListener(status2DField);
		//displayModel.addChangeListener(status2dPanel);
		displayModel.addChangeListener(status3DField);
		// add models and views
		displayController.addModel(displayModel);
		//displayController.addTooltip(tooltip);
		//displayController.addTooltip(tooltip2);
		//displayController.addModel(toolbarModel);

		displayModel.initDefault();
		// d2DInFrame.addDisplayView(d2dView); //兼容之前的
		// d2DInFrame.add(d2dView, BorderLayout.CENTER);
		// d2DInFrame.add(d2dView, BorderLayout.CENTER);
		d2DInFrame.addDisplayView(d2dView);
		d2DInFrame.add(displayToolbar, BorderLayout.NORTH);
		//JPanel status2dPanel = new JPanel(new BorderLayout());
		//status2dPanel.add(statusField,BorderLayout.NORTH);
		//status2dPanel.add(tooltip,BorderLayout.CENTER);status2dPanel.remove(tooltip);
		d2DInFrame.add(status2DField, BorderLayout.SOUTH);

		// d2DInFrame.setTitle("2D视图" + "-" + ddoc.getFilePath());

		// d3DInFrame.add(d3dView, BorderLayout.CENTER);
		d3DInFrame.addDisplayView(d3dView);
		d3DInFrame.add(displayToolbar2, BorderLayout.NORTH);
		//JPanel status3dPanel = new JPanel(new BorderLayout());
		//status3dPanel.add(status3DField,BorderLayout.NORTH);
		//status3dPanel.add(tooltip2,BorderLayout.CENTER);
		d3DInFrame.add(status3DField, BorderLayout.SOUTH);

		graphInFrame.add(graphView);

		desktop.setBackground(new Color(240, 240, 240));
		desktop.add(d2DInFrame);
		d2DInFrame.setLocation(0, 0);
		d2DInFrame.setVisible(true);
		desktop.add(d3DInFrame);
		d3DInFrame.setLocation(250, 0);
		d3DInFrame.setVisible(true);
		desktop.add(graphInFrame);
		graphInFrame.setLocation(500, 80);
		graphInFrame.setVisible(true);
//		isExist3D = false;
//		d3DInFrame.doDefaultCloseAction();
	}

	// public void build3D(MDIDesktopPane desktopPane) {
	// isExist3D = true;
	// d3DInFrame = new DisplayInternalFrame("3D视图" + "-" +
	// ddoc.getFilePath());
	// d3dView = new Display3DView(this);
	//
	// d3DInFrame.addDisplayView(d3dView);
	//
	//
	// desktopPane.addJInternalFrame(d3DInFrame);
	// }

	public void setClose(JInternalFrame f) {
		if (f == d2DInFrame)
			isExist2D = false;
		else if (f == d3DInFrame)
			isExist3D = false;
		else if (f == graphInFrame)
			isExistGrpah = false;
		if (!isExist2D && !isExist3D && !isExistGrpah) {
			firePropertyChange(MainTabbedPane.CLOSE_DISPLAY_FILE_TEMPLATE, null, this);
		}

	}

	public void setOpen(JInternalFrame f) {
		if (f == d2DInFrame)
			isExist2D = true;
		else if (f == d3DInFrame)
			isExist3D = true;
		else if (f == graphInFrame)
			isExistGrpah = true;
	}

	public DisplayController getDisplayController() {
		return displayController;
	}

	public DisplayModel getDisplayModel() {
		return displayModel;
	}

//	public ToolBarModel getToolbarModel() {
//		return toolbarModel;
//	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void closeDirectly() {
		if (isExist2D) {
			d2DInFrame.doDefaultCloseAction();
		}
		if (isExist3D) {
			d3DInFrame.doDefaultCloseAction();
		}
		if (isExistGrpah) {
			graphInFrame.doDefaultCloseAction();
		}
		firePropertyChange(MainTabbedPane.CLOSE_DISPLAY_FILE_TEMPLATE, null, this);
	}

	@Override
	public String toString() {
		return "DisplayFileTemplate [d2DInFrame=" + d2DInFrame + ", d3DInFrame=" + d3DInFrame + ", graphInFrame=" + graphInFrame + "]";
	}

	public void save() {
		if (!archPoints.isDirty() /*&& !displayFileTemplate.ddoc.isDirty()*/)
			return;
		//保存控制点
		int answer = JOptionPane.showConfirmDialog(mainFrame, getResourceString("isSaveComment"), getResourceString("pleaseConfirm"), JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			archPoints.savePoints();
			int index = ddoc.saveArchPoints2File();
			if (index == 1) {
				JOptionPane.showMessageDialog(d2DInFrame, getResourceString("saveSuccess"), getResourceString("Teemo"), JOptionPane.INFORMATION_MESSAGE);
			} else if (index == 2) {
				JOptionPane.showMessageDialog(d2DInFrame, getResourceString("saveFail"), getResourceString("Teemo"), JOptionPane.INFORMATION_MESSAGE);
			}
		}
		d2dView.repaint();
	}

	public void restore() {
		if (!archPoints.isDirty() /*&& !displayFileTemplate.ddoc.isDirty()*/)
			return;
		archPoints.resetArchPoints();
		d2dView.repaint();
	}

	public void refreshLengend() {
		if (!displayModel.isStrange()) {
			//dm.selectNowFrame();	
			displayModel.calculatePercentData();
		} else {
			displayModel.calculatePercentDataStrange();
			if (displayModel.getWhichInStrange() == 1)
				//displayModel.selectMaxForceFrame();
				displayModel.setMaxForceFrame();
			if (displayModel.getWhichInStrange() == 2)
				//displayModel.selectDletaFrame();
				displayModel.setDeltaFrame();
		}
	}

	public void refresh(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(LengendPanel.LOWER)) {
			refreshLengend();
		}
		if (evt.getPropertyName().equals(UserPreferencePanel.MAX_OPTION)) {
			if (displayModel.isStrange()) {
				if (displayModel.getWhichInStrange() == 1)
					displayModel.selectMaxForceFrame();
				if (displayModel.getWhichInStrange() == 2)
					displayModel.selectDletaFrame();
			}
		} else if (evt.getPropertyName().equals(UserPreferencePanel.FRAME_OR_PERIOD) || evt.getPropertyName().equals(UserPreferencePanel.THRESHOLD_CONSTANT) || evt.getPropertyName().equals(UserPreferencePanel.TOP_CONSTANT) || evt.getPropertyName().equals(UserPreferencePanel.LEFT_CONSTANT)) {
			if (!displayModel.isStrange()) {
				displayModel.selectNowFrame();
			}
		}
	}

}
