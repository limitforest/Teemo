package com.ciotc.teemo.frame;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.commons.lang.time.DateFormatUtils;
import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.service.PatientsService;
import com.ciotc.teemo.util.Constant;
import com.ciotc.teemo.util.IdCard;
import static com.ciotc.teemo.resource.MyResources.*;
import com.ciotc.teemo.util.CheckLanguage;
import com.ciotc.teemo.util.textfield.TeemoJTextField;
import com.ciotc.teemo.util.IdCard;
import com.ciotc.teemo.util.CheckInfo;

/*
 * Description: NewPantientsFrame.
 * author@ pan
 * EditTime：2011.10.6
 * */

public class NewPatientsDialog extends JDialog {
private JPanel pnlBase, pnlPntInfo;
private JButton btnOK, btnRedo;
private JLabel lblPntName, lblPntID, lblPntBirth, lblPntAge, lblPntGender, lblPntTeeth, lblUnit;
private TeemoJTextField txtPntName, txtPntAge, txtPntID, txtPntTeeth, txtPntBirthY, txtPntBirthM,
		txtPntBirthD;
private JRadioButton rbtnMale, rbtnFemale;
private ButtonGroup btngGender;
private String birthday;

Patients pnt = null;
PatientsFrame pntFrame = null;
private IdCard idCardStr;

private static int idCardFlag;
private static String pntIDCard;
private static int infoCheckFlag;

public NewPatientsDialog(final PatientsFrame pntFrame, final String frmName) {
	// Properties of the NewPatientsDialog.
	super(pntFrame, frmName, true);
	this.pntFrame = pntFrame;
	this.setSize(500, 230);
	this.setLocationRelativeTo(null);
	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	this.setResizable(false);
	// this.pack();
	
	pnlBase = new JPanel();
	pnlBase.setLayout(null);
	// pnlBase.setBackground(pnlBaseClr);
	pnlBase.setVisible(true);
	pnlBase.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	
	pnlPntInfo = new JPanel();
	pnlPntInfo.setLayout(null);
	// pnlPntInfo.setBackground(pnlPntInfoClr);
	pnlPntInfo.setVisible(true);
	pnlPntInfo.setBounds(0, 0, 494, 200);
	pnlPntInfo.setBorder(BorderFactory.createCompoundBorder(BorderFactory
			.createTitledBorder(getResourceString("NewPatientsDialog.patientsInformation")),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	
	lblPntID = new JLabel(getResourceString("IDCardInformation"));
	lblPntID.setFont(Constant.font);
	lblPntID.setBounds(170, 20, 135, 30);
	lblPntID.setVisible(true);
	
	txtPntID = new TeemoJTextField();
	txtPntID.setFont(Constant.font);
	txtPntID.setBounds(170, 50, 135, 30);
	txtPntID.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	txtPntID.setDocument(new JTextFieldLimit(18));
	txtPntID.setNumberOnly(true);
	
	txtPntID.getDocument().addDocumentListener(new OnValueChanged());
	
	lblPntName = new JLabel(getResourceString("nameInformation"));
	lblPntName.setFont(Constant.font);
	lblPntName.setBounds(10, 20, 80, 30);
	lblPntName.setVisible(true);
	
	txtPntName = new TeemoJTextField();
	txtPntName.setFont(Constant.font);
	txtPntName.setBounds(10, 50, 100, 30);
	txtPntName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	
	lblPntBirth = new JLabel(getResourceString("BirthInformation"));
	lblPntBirth.setFont(Constant.font);
	lblPntBirth.setBounds(170, 90, 145, 30);
	lblPntBirth.setVisible(true);
	
	// txtPntBirth.setBounds(170, 120, 135, 30);
	txtPntBirthY = new TeemoJTextField();
	txtPntBirthY.setFont(Constant.font);
	txtPntBirthY.setBounds(170, 120, 40, 30);
//	txtPntBirthY.setMaxTextLength(4);
	txtPntBirthY.setDocument(new JTextFieldLimit(4));
	txtPntBirthY.setNumberOnly(true);
	
	txtPntBirthM = new TeemoJTextField();
//	txtPntBirthM.setMaxTextLength(2);
	txtPntBirthM.setFont(Constant.font);
	txtPntBirthM.setBounds(215, 120, 40, 30);
	txtPntBirthM.setDocument(new JTextFieldLimit(2));
	txtPntBirthM.setNumberOnly(true);
	
	txtPntBirthD = new TeemoJTextField();
//	txtPntBirthY.setMaxTextLength(4);
	txtPntBirthD.setFont(Constant.font);
	txtPntBirthD.setBounds(260, 120, 40, 30);
	txtPntBirthD.setNumberOnly(true);
	txtPntBirthD.setDocument(new JTextFieldLimit(2));
	
	txtPntBirthY.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	txtPntBirthM.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	txtPntBirthD.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	
	lblPntAge = new JLabel(getResourceString("ageInformation"));
	lblPntAge.setFont(Constant.font);
	lblPntAge.setBounds(350, 20, 80, 30);
	lblPntAge.setVisible(true);
	
	txtPntAge = new TeemoJTextField();
	txtPntAge.setFont(Constant.font);
	txtPntAge.setBounds(350, 50, 60, 30);
	txtPntAge.setNumberOnly(true);
	txtPntAge.setDocument(new JTextFieldLimit(3));
	
	txtPntAge.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	
	lblPntGender = new JLabel(getResourceString("sexInformation"));
	lblPntGender.setFont(Constant.font);
	lblPntGender.setBounds(10, 90, 135, 30);
	lblPntGender.setVisible(true);
	
	rbtnMale = new JRadioButton(getResourceString("male"), true);
	rbtnMale.setBounds(10, 120, 60, 30);
	// rbtnMale.setBounds(10, 120, 50, 30);
	rbtnMale.setFont(Constant.font);
	
	rbtnFemale = new JRadioButton(getResourceString("female"));
	rbtnFemale.setBounds(60, 120, 66, 30);
	rbtnFemale.setFont(Constant.font);
	
	btngGender = new ButtonGroup();
	rbtnMale.setOpaque(false);
	rbtnFemale.setOpaque(false);
	btngGender.add(rbtnMale);
	btngGender.add(rbtnFemale);
	
	lblPntTeeth = new JLabel(getResourceString("arch") + " :");
	
	lblPntTeeth.setFont(Constant.font);
	lblPntTeeth.setBounds(350, 90, 120, 30);
	lblPntTeeth.setVisible(true);
	
	txtPntTeeth = new TeemoJTextField();
	txtPntTeeth.setFont(Constant.font);
	txtPntTeeth.setText("8.5");
	txtPntTeeth.setNumberOnly(true);
	txtPntTeeth.setBounds(350, 120, 40, 30);
	txtPntTeeth.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	
	lblUnit = new JLabel(getResourceString("mm"));
	lblUnit.setFont(Constant.font);
	lblUnit.setBounds(390, 120, 30, 30);
	lblUnit.setVisible(true);
	
	btnOK = new JButton(getResourceString("ok"));
	btnOK.setFont(Constant.font);
	btnOK.setEnabled(true);
	btnOK.setBounds(400, 160, 80, 30);
	btnOK.setMargin(new Insets(1, 1, 1, 1));
	btnOK.setMnemonic(KeyEvent.VK_ENTER);
	// 加入对回车键的支持。
	
	btnOK.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			// 身份证为空时，不进行校验。
			if (txtPntID.getText().trim().length() == 0) {
				creatPntNoIdCheck();
			} else {
				// 进行身份证校验的插入。
				creatPntID(txtPntID.getText().trim());
			}
			pntFrame.getLstObj().setListData(pntFrame.pntObjList());
		}
		
	});
	
	btnRedo = new JButton(getResourceString("reset"));
	btnRedo.setFont(Constant.font);
	btnRedo.setEnabled(true);
	btnRedo.setBounds(300, 160, 80, 30);
	btnRedo.setMargin(new Insets(1, 1, 1, 1));
	btnRedo.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			// Add methods to deal with the action.
			clearText();
		}
	});
	
	pnlPntInfo.add(lblPntName);
	pnlPntInfo.add(lblPntID);
	pnlPntInfo.add(lblPntBirth);
	pnlPntInfo.add(lblPntAge);
	pnlPntInfo.add(lblPntGender);
	pnlPntInfo.add(lblPntTeeth);
	pnlPntInfo.add(lblUnit);
	
	pnlPntInfo.add(txtPntName);
	pnlPntInfo.add(txtPntBirthY);
	pnlPntInfo.add(txtPntBirthM);
	pnlPntInfo.add(txtPntBirthD);
	
	pnlPntInfo.add(txtPntID);
	pnlPntInfo.add(txtPntAge);
	pnlPntInfo.add(txtPntTeeth);
	
	pnlPntInfo.add(btnOK);
	pnlPntInfo.add(btnRedo);
	
	pnlPntInfo.add(rbtnMale);
	pnlPntInfo.add(rbtnFemale);
	
	pnlBase.add(pnlPntInfo);
	this.getContentPane().add(pnlBase);
	pnt = new Patients();
	idCardStr = new IdCard();
}

public NewPatientsDialog() {
	
}

private boolean checkPntInfoId() {
	// 验证输入患者信息的合法性。
	// infoCheckFlag = 0;
	if (txtPntName.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog(null, getResourceString("NewPatientsDialog.checkPntInfo1"));
		return false;
	} else if (txtPntID.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog(null, getResourceString("NewPatientsDialog.checkPntInfo2"));
		return false;
	} else if (txtPntAge.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog(null, getResourceString("NewPatientsDialog.checkPntInfo3"));
		return false;
	} else if (txtPntBirthY.getText().trim().length() == 0 || txtPntBirthM.getText().trim().length() == 0
			|| txtPntBirthD.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog(null, getResourceString("NewPatientsDialog.checkPntInfo4"));
		return false;
	} else if (txtPntTeeth.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog(null, getResourceString("NewPatientsDialog.checkPntInfo5"));
		return false;
	} else {
		// 信息完整性校验正确。
		return true;
	}
}

private boolean checkPntInfoNoId() {
	// 验证输入患者信息的合法性。
	// infoCheckFlag = 0;
	if (txtPntName.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog(null, getResourceString("NewPatientsDialog.checkPntInfo1"));
		return false;
	}
	// 身份证字段空。
	else if (txtPntAge.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog(null, getResourceString("NewPatientsDialog.checkPntInfo3"));
		return false;
	} else if (txtPntBirthY.getText().trim().length() == 0 || txtPntBirthM.getText().trim().length() == 0
			|| txtPntBirthD.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog(null, getResourceString("NewPatientsDialog.checkPntInfo4"));
		return false;
	} else if (txtPntTeeth.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog(null, getResourceString("NewPatientsDialog.checkPntInfo5"));
		return false;
	} else {
		// infoCheckFlag = 1;
		// 信息完整性校验正确。
		return true;
	}
}

protected void setPntAge() {
	txtPntAge.setText(idCardStr.getAgeFrmIdCard(pntIDCard));
}

protected void setPntBirth() {
	// 获取身份证上的出生年月。
	String birthFromId = idCardStr.getBirthFrmIdCard(pntIDCard);
	// 15位身份证号
	if (pntIDCard.length() == 15) {
		txtPntBirthY.setText("19" + pntIDCard.substring(6, 8));
		txtPntBirthM.setText(pntIDCard.substring(8, 10));
		txtPntBirthD.setText(pntIDCard.substring(10, 12));
	} else {
		// 18位身份证号
		txtPntBirthY.setText(pntIDCard.substring(6, 10));
		txtPntBirthM.setText(pntIDCard.substring(10, 12));
		txtPntBirthD.setText(pntIDCard.substring(12, 14));
	}
}

protected void setPntGender() {
	if (idCardStr.getPntGender(pntIDCard) == 1) {
		rbtnMale.setSelected(true);
	} else {
		rbtnFemale.setSelected(true);
	}
}

// 进行身份证校验。
private void creatPntID(String idCard) {
	// 加入逻辑判断。每个字段都要非空。
	if (checkPntInfoId() && new IdCard().IDCardValidate(idCard)) {
		// 身份证填写后就不能在修改
		// 数据校验正确，插入数据库.
		pnt.setName(txtPntName.getText().trim());
		pnt.setIdcard(pntIDCard);
		pnt.setAge(Integer.parseInt(txtPntAge.getText().trim()));
		String pntGender = rbtnMale.isSelected() ? "男" : "女";
		// Let gender=1 represents "男".
		int gender = 1;
		if (pntGender.equals("男")) {
			gender = 1;
			pnt.setGender(gender);
		} else {
			gender = 0;
			pnt.setGender(gender);
		}
		pnt.setArch(Float.parseFloat(txtPntTeeth.getText().trim()));
		pnt.setCreatetime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		pnt.setBirth(txtPntBirthY.getText().trim() + "-" + txtPntBirthM.getText().trim() + "-"
				+ txtPntBirthD.getText().trim());
		try {
			PatientsService pntService = PatientsService.getInstance();
			pntService.insertSelective(pnt);
			JOptionPane.showMessageDialog(null,
					getResourceString("NewPatientsDialog.insertSuccess"));
			// pntService.
			this.dispose();
			pntFrame.mainFrame.setVisible(true);
			if (!pntFrame.isVisible())
				pntFrame.setVisible(true);
			else {
				if (pntFrame.getExtendedState() != JFrame.NORMAL) // JFrame.NORMAL
																	// == 0
																	// 判断是否最小化
					pntFrame.setExtendedState(JFrame.NORMAL);
				if (!pntFrame.isActive())
					pntFrame.setVisible(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} else {
JOptionPane.showMessageDialog(null, "身份证号码不合法，请重新输入。");
		//		txt获取焦点。
//		txtPntID.setText("");
//		txtPntID.setFocusable(true);
		txtPntID.requestFocus();
//		txtPntID.setF
	}
}

private void setUnedit() {
	// TODO Auto-generated method stub
	txtPntAge.setEditable(false);
	rbtnMale.setEnabled(false);
	rbtnFemale.setEnabled(false);
	txtPntBirthY.setEditable(false);
	txtPntBirthM.setEditable(false);
	txtPntBirthD.setEditable(false);
}

// 不进行身份证校验。
private void creatPntNoIdCheck() {
	// TODO Auto-generated method stub
	// 检验填写信息的完整性。
	if (checkPntInfoNoId()) {
		// 填入数据库。
		pnt.setName(txtPntName.getText().trim());
		pnt.setIdcard(txtPntID.getText().trim());
		// 对年龄的校验
		if (Integer.parseInt(txtPntAge.getText().trim()) > 1
				&& Integer.parseInt(txtPntAge.getText().trim()) < 100) {
			pnt.setAge(Integer.parseInt(txtPntAge.getText().trim()));
		} else {
			JOptionPane.showMessageDialog(null, "年龄范围1——110.");
			txtPntAge.setText("");
			return;
		}
		String pntGender = rbtnMale.isSelected() ? "男" : "女";
		// Let gender=1 represents "男".
		int gender = 1;
		if (pntGender.equals("男")) {
			gender = 1;
			pnt.setGender(gender);
		} else {
			gender = 0;
			pnt.setGender(gender);
		}
		
		String birthY = txtPntBirthY.getText().trim();
		String birthM = txtPntBirthM.getText().trim();
		String birthD = txtPntBirthD.getText().trim();
		birthday = birthY + "-" + birthM + "-" + birthD;
		
		if (CheckInfo.compareDateToNow(birthday)) {
			if (CheckInfo.isBirthday(birthday)) {
//System.out.println(birthday);
				pnt.setBirth(birthday);
			} else {
				JOptionPane.showMessageDialog(null, "请输入正确的格式。如：2010-01-01");
				txtPntBirthY.requestFocus();
				return;
			}
		} else {
			return;
		}
		pnt.setArch(Float.parseFloat(txtPntTeeth.getText().trim()));
		pnt.setCreatetime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		try {
			PatientsService pntService = PatientsService.getInstance();
			pntService.insertSelective(pnt);
			JOptionPane.showMessageDialog(null,
					getResourceString("NewPatientsDialog.insertSuccess"));
			clearText();
			// pntService.
			this.dispose();
			pntFrame.mainFrame.setVisible(true);
			if (!pntFrame.isVisible())
				pntFrame.setVisible(true);
			else {
				if (pntFrame.getExtendedState() != JFrame.NORMAL) // JFrame.NORMAL
																	// == 0
																	// 判断是否最小化
					pntFrame.setExtendedState(JFrame.NORMAL);
				if (!pntFrame.isActive())
					pntFrame.setVisible(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} else {
	}
}



public void clearText() {
	txtPntName.setText("");
	txtPntAge.setText("");
	txtPntID.setText("");
	txtPntBirthY.setText("");
	txtPntBirthM.setText("");
	txtPntBirthD.setText("");
	txtPntTeeth.setText("8.5");
}

class JTextFieldLimit extends PlainDocument {
private int limit;

JTextFieldLimit(int limit) {
	super();
	this.limit = limit;
}

JTextFieldLimit(int limit, boolean upper) {
	super();
	this.limit = limit;
}

public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
	if (str == null)
		return;
	if ((getLength() + str.length()) <= limit) {
		super.insertString(offset, str, attr);
	}
}
}


class OnValueChanged implements DocumentListener {
public void insertUpdate(DocumentEvent e) {
	if (checkPntIdCard()) {
		// 在未填写年龄、性别、生日的情况下，自动根据身份证填写。
		if (txtPntAge.getText().trim().length() == 0) {
			setPntAge();
		}
		setPntGender();
		if (txtPntBirthY.getText().trim().length() == 0 && txtPntBirthM.getText().trim().length() == 0
				&& txtPntBirthD.getText().trim().length() == 0) {
			setPntBirth();
		}
		setUnedit();
	}
}

public void removeUpdate(DocumentEvent e) {
	if (checkPntIdCard()) {
		if (txtPntAge.getText().trim().length() == 0) {
			setPntAge();
		}
		setPntGender();
		if (txtPntBirthY.getText().trim().length() == 0 && txtPntBirthM.getText().trim().length() == 0
				&& txtPntBirthD.getText().trim().length() == 0) {
			setPntBirth();
		}
		setUnedit();
	} else {
		setEdit();
	}
}

public void changedUpdate(DocumentEvent e) {
	if (checkPntIdCard()) {
		if (txtPntAge.getText().trim().length() == 0) {
			setPntAge();
		}
		setPntGender();
		if (txtPntBirthY.getText().trim().length() == 0 && txtPntBirthM.getText().trim().length() == 0
				&& txtPntBirthD.getText().trim().length() == 0) {
			setPntBirth();
		}
		setUnedit();
	} else {
		setEdit();
	}
}

private  boolean checkPntIdCard() {
	if (txtPntID.getText().trim().length() == 18) {
		pntIDCard = txtPntID.getText().trim();
		if (new IdCard().IDCardValidate(pntIDCard)) {
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "身份证不合法。请核实。");
			return false;
		}
	} else if (txtPntID.getText().trim().length() == 15) {
		pntIDCard = txtPntID.getText().trim();
		if (new IdCard().IDCardValidate(pntIDCard)) {
			return true;
		} else {
			return false;
		}
	}
	return false;
}
}

private void setEdit() {
	txtPntAge.setEditable(true);
	rbtnMale.setEnabled(true);
	rbtnFemale.setEnabled(true);
	txtPntBirthY.setEditable(true);
	txtPntBirthM.setEditable(true);
	txtPntBirthD.setEditable(true);
	
}
}
