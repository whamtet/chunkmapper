package com.chunkmapper.layer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.ScreenAnnotation;

public class StartControlsLayer extends RenderableLayer {
	public ScreenAnnotation startButton;
	public ScreenAnnotation cancelButton;
	private boolean initialized;
	protected int borderWidth = 30;
	
	private static final String START_IM = "images/CHOOSE_START_POINT.png", CANCEL_IM = "images/CANCEL.png";
	public static final int BUTTON_HEIGHT = 40; 
	public static final int START_WIDTH = 310;
	public static final int CANCEL_WIDTH = 126;
	public static final int BROWSE_PHASE = 0, SELECT_PHASE = 1;
	private int currentPhase = 0;
	
	protected Rectangle referenceViewport;
	
	public int getCurrentPhase() {
		return currentPhase;
	}
	
	public void updateRenderables(int phase) {
		currentPhase = phase;
		super.removeAllRenderables();
		if (phase == BROWSE_PHASE) {
			super.addRenderable(this.startButton);
		}
		if (phase == SELECT_PHASE) {
			super.addRenderable(this.cancelButton);
		}
	}
	
	protected void initialize(DrawContext dc)
	{
		if (this.initialized)
			return;

		// Setup user interface - common default attributes
		AnnotationAttributes ca = new AnnotationAttributes();
		ca.setAdjustWidthToText(AVKey.SIZE_FIXED);
		ca.setInsets(new Insets(0, 0, 0, 0));
		ca.setBorderWidth(0);
		ca.setCornerRadius(0);
		ca.setSize(new Dimension(32, 32));
		ca.setBackgroundColor(new Color(0, 0, 0, 0));
		ca.setImageOpacity(1);
		
		ca.setScale(1);

		final String NOTEXT = "";
		final Point ORIGIN = new Point(0, 0);
		
		this.startButton = new ScreenAnnotation(NOTEXT, ORIGIN, ca);
		startButton.getAttributes().setImageSource(START_IM);
		startButton.getAttributes().setSize(new Dimension(START_WIDTH, BUTTON_HEIGHT));
		
		this.cancelButton = new ScreenAnnotation(NOTEXT, ORIGIN, ca);
		cancelButton.getAttributes().setImageSource(CANCEL_IM);
		cancelButton.getAttributes().setSize(new Dimension(CANCEL_WIDTH, BUTTON_HEIGHT));
		
		updateRenderables(BROWSE_PHASE);
		updatePositions(dc);

		this.initialized = true;
	}
	
	public void doRender(DrawContext dc)
	{
		if (!this.initialized)
			initialize(dc);

		if (!this.referenceViewport.equals(dc.getView().getViewport()))
			updatePositions(dc);

		super.doRender(dc);
	}
	
	protected void updatePositions(DrawContext dc)
	{
		referenceViewport = dc.getView().getViewport();
		Point p = new Point(referenceViewport.width / 2, this.borderWidth);
		startButton.setScreenPoint(p);
		cancelButton.setScreenPoint(p);
	}

}
