package com.chunkmapper.gui.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

public class NoMinecraftDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 * @param appFrame 
	 * @param wwd 
	 */
	public NoMinecraftDialog(final double lat, final double lon, final JFrame appFrame) {
		super(appFrame);
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 250);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		{
			JTextPane txtpnCannotLocateMinecraft = new JTextPane();
			txtpnCannotLocateMinecraft.setText("Chunkmapper cannot locate your Minecraft saves folder.\nMinecraft must be installed to continue.");
			contentPanel.add(txtpnCannotLocateMinecraft);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Locate Manually");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						chooseMinecraftFile(lat, lon, appFrame);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
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
	private static void chooseMinecraftFile(double lat, double lon, JFrame appFrame) {
		JFileChooser fc = new JFileChooser();
		
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//todo: disable multiple file selection
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new MinecraftInstallFileFilter());
		fc.setDialogTitle("Select Minecraft Install Location");

		int selection = fc.showOpenDialog(appFrame);
		if (selection == JFileChooser.APPROVE_OPTION) {
			MapMakingDialog d = new MapMakingDialog(lat, lon, fc.getSelectedFile(), appFrame);
			d.setVisible(true);
		}
	}
	private static class MinecraftInstallFileFilter extends FileFilter {

		@Override
		public boolean accept(File arg0) {
			
			File binDir = new File(arg0, "bin"), minecraftJar = new File(binDir, "minecraft.jar");
			return minecraftJar.exists();
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "Minecraft Directory";
		}

	}

	
	

	
}
