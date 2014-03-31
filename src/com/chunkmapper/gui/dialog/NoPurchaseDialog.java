package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.chunkmapper.admin.FeedbackManager;
import com.chunkmapper.admin.PreferenceManager;

public class NoPurchaseDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JCheckBox badInterface;
	private JCheckBox cantpay;
	private JCheckBox dontwanttopay;
	public boolean submitted;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NoPurchaseDialog dialog = new NoPurchaseDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void submitFeedback() {
		submitted = true;
		ArrayList<NameValuePair> l = new ArrayList<NameValuePair>();
		l.add(new BasicNameValuePair("bad-interface", "" + badInterface.isSelected()));
		l.add(new BasicNameValuePair("cant-pay", "" + cantpay.isSelected()));
		l.add(new BasicNameValuePair("dont-want-to-pay", "" + dontwanttopay.isSelected()));
		FeedbackManager.submitFeedback(l);
	}
	public NoPurchaseDialog() {
		setModal(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				PreferenceManager.setNoPurchaseShown();
			}
//			public void windowClosed(WindowEvent e) {
//				System.exit(0);
//			}
		});
		setTitle("Feedback");
		setResizable(false);
		setBounds(100, 100, 524, 198);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblPleaseHelpMake = new JLabel("Please tell us why you didn't purchase Chunkmapper (anonymous)");

		badInterface = new JCheckBox("Interface not working");

		cantpay = new JCheckBox("Can't pay for it");

		dontwanttopay = new JCheckBox("Too expensive");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPleaseHelpMake)
						.addComponent(badInterface)
						.addComponent(cantpay)
						.addComponent(dontwanttopay))
					.addContainerGap(177, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPleaseHelpMake)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(badInterface)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cantpay)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(dontwanttopay)
					.addContainerGap(258, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				final JButton okButton = new JButton("Submit");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButton.setEnabled(false);
						okButton.setText("Submitting...");
						Thread t = new Thread(new Runnable() {
							public void run() {
								submitFeedback();
								PreferenceManager.setNoPurchaseShown();
								dispose();
							}
						});
						t.start();
					}

				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				final JButton cancelButton = new JButton("No Thanks");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PreferenceManager.setNoPurchaseShown();
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
