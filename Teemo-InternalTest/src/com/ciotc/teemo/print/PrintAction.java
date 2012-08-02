package com.ciotc.teemo.print;

import java.awt.event.ActionEvent;
import java.awt.print.Book;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.AbstractAction;
import javax.swing.JDialog;

class PrintAction extends AbstractAction {
	protected JDialog dialog;
	protected Book book;

	public PrintAction(JDialog dialog, Book book) {
//		设置图标的名称。
//		super("打印");
		this.dialog = dialog;
		this.book = book;
	}

	public PrintAction(Book book) {
		this.book = book;
		printAction();
	}

	public void actionPerformed(ActionEvent e) {
		printAction();
	}

	private void printAction() {
		// TODO Auto-generated method stub
		DocFlavor myFormat = DocFlavor.SERVICE_FORMATTED.PAGEABLE;

		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(MediaSizeName.ISO_A4);

		PrintService defaultService = PrintServiceLookup
				.lookupDefaultPrintService();
		PrintService[] printService = PrintServiceLookup.lookupPrintServices(
				myFormat, aset);
		// 打印机列表。
		/*
		 * for (int i = 0; i < printService.length; i++) { System.out.println(i
		 * + " " + printService[i]); }
		 */
		// 默认打印机。
		PrintService service = ServiceUI.printDialog(null, 200, 200,
				printService, defaultService, myFormat, aset);
		System.out.println("service: " + service);
		if (service != null) {
			if (myFormat == null) {
				System.out.println("PageFormat: myFormat 为空。");
				throw new IllegalArgumentException("null argument(s)");
			} else {
				if (this.book == null) {
					System.out.println("Book 为空。");
					throw new IllegalArgumentException("null argument(s)");
				}
			}

			Doc myDoc = new SimpleDoc(this.book, myFormat, null);

			DocPrintJob job = service.createPrintJob();
			try {
				job.print(myDoc, aset);
			} catch (PrintException pe) {
				System.out.println(pe.toString());
			}
		}
	}
}
