package com.ciotc.teemo.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.ciotc.teemo.domain.Movies;
import com.ciotc.teemo.domain.MoviesExample;
import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.domain.PatientsExample;
import com.ciotc.teemo.frame.MainFrame;
import com.ciotc.teemo.service.MoviesService;
import com.ciotc.teemo.service.PatientsService;
import com.ciotc.teemo.util.CheckLanguage;

import static com.ciotc.teemo.resource.MyResources.getResourceString;
/*
 * Description: PantientsFrame.
 * author@ pan
 * EditTime：2011.10.4
 * */

public class PatientsFrame extends JFrame {
	private JList lstPnt;
	private JLabel lblPntName, lblSrchPnt;
//	lblRegByCheck;
	private JTextField txtPntName;
	private JTextField txtSrchPnt;
	public JTextArea txtPntInfo;
//	private JComboBox cmbBoxRegByCheck;
	private JButton btnNewPnt, btnOpenPnt, btnDelPnt, btnExit, btnHelp,
			btnImptPnt;
	private JPanel pnlBase, pnlButton, pnlPntLst, pnlPntInfo;
	private JScrollPane scrlPnlPntInfo;
	OpenPatientsFrame openPntFrm;

	// Is the str set to be static properly?
	private static String defaultPntStr = null;

	Font btnFont = new Font("微软雅黑", 0, 12);
	MainFrame mainFrame = null;
	// private ArrayList<OpenPatientsFrame> openPntFrms = new
	// ArrayList<OpenPatientsFrame>();
	private HashMap<String, OpenPatientsFrame> hashPntFrms = new HashMap<String, OpenPatientsFrame>();

	public PatientsFrame(String frmName, MainFrame mainFrame) {
		// Properties of the PatientsFrame.
		super(frmName);
		this.mainFrame = mainFrame;
		this.setSize(500, 530);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);

		// Properties of the PntInfo Panel.
		pnlBase = new JPanel();
		pnlBase.setLayout(null);
		pnlBase.setVisible(true);
		// The following components are all on the pnlPntLst Panel.
		pnlPntLst = new JPanel();
		pnlPntLst.setLayout(null);
		pnlPntLst.setBounds(0, 0, 250, 410);
		pnlPntLst.setVisible(true);
		// Properties of the label.
		lblPntName = new JLabel(getResourceString("nameInformation").trim());
		lblPntName.setFont(btnFont);
		lblPntName.setBounds(10, 10, 135, 30);
		lblPntName.setVisible(true);

		setTxtPntName(new JTextField());
		getTxtPntName().setFont(btnFont);
		getTxtPntName().setBounds(3, 35, 243, 30);
		getTxtPntName().setVisible(true);

		getTxtPntName().setEditable(false);
		getTxtPntName().setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(""),
						BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		scrlPnlPntInfo = new JScrollPane();
		scrlPnlPntInfo.setFont(btnFont);
		scrlPnlPntInfo
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrlPnlPntInfo.setBounds(3, 70, 246, 340);

		// Properties of the JList.
		// JList obj is private.
		setLstObj(new JList());
		getLstObj().setFont(btnFont);
		getLstObj().setFixedCellWidth(180);
		getLstObj().setFixedCellHeight(21);
		// setListData display the obj in the JList, so toString() in class
		// Patients should be override to display the pnt name only.
		getLstObj().setListData(pntObjList());

		// Set the first obj in the list as selected.
		getLstObj().setSelectedIndex(0);
		
		
		getTxtPntName().setText(getLstObj().getSelectedValue().toString());
		
		
		
		// Listen MouseClick Event.
		getLstObj().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (getLstObj().getSelectedIndex() != -1) {
					if (e.getClickCount() == 1)
						// oneClick(getLstPnt().getSelectedValue());
						getTxtPntName().setText(
								getLstObj().getSelectedValue().toString());
					oneClick(((Patients) getLstObj().getSelectedValue())
							.getId());
					if (e.getClickCount() == 2)
						// doubleClick(getLstPnt().getSelectedValue());
						doubleClick(((Patients) (getLstObj().getSelectedValue()))
								.getId());
				}
			}
		});
		scrlPnlPntInfo.setViewportView(getLstObj());

		// The following components are all on the pnlPntInfo Panel.
		pnlPntInfo = new JPanel();
		pnlPntInfo.setLayout(null);
		pnlPntInfo.setBounds(250, 0, 250, 410);
		pnlPntInfo.setVisible(true);

		lblSrchPnt = new JLabel(getResourceString("patientsFrame.search"));
		lblSrchPnt.setFont(btnFont);
		lblSrchPnt.setBounds(10, 10, 135, 30);
		lblSrchPnt.setVisible(true);

		txtSrchPnt = new JTextField();
		txtSrchPnt.setFont(btnFont);
		txtSrchPnt.setBounds(0, 35, 241, 30);
		txtSrchPnt.setEditable(true);
		txtSrchPnt.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		// Need to be finished.
		txtSrchPnt.getDocument().addDocumentListener(new OnSrchValueChanged());

		txtPntInfo = new JTextArea();
		txtPntInfo.setFont(btnFont);
		txtPntInfo.setBounds(0, 70, 241, 340);
		txtPntInfo.setEditable(false);
		txtPntInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		// setPntInfo(getLstPnt().getSelectedValue());

		// Properties of the PnlBtn Panel.
		pnlButton = new JPanel();
		pnlButton.setLayout(null);
		// Set the border of the panel to be visible.
//		pnlButton.setBounds(4, 415, 488, 50);
		pnlButton.setBounds(4, 415, 488, 100);
		pnlButton.setVisible(true);
		// Properties of buttons.
		btnNewPnt = new JButton(getResourceString("newPatients"));
		btnNewPnt.setFont(btnFont);
		btnNewPnt.setEnabled(true);
		btnNewPnt.setBounds(10, 42, 85, 30);
		btnNewPnt.setMargin(new Insets(1, 1, 1, 1));
		btnNewPnt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// NewPatientsFrame.newPntFrm.setVisible(true);

				// PatientsFrame.this.mainFrame.newPntFrm = new
				// NewPatientsFrame("患者记录——新患者");
				// //run(PatientsFrame.this.mainFrame.newPntFrm);
				// PatientsFrame.this.mainFrame.newPntFrm.setVisible(true);
				// dispose();
				NewPatientsDialog newPntDlg = new NewPatientsDialog(
						PatientsFrame.this,getResourceString("NewPatientsDialog.title"));
				newPntDlg.setVisible(true);
			}
		});

		btnOpenPnt = new JButton(getResourceString("openPatients"));
		btnOpenPnt.setFont(btnFont);
		btnOpenPnt.setEnabled(true);
		btnOpenPnt.setBounds(110, 42, 85, 30);
		btnOpenPnt.setMargin(new Insets(1, 1, 1, 1));
		btnOpenPnt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the pntID from the str pntInfo.
				String pntInfo = txtPntInfo.getText();
				// System.out.println(pntInfo);
				/*
				 * The problem is that the patients are increasing. So the
				 * substring between 5th and 6th may not correct. Add 6 extra
				 * spaces before the character '\n'.
				 */
				String idKey = ((Patients)(getLstObj().getSelectedValue())).getId();
//				String pntName = ((Patients)(getLstObj().getSelectedValue())).getName();
				newOpenFrm(idKey);
			}
		});

		btnDelPnt = new JButton(getResourceString("patientsFrame.deletePatients"));
		btnDelPnt.setFont(btnFont);
		btnDelPnt.setEnabled(true);
		btnDelPnt.setBounds(210, 42, 85, 30);
		btnDelPnt.setMargin(new Insets(1, 1, 1, 1));

		// Let the user confirm the del option.
		btnDelPnt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(new JFrame(),
						getResourceString("patientsFrame.confirmDeletePatients"),getResourceString("pleaseConfirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					delPnt();
					// Refresh the JList after the del option.
					getLstObj().setListData(pntObjList());
					txtPntName.setText("");
					txtPntInfo.setText("");
				}
			}
		});

		btnExit = new JButton(getResourceString("exit"));
		btnExit.setFont(btnFont);
		btnExit.setEnabled(true);
		btnExit.setBounds(390, 42, 85, 30);
		btnExit.setMargin(new Insets(1, 1, 1, 1));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				// dispose();
				// System.exit(0);
			}
		});


		// Add all the components to the pnlButton Panel.
		pnlButton.add(btnNewPnt);
		pnlButton.add(btnOpenPnt);
		pnlButton.add(btnDelPnt);
		pnlButton.add(btnExit);
		// Add all the components to the pnlPntLst Panel.
		pnlPntLst.add(lblPntName);
		pnlPntLst.add(scrlPnlPntInfo);
		pnlPntLst.add(lblPntName);
		pnlPntLst.add(getTxtPntName());
		// Add all the components to the pnlPntInfo Panel.
		pnlPntInfo.add(lblSrchPnt);
		pnlPntInfo.add(txtSrchPnt);
		pnlPntInfo.add(txtPntInfo);

		// Add three panels to the pnlBase Panel.
		pnlBase.add(pnlButton);
		pnlBase.add(pnlPntLst);
		pnlBase.add(pnlPntInfo);
		// Add pnlBase panel to the frame.
		this.getContentPane().add(pnlBase);
		// setVisible(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	private void delPnt() {
		Patients pnt = new Patients();
		PatientsService pntService = PatientsService.getInstance();
//		String id = txtPntInfo.getText().substring(5, 7).trim();
		String id = ((Patients) getLstObj().getSelectedValue()).getId();
		
		try {
			pntService.deleteByPrimaryKey(id);
			if (hashPntFrms.containsKey(id)) {
				OpenPatientsFrame frm = hashPntFrms.get(id);
				frm.dispose();
			}
			JOptionPane.showMessageDialog(null, getResourceString("patientsFrame.alreadyDelete"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, getResourceString("deleteFail"));
		}
	}

	// initJList() get all the patients' obj from the DB under the search
	// condition.
	// Convert list to Object [] array in order to call setListData(Object
	// []args ).
	Object[] pntObjList() {
		List<Patients> list = null;
		PatientsService pntService = PatientsService.getInstance();
		PatientsExample pntExample = new PatientsExample();
		PatientsExample.Criteria pntCriteria = pntExample.createCriteria();
		// Get all the pnts obj.
		pntCriteria.andIdIsNotNull();
		try {
			list = (List<Patients>) pntService.selectByExample(pntExample);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.toArray();
	}

	// If you select the pnt in the JList, the pnt will show in the txtArea
	// synchronously.
	private void oneClick(String pntId) {
			setPntInfoNoCheck(pntId);
		}

	private void setPntInfoNoCheck(String pntId) {
		// TODO Auto-generated method stub

		List<Patients> lstPnt = getPntList(pntId);
		String pntID = pntId;
		String pntName = lstPnt.get(0).getName();
		String pntIdCard = lstPnt.get(0).getIdcard();
		String pntBirth = lstPnt.get(0).getBirth();
		String pntGender = lstPnt.get(0).getGender() == 0 ? getResourceString("female") : getResourceString("male");
		int pntAge = lstPnt.get(0).getAge();
		float pntTeeth = lstPnt.get(0).getArch();
		String pntCreat = lstPnt.get(0).getCreatetime();
		String movInfo = movInfo(pntId);

		// edit by LIN
		mainFrame.patientID = Integer.parseInt(pntID.trim());
		mainFrame.patientName = pntName;
		/*
		 * It is a str that displayed in the txtPntInfo area. Add 6 spaces right
		 * after the pntID to prevent the pnt number increase. When get the
		 * pntID from the txt area, you must watch out the addtional spaces.
		 * trim() is needed to get the right pntID.
		 */
		txtPntInfo.setText(getResourceString("IDInformation") + pntID + "      " + "\n"+getResourceString("nameInformation") + pntName
				+ "\n"+getResourceString("IDCardInformation") + pntIdCard + "\n"+getResourceString("BirthInformation2") 
				+ pntBirth + "\n"+getResourceString("ageInformation")+ pntAge + "\n"+getResourceString("sexInformation") + pntGender 
				+ "\n"+getResourceString("patientsFrame.arch") + pntTeeth+ "\n"+getResourceString("createInformation")
				+ pntCreat+"\n\n"+getResourceString("movieInformation")+"\n"+movInfo);
//		System.out.println(movInfo);
	}

	// Get the specific pnt list(in case there are persons of the same name).
	@SuppressWarnings("unchecked")
	private List getPntList(String pntId) {
		// String pntName = selectedValue.toString();
		List<Patients> list = null;
		Patients pntDB = new Patients();
		PatientsService pntService = PatientsService.getInstance();
		PatientsExample pntExample = new PatientsExample();
		PatientsExample.Criteria pntExamCriteria = pntExample.createCriteria()
				.andIdEqualTo(pntId);
		// .andNameEqualTo(pntName);
		try {
			list = (List<Patients>) pntService.selectByExample(pntExample);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// For debug.
		/*
		 * for(int i=0;i<list.size();i++){
		 * System.out.println(list.get(i).getId()
		 * +list.get(i).getName()+list.get(i).getIdcard()); }
		 */
		return list;
	}
	
	private List getMovInfo(String pntId){
		List<Movies> movLst = null;
		Movies mov = new Movies();
		MoviesService movService = MoviesService.getInstance();
		MoviesExample movExample = new MoviesExample();
		MoviesExample.Criteria movExamCriteria = movExample.createCriteria().andPatientidEqualTo(pntId);
		try {
			movLst = (List<Movies>)movService.selectByExample(movExample);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movLst;
	}
// Construct the str of movies displayed in the txt area.
	private String movInfo(String pntId) {
		String strMovInfo = "";
		List<Movies> lstMov = getMovInfo(pntId);
		for(int i= 0; i<lstMov.size() ; i++){
			strMovInfo =strMovInfo +  lstMov.get(i).getCreatetime()+ "    " + lstMov.get(i).getDescription()+"\n";
//			System.out.println(strMovInfo);
		}
		return strMovInfo;
	}

	private void doubleClick(String pntId) {
		newOpenFrm(pntId);
	}

	public boolean closeAllOpnFrms() {

		return true;
	}

	public void removeOpenFrm(OpenPatientsFrame frm) {
		hashPntFrms.remove(frm.getPntID());
	}

	public void newOpenFrm(String ID) {
		if (!hashPntFrms.containsKey(ID)) {
			Patients pnts = new Patients();
			PatientsService pntService = PatientsService.getInstance();
			OpenPatientsFrame btnOpenPntFrm = null;
			try {
				btnOpenPntFrm = new OpenPatientsFrame(pntService.selectByPrimaryKey(ID).getName(), ID,
						PatientsFrame.this.mainFrame);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			btnOpenPntFrm.setVisible(true);
			hashPntFrms.put(ID, btnOpenPntFrm);
		} else {
			OpenPatientsFrame openPntFrm = hashPntFrms.get(ID);
			if (openPntFrm.getExtendedState() != 0)
				openPntFrm.setExtendedState(0);

			if (!openPntFrm.isActive())
				openPntFrm.setVisible(true);
		}
	}

	public void validateOpenFrm(String ID) {
		if (hashPntFrms.containsKey(ID)) {
			OpenPatientsFrame btnOpenPntFrm = hashPntFrms.get(ID);
			btnOpenPntFrm.validateList();

		}
	}

	public JTextField getTxtPntName() {
		return txtPntName;
	}

	public void setTxtPntName(JTextField txtPntName) {
		this.txtPntName = txtPntName;
	}

	public JList getLstObj() {
		return lstPnt;
	}

	public void setLstObj(JList lstPnt) {
		this.lstPnt = lstPnt;
	}

	class OnSrchValueChanged implements DocumentListener {
		private String srchStr = null;
		List<Patients> list = null;

		public void insertUpdate(DocumentEvent e) {
			/*
			 * if(lstPnt.isFocusable() == true){
			 * txtSrchPnt.setText(getTxtPntName().getText()); }
			 */
			txtPntInfo.setText("");
			txtPntName.setText("");
			selectPnt();
		}

		public void removeUpdate(DocumentEvent e) {
			txtPntName.setText("");
			txtPntInfo.setText("");
			selectPnt();
		}

		public void changedUpdate(DocumentEvent e) {
			txtPntName.setText("");
			txtPntInfo.setText("");
			selectPnt();
		}

		private void selectPnt() {
			// txtPntName.setText("");
			// txtPntInfo.setText("");
			srchStr = txtSrchPnt.getText();
			list = getSrchPnt(srchStr);
			if (list != null) {
				Object[] pntSelectedArray = new Object[list.size()];
				for (int i = 0; i < list.size(); i++) {
					// pntSelectedArray[i] = list.get(i).getName();
					pntSelectedArray[i] = list.get(i);
				}
				getLstObj().setListData(pntSelectedArray);
			}
		}

		private List getSrchPnt(String pntName) {
			List<Patients> list = null;
			Patients pntDB = new Patients();
			PatientsService pntService = PatientsService.getInstance();
			PatientsExample pntExample = new PatientsExample();
			PatientsExample.Criteria pntExamCriteria = pntExample
					.createCriteria().andNameLike("%" + pntName + "%");
			try {
				list = (List<Patients>) pntService.selectByExample(pntExample);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}
	}
}
