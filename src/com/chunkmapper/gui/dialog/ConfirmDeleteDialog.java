package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

public class ConfirmDeleteDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel txtpnDeleteMap;
	public boolean hasDeletedSomething = false;


	/**
	 * Create the dialog.
	 */
	public ConfirmDeleteDialog(final JFrame parent, final String gameName, final File savesDir) {
		super(parent);
		setResizable(false);
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Rectangle parentBounds = parent.getBounds();
		setBounds(parentBounds.x + 100, parentBounds.y + 100, 217, 100);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			txtpnDeleteMap = new JLabel();
			txtpnDeleteMap.setOpaque(false);
			txtpnDeleteMap.setBackground(Color.LIGHT_GRAY);
			txtpnDeleteMap.setText("Delete Map " + gameName + "?");
		}
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtpnDeleteMap, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(txtpnDeleteMap, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Yes");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//delete game
						hasDeletedSomething = true;
						File dirToDelete = new File(savesDir, gameName);
						if (dirToDelete.exists()) {
							try {
								FileUtils.deleteDirectory(dirToDelete);
								
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("No");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	public static void main(String[] args) {
		ConfirmDeleteDialog d = new ConfirmDeleteDialog(null, "hi", new File("hi"));
		d.setEnabled(true);
	}

}
