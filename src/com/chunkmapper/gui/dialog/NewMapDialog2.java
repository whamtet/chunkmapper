package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.chunkmapper.gui.layer.GameAvailableInterface;
import com.chunkmapper.gui.layer.MainLayer;

import java.awt.Dialog.ModalityType;

public class NewMapDialog2 extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JLabel lblNewLabel;
	private JButton okButton;
	private JButton cancelButton;
	private String gameName;
	
	public String getGameName() {
		return gameName;
	}

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	private void checkInputValid(GameAvailableInterface mainLayer) {
		String text = this.textField.getText();
		if (text.equals("")) {
			this.lblNewLabel.setVisible(false);
			this.okButton.setEnabled(false);
		} else if (!mainLayer.gameAvailable(text)) {
			this.okButton.setEnabled(false);
			this.lblNewLabel.setVisible(true);
		} else {
			this.okButton.setEnabled(true);
			this.lblNewLabel.setVisible(false);
		}
	}
	public NewMapDialog2(final GameAvailableInterface mainLayer, final JFrame frame) {
		super(frame);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Create New Map");
		Rectangle parentBounds = frame.getBounds();
		setBounds(parentBounds.x + 100, parentBounds.y + 100, 450, 111);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			textField = new JTextField();
			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					checkInputValid(mainLayer);
					
				}
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
						dispose();
					}
				}
			});
			textField.setColumns(10);
		}
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(18)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						NewMapDialog2.this.gameName = textField.getText();
						dispose();
					}
				});
				{
					lblNewLabel = new JLabel("Map Name Unavailable");
					lblNewLabel.setVisible(false);
				}
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
			}
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(Alignment.TRAILING, gl_buttonPane.createSequentialGroup()
						.addContainerGap(59, Short.MAX_VALUE)
						.addComponent(lblNewLabel)
						.addGap(109)
						.addComponent(okButton)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(cancelButton)
						.addContainerGap())
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel)
							.addComponent(okButton)
							.addComponent(cancelButton)))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
	}

}
