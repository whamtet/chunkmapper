package com.chunkmapper.gui.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.ManagingThread;
import com.chunkmapper.Point;
import com.chunkmapper.ProgressManager;
import com.chunkmapper.writer.LoadedLevelDat;

public class GameInfoPanel extends JPanel {
	
//	private int numChunksGenerated = 0;
	private JLabel lblMapChunksGenerated;
	private final LoadedLevelDat loadedLevelDat;
	private final GameMetaInfo gameMetaInfo;
	private ManagingThread managingThread;
	private JButton btnGenerateMap, btnCancel;
	private boolean generateMapEnabled = true, cancelEnabled = false;
	private JLabel lblGameName, lblPlayerLocation;
	private JProgressBar progressBar;
	public ProgressManager progressManager;
	private double lat, lon;
	
	
	public void shutDown() {
		if (managingThread != null)
			ManagingThread.blockingShutDown(managingThread);
	}
	
	private static String getNumChunksGeneratedString(int i) {
		return "Map Chunks Generated: " + i;
	}
	public void setNumChunksGenerated(int i) {
		this.lblMapChunksGenerated.setText(getNumChunksGeneratedString(i));
	}
	private String getPlayerLocationString() {
		Point relativePlayerPosition = loadedLevelDat.getPlayerPosition();
		double lon = (gameMetaInfo.rootPoint.x * 512 + relativePlayerPosition.x) / 3600.;
		double lat = -(gameMetaInfo.rootPoint.z * 512 + relativePlayerPosition.z) / 3600.;
		String latHemi, lonHemi;
		if (lat >= 0) {
			latHemi = "N";
		} else {
			latHemi = "S";
			lat = -lat;
		}
		if (lon >= 0) {
			lonHemi = "E";
		} else {
			lonHemi = "W";
			lon = -lon;
		}
		return String.format("Player location %.2f¼%s %.2f¼%s", lat, latHemi, lon, lonHemi);
	}

	/**
	 * Create the panel.
	 * @param mapMakingDialog 
	 * @throws IOException 
	 */
	
	public GameInfoPanel(File gameFolder, final double lat, final double lon, final String gameName, final MapMakingDialog mapMakingDialog) throws IOException {
		this.lat = lat; this.lon = lon;
		setPreferredSize(new Dimension(369, 174));
		loadedLevelDat = new LoadedLevelDat(new File(gameFolder, "level.dat"));
		File chunkmapperDir = new File(gameFolder, "chunkmapper");
		gameMetaInfo = new GameMetaInfo(new File(chunkmapperDir, GameMetaInfo.STORE_NAME));
		
		lblGameName = new JLabel(loadedLevelDat.getGameName());
		
		lblPlayerLocation = new JLabel(this.getPlayerLocationString());
		
		lblMapChunksGenerated = new JLabel(getNumChunksGeneratedString(gameMetaInfo.getNumChunksMade()));
		progressBar = new JProgressBar();
		
		btnGenerateMap = new JButton("Generate Map");
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnCancel.setText("Cancelling...");
				btnCancel.setEnabled(false);
				cancelEnabled = false;
				shutDown();
				mapMakingDialog.unlockDialog();
				//then back to defaults
				btnCancel.setText("Cancel");
				btnGenerateMap.setText("Generate Map");
				btnGenerateMap.setEnabled(true);
				generateMapEnabled = true;
			}
		});
		
		//ManagingThread(double lat, double lon, String gameName, boolean forceRestart, boolean reteleport) {
		btnGenerateMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mapMakingDialog.lockDialog();
				btnGenerateMap.setText("Generating...");
				generateMapEnabled = false;
				cancelEnabled = true;
				btnGenerateMap.setEnabled(false);
				btnCancel.setEnabled(true);
				
				boolean forceRestart = false, reteleport = false;
//				progressManager = new ProgressManager(progressBar, gameMetaInfo.getNumChunksMade());
				//now for temporary sake
				double[] latlon = geocode.core.placeToCoords("nelson, nz");
				progressManager = new ProgressManager(progressBar, 0);
				managingThread = new ManagingThread(latlon[0], latlon[1], "world", true, false, GameInfoPanel.this);
				//end
//				managingThread = new ManagingThread(lat, lon, gameName, forceRestart, reteleport, progressManager);
				managingThread.start();
			}
		});
		
		btnCancel.setEnabled(false);
		btnGenerateMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JButton btnCheckLocation = new JButton("Check Location");
		btnCheckLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					URI uri = new URI(String.format("https://maps.google.com.au/?q=loc:%s+%s", lat, lon));
					java.awt.Desktop.getDesktop().browse(uri);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(187)
							.addComponent(lblGameName))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPlayerLocation))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblMapChunksGenerated))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(6)
									.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnGenerateMap)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnCancel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnCheckLocation)))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(lblGameName)
					.addGap(18)
					.addComponent(lblPlayerLocation)
					.addGap(18)
					.addComponent(lblMapChunksGenerated)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnGenerateMap)
						.addComponent(btnCancel)
						.addComponent(btnCheckLocation))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
	public void deactivate() {
		btnGenerateMap.setEnabled(false);
		btnCancel.setEnabled(false);
		lblGameName.setEnabled(false);
		lblPlayerLocation.setEnabled(false);
		progressBar.setEnabled(false);
		this.lblMapChunksGenerated.setEnabled(false);
		
	}
	public void activate() {
		btnGenerateMap.setEnabled(generateMapEnabled);
		btnCancel.setEnabled(cancelEnabled);
		lblGameName.setEnabled(true);
		lblPlayerLocation.setEnabled(true);
		progressBar.setEnabled(true);
		lblMapChunksGenerated.setEnabled(true);
	}
}
