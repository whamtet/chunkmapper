package com.chunkmapper.gui.swing;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;

public class CloseMapMakingDialogTask implements WindowListener {
	private final MapMakingDialog dialog;
	private boolean virgin = true;
	public CloseMapMakingDialogTask(MapMakingDialog d) {
		dialog = d;
	}

	@Override
	public void windowActivated(WindowEvent e) {
		dialog.activate();

	}

	@Override
	public void windowClosed(WindowEvent e) {
		if (virgin) {
			dialog.shutDown();
			virgin = false;
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		dialog.deactivate();

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
