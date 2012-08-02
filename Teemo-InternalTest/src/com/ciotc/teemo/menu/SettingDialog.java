package com.ciotc.teemo.menu;

import static com.ciotc.teemo.resource.MyResources.getResizableIconFromResource;
import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.pushingpixels.flamingo.api.common.JCommandButton;

public class SettingDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton cancelButton;
	private JButton okButton;
	private JCommandButton recordButton;
	private JCommandButton networkButton;
	private JCommandButton UserButton;
	private JCommandButton languageButton;
	private JButton defaultButton;
	private JButton applyButton;
	private Component currentComponent;
	private MainPanel mainPanel;
	private RecordSettingPanel recordPanel;
	private NetworkPanel networkPanel;
	private UserPreferencePanel userPreferencePanel;
	private LanguagePanel languagePanel;

	public SettingDialog() {
		setTitle(getResourceString("setting"));
		JPanel panel = null;
		mainPanel = new MainPanel();
		add(mainPanel, BorderLayout.CENTER);
		panel = createButtonPanel();
		add(panel, BorderLayout.WEST);
		panel = createBottomPanel();
		add(panel, BorderLayout.SOUTH);

		mainPanel.show(recordPanel);
		//pack();
		setSize(500,350);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public void addListener(PropertyChangeListener listener){
		userPreferencePanel.addPropertyChangeListener(listener);
		languagePanel.addPropertyChangeListener(listener);
	}
	
	public void removeListener(PropertyChangeListener listener){
		userPreferencePanel.removePropertyChangeListener(listener);
		languagePanel.removePropertyChangeListener(listener);
	}
	
	class MainPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		void show(Component component) {
			if (currentComponent != null) {
				remove(currentComponent);
			}
			add("Center", currentComponent = component);
			revalidate();
			repaint();
		}

		public MainPanel() {
			setLayout(new BorderLayout());
			setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			add(createMainSouthPanel(), BorderLayout.SOUTH);
		}
		
		private JPanel createMainSouthPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			panel.add(Box.createHorizontalGlue());
			defaultButton = new JButton(getResourceString("default"));
			applyButton = new JButton(getResourceString("apply"));
						
			defaultButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(currentComponent == recordPanel){
						recordPanel.setDefault();
					}else if(currentComponent == networkPanel){
						networkPanel.setDefault();
					}else if(currentComponent == userPreferencePanel){
						userPreferencePanel.setDefault();
					}else if(currentComponent == languagePanel){
						languagePanel.setDefault();
					}
				}
			});
			applyButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(currentComponent == recordPanel){
						recordPanel.apply();
					}else if(currentComponent == networkPanel){
						networkPanel.apply();
					}else if(currentComponent == userPreferencePanel){
						userPreferencePanel.apply();
					}else if(currentComponent == languagePanel){
						languagePanel.apply();
					}
				}
			});
			
			
			panel.add(Box.createRigidArea(new Dimension(10, 0)));
			panel.add(applyButton);
			panel.add(Box.createRigidArea(new Dimension(10, 0)));
			panel.add(defaultButton);

			return panel;

		}
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(4, 1));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		recordButton = new JCommandButton(getResourceString("RecordSettingDialog.title"), getResizableIconFromResource("images/toolbar/setting1.png"));
		networkButton = new JCommandButton(getResourceString("networkDialog.title"), getResizableIconFromResource("images/toolbar/setting2.png"));
		UserButton = new JCommandButton(getResourceString("userPreferenceDialog.title"), getResizableIconFromResource("images/toolbar/setting3.png"));
		languageButton = new JCommandButton(getResourceString("SelectLanguageFrame.title"), getResizableIconFromResource("images/toolbar/language.png"));
		
		
		recordPanel = new RecordSettingPanel();
		networkPanel = new NetworkPanel(); 
		userPreferencePanel = new UserPreferencePanel();
		languagePanel = new LanguagePanel();
		recordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.show(recordPanel);
			}
		});
		networkButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.show(networkPanel);
			}
		});
		UserButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.show(userPreferencePanel);
			}
		});
		languageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.show(languagePanel);
			}
		});
		
		panel.add(recordButton);
		panel.add(networkButton);
		panel.add(UserButton);
		panel.add(languageButton);

		return panel;
	}

	private JPanel createBottomPanel() {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonPane.add(Box.createHorizontalGlue());
		cancelButton = new JButton(getResourceString("cancel"));
		okButton = new JButton(getResourceString("ok"));

		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				recordPanel.apply();
				networkPanel.apply();
				userPreferencePanel.apply();
				languagePanel.apply();
				dispose();
				
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(okButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);

		return buttonPane;
	}

	public static void main(String[] args) {
		SettingDialog sd = new SettingDialog();
		sd.setVisible(true);
		sd.addListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(evt.getPropertyName());
			}
		});
		sd.setLocationRelativeTo(null);
		sd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		sd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
	}
}
