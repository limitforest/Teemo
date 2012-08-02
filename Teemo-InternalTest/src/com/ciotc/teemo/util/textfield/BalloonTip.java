package com.ciotc.teemo.util.textfield;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A balloon tip which is displayed above the attached component. 
 * The balloon tip uses a <code>JLabel</code> to render its content.
 * 
 * @author Bernhard Pauler
 */
public class BalloonTip extends JPanel {

    private BalloonBorder border;
    private JLabel label = new JLabel();

    private Component attachedComponent;

    public BalloonTip(Component attachedComponent, Color fillColor, int borderWidth, int offset) {
        this.attachedComponent = attachedComponent;

        label.setBorder(new EmptyBorder(borderWidth, borderWidth, borderWidth, borderWidth));
        label.setBackground(fillColor);
        label.setOpaque(true);

        setBorder(border = new BalloonBorder(fillColor, offset));
        setOpaque(false);

        setLayout(new BorderLayout());
        add(label);

        setVisible(false);

        // we use the popup layer of the top level container (frame or dialog) to show the balloon tip
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

        // if the attached component is moved while the balloon tip is visible, we need to move as well
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
        setBounds(location.x, location.y - getPreferredSize().height, getPreferredSize().width, getPreferredSize().height);	
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
        }
        super.setVisible(show);
    }

}
