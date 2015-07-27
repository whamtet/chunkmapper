package com.chunkmapper.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.chunkmapper.gui.layer.GameAvailableInterface;
import com.chunkmapper.gui.layer.MainLayer;

import java.awt.Dialog.ModalityType;

import javax.swing.JCheckBox;
import javax.swing.border.LineBorder;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import com.chunkmapper.gui.dialog.NewMapDialog.Difficulty;
import com.chunkmapper.gui.dialog.NewMapDialog.GameMode;

public class NewMapDialog extends JDialog {
	
	public static enum Difficulty {Peaceful, Easy, Normal, Hard}
	public static enum GameMode {Survival_Mode, Creative_Mode, Hardcore_Mode;
	
		public String toString() {
			return super.toString().replace("_", " ");
		};
	}
	
	public static class NewGameInfo {
		public final String gameName;
		public final boolean hasCheats, isGaia;
		public final Difficulty difficulty;
		public final GameMode gameMode;
		
		public NewGameInfo(String gameName, boolean hasCheats, boolean isGaia, Difficulty difficulty, GameMode gameMode) {
			this.gameName = gameName;
			this.hasCheats = hasCheats;
			this.isGaia = isGaia;
			this.difficulty = difficulty;
			this.gameMode = gameMode;
		}
		public NewGameInfo(String gameName) {
			this.gameName = gameName;
			hasCheats = false;
			isGaia = false;
			difficulty = Difficulty.Peaceful;
			gameMode = GameMode.Creative_Mode;
		}
	}

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JLabel lblNewLabel;
	private JButton okButton;
	private JButton cancelButton;
	private String gameName;
	private JCheckBox chckbxGaiaMode, chckbxMinecraftCheats;
	private JLabel lblGameDifficulty;
	private JLabel lblGameMode;
	private JComboBox comboBox_1;
	private JComboBox comboBox;
	
//	public String getGameInfo() {
//		return gameName;
//	}
	
	public NewGameInfo getGameInfo() {
		Difficulty difficulty = (Difficulty) comboBox.getSelectedObjects()[0];
		GameMode gameMode = (GameMode) comboBox_1.getSelectedObjects()[0];
		return new NewGameInfo(gameName, chckbxMinecraftCheats.isSelected(),
				chckbxGaiaMode.isSelected(),
				difficulty, gameMode);
	}

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	private void checkInputValid(GameAvailableInterface mainLayer) {
		String text = this.textField.getText();
		if (text.equals("")) {
			this.lblNewLabel.setVisible(false);
			this.okButton.setEnabled(false);
		} else if (!mainLayer.gameAvailable(text)) {
			this.okButton.setEnabled(false);
			this.lblNewLabel.setVisible(true);
		} else {
			this.okButton.setEnabled(true);
			this.lblNewLabel.setVisible(false);
		}
	}
	public static void main(String[] args) throws Exception {
		try {
			GameAvailableInterface i = new GameAvailableInterface() {

				@Override
				public boolean gameAvailable(String game) {
					// TODO Auto-generated method stub
					return true;
				}
				
			};
			NewMapDialog dialog = new NewMapDialog(i, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public NewMapDialog(final GameAvailableInterface mainLayer, final JFrame frame) {
		
		super(frame);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Create New Map");
		
		if (frame != null) {
			Rectangle parentBounds = frame.getBounds();
			setBounds(parentBounds.x + 100, parentBounds.y + 100, 520, 240);
		} else {
			setBounds(100, 100, 520, 240);
		}
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			textField = new JTextField();
			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					checkInputValid(mainLayer);
					
				}
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
						dispose();
					}
				}
			});
			textField.setColumns(10);
		}
		
		JLabel lblGameName = new JLabel("Map Name");
		
		chckbxGaiaMode = new JCheckBox("Gaia Mode");
		
		JButton button = new JButton("?");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(NewMapDialog.this, "Map excludes all man made features");
			}
		});
		
		chckbxMinecraftCheats = new JCheckBox("Minecraft Cheats");
		
		JLabel label = new JLabel("");
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(Difficulty.values()));
		
		lblGameDifficulty = new JLabel("Game Difficulty");
		
		lblGameMode = new JLabel("Game Mode");
		
		comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(GameMode.values()));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(chckbxGaiaMode)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(button, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
								.addComponent(chckbxMinecraftCheats))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(label)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(59)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblGameMode)
										.addComponent(lblGameDifficulty))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))))
							.addContainerGap())
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
							.addGap(17))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblGameName)
							.addContainerGap(410, Short.MAX_VALUE))))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblGameName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxGaiaMode)
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGameDifficulty)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxMinecraftCheats)
						.addComponent(lblGameMode)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label)
					.addContainerGap(362, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						NewMapDialog.this.gameName = textField.getText();
						dispose();
					}
				});
				{
					lblNewLabel = new JLabel("Map Name Unavailable");
					lblNewLabel.setVisible(false);
				}
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
			}
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(Alignment.TRAILING, gl_buttonPane.createSequentialGroup()
						.addContainerGap(59, Short.MAX_VALUE)
						.addComponent(lblNewLabel)
						.addGap(109)
						.addComponent(okButton)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(cancelButton)
						.addContainerGap())
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel)
							.addComponent(okButton)
							.addComponent(cancelButton)))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
	}
}
