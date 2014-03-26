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

public class FeedbackDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JCheckBox chckbxDontAskAgain;
	private JCheckBox chckbxMapsDidntCompletely;
	private JCheckBox chckbxChunkmapperMadeMy;
	private JCheckBox chckbxNewCheckBox;
	private JCheckBox chckbxItWasDifficult;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FeedbackDialog dialog = new FeedbackDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void submitFeedback() {
		ArrayList<NameValuePair> l = new ArrayList<NameValuePair>();
		l.add(new BasicNameValuePair("incomplete_maps", "" + chckbxMapsDidntCompletely.isSelected()));
		l.add(new BasicNameValuePair("too_slow", "" + chckbxChunkmapperMadeMy.isSelected()));
		l.add(new BasicNameValuePair("difficult_to_click", "" + chckbxNewCheckBox.isSelected()));
		l.add(new BasicNameValuePair("difficult_to_move", "" + chckbxItWasDifficult.isSelected()));
		l.add(new BasicNameValuePair("comments", textArea.getText()));
		FeedbackManager.submitFeedback(l);
	}
	public FeedbackDialog() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (chckbxDontAskAgain.isSelected()) {
					PreferenceManager.setIgnoreFeedback();
				}
			}
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		setTitle("Feedback");
		setResizable(false);
		setBounds(100, 100, 524, 444);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblPleaseHelpMake = new JLabel("Please help make Chunkmapper better");

		JLabel lblWereThereAny = new JLabel("Were there any problems?");

		chckbxMapsDidntCompletely = new JCheckBox("Maps didn't completely generate");

		chckbxChunkmapperMadeMy = new JCheckBox("Chunkmapper made my computer very slow");

		chckbxNewCheckBox = new JCheckBox("It was difficult to click Chunkmapper buttons");

		chckbxItWasDifficult = new JCheckBox("It was difficult to move the globe");

		JLabel lblComments = new JLabel("Comments");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
								.addComponent(lblPleaseHelpMake)
								.addComponent(lblWereThereAny)
								.addComponent(chckbxMapsDidntCompletely)
								.addComponent(chckbxChunkmapperMadeMy)
								.addComponent(chckbxNewCheckBox)
								.addComponent(chckbxItWasDifficult)
								.addComponent(lblComments))
								.addContainerGap())
				);
		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblPleaseHelpMake)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblWereThereAny)
						.addGap(18)
						.addComponent(chckbxMapsDidntCompletely)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(chckbxChunkmapperMadeMy)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(chckbxNewCheckBox)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(chckbxItWasDifficult)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblComments)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(35, Short.MAX_VALUE))
				);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);
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
								if (chckbxDontAskAgain.isSelected()) {
									PreferenceManager.setIgnoreFeedback();
								}
								submitFeedback();
								System.exit(0);
							}
						});
						t.start();
					}

				});

				chckbxDontAskAgain = new JCheckBox("Don't ask again");
				buttonPane.add(chckbxDontAskAgain);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				final JButton cancelButton = new JButton("No Thanks");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (chckbxDontAskAgain.isSelected()) {
							PreferenceManager.setIgnoreFeedback();
						}
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
