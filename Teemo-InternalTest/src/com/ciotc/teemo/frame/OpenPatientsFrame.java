package com.ciotc.teemo.frame;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.ciotc.teemo.domain.Movies;
import com.ciotc.teemo.domain.MoviesExample;
import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.service.MoviesService;
import com.ciotc.teemo.service.PatientsService;
import com.ciotc.teemo.usbdll.USBDLL;
import com.ciotc.teemo.util.CheckInfo;
import com.ciotc.teemo.util.Constant;
import com.ciotc.teemo.util.FileOperation;
import com.ciotc.teemo.util.IdCard;
import com.ciotc.teemo.util.textfield.TeemoJTextField;

/**
 * @author Will
 * 
 */
public class OpenPatientsFrame extends JFrame implements PropertyChangeListener {
// private String pntID, pntName;
	private String pntID, pntName, pntAge, pntGender, pntBirth, pntArch, pntCreatTime;
	private List<Movies> lstMovTable = null;
	private String path = null;
// private MyJTableModel tblModel;
	private JPanel pnlBase, pnlPntInfo, pnlMovBase, pnlMovInfo;
	private JButton btnOK, btnNewMov, btnOpenMov, btnDelMov, btnDel, btnChange/*, btnHelp*/;
// btnImptMov,, btnHelp, btnChange, btnChngDetails, btnDspAB,
// btnDsp, btnChgName;
	private JLabel lblPntName, lblPntID, lblPntBirth, lblPntGender, lblPntAge, lblPntTeeth, lblUnit, lblMovInfo, lblMovChk, lblRegByChk;
	public TeemoJTextField txtPntName, txtPntBirthY, txtPntBirthM, txtPntBirthD, txtPntAge, txtPntID, txtPntTeeth, txtMovDesc, txtMovChk, txtRegCheck;
	private JTextArea txtMovInfo;
	private JScrollPane scrlTblMovDesc, scrlTxtMovInfo;
	private JComboBox cmbBoxRegByCheck;
// private JList lstDetails;
	private JRadioButton rbtnMale, rbtnFemale;
	private ButtonGroup btngGender;
	private JTable tblMovDesc;

	Font btnOKFont = new Font("微软雅黑", 0, 14);
	MainFrame mainFrame = null;
	private String pntIdCard;

	
	@SuppressWarnings("serial")
	public OpenPatientsFrame(final String pntNameArgs, final String keyId, final MainFrame mainFrame) {
		// TODO Auto-generated constructor stub
		super(" - - " + pntNameArgs + " - - " + getResourceString("OpenPatientsFrame.title"));
		this.mainFrame = mainFrame;
		this.pntID = keyId;
		this.pntName = pntNameArgs;
		this.setSize(800, 420);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		
		pnlBase = new JPanel();
		pnlBase.setLayout(null);
		pnlBase.setBounds(0, 0, 796, 440);
		pnlBase.setVisible(true);
		pnlBase.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		
		pnlPntInfo = new JPanel();
		pnlPntInfo.setLayout(null);
		pnlPntInfo.setVisible(true);
		pnlPntInfo.setBounds(0, 0, 794, 100);
		pnlPntInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(getResourceString("OpenPatientsFrame.pnlPntInfo")),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		
		pnlMovBase = new JPanel();
		pnlMovBase.setLayout(null);
		pnlMovBase.setVisible(true);
		pnlMovBase.setBounds(0, 98, 794, 284);
		pnlMovBase.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(getResourceString("movie")),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		
		pnlMovInfo = new JPanel();
		pnlMovInfo.setLayout(null);
		pnlMovInfo.setVisible(true);
		pnlMovInfo.setBounds(5, 16, 784, 230);
		pnlMovInfo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		
		// Components on the pnlPntInfo Panel.
		lblPntName = new JLabel(getResourceString("nameInformation"));
		lblPntName.setFont(Constant.font);
		lblPntName.setBounds(10, 20, 70, 30);
		lblPntName.setVisible(true);
		
		txtPntName = new TeemoJTextField();
		txtPntName.setFont(Constant.font);
		txtPntName.setBounds(10, 50, 80, 30);
		// txtPntName.setEditable(false);
		txtPntName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		
		lblPntBirth = new JLabel(getResourceString("BirthInformation"));
		lblPntBirth.setFont(Constant.font);
		lblPntBirth.setBounds(110, 20, 150, 30);
		lblPntBirth.setVisible(true);
		
		txtPntBirthY = new TeemoJTextField();
		txtPntBirthY.setFont(Constant.font);
		txtPntBirthY.setBounds(110, 50, 40, 30);
		// txtPntName.setEditable(false);
		txtPntBirthY.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		txtPntBirthY.setDocument((new NewPatientsDialog()).new JTextFieldLimit(4));
		// txtPntBirthY.getDocument().addDocumentListener(new OnValueChanged());
//		txtPntBirthY.getDocument().addDocumentListener(new FocusChange(this) );
		
		txtPntBirthM = new TeemoJTextField();
		txtPntBirthM.setFont(Constant.font);
		txtPntBirthM.setBounds(155, 50, 40, 30);
		// txtPntName.setEditable(false);
		txtPntBirthM.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		txtPntBirthM.setDocument((new NewPatientsDialog()).new JTextFieldLimit(2));
		
		txtPntBirthD = new TeemoJTextField();
		txtPntBirthD.setFont(Constant.font);
		txtPntBirthD.setBounds(200, 50, 40, 30);
		// txtPntName.setEditable(false);
		txtPntBirthD.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		txtPntBirthD.setDocument((new NewPatientsDialog()).new JTextFieldLimit(2));
		
		lblPntAge = new JLabel(getResourceString("ageInformation"));
		lblPntAge.setFont(Constant.font);
		lblPntAge.setBounds(270, 20, 60, 30);
		lblPntAge.setVisible(true);
		
		txtPntAge = new TeemoJTextField();
		txtPntAge.setFont(Constant.font);
		txtPntAge.setBounds(270, 50, 40, 30);
		
		lblPntGender = new JLabel(getResourceString("sexInformation"));
		lblPntGender.setFont(Constant.font);
		lblPntGender.setBounds(340, 20, 80, 30);
		lblPntGender.setVisible(true);
		
		rbtnMale = new JRadioButton(getResourceString("male"), true);
		rbtnMale.setBounds(330, 50, 55, 30);
		rbtnMale.setFont(Constant.font);
		rbtnFemale = new JRadioButton(getResourceString("female"));
		rbtnFemale.setBounds(380, 50, 70, 30);
		rbtnFemale.setFont(Constant.font);
		
		btngGender = new ButtonGroup();
		rbtnMale.setOpaque(false);
		rbtnFemale.setOpaque(false);
		btngGender.add(rbtnMale);
		btngGender.add(rbtnFemale);
		
		lblPntID = new JLabel(getResourceString("IDCardInformation"));
		lblPntID.setFont(Constant.font);
		lblPntID.setBounds(450, 20, 100, 30);
		lblPntID.setVisible(true);
		
		txtPntID = new TeemoJTextField();
		txtPntID.setFont(Constant.font);
		txtPntID.setBounds(450, 50, 135, 30);
		// txtPntName.setEditable(false);
		txtPntID.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		txtPntID.setDocument(new NewPatientsDialog().new JTextFieldLimit(18));
		txtPntID.setNumberOnly(true);
		
		lblPntTeeth = new JLabel(getResourceString("archInformation"));
		lblPntTeeth.setFont(Constant.font);
		lblPntTeeth.setBounds(620, 20, 120, 30);
		lblPntTeeth.setVisible(true);
		
		txtPntBirthY.setNumberOnly(true);
		txtPntBirthM.setNumberOnly(true);
		txtPntBirthD.setNumberOnly(true);
		txtPntAge.setNumberOnly(true);
		txtPntID.setNumberOnly(true);
		
		txtPntTeeth = new TeemoJTextField();
		txtPntTeeth.setFont(Constant.font);
		txtPntTeeth.setBounds(620, 50, 40, 30);
		// txtPntName.setEditable(false);
		txtPntTeeth.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		
		lblUnit = new JLabel(getResourceString("mm"));
		lblUnit.setFont(Constant.font);
		lblUnit.setBounds(660, 50, 30, 30);
		lblUnit.setVisible(true);
		
		btnOK = new JButton(getResourceString("change"));
		// MessageFormat.format("<html>{0}<br>{1}</html>" ,
		// getResourceString("ok"),getResourceString("alter")));//"<html>确   定<br>更   改</html>");
		btnOK.setFont(btnOKFont);
		btnOK.setEnabled(true);
		btnOK.setMargin(new Insets(1, 1, 1, 1));
		btnOK.setBounds(720, 20, 60, 60);
		// btnOK.setMnemonic(KeyEvent.VK_ENTER);
		
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Add methods to deal with the action.
				Patients pnt = new Patients();
				PatientsService pntService = PatientsService.getInstance();
				try {
					pnt = pntService.selectByPrimaryKey(keyId);
					
//					除了身份证需要校验，其他都不校验。以用户输入为主。
				if (txtPntName.getText().length() > 0) {
						if (txtPntBirthY.getText().length() > 0 && txtPntBirthM.getText().length() > 0
								&& txtPntBirthD.getText().length() > 0) {
							if (txtPntTeeth.getText().length() > 0) {
								if (txtPntAge.getText().length() > 0) {
									if (Integer.parseInt(txtPntAge.getText()) > 1
											&& Integer.parseInt(txtPntAge.getText()) < 100) {
										pnt.setAge(Integer.parseInt(txtPntAge.getText()));
									} else {
										JOptionPane.showMessageDialog(null, "年龄范围1——110.");
										txtPntAge.setText("");
										return;
									}
									pnt.setArch(Float.parseFloat(txtPntTeeth.getText()));

									
									String birthStr = txtPntBirthY.getText() + "-"
											+ txtPntBirthM.getText() + "-" + txtPntBirthD.getText();
									
									if (CheckInfo.compareDateToNow(birthStr)) {
										if (CheckInfo.isBirthday(birthStr)) {
											pnt.setBirth(birthStr);
										} else {
											JOptionPane.showMessageDialog(null, "请输入正确的格式。如：2010-01-01");
											txtPntBirthY.requestFocus();
											return;
										}
									} else {
										JOptionPane.showMessageDialog(null, "请输入合法日期。");
										return;
									}
									
									if (rbtnMale.isSelected()) {
										pnt.setGender(1);
									} else {
										pnt.setGender(0);
									}
									pnt.setName(txtPntName.getText());
									pnt.setIdcard(txtPntID.getText());
									
									
									if(txtPntID.getText().length() == 0){
//										身份证字段为空 。不判断。直接插入.
//										插入过程校验年龄和出生年月。
										pntService.updateByPrimaryKeySelective(pnt);
										JOptionPane.showMessageDialog(null, "修改成功。");
									}else{
//										身份证不为空时进行校验。
										if (new IdCard().IDCardValidate(txtPntID.getText())) {
											pnt.setAge(Integer.parseInt(txtPntAge.getText()));
											pnt.setArch(Float.parseFloat(txtPntTeeth.getText()));
											pnt.setBirth(txtPntBirthY.getText() + "-"
													+ txtPntBirthM.getText() + "-" + txtPntBirthD.getText());
											if (rbtnMale.isSelected()) {
												pnt.setGender(1);
											} else {
												pnt.setGender(0);
											}
											pnt.setName(txtPntName.getText());
											pnt.setIdcard(txtPntID.getText());
											pntService.updateByPrimaryKeySelective(pnt);
											JOptionPane.showMessageDialog(null, "修改成功。");
										} else {
											JOptionPane.showMessageDialog(null, "身份证不合法，请重新输入。");
											txtPntID.requestFocus();
										}
									}
									
								} else {
									JOptionPane.showMessageDialog(null, "年龄字段不能为空。");
									txtPntAge.requestFocus();
								}
							} else {
								JOptionPane.showMessageDialog(null, "门牙宽度不能为空。");
								txtPntTeeth.requestFocus();
							}
						} else {
							JOptionPane.showMessageDialog(null, "出生年月不能为空。");
							txtPntBirthY.requestFocus();
						}
					} else {
						JOptionPane.showMessageDialog(null, "姓名字段不能为空。");
						txtPntName.requestFocus();
					}
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(OpenPatientsFrame.this,
							getResourceString("OpenPatientsFrame.SQLException1"),
							getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// Can not edit the rows and column. But can not add double-click to it.
		final String[] COLUMN_NAMES = new String[] { getResourceString("OpenPatientsFrame.ordinal"),
				getResourceString("OpenPatientsFrame.patientsID"),
				getResourceString("OpenPatientsFrame.createDate"),
				getResourceString("OpenPatientsFrame.path"),
				getResourceString("OpenPatientsFrame.description") };
		
		tblMovDesc = new JTable(setJTableModel(getJTableData(keyId), COLUMN_NAMES)) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		// Let the JTable select the first row as default.
		listSelectionModel.setSelectionInterval(0, 0);
		tblMovDesc.setSelectionModel(listSelectionModel);
		setColumnWidth();
		
		tblMovDesc.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int selectedRow = tblMovDesc.rowAtPoint(e.getPoint());
				if (tblMovDesc.getRowSelectionAllowed()) {
					if (e.getClickCount() == 1) {
						movInfoFresh();
					}
					if (e.getClickCount() == 2) {
						
						path = (String) (tblMovDesc.getValueAt(selectedRow, 3));
						
						if (!mainFrame.tabs.openDisplayFileTemplate(path)) {
							JOptionPane.showMessageDialog(mainFrame.tabs, getResourceString("OpenPatientsFrame.openFileException"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
						}
						
						// System.out.println(path);
						// edit by LIN
//						if (!OpenPatientsFrame.this.mainFrame.desktopPane.containsKey(path)) {
//							DisplayFileTemplate dfTemplate = new DisplayFileTemplate(
//									OpenPatientsFrame.this.mainFrame.desktopPane, path);
//							if (dfTemplate.isCreateSuccess) {
//								OpenPatientsFrame.this.mainFrame.desktopPane
//										.addFileTemplate(dfTemplate);
//							} else {
//								JOptionPane.showMessageDialog(OpenPatientsFrame.this,
//										getResourceString("OpenPatientsFrame.openFileException"),
//										getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//							}
//							
//							// dfTemplateHashMap.put(filePath, dfTemplate);
//						}// else dfTemplateHashMap.get(filePath); 最好能使该窗口激活
//							// OpenPatientsFrame.this.dispose();
					}
				}
			}
		});
		
		tblMovDesc.getTableHeader().setReorderingAllowed(false);
		scrlTblMovDesc = new JScrollPane(tblMovDesc);
		scrlTblMovDesc.setViewportView(tblMovDesc);
		scrlTblMovDesc.setFont(Constant.font);
		scrlTblMovDesc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrlTblMovDesc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrlTblMovDesc.setBounds(2, 5, 500, 220);
		scrlTblMovDesc.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		
		lblMovInfo = new JLabel(getResourceString("movieInformation"));
		lblMovInfo.setFont(Constant.font);
		lblMovInfo.setBounds(508, 2, 120, 30);
		lblMovInfo.setVisible(true);
		
		txtMovInfo = new JTextArea(10, 50);
		txtMovInfo.setFont(Constant.font);
		txtMovInfo.setEditable(false);
		txtMovInfo.setVisible(true);
		txtMovInfo.setLineWrap(true);
		
		scrlTxtMovInfo = new JScrollPane();
		scrlTxtMovInfo.setFont(Constant.font);
		scrlTxtMovInfo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scrlTxtMovInfo.setVerticalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrlTxtMovInfo.setBounds(504, 30, 274, 160);
		scrlTxtMovInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		scrlTxtMovInfo.setViewportView(txtMovInfo);

		btnNewMov = new JButton(getResourceString("newMovie"));
		btnNewMov.setFont(Constant.font);
		btnNewMov.setEnabled(true);
		btnNewMov.setMargin(new Insets(1, 1, 1, 1));
		btnNewMov.setBounds(510, 195, 80, 30);
		btnNewMov.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Add methods to deal with the action.
//			// edit by LIN
//			if (!MainFrame.isOpenHandle) {
//				JOptionPane.showMessageDialog(OpenPatientsFrame.this,
//						getResourceString("noHandleTips"), getResourceString("Teemo"),
//						JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//			
//			if (OpenPatientsFrame.this.mainFrame.desktopPane.isExistRealtimeFileTemplate()) {
//				JOptionPane.showMessageDialog(OpenPatientsFrame.this,
//						getResourceString("OpenPatientsFrame.alreadyExitMovie"),
//						getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//			
				OpenPatientsFrame.this.mainFrame.patientID = Integer.parseInt(pntID.trim());
				OpenPatientsFrame.this.mainFrame.patientName = pntName;
				synchronized (MainFrame.lock) {
					USBDLL.setButton1On();
				}
			}
		});

		btnOpenMov = new JButton(getResourceString("openMovie"));
		btnOpenMov.setFont(Constant.font);
		btnOpenMov.setEnabled(true);
		btnOpenMov.setMargin(new Insets(1, 1, 1, 1));
		btnOpenMov.setBounds(605, 195, 80, 30);
		// btnOK.setMnemonic(KeyEvent.VK_ENTER);
		btnOpenMov.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open the first movie info if there are more than one movie.

				// int rowCnt = tblMovDesc.getRowCount();
				// If there is nothing in the JTable, getRowCount() will return
				// 0.
				// System.out.println(rowCnt);
				// if (rowCnt > 0) {
				// Open all the movies the patients have.
				// for (int i = 0; i <= rowCnt; i++) {
				int i = tblMovDesc.getSelectedRow();
				if (i >= 0) {
					String path = (String) (tblMovDesc.getValueAt(i, 3));
					// System.out.println(path);
					// Add method to open the specific movie.
					if (path == null)
						return;
					if (!mainFrame.tabs.openDisplayFileTemplate(path)) {
						JOptionPane.showMessageDialog(mainFrame.tabs, getResourceString("OpenPatientsFrame.openFileException"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
					}

					// if(!dfTemplateHashMap.containsKey(filePath)){
//				if (!OpenPatientsFrame.this.mainFrame.desktopPane.containsKey(path)) {
//					DisplayFileTemplate dfTemplate = new DisplayFileTemplate(
//							OpenPatientsFrame.this.mainFrame.desktopPane, path);
//					if (dfTemplate.isCreateSuccess)
//						OpenPatientsFrame.this.mainFrame.desktopPane.addFileTemplate(dfTemplate);
//					else
//						JOptionPane.showMessageDialog(OpenPatientsFrame.this,
//								getResourceString("OpenPatientsFrame.openFileException"),
//								getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//					// dfTemplateHashMap.put(filePath, dfTemplate);
//				}// else dfTemplateHashMap.get(filePath); 最好能使该窗口激活
//					// }
				} else {
					JOptionPane.showMessageDialog(null, getResourceString("OpenPatientsFrame.noMovies"));
				}
			}
		});

		btnDelMov = new JButton(getResourceString("deleteMovie"));
		btnDelMov.setFont(Constant.font);
		btnDelMov.setEnabled(true);
		btnDelMov.setMargin(new Insets(1, 1, 1, 1));
		btnDelMov.setBounds(700, 195, 80, 30);
		// btnOK.setMnemonic(KeyEvent.VK_ENTER);
		btnDelMov.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Add methods to deal with the action.
				int selectedRow = tblMovDesc.getSelectedRow();
				String movID = null;
				if (selectedRow != -1) {
					movID = (String) (tblMovDesc.getValueAt(selectedRow, 0));
					if (JOptionPane.showConfirmDialog(new JFrame(), getResourceString("OpenPatientsFrame.confrimTip"), getResourceString("OpenPatientsFrame.confrim"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						delMov(movID, selectedRow);
						movInfoFresh();
					}
				} else if (tblMovDesc.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null, getResourceString("OpenPatientsFrame.noMovies"));
				} else {
					JOptionPane.showMessageDialog(null, getResourceString("OpenPatientsFrame.slectMovie"));
				}
			}
		});

		lblMovChk = new JLabel(getResourceString("OpenPatientsFrame.diagnose"));
		lblMovChk.setFont(Constant.font);
		lblMovChk.setBounds(10, 248, 130, 30);
		lblMovChk.setVisible(true);

		txtMovChk = new TeemoJTextField();
		txtMovChk.setFont(Constant.font);
		txtMovChk.setBounds(135, 250, 125, 27); //
		txtMovChk.setEditable(false);
		txtMovChk.setVisible(true);
		txtMovChk.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""), BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		lblRegByChk = new JLabel(getResourceString("OpenPatientsFrame.diagnoseIdentiy")); //
		// lblServerName.setForeground(Color.YELLOW);
		lblRegByChk.setFont(Constant.font);
		lblRegByChk.setBounds(460, 250, 140, 30);
		lblRegByChk.setVisible(true);

		cmbBoxRegByCheck = new JComboBox();
		cmbBoxRegByCheck.setModel(new DefaultComboBoxModel(new String[] { getResourceString("OpenPatientsFrame.no"), "centric relation —下中颌关系"
		// , "habituel force","left lateral", "ip - co", "mutil - bite",
		// "protrusion", "right lateral"
				}));
		cmbBoxRegByCheck.setBounds(600, 248, 180, 27);
		cmbBoxRegByCheck.setVisible(true);
		cmbBoxRegByCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// txtMovChk.setText(cmbBoxRegByCheck.getSelectedItem().toString());

				String strToSrch = cmbBoxRegByCheck.getSelectedItem().toString();
				int row = tblMovDesc.getRowCount();
				for (int i = 0; i < row; i++) {
					// *
					String tmpId = tblMovDesc.getValueAt(i, 0).toString().trim();
					int srcLength = tmpId.length();
					if (tmpId.startsWith("**")) {
						tmpId = tmpId.substring(2, srcLength);
						tblMovDesc.setValueAt(tmpId, i, 0);
					}
					if (tblMovDesc.getValueAt(i, 4).toString().equals(strToSrch)) {
						String srcStr = tblMovDesc.getValueAt(i, 0).toString().trim();
						String newStr = "*" + srcStr;
						tblMovDesc.setValueAt(newStr, i, 0);
					} else {
						// No records match.
					}
				}
			}
		});

		btnChange = new JButton(getResourceString("change"));
		btnChange.setFont(Constant.font);
		btnChange.setEnabled(true);
		btnChange.setMargin(new Insets(1, 1, 1, 1));
		btnChange.setBounds(270, 248, 80, 30); //
		// btnOK.setMnemonic(KeyEvent.VK_ENTER);
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				// Add methods to deal with the action.
				int selected = tblMovDesc.getSelectedRow();
				String oldDesc = tblMovDesc.getValueAt(selected, 4).toString();
				String movId = tblMovDesc.getValueAt(selected, 0).toString();
				if (movId.startsWith("**")) {
					movId = movId.substring(2, movId.length());
				}
				Component source = (Component) actionEvent.getSource();
				Object response = JOptionPane.showInputDialog(source, null, getResourceString("OpenPatientsFrame.changeDiagnose"), JOptionPane.QUESTION_MESSAGE, null, new String[] { getResourceString("OpenPatientsFrame.no"), "centric relation—下中颌关系",
				// "habituel force", "left lateral", "ip - co",
				// "mutil - bite",
				// "protrusion", "right lateral"
						}, "centric relation——正中关系");
				if (response == null) {
				} else {
					String newDesc = response.toString();
					if (oldDesc.equals(newDesc)) {
						// Do nothing.
					} else {
						txtMovChk.setText(newDesc);
						// Update the DB.
						Movies mov = new Movies();
						MoviesService movService = MoviesService.getInstance();
						try {
							mov = movService.selectByPrimaryKey(movId);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						mov.setDescription(newDesc);
						try {
							// Update the DB.
							movService.updateByPrimaryKey(mov);
							tblMovDesc.setValueAt(response, selected, 4);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}
		});

//	btnHelp = new JButton(getResourceString("help"));
//	btnHelp.setFont(btnOKFont);
//	btnHelp.setEnabled(true);
//	btnHelp.setBounds(370, 248, 80, 30); //
//	btnHelp.setMnemonic(KeyEvent.VK_ENTER);
//	btnHelp.addActionListener(new ActionListener() {
//		public void actionPerformed(ActionEvent arg0) {
//			try {
//				Runtime.getRuntime().exec("cmd.exe /c start help.chm");
//			} catch (IOException ex) {
//				JOptionPane.showMessageDialog(null, getResourceString("cannotOpenDoc"),
//						getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//			}
//			
//		}
//	});

		pnlPntInfo.add(lblPntName);
		pnlPntInfo.add(lblPntID);
		pnlPntInfo.add(lblPntBirth);
		pnlPntInfo.add(lblPntGender);
		pnlPntInfo.add(lblPntTeeth);
		pnlPntInfo.add(lblUnit);
		pnlPntInfo.add(lblPntAge);

		pnlPntInfo.add(txtPntName);
		pnlPntInfo.add(txtPntBirthY);
		pnlPntInfo.add(txtPntBirthM);
		pnlPntInfo.add(txtPntBirthD);

		pnlPntInfo.add(txtPntID);
		pnlPntInfo.add(txtPntTeeth);
		pnlPntInfo.add(txtPntAge);
		pnlPntInfo.add(rbtnMale);
		pnlPntInfo.add(rbtnFemale);
		pnlPntInfo.add(btnOK);

		// pnlMovInfo.add(txtMovDesc);
		pnlMovInfo.add(scrlTblMovDesc);
		pnlMovInfo.add(scrlTxtMovInfo);
		pnlMovInfo.add(lblMovInfo);
		pnlMovInfo.add(btnNewMov);
		pnlMovInfo.add(btnOpenMov);
		pnlMovInfo.add(btnDelMov);

		pnlMovBase.add(cmbBoxRegByCheck);
		pnlMovBase.add(btnChange);
		pnlMovBase.add(lblMovChk);
		pnlMovBase.add(lblRegByChk);
		pnlMovBase.add(txtMovChk);
		//pnlMovBase.add(btnHelp);

		pnlMovBase.add(pnlMovInfo);

		pnlBase.add(pnlMovBase);
		pnlBase.add(pnlPntInfo);
		this.getContentPane().add(pnlBase);

		/****add***/
		mainFrame.addPropertyChangeListener(this);
		btnNewMov.setEnabled(false);
		bool1 = mainFrame.isExistRealtimeFile;
		bool2 = mainFrame.isOpenHandle;
		if (!bool1 && bool2)
			btnNewMov.setEnabled(true);
		else
			btnNewMov.setEnabled(false);
		/****add***/

		txtInit(pntNameArgs, keyId);
		movInfoFresh();
		addWindowListener(new MyWindowAdapter());
	}

	private TableModel setJTableModel(List<Movies> list, String[] name) {
		// TODO Auto-generated method stub
		List<Movies> lst = list;
		int x = lst.size();
		Object[][] data = new Object[x][5];
		for (int i = 0; i < x; i++) {
			data[i][0] = lstMovTable.get(i).getId();
			data[i][1] = lstMovTable.get(i).getPatientid();
			data[i][2] = lstMovTable.get(i).getCreatetime();
			data[i][3] = lstMovTable.get(i).getPath();
			data[i][4] = lstMovTable.get(i).getDescription();
		}
		return new DefaultTableModel(data, name);
	}

	private void setColumnWidth() {
		TableColumn clmnMovId, clmnPntId, clmnCreateTime, clmnPath, clmnDesc;
		clmnMovId = tblMovDesc.getColumnModel().getColumn(0);
		clmnMovId.setPreferredWidth(60);
		clmnMovId.setMaxWidth(60);
		clmnPntId = tblMovDesc.getColumnModel().getColumn(1);
		clmnPntId.setPreferredWidth(90);
		clmnPntId.setMaxWidth(90);
		clmnCreateTime = tblMovDesc.getColumnModel().getColumn(2);
		clmnCreateTime.setPreferredWidth(140);
		// clmnCreateTime.setMaxWidth(200);
		clmnPath = tblMovDesc.getColumnModel().getColumn(3);
		clmnPath.setPreferredWidth(200);
		// clmnPath.setMaxWidth(200);
		clmnDesc = tblMovDesc.getColumnModel().getColumn(4);
		clmnDesc.setPreferredWidth(100);
		// clmnDesc.setMaxWidth(80);

	}

	private void movInfoFresh() {
		String movInfo = null;
		if (tblMovDesc.getRowCount() == 0) {
			movInfo = getResourceString("OpenPatientsFrame.record");
			txtMovInfo.setText(movInfo);
		} else if (tblMovDesc.getSelectedRow() != -1) {
			int selectedRow = tblMovDesc.getSelectedRow();
			String movieName = getResourceString("movie");
			movInfo = (movieName + getResourceString("OpenPatientsFrame.ordinal") + ":" + tblMovDesc.getValueAt(selectedRow, 0).toString() + "\n " + getResourceString("OpenPatientsFrame.patientsID") + ":" + tblMovDesc.getValueAt(selectedRow, 1).toString() + "\n" + movieName
					+ getResourceString("OpenPatientsFrame.createDate") + ":" + tblMovDesc.getValueAt(selectedRow, 2).toString() + "\n" + movieName + getResourceString("OpenPatientsFrame.path") + ":" + tblMovDesc.getValueAt(selectedRow, 3).toString());
			txtMovInfo.setText(movInfo);
		} else {
			// If no row is selected, let the text area display the info of the
			// first record.
			String movieName = getResourceString("movie");
			movInfo = (movieName + getResourceString("OpenPatientsFrame.ordinal") + ":" + tblMovDesc.getValueAt(0, 0).toString() + "\n " + getResourceString("OpenPatientsFrame.patientsID") + ":" + tblMovDesc.getValueAt(0, 1).toString() + "\n" + movieName
					+ getResourceString("OpenPatientsFrame.OpenPatientsFrame.createDate") + ":" + tblMovDesc.getValueAt(0, 2).toString() + "\n" + movieName + getResourceString("OpenPatientsFrame.path") + ":" + tblMovDesc.getValueAt(0, 3).toString());
			txtMovInfo.setText(movInfo);
		}
	}

	protected void checkChageInfo(String id, String name, String idCard, String age, String gender, String arch, String birth, String createTime) {
		String txtChgPntName = txtPntName.getText();
		String txtChgPntIdCard = txtPntID.getText();
		String txtChgPntArch = txtPntTeeth.getText();
		String txtChgPntAge = txtPntAge.getText();
		String txtChgPntGender = rbtnMale.isSelected() ? "男" : "女";
		String txtChgPntBirth = txtPntBirthY.getText() + "-" + txtPntBirthM.getText() + "-" + txtPntBirthD.getText();

		Patients pnt = new Patients();
		PatientsService pntService = PatientsService.getInstance();
		pnt.setId(id);

		IdCard idCardOption = new IdCard();

		if (name.equals(txtChgPntName) && idCard.equals(txtChgPntIdCard) && age.equals(txtChgPntAge) && gender.equals(txtChgPntGender) && birth.equals(txtChgPntBirth) && arch.equals(txtChgPntArch)) {
			JOptionPane.showMessageDialog(null, getResourceString("OpenPatientsFrame.noChange"), getResourceString("Teemo"), JOptionPane.WARNING_MESSAGE);
		} else {
			if (!name.equals(txtChgPntName)) {
				// pntName has changed.
				pnt.setName(txtChgPntName);
			}
			if (!arch.equals(txtChgPntArch)) {
				// txtPntArch has changed.
				pnt.setArch(Float.parseFloat(txtChgPntArch));
			}
			if (!age.equals(txtChgPntAge)) {
				// age has changed.
				pnt.setAge(Integer.parseInt(txtChgPntAge));
			}
			if (!birth.equals(txtChgPntBirth)) {
				// birth has changed.
				pnt.setBirth(txtChgPntBirth);
			}
			if (!gender.equals(txtChgPntGender)) {
				// gender has changed.
				pnt.setGender(rbtnMale.isSelected() ? 1 : 0);
			}

			if (!idCard.equals(txtChgPntIdCard)) {
				// idCard has changed.
				if (idCardOption.IDCardValidate(txtChgPntIdCard)) {
					/*
					 * 以用户输入为主，身份证件校验通过即可。
					 * 
					 * // idCard is correct.
					// Reset the value of pntAge, pntBirth, pntGender.
					txtPntAge.setText(idCardOption.getAgeFrmIdCard(txtChgPntIdCard));
					//				txtPntBirth.setText(idCardOption.getBirthFrmIdCard(txtChgPntIdCard));
					if (idCardOption.getPntGender(txtChgPntIdCard) == 0) { //
						rbtnFemale.setEnabled(true);
						rbtnFemale.setSelected(true);
						pnt.setGender(0);
					} else { // rbtnMale.setEnabled(true);
						rbtnMale.setSelected(true);
						pnt.setGender(1);
					}
					pnt.setAge(Integer.parseInt((idCardOption.getAgeFrmIdCard(txtChgPntIdCard))));
					pnt.setIdcard(txtChgPntIdCard);
					//				idChg = true;
					*/} else {
					pnt.setGender(idCardOption.getPntGender(idCard));
					pnt.setAge(Integer.parseInt(age));
					pnt.setIdcard(idCard); //
//				JOptionPane.showMessageDialog(null, "身份证不合法。");
				}
			} else {
				pnt.setGender(idCardOption.getPntGender(idCard));
				pnt.setAge(Integer.parseInt(age));
				pnt.setIdcard(idCard);
			}

			pnt.setCreatetime(createTime);
			try {
				pntService.updateByPrimaryKeySelective(pnt);
				JOptionPane.showMessageDialog(null, "患者信息已更改。");
				freshJList(id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(OpenPatientsFrame.this, getResourceString("OpenPatientsFrame.changeError"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/*
	 * protected void setChangeInfoEnglish(String id, String name, String idCard,
	 * String age, String gender, String arch, String createTime, String birth) {
	 * int txtChgPntGender = 1; if(rbtnMale.isSelected() == true){ txtChgPntGender =
	 * 1; }else{ txtChgPntGender =0; } Patients pnt = new Patients();
	 * PatientsService pntService = PatientsService.getInstance(); pnt.setId(id);
	 * pnt.setAge(Integer.parseInt(txtPntAge.getText()));
	 * pnt.setArch(Float.parseFloat(txtPntTeeth.getText()));
	 * pnt.setBirth(txtPntBirth.getText()); pnt.setGender(txtChgPntGender);
	 * pnt.setCreatetime(createTime); try {
	 * pntService.updateByPrimaryKeySelective(pnt); //
	 * JOptionPane.showMessageDialog(null, "患者信息已更改。");
	 * JOptionPane.showMessageDialog(null, "Change successfully.");
	 * freshJListEnglish(id); } catch (SQLException e) { // TODO Auto-generated
	 * catch block e.printStackTrace();
	 * JOptionPane.showMessageDialog(OpenPatientsFrame.this,
	 * getResourceString("OpenPatientsFrame.changeError"),
	 * getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE); } }
	 */

	private void freshJList(String pntIdArgs4) {
		mainFrame.pntFrm.txtPntInfo.setText("");
		mainFrame.pntFrm.getTxtPntName().setText("");
		mainFrame.pntFrm.getLstObj().setListData(mainFrame.pntFrm.pntObjList());
		Patients pnt = new Patients();
		PatientsService pntService = PatientsService.getInstance();
		try {
			pnt = pntService.selectByPrimaryKey(pntIdArgs4);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IdCard getCard = new IdCard();
		pntID = pnt.getId();
		pntName = pnt.getName();
		pntIdCard = pnt.getIdcard();
		pntBirth = getCard.getBirthFrmIdCard(pntIdCard);
		pntAge = getCard.getAgeFrmIdCard(pntIdCard);
		pntGender = getCard.getPntGender(pntIdCard) == 0 ? getResourceString("female") : getResourceString("male");
		pntArch = String.valueOf(pnt.getArch());
		pntCreatTime = pnt.getCreatetime();

		mainFrame.pntFrm.getTxtPntName().setText(pntName);
		mainFrame.pntFrm.txtPntInfo.setText(getResourceString("IDInformation") + pntID + "      " + "\n" + getResourceString("nameInformation") + pntName + "\n" + getResourceString("IDCardInformation") + pntIdCard + "\n" + getResourceString("BirthInformation2") + pntBirth + "\n"
				+ getResourceString("ageInformation") + pntAge + "\n" + getResourceString("sexInformation") + pntGender + "\n" + getResourceString("archInformation") + pntArch + "\n" + getResourceString("createInformation") + pntCreatTime);
	}

	private void freshJListEnglish(String pntIdArgs4) {
		mainFrame.pntFrm.txtPntInfo.setText("");
		mainFrame.pntFrm.getTxtPntName().setText("");
		mainFrame.pntFrm.getLstObj().setListData(mainFrame.pntFrm.pntObjList());
		Patients pnt = new Patients();
		PatientsService pntService = PatientsService.getInstance();
		try {
			pnt = pntService.selectByPrimaryKey(pntIdArgs4);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IdCard getCard = new IdCard();
		pntID = pnt.getId();
		pntName = pnt.getName();
		pntIdCard = pnt.getIdcard();
		pntBirth = pnt.getBirth();
		pntAge = pnt.getBirth();
		pntGender = pnt.getGender() == 0 ? getResourceString("female") : getResourceString("male");
		pntArch = String.valueOf(pnt.getArch());
		pntCreatTime = pnt.getCreatetime();

		mainFrame.pntFrm.getTxtPntName().setText(pntName);
		mainFrame.pntFrm.txtPntInfo.setText(getResourceString("IDInformation") + pntID + "      " + "\n" + getResourceString("nameInformation") + pntName + "\n" + getResourceString("IDCardInformation") + pntIdCard + "\n" + getResourceString("BirthInformation2") + pntBirth + "\n"
				+ getResourceString("ageInformation") + pntAge + "\n" + getResourceString("sexInformation") + pntGender + "\n" + getResourceString("archInformation") + pntArch + "\n" + getResourceString("createInformation") + pntCreatTime);
	}

	protected void delMov(String movID, int selectRow) {
		MoviesService movService = MoviesService.getInstance();
		String path = (String) tblMovDesc.getValueAt(selectRow, 3);
		
		//mainFrame.desktopPane.closeDeletedFrame(path); // 关闭目前打开的窗口
		mainFrame.tabs.closeDisplayFileTemplate(path);
		
		FileOperation.delFile(path);
		try {
			movService.deleteByPrimaryKey(movID);
			// System.out.println(movService.selectByPrimaryKey(movID).getPath());
			// JOptionPane.showMessageDialog(null, "所选患者相关影片资料已删除。");
			DefaultTableModel dftTblMdl = (DefaultTableModel) (tblMovDesc.getModel());
			dftTblMdl.removeRow(selectRow);
			if (tblMovDesc.getRowCount() == 0) {
			} else {
				dftTblMdl.fireTableRowsDeleted(0, tblMovDesc.getRowCount() - 1);
			}
			tblMovDesc.revalidate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更新影片列表
	 * 
	 * @param id
	 * @author 林晓智
	 */
	public void validateList() {
		List<Movies> lst = getJTableData(pntID);
		int x = lst.size();
		Object[][] data = new Object[x][5];
		for (int i = 0; i < x; i++) {
			data[i][0] = lstMovTable.get(i).getId();
			data[i][1] = lstMovTable.get(i).getPatientid();
			data[i][2] = lstMovTable.get(i).getCreatetime();
			data[i][3] = lstMovTable.get(i).getPath();
			data[i][4] = lstMovTable.get(i).getDescription();
		}
		final String[] COLUMN_NAMES = new String[] { getResourceString("OpenPatientsFrame.ordinal"), getResourceString("OpenPatientsFrame.patientsID"), getResourceString("OpenPatientsFrame.createDate"), getResourceString("OpenPatientsFrame.path"),
				getResourceString("OpenPatientsFrame.description") };
		DefaultTableModel dftTblMdl = new DefaultTableModel(data, COLUMN_NAMES);
		tblMovDesc.setModel(dftTblMdl);
		tblMovDesc.validate();
		movInfoFresh();
	}

	private List<Movies> getJTableData(String pntIDArgs2) {
		MoviesService movService = MoviesService.getInstance();
		MoviesExample movExample = new MoviesExample();
		MoviesExample.Criteria movExamCriteria = movExample.createCriteria().andPatientidEqualTo(pntIDArgs2);
		try {
			lstMovTable = (List<Movies>) movService.selectByExample(movExample);
			// System.out.println(list.get(0).getCreatetime());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lstMovTable;
	}

	class MyJTableModel extends DefaultTableModel {
		public MyJTableModel() {
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	/*
	 * private void txtInit(String pntNameArgs, String pntIDArgs1) { String pntID1 =
	 * pntIDArgs1; String pntBirth, pntIdCard;
	 * 
	 * Patients pnt = new Patients(); PatientsService pntService =
	 * PatientsService.getInstance(); try { pnt =
	 * pntService.selectByPrimaryKey(pntID1); } catch (SQLException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } pntIdCard =
	 * pnt.getIdcard(); pntBirth = pnt.getBirth();
	 * 
	 * if (pntIdCard.length() == 15) { pntBirth = ("19" + pntIdCard.substring(6, 8)
	 * + "-" + pntIdCard.substring(8, 10) + "-" + pntIdCard .substring(10, 12)); }
	 * else { pntBirth = (pntIdCard.substring(6, 10) + "-" + pntIdCard.substring(10,
	 * 12) + "-" + pntIdCard .substring(12, 14)); } if (pnt.getGender() == 0) {
	 * rbtnFemale.setSelected(true); } else { rbtnMale.setSelected(true); }
	 * rbtnFemale.setEnabled(true); rbtnMale.setEnabled(true);
	 * txtPntName.setText(pnt.getName());
	 * txtPntAge.setText(String.valueOf(pnt.getAge())); txtPntAge.setEditable(true);
	 * txtPntTeeth.setText(String.valueOf(pnt.getArch()));
	 * txtPntBirth.setText(pntBirth); txtPntBirth.setEditable(true);
	 * txtPntID.setText(pntIdCard); }
	 */

	private void txtInit(String pntNameArgs, String pntIDArgs1) {
		String pntID1 = pntIDArgs1;
		String pntBirth, pntBirthY, pntBirthM, pntBirthD, pntIdCard;

		Patients pnt = new Patients();
		PatientsService pntService = PatientsService.getInstance();
		try {
			pnt = pntService.selectByPrimaryKey(pntID1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pntIdCard = pnt.getIdcard();
		pntBirth = pnt.getBirth();

		if (pnt.getGender() == 0) {
			rbtnFemale.setSelected(true);
		} else {
			rbtnMale.setSelected(true);
		}
		txtPntName.setText(pnt.getName());
		txtPntAge.setText(String.valueOf(pnt.getAge()));
		txtPntTeeth.setText(String.valueOf(pnt.getArch()));
		txtPntBirthY.setText(pntBirth.substring(0, 4));
//		txtPntBirthM.setText(pntBirth.substring(5, 7));
//		txtPntBirthD.setText(pntBirth.substring(8, 10));
		int index1 = pntBirth.indexOf("-");
		txtPntBirthM.setText(pntBirth.substring(index1+1,pntBirth.indexOf("-", index1+1)));
		txtPntBirthD.setText(pntBirth.substring(pntBirth.indexOf("-", index1+1)+1,pntBirth.length()));
		txtPntID.setText(pntIdCard);
	}

	public String getPntID() {
		return pntID;
	}

	class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			mainFrame.pntFrm.removeOpenFrm(OpenPatientsFrame.this);
			super.windowClosing(e);
		}

	}
	class FocusChange implements DocumentListener{
		public FocusChange(JTextField component){
//			component
		}
		@Override
		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}


	}
	private boolean bool1; //for isExistRealtimeFile
	private boolean bool2; //for isOpenHandle

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("openPatientFrame Info " + evt);
		if (evt.getPropertyName().equals(MainTabbedPane.IS_EXIST_REALTIME_FILE)) {
			bool1 = (Boolean) evt.getNewValue();
			if (!bool1 && bool2)
				btnNewMov.setEnabled(true);
			else
				btnNewMov.setEnabled(false);
		} else if (evt.getPropertyName().equals(MainFrame.IS_OPEN_HANDLE)) {
			bool2 = (Boolean) evt.getNewValue();
			if (!bool1 && bool2)
				btnNewMov.setEnabled(true);
			else
				btnNewMov.setEnabled(false);
		}
	}

}
