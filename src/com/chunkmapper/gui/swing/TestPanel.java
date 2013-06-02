package com.chunkmapper.gui.swing;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.SystemColor;

public class TestPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public TestPanel() {
		setBackground(SystemColor.desktop);
		
		JLabel lblNewLabel = new JLabel("New label");
		add(lblNewLabel);

	}

}
