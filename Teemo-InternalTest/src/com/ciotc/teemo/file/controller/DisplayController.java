package com.ciotc.teemo.file.controller;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import com.ciotc.teemo.action.MyAction;
import com.ciotc.teemo.file.model.AbstractModel;
import com.ciotc.teemo.file.model.DisplayModel;
import com.ciotc.teemo.file.template.DisplayFileTemplate;

public class DisplayController implements MouseListener {
	public final static String SUFFIX = "Action";

	public static final String IS_FIRST_CONTACT_PROPERTY = "FirstContact";

	public static final String IS_MAX_AREA_PROPERTY = "MaxArea";

	public static final String IS_MAX_FORCE_PROPERTY = "MaxForce";

	public static final String IS_DELTA_PROPERTY = "Delta";

	public static final String IS_VIEW_2D = "View2D";

	public static final String IS_VIEW_CONTOUR = "ViewContour";

	public static final String IS_COF = "COF";

	public static final String IS_ARCH_MODEL = "ArchModel";

	public static final String IS_FORCE_DIST = "ForceDist";

	public static final String IS_COMMENT = "Comment";

	public static final String IS_SAVE = "Save";

	public static final String IS_RESTORE = "Restore";

	/*	public static final String IS_FRAME_2D="Frame2D";
		
		public static final String IS_FRAME_3D="Frame2D";
		
		public static final String IS_FRAME_GRAPH="FrameGraph";*/

	List<AbstractModel> modellists = new ArrayList<AbstractModel>();

	private DisplayFileTemplate dft = null;

	public void addModel(AbstractModel am) {
		modellists.add(am);
	}

//	public List<AbstractModel> getModellists() {
//		return modellists;
//	}

	public DisplayController(DisplayFileTemplate dft) {
		super();
		this.dft = dft;
	}

	public DisplayFileTemplate getDft() {
		return dft;
	}

	public void perform(MyAction ma) {
		String name = ma.getClass().getSimpleName();

		for (AbstractModel am : modellists) {
			if (am instanceof DisplayModel) {
				DisplayModel dm = (DisplayModel) am;
				if (name.equals(IS_FIRST_CONTACT_PROPERTY + SUFFIX))
					dm.selectfirstFrame();
				else if (name.equals(IS_MAX_AREA_PROPERTY + SUFFIX))
					dm.selectMaxAreaFrame();
				else if (name.equals(IS_MAX_FORCE_PROPERTY + SUFFIX))
					dm.selectMaxForceFrame();
				else if (name.equals(IS_DELTA_PROPERTY + SUFFIX))
					dm.selectDletaFrame();
				else if (name.equals(IS_VIEW_2D + SUFFIX))
					dm.select2DView();
				else if (name.equals(IS_VIEW_CONTOUR + SUFFIX))
					dm.selectContourView();
				else if (name.equals(IS_COF + SUFFIX))
					dm.selectCOF();
				else if (name.equals(IS_ARCH_MODEL + SUFFIX))
					dm.selectArchModel();
				else if (name.equals(IS_FORCE_DIST + SUFFIX))
					dm.selectForceDist();
				else if (name.equals(IS_COMMENT + SUFFIX))
					dft.commentDialog.setVisible(true);
				else if (name.equals(IS_SAVE + SUFFIX))
					dft.save();
				else if (name.equals(IS_RESTORE + SUFFIX))
					dft.restore();
			}
//			if(am instanceof ToolBarModel){
//				ToolBarModel tbm =(ToolBarModel) am;
//				String s = name.substring(0,1).toLowerCase()+name.substring(1,name.length()-6);
//				tbm.setButtonName(s);
//				//tbm.setAction(ma);
//			}

		}
	}

	public void perfomrMovieEvent(String name) {
		for (AbstractModel am : modellists) {
			if (am instanceof DisplayModel) {
				DisplayModel dm = (DisplayModel) am;
				if (name.equals(getResourceString("playforward"))) {
					dm.play();
				} else if (name.equals(getResourceString("stop"))) {
					dm.stop();
				} else if (name.equals(getResourceString("first"))) {
					dm.first();
				} else if (name.equals(getResourceString("last"))) {
					dm.last();
				} else if (name.equals(getResourceString("forward"))) {
					dm.forward();
				} else if (name.equals(getResourceString("slowest"))) {
					dm.slowest();
				} else if (name.equals(getResourceString("mediumSlow"))) {
					dm.mediumSlow();
				} else if (name.equals(getResourceString("nomal"))) {
					dm.nomal();
				} else if (name.equals(getResourceString("mediumFast"))) {
					dm.mediumFast();
				} else if (name.equals(getResourceString("fastest"))) {
					dm.fastest();
				} else if (name.equals(getResourceString("backward"))) {
					dm.backward();
				}
			}
//			if(am instanceof ToolBarModel){
//				ToolBarModel tbm =(ToolBarModel) am;
//				tbm.setButtonName(name);				
//			}

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		dft.status2DField.mouseEntered(e);
		dft.status3DField.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		dft.status2DField.mouseExited(e);
		dft.status3DField.mouseExited(e);
	}

}
