package com.chunkmapper.gui.simple;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultListModel;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.PreferenceManager;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.layer.GameAvailableInterface;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

public class SimplifiedGUI extends JFrame implements GameAvailableInterface {

	private JPanel contentPane;
	private JList<String> list;
	private DefaultListModel<String> defaultListModel = getListModel();
	private JButton btnNewChunkmap;
	private JPanel currentPanel, panel;
	private static final File saveFolder = new File(Utila.MINECRAFT_DIR, "saves");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		BucketInfo.initMap();
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
	private static DefaultListModel<String> getListModel() {
		DefaultListModel<String> out = new DefaultListModel<String>();
		File[] files = saveFolder.listFiles();
		if (files != null) {
			for (File f : files) {
				if ((new File(f, "chunkmapper")).exists()) {
					out.addElement(f.getName());
				}
			}
		}
		return out;
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
		
		list = new JList<String>(defaultListModel);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (currentPanel != null) {
						panel.remove(currentPanel);
					}
					File f = new File(saveFolder, list.getSelectedValue());
					GeneratingPanel p = new GeneratingPanel(f, SimplifiedGUI.this);
					panel.add(p, BorderLayout.CENTER);
					currentPanel = p;
					panel.validate();
				}
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JLabel lblChunkmaps = new JLabel("Chunkmaps");
		
		btnNewChunkmap = new JButton("New Chunkmap");
		
		panel = new JPanel();
		
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(list, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(panel, GroupLayout.PREFERRED_SIZE, 435, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblChunkmaps)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(34)
							.addComponent(btnNewChunkmap)))
					.addContainerGap(28, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(15)
					.addComponent(lblChunkmaps)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(list, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnNewChunkmap)))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		panel.setLayout(new BorderLayout(0, 0));
		
		contentPane.setLayout(gl_contentPane);
	}
	@Override
	public boolean gameAvailable(String game) {
		return !defaultListModel.contains(game);
	}
	public void setActive(boolean b) {
		list.setEnabled(b);
		btnNewChunkmap.setEnabled(b);
	}
}
