package com.ciotc.teemo.menu;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ciotc.teemo.util.Constant;
import com.ciotc.teemo.util.GeneralUtils;

public class NetworkPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String aet;
	public String host;
	public int port;
	/**
	 * 默认值
	 */
	public final static String AET = "DCM4CHEE";
	public final static String HOST = "192.168.110.92";
	public final static int PORT = 11112;

	// JTextArea aetText = null;
	// JTextArea destIPText = null;
	// JTextArea destPortText = null;
	JLabel aetText = null;
	JLabel destIPText = null;
	JLabel destPortText = null;
	JTextField aetField = null;
	JTextField destIPField = null;
	JTextField destPortField = null;

	JPanel jp1 = null;
	JPanel jp2 = null;

	//JButton ok = null;
	//JButton cancel = null;
	//MainFrame mainFrame = null;

	public NetworkPanel() {
		//super(parent);
		//this.mainFrame = parent;
		//setTitle(getResourceString("networkDialog.title"));
		//setSize(200, 135);
		//setLocation(parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2, parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		//setAlwaysOnTop(true);
		//setResizable(false);
		// setLayout(new GridLayout(4,2));

		//JPanel panel = new JPanel();
		//panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), getResourceString("networkDialog.title")));
		setLayout(null);
		//setBorder(BorderFactory.createEmptyBorder(50, 50, 0, 0));
		int x = 8;
		int y = 10;

		aetText = new JLabel(getResourceString("networkDialog.aet"));
		aetText.setBorder(BorderFactory.createEmptyBorder());
		aetText.setBounds(5 + x, 0 + y, 100, 20);
		aetField = new JTextField(30);
		aetField.setBounds(80 + x, 0 + y, 100, 20);
		destIPText = new JLabel(getResourceString("networkDialog.ip"));
		destIPText.setBorder(BorderFactory.createEmptyBorder());
		destIPText.setBounds(5 + x, 25 + y, 100, 20);
		destIPField = new JTextField(15);
		destIPField.setBounds(80 + x, 25 + y, 100, 20);
		destPortText = new JLabel(getResourceString("networkDialog.port"));
		destPortText.setBorder(BorderFactory.createEmptyBorder());
		destPortText.setBounds(5 + x, 50 + y, 100, 20);
		destPortField = new JTextField(5);
		destPortField.setBounds(80 + x, 50 + y, 100, 20);
		//ok = new JButton(getResourceString("ok"));
		//ok.setBounds(25, 75, 70, 25);
		//cancel = new JButton(getResourceString("cancel"));
		//cancel.setBounds(110, 75, 70, 25);
		//setArgs();

		add(aetText);
		add(aetField);
		add(destIPText);
		add(destIPField);
		add(destPortText);
		add(destPortField);

		aet = GeneralUtils.getProp().getString(Constant.CONFIG_KEY_PACS_AET);
		host = GeneralUtils.getProp().getString(Constant.CONFIG_KEY_PACS_HOST);
		port = GeneralUtils.getProp().getInt(Constant.CONFIG_KEY_PACS_PORT);

		load();

		//setLayout(new BorderLayout());
		//add(panel);
		//add(ok);
		//add(cancel);

//			ok.addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					String aetP = OptionMenu.aet;
//					String hostP = OptionMenu.host;
//					int portP = OptionMenu.port;
//					String aetT = aetP;
//					String hostT = hostP;
//					String portStr = null;
//					int portT = portP;
//					boolean isClean = true;
//					try {
//						aetT = aetField.getText();
//						portStr = destPortField.getText();
//						hostT = destIPField.getText();
//						try {
//							checkIP(hostT);
//						} catch (NumberRangeException e1) {
//							// e1.printStackTrace();
//							JOptionPane.showMessageDialog(mainFrame, e1.getMsg(), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//							// hostT = hostP;
//							isClean = false;
//						}
//						try {
//							portT = Integer.parseInt(portStr);
//						} catch (NumberFormatException e1) {
//							// e1.printStackTrace();
//							JOptionPane.showMessageDialog(mainFrame, getResourceString("networkDialog.portException"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//							isClean = false;
//						}
//						try {
//							checkHost(portT);
//						} catch (NumberRangeException e1) {
//							// e1.printStackTrace();
//							JOptionPane.showMessageDialog(mainFrame, e1.getMsg(), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//							portT = portP;
//							isClean = false;
//						}
//					} finally {
//
//						if (isClean == false) {
//							aetField.setText(aetT);
//							destIPField.setText(hostT);
//							destPortField.setText(portStr);
//						} else {
//							OptionMenu.aet = aetT;
//							OptionMenu.host = hostT;
//							OptionMenu.port = portT;
//							dispose();
//						}
//					}
//
//				}
//
//			});

//			cancel.addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					dispose();
//				}
//
//			});

	}

	public void load() {
		aetField.setText(aet);
		destIPField.setText(host);
		destPortField.setText(String.valueOf(port));
	}

	public void apply() {
		String aetP = aet;
		String hostP = host;
		int portP = port;
		String aetT = aetP;
		String hostT = hostP;
		String portStr = null;
		int portT = portP;
		boolean isClean = true;
		try {
			aetT = aetField.getText();
			portStr = destPortField.getText();
			hostT = destIPField.getText();
			try {
				checkIP(hostT);
			} catch (NumberRangeException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(this, e1.getMsg(), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
				// hostT = hostP;
				isClean = false;
			}
			try {
				portT = Integer.parseInt(portStr);
			} catch (NumberFormatException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(this, getResourceString("networkDialog.portException"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
				isClean = false;
			}
			try {
				checkHost(portT);
			} catch (NumberRangeException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(this, e1.getMsg(), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
				portT = portP;
				isClean = false;
			}
		} finally {

			if (isClean == false) {
				aetField.setText(aetT);
				destIPField.setText(hostT);
				destPortField.setText(portStr);
			} else {
				aet = aetT;
				host = hostT;
				port = portT;
				GeneralUtils.getProp().setProperty(Constant.CONFIG_KEY_PACS_AET, aet);
				GeneralUtils.getProp().setProperty(Constant.CONFIG_KEY_PACS_HOST, host);
				GeneralUtils.getProp().setProperty(Constant.CONFIG_KEY_PACS_PORT, port);

				//dispose();
			}
		}
	}

	public void setDefault() {
		aetField.setText(AET);
		destIPField.setText(HOST);
		destPortField.setText(String.valueOf(PORT));
	}

//	public void setArgs() {
//		aetField.setText(OptionMenu.aet);
//		destIPField.setText(OptionMenu.host);
//		destPortField.setText(String.valueOf(OptionMenu.port));
//	}

	public void checkIP(String str) throws NumberRangeException {
		String[] strs = null;
		try {
			strs = str.trim().split("\\.");
		} catch (PatternSyntaxException e) {
			throw new NumberRangeException(e.getMessage());
		}
		if (strs.length != 4)
			throw new NumberRangeException(getResourceString("networkDialog.rightIP"));
		int num = 0;
		for (String s : strs) {
			try {
				num = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				throw new NumberRangeException(getResourceString("networkDialog.rightInt"));
			}
			if (num < 0 || num > 255)
				throw new NumberRangeException(getResourceString("networkDialog.rightIntRange"));
		}
	}

	public void checkHost(int port) throws NumberRangeException {
		if (port < 0 || port > 65535)
			throw new NumberRangeException(getResourceString("networkDialog.rightIntRangePort"));
	}

}

class NumberRangeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String msg = null;

	public NumberRangeException(String message) {
		this.msg = message;
	}

	public String getMsg() {
		return msg;
	}
}