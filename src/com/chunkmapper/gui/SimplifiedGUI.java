package com.chunkmapper.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.chunkmapper.admin.PreferenceManager;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.layer.GameAvailableInterface;

public class SimplifiedGUI extends JFrame implements GameAvailableInterface {

	private JPanel contentPane;
	private ArrayList<String> gameNames;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimplifiedGUI frame = new SimplifiedGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private String[] initGamesList() {
		File saveFolder = new File(Utila.MINECRAFT_DIR, "saves");
		File[] games = saveFolder.listFiles();
		String[] gameNamesArr;
		gameNames = new ArrayList<String>();
		if (games == null) {
			gameNamesArr = new String[0];
		} else {
			for (File f : games) {
				if ((new File(f, "chunkmapper")).exists()) {
					gameNames.add(f.getName());
				}
			}
			gameNamesArr = new String[gameNames.size()];
			for (int i = 0; i < gameNames.size(); i++) {
				gameNamesArr[i] = gameNames.get(i);
			}
		}
		return gameNamesArr;
	}

	/**
	 * Create the frame.
	 */
	public SimplifiedGUI() {
		setTitle("Chunkmapper - Simple Interface");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				PreferenceManager.setNoPurchaseShown();
			}
//			public void windowClosed(WindowEvent e) {
//				System.exit(0);
//			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		JList list = new JList(initGamesList());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JLabel lblChunkmaps = new JLabel("Chunkmaps");
		
		JButton btnNewChunkmap = new JButton("New Chunkmap");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(list, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblChunkmaps)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(34)
							.addComponent(btnNewChunkmap)))
					.addContainerGap(481, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(15)
					.addComponent(lblChunkmaps)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(list, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewChunkmap)
					.addContainerGap(13, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	@Override
	public boolean gameAvailable(String game) {
		return !gameNames.contains(game);
	}
}
