/**
 * @(#)CoolBorder.java  0.1.2  2007-9-10
 */
package com.ciotc.teemo.util.textfield;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * Custom Border.
 * 
 * @version 0.1.2, 2007-9-10
 * @author ruislan <a href="mailto:z17520@126.com"/>
 */
public class CoolBorder implements Border {
	private int thickness;
	private Insets insets;
	private Dimension lastComponentSize;
	private Color color;
	private Color color2;

	public CoolBorder(Color color, int thickness) {
		this.color = color;
		if (color == null) {
			this.color = color = Color.gray;
		}
		color2 = new Color(210, 210, 210, 0);
		this.thickness = thickness;
	}

	@Override
	public Insets getBorderInsets(Component c) {
		Dimension currentComponent = c.getSize();

		if (currentComponent.equals(lastComponentSize)) {
			return insets;
		}

		insets = new Insets(thickness, thickness, thickness, thickness);
		lastComponentSize = currentComponent;
		return insets;
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		Graphics2D g2d = (Graphics2D) g.create();
		// 画上边缘
		GradientPaint gp = new GradientPaint(x, y, color, x, y + thickness,
				color2);
		g2d.setPaint(gp);
		g2d.fillRect(x, y, width, thickness);
		// 画下边缘
		gp = new GradientPaint(x, y + height - thickness - 1, color2, x, y
				+ height, color);
		g2d.setPaint(gp);
		g2d.fillRect(x, y + height - thickness - 1, width, thickness);
		// 画左边缘
		gp = new GradientPaint(x, y, color, x + thickness, y, color2);
		g2d.setPaint(gp);
		g2d.fillRect(x, y, thickness, height);
		// 画右边缘
		gp = new GradientPaint(x + width - thickness - 1, y, color2, x + width,
				y, color);
		g2d.setPaint(gp);
		g2d.fillRect(x + width - thickness - 1, y, thickness, height);
		// 画外框
		g2d.setPaint(color);
		g2d.drawRect(x, y, width - 1, height - 1);
		g2d.dispose();
	}

}
