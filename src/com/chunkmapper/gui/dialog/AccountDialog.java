package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.chunkmapper.security.MySecurityManager;
import com.chunkmapper.security.MySecurityManager.Status;
import java.awt.Dialog.ModalityType;

public class AccountDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passwordField;
	public boolean ok;
	JLabel lblToCreateA;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AccountDialog dialog = new AccountDialog(null);
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
		JOptionPane.showMessageDialog(this, "Please visit www.chunkmapper.com to create an account.");
	}
	private void verifyAccount() {
		Status s = MySecurityManager.getStatus(textField.getText(), new String(passwordField.getPassword()));
		if (s == null) {
			lblToCreateA.setText("<html>Chunkmapper could not connect online.  Please check your connection</html>");
			lblToCreateA.setForeground(Color.RED);
			return;
		}
		switch(s) {
		case OK:
			ok = true;
			dispose();
			return;
		case HACKED:
			lblToCreateA.setText("<html>Your account has been disabled because of suspicious activity.  Please email support@chunkmapper.com</html>");
			lblToCreateA.setForeground(Color.RED);
			return;
		case UNPAID:
			lblToCreateA.setText("<html>Your account has not been activated.  Please complete payment or email support@chunkmapper.com</html>");
			lblToCreateA.setForeground(Color.RED);
			return;
		case INVALID_PW:
			lblToCreateA.setText("<html>Email or password invalid.</html>");
			lblToCreateA.setForeground(Color.RED);
			return;
		}
	}

	/**
	 * Create the dialog.
	 */
	public AccountDialog(JFrame f) {
		super(f);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		
		setResizable(false);
		setTitle("Verify Account");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		lblToCreateA = new JLabel("Please enter your account details");
		
		JLabel lblEmail = new JLabel("Email");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		
		passwordField = new JPasswordField();
		
		JButton btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openWebpage();
			}
		});
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblToCreateA)
						.addComponent(lblEmail)
						.addComponent(lblPassword)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(passwordField, Alignment.LEADING)
							.addComponent(textField, Alignment.LEADING))
						.addComponent(btnCreateAccount))
					.addContainerGap(227, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblToCreateA)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEmail)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblPassword)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnCreateAccount)
					.addContainerGap(48, Short.MAX_VALUE))
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
						verifyAccount();
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
