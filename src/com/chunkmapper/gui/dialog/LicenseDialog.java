package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.admin.PreferenceManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class LicenseDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public boolean licenseAccepted;
	private JCheckBox chckbxSubmitAnonymousUsage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LicenseDialog dialog = new LicenseDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @param frame 
	 */
	public LicenseDialog(JFrame frame) {
		super(frame);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Chunkmapper License");
		setBounds(100, 100, 682, 435);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Accept");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (chckbxSubmitAnonymousUsage.isSelected()) {
							PreferenceManager.setAllowUsageReports();
						}
						licenseAccepted = true;
						dispose();
					}
				});
				{
					chckbxSubmitAnonymousUsage = new JCheckBox("Submit Anonymous Usage Reports");
					chckbxSubmitAnonymousUsage.setSelected(true);
					buttonPane.add(chckbxSubmitAnonymousUsage);
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Decline");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		textPane.setText(getLicense());
//		textPane.setContentType("text/html");
	}
	private static String getLicense() {
		try {
			InputStream in = LicenseDialog.class.getResource("/LICENSE.txt").openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
			return null;
		}
	}
}
