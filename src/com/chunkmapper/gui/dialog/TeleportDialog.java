package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import com.chunkmapper.Point;
import com.chunkmapper.writer.LevelDat;

public class TeleportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private double lat, lon;
	private JTextField latTextField;
	private JTextField lonTextField;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public TeleportDialog(final JFrame jFrame, final String name, final LevelDat levelDat, final Point rootPoint) {
		super(jFrame);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 480, 202);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblTeleportTo = new JLabel("Teleport to...");
		JLabel lblLat = new JLabel("Latitude");
		
		latTextField = new JTextField();
		latTextField.setColumns(10);
		PlainDocument doc1 = (PlainDocument) latTextField.getDocument();
		doc1.setDocumentFilter(new MyFilter(latTextField));
		
		JLabel lblLongitude = new JLabel("Longitude");
		
		lonTextField = new JTextField();
		lonTextField.setColumns(10);
		PlainDocument doc2 = (PlainDocument) lonTextField.getDocument();
		doc2.setDocumentFilter(new MyFilter(lonTextField));
		
		JButton btnNewButton = new JButton("Find Lat Lon...");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTeleportTo)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblLat)
							.addGap(18)
							.addComponent(latTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblLongitude)
							.addGap(18)
							.addComponent(lonTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnNewButton))
					.addContainerGap(76, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTeleportTo)
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLat)
						.addComponent(latTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLongitude)
						.addComponent(lonTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnNewButton)
					.addContainerGap(72, Short.MAX_VALUE))
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
						//showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType)
						String message = String.format("Warning: %s must not be open in Minecraft.  Continue?", name);
						String title = "";
						int optionType = JOptionPane.YES_NO_OPTION, messageType = JOptionPane.WARNING_MESSAGE;
						int choice = JOptionPane.showConfirmDialog(TeleportDialog.this, message, title, optionType, messageType);
						if (choice == JOptionPane.YES_OPTION) {
							levelDat.setPlayerPosition(lat, lon, rootPoint);
							levelDat.save();
							dispose();
						}
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
	
	private class MyFilter extends DocumentFilter {
		private JTextField parentField;
		public MyFilter(JTextField pField) {
			parentField = pField;
		}

		@Override
		public void insertString(FilterBypass fb, int offset, String string,
				AttributeSet attr) throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.insert(offset, string);

			if (test(sb.toString())) {
				super.insertString(fb, offset, string, attr);
			}
		}

		private boolean test(String text) {

			try {
				if (text.isEmpty()) return true;
				if (text.endsWith("d") || text.endsWith("f")) return false;
				if ("-".equals(text)) return true;
				if (parentField == latTextField) {
					lat = Double.parseDouble(text);
				} else {
					lon = Double.parseDouble(text);
				}

				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text,
				AttributeSet attrs) throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.replace(offset, offset + length, text);

			if (test(sb.toString())) {
				super.replace(fb, offset, length, text, attrs);
			}

		}

		@Override
		public void remove(FilterBypass fb, int offset, int length)
				throws BadLocationException {
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.delete(offset, offset + length);

			if (test(sb.toString())) {
				super.remove(fb, offset, length);
			}

		}
	}
}
