package com.ciotc.teemo.print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.ciotc.teemo.util.Constant;

public class PreviewDialog extends JDialog implements ActionListener {
	private final static double DEFAULT_ZOOM_FACTOR_STEP = 0.1;
	protected Pageable pageable;
	private Book book;

	public PreviewDialog(String title, JFrame owner, Pageable pageable,
			double zoom) {
		super(owner, title, true);
		this.pageable = pageable;
		book = (Book) pageable;
		Preview preview = new Preview(pageable, zoom);

		// Dialog 界面初始化。
		initLayout(preview, zoom);
	}

	public PreviewDialog(String title, JFrame owner, Pageable pageable) {
		this(title, owner, pageable, 0.0);
	}

	public PreviewDialog(String title, JFrame owner, Printable printable,
			PageFormat format, int pages, double zoom) {
		this(title, owner, new MyPageable(printable, format, pages), zoom);
	}

	public PreviewDialog(String title, JFrame owner, Printable printable,
			PageFormat format, int pages) {
		this(title, owner, printable, format, pages, 0.0);
	}

	private void initLayout(Preview preview, double zoom) {
		// TODO Auto-generated method stub

		this.setSize(new Dimension(700, 700));
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		this.getContentPane().setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(preview);
		this.getContentPane().add(scrollPane, "Center");

		// 打印工具栏。
		JToolBar printToolBar = new JToolBar();
		// toolbar.setRollover(true);
		this.getContentPane().add(printToolBar, "North");
		// printToolBar.setPreferredSize(new Dimension(450, 30));
		printToolBar.add(getButton("前一页", "back.png", new BrowseAction(preview,
				-1)));
		printToolBar.add(getButton("下一页", "forward.png", new BrowseAction(
				preview, 1)));
		printToolBar.add(new JToolBar.Separator());
		printToolBar.add(getButton("放大", "zoomIn.png", new ZoomAction(preview,
				DEFAULT_ZOOM_FACTOR_STEP)));
		printToolBar.add(getButton("缩小", "zoomOut.png", new ZoomAction(preview,
				-DEFAULT_ZOOM_FACTOR_STEP)));

		printToolBar.add(new JToolBar.Separator());
		printToolBar.add(getButton("打印", "printer.png", new PrintAction(this,
				book)));
		printToolBar.add(getButton("关闭预览", "close.png", new CloseAction(this)));
	}

	private JButton getButton(String name, String icon, AbstractAction action) {
		URL imageURL = getClass().getClassLoader()
				.getResource("images/" + icon);
		if (imageURL != null) {
			action.putValue("SmallIcon", new ImageIcon(imageURL));
		} else {
			action.putValue("Name", name);
		}
		return new JButton(action);
	}

	// static 解决问题？？？？？？
	private static class MyPageable implements Pageable {
		private Printable printable;
		private PageFormat format;
		private int pages;

		public MyPageable(Printable printable, PageFormat format, int pages) {
			this.printable = printable;
			this.format = format;
			this.pages = pages;
		}

		public int getNumberOfPages() {
			return pages;
		}

		public Printable getPrintable(int index) {
			if (index >= pages)
				throw new IndexOutOfBoundsException();
			return printable;
		}

		public PageFormat getPageFormat(int index) {
			if (index >= pages)
				throw new IndexOutOfBoundsException();
			return format;
		}

	}

	public void actionPerformed(ActionEvent e) {
		dispose();
	}

}
