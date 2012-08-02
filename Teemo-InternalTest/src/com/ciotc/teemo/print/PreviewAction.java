package com.ciotc.teemo.print;

import java.awt.event.ActionEvent;
import java.awt.print.Book;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;

import com.ciotc.teemo.file.frame.DisplayInternalFrame;

class PreviewAction extends AbstractAction {
	protected JFrame frame;
	protected Book book;

	public PreviewAction(JFrame frame, Book book) {
		super("Vorschau");
		this.frame = frame;
		this.book = book;
	}

	public void actionPerformed(ActionEvent e) {
		PreviewDialog dialog = new PreviewDialog("Vorschau", this.frame,
				this.book);
		dialog.pack();
		dialog.setVisible(true);
	}
}
