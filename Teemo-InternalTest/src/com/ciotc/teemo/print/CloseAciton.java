package com.ciotc.teemo.print;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

class CloseAction extends AbstractAction{
	JDialog dialog = null;
	

	public CloseAction(PreviewDialog previewDialog) {
		// TODO Auto-generated constructor stub
		this.dialog = previewDialog;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.dispose();
	}
	
}
