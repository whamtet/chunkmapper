package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.FeedbackManager;
import com.chunkmapper.admin.GlobalSettings;
import com.chunkmapper.admin.Utila;
import javax.swing.JTextField;

public class SettingsDialog extends JDialog {
	

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SettingsDialog dialog = new SettingsDialog(null, new GlobalSettings());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @param appFrame 
	 * @param globalSettings3 
	 * @throws  
	 */
	public SettingsDialog(final JFrame appFrame, final GlobalSettings globalSettings) {
		super(appFrame);
		setResizable(false);
		setTitle("Settings");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 492, 355);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		boolean allowLive;
		try {
			allowLive = BucketInfo.allowLive();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			allowLive = false;
		}
		final JCheckBox checkBox = new JCheckBox("Live Mode");
		checkBox.setEnabled(allowLive);
		if (allowLive) {
			checkBox.setSelected(globalSettings.isLive());
		}
		
		String color = allowLive ? "black" : "gray";
		JLabel lblNewLabel = new JLabel(String.format("<html><font color=\"%s\">Include the latest changes from Open Street Map.  Map may generate slowly.\n</html>", color));
		
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 0, 10, 1));
		spinner.setValue(globalSettings.getVerticalExaggeration());
		
		JLabel lblNewLabel_1 = new JLabel("<html>\nVertical Exaggeration\n(New maps only)\n</html>");
		
		JButton btnClearCache = new JButton("Clear Cache");
		btnClearCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null,
                        "Are you sure?",
                        "Delete Cache", 
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == 0) {
					try {
						Utila.clearCache();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		long availableMemory = Runtime.getRuntime().maxMemory();
		JLabel lblNewLabel_2 = new JLabel("xmx " + availableMemory);
		
		final JCheckBox chckbxGaiaMode = new JCheckBox("Gaia Mode");
		
		JLabel lblGenerateNoMan = new JLabel("Generate no man made features.");
		
		JButton btnChangeOre = new JButton("Change Ore...");
		btnChangeOre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				(new OreFrequencyDialog(appFrame)).setVisible(true);
			}
		});
		
		JLabel lblCommand = new JLabel("Command");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		final JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(globalSettings.generationRadius, new Integer(1), null, new Integer(1)));
		
		JLabel lblGenerationRadius = new JLabel("Generation Radius");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(20)
							.addComponent(lblNewLabel_2)
							.addGap(18)
							.addComponent(lblCommand)
							.addGap(18)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(checkBox)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 325, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addContainerGap()
									.addComponent(chckbxGaiaMode))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(14)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(spinner_1, Alignment.LEADING)
										.addComponent(spinner, Alignment.LEADING))))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblGenerationRadius)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblGenerateNoMan)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnClearCache)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnChangeOre)))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(checkBox)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxGaiaMode)
						.addComponent(lblGenerateNoMan))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGenerationRadius))
					.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnClearCache)
						.addComponent(btnChangeOre))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(lblCommand)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						globalSettings.setIsLive(checkBox.isSelected());
						globalSettings.setVerticalExaggeration((Integer) spinner.getValue());
						globalSettings.gaiaMode = chckbxGaiaMode.isSelected();
						globalSettings.generationRadius = (Integer) spinner_1.getValue();
						String command = textField.getText().trim().toLowerCase();
						if (command.equals("refresh")) {
							globalSettings.refreshNext = true;
						}
						if (command.equals("nz")) {
							globalSettings.nz = true;
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
