package com.ciotc.teemo.util.textfield;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.ciotc.teemo.util.Constant;

public class TeemoJTextField extends JTextField {
private static final long serialVersionUID = -3438563085728628986L;
private static final Color TIP_COLOR = new Color(255, 255, 225);
private static final Color HOVER_BACKGROUND_COLOR = new Color(255, 255, 225);
private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
private static final Color HOVER_BORDER_COLOR = new Color(215, 215, 215, 255);
private static final Color BORDER_COLOR = new Color(125, 125, 125, 100);
private int limit = Integer.MAX_VALUE;
private boolean numberOnly;
private CoolToolTip numberTip;
private CoolToolTip limitTip;
private CoolToolTip idCardTip;
private ImageIcon tipIcon;
private Border hoverBorder;
private Border border;

public TeemoJTextField() {
	initComponents();
	initEventListeners(this);
}

private void initComponents() {
	tipIcon = new ImageIcon(TeemoJTextField.class.getResource("tips.png"));
	
	numberTip = new CoolToolTip(this, TIP_COLOR, getColumns(), 10);
	numberTip.setText("只能输入数字！");
	numberTip.setIcon(tipIcon);
	numberTip.setIconTextGap(10);
	
	limitTip = new CoolToolTip(this, TIP_COLOR, getColumns(), 10);
	limitTip.setIcon(tipIcon);
	limitTip.setIconTextGap(10);
	
	idCardTip = new CoolToolTip(this, TIP_COLOR, getColumns(), 10);
	idCardTip.setText("身份证不合法。请核实。");
	idCardTip.setIcon(tipIcon);
	idCardTip.setIconTextGap(10);
	
	hoverBorder = new CoolBorder(HOVER_BORDER_COLOR, 3);
	border = BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR, 1), new EmptyBorder(
			new Insets(2, 2, 2, 2)));
	
	setBackground(BACKGROUND_COLOR);
	setBorder(border);
}

private void initEventListeners(final TeemoJTextField teemoJTextField) {
	addKeyListener(new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			char input = e.getKeyChar();
			// ESC27 ,Backspace 8 ,Enter 10, Del 127, must ignore
			boolean ignoreInput = input == (char) KeyEvent.VK_ESCAPE
					|| input == (char) KeyEvent.VK_BACK_SPACE || input == (char) KeyEvent.VK_ENTER
					|| input == (char) KeyEvent.VK_DELETE;
			if (ignoreInput) {
				limitTip.setVisible(false);
				numberTip.setVisible(false);
				return;
			}
			if (teemoJTextField.getText().length() + 1 > limit) {
				Toolkit.getDefaultToolkit().beep();
				deleteInputChar(e);
				limitTip.setVisible(true);
				return;
			} else {
				limitTip.setVisible(false);
			}
			if (numberOnly) {
				// 第18位可以是字母x。
				if (teemoJTextField.getText().length() < 17) {
//System.out.println(teemoJTextField.getText().length());
//					对前18位校验。
					if (!Character.isDigit(input)) {
						numberTip.setVisible(true);
						Toolkit.getDefaultToolkit().beep();
						deleteInputChar(e);
					} else {
						numberTip.setVisible(false);
					}
				} else if (input == 'X' || input == 'x' || Character.isDigit(input)) {
					// 第18位校验。
					// do nothing
					numberTip.setVisible(false);
				} else {
					numberTip.setVisible(true);
					Toolkit.getDefaultToolkit().beep();
					deleteInputChar(e);
				}
			}
		}
		
		private void deleteInputChar(KeyEvent source) {
			source.setKeyChar((char) KeyEvent.VK_CLEAR);
		}
	});
	
	addMouseListener(new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			setBorder(hoverBorder);
			setBackground(HOVER_BACKGROUND_COLOR);
			repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			setBorder(border);
			setBackground(BACKGROUND_COLOR);
			repaint();
		}
	});
}

public void setMaxTextLength(int limit) {
	if (limit < 0) {
		return;
	}
	this.limit = limit;
	limitTip.setText(String.format("超过最大长度 \"%d\"", limit));
}

public int getMaxTextLength() {
	return limit;
}

public void setNumberOnly(boolean numberOnly) {
	this.numberOnly = numberOnly;
}

public boolean isNumberOnly() {
	return this.numberOnly;
}

private class CoolToolTip extends JPanel {
private JLabel label = new JLabel();
private boolean haveShowPlace;

private Component attachedComponent;

public CoolToolTip(Component attachedComponent, Color fillColor, int borderWidth, int offset) {
	this.attachedComponent = attachedComponent;
	
	label.setBorder(new EmptyBorder(borderWidth, borderWidth, borderWidth, borderWidth));
	label.setBackground(fillColor);
	label.setOpaque(true);
	label.setFont(Constant.font);
	
	setOpaque(false);
	this.setBorder(new BalloonBorder(fillColor, offset));
	this.setLayout(new BorderLayout());
	add(label);
	
	setVisible(false);
	
	// if the attached component is moved while the balloon tip is
	// visible, we need to move as well
	attachedComponent.addComponentListener(new ComponentAdapter() {
		public void componentMoved(ComponentEvent e) {
			if (isShowing()) {
				determineAndSetLocation();
			}
		}
	});
	
}

private void determineAndSetLocation() {
	Point location = attachedComponent.getLocation();
	setBounds(location.x, location.y - getPreferredSize().height, getPreferredSize().width,
			getPreferredSize().height);
}

public void setText(String text) {
	label.setText(text);
}

public void setIcon(Icon icon) {
	label.setIcon(icon);
}

public void setIconTextGap(int iconTextGap) {
	label.setIconTextGap(iconTextGap);
}

public void setVisible(boolean show) {
	if (show) {
		determineAndSetLocation();
		findShowPlace();
	}
	super.setVisible(show);
}

private void findShowPlace() {
	if (haveShowPlace) {
		return;
	}
	// we use the popup layer of the top level container (frame or
	// dialog) to show the balloon tip
	// first we need to determine the top level container...
	Container parent = attachedComponent.getParent();
	JLayeredPane layeredPane;
	while (true) {
		if (parent instanceof JFrame) {
			layeredPane = ((JFrame) parent).getLayeredPane();
			break;
		} else if (parent instanceof JDialog) {
			layeredPane = ((JDialog) parent).getLayeredPane();
			break;
		}
		parent = parent.getParent();
	}
	layeredPane.add(this, JLayeredPane.POPUP_LAYER);
	haveShowPlace = true;
}
}
}
