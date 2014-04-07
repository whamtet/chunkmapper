package com.chunkmapper.gui.simple;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.parser.Nominatim;

public class FindDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	public double[] latlon;
	private JLabel label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FindDialog dialog = new FindDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void warn(String s) {
		label.setText(s);
	}

	/**
	 * Create the dialog.
	 */
	public FindDialog(JFrame parent) {
		super(parent);
		setResizable(false);
		setTitle("Find Latitude and Longitude");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 148);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblPlaceName = new JLabel("Place Name");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
						.addComponent(lblPlaceName))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addContainerGap(9, Short.MAX_VALUE)
					.addComponent(lblPlaceName)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(25))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				final JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						final String s = textField.getText();
						if (s.trim().equals("")) {
							warn("Enter valid place name");
							return;
						}
						okButton.setEnabled(false);
						okButton.setText("Finding...");
						warn("");
						
						SwingWorker<double[], Void> worker 
					       = new SwingWorker<double[], Void>() {
					 
					       @Override
					       public double[] doInBackground() {
					         return Nominatim.getPointSafe(s);
					       }
					       @Override
					       public void done() {
					    	 try {
								latlon = this.get();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								MyLogger.LOGGER.info(MyLogger.printException(e));
								return;
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								MyLogger.LOGGER.warning(MyLogger.printException(e));
							}
					    	 if (latlon == null) {
									warn("Location not found.");
									okButton.setEnabled(true);
									okButton.setText("OK");
								} else {
									dispose();
								}
					       }
					     };
					     worker.execute();
					}
				});
				{
					label = new JLabel("");
					buttonPane.add(label);
				}
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
