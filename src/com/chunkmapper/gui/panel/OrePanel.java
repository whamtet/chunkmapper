package com.chunkmapper.gui.panel;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class OrePanel extends JPanel {
	/**
	 * @wbp.nonvisual location=55,161
	 */
	private final JSpinner spinner = new JSpinner();
	
	public int getSpacing() {
		return 2 * ((Integer) spinner.getValue());
	}
	private void setSpacing(int i) {
		spinner.setValue(i / 2);
	}

	//	public Image image;
	//	@Override
	//    protected void paintComponent(Graphics g) {
	//        super.paintComponent(g);
	//        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
	//    }
	public OrePanel(String caption, int frequency) {

		spinner.setModel(new SpinnerNumberModel(new Integer(5), new Integer(5), null, new Integer(1)));
		setSpacing(frequency);
		String f = caption.toLowerCase().replace(" ", "_") + ".png";
		URL url = OrePanel.class.getResource("/ore/" + f);
		ImageIcon icon = new ImageIcon(url);
		setLayout(null);
		JLabel lblHi = new JLabel(caption,
				icon,
				JLabel.CENTER);
		lblHi.setBounds(0, 0, 150, 180);
		//Set the position of the text, relative to the icon:
		lblHi.setVerticalTextPosition(JLabel.BOTTOM);
		lblHi.setHorizontalTextPosition(JLabel.CENTER);
		add(lblHi);
		
		JLabel spacing = new JLabel("Ore Spacing");
		spacing.setBounds(10, 180, 90, 30);
		add(spacing);
		spinner.setBounds(95, 180, 55, 30);
		add(spinner);

		


	}
	


}
