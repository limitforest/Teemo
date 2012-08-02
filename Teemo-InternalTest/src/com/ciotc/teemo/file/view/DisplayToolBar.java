package com.ciotc.teemo.file.view;

import static com.ciotc.teemo.resource.MyResources.getResourceString;
import static com.ciotc.teemo.resource.MyResources.getResourceURL;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.teemo.action.ActionConstructor;
import com.ciotc.teemo.action.MyAction;
import com.ciotc.teemo.file.controller.DisplayController;
import com.ciotc.teemo.file.model.DisplayModel;
import com.ciotc.teemo.file.model.ToolBarModel;

public class DisplayToolBar extends JToolBar implements ChangeListener {

	private DisplayController displayController;

	private ToolBarFactory toolBarfactory;

	private Map<String, Action> maps;

	private String resourceName;

	public DisplayToolBar(DisplayController controller, String resourceName) {
		this.displayController = controller;
		this.resourceName = resourceName;
		setFloatable(false);
		setRollover(true);
		initToolBar();
	}

	public void initToolBar() {
		toolBarfactory = new ToolBarFactory();
		maps = new HashMap<String, Action>();
		String[] toolKeys = toolBarfactory.getToolKeys();
		for (int i = 0; i < toolKeys.length; i++) {
			if (toolKeys[i].equals("-")) {
				add(Box.createHorizontalStrut(5));
			} else {
				JButton jb = toolBarfactory.createToolbarButton(toolKeys[i]);
				add(jb);
				maps.put(toolKeys[i], jb.getAction());
			}
		}
		add(Box.createHorizontalStrut(50));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		DisplayModel dm = (DisplayModel) e.getSource();
		int index = dm.getShowCOF();
		String str = "cof";
		AbstractAction aa = (AbstractAction) maps.get(str);
		if (aa != null){
			switch (index) {
			case 0:
				ImageIcon icon = new ImageIcon(getResourceURL(str + "Image"));
				aa.putValue(Action.SMALL_ICON, icon);
				break;
			case 1:
				icon = new ImageIcon(getResourceURL(str + "Image1"));
				aa.putValue(Action.SMALL_ICON, icon);
				break;
			case 2:
				icon = new ImageIcon(getResourceURL(str + "Image2"));
				aa.putValue(Action.SMALL_ICON, icon);
				break;
			default:
				break;
			}
		}
		
		if (dm.isStrange()) {
			int which = dm.getWhichInStrange();
			String val = "";
			if(which == 1)
				val = "maxForce";
			else
				val = "delta";
			
			for (String ss : maps.keySet()) {
					maps.get(ss).setEnabled(false);
			}

			
			ImageIcon icon = new ImageIcon(getResourceURL(val + "OpenImage"));
			aa = (AbstractAction) maps.get(val);
			aa.putValue(Action.SMALL_ICON, icon);
			maps.get(val).setEnabled(true);
			
		} else {
			for (String ss : maps.keySet()) {
				if (!ss.equals("frame2D") && !ss.equals("frame3D") && !ss.equals("frameGraph"))
					maps.get(ss).setEnabled(true);
			}
			String vals[] = { "maxForce", "delta" };
			for (String val : vals) {
				ImageIcon icon = new ImageIcon(getResourceURL(val + "Image"));
				aa = (AbstractAction) maps.get(val);
				aa.putValue(Action.SMALL_ICON, icon);
			}

		}
		//特殊的
		String[] vals ={"frame2D","frame3D","frameGraph","comment","save","restore"};
			for(String string:vals){
				if(maps.get(string)!=null)
					maps.get(string).setEnabled(true);
			}
			
		boolean bool = dm.isDirty();
		try{
		if(bool){
			maps.get("save").setEnabled(true);
			maps.get("restore").setEnabled(true);
		}else{
			maps.get("save").setEnabled(false);
			maps.get("restore").setEnabled(false);
		}
		}catch(Exception e2){
			
		}
		
//		ToolBarModel tbm = (ToolBarModel) e.getSource();
//		String kk = tbm.getButtonName();
//		//不好的设计 专门针对cof做判断
//		if (kk.equals("cOF"))
//			kk = "cof";
//		AbstractAction aa = (AbstractAction) maps.get(kk);
//		System.out.println(aa);
//		if (aa == null)
//			return;
//
//		String val = (String) aa.getValue(Action.NAME);
//
//		if (val.equals("maxForce") || val.equals("delta")) {
//			String s = (String) aa.getValue("set");
//
//			//if (s.equals("false")) {
//			ImageIcon icon = new ImageIcon(getResourceURL(val + "OpenImage"));
//			aa.putValue(Action.SMALL_ICON, icon);
//			for (String ss : maps.keySet()) {
//				if (!ss.equals(val) && !ss.equals("frame2D") && !ss.equals("frame3D") && !ss.equals("frameGraph"))
//					maps.get(ss).setEnabled(false);
//			}
//			//	aa.putValue("set", "true");
//
//			//} else {
////				ImageIcon icon = new ImageIcon(getResourceURL(val + "Image"));
////				aa.putValue(Action.SMALL_ICON, icon);
////				for (String ss : maps.keySet()) {
////					if (!ss.equals(val))
////						;//maps.get(ss).setEnabled(true);
////				}
//			//	aa.putValue("set", "false");
//
//			//}
//
//		}
//		if (val.equals("cof")) {
//			String s = (String) aa.getValue("set");
//			if (s.equals("0")) {
//				ImageIcon icon = new ImageIcon(getResourceURL(val + "Image1"));
//				aa.putValue(Action.SMALL_ICON, icon);
//				aa.putValue("set", "1");
//			} else if (s.equals("1")) {
//				ImageIcon icon = new ImageIcon(getResourceURL(val + "Image2"));
//				aa.putValue(Action.SMALL_ICON, icon);
//				aa.putValue("set", "2");
//			} else {
//				ImageIcon icon = new ImageIcon(getResourceURL(val + "Image"));
//				aa.putValue(Action.SMALL_ICON, icon);
//				aa.putValue("set", "0");
//			}
//		}

//		if (s.equals("MaxForceAction") || s.equals("DeltaAction")) {
//			StringBuilder stb = new StringBuilder();
//			stb.append(s.substring(0, 1).toLowerCase());
//			stb.append(s.substring(1, s.length() - 6));
//			String sb = stb.toString();
//			JButton jb = maps.get(sb);
//			if (jb == null)
//				return;
//			if (jb.getActionCommand() == null)
//				return;
//			if (jb.getActionCommand().equals(s)) {
//				jb.setIcon(new ImageIcon(getResourceURL(sb + "OpenImage")));
//				for (String ss : maps.keySet()) {
//					if (!ss.equals(sb)) {
//						maps.get(ss).setEnabled(false);
//					}
//
//				}
//				String sb2 = sb.substring(0, 1).toUpperCase() + sb.substring(1);
//				jb.setActionCommand(sb2 + "OpenAction");
//
////				for(AbstractModel am:displayController.getModellists()){
////					if(am instanceof DisplayModel){
////						((DisplayModel) am).setStrange(true);
////						if(s.equals("MaxForceAction"))
////							((DisplayModel) am).selectMaxForceFrame();
////						if(s.equals("DeltaAction"))
////							((DisplayModel) am).selectDletaFrame();
////					}
////				}
//
//			} else {
//				jb.setIcon(new ImageIcon(getResourceURL(sb + "Image")));
//				for (String ss : maps.keySet()) {
//					if (!ss.equals(sb)) {
//						maps.get(ss).setEnabled(true);
//					}
//
//				}
//				String sb2 = sb.substring(0, 1).toUpperCase() + sb.substring(1);
//				jb.setActionCommand(sb2 + "Action");
//
////				for(AbstractModel am:displayController.getModellists()){
////					if(am instanceof DisplayModel){
////						((DisplayModel) am).setStrange(false);
////						if(s.equals("MaxForceAction"))
////							((DisplayModel) am).selectMaxForceFrame();
////						if(s.equals("DeltaAction"))
////							((DisplayModel) am).selectDletaFrame();
////					}
////				}
//			}
//		}

	}

	/**
	 * This is a factory for create toolbar button and so on.
	 * 
	 * @author Linxiaozhi
	 * 
	 */
	class ToolBarFactory {

		/**
		 * Suffix applied to the key used in resource file lookups for
		 * an image.
		 */
		public static final String imageSuffix = "Image";
		/**
		 * Suffix applied to the key used in resource file lookups for
		 * an action.
		 */
		public static final String actionSuffix = "Action";
		/**
		 * Suffix applied to the key used in resource file lookups for
		 * tooltip text.
		 */
		public static final String tipSuffix = "Tooltip";

		/**
		 * 每个按键详细的说明
		 */
		public static final String detailSuffix = "Detail";
		
		/**
		 * Take the given string and chop it up into a series of strings
		 * on whitespace boundaries. This is useful for trying to get an
		 * array of strings out of the resource file.
		 */
		public String[] tokenize(String input) {
			List<String> v = new ArrayList<String>();
			StringTokenizer t = new StringTokenizer(input);
			String cmd[];

			while (t.hasMoreTokens()) {
				v.add(t.nextToken());
			}
			cmd = new String[v.size()];
			for (int i = 0; i < cmd.length; i++) {
				cmd[i] = v.get(i);
			}

			return cmd;
		}

		public String[] getToolKeys() {
			return tokenize(getResourceString(resourceName));
		}

		public JButton createToolbarButton(String key) {

			JButton b = new JButton() {
				private static final long serialVersionUID = 1L;

				@Override
				public float getAlignmentY() {
					return 0.5f;
				}
			};
			b.addMouseListener(displayController);
			Action a = null;
			try {
				String astr = getResourceString(key + actionSuffix);
				if (astr == null) {
					astr = key;
				}
				URL url = getResourceURL(key + imageSuffix);
				a = ActionConstructor.constructAction(astr, displayController, key, new ImageIcon(url));
				
				//actionMaps.put(key, a);
			} catch (Exception e) {
				a = null;
			}
			String detail = getResourceString(key+detailSuffix);
			b.putClientProperty("detail", detail);
			b.setFocusPainted(false);
			b.setAction(a);
			b.setText("");
//			JButton b = new JButton(new ImageIcon(url)) {
//
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public float getAlignmentY() {
//					return 0.5f;
//				}
//			};
//			b.setRequestFocusEnabled(false);
//			b.setMargin(new Insets(1, 1, 1, 1));
//
//			String astr = getResourceString(key + actionSuffix);
//			if (astr == null) {
//				astr = key;
//			}
//			Action a = null;
//			try {
//				a = ActionConstructor.constructAction(astr, displayController);
//			} catch (Exception e) {
//				a = null;
//			}
//			if (a != null) {
//				b.setActionCommand(astr);
//				// It's only for maxForce and delta
//				a.putValue("enable", false);
//				b.addActionListener(a);
//			} else {
//				b.setEnabled(false);
//			}

			String tip = getResourceString(key + tipSuffix);
			if (tip != null) {
				b.setToolTipText(tip);
			}

			return b;
		}
	}

}
