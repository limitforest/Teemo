package com.ciotc.teemo.frame;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.IOUtils;

import com.ciotc.teemo.domain.Shortcomnts;
import com.ciotc.teemo.domain.ShortcomntsExample;
import com.ciotc.teemo.service.ShortcomntsService;
import com.ciotc.teemo.util.Constant;

public class EditDialog extends JDialog {
private JPanel pnlBase, pnlBtn;
private JList lstEditDialog;
private JTextField txtEdit;
private JButton btnAdd, btnReplace, btnDel, btnClose;
private JScrollPane scrllEditDialog;

public EditDialog(final CommentsDialog parent) {
	super(parent, getResourceString("editCommentDialog"), true);
	this.setSize(300, 300);
	this.setResizable(false);
	this.setLocationRelativeTo(null);
	this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			parent.getLstEdit().setListData(getRecords().toArray());
		}
	});
	
	pnlBase = new JPanel();
	pnlBase.setSize(300, 300);
	pnlBase.setLayout(null);
	
	pnlBtn = new JPanel();
	pnlBtn.setBounds(160, 2, 140, 300);
	pnlBtn.setLayout(null);
	
	txtEdit = new JTextField();
	txtEdit.setBounds(2, 2, 125, 35);
	txtEdit.setFont(Constant.font);
	txtEdit.setEditable(true);
	txtEdit.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	txtEdit.getDocument().addDocumentListener(new ValueChanged());
	
	btnAdd = new JButton(getResourceString("editCommentDialog.append"));
	btnAdd.setBounds(2, 55, 125, 35);
	btnAdd.setFont(Constant.font);
	btnAdd.setEnabled(false);
	btnAdd.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String tempAdd = txtEdit.getText().trim();
			if (tempAdd.length() == 0) {
				JOptionPane.showMessageDialog(EditDialog.this,
						getResourceString("editCommentDialog.appendNull"),
						getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
			} else {
				addRecords(tempAdd);
			}
			lstEditDialog.setListData(getRecords().toArray());
		}
	});
	
	btnReplace = new JButton(getResourceString("editCommentDialog.replace"));
	btnReplace.setBounds(2, 105, 125, 35);
	btnReplace.setFont(Constant.font);
	btnReplace.setEnabled(false);
	btnReplace.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String newStr = null;
			String oldStr = null;
			
			if (lstEditDialog.getSelectedValue() == null) {
//				已选择。
				JOptionPane.showMessageDialog(EditDialog.this,
						getResourceString("editCommentDialog.replaceNull"),
						getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
			} else{
//				要替换的内容和
				newStr = txtEdit.getText().trim();
				oldStr = lstEditDialog.getSelectedValue().toString().trim();
				if(newStr.equals(oldStr)){
					JOptionPane.showMessageDialog(EditDialog.this,
							getResourceString("editCommentDialog.replaceSame"),
							getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
				}else{
					List<Shortcomnts> tempList = getRecords();
					for (int i = 0; i < tempList.size(); i++) {
						if (tempList.get(i).getRecords().equals(oldStr)) {
							ShortcomntsService comntsService = ShortcomntsService.getInstance();
							tempList.get(i).setRecords(newStr);
							try {
								comntsService.updateByPrimaryKey(tempList.get(i));
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							JOptionPane.showMessageDialog(EditDialog.this,
									getResourceString("editCommentDialog.replaceSuccess"),
									getResourceString("Teemo"), JOptionPane.INFORMATION_MESSAGE);
							txtEdit.setText("");
							lstEditDialog.setListData(getRecords().toArray());
						}
					
				}
				
				}
			}
		}
	});
	
	btnDel = new JButton(getResourceString("editCommentDialog.delete"));
	btnDel.setBounds(2, 155, 125, 35);
	btnDel.setFont(Constant.font);
	btnDel.setEnabled(false);
	btnDel.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Shortcomnts> tempList = getRecords();
			String strDel = "";
			try {
				if (lstEditDialog.getSelectedValue() != null) {
					strDel = lstEditDialog.getSelectedValue().toString();
				} else {
					JOptionPane.showMessageDialog(EditDialog.this,
							getResourceString("editCommentDialog.select"),
							getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
				}
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
			for (int i = 0; i < tempList.size(); i++) {
				if (tempList.get(i).getRecords().equals(strDel)) {
					
					ShortcomntsService comntsService = ShortcomntsService.getInstance();
					try {
						comntsService.deleteByPrimaryKey(tempList.get(i).getId());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					JOptionPane.showMessageDialog(EditDialog.this,
							getResourceString("deleteSuccess"), getResourceString("Teemo"),
							JOptionPane.INFORMATION_MESSAGE);
					txtEdit.setText("");
					lstEditDialog.setListData(getRecords().toArray());
				} else {
				}
			}
		}
	});
	
	btnClose = new JButton(getResourceString("close"));
	btnClose.setBounds(2, 205, 125, 35);
	btnClose.setFont(Constant.font);
	btnClose.setEnabled(true);
	btnClose.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			parent.getLstEdit().setListData(getRecords().toArray());
		}
	});
	
	pnlBtn.add(txtEdit);
	pnlBtn.add(btnAdd);
	pnlBtn.add(btnReplace);
	pnlBtn.add(btnDel);
	pnlBtn.add(btnClose);
	
	lstEditDialog = new JList();
	lstEditDialog.setBounds(1, 1, 155, 264);
	lstEditDialog.setVisible(true);
	lstEditDialog.setFixedCellHeight(21);
	lstEditDialog.setFixedCellWidth(100);
	lstEditDialog.setFont(Constant.font);
	// At least one record in the txt file.
	// lstEditDialog.setSelectedIndex(0);
	lstEditDialog.setListData(getRecords().toArray());
	
	/*
	 * lstEditDialog.addListSelectionListener(new ListSelectionListener() { int
	 * i = 0;
	 * 
	 * @Override public void valueChanged(ListSelectionEvent e) { // TODO
	 * Auto-generated method stub System.out.println("元素变化" + (i++)); }
	 * 
	 * });
	 * 
	 * lstEditDialog.getModel().addListDataListener(new ListDataListener() {
	 * 
	 * @Override public void intervalAdded(ListDataEvent e) { // TODO
	 * Auto-generated method stub System.out.println("元素增加"); }
	 * 
	 * @Override public void intervalRemoved(ListDataEvent e) { // TODO
	 * Auto-generated method stub System.out.println("元素减少"); }
	 * 
	 * @Override public void contentsChanged(ListDataEvent e) { // TODO
	 * Auto-generated method stub System.out.println("元素变化。"); } });
	 */
	
	lstEditDialog.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent mev) {
			if (lstEditDialog.getSelectedIndex() != -1) {
				txtEdit.setText(lstEditDialog.getSelectedValue().toString());
				btnDel.setEnabled(true);
			} else {
			}
		}
	});
	
	scrllEditDialog = new JScrollPane();
	scrllEditDialog.setBounds(2, 2, 155, 264);
	scrllEditDialog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	scrllEditDialog.getViewport().add(lstEditDialog);
	
	this.add(pnlBase);
	pnlBase.add(pnlBtn);
	pnlBase.add(scrllEditDialog);
	
}

public EditDialog() {
	// TODO Auto-generated constructor stub
}

private void addRecords(String newAdd) {
	if (checkAdd(newAdd)) {
		// 通过校验。插入信息不同。
		Shortcomnts comnts = new Shortcomnts();
		ShortcomntsService comntsService = ShortcomntsService.getInstance();
		try {
			// Append to the file.
			/*
			 * IOUtils.write(newAdd+"\n", new FileOutputStream(f,true),
			 * "UTF-8"); if (JOptionPane.showConfirmDialog(EditDialog.this,
			 * getResourceString("editCommentDialog.insert"),
			 * getResourceString("Teemo"), JOptionPane.YES_NO_OPTION) ==
			 * JOptionPane.YES_OPTION) { txtEdit.setText("");
			 */
			comnts.setRecords(newAdd);
			comntsService.insert(comnts);
			JOptionPane.showMessageDialog(EditDialog.this,
					getResourceString("editCommentDialog.insertSuccess"),
					getResourceString("Teemo"), JOptionPane.INFORMATION_MESSAGE);
			txtEdit.setText("");
			lstEditDialog.setListData(getRecords().toArray());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} else {
		// 数据库中已经存在相同的数据。不插入。
		
	}
}

private boolean checkAdd(String newAdd) {
	// Check if there is a record of the same.
	boolean flag = false;
	List<Shortcomnts> tempList = getRecords();
	if (tempList.size() == 0) {
		flag = true;
	} else {
		for (int i = 0; i < tempList.size(); i++) {
			if (newAdd.equals(tempList.get(i).getRecords())) {
				flag = false;
				JOptionPane.showMessageDialog(EditDialog.this,
						getResourceString("editCommentDialog.alreadyExit"),
						getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
			} else {
				flag = true;
			}
		}
	}
	return flag;
}

/*
 * private void replaceRecords(String strRep, String oldRecord) { List<String>
 * tempFileList = getRecords(); for (int i = 0; i < tempFileList.size(); i++) {
 * if (!(oldRecord.equals(tempFileList.get(i)))) { Shortcomnts comnts = new
 * Shortcomnts(); System.out.println(comnts.getId()); comnts.setRecords(strRep);
 * 
 * ShortcomntsService service = ShortcomntsService.getInstance();
 * ShortcomntsExample example = new ShortcomntsExample();
 * ShortcomntsExample.Criteria criteria = example.createCriteria();
 * criteria.andRecordsEqualTo(oldRecord); try { service.updateByExample(comnts,
 * example); } catch (SQLException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } } } }
 */

/*
 * private void writeToFile(String[] srcArray, String targetFile) { // Write the
 * new contents to txt. // clearFile(targetFile); try { // FileWriter fWriter =
 * new FileWriter(new File(targetFile), true); // for (int i = 0; i <
 * srcArray.length; i++) { // fWriter.write(srcArray[i] +"\r\n"); // fWriter.w
 * // } // fWriter.close(); StringBuffer sb = new StringBuffer(); for (String
 * str : srcArray) { sb.append(str).append("\n"); }
 * 
 * // // 最后一个记录的\n 删掉。 if (sb.length() > 0) { sb.deleteCharAt(sb.length() - 1);
 * FileUtils.writeStringToFile(new File(targetFile), sb.toString(), "UTF-8"); }
 * else { FileUtils.writeStringToFile(new File(targetFile), "", "UTF-8"); }
 * 
 * } catch (IOException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } }
 */
/*
 * private void clearFile(String filepath) { // Clear the contents of the txt.
 * try { FileUtils.writeStringToFile(new File(filepath), "", "UTF-8"); // //
 * FileWriter fWriter = new FileWriter(new File(filepath)); //
 * fWriter.write(""); // fWriter.close(); } catch (IOException e) { // TODO
 * Auto-generated catch block e.printStackTrace(); } }
 */

List<Shortcomnts> getRecords() {
	Shortcomnts recds = new Shortcomnts();
	ShortcomntsService comntsService = ShortcomntsService.getInstance();
	ShortcomntsExample example = new ShortcomntsExample();
	ShortcomntsExample.Criteria criteria = example.createCriteria();
	criteria.andIdIsNotNull();
	List<Shortcomnts> allRecds = null;
	try {
		allRecds = comntsService.selectByExample(example);
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return allRecds;
}

class ValueChanged implements DocumentListener {
@Override
public void changedUpdate(DocumentEvent e) {
	changeBtnState();
}

@Override
public void insertUpdate(DocumentEvent e) {
	changeBtnState();
}

@Override
public void removeUpdate(DocumentEvent e) {
	changeBtnState();
}
}

private void changeBtnState() {
	String lstSelectedStr = "";
	String txtEditStr = txtEdit.getText().trim();
	// At least one item is selected in the JList.
	if (lstEditDialog.getSelectedIndex() != -1) {
		// When the contents of lst and txt are the same. btnDel is
		// activated.
		lstSelectedStr = lstEditDialog.getSelectedValue().toString();
		if (lstSelectedStr.equals(txtEditStr)) {
			btnAdd.setEnabled(false);
			btnReplace.setEnabled(false);
			btnDel.setEnabled(true);
			// When the contents of lst and txt are different,all btns are
			// activated.
		} else if ((!(lstSelectedStr.equals(txtEditStr))) && (txtEditStr.length() != 0)) {
			btnAdd.setEnabled(true);
			btnReplace.setEnabled(true);
			btnDel.setEnabled(true);
		} else {
			
		}
	} else if (txtEditStr.length() != 0) {
		btnAdd.setEnabled(true);
	}
	
}
}
