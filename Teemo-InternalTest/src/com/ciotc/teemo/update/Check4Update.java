package com.ciotc.teemo.update;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.ciotc.teemo.util.GeneralUtils;
import com.install4j.api.context.UserCanceledException;
import com.install4j.api.launcher.ApplicationLauncher;
import com.install4j.api.update.ApplicationDisplayMode;
import com.install4j.api.update.UpdateChecker;
import com.install4j.api.update.UpdateDescriptor;

public class Check4Update {

	public final static String CHECK_UPDATE_ID = "1378";

	
	/**
	 * 若有新版本时 是否需要弹出对话框提醒用户更新
	 * 可以配置
	 */
//	public static boolean isShow = true;

	public final static String SHOW_UPDATE = "showUpdate";

	public final static String HTTP_URL = "httpURL";
		
	/**
	 * 从这个地址获取updates.xml
	 * 这个地址需与install4j的地址同步
	 */
	private final static String UPDATE_URL = GeneralUtils.getProp().getString(HTTP_URL);
	
	/**
	 * 后台检查是否有新的版本
	 * @return 有可用的新版本返回true 否则返回false
	 */
	private static boolean check() {
		
		UpdateDescriptor ud = null;
		try {
			ud = UpdateChecker.getUpdateDescriptor(UPDATE_URL, ApplicationDisplayMode.CONSOLE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UserCanceledException e) {
			e.printStackTrace();
		}
		if (ud == null || ud.getPossibleUpdateEntry() == null)
			return false;
		return true;

	}

	/**
	 * 当程序第一次运行的时候调用 
	 * 如果有新版本 并且 需要提醒 那么它会调出检查更新的程序 否则什么也不发生
	 * @param callBack 回调函数 该对象必须实现在程序退出前所作的事情
	 */
	public static void updateBeforeCheck(ApplicationLauncher.Callback callBack, JFrame frame) {

		boolean bool = GeneralUtils.getProp().getString(SHOW_UPDATE).equals("true") ? true : false;
		if (bool) {
			boolean bool1 = check();
			if (bool1) {
				//JOptionPane pane = JOptionPane.showOptionDialog(frame, MyResources.getResourceString("update?"),)
				int answer = JOptionPane.showConfirmDialog(frame, getResourceString("Check4Update"), getResourceString("Teemo"), JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION)
					update(callBack);
			}
		}
	}

	/**
	 * 调用check4update.exe 来检查新版本 并且更新程序
	 * @param callBack 回调函数 该对象必须实现在程序退出前所作的事情
	 */
	private static void update(ApplicationLauncher.Callback callBack) {
		try {
			ApplicationLauncher.launchApplication(CHECK_UPDATE_ID, null, true, callBack);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
//		updateBeforeCheck(new ApplicationLauncher.Callback() {
//
//			@Override
//			public void exited(int arg0) {
//				System.out.println("exited " + arg0);
//			}
//
//			@Override
//			public void prepareShutdown() {
//				System.out.println("prepare shutdown");
//			}
//
//		});
		update(new ApplicationLauncher.Callback() {

			@Override
			public void exited(int arg0) {
				System.out.println("exited " + arg0);
			}

			@Override
			public void prepareShutdown() {
				System.out.println("prepare shutdown");
			}

		});

	}
}
