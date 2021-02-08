package com.chunkmapper.gui.simple;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.ManagingThread;
import com.chunkmapper.Point;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.gui.dialog.NewMapDialog.NewGameInfo;
import com.chunkmapper.gui.dialog.SettingsDialog;
import com.chunkmapper.writer.LevelDat;

public class GeneratingPanel extends JPanel {
	private JTextField latTextField;
	private JTextField lonTextField;
	private MapPanel panel;
	private double lat = 0d, lon = 0d;
	private JButton btnGenerateMap, btnFindLatLon, btnSettings, btnDeleteMap;
	private boolean generating;
	private final File gameFolder;
	private final SimplifiedGUI simplifiedGUI;
	private ManagingThread t;
	private final LevelDat levelDat;
	private final NewGameInfo newGameInfo;
	/**
	 * Create the panel.
	 */
	private void startGeneration() {
		if (lat < -90) {
			alert("Minimum Latitude is -90 (90 South)");
			return;
		}
		if (lat > 90) {
			alert("Maximum Latitude is 90 (90 North)");
			return;
		}
		if (lon < -180) {
			alert("Minimum Longitude is -180 (180 West)");
			return;
		}
		if (lon > 180) {
			alert("Maximum Longitude is 180 (180 East)");
			return;
		}

		t = new ManagingThread(lat, lon, gameFolder, panel, panel,null, newGameInfo);
		t.start();
		btnGenerateMap.setText("Cancel...");
		setAllEnabled(false);
		generating = true;
	}
	private void cancelGeneration() {
		System.out.println("cancelling");
		ManagingThread.blockingShutDown(t, false);
		setAllEnabled(true);
		btnGenerateMap.setText("Resume...");
		generating = false;
		repaint();
	}
	private void setAllEnabled(boolean b) {
		latTextField.setEnabled(b);
		lonTextField.setEnabled(b);
		btnSettings.setEnabled(b);
		btnFindLatLon.setEnabled(b);
		btnDeleteMap.setEnabled(b);
		if (simplifiedGUI != null) {
			simplifiedGUI.setActive(b);
		}
	}

	public GeneratingPanel(File gameFolder, final SimplifiedGUI simplifiedGUI, NewGameInfo newGameInfo) {

		this.simplifiedGUI = simplifiedGUI;
		this.gameFolder = gameFolder;
		this.newGameInfo = newGameInfo;

		JLabel lblMapname = new JLabel(gameFolder.getName());

		JLabel lblPlayerPosition = new JLabel("Player Position");

		latTextField = new JTextField();
		latTextField.setColumns(10);

		PlainDocument doc = (PlainDocument) latTextField.getDocument();
		doc.setDocumentFilter(new MyFilter(latTextField));

		JLabel lblLatitude = new JLabel("Latitude");

		JLabel lblLongitude = new JLabel("Longitude");

		lonTextField = new JTextField();
		lonTextField.setColumns(10);
		PlainDocument doc1 = (PlainDocument) lonTextField.getDocument();
		doc1.setDocumentFilter(new MyFilter(lonTextField));

		GameMetaInfo info = null;
		try {
			info = new GameMetaInfo(gameFolder, 0, 0, 0, false);
		} catch (IOException e1) {
			MyLogger.LOGGER.warning(MyLogger.printException(e1));
		}
		LevelDat lLevelDat = null;
		try {
			lLevelDat = LevelDat.getFromGameFolder(gameFolder);
			Point relativePlayerPoint = lLevelDat.getPlayerPosition();
			double lat = - (relativePlayerPoint.z + info.rootPoint.z * 512) / 3600.;
			double lon = (relativePlayerPoint.x + info.rootPoint.x * 512) / 3600.;
			latTextField.setText(String.format("%.4f", lat));
			lonTextField.setText(String.format("%.4f", lon));
		} catch (IOException e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
			lLevelDat = null;
		}
		levelDat = lLevelDat;
		if (info != null && !info.isNew) {
				Point relativePlayerPoint = levelDat.getPlayerPosition();
				double lat = - (relativePlayerPoint.z + info.rootPoint.z * 512) / 3600.;
				double lon = (relativePlayerPoint.x + info.rootPoint.x * 512) / 3600.;
				latTextField.setText(String.format("%.4f", lat));
				lonTextField.setText(String.format("%.4f", lon));
		}


		btnFindLatLon = new JButton("Find Lat Lon...");
		btnFindLatLon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FindDialog d = new FindDialog(simplifiedGUI);
				d.setVisible(true);
				if (d.latlon != null) {
					latTextField.setText(String.format("%.4f", d.latlon[0]));
					lonTextField.setText(String.format("%.4f", d.latlon[1]));
				}
			}
		});

		btnGenerateMap = new JButton("Generate Map");
		btnGenerateMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!generating) {
					startGeneration();
				} else {
					cancelGeneration();
				}
			}
		});
		updateGenerateButton();

		panel = new MapPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));

		btnSettings = new JButton("Settings...");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsDialog d = new SettingsDialog(simplifiedGUI);
				d.setVisible(true);
			}
		});

		btnDeleteMap = new JButton("Delete Map");
		btnDeleteMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simplifiedGUI.deleteGame(GeneratingPanel.this.gameFolder);
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblMapname)
								.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createSequentialGroup()
														.addGap(25)
														.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
																.addComponent(btnDeleteMap, GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
																.addComponent(btnFindLatLon, GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
																.addComponent(btnGenerateMap, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))
																.addGap(18))
																.addGroup(groupLayout.createSequentialGroup()
																		.addGap(49)
																		.addComponent(btnSettings)
																		.addGap(45)))
																		.addGap(18)
																		.addComponent(panel, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
																		.addGap(136))
																		.addGroup(groupLayout.createSequentialGroup()
																				.addComponent(lblLatitude)
																				.addPreferredGap(ComponentPlacement.UNRELATED)
																				.addComponent(latTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																				.addGap(12)
																				.addComponent(lblLongitude)
																				.addPreferredGap(ComponentPlacement.UNRELATED)
																				.addComponent(lonTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																				.addComponent(lblPlayerPosition))
																				.addGap(28))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addContainerGap()
										.addComponent(lblMapname)
										.addGap(18)
										.addComponent(lblPlayerPosition)
										.addGap(18)
										.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblLatitude)
												.addComponent(latTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(lonTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblLongitude))
												.addGap(18)
												.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
														.addGroup(groupLayout.createSequentialGroup()
																.addComponent(btnDeleteMap)
																.addGap(12)
																.addComponent(btnSettings)
																.addGap(18)
																.addComponent(btnGenerateMap, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
																.addComponent(panel, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)))
																.addGroup(groupLayout.createSequentialGroup()
																		.addGap(120)
																		.addComponent(btnFindLatLon)))
																		.addContainerGap(153, Short.MAX_VALUE))
				);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 226, Short.MAX_VALUE)
				);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 145, Short.MAX_VALUE)
				);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);
	}
	private void updateGenerateButton() {
		String a = latTextField.getText(), b = lonTextField.getText();
		if (btnGenerateMap != null)
			btnGenerateMap.setEnabled(!a.isEmpty() && !b.isEmpty() && !"-".equals(a) && !"-".equals(b));
	}
	private void alert(String s) {
		JOptionPane.showMessageDialog(this, s);
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
				updateGenerateButton();
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
				updateGenerateButton();
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
				updateGenerateButton();
			}

		}
	}
}
