package com.chunkmapper.gui.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.MyLogger;

public class TestFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MyLogger.init();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Thread t = new Thread(new Runnable() {
			public void run() {
				BucketInfo.initMap();
			}
		});
		t.start();
	}

	/**
	 * Create the frame.
	 */
	public TestFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new GeneratingPanel(new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/Hong Kong"), null);
		JPanel contentPane2 = new JPanel();
		setContentPane(contentPane2);
		contentPane2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		contentPane2.add(contentPane);
		
	}

}
