package br.unicentro.jah.eyes;

import java.awt.Color;

import org.opencv.core.Rect;

import br.unicentro.jah.client.TrackWindow;

public class TrackablePlayer extends Trackable
{
	public TrackablePlayer(Rect startingPoint) 
	{
		super("Disk", startingPoint, Color.GREEN);
		setHSVBounds(TrackWindow.HSV_LOWER_BLUE, TrackWindow.HSV_UPPER_BLUE);
	}
}
