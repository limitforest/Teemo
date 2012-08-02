package com.ciotc.teemo.frame;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.util.Constant;
import static com.ciotc.teemo.resource.MyResources.getResourceString;

public class CommentsDialog extends JDialog {
	private JPanel pnlBase, pnlLabel, pnlBtn, pnlEdit, pnlTxtComnts;
	private JButton btnOK, btnCancel, /* btnHelp, */btnClearAll, /* btnChange, */
			btnEdit;
	private JLabel lblName, lblId, lblIdCard, lblBirth, lblGender, lblArch,
			lblComments;
	private JTextField txtName, txtId, txtIdCard, txtBirth, txtGender, txtArch;
	private JList lstEdit;
	private JTextArea txtAreaComnt;
	private JScrollPane scrllComments, scrllEdit;
	private EditDialog editDialogObj;

	DisplayFileTemplate displayFileTemplate;

	private Patients patient;

	public CommentsDialog(final DisplayFileTemplate dft, String name) {
		// super(mainFrm, "影片注解- -" + "患者Id：" + pntId + "- - 影片Id: " + movId
		// + "- -" + desc, true);

//		super(dft.desktopPane.mainFrame, name, true);
		super(dft.getMainFrame(), name, true);
		this.displayFileTemplate = dft;

		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
		// setSize is necessary to show the dialog.
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);

		pnlBase = new JPanel();
		pnlBase.setSize(500, 500);
		pnlBase.setLayout(null);
		pnlBase.setVisible(true);

		pnlLabel = new JPanel();
		pnlLabel.setBounds(2, 2, 230, 220);
		pnlLabel.setLayout(null);
		pnlLabel.setVisible(true);

		pnlEdit = new JPanel();
		pnlEdit.setBounds(2, 222, 230, 200);
		// pnlEdit.setSize(230, 200);
		pnlEdit.setLayout(null);
		// pnlEdit.setBackground(pnlEditClr);
		pnlEdit.setVisible(true);
		// pnlEdit.setBorder(BorderFactory.createCompoundBorder(
		// BorderFactory.createTitledBorder(""),
		// BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		pnlTxtComnts = new JPanel();
		pnlTxtComnts.setBounds(234, 0, 260, 420);
		// pnlTxtComnts.setSize(260, 300);
		pnlTxtComnts.setLayout(null);
		// pnlTxtComnts.setBackground(pnlTxtComntsClr);
		pnlTxtComnts.setVisible(true);
		// pnlTxtComnts.setBorder(BorderFactory.createCompoundBorder(
		// BorderFactory.createTitledBorder(""),
		// BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		pnlBtn = new JPanel();
		pnlBtn.setBounds(2, 430, 490, 30);
		// pnlBtn.setSize(490, 50);
		pnlBtn.setLayout(new FlowLayout());
		// pnlBtn.setBackground(pnlBtnClr);
		pnlBtn.setVisible(true);
		// pnlBtn.setBorder(BorderFactory.createCompoundBorder(
		// BorderFactory.createTitledBorder(""),
		// BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		lblName = new JLabel(getResourceString("nameInformation"));
		lblName.setFont(Constant.font);
		// lblName.setSize(80, 30);
		lblName.setBounds(2, 4, 80, 30);
		lblName.setVisible(true);

		lblId = new JLabel("\t" + getResourceString("IDInformation"));
		lblId.setFont(Constant.font);
		lblId.setBounds(2, 39, 80, 30);
		// lblId.setSize(80, 30);
		lblId.setVisible(true);

		lblIdCard = new JLabel("" + getResourceString("IDCardInformation"));
		lblIdCard.setFont(Constant.font);
		lblIdCard.setBounds(2, 74, 90, 30);
		// lblIdCard.setSize(80, 30);
		lblIdCard.setVisible(true);

		lblBirth = new JLabel("		" + getResourceString("BirthInformation2"));
		lblBirth.setFont(Constant.font);
		lblBirth.setBounds(2, 109, 80, 30);
		// lblBirth.setSize(80, 30);
		lblBirth.setVisible(true);

		lblGender = new JLabel("	" + getResourceString("sexInformation"));
		lblGender.setFont(Constant.font);
		lblGender.setBounds(2, 144, 80, 30);
		// lblGender.setSize(80, 30);
		lblGender.setVisible(true);

		lblArch = new JLabel("" + getResourceString("commentsDialog.arch"));
		lblArch.setFont(Constant.font);
		lblArch.setBounds(2, 179, 80, 30);
		// lblArch.setSize(80, 30);
		lblArch.setVisible(true);

		txtName = new JTextField();
		txtName.setFont(Constant.font);
		// txtName.setSize(150, 30);
		txtName.setBounds(90, 8, 140, 30);
		txtName.setEditable(false);
		txtName.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		txtId = new JTextField();
		txtId.setFont(Constant.font);
		txtId.setBounds(90, 43, 140, 30);
		// txtId.setSize(150, 30);
		txtId.setEditable(false);
		txtId.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		txtIdCard = new JTextField();
		txtIdCard.setFont(Constant.font);
		txtIdCard.setBounds(90, 78, 140, 30);
		// txtIdCard.setSize(150, 30);
		txtIdCard.setEditable(false);
		txtIdCard.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		txtBirth = new JTextField();
		txtBirth.setFont(Constant.font);
		txtBirth.setBounds(90, 113, 140, 30);
		// txtBirth.setSize(150, 30);
		txtBirth.setEditable(false);
		txtBirth.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		txtGender = new JTextField();
		txtGender.setFont(Constant.font);
		txtGender.setBounds(90, 148, 140, 30);
		// txtGender.setSize(150, 30);
		txtGender.setEditable(false);
		txtGender.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		txtArch = new JTextField();
		txtArch.setFont(Constant.font);
		txtArch.setBounds(90, 183, 140, 30);
		// txtArch.setSize(150, 30
		txtArch.setEditable(false);
		txtArch.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		btnEdit = new JButton(getResourceString("editCommentDialog"));
		btnEdit.setFont(Constant.font);
		btnEdit.setBounds(0, 2, 140, 30);
		btnEdit.setVisible(true);
		btnEdit.setEnabled(true);
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				/*
				 * String [] lstElements; for(int i=0;i<
				 * lstEdit.getVisibleRowCount();i++){ lstElements[i] =
				 * lstEdit.get
				 * 
				 * }
				 */
				new EditDialog(CommentsDialog.this).setVisible(true);
			}
		});

		setLstEdit(new JList());
		getLstEdit().setBounds(0, 32, 220, 170);
		getLstEdit().setEnabled(true);
		getLstEdit().setFont(Constant.font);
		getLstEdit().setFixedCellWidth(180);
		getLstEdit().setFixedCellHeight(21);
		// setListData display the obj in the JList, so toString() in class
		// Patients should be override to display the pnt name only.
		editDialogObj = new EditDialog();
		getLstEdit().setListData(editDialogObj.getRecords().toArray());
		getLstEdit().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (getLstEdit().getSelectedIndex() != -1) {
					if (e.getClickCount() == 2) {
						txtAreaComnt.setText(txtAreaComnt.getText()
								+ getLstEdit().getSelectedValue().toString());
					}
				}
			}
		});

		lblComments = new JLabel(getResourceString("movieComment"));
		lblComments.setFont(Constant.font);
		lblComments.setBounds(4, 2, 120, 30);
		lblComments.setVisible(true);

		txtAreaComnt = new JTextArea();
		txtAreaComnt.setText(displayFileTemplate.ddoc.getpComment());
		// txtAreaComnt.setBounds(2, 30, 234, 390);
		// txtAreaComnt.setSize(230,390);
		txtAreaComnt.setEditable(true);
		txtAreaComnt.setWrapStyleWord(true);
		txtAreaComnt.setLineWrap(true);
		txtAreaComnt.setFont(Constant.font);
		txtAreaComnt.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		// scrllComments = new JScrollPane(txtAreaComnt);
		scrllComments = new JScrollPane();
		// scrllComments.setFont(Constant.font);
		scrllComments
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scrllComments.getVerticalScrollBar().setValue(txtAreaComnt.getHeight());
		scrllComments.setBounds(2, 30, 250, 390);
		scrllComments.getViewport().add(txtAreaComnt);

		scrllEdit = new JScrollPane();
		scrllEdit
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrllEdit.setBounds(2, 34, 220, 160);
		scrllEdit.getViewport().add(getLstEdit());

		btnOK = new JButton(getResourceString("ok"));
		btnOK.setFont(Constant.font);
		btnOK.setEnabled(true);
		// btnOK.setBounds(370, 248, 80, 30);
		btnOK.setMnemonic(KeyEvent.VK_ENTER);
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newComment = txtAreaComnt.getText();
				String oldComment = displayFileTemplate.ddoc.getpComment();
				displayFileTemplate.ddoc.setpComment(newComment);
				if (displayFileTemplate.ddoc.saveComment2File()) {
					JOptionPane.showMessageDialog(CommentsDialog.this,
							getResourceString("saveSuccess"));
					// 保存成功后 将编辑框更新
					txtAreaComnt.setText(newComment);
				} else {
					JOptionPane.showMessageDialog(CommentsDialog.this,
							getResourceString("saveFail"));
					// 还原评论
					displayFileTemplate.ddoc.setpComment(oldComment);
					txtAreaComnt.setText(oldComment);
				}
				setVisible(false);
			}
			// // Edit the desc of the pnt's records.
			// String descStr = txtAreaComnt.getText();
			// Movies mov = new Movies();
			// MoviesService movService = MoviesService.getInstance();
			// try {
			// mov = movService.selectByPrimaryKey(Integer.toString(movId));
			// } catch (SQLException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// mov.setDescription(descStr);
			// try {
			// movService.updateByPrimaryKey(mov);
			// // write to file.
			// JOptionPane.showMessageDialog(CommentsDialog.this, "保存成功。");
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// // movService.updateByPrimaryKeySelective(mov);
			// // MoviesExample movExam = new MoviesExample();
			// // MoviesExample.Criteria movCriteria = movExam.createCriteria();
			// // movCriteria.
			// // movService
			// }
		});

		btnCancel = new JButton(getResourceString("cancel"));
		btnCancel.setFont(Constant.font);
		btnCancel.setEnabled(true);
		// btnOK.setBounds(370, 248, 80, 30);
		btnCancel.setMnemonic(KeyEvent.VK_ENTER);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// dispose();
				// 将数据还原
				String oldComment = displayFileTemplate.ddoc.getpComment();
				txtAreaComnt.setText(oldComment);
				setVisible(false);
			}
		});

		btnClearAll = new JButton(getResourceString("clearAll"));
		btnClearAll.setFont(Constant.font);
		btnClearAll.setEnabled(true);
		// btnOK.setBounds(370, 248, 80, 30);
		btnClearAll.setMnemonic(KeyEvent.VK_ENTER);
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (JOptionPane.showConfirmDialog(new JFrame(),
						getResourceString("clearAllComment"),
						getResourceString("pleaseConfirm"),
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					txtAreaComnt.setText("");
					// pComment = "";
					// txtAreaComnt.setText(pComment);
					// String oldComment =
					// displayFileTemplate.ddoc.getpComment();
					// displayFileTemplate.ddoc.setpComment(pComment);
					// if(displayFileTemplate.ddoc.saveData2File())
					// JOptionPane.showMessageDialog(CommentsDialog.this,
					// "保存成功!");
					// else{
					// JOptionPane.showMessageDialog(CommentsDialog.this,
					// "保存失败，请检查后重试！");
					// //还原评论 两者需保持一致
					// displayFileTemplate.ddoc.setpComment(oldComment);
					// txtAreaComnt.setText(oldComment);
					// }
				}
			}
		});

		btnClearAll.setFont(Constant.font);
		btnClearAll.setEnabled(true);
		// btnOK.setBounds(370, 248, 80, 30);
		btnClearAll.setMnemonic(KeyEvent.VK_ENTER);
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		pnlBase.add(pnlLabel);
		pnlBase.add(pnlTxtComnts);
		pnlBase.add(pnlEdit);
		pnlBase.add(pnlBtn);

		pnlLabel.add(lblName);
		pnlLabel.add(txtName);
		pnlLabel.add(lblId);
		pnlLabel.add(txtId);
		pnlLabel.add(lblIdCard);
		pnlLabel.add(txtIdCard);
		pnlLabel.add(lblBirth);
		pnlLabel.add(txtBirth);
		pnlLabel.add(lblGender);
		pnlLabel.add(txtGender);
		pnlLabel.add(lblArch);
		pnlLabel.add(txtArch);

		pnlEdit.add(btnEdit);
		pnlEdit.add(scrllEdit);

		pnlBtn.add(btnOK);
		pnlBtn.add(btnCancel);
		pnlBtn.add(btnClearAll);
		// pnlBtn.add(btnChange);
		// pnlBtn.add();
		// pnlBtn.add(btnHelp);
		// scrllComments.add(txtAreaComnt);

		pnlTxtComnts.add(lblComments);
		// pnlTxtComnts.add(txtAreaComnt);
		pnlTxtComnts.add(scrllComments);

		this.getContentPane().add(pnlBase);

		patient = displayFileTemplate.ddoc.getPatient();
		if (patient != null)
			initTxtField(patient);

	}

	/**
	 * 将患者的信息显示在dialog上 该部分是患者的普通信息 不包括注释部分
	 * 
	 * @param pnt
	 */
	private void initTxtField(Patients pnt) {
		txtName.setText(pnt.getName());
		txtId.setText(pnt.getId());
		txtIdCard.setText(pnt.getIdcard());
		txtBirth.setText(pnt.getBirth());
		if (pnt.getGender() == 0) {
			txtGender.setText(getResourceString("female"));
		} else {
			txtGender.setText(getResourceString("male"));
		}
		txtArch.setText(Float.toString(pnt.getArch()));
	}

	public JList getLstEdit() {
		return lstEdit;
	}

	public void setLstEdit(JList lstEdit) {
		this.lstEdit = lstEdit;
	}

	public String getTxtName() {
		return txtName.getText();
	}

	public String getTxtId() {
		return txtId.getText();
	}

	public String getTxtIdCard() {
		return txtIdCard.getText();
	}

	public String getTxtBirth() {
		return txtBirth.getText();
	}

	public String getTxtGender() {
		return txtGender.getText();
	}

	public String getTxtArch() {
		return txtArch.getText();
	}

	public String getTxtAreaComnt() {
		return txtAreaComnt.getText();
	}
}
