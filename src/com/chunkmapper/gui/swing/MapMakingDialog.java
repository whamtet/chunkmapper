package com.chunkmapper.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.writer.LoadedLevelDat;

public class MapMakingDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTree tree;
	//needed if we wish to use icons
	private final HashMap<String, Boolean> hasBeenCreatedMap = new HashMap<String, Boolean>();
	public final HashSet<String> createdGames = new HashSet<String>();
	private final HashSet<String> partiallyCreatedGames = new HashSet<String>();
	public final File savesDir;
	private final JButton btnDeleteMap, btnNewMap, closeButton;
	private JPanel panel;
	private final double lat, lon;
	private boolean canBeDeleted;
	private boolean ignoreTreeChange = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MapMakingDialog dialog = new MapMakingDialog(0, 0, new File("/Users/matthewmolloy/Library/Application Support/minecraft"), null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private DefaultMutableTreeNode getRoot() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Chunkmapper Maps");
		for (File gameFolder : savesDir.listFiles()) {
			File metainfo = new File(gameFolder, "chunkmapper/meta.txt");
			File loadedLevelDatFile = new File(gameFolder, "level.dat");
			if (metainfo.exists() && loadedLevelDatFile.exists()) {
				String gameName = (new LoadedLevelDat(loadedLevelDatFile)).getGameName();
				this.hasBeenCreatedMap.put(gameName, true);
				this.createdGames.add(gameName);
				DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(gameName);
				root.add(treeNode);
			}
		}
		//		for (File gameFolder : this.createdGameFolders) {
		//			root.add(new DefaultMutableTreeNode(gameFolder.getName()));
		//		}
		return root;
	}
	/**
	 * Create the dialog.
	 * @param appFrame 
	 */
	public MapMakingDialog(double lat, double lon, File minecraftDir, JFrame appFrame) {
		super(appFrame);
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setModal(true);

		this.lat = lat; this.lon = lon;
		setPreferredSize(new Dimension(1000, 500));
		this.addWindowListener(new CloseMapMakingDialogTask(this));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		savesDir = new File(minecraftDir, "saves");
		setTitle("Chunkmapper");
		setBounds(350, 250, 550, 300);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);

		btnNewMap = new JButton("New Map");
		btnNewMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewMapDialog dialog = new NewMapDialog(MapMakingDialog.this);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		{
			tree = new JTree();
			tree.setModel(new DefaultTreeModel(getRoot()));
			tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			//			tree.setCellRenderer(new MyTreeCellRenderer(tree.getCellRenderer()));
			tree.addTreeSelectionListener(new MyTreeSelectionListener());
			tree.setToggleClickCount(0);

		}

		panel = new JPanel();

		btnDeleteMap = new JButton("Delete Map");
		btnDeleteMap.setEnabled(false);
		btnDeleteMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TreePath path = tree.getSelectionPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				String gameName = (String) node.getUserObject();
				ConfirmDeleteDialog dialog = new ConfirmDeleteDialog(MapMakingDialog.this, gameName);
				dialog.setVisible(true);
			}
		});
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		contentPanel.setLayout(gl_contentPanel);
		gl_contentPanel.setAutoCreateContainerGaps(true);
		gl_contentPanel.setAutoCreateGaps(true);

		//		gl_contentPanel.
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createSequentialGroup().addGroup(
						gl_contentPanel.createParallelGroup()
						.addComponent(tree)
						.addComponent(btnNewMap)
						.addComponent(btnDeleteMap))
						.addComponent(panel));

		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup().addGroup(
						gl_contentPanel.createSequentialGroup()
						.addComponent(tree)
						.addComponent(btnNewMap)
						.addComponent(btnDeleteMap))
						.addComponent(panel));
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				closeButton = new JButton("Change Location");
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				closeButton.setActionCommand("Cancel");
				buttonPane.add(closeButton);
			}
		}


	}

	private class MyTreeSelectionListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (ignoreTreeChange) {
				ignoreTreeChange = false;
				return;
			}
			System.out.println("tree value changed");
			TreePath path = e.getPath();
			canBeDeleted = path.getPathCount() == 2;
			MapMakingDialog.this.btnDeleteMap.setEnabled(canBeDeleted);
			
			if (!canBeDeleted) {
				tree.setSelectionPath(null);
				if (panel.getComponents().length == 1)
					panel.remove(0);
				panel.revalidate();
				panel.repaint();
				return;
			}
			
			Object lastPathComponent = path.getLastPathComponent();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
			String gameName = (String) node.getUserObject();
			makeMockGame(gameName, savesDir);
			if (panel.getComponents().length == 1)
				panel.remove(0);
			try {
				File gameFolder = new File(savesDir, gameName);
				GameInfoPanel gameInfoPanel = new GameInfoPanel(gameFolder, lat, lon, gameName, MapMakingDialog.this);			
				panel.add(gameInfoPanel);
				panel.revalidate();
				panel.repaint();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

		}

	}
	private static void makeMockGame(String gameName, File savesDir) {
		System.out.println("making mock game " + gameName);
		File gameDir = new File(savesDir, gameName);
		if (gameDir.exists())
			return;
		File chunkmapperDir = new File(gameDir, "chunkmapper");
		chunkmapperDir.mkdirs();
		GameMetaInfo info = new GameMetaInfo(new File(chunkmapperDir, "meta.txt"), 0, 0);
		File src = new File("/Users/matthewmolloy/workspace/chunkmapper2/resources/level.dat");
		File dest = new File(gameDir, "level.dat");
		try {
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoadedLevelDat loadedLevelDat = new LoadedLevelDat(dest);
		loadedLevelDat.setName(gameName);
		try {
			loadedLevelDat.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private class MyTreeCellRenderer implements TreeCellRenderer {
		private final TreeCellRenderer oldRenderer;

		public MyTreeCellRenderer(TreeCellRenderer cellRenderer) {
			oldRenderer = cellRenderer;
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

			String nodeValue = (String) ((DefaultMutableTreeNode) value).getUserObject();
			Boolean hasBeenCreated = hasBeenCreatedMap.get(nodeValue);
			if (hasBeenCreated == null || !hasBeenCreated)
				return oldRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

			JLabel label = new JLabel();
			int size = 16;
			String s = String.format("src/images/chunkmapper-icon-%sx%s.png", size, size);
			ImageIcon icon = new ImageIcon(s);
			label.setIcon(icon);
			return label;
		}

	}
	public void addMap(String text) {
		this.createdGames.add(text);
		this.partiallyCreatedGames.add(text);

		DefaultTreeModel treeModel = (DefaultTreeModel) this.tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
		root.add(new DefaultMutableTreeNode(text));
		treeModel.setRoot(root);

	}
	public void delete(String gameToDelete) {
		this.createdGames.remove(gameToDelete);
		try {
			System.out.println(new File(savesDir, gameToDelete));
			FileUtils.deleteDirectory(new File(savesDir, gameToDelete));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnDeleteMap.setEnabled(false);
		ignoreTreeChange = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) this.tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
			String gameName = (String) child.getUserObject();
			if (gameName.equals(gameToDelete)) {
				root.remove(i);
				break;
			}
		}
		treeModel.setRoot(root);

	}
	private void deletePartiallyCreatedGames() {
		for (String s : this.partiallyCreatedGames) {
			File f = new File(this.savesDir, s);
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void shutDown() {
		System.out.println("shutting down MapMakingDialog");
		deletePartiallyCreatedGames();
		if (panel.getComponents().length == 1) {
			((GameInfoPanel) panel.getComponent(0)).shutDown();
		}
	}
	public void deactivate() {
		tree.setEnabled(false);
		closeButton.setEnabled(false);
		this.btnDeleteMap.setEnabled(false);
		this.btnNewMap.setEnabled(false);
		if (panel.getComponents().length == 1)
			((GameInfoPanel) panel.getComponent(0)).deactivate();
	}
	public void activate() {
		tree.setEnabled(true);
		closeButton.setEnabled(true);
		btnDeleteMap.setEnabled(this.canBeDeleted);
		btnNewMap.setEnabled(true);
		if (panel.getComponents().length == 1)
			((GameInfoPanel) panel.getComponent(0)).activate();

	}
	public void lockDialog() {
		tree.setEnabled(false);
		closeButton.setEnabled(false);
		this.btnDeleteMap.setEnabled(false);
		this.btnNewMap.setEnabled(false);
	}
	public void unlockDialog() {
		tree.setEnabled(true);
		closeButton.setEnabled(true);
		this.btnDeleteMap.setEnabled(this.canBeDeleted);
		this.btnNewMap.setEnabled(true);
	}
}
