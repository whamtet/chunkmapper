package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.chunkmapper.admin.PreferenceManager;
import com.chunkmapper.gui.panel.OrePanel;
import com.chunkmapper.writer.GenericWriter;

public class OreFrequencyDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			OreFrequencyDialog dialog = new OreFrequencyDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public OreFrequencyDialog(final JFrame jFrame) {
		super(jFrame);
		setTitle("Change Ore Frequency");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(100, 100, 700, 510);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(2, 4, 0, 0));
		final OrePanel[] orePanels = {
				new OrePanel("Coal", GenericWriter.COAL_WIDTH),
				new OrePanel("Diamond", GenericWriter.DIAMOND_WIDTH),
				new OrePanel("Emerald", GenericWriter.EMERALD_WIDTH),
				new OrePanel("Gold", GenericWriter.GOLD_WIDTH),
				new OrePanel("Iron", GenericWriter.IRON_WIDTH),
				new OrePanel("Lapis Lazuli", GenericWriter.LAPIS_LAZULI_WIDTH),
				new OrePanel("Redstone", GenericWriter.REDSTONE_WIDTH)
		};
		for (int i = 0; i < orePanels.length; i++) {
			contentPanel.add(orePanels[i], i);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GenericWriter.COAL_WIDTH = orePanels[0].getSpacing();
						GenericWriter.DIAMOND_WIDTH = orePanels[1].getSpacing();
						GenericWriter.EMERALD_WIDTH = orePanels[2].getSpacing();
						GenericWriter.GOLD_WIDTH = orePanels[3].getSpacing();
						GenericWriter.IRON_WIDTH = orePanels[4].getSpacing();
						GenericWriter.LAPIS_LAZULI_WIDTH= orePanels[5].getSpacing();
						GenericWriter.REDSTONE_WIDTH = orePanels[6].getSpacing();
						PreferenceManager.setOrePrefs();
						dispose();
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
						System.out.println(OreFrequencyDialog.this.getBounds());
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
