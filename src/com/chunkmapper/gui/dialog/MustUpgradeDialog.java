package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import com.chunkmapper.OpenURL;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MustUpgradeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblThisVersionOf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MustUpgradeDialog dialog = new MustUpgradeDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MustUpgradeDialog() {
		setTitle("Please Upgrade");
		setBounds(100, 100, 450, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblThisVersionOf = new JLabel("This version of Chunkmapper has been discontinued.");
		}
		
		JLabel lblVisitWwwchunkmappercomTo = new JLabel("<html>Visit <a href=\"http://www.chunkmapper.com\"> www.chunkmapper.com </a> to download the latest version.</html>");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(52)
					.addComponent(lblThisVersionOf)
					.addContainerGap(176, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addContainerGap(36, Short.MAX_VALUE)
					.addComponent(lblVisitWwwchunkmappercomTo)
					.addGap(22))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(5)
					.addComponent(lblThisVersionOf)
					.addGap(18)
					.addComponent(lblVisitWwwchunkmappercomTo)
					.addContainerGap(45, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Go");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						OpenURL.openWebpage("http://www.chunkmapper.com");
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
