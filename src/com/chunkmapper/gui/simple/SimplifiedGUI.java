package com.chunkmapper.gui.simple;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.admin.PreferenceManager;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.gui.dialog.NewMapDialog;
import com.chunkmapper.gui.layer.GameAvailableInterface;
import com.chunkmapper.security.MySecurityManager;

public class SimplifiedGUI extends JFrame implements GameAvailableInterface {

	private JPanel contentPane;
	private DefaultListModel<String> defaultListModel = getListModel();
	private JButton btnNewChunkmap;
	private JPanel currentPanel, panel;
	private static final File saveFolder = new File(Utila.MINECRAFT_DIR, "saves");
	private JScrollPane scrollPane;
	private JList<String> list;

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
	public int numGames() {
		return defaultListModel.size();
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
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 736, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblChunkmaps = new JLabel("Chunkmaps");

		btnNewChunkmap = new JButton("New Chunkmap");
		btnNewChunkmap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (defaultListModel.size() >= MySecurityManager.ALLOWED_GAMES && MySecurityManager.mustPurchase(SimplifiedGUI.this)) {
					return;
				}
				NewMapDialog d = new NewMapDialog(SimplifiedGUI.this, SimplifiedGUI.this);
				d.setVisible(true);
				if (d.getGameName() != null) {
					defaultListModel.addElement(d.getGameName());
				}
			}
		});

		panel = new JPanel();

		panel.setBorder(null);

		scrollPane = new JScrollPane();

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addContainerGap()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_contentPane.createSequentialGroup()
														.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
														.addGap(9)
														.addComponent(panel, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE))
														.addComponent(lblChunkmaps)))
														.addGroup(gl_contentPane.createSequentialGroup()
																.addGap(34)
																.addComponent(btnNewChunkmap)))
																.addContainerGap())
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
										.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
										.addGap(12)
										.addComponent(btnNewChunkmap)))
										.addContainerGap(13, Short.MAX_VALUE))
				);

		list = new JList<String>(defaultListModel);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
					if (currentPanel != null) {
						panel.remove(currentPanel);
					}
					File f = new File(saveFolder, list.getSelectedValue());
					GeneratingPanel p = new GeneratingPanel(f, SimplifiedGUI.this);
					currentPanel = p;
					panel.add(currentPanel);
					SimplifiedGUI.this.validate();
				}
			}
		});
		scrollPane.setViewportView(list);
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
	public void deleteGame(File gameFolder) {
		int response = JOptionPane.showConfirmDialog(this, "Delete map " + gameFolder.getName() + "?", "", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			panel.remove(currentPanel);
			defaultListModel.removeElement(gameFolder.getName());
			validate();
			try {
				FileUtils.deleteDirectory(gameFolder);
			} catch (IOException e) {
				MyLogger.LOGGER.warning(MyLogger.printException(e));
			}
		}
	}
	public static void open() {
		final SimplifiedGUI frame = new SimplifiedGUI();
		frame.setVisible(true);
		java.awt.EventQueue.invokeLater(new Runnable() {
		    @Override
		    public void run() {
		        frame.toFront();
		        frame.repaint();
		    }
		});
	}
}
