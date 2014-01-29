package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import com.chunkmapper.Utila;
import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.gui.GlobalSettingsImpl;
import com.chunkmapper.interfaces.GlobalSettings;

public class SettingsDialog extends JDialog {
	

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SettingsDialog dialog = new SettingsDialog(null, new GlobalSettingsImpl());
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
	 */
	public SettingsDialog(JFrame appFrame, final GlobalSettings globalSettings) {
		super(appFrame);
		setResizable(false);
		setTitle("Settings");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 492, 265);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		boolean allowLive = BucketInfo.allowLive();
		final JCheckBox checkBox = new JCheckBox("Live Mode");
		checkBox.setEnabled(allowLive);
		
		String color = allowLive ? "black" : "gray";
		JLabel lblNewLabel = new JLabel(String.format("<html><font color=\"%s\">Include the latest changes from Open Street Map.  Map may generate slowly.\n</html>", color));
		
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 0, 10, 1));
		
		JLabel lblNewLabel_1 = new JLabel("<html>\nVertical Exaggeration\n(New maps only)\n</html>");
		
		JButton btnClearCache = new JButton("Clear Cache");
		btnClearCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null,
                        "Are you sure?",
                        "Delete Cache", 
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == 0) {
					Utila.CACHE.delete();
				}
			}
		});
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addContainerGap()
									.addComponent(checkBox))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(14)
									.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 325, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)))
						.addComponent(btnClearCache))
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
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnClearCache)
					.addContainerGap(43, Short.MAX_VALUE))
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
