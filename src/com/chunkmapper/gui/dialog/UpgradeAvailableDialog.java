package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.chunkmapper.admin.PreferenceManager;

public class UpgradeAvailableDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JCheckBox chckbxDontAskAgain;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if (PreferenceManager.getIgnoreUpgrade()) {
			System.out.println("ignoring");
			System.exit(0);
		}
		try {
			UpgradeAvailableDialog dialog = new UpgradeAvailableDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void openWebpage() {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URI("http://www.chunkmapper.com"));
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(this, "Please visit www.chunkmapper.com to upgrade.");
	}
	/**
	 * Create the dialog.
	 */
	public UpgradeAvailableDialog(JFrame f) {
		super(f);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("closing");
				if (chckbxDontAskAgain.isSelected()) {
					PreferenceManager.setIgnoreUpgrade();
				}
			}
			
		});
		
		setTitle("Upgrade Available");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(100, 100, 450, 129);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblANewVersion = new JLabel("Upgrade Chunkmapper for the latest features and best performance.");
		
		chckbxDontAskAgain = new JCheckBox("Don't ask again");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblANewVersion)
						.addComponent(chckbxDontAskAgain))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblANewVersion)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxDontAskAgain)
					.addContainerGap(7, Short.MAX_VALUE))
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
						if (chckbxDontAskAgain.isSelected()) {
							PreferenceManager.setIgnoreUpgrade();
						}
						openWebpage();
						System.exit(0);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Later");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (chckbxDontAskAgain.isSelected()) {
							PreferenceManager.setIgnoreUpgrade();
						}
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
