package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class FeedbackDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

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

	/**
	 * Create the dialog.
	 */
	public FeedbackDialog() {
		setTitle("Feedback");
		setResizable(false);
		setBounds(100, 100, 524, 444);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblPleaseHelpMake = new JLabel("Please help make Chunkmapper better");
		
		JLabel lblWereThereAny = new JLabel("Were there any problems?");
		
		JCheckBox chckbxMapsDidntCompletely = new JCheckBox("Maps didn't completely generate");
		
		JCheckBox chckbxChunkmapperMadeMy = new JCheckBox("Chunkmapper made my computer very slow");
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("It was difficult to click Chunkmapper buttons");
		
		JCheckBox chckbxItWasDifficult = new JCheckBox("It was difficult to move the globe");
		
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
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Submit");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});
				
				JCheckBox chckbxDontAskAgain = new JCheckBox("Don't ask again");
				buttonPane.add(chckbxDontAskAgain);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("No Thanks");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
